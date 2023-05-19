package com.kihong.health.web.controller;

import com.kihong.health.persistence.dto.record.CreateRecord;
import com.kihong.health.persistence.dto.record.RecordResponse;
import com.kihong.health.persistence.dto.record.UpdateRecord;
import com.kihong.health.persistence.dto.workoutOftheDay.WorkoutOftheDayResponse;
import com.kihong.health.persistence.model.Record;
import com.kihong.health.persistence.model.WorkoutOftheDay;
import com.kihong.health.persistence.repository.RecordRepository;
import com.kihong.health.persistence.service.record.RecordService;
import com.kihong.health.web.resource.RecordResource;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/record")
public class RecordController {

  final RecordRepository recordRepository;
  final RecordService recordService;

  @GetMapping("/{id}")
  public ResponseEntity getRecord(@PathVariable Long id) {
    var record = recordService.getRecord(id);
    return ResponseEntity.ok(RecordResource.of(RecordResponse.getValueFrom(record), RecordResource.getPrefix() + "record-get"));
  }

  @GetMapping
  public ResponseEntity listRecord(
      Pageable pageable, PagedResourcesAssembler assembler,
      @RequestParam(value = "search", required = false) String search,
      @RequestParam(value = "id", required = false) Long userId
  ) {
    Page<Record> recordPage = recordService.listRecord(search, userId, pageable);

    Page<RecordResponse> recordResponsePage = toPageResponse(recordPage, pageable);
    return ResponseEntity.ok(RecordResource.toPageResources(assembler, recordResponsePage));
  }

  @PostMapping
  public ResponseEntity createRecord(
      @RequestBody @Valid CreateRecord cr
  ) {
    Record createdRecord = recordService.createRecord(cr);

    return ResponseEntity.created(RecordResource.selfLinkBuilder.slash(createdRecord.getId()).toUri())
        .body(RecordResource.of(RecordResponse.getValueFrom(createdRecord), RecordResource.getPrefix()+"record-create"));
  }

  @PutMapping("/{id}")
  public ResponseEntity updateRecord(
      @PathVariable Long id,
      @RequestBody UpdateRecord ur
  ) {
    Record updatedRecord = recordService.updateRecord(ur);


    return ResponseEntity.ok(RecordResource.of(RecordResponse.getValueFrom(updatedRecord),RecordResource.getPrefix()+"record-put"));
  }

  private Page<RecordResponse> toPageResponse(Page<Record> page,
      Pageable pageable) {
    List<RecordResponse> contents = page.getContent().stream()
        .map(v -> RecordResponse.getValueFrom(v)).toList();

    Page<RecordResponse> pageResponse = new PageImpl(contents, pageable,
        page.getTotalElements());

    return pageResponse;
  }
}
