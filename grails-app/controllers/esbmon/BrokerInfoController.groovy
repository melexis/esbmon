package esbmon

class BrokerInfoController {

  BrokerInfoService brokerInfoService

  def makeInfo(info) {
    return [info['used'], info['max'] - info['used']]
  }

  def index = {
    params.max = Math.min(params.max ? params.int('max') : 10, 100)
    def brokers = Broker.list(params)
    def heapInfo = [:]
    def nonHeapInfo = [:]
    brokers.each {
      def info =brokerInfoService.getBrokerInfo(it)
      heapInfo[it] = makeInfo(info['heapMemoryUsage'])
      nonHeapInfo[it] = makeInfo(info['nonHeapMemoryUsage'])
    }

    [brokerInstanceList: brokers,
     brokerInstanceTotal: Broker.count(),
     heapInfo: heapInfo,
     nonHeapInfo: nonHeapInfo]
  }

}
