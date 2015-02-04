package com.netease.filebinding.core.fieldSupport.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.netease.filebinding.core.config.DocumentFieldConfiguration;
import com.netease.filebinding.core.exception.FieldException;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.fieldSupport.FieldParser;
import com.netease.filebinding.core.util.DateFormatUtil;

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
