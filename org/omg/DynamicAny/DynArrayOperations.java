package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

public abstract interface DynArrayOperations extends DynAnyOperations
{
  public abstract Any[] get_elements();

  public abstract void set_elements(Any[] paramArrayOfAny)
    throws TypeMismatch, InvalidValue;

  public abstract DynAny[] get_elements_as_dyn_any();

  public abstract void set_elements_as_dyn_any(DynAny[] paramArrayOfDynAny)
    throws TypeMismatch, InvalidValue;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynArrayOperations
 * JD-Core Version:    0.6.2
 */