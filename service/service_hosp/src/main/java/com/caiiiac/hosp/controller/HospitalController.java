package com.caiiiac.hosp.controller;

import com.caiiiac.hosp.model.hosp.Hospital;
import com.caiiiac.hosp.result.Result;
import com.caiiiac.hosp.service.HospitalService;
import com.caiiiac.hosp.vo.hosp.HospitalQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 医院列表
     * @param page
     * @param limit
     * @param hospitalQueryVo
     * @return
     */
    @GetMapping("list/{page}/{limit}")
    public Result listHostp(@PathVariable Integer page, @PathVariable Integer limit, HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> pageModel = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(pageModel);
    }
}
