package com.kihong.health.web.resource;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.kihong.health.persistence.dto.record.RecordResponse;
import com.kihong.health.web.controller.RecordController;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class RecordResource extends Resource<RecordResponse> {

  public static WebMvcLinkBuilder selfLinkBuilder = linkTo(RecordController.class);

  public static String getDocs() {
    return getPrefix() + "record-get";
  }

  public static String getListDocs() {
    return getPrefix() + "record-list";
  }

  public static EntityModel<RecordResponse> of(RecordResponse contents, String profile) {
    List<Link> links = getSelfLink(contents);
    links.add(Link.of(profile, "profile"));
    return EntityModel.of(contents, links);
  }

  public static PagedModel toPageResources(
      PagedResourcesAssembler<RecordResponse> assembler, Page<RecordResponse> page) {
    PagedModel pagedResources = assembler.toModel(page,
        (e) -> of(e, getDocs()));
    if (page.isEmpty()) {
      pagedResources = assembler.toEmptyModel(page, RecordResponse.class);
    }
    pagedResources.add(Link.of(getListDocs())
        .withRel("profile"));

    return pagedResources;
  }

  private static List<Link> getSelfLink(RecordResponse contents) {
    List<Link> links = new ArrayList<>();
    links.add(selfLinkBuilder.slash(contents.getId())
        .withSelfRel());

    return links;
  }
}
