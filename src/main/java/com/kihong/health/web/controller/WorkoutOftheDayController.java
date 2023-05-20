package com.kihong.health.web.controller;

import com.kihong.health.persistence.dto.workoutOftheDay.CreateWorkoutOftheDay;
import com.kihong.health.persistence.dto.workoutOftheDay.WorkoutOftheDayResponse;
import com.kihong.health.persistence.model.WorkoutOftheDay;
import com.kihong.health.persistence.repository.WorkoutOftheDayRepository;
import com.kihong.health.persistence.service.workoutOftheDay.WorkoutOftheDayService;
import com.kihong.health.util.DateTemper;
import com.kihong.health.web.resource.WorkoutOftheDayResource;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/wod")
public class WorkoutOftheDayController {

  final WorkoutOftheDayRepository workoutOftheDayRepository;
  final WorkoutOftheDayService wodService;
  final ModelMapper modelMapper;

  @GetMapping
  public ResponseEntity listWOD(Pageable pageable, PagedResourcesAssembler assembler,
      @RequestParam(name = "date", required = false, defaultValue = "#{java.time.LocalDate.now()}") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    LocalDate monday = DateTemper.getFirstDayOfWeek(date);
    LocalDate sunday = DateTemper.getLastDayOfWeek(date);
    Page<WorkoutOftheDay> workoutOftheDayPage = workoutOftheDayRepository.findByDateBetweenOrderByDate(
        monday, sunday, pageable);

    Page<WorkoutOftheDayResponse> pageResponse = toPageResponse(workoutOftheDayPage, pageable);
    PagedModel<WorkoutOftheDayResponse> body = WorkoutOftheDayResource.toPageResources(assembler,
        pageResponse);

    return ResponseEntity.ok(body);
  }

  @PostMapping
  public ResponseEntity createWOD(@RequestBody @Valid CreateWorkoutOftheDay createWOD) {
    WorkoutOftheDay wod = wodService.createWOD(createWOD);

    EntityModel<WorkoutOftheDayResponse> body = WorkoutOftheDayResource.of(
        WorkoutOftheDayResponse.getValueFrom(wod),
        WorkoutOftheDayResource.getPrefix()+"wod-create");
    return ResponseEntity.created(
            WorkoutOftheDayResource.selfLinkBuilder.slash(wod.getId()).toUri())
        .body(body);
  }

  private Page<WorkoutOftheDayResponse> toPageResponse(Page<WorkoutOftheDay> page,
      Pageable pageable) {
    List<WorkoutOftheDayResponse> contents = page.getContent().stream()
        .map(v -> WorkoutOftheDayResponse.getValueFrom(v)).toList();

    Page<WorkoutOftheDayResponse> pageResponse = new PageImpl(contents, pageable,
        page.getTotalElements());

    return pageResponse;
  }
}
