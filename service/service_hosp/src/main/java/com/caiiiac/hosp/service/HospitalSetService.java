package com.caiiiac.hosp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiiiac.hosp.model.hosp.HospitalSet;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);
}
