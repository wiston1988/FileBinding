
package com.netease.filebinding.core.io.jdkAdapter;

import java.io.IOException;
import java.util.List;
import java.io.Writer;
import java.io.BufferedWriter;

import com.netease.filebinding.core.io.DocumentWriter;

public class DocumentBufferWriter implements DocumentWriter<String> {

	private final BufferedWriter bw;
	private boolean isAppend = false;
	
	public DocumentBufferWriter(Writer writer){
		bw = new BufferedWriter(writer);
	}
	
	public void flush() throws IOException {
		bw.flush();
	}

	public void writeAll(List<String> allLines) throws IOException {
		if(allLines == null)
			return;
		
		int size = allLines.size();
		
		for (int i = 0; i < size; i++) {
			writeNext(allLines.get(i));
		}
	}

	public void writeNext(String nextLine) throws IOException {
		if(isAppend == false)//After first time for writing, set append to true
			isAppend = true;
		else//If appendant write(not first line), start from new line
			bw.newLine();
		
		bw.write(nextLine);
	}

	public void close() throws IOException {
		flush();
		bw.close();
	}

}
