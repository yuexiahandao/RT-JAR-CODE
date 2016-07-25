/*    */ package com.sun.xml.internal.ws.client;
/*    */ 
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public abstract class AsyncInvoker
/*    */   implements Runnable
/*    */ {
/*    */   protected AsyncResponseImpl responseImpl;
/*    */ 
/*    */   public void setReceiver(AsyncResponseImpl responseImpl)
/*    */   {
/* 49 */     this.responseImpl = responseImpl;
/*    */   }
/*    */ 
/*    */   public void run() {
/*    */     try {
/* 54 */       do_run();
/*    */     } catch (WebServiceException e) {
/* 56 */       throw e;
/*    */     }
/*    */     catch (Throwable t) {
/* 59 */       throw new WebServiceException(t);
/*    */     }
/*    */   }
/*    */ 
/*    */   public abstract void do_run();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.AsyncInvoker
 * JD-Core Version:    0.6.2
 */