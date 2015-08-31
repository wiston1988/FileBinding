package filebinding.strategy;

import java.io.IOException;
import java.util.List;

import com.filebinding.core.exception.MappingException;
import com.filebinding.core.io.DocumentWriter;
import com.filebinding.core.strategy.base.DefaultBuildingStrategy;

public class ExportStrategy extends DefaultBuildingStrategy {

	public void build(DocumentWriter<String[]> output, List records) throws MappingException, IOException {
		
		super.build(output, records);			
	}

}
