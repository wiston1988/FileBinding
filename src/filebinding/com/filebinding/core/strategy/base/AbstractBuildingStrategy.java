package com.filebinding.core.strategy.base;

import java.util.ArrayList;
import java.util.List;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.config.DocumentRecordConfiguration;
import com.filebinding.core.exception.FieldException;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.exception.RecordException;

public abstract class AbstractBuildingStrategy extends BaseStrategy {
	
	protected static final String EMPTY_STR = "";

	protected AbstractBuildingStrategy() {
		super();
	}

	protected DocumentRecordConfiguration recordconfig;
	protected List<DocumentFieldConfiguration> fieldConfigurations;
	
	protected final void initializeConfig()throws MappingException{
		String info = "\n AbstractBuildingStrategy::initializeConfig - ";
		
		recordconfig = this.getRecordConfiguration(tag);
		if(recordconfig == null)
			throw new RecordException(info + "No Valid CSV Building Configuration");

		fieldConfigurations = recordconfig.getFieldConfigurations();
		if(fieldConfigurations == null || fieldConfigurations.size() == 0)
			throw new RecordException(info + "No field is defined in CSV configuration");
	}

	protected List<String[]> toCSVLine(List pojos, DocumentRecordConfiguration recordConfiguration) throws MappingException {
		return toCSVLine(pojos, recordConfiguration, recordConfiguration.getFieldConfigurations());
	}

	protected List<String[]> toCSVLine(List pojos, DocumentRecordConfiguration recordConfiguration, List<DocumentFieldConfiguration> fieldConfigurations) throws MappingException {
		String info = "\ntoCSVLine - ";

		if(pojos == null)
			throw new RecordException(info + "No Beans");

		if(recordConfiguration == null)
			throw new RecordException(info + "No Valid CSV Building Configuration");

		if(fieldConfigurations == null || fieldConfigurations.size() == 0)
			throw new RecordException(info + "No field is defined in CSV configuration");

		List<String[]> csvLines = new ArrayList<String[]>();

		boolean labelSupport = recordConfiguration.isLabelSupport();

		if(labelSupport){
			csvLines.add(buildCSVLabel(fieldConfigurations));
		}

		for(int idx=0;idx<pojos.size();idx++){
			Object bean = pojos.get(idx);
			csvLines.add(buildRecordLine(bean, fieldConfigurations));
		}

		return csvLines;
	}

	protected String[] buildRecordLine(Object bean, List<DocumentFieldConfiguration> fieldConfigurations)throws MappingException{
		if(fieldConfigurations == null)
			return new String[0];

		String[] segment = new String[fieldConfigurations.size()];

		int size = fieldConfigurations.size();
		for(int i=0;i<size;i++){
			DocumentFieldConfiguration fieldConfiguration = fieldConfigurations.get(i);

			segment[i] = getFieldValue(bean, fieldConfiguration);
		}

		return segment;
	}

	protected String getFieldValue(Object bean, DocumentFieldConfiguration fieldConfiguration)throws MappingException{
		if(fieldConfiguration == null)
			throw new FieldException(null, "There is an invalid field configuration.");
		
		return fieldConfiguration.getFieldValue(bean);
	}

	//build Header
	protected String[] buildCSVLabel(List<DocumentFieldConfiguration> fieldConfigurations){
		if(fieldConfigurations == null)
			return new String[0];

		int size = fieldConfigurations.size();
		String[] labels = new String[size];

		for(int i=0;i<size;i++){
			DocumentFieldConfiguration fieldConfiguration = fieldConfigurations.get(i);
			String fieldColumn = fieldConfiguration.getColumnName();

			labels[i] = fieldColumn == null ? EMPTY_STR : fieldColumn;
		}

		return labels;
	}

	public void addBeans(String tag, List pojos) {
		super.addBeans(tag, pojos);
	}
}
