package com.netease.filebinding.core.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class NumberFormatUtil {
	
	private static ThreadLocal<Map<String, DecimalFormat>> numberformats = new ThreadLocal<Map<String, DecimalFormat>>(){
		@Override
		protected Map<String, DecimalFormat> initialValue() {
			return new HashMap<String, DecimalFormat>();
		}
	};
	
	public static DecimalFormat getNumberFormat(String formatStr){
		DecimalFormat numberFormat = numberformats.get().get(formatStr);
		
		if(numberFormat == null){
			numberFormat = new DecimalFormat();
			if(formatStr != null && formatStr.length() != 0)
				numberFormat.applyPattern(formatStr);
			
			numberFormat.setGroupingUsed(false);			
			numberformats.get().put(formatStr, numberFormat);
		}
		
		return numberFormat;
	}
}
