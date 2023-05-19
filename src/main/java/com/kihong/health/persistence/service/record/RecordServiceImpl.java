package com.kihong.health.persistence.service.record;

import com.kihong.health.persistence.dto.record.CreateRecord;
import com.kihong.health.persistence.dto.record.UpdateRecord;
import com.kihong.health.persistence.model.MovementRecord;
import com.kihong.health.persistence.model.Record;
import com.kihong.health.persistence.model.User;
import com.kihong.health.persistence.model.User.Role;
import com.kihong.health.persistence.model.WorkoutOftheDay;
import com.kihong.health.persistence.repository.MovementRecordRepository;
import com.kihong.health.persistence.repository.RecordRepository;
import com.kihong.health.persistence.repository.WorkoutOftheDayRepository;
import com.kihong.health.web.exception.ErrorCode;
import com.kihong.health.web.exception.HttpException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {

  private final ModelMapper modelMapper;
  private final RecordRepository recordRepository;
  private final MovementRecordRepository movementRecordRepository;
  private final WorkoutOftheDayRepository workoutOftheDayRepository;

  @Override
  @Transactional
  public Page<Record> listRecord(String search, Long userId, Pageable pageable) {
    Optional<User> user = getUserContext();
    Page<Record> records = Page.empty();

    if (user.get().getRole() == Role.USER) {
      if (user.get().getId() != userId) {
        throw new HttpException(ErrorCode.USER_FORBIDDEN, "User Forbidden");
      }
    }
    if (user.get().getAuthorities().contains(new SimpleGrantedAuthority(Role.ADMIN.getRoleName()))
        || user.get().getAuthorities()
        .contains(new SimpleGrantedAuthority(Role.MASTER.getRoleName()))) {
      if (search == null && userId == null) {
        records = recordRepository.findAll(pageable);
      } else if (search == null && userId != null) {
        records = recordRepository.findByUser_id(userId, pageable);
      } else if (search != null && userId == null) {
        records = recordRepository.findRecordsByMovementRecordName(search, pageable);
      } else {
        records = recordRepository.findRecordsByMovementRecordNameByUser_id(search, userId,
            pageable);
      }
    } else {
      if (search == null) {
        records = recordRepository.findByUser_id(user.get().getId(), pageable);
      } else {
        records = recordRepository.findRecordsByMovementRecordNameByUser_id(search,
            user.get().getId(), pageable);
      }
    }

    return records;
  }

  @Override
  @Transactional
  public Record createRecord(CreateRecord createRecord) {
    Optional<User> user = getUserContext();
    Record record = Record.getValueFrom(createRecord);
    if (!user.isEmpty()) {
      record.setUser(user.get());
    }
    if (createRecord.getWodId() != null) {
      Optional<WorkoutOftheDay> wod = workoutOftheDayRepository.findById(createRecord.getWodId());
      record.setWod(wod.get());
    }
    final Record result = recordRepository.save(record);
    List<MovementRecord> mvList = createRecord.getMovementRecords().stream()
        .map(cmr -> modelMapper.map(cmr, MovementRecord.class)).toList();
    mvList.stream().forEach(mr -> mr.setRecord(result));
    result.setMovementRecords(movementRecordRepository.saveAll(mvList));

    return result;
  }

  @Override
  @Transactional
  public Record updateRecord(UpdateRecord updateRecord) {
    Optional<User> user = getUserContext();
    Optional<Record> record = recordRepository.findById(updateRecord.getId());
    Optional<WorkoutOftheDay> wod = Optional.empty();

    if (updateRecord.getWodId() != null) {
      wod = workoutOftheDayRepository.findById(updateRecord.getWodId());
      if (wod.isEmpty()) {
        throw new HttpException(ErrorCode.WOD_NOT_FOUND, "");
      }
    }
    if (record.isEmpty()) {
      throw new HttpException(ErrorCode.RECORD_NOT_FOUND, "");
    }
    if (user.get().getRole().getRoleName() == "ROLE_USER" && user.get().getId() != record.get()
        .getUser().getId()) {
      throw new HttpException(ErrorCode.USER_FORBIDDEN, "");
    }
    if (wod.isPresent()) {
      record.get().setWod(wod.get());
    }
    Record result = record.get();
    List<MovementRecord> mrs = updateRecord.getMovementRecords().stream()
        .map(mr -> MovementRecord.getValueFrom(mr)).map(mr -> movementRecordRepository.save(mr))
        .toList();

    result.setDescription(updateRecord.getDescription());
    result.setNote(updateRecord.getNote());
    result.setDate(updateRecord.getDate());

    result = recordRepository.save(result);
    result.setMovementRecords(mrs);
    return result;
  }

  @Override
  public Record getRecord(Long id) {
    Optional<User> user = getUserContext();
    var record = recordRepository.findById(id);
    if(record.isEmpty()) {
      throw new HttpException(ErrorCode.RECORD_NOT_FOUND, "");
    }
    if(!user.isEmpty() && user.get().getRole() == Role.USER && user.get().getId() != record.get().getUser().getId() ) {
      throw new HttpException(ErrorCode.USER_UNAUTHORIZED, "");
    }

    return recordRepository.findById(id).get();
  }

}
