package esbmon

import com.sun.management.OperatingSystemMXBean
import java.lang.management.ManagementFactory
import java.lang.management.MemoryMXBean
import javax.management.MBeanServerConnection
import javax.management.ObjectName
import javax.management.remote.JMXConnector
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXServiceURL
import org.apache.activemq.broker.jmx.NetworkBridgeViewMBean
import org.apache.activemq.broker.jmx.NetworkConnectorViewMBean
import org.apache.activemq.broker.jmx.NetworkConnectorView
import org.apache.activemq.network.NetworkBridge
import org.apache.activemq.broker.jmx.NetworkBridgeView
import javax.management.ObjectInstance
import org.codehaus.groovy.ant.Groovy

class JmxService {

    static transactional = true

    def withConnection(Broker broker, Closure closure) {
        def context = [
                (JMXConnector.CREDENTIALS): (String[]) ["smx", "smx"].toArray()
        ]

        JMXConnector jmxc = null
        MBeanServerConnection connection = null

        try {
            def jmxUri = new JMXServiceURL(broker.jmxUri)
            jmxc = JMXConnectorFactory.connect(jmxUri, context)
            connection = jmxc.MBeanServerConnection
            closure.call(broker, connection)
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("Error creating JMX connection to ${broker.name}", e)
            } else {
                log.info("Error creating JMX connection to ${broker.name} : " + e.message)
            }
        } finally {
            jmxc?.close()
        }
    }

    OperatingInfo getMemoryInfo(MBeanServerConnection connector, OperatingInfo opInfo) {

        try {
            def memProxy = ManagementFactory.newPlatformMXBeanProxy(connector, ManagementFactory.MEMORY_MXBEAN_NAME,
                    MemoryMXBean.class)
            copyMemoryInfo(memProxy, opInfo)
        } catch (Exception e) {
            log.error("Error creating JMX connection", e)
            opInfo = null
        }
        return opInfo

    }

    private def copyMemoryInfo(MemoryMXBean memProxy, OperatingInfo opInfo) {
        opInfo.usedHeap = memProxy.heapMemoryUsage.used
        opInfo.maxHeap = memProxy.heapMemoryUsage.max
        opInfo.usedNonHeap = memProxy.nonHeapMemoryUsage.used
        opInfo.maxNonHeap = memProxy.nonHeapMemoryUsage.max
    }

    OperatingInfo getOperatingSystemInfo(MBeanServerConnection connector, OperatingInfo opInfo) {

        try {
            com.sun.management.OperatingSystemMXBean cpuProxy = ManagementFactory.newPlatformMXBeanProxy(connector, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
                    com.sun.management.OperatingSystemMXBean.class)

            copyStandardOsInfo(cpuProxy, opInfo)
            if (cpuProxy instanceof com.sun.management.OperatingSystemMXBean) {
                copySunExtendedOsInfo(cpuProxy, opInfo)
            }
        } catch (Exception e) {
            log.error("Error creating reading remote operating system parameters", e)
            opInfo = null
        }
        return opInfo

    }

    private def copySunExtendedOsInfo(OperatingSystemMXBean cpuProxy, OperatingInfo opInfo) {
        def sunCpuProxy = (OperatingSystemMXBean) cpuProxy
        opInfo.processCpuTime = sunCpuProxy.processCpuTime
        opInfo.committedVirtualMemory = sunCpuProxy.committedVirtualMemorySize

        def freeMem = sunCpuProxy.freePhysicalMemorySize
        def totalMem = sunCpuProxy.totalPhysicalMemorySize
        opInfo.usedMemory = totalMem - freeMem
        opInfo.maxMemory = totalMem

        def freeSwap = sunCpuProxy.freeSwapSpaceSize
        def totalSwap = sunCpuProxy.totalSwapSpaceSize
        opInfo.usedSwap = totalSwap - freeSwap
        opInfo.maxSwap = totalSwap
    }

    private def copyStandardOsInfo(OperatingSystemMXBean cpuProxy, OperatingInfo opInfo) {
        opInfo.name = cpuProxy.name
        opInfo.osVersion = cpuProxy.version
        opInfo.architecture = cpuProxy.arch
        opInfo.processors = cpuProxy.availableProcessors
        opInfo.loadAverage = cpuProxy.systemLoadAverage
    }


    List<NetworkInfo> getNetworkConnectionInfo(Broker broker, MBeanServerConnection connection, SampleTime sampleTime) {

        def nwInfoList = []

        try {

            def root = new ObjectName("${broker.baseJmxName},Type=NetworkConnector,*")
            def connectors = connection.queryNames(root, null).collect {new GroovyMBean(connection, it)}

            connectors.each {
                NetworkInfo nwInfo = getNetworkConnectorInfo(it, sampleTime)
                nwInfo.broker =  broker

                String name = "${broker.baseJmxName},NetworkConnectorName=${nwInfo.name},Type=NetworkBridge,*"
                def query = new ObjectName(name)
                def bridges = connection.queryNames(query,null).collect {new GroovyMBean(connection, it)}
                bridges.each {
                    def bridge = getNetworkBridgeInfo(it)
                    nwInfo.addToBridges(bridge)
                }
                nwInfo.save(failOnError:true);

                nwInfoList << nwInfo
            }

        } catch (Exception e) {
            log.error("Error creating reading remote operating system parameters", e)
        }

        return nwInfoList

    }

    private def getNetworkConnectorInfo(nwcProxy, SampleTime sampleTime) {

        def nwInfo = new NetworkInfo()

        nwInfo.sampleTime = sampleTime
        nwInfo.name = nwcProxy.Name
        nwInfo.bridgeTempDestination = nwcProxy.BridgeTempDestinations
        nwInfo.conduitSubscriptions = nwcProxy.ConduitSubscriptions
        nwInfo.decreaseNetworkConsumerPriority = nwcProxy.DecreaseNetworkConsumerPriority
        nwInfo.dispatchAsync = nwcProxy.DispatchAsync
        nwInfo.duplex = nwcProxy.Duplex
        nwInfo.dynamicOnly = nwcProxy.DynamicOnly
        nwInfo.networkTtl = nwcProxy.NetworkTTL
        nwInfo.prefetchSize = nwcProxy.PrefetchSize

        nwInfo.save()

        return nwInfo
    }

    BridgeInfo getNetworkBridgeInfo(nwbProxy) {

        BridgeInfo bridgeInfo = new BridgeInfo()

        bridgeInfo.createdByDuplex = nwbProxy.CreatedByDuplex
        bridgeInfo.dequeueCounter = nwbProxy.DequeueCounter
        bridgeInfo.enqueueCounter = nwbProxy.EnqueueCounter
        bridgeInfo.remoteAddress = nwbProxy.RemoteAddress
        bridgeInfo.remoteBrokerName = nwbProxy.RemoteBrokerName

        return bridgeInfo;

    }


}
