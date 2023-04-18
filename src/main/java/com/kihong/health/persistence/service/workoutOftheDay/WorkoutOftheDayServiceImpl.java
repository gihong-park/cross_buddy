package com.kihong.health.persistence.service.workoutOftheDay;

import com.kihong.health.persistence.dto.workoutOftheDay.CreateWorkoutOftheDay;
import com.kihong.health.persistence.model.MovementRecord;
import com.kihong.health.persistence.model.WorkoutOftheDay;
import com.kihong.health.persistence.repository.MovementRecordRepository;
import com.kihong.health.persistence.repository.WorkoutOftheDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutOftheDayServiceImpl implements WorkoutOftheDayService{

  final WorkoutOftheDayRepository wodRepository;
  final MovementRecordRepository mrRepository;

  @Override
  public WorkoutOftheDay createWOD(CreateWorkoutOftheDay cwod) {
    WorkoutOftheDay wod = WorkoutOftheDay.getValueFrom(cwod);
    final WorkoutOftheDay result = wodRepository.saveAndFlush(wod);
    result.setMovementRecords(
        cwod.getCmrList().stream().map((cmr) -> MovementRecord.getValueFrom(cmr)).map((mr) -> {
          mr.setWod(result);
          return mr;
        }).map((mr) -> mrRepository.saveAndFlush(mr)).toList());

    return result;
  }

}
