package com.rockyzhu.myserver.zk;

import com.linkedin.d2.discovery.util.D2Config;
import java.util.Collections;
import java.util.Map;
import org.json.simple.JSONObject;

public class ZooKeeperPopulator {

  public void populateZooKeeper(JSONObject json) throws Exception {
    D2Config d2Config = new D2Config(
        (String)json.get("zkHosts"),
        ((Long)json.get("zkSessionTimeout")).intValue(),
        (String)json.get("zkBasePath"),
        ((Long)json.get("zkSessionTimeout")).intValue(),
        ((Long)json.get("zkRetryLimit")).intValue(),
        (Map<String, Object>) Collections.EMPTY_MAP,
        (Map<String, Object>)json.get("serviceDefaults"),
        (Map<String, Object>)json.get("clusterServiceConfigurations"),
        (Map<String, Object>)Collections.EMPTY_MAP,
        (Map<String, Object>)Collections.EMPTY_MAP);

    d2Config.configure();

    System.out.println("Finished populating zookeeper with d2 configuration");
  }
}
