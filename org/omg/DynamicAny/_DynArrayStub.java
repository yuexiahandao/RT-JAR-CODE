/*      */ package org.omg.DynamicAny;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Properties;
/*      */ import org.omg.CORBA.Any;
/*      */ import org.omg.CORBA.ORB;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.portable.Delegate;
/*      */ import org.omg.CORBA.portable.ObjectImpl;
/*      */ import org.omg.CORBA.portable.ServantObject;
/*      */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*      */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*      */ 
/*      */ public class _DynArrayStub extends ObjectImpl
/*      */   implements DynArray
/*      */ {
/*   19 */   public static final Class _opsClass = DynArrayOperations.class;
/*      */ 
/* 1131 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynArray:1.0", "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*      */ 
/*      */   public Any[] get_elements()
/*      */   {
/*   28 */     ServantObject localServantObject = _servant_preinvoke("get_elements", _opsClass);
/*   29 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   32 */       return localDynArrayOperations.get_elements();
/*      */     } finally {
/*   34 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_elements(Any[] paramArrayOfAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   47 */     ServantObject localServantObject = _servant_preinvoke("set_elements", _opsClass);
/*   48 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   51 */       localDynArrayOperations.set_elements(paramArrayOfAny);
/*      */     } finally {
/*   53 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny[] get_elements_as_dyn_any()
/*      */   {
/*   63 */     ServantObject localServantObject = _servant_preinvoke("get_elements_as_dyn_any", _opsClass);
/*   64 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   67 */       return localDynArrayOperations.get_elements_as_dyn_any();
/*      */     } finally {
/*   69 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_elements_as_dyn_any(DynAny[] paramArrayOfDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   82 */     ServantObject localServantObject = _servant_preinvoke("set_elements_as_dyn_any", _opsClass);
/*   83 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   86 */       localDynArrayOperations.set_elements_as_dyn_any(paramArrayOfDynAny);
/*      */     } finally {
/*   88 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*  104 */     ServantObject localServantObject = _servant_preinvoke("type", _opsClass);
/*  105 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  108 */       return localDynArrayOperations.type();
/*      */     } finally {
/*  110 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assign(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*  126 */     ServantObject localServantObject = _servant_preinvoke("assign", _opsClass);
/*  127 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  130 */       localDynArrayOperations.assign(paramDynAny);
/*      */     } finally {
/*  132 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void from_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  147 */     ServantObject localServantObject = _servant_preinvoke("from_any", _opsClass);
/*  148 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  151 */       localDynArrayOperations.from_any(paramAny);
/*      */     } finally {
/*  153 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any to_any()
/*      */   {
/*  167 */     ServantObject localServantObject = _servant_preinvoke("to_any", _opsClass);
/*  168 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  171 */       return localDynArrayOperations.to_any();
/*      */     } finally {
/*  173 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equal(DynAny paramDynAny)
/*      */   {
/*  188 */     ServantObject localServantObject = _servant_preinvoke("equal", _opsClass);
/*  189 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  192 */       return localDynArrayOperations.equal(paramDynAny);
/*      */     } finally {
/*  194 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  215 */     ServantObject localServantObject = _servant_preinvoke("destroy", _opsClass);
/*  216 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  219 */       localDynArrayOperations.destroy();
/*      */     } finally {
/*  221 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny copy()
/*      */   {
/*  235 */     ServantObject localServantObject = _servant_preinvoke("copy", _opsClass);
/*  236 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  239 */       return localDynArrayOperations.copy();
/*      */     } finally {
/*  241 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  254 */     ServantObject localServantObject = _servant_preinvoke("insert_boolean", _opsClass);
/*  255 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  258 */       localDynArrayOperations.insert_boolean(paramBoolean);
/*      */     } finally {
/*  260 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  273 */     ServantObject localServantObject = _servant_preinvoke("insert_octet", _opsClass);
/*  274 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  277 */       localDynArrayOperations.insert_octet(paramByte);
/*      */     } finally {
/*  279 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  292 */     ServantObject localServantObject = _servant_preinvoke("insert_char", _opsClass);
/*  293 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  296 */       localDynArrayOperations.insert_char(paramChar);
/*      */     } finally {
/*  298 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  311 */     ServantObject localServantObject = _servant_preinvoke("insert_short", _opsClass);
/*  312 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  315 */       localDynArrayOperations.insert_short(paramShort);
/*      */     } finally {
/*  317 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  330 */     ServantObject localServantObject = _servant_preinvoke("insert_ushort", _opsClass);
/*  331 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  334 */       localDynArrayOperations.insert_ushort(paramShort);
/*      */     } finally {
/*  336 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  349 */     ServantObject localServantObject = _servant_preinvoke("insert_long", _opsClass);
/*  350 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  353 */       localDynArrayOperations.insert_long(paramInt);
/*      */     } finally {
/*  355 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  368 */     ServantObject localServantObject = _servant_preinvoke("insert_ulong", _opsClass);
/*  369 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  372 */       localDynArrayOperations.insert_ulong(paramInt);
/*      */     } finally {
/*  374 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  387 */     ServantObject localServantObject = _servant_preinvoke("insert_float", _opsClass);
/*  388 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  391 */       localDynArrayOperations.insert_float(paramFloat);
/*      */     } finally {
/*  393 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  406 */     ServantObject localServantObject = _servant_preinvoke("insert_double", _opsClass);
/*  407 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  410 */       localDynArrayOperations.insert_double(paramDouble);
/*      */     } finally {
/*  412 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  427 */     ServantObject localServantObject = _servant_preinvoke("insert_string", _opsClass);
/*  428 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  431 */       localDynArrayOperations.insert_string(paramString);
/*      */     } finally {
/*  433 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  446 */     ServantObject localServantObject = _servant_preinvoke("insert_reference", _opsClass);
/*  447 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  450 */       localDynArrayOperations.insert_reference(paramObject);
/*      */     } finally {
/*  452 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_typecode(TypeCode paramTypeCode)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  465 */     ServantObject localServantObject = _servant_preinvoke("insert_typecode", _opsClass);
/*  466 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  469 */       localDynArrayOperations.insert_typecode(paramTypeCode);
/*      */     } finally {
/*  471 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  484 */     ServantObject localServantObject = _servant_preinvoke("insert_longlong", _opsClass);
/*  485 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  488 */       localDynArrayOperations.insert_longlong(paramLong);
/*      */     } finally {
/*  490 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  504 */     ServantObject localServantObject = _servant_preinvoke("insert_ulonglong", _opsClass);
/*  505 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  508 */       localDynArrayOperations.insert_ulonglong(paramLong);
/*      */     } finally {
/*  510 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  523 */     ServantObject localServantObject = _servant_preinvoke("insert_wchar", _opsClass);
/*  524 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  527 */       localDynArrayOperations.insert_wchar(paramChar);
/*      */     } finally {
/*  529 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  543 */     ServantObject localServantObject = _servant_preinvoke("insert_wstring", _opsClass);
/*  544 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  547 */       localDynArrayOperations.insert_wstring(paramString);
/*      */     } finally {
/*  549 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  562 */     ServantObject localServantObject = _servant_preinvoke("insert_any", _opsClass);
/*  563 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  566 */       localDynArrayOperations.insert_any(paramAny);
/*      */     } finally {
/*  568 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_dyn_any(DynAny paramDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  581 */     ServantObject localServantObject = _servant_preinvoke("insert_dyn_any", _opsClass);
/*  582 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  585 */       localDynArrayOperations.insert_dyn_any(paramDynAny);
/*      */     } finally {
/*  587 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_val(Serializable paramSerializable)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  601 */     ServantObject localServantObject = _servant_preinvoke("insert_val", _opsClass);
/*  602 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  605 */       localDynArrayOperations.insert_val(paramSerializable);
/*      */     } finally {
/*  607 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean get_boolean()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  622 */     ServantObject localServantObject = _servant_preinvoke("get_boolean", _opsClass);
/*  623 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  626 */       return localDynArrayOperations.get_boolean();
/*      */     } finally {
/*  628 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte get_octet()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  643 */     ServantObject localServantObject = _servant_preinvoke("get_octet", _opsClass);
/*  644 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  647 */       return localDynArrayOperations.get_octet();
/*      */     } finally {
/*  649 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_char()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  664 */     ServantObject localServantObject = _servant_preinvoke("get_char", _opsClass);
/*  665 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  668 */       return localDynArrayOperations.get_char();
/*      */     } finally {
/*  670 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_short()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  685 */     ServantObject localServantObject = _servant_preinvoke("get_short", _opsClass);
/*  686 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  689 */       return localDynArrayOperations.get_short();
/*      */     } finally {
/*  691 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_ushort()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  706 */     ServantObject localServantObject = _servant_preinvoke("get_ushort", _opsClass);
/*  707 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  710 */       return localDynArrayOperations.get_ushort();
/*      */     } finally {
/*  712 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_long()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  727 */     ServantObject localServantObject = _servant_preinvoke("get_long", _opsClass);
/*  728 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  731 */       return localDynArrayOperations.get_long();
/*      */     } finally {
/*  733 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_ulong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  748 */     ServantObject localServantObject = _servant_preinvoke("get_ulong", _opsClass);
/*  749 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  752 */       return localDynArrayOperations.get_ulong();
/*      */     } finally {
/*  754 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float get_float()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  769 */     ServantObject localServantObject = _servant_preinvoke("get_float", _opsClass);
/*  770 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  773 */       return localDynArrayOperations.get_float();
/*      */     } finally {
/*  775 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double get_double()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  790 */     ServantObject localServantObject = _servant_preinvoke("get_double", _opsClass);
/*  791 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  794 */       return localDynArrayOperations.get_double();
/*      */     } finally {
/*  796 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_string()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  812 */     ServantObject localServantObject = _servant_preinvoke("get_string", _opsClass);
/*  813 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  816 */       return localDynArrayOperations.get_string();
/*      */     } finally {
/*  818 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object get_reference()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  833 */     ServantObject localServantObject = _servant_preinvoke("get_reference", _opsClass);
/*  834 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  837 */       return localDynArrayOperations.get_reference();
/*      */     } finally {
/*  839 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode get_typecode()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  854 */     ServantObject localServantObject = _servant_preinvoke("get_typecode", _opsClass);
/*  855 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  858 */       return localDynArrayOperations.get_typecode();
/*      */     } finally {
/*  860 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_longlong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  875 */     ServantObject localServantObject = _servant_preinvoke("get_longlong", _opsClass);
/*  876 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  879 */       return localDynArrayOperations.get_longlong();
/*      */     } finally {
/*  881 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_ulonglong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  897 */     ServantObject localServantObject = _servant_preinvoke("get_ulonglong", _opsClass);
/*  898 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  901 */       return localDynArrayOperations.get_ulonglong();
/*      */     } finally {
/*  903 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_wchar()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  918 */     ServantObject localServantObject = _servant_preinvoke("get_wchar", _opsClass);
/*  919 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  922 */       return localDynArrayOperations.get_wchar();
/*      */     } finally {
/*  924 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_wstring()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  939 */     ServantObject localServantObject = _servant_preinvoke("get_wstring", _opsClass);
/*  940 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  943 */       return localDynArrayOperations.get_wstring();
/*      */     } finally {
/*  945 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any get_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  960 */     ServantObject localServantObject = _servant_preinvoke("get_any", _opsClass);
/*  961 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  964 */       return localDynArrayOperations.get_any();
/*      */     } finally {
/*  966 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny get_dyn_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  982 */     ServantObject localServantObject = _servant_preinvoke("get_dyn_any", _opsClass);
/*  983 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  986 */       return localDynArrayOperations.get_dyn_any();
/*      */     } finally {
/*  988 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable get_val()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1004 */     ServantObject localServantObject = _servant_preinvoke("get_val", _opsClass);
/* 1005 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1008 */       return localDynArrayOperations.get_val();
/*      */     } finally {
/* 1010 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean seek(int paramInt)
/*      */   {
/* 1026 */     ServantObject localServantObject = _servant_preinvoke("seek", _opsClass);
/* 1027 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1030 */       return localDynArrayOperations.seek(paramInt);
/*      */     } finally {
/* 1032 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rewind()
/*      */   {
/* 1042 */     ServantObject localServantObject = _servant_preinvoke("rewind", _opsClass);
/* 1043 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1046 */       localDynArrayOperations.rewind();
/*      */     } finally {
/* 1048 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */   {
/* 1061 */     ServantObject localServantObject = _servant_preinvoke("next", _opsClass);
/* 1062 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1065 */       return localDynArrayOperations.next();
/*      */     } finally {
/* 1067 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int component_count()
/*      */   {
/* 1089 */     ServantObject localServantObject = _servant_preinvoke("component_count", _opsClass);
/* 1090 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1093 */       return localDynArrayOperations.component_count();
/*      */     } finally {
/* 1095 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny current_component()
/*      */     throws TypeMismatch
/*      */   {
/* 1120 */     ServantObject localServantObject = _servant_preinvoke("current_component", _opsClass);
/* 1121 */     DynArrayOperations localDynArrayOperations = (DynArrayOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1124 */       return localDynArrayOperations.current_component();
/*      */     } finally {
/* 1126 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] _ids()
/*      */   {
/* 1137 */     return (String[])__ids.clone();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*      */   {
/* 1142 */     String str = paramObjectInputStream.readUTF();
/* 1143 */     String[] arrayOfString = null;
/* 1144 */     Properties localProperties = null;
/* 1145 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 1146 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 1147 */     _set_delegate(localDelegate);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1152 */     String[] arrayOfString = null;
/* 1153 */     Properties localProperties = null;
/* 1154 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 1155 */     paramObjectOutputStream.writeUTF(str);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynArrayStub
 * JD-Core Version:    0.6.2
 */