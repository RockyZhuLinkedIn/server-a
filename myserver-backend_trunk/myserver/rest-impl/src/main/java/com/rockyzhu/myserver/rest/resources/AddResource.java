package com.rockyzhu.myserver.rest.resources;

import com.linkedin.restli.common.ComplexResourceKey;
import com.linkedin.restli.common.EmptyRecord;
import com.linkedin.restli.server.CreateResponse;
import com.linkedin.restli.server.UpdateResponse;
import com.linkedin.restli.server.annotations.RestLiCollection;
import com.linkedin.restli.server.resources.ComplexKeyResourceTemplate;
import com.rockyzhu.myserver.api.AddKey;
import com.rockyzhu.myserver.api.AddEntity;
import com.rockyzhu.myserver.rest.services.AddService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Rocky Zhu
 */

@RestLiCollection(name = "add", namespace = "com.rockyzhu.rest.resources")
public class AddResource extends ComplexKeyResourceTemplate<AddKey, EmptyRecord, AddEntity> {

  @Inject @Named("addService")
  private AddService _addService;

  @Override
  public AddEntity get(ComplexResourceKey<AddKey, EmptyRecord> complexResourceKey) {
    return _addService.get(complexResourceKey);
  }

  @Override
  public CreateResponse create(AddEntity addEntity) {
    return _addService.create(addEntity);
  }

  @Override
  public UpdateResponse update(ComplexResourceKey<AddKey, EmptyRecord> complexResourceKey, AddEntity addEntity) {
    return _addService.update(complexResourceKey, addEntity);
  }

  @Override
  public UpdateResponse delete(ComplexResourceKey<AddKey, EmptyRecord> complexResourceKey) {
    return _addService.delete(complexResourceKey);
  }
}
