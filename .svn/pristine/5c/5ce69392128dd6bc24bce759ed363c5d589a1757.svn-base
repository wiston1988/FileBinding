package filebinding.parsers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.netease.filebinding.core.config.DocumentFieldConfiguration;
import com.netease.filebinding.core.exception.FieldException;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.fieldSupport.FieldParser;

public class ToLongParserThrowE implements FieldParser {

	private static Log logger = LogFactory.getLog(ToLongParserThrowE.class);
	public Object getParsedValue(String fieldValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		// TODO Auto-generated method stub

		if(config == null )
			throw new FieldException(config.getName(), "Field config is null");
					
		try{
			if(fieldValue == null || fieldValue.trim().equals("")){
				return null;
			}else{
				if(config.getParseFormatStr() != null && !config.getParseFormatStr().trim().equals("")){
					String formatStr = config.getParseFormatStr().trim();
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
					}
				
					if (subLen == fieldValue.length()){
						return new Long(fieldValue);	
					}else if( fieldValue.length() >subLen){
						throw new FieldException(config.getName(), "Field value is too long.");
					}else{
						return new Long(fieldValue);	
					}			
			    }
				return new Long(fieldValue);
			}
		}catch(Exception ex){
			if(logger.isInfoEnabled()){
				logger.info("When parse field "+config.getName()+" value to Long throw this exception:"+ex.getMessage());
			}
			throw new FieldException(config.getName(), ex.getMessage(),ex);
		}
	
	}

	public boolean canConvert(DocumentFieldConfiguration config) {
		// TODO Auto-generated method stub
		return false;
	}

}
