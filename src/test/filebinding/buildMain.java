package filebinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import com.filebinding.core.io.DocumentWriter;
import com.filebinding.core.io.csv.CSVWriter;
import com.filebinding.core.io.excel.XLSXWriter;
import com.filebinding.core.strategy.BuildingStrategy;

import filebinding.model.People;
import filebinding.utils.DocumentConfigFactory;

public class buildMain {

	/**
	 * @param args
	 */
	private static final String TAG = "[TestExport]";
	
	private void testCSV(){
		File file=new File("src/test/filebinding/testExport.csv");
		try{
			/***1. init config***/
			DocumentConfigFactory docConfigFactory = new DocumentConfigFactory();
			/***2. get builderStrategy***/
			BuildingStrategy builder = docConfigFactory.getBuilder(TAG); 
			/***3. init CSVWriter***/
			StringWriter contentWriter = new StringWriter();
			DocumentWriter<String[]> docWriter = new CSVWriter(contentWriter);
			/***4. fill Content***/
			List<People> peopleList = new ArrayList();
			People people = new People();
			for(int i=0; i<10; i++){
				people.setName("陈慧");
				Long bithday = new Long(19880311);
				people.setBirthday(bithday);
				peopleList.add(people);
			}
			builder.build(docWriter,peopleList);	
			docWriter.flush(); 
			FileOutputStream out = new FileOutputStream(file);
			out.write(contentWriter.getBuffer().toString().getBytes("gbk"));
			out.flush();
			out.close();
			System.out.println("write csv success");
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	private void testXLSX(){
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		File file=new File("src/test/filebinding/testExport.xlsx");
			try{
				/***1. init config***/
				DocumentConfigFactory docConfigFactory = new DocumentConfigFactory();
				/***2. get builderStrategy***/
				BuildingStrategy builder = docConfigFactory.getBuilder(TAG); 
				/***3. init CSVWriter***/
				DocumentWriter<Object[]> docWriter = new XLSXWriter(output,"testSheet");
				/***4. fill Content***/
				List<People> peopleList = new ArrayList();
				People people = new People();
				for(int i=0; i<10; i++){
					people.setName("陈慧");
					Long bithday = new Long(19880311);
					people.setBirthday(bithday);
					peopleList.add(people);
				}
				builder.build(docWriter,peopleList);	
				docWriter.flush(); 
				docWriter.close();
				FileOutputStream out = new FileOutputStream(file);
				output.writeTo(out);
				System.out.println("write xlsx success");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try{
					output.close();
				}catch(IOException ioE){
					output = null;
				}
			}
	}
	
	public static void main(String[] args) {
		new buildMain().testCSV();
		new buildMain().testXLSX();
	}

}
