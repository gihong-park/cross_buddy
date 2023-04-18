package com.kihong.health.web.controller;

import com.kihong.health.persistence.dto.workoutOftheDay.CreateWorkoutOftheDay;
import com.kihong.health.persistence.dto.workoutOftheDay.WorkoutOftheDayResponse;
import com.kihong.health.persistence.model.WorkoutOftheDay;
import com.kihong.health.persistence.repository.WorkoutOftheDayRepository;
import com.kihong.health.persistence.service.workoutOftheDay.WorkoutOftheDayService;
import com.kihong.health.persistence.service.workoutOftheDay.WorkoutOftheDayServiceImpl;
import com.kihong.health.util.DateTemper;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
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

  @GetMapping
  public ResponseEntity listWOD(Pageable pageable, PagedResourcesAssembler assembler, @RequestParam(name = "date", required = false, defaultValue = "#{T(java.time.LocalDate).now()}" )@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    LocalDate monday = DateTemper.getFirstDayOfWeek(date);
    LocalDate sunday = DateTemper.getLastDayOfWeek(date);
    Page<WorkoutOftheDay> workoutOftheDayList = workoutOftheDayRepository.findByDateBetweenOrderByDate(monday, sunday, pageable);

    return ResponseEntity.ok(workoutOftheDayList);
  }
  @PostMapping
  public ResponseEntity createWOD(@RequestBody @Valid CreateWorkoutOftheDay createWOD) {
    WorkoutOftheDay wod = wodService.createWOD(createWOD);

    return ResponseEntity.created(URI.create("/api/v1/wod" + wod.getId())).body(WorkoutOftheDayResponse.getValueFrom(wod));
  }
}
