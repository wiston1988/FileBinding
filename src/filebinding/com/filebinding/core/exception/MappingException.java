package com.filebinding.core.exception;

public abstract class MappingException extends GeneralException
{
	private static final long serialVersionUID = 1298927821129948455L;
	public final static int RECORD_LEVEL = 0;
	public final static int FIELD_LEVEL = 1;
	
	private int errorLevel;
	private String identifier;
	
	public MappingException(String identifier)
	{
		super();
		this.identifier = identifier;
	}

	public MappingException(String identifier, String reason)
	{
		super(reason);
		this.identifier = identifier;
	}

	public MappingException(String identifier, Throwable e)
	{
		super(e);
		this.identifier = identifier;
	}

	public MappingException(String identifier, String reason, Throwable e)
	{
		super(reason, e);
		this.identifier = identifier;
	}

	public int getErrorLevel() 
	{
		return errorLevel;
	}

	public void setErrorLevel(int errorLevel) 
	{
		this.errorLevel = errorLevel;
	}

	public String getIdentifier()
	{
		return identifier;
	}
}
