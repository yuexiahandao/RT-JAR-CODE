package org.omg.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.ValueBase;

public abstract interface DataInputStream extends ValueBase
{
  public abstract Any read_any();

  public abstract boolean read_boolean();

  public abstract char read_char();

  public abstract char read_wchar();

  public abstract byte read_octet();

  public abstract short read_short();

  public abstract short read_ushort();

  public abstract int read_long();

  public abstract int read_ulong();

  public abstract long read_longlong();

  public abstract long read_ulonglong();

  public abstract float read_float();

  public abstract double read_double();

  public abstract String read_string();

  public abstract String read_wstring();

  public abstract Object read_Object();

  public abstract java.lang.Object read_Abstract();

  public abstract Serializable read_Value();

  public abstract TypeCode read_TypeCode();

  public abstract void read_any_array(AnySeqHolder paramAnySeqHolder, int paramInt1, int paramInt2);

  public abstract void read_boolean_array(BooleanSeqHolder paramBooleanSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_char_array(CharSeqHolder paramCharSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_wchar_array(WCharSeqHolder paramWCharSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_octet_array(OctetSeqHolder paramOctetSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_short_array(ShortSeqHolder paramShortSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_ushort_array(UShortSeqHolder paramUShortSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_long_array(LongSeqHolder paramLongSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_ulong_array(ULongSeqHolder paramULongSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_ulonglong_array(ULongLongSeqHolder paramULongLongSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_longlong_array(LongLongSeqHolder paramLongLongSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_float_array(FloatSeqHolder paramFloatSeqHolder, int paramInt1, int paramInt2);

  public abstract void read_double_array(DoubleSeqHolder paramDoubleSeqHolder, int paramInt1, int paramInt2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DataInputStream
 * JD-Core Version:    0.6.2
 */