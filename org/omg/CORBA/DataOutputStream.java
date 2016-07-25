package org.omg.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.ValueBase;

public abstract interface DataOutputStream extends ValueBase
{
  public abstract void write_any(Any paramAny);

  public abstract void write_boolean(boolean paramBoolean);

  public abstract void write_char(char paramChar);

  public abstract void write_wchar(char paramChar);

  public abstract void write_octet(byte paramByte);

  public abstract void write_short(short paramShort);

  public abstract void write_ushort(short paramShort);

  public abstract void write_long(int paramInt);

  public abstract void write_ulong(int paramInt);

  public abstract void write_longlong(long paramLong);

  public abstract void write_ulonglong(long paramLong);

  public abstract void write_float(float paramFloat);

  public abstract void write_double(double paramDouble);

  public abstract void write_string(String paramString);

  public abstract void write_wstring(String paramString);

  public abstract void write_Object(Object paramObject);

  public abstract void write_Abstract(java.lang.Object paramObject);

  public abstract void write_Value(Serializable paramSerializable);

  public abstract void write_TypeCode(TypeCode paramTypeCode);

  public abstract void write_any_array(Any[] paramArrayOfAny, int paramInt1, int paramInt2);

  public abstract void write_boolean_array(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2);

  public abstract void write_char_array(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract void write_wchar_array(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract void write_octet_array(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  public abstract void write_short_array(short[] paramArrayOfShort, int paramInt1, int paramInt2);

  public abstract void write_ushort_array(short[] paramArrayOfShort, int paramInt1, int paramInt2);

  public abstract void write_long_array(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract void write_ulong_array(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract void write_ulonglong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2);

  public abstract void write_longlong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2);

  public abstract void write_float_array(float[] paramArrayOfFloat, int paramInt1, int paramInt2);

  public abstract void write_double_array(double[] paramArrayOfDouble, int paramInt1, int paramInt2);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DataOutputStream
 * JD-Core Version:    0.6.2
 */