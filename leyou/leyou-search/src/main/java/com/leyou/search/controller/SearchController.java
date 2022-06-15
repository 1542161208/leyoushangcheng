package com.leyou.search.controller;

import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author lx
 * @description 搜索接口
 * @date 2021/05/04
 */
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 前台搜索功能
     * @param searchRequest
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<SearchResult> searchGoodsByPage(@RequestBody SearchRequest searchRequest){
        SearchResult goodsPageResult = this.searchService.searchGoodsByPage(searchRequest);
        if(goodsPageResult==null || CollectionUtils.isEmpty(goodsPageResult.getItems())){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(goodsPageResult);
    }
}
