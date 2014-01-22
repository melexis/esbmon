package esbmon

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.joda.time.DateTime
import static groovyx.gpars.GParsExecutorsPool.withPool
import static groovyx.gpars.GParsPoolUtil.callAsync
import static java.lang.StrictMath.max

class UpdateNetworkInfoJob {

    private static Log log = LogFactory.getLog(this)

    static triggers = {
        simple name: 'NetworkInfoTrigger', startDelay: 2000, repeatInterval: 60000
    }

    BrokerInfoService brokerInfoService
    NetworkInfoService networkInfoService

    static long timeLeft(long endTime) {
        max(10L, endTime - System.currentTimeMillis())
    }

    def execute() {

        log.info("Polling statistics for ActiveMQ cluster")


        def endTime = System.currentTimeMillis() + 58000

        SampleTime sampleTime = new SampleTime(sampleTime: new DateTime()).save()

        Closure updateWorker = { broker ->
            log.info("   start getting network info for ${broker}")
            networkInfoService.getNetworkInfo(broker, sampleTime)
            log.info("   done getting network info for ${broker}")

        }

        List<Future> futures = []
        withPool(8, {
            Broker.findAll().each { broker ->
                futures << updateWorker.callAsync(broker)
            }

            futures.each { f ->
                f.get(timeLeft(endTime), TimeUnit.MILLISECONDS)
                if (!f.done) {
                    f.cancel(true)
                }
            }
        })

        sampleTime.networkComplete = true;
        sampleTime.save();

        log.info("Polling statistics job done.")
    }
}
