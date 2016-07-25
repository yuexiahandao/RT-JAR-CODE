/*      */ package org.omg.DynamicAny;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Properties;
/*      */ import org.omg.CORBA.Any;
/*      */ import org.omg.CORBA.ORB;
/*      */ import org.omg.CORBA.TCKind;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.portable.Delegate;
/*      */ import org.omg.CORBA.portable.ObjectImpl;
/*      */ import org.omg.CORBA.portable.ServantObject;
/*      */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*      */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*      */ 
/*      */ public class _DynValueStub extends ObjectImpl
/*      */   implements DynValue
/*      */ {
/*   26 */   public static final Class _opsClass = DynValueOperations.class;
/*      */ 
/* 1258 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynValue:1.0", "IDL:omg.org/DynamicAny/DynValueCommon:1.0", "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*      */ 
/*      */   public String current_member_name()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   40 */     ServantObject localServantObject = _servant_preinvoke("current_member_name", _opsClass);
/*   41 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   44 */       return localDynValueOperations.current_member_name();
/*      */     } finally {
/*   46 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TCKind current_member_kind()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   59 */     ServantObject localServantObject = _servant_preinvoke("current_member_kind", _opsClass);
/*   60 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   63 */       return localDynValueOperations.current_member_kind();
/*      */     } finally {
/*   65 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NameValuePair[] get_members()
/*      */     throws InvalidValue
/*      */   {
/*   82 */     ServantObject localServantObject = _servant_preinvoke("get_members", _opsClass);
/*   83 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   86 */       return localDynValueOperations.get_members();
/*      */     } finally {
/*   88 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_members(NameValuePair[] paramArrayOfNameValuePair)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  109 */     ServantObject localServantObject = _servant_preinvoke("set_members", _opsClass);
/*  110 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  113 */       localDynValueOperations.set_members(paramArrayOfNameValuePair);
/*      */     } finally {
/*  115 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NameDynAnyPair[] get_members_as_dyn_any()
/*      */     throws InvalidValue
/*      */   {
/*  132 */     ServantObject localServantObject = _servant_preinvoke("get_members_as_dyn_any", _opsClass);
/*  133 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  136 */       return localDynValueOperations.get_members_as_dyn_any();
/*      */     } finally {
/*  138 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_members_as_dyn_any(NameDynAnyPair[] paramArrayOfNameDynAnyPair)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  159 */     ServantObject localServantObject = _servant_preinvoke("set_members_as_dyn_any", _opsClass);
/*  160 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  163 */       localDynValueOperations.set_members_as_dyn_any(paramArrayOfNameDynAnyPair);
/*      */     } finally {
/*  165 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean is_null()
/*      */   {
/*  175 */     ServantObject localServantObject = _servant_preinvoke("is_null", _opsClass);
/*  176 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  179 */       return localDynValueOperations.is_null();
/*      */     } finally {
/*  181 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_to_null()
/*      */   {
/*  191 */     ServantObject localServantObject = _servant_preinvoke("set_to_null", _opsClass);
/*  192 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  195 */       localDynValueOperations.set_to_null();
/*      */     } finally {
/*  197 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_to_value()
/*      */   {
/*  209 */     ServantObject localServantObject = _servant_preinvoke("set_to_value", _opsClass);
/*  210 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  213 */       localDynValueOperations.set_to_value();
/*      */     } finally {
/*  215 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*  231 */     ServantObject localServantObject = _servant_preinvoke("type", _opsClass);
/*  232 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  235 */       return localDynValueOperations.type();
/*      */     } finally {
/*  237 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assign(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*  253 */     ServantObject localServantObject = _servant_preinvoke("assign", _opsClass);
/*  254 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  257 */       localDynValueOperations.assign(paramDynAny);
/*      */     } finally {
/*  259 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void from_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  274 */     ServantObject localServantObject = _servant_preinvoke("from_any", _opsClass);
/*  275 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  278 */       localDynValueOperations.from_any(paramAny);
/*      */     } finally {
/*  280 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any to_any()
/*      */   {
/*  294 */     ServantObject localServantObject = _servant_preinvoke("to_any", _opsClass);
/*  295 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  298 */       return localDynValueOperations.to_any();
/*      */     } finally {
/*  300 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equal(DynAny paramDynAny)
/*      */   {
/*  315 */     ServantObject localServantObject = _servant_preinvoke("equal", _opsClass);
/*  316 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  319 */       return localDynValueOperations.equal(paramDynAny);
/*      */     } finally {
/*  321 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  342 */     ServantObject localServantObject = _servant_preinvoke("destroy", _opsClass);
/*  343 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  346 */       localDynValueOperations.destroy();
/*      */     } finally {
/*  348 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny copy()
/*      */   {
/*  362 */     ServantObject localServantObject = _servant_preinvoke("copy", _opsClass);
/*  363 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  366 */       return localDynValueOperations.copy();
/*      */     } finally {
/*  368 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  381 */     ServantObject localServantObject = _servant_preinvoke("insert_boolean", _opsClass);
/*  382 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  385 */       localDynValueOperations.insert_boolean(paramBoolean);
/*      */     } finally {
/*  387 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  400 */     ServantObject localServantObject = _servant_preinvoke("insert_octet", _opsClass);
/*  401 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  404 */       localDynValueOperations.insert_octet(paramByte);
/*      */     } finally {
/*  406 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  419 */     ServantObject localServantObject = _servant_preinvoke("insert_char", _opsClass);
/*  420 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  423 */       localDynValueOperations.insert_char(paramChar);
/*      */     } finally {
/*  425 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  438 */     ServantObject localServantObject = _servant_preinvoke("insert_short", _opsClass);
/*  439 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  442 */       localDynValueOperations.insert_short(paramShort);
/*      */     } finally {
/*  444 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  457 */     ServantObject localServantObject = _servant_preinvoke("insert_ushort", _opsClass);
/*  458 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  461 */       localDynValueOperations.insert_ushort(paramShort);
/*      */     } finally {
/*  463 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  476 */     ServantObject localServantObject = _servant_preinvoke("insert_long", _opsClass);
/*  477 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  480 */       localDynValueOperations.insert_long(paramInt);
/*      */     } finally {
/*  482 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  495 */     ServantObject localServantObject = _servant_preinvoke("insert_ulong", _opsClass);
/*  496 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  499 */       localDynValueOperations.insert_ulong(paramInt);
/*      */     } finally {
/*  501 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  514 */     ServantObject localServantObject = _servant_preinvoke("insert_float", _opsClass);
/*  515 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  518 */       localDynValueOperations.insert_float(paramFloat);
/*      */     } finally {
/*  520 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  533 */     ServantObject localServantObject = _servant_preinvoke("insert_double", _opsClass);
/*  534 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  537 */       localDynValueOperations.insert_double(paramDouble);
/*      */     } finally {
/*  539 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  554 */     ServantObject localServantObject = _servant_preinvoke("insert_string", _opsClass);
/*  555 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  558 */       localDynValueOperations.insert_string(paramString);
/*      */     } finally {
/*  560 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  573 */     ServantObject localServantObject = _servant_preinvoke("insert_reference", _opsClass);
/*  574 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  577 */       localDynValueOperations.insert_reference(paramObject);
/*      */     } finally {
/*  579 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_typecode(TypeCode paramTypeCode)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  592 */     ServantObject localServantObject = _servant_preinvoke("insert_typecode", _opsClass);
/*  593 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  596 */       localDynValueOperations.insert_typecode(paramTypeCode);
/*      */     } finally {
/*  598 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  611 */     ServantObject localServantObject = _servant_preinvoke("insert_longlong", _opsClass);
/*  612 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  615 */       localDynValueOperations.insert_longlong(paramLong);
/*      */     } finally {
/*  617 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  631 */     ServantObject localServantObject = _servant_preinvoke("insert_ulonglong", _opsClass);
/*  632 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  635 */       localDynValueOperations.insert_ulonglong(paramLong);
/*      */     } finally {
/*  637 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  650 */     ServantObject localServantObject = _servant_preinvoke("insert_wchar", _opsClass);
/*  651 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  654 */       localDynValueOperations.insert_wchar(paramChar);
/*      */     } finally {
/*  656 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  670 */     ServantObject localServantObject = _servant_preinvoke("insert_wstring", _opsClass);
/*  671 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  674 */       localDynValueOperations.insert_wstring(paramString);
/*      */     } finally {
/*  676 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  689 */     ServantObject localServantObject = _servant_preinvoke("insert_any", _opsClass);
/*  690 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  693 */       localDynValueOperations.insert_any(paramAny);
/*      */     } finally {
/*  695 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_dyn_any(DynAny paramDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  708 */     ServantObject localServantObject = _servant_preinvoke("insert_dyn_any", _opsClass);
/*  709 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  712 */       localDynValueOperations.insert_dyn_any(paramDynAny);
/*      */     } finally {
/*  714 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_val(Serializable paramSerializable)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  728 */     ServantObject localServantObject = _servant_preinvoke("insert_val", _opsClass);
/*  729 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  732 */       localDynValueOperations.insert_val(paramSerializable);
/*      */     } finally {
/*  734 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean get_boolean()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  749 */     ServantObject localServantObject = _servant_preinvoke("get_boolean", _opsClass);
/*  750 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  753 */       return localDynValueOperations.get_boolean();
/*      */     } finally {
/*  755 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte get_octet()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  770 */     ServantObject localServantObject = _servant_preinvoke("get_octet", _opsClass);
/*  771 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  774 */       return localDynValueOperations.get_octet();
/*      */     } finally {
/*  776 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_char()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  791 */     ServantObject localServantObject = _servant_preinvoke("get_char", _opsClass);
/*  792 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  795 */       return localDynValueOperations.get_char();
/*      */     } finally {
/*  797 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_short()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  812 */     ServantObject localServantObject = _servant_preinvoke("get_short", _opsClass);
/*  813 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  816 */       return localDynValueOperations.get_short();
/*      */     } finally {
/*  818 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_ushort()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  833 */     ServantObject localServantObject = _servant_preinvoke("get_ushort", _opsClass);
/*  834 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  837 */       return localDynValueOperations.get_ushort();
/*      */     } finally {
/*  839 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_long()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  854 */     ServantObject localServantObject = _servant_preinvoke("get_long", _opsClass);
/*  855 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  858 */       return localDynValueOperations.get_long();
/*      */     } finally {
/*  860 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_ulong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  875 */     ServantObject localServantObject = _servant_preinvoke("get_ulong", _opsClass);
/*  876 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  879 */       return localDynValueOperations.get_ulong();
/*      */     } finally {
/*  881 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float get_float()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  896 */     ServantObject localServantObject = _servant_preinvoke("get_float", _opsClass);
/*  897 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  900 */       return localDynValueOperations.get_float();
/*      */     } finally {
/*  902 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double get_double()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  917 */     ServantObject localServantObject = _servant_preinvoke("get_double", _opsClass);
/*  918 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  921 */       return localDynValueOperations.get_double();
/*      */     } finally {
/*  923 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_string()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  939 */     ServantObject localServantObject = _servant_preinvoke("get_string", _opsClass);
/*  940 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  943 */       return localDynValueOperations.get_string();
/*      */     } finally {
/*  945 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object get_reference()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  960 */     ServantObject localServantObject = _servant_preinvoke("get_reference", _opsClass);
/*  961 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  964 */       return localDynValueOperations.get_reference();
/*      */     } finally {
/*  966 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode get_typecode()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  981 */     ServantObject localServantObject = _servant_preinvoke("get_typecode", _opsClass);
/*  982 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  985 */       return localDynValueOperations.get_typecode();
/*      */     } finally {
/*  987 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_longlong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1002 */     ServantObject localServantObject = _servant_preinvoke("get_longlong", _opsClass);
/* 1003 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1006 */       return localDynValueOperations.get_longlong();
/*      */     } finally {
/* 1008 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_ulonglong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1024 */     ServantObject localServantObject = _servant_preinvoke("get_ulonglong", _opsClass);
/* 1025 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1028 */       return localDynValueOperations.get_ulonglong();
/*      */     } finally {
/* 1030 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_wchar()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1045 */     ServantObject localServantObject = _servant_preinvoke("get_wchar", _opsClass);
/* 1046 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1049 */       return localDynValueOperations.get_wchar();
/*      */     } finally {
/* 1051 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_wstring()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1066 */     ServantObject localServantObject = _servant_preinvoke("get_wstring", _opsClass);
/* 1067 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1070 */       return localDynValueOperations.get_wstring();
/*      */     } finally {
/* 1072 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any get_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1087 */     ServantObject localServantObject = _servant_preinvoke("get_any", _opsClass);
/* 1088 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1091 */       return localDynValueOperations.get_any();
/*      */     } finally {
/* 1093 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny get_dyn_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1109 */     ServantObject localServantObject = _servant_preinvoke("get_dyn_any", _opsClass);
/* 1110 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1113 */       return localDynValueOperations.get_dyn_any();
/*      */     } finally {
/* 1115 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable get_val()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1131 */     ServantObject localServantObject = _servant_preinvoke("get_val", _opsClass);
/* 1132 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1135 */       return localDynValueOperations.get_val();
/*      */     } finally {
/* 1137 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean seek(int paramInt)
/*      */   {
/* 1153 */     ServantObject localServantObject = _servant_preinvoke("seek", _opsClass);
/* 1154 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1157 */       return localDynValueOperations.seek(paramInt);
/*      */     } finally {
/* 1159 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rewind()
/*      */   {
/* 1169 */     ServantObject localServantObject = _servant_preinvoke("rewind", _opsClass);
/* 1170 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1173 */       localDynValueOperations.rewind();
/*      */     } finally {
/* 1175 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */   {
/* 1188 */     ServantObject localServantObject = _servant_preinvoke("next", _opsClass);
/* 1189 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1192 */       return localDynValueOperations.next();
/*      */     } finally {
/* 1194 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int component_count()
/*      */   {
/* 1216 */     ServantObject localServantObject = _servant_preinvoke("component_count", _opsClass);
/* 1217 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1220 */       return localDynValueOperations.component_count();
/*      */     } finally {
/* 1222 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny current_component()
/*      */     throws TypeMismatch
/*      */   {
/* 1247 */     ServantObject localServantObject = _servant_preinvoke("current_component", _opsClass);
/* 1248 */     DynValueOperations localDynValueOperations = (DynValueOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1251 */       return localDynValueOperations.current_component();
/*      */     } finally {
/* 1253 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] _ids()
/*      */   {
/* 1265 */     return (String[])__ids.clone();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*      */   {
/* 1270 */     String str = paramObjectInputStream.readUTF();
/* 1271 */     String[] arrayOfString = null;
/* 1272 */     Properties localProperties = null;
/* 1273 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 1274 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 1275 */     _set_delegate(localDelegate);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1280 */     String[] arrayOfString = null;
/* 1281 */     Properties localProperties = null;
/* 1282 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 1283 */     paramObjectOutputStream.writeUTF(str);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynValueStub
 * JD-Core Version:    0.6.2
 */