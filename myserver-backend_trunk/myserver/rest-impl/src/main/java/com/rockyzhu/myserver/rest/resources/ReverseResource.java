package com.rockyzhu.myserver.rest.resources;

import com.linkedin.restli.server.annotations.RestLiCollection;
import com.linkedin.restli.server.resources.CollectionResourceTemplate;
import com.rockyzhu.myserver.rest.services.ReverseService;

import com.rockyzhu.myserver.api.Buddy;
import javax.inject.Inject;
import javax.inject.Named;

@RestLiCollection(name = "reverse", namespace = "com.rockyzhu.rest.resources")
public class ReverseResource extends CollectionResourceTemplate<Long, Buddy> {

  @Inject
  @Named("reverseService")
  private ReverseService _reverseService;

  @Override
  public Buddy get(Long key) {
    return _reverseService.get(key);
  }
}
