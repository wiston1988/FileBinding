package com.filebinding.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.input.SAXBuilder;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

import com.filebinding.core.exception.ConfigException;
import com.filebinding.core.fieldSupport.FieldParser;
import com.filebinding.core.fieldSupport.FieldRenderer;
import com.filebinding.core.util.introspector.IntrospectUtil;

public class DocumentMappingConfigurator {

	/***ROOT LEVEL***/
	private static final String DESCRIPTION_EL = "description";
	private static final String DOC_ROOT_EL = "docmapping";
	private static final String PROPERTY_EL = "property";
	private static final String NAME_ATTR = "name";
	private static final String VALUE_ATTR = "value";

	/***CONVERTER LEVEL***/
	private static final String AUTO_FIELDPARSER_EL = "auto-fieldParser";
	private static final String AUTO_FIELDRENDERER_EL = "auto-fieldRenderer";

	/***CLASS CONFIG LEVEL***/
	private static final String CLASSCONFIG_EL = "classConfig";
	private static final String CLASS_EL = "class";

	/***RECORD LEVEL***/
	private static final String RECORD_EL = "record";
	private static final String TAG_ATTR = "tag";
	private static final String CLASS_ATTR = "class";
	private static final String LABEL_ATTR = "label-support";
	private static final String PARSER_ATTR = "parser";
	private static final String BUILDER_ATTR = "builder";
	private static final String IGNOREERROR_ATTR = "ignoreerror";
	private static final String DATE_FORMAT_ATTR = "dateFormat";
	private static final String NUMBER_FORMAT_ATTR = "numberFormat";

	/***FIELD LEVEL***/
	private static final String MAPPING_FIELD_EL = "mapping-field";
	private static final String VALID_ATTR = "valid";
	private static final String COLUMN_ATTR = "bind-to-column";
	private static final String GROUPNAME_ATTR = "groupname";
	private static final String RENDERER_ATTR = "renderer";
	private static final String REQUIRED_ATTR = "required";
	private static final String FORMATSTR_ATTR = "formatStr";
	private static final String PARSE_FORMATSTR_ATTR = "parseFormatStr";
	private static final String BUILD_FORMATSTR_ATTR = "buildFormatStr";
	private static final String FLAG_ATTR = "flag";

	protected Log log = LogFactory.getLog(DocumentMappingConfigurator.class);
	private Map<String, String> classMap = new HashMap<String, String>();

	private static ThreadLocal<Boolean> isImport = new ThreadLocal<Boolean>(){
		@Override
		protected Boolean initialValue() {
			return Boolean.TRUE;
		}
	};

	private static void setIsImport(Boolean _isImport){
		isImport.set(_isImport);
	}

	private static Boolean getIsImport(){
		return isImport.get();
	}

	public DocumentMappingConfigurator(){}

	public DocumentMappingConfiguration parseImportConfiguration(InputStream config) throws ConfigException{
		return parseConfiguration(config, Boolean.TRUE);
	}

	public DocumentMappingConfiguration parseExportConfiguration(InputStream config) throws ConfigException{
		return parseConfiguration(config, Boolean.FALSE);
	}

	private DocumentMappingConfiguration parseConfiguration(InputStream config, Boolean isImport) throws ConfigException{
		String info = "\n DocumentMappingConfigurator::parseConfiguration - ";
		boolean traced = log.isTraceEnabled();

		if(traced)
			log.trace(info + "begin <");

		setIsImport(isImport);
		DocumentMappingConfiguration configuration = new DocumentMappingConfiguration();

		try{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(config);
			Element root = doc.getRootElement();

			if (root == null)
				throw new ConfigException(info + "< null root element");
			if (!DOC_ROOT_EL.equals(root.getName()))
				throw new ConfigException(info + "< invalid doc root - " + root.getName());

			parseBaseConfiguration(root,configuration);

			log.info(info + "current configuration := " + configuration.getDescription());
		}catch ( Throwable t ){
			throw new ConfigException(info + "< caught exception - " + t.getMessage(), t);
		}finally{
			try {
				if(config != null)
					config.close();
			} catch (IOException ioe) {
				config = null;
			}
		}

		if(traced)
			log.trace(info + "over >");

		return configuration;
	}

