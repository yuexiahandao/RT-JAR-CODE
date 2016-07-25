package com.sun.corba.se.spi.activation;

import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBound;

public abstract interface InitialNameServiceOperations
{
  public abstract void bind(String paramString, org.omg.CORBA.Object paramObject, boolean paramBoolean)
    throws NameAlreadyBound;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.InitialNameServiceOperations
 * JD-Core Version:    0.6.2
 */