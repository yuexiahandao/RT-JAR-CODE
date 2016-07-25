/*     */ package org.omg.PortableServer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.ServantObject;
/*     */ 
/*     */ public class _ServantActivatorStub extends ObjectImpl
/*     */   implements ServantActivator
/*     */ {
/*  18 */   public static final Class _opsClass = ServantActivatorOperations.class;
/*     */ 
/*  86 */   private static String[] __ids = { "IDL:omg.org/PortableServer/ServantActivator:2.3", "IDL:omg.org/PortableServer/ServantManager:1.0" };
/*     */ 
/*     */   public Servant incarnate(byte[] paramArrayOfByte, POA paramPOA)
/*     */     throws ForwardRequest
/*     */   {
/*  41 */     ServantObject localServantObject = _servant_preinvoke("incarnate", _opsClass);
/*  42 */     ServantActivatorOperations localServantActivatorOperations = (ServantActivatorOperations)localServantObject.servant;
/*     */     try
/*     */     {
/*  45 */       return localServantActivatorOperations.incarnate(paramArrayOfByte, paramPOA);
/*     */     } finally {
/*  47 */       _servant_postinvoke(localServantObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void etherealize(byte[] paramArrayOfByte, POA paramPOA, Servant paramServant, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*  75 */     ServantObject localServantObject = _servant_preinvoke("etherealize", _opsClass);
/*  76 */     ServantActivatorOperations localServantActivatorOperations = (ServantActivatorOperations)localServantObject.servant;
/*     */     try
/*     */     {
/*  79 */       localServantActivatorOperations.etherealize(paramArrayOfByte, paramPOA, paramServant, paramBoolean1, paramBoolean2);
/*     */     } finally {
/*  81 */       _servant_postinvoke(localServantObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/*  92 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/*  97 */     String str = paramObjectInputStream.readUTF();
/*  98 */     String[] arrayOfString = null;
/*  99 */     Properties localProperties = null;
/* 100 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 101 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 102 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 107 */     String[] arrayOfString = null;
/* 108 */     Properties localProperties = null;
/* 109 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 110 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer._ServantActivatorStub
 * JD-Core Version:    0.6.2
 */