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
/*      */ public class _DynUnionStub extends ObjectImpl
/*      */   implements DynUnion
/*      */ {
/*   24 */   public static final Class _opsClass = DynUnionOperations.class;
/*      */ 
/* 1242 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynUnion:1.0", "IDL:omg.org/DynamicAny/DynAny:1.0" };
/*      */ 
/*      */   public DynAny get_discriminator()
/*      */   {
/*   33 */     ServantObject localServantObject = _servant_preinvoke("get_discriminator", _opsClass);
/*   34 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   37 */       return localDynUnionOperations.get_discriminator();
/*      */     } finally {
/*   39 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_discriminator(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*   61 */     ServantObject localServantObject = _servant_preinvoke("set_discriminator", _opsClass);
/*   62 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   65 */       localDynUnionOperations.set_discriminator(paramDynAny);
/*      */     } finally {
/*   67 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_to_default_member()
/*      */     throws TypeMismatch
/*      */   {
/*   80 */     ServantObject localServantObject = _servant_preinvoke("set_to_default_member", _opsClass);
/*   81 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*   84 */       localDynUnionOperations.set_to_default_member();
/*      */     } finally {
/*   86 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set_to_no_active_member()
/*      */     throws TypeMismatch
/*      */   {
/*  100 */     ServantObject localServantObject = _servant_preinvoke("set_to_no_active_member", _opsClass);
/*  101 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  104 */       localDynUnionOperations.set_to_no_active_member();
/*      */     } finally {
/*  106 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean has_no_active_member()
/*      */   {
/*  120 */     ServantObject localServantObject = _servant_preinvoke("has_no_active_member", _opsClass);
/*  121 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  124 */       return localDynUnionOperations.has_no_active_member();
/*      */     } finally {
/*  126 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TCKind discriminator_kind()
/*      */   {
/*  136 */     ServantObject localServantObject = _servant_preinvoke("discriminator_kind", _opsClass);
/*  137 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  140 */       return localDynUnionOperations.discriminator_kind();
/*      */     } finally {
/*  142 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TCKind member_kind()
/*      */     throws InvalidValue
/*      */   {
/*  154 */     ServantObject localServantObject = _servant_preinvoke("member_kind", _opsClass);
/*  155 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  158 */       return localDynUnionOperations.member_kind();
/*      */     } finally {
/*  160 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny member()
/*      */     throws InvalidValue
/*      */   {
/*  174 */     ServantObject localServantObject = _servant_preinvoke("member", _opsClass);
/*  175 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  178 */       return localDynUnionOperations.member();
/*      */     } finally {
/*  180 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String member_name()
/*      */     throws InvalidValue
/*      */   {
/*  193 */     ServantObject localServantObject = _servant_preinvoke("member_name", _opsClass);
/*  194 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  197 */       return localDynUnionOperations.member_name();
/*      */     } finally {
/*  199 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*  215 */     ServantObject localServantObject = _servant_preinvoke("type", _opsClass);
/*  216 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  219 */       return localDynUnionOperations.type();
/*      */     } finally {
/*  221 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void assign(DynAny paramDynAny)
/*      */     throws TypeMismatch
/*      */   {
/*  237 */     ServantObject localServantObject = _servant_preinvoke("assign", _opsClass);
/*  238 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  241 */       localDynUnionOperations.assign(paramDynAny);
/*      */     } finally {
/*  243 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void from_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  258 */     ServantObject localServantObject = _servant_preinvoke("from_any", _opsClass);
/*  259 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  262 */       localDynUnionOperations.from_any(paramAny);
/*      */     } finally {
/*  264 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any to_any()
/*      */   {
/*  278 */     ServantObject localServantObject = _servant_preinvoke("to_any", _opsClass);
/*  279 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  282 */       return localDynUnionOperations.to_any();
/*      */     } finally {
/*  284 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean equal(DynAny paramDynAny)
/*      */   {
/*  299 */     ServantObject localServantObject = _servant_preinvoke("equal", _opsClass);
/*  300 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  303 */       return localDynUnionOperations.equal(paramDynAny);
/*      */     } finally {
/*  305 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  326 */     ServantObject localServantObject = _servant_preinvoke("destroy", _opsClass);
/*  327 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  330 */       localDynUnionOperations.destroy();
/*      */     } finally {
/*  332 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny copy()
/*      */   {
/*  346 */     ServantObject localServantObject = _servant_preinvoke("copy", _opsClass);
/*  347 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  350 */       return localDynUnionOperations.copy();
/*      */     } finally {
/*  352 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  365 */     ServantObject localServantObject = _servant_preinvoke("insert_boolean", _opsClass);
/*  366 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  369 */       localDynUnionOperations.insert_boolean(paramBoolean);
/*      */     } finally {
/*  371 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  384 */     ServantObject localServantObject = _servant_preinvoke("insert_octet", _opsClass);
/*  385 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  388 */       localDynUnionOperations.insert_octet(paramByte);
/*      */     } finally {
/*  390 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  403 */     ServantObject localServantObject = _servant_preinvoke("insert_char", _opsClass);
/*  404 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  407 */       localDynUnionOperations.insert_char(paramChar);
/*      */     } finally {
/*  409 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  422 */     ServantObject localServantObject = _servant_preinvoke("insert_short", _opsClass);
/*  423 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  426 */       localDynUnionOperations.insert_short(paramShort);
/*      */     } finally {
/*  428 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  441 */     ServantObject localServantObject = _servant_preinvoke("insert_ushort", _opsClass);
/*  442 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  445 */       localDynUnionOperations.insert_ushort(paramShort);
/*      */     } finally {
/*  447 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  460 */     ServantObject localServantObject = _servant_preinvoke("insert_long", _opsClass);
/*  461 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  464 */       localDynUnionOperations.insert_long(paramInt);
/*      */     } finally {
/*  466 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  479 */     ServantObject localServantObject = _servant_preinvoke("insert_ulong", _opsClass);
/*  480 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  483 */       localDynUnionOperations.insert_ulong(paramInt);
/*      */     } finally {
/*  485 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  498 */     ServantObject localServantObject = _servant_preinvoke("insert_float", _opsClass);
/*  499 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  502 */       localDynUnionOperations.insert_float(paramFloat);
/*      */     } finally {
/*  504 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  517 */     ServantObject localServantObject = _servant_preinvoke("insert_double", _opsClass);
/*  518 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  521 */       localDynUnionOperations.insert_double(paramDouble);
/*      */     } finally {
/*  523 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  538 */     ServantObject localServantObject = _servant_preinvoke("insert_string", _opsClass);
/*  539 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  542 */       localDynUnionOperations.insert_string(paramString);
/*      */     } finally {
/*  544 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  557 */     ServantObject localServantObject = _servant_preinvoke("insert_reference", _opsClass);
/*  558 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  561 */       localDynUnionOperations.insert_reference(paramObject);
/*      */     } finally {
/*  563 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_typecode(TypeCode paramTypeCode)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  576 */     ServantObject localServantObject = _servant_preinvoke("insert_typecode", _opsClass);
/*  577 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  580 */       localDynUnionOperations.insert_typecode(paramTypeCode);
/*      */     } finally {
/*  582 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  595 */     ServantObject localServantObject = _servant_preinvoke("insert_longlong", _opsClass);
/*  596 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  599 */       localDynUnionOperations.insert_longlong(paramLong);
/*      */     } finally {
/*  601 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  615 */     ServantObject localServantObject = _servant_preinvoke("insert_ulonglong", _opsClass);
/*  616 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  619 */       localDynUnionOperations.insert_ulonglong(paramLong);
/*      */     } finally {
/*  621 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  634 */     ServantObject localServantObject = _servant_preinvoke("insert_wchar", _opsClass);
/*  635 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  638 */       localDynUnionOperations.insert_wchar(paramChar);
/*      */     } finally {
/*  640 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  654 */     ServantObject localServantObject = _servant_preinvoke("insert_wstring", _opsClass);
/*  655 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  658 */       localDynUnionOperations.insert_wstring(paramString);
/*      */     } finally {
/*  660 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  673 */     ServantObject localServantObject = _servant_preinvoke("insert_any", _opsClass);
/*  674 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  677 */       localDynUnionOperations.insert_any(paramAny);
/*      */     } finally {
/*  679 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_dyn_any(DynAny paramDynAny)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  692 */     ServantObject localServantObject = _servant_preinvoke("insert_dyn_any", _opsClass);
/*  693 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  696 */       localDynUnionOperations.insert_dyn_any(paramDynAny);
/*      */     } finally {
/*  698 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_val(Serializable paramSerializable)
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  712 */     ServantObject localServantObject = _servant_preinvoke("insert_val", _opsClass);
/*  713 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  716 */       localDynUnionOperations.insert_val(paramSerializable);
/*      */     } finally {
/*  718 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean get_boolean()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  733 */     ServantObject localServantObject = _servant_preinvoke("get_boolean", _opsClass);
/*  734 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  737 */       return localDynUnionOperations.get_boolean();
/*      */     } finally {
/*  739 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public byte get_octet()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  754 */     ServantObject localServantObject = _servant_preinvoke("get_octet", _opsClass);
/*  755 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  758 */       return localDynUnionOperations.get_octet();
/*      */     } finally {
/*  760 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_char()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  775 */     ServantObject localServantObject = _servant_preinvoke("get_char", _opsClass);
/*  776 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  779 */       return localDynUnionOperations.get_char();
/*      */     } finally {
/*  781 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_short()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  796 */     ServantObject localServantObject = _servant_preinvoke("get_short", _opsClass);
/*  797 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  800 */       return localDynUnionOperations.get_short();
/*      */     } finally {
/*  802 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public short get_ushort()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  817 */     ServantObject localServantObject = _servant_preinvoke("get_ushort", _opsClass);
/*  818 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  821 */       return localDynUnionOperations.get_ushort();
/*      */     } finally {
/*  823 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_long()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  838 */     ServantObject localServantObject = _servant_preinvoke("get_long", _opsClass);
/*  839 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  842 */       return localDynUnionOperations.get_long();
/*      */     } finally {
/*  844 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int get_ulong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  859 */     ServantObject localServantObject = _servant_preinvoke("get_ulong", _opsClass);
/*  860 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  863 */       return localDynUnionOperations.get_ulong();
/*      */     } finally {
/*  865 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float get_float()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  880 */     ServantObject localServantObject = _servant_preinvoke("get_float", _opsClass);
/*  881 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  884 */       return localDynUnionOperations.get_float();
/*      */     } finally {
/*  886 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public double get_double()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  901 */     ServantObject localServantObject = _servant_preinvoke("get_double", _opsClass);
/*  902 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  905 */       return localDynUnionOperations.get_double();
/*      */     } finally {
/*  907 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_string()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  923 */     ServantObject localServantObject = _servant_preinvoke("get_string", _opsClass);
/*  924 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  927 */       return localDynUnionOperations.get_string();
/*      */     } finally {
/*  929 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object get_reference()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  944 */     ServantObject localServantObject = _servant_preinvoke("get_reference", _opsClass);
/*  945 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  948 */       return localDynUnionOperations.get_reference();
/*      */     } finally {
/*  950 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode get_typecode()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  965 */     ServantObject localServantObject = _servant_preinvoke("get_typecode", _opsClass);
/*  966 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  969 */       return localDynUnionOperations.get_typecode();
/*      */     } finally {
/*  971 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_longlong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/*  986 */     ServantObject localServantObject = _servant_preinvoke("get_longlong", _opsClass);
/*  987 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/*  990 */       return localDynUnionOperations.get_longlong();
/*      */     } finally {
/*  992 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public long get_ulonglong()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1008 */     ServantObject localServantObject = _servant_preinvoke("get_ulonglong", _opsClass);
/* 1009 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1012 */       return localDynUnionOperations.get_ulonglong();
/*      */     } finally {
/* 1014 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public char get_wchar()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1029 */     ServantObject localServantObject = _servant_preinvoke("get_wchar", _opsClass);
/* 1030 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1033 */       return localDynUnionOperations.get_wchar();
/*      */     } finally {
/* 1035 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get_wstring()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1050 */     ServantObject localServantObject = _servant_preinvoke("get_wstring", _opsClass);
/* 1051 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1054 */       return localDynUnionOperations.get_wstring();
/*      */     } finally {
/* 1056 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Any get_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1071 */     ServantObject localServantObject = _servant_preinvoke("get_any", _opsClass);
/* 1072 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1075 */       return localDynUnionOperations.get_any();
/*      */     } finally {
/* 1077 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny get_dyn_any()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1093 */     ServantObject localServantObject = _servant_preinvoke("get_dyn_any", _opsClass);
/* 1094 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1097 */       return localDynUnionOperations.get_dyn_any();
/*      */     } finally {
/* 1099 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable get_val()
/*      */     throws TypeMismatch, InvalidValue
/*      */   {
/* 1115 */     ServantObject localServantObject = _servant_preinvoke("get_val", _opsClass);
/* 1116 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1119 */       return localDynUnionOperations.get_val();
/*      */     } finally {
/* 1121 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean seek(int paramInt)
/*      */   {
/* 1137 */     ServantObject localServantObject = _servant_preinvoke("seek", _opsClass);
/* 1138 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1141 */       return localDynUnionOperations.seek(paramInt);
/*      */     } finally {
/* 1143 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rewind()
/*      */   {
/* 1153 */     ServantObject localServantObject = _servant_preinvoke("rewind", _opsClass);
/* 1154 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1157 */       localDynUnionOperations.rewind();
/*      */     } finally {
/* 1159 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean next()
/*      */   {
/* 1172 */     ServantObject localServantObject = _servant_preinvoke("next", _opsClass);
/* 1173 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1176 */       return localDynUnionOperations.next();
/*      */     } finally {
/* 1178 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int component_count()
/*      */   {
/* 1200 */     ServantObject localServantObject = _servant_preinvoke("component_count", _opsClass);
/* 1201 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1204 */       return localDynUnionOperations.component_count();
/*      */     } finally {
/* 1206 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DynAny current_component()
/*      */     throws TypeMismatch
/*      */   {
/* 1231 */     ServantObject localServantObject = _servant_preinvoke("current_component", _opsClass);
/* 1232 */     DynUnionOperations localDynUnionOperations = (DynUnionOperations)localServantObject.servant;
/*      */     try
/*      */     {
/* 1235 */       return localDynUnionOperations.current_component();
/*      */     } finally {
/* 1237 */       _servant_postinvoke(localServantObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] _ids()
/*      */   {
/* 1248 */     return (String[])__ids.clone();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*      */   {
/* 1253 */     String str = paramObjectInputStream.readUTF();
/* 1254 */     String[] arrayOfString = null;
/* 1255 */     Properties localProperties = null;
/* 1256 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 1257 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 1258 */     _set_delegate(localDelegate);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*      */   {
/* 1263 */     String[] arrayOfString = null;
/* 1264 */     Properties localProperties = null;
/* 1265 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 1266 */     paramObjectOutputStream.writeUTF(str);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynUnionStub
 * JD-Core Version:    0.6.2
 */