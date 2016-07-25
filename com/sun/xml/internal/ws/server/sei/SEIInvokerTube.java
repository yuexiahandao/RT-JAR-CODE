/*    */ package com.sun.xml.internal.ws.server.sei;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*    */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*    */ import com.sun.xml.internal.ws.api.server.Invoker;
/*    */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*    */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*    */ import com.sun.xml.internal.ws.server.InvokerTube;
/*    */ import com.sun.xml.internal.ws.server.WSEndpointImpl;
/*    */ import com.sun.xml.internal.ws.util.QNameMap;
/*    */ import com.sun.xml.internal.ws.wsdl.DispatchException;
/*    */ import com.sun.xml.internal.ws.wsdl.OperationDispatcher;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class SEIInvokerTube extends InvokerTube
/*    */ {
/*    */   private final WSBinding binding;
/*    */   private final AbstractSEIModelImpl model;
/*    */   private final QNameMap<EndpointMethodHandler> wsdlOpMap;
/*    */ 
/*    */   public SEIInvokerTube(AbstractSEIModelImpl model, Invoker invoker, WSBinding binding)
/*    */   {
/* 67 */     super(invoker);
/* 68 */     this.binding = binding;
/* 69 */     this.model = model;
/* 70 */     this.wsdlOpMap = new QNameMap();
/* 71 */     for (JavaMethodImpl jm : model.getJavaMethods())
/* 72 */       this.wsdlOpMap.put(jm.getOperation().getName(), new EndpointMethodHandler(this, jm, binding));
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public NextAction processRequest(@NotNull Packet req)
/*    */   {
/*    */     try
/*    */     {
/* 84 */       QName wsdlOp = ((WSEndpointImpl)getEndpoint()).getOperationDispatcher().getWSDLOperationQName(req);
/* 85 */       Packet res = ((EndpointMethodHandler)this.wsdlOpMap.get(wsdlOp)).invoke(req);
/* 86 */       assert (res != null);
/* 87 */       return doReturnWith(res);
/*    */     } catch (DispatchException e) {
/* 89 */       return doReturnWith(req.createServerResponse(e.fault, this.model.getPort(), null, this.binding));
/*    */     }
/*    */   }
/*    */ 
/* 94 */   @NotNull
/*    */   public NextAction processResponse(@NotNull Packet response) { throw new IllegalStateException("InovkerPipe's processResponse shouldn't be called."); }
/*    */ 
/*    */   @NotNull
/*    */   public NextAction processException(@NotNull Throwable t) {
/* 98 */     throw new IllegalStateException("InovkerPipe's processException shouldn't be called.");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.sei.SEIInvokerTube
 * JD-Core Version:    0.6.2
 */