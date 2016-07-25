/*    */ package com.sun.xml.internal.ws.server.provider;
/*    */ 
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import javax.xml.ws.soap.SOAPBinding;
/*    */ 
/*    */ abstract class ProviderArgumentsBuilder<T>
/*    */ {
/*    */   protected abstract Message getResponseMessage(Exception paramException);
/*    */ 
/*    */   protected Packet getResponse(Packet request, Exception e, WSDLPort port, WSBinding binding)
/*    */   {
/* 51 */     Message message = getResponseMessage(e);
/* 52 */     Packet response = request.createServerResponse(message, port, null, binding);
/* 53 */     return response;
/*    */   }
/*    */ 
/*    */   protected abstract T getParameter(Packet paramPacket);
/*    */ 
/*    */   protected abstract Message getResponseMessage(T paramT);
/*    */ 
/*    */   protected Packet getResponse(Packet request, @Nullable T returnValue, WSDLPort port, WSBinding binding)
/*    */   {
/* 68 */     Message message = null;
/* 69 */     if (returnValue != null) {
/* 70 */       message = getResponseMessage(returnValue);
/*    */     }
/* 72 */     Packet response = request.createServerResponse(message, port, null, binding);
/* 73 */     return response;
/*    */   }
/*    */ 
/*    */   public static ProviderArgumentsBuilder<?> create(ProviderEndpointModel model, WSBinding binding) {
/* 77 */     return (binding instanceof SOAPBinding) ? SOAPProviderArgumentBuilder.create(model, binding.getSOAPVersion()) : XMLProviderArgumentBuilder.createBuilder(model, binding);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.provider.ProviderArgumentsBuilder
 * JD-Core Version:    0.6.2
 */