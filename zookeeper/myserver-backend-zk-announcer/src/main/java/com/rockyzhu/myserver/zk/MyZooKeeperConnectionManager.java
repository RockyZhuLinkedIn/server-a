package com.rockyzhu.myserver.zk;

import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.balancer.config.PartitionDataFactory;
import com.linkedin.d2.balancer.servers.ZKUriStoreFactory;
import com.linkedin.d2.balancer.servers.ZooKeeperAnnouncer;
import com.linkedin.d2.balancer.servers.ZooKeeperConnectionManager;
import com.linkedin.d2.balancer.servers.ZooKeeperServer;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;

public class MyZooKeeperConnectionManager {

  private final JSONObject _json;
  private final ZooKeeperConnectionManager _manager;
  ExecutorService _executorService;

  public MyZooKeeperConnectionManager(JSONObject json) {
    _json = json;
    List<Map<String, Object>> serviceClusterMappings = (List <Map<String, Object>>) json.get("zkAnnouncers");

    // create d2 announcers to announce the existence of these servers to zookeeper
    ZooKeeperAnnouncer[] zkAnnouncers = new ZooKeeperAnnouncer[serviceClusterMappings.size()];
    for (int i = 0; i < zkAnnouncers.length; i++) {
      Map<String, Object> d2ServerConfig = serviceClusterMappings.get(i);
      zkAnnouncers[i] = new ZooKeeperAnnouncer(new ZooKeeperServer());
      zkAnnouncers[i].setUri((String) d2ServerConfig.get("uri"));
      zkAnnouncers[i].setCluster((String) d2ServerConfig.get("cluster"));
      zkAnnouncers[i].setWeightOrPartitionData(PartitionDataFactory.createPartitionDataMap((Map<String, Object>) d2ServerConfig.get("partitionData")));
    }

    String zkConnectString = (String)json.get("zkConnectString");
    int zkSessionTimeout = ((Integer)json.get("zkSessionTimeout")).intValue();
    String zkBasePath = (String)json.get("zkBasePath");

    // manager will keep track of the lifecycle of d2 announcers
    _manager = new ZooKeeperConnectionManager(zkConnectString, zkSessionTimeout, zkBasePath, new ZKUriStoreFactory(), zkAnnouncers);

    _executorService = Executors.newSingleThreadExecutor();
  }

  public void start() throws Exception {
    System.out.println("Starting zookeeper announcers...");
    long timeout = ((Long) _json.get("announcerStartTimeout"));
    Future task = _executorService.submit(() -> _manager.start(new Callback<None>() {
      @Override
      public void onError (Throwable e) {
        System.err.println("problem starting D2 announcers. Aborting...");
        e.printStackTrace();
        System.exit(1);
      }

      @Override
      public void onSuccess (None result) {
        System.out.println("D2 announcers successfully started. ");
        System.out.println("press any key to shut down zookeeper announcers");
      }
    }));
    try {
      task.get(timeout, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      System.err.println("Cannot start zookeeper announcer. Timeout is set to " + timeout + " ms");
      e.printStackTrace();
      System.exit(1);
    }
  }

  public void shutdown() throws IOException {
    System.out.println("Shutting down zookeeper announcers...");
    long timeout = ((Long)_json.get("announcerShutdownTimeout"));
    Future task = _executorService.submit(() -> _manager.shutdown(new Callback<None>() {
      @Override
      public void onError (Throwable e) {
        System.err.println("problem stopping D2 announcers.");
        e.printStackTrace();
      }

      @Override
      public void onSuccess (None result) {
        System.out.println("D2 announcers successfully stopped.");
      }
    }));
    try {
      task.get(timeout, TimeUnit.MILLISECONDS);
    } catch (Exception e) {
      System.err.println("Cannot stop zookeeper announcer. Timeout is set to " + timeout + " ms");
      e.printStackTrace();
    } finally {
      _executorService.shutdown();
    }
  }
}
