package com.xiaoke1256.orders.core.dto;

import com.xiaoke1256.orders.common.page.QueryResult;

public class ProductWithStorageQueryResult extends QueryResult<ProductWithStorage> {
	
	

	public ProductWithStorageQueryResult() {
		super();
	}

	public ProductWithStorageQueryResult(int pageNo, int pageSize, int totalCount) {
		super(pageNo, pageSize, totalCount);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
