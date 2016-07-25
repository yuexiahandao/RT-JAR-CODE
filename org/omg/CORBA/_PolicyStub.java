/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ 
/*     */ public class _PolicyStub extends ObjectImpl
/*     */   implements Policy
/*     */ {
/* 137 */   private static String[] __ids = { "IDL:omg.org/CORBA/Policy:1.0" };
/*     */ 
/*     */   public _PolicyStub()
/*     */   {
/*     */   }
/*     */ 
/*     */   public _PolicyStub(Delegate paramDelegate)
/*     */   {
/*  58 */     _set_delegate(paramDelegate);
/*     */   }
/*     */ 
/*     */   public int policy_type()
/*     */   {
/*  74 */     InputStream localInputStream = null;
/*     */     try {
/*  76 */       OutputStream localOutputStream = _request("_get_policy_type", true);
/*  77 */       localInputStream = _invoke(localOutputStream);
/*  78 */       int i = PolicyTypeHelper.read(localInputStream);
/*  79 */       return i;
/*     */     } catch (ApplicationException localApplicationException) {
/*  81 */       localInputStream = localApplicationException.getInputStream();
/*  82 */       String str = localApplicationException.getId();
/*  83 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  85 */       return policy_type();
/*     */     } finally {
/*  87 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Policy copy()
/*     */   {
/*  98 */     InputStream localInputStream = null;
/*     */     try {
/* 100 */       OutputStream localOutputStream = _request("copy", true);
/* 101 */       localInputStream = _invoke(localOutputStream);
/* 102 */       localObject1 = PolicyHelper.read(localInputStream);
/* 103 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 105 */       localInputStream = localApplicationException.getInputStream();
/* 106 */       localObject1 = localApplicationException.getId();
/* 107 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 109 */       return copy();
/*     */     } finally {
/* 111 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 121 */     InputStream localInputStream = null;
/*     */     try {
/* 123 */       OutputStream localOutputStream = _request("destroy", true);
/* 124 */       localInputStream = _invoke(localOutputStream);
/*     */     } catch (ApplicationException localApplicationException) {
/* 126 */       localInputStream = localApplicationException.getInputStream();
/* 127 */       String str = localApplicationException.getId();
/* 128 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 130 */       destroy();
/*     */     } finally {
/* 132 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 142 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 149 */       String str = paramObjectInputStream.readUTF();
/* 150 */       Object localObject = ORB.init().string_to_object(str);
/* 151 */       Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 152 */       _set_delegate(localDelegate);
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) {
/*     */     try {
/* 160 */       String str = ORB.init().object_to_string(this);
/* 161 */       paramObjectOutputStream.writeUTF(str);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA._PolicyStub
 * JD-Core Version:    0.6.2
 */