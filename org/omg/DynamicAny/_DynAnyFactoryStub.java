/*     */ package org.omg.DynamicAny;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.ServantObject;
/*     */ import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
/*     */ 
/*     */ public class _DynAnyFactoryStub extends ObjectImpl
/*     */   implements DynAnyFactory
/*     */ {
/*  35 */   public static final Class _opsClass = DynAnyFactoryOperations.class;
/*     */ 
/* 106 */   private static String[] __ids = { "IDL:omg.org/DynamicAny/DynAnyFactory:1.0" };
/*     */ 
/*     */   public DynAny create_dyn_any(Any paramAny)
/*     */     throws InconsistentTypeCode
/*     */   {
/*  51 */     ServantObject localServantObject = _servant_preinvoke("create_dyn_any", _opsClass);
/*  52 */     DynAnyFactoryOperations localDynAnyFactoryOperations = (DynAnyFactoryOperations)localServantObject.servant;
/*     */     try
/*     */     {
/*  55 */       return localDynAnyFactoryOperations.create_dyn_any(paramAny);
/*     */     } finally {
/*  57 */       _servant_postinvoke(localServantObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DynAny create_dyn_any_from_type_code(TypeCode paramTypeCode)
/*     */     throws InconsistentTypeCode
/*     */   {
/*  95 */     ServantObject localServantObject = _servant_preinvoke("create_dyn_any_from_type_code", _opsClass);
/*  96 */     DynAnyFactoryOperations localDynAnyFactoryOperations = (DynAnyFactoryOperations)localServantObject.servant;
/*     */     try
/*     */     {
/*  99 */       return localDynAnyFactoryOperations.create_dyn_any_from_type_code(paramTypeCode);
/*     */     } finally {
/* 101 */       _servant_postinvoke(localServantObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 111 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/* 116 */     String str = paramObjectInputStream.readUTF();
/* 117 */     String[] arrayOfString = null;
/* 118 */     Properties localProperties = null;
/* 119 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 120 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 121 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 126 */     String[] arrayOfString = null;
/* 127 */     Properties localProperties = null;
/* 128 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 129 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.DynamicAny._DynAnyFactoryStub
 * JD-Core Version:    0.6.2
 */