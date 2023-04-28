package com.kihong.health.persistence.service.workoutOftheDay;

import com.kihong.health.persistence.dto.workoutOftheDay.CreateWorkoutOftheDay;
import com.kihong.health.persistence.model.WorkoutOftheDay;
import com.kihong.health.persistence.service.base.BaseService;

public interface WorkoutOftheDayService extends BaseService {

  WorkoutOftheDay createWOD(CreateWorkoutOftheDay cwod);
}
