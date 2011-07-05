package esbmon

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import javax.management.MBeanServerConnection

class NetworkInfoService {

    static transactional = true

    private static final Log log = LogFactory.getLog(this);

    JmxService jmxService

    List<NetworkInfo> getNetworkInfo(Broker b, SampleTime sampleTime) {

        def nwInfo = []

        jmxService.withConnection(b) { Broker broker, MBeanServerConnection connection ->
            try {
                log.info("retrieving network info for broker ${broker.name} for time ${sampleTime}")

                nwInfo = jmxService.getNetworkConnectionInfo(broker, connection, sampleTime)

                log.info("Network information retrieved for broker ${broker.name} : ${nwInfo}")

            } catch (Exception e) {
                log.error("Caught exception in BrokerInfoService.getNetworkInfo(${broker.name})", e)
            }
        }

        return nwInfo

    }

}
