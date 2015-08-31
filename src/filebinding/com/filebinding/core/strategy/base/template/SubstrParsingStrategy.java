package com.filebinding.core.strategy.base.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.config.DocumentRecordConfiguration;
import com.filebinding.core.domain.DocumentParseResult;
import com.filebinding.core.domain.DocumentParseSummary;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.exception.RecordException;
import com.filebinding.core.io.DocumentReader;
import com.filebinding.core.strategy.ParsingStrategy;
import com.filebinding.core.strategy.base.AbstractParsingStrategy;

/***
 * In this Strategy, columnName is the starting&ending offset for column.
 * ***/
public  class SubstrParsingStrategy extends AbstractParsingStrategy implements ParsingStrategy<String> {

	protected Long totalCount = new Long(0);
	protected Long discardCount = new Long(0);
	
	protected List<DocumentFieldConfiguration> fieldConfigurations = new ArrayList<DocumentFieldConfiguration>();
	protected DocumentRecordConfiguration recordConfiguration;
	
	private Map<Integer,SubstrExpress> substrExpresses = new HashMap<Integer,SubstrExpress>();
	
	protected String ERRORCONFIGMSG = "Invalid Config of SubstrExpress for Record Tag [" + tag +"].";
	
	public final DocumentParseResult parse(DocumentReader<String> reader)throws MappingException, IOException {
		initBeforeParse();
		
		while(reader.hasNext()) {
			String line = reader.readNext();
			
			if(line == null)
				continue;
			
			Object bean = parseRecord(line);
			if(bean != null)
				this.addBean(tag, bean);
		}
		DocumentParseSummary documentParseSummary = new DocumentParseSummary(totalCount, discardCount);
		return new DocumentParseResult(documentParseSummary,this.getBeans(tag));
	}
	
	protected  Object parseRecord(String record) throws MappingException{
		return  defaultParseRecord(record);
	}
	
	/***hooks***/
	protected void initBeforeParse()throws MappingException{
		/***Always clean beans before parse***/
		clearBeans(tag);
		
		if(recordConfiguration == null){
			recordConfiguration = this.getRecordConfiguration(tag);
			if(recordConfiguration == null)
				throw new RecordException("Can not get the mapping config for Record Tag " + tag +".");
		}
		
		if(fieldConfigurations == null || fieldConfigurations.isEmpty()){
			fieldConfigurations = recordConfiguration.getFieldConfigurations();
		}
		
		/***build subStr express***/
		if(substrExpresses == null || substrExpresses.isEmpty())
			substrExpresses = buildExpress(fieldConfigurations);
	}
	
	protected final Map<Integer,SubstrExpress> buildExpress(List<DocumentFieldConfiguration> fieldConfigurations) throws MappingException{
		Map<Integer,SubstrExpress> substrExpresses = new HashMap<Integer,SubstrExpress>();
		
		if(fieldConfigurations == null)
			return substrExpresses;
		
		int size = fieldConfigurations.size();
		
		for(int i=0;i<size;i++){
			DocumentFieldConfiguration fieldConfiguration =  (DocumentFieldConfiguration)fieldConfigurations.get(i);
			if(fieldConfiguration != null)
				substrExpresses.put(Integer.valueOf(i), buildExpress(fieldConfiguration.getColumnName()));
			else
				throw new RecordException(ERRORCONFIGMSG);
		}
		
		return substrExpresses;
	}
	
	protected SubstrExpress buildExpress(String columnName) throws MappingException{
		if(columnName == null)
			throw new RecordException(ERRORCONFIGMSG);
		
		String[] expressArr = columnName.split(",");
		if(expressArr.length != 2)
			throw new RecordException(ERRORCONFIGMSG);
		
		int start = 0;
		int end = 0;
		
		try{
			start = Integer.valueOf(expressArr[0]);
			end = Integer.valueOf(expressArr[1]);
		}catch(NumberFormatException e){
			throw new RecordException(ERRORCONFIGMSG);
		}
		
		return new SubstrExpress(start,end);
	}
	
	protected final Object defaultParseRecord(String record) throws MappingException{
		totalCount +=1;
		Object bean = null;
		try{
			bean = initBean(recordConfiguration.getRecordClassName());
			setFieldValues(record, bean);
		}catch(MappingException e){
			if(recordConfiguration.isIgnoreerror()){
				discardCount +=1;
				return null;
			}else{
				throw e;
			}
		}
		
		return bean;
	}
	
	protected final void setFieldValues(String line, Object bean) throws MappingException{
		if(fieldConfigurations == null || line == null)
			return;
		
		int size = fieldConfigurations.size();
		
		for(int i=0;i<size;i++){
			String fieldValue = getFieldValue(line,getSubstrExpress(i));
			
			DocumentFieldConfiguration fieldConfiguration =  (DocumentFieldConfiguration)fieldConfigurations.get(i);
			setFieldValue(fieldValue, fieldConfiguration, bean);
		}
	}
	
	protected final String getFieldValue(String line, SubstrExpress express) throws MappingException{
		if(line == null)
			return null;
		
		if(express == null)
			throw new RecordException(ERRORCONFIGMSG);
		if(express.getEnd() > line.length())
			throw new RecordException("This Line is too short to parse correctly.");
		
		return line.substring(express.getStart(),express.getEnd());
	}
	
	protected final SubstrExpress getSubstrExpress(int column){
		if(substrExpresses == null)
			return null;
		
		return substrExpresses.get(Integer.valueOf(column));
	}
	
	/***TODO Could SubstrExpress combination with field configuration***/
	protected final class SubstrExpress{
		private int start;
		private int end;
		
		public SubstrExpress(int start, int end) throws MappingException{
			validateExpress(start, end);
			
			this.start = start;
			this.end = end;
		}
		
		private void validateExpress(int start, int end) throws MappingException{
			if(start < 0 || start > end)
				throw new RecordException(ERRORCONFIGMSG);
			
			return;
		}
		
		public int getEnd() {
			return end;
		}
		
		public int getStart() {
			return start;
		}
	}
}
