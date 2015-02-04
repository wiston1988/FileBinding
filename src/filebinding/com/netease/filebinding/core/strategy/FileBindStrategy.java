package com.netease.filebinding.core.strategy;

import com.netease.filebinding.core.config.DocumentMappingConfiguration;

interface FileBindStrategy {
	public void setMappingConfiguration(DocumentMappingConfiguration mappingConfiguration);
	
	public void setTag(String tag);
	
	public void setValue(Object key, Object value);
	
	public Object getValue(Object key);
}
