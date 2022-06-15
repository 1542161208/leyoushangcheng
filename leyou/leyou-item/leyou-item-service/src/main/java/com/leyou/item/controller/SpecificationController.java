package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @description 规格参数接口
 * @author lx
 * @date 2020/12/13
 */
@Controller
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;

    /**
     * 根据分类id查询参数组
     *
     * @param cid
     * @return List<SpecGroup>
     */
    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> listSpecGroups(@PathVariable("cid") Long cid) {
        List<SpecGroup> listSpecGroups = specificationService.listSpecGroups(cid);
        if (CollectionUtils.isEmpty(listSpecGroups)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(listSpecGroups);
    }

    /**
     * 根据组id查询该组下面对应的规格参数
     *
     * @param gid
     * @return List<SpecParam>
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> listSpecParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching
    ) {
        List<SpecParam> listSpecParams = specificationService.listSpecParams(gid, cid, generic, searching);
        if (CollectionUtils.isEmpty(listSpecParams)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(listSpecParams);
    }

    @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> listSpecGroupWithParam(@PathVariable("cid")Long cid){
        List<SpecGroup> specGroups = this.specificationService.listSpecGroupWithParam(cid);
        if (CollectionUtils.isEmpty(specGroups)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(specGroups);
    }
}
