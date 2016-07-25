/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
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
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.activation.DataHandler;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.handler.MessageContext;
/*     */ 
/*     */ public class ServerLogicalHandlerTube extends HandlerTube
/*     */ {
/*     */   private WSBinding binding;
/*     */   private SEIModel seiModel;
/*     */ 
/*     */   public ServerLogicalHandlerTube(WSBinding binding, SEIModel seiModel, WSDLPort port, Tube next)
/*     */   {
/*  61 */     super(next, port);
/*  62 */     this.binding = binding;
/*  63 */     this.seiModel = seiModel;
/*  64 */     setUpHandlersOnce();
/*     */   }
/*     */ 
/*     */   public ServerLogicalHandlerTube(WSBinding binding, SEIModel seiModel, Tube next, HandlerTube cousinTube)
/*     */   {
/*  75 */     super(next, cousinTube);
/*  76 */     this.binding = binding;
/*  77 */     this.seiModel = seiModel;
/*  78 */     setUpHandlersOnce();
/*     */   }
/*     */ 
/*     */   private ServerLogicalHandlerTube(ServerLogicalHandlerTube that, TubeCloner cloner)
/*     */   {
/*  86 */     super(that, cloner);
/*  87 */     this.binding = that.binding;
/*  88 */     this.seiModel = that.seiModel;
/*  89 */     this.handlers = that.handlers;
/*     */   }
/*     */ 
/*     */   protected void initiateClosing(MessageContext mc)
/*     */   {
/*  95 */     if (this.binding.getSOAPVersion() != null) {
/*  96 */       super.initiateClosing(mc);
/*     */     } else {
/*  98 */       close(mc);
/*  99 */       super.initiateClosing(mc);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AbstractFilterTubeImpl copy(TubeCloner cloner) {
/* 104 */     return new ServerLogicalHandlerTube(this, cloner);
/*     */   }
/*     */ 
/*     */   private void setUpHandlersOnce() {
/* 108 */     this.handlers = new ArrayList();
/* 109 */     List logicalSnapShot = ((BindingImpl)this.binding).getHandlerConfig().getLogicalHandlers();
/* 110 */     if (!logicalSnapShot.isEmpty())
/* 111 */       this.handlers.addAll(logicalSnapShot);
/*     */   }
/*     */ 
/*     */   void setUpProcessor()
/*     */   {
/* 116 */     if (!this.handlers.isEmpty())
/* 117 */       if (this.binding.getSOAPVersion() == null) {
/* 118 */         this.processor = new XMLHandlerProcessor(this, this.binding, this.handlers);
/*     */       }
/*     */       else
/* 121 */         this.processor = new SOAPHandlerProcessor(false, this, this.binding, this.handlers);
/*     */   }
/*     */ 
/*     */   MessageUpdatableContext getContext(Packet packet)
/*     */   {
/* 127 */     return new LogicalMessageContextImpl(this.binding, this.seiModel != null ? this.seiModel.getJAXBContext() : null, packet);
/*     */   }
/*     */ 
/*     */   boolean callHandlersOnRequest(MessageUpdatableContext context, boolean isOneWay)
/*     */   {
/*     */     boolean handlerResult;
/*     */     try
/*     */     {
/* 135 */       handlerResult = this.processor.callHandlersRequest(HandlerProcessor.Direction.INBOUND, context, !isOneWay);
/*     */     }
/*     */     catch (RuntimeException re) {
/* 138 */       this.remedyActionTaken = true;
/* 139 */       throw re;
/*     */     }
/* 141 */     if (!handlerResult) {
/* 142 */       this.remedyActionTaken = true;
/*     */     }
/* 144 */     return handlerResult;
/*     */   }
/*     */ 
/*     */   void callHandlersOnResponse(MessageUpdatableContext context, boolean handleFault)
/*     */   {
/* 149 */     Map atts = (Map)context.get("javax.xml.ws.binding.attachments.outbound");
/* 150 */     AttachmentSet attSet = this.packet.getMessage().getAttachments();
/* 151 */     for (String cid : atts.keySet()) {
/* 152 */       Attachment att = new DataHandlerAttachment(cid, (DataHandler)atts.get(cid));
/* 153 */       attSet.add(att);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 158 */       this.processor.callHandlersResponse(HandlerProcessor.Direction.OUTBOUND, context, handleFault);
/*     */     }
/*     */     catch (WebServiceException wse)
/*     */     {
/* 162 */       throw wse;
/*     */     } catch (RuntimeException re) {
/* 164 */       throw re;
/*     */     }
/*     */   }
/*     */ 
/*     */   void closeHandlers(MessageContext mc) {
/* 169 */     closeServersideHandlers(mc);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.ServerLogicalHandlerTube
 * JD-Core Version:    0.6.2
 */