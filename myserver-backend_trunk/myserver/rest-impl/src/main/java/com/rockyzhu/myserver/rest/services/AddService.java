package com.rockyzhu.myserver.rest.services;

import com.google.common.collect.Maps;
import com.linkedin.restli.common.ComplexResourceKey;
import com.linkedin.restli.common.EmptyRecord;
import com.linkedin.restli.common.HttpStatus;
import com.linkedin.restli.server.CreateResponse;
import com.linkedin.restli.server.RestLiServiceException;
import com.linkedin.restli.server.UpdateResponse;
import com.rockyzhu.myserver.api.AddEntity;
import com.rockyzhu.myserver.api.AddKey;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Created by hozhu on 10/19/16.
 */
@Component("addService") // singleton
public class AddService {

  private final Map<String, Integer> _database = Maps.newHashMap();

  public AddEntity get(ComplexResourceKey<AddKey, EmptyRecord> complexResourceKey) {
    AddKey addKey = complexResourceKey.getKey();
    String key = addKey.getA() <  addKey.getB() ? addKey.getA() + "_" + addKey.getB()
                     : addKey.getB() + "_" + addKey.getA();
    int result = _database.containsKey(key) ? _database.get(key) : addKey.getA() + addKey.getB();

    AddEntity addEntity = new AddEntity()
        .setA(addKey.getA())
        .setB(addKey.getB())
        .setResult(result);
    System.out.println(addKey.getA() + " + " + addKey.getB() + " = " + addEntity.getResult());
    return addEntity;
  }

  public CreateResponse create(AddEntity addEntity) {
    String key = addEntity.getA() <  addEntity.getB() ? addEntity.getA() + "_" + addEntity.getB()
                     : addEntity.getB() + "_" + addEntity.getA();
    if (_database.containsKey(key)) {
      throw new RestLiServiceException(HttpStatus.S_409_CONFLICT);
    }
    _database.put(key, addEntity.getResult());
    return new CreateResponse(HttpStatus.S_200_OK);
  }

  public UpdateResponse update(ComplexResourceKey<AddKey, EmptyRecord> complexResourceKey, AddEntity addEntity) {
    AddKey addKey = complexResourceKey.getKey();
    String key = addKey.getA() <  addKey.getB() ? addKey.getA() + "_" + addKey.getB()
        : addKey.getB() + "_" + addKey.getA();
    _database.put(key, addEntity.getResult());
    return new UpdateResponse(HttpStatus.S_200_OK);
  }

  public UpdateResponse delete(ComplexResourceKey<AddKey, EmptyRecord> complexResourceKey) {
    AddKey addKey = complexResourceKey.getKey();
    String key = addKey.getA() <  addKey.getB() ? addKey.getA() + "_" + addKey.getB()
        : addKey.getB() + "_" + addKey.getA();
    _database.remove(key);
    return new UpdateResponse(HttpStatus.S_200_OK);
  }
}
