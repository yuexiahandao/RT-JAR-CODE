package com.sun.corba.se.impl.presentation.rmi;

import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA_2_3.portable.OutputStream;

public abstract interface ExceptionHandler
{
  public abstract boolean isDeclaredException(Class paramClass);

  public abstract void writeException(OutputStream paramOutputStream, Exception paramException);

  public abstract Exception readException(ApplicationException paramApplicationException);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.ExceptionHandler
 * JD-Core Version:    0.6.2
 */