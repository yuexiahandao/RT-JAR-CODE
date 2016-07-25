package com.sun.xml.internal.ws.api.model;

import com.sun.xml.internal.bind.api.Bridge;
import javax.jws.WebParam.Mode;
import javax.xml.namespace.QName;

public abstract interface Parameter
{
  public abstract SEIModel getOwner();

  public abstract JavaMethod getParent();

  public abstract QName getName();

  public abstract Bridge getBridge();

  public abstract WebParam.Mode getMode();

  public abstract int getIndex();

  public abstract boolean isWrapperStyle();

  public abstract boolean isReturnValue();

  public abstract ParameterBinding getBinding();

  public abstract ParameterBinding getInBinding();

  public abstract ParameterBinding getOutBinding();

  public abstract boolean isIN();

  public abstract boolean isOUT();

  public abstract boolean isINOUT();

  public abstract boolean isResponse();

  public abstract Object getHolderValue(Object paramObject);

  public abstract String getPartName();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.Parameter
 * JD-Core Version:    0.6.2
 */