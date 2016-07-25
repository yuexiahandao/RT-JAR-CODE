package com.sun.corba.se.impl.encoding;

import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;

public abstract interface MarshalInputStream
{
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

  public abstract void read_boolean_array(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2);

  public abstract void read_char_array(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract void read_wchar_array(char[] paramArrayOfChar, int paramInt1, int paramInt2);

  public abstract void read_octet_array(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  public abstract void read_short_array(short[] paramArrayOfShort, int paramInt1, int paramInt2);

  public abstract void read_ushort_array(short[] paramArrayOfShort, int paramInt1, int paramInt2);

  public abstract void read_long_array(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract void read_ulong_array(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public abstract void read_longlong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2);

  public abstract void read_ulonglong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2);

  public abstract void read_float_array(float[] paramArrayOfFloat, int paramInt1, int paramInt2);

  public abstract void read_double_array(double[] paramArrayOfDouble, int paramInt1, int paramInt2);

  public abstract org.omg.CORBA.Object read_Object();

  public abstract TypeCode read_TypeCode();

  public abstract Any read_any();

  public abstract Principal read_Principal();

  public abstract org.omg.CORBA.Object read_Object(Class paramClass);

  public abstract Serializable read_value()
    throws Exception;

  public abstract void consumeEndian();

  public abstract int getPosition();

  public abstract void mark(int paramInt);

  public abstract void reset();

  public abstract void performORBVersionSpecificInit();

  public abstract void resetCodeSetConverters();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.MarshalInputStream
 * JD-Core Version:    0.6.2
 */