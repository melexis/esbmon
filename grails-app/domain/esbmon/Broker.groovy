package esbmon

class Broker {

    def Environment environment
    def Site site
    def Node node
    static belongsTo = [Environment, Site, Node]

    static constraints = {
        node(unique: ['site', 'environment'])
    }

    String getHostName() {"${node.name}${environment.suffix}.${site.domainName}"}
    def setHostName(String s){}; \

    String getJmxUri() {
        "service:jmx:rmi:///jndi/rmi://${hostName}:1099/karaf-root"
    }

    def setJmxUri(String) {}

    String getName() {
        "${node.name}.${site.name}"
    }

    def setName(String) {}

    String getBaseJmxName() {
        "org.apache.activemq:BrokerName=broker${node.nodeCode}"
    }

    def setBaseJmxName(String s) {}


    String getJMXName = {
        getBaseJMXName + ",Type=Broker"
    }

    def setJMXName(String s) {}


}
