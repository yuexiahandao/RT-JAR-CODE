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
/*      */ public class _DynFixedStub extends ObjectImpl
/*      */   implements DynFixed
/*      */ {
/*   19 */   public static final Class _opsClass = DynFixedOperations.class;
/*      */ 
/* 1102 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynFixed:1.0", "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*      */ 
/*      */   public String get_value()
/*      */   {
/*   28 */     ServantObject localServantObject = _servant_preinvoke("get_value", _opsClass);
/*   29 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   32 */       return localDynFixedOperations.get_value();
/*      */     } finally {
/*   34 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean set_value(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   53 */     ServantObject localServantObject = _servant_preinvoke("set_value", _opsClass);
/*   54 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   57 */       return localDynFixedOperations.set_value(paramString);
/*      */     } finally {
/*   59 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*   75 */     ServantObject localServantObject = _servant_preinvoke("type", _opsClass);
/*   76 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   79 */       return localDynFixedOperations.type();
/*      */     } finally {
/*   81 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assign(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*   97 */     ServantObject localServantObject = _servant_preinvoke("assign", _opsClass);
/*   98 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  101 */       localDynFixedOperations.assign(paramDynAny);
/*      */     } finally {
/*  103 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void from_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  118 */     ServantObject localServantObject = _servant_preinvoke("from_any", _opsClass);
/*  119 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  122 */       localDynFixedOperations.from_any(paramAny);
/*      */     } finally {
/*  124 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any to_any()
/*      */   {
/*  138 */     ServantObject localServantObject = _servant_preinvoke("to_any", _opsClass);
/*  139 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  142 */       return localDynFixedOperations.to_any();
/*      */     } finally {
/*  144 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equal(DynAny paramDynAny)
/*      */   {
/*  159 */     ServantObject localServantObject = _servant_preinvoke("equal", _opsClass);
/*  160 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  163 */       return localDynFixedOperations.equal(paramDynAny);
/*      */     } finally {
/*  165 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  186 */     ServantObject localServantObject = _servant_preinvoke("destroy", _opsClass);
/*  187 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  190 */       localDynFixedOperations.destroy();
/*      */     } finally {
/*  192 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny copy()
/*      */   {
/*  206 */     ServantObject localServantObject = _servant_preinvoke("copy", _opsClass);
/*  207 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  210 */       return localDynFixedOperations.copy();
/*      */     } finally {
/*  212 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  225 */     ServantObject localServantObject = _servant_preinvoke("insert_boolean", _opsClass);
/*  226 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  229 */       localDynFixedOperations.insert_boolean(paramBoolean);
/*      */     } finally {
/*  231 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  244 */     ServantObject localServantObject = _servant_preinvoke("insert_octet", _opsClass);
/*  245 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  248 */       localDynFixedOperations.insert_octet(paramByte);
/*      */     } finally {
/*  250 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  263 */     ServantObject localServantObject = _servant_preinvoke("insert_char", _opsClass);
/*  264 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  267 */       localDynFixedOperations.insert_char(paramChar);
/*      */     } finally {
/*  269 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  282 */     ServantObject localServantObject = _servant_preinvoke("insert_short", _opsClass);
/*  283 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  286 */       localDynFixedOperations.insert_short(paramShort);
/*      */     } finally {
/*  288 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  301 */     ServantObject localServantObject = _servant_preinvoke("insert_ushort", _opsClass);
/*  302 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  305 */       localDynFixedOperations.insert_ushort(paramShort);
/*      */     } finally {
/*  307 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  320 */     ServantObject localServantObject = _servant_preinvoke("insert_long", _opsClass);
/*  321 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  324 */       localDynFixedOperations.insert_long(paramInt);
/*      */     } finally {
/*  326 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  339 */     ServantObject localServantObject = _servant_preinvoke("insert_ulong", _opsClass);
/*  340 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  343 */       localDynFixedOperations.insert_ulong(paramInt);
/*      */     } finally {
/*  345 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  358 */     ServantObject localServantObject = _servant_preinvoke("insert_float", _opsClass);
/*  359 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  362 */       localDynFixedOperations.insert_float(paramFloat);
/*      */     } finally {
/*  364 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  377 */     ServantObject localServantObject = _servant_preinvoke("insert_double", _opsClass);
/*  378 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  381 */       localDynFixedOperations.insert_double(paramDouble);
/*      */     } finally {
/*  383 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  398 */     ServantObject localServantObject = _servant_preinvoke("insert_string", _opsClass);
/*  399 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  402 */       localDynFixedOperations.insert_string(paramString);
/*      */     } finally {
/*  404 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  417 */     ServantObject localServantObject = _servant_preinvoke("insert_reference", _opsClass);
/*  418 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  421 */       localDynFixedOperations.insert_reference(paramObject);
/*      */     } finally {
/*  423 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_typecode(TypeCode paramTypeCode)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  436 */     ServantObject localServantObject = _servant_preinvoke("insert_typecode", _opsClass);
/*  437 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  440 */       localDynFixedOperations.insert_typecode(paramTypeCode);
/*      */     } finally {
/*  442 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  455 */     ServantObject localServantObject = _servant_preinvoke("insert_longlong", _opsClass);
/*  456 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  459 */       localDynFixedOperations.insert_longlong(paramLong);
/*      */     } finally {
/*  461 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  475 */     ServantObject localServantObject = _servant_preinvoke("insert_ulonglong", _opsClass);
/*  476 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  479 */       localDynFixedOperations.insert_ulonglong(paramLong);
/*      */     } finally {
/*  481 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  494 */     ServantObject localServantObject = _servant_preinvoke("insert_wchar", _opsClass);
/*  495 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  498 */       localDynFixedOperations.insert_wchar(paramChar);
/*      */     } finally {
/*  500 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  514 */     ServantObject localServantObject = _servant_preinvoke("insert_wstring", _opsClass);
/*  515 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  518 */       localDynFixedOperations.insert_wstring(paramString);
/*      */     } finally {
/*  520 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  533 */     ServantObject localServantObject = _servant_preinvoke("insert_any", _opsClass);
/*  534 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  537 */       localDynFixedOperations.insert_any(paramAny);
/*      */     } finally {
/*  539 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_dyn_any(DynAny paramDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  552 */     ServantObject localServantObject = _servant_preinvoke("insert_dyn_any", _opsClass);
/*  553 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  556 */       localDynFixedOperations.insert_dyn_any(paramDynAny);
/*      */     } finally {
/*  558 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_val(Serializable paramSerializable)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  572 */     ServantObject localServantObject = _servant_preinvoke("insert_val", _opsClass);
/*  573 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  576 */       localDynFixedOperations.insert_val(paramSerializable);
/*      */     } finally {
/*  578 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean get_boolean()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  593 */     ServantObject localServantObject = _servant_preinvoke("get_boolean", _opsClass);
/*  594 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  597 */       return localDynFixedOperations.get_boolean();
/*      */     } finally {
/*  599 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte get_octet()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  614 */     ServantObject localServantObject = _servant_preinvoke("get_octet", _opsClass);
/*  615 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  618 */       return localDynFixedOperations.get_octet();
/*      */     } finally {
/*  620 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_char()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  635 */     ServantObject localServantObject = _servant_preinvoke("get_char", _opsClass);
/*  636 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  639 */       return localDynFixedOperations.get_char();
/*      */     } finally {
/*  641 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_short()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  656 */     ServantObject localServantObject = _servant_preinvoke("get_short", _opsClass);
/*  657 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  660 */       return localDynFixedOperations.get_short();
/*      */     } finally {
/*  662 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_ushort()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  677 */     ServantObject localServantObject = _servant_preinvoke("get_ushort", _opsClass);
/*  678 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  681 */       return localDynFixedOperations.get_ushort();
/*      */     } finally {
/*  683 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_long()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  698 */     ServantObject localServantObject = _servant_preinvoke("get_long", _opsClass);
/*  699 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  702 */       return localDynFixedOperations.get_long();
/*      */     } finally {
/*  704 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_ulong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  719 */     ServantObject localServantObject = _servant_preinvoke("get_ulong", _opsClass);
/*  720 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  723 */       return localDynFixedOperations.get_ulong();
/*      */     } finally {
/*  725 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float get_float()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  740 */     ServantObject localServantObject = _servant_preinvoke("get_float", _opsClass);
/*  741 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  744 */       return localDynFixedOperations.get_float();
/*      */     } finally {
/*  746 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double get_double()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  761 */     ServantObject localServantObject = _servant_preinvoke("get_double", _opsClass);
/*  762 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  765 */       return localDynFixedOperations.get_double();
/*      */     } finally {
/*  767 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_string()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  783 */     ServantObject localServantObject = _servant_preinvoke("get_string", _opsClass);
/*  784 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  787 */       return localDynFixedOperations.get_string();
/*      */     } finally {
/*  789 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object get_reference()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  804 */     ServantObject localServantObject = _servant_preinvoke("get_reference", _opsClass);
/*  805 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  808 */       return localDynFixedOperations.get_reference();
/*      */     } finally {
/*  810 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode get_typecode()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  825 */     ServantObject localServantObject = _servant_preinvoke("get_typecode", _opsClass);
/*  826 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  829 */       return localDynFixedOperations.get_typecode();
/*      */     } finally {
/*  831 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_longlong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  846 */     ServantObject localServantObject = _servant_preinvoke("get_longlong", _opsClass);
/*  847 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  850 */       return localDynFixedOperations.get_longlong();
/*      */     } finally {
/*  852 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_ulonglong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  868 */     ServantObject localServantObject = _servant_preinvoke("get_ulonglong", _opsClass);
/*  869 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  872 */       return localDynFixedOperations.get_ulonglong();
/*      */     } finally {
/*  874 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_wchar()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  889 */     ServantObject localServantObject = _servant_preinvoke("get_wchar", _opsClass);
/*  890 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  893 */       return localDynFixedOperations.get_wchar();
/*      */     } finally {
/*  895 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_wstring()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  910 */     ServantObject localServantObject = _servant_preinvoke("get_wstring", _opsClass);
/*  911 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  914 */       return localDynFixedOperations.get_wstring();
/*      */     } finally {
/*  916 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any get_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  931 */     ServantObject localServantObject = _servant_preinvoke("get_any", _opsClass);
/*  932 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  935 */       return localDynFixedOperations.get_any();
/*      */     } finally {
/*  937 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny get_dyn_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  953 */     ServantObject localServantObject = _servant_preinvoke("get_dyn_any", _opsClass);
/*  954 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  957 */       return localDynFixedOperations.get_dyn_any();
/*      */     } finally {
/*  959 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable get_val()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  975 */     ServantObject localServantObject = _servant_preinvoke("get_val", _opsClass);
/*  976 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  979 */       return localDynFixedOperations.get_val();
/*      */     } finally {
/*  981 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean seek(int paramInt)
/*      */   {
/*  997 */     ServantObject localServantObject = _servant_preinvoke("seek", _opsClass);
/*  998 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1001 */       return localDynFixedOperations.seek(paramInt);
/*      */     } finally {
/* 1003 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rewind()
/*      */   {
/* 1013 */     ServantObject localServantObject = _servant_preinvoke("rewind", _opsClass);
/* 1014 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1017 */       localDynFixedOperations.rewind();
/*      */     } finally {
/* 1019 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */   {
/* 1032 */     ServantObject localServantObject = _servant_preinvoke("next", _opsClass);
/* 1033 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1036 */       return localDynFixedOperations.next();
/*      */     } finally {
/* 1038 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int component_count()
/*      */   {
/* 1060 */     ServantObject localServantObject = _servant_preinvoke("component_count", _opsClass);
/* 1061 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1064 */       return localDynFixedOperations.component_count();
/*      */     } finally {
/* 1066 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny current_component()
/*      */     throws TypeMismatch
/*      */   {
/* 1091 */     ServantObject localServantObject = _servant_preinvoke("current_component", _opsClass);
/* 1092 */     DynFixedOperations localDynFixedOperations = (DynFixedOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1095 */       return localDynFixedOperations.current_component();
/*      */     } finally {
/* 1097 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] _ids()
/*      */   {
/* 1108 */     return (String[])__ids.clone();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*      */   {
/* 1113 */     String str = paramObjectInputStream.readUTF();
/* 1114 */     String[] arrayOfString = null;
/* 1115 */     Properties localProperties = null;
/* 1116 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 1117 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 1118 */     _set_delegate(localDelegate);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1123 */     String[] arrayOfString = null;
/* 1124 */     Properties localProperties = null;
/* 1125 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 1126 */     paramObjectOutputStream.writeUTF(str);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynFixedStub
 * JD-Core Version:    0.6.2
 */