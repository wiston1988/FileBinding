package com.filebinding.core.domain;

public class DocumentParseSummary {

	private Long totalCount = new Long(0);
	private Long discardCount = new Long(0);
	private String description;
	private Object Data;
	
	public DocumentParseSummary(){}
	
	public DocumentParseSummary(Long discardCount){
		this.discardCount = discardCount;
	}
	
	public DocumentParseSummary(Long totalCount, Long discardCount){
		this.totalCount = totalCount;
		this.discardCount = discardCount;
	}
	
	public void mergeCount(DocumentParseSummary summary){
		totalCount = totalCount + summary.getTotalCount();
		discardCount = discardCount + summary.getDiscardCount();
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getDiscardCount() {
		if(discardCount != null)
			return discardCount;
		else
			return new Long(0);
	}
	
	public void setDiscardCount(Long discardCount) {
		this.discardCount = discardCount;
	}
	
	public Long getTotalCount() {
		if(totalCount != null)
			return totalCount;
		else
			return new Long(0);
	}
	
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	
	public Object getData() {
		return Data;
	}

	public void setData(Object data) {
		Data = data;
	}
}
