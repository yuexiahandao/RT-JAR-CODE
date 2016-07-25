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
/*      */ public class _DynStructStub extends ObjectImpl
/*      */   implements DynStruct
/*      */ {
/*   18 */   public static final Class _opsClass = DynStructOperations.class;
/*      */ 
/* 1194 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynStruct:1.0", "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*      */ 
/*      */   public String current_member_name()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   32 */     ServantObject localServantObject = _servant_preinvoke("current_member_name", _opsClass);
/*   33 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   36 */       return localDynStructOperations.current_member_name();
/*      */     } finally {
/*   38 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TCKind current_member_kind()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   51 */     ServantObject localServantObject = _servant_preinvoke("current_member_kind", _opsClass);
/*   52 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   55 */       return localDynStructOperations.current_member_kind();
/*      */     } finally {
/*   57 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NameValuePair[] get_members()
/*      */   {
/*   72 */     ServantObject localServantObject = _servant_preinvoke("get_members", _opsClass);
/*   73 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   76 */       return localDynStructOperations.get_members();
/*      */     } finally {
/*   78 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_members(NameValuePair[] paramArrayOfNameValuePair)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   98 */     ServantObject localServantObject = _servant_preinvoke("set_members", _opsClass);
/*   99 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  102 */       localDynStructOperations.set_members(paramArrayOfNameValuePair);
/*      */     } finally {
/*  104 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NameDynAnyPair[] get_members_as_dyn_any()
/*      */   {
/*  119 */     ServantObject localServantObject = _servant_preinvoke("get_members_as_dyn_any", _opsClass);
/*  120 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  123 */       return localDynStructOperations.get_members_as_dyn_any();
/*      */     } finally {
/*  125 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_members_as_dyn_any(NameDynAnyPair[] paramArrayOfNameDynAnyPair)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  145 */     ServantObject localServantObject = _servant_preinvoke("set_members_as_dyn_any", _opsClass);
/*  146 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  149 */       localDynStructOperations.set_members_as_dyn_any(paramArrayOfNameDynAnyPair);
/*      */     } finally {
/*  151 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*  167 */     ServantObject localServantObject = _servant_preinvoke("type", _opsClass);
/*  168 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  171 */       return localDynStructOperations.type();
/*      */     } finally {
/*  173 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assign(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*  189 */     ServantObject localServantObject = _servant_preinvoke("assign", _opsClass);
/*  190 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  193 */       localDynStructOperations.assign(paramDynAny);
/*      */     } finally {
/*  195 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void from_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  210 */     ServantObject localServantObject = _servant_preinvoke("from_any", _opsClass);
/*  211 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  214 */       localDynStructOperations.from_any(paramAny);
/*      */     } finally {
/*  216 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any to_any()
/*      */   {
/*  230 */     ServantObject localServantObject = _servant_preinvoke("to_any", _opsClass);
/*  231 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  234 */       return localDynStructOperations.to_any();
/*      */     } finally {
/*  236 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equal(DynAny paramDynAny)
/*      */   {
/*  251 */     ServantObject localServantObject = _servant_preinvoke("equal", _opsClass);
/*  252 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  255 */       return localDynStructOperations.equal(paramDynAny);
/*      */     } finally {
/*  257 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  278 */     ServantObject localServantObject = _servant_preinvoke("destroy", _opsClass);
/*  279 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  282 */       localDynStructOperations.destroy();
/*      */     } finally {
/*  284 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny copy()
/*      */   {
/*  298 */     ServantObject localServantObject = _servant_preinvoke("copy", _opsClass);
/*  299 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  302 */       return localDynStructOperations.copy();
/*      */     } finally {
/*  304 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  317 */     ServantObject localServantObject = _servant_preinvoke("insert_boolean", _opsClass);
/*  318 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  321 */       localDynStructOperations.insert_boolean(paramBoolean);
/*      */     } finally {
/*  323 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  336 */     ServantObject localServantObject = _servant_preinvoke("insert_octet", _opsClass);
/*  337 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  340 */       localDynStructOperations.insert_octet(paramByte);
/*      */     } finally {
/*  342 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  355 */     ServantObject localServantObject = _servant_preinvoke("insert_char", _opsClass);
/*  356 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  359 */       localDynStructOperations.insert_char(paramChar);
/*      */     } finally {
/*  361 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  374 */     ServantObject localServantObject = _servant_preinvoke("insert_short", _opsClass);
/*  375 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  378 */       localDynStructOperations.insert_short(paramShort);
/*      */     } finally {
/*  380 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  393 */     ServantObject localServantObject = _servant_preinvoke("insert_ushort", _opsClass);
/*  394 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  397 */       localDynStructOperations.insert_ushort(paramShort);
/*      */     } finally {
/*  399 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  412 */     ServantObject localServantObject = _servant_preinvoke("insert_long", _opsClass);
/*  413 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  416 */       localDynStructOperations.insert_long(paramInt);
/*      */     } finally {
/*  418 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  431 */     ServantObject localServantObject = _servant_preinvoke("insert_ulong", _opsClass);
/*  432 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  435 */       localDynStructOperations.insert_ulong(paramInt);
/*      */     } finally {
/*  437 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  450 */     ServantObject localServantObject = _servant_preinvoke("insert_float", _opsClass);
/*  451 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  454 */       localDynStructOperations.insert_float(paramFloat);
/*      */     } finally {
/*  456 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  469 */     ServantObject localServantObject = _servant_preinvoke("insert_double", _opsClass);
/*  470 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  473 */       localDynStructOperations.insert_double(paramDouble);
/*      */     } finally {
/*  475 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  490 */     ServantObject localServantObject = _servant_preinvoke("insert_string", _opsClass);
/*  491 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  494 */       localDynStructOperations.insert_string(paramString);
/*      */     } finally {
/*  496 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  509 */     ServantObject localServantObject = _servant_preinvoke("insert_reference", _opsClass);
/*  510 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  513 */       localDynStructOperations.insert_reference(paramObject);
/*      */     } finally {
/*  515 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_typecode(TypeCode paramTypeCode)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  528 */     ServantObject localServantObject = _servant_preinvoke("insert_typecode", _opsClass);
/*  529 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  532 */       localDynStructOperations.insert_typecode(paramTypeCode);
/*      */     } finally {
/*  534 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  547 */     ServantObject localServantObject = _servant_preinvoke("insert_longlong", _opsClass);
/*  548 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  551 */       localDynStructOperations.insert_longlong(paramLong);
/*      */     } finally {
/*  553 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  567 */     ServantObject localServantObject = _servant_preinvoke("insert_ulonglong", _opsClass);
/*  568 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  571 */       localDynStructOperations.insert_ulonglong(paramLong);
/*      */     } finally {
/*  573 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  586 */     ServantObject localServantObject = _servant_preinvoke("insert_wchar", _opsClass);
/*  587 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  590 */       localDynStructOperations.insert_wchar(paramChar);
/*      */     } finally {
/*  592 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  606 */     ServantObject localServantObject = _servant_preinvoke("insert_wstring", _opsClass);
/*  607 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  610 */       localDynStructOperations.insert_wstring(paramString);
/*      */     } finally {
/*  612 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  625 */     ServantObject localServantObject = _servant_preinvoke("insert_any", _opsClass);
/*  626 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  629 */       localDynStructOperations.insert_any(paramAny);
/*      */     } finally {
/*  631 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_dyn_any(DynAny paramDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  644 */     ServantObject localServantObject = _servant_preinvoke("insert_dyn_any", _opsClass);
/*  645 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  648 */       localDynStructOperations.insert_dyn_any(paramDynAny);
/*      */     } finally {
/*  650 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_val(Serializable paramSerializable)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  664 */     ServantObject localServantObject = _servant_preinvoke("insert_val", _opsClass);
/*  665 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  668 */       localDynStructOperations.insert_val(paramSerializable);
/*      */     } finally {
/*  670 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean get_boolean()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  685 */     ServantObject localServantObject = _servant_preinvoke("get_boolean", _opsClass);
/*  686 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  689 */       return localDynStructOperations.get_boolean();
/*      */     } finally {
/*  691 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte get_octet()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  706 */     ServantObject localServantObject = _servant_preinvoke("get_octet", _opsClass);
/*  707 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  710 */       return localDynStructOperations.get_octet();
/*      */     } finally {
/*  712 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_char()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  727 */     ServantObject localServantObject = _servant_preinvoke("get_char", _opsClass);
/*  728 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  731 */       return localDynStructOperations.get_char();
/*      */     } finally {
/*  733 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_short()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  748 */     ServantObject localServantObject = _servant_preinvoke("get_short", _opsClass);
/*  749 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  752 */       return localDynStructOperations.get_short();
/*      */     } finally {
/*  754 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_ushort()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  769 */     ServantObject localServantObject = _servant_preinvoke("get_ushort", _opsClass);
/*  770 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  773 */       return localDynStructOperations.get_ushort();
/*      */     } finally {
/*  775 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_long()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  790 */     ServantObject localServantObject = _servant_preinvoke("get_long", _opsClass);
/*  791 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  794 */       return localDynStructOperations.get_long();
/*      */     } finally {
/*  796 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_ulong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  811 */     ServantObject localServantObject = _servant_preinvoke("get_ulong", _opsClass);
/*  812 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  815 */       return localDynStructOperations.get_ulong();
/*      */     } finally {
/*  817 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float get_float()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  832 */     ServantObject localServantObject = _servant_preinvoke("get_float", _opsClass);
/*  833 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  836 */       return localDynStructOperations.get_float();
/*      */     } finally {
/*  838 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double get_double()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  853 */     ServantObject localServantObject = _servant_preinvoke("get_double", _opsClass);
/*  854 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  857 */       return localDynStructOperations.get_double();
/*      */     } finally {
/*  859 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_string()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  875 */     ServantObject localServantObject = _servant_preinvoke("get_string", _opsClass);
/*  876 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  879 */       return localDynStructOperations.get_string();
/*      */     } finally {
/*  881 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object get_reference()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  896 */     ServantObject localServantObject = _servant_preinvoke("get_reference", _opsClass);
/*  897 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  900 */       return localDynStructOperations.get_reference();
/*      */     } finally {
/*  902 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode get_typecode()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  917 */     ServantObject localServantObject = _servant_preinvoke("get_typecode", _opsClass);
/*  918 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  921 */       return localDynStructOperations.get_typecode();
/*      */     } finally {
/*  923 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_longlong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  938 */     ServantObject localServantObject = _servant_preinvoke("get_longlong", _opsClass);
/*  939 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  942 */       return localDynStructOperations.get_longlong();
/*      */     } finally {
/*  944 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_ulonglong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  960 */     ServantObject localServantObject = _servant_preinvoke("get_ulonglong", _opsClass);
/*  961 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  964 */       return localDynStructOperations.get_ulonglong();
/*      */     } finally {
/*  966 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_wchar()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  981 */     ServantObject localServantObject = _servant_preinvoke("get_wchar", _opsClass);
/*  982 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  985 */       return localDynStructOperations.get_wchar();
/*      */     } finally {
/*  987 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_wstring()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1002 */     ServantObject localServantObject = _servant_preinvoke("get_wstring", _opsClass);
/* 1003 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1006 */       return localDynStructOperations.get_wstring();
/*      */     } finally {
/* 1008 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any get_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1023 */     ServantObject localServantObject = _servant_preinvoke("get_any", _opsClass);
/* 1024 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1027 */       return localDynStructOperations.get_any();
/*      */     } finally {
/* 1029 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny get_dyn_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1045 */     ServantObject localServantObject = _servant_preinvoke("get_dyn_any", _opsClass);
/* 1046 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1049 */       return localDynStructOperations.get_dyn_any();
/*      */     } finally {
/* 1051 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable get_val()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1067 */     ServantObject localServantObject = _servant_preinvoke("get_val", _opsClass);
/* 1068 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1071 */       return localDynStructOperations.get_val();
/*      */     } finally {
/* 1073 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean seek(int paramInt)
/*      */   {
/* 1089 */     ServantObject localServantObject = _servant_preinvoke("seek", _opsClass);
/* 1090 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1093 */       return localDynStructOperations.seek(paramInt);
/*      */     } finally {
/* 1095 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rewind()
/*      */   {
/* 1105 */     ServantObject localServantObject = _servant_preinvoke("rewind", _opsClass);
/* 1106 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1109 */       localDynStructOperations.rewind();
/*      */     } finally {
/* 1111 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */   {
/* 1124 */     ServantObject localServantObject = _servant_preinvoke("next", _opsClass);
/* 1125 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1128 */       return localDynStructOperations.next();
/*      */     } finally {
/* 1130 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int component_count()
/*      */   {
/* 1152 */     ServantObject localServantObject = _servant_preinvoke("component_count", _opsClass);
/* 1153 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1156 */       return localDynStructOperations.component_count();
/*      */     } finally {
/* 1158 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny current_component()
/*      */     throws TypeMismatch
/*      */   {
/* 1183 */     ServantObject localServantObject = _servant_preinvoke("current_component", _opsClass);
/* 1184 */     DynStructOperations localDynStructOperations = (DynStructOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1187 */       return localDynStructOperations.current_component();
/*      */     } finally {
/* 1189 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] _ids()
/*      */   {
/* 1200 */     return (String[])__ids.clone();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*      */   {
/* 1205 */     String str = paramObjectInputStream.readUTF();
/* 1206 */     String[] arrayOfString = null;
/* 1207 */     Properties localProperties = null;
/* 1208 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 1209 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 1210 */     _set_delegate(localDelegate);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1215 */     String[] arrayOfString = null;
/* 1216 */     Properties localProperties = null;
/* 1217 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 1218 */     paramObjectOutputStream.writeUTF(str);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynStructStub
 * JD-Core Version:    0.6.2
 */