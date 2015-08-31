package com.filebinding.core.fieldSupport.base;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.exception.FieldException;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.fieldSupport.FieldRenderer;
import com.filebinding.core.util.BaseTypeConverter;

public class DefaultFieldRenderer extends FieldRenderer {

	public String getRenderedValue(Object pojoValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		try{
			return (String)BaseTypeConverter.getInstance().convertIfNecessary(pojoValue, String.class);
		}catch(Exception e){
			throw new FieldException(config.getName(), "Failed to Convert Value ["+pojoValue+"] to a String.", e);
		}
	}

	public boolean canConvert(DocumentFieldConfiguration config) {
		return true;
	}
}
