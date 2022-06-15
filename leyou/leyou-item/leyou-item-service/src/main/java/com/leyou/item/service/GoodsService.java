package com.leyou.item.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.*;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import tk.mybatis.mapper.entity.Example;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lx
 * @description 商品服务类
 * @date 2020/12/26
 */
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据条件分页查询spu
     *
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return PageResult<SpuBo>
     */
    public PageResult<SpuBo> searchSpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        // 1)创建查询对象
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        // 2)添加查询条件
        // 2.1标题使用模糊查询:andLike相当于spu.title like '%"+key+"%'
        if (StringUtils.isNotEmpty(key)) {
            criteria.andLike("title", "%" + key + "%");
        }
        // 2.2上架还是下架:andEqualTo相当于and spu.saleable = '"+saleable+"'
        if (saleable != null) {
            criteria.andEqualTo("saleable", saleable);
        }
        // 2.3
        // 添加分页条件:使用PageHelper.startPage(第几页,每页显示几条)
        PageHelper.startPage(page, rows);

        // 3)执行查询获取spu集合
        List<Spu> listSpus = spuMapper.selectByExample(example);
        // 分页条件查询出来的结果放入PageInfo对象中通过这个对象可以获取总记录数
        PageInfo pageInfo = new PageInfo(listSpus);

        // 4)spu集合转化为spubo集合
        List<SpuBo> listSpuBos = listSpus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            // 查询品牌名称
            spuBo.setBname(brandMapper.selectByPrimaryKey(spuBo.getBrandId()).getName());
            // 查询分类名称
            List<String> categoryNames = categoryService.listNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(categoryNames, ">"));
            return spuBo;
        }).collect(Collectors.toList());

        // 5)返回pageResult<SpuBo>
        return new PageResult<>(pageInfo.getTotal(), listSpuBos);
    }

    /**
     * 新增商品
     *
     * @param spuBo
     * @return
     */
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        // 先新增spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        spuMapper.insertSelective(spuBo);

        // 再新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        spuDetailMapper.insertSelective(spuDetail);

        savaSkuAndStock(spuBo);

        // add by lx 20211004
        sendMsg("insert", spuBo.getId());
    }

    /**
     * 通过RabbitMQ发送消息,避免消息发送失败回滚保存的业务
     * 这里对发送消息的逻辑进行try-catch处理
     */
    private void sendMsg(String type, Long id) {
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存sku&stock公用方法抽取
     *
     * @param spuBo
     */
    private void savaSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            // 新增sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            skuMapper.insertSelective(sku);
            // 新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuId查询SpuDetail
     *
     * @param spuId
     * @return
     */
    public SpuDetail getSpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据spuId查询Sku列表
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public List<Sku> listSkusBySpuId(@PathParam("id") Long spuId) {
        Sku example = new Sku();
        example.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(example);
        skus.forEach(sku -> {
            sku.setStock(this.stockMapper.selectByPrimaryKey(sku.getId()).getStock());
        });

        return skus;
    }

    /**
     * 商品更新
     *
     * @param spuBo
     */
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        // 1、删除stock
        // 根据spuId查询要删除的sku
        Sku example = new Sku();
        example.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(example);
        skus.forEach(sku -> {
            this.stockMapper.deleteByPrimaryKey(sku.getId());
        });

        // 2、刪除sku
        this.skuMapper.delete(example);

        // 3、新增sku&stock
        this.savaSkuAndStock(spuBo);

        // 更新spu
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        spuBo.setSaleable(null);
        spuBo.setValid(null);
        this.spuMapper.updateByPrimaryKeySelective(spuBo);

        // 更新spuDetail
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        sendMsg("update", spuBo.getId());
    }

    public Spu getSpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }
}
