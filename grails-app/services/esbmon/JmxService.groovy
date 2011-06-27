package esbmon

import javax.management.remote.JMXConnector
import javax.management.remote.JMXConnectorFactory
import java.lang.management.MemoryMXBean
import java.lang.management.ManagementFactory
import javax.management.remote.JMXServiceURL
import javax.management.MBeanServerConnection
import java.lang.management.OperatingSystemMXBean

class JmxService {

  static transactional = true

  MBeanServerConnection getConnector(Broker broker) {
    def context = [
            (JMXConnector.CREDENTIALS): (String[])["smx", "smx"].toArray()
    ]
    def connector = null

    try {
      def jmxUri = new JMXServiceURL(broker.jmxUri)
      JMXConnector jmxc = JMXConnectorFactory.connect(jmxUri, context)
      connector = jmxc.MBeanServerConnection
    } catch (Exception e) {
      log.error("Error creating JMX connection to " + broker, e)
    }
    return connector
  }

  def getMemoryInfo(MBeanServerConnection connector) {

    def memProxy = null
    try {
      memProxy = ManagementFactory.newPlatformMXBeanProxy(connector, ManagementFactory.MEMORY_MXBEAN_NAME,
              MemoryMXBean.class)
      return [
              heapMemoryUsage: [used: memProxy.heapMemoryUsage.used, max: memProxy.heapMemoryUsage.max],
              nonHeapMemoryUsage: [used: memProxy.nonHeapMemoryUsage.used, max: memProxy.nonHeapMemoryUsage.max]
      ]
    } catch (Exception e) {
      log.error("Error creating JMX connection", e)
      return [
              heapMemoryUsage: [used: 'no data', max: 'no data'],
              nonHeapMemoryUsage: [used: 'no data', max: 'no data']
      ]
    }

  }

  def getOperatingSystemInfo(MBeanServerConnection connector) {

    def memProxy = null
    def osInfo=[:]

    try {
      cpuProxy = ManagementFactory.newPlatformMXBeanProxy(connector, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
              OperatingSystemMXBean.class)

      osInfo['name'] = cpuProxy.name
      osInfo['arch'] = cpuProxy.arch
      osInfo['availableProcessors'] = cpuProxy.availableProcessors
      osInfo['loadAverage'] = cpuProxy.systemLoadAverage
      if(cpuProxy instanceof com.sun.management.OperatingSystemMXBean) {
        osInfo['processCpuTime'] = (com.sun.management.OperatingSystemMXBean)cpuProxy.processCpuTime
        osInfo['committedVM'] = (com.sun.management.OperatingSystemMXBean)cpuProxy.committedVirtualMemorySize

        def freeMem = (com.sun.management.OperatingSystemMXBean) cpuProxy.freePhysicalMemorySize
        def totalMem = (com.sun.management.OperatingSystemMXBean)cpuProxy.totalPhysicalMemorySize
        osInfo['memoryUsage'] = [used: totalMem - freeMem, max: totalMem]

        def freeSwap = (com.sun.management.OperatingSystemMXBean) cpuProxy.freeSwapSpaceSize
        def totalSwap = (com.sun.management.OperatingSystemMXBean) cpuProxy.totalSwapSpaceSize
        osInfo['swapUsage'] = [used: totalSwap-freeSwap, max: totalSwap]
      }
    } catch (Exception e) {
      log.error("Error creating JMX connection", e)
    }
    return osInfo

  }

}
