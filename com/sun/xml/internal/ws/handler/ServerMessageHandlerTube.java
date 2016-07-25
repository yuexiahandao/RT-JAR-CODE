/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
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
/*     */ public class ServerMessageHandlerTube extends HandlerTube
/*     */ {
/*     */   private SEIModel seiModel;
/*     */   private WSBinding binding;
/*     */   private Set<String> roles;
/*     */ 
/*     */   public ServerMessageHandlerTube(SEIModel seiModel, WSBinding binding, Tube next, HandlerTube cousinTube)
/*     */   {
/*  56 */     super(next, cousinTube);
/*  57 */     this.seiModel = seiModel;
/*  58 */     this.binding = binding;
/*  59 */     setUpHandlersOnce();
/*     */   }
/*     */ 
/*     */   private ServerMessageHandlerTube(ServerMessageHandlerTube that, TubeCloner cloner)
/*     */   {
/*  66 */     super(that, cloner);
/*  67 */     this.seiModel = that.seiModel;
/*  68 */     this.binding = that.binding;
/*  69 */     this.handlers = that.handlers;
/*  70 */     this.roles = that.roles;
/*     */   }
/*     */ 
/*     */   private void setUpHandlersOnce() {
/*  74 */     this.handlers = new ArrayList();
/*  75 */     HandlerConfiguration handlerConfig = ((BindingImpl)this.binding).getHandlerConfig();
/*  76 */     List msgHandlersSnapShot = handlerConfig.getMessageHandlers();
/*  77 */     if (!msgHandlersSnapShot.isEmpty()) {
/*  78 */       this.handlers.addAll(msgHandlersSnapShot);
/*  79 */       this.roles = new HashSet();
/*  80 */       this.roles.addAll(handlerConfig.getRoles());
/*     */     }
/*     */   }
/*     */ 
/*     */   void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault)
/*     */   {
/*  86 */     Map atts = (Map)context.get("javax.xml.ws.binding.attachments.outbound");
/*  87 */     AttachmentSet attSet = this.packet.getMessage().getAttachments();
/*  88 */     for (String cid : atts.keySet()) {
/*  89 */       if (attSet.get(cid) == null) {
/*  90 */         Attachment att = new DataHandlerAttachment(cid, (DataHandler)atts.get(cid));
/*  91 */         attSet.add(att);
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  97 */       this.processor.callHandlersResponse(HandlerProcessor.Direction.OUTBOUND, context, handleFault);
/*     */     }
/*     */     catch (WebServiceException wse)
/*     */     {
/* 101 */       throw wse;
/*     */     } catch (RuntimeException re) {
/* 103 */       throw re;
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean callHandlersOnRequest(MessageUpdatableContext context, boolean isOneWay)
/*     */   {
/*     */     boolean handlerResult;
/*     */     try
/*     */     {
/* 112 */       handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.INBOUND, context, !isOneWay);
/*     */     }
/*     */     catch (RuntimeException re) {
/* 115 */       this.remedyActionTaken = true;
/* 116 */       throw re;
/*     */     }
/*     */ 
/* 119 */     if (!handlerResult) {
/* 120 */       this.remedyActionTaken = true;
/*     */     }
/* 122 */     return handlerResult;
/*     */   }
/*     */ 
/*     */   void setUpProcessor() {
/* 126 */     if (!this.handlers.isEmpty())
/* 127 */       this.processor = new SOAPHandlerProcessor(false, this, this.binding, this.handlers);
/*     */   }
/*     */ 
/*     */   void closeHandlers(MessageContext mc)
/*     */   {
/* 132 */     closeServersideHandlers(mc);
/*     */   }
/*     */ 
/*     */   MessageUpdatableContext getContext(Packet packet) {
/* 136 */     MessageHandlerContextImpl context = new MessageHandlerContextImpl(this.seiModel, this.binding, this.port, packet, this.roles);
/* 137 */     return context;
/*     */   }
/*     */ 
/*     */   protected void initiateClosing(MessageContext mc)
/*     */   {
/* 143 */     close(mc);
/* 144 */     super.initiateClosing(mc);
/*     */   }
/*     */ 
/*     */   public AbstractFilterTubeImpl copy(TubeCloner cloner) {
/* 148 */     return new ServerMessageHandlerTube(this, cloner);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.ServerMessageHandlerTube
 * JD-Core Version:    0.6.2
 */