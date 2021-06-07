package com.caiiiac.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.caiiiac.hosp.cmn.client.DictFeignClient;
import com.caiiiac.hosp.model.hosp.Hospital;
import com.caiiiac.hosp.repository.HospitalRepository;
import com.caiiiac.hosp.service.HospitalService;
import com.caiiiac.hosp.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        //把参数 map 集合转换对象 Hospital
        String mapString = JSONObject.toJSONString(paramMap);
        Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

        // 判断是否存在数据
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hoscode);

        // 存在
        if (hospitalExist != null) {
            hospitalExist.setStatus(hospital.getStatus());
            hospitalExist.setUpdateTime(new Date());
            hospitalExist.setIsDeleted(0);
            hospitalRepository.save(hospitalExist);
        } else {
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {

        Pageable pageable = PageRequest.of(page - 1, limit);
        // 创建条件匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);

        // 转换对象
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        Example<Hospital> example = Example.of(hospital, matcher);

        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);

        pages.getContent().stream().forEach(item -> {
            String hostypeString = dictFeignClient.getName("Hostype", hospital.getHostype());
            // 查询省市区
            String provinceString = dictFeignClient.getName(hospital.getProvinceCode());
            String cityString = dictFeignClient.getName(hospital.getCityCode());
            String districtString = dictFeignClient.getName(hospital.getDistrictCode());

            item.getParam().put("fullAddress", provinceString + cityString + districtString);
            item.getParam().put("hostypeString", hostypeString);
        });

        return pages;
    }
}
