package org.omg.CORBA;

import org.omg.CORBA.DynAnyPackage.InvalidSeq;

@Deprecated
public abstract interface DynArray extends Object, DynAny
{
  public abstract Any[] get_elements();

  public abstract void set_elements(Any[] paramArrayOfAny)
    throws InvalidSeq;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DynArray
 * JD-Core Version:    0.6.2
 */