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
/*      */ public class _DynSequenceStub extends ObjectImpl
/*      */   implements DynSequence
/*      */ {
/*   17 */   public static final Class _opsClass = DynSequenceOperations.class;
/*      */ 
/* 1185 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynSequence:1.0", "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*      */ 
/*      */   public int get_length()
/*      */   {
/*   26 */     ServantObject localServantObject = _servant_preinvoke("get_length", _opsClass);
/*   27 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   30 */       return localDynSequenceOperations.get_length();
/*      */     } finally {
/*   32 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_length(int paramInt)
/*      */     throws InvalidValue
/*      */   {
/*   60 */     ServantObject localServantObject = _servant_preinvoke("set_length", _opsClass);
/*   61 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   64 */       localDynSequenceOperations.set_length(paramInt);
/*      */     } finally {
/*   66 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any[] get_elements()
/*      */   {
/*   76 */     ServantObject localServantObject = _servant_preinvoke("get_elements", _opsClass);
/*   77 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   80 */       return localDynSequenceOperations.get_elements();
/*      */     } finally {
/*   82 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_elements(Any[] paramArrayOfAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*   98 */     ServantObject localServantObject = _servant_preinvoke("set_elements", _opsClass);
/*   99 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  102 */       localDynSequenceOperations.set_elements(paramArrayOfAny);
/*      */     } finally {
/*  104 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny[] get_elements_as_dyn_any()
/*      */   {
/*  114 */     ServantObject localServantObject = _servant_preinvoke("get_elements_as_dyn_any", _opsClass);
/*  115 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  118 */       return localDynSequenceOperations.get_elements_as_dyn_any();
/*      */     } finally {
/*  120 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_elements_as_dyn_any(DynAny[] paramArrayOfDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  136 */     ServantObject localServantObject = _servant_preinvoke("set_elements_as_dyn_any", _opsClass);
/*  137 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  140 */       localDynSequenceOperations.set_elements_as_dyn_any(paramArrayOfDynAny);
/*      */     } finally {
/*  142 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*  158 */     ServantObject localServantObject = _servant_preinvoke("type", _opsClass);
/*  159 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  162 */       return localDynSequenceOperations.type();
/*      */     } finally {
/*  164 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assign(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*  180 */     ServantObject localServantObject = _servant_preinvoke("assign", _opsClass);
/*  181 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  184 */       localDynSequenceOperations.assign(paramDynAny);
/*      */     } finally {
/*  186 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void from_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  201 */     ServantObject localServantObject = _servant_preinvoke("from_any", _opsClass);
/*  202 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  205 */       localDynSequenceOperations.from_any(paramAny);
/*      */     } finally {
/*  207 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any to_any()
/*      */   {
/*  221 */     ServantObject localServantObject = _servant_preinvoke("to_any", _opsClass);
/*  222 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  225 */       return localDynSequenceOperations.to_any();
/*      */     } finally {
/*  227 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equal(DynAny paramDynAny)
/*      */   {
/*  242 */     ServantObject localServantObject = _servant_preinvoke("equal", _opsClass);
/*  243 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  246 */       return localDynSequenceOperations.equal(paramDynAny);
/*      */     } finally {
/*  248 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  269 */     ServantObject localServantObject = _servant_preinvoke("destroy", _opsClass);
/*  270 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  273 */       localDynSequenceOperations.destroy();
/*      */     } finally {
/*  275 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny copy()
/*      */   {
/*  289 */     ServantObject localServantObject = _servant_preinvoke("copy", _opsClass);
/*  290 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  293 */       return localDynSequenceOperations.copy();
/*      */     } finally {
/*  295 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  308 */     ServantObject localServantObject = _servant_preinvoke("insert_boolean", _opsClass);
/*  309 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  312 */       localDynSequenceOperations.insert_boolean(paramBoolean);
/*      */     } finally {
/*  314 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  327 */     ServantObject localServantObject = _servant_preinvoke("insert_octet", _opsClass);
/*  328 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  331 */       localDynSequenceOperations.insert_octet(paramByte);
/*      */     } finally {
/*  333 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  346 */     ServantObject localServantObject = _servant_preinvoke("insert_char", _opsClass);
/*  347 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  350 */       localDynSequenceOperations.insert_char(paramChar);
/*      */     } finally {
/*  352 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  365 */     ServantObject localServantObject = _servant_preinvoke("insert_short", _opsClass);
/*  366 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  369 */       localDynSequenceOperations.insert_short(paramShort);
/*      */     } finally {
/*  371 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  384 */     ServantObject localServantObject = _servant_preinvoke("insert_ushort", _opsClass);
/*  385 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  388 */       localDynSequenceOperations.insert_ushort(paramShort);
/*      */     } finally {
/*  390 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  403 */     ServantObject localServantObject = _servant_preinvoke("insert_long", _opsClass);
/*  404 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  407 */       localDynSequenceOperations.insert_long(paramInt);
/*      */     } finally {
/*  409 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  422 */     ServantObject localServantObject = _servant_preinvoke("insert_ulong", _opsClass);
/*  423 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  426 */       localDynSequenceOperations.insert_ulong(paramInt);
/*      */     } finally {
/*  428 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  441 */     ServantObject localServantObject = _servant_preinvoke("insert_float", _opsClass);
/*  442 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  445 */       localDynSequenceOperations.insert_float(paramFloat);
/*      */     } finally {
/*  447 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  460 */     ServantObject localServantObject = _servant_preinvoke("insert_double", _opsClass);
/*  461 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  464 */       localDynSequenceOperations.insert_double(paramDouble);
/*      */     } finally {
/*  466 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  481 */     ServantObject localServantObject = _servant_preinvoke("insert_string", _opsClass);
/*  482 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  485 */       localDynSequenceOperations.insert_string(paramString);
/*      */     } finally {
/*  487 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  500 */     ServantObject localServantObject = _servant_preinvoke("insert_reference", _opsClass);
/*  501 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  504 */       localDynSequenceOperations.insert_reference(paramObject);
/*      */     } finally {
/*  506 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_typecode(TypeCode paramTypeCode)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  519 */     ServantObject localServantObject = _servant_preinvoke("insert_typecode", _opsClass);
/*  520 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  523 */       localDynSequenceOperations.insert_typecode(paramTypeCode);
/*      */     } finally {
/*  525 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  538 */     ServantObject localServantObject = _servant_preinvoke("insert_longlong", _opsClass);
/*  539 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  542 */       localDynSequenceOperations.insert_longlong(paramLong);
/*      */     } finally {
/*  544 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  558 */     ServantObject localServantObject = _servant_preinvoke("insert_ulonglong", _opsClass);
/*  559 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  562 */       localDynSequenceOperations.insert_ulonglong(paramLong);
/*      */     } finally {
/*  564 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  577 */     ServantObject localServantObject = _servant_preinvoke("insert_wchar", _opsClass);
/*  578 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  581 */       localDynSequenceOperations.insert_wchar(paramChar);
/*      */     } finally {
/*  583 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  597 */     ServantObject localServantObject = _servant_preinvoke("insert_wstring", _opsClass);
/*  598 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  601 */       localDynSequenceOperations.insert_wstring(paramString);
/*      */     } finally {
/*  603 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  616 */     ServantObject localServantObject = _servant_preinvoke("insert_any", _opsClass);
/*  617 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  620 */       localDynSequenceOperations.insert_any(paramAny);
/*      */     } finally {
/*  622 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_dyn_any(DynAny paramDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  635 */     ServantObject localServantObject = _servant_preinvoke("insert_dyn_any", _opsClass);
/*  636 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  639 */       localDynSequenceOperations.insert_dyn_any(paramDynAny);
/*      */     } finally {
/*  641 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_val(Serializable paramSerializable)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  655 */     ServantObject localServantObject = _servant_preinvoke("insert_val", _opsClass);
/*  656 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  659 */       localDynSequenceOperations.insert_val(paramSerializable);
/*      */     } finally {
/*  661 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean get_boolean()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  676 */     ServantObject localServantObject = _servant_preinvoke("get_boolean", _opsClass);
/*  677 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  680 */       return localDynSequenceOperations.get_boolean();
/*      */     } finally {
/*  682 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte get_octet()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  697 */     ServantObject localServantObject = _servant_preinvoke("get_octet", _opsClass);
/*  698 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  701 */       return localDynSequenceOperations.get_octet();
/*      */     } finally {
/*  703 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_char()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  718 */     ServantObject localServantObject = _servant_preinvoke("get_char", _opsClass);
/*  719 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  722 */       return localDynSequenceOperations.get_char();
/*      */     } finally {
/*  724 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_short()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  739 */     ServantObject localServantObject = _servant_preinvoke("get_short", _opsClass);
/*  740 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  743 */       return localDynSequenceOperations.get_short();
/*      */     } finally {
/*  745 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_ushort()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  760 */     ServantObject localServantObject = _servant_preinvoke("get_ushort", _opsClass);
/*  761 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  764 */       return localDynSequenceOperations.get_ushort();
/*      */     } finally {
/*  766 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_long()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  781 */     ServantObject localServantObject = _servant_preinvoke("get_long", _opsClass);
/*  782 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  785 */       return localDynSequenceOperations.get_long();
/*      */     } finally {
/*  787 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_ulong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  802 */     ServantObject localServantObject = _servant_preinvoke("get_ulong", _opsClass);
/*  803 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  806 */       return localDynSequenceOperations.get_ulong();
/*      */     } finally {
/*  808 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float get_float()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  823 */     ServantObject localServantObject = _servant_preinvoke("get_float", _opsClass);
/*  824 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  827 */       return localDynSequenceOperations.get_float();
/*      */     } finally {
/*  829 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double get_double()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  844 */     ServantObject localServantObject = _servant_preinvoke("get_double", _opsClass);
/*  845 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  848 */       return localDynSequenceOperations.get_double();
/*      */     } finally {
/*  850 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_string()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  866 */     ServantObject localServantObject = _servant_preinvoke("get_string", _opsClass);
/*  867 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  870 */       return localDynSequenceOperations.get_string();
/*      */     } finally {
/*  872 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object get_reference()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  887 */     ServantObject localServantObject = _servant_preinvoke("get_reference", _opsClass);
/*  888 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  891 */       return localDynSequenceOperations.get_reference();
/*      */     } finally {
/*  893 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode get_typecode()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  908 */     ServantObject localServantObject = _servant_preinvoke("get_typecode", _opsClass);
/*  909 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  912 */       return localDynSequenceOperations.get_typecode();
/*      */     } finally {
/*  914 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_longlong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  929 */     ServantObject localServantObject = _servant_preinvoke("get_longlong", _opsClass);
/*  930 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  933 */       return localDynSequenceOperations.get_longlong();
/*      */     } finally {
/*  935 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_ulonglong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  951 */     ServantObject localServantObject = _servant_preinvoke("get_ulonglong", _opsClass);
/*  952 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  955 */       return localDynSequenceOperations.get_ulonglong();
/*      */     } finally {
/*  957 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_wchar()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  972 */     ServantObject localServantObject = _servant_preinvoke("get_wchar", _opsClass);
/*  973 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  976 */       return localDynSequenceOperations.get_wchar();
/*      */     } finally {
/*  978 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_wstring()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  993 */     ServantObject localServantObject = _servant_preinvoke("get_wstring", _opsClass);
/*  994 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  997 */       return localDynSequenceOperations.get_wstring();
/*      */     } finally {
/*  999 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any get_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1014 */     ServantObject localServantObject = _servant_preinvoke("get_any", _opsClass);
/* 1015 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1018 */       return localDynSequenceOperations.get_any();
/*      */     } finally {
/* 1020 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny get_dyn_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1036 */     ServantObject localServantObject = _servant_preinvoke("get_dyn_any", _opsClass);
/* 1037 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1040 */       return localDynSequenceOperations.get_dyn_any();
/*      */     } finally {
/* 1042 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable get_val()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1058 */     ServantObject localServantObject = _servant_preinvoke("get_val", _opsClass);
/* 1059 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1062 */       return localDynSequenceOperations.get_val();
/*      */     } finally {
/* 1064 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean seek(int paramInt)
/*      */   {
/* 1080 */     ServantObject localServantObject = _servant_preinvoke("seek", _opsClass);
/* 1081 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1084 */       return localDynSequenceOperations.seek(paramInt);
/*      */     } finally {
/* 1086 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rewind()
/*      */   {
/* 1096 */     ServantObject localServantObject = _servant_preinvoke("rewind", _opsClass);
/* 1097 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1100 */       localDynSequenceOperations.rewind();
/*      */     } finally {
/* 1102 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */   {
/* 1115 */     ServantObject localServantObject = _servant_preinvoke("next", _opsClass);
/* 1116 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1119 */       return localDynSequenceOperations.next();
/*      */     } finally {
/* 1121 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int component_count()
/*      */   {
/* 1143 */     ServantObject localServantObject = _servant_preinvoke("component_count", _opsClass);
/* 1144 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1147 */       return localDynSequenceOperations.component_count();
/*      */     } finally {
/* 1149 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny current_component()
/*      */     throws TypeMismatch
/*      */   {
/* 1174 */     ServantObject localServantObject = _servant_preinvoke("current_component", _opsClass);
/* 1175 */     DynSequenceOperations localDynSequenceOperations = (DynSequenceOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1178 */       return localDynSequenceOperations.current_component();
/*      */     } finally {
/* 1180 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] _ids()
/*      */   {
/* 1191 */     return (String[])__ids.clone();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*      */   {
/* 1196 */     String str = paramObjectInputStream.readUTF();
/* 1197 */     String[] arrayOfString = null;
/* 1198 */     Properties localProperties = null;
/* 1199 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 1200 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 1201 */     _set_delegate(localDelegate);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1206 */     String[] arrayOfString = null;
/* 1207 */     Properties localProperties = null;
/* 1208 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 1209 */     paramObjectOutputStream.writeUTF(str);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynSequenceStub
 * JD-Core Version:    0.6.2
 */