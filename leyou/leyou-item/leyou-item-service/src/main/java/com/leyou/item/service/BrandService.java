package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author lx
 * @date 2020/10/26
 */
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据查询条件分页并排序查询商品的品牌
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return ResponseEntity<PageResult < Brand>>
     */
    public PageResult<Brand> searchBrands(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 初始化example对象
        Example example = new Example(Brand.class);
        // Criteria:标准，准则
        Example.Criteria criteria = example.createCriteria();

        // key:根据商品名称name模糊查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andLike("name", "%" + key + "%").orEqualTo("letter", key);
        }

        // page|rows:添加分页条件
        PageHelper.startPage(page, rows);

        // sortBy|desc:添加排序条件
        // Clause:条款
        example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));

        // 查询
        List<Brand> listBrands = brandMapper.selectByExample(example);

        // 包装成PageInfo对象
        PageInfo<Brand> pageInfo = new PageInfo<>(listBrands);

        // 包装成分页对象并返回
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        // insertSelective:有参数就会拼接进去sql没有参数不会插入字段效率高
        // insert：表的所有字段都会插入即时没有参数插入
        // 先新增brand
         /*Boolean flag = this.brandMapper.insertSelective(brand)==1;
         if(flag){
             // 注意:这里使用箭头函数进行遍历操作
             cids.forEach(cid->{
                 this.brandMapper.insertBrandAndCategory(cid,brand.getId());
             });
         }*/

        // 加了事务@Transactional之后的写法
        this.brandMapper.insertSelective(brand);
        cids.forEach(cid -> {
            this.brandMapper.insertBrandAndCategory(cid, brand.getId());
        });
    }

    /**
     * 根据商品分类查询对应的品牌
     * @param cid
     * @return
     */
    public List<Brand> listBrandsByCid(Long cid) {
        return brandMapper.selectBrandByCid(cid);
    }

    /**
     * 根据品牌id查询品牌
     * @param bid
     * @return
     */
    public Brand getBrandById(Long bid) {
        return brandMapper.selectByPrimaryKey(bid);
    }
}
