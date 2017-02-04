package com.rockyzhu.myserver.oracle.db;


import com.google.common.collect.Lists;
import com.rockyzhu.myserver.api.Spider;
import com.rockyzhu.myserver.api.SpiderType;
import java.beans.PropertyVetoException;

import javax.sql.DataSource;

import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


/**
 * Created by hozhu on 11/29/16.
 */
public class TestSpiderDao {

  private List<Long> _spiderIds;
  private SpiderWriteDao _spiderWriteDao;
  private SpiderReadDao _spiderReadDao;
  private DataSource _dataSource;

  @BeforeMethod
  public void setup() throws PropertyVetoException {

    ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    _dataSource = (DataSource)context.getBean("dataSource");
    _spiderWriteDao = new SpiderWriteDaoImpl(_dataSource);
    _spiderReadDao = new SpiderReadDaoImpl(_dataSource);

    _spiderIds = Lists.newArrayList();
  }

  @AfterMethod
  public void testDown() {
    _spiderIds.forEach(id -> _spiderWriteDao.delete(id));
  }

  @Test
  public void test() {
    Spider spider = new Spider()
        .setSpiderId(123)
        .setActive(true)
        .setName("a name")
        .setType(SpiderType.XYZ);

    _spiderWriteDao.insert(spider); // this returns the id of the record in the database
    _spiderIds.add(spider.getSpiderId());
    Spider spiderDB = _spiderReadDao.get(spider.getSpiderId());
    Assert.assertEquals((long)spiderDB.getSpiderId(), 123);
    Assert.assertEquals((boolean)spiderDB.isActive(), true);
    Assert.assertEquals(spiderDB.getName(), "a name");
    Assert.assertEquals(spiderDB.getType(), SpiderType.XYZ);

    spider.setName("A new Name");
    _spiderWriteDao.update(spider);
    Spider spiderDbUpdated = _spiderReadDao.get(spider.getSpiderId());
    Assert.assertEquals((long)spiderDbUpdated.getSpiderId(), 123);
    Assert.assertEquals((boolean)spiderDbUpdated.isActive(), true);
    Assert.assertEquals(spiderDbUpdated.getName(), "A new Name");
    Assert.assertEquals(spiderDbUpdated.getType(), SpiderType.XYZ);
  }
}

