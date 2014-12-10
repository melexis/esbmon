package esbmon

import javax.management.MBeanServerConnection
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

class BrokerInfoService {

  private static final Log log = LogFactory.getLog(this);

  JmxService jmxService

  static transactional = true

  OperatingInfo getOperatingInfo(Broker b, SampleTime sampleTime) {

    def opInfo = new OperatingInfo()

    log.info("getOperatingInfo(${b},${sampleTime}")

    jmxService.withConnection(b) { Broker broker, MBeanServerConnection connection ->
      try {
        log.info("retrieving operating info for broker ${broker.name} for time ${sampleTime}")

        opInfo.broker = broker
        opInfo.sampleTime = sampleTime

        opInfo = jmxService.getMemoryInfo(connection, opInfo)
        opInfo = jmxService.getOperatingSystemInfo(connection, opInfo)

        log.info("Information retrieved for broker ${broker.name} : ${opInfo}")
        opInfo?.save()
        log.info("operating info saved for broker ${broker.name} for time ${sampleTime}")

      } catch (Exception e) {
        log.error("Caught exception in BrokerInfoService.getOperatingInfo(${broker.name})", e)
        return [heapMemoryUsage: 'no data', nonHeapMemoryUsage: 'no data']
      }
    }

    return opInfo

  }


}
