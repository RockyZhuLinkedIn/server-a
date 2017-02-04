package com.rockyzhu.myserver.oracle.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.sql.DataSource;

public class DataSourceFactory {
  public static DataSource getDataSource(Properties allProperties) throws IOException, PropertyVetoException {
    ComboPooledDataSource dataSource = new ComboPooledDataSource();
    dataSource.setDriverClass(allProperties.getProperty("db.driverClass")); //loads the jdbc driver
    //   jdbc:oracle:thin:<user>/<password>@<host>:<port>/<db>
    dataSource.setJdbcUrl(allProperties.getProperty("db.jdbcUrl"));
    dataSource.setMinPoolSize(Integer.valueOf(allProperties.getProperty("db.minPoolSize")));
    dataSource.setAcquireIncrement(Integer.valueOf(allProperties.getProperty("db.acquireIncrement")));
    dataSource.setMaxPoolSize(Integer.valueOf(allProperties.getProperty("db.maxPoolSize")));
    return dataSource;
  }
}
