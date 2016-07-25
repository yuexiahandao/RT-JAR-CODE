package org.omg.PortableInterceptor;

import org.omg.CORBA.portable.ValueBase;

public abstract interface ObjectReferenceFactory extends ValueBase
{
  public abstract org.omg.CORBA.Object make_object(String paramString, byte[] paramArrayOfByte);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableInterceptor.ObjectReferenceFactory
 * JD-Core Version:    0.6.2
 */