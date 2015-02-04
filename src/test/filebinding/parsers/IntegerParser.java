package filebinding.parsers;

import com.netease.filebinding.core.config.DocumentFieldConfiguration;
import com.netease.filebinding.core.exception.FieldException;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.fieldSupport.FieldParser;
import com.netease.filebinding.core.util.BaseTypeConverter;

public class IntegerParser implements FieldParser {

	public boolean canConvert(DocumentFieldConfiguration config) {
		if(config != null && config.getType() != null
				&& (config.getType() == Long.class || config.getType() == Integer.class))
			return true;
		
		return false;
	}

	public Object getParsedValue(String fieldValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		try{
			return BaseTypeConverter.getInstance().convertIfNecessary(ignoreZero(fieldValue),  config.getType());
		}catch(Exception e){
			throw new FieldException(config.getName(), "Failed to Convert Value ["+fieldValue+"] for class ["+config.getType()+"]", e);
		}
	}
	
	public static String ignoreZero(String str){
		if(str == null || str.trim().length()==0 || str.indexOf("0") > 0){
			return str;
		}else{
			char[] charArr = str.toCharArray();
			int size = charArr.length;
			int start = 0;

			while ((start < size) && (charArr [start] == '0')) start++;

			String value = str.substring(start, size);

			return value.trim().length() == 0 ? "0" : value;
		}
	}

}
