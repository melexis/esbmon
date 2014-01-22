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
        "service:jmx:rmi:///jndi/rmi://${hostName}:1099/karaf-root"
    }

    def setJmxUri(String) {}

    String getName() {
        "${node.name}.${site.name}"
    }

    def setName(String) {}

    String getBaseJmxName() {
        "org.apache.activemq:BrokerName=global"
    }

    def setBaseJmxName(String s) {}


    String getJMXName = {
        getBaseJMXName + ",Type=Broker"
    }

    def setJMXName(String s) {}


}
