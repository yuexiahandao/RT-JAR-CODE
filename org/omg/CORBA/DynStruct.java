package org.omg.CORBA;

import org.omg.CORBA.DynAnyPackage.InvalidSeq;

@Deprecated
public abstract interface DynStruct extends Object, DynAny
{
  public abstract String current_member_name();

  public abstract TCKind current_member_kind();

  public abstract NameValuePair[] get_members();

  public abstract void set_members(NameValuePair[] paramArrayOfNameValuePair)
    throws InvalidSeq;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DynStruct
 * JD-Core Version:    0.6.2
 */