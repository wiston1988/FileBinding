package filebinding.parsers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netease.filebinding.core.config.DocumentFieldConfiguration;
import com.netease.filebinding.core.exception.FieldException;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.fieldSupport.FieldParser;

public class ToUpperCaseParser implements FieldParser {
	private static Log logger = LogFactory.getLog(ToUpperCaseParser.class);
	public boolean canConvert(DocumentFieldConfiguration config) {		
		return false;
	}

	public Object getParsedValue(String fieldValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		if(config == null )
			throw new FieldException(config.getName(), "Field config is null");
		
		String retVal = fieldValue == null ? null : fieldValue.toUpperCase().trim();	
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
}
