package com.netease.filebinding.core.strategy.base.template;

import com.netease.filebinding.core.exception.MappingException;
import com.netease.filebinding.core.exception.RecordException;

/***
 * In this Strategy, columnName is the length for column.
 * ***/
public  class SubstrByLenthParsingStrategy extends SubstrParsingStrategy {
	private int start = 0;	
	
	@Override
	protected SubstrExpress buildExpress(String columnName) throws MappingException {
		int strLength =0;
		try{
			strLength = Integer.valueOf(columnName);
		}catch(NumberFormatException e){
			throw new RecordException(ERRORCONFIGMSG);
		}
		
		int end = start+strLength;
		SubstrExpress express = new SubstrExpress(start,end);
		start = end;//next field will use the new start offset
		return express;
	}
}
