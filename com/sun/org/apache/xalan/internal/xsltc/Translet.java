package com.sun.org.apache.xalan.internal.xsltc;

import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

public abstract interface Translet
{
  public abstract void transform(DOM paramDOM, SerializationHandler paramSerializationHandler)
    throws TransletException;

  public abstract void transform(DOM paramDOM, SerializationHandler[] paramArrayOfSerializationHandler)
    throws TransletException;

  public abstract void transform(DOM paramDOM, DTMAxisIterator paramDTMAxisIterator, SerializationHandler paramSerializationHandler)
    throws TransletException;

  public abstract Object addParameter(String paramString, Object paramObject);

  public abstract void buildKeys(DOM paramDOM, DTMAxisIterator paramDTMAxisIterator, SerializationHandler paramSerializationHandler, int paramInt)
    throws TransletException;

  public abstract void addAuxiliaryClass(Class paramClass);

  public abstract Class getAuxiliaryClass(String paramString);

  public abstract String[] getNamesArray();

  public abstract String[] getUrisArray();

  public abstract int[] getTypesArray();

  public abstract String[] getNamespaceArray();

  public abstract boolean useServicesMechnism();

  public abstract void setServicesMechnism(boolean paramBoolean);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.Translet
 * JD-Core Version:    0.6.2
 */