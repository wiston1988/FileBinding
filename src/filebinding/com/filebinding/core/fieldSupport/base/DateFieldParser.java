package com.filebinding.core.fieldSupport.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.exception.FieldException;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.fieldSupport.FieldParser;
import com.filebinding.core.util.DateFormatUtil;

public class DateFieldParser implements FieldParser {

	public Object getParsedValue(String fieldValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		if(fieldValue == null || fieldValue.trim().length() == 0)
			return null;
		
		SimpleDateFormat dateFormat = DateFormatUtil.getDateFormat(config.getParseFormatStr());
		
		try{
			return dateFormat.parse(fieldValue);
		}catch(Exception e){
			throw new FieldException(config.getName(), "Failed to Convert Value ["+fieldValue+"] for class [Date]", e);
		}
	}

	public boolean canConvert(DocumentFieldConfiguration config) {
		return config.getType() == Date.class;
	}

}
