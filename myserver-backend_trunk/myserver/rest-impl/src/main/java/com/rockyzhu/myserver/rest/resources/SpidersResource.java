package com.rockyzhu.myserver.rest.resources;

import com.linkedin.restli.common.HttpStatus;
import com.linkedin.restli.server.CreateResponse;
import com.linkedin.restli.server.UpdateResponse;
import com.linkedin.restli.server.annotations.RestLiCollection;
import com.linkedin.restli.server.resources.CollectionResourceTemplate;
import com.rockyzhu.myserver.oracle.db.SpiderReadDao;
import com.rockyzhu.myserver.oracle.db.SpiderWriteDao;
import com.rockyzhu.myserver.api.Spider;
import javax.inject.Inject;
import javax.inject.Named;


@RestLiCollection(name = "spiders", namespace = "com.rockyzhu.rest.resources", keyName = "id")
public class SpidersResource extends CollectionResourceTemplate<Long, Spider> {
  @Inject
  @Named("spiderReadDao")
  private SpiderReadDao _spiderReadDao;

  @Inject
  @Named("spiderWriteDao")
  private SpiderWriteDao _spiderWriteDao;

  @Override
  public Spider get(Long id) {
    return _spiderReadDao.get(id);
  }

  @Override
  public CreateResponse create(Spider entity) {
    _spiderWriteDao.insert(entity);
    return new CreateResponse(entity.getSpiderId());
  }

  @Override
  public UpdateResponse update(Long id, Spider entity) {
    entity.setSpiderId(id);
    _spiderWriteDao.update(entity);
    return new UpdateResponse(HttpStatus.S_204_NO_CONTENT);
  }

  @Override
  public UpdateResponse delete(Long id) {
    _spiderWriteDao.delete(id);
    return new UpdateResponse(HttpStatus.S_204_NO_CONTENT);
  }
}
