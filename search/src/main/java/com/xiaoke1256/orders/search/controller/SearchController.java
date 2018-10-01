package com.xiaoke1256.orders.search.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction.Modifier;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.search.common.ErrMsg;
import com.xiaoke1256.orders.search.vo.Product;
import com.xiaoke1256.orders.search.vo.SearchCondition;
import com.xiaoke1256.orders.search.vo.SearchResult;

@RestController
@RequestMapping("/")
public class SearchController {

	@Autowired
	private Client client;

	@RequestMapping(value="/search",method= {RequestMethod.POST})
	@ResponseBody
	public SearchResult search(@RequestBody SearchCondition condition){
		try {
			validate(condition);
			
			BoolQueryBuilder qb = new BoolQueryBuilder();
			if(!StringUtils.isEmpty(condition.getSearchName()))
				qb.must(QueryBuilders.matchQuery("_all", condition.getSearchName()).boost(2.0f));
			
			qb.should(toUserScore(condition.getUserId()));
			
			//TODO order by.
			
			return searchFunction(qb,condition.getPageNo(),condition.getPageSize());
		}catch(Exception e) {
			e.printStackTrace();
			SearchResult result = new SearchResult();
			ErrMsg error = new ErrMsg("error",e.getMessage());
			result.setError(error );
			return result;
		}
	}
	
	 private SearchResult searchFunction(QueryBuilder queryBuilder,int pageNo,int pageSize) {
        SearchResponse response = client.prepareSearch("orders")
        		.setTypes("product")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setScroll(new TimeValue(60000))
                .setQuery(queryBuilder)
                .setFrom((pageNo-1)*pageSize)
                .setSize(pageSize).execute().actionGet();
        
        response = client.prepareSearchScroll(response.getScrollId())
            .setScroll(new TimeValue(60000)).execute().actionGet();
        
        int totalCount = (int) response.getHits().getTotalHits();
        List<Product> rsultList = new ArrayList<Product>();
        for (SearchHit hit : response.getHits()) {
        	Map<String, SearchHitField> fieldMaps = hit.getFields();
        	String code = fieldMaps.get("code").getValue();
        	String name = fieldMaps.get("name").getValue();
        	Double price = fieldMaps.get("price").getValue();
        	String storeNo = fieldMaps.get("storeNo").getValue();
        	String storeName = fieldMaps.get("storeName").getValue();
        	Date updTime = fieldMaps.get("updTime").getValue();
        	String typeId = fieldMaps.get("typeId").getValue();
        	String typeName = fieldMaps.get("typeName").getValue();
        	Product product = new Product();
        	product.setCode(code);
        	product.setName(name);
        	product.setPrice(price);
        	product.setStoreNo(storeNo);
        	product.setStoreName(storeName);
        	product.setUpdTime(updTime);
        	product.setTypeId(typeId);
        	product.setTypeName(typeName);
        	rsultList.add(product);
        }
        return new SearchResult(pageNo,pageSize,totalCount,rsultList);
    }

	private QueryBuilder toUserScore(String userId) {
		String[] userIds = null;
		if(StringUtils.isEmpty(userId))
			userIds = new String[] {"0"};
		else
			userIds = new String[] {"0",userId};
		

		QueryBuilder hasChind = QueryBuilders.hasChildQuery("product_user_map", 
				QueryBuilders.functionScoreQuery(
						new BoolQueryBuilder()
							.must(QueryBuilders.termsQuery("userId", userIds))
							.must(QueryBuilders.rangeQuery("upd_time").from("now-1M")),
						ScoreFunctionBuilders
							.fieldValueFactorFunction("score")
							.modifier(Modifier.LN1P))
				.boostMode(CombineFunction.REPLACE), 
			ScoreMode.Max);
		return hasChind;
	}

	private void validate(SearchCondition condition) {
		int pageNo = condition.getPageNo();
		int pageSize = condition.getPageSize();
		Map<String, Boolean> orderbys = condition.getOrderBy();
		if(pageNo<=0) {
			pageNo = 1;
		}
		if(!Arrays.asList(10,20,50,100).contains(Integer.valueOf(pageSize)) ) {
			pageSize = 10;
		}
		if(orderbys!=null&&orderbys.size()>1) {//目前只支持一个排序条件
			throw new RuntimeException("Only one collumn can be sorted.");
		}
		if(orderbys!=null) {
			//目前排序条件的字段只允许是 price 
			for(String propertyName:orderbys.keySet()) {
				if(!"price".equals(propertyName)) {
					throw new RuntimeException("Only \"price\" collumn can be used as sort collumn.");
				}
			}
		}
	}
	
	@PreDestroy
	public void distory() {
		client.close();
	}
}
