package com.caiiiac.hosp.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-cmn")
public interface DictFeignClient {
    // 根据 dictCode 和 value 查询
    @GetMapping("admin/cmn/dict/getName/{dictCode}/{value}")
    String getName(@PathVariable("dictCode") String dictCode, @PathVariable("value") String value);

    // 根据 value 查询
    @GetMapping("admin/cmn/dict/getName/{value}")
    String getName(@PathVariable("value") String value);

}
