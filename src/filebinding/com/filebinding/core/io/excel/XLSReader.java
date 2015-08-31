package com.filebinding.core.io.excel;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * A very simple Excel reader, which read the first sheet of xls file, line by line.
 * 
 */

public class XLSReader extends ExcelReader{

	public XLSReader(InputStream input) throws IOException{
		this(input, 0);
	}
	
	public XLSReader(InputStream input, int skipLine) throws IOException{
		initialize(input, skipLine);
	}

	@Override
	void initialize(InputStream input, int skipLine) throws IOException{
		HSSFWorkbook workbook = new HSSFWorkbook(input);
		if(workbook != null)
			sheet = workbook.getSheetAt(0);

		if(sheet != null){
			currentRowNum = sheet.getFirstRowNum()+skipLine;
			lastRowNum = sheet.getLastRowNum();
		}
	}
}
