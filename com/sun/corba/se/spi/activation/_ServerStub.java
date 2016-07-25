/*     */ package com.sun.corba.se.spi.activation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ 
/*     */ public class _ServerStub extends ObjectImpl
/*     */   implements Server
/*     */ {
/*  83 */   private static String[] __ids = { "IDL:activation/Server:1.0" };
/*     */ 
/*     */   public void shutdown()
/*     */   {
/*  22 */     InputStream localInputStream = null;
/*     */     try {
/*  24 */       OutputStream localOutputStream = _request("shutdown", true);
/*  25 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/*  28 */       localInputStream = localApplicationException.getInputStream();
/*  29 */       String str = localApplicationException.getId();
/*  30 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  32 */       shutdown();
/*     */     } finally {
/*  34 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void install()
/*     */   {
/*  44 */     InputStream localInputStream = null;
/*     */     try {
/*  46 */       OutputStream localOutputStream = _request("install", true);
/*  47 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/*  50 */       localInputStream = localApplicationException.getInputStream();
/*  51 */       String str = localApplicationException.getId();
/*  52 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  54 */       install();
/*     */     } finally {
/*  56 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void uninstall()
/*     */   {
/*  66 */     InputStream localInputStream = null;
/*     */     try {
/*  68 */       OutputStream localOutputStream = _request("uninstall", true);
/*  69 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/*  72 */       localInputStream = localApplicationException.getInputStream();
/*  73 */       String str = localApplicationException.getId();
/*  74 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  76 */       uninstall();
/*     */     } finally {
/*  78 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/*  88 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/*  93 */     String str = paramObjectInputStream.readUTF();
/*  94 */     String[] arrayOfString = null;
/*  95 */     Properties localProperties = null;
/*  96 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/*  97 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/*  98 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 103 */     String[] arrayOfString = null;
/* 104 */     Properties localProperties = null;
/* 105 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 106 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._ServerStub
 * JD-Core Version:    0.6.2
 */