package org.omg.CORBA.portable;

import org.omg.CORBA.SystemException;

public abstract interface InvokeHandler
{
  public abstract OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
    throws SystemException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.portable.InvokeHandler
 * JD-Core Version:    0.6.2
 */