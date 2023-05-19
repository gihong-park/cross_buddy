package com.kihong.health.web.resource;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.kihong.health.persistence.dto.workoutOftheDay.WorkoutOftheDayResponse;
import com.kihong.health.persistence.model.Movement;
import com.kihong.health.web.controller.WorkoutOftheDayController;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class WorkoutOftheDayResource extends Resource<WorkoutOftheDayResponse> {

  public static WebMvcLinkBuilder selfLinkBuilder = linkTo(WorkoutOftheDayController.class);

  public static String getDocs() {
    return getPrefix() + "workoutOftheDay-get";
  }

  public static String getListDocs() {
    return getPrefix() + "workoutOftheDay-list";
  }

  public static EntityModel<WorkoutOftheDayResponse> of(WorkoutOftheDayResponse contents,
      String profile) {
    List<Link> links = getSelfLink(contents);
    links.add(Link.of(profile, "profile"));
    return EntityModel.of(contents, links);
  }

  public static PagedModel toPageResources(
      PagedResourcesAssembler<WorkoutOftheDayResponse> assembler,
      Page<WorkoutOftheDayResponse> page) {
    PagedModel pagedResources = assembler.toModel(page,
        (e) -> of(e, getDocs()));
    if (page.isEmpty()) {
      pagedResources = assembler.toEmptyModel(page, WorkoutOftheDayResponse.class);
    }
    pagedResources.add(Link.of(getListDocs())
        .withRel("profile"));

    return pagedResources;
  }

  private static List<Link> getSelfLink(WorkoutOftheDayResponse contents) {
    List<Link> links = new ArrayList<>();
    links.add(selfLinkBuilder.slash(contents.getId())
        .withSelfRel());

    return links;
  }
}
