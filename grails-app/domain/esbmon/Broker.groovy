package esbmon

class Broker {

  def Environment environment
  def Site site
  def Node node
  static belongsTo = [Environment, Site, Node]

  static constraints = {
    node(unique: ['site','environment'])
  }

  String getJmxUri() {
    "service:jmx:rmi:///jndi/rmi://${node.name}${environment.suffix}.${site.domainName}:1099/karaf-root"
  }
  def setJmxUri(String) {}

  String getName() {
    "${node.name}.${site.name}"
  }
  def setName(String) {}

}