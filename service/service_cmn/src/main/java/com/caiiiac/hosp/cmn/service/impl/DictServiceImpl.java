package com.caiiiac.hosp.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiiiac.hosp.cmn.listener.DictListener;
import com.caiiiac.hosp.cmn.mapper.DictMapper;
import com.caiiiac.hosp.cmn.service.DictService;
import com.caiiiac.hosp.model.cmn.Dict;
import com.caiiiac.hosp.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * 根据 id 查询子列表
     * @param id
     * @return
     */
    @Cacheable(value = "dict", keyGenerator="keyGenerator")
    @Override
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper();
        wrapper.eq("parent_id", id);
        List<Dict> dicts = baseMapper.selectList(wrapper);
        // 向 list 集合每个 dict 对象中设置 hasChildren
        for (Dict dict: dicts) {
            Long dictId = dict.getId();
            boolean isChild = this.isChildren(dictId);
            dict.setHasChildren(isChild);
        }
        return dicts;
    }

    /**
     * 导出数据字典
     * @param response
     */
    @Override
    public void exportDictData(HttpServletResponse response) {
        //设置下载信息
        response.setContentType("applicaion/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = "dict";
        response.setHeader("Content-disposition", "attachment;filename" + fileName + ".xlsx");
        //查询数据库
        List<Dict> dictList = baseMapper.selectList(null);
        List<DictEeVo> dictEeVoList = dictList.stream().map(item -> {
            DictEeVo vo = new DictEeVo();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());

        // 调用方法进行写操作
        try {
            EasyExcel.write(response.getOutputStream(), DictEeVo.class)
                    .sheet("dict")
                    .doWrite(dictEeVoList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 导入数据字典
    @CacheEvict(value = "dict", allEntries = true)
    @Override
    public void importDictDate(MultipartFile file) {

        try {
            EasyExcel.read(file.getInputStream(), DictEeVo.class, new DictListener(baseMapper)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDictName(String dictCode, String value) {
        if (StringUtils.isEmpty(dictCode)) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {
            // 根据 dictCode 查询 dict 对象,得到 dict 的 id 值
            Dict codeDict = this.getDictByDictCode(dictCode);
            Long parent_id = codeDict.getId();
            // 根据 parent_id 和 value 进行查询
            Dict finalDict = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", parent_id)
                    .eq("value", value));
            return finalDict.getName();
        }
    }

    @Override
    public List<Dict> findByDict(String dictCode) {
        Dict dict = this.getDictByDictCode(dictCode);
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;
    }

    private Dict getDictByDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        return dict;
    }

    // 判断 id 下面是否有子节点
    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        int count = baseMapper.selectCount(wrapper);
        return count > 0;
    }
}
