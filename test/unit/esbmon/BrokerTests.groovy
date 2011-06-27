package esbmon

import grails.test.*

class BrokerTests extends GrailsUnitTestCase {

  def Broker broker

  protected void setUp() {
    super.setUp()
    def env = new Environment(name:"Test", suffix:"-test")
    def site = new Site(name:"Colo", domainName: "colo.elex.be")
    def node = new Node(name:"esb-a")
    broker = new Broker(environment: env, site: site, node: node)
  }

  protected void tearDown() {
    super.tearDown()
  }

  void testJmxUri() {
    def uri = broker.jmxUri

    assertEquals ("JMX Uri is wrong for broker", "service:jmx:rmi:///jndi/rmi://esb-a-test.colo.elex.be:1099/karaf-root", uri)
  }

  void testName() {
    def name = broker.name
    assertEquals ("Name is wrong for broker", "esb-a.Colo", name)
  }
}
