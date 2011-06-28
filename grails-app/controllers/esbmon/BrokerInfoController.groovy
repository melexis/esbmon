package esbmon

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.round

class BrokerInfoController {

    BrokerInfoService brokerInfoService


    def index = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def brokers = Broker.list(params)
        def brokerInfo = [:]
        def trendInfoHeap = [:]
        def trendInfoNonHeap = [:]
        def trendInfoLoad = [:]
        def trendInfoCpu = [:]
        def cpuAverage = [:]

        def sampleTime = SampleTime.findByComplete(true, [sort: 'sampleTime', order: 'desc'])
        brokers.each {
            brokerInfo[it] = OperatingInfo.findByBrokerAndSampleTime(it, sampleTime)
            getTrendInformation(it, trendInfoHeap, trendInfoNonHeap, trendInfoLoad, trendInfoCpu, cpuAverage)
        }


        [brokerInstanceList: brokers,
                brokerInstanceTotal: Broker.count(),
                brokerInfo: brokerInfo,
                sampleTime: sampleTime,
                trendInfoHeap: trendInfoHeap,
                trendInfoNonHeap: trendInfoNonHeap,
                trendInfoLoad: trendInfoLoad,
                trendInfoCpu: trendInfoCpu,
                cpuAverage: cpuAverage]
    }

    private def getTrendInformation(Broker it, LinkedHashMap trendInfoHeap, LinkedHashMap trendInfoNonHeap, LinkedHashMap trendInfoLoad, LinkedHashMap trendInfoCpu, LinkedHashMap cpuAverage) {
        def opInfo = OperatingInfo.findAllByBroker(it, [sort: 'sampleTime', order: 'desc', max: 25])

        def l1 = []
        def l2 = []
        def l3 = []
        def l4 = []
        def lastCpu = -1
        def firstCpu = -1
        def cpu = 0
        opInfo.reverseEach {
            l1 << it.usedHeap
            l2 << it.usedNonHeap
            l3 << it.loadAverage
            cpu = it.processCpuTime
            if (lastCpu < 1) { lastCpu = cpu; firstCpu = cpu}
            l4 << (cpu - lastCpu)
            lastCpu = cpu
        }
        trendInfoHeap[it] = l1
        trendInfoNonHeap[it] = l2
        trendInfoLoad[it] = l3
        trendInfoCpu[it] = l4
        def cpuDiff = lastCpu - firstCpu
        def cpuAvg = (l4.size > 1) ? cpuDiff / (l4.size() - 1) : cpuDiff
        cpuAverage[it] = round(cpuAvg / 1000000, 3)
    }

}
