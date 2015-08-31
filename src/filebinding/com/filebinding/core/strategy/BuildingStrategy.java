package com.filebinding.core.strategy;

import java.io.IOException;
import java.util.List;

import com.filebinding.core.exception.MappingException;
import com.filebinding.core.io.DocumentWriter;

public interface BuildingStrategy<T> extends FileBindStrategy{
//	@Deprecated
//    public void addBeans(String tag, List records);
	
    public void build(final DocumentWriter<T> output,List records) throws MappingException, IOException;

}
