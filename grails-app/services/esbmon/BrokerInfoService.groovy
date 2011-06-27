package esbmon

import javax.management.MBeanServerConnection

class BrokerInfoService {

  JmxService jmxService

  static transactional = true

  def getBrokerInfo(Broker broker) {
    try {
      MBeanServerConnection connection = jmxService.getConnector(broker)
      def brokerInfo = [:]
      brokerInfo << jmxService.getMemoryInfo(connection)
      brokerInfo << jmxService.getOperatingSystemInfo(connection)
    } catch (Exception e) {
      log.error("Caught exception in BrokerInfoService.getMemoryStatus(" + broker + ")", e)
      return [heapMemoryUsage:'no data', nonHeapMemoryUsage:'no data']
    }
  }


}
