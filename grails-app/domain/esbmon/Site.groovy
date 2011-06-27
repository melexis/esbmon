package esbmon

class Site {

  String name

  String domainName

  static constraints = {
    domainName(matches: '^(([a-zA-Z0-9-]+\\.)+)[a-zA-Z]{2,4}$')
  }

  String toString() { name }

}
