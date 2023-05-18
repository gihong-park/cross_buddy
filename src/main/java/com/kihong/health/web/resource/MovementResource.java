package com.kihong.health.web.resource;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.kihong.health.persistence.model.Movement;
import com.kihong.health.web.controller.MovementController;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class MovementResource extends EntityModel<Movement> {
  public static WebMvcLinkBuilder selfLinkBuilder = linkTo(MovementController.class);

  public static EntityModel<Movement> of(Movement contents, String profile) {
    List<Link> links = getSelfLink(contents);
    links.add(Link.of(profile, "profile"));
    return EntityModel.of(contents, links);
  }

  public static PagedModel toPageResources(PagedResourcesAssembler<Movement> assembler, Page<Movement> page) {
    PagedModel pagedResources = assembler.toModel(page,
        (e) -> of(e, "/docs/index.html#resources-movement-get"));
    if (page.isEmpty()) {
      pagedResources = assembler.toEmptyModel(page, Movement.class);
    }
    pagedResources.add(Link.of("/docs/index.html#resources-movement-list")
        .withRel("profile"));

    return pagedResources;
  }

  private static List<Link> getSelfLink(Movement contents) {
    List<Link> links = new ArrayList<>();
    links.add(selfLinkBuilder.slash(contents.getId())
        .withSelfRel());

    return links;
  }
}
