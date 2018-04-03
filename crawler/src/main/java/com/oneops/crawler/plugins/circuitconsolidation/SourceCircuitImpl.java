package com.oneops.crawler.plugins.circuitconsolidation;

import static com.oneops.crawler.jooq.cms.Tables.CM_CI;
import static com.oneops.crawler.jooq.cms.Tables.CM_CI_ATTRIBUTES;
import static com.oneops.crawler.jooq.cms.Tables.CM_CI_RELATIONS;
import static com.oneops.crawler.jooq.cms.Tables.MD_CLASSES;
import static com.oneops.crawler.jooq.cms.Tables.MD_CLASS_ATTRIBUTES;
import static com.oneops.crawler.jooq.cms.Tables.MD_RELATIONS;
import static com.oneops.crawler.jooq.cms.Tables.NS_NAMESPACES;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceCircuitImpl implements SourceCircuit {

  private final Logger log = LoggerFactory.getLogger(getClass());
  DSLContext dSLContext;


  @Override
  public List<Record> getPlatformListForSourcePackPhase(String SourceName, String packName,
      String phase) {
    // get list of platforms with phase

    Result<Record> platformsInDesignPhase = getPlatformsInGivenPhase(phase);
    List<Record> designPhasePlatformsForGivenSourceAndPack =
        getListOfPlatformsForGivenSourceAndPack(platformsInDesignPhase, SourceName, packName);

    log.info("designPhasePlatformsForGivenSourceAndPack: ");
    log.info("" + designPhasePlatformsForGivenSourceAndPack);
    return designPhasePlatformsForGivenSourceAndPack;
  }

  private Result<Record> getPlatformsInGivenPhase(String phase) {

    Result<Record> platformsInDesignPhase = dSLContext.select().from(CM_CI).join(MD_CLASSES)
        .on(CM_CI.CLASS_ID.eq(MD_CLASSES.CLASS_ID)).join(NS_NAMESPACES)
        .on(CM_CI.NS_ID.eq(NS_NAMESPACES.NS_ID)).where(MD_CLASSES.CLASS_NAME.eq(phase)).fetch();

    log.info("Number of platforms in Design Phase : " + platformsInDesignPhase.size());
    return platformsInDesignPhase;
  }

  @Override
  public Long getPlatformCompute(long platformId) {

    Result<Record1<Long>> relationsInPlatform =
        dSLContext.select(CM_CI_RELATIONS.TO_CI_ID).from(CM_CI_RELATIONS).join(MD_RELATIONS)
            .on(MD_RELATIONS.RELATION_ID.eq(CM_CI_RELATIONS.RELATION_ID)).join(CM_CI)
            .on(CM_CI.CI_ID.eq(CM_CI_RELATIONS.TO_CI_ID)).where(CM_CI.CI_NAME.eq("compute"))
            .and(CM_CI_RELATIONS.FROM_CI_ID.eq(platformId)).fetch();
    log.info("relationsInPlatform: " + relationsInPlatform);
    return relationsInPlatform.get(0).getValue(CM_CI_RELATIONS.TO_CI_ID);

  }

  @Override
  public Result<Record> getComputeAttributes(long computeCid) {

    Map<String, String> computeAttributesMap = new HashMap<String, String>();
    Result<Record> computeAttributes= getCiAttributesForCid(computeCid);
    log.info("computeAttributes: "+computeAttributes);
    for (Record computeAttribute:computeAttributes) {
      //log.info("computeAttribute: " +computeAttribute);
      
    }
    return computeAttributes;

  }

  public DSLContext getdSLContext() {
    return dSLContext;
  }

  public void setdSLContext(DSLContext dSLContext) {
    this.dSLContext = dSLContext;
  }

  public List<Record> getListOfPlatformsForGivenSourceAndPack(Result<Record> platforms,
      String source, String pack) {

    List<Record> platformsForGivenSourceAndPack = new ArrayList<Record>();
    for (Record platform : platforms) // iterate through all platforms to find target source and
                                      // pack
    {
      log.info("platform: " + platform);

      Result<Record> platformAttributes = getCiAttributesForCid(platform.getValue(CM_CI.CI_ID));

      boolean sourceExists = false;
      boolean packExists = false;
      for (Record attribute : platformAttributes) {
        String attributeName = attribute.getValue(MD_CLASS_ATTRIBUTES.ATTRIBUTE_NAME);
        String attributevalue = attribute.getValue(CM_CI_ATTRIBUTES.DF_ATTRIBUTE_VALUE);


        switch (attributeName) {
          case "source":
            if (attributevalue.equals(source)) {
              sourceExists = true;

            }
            break;
          case "pack":
            if (attributevalue.equals(pack)) {

              packExists = true;

            }
            break;
        }


      }
      if (sourceExists && packExists) {
        platformsForGivenSourceAndPack.add(platform);
      }

    }
    log.info("platformsForGivenSourceAndPack <source> {}, <pack> {} <size> {}: ", source, pack,
        platformsForGivenSourceAndPack.size());
    return platformsForGivenSourceAndPack;
  }

  public Result<Record> getCiAttributesForCid(long cId) {

    Result<Record> ciAttributes =
        dSLContext.select().from(CM_CI_ATTRIBUTES).join(MD_CLASS_ATTRIBUTES)
            .on(MD_CLASS_ATTRIBUTES.ATTRIBUTE_ID.eq(CM_CI_ATTRIBUTES.ATTRIBUTE_ID))
            .where(CM_CI_ATTRIBUTES.CI_ID.eq(cId)).fetch();
    return ciAttributes;

  }

}
