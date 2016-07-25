package org.omg.CORBA;

import org.omg.CORBA.DynAnyPackage.InvalidValue;

@Deprecated
public abstract interface DynFixed extends Object, DynAny
{
  public abstract byte[] get_value();

  public abstract void set_value(byte[] paramArrayOfByte)
    throws InvalidValue;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DynFixed
 * JD-Core Version:    0.6.2
 */