/*    */ package com.sun.xml.internal.ws.server.provider;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*    */ import com.sun.xml.internal.ws.api.server.Invoker;
/*    */ import com.sun.xml.internal.ws.api.server.TransportBackChannel;
/*    */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ class SyncProviderInvokerTube<T> extends ProviderInvokerTube<T>
/*    */ {
/* 46 */   private static final Logger LOGGER = Logger.getLogger("com.sun.xml.internal.ws.server.SyncProviderInvokerTube");
/*    */ 
/*    */   public SyncProviderInvokerTube(Invoker invoker, ProviderArgumentsBuilder<T> argsBuilder)
/*    */   {
/* 50 */     super(invoker, argsBuilder);
/*    */   }
/*    */ 
/*    */   public NextAction processRequest(Packet request)
/*    */   {
/* 60 */     WSDLPort port = getEndpoint().getPort();
/* 61 */     WSBinding binding = getEndpoint().getBinding();
/* 62 */     Object param = this.argsBuilder.getParameter(request);
/*    */ 
/* 64 */     LOGGER.fine("Invoking Provider Endpoint");
/*    */     Object returnValue;
/*    */     try
/*    */     {
/* 68 */       returnValue = getInvoker(request).invokeProvider(request, param);
/*    */     } catch (Exception e) {
/* 70 */       LOGGER.log(Level.SEVERE, e.getMessage(), e);
/* 71 */       Packet response = this.argsBuilder.getResponse(request, e, port, binding);
/* 72 */       return doReturnWith(response);
/*    */     }
/* 74 */     if (returnValue == null)
/*    */     {
/* 77 */       if (request.transportBackChannel != null) {
/* 78 */         request.transportBackChannel.close();
/*    */       }
/*    */     }
/* 81 */     Packet response = this.argsBuilder.getResponse(request, returnValue, port, binding);
/* 82 */     return doReturnWith(response);
/*    */   }
/*    */ 
/*    */   public NextAction processResponse(Packet response) {
/* 86 */     throw new IllegalStateException("InovkerPipe's processResponse shouldn't be called.");
/*    */   }
/*    */ 
/*    */   public NextAction processException(@NotNull Throwable t) {
/* 90 */     throw new IllegalStateException("InovkerPipe's processException shouldn't be called.");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.provider.SyncProviderInvokerTube
 * JD-Core Version:    0.6.2
 */