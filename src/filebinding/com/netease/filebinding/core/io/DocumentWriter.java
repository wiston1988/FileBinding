package com.netease.filebinding.core.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface DocumentWriter<T> extends Closeable{

	public void writeAll(List<T> allLines)throws IOException;
	
	public void writeNext(T nextLine)throws IOException;
	
	public void flush()throws IOException;
}
