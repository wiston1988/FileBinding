package filebinding.strategy;

import java.io.IOException;
import java.util.regex.Pattern;

import com.netease.filebinding.core.exception.FieldException;
import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.exception.RecordException;
import com.netease.filebinding.core.io.DocumentReader;
import com.netease.filebinding.core.strategy.base.template.HeaderMappingParsingStrategy;

import filebinding.model.AdvancedBean;

public class AdvancedImportStrategy extends HeaderMappingParsingStrategy {
	public Long rowNum = 1L;
	
	public void captureHeader(DocumentReader<String[]> reader)throws MappingException, IOException{
		super.defaultCaptureHeader(reader);
	}

	protected Object parseRecord(String[] record) throws MappingException {
		
		AdvancedBean bean = null;
		boolean isIgnoreerror = recordConfiguration.isIgnoreerror();
		
		try{
			rowNum = rowNum +1;
			bean = (AdvancedBean)super.defaultParseRecord(record);
			
		}catch(MappingException e){
			if(isIgnoreerror)
				return null;
			else{
				if (e instanceof FieldException) {
					log.error("Exception - " + e.getMessage());
					String columnName = "";
					for(int i=0;i<fieldConfigurations.size();i++){
						if(fieldConfigurations.get(i).getName().equalsIgnoreCase(e.getIdentifier())){
							columnName = fieldConfigurations.get(i).getColumnName();
							break;
						}
					}
					throw new RecordException(rowNum.toString(),columnName +" is invalid.");
				}else{
					throw e;
				}
			}
		}
		return bean;
	}
}
