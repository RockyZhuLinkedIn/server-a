package com.rockyzhu.myserver.oracle.db;

import com.rockyzhu.myserver.api.Spider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import javax.sql.DataSource;

/**
 * Created by hozhu on 11/29/16.
 */
public class SpiderWriteDaoImpl implements SpiderWriteDao {

  private DataSource _dataSource;

  public SpiderWriteDaoImpl(DataSource dataSource) {
    _dataSource = dataSource;
  }

  @Override
  public Integer insert(Spider spider) {
    String sql = String.format("INSERT INTO spiders (spider_id, name, type, is_active) VALUES (:spiderId, :name, :type, :isActive)");
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("spiderId", spider.getSpiderId())
        .addValue("name", spider.getName())
        .addValue("type", spider.getType().toString())
        .addValue("isActive", spider.isActive() ? "Y" : "N");
    return new NamedParameterJdbcTemplate(_dataSource).update(sql, params);
  }

  @Override
  public Integer update(Spider spider) {
    String sql = String.format("UPDATE spiders SET name = :name, type = :type, is_active = :isActive WHERE spider_id = :spiderId");
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("spiderId", spider.getSpiderId())
        .addValue("name", spider.getName())
        .addValue("type", spider.getType().toString())
        .addValue("isActive", spider.isActive() ? "Y" : "N");
    return new NamedParameterJdbcTemplate(_dataSource).update(sql, params);
  }

  @Override
  public void delete(long spiderId) {
    String sql = String.format("DELETE FROM spiders WHERE spider_id = :spiderId");
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("spiderId", spiderId);
    new NamedParameterJdbcTemplate(_dataSource).update(sql, params);
  }
}