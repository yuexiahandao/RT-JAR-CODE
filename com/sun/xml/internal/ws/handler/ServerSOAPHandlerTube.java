/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.client.HandlerConfiguration;
/*     */ import com.sun.xml.internal.ws.message.DataHandlerAttachment;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.handler.MessageContext;
/*     */ 
/*     */ public class ServerSOAPHandlerTube extends HandlerTube
/*     */ {
/*     */   private WSBinding binding;
/*     */   private Set<String> roles;
/*     */ 
/*     */   public ServerSOAPHandlerTube(WSBinding binding, WSDLPort port, Tube next)
/*     */   {
/*  60 */     super(next, port);
/*  61 */     if (binding.getSOAPVersion() != null);
/*  65 */     this.binding = binding;
/*  66 */     setUpHandlersOnce();
/*     */   }
/*     */ 
/*     */   public ServerSOAPHandlerTube(WSBinding binding, Tube next, HandlerTube cousinTube)
/*     */   {
/*  78 */     super(next, cousinTube);
/*  79 */     this.binding = binding;
/*  80 */     setUpHandlersOnce();
/*     */   }
/*     */ 
/*     */   private ServerSOAPHandlerTube(ServerSOAPHandlerTube that, TubeCloner cloner)
/*     */   {
/*  87 */     super(that, cloner);
/*  88 */     this.binding = that.binding;
/*  89 */     this.handlers = that.handlers;
/*  90 */     this.roles = that.roles;
/*     */   }
/*     */ 
/*     */   public AbstractFilterTubeImpl copy(TubeCloner cloner)
/*     */   {
/*  95 */     return new ServerSOAPHandlerTube(this, cloner);
/*     */   }
/*     */ 
/*     */   private void setUpHandlersOnce() {
/*  99 */     this.handlers = new ArrayList();
/* 100 */     HandlerConfiguration handlerConfig = ((BindingImpl)this.binding).getHandlerConfig();
/* 101 */     List soapSnapShot = handlerConfig.getSoapHandlers();
/* 102 */     if (!soapSnapShot.isEmpty()) {
/* 103 */       this.handlers.addAll(soapSnapShot);
/* 104 */       this.roles = new HashSet();
/* 105 */       this.roles.addAll(handlerConfig.getRoles());
/*     */     }
/*     */   }
/*     */ 
/*     */   void setUpProcessor() {
/* 110 */     if (!this.handlers.isEmpty())
/* 111 */       this.processor = new SOAPHandlerProcessor(false, this, this.binding, this.handlers); 
/*     */   }
/*     */ 
/* 114 */   MessageUpdatableContext getContext(Packet packet) { SOAPMessageContextImpl context = new SOAPMessageContextImpl(this.binding, packet, this.roles);
/* 115 */     return context;
/*     */   }
/*     */ 
/*     */   boolean callHandlersOnRequest(MessageUpdatableContext context, boolean isOneWay)
/*     */   {
/*     */     boolean handlerResult;
/*     */     try
/*     */     {
/* 123 */       handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.INBOUND, context, !isOneWay);
/*     */     }
/*     */     catch (RuntimeException re) {
/* 126 */       this.remedyActionTaken = true;
/* 127 */       throw re;
/*     */     }
/*     */ 
/* 130 */     if (!handlerResult) {
/* 131 */       this.remedyActionTaken = true;
/*     */     }
/* 133 */     return handlerResult;
/*     */   }
/*     */ 
/*     */   void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault)
/*     */   {
/* 139 */     Map atts = (Map)context.get("javax.xml.ws.binding.attachments.outbound");
/* 140 */     AttachmentSet attSet = this.packet.getMessage().getAttachments();
/* 141 */     for (String cid : atts.keySet()) {
/* 142 */       if (attSet.get(cid) == null) {
/* 143 */         Attachment att = new DataHandlerAttachment(cid, (DataHandler)atts.get(cid));
/* 144 */         attSet.add(att);
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 150 */       this.processor.callHandlersResponse(HandlerProcessor.Direction.OUTBOUND, context, handleFault);
/*     */     }
/*     */     catch (WebServiceException wse)
/*     */     {
/* 154 */       throw wse;
/*     */     } catch (RuntimeException re) {
/* 156 */       throw re;
/*     */     }
/*     */   }
/*     */ 
/*     */   void closeHandlers(MessageContext mc)
/*     */   {
/* 162 */     closeServersideHandlers(mc);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.ServerSOAPHandlerTube
 * JD-Core Version:    0.6.2
 */