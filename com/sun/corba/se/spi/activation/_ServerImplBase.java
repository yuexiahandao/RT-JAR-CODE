/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import org.omg.CORBA.BAD_OPERATION;
/*    */ import org.omg.CORBA.CompletionStatus;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.InvokeHandler;
/*    */ import org.omg.CORBA.portable.ObjectImpl;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.ResponseHandler;
/*    */ 
/*    */ public abstract class _ServerImplBase extends ObjectImpl
/*    */   implements Server, InvokeHandler
/*    */ {
/* 23 */   private static Hashtable _methods = new Hashtable();
/*    */ 
/* 82 */   private static String[] __ids = { "IDL:activation/Server:1.0" };
/*    */ 
/*    */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*    */   {
/* 35 */     OutputStream localOutputStream = null;
/* 36 */     Integer localInteger = (Integer)_methods.get(paramString);
/* 37 */     if (localInteger == null) {
/* 38 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*    */     }
/* 40 */     switch (localInteger.intValue())
/*    */     {
/*    */     case 0:
/* 47 */       shutdown();
/* 48 */       localOutputStream = paramResponseHandler.createReply();
/* 49 */       break;
/*    */     case 1:
/* 58 */       install();
/* 59 */       localOutputStream = paramResponseHandler.createReply();
/* 60 */       break;
/*    */     case 2:
/* 69 */       uninstall();
/* 70 */       localOutputStream = paramResponseHandler.createReply();
/* 71 */       break;
/*    */     default:
/* 75 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*    */     }
/*    */ 
/* 78 */     return localOutputStream;
/*    */   }
/*    */ 
/*    */   public String[] _ids()
/*    */   {
/* 87 */     return (String[])__ids.clone();
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 26 */     _methods.put("shutdown", new Integer(0));
/* 27 */     _methods.put("install", new Integer(1));
/* 28 */     _methods.put("uninstall", new Integer(2));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._ServerImplBase
 * JD-Core Version:    0.6.2
 */