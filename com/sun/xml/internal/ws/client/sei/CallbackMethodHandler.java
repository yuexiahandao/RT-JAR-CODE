/*    */ package com.sun.xml.internal.ws.client.sei;
/*    */ 
/*    */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*    */ import java.util.concurrent.Future;
/*    */ import javax.xml.ws.AsyncHandler;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ final class CallbackMethodHandler extends AsyncMethodHandler
/*    */ {
/*    */   private final int handlerPos;
/*    */ 
/*    */   CallbackMethodHandler(SEIStub owner, JavaMethodImpl jm, JavaMethodImpl core, int handlerPos)
/*    */   {
/* 46 */     super(owner, jm, core);
/* 47 */     this.handlerPos = handlerPos;
/*    */   }
/*    */ 
/*    */   Future<?> invoke(Object proxy, Object[] args) throws WebServiceException
/*    */   {
/* 52 */     AsyncHandler handler = (AsyncHandler)args[this.handlerPos];
/*    */ 
/* 54 */     return doInvoke(proxy, args, handler);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.CallbackMethodHandler
 * JD-Core Version:    0.6.2
 */