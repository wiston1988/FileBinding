package com.netease.filebinding.core.strategy;

import java.io.IOException;
import java.util.List;

import com.netease.filebinding.core.domain.DocumentParseResult;
import com.netease.filebinding.core.domain.DocumentParseSummary;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.io.DocumentReader;

public interface ParsingStrategy<P> extends FileBindStrategy{	
	public DocumentParseResult parse(DocumentReader<P> reader) throws MappingException, IOException;
	
//	@Deprecated
//	public List getBeans(String tag);
}