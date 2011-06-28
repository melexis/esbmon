package esbmon

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.joda.time.DateTime
import static groovyx.gpars.GParsPool.withPool
import static groovyx.gpars.GParsPoolUtil.callAsync
import static java.lang.StrictMath.max

class UpdateStatisticsJob {

    private static Log log = LogFactory.getLog(this)

    static triggers = {
        simple name: 'StatisticsTrigger', startDelay: 1000, repeatInterval: 10000
    }

    BrokerInfoService brokerInfoService
    NetworkInfoService networkInfoService

    static long timeLeft(long endTime) {
        max(10L, endTime - System.currentTimeMillis())
    }

    def execute() {

        log.info("Polling statistics for ActiveMQ cluster")


        def endTime = System.currentTimeMillis() + 8000

        SampleTime sampleTime = new SampleTime(sampleTime: new DateTime()).save()

        Closure updateWorker = { broker ->
            brokerInfoService.getOperatingInfo(broker, sampleTime)
            networkInfoService.getNetworkInfo(broker, sampleTime)
        }


        List<Future> futures = []
        withPool(8, {
            Broker.findAll().each { broker ->
                log.info("Starting updating broker info : ${broker}")
                futures << callAsync(updateWorker, broker)
                log.info("Worker thread started for : ${broker}")
            }

            futures.each { f ->
                f.get(timeLeft(endTime), TimeUnit.MILLISECONDS)
                if (!f.done) {
                    log.info("Killing long running thread.")
                    f.cancel(true)
                }
            }
        })

        sampleTime.complete = true;
        sampleTime.save();

        log.info("Polling statistics job done.")

    }
}
