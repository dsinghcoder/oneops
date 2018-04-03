package com.oneops.crawler.plugins.circuitconsolidation;

import java.util.List;
import java.util.Map;
import org.jooq.Record;
import org.jooq.Result;

public interface SourceCircuit {

  public List<Record> getPlatformListForSourcePackPhase(String SourceName, String packName, String phase);
  public Long getPlatformCompute(long platformId); 
  public Result<Record> getComputeAttributes(long computeCid);
  
  
}