	protected void parseBaseConfiguration(Element root, DocumentMappingConfiguration configuration) throws ConfigException{
		String info = "\n DocumentMappingConfigurator::parseBaseConfiguration - ";
		final boolean tracing = log.isTraceEnabled();

		if(configuration == null)
			return;

		if (tracing){
			log.trace(info + "begin() > ");
		}

		//parse description
		Element description = root.getChild(DESCRIPTION_EL);
		if( description != null ){
			configuration.setDescription(description.getText());
		}

		//parse PROPERTY ELEMENT
		List csvProperties = root.getChildren(PROPERTY_EL);
		if( csvProperties != null ) {   
			for(int i=0;i<csvProperties.size();i++){
				Element csvProp = (Element)csvProperties.get(i);
				Attribute propName = csvProp.getAttribute(NAME_ATTR);
				Attribute propValue = csvProp.getAttribute(VALUE_ATTR);

				if( propName != null && propValue != null)
					configuration.setProperty(propName.getValue(), propValue.getValue());
			}
		}

		//parse class config
		Element classConfig = root.getChild(CLASSCONFIG_EL);
		if( classConfig != null ){
			List classMappings = classConfig.getChildren(CLASS_EL);
			for(int i=0;i<classMappings.size();i++){
				Element classMapping = (Element)classMappings.get(i);
				if(classMapping != null){
					Attribute className = classMapping.getAttribute( NAME_ATTR );
					Attribute classValue = classMapping.getAttribute( VALUE_ATTR );

					if(className != null && classValue != null)
						addClassMapping(className.getValue(), classValue.getValue());
				}
			}
		}

		//parse converters (parsers and renderers)
		Element autoFieldParser = root.getChild(AUTO_FIELDPARSER_EL);
		if(autoFieldParser != null){
			List values = autoFieldParser.getChildren(VALUE_ATTR);
			int size = values.size();

			for(int i=0; i<size; i++){
				Element value = (Element)values.get(i);
				if(value != null)
					configuration.addFieldParser(getClassName(value.getValue()));
			}
		}

		Element autoFieldRenderer = root.getChild(AUTO_FIELDRENDERER_EL);
		if(autoFieldRenderer != null){
			List values = autoFieldRenderer.getChildren(VALUE_ATTR);
			int size = values.size();

			for(int i=0; i<size; i++){
				Element value = (Element)values.get(i);
				if(value != null)
					configuration.addFieldRenderer(getClassName(value.getValue()));
			}
		}

		//parse RECORD ELEMENT
		List records = root.getChildren(RECORD_EL);

		if(records != null && records.size() > 0)
			parseRecordConfiguration(records, configuration);

		if (tracing){
			log.trace(info + "over() < ");
		}
	}

