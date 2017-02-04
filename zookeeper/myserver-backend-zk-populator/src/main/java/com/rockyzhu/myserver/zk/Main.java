package com.rockyzhu.myserver.zk;

import java.io.FileReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main {
  public static void main(String[] args) throws Exception {
    if (args == null || args.length != 1) {
      System.out.println("must provide 1 parameter as the configuration file");
      System.exit(-1);
    }
    JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(args[0]));

    ZooKeeperPopulator zooKeeperPopulator = new ZooKeeperPopulator();
    zooKeeperPopulator.populateZooKeeper(json);
  }
}
