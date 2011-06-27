import esbmon.Site
import esbmon.Environment
import esbmon.Node

class BootStrap {

    def init = { servletContext ->
      new Site(name: "Colo Room", domainName: "colo.elex.be").save()
      new Site(name: "Erfurt", domainName: "erfurt.elex.be").save()
      new Site(name: "Ieper", domainName: "sensors.elex.be").save()
      new Site(name: "Sofia", domainName: "sofia.elex.be").save()

      new Node(name:"esb-a").save()
      new Node(name:"esb-b").save()

      new Environment(name:"Test", suffix:"-test").save()
    }

    def destroy = {
    }
}
