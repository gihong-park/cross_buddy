package com.kihong.health.web.controller;

import com.kihong.health.persistence.dto.record.CreateRecord;
import com.kihong.health.persistence.dto.record.RecordResponse;
import com.kihong.health.persistence.dto.record.UpdateRecord;
import com.kihong.health.persistence.model.Record;
import com.kihong.health.persistence.repository.RecordRepository;
import com.kihong.health.persistence.service.record.RecordService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
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

  @GetMapping
  public ResponseEntity listRecord(
      Pageable pageable, PagedResourcesAssembler assembler,
      @RequestParam(value="search", required = false) String search,
      @RequestParam(value="id", required = false) Long userId
      ) {
    Page<Record> recordPage = recordService.listRecord(search, userId, pageable);

    List<RecordResponse> contents = recordPage.getContent().stream().map(record -> RecordResponse.getValueFrom(record)).toList();

    return ResponseEntity.ok(new PageImpl(contents, pageable, recordPage.getTotalElements()));
  }

  @PostMapping
  public ResponseEntity createRecord(
      @RequestBody @Valid CreateRecord cr
  ) {
    Record createdRecord= recordService.createRecord(cr);

    return ResponseEntity.created(URI.create("/api/v1/record" + createdRecord.getId())).body(RecordResponse.getValueFrom(createdRecord));
  }

  @PutMapping("/{id}")
  public ResponseEntity updateRecord(
      @PathVariable Long id,
      @RequestBody UpdateRecord ur
  ) {
    Record updatedRecord = recordService.updateRecord(ur);

    return ResponseEntity.ok(RecordResponse.getValueFrom(updatedRecord));
  }
}
