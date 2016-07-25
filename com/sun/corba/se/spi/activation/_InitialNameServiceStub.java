/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBound;
/*    */ import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBoundHelper;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.Properties;
/*    */ import org.omg.CORBA.MARSHAL;
/*    */ import org.omg.CORBA.ORB;
/*    */ import org.omg.CORBA.ObjectHelper;
/*    */ import org.omg.CORBA.portable.ApplicationException;
/*    */ import org.omg.CORBA.portable.Delegate;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.ObjectImpl;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.RemarshalException;
/*    */ 
/*    */ public class _InitialNameServiceStub extends ObjectImpl
/*    */   implements InitialNameService
/*    */ {
/* 41 */   private static String[] __ids = { "IDL:activation/InitialNameService:1.0" };
/*    */ 
/*    */   public void bind(String paramString, org.omg.CORBA.Object paramObject, boolean paramBoolean)
/*    */     throws NameAlreadyBound
/*    */   {
/* 18 */     InputStream localInputStream = null;
/*    */     try {
/* 20 */       OutputStream localOutputStream = _request("bind", true);
/* 21 */       localOutputStream.write_string(paramString);
/* 22 */       ObjectHelper.write(localOutputStream, paramObject);
/* 23 */       localOutputStream.write_boolean(paramBoolean);
/* 24 */       localInputStream = _invoke(localOutputStream);
/*    */     }
/*    */     catch (ApplicationException localApplicationException) {
/* 27 */       localInputStream = localApplicationException.getInputStream();
/* 28 */       String str = localApplicationException.getId();
/* 29 */       if (str.equals("IDL:activation/InitialNameService/NameAlreadyBound:1.0")) {
/* 30 */         throw NameAlreadyBoundHelper.read(localInputStream);
/*    */       }
/* 32 */       throw new MARSHAL(str);
/*    */     } catch (RemarshalException localRemarshalException) {
/* 34 */       bind(paramString, paramObject, paramBoolean);
/*    */     } finally {
/* 36 */       _releaseReply(localInputStream);
/*    */     }
/*    */   }
/*    */ 
/*    */   public String[] _ids()
/*    */   {
/* 46 */     return (String[])__ids.clone();
/*    */   }
/*    */ 
/*    */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*    */   {
/* 51 */     String str = paramObjectInputStream.readUTF();
/* 52 */     String[] arrayOfString = null;
/* 53 */     Properties localProperties = null;
/* 54 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 55 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 56 */     _set_delegate(localDelegate);
/*    */   }
/*    */ 
/*    */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*    */   {
/* 61 */     String[] arrayOfString = null;
/* 62 */     Properties localProperties = null;
/* 63 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 64 */     paramObjectOutputStream.writeUTF(str);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._InitialNameServiceStub
 * JD-Core Version:    0.6.2
 */