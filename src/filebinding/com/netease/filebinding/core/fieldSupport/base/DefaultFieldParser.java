package com.netease.filebinding.core.fieldSupport.base;

import com.netease.filebinding.core.config.DocumentFieldConfiguration;
import com.netease.filebinding.core.exception.FieldException;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.fieldSupport.FieldParser;
import com.netease.filebinding.core.util.BaseTypeConverter;

public class DefaultFieldParser implements FieldParser {

	public Object getParsedValue(String fieldValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		try{
			return BaseTypeConverter.getInstance().convertIfNecessary(fieldValue, config.getType());
		}catch(Exception e){
			throw new FieldException(config.getName(), "Failed to Convert Value ["+fieldValue+"] for class ["+config.getType()+"]", e);
		}
	}

	public boolean canConvert(DocumentFieldConfiguration config) {
		return true;
	}
}
