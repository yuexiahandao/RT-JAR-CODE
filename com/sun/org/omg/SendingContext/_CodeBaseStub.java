/*     */ package com.sun.org.omg.SendingContext;
/*     */ 
/*     */ import com.sun.org.omg.CORBA.Repository;
/*     */ import com.sun.org.omg.CORBA.RepositoryHelper;
/*     */ import com.sun.org.omg.CORBA.RepositoryIdHelper;
/*     */ import com.sun.org.omg.CORBA.RepositoryIdSeqHelper;
/*     */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
/*     */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescriptionHelper;
/*     */ import com.sun.org.omg.SendingContext.CodeBasePackage.URLHelper;
/*     */ import com.sun.org.omg.SendingContext.CodeBasePackage.URLSeqHelper;
/*     */ import com.sun.org.omg.SendingContext.CodeBasePackage.ValueDescSeqHelper;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ 
/*     */ public class _CodeBaseStub extends ObjectImpl
/*     */   implements CodeBase
/*     */ {
/* 180 */   private static String[] __ids = { "IDL:omg.org/SendingContext/CodeBase:1.0", "IDL:omg.org/SendingContext/RunTime:1.0" };
/*     */ 
/*     */   public _CodeBaseStub()
/*     */   {
/*     */   }
/*     */ 
/*     */   public _CodeBaseStub(Delegate paramDelegate)
/*     */   {
/*  49 */     _set_delegate(paramDelegate);
/*     */   }
/*     */ 
/*     */   public Repository get_ir()
/*     */   {
/*  56 */     InputStream localInputStream = null;
/*     */     try {
/*  58 */       OutputStream localOutputStream = _request("get_ir", true);
/*  59 */       localInputStream = _invoke(localOutputStream);
/*  60 */       localObject1 = RepositoryHelper.read(localInputStream);
/*  61 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  63 */       localInputStream = localApplicationException.getInputStream();
/*  64 */       localObject1 = localApplicationException.getId();
/*  65 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  67 */       return get_ir();
/*     */     } finally {
/*  69 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String implementation(String paramString)
/*     */   {
/*  77 */     InputStream localInputStream = null;
/*     */     try {
/*  79 */       OutputStream localOutputStream = _request("implementation", true);
/*  80 */       RepositoryIdHelper.write(localOutputStream, paramString);
/*  81 */       localInputStream = _invoke(localOutputStream);
/*  82 */       str1 = URLHelper.read(localInputStream);
/*  83 */       return str1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  85 */       localInputStream = localApplicationException.getInputStream();
/*  86 */       str1 = localApplicationException.getId();
/*  87 */       throw new MARSHAL(str1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       String str1;
/*  89 */       return implementation(paramString);
/*     */     } finally {
/*  91 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] implementations(String[] paramArrayOfString)
/*     */   {
/*  97 */     InputStream localInputStream = null;
/*     */     try {
/*  99 */       OutputStream localOutputStream = _request("implementations", true);
/* 100 */       RepositoryIdSeqHelper.write(localOutputStream, paramArrayOfString);
/* 101 */       localInputStream = _invoke(localOutputStream);
/* 102 */       localObject1 = URLSeqHelper.read(localInputStream);
/* 103 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 105 */       localInputStream = localApplicationException.getInputStream();
/* 106 */       localObject1 = localApplicationException.getId();
/* 107 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 109 */       return implementations(paramArrayOfString);
/*     */     } finally {
/* 111 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FullValueDescription meta(String paramString)
/*     */   {
/* 119 */     InputStream localInputStream = null;
/*     */     try {
/* 121 */       OutputStream localOutputStream = _request("meta", true);
/* 122 */       RepositoryIdHelper.write(localOutputStream, paramString);
/* 123 */       localInputStream = _invoke(localOutputStream);
/* 124 */       localObject1 = FullValueDescriptionHelper.read(localInputStream);
/* 125 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 127 */       localInputStream = localApplicationException.getInputStream();
/* 128 */       localObject1 = localApplicationException.getId();
/* 129 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 131 */       return meta(paramString);
/*     */     } finally {
/* 133 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public FullValueDescription[] metas(String[] paramArrayOfString)
/*     */   {
/* 139 */     InputStream localInputStream = null;
/*     */     try {
/* 141 */       OutputStream localOutputStream = _request("metas", true);
/* 142 */       RepositoryIdSeqHelper.write(localOutputStream, paramArrayOfString);
/* 143 */       localInputStream = _invoke(localOutputStream);
/* 144 */       localObject1 = ValueDescSeqHelper.read(localInputStream);
/* 145 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 147 */       localInputStream = localApplicationException.getInputStream();
/* 148 */       localObject1 = localApplicationException.getId();
/* 149 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 151 */       return metas(paramArrayOfString);
/*     */     } finally {
/* 153 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] bases(String paramString)
/*     */   {
/* 161 */     InputStream localInputStream = null;
/*     */     try {
/* 163 */       OutputStream localOutputStream = _request("bases", true);
/* 164 */       RepositoryIdHelper.write(localOutputStream, paramString);
/* 165 */       localInputStream = _invoke(localOutputStream);
/* 166 */       localObject1 = RepositoryIdSeqHelper.read(localInputStream);
/* 167 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 169 */       localInputStream = localApplicationException.getInputStream();
/* 170 */       localObject1 = localApplicationException.getId();
/* 171 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 173 */       return bases(paramString);
/*     */     } finally {
/* 175 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 186 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 193 */       String str = paramObjectInputStream.readUTF();
/* 194 */       org.omg.CORBA.Object localObject = ORB.init().string_to_object(str);
/* 195 */       Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 196 */       _set_delegate(localDelegate);
/*     */     }
/*     */     catch (IOException localIOException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) {
/*     */     try {
/* 204 */       String str = ORB.init().object_to_string(this);
/* 205 */       paramObjectOutputStream.writeUTF(str);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.omg.SendingContext._CodeBaseStub
 * JD-Core Version:    0.6.2
 */