package com.filebinding.core.fieldSupport.base;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.exception.FieldException;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.fieldSupport.FieldRenderer;
import com.filebinding.core.util.DateFormatUtil;
import com.filebinding.core.util.introspector.IntrospectUtil;

public class DateFieldRenderer extends FieldRenderer {

	public String getRenderedValue(Object pojoValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		Date date = null;
		try{
			date = (Date)pojoValue;
		}catch(Exception e){
			throw new FieldException(config.getName(), "Failed to Convert Value ["+pojoValue+"] as class [Date]", e);
		}

		if(date == null)
			return "";
		
		SimpleDateFormat dateFormat = DateFormatUtil.getDateFormat(config.getBuildFormatStr());
		
		return dateFormat.format(date);
	}

	public boolean canConvert(DocumentFieldConfiguration config) {
		return IntrospectUtil.isTypeMatch(Date.class, config.getType());
	}

}
