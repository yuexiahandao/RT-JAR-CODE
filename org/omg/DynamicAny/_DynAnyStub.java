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
/*      */ public class _DynAnyStub extends ObjectImpl
/*      */   implements DynAny
/*      */ {
/*   81 */   public static final Class _opsClass = DynAnyOperations.class;
/*      */ 
/* 1123 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*   96 */     ServantObject localServantObject = _servant_preinvoke("type", _opsClass);
/*   97 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  100 */       return localDynAnyOperations.type();
/*      */     } finally {
/*  102 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assign(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*  118 */     ServantObject localServantObject = _servant_preinvoke("assign", _opsClass);
/*  119 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  122 */       localDynAnyOperations.assign(paramDynAny);
/*      */     } finally {
/*  124 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void from_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  139 */     ServantObject localServantObject = _servant_preinvoke("from_any", _opsClass);
/*  140 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  143 */       localDynAnyOperations.from_any(paramAny);
/*      */     } finally {
/*  145 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any to_any()
/*      */   {
/*  159 */     ServantObject localServantObject = _servant_preinvoke("to_any", _opsClass);
/*  160 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  163 */       return localDynAnyOperations.to_any();
/*      */     } finally {
/*  165 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equal(DynAny paramDynAny)
/*      */   {
/*  180 */     ServantObject localServantObject = _servant_preinvoke("equal", _opsClass);
/*  181 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  184 */       return localDynAnyOperations.equal(paramDynAny);
/*      */     } finally {
/*  186 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  207 */     ServantObject localServantObject = _servant_preinvoke("destroy", _opsClass);
/*  208 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  211 */       localDynAnyOperations.destroy();
/*      */     } finally {
/*  213 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny copy()
/*      */   {
/*  227 */     ServantObject localServantObject = _servant_preinvoke("copy", _opsClass);
/*  228 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  231 */       return localDynAnyOperations.copy();
/*      */     } finally {
/*  233 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  246 */     ServantObject localServantObject = _servant_preinvoke("insert_boolean", _opsClass);
/*  247 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  250 */       localDynAnyOperations.insert_boolean(paramBoolean);
/*      */     } finally {
/*  252 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  265 */     ServantObject localServantObject = _servant_preinvoke("insert_octet", _opsClass);
/*  266 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  269 */       localDynAnyOperations.insert_octet(paramByte);
/*      */     } finally {
/*  271 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  284 */     ServantObject localServantObject = _servant_preinvoke("insert_char", _opsClass);
/*  285 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  288 */       localDynAnyOperations.insert_char(paramChar);
/*      */     } finally {
/*  290 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  303 */     ServantObject localServantObject = _servant_preinvoke("insert_short", _opsClass);
/*  304 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  307 */       localDynAnyOperations.insert_short(paramShort);
/*      */     } finally {
/*  309 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  322 */     ServantObject localServantObject = _servant_preinvoke("insert_ushort", _opsClass);
/*  323 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  326 */       localDynAnyOperations.insert_ushort(paramShort);
/*      */     } finally {
/*  328 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  341 */     ServantObject localServantObject = _servant_preinvoke("insert_long", _opsClass);
/*  342 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  345 */       localDynAnyOperations.insert_long(paramInt);
/*      */     } finally {
/*  347 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  360 */     ServantObject localServantObject = _servant_preinvoke("insert_ulong", _opsClass);
/*  361 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  364 */       localDynAnyOperations.insert_ulong(paramInt);
/*      */     } finally {
/*  366 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  379 */     ServantObject localServantObject = _servant_preinvoke("insert_float", _opsClass);
/*  380 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  383 */       localDynAnyOperations.insert_float(paramFloat);
/*      */     } finally {
/*  385 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  398 */     ServantObject localServantObject = _servant_preinvoke("insert_double", _opsClass);
/*  399 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  402 */       localDynAnyOperations.insert_double(paramDouble);
/*      */     } finally {
/*  404 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  419 */     ServantObject localServantObject = _servant_preinvoke("insert_string", _opsClass);
/*  420 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  423 */       localDynAnyOperations.insert_string(paramString);
/*      */     } finally {
/*  425 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  438 */     ServantObject localServantObject = _servant_preinvoke("insert_reference", _opsClass);
/*  439 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  442 */       localDynAnyOperations.insert_reference(paramObject);
/*      */     } finally {
/*  444 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_typecode(TypeCode paramTypeCode)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  457 */     ServantObject localServantObject = _servant_preinvoke("insert_typecode", _opsClass);
/*  458 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  461 */       localDynAnyOperations.insert_typecode(paramTypeCode);
/*      */     } finally {
/*  463 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  476 */     ServantObject localServantObject = _servant_preinvoke("insert_longlong", _opsClass);
/*  477 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  480 */       localDynAnyOperations.insert_longlong(paramLong);
/*      */     } finally {
/*  482 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  496 */     ServantObject localServantObject = _servant_preinvoke("insert_ulonglong", _opsClass);
/*  497 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  500 */       localDynAnyOperations.insert_ulonglong(paramLong);
/*      */     } finally {
/*  502 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  515 */     ServantObject localServantObject = _servant_preinvoke("insert_wchar", _opsClass);
/*  516 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  519 */       localDynAnyOperations.insert_wchar(paramChar);
/*      */     } finally {
/*  521 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  535 */     ServantObject localServantObject = _servant_preinvoke("insert_wstring", _opsClass);
/*  536 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  539 */       localDynAnyOperations.insert_wstring(paramString);
/*      */     } finally {
/*  541 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  554 */     ServantObject localServantObject = _servant_preinvoke("insert_any", _opsClass);
/*  555 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  558 */       localDynAnyOperations.insert_any(paramAny);
/*      */     } finally {
/*  560 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_dyn_any(DynAny paramDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  573 */     ServantObject localServantObject = _servant_preinvoke("insert_dyn_any", _opsClass);
/*  574 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  577 */       localDynAnyOperations.insert_dyn_any(paramDynAny);
/*      */     } finally {
/*  579 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_val(Serializable paramSerializable)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  593 */     ServantObject localServantObject = _servant_preinvoke("insert_val", _opsClass);
/*  594 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  597 */       localDynAnyOperations.insert_val(paramSerializable);
/*      */     } finally {
/*  599 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean get_boolean()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  614 */     ServantObject localServantObject = _servant_preinvoke("get_boolean", _opsClass);
/*  615 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  618 */       return localDynAnyOperations.get_boolean();
/*      */     } finally {
/*  620 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte get_octet()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  635 */     ServantObject localServantObject = _servant_preinvoke("get_octet", _opsClass);
/*  636 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  639 */       return localDynAnyOperations.get_octet();
/*      */     } finally {
/*  641 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_char()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  656 */     ServantObject localServantObject = _servant_preinvoke("get_char", _opsClass);
/*  657 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  660 */       return localDynAnyOperations.get_char();
/*      */     } finally {
/*  662 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_short()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  677 */     ServantObject localServantObject = _servant_preinvoke("get_short", _opsClass);
/*  678 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  681 */       return localDynAnyOperations.get_short();
/*      */     } finally {
/*  683 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_ushort()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  698 */     ServantObject localServantObject = _servant_preinvoke("get_ushort", _opsClass);
/*  699 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  702 */       return localDynAnyOperations.get_ushort();
/*      */     } finally {
/*  704 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_long()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  719 */     ServantObject localServantObject = _servant_preinvoke("get_long", _opsClass);
/*  720 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  723 */       return localDynAnyOperations.get_long();
/*      */     } finally {
/*  725 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_ulong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  740 */     ServantObject localServantObject = _servant_preinvoke("get_ulong", _opsClass);
/*  741 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  744 */       return localDynAnyOperations.get_ulong();
/*      */     } finally {
/*  746 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float get_float()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  761 */     ServantObject localServantObject = _servant_preinvoke("get_float", _opsClass);
/*  762 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  765 */       return localDynAnyOperations.get_float();
/*      */     } finally {
/*  767 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double get_double()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  782 */     ServantObject localServantObject = _servant_preinvoke("get_double", _opsClass);
/*  783 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  786 */       return localDynAnyOperations.get_double();
/*      */     } finally {
/*  788 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_string()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  804 */     ServantObject localServantObject = _servant_preinvoke("get_string", _opsClass);
/*  805 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  808 */       return localDynAnyOperations.get_string();
/*      */     } finally {
/*  810 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object get_reference()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  825 */     ServantObject localServantObject = _servant_preinvoke("get_reference", _opsClass);
/*  826 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  829 */       return localDynAnyOperations.get_reference();
/*      */     } finally {
/*  831 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode get_typecode()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  846 */     ServantObject localServantObject = _servant_preinvoke("get_typecode", _opsClass);
/*  847 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  850 */       return localDynAnyOperations.get_typecode();
/*      */     } finally {
/*  852 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_longlong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  867 */     ServantObject localServantObject = _servant_preinvoke("get_longlong", _opsClass);
/*  868 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  871 */       return localDynAnyOperations.get_longlong();
/*      */     } finally {
/*  873 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_ulonglong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  889 */     ServantObject localServantObject = _servant_preinvoke("get_ulonglong", _opsClass);
/*  890 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  893 */       return localDynAnyOperations.get_ulonglong();
/*      */     } finally {
/*  895 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_wchar()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  910 */     ServantObject localServantObject = _servant_preinvoke("get_wchar", _opsClass);
/*  911 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  914 */       return localDynAnyOperations.get_wchar();
/*      */     } finally {
/*  916 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_wstring()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  931 */     ServantObject localServantObject = _servant_preinvoke("get_wstring", _opsClass);
/*  932 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  935 */       return localDynAnyOperations.get_wstring();
/*      */     } finally {
/*  937 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any get_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  952 */     ServantObject localServantObject = _servant_preinvoke("get_any", _opsClass);
/*  953 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  956 */       return localDynAnyOperations.get_any();
/*      */     } finally {
/*  958 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny get_dyn_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  974 */     ServantObject localServantObject = _servant_preinvoke("get_dyn_any", _opsClass);
/*  975 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  978 */       return localDynAnyOperations.get_dyn_any();
/*      */     } finally {
/*  980 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable get_val()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  996 */     ServantObject localServantObject = _servant_preinvoke("get_val", _opsClass);
/*  997 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1000 */       return localDynAnyOperations.get_val();
/*      */     } finally {
/* 1002 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean seek(int paramInt)
/*      */   {
/* 1018 */     ServantObject localServantObject = _servant_preinvoke("seek", _opsClass);
/* 1019 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1022 */       return localDynAnyOperations.seek(paramInt);
/*      */     } finally {
/* 1024 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rewind()
/*      */   {
/* 1034 */     ServantObject localServantObject = _servant_preinvoke("rewind", _opsClass);
/* 1035 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1038 */       localDynAnyOperations.rewind();
/*      */     } finally {
/* 1040 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */   {
/* 1053 */     ServantObject localServantObject = _servant_preinvoke("next", _opsClass);
/* 1054 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1057 */       return localDynAnyOperations.next();
/*      */     } finally {
/* 1059 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int component_count()
/*      */   {
/* 1081 */     ServantObject localServantObject = _servant_preinvoke("component_count", _opsClass);
/* 1082 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1085 */       return localDynAnyOperations.component_count();
/*      */     } finally {
/* 1087 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny current_component()
/*      */     throws TypeMismatch
/*      */   {
/* 1112 */     ServantObject localServantObject = _servant_preinvoke("current_component", _opsClass);
/* 1113 */     DynAnyOperations localDynAnyOperations = (DynAnyOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1116 */       return localDynAnyOperations.current_component();
/*      */     } finally {
/* 1118 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] _ids()
/*      */   {
/* 1128 */     return (String[])__ids.clone();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*      */   {
/* 1133 */     String str = paramObjectInputStream.readUTF();
/* 1134 */     String[] arrayOfString = null;
/* 1135 */     Properties localProperties = null;
/* 1136 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 1137 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 1138 */     _set_delegate(localDelegate);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1143 */     String[] arrayOfString = null;
/* 1144 */     Properties localProperties = null;
/* 1145 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 1146 */     paramObjectOutputStream.writeUTF(str);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynAnyStub
 * JD-Core Version:    0.6.2
 */