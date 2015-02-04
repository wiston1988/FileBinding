package com.netease.filebinding.core.strategy.base;

import java.util.ArrayList;
import java.util.List;

import com.netease.filebinding.core.config.DocumentFieldConfiguration;
import com.netease.filebinding.core.config.DocumentRecordConfiguration;
import com.netease.filebinding.core.exception.FieldException;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.exception.RecordException;

public class AbstractParsingStrategy extends BaseStrategy {
	
	protected AbstractParsingStrategy(){
		super();
	}
	
	protected List toBeans(List<String[]> records, DocumentRecordConfiguration recordConfiguration)throws MappingException{
		return toBeans(records, recordConfiguration, recordConfiguration.getFieldConfigurations());
	}
	
	protected List toBeans(List<String[]> records, DocumentRecordConfiguration recordConfiguration, List fieldConfigurations)throws MappingException{
		List<Object> pojos = new ArrayList<Object>();

		if(records == null || records.isEmpty())
			return pojos;

		for(String[] record : records){
			pojos.add(toBean(record, recordConfiguration, fieldConfigurations));
		}

		return pojos;
	} 
	
	protected Object toBean(String[] record, DocumentRecordConfiguration recordConfiguration) throws MappingException{
		if(record == null)
			return null;
		
		return toBean(record, recordConfiguration, recordConfiguration.getFieldConfigurations());
	}

	protected Object toBean(String[] record, DocumentRecordConfiguration recordConfiguration, List fieldConfigurations) throws MappingException{
		if(record == null)
			return null;		
		
		Object bean = initBean(recordConfiguration.getRecordClassName());

		if(fieldConfigurations == null)
			return bean;

		setFieldValues(record, fieldConfigurations, bean);

		return bean;
	}

	protected Object initBean(String recordClassName) throws RecordException{
		String info = "\n AbstractParsingStrategy::initBean - ";
		boolean traced = log.isTraceEnabled();
		
		if(recordClassName == null){
			String msg = "Failed to initialize since ClassName is blank.";
			log.error(info + msg);
			throw new RecordException(msg);
		}
		
		Object bean = null;

		try{
			bean = Class.forName(recordClassName).newInstance();
			if(traced)
				log.trace(info + "Bean name is: " + bean.getClass().getName());     
		}catch ( InstantiationException e ){
			log.error(info + e.getMessage(), e);
			throw new RecordException(e.getMessage(),e);
		}catch ( IllegalAccessException e ){
			log.error(info + e.getMessage(), e);
			throw new RecordException(e.getMessage(),e);
		}catch ( ClassNotFoundException e ){
			log.error(info + e.getMessage(), e);
			throw new RecordException(e.getMessage(),e);
		}

		if(bean == null){
			String msg = "Failed to initialize Bean Class: " + recordClassName;
			log.error(info + msg);
			throw new RecordException(msg);
		}
		
		return bean;
	}
	
	protected void setFieldValues(String[] record, List fieldConfigurations, Object bean) throws MappingException{
		if(fieldConfigurations == null)
			return ;

		int size = fieldConfigurations.size();
		int recordLength = record.length;
		
		for(int i=0;i<size;i++){
			DocumentFieldConfiguration fieldConfiguration =  (DocumentFieldConfiguration)fieldConfigurations.get(i);
			
			String fieldValue = null;
			if(i < recordLength)
				fieldValue = record[i];
			
			setFieldValue(fieldValue, fieldConfiguration, bean);
		}
	}

	protected void setFieldValue(String fieldValue, DocumentFieldConfiguration fieldConfiguration, Object bean) throws MappingException{
		if(fieldConfiguration == null)
			throw new FieldException(null, "There is an invalid field configuration.");
		if(!fieldConfiguration.isValid())//For invalid field config, do nothing
			return;
		
		
		fieldConfiguration.setFieldValue(fieldValue, bean);
	}
	
	public List getBeans(String tag){
		return super.getBeans(tag);
	}
}
