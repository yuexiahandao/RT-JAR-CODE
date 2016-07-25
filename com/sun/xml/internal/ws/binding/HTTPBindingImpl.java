/*    */ package com.sun.xml.internal.ws.binding;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.BindingID;
/*    */ import com.sun.xml.internal.ws.client.HandlerConfiguration;
/*    */ import com.sun.xml.internal.ws.resources.ClientMessages;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ import javax.xml.ws.handler.Handler;
/*    */ import javax.xml.ws.handler.LogicalHandler;
/*    */ import javax.xml.ws.http.HTTPBinding;
/*    */ 
/*    */ public class HTTPBindingImpl extends BindingImpl
/*    */   implements HTTPBinding
/*    */ {
/*    */   HTTPBindingImpl()
/*    */   {
/* 51 */     super(BindingID.XML_HTTP);
/*    */   }
/*    */ 
/*    */   public void setHandlerChain(List<Handler> chain)
/*    */   {
/* 61 */     List logicalHandlers = new ArrayList();
/* 62 */     for (Handler handler : chain) {
/* 63 */       if (!(handler instanceof LogicalHandler)) {
/* 64 */         throw new WebServiceException(ClientMessages.NON_LOGICAL_HANDLER_SET(handler.getClass()));
/*    */       }
/* 66 */       logicalHandlers.add((LogicalHandler)handler);
/*    */     }
/*    */ 
/* 69 */     this.handlerConfig = new HandlerConfiguration(Collections.emptySet(), chain);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.binding.HTTPBindingImpl
 * JD-Core Version:    0.6.2
 */