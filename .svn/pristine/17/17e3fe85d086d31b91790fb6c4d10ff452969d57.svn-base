package com.netease.filebinding.core.strategy.base.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.netease.filebinding.core.config.DocumentFieldConfiguration;
import com.netease.filebinding.core.config.DocumentRecordConfiguration;
import com.netease.filebinding.core.domain.*;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.exception.RecordException;
import com.netease.filebinding.core.io.DocumentReader;
import com.netease.filebinding.core.strategy.ParsingStrategy;
import com.netease.filebinding.core.strategy.base.AbstractParsingStrategy;

public class HeaderMappingParsingStrategy extends AbstractParsingStrategy implements ParsingStrategy<String[]> {

	protected Long totalCount = new Long(0);
	protected Long discardCount = new Long(0);

	protected List<DocumentFieldConfiguration> fieldConfigurations = new ArrayList<DocumentFieldConfiguration>();
	protected Set<String> columnGroup = new HashSet<String>();
	protected List<String> columns = new ArrayList<String>(); //All Header

	protected DocumentRecordConfiguration recordConfiguration;
	protected String[] header;

	/***Main Flow for this templet, thus final***/

	public final DocumentParseResult parse(DocumentReader<String[]> reader)throws MappingException, IOException{
		String info = "\n ParsingStrategy - ";
		try{
			initBeforeParse();

			captureHeader(reader);

			while(reader.hasNext()) {
				String[] line = reader.readNext();
				if(line == null)
					continue;

				Object bean = parseRecord(line);
				if(bean != null)
					this.addBean(tag, bean);
			}
			DocumentParseSummary documentParseSummary = new DocumentParseSummary(totalCount, discardCount);
			
			return new DocumentParseResult(documentParseSummary,this.getBeans(tag));
			
		}catch(MappingException e){
			log.error(info + "Exception - " + e.getMessage());
			throw e;
		}catch(IOException e){
			log.error(info + "Exception - " + e.getMessage());
			throw e;
		}
	}

//	protected abstract void captureHeader(DocumentReader<String[]> reader)throws MappingException, IOException;
//	protected abstract Object parseRecord(String[] record) throws MappingException;
	protected void captureHeader(DocumentReader<String[]> reader)throws MappingException, IOException {
		defaultCaptureHeader(reader);
	}

	protected Object parseRecord(String[] record) throws MappingException {
		return defaultParseRecord(record);
	}
	/***hooks***/
	protected void initBeforeParse()throws MappingException{
		/***Always clean beans before parse***/
		clearBeans(tag);
		
		if(recordConfiguration != null)
			return;
		
		recordConfiguration = this.getRecordConfiguration(tag);
		if(recordConfiguration == null)
			throw new RecordException("Can not get the mapping config for Record Tag " + tag +".");
	}

	/***Some default implement for CaptureHeader and Parese Each Line***/
	protected final void defaultCaptureHeader(DocumentReader<String[]> reader)throws MappingException, IOException{
		//fix in 05/21/2010, get the first not-null line as header
		while(reader.hasNext()){
			String[] line = reader.readNext();
			if(line != null){
				header = line;
				break;
			}
		}
		
		parseHeader(header);
	}

	protected final void parseHeader(String[] headerLine)throws MappingException{
		if(headerLine == null || headerLine.length == 0)
			return;

		fieldConfigurations.clear();
		columnGroup.clear();
		columns.clear();

		int length = headerLine.length;
		for(int i=0;i<length;i++){
//			System.out.println("header "+i+":"+headerLine[i]);
			String columnName = null;
			if(headerLine[i]!=null){
				columnName = headerLine[i].trim().toLowerCase();//LowerCase all header, to ignore case
			}
			if(columns.contains(columnName)){
				String errMsg = "There is reduplicate columns in header!";
				log.error(errMsg);
				throw new RecordException(errMsg);
			}

			DocumentFieldConfiguration fieldConfig = recordConfiguration.getSingleConfigByColumnName(columnName);

			if(fieldConfig == null)
				fieldConfig = DocumentFieldConfiguration.getIgnoreFieldConfiguration();

			fieldConfigurations.add(fieldConfig);
			columns.add(columnName);

			String groupName = fieldConfig.getGroupName();
			columnGroup.add(groupName == null ? columnName : groupName);
		}
//		for(int j=0; j<length;j++){
//			System.out.println("column "+j+":"+columns.get(j));
//		}
	}

	protected final Object defaultParseRecord(String[] record) throws MappingException{
		totalCount +=1;

		Object bean = null;//If invalid, return null
		try{
			bean = this.toBean(record, recordConfiguration, fieldConfigurations);
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
}
