package com.sun.xml.internal.ws.api.model;

import com.sun.xml.internal.bind.api.Bridge;

public abstract interface CheckedException
{
  public abstract SEIModel getOwner();

  public abstract JavaMethod getParent();

  public abstract Class getExceptionClass();

  public abstract Class getDetailBean();

  public abstract Bridge getBridge();

  public abstract ExceptionType getExceptionType();

  public abstract String getMessageName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.CheckedException
 * JD-Core Version:    0.6.2
 */