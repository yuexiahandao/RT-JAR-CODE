/*     */ package org.omg.CosNaming;
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
/*     */ public class _BindingIteratorStub extends ObjectImpl
/*     */   implements BindingIterator
/*     */ {
/* 102 */   private static String[] __ids = { "IDL:omg.org/CosNaming/BindingIterator:1.0" };
/*     */ 
/*     */   public boolean next_one(BindingHolder paramBindingHolder)
/*     */   {
/*  32 */     InputStream localInputStream = null;
/*     */     try {
/*  34 */       OutputStream localOutputStream = _request("next_one", true);
/*  35 */       localInputStream = _invoke(localOutputStream);
/*  36 */       boolean bool1 = localInputStream.read_boolean();
/*  37 */       paramBindingHolder.value = BindingHelper.read(localInputStream);
/*  38 */       return bool1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  40 */       localInputStream = localApplicationException.getInputStream();
/*  41 */       String str = localApplicationException.getId();
/*  42 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  44 */       return next_one(paramBindingHolder);
/*     */     } finally {
/*  46 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean next_n(int paramInt, BindingListHolder paramBindingListHolder)
/*     */   {
/*  60 */     InputStream localInputStream = null;
/*     */     try {
/*  62 */       OutputStream localOutputStream = _request("next_n", true);
/*  63 */       localOutputStream.write_ulong(paramInt);
/*  64 */       localInputStream = _invoke(localOutputStream);
/*  65 */       boolean bool1 = localInputStream.read_boolean();
/*  66 */       paramBindingListHolder.value = BindingListHelper.read(localInputStream);
/*  67 */       return bool1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  69 */       localInputStream = localApplicationException.getInputStream();
/*  70 */       String str = localApplicationException.getId();
/*  71 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  73 */       return next_n(paramInt, paramBindingListHolder);
/*     */     } finally {
/*  75 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/*  85 */     InputStream localInputStream = null;
/*     */     try {
/*  87 */       OutputStream localOutputStream = _request("destroy", true);
/*  88 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/*  91 */       localInputStream = localApplicationException.getInputStream();
/*  92 */       String str = localApplicationException.getId();
/*  93 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  95 */       destroy();
/*     */     } finally {
/*  97 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 107 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/* 112 */     String str = paramObjectInputStream.readUTF();
/* 113 */     String[] arrayOfString = null;
/* 114 */     Properties localProperties = null;
/* 115 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 116 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 117 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 122 */     String[] arrayOfString = null;
/* 123 */     Properties localProperties = null;
/* 124 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 125 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming._BindingIteratorStub
 * JD-Core Version:    0.6.2
 */