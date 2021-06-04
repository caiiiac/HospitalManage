package com.caiiiac.hosp.service;

import com.caiiiac.hosp.model.hosp.Schedule;
import com.caiiiac.hosp.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {
    void save(Map<String, Object> paramMap);

    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void remove(String hoscode, String hosSchedultId);
}
