package com.filebinding.core.strategy.base;

import java.io.IOException;
import java.util.List;

import com.filebinding.core.exception.MappingException;
import com.filebinding.core.io.DocumentWriter;
import com.filebinding.core.strategy.BuildingStrategy;

public class DefaultBuildingStrategy extends AbstractBuildingStrategy implements BuildingStrategy<String[]> {
	
	protected DocumentWriter<String[]> output;
	
	//The default implement is: just write the records of default Tag
	public void build(DocumentWriter<String[]> output,List records) throws MappingException, IOException {
		this.output = output;
		this.addBeans(tag, records);
		initializeConfig();

		//write Header
		if(recordconfig != null && recordconfig.isLabelSupport())
			output.writeNext(buildCSVLabel(fieldConfigurations));

		//write data lines
		List beans = getBeans(tag);
		if(beans != null){
			for(Object bean : beans)
				writeLine(bean);
		}
	}
	
	//hook
	protected void writeLine(Object bean)throws MappingException, IOException{
		output.writeNext(buildRecordLine(bean, fieldConfigurations));
	}
}
