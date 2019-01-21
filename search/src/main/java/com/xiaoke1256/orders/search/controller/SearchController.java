package com.xiaoke1256.orders.search.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction.Modifier;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiaoke1256.orders.common.ErrMsg;
import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.common.page.QueryResult;
import com.xiaoke1256.orders.search.vo.Product;
import com.xiaoke1256.orders.search.vo.SearchCondition;

@Controller
@RequestMapping("/")
public class SearchController {
	
	@Autowired
	private Client client;
	

	@RequestMapping(value="/search",method= {RequestMethod.POST})
	@ResponseBody
	public RespMsg search(@RequestBody SearchCondition condition){
		try {
			validate(condition);
			
			BoolQueryBuilder qb = new BoolQueryBuilder();
			if(!StringUtils.isEmpty(condition.getSearchName()))
				qb.must(QueryBuilders.multiMatchQuery(condition.getSearchName()).field("name", 2.0f).field("store_name").field("type_name").boost(1.0f));
			else
				qb.must(QueryBuilders.matchAllQuery());
			
			qb.should(toUserScore(condition.getUserId()));
			
			//TODO order by.
			
			return new RespMsg("0","success!",searchFunction(qb,condition.getPageNo(),condition.getPageSize()));
		}catch(Exception e) {
			e.printStackTrace();
			ErrMsg error = new ErrMsg("error",e.getMessage());
			return error;
		}
	}
	
	 private QueryResult<Product> searchFunction(QueryBuilder queryBuilder,int pageNo,int pageSize) throws Exception {
		 
		 HighlightBuilder highlightBuilder = new HighlightBuilder().field("*").requireFieldMatch(false);
		 highlightBuilder.preTags("<em>");
		 highlightBuilder.postTags("</em>");
		 
         SearchResponse response = client.prepareSearch("prod")
        		.setTypes("product")
        		.setFetchSource(new String[] {"code","name","price","store_no","store_name","upd_time","type_id","type_name" }, null)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .highlighter(highlightBuilder)
                .setFrom((pageNo-1)*pageSize)
                .setSize(pageSize).get();
        
        int totalCount = (int) response.getHits().getTotalHits();
        List<Product> rsultList = new ArrayList<Product>();
        for (SearchHit hit : response.getHits().getHits()) {
        	Map<String,Object> values = hit.getSource();
        	Map<String, HighlightField> hightlingFields = hit.highlightFields();
        	String code = (String)values.get("code");
        	String name = getValueWithHighlight("name", hightlingFields,values);
        	Double price = ((Number)values.get("price")).doubleValue();
        	String storeNo = (String)values.get("store_no");
        	String storeName = getValueWithHighlight("store_name", hightlingFields,values);
        	Date updTime = null;
        	if(values.get("upd_time") instanceof String)
        		updTime = DateUtils.parseDate((String)values.get("upd_time"), "yyyy-MM-dd HH:mm:ss");
        	if(values.get("upd_time") instanceof Long)
        		updTime = new Date((Long)values.get("upd_time"));
        	if(values.get("upd_time") instanceof Date)
        		updTime = (Date)values.get("upd_time");
        	String typeId = (String)values.get("type_id");
        	String typeName = getValueWithHighlight("type_name", hightlingFields,values);
        	double score = hit.getScore();
        	Product product = new Product();
        	product.setCode(code);
        	product.setName(name);
        	product.setPrice(price);
        	product.setStoreNo(storeNo);
        	product.setStoreName(storeName);
        	product.setUpdTime(updTime);
        	product.setTypeId(typeId);
        	product.setTypeName(typeName);
        	product.setScore(score);
        	rsultList.add(product);
        }
        return new QueryResult<Product>(pageNo,pageSize,totalCount,rsultList);
    }

	private String getValueWithHighlight(String queryName, Map<String, HighlightField> hightlingFields,Map<String, Object> values) {
		String name;
		if(hightlingFields!=null&&hightlingFields.get(queryName)!=null&&hightlingFields.get(queryName).fragments()!=null&&hightlingFields.get(queryName).fragments().length>0) {
			StringBuilder sb = new StringBuilder();
			for(Text fragment:hightlingFields.get(queryName).fragments()) {
				sb.append(fragment.string());
			}
			name = sb.toString();
		}else
			name = (String)values.get(queryName);
		return name;
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
							.must(QueryBuilders.termsQuery("user_id", userIds))
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
