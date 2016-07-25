package org.omg.DynamicAny;

import org.omg.DynamicAny.DynAnyPackage.InvalidValue;

public abstract interface DynEnumOperations extends DynAnyOperations
{
  public abstract String get_as_string();

  public abstract void set_as_string(String paramString)
    throws InvalidValue;

  public abstract int get_as_ulong();

  public abstract void set_as_ulong(int paramInt)
    throws InvalidValue;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynEnumOperations
 * JD-Core Version:    0.6.2
 */