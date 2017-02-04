package com.rockyzhu.myserver.oracle.db;

import com.rockyzhu.myserver.api.Spider;
import com.rockyzhu.myserver.api.SpiderType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by hozhu on 11/29/16.
 */
public class SpiderMapper implements RowMapper {

  public Spider mapRow(ResultSet rs, int rowNum)
      throws SQLException {
    Spider spider = new Spider();
    spider.setSpiderId(rs.getInt("spider_id"));
    spider.setName(rs.getString("name"));
    spider.setType(SpiderType.valueOf(rs.getString("type")));
    spider.setActive(rs.getString("is_active").equals("Y") ? true : false);
    return spider;
  }
}
