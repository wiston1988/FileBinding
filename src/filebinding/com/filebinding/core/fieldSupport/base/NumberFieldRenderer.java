package com.filebinding.core.fieldSupport.base;

import java.text.NumberFormat;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.fieldSupport.FieldRenderer;
import com.filebinding.core.util.NumberFormatUtil;
import com.filebinding.core.util.introspector.IntrospectUtil;

public class NumberFieldRenderer extends FieldRenderer {

	public String getRenderedValue(Object pojoValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		if(pojoValue == null)
			return "";
		
		NumberFormat numberFormat = NumberFormatUtil.getNumberFormat(config.getBuildFormatStr());
		
		return numberFormat.format(pojoValue);
	}

	public boolean canConvert(DocumentFieldConfiguration config) {
		return IntrospectUtil.isTypeMatch(Number.class, config.getType());
	}
}
