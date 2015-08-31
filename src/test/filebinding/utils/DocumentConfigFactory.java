package filebinding.utils;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.filebinding.core.config.DocumentMappingConfiguration;
import com.filebinding.core.config.DocumentMappingConfigurator;
import com.filebinding.core.exception.ConfigException;
import com.filebinding.core.exception.GeneralException;
import com.filebinding.core.strategy.BuildingStrategy;
import com.filebinding.core.strategy.ParsingStrategy;

public class DocumentConfigFactory {
	private Log log;

	private DocumentMappingConfiguration parseConfiguration;
	private DocumentMappingConfiguration buildConfiguration;
	//private String encoding;

	private static final String IMPORT_CONFIG = "filebinding/config/ImportDocumentConfig.xml";
	private static final String EXPORT_CONFIG = "filebinding/config/ExportDocumentConfig.xml";
	
	public DocumentConfigFactory() throws ConfigException{
		log = LogFactory.getLog(this.getClass());

		InputStream parseConfig = Thread.currentThread().getContextClassLoader().getResourceAsStream(IMPORT_CONFIG);
		InputStream buildConfig = Thread.currentThread().getContextClassLoader().getResourceAsStream(EXPORT_CONFIG);

		DocumentMappingConfigurator configurator = new DocumentMappingConfigurator();
		
		parseConfiguration = configurator.parseImportConfiguration(parseConfig);
		buildConfiguration = configurator.parseExportConfiguration(buildConfig);
	}
	
	public ParsingStrategy getParser(String tag)throws GeneralException{
		String METHOD = "\n DocumentConfigFactory::getParser - ";
		
		ParsingStrategy parser = null;
		
		try{
			parser = parseConfiguration.getParserClass(tag);

			if(parser == null){
				throw new ConfigException("Internal error: Failed to get Parser.");
			}
		}catch(GeneralException e){
			log.error(METHOD + "Failed to get parser because of config error.", e);
			throw e;
		}catch(Exception e){
			log.error(METHOD + "Failed to get parser because of Unexpected Error.", e);
			throw new GeneralException(e);
		}
		
		return parser;
	}
	
	public BuildingStrategy getBuilder(String tag)throws GeneralException{
		String METHOD = "\n DocumentConfigFactory::getBuilder - ";
		
		BuildingStrategy builder = null;
		
		try{
			builder = buildConfiguration.getBuilderClass(tag);

			if(builder == null){
				throw new ConfigException("Internal error: Failed to get Builder.");
			}
		}catch(GeneralException e){
			log.error(METHOD + "Failed to get builder because of config error.", e);
			throw e;
		}catch(Exception e){
			log.error(METHOD + "Failed to get builder because of Unexpected Error.", e);
			throw new GeneralException(e);
		}
		
		return builder;
	}

	public DocumentMappingConfiguration getBuildConfiguration() {
		return buildConfiguration;
	}

	public DocumentMappingConfiguration getParseConfiguration() {
		return parseConfiguration;
	}
}
