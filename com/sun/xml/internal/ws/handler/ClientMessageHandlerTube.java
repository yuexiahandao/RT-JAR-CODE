/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
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
/*     */ public class ClientMessageHandlerTube extends HandlerTube
/*     */ {
/*     */   private SEIModel seiModel;
/*     */   private WSBinding binding;
/*     */   private Set<String> roles;
/*     */ 
/*     */   public ClientMessageHandlerTube(@Nullable SEIModel seiModel, WSBinding binding, WSDLPort port, Tube next)
/*     */   {
/*  61 */     super(next, port);
/*  62 */     this.seiModel = seiModel;
/*  63 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */   private ClientMessageHandlerTube(ClientMessageHandlerTube that, TubeCloner cloner)
/*     */   {
/*  70 */     super(that, cloner);
/*  71 */     this.seiModel = that.seiModel;
/*  72 */     this.binding = that.binding;
/*     */   }
/*     */ 
/*     */   public AbstractFilterTubeImpl copy(TubeCloner cloner) {
/*  76 */     return new ClientMessageHandlerTube(this, cloner);
/*     */   }
/*     */ 
/*     */   void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault)
/*     */   {
/*     */     try {
/*  82 */       this.processor.callHandlersResponse(HandlerProcessor.Direction.INBOUND, context, handleFault);
/*     */     }
/*     */     catch (WebServiceException wse)
/*     */     {
/*  86 */       throw wse;
/*     */     } catch (RuntimeException re) {
/*  88 */       throw new WebServiceException(re);
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean callHandlersOnRequest(MessageUpdatableContext context, boolean isOneWay)
/*     */   {
/*  96 */     Map atts = (Map)context.get("javax.xml.ws.binding.attachments.outbound");
/*  97 */     AttachmentSet attSet = this.packet.getMessage().getAttachments();
/*  98 */     for (String cid : atts.keySet()) {
/*  99 */       if (attSet.get(cid) == null) {
/* 100 */         Attachment att = new DataHandlerAttachment(cid, (DataHandler)atts.get(cid));
/* 101 */         attSet.add(att);
/*     */       }
/*     */     }
/*     */     boolean handlerResult;
/*     */     try
/*     */     {
/* 107 */       handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.OUTBOUND, context, !isOneWay);
/*     */     } catch (WebServiceException wse) {
/* 109 */       this.remedyActionTaken = true;
/*     */ 
/* 111 */       throw wse;
/*     */     } catch (RuntimeException re) {
/* 113 */       this.remedyActionTaken = true;
/*     */ 
/* 115 */       throw new WebServiceException(re);
/*     */     }
/*     */ 
/* 118 */     if (!handlerResult) {
/* 119 */       this.remedyActionTaken = true;
/*     */     }
/* 121 */     return handlerResult;
/*     */   }
/*     */ 
/*     */   void closeHandlers(MessageContext mc) {
/* 125 */     closeClientsideHandlers(mc);
/*     */   }
/*     */ 
/*     */   void setUpProcessor()
/*     */   {
/* 132 */     this.handlers = new ArrayList();
/* 133 */     HandlerConfiguration handlerConfig = ((BindingImpl)this.binding).getHandlerConfig();
/* 134 */     List msgHandlersSnapShot = handlerConfig.getMessageHandlers();
/* 135 */     if (!msgHandlersSnapShot.isEmpty()) {
/* 136 */       this.handlers.addAll(msgHandlersSnapShot);
/* 137 */       this.roles = new HashSet();
/* 138 */       this.roles.addAll(handlerConfig.getRoles());
/* 139 */       this.processor = new SOAPHandlerProcessor(true, this, this.binding, this.handlers);
/*     */     }
/*     */   }
/*     */ 
/*     */   MessageUpdatableContext getContext(Packet p)
/*     */   {
/* 146 */     MessageHandlerContextImpl context = new MessageHandlerContextImpl(this.seiModel, this.binding, this.port, this.packet, this.roles);
/* 147 */     return context;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.ClientMessageHandlerTube
 * JD-Core Version:    0.6.2
 */