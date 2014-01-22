package esbmon

import grails.test.GrailsUnitTestCase

import javax.management.ObjectName
import java.lang.management.MemoryMXBean
import java.lang.management.MemoryUsage
import javax.management.MBeanServerConnection
import org.joda.time.DateTime

class BrokerInfoServiceTests extends GrailsUnitTestCase {

  def Broker broker

  protected void setUp() {
    super.setUp()
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testOperatingStatus() {

    def jmxControl = mockFor(JmxService)
    def connControl = mockFor(MBeanServerConnection)
    Broker broker = createbroker()
    def sampleTime = new SampleTime(sampleTime: new DateTime("20110629T16:34:27"))

    def memMXBean = createMemoryMXBean()
    def opsMXBean = createOpsMXBean()


    jmxControl.demand.withConnection {Broker b, Closure f ->
      def connection =  connControl.createMock()
      f.call(b,connection)
    }
    jmxControl.demand.getMemoryInfo {c, OperatingInfo o ->
      o.usedHeap = 10000000
      return o
    }
    jmxControl.demand.getOperatingSystemInfo { connector, OperatingInfo o ->
      o.name = "MyName"
      o.osVersion = "0.1"
      o.architecture ="MyArch"
      o.processors = 4
      o.processCpuTime = 123456
      o.committedVirtualMemory = 20000000
      o.usedHeap = 10000000
      o.maxHeap =  20000000
      o.usedNonHeap = 1500000
      o.maxNonHeap =  2500000
      o.usedSwap = 987654
      o.maxSwap = 2000000
      o.usedMemory = 2345678
      o.maxMemory = 4096000
      o.loadAverage = 3.14
      return o
    }

    mockLogging(BrokerInfoService, true)
    def svc = new BrokerInfoService()
    svc.jmxService = jmxControl.createMock()

    def testInstances = []
    mockDomain(OperatingInfo, testInstances)

    def rslt = svc.getOperatingInfo(broker, sampleTime)

    def instances = OperatingInfo.findAll()
    assertEquals("OperatingInfo should have been saved", 1, instances.size())

    OperatingInfo opsInfo = instances[0]

    assertEquals("MyName", opsInfo.name)
    assertEquals("MyArch", opsInfo.architecture)

    assertEquals(20000000, opsInfo.maxHeap)
    assertEquals(1500000, opsInfo.usedNonHeap)
    assertEquals(3.14, opsInfo.loadAverage)

    jmxControl.verify()
  }

  private Broker createbroker() {
    def node = new Node(name: "esb-a")
    def site = new Site(name: "Colo", domainName: "colo.elex.be")
    def environment = new Environment(name: "Test", suffix: "-test")
    def broker = new Broker(node: node, site: site, environment: environment)
    return broker
  }

  Object createOpsMXBean() {
    return new com.sun.management.OperatingSystemMXBean() {
      long getCommittedVirtualMemorySize() { return 10000000 }

      long getTotalSwapSpaceSize() { return 2000000 }

      long getFreeSwapSpaceSize() { return 1234567 }

      long getProcessCpuTime() { return 3141562 }

      long getFreePhysicalMemorySize() { return 15000000 }

      long getTotalPhysicalMemorySize() { return 20000000 }

        @Override
        double getSystemCpuLoad() {
            return 0.123
        }

        @Override
        double getProcessCpuLoad() {
            return 0.246
        }

        String getName() { return "MyName" }

      String getArch() { return "MyArch" }

      String getVersion() { return "0.1" }

      int getAvailableProcessors() { return 4 }

      double getSystemLoadAverage() { return 3.1415 }

        @Override
        ObjectName getObjectName() {
            return "myObjectName"
        }
    }
  }

  private MemoryMXBean createMemoryMXBean() {
    return new MemoryMXBean() {
      int getObjectPendingFinalizationCount() {
        return 0
      }

      MemoryUsage getHeapMemoryUsage() {
        def usage = new MemoryUsage()
        usage.used = 1000000
        usage.max = 2000000
        return usage
      }

      MemoryUsage getNonHeapMemoryUsage() {
        def usage = new MemoryUsage()
        usage.used = 1500000
        usage.max = 2500000
        return usage
      }

      boolean isVerbose() {
        return false
      }

      void setVerbose(boolean value) { }

      void gc() { }

        @Override
        ObjectName getObjectName() {
            return "myObjectName"
        }
    }
  }

}
