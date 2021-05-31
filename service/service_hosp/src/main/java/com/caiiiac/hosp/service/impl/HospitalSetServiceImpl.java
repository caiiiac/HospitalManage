package com.caiiiac.hosp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiiiac.hosp.mapper.HospitalSetMapper;
import com.caiiiac.hosp.model.hosp.HospitalSet;
import com.caiiiac.hosp.service.HospitalSetService;
import org.springframework.stereotype.Service;

@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
}
