package com.filebinding.core.strategy;

import java.io.IOException;
import java.util.List;

import com.filebinding.core.domain.DocumentParseResult;
import com.filebinding.core.domain.DocumentParseSummary;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.io.DocumentReader;

public interface ParsingStrategy<P> extends FileBindStrategy{	
	public DocumentParseResult parse(DocumentReader<P> reader) throws MappingException, IOException;
	
//	@Deprecated
//	public List getBeans(String tag);
}