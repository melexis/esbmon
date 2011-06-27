databaseChangeLog = {

  changeSet(author: "pti (generated)", id: "1309159083403-1") {
    createTable(tableName: "BROKER") {
      column(autoIncrement: "true", name: "ID", type: "BIGINT") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_48")
      }

      column(name: "VERSION", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "ENVIRONMENT_ID", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "NODE_ID", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "SITE_ID", type: "BIGINT") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "pti (generated)", id: "1309159083403-2") {
    createTable(tableName: "ENVIRONMENT") {
      column(autoIncrement: "true", name: "ID", type: "BIGINT") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_51")
      }

      column(name: "VERSION", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "NAME", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "SUFFIX", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "pti (generated)", id: "1309159083403-3") {
    createTable(tableName: "NODE") {
      column(autoIncrement: "true", name: "ID", type: "BIGINT") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_53")
      }

      column(name: "VERSION", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "NAME", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "pti (generated)", id: "1309159083403-4") {
    createTable(tableName: "SITE") {
      column(autoIncrement: "true", name: "ID", type: "BIGINT") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "SYS_PK_55")
      }

      column(name: "VERSION", type: "BIGINT") {
        constraints(nullable: "false")
      }

      column(name: "DOMAIN_NAME", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }

      column(name: "NAME", type: "VARCHAR(255)") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "pti (generated)", id: "1309159083403-5") {
    createIndex(indexName: "SYS_IDX_SYS_CT_46_49", tableName: "BROKER", unique: "true") {
      column(name: "ENVIRONMENT_ID")

      column(name: "SITE_ID")

      column(name: "NODE_ID")
    }
  }

  changeSet(author: "pti (generated)", id: "1309159083403-6") {
    addForeignKeyConstraint(baseColumnNames: "ENVIRONMENT_ID", baseTableName: "BROKER", baseTableSchemaName: "PUBLIC", constraintName: "FKADB577D9B61188E4", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "ENVIRONMENT", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
  }

  changeSet(author: "pti (generated)", id: "1309159083403-7") {
    addForeignKeyConstraint(baseColumnNames: "NODE_ID", baseTableName: "BROKER", baseTableSchemaName: "PUBLIC", constraintName: "FKADB577D9A488FDB0", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "NODE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
  }

  changeSet(author: "pti (generated)", id: "1309159083403-8") {
    addForeignKeyConstraint(baseColumnNames: "SITE_ID", baseTableName: "BROKER", baseTableSchemaName: "PUBLIC", constraintName: "FKADB577D9A3AEBB90", deferrable: "false", initiallyDeferred: "false", onDelete: "NO ACTION", onUpdate: "NO ACTION", referencedColumnNames: "ID", referencedTableName: "SITE", referencedTableSchemaName: "PUBLIC", referencesUniqueColumn: "false")
  }
}
