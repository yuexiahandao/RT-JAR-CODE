/*    */ package com.sun.xml.internal.ws.protocol.soap;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*    */ import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*    */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*    */ import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
/*    */ import com.sun.xml.internal.ws.client.HandlerConfiguration;
/*    */ import java.util.Set;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class ServerMUTube extends MUTube
/*    */ {
/*    */   private HandlerConfiguration handlerConfig;
/*    */   private ServerTubeAssemblerContext tubeContext;
/*    */   private final Set<String> roles;
/*    */   private final Set<QName> handlerKnownHeaders;
/*    */ 
/*    */   public ServerMUTube(ServerTubeAssemblerContext tubeContext, Tube next)
/*    */   {
/* 48 */     super(tubeContext.getEndpoint().getBinding(), next);
/*    */ 
/* 50 */     this.tubeContext = tubeContext;
/*    */ 
/* 53 */     this.handlerConfig = this.binding.getHandlerConfig();
/* 54 */     this.roles = this.handlerConfig.getRoles();
/* 55 */     this.handlerKnownHeaders = this.handlerConfig.getHandlerKnownHeaders();
/*    */   }
/*    */ 
/*    */   protected ServerMUTube(ServerMUTube that, TubeCloner cloner) {
/* 59 */     super(that, cloner);
/* 60 */     this.handlerConfig = that.handlerConfig;
/* 61 */     this.tubeContext = that.tubeContext;
/* 62 */     this.roles = that.roles;
/* 63 */     this.handlerKnownHeaders = that.handlerKnownHeaders;
/*    */   }
/*    */ 
/*    */   public NextAction processRequest(Packet request)
/*    */   {
/* 76 */     Set misUnderstoodHeaders = getMisUnderstoodHeaders(request.getMessage().getHeaders(), this.roles, this.handlerKnownHeaders);
/* 77 */     if ((misUnderstoodHeaders == null) || (misUnderstoodHeaders.isEmpty())) {
/* 78 */       return doInvoke(this.next, request);
/*    */     }
/* 80 */     return doReturnWith(request.createServerResponse(createMUSOAPFaultMessage(misUnderstoodHeaders), this.tubeContext.getWsdlModel(), this.tubeContext.getSEIModel(), this.tubeContext.getEndpoint().getBinding()));
/*    */   }
/*    */ 
/*    */   public ServerMUTube copy(TubeCloner cloner)
/*    */   {
/* 85 */     return new ServerMUTube(this, cloner);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.protocol.soap.ServerMUTube
 * JD-Core Version:    0.6.2
 */