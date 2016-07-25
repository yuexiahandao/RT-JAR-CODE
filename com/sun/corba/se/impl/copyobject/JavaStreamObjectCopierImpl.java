/*    */ package com.sun.corba.se.impl.copyobject;
/*    */ 
/*    */ import com.sun.corba.se.impl.util.Utility;
/*    */ import com.sun.corba.se.spi.copyobject.ObjectCopier;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.PrintStream;
/*    */ import java.rmi.Remote;
/*    */ import org.omg.CORBA.ORB;
/*    */ 
/*    */ public class JavaStreamObjectCopierImpl
/*    */   implements ObjectCopier
/*    */ {
/*    */   private ORB orb;
/*    */ 
/*    */   public JavaStreamObjectCopierImpl(ORB paramORB)
/*    */   {
/* 51 */     this.orb = paramORB;
/*    */   }
/*    */ 
/*    */   public Object copy(Object paramObject) {
/* 55 */     if ((paramObject instanceof Remote))
/*    */     {
/* 58 */       return Utility.autoConnect(paramObject, this.orb, true);
/*    */     }
/*    */     try
/*    */     {
/* 62 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(10000);
/* 63 */       ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
/* 64 */       localObjectOutputStream.writeObject(paramObject);
/*    */ 
/* 66 */       byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
/* 67 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
/* 68 */       ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
/*    */ 
/* 70 */       return localObjectInputStream.readObject();
/*    */     } catch (Exception localException) {
/* 72 */       System.out.println("Failed with exception:" + localException);
/* 73 */     }return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.copyobject.JavaStreamObjectCopierImpl
 * JD-Core Version:    0.6.2
 */