package com.filebinding.core.exception;

public class GeneralException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public GeneralException()
    {
        super();
    }
    
    public GeneralException(String reason)
    {
        super(reason);
    }
    
    public GeneralException(Throwable t)
    {
        super(t);
    }
    
    public GeneralException(String reason, Throwable t)
    {
        super(reason, t);
    }

}
