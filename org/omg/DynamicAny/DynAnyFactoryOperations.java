package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;

public abstract interface DynAnyFactoryOperations
{
  public abstract DynAny create_dyn_any(Any paramAny)
    throws InconsistentTypeCode;

  public abstract DynAny create_dyn_any_from_type_code(TypeCode paramTypeCode)
    throws InconsistentTypeCode;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynAnyFactoryOperations
 * JD-Core Version:    0.6.2
 */