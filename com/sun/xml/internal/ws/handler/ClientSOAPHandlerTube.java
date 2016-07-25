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
/*     */ public class ClientSOAPHandlerTube extends HandlerTube
/*     */ {
/*     */   private WSBinding binding;
/*     */   private Set<String> roles;
/*     */ 
/*     */   public ClientSOAPHandlerTube(WSBinding binding, WSDLPort port, Tube next)
/*     */   {
/*  60 */     super(next, port);
/*  61 */     if (binding.getSOAPVersion() != null);
/*  65 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */   public ClientSOAPHandlerTube(WSBinding binding, Tube next, HandlerTube cousinTube)
/*     */   {
/*  77 */     super(next, cousinTube);
/*  78 */     this.binding = binding;
/*     */   }
/*     */ 
/*     */   private ClientSOAPHandlerTube(ClientSOAPHandlerTube that, TubeCloner cloner)
/*     */   {
/*  85 */     super(that, cloner);
/*  86 */     this.binding = that.binding;
/*     */   }
/*     */ 
/*     */   public AbstractFilterTubeImpl copy(TubeCloner cloner) {
/*  90 */     return new ClientSOAPHandlerTube(this, cloner);
/*     */   }
/*     */ 
/*     */   void setUpProcessor()
/*     */   {
/*  96 */     this.handlers = new ArrayList();
/*  97 */     HandlerConfiguration handlerConfig = ((BindingImpl)this.binding).getHandlerConfig();
/*  98 */     List soapSnapShot = handlerConfig.getSoapHandlers();
/*  99 */     if (!soapSnapShot.isEmpty()) {
/* 100 */       this.handlers.addAll(soapSnapShot);
/* 101 */       this.roles = new HashSet();
/* 102 */       this.roles.addAll(handlerConfig.getRoles());
/* 103 */       this.processor = new SOAPHandlerProcessor(true, this, this.binding, this.handlers);
/*     */     }
/*     */   }
/*     */ 
/*     */   MessageUpdatableContext getContext(Packet packet) {
/* 108 */     SOAPMessageContextImpl context = new SOAPMessageContextImpl(this.binding, packet, this.roles);
/* 109 */     return context;
/*     */   }
/*     */ 
/*     */   boolean callHandlersOnRequest(MessageUpdatableContext context, boolean isOneWay)
/*     */   {
/* 116 */     Map atts = (Map)context.get("javax.xml.ws.binding.attachments.outbound");
/* 117 */     AttachmentSet attSet = this.packet.getMessage().getAttachments();
/* 118 */     for (String cid : atts.keySet()) {
/* 119 */       if (attSet.get(cid) == null) {
/* 120 */         Attachment att = new DataHandlerAttachment(cid, (DataHandler)atts.get(cid));
/* 121 */         attSet.add(att);
/*     */       }
/*     */     }
/*     */     boolean handlerResult;
/*     */     try
/*     */     {
/* 127 */       handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.OUTBOUND, context, !isOneWay);
/*     */     } catch (WebServiceException wse) {
/* 129 */       this.remedyActionTaken = true;
/*     */ 
/* 131 */       throw wse;
/*     */     } catch (RuntimeException re) {
/* 133 */       this.remedyActionTaken = true;
/*     */ 
/* 135 */       throw new WebServiceException(re);
/*     */     }
/*     */ 
/* 138 */     if (!handlerResult) {
/* 139 */       this.remedyActionTaken = true;
/*     */     }
/* 141 */     return handlerResult;
/*     */   }
/*     */ 
/*     */   void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault)
/*     */   {
/*     */     try
/*     */     {
/* 148 */       this.processor.callHandlersResponse(HandlerProcessor.Direction.INBOUND, context, handleFault);
/*     */     }
/*     */     catch (WebServiceException wse)
/*     */     {
/* 152 */       throw wse;
/*     */     } catch (RuntimeException re) {
/* 154 */       throw new WebServiceException(re);
/*     */     }
/*     */   }
/*     */ 
/*     */   void closeHandlers(MessageContext mc) {
/* 159 */     closeClientsideHandlers(mc);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.ClientSOAPHandlerTube
 * JD-Core Version:    0.6.2
 */