package org.omg.CORBA;

import java.io.Serializable;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.DynAnyPackage.TypeMismatch;

@Deprecated
public abstract interface DynAny extends Object
{
  public abstract TypeCode type();

  public abstract void assign(DynAny paramDynAny)
    throws Invalid;

  public abstract void from_any(Any paramAny)
    throws Invalid;

  public abstract Any to_any()
    throws Invalid;

  public abstract void destroy();

  public abstract DynAny copy();

  public abstract void insert_boolean(boolean paramBoolean)
    throws InvalidValue;

  public abstract void insert_octet(byte paramByte)
    throws InvalidValue;

  public abstract void insert_char(char paramChar)
    throws InvalidValue;

  public abstract void insert_short(short paramShort)
    throws InvalidValue;

  public abstract void insert_ushort(short paramShort)
    throws InvalidValue;

  public abstract void insert_long(int paramInt)
    throws InvalidValue;

  public abstract void insert_ulong(int paramInt)
    throws InvalidValue;

  public abstract void insert_float(float paramFloat)
    throws InvalidValue;

  public abstract void insert_double(double paramDouble)
    throws InvalidValue;

  public abstract void insert_string(String paramString)
    throws InvalidValue;

  public abstract void insert_reference(Object paramObject)
    throws InvalidValue;

  public abstract void insert_typecode(TypeCode paramTypeCode)
    throws InvalidValue;

  public abstract void insert_longlong(long paramLong)
    throws InvalidValue;

  public abstract void insert_ulonglong(long paramLong)
    throws InvalidValue;

  public abstract void insert_wchar(char paramChar)
    throws InvalidValue;

  public abstract void insert_wstring(String paramString)
    throws InvalidValue;

  public abstract void insert_any(Any paramAny)
    throws InvalidValue;

  public abstract void insert_val(Serializable paramSerializable)
    throws InvalidValue;

  public abstract Serializable get_val()
    throws TypeMismatch;

  public abstract boolean get_boolean()
    throws TypeMismatch;

  public abstract byte get_octet()
    throws TypeMismatch;

  public abstract char get_char()
    throws TypeMismatch;

  public abstract short get_short()
    throws TypeMismatch;

  public abstract short get_ushort()
    throws TypeMismatch;

  public abstract int get_long()
    throws TypeMismatch;

  public abstract int get_ulong()
    throws TypeMismatch;

  public abstract float get_float()
    throws TypeMismatch;

  public abstract double get_double()
    throws TypeMismatch;

  public abstract String get_string()
    throws TypeMismatch;

  public abstract Object get_reference()
    throws TypeMismatch;

  public abstract TypeCode get_typecode()
    throws TypeMismatch;

  public abstract long get_longlong()
    throws TypeMismatch;

  public abstract long get_ulonglong()
    throws TypeMismatch;

  public abstract char get_wchar()
    throws TypeMismatch;

  public abstract String get_wstring()
    throws TypeMismatch;

  public abstract Any get_any()
    throws TypeMismatch;

  public abstract DynAny current_component();

  public abstract boolean next();

  public abstract boolean seek(int paramInt);

  public abstract void rewind();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DynAny
 * JD-Core Version:    0.6.2
 */