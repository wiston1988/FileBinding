package com.netease.filebinding.core.exception;


public class RecordException extends MappingException {
	private static final long serialVersionUID = -3673818951344343679L;

	public RecordException(String recordIdentifier)
	{
		super(recordIdentifier);
		setErrorLevel(RECORD_LEVEL);
	}

	public RecordException(String recordIdentifier, String reason)
	{
		super(recordIdentifier, reason);
		setErrorLevel(RECORD_LEVEL);
		
	}

	public RecordException(String recordIdentifier, Throwable e)
	{
		super(recordIdentifier, e);
		setErrorLevel(RECORD_LEVEL);
	}

	public RecordException(String recordIdentifier, String reason, Throwable e)
	{
		super(recordIdentifier, reason, e);
		setErrorLevel(RECORD_LEVEL);
	}
}
