package com.sun.org.omg.CORBA.portable;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.BoxedValueHelper;

@Deprecated
public abstract interface ValueHelper extends BoxedValueHelper
{
  public abstract Class get_class();

  public abstract String[] get_truncatable_base_ids();

  public abstract TypeCode get_type();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA.portable.ValueHelper
 * JD-Core Version:    0.6.2
 */