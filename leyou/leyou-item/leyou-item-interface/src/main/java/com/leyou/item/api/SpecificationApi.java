package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
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
@RequestMapping("spec")
public interface SpecificationApi {
    /**
     * 根据组id查询该组下面对应的规格参数
     *
     * @param gid
     * @return List<SpecParam>
     */
    @GetMapping("params")
    public List<SpecParam> listSpecParams(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "generic", required = false) Boolean generic,
            @RequestParam(value = "searching", required = false) Boolean searching
    );

    @GetMapping("group/param/{cid}")
    public List<SpecGroup> listSpecGroupWithParam(@PathVariable("cid")Long cid);
}
