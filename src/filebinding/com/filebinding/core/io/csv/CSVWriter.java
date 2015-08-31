package com.filebinding.core.io.csv;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.Writer;
import java.util.List;

import com.filebinding.core.io.DocumentWriter;

public class CSVWriter implements DocumentWriter<String[]>, CSVFeature{

	private BufferedWriter bw;

	private char separator;

	private char quotechar;//quotechar only works for element contains special character

	private char escapechar;
	
	//used for Class internal
	private boolean isAppend = false;

	public CSVWriter(Writer writer) {
		this(writer, DEFAULT_SEPARATOR);
	}

	public CSVWriter(Writer writer, char separator) {
		this(writer, separator, DEFAULT_QUOTE_CHARACTER);
	}

	public CSVWriter(Writer writer, char separator, char quotechar) {
		this(writer, separator, quotechar, DEFAULT_ESCAPE_CHARACTER);
	} 

	public CSVWriter(Writer writer, char separator, char quotechar, char escapechar) {
		this.bw = new BufferedWriter(writer);
		this.separator = separator == NO_SEPARATOR_CHARACTER ? DEFAULT_SEPARATOR : separator;//If separator is empty, use default value
		this.quotechar = quotechar;
		this.escapechar = escapechar;
	}

	public void writeAll(List<String[]> allLines) throws IOException {
		if(allLines == null)
			return;
		
		int size = allLines.size();
		
		for (int i = 0; i < size; i++) {
			writeNext(allLines.get(i));
		}
	}

	public void writeNext(String[] nextLine) throws IOException {
		if (nextLine == null)
			return;

		StringBuilder sb = new StringBuilder(INITIAL_BUFFER_SIZE);

		for (int i = 0; i < nextLine.length; i++) {

			if (i > 0) 
				sb.append(separator);

			String nextElement = nextLine[i];
			if (nextElement == null)
				continue;

			boolean hasSpecialChar = containSpecialChar(nextElement);
			boolean needQuote = (hasSpecialChar || nextElement.indexOf(separator) != -1) && quotechar != NO_QUOTE_CHARACTER;
			//quote element if there is special char or separator, and quote char is not empty
			if(needQuote)
				sb.append(quotechar);

			sb.append(hasSpecialChar ? processLine(nextElement) : nextElement);
			
			if(needQuote)
				sb.append(quotechar);
		}

		if(isAppend == false)//After first time for writing, set append to true
			isAppend = true;
		else//If appendant write, start from new line
			bw.newLine();
			
		bw.write(sb.toString());
	}

	private boolean containSpecialChar(String line) {
		return line.indexOf(quotechar) != -1 || line.indexOf(escapechar) != -1;
	}

	//hook
	protected StringBuilder processLine(String nextElement){
		StringBuilder sb = new StringBuilder(INITIAL_BUFFER_SIZE);
		for (int j = 0; j < nextElement.length(); j++) {
			char nextChar = nextElement.charAt(j);
			if (escapechar != NO_ESCAPE_CHARACTER && nextChar == quotechar) {
				sb.append(escapechar).append(nextChar);
			} else if (escapechar != NO_ESCAPE_CHARACTER && nextChar == escapechar) {
				sb.append(escapechar).append(nextChar);
			} else {
				sb.append(nextChar);
			}
		}

		return sb;
	}
	
	public void flush()throws IOException {
		bw.flush();
	}

	public void close()throws IOException {
		flush();
		bw.close();
	}
}
