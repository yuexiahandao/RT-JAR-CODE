package org.omg.DynamicAny;

import org.omg.CORBA.TCKind;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

public abstract interface DynValueOperations extends DynValueCommonOperations
{
  public abstract String current_member_name()
    throws TypeMismatch, InvalidValue;

  public abstract TCKind current_member_kind()
    throws TypeMismatch, InvalidValue;

  public abstract NameValuePair[] get_members()
    throws InvalidValue;

  public abstract void set_members(NameValuePair[] paramArrayOfNameValuePair)
    throws TypeMismatch, InvalidValue;

  public abstract NameDynAnyPair[] get_members_as_dyn_any()
    throws InvalidValue;

  public abstract void set_members_as_dyn_any(NameDynAnyPair[] paramArrayOfNameDynAnyPair)
    throws TypeMismatch, InvalidValue;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynValueOperations
 * JD-Core Version:    0.6.2
 */