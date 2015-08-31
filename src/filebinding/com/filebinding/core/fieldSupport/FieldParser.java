package com.filebinding.core.fieldSupport;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.exception.MappingException;

public interface FieldParser extends FieldConverter {
	public Object getParsedValue(String fieldValue, Object pojo, DocumentFieldConfiguration config)throws MappingException;
}