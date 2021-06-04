package com.caiiiac.hosp.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiiiac.hosp.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse response);

    void importDictDate(MultipartFile file);

    String getDictName(String dictCode, String value);
}
