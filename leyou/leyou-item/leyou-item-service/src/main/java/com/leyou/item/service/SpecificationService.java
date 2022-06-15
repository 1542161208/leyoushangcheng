package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 规格参数相关相关服务类
 * @author lx
 * @date 2020/12/13
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类id查询参数组
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> listSpecGroups(Long cid) {
        SpecGroup example = new SpecGroup();
        example.setCid(cid);
        List<SpecGroup> listSpecGroups = specGroupMapper.select(example);
        return listSpecGroups;
    }

    /**
     * 根据组id查询该组下面对应的规格参数
     *
     * @param gid
     * @return
     */
    public List<SpecParam> listSpecParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam example = new SpecParam();
        example.setGroupId(gid);
        example.setCid(cid);
        example.setGeneric(generic);
        example.setSearching(searching);
        List<SpecParam> listSpecParams = specParamMapper.select(example);
        return listSpecParams;
    }

    public List<SpecGroup> listSpecGroupWithParam(Long cid) {
        List<SpecGroup> specGroups = this.listSpecGroups(cid);
        if (CollectionUtils.isEmpty(specGroups)) {
            return new ArrayList<>(0);
        }

        specGroups.forEach(g -> {
            List<SpecParam> specParams = this.listSpecParams(g.getId(), null, null, null);
            g.setParams(specParams);
        });

        return specGroups;
    }
}
