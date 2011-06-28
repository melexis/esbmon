import esbmon.Site
import esbmon.Environment
import esbmon.Node
import esbmon.BrokerService

class BootStrap {

  BrokerService brokerService

    def init = { servletContext ->
      new Site(name: "Colo Room", domainName: "colo.elex.be").save()
      new Site(name: "Erfurt", domainName: "erfurt.elex.be").save()
      new Site(name: "Ieper", domainName: "sensors.elex.be").save()
      new Site(name: "Sofia", domainName: "sofia.elex.be").save()

      new Node(name:"esb-a",nodeCode: "A").save()
      new Node(name:"esb-b",nodeCode: "B").save()

      new Environment(name:"Test", suffix:"-test").save()

      brokerService.generateAllBrokers()
    }

    def destroy = {
    }
}
