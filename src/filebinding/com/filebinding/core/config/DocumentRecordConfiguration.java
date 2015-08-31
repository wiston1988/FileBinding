package com.filebinding.core.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.filebinding.core.exception.ConfigException;
import com.filebinding.core.strategy.BuildingStrategy;
import com.filebinding.core.strategy.ParsingStrategy;

public class DocumentRecordConfiguration implements Serializable {
	
	private static final long serialVersionUID = 3573539158844940583L;

	protected Log log;
	
	/**Configuration field**/
	private String recordTag;
    
    private boolean labelSupport;
    
    private String recordClassName;
    
    private String parserClassName;

	private String builderClassName;
	
	private boolean ignoreerror;
	
	private String dateFormatStr;
	
	private String numberFormatStr;
	
	/**********UnConfiguration field*****************/
	private List<DocumentFieldConfiguration> fields = new ArrayList<DocumentFieldConfiguration>();
    
	public DocumentRecordConfiguration(){
    	log = LogFactory.getLog(this.getClass());
    }
	
    public String getParserClassName(){
		return parserClassName;
	}
    
    public String getBuilderClassName(){
		return builderClassName;
	}
    
    public BuildingStrategy getBuilderClass(DocumentMappingConfiguration mappingConfiguration) throws ConfigException{
        String info = "\n getBuilderClass - ";
         
        BuildingStrategy builder = null;
        
        try{
            builder = (BuildingStrategy) Class.forName(builderClassName).newInstance();
            
            builder.setMappingConfiguration(mappingConfiguration);
            builder.setTag(this.getRecordTag());
        }catch ( InstantiationException e ){
            log.error(info + "InstantiationException for Builder: " + builderClassName,e);
            throw new ConfigException(info + "InstantiationException for Builder: " + builderClassName,e);
        }catch ( IllegalAccessException e ){
            log.error(info + "IllegalAccessException for Builder: " + builderClassName ,e);
            throw new ConfigException(info + "IllegalAccessException for Builder: " + builderClassName,e);
        }catch ( ClassNotFoundException e ){
            log.error(info + "ClassNotFoundException for Builder: " + builderClassName,e); 
            throw new ConfigException(info + "ClassNotFoundException for Builder: " + builderClassName,e);
        }
        
        return builder;
    }
    
    public ParsingStrategy getParserClass(DocumentMappingConfiguration mappingConfiguration) throws ConfigException{
        String info = "\n getParserClass - ";
        
        ParsingStrategy parser = null;
        
        try{            
            parser = (ParsingStrategy) Class.forName(parserClassName).newInstance();
            
            parser.setMappingConfiguration(mappingConfiguration);
            parser.setTag(this.getRecordTag());
        }catch ( InstantiationException e ){
            log.error(info + "InstantiationException for Parser: " + parserClassName,e);
            throw new ConfigException(info + "InstantiationException for Parser: " + builderClassName,e);
        }catch ( IllegalAccessException e ){
            log.error(info + "IllegalAccessException for Parser: " + parserClassName ,e);
            throw new ConfigException(info + "IllegalAccessException for Parser: " + builderClassName,e);
        }catch ( ClassNotFoundException e ){
            log.error(info + "ClassNotFoundException for Parser: " + parserClassName,e);
            throw new ConfigException(info + "ClassNotFoundException for Parser: " + builderClassName,e);
        }
        
        return parser;
    }
    
    public List<DocumentFieldConfiguration> getConfigByName(String fieldName){
    	List<DocumentFieldConfiguration> configs = new ArrayList<DocumentFieldConfiguration>();
    	if(fieldName == null)
    		return configs;
    	
        int size = fields.size();
        for(int i=0;i<size;i++){
        	DocumentFieldConfiguration currConfig = fields.get(i);
        	if(fieldName.trim().equalsIgnoreCase(currConfig.getName()))
        		configs.add(currConfig);
        }
        
        return configs;
    }
    
    public DocumentFieldConfiguration getSingleConfigByName(String fieldName){
    	List<DocumentFieldConfiguration> configs = getConfigByName(fieldName);
    	if(configs == null || configs.isEmpty() || configs.get(0) == null)
    		return null;
    	
    	return configs.get(0);
    }

    public List<DocumentFieldConfiguration> getConfigByColumnName(String columnName){
    	List<DocumentFieldConfiguration> configs = new ArrayList<DocumentFieldConfiguration>();
    	if(columnName == null)
    		return configs;
    	
        int size = fields.size();
        for(int i=0;i<size;i++){
        	DocumentFieldConfiguration currConfig = fields.get(i);
        	if(columnName.trim().equalsIgnoreCase(currConfig.getColumnName()))
        		configs.add(currConfig);
        }
        
        return configs;
    }
    
    public DocumentFieldConfiguration getSingleConfigByColumnName(String columnName){
    	List<DocumentFieldConfiguration> configs = getConfigByColumnName(columnName);
    	if(configs == null || configs.isEmpty() || configs.get(0) == null)
    		return null;
    	
    	return configs.get(0);
    }
    
    public List<DocumentFieldConfiguration> getFieldConfigurations(){
        return fields;
    }
    
	public String getRecordTag() {
		return recordTag;
	}

	public String getRecordClassName() {
		return recordClassName;
	}

	public boolean isLabelSupport() {
		return labelSupport;
	}

	public boolean isIgnoreerror() {
		return ignoreerror;
	}
	
	public String getDateFormatStr() {
		return dateFormatStr;
	}

	public String getNumberFormatStr() {
		return numberFormatStr;
	}
	
	/***protected for setter***/
	protected void setParserClassName(String parserClassName){
        this.parserClassName = parserClassName;
    }
    
	protected void setBuilderClassName(String builderClassName){
        this.builderClassName = builderClassName;
    }
    
	protected void addFieldConfiguration(DocumentFieldConfiguration fieldConfiguration){
        fields.add(fieldConfiguration);
    }

	protected void setRecordClassName(String recordClassName) {
		this.recordClassName = recordClassName;
	}

	protected void setRecordTag(String recordTag) {
		this.recordTag = recordTag;
	}

	protected void setLabelSupport(boolean labelSupport) {
		this.labelSupport = labelSupport;
	}

	protected void setIgnoreerror(boolean ignoreerror) {
		this.ignoreerror = ignoreerror;
	}

	protected void setDateFormatStr(String dateFormatStr) {
		this.dateFormatStr = dateFormatStr;
	}

	protected void setNumberFormatStr(String numberFormatStr) {
		this.numberFormatStr = numberFormatStr;
	}
}
