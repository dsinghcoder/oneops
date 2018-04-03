package com.oneops.crawler.plugins.circuitconsolidation;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresDSLContextImpl implements PostgresDSLContext{

  private final Logger log = LoggerFactory.getLogger(getClass());

  Connection conn;
  DSLContext dSLContext;

  public DSLContext getDSLContext(String cmsDbUrl, String cmsDbUserName, String cmsDbPassword)
      throws SQLException {
    this.conn = DriverManager.getConnection(cmsDbUrl, cmsDbUserName, cmsDbPassword);
    return DSL.using(conn, SQLDialect.POSTGRES);
  }

  public void closeDSLContext() {
    try {

      if (this.conn != null) {
        this.conn.close();
      }
      if (this.dSLContext != null) {
        this.dSLContext = null;
        this.dSLContext.close();
      }

    } catch (Exception e) {
      log.error("Error while closing DSLContext: {}", e);
    }

  }

}
