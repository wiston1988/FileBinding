package filebinding.parsers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.exception.FieldException;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.fieldSupport.FieldParser;

public class ToStringParserThrowE implements FieldParser {

	private static Log logger = LogFactory.getLog(ToStringParserThrowE.class);
	public Object getParsedValue(String fieldValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {

		if(config == null )
			throw new FieldException(config.getName(), "Field config is null");
		
		String retVal = fieldValue == null ? null : fieldValue.trim();	
		String formatStr = config.getParseFormatStr()==null ? null : config.getParseFormatStr().trim();
		int subLen = 0;
				
		if ( formatStr != null && !formatStr.equals("") ){
			try{
				subLen = Integer.valueOf(formatStr).intValue();
			}catch(Exception ex){
				if(logger.isInfoEnabled()){
					logger.info("Configuration for field "+config.getName() +"->formatStr is not correct.");
				}
				subLen = 0;
			}
		}else{//If no config, just return the fieldvalue
			return retVal;			
		}
		
		try{						
			if(retVal == null || retVal.equals("")){
				return null;
			}else{				
				if (subLen == 0 ){
					return retVal;
				}
				if(retVal.length() > subLen ) {
					throw new FieldException(config.getName(), "Field value is too long");
				}else {
					return retVal;
				}
			}
		}catch(Exception ex){
			if(logger.isInfoEnabled()){
				logger.info("When parse field "+config.getName()+" value to String skip this exception:"+ex.getMessage());
			}
			throw new FieldException(config.getName(), ex.getMessage(),ex);
		}
	
	
	}

	public boolean canConvert(DocumentFieldConfiguration config) {
		return false;
	}

}
