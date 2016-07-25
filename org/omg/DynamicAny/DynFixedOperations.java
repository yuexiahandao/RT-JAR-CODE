package org.omg.DynamicAny;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

public abstract interface DynFixedOperations extends DynAnyOperations
{
  public abstract String get_value();

  public abstract boolean set_value(String paramString)
    throws TypeMismatch, InvalidValue;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynFixedOperations
 * JD-Core Version:    0.6.2
 */