/*    */ package com.sun.xml.internal.ws.handler;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Messages;
/*    */ import java.util.List;
/*    */ import javax.xml.ws.ProtocolException;
/*    */ import javax.xml.ws.handler.Handler;
/*    */ import javax.xml.ws.http.HTTPException;
/*    */ 
/*    */ final class XMLHandlerProcessor<C extends MessageUpdatableContext> extends HandlerProcessor<C>
/*    */ {
/*    */   public XMLHandlerProcessor(HandlerTube owner, WSBinding binding, List<? extends Handler> chain)
/*    */   {
/* 53 */     super(owner, binding, chain);
/*    */   }
/*    */ 
/*    */   final void insertFaultMessage(C context, ProtocolException exception)
/*    */   {
/* 62 */     if ((exception instanceof HTTPException)) {
/* 63 */       context.put("javax.xml.ws.http.response.code", Integer.valueOf(((HTTPException)exception).getStatusCode()));
/*    */     }
/* 65 */     if (context != null)
/*    */     {
/* 67 */       context.setPacketMessage(Messages.createEmpty(this.binding.getSOAPVersion()));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.XMLHandlerProcessor
 * JD-Core Version:    0.6.2
 */