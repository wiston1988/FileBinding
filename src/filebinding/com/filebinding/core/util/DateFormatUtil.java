package com.filebinding.core.util;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DateFormatUtil {
	private static ThreadLocal<Map<String, SimpleDateFormat>> dateformats = new ThreadLocal<Map<String, SimpleDateFormat>>(){
		@Override
		protected Map<String, SimpleDateFormat> initialValue() {
			return new HashMap<String, SimpleDateFormat>();
		}
	};
	
	public static SimpleDateFormat getDateFormat(String formatStr){
		SimpleDateFormat dateFormat = dateformats.get().get(formatStr);
		
		if(dateFormat == null){
			dateFormat = new SimpleDateFormat();
			if(formatStr != null && formatStr.trim().length() != 0)
				dateFormat.applyPattern(formatStr.trim());
			
			dateformats.get().put(formatStr.trim(), dateFormat);
		}
		
		return dateFormat;
	}
}
