/*    */ package com.sun.xml.internal.ws.client.sei;
/*    */ 
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public abstract class MethodHandler
/*    */ {
/*    */   protected final SEIStub owner;
/*    */ 
/*    */   protected MethodHandler(SEIStub owner)
/*    */   {
/* 44 */     this.owner = owner;
/*    */   }
/*    */ 
/*    */   abstract Object invoke(Object paramObject, Object[] paramArrayOfObject)
/*    */     throws WebServiceException, Throwable;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.MethodHandler
 * JD-Core Version:    0.6.2
 */