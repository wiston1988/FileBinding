package com.filebinding.core.domain;

import java.util.ArrayList;
import java.util.List;

public class DocumentParseResult {

	private List parsedData = new ArrayList();
	
	private DocumentParseSummary summary = new DocumentParseSummary();

	public DocumentParseResult(){}
	
	public DocumentParseResult(DocumentParseSummary summary,List parsedData){
		this.summary = summary;
		this.parsedData = parsedData;
	}
	
	public List getParsedData() {
		return parsedData;
	}

	public void setParsedData(List parsedData) {
		this.parsedData = parsedData;
	}

	public DocumentParseSummary getSummary() {
		return summary;
	}

	public void setSummary(DocumentParseSummary summary) {
		this.summary = summary;
	}
}
