/*    */ package com.sun.xml.internal.ws.protocol.soap;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*    */ import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
/*    */ import com.sun.xml.internal.ws.client.HandlerConfiguration;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class ClientMUTube extends MUTube
/*    */ {
/*    */   public ClientMUTube(WSBinding binding, Tube next)
/*    */   {
/* 49 */     super(binding, next);
/*    */   }
/*    */ 
/*    */   protected ClientMUTube(ClientMUTube that, TubeCloner cloner) {
/* 53 */     super(that, cloner);
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public NextAction processResponse(Packet response)
/*    */   {
/* 67 */     if (response.getMessage() == null) {
/* 68 */       return super.processResponse(response);
/*    */     }
/* 70 */     HandlerConfiguration handlerConfig = response.handlerConfig;
/*    */ 
/* 85 */     if (handlerConfig == null)
/*    */     {
/* 88 */       handlerConfig = this.binding.getHandlerConfig();
/*    */     }
/* 90 */     Set misUnderstoodHeaders = getMisUnderstoodHeaders(response.getMessage().getHeaders(), handlerConfig.getRoles(), handlerConfig.getHandlerKnownHeaders());
/* 91 */     if ((misUnderstoodHeaders == null) || (misUnderstoodHeaders.isEmpty())) {
/* 92 */       return super.processResponse(response);
/*    */     }
/* 94 */     throw createMUSOAPFaultException(misUnderstoodHeaders);
/*    */   }
/*    */ 
/*    */   public ClientMUTube copy(TubeCloner cloner) {
/* 98 */     return new ClientMUTube(this, cloner);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.protocol.soap.ClientMUTube
 * JD-Core Version:    0.6.2
 */