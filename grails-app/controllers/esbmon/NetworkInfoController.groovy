package esbmon

import org.apache.activemq.network.NetworkConnector
import org.apache.commons.logging.LogFactory
import org.apache.commons.logging.Log
import org.joda.time.DateTime

class NetworkInfoController {

    private static Log log = LogFactory.getLog(this)

    NetworkInfoService networkInfoService

    def index = {

        log.info("Starting NetworkInfo Controller")
        println("${new DateTime()} : Starting NetworkInfo Controller")

        List<Broker> brokers = Broker.list(params)
        println("${new DateTime()} : Got brokers")

        def sampleTime = SampleTime.findByNetworkComplete(true, [sort: 'sampleTime', order: 'desc'])
        println("${new DateTime()} : Got last sampletime")


        def bridges = [:]
        Set<Broker> brokerNames = []
        Set<String> connectorNames = []
        def trends = [:]

        brokers.each { broker ->
            brokerNames << broker.name
            log.info("getting bridges for ${broker.name}")
            List <NetworkInfo> connectors = NetworkInfo.findAllByBrokerAndSampleTime(broker, sampleTime)
            println("${new DateTime()} : Got ${connectors.size()} connectors for ${broker.name}")
            connectors.each {connector ->
                log.info("  got bridge ${connector.name} for ${broker.name}")
                connectorNames << connector.name
                def key = "${broker.name}:${connector.name}"
                bridges[key] = connector
                trends[key] = getTrendInfo(connector, broker)
            }
        }

        log.info("Finished NetworkInfo Controller")
        println("${new DateTime()} : Finished NetworkInfo Controller")

        [sampleTime: sampleTime,
                brokerNames: brokerNames.sort(),
                connectorNames: connectorNames.sort(),
                bridges: bridges,
                trends: trends]
    }

    List getTrendInfo(NetworkInfo networkInfo, Broker broker) {
        def samples = NetworkInfo.findAllByNameAndBroker(networkInfo.name,broker,[sort: 'sampleTime', order: 'desc', max:25])
        def trend = []
        def eTrend = []
        def dTrend = []

        println(samples)
        samples.each {
            def e = 0
            def d = 0
            it.bridges.each {
                e += it.enqueueCounter
                d += it.dequeueCounter
            }
            trend << [d,e]
        }
        def last = trend.last();
        trend.reverse().tail().each {
            def smpl = [it[0] - last[0], it[1] - last[1]]
            last = it
            eTrend << it[0]
            dTrend << it[1]
        }
        return [dTrend,eTrend]
    }

}
