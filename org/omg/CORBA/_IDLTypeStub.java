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
/*     */ public class _IDLTypeStub extends ObjectImpl
/*     */   implements IDLType
/*     */ {
/* 125 */   private static String[] __ids = { "IDL:omg.org/CORBA/IDLType:1.0", "IDL:omg.org/CORBA/IRObject:1.0" };
/*     */ 
/*     */   public _IDLTypeStub()
/*     */   {
/*     */   }
/*     */ 
/*     */   public _IDLTypeStub(Delegate paramDelegate)
/*     */   {
/*  62 */     _set_delegate(paramDelegate);
/*     */   }
/*     */ 
/*     */   public TypeCode type()
/*     */   {
/*  67 */     InputStream localInputStream = null;
/*     */     try {
/*  69 */       OutputStream localOutputStream = _request("_get_type", true);
/*  70 */       localInputStream = _invoke(localOutputStream);
/*  71 */       localObject1 = localInputStream.read_TypeCode();
/*  72 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  74 */       localInputStream = localApplicationException.getInputStream();
/*  75 */       localObject1 = localApplicationException.getId();
/*  76 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  78 */       return type();
/*     */     } finally {
/*  80 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DefinitionKind def_kind()
/*     */   {
/*  88 */     InputStream localInputStream = null;
/*     */     try {
/*  90 */       OutputStream localOutputStream = _request("_get_def_kind", true);
/*  91 */       localInputStream = _invoke(localOutputStream);
/*  92 */       localObject1 = DefinitionKindHelper.read(localInputStream);
/*  93 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  95 */       localInputStream = localApplicationException.getInputStream();
/*  96 */       localObject1 = localApplicationException.getId();
/*  97 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  99 */       return def_kind();
/*     */     } finally {
/* 101 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 109 */     InputStream localInputStream = null;
/*     */     try {
/* 111 */       OutputStream localOutputStream = _request("destroy", true);
/* 112 */       localInputStream = _invoke(localOutputStream);
/*     */     } catch (ApplicationException localApplicationException) {
/* 114 */       localInputStream = localApplicationException.getInputStream();
/* 115 */       String str = localApplicationException.getId();
/* 116 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 118 */       destroy();
/*     */     } finally {
/* 120 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 131 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 138 */       String str = paramObjectInputStream.readUTF();
/* 139 */       Object localObject = ORB.init().string_to_object(str);
/* 140 */       Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 141 */       _set_delegate(localDelegate);
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) {
/*     */     try {
/* 149 */       String str = ORB.init().object_to_string(this);
/* 150 */       paramObjectOutputStream.writeUTF(str);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA._IDLTypeStub
 * JD-Core Version:    0.6.2
 */