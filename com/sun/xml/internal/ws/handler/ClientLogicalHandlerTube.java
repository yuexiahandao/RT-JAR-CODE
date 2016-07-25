/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.client.HandlerConfiguration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.handler.MessageContext;
/*     */ 
/*     */ public class ClientLogicalHandlerTube extends HandlerTube
/*     */ {
/*     */   private WSBinding binding;
/*     */   private SEIModel seiModel;
/*     */ 
/*     */   public ClientLogicalHandlerTube(WSBinding binding, SEIModel seiModel, WSDLPort port, Tube next)
/*     */   {
/*  57 */     super(next, port);
/*  58 */     this.binding = binding;
/*  59 */     this.seiModel = seiModel;
/*     */   }
/*     */ 
/*     */   public ClientLogicalHandlerTube(WSBinding binding, SEIModel seiModel, Tube next, HandlerTube cousinTube)
/*     */   {
/*  70 */     super(next, cousinTube);
/*  71 */     this.binding = binding;
/*  72 */     this.seiModel = seiModel;
/*     */   }
/*     */ 
/*     */   private ClientLogicalHandlerTube(ClientLogicalHandlerTube that, TubeCloner cloner)
/*     */   {
/*  80 */     super(that, cloner);
/*  81 */     this.binding = that.binding;
/*  82 */     this.seiModel = that.seiModel;
/*     */   }
/*     */ 
/*     */   protected void initiateClosing(MessageContext mc)
/*     */   {
/*  88 */     close(mc);
/*  89 */     super.initiateClosing(mc);
/*     */   }
/*     */ 
/*     */   public AbstractFilterTubeImpl copy(TubeCloner cloner) {
/*  93 */     return new ClientLogicalHandlerTube(this, cloner);
/*     */   }
/*     */ 
/*     */   void setUpProcessor()
/*     */   {
/*  99 */     this.handlers = new ArrayList();
/* 100 */     List logicalSnapShot = ((BindingImpl)this.binding).getHandlerConfig().getLogicalHandlers();
/* 101 */     if (!logicalSnapShot.isEmpty()) {
/* 102 */       this.handlers.addAll(logicalSnapShot);
/* 103 */       if (this.binding.getSOAPVersion() == null) {
/* 104 */         this.processor = new XMLHandlerProcessor(this, this.binding, this.handlers);
/*     */       }
/*     */       else
/* 107 */         this.processor = new SOAPHandlerProcessor(true, this, this.binding, this.handlers);
/*     */     }
/*     */   }
/*     */ 
/*     */   MessageUpdatableContext getContext(Packet packet)
/*     */   {
/* 115 */     return new LogicalMessageContextImpl(this.binding, this.seiModel != null ? this.seiModel.getJAXBContext() : null, packet);
/*     */   }
/*     */ 
/*     */   boolean callHandlersOnRequest(MessageUpdatableContext context, boolean isOneWay)
/*     */   {
/*     */     boolean handlerResult;
/*     */     try
/*     */     {
/* 124 */       handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.OUTBOUND, context, !isOneWay);
/*     */     } catch (WebServiceException wse) {
/* 126 */       this.remedyActionTaken = true;
/*     */ 
/* 128 */       throw wse;
/*     */     } catch (RuntimeException re) {
/* 130 */       this.remedyActionTaken = true;
/*     */ 
/* 132 */       throw new WebServiceException(re);
/*     */     }
/*     */ 
/* 135 */     if (!handlerResult) {
/* 136 */       this.remedyActionTaken = true;
/*     */     }
/* 138 */     return handlerResult;
/*     */   }
/*     */ 
/*     */   void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault)
/*     */   {
/*     */     try
/*     */     {
/* 145 */       this.processor.callHandlersResponse(HandlerProcessor.Direction.INBOUND, context, handleFault);
/*     */     }
/*     */     catch (WebServiceException wse)
/*     */     {
/* 149 */       throw wse;
/*     */     }
/*     */     catch (RuntimeException re) {
/* 152 */       throw new WebServiceException(re);
/*     */     }
/*     */   }
/*     */ 
/*     */   void closeHandlers(MessageContext mc) {
/* 157 */     closeClientsideHandlers(mc);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.ClientLogicalHandlerTube
 * JD-Core Version:    0.6.2
 */