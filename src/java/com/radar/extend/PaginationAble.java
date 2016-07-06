package com.radar.extend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
public class PaginationAble implements Serializable {

	private static final long serialVersionUID = 5390242392714690503L;
	
	public static final  int DEFAULT_PAGE_SIZE=15;
	/**
	 * 每一页的显示条数
	 */
	private int pageSize;
	
	/**
	 * 总的页数
	 */
	private int totalPages;
	
	/**
	 * 查询的数据总条数
	 */
	private int totalResults;
	
	/**
	 * 当前页
	 */
	private int pageNo;
	
	/**
	 * 查询参数
	 */
	private Map<String, Object> whereParameters = new HashMap<String, Object>();
	
	private List<?> results = new ArrayList<Object>();

	private String paramsFormat = "";

	public PaginationAble() {
		this(1);
	}

	public PaginationAble(int pageNo) {
		this(pageNo,200);
	}

	public PaginationAble(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		if (pageSize > 0) {
			this.pageSize = pageSize;
		}else{
			this.pageSize = DEFAULT_PAGE_SIZE;
		}
		// 错误处理
		if (this.pageNo < 1) {
			this.pageNo = 1;
		}
	}

	public int getTotalPages() {
		return this.totalPages;
	}
	
	// 计算从第几条获取数据
	public int getCurrentResult() {
		return (pageNo - 1) * pageSize;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
		 //计算总页数
		if (this.totalResults > 0) {
			this.totalPages = (int)Math.ceil(this.totalResults / (this.pageSize*1.0));
		}
		pageNo=(pageNo <= 0 ? 1 :getTotalPages() <= 0 ?1 : pageNo > this.getTotalPages() ? this.getTotalPages() : pageNo);
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		
		this.pageNo = (pageNo <= 0 ? 1 : pageNo);
	}

	public Map<String, Object> getWhereParameters() {
		return whereParameters;
	}

	public void setWhereParameters(Map<String, Object> whereParameters) {
		if (whereParameters != null) {
			this.whereParameters = whereParameters;
			StringBuffer sbf = new StringBuffer();
			for (Entry<String, Object> entry : whereParameters.entrySet()) {
				sbf.append(entry.getKey()).append("=").append(entry.getValue())
						.append("&");
			}
			this.paramsFormat = sbf.toString();
			if (paramsFormat.endsWith("&")) {
				paramsFormat = paramsFormat.substring(0,
						paramsFormat.length() - 1);
			}
		} else {
			this.whereParameters = new HashMap<String, Object>();
			paramsFormat = "";
		}
	}

	public String getParamsFormat() {
		return paramsFormat;
	}

	public List<?> getResults() {
		return results;
	}

	public void setResults(List<?> results) {
		this.results = results;
	}
	
}
