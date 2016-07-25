/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBound;
/*    */ import com.sun.corba.se.spi.activation.InitialNameServicePackage.NameAlreadyBoundHelper;
/*    */ import java.util.Hashtable;
/*    */ import org.omg.CORBA.BAD_OPERATION;
/*    */ import org.omg.CORBA.CompletionStatus;
/*    */ import org.omg.CORBA.Object;
/*    */ import org.omg.CORBA.ObjectHelper;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.InvokeHandler;
/*    */ import org.omg.CORBA.portable.ObjectImpl;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.ResponseHandler;
/*    */ 
/*    */ public abstract class _InitialNameServiceImplBase extends ObjectImpl
/*    */   implements InitialNameService, InvokeHandler
/*    */ {
/* 20 */   private static Hashtable _methods = new Hashtable();
/*    */ 
/* 62 */   private static String[] __ids = { "IDL:activation/InitialNameService:1.0" };
/*    */ 
/*    */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*    */   {
/* 30 */     OutputStream localOutputStream = null;
/* 31 */     Integer localInteger = (Integer)_methods.get(paramString);
/* 32 */     if (localInteger == null) {
/* 33 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*    */     }
/* 35 */     switch (localInteger.intValue())
/*    */     {
/*    */     case 0:
/*    */       try
/*    */       {
/* 42 */         String str = paramInputStream.read_string();
/* 43 */         Object localObject = ObjectHelper.read(paramInputStream);
/* 44 */         boolean bool = paramInputStream.read_boolean();
/* 45 */         bind(str, localObject, bool);
/* 46 */         localOutputStream = paramResponseHandler.createReply();
/*    */       } catch (NameAlreadyBound localNameAlreadyBound) {
/* 48 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 49 */         NameAlreadyBoundHelper.write(localOutputStream, localNameAlreadyBound);
/*    */       }
/*    */ 
/*    */     default:
/* 55 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*    */     }
/*    */ 
/* 58 */     return localOutputStream;
/*    */   }
/*    */ 
/*    */   public String[] _ids()
/*    */   {
/* 67 */     return (String[])__ids.clone();
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 23 */     _methods.put("bind", new Integer(0));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._InitialNameServiceImplBase
 * JD-Core Version:    0.6.2
 */