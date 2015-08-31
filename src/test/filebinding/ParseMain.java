package filebinding;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.filebinding.core.domain.DocumentParseResult;
import com.filebinding.core.domain.DocumentParseSummary;
import com.filebinding.core.exception.ConfigException;
import com.filebinding.core.exception.RecordException;
import com.filebinding.core.io.DocumentReader;
import com.filebinding.core.io.csv.CSVReader;
import com.filebinding.core.io.excel.XLSReader;
import com.filebinding.core.io.excel.XLSXReader;
import com.filebinding.core.io.jdkAdapter.DocumentBufferReader;
import com.filebinding.core.strategy.ParsingStrategy;

import filebinding.model.*;
import filebinding.utils.DocumentConfigFactory;

public class ParseMain {

	/**
	 * @param args
	 */
	public static final String XLSX_FILE = "filebinding/testImport.xlsx";
	public static final String XLS_FILE = "filebinding/testImport.xls";
	public static final String CSV_FILE = "filebinding/testImport.csv";
	public static final String TXT_FILE = "filebinding/testImport.txt";
	public static final String NO_HEADER_FILE = "filebinding/testImportWithoutHeader.csv";
	public static final String ADVANCED_IMPORT_FILE = "filebinding/advancedImport.xlsx";
	
	private static final String NO_HEADER_TAG="[ImportWithoutBindingColumn]";
	private static final String COMMON_TAG = "[TestImport]";
	private static final String ADVANCED_IMPORT_TAG = "[AdvancedImport]";
	private static final String IMPORT_TXT_TAG = "[ImportTextFile]";
	
	private void testParseCSV(DocumentConfigFactory docConfigFactory){
		try{

			/****2. Adapter IO**/
			InputStream testCsvStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(CSV_FILE);
			DocumentReader csvReader =  new CSVReader(new BufferedReader(new InputStreamReader(testCsvStream)));
			
			/***3. get parser from ConfigFactory***/
			ParsingStrategy parser = docConfigFactory.getParser(COMMON_TAG);
			
			/***4. parse and getBeans***/
			DocumentParseResult result = parser.parse(csvReader);
			
			List resultList = result.getParsedData();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				Teacher tc = (Teacher)resultList.get(i);
				System.out.println(tc.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	private void testParseXLS(DocumentConfigFactory docConfigFactory){
		try{
			
			/****2. Adapter IO**/
			InputStream testExcelStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(XLS_FILE);
			DocumentReader excelReader =  new XLSReader(testExcelStream);
			
			/***3. get parser from ConfigFactory***/
			ParsingStrategy parser = docConfigFactory.getParser(COMMON_TAG);
			
			/***4. parse and getBeans***/
			DocumentParseResult result = parser.parse(excelReader);
			List resultList = result.getParsedData();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				Teacher tc = (Teacher)resultList.get(i);
				System.out.println(tc.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	private void testParseXLSX(DocumentConfigFactory docConfigFactory){
		try{
			
			/****2. Adapter IO**/
			InputStream testExcelStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(XLSX_FILE);
			DocumentReader excelReader =  new XLSXReader(testExcelStream);
			
			/***3. get parser from ConfigFactory***/
			ParsingStrategy parser = docConfigFactory.getParser(COMMON_TAG);
			
			/***4. parse and getBeans***/
			DocumentParseResult result = parser.parse(excelReader);
			List resultList = result.getParsedData();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				Teacher tc = (Teacher)resultList.get(i);
				System.out.println(tc.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	private void testAdvancedParser(DocumentConfigFactory docConfigFactory){
		try{
			
			/****2. Adapter IO**/
			InputStream testExcelStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ADVANCED_IMPORT_FILE);
			DocumentReader excelReader =  new XLSXReader(testExcelStream);
			
			/***3. get parser from ConfigFactory***/
			ParsingStrategy parser = docConfigFactory.getParser(ADVANCED_IMPORT_TAG);
			
			/***4. parse and getBeans***/
			DocumentParseResult result = parser.parse(excelReader);
			List resultList = result.getParsedData();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				System.out.println(resultList.get(i).toString());
			}

		}catch(RecordException e){
			System.err.println("record:"+e.getIdentifier()+" reason:"+e.getMessage());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private void testParseNoHeaderFile(DocumentConfigFactory docConfigFactory){
		try{

			/****2. Adapter IO**/
			InputStream testCsvStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(NO_HEADER_FILE);
			DocumentReader csvReader =  new CSVReader(new BufferedReader(new InputStreamReader(testCsvStream)));
			
			/***3. get parser from ConfigFactory***/
			ParsingStrategy parser = docConfigFactory.getParser(NO_HEADER_TAG);
			
			/***4. parse and getBeans***/
			DocumentParseResult result = parser.parse(csvReader);
			
			List resultList = result.getParsedData();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				Teacher tc = (Teacher)resultList.get(i);
				System.out.println(tc.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	private void testParseTXT(DocumentConfigFactory docConfigFactory){
		try{

			/****2. Adapter IO**/
			InputStream testCsvStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(TXT_FILE);
			DocumentReader txtReader =  new DocumentBufferReader(new BufferedReader(new InputStreamReader(testCsvStream)));
			
			/***3. get parser from ConfigFactory***/
			ParsingStrategy parser = docConfigFactory.getParser(IMPORT_TXT_TAG);
			
			/***4. parse and getBeans***/
			DocumentParseResult result = parser.parse(txtReader);
			
			List resultList = result.getParsedData();
			System.out.println("the record size:"+resultList.size());
			for(int i=0;i<resultList.size();i++){
				Teacher tc = (Teacher)resultList.get(i);
				System.out.println(tc.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	public static void main(String[] args) {
		/***1. init config, if you are using spring, you can config this factory as a serice in spring context***/
		DocumentConfigFactory docConfigFactory;
		try {
			docConfigFactory = new DocumentConfigFactory();
			//测试没有header的文件解析
			System.out.println("================测试没有header的文件解析==============");
			new ParseMain().testParseNoHeaderFile(docConfigFactory);
			//测试包含header的文件解析
			System.out.println("================测试包含header的CSV文件解析==============");
			new ParseMain().testParseCSV(docConfigFactory);
			System.out.println("================测试包含header的XLSX文件解析==============");
			new ParseMain().testParseXLSX(docConfigFactory);
			System.out.println("================测试包含header的XLS文件解析==============");
			new ParseMain().testParseXLS(docConfigFactory);
			System.out.println("================测试包含各个类型字段的XLSX文件解析==============");
			new ParseMain().testAdvancedParser(docConfigFactory);
			//测试列名表示成字符位置的文件
			System.out.println("================测试列名表示成字符位置的TXT文件解析==============");
			new ParseMain().testParseTXT(docConfigFactory);
		} catch (ConfigException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
