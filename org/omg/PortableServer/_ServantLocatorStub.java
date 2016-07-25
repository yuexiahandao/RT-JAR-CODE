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
/*     */ import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;
/*     */ 
/*     */ public class _ServantLocatorStub extends ObjectImpl
/*     */   implements ServantLocator
/*     */ {
/*  32 */   public static final Class _opsClass = ServantLocatorOperations.class;
/*     */ 
/*  93 */   private static String[] __ids = { "IDL:omg.org/PortableServer/ServantLocator:1.0", "IDL:omg.org/PortableServer/ServantManager:1.0" };
/*     */ 
/*     */   public Servant preinvoke(byte[] paramArrayOfByte, POA paramPOA, String paramString, CookieHolder paramCookieHolder)
/*     */     throws ForwardRequest
/*     */   {
/*  57 */     ServantObject localServantObject = _servant_preinvoke("preinvoke", _opsClass);
/*  58 */     ServantLocatorOperations localServantLocatorOperations = (ServantLocatorOperations)localServantObject.servant;
/*     */     try
/*     */     {
/*  61 */       return localServantLocatorOperations.preinvoke(paramArrayOfByte, paramPOA, paramString, paramCookieHolder);
/*     */     } finally {
/*  63 */       _servant_postinvoke(localServantObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void postinvoke(byte[] paramArrayOfByte, POA paramPOA, String paramString, java.lang.Object paramObject, Servant paramServant)
/*     */   {
/*  82 */     ServantObject localServantObject = _servant_preinvoke("postinvoke", _opsClass);
/*  83 */     ServantLocatorOperations localServantLocatorOperations = (ServantLocatorOperations)localServantObject.servant;
/*     */     try
/*     */     {
/*  86 */       localServantLocatorOperations.postinvoke(paramArrayOfByte, paramPOA, paramString, paramObject, paramServant);
/*     */     } finally {
/*  88 */       _servant_postinvoke(localServantObject);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/*  99 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/* 104 */     String str = paramObjectInputStream.readUTF();
/* 105 */     String[] arrayOfString = null;
/* 106 */     Properties localProperties = null;
/* 107 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 108 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 109 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 114 */     String[] arrayOfString = null;
/* 115 */     Properties localProperties = null;
/* 116 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 117 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.PortableServer._ServantLocatorStub
 * JD-Core Version:    0.6.2
 */