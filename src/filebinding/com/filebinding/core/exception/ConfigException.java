package com.filebinding.core.exception;

public class ConfigException extends GeneralException
{
    private static final long serialVersionUID = 1L;

    
    public ConfigException()
    {
        super();
    }
    
    public ConfigException(String error)
    {
        super(error);
    }

    public ConfigException(Throwable e)
    {
        super(e);
    }
    
    public ConfigException(String error, Throwable e)
    {
        super(error, e);
    }
}
