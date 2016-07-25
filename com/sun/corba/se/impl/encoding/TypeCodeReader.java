package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.corba.TypeCodeImpl;
import org.omg.CORBA_2_3.portable.InputStream;

public abstract interface TypeCodeReader extends MarshalInputStream
{
  public abstract void addTypeCodeAtPosition(TypeCodeImpl paramTypeCodeImpl, int paramInt);

  public abstract TypeCodeImpl getTypeCodeAtPosition(int paramInt);

  public abstract void setEnclosingInputStream(InputStream paramInputStream);

  public abstract TypeCodeReader getTopLevelStream();

  public abstract int getTopLevelPosition();

  public abstract int getPosition();

  public abstract void printTypeMap();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.TypeCodeReader
 * JD-Core Version:    0.6.2
 */