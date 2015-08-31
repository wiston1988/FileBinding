package com.filebinding.core.fieldSupport;

import com.filebinding.core.config.DocumentFieldConfiguration;

public interface FieldConverter {
	public boolean canConvert(DocumentFieldConfiguration config);
}
