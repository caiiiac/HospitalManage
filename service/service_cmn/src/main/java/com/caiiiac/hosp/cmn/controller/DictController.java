package com.caiiiac.hosp.cmn.controller;

import com.caiiiac.hosp.cmn.service.DictService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "数据字典管理")
@RestController
@RequestMapping("admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;
}
