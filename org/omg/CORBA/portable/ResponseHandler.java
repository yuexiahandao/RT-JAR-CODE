package org.omg.CORBA.portable;

public abstract interface ResponseHandler
{
  public abstract OutputStream createReply();

  public abstract OutputStream createExceptionReply();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.portable.ResponseHandler
 * JD-Core Version:    0.6.2
 */