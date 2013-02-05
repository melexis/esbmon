package esbmon

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.joda.time.DateTime
import static groovyx.gpars.GParsExecutorsPool.withPool
import static groovyx.gpars.GParsPoolUtil.callAsync
import static java.lang.StrictMath.max

class UpdateBrokerInfoJob {

    private static Log log = LogFactory.getLog(this)

    static triggers = {
        simple name: 'BrokerInfoTrigger', startDelay: 1000, repeatInterval: 60000
    }

    BrokerInfoService brokerInfoService
    NetworkInfoService networkInfoService

    static long timeLeft(long endTime) {
        max(10L, endTime - System.currentTimeMillis())
    }

    def execute() {

        log.info("Polling statistics for ActiveMQ cluster")


        def endTime = System.currentTimeMillis() + 55000

        SampleTime sampleTime = new SampleTime(sampleTime: new DateTime()).save()

        Closure updateWorker = { Broker broker ->
            Log wlog = LogFactory.getLog("UpdateBrokerInfoWorker")
            wlog.info("Polling statistics start for ${broker.hostName}")
            println("Polling statistics start for ${broker.hostName}")
            long s = System.currentTimeMillis();
            brokerInfoService.getOperatingInfo(broker, sampleTime)
            wlog.info("Polling statistics done for ${broker.hostName}")
            long s2 = System.currentTimeMillis() - s;
            println("Polling statistics done for ${broker.hostName} : ${s2}ms")
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

        sampleTime.operatingComplete = true;
        sampleTime.save();

        log.info("Polling statistics job done.")

    }
}
