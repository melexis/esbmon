package esbmon

import grails.test.*
import javax.management.remote.JMXConnector

class BrokerInfoServiceTests extends GrailsUnitTestCase {

  def Broker broker

  protected void setUp() {
    super.setUp()
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testMemoryStatus() {
    def jmxService = mockFor(JmxService)
    def conn = mockFor(JMXConnector)
    def memInfo = [heapMemoryUsage: 1000, nonHeapMemoryUsage: 2000]
    jmxService.demand.getConnector {broker -> return conn.createMock()}
    jmxService.demand.getMemoryInfo {connector -> return memInfo}

    def svc = new BrokerInfoService()
    svc.jmxService = jmxService.createMock()

    def rslt = svc.getBrokerInfo(broker)
    assertEquals(1000,rslt['heapMemoryUsage'])
    assertEquals(2000,rslt['nonHeapMemoryUsage'])
  }

}
