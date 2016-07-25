/*    */ package com.sun.xml.internal.ws.client.sei;
/*    */ 
/*    */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*    */ import javax.xml.ws.Response;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ final class PollingMethodHandler extends AsyncMethodHandler
/*    */ {
/*    */   PollingMethodHandler(SEIStub owner, JavaMethodImpl jm, JavaMethodImpl core)
/*    */   {
/* 39 */     super(owner, jm, core);
/*    */   }
/*    */ 
/*    */   Response<?> invoke(Object proxy, Object[] args) throws WebServiceException {
/* 43 */     return doInvoke(proxy, args, null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.sei.PollingMethodHandler
 * JD-Core Version:    0.6.2
 */