package com.oneops.crawler.plugins.circuitconsolidation;

import java.sql.SQLException;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Service {
  private final static Logger log = LoggerFactory.getLogger(Service.class);


  public static void main(String[] args) throws SQLException {
    String cmsDbUserName = System.getProperty("cms.db.user.name");
    String cmsDbPassword = System.getProperty("cms.db.user.password");
    String cmsDbUrl = System.getProperty("cms.db.url");

    PostgresDSLContext postgresDSLContext = new PostgresDSLContextImpl();
    DSLContext dSLContext =
        postgresDSLContext.getDSLContext(cmsDbUrl, cmsDbUserName, cmsDbPassword);

    SourceCircuitImpl SourceCircuitImpl = new SourceCircuitImpl();
    SourceCircuitImpl.setdSLContext(dSLContext);

    // List<Record> platforms= SourceCircuitImpl.getPlatformListForSourcePackPhase("main",
    // "cassandra", "catalog.Platform");
    // 122652849 CIS exists in prod

   // long computeCid = SourceCircuitImpl.getPlatformCompute(Long.valueOf("122652849"));
   // log.info("computeCid: " + computeCid);
    Result<Record> computeAttributes=SourceCircuitImpl.getComputeAttributes(Long.valueOf("122652869"));
    postgresDSLContext.closeDSLContext();
  }

}

