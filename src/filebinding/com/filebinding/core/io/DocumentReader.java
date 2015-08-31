package com.filebinding.core.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface DocumentReader<T> extends Closeable{
	public List<T> readAll() throws IOException;//Read all lines except for null
	
	public T readNext() throws IOException;//If line is empty, return null
	
	public boolean hasNext();
}
