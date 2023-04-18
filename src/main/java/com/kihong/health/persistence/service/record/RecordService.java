package com.kihong.health.persistence.service.record;

import com.kihong.health.persistence.dto.record.CreateRecord;
import com.kihong.health.persistence.dto.record.UpdateRecord;
import com.kihong.health.persistence.model.Record;
import com.kihong.health.persistence.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;

public interface RecordService extends BaseService {

  @Transactional
  Page<Record> listRecord(String search, Long userId, Pageable pageable);

  public Record createRecord(CreateRecord createRecord);

  @Transactional
  Record updateRecord(UpdateRecord updateRecord);

  public Record getRecord(Long id);
}
