package filebinding.strategy;

import java.io.IOException;

import com.filebinding.core.exception.MappingException;
import com.filebinding.core.io.DocumentReader;
import com.filebinding.core.strategy.base.template.HeaderMappingParsingStrategy;

public class ImportStrategy extends HeaderMappingParsingStrategy {

	@Override
	protected void captureHeader(DocumentReader<String[]> reader)throws MappingException, IOException {
		super.captureHeader(reader);
	}

	@Override
	protected Object parseRecord(String[] record) throws MappingException {
		return super.parseRecord(record);
	}

}
