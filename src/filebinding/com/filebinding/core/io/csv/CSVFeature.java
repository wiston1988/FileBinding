package com.filebinding.core.io.csv;

public interface CSVFeature {
	
	public static final int INITIAL_BUFFER_SIZE = 128;

	public static final char DEFAULT_SEPARATOR = ',';
	public static final char NO_SEPARATOR_CHARACTER = '\u0000';
	
	public static final char DEFAULT_QUOTE_CHARACTER = '"';
	public static final char NO_QUOTE_CHARACTER = '\u0000';
	
	/** The character used for escaping quotes. */
	public static final char DEFAULT_ESCAPE_CHARACTER = '"';
	/** The escape constant to use when you wish to suppress all escaping. */
	public static final char NO_ESCAPE_CHARACTER = '\u0000';
	
	/***Reader specific***/
	public static final int DEFAULT_SKIP_LINES = 0;//The default line to start reading.
}
