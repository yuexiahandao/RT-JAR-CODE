/*     */ package com.sun.org.omg.CORBA;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.omg.CORBA.DefinitionKind;
/*     */ import org.omg.CORBA.IDLType;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ 
/*     */ public class _IDLTypeStub extends ObjectImpl
/*     */   implements IDLType
/*     */ {
/* 120 */   private static String[] __ids = { "IDL:omg.org/CORBA/IDLType:1.0", "IDL:omg.org/CORBA/IRObject:1.0" };
/*     */ 
/*     */   public _IDLTypeStub()
/*     */   {
/*     */   }
/*     */ 
/*     */   public _IDLTypeStub(Delegate paramDelegate)
/*     */   {
/*  53 */     _set_delegate(paramDelegate);
/*     */   }
/*     */ 
/*     */   public TypeCode type()
/*     */   {
/*  58 */     InputStream localInputStream = null;
/*     */     try {
/*  60 */       OutputStream localOutputStream = _request("_get_type", true);
/*  61 */       localInputStream = _invoke(localOutputStream);
/*  62 */       localObject1 = localInputStream.read_TypeCode();
/*  63 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  65 */       localInputStream = localApplicationException.getInputStream();
/*  66 */       localObject1 = localApplicationException.getId();
/*  67 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  69 */       return type();
/*     */     } finally {
/*  71 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DefinitionKind def_kind()
/*     */   {
/*  81 */     InputStream localInputStream = null;
/*     */     try {
/*  83 */       OutputStream localOutputStream = _request("_get_def_kind", true);
/*  84 */       localInputStream = _invoke(localOutputStream);
/*     */ 
/*  87 */       localObject1 = DefinitionKindHelper.read(localInputStream);
/*  88 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  90 */       localInputStream = localApplicationException.getInputStream();
/*  91 */       localObject1 = localApplicationException.getId();
/*  92 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  94 */       return def_kind();
/*     */     } finally {
/*  96 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 104 */     InputStream localInputStream = null;
/*     */     try {
/* 106 */       OutputStream localOutputStream = _request("destroy", true);
/* 107 */       localInputStream = _invoke(localOutputStream);
/*     */     } catch (ApplicationException localApplicationException) {
/* 109 */       localInputStream = localApplicationException.getInputStream();
/* 110 */       String str = localApplicationException.getId();
/* 111 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 113 */       destroy();
/*     */     } finally {
/* 115 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 126 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 133 */       String str = paramObjectInputStream.readUTF();
/* 134 */       org.omg.CORBA.Object localObject = ORB.init().string_to_object(str);
/* 135 */       Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 136 */       _set_delegate(localDelegate);
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) {
/*     */     try {
/* 144 */       String str = ORB.init().object_to_string(this);
/* 145 */       paramObjectOutputStream.writeUTF(str);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.CORBA._IDLTypeStub
 * JD-Core Version:    0.6.2
 */