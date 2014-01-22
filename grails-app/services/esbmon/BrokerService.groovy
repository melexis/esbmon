package esbmon

class BrokerService {

  static transactional = true

  def generateAllBrokers() {
    def environments = Environment.findAll()
    def sites = Site.findAll();
    def nodes = Node.findAll();

    environments.each { Environment environment ->
      sites.each { site ->
        nodes.each {node ->
          def c = Broker.createCriteria()
          def brokers = c {
            and {
              eq('stage', environment)
              eq('site', site)
              eq('node', node)
            }
          }

          if (brokers.empty) {
            def b = new Broker(
                stage: environment,
                site: site,
                node: node)
            b.save()
          }
        }
      }
    }
  }
}
