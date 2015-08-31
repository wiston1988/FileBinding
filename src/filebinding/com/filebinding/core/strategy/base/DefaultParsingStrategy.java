package com.filebinding.core.strategy.base;

import java.io.IOException;

import com.filebinding.core.config.DocumentRecordConfiguration;
import com.filebinding.core.domain.DocumentParseResult;
import com.filebinding.core.domain.DocumentParseSummary;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.exception.RecordException;
import com.filebinding.core.io.DocumentReader;
import com.filebinding.core.strategy.ParsingStrategy;

public class DefaultParsingStrategy extends AbstractParsingStrategy implements ParsingStrategy<String[]> {

	protected DocumentRecordConfiguration recordConfiguration;
	
	protected Long totalCount = new Long(0);
	protected Long discardCount = new Long(0);

	public final DocumentParseResult parse(DocumentReader<String[]> reader)throws MappingException, IOException{
		String info = "\n parse - ";

		try{			
			recordConfiguration = this.getRecordConfiguration(tag);
			if(recordConfiguration == null)
				throw new RecordException("Can not get the mapping config for Record Tag " + tag +".");
			
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
		}catch(IOException e){
			log.error(info + "Exception - " + e.getMessage());
			throw e;
		}
	}
	
	/***hook for parse Line***/
	protected Object parseRecord(String[] record) throws MappingException{
		totalCount +=1;

		Object bean = null;//If invalid, return null
		try{
			bean = this.toBean(record, recordConfiguration);
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
