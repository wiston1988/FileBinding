package com.netease.filebinding.core.fieldSupport;

import com.netease.filebinding.core.config.DocumentFieldConfiguration;
import com.netease.filebinding.core.exception.MappingException;

public abstract class FieldRenderer implements FieldConverter{	
	public abstract String getRenderedValue(Object pojoValue, Object pojo, DocumentFieldConfiguration config)throws MappingException;

	public Object getRenderedObjectValue(Object pojoValue, Object pojo, DocumentFieldConfiguration config)throws MappingException
	{
		return getRenderedValue( pojoValue,  pojo,  config);
	}

}