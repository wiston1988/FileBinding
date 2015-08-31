package filebinding.renders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.filebinding.core.config.DocumentFieldConfiguration;
import com.filebinding.core.exception.FieldException;
import com.filebinding.core.exception.MappingException;
import com.filebinding.core.fieldSupport.FieldRenderer;

public class NumDateRenderer extends FieldRenderer {
	public static final String BUSSINESSDAE_DATEFMT_STR = "yyyyMMdd";
	
	public String getRenderedValue(Object pojoValue, Object pojo, DocumentFieldConfiguration config) throws MappingException {
		
		if(config == null || config.getBuildFormatStr() == null)
			throw new FieldException(config.getName(), "Invalid Format Expression for NumDate.");
		
		if(pojoValue == null)
			return "";
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		dateFormat.applyPattern(config.getBuildFormatStr());
		
		try{
			Date dateValue = fromNumDate((Long)pojoValue);
			return dateFormat.format(dateValue);
		}catch(ParseException e){
			throw new FieldException(config.getName(),pojoValue.toString(), e);
		}
	}
	private static Date fromNumDate(Long numDate) throws ParseException{
		DateFormat bussinessDateFormat = new SimpleDateFormat(BUSSINESSDAE_DATEFMT_STR);
		return bussinessDateFormat.parse(String.valueOf(numDate.longValue()));
	}

	public boolean canConvert(DocumentFieldConfiguration config) {		
		return false;
	}
}
