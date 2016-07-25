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
/*      */ public class _DynEnumStub extends ObjectImpl
/*      */   implements DynEnum
/*      */ {
/*   18 */   public static final Class _opsClass = DynEnumOperations.class;
/*      */ 
/* 1132 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynEnum:1.0", "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*      */ 
/*      */   public String get_as_string()
/*      */   {
/*   27 */     ServantObject localServantObject = _servant_preinvoke("get_as_string", _opsClass);
/*   28 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   31 */       return localDynEnumOperations.get_as_string();
/*      */     } finally {
/*   33 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_as_string(String paramString)
/*      */     throws InvalidValue
/*      */   {
/*   46 */     ServantObject localServantObject = _servant_preinvoke("set_as_string", _opsClass);
/*   47 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   50 */       localDynEnumOperations.set_as_string(paramString);
/*      */     } finally {
/*   52 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_as_ulong()
/*      */   {
/*   64 */     ServantObject localServantObject = _servant_preinvoke("get_as_ulong", _opsClass);
/*   65 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   68 */       return localDynEnumOperations.get_as_ulong();
/*      */     } finally {
/*   70 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_as_ulong(int paramInt)
/*      */     throws InvalidValue
/*      */   {
/*   83 */     ServantObject localServantObject = _servant_preinvoke("set_as_ulong", _opsClass);
/*   84 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   87 */       localDynEnumOperations.set_as_ulong(paramInt);
/*      */     } finally {
/*   89 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*  105 */     ServantObject localServantObject = _servant_preinvoke("type", _opsClass);
/*  106 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  109 */       return localDynEnumOperations.type();
/*      */     } finally {
/*  111 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assign(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*  127 */     ServantObject localServantObject = _servant_preinvoke("assign", _opsClass);
/*  128 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  131 */       localDynEnumOperations.assign(paramDynAny);
/*      */     } finally {
/*  133 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void from_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  148 */     ServantObject localServantObject = _servant_preinvoke("from_any", _opsClass);
/*  149 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  152 */       localDynEnumOperations.from_any(paramAny);
/*      */     } finally {
/*  154 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any to_any()
/*      */   {
/*  168 */     ServantObject localServantObject = _servant_preinvoke("to_any", _opsClass);
/*  169 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  172 */       return localDynEnumOperations.to_any();
/*      */     } finally {
/*  174 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equal(DynAny paramDynAny)
/*      */   {
/*  189 */     ServantObject localServantObject = _servant_preinvoke("equal", _opsClass);
/*  190 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  193 */       return localDynEnumOperations.equal(paramDynAny);
/*      */     } finally {
/*  195 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  216 */     ServantObject localServantObject = _servant_preinvoke("destroy", _opsClass);
/*  217 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  220 */       localDynEnumOperations.destroy();
/*      */     } finally {
/*  222 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny copy()
/*      */   {
/*  236 */     ServantObject localServantObject = _servant_preinvoke("copy", _opsClass);
/*  237 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  240 */       return localDynEnumOperations.copy();
/*      */     } finally {
/*  242 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  255 */     ServantObject localServantObject = _servant_preinvoke("insert_boolean", _opsClass);
/*  256 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  259 */       localDynEnumOperations.insert_boolean(paramBoolean);
/*      */     } finally {
/*  261 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  274 */     ServantObject localServantObject = _servant_preinvoke("insert_octet", _opsClass);
/*  275 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  278 */       localDynEnumOperations.insert_octet(paramByte);
/*      */     } finally {
/*  280 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  293 */     ServantObject localServantObject = _servant_preinvoke("insert_char", _opsClass);
/*  294 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  297 */       localDynEnumOperations.insert_char(paramChar);
/*      */     } finally {
/*  299 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  312 */     ServantObject localServantObject = _servant_preinvoke("insert_short", _opsClass);
/*  313 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  316 */       localDynEnumOperations.insert_short(paramShort);
/*      */     } finally {
/*  318 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  331 */     ServantObject localServantObject = _servant_preinvoke("insert_ushort", _opsClass);
/*  332 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  335 */       localDynEnumOperations.insert_ushort(paramShort);
/*      */     } finally {
/*  337 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  350 */     ServantObject localServantObject = _servant_preinvoke("insert_long", _opsClass);
/*  351 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  354 */       localDynEnumOperations.insert_long(paramInt);
/*      */     } finally {
/*  356 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  369 */     ServantObject localServantObject = _servant_preinvoke("insert_ulong", _opsClass);
/*  370 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  373 */       localDynEnumOperations.insert_ulong(paramInt);
/*      */     } finally {
/*  375 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  388 */     ServantObject localServantObject = _servant_preinvoke("insert_float", _opsClass);
/*  389 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  392 */       localDynEnumOperations.insert_float(paramFloat);
/*      */     } finally {
/*  394 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  407 */     ServantObject localServantObject = _servant_preinvoke("insert_double", _opsClass);
/*  408 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  411 */       localDynEnumOperations.insert_double(paramDouble);
/*      */     } finally {
/*  413 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  428 */     ServantObject localServantObject = _servant_preinvoke("insert_string", _opsClass);
/*  429 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  432 */       localDynEnumOperations.insert_string(paramString);
/*      */     } finally {
/*  434 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  447 */     ServantObject localServantObject = _servant_preinvoke("insert_reference", _opsClass);
/*  448 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  451 */       localDynEnumOperations.insert_reference(paramObject);
/*      */     } finally {
/*  453 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_typecode(TypeCode paramTypeCode)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  466 */     ServantObject localServantObject = _servant_preinvoke("insert_typecode", _opsClass);
/*  467 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  470 */       localDynEnumOperations.insert_typecode(paramTypeCode);
/*      */     } finally {
/*  472 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  485 */     ServantObject localServantObject = _servant_preinvoke("insert_longlong", _opsClass);
/*  486 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  489 */       localDynEnumOperations.insert_longlong(paramLong);
/*      */     } finally {
/*  491 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  505 */     ServantObject localServantObject = _servant_preinvoke("insert_ulonglong", _opsClass);
/*  506 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  509 */       localDynEnumOperations.insert_ulonglong(paramLong);
/*      */     } finally {
/*  511 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  524 */     ServantObject localServantObject = _servant_preinvoke("insert_wchar", _opsClass);
/*  525 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  528 */       localDynEnumOperations.insert_wchar(paramChar);
/*      */     } finally {
/*  530 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  544 */     ServantObject localServantObject = _servant_preinvoke("insert_wstring", _opsClass);
/*  545 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  548 */       localDynEnumOperations.insert_wstring(paramString);
/*      */     } finally {
/*  550 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  563 */     ServantObject localServantObject = _servant_preinvoke("insert_any", _opsClass);
/*  564 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  567 */       localDynEnumOperations.insert_any(paramAny);
/*      */     } finally {
/*  569 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_dyn_any(DynAny paramDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  582 */     ServantObject localServantObject = _servant_preinvoke("insert_dyn_any", _opsClass);
/*  583 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  586 */       localDynEnumOperations.insert_dyn_any(paramDynAny);
/*      */     } finally {
/*  588 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_val(Serializable paramSerializable)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  602 */     ServantObject localServantObject = _servant_preinvoke("insert_val", _opsClass);
/*  603 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  606 */       localDynEnumOperations.insert_val(paramSerializable);
/*      */     } finally {
/*  608 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean get_boolean()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  623 */     ServantObject localServantObject = _servant_preinvoke("get_boolean", _opsClass);
/*  624 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  627 */       return localDynEnumOperations.get_boolean();
/*      */     } finally {
/*  629 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte get_octet()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  644 */     ServantObject localServantObject = _servant_preinvoke("get_octet", _opsClass);
/*  645 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  648 */       return localDynEnumOperations.get_octet();
/*      */     } finally {
/*  650 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_char()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  665 */     ServantObject localServantObject = _servant_preinvoke("get_char", _opsClass);
/*  666 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  669 */       return localDynEnumOperations.get_char();
/*      */     } finally {
/*  671 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_short()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  686 */     ServantObject localServantObject = _servant_preinvoke("get_short", _opsClass);
/*  687 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  690 */       return localDynEnumOperations.get_short();
/*      */     } finally {
/*  692 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_ushort()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  707 */     ServantObject localServantObject = _servant_preinvoke("get_ushort", _opsClass);
/*  708 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  711 */       return localDynEnumOperations.get_ushort();
/*      */     } finally {
/*  713 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_long()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  728 */     ServantObject localServantObject = _servant_preinvoke("get_long", _opsClass);
/*  729 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  732 */       return localDynEnumOperations.get_long();
/*      */     } finally {
/*  734 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_ulong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  749 */     ServantObject localServantObject = _servant_preinvoke("get_ulong", _opsClass);
/*  750 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  753 */       return localDynEnumOperations.get_ulong();
/*      */     } finally {
/*  755 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float get_float()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  770 */     ServantObject localServantObject = _servant_preinvoke("get_float", _opsClass);
/*  771 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  774 */       return localDynEnumOperations.get_float();
/*      */     } finally {
/*  776 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double get_double()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  791 */     ServantObject localServantObject = _servant_preinvoke("get_double", _opsClass);
/*  792 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  795 */       return localDynEnumOperations.get_double();
/*      */     } finally {
/*  797 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_string()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  813 */     ServantObject localServantObject = _servant_preinvoke("get_string", _opsClass);
/*  814 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  817 */       return localDynEnumOperations.get_string();
/*      */     } finally {
/*  819 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object get_reference()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  834 */     ServantObject localServantObject = _servant_preinvoke("get_reference", _opsClass);
/*  835 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  838 */       return localDynEnumOperations.get_reference();
/*      */     } finally {
/*  840 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode get_typecode()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  855 */     ServantObject localServantObject = _servant_preinvoke("get_typecode", _opsClass);
/*  856 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  859 */       return localDynEnumOperations.get_typecode();
/*      */     } finally {
/*  861 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_longlong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  876 */     ServantObject localServantObject = _servant_preinvoke("get_longlong", _opsClass);
/*  877 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  880 */       return localDynEnumOperations.get_longlong();
/*      */     } finally {
/*  882 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_ulonglong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  898 */     ServantObject localServantObject = _servant_preinvoke("get_ulonglong", _opsClass);
/*  899 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  902 */       return localDynEnumOperations.get_ulonglong();
/*      */     } finally {
/*  904 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_wchar()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  919 */     ServantObject localServantObject = _servant_preinvoke("get_wchar", _opsClass);
/*  920 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  923 */       return localDynEnumOperations.get_wchar();
/*      */     } finally {
/*  925 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_wstring()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  940 */     ServantObject localServantObject = _servant_preinvoke("get_wstring", _opsClass);
/*  941 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  944 */       return localDynEnumOperations.get_wstring();
/*      */     } finally {
/*  946 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any get_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  961 */     ServantObject localServantObject = _servant_preinvoke("get_any", _opsClass);
/*  962 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  965 */       return localDynEnumOperations.get_any();
/*      */     } finally {
/*  967 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny get_dyn_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  983 */     ServantObject localServantObject = _servant_preinvoke("get_dyn_any", _opsClass);
/*  984 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  987 */       return localDynEnumOperations.get_dyn_any();
/*      */     } finally {
/*  989 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable get_val()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1005 */     ServantObject localServantObject = _servant_preinvoke("get_val", _opsClass);
/* 1006 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1009 */       return localDynEnumOperations.get_val();
/*      */     } finally {
/* 1011 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean seek(int paramInt)
/*      */   {
/* 1027 */     ServantObject localServantObject = _servant_preinvoke("seek", _opsClass);
/* 1028 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1031 */       return localDynEnumOperations.seek(paramInt);
/*      */     } finally {
/* 1033 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rewind()
/*      */   {
/* 1043 */     ServantObject localServantObject = _servant_preinvoke("rewind", _opsClass);
/* 1044 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1047 */       localDynEnumOperations.rewind();
/*      */     } finally {
/* 1049 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */   {
/* 1062 */     ServantObject localServantObject = _servant_preinvoke("next", _opsClass);
/* 1063 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1066 */       return localDynEnumOperations.next();
/*      */     } finally {
/* 1068 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int component_count()
/*      */   {
/* 1090 */     ServantObject localServantObject = _servant_preinvoke("component_count", _opsClass);
/* 1091 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1094 */       return localDynEnumOperations.component_count();
/*      */     } finally {
/* 1096 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny current_component()
/*      */     throws TypeMismatch
/*      */   {
/* 1121 */     ServantObject localServantObject = _servant_preinvoke("current_component", _opsClass);
/* 1122 */     DynEnumOperations localDynEnumOperations = (DynEnumOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1125 */       return localDynEnumOperations.current_component();
/*      */     } finally {
/* 1127 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] _ids()
/*      */   {
/* 1138 */     return (String[])__ids.clone();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*      */   {
/* 1143 */     String str = paramObjectInputStream.readUTF();
/* 1144 */     String[] arrayOfString = null;
/* 1145 */     Properties localProperties = null;
/* 1146 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 1147 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 1148 */     _set_delegate(localDelegate);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1153 */     String[] arrayOfString = null;
/* 1154 */     Properties localProperties = null;
/* 1155 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 1156 */     paramObjectOutputStream.writeUTF(str);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynEnumStub
 * JD-Core Version:    0.6.2
 */