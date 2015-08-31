package com.filebinding.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.filebinding.core.exception.ConfigException;
import com.filebinding.core.fieldSupport.FieldParser;
import com.filebinding.core.fieldSupport.FieldRenderer;
import com.filebinding.core.fieldSupport.base.DateFieldParser;
import com.filebinding.core.fieldSupport.base.DateFieldRenderer;
import com.filebinding.core.fieldSupport.base.DefaultFieldParser;
import com.filebinding.core.fieldSupport.base.DefaultFieldRenderer;
import com.filebinding.core.fieldSupport.base.NumberFieldRenderer;
import com.filebinding.core.strategy.BuildingStrategy;
import com.filebinding.core.strategy.ParsingStrategy;

public class DocumentMappingConfiguration implements Serializable {
	
	private static final long serialVersionUID = 8099945832346319491L;
	protected Log log = LogFactory.getLog(DocumentMappingConfiguration.class);
	
	private String description;
	private Map<String, String> properties = new HashMap<String, String>();;
	private Map<String, DocumentRecordConfiguration> records = new HashMap<String, DocumentRecordConfiguration>();;
	
	private List<FieldParser> fieldParsers = new ArrayList<FieldParser>();;
	private List<FieldRenderer> fieldRenderers = new ArrayList<FieldRenderer>();;
	
	protected DocumentMappingConfiguration(){		
		fieldParsers.add(new DefaultFieldParser());
		fieldParsers.add(new DateFieldParser());
		
		fieldRenderers.add(new DefaultFieldRenderer());
		fieldRenderers.add(new DateFieldRenderer());
		fieldRenderers.add(new NumberFieldRenderer());
	}
	
	/***All Public method is getters, used for parse/build strategies ***/
    public String getProperty(String propName){
        return properties.get(propName);
    }
    
    public DocumentRecordConfiguration getRecordConfiguration(String recordTag){
    	return records.get(recordTag);
    }
    
    public BuildingStrategy getBuilderClass(String recordTag) throws ConfigException{
    	DocumentRecordConfiguration recordConfig = records.get(recordTag);
    	return recordConfig.getBuilderClass(this);
    }
    
    public ParsingStrategy getParserClass(String recordTag) throws ConfigException {
    	DocumentRecordConfiguration recordConfig = records.get(recordTag);
    	return recordConfig.getParserClass(this);
    }

	public String getDescription() {
		return description == null ? "" : description;
	}

	public List<FieldParser> getFieldParsers() {
		return fieldParsers;
	}

	public List<FieldRenderer> getFieldRenderers() {
		return fieldRenderers;
	}

	/***protected for setter***/	
	protected void setProperty(String propName, String propValue){
        properties.put(propName, propValue);
    }
    
    protected void addRecordConfiguration(String recordTag, DocumentRecordConfiguration recordConfiguration){
        records.put(recordTag, recordConfiguration);
    }

    protected void setDescription(String description) {
		this.description = description;
	}
    
    protected void addFieldParser(String paserName) throws ConfigException {
    	final String info = "\nDocumentMappingConfiguration::addFieldRenderer - ";
		if(paserName == null || paserName.trim().length() == 0)
        	return;
		
		try{
			FieldParser parser = (FieldParser)Class.forName(paserName).newInstance();
			fieldParsers.add(parser);
		}catch(Exception e){
			log.error(info + "Exception for csv renderer: " + paserName, e);
            throw new ConfigException(info + "Exception for csv renderer: " + paserName,e);
		}
    }
    
	protected void addFieldRenderer(String rendererName) throws ConfigException {
		final String info = "\nDocumentMappingConfiguration::addFieldRenderer - ";
		if(rendererName == null || rendererName.trim().length() == 0)
        	return;
		
		try{
			FieldRenderer renderer = (FieldRenderer)Class.forName(rendererName).newInstance();
			fieldRenderers.add(renderer);
		}catch(Exception e){
			log.error(info + "Exception for csv parser: " + rendererName, e);
            throw new ConfigException(info + "Exception for csv parser: " + rendererName,e);
		}
    }
}