	protected void parseRecordConfiguration(List records, DocumentMappingConfiguration configuration) throws ConfigException{
		String info = "\n DocumentMappingConfigurator::parseRecordConfiguration - ";
		final boolean tracing = log.isTraceEnabled();

		if(records == null || configuration == null)
			return;

		if (tracing){
			log.trace(info + "begin() > ");
		}

		int size = records.size();
		for(int i=0;i<size;i++)
		{
			DocumentRecordConfiguration recordConfiguration = new DocumentRecordConfiguration();
			Element record = (Element)records.get(i);
			if(record == null)
				continue;

			Attribute recordTag = record.getAttribute(TAG_ATTR);
			if(recordTag != null){
				recordConfiguration.setRecordTag(recordTag.getValue());
			}else{
				log.error(info + "Record Configuration is invalid since mapping class is missing");
				throw new ConfigException("Record Configuration is invalid since mapping class is missing");
			}

			/***These 3 config will use class Map, put them together to manager***/
			Attribute recordClass = record.getAttribute(CLASS_ATTR);
			if(recordClass != null){
				String className = getClassName(recordClass.getValue());
				recordConfiguration.setRecordClassName(className);
			}else{
				log.error(info + "Record Configuration of "+recordConfiguration.getRecordTag()+" is invalid since mapping class is missing");
				throw new ConfigException("Record Configuration of "+recordConfiguration.getRecordTag()+" is invalid since mapping class is missing");
			}

			Attribute recordParser = record.getAttribute(PARSER_ATTR);
			if(recordParser != null){
				String parseClass = getClassName(recordParser.getValue());
				recordConfiguration.setParserClassName(parseClass);
			}

			Attribute recordBuilder = record.getAttribute(BUILDER_ATTR);
			if(recordBuilder != null){
				String builderClass = getClassName(recordBuilder.getValue());
				recordConfiguration.setBuilderClassName(builderClass);
			}
			/***use class map end***/

			Attribute labelSupport = record.getAttribute(LABEL_ATTR);
			if (labelSupport != null)
				recordConfiguration.setLabelSupport((Boolean.valueOf(labelSupport.getValue())).booleanValue());

			Attribute ignoreerror = record.getAttribute(IGNOREERROR_ATTR);
			if(ignoreerror != null)
				recordConfiguration.setIgnoreerror(Boolean.parseBoolean(ignoreerror.getValue()));

			Attribute dateformat = record.getAttribute(DATE_FORMAT_ATTR);
			if (dateformat != null)
				recordConfiguration.setDateFormatStr(dateformat.getValue());
			else if(configuration.getProperty(DATE_FORMAT_ATTR) != null)
				recordConfiguration.setDateFormatStr(configuration.getProperty(DATE_FORMAT_ATTR));//set default format for recordConfig

			Attribute numberFormat = record.getAttribute(NUMBER_FORMAT_ATTR);
			if(numberFormat != null)
				recordConfiguration.setNumberFormatStr(numberFormat.getValue());
			else if(configuration.getProperty(NUMBER_FORMAT_ATTR) != null)
				recordConfiguration.setNumberFormatStr(configuration.getProperty(NUMBER_FORMAT_ATTR));

			List fields = record.getChildren(MAPPING_FIELD_EL);
			if(fields != null && fields.size() > 0)
				parseFieldConfiguration(fields, recordConfiguration, configuration);

			configuration.addRecordConfiguration(recordTag.getValue(), recordConfiguration);
		}
	}

	protected void parseFieldConfiguration(List fields, DocumentRecordConfiguration recordConfiguration,
			DocumentMappingConfiguration configuration) throws ConfigException {
		String info = "\n DocumentMappingConfigurator::parseFieldConfiguration - ";
		final boolean tracing = log.isTraceEnabled();

		if(fields == null || recordConfiguration == null)
			return;

		if (tracing){
			log.trace(info + "begin() > ");
		}

		String className = recordConfiguration.getRecordClassName();

		int size = fields.size();
		for(int i=0;i<size;i++){
			Element field = (Element)fields.get(i);
			DocumentFieldConfiguration fieldConfiguration = parseFieldConfiguration(field);
			if(fieldConfiguration == null)
				continue;

			if(fieldConfiguration.getName() != null)//Get Property Descriptor if there is name specified
			{
				if(getIsImport())
					fieldConfiguration.setPropertyDescriptors(IntrospectUtil.introspectWriteableProperty(className, fieldConfiguration.getName()));
				else
					fieldConfiguration.setPropertyDescriptors(IntrospectUtil.introspectReadbleProperty(className, fieldConfiguration.getName()));
			}

			/***Set default parse format***/
			if(fieldConfiguration.getParseFormatStr() == null || fieldConfiguration.getParseFormatStr().trim().length() ==0){
				String defaultParseFormat = getFormatStrFromRecordLevel(fieldConfiguration.getType(), recordConfiguration);
				if(defaultParseFormat != null && defaultParseFormat.trim().length() > 0)
					fieldConfiguration.setParseFormatStr(defaultParseFormat);
			}

			/***Set default build format***/
			if(fieldConfiguration.getBuildFormatStr() == null || fieldConfiguration.getBuildFormatStr().trim().length() ==0){
				String defaultBuildFormat = getFormatStrFromRecordLevel(fieldConfiguration.getType(), recordConfiguration);
				if(defaultBuildFormat != null && defaultBuildFormat.trim().length() > 0)
					fieldConfiguration.setBuildFormatStr(defaultBuildFormat);
			}

			/***If Paser or Renderer is nonexistent, get from mapping configuration***/
			if(getIsImport() && fieldConfiguration.getFieldParser() == null){
				List<FieldParser> parsers = configuration.getFieldParsers();
				int length = parsers.size();
				int idx = length -1;
				
				while(idx >=0){
					FieldParser fieldParser = parsers.get(idx);
					if(fieldParser.canConvert(fieldConfiguration)){
						fieldConfiguration.setFieldParser(fieldParser);
						break;
					}					
					idx --;
				}
			}else if(!getIsImport() && fieldConfiguration.getFieldRenderer() == null){
				List<FieldRenderer> renderers = configuration.getFieldRenderers();
				int length = renderers.size();
				int idx = length -1;

				while(idx >=0){
					FieldRenderer fieldRenderer = renderers.get(idx);
					if(fieldRenderer.canConvert(fieldConfiguration)){
						fieldConfiguration.setFieldRenderer(fieldRenderer);
						break;
					}
					idx --;
				}
			}
			
			
			/***Copy field config if it's a clomn group***/
			ColumnGroupConfig groupConfig = parseColumnNames(field);
			List columnNames = groupConfig == null ? null : groupConfig.getColumnNames();

			if(columnNames!=null && !columnNames.isEmpty()){//Multi column Name
				addGroupConfig(recordConfiguration, groupConfig, fieldConfiguration);
			}else{
				recordConfiguration.addFieldConfiguration(fieldConfiguration);
			}
		}

		if (tracing){
			log.trace(info + "over() < ");
		}
	}

	protected DocumentFieldConfiguration parseFieldConfiguration(Element field) throws ConfigException{
		if(field == null)
			return null;

		DocumentFieldConfiguration fieldConfiguration = new DocumentFieldConfiguration();

		Attribute fieldName = field.getAttribute(NAME_ATTR);
		if (fieldName != null){
			fieldConfiguration.setName(fieldName.getValue());
		}

		Attribute fieldColumn = field.getAttribute(COLUMN_ATTR);
		if (fieldColumn != null){
			fieldConfiguration.setColumnName(fieldColumn.getValue());
		}

		Attribute fieldValid = field.getAttribute(VALID_ATTR);
		if (fieldValid != null){
			boolean valid = Boolean.parseBoolean(fieldValid.getValue());
			fieldConfiguration.setValid(valid);
		}

		Attribute fieldRequired = field.getAttribute(REQUIRED_ATTR);
		if (fieldRequired != null){
			boolean required = Boolean.parseBoolean(fieldRequired.getValue());
			fieldConfiguration.setRequired(required);
		}

		Attribute renderer = field.getAttribute(RENDERER_ATTR);
		if(renderer != null) {
			String rendererClass = getClassName(renderer.getValue());
			fieldConfiguration.setRendererName(rendererClass);
		}

		Attribute fieldParser = field.getAttribute(PARSER_ATTR);
		if (fieldParser != null){
			String parserClass = getClassName(fieldParser.getValue());
			fieldConfiguration.setParserName(parserClass);
		}

		Attribute fieldParseFormatStr = field.getAttribute(PARSE_FORMATSTR_ATTR);
		if (fieldParseFormatStr != null){
			fieldConfiguration.setParseFormatStr(fieldParseFormatStr.getValue());
		}

		Attribute fieldBuildFormatStr = field.getAttribute(BUILD_FORMATSTR_ATTR);
		if (fieldBuildFormatStr != null){
			fieldConfiguration.setBuildFormatStr(fieldBuildFormatStr.getValue());
		}

		Attribute fieldFormatStr = field.getAttribute(FORMATSTR_ATTR);
		if (fieldFormatStr != null){
			fieldConfiguration.setFormatStr(fieldFormatStr.getValue());
		}

		Attribute fieldFlag = field.getAttribute(FLAG_ATTR);
		if (fieldFlag != null){
			fieldConfiguration.setFlag(fieldFlag.getValue());
		}

		return fieldConfiguration;
	}
	
	private ColumnGroupConfig parseColumnNames(Element field){
		ColumnGroupConfig groupConfig = new ColumnGroupConfig();
		if(field == null)
			return groupConfig;

		Element column = field.getChild(COLUMN_ATTR);
		if(column == null)
			return groupConfig;

		Attribute groupName = column.getAttribute(GROUPNAME_ATTR);
		if (groupName != null){
			groupConfig.setGroupName(groupName.getValue());
		}

		List values = column.getChildren(VALUE_ATTR);
		int size = values.size();

		for(int i=0; i<size; i++){
			Element value = (Element)values.get(i);
			if(value != null)
				groupConfig.addColumnName(value.getTextTrim());
		}

		return groupConfig;
	}

	private void addGroupConfig(DocumentRecordConfiguration recordConfiguration,
			ColumnGroupConfig groupConfig, DocumentFieldConfiguration fieldConfiguration) throws ConfigException {
		List<String> columnNames = groupConfig == null ? null : groupConfig.getColumnNames();
		if(columnNames == null)
			return;

		int size = columnNames.size();
		String groupName = groupConfig.getGroupName();

		for(int i=0;i<size;i++){
			if(i==0){
				//If groupname is not setup, the first column value will be groupname
				if(groupName == null || groupName.trim().length() == 0)
					groupName = columnNames.get(i);

				if(fieldConfiguration.getColumnName() == null || fieldConfiguration.getColumnName().trim().length() == 0)
					fieldConfiguration.setColumnName(groupName);

				fieldConfiguration.setGroupName(groupName);
				recordConfiguration.addFieldConfiguration(fieldConfiguration);
			}else{
				DocumentFieldConfiguration copyConfig = fieldConfiguration.copy();
				copyConfig.setColumnName(columnNames.get(i));
				recordConfiguration.addFieldConfiguration(copyConfig);
			}
		}
	}

	private String getClassName(String key){
		String className = (String)classMap.get(key);
		if(className == null)
			return key;
		else
			return className;
	}

	private void addClassMapping(String key, String className){
		classMap.put(key, className);
	}

	private String getFormatStrFromRecordLevel(Class type, DocumentRecordConfiguration recordConfiguration){

		if(IntrospectUtil.isTypeMatch(Date.class, type)){
			String formatStr = recordConfiguration.getDateFormatStr();
			if(formatStr != null && formatStr.trim().length() > 0)
				return formatStr;
		}else if(IntrospectUtil.isTypeMatch(Number.class, type)){
			String formatStr = recordConfiguration.getNumberFormatStr();
			if(formatStr != null && formatStr.trim().length() > 0)
				return formatStr;
		}

		return null;
	}

	private static class ColumnGroupConfig{
		private String groupName;
		private List<String> columnNames;

		ColumnGroupConfig(){
			columnNames = new ArrayList<String>();
		} 

		public void addColumnName(String columnName){
			if(columnNames == null)
				columnNames = new ArrayList<String>();

			columnNames.add(columnName);
		}

		public List<String> getColumnNames() {
			return columnNames;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}
	}
}
