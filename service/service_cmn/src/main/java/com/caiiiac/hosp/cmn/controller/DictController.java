package com.caiiiac.hosp.cmn.controller;

import com.caiiiac.hosp.cmn.service.DictService;
import com.caiiiac.hosp.model.cmn.Dict;
import com.caiiiac.hosp.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(tags = "数据字典管理")
@RestController
@CrossOrigin
@RequestMapping("admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "根据数据 id 查询子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    /**
     * 导出数据字典接口
     * @param response
     * @return
     */
    @GetMapping("exportData")
    public void exportDict(HttpServletResponse response) {
        dictService.exportDictData(response);
    }

    /**
     * 导入数据字典
     * @param file
     * @return
     */
    @PostMapping("importData")
    public Result importDict(MultipartFile file) {
        dictService.importDictDate(file);
        return Result.ok();
    }

    /**
     * 根据 code 和 value 查询
     * @param dictCode
     * @param value
     * @return
     */
    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode, @PathVariable String value) {
        String dictName = dictService.getDictName(dictCode, value);
        return dictName;
    }

    /**
     * 根据 value 查询
     * @param value
     * @return
     */
    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value) {
        String dictName = dictService.getDictName("", value);
        return dictName;
    }

    @ApiOperation(value = "根据 dictCode 获取下级节点")
    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findByDict(dictCode);
        return Result.ok(list);
    }
}
