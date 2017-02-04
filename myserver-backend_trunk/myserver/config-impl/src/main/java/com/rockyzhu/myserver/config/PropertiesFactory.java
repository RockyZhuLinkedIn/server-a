package com.rockyzhu.myserver.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public class PropertiesFactory {
  public static Properties getProperties(String relativePath) throws IOException {
    String path = new File(new File(".").getAbsolutePath()).getCanonicalPath() + relativePath; //"/config/application.properties";
    Properties allProperties = new Properties();
    allProperties.load(new FileInputStream(path));
    return allProperties;
  }
}
