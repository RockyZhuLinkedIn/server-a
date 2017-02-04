package com.rockyzhu.myserver.oracle.db;

import com.rockyzhu.myserver.api.Spider;

/**
 * Created by hozhu on 11/29/16.
 */
public interface SpiderReadDao {
  Spider get(long id);
}
