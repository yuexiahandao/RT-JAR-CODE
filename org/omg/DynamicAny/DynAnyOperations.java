package org.omg.DynamicAny;

import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

public abstract interface DynAnyOperations
{
  public abstract TypeCode type();

  public abstract void assign(DynAny paramDynAny)
    throws TypeMismatch;

  public abstract void from_any(Any paramAny)
    throws TypeMismatch, InvalidValue;

  public abstract Any to_any();

  public abstract boolean equal(DynAny paramDynAny);

  public abstract void destroy();

  public abstract DynAny copy();

  public abstract void insert_boolean(boolean paramBoolean)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_octet(byte paramByte)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_char(char paramChar)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_short(short paramShort)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_ushort(short paramShort)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_long(int paramInt)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_ulong(int paramInt)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_float(float paramFloat)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_double(double paramDouble)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_string(String paramString)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_reference(org.omg.CORBA.Object paramObject)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_typecode(TypeCode paramTypeCode)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_longlong(long paramLong)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_ulonglong(long paramLong)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_wchar(char paramChar)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_wstring(String paramString)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_any(Any paramAny)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_dyn_any(DynAny paramDynAny)
    throws TypeMismatch, InvalidValue;

  public abstract void insert_val(Serializable paramSerializable)
    throws TypeMismatch, InvalidValue;

  public abstract boolean get_boolean()
    throws TypeMismatch, InvalidValue;

  public abstract byte get_octet()
    throws TypeMismatch, InvalidValue;

  public abstract char get_char()
    throws TypeMismatch, InvalidValue;

  public abstract short get_short()
    throws TypeMismatch, InvalidValue;

  public abstract short get_ushort()
    throws TypeMismatch, InvalidValue;

  public abstract int get_long()
    throws TypeMismatch, InvalidValue;

  public abstract int get_ulong()
    throws TypeMismatch, InvalidValue;

  public abstract float get_float()
    throws TypeMismatch, InvalidValue;

  public abstract double get_double()
    throws TypeMismatch, InvalidValue;

  public abstract String get_string()
    throws TypeMismatch, InvalidValue;

  public abstract org.omg.CORBA.Object get_reference()
    throws TypeMismatch, InvalidValue;

  public abstract TypeCode get_typecode()
    throws TypeMismatch, InvalidValue;

  public abstract long get_longlong()
    throws TypeMismatch, InvalidValue;

  public abstract long get_ulonglong()
    throws TypeMismatch, InvalidValue;

  public abstract char get_wchar()
    throws TypeMismatch, InvalidValue;

  public abstract String get_wstring()
    throws TypeMismatch, InvalidValue;

  public abstract Any get_any()
    throws TypeMismatch, InvalidValue;

  public abstract DynAny get_dyn_any()
    throws TypeMismatch, InvalidValue;

  public abstract Serializable get_val()
    throws TypeMismatch, InvalidValue;

  public abstract boolean seek(int paramInt);

  public abstract void rewind();

  public abstract boolean next();

  public abstract int component_count();

  public abstract DynAny current_component()
    throws TypeMismatch;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny.DynAnyOperations
 * JD-Core Version:    0.6.2
 */