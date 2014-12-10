package esbmon

class Broker {

    static belongsTo = [
        stage: esbmon.Environment,
        site:Site,
        node:Node
    ]

    static constraints = {
        node(unique: ['site', 'stage'])
    }

    String getHostName() {"${node.name}${stage.suffix}.${site.domainName}"}
    def setHostName(String s){}; \

    String getJmxUri() {
        // karaf container:
        // "service:jmx:rmi:///jndi/rmi://${hostName}:1099/karaf-root"

        // default broker
        // "service:jmx:rmi:///jndi/rmi://${hostName}:11099/jmxrmi"

        // local broker
        // "service:jmx:rmi:///jndi/rmi://${hostName}:11109/jmxrmi"

        // global broker
        "service:jmx:rmi:///jndi/rmi://${hostName}:11119/jmxrmi"
    }

    def setJmxUri(String) {}

    String getName() {
        "${node.name}.${site.name}"
    }

    def setName(String) {}

    String getBaseJmxName() {
      // TODO get this better modelled.
      def siteName = site.domainName
                         .replace(".elex.be", ".global")
                         .replace("sensors", "ieper");

        "org.apache.activemq:brokerName=${node.name}-test.${siteName}"
    }

    def setBaseJmxName(String s) {}


    String getJMXName = {
        getBaseJMXName + ",Type=Broker"
    }

    def setJMXName(String s) {}


}
