package com.kihong.health.web.resource;

import org.springframework.hateoas.EntityModel;

public class Resource<T> extends EntityModel<T> {
  public static String getPrefix() {
    return "/docs/index.html#resources-";
  }
}
