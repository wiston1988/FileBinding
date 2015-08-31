package com.filebinding.core.io.jdkAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.filebinding.core.io.DocumentReader;

public class DocumentBufferReader implements DocumentReader<String> {

	private BufferedReader br;
	private boolean hasNext = true;
	
	public DocumentBufferReader(Reader reader){
		br = new BufferedReader(reader);
	}
	
	public boolean hasNext() {
		return hasNext;
	}

	public List<String> readAll() throws IOException {
		List<String> allElements = new ArrayList<String>();
		
		while (hasNext) {
			String line = readNext();
			if(line != null)
				allElements.add(line);
		}
		
		return allElements;
	}

	public String readNext() throws IOException {
		String line = br.readLine();
		if(line == null)
			hasNext = false;
		
		if(line == null || line.length() == 0)
			return null;
		
		return line;
	}

	public void close() throws IOException {
		if(br != null)
			br.close();
	}
}
