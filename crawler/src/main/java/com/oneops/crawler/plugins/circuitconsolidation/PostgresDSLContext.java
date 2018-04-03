package com.oneops.crawler.plugins.circuitconsolidation;

import java.sql.SQLException;
import org.jooq.DSLContext;

/**
 * @author dsing17
 *
 */
public interface PostgresDSLContext {

  public DSLContext getDSLContext(String cmsDbUrl, String cmsDbUserName, String cmsDbPassword) throws SQLException;
  public void closeDSLContext(); 
  
}
