dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    username = "root"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = "net.sf.ehcache.hibernate.EhCacheProvider"
}

// environment specific settings
environments {
    development {
	    dataSource {
			      dbCreate = "create-drop" // one of 'create', 'create-drop','update'
			      url = "jdbc:mysql://localhost:3306/esbmon_development"
		    }
	  }
	  test {
		    dataSource {
			      dbCreate = "update"
			      url = "jdbc:mysql://localhost:3306/esbmon_test"
		    }
	  }
	  production {
		    dataSource {
			      dbCreate = "update"
			      url = "jdbc:mysql://localhost:3306/esbmon_production"
		    }
	  }
}
