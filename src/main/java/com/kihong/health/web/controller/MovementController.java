package com.kihong.health.web.controller;

import com.kihong.health.persistence.model.Movement;
import com.kihong.health.persistence.repository.MovementRepository;
import com.kihong.health.web.resource.MovementResource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/movement")
public class MovementController {

  final MovementRepository movementRepository;

  @GetMapping
  public ResponseEntity listMovement(
      Pageable pageable, PagedResourcesAssembler<Movement> assembler) {
    Page<Movement> movementPage = movementRepository.findAll(pageable);

    return ResponseEntity.ok(MovementResource.toPageResources(assembler, movementPage));
  }
}
