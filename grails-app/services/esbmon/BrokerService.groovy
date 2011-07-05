package esbmon

class BrokerService {

  static transactional = true

  def generateAllBrokers() {
    def environments = Environment.findAll()
    def sites = Site.findAll();
    def nodes = Node.findAll();

    environments.each { environment ->
      sites.each { site ->
        nodes.each {node ->
          def c = Broker.createCriteria()
          def brokers = c {
            and {
              eq('environment', environment)
              eq('site', site)
              eq('node', node)
            }
          }

          if (brokers.empty) {
            new Broker(environment: environment, site: site, node: node).save()
          }
        }
      }
    }
  }
}