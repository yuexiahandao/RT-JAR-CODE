package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

public abstract interface DynValueBoxOperations extends DynValueCommonOperations
{
  public abstract Any get_boxed_value()
    throws InvalidValue;

  public abstract void set_boxed_value(Any paramAny)
    throws TypeMismatch;

  public abstract DynAny get_boxed_value_as_dyn_any()
    throws InvalidValue;

  public abstract void set_boxed_value_as_dyn_any(DynAny paramDynAny)
    throws TypeMismatch;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynValueBoxOperations
 * JD-Core Version:    0.6.2
 */