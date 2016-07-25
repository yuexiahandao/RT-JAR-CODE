/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
/*     */ import com.sun.xml.internal.ws.api.server.TransportBackChannel;
/*     */ import java.util.List;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import javax.xml.ws.handler.MessageContext;
/*     */ 
/*     */ public abstract class HandlerTube extends AbstractFilterTubeImpl
/*     */ {
/*     */   HandlerTube cousinTube;
/*     */   protected List<Handler> handlers;
/*     */   HandlerProcessor processor;
/*  49 */   boolean remedyActionTaken = false;
/*     */ 
/*     */   @Nullable
/*     */   protected final WSDLPort port;
/*  52 */   boolean requestProcessingSucessful = false;
/*     */   Packet packet;
/*     */   private HandlerTubeExchange exchange;
/*     */ 
/*  59 */   public HandlerTube(Tube next, WSDLPort port) { super(next);
/*  60 */     this.port = port; }
/*     */ 
/*     */   public HandlerTube(Tube next, HandlerTube cousinTube)
/*     */   {
/*  64 */     super(next);
/*  65 */     this.cousinTube = cousinTube;
/*  66 */     if (cousinTube != null)
/*  67 */       this.port = cousinTube.port;
/*     */     else
/*  69 */       this.port = null;
/*     */   }
/*     */ 
/*     */   protected HandlerTube(HandlerTube that, TubeCloner cloner)
/*     */   {
/*  77 */     super(that, cloner);
/*  78 */     if (that.cousinTube != null) {
/*  79 */       this.cousinTube = ((HandlerTube)cloner.copy(that.cousinTube));
/*     */     }
/*  81 */     this.port = that.port;
/*     */   }
/*     */ 
/*     */   public NextAction processRequest(Packet request)
/*     */   {
/*  86 */     this.packet = request;
/*  87 */     setupExchange();
/*     */ 
/*  89 */     if (isHandleFalse())
/*     */     {
/*  92 */       this.remedyActionTaken = true;
/*  93 */       return doInvoke(this.next, this.packet);
/*     */     }
/*     */ 
/*  98 */     setUpProcessor();
/*     */ 
/* 100 */     MessageUpdatableContext context = getContext(this.packet);
/* 101 */     boolean isOneWay = checkOneWay(this.packet);
/*     */     try
/*     */     {
/*     */       boolean handlerResult;
/* 103 */       if (!isHandlerChainEmpty())
/*     */       {
/* 105 */         handlerResult = callHandlersOnRequest(context, isOneWay);
/*     */ 
/* 107 */         context.updatePacket();
/*     */ 
/* 109 */         if ((!isOneWay) && (!handlerResult)) {
/* 110 */           return doReturnWith(this.packet);
/*     */         }
/*     */       }
/* 113 */       this.requestProcessingSucessful = true;
/*     */ 
/* 115 */       return doInvoke(this.next, this.packet);
/*     */     }
/*     */     catch (RuntimeException re)
/*     */     {
/*     */       NextAction localNextAction;
/* 117 */       if (isOneWay)
/*     */       {
/* 119 */         if (this.packet.transportBackChannel != null) {
/* 120 */           this.packet.transportBackChannel.close();
/*     */         }
/* 122 */         this.packet.setMessage(null);
/* 123 */         return doReturnWith(this.packet);
/*     */       }
/* 125 */       throw re;
/*     */     } finally {
/* 127 */       if (!this.requestProcessingSucessful)
/* 128 */         initiateClosing(context.getMessageContext());
/*     */     }
/*     */   }
/*     */ 
/*     */   public NextAction processResponse(Packet response)
/*     */   {
/* 136 */     this.packet = response;
/* 137 */     MessageUpdatableContext context = getContext(this.packet);
/*     */     try {
/* 139 */       if ((isHandleFalse()) || (this.packet.getMessage() == null))
/*     */       {
/* 144 */         return doReturnWith(this.packet);
/*     */       }
/* 146 */       boolean isFault = isHandleFault(this.packet);
/* 147 */       if (!isHandlerChainEmpty())
/*     */       {
/* 149 */         callHandlersOnResponse(context, isFault);
/*     */       }
/*     */     } finally {
/* 152 */       initiateClosing(context.getMessageContext());
/*     */     }
/*     */ 
/* 155 */     context.updatePacket();
/*     */ 
/* 157 */     return doReturnWith(this.packet);
/*     */   }
/*     */ 
/*     */   public NextAction processException(Throwable t)
/*     */   {
/*     */     try
/*     */     {
/*     */       MessageUpdatableContext context;
/* 164 */       return doThrow(t);
/*     */     } finally {
/* 166 */       MessageUpdatableContext context = getContext(this.packet);
/* 167 */       initiateClosing(context.getMessageContext());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void initiateClosing(MessageContext mc)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void close(MessageContext msgContext)
/*     */   {
/* 205 */     if ((this.requestProcessingSucessful) && 
/* 206 */       (this.cousinTube != null)) {
/* 207 */       this.cousinTube.close(msgContext);
/*     */     }
/*     */ 
/* 211 */     if (this.processor != null) {
/* 212 */       closeHandlers(msgContext);
/*     */     }
/*     */ 
/* 215 */     this.exchange = null;
/* 216 */     this.requestProcessingSucessful = false;
/*     */   }
/*     */ 
/*     */   abstract void closeHandlers(MessageContext paramMessageContext);
/*     */ 
/*     */   protected void closeClientsideHandlers(MessageContext msgContext)
/*     */   {
/* 232 */     if (this.processor == null)
/* 233 */       return;
/* 234 */     if (this.remedyActionTaken)
/*     */     {
/* 238 */       this.processor.closeHandlers(msgContext, this.processor.getIndex(), 0);
/* 239 */       this.processor.setIndex(-1);
/*     */ 
/* 241 */       this.remedyActionTaken = false;
/*     */     }
/*     */     else
/*     */     {
/* 246 */       this.processor.closeHandlers(msgContext, this.handlers.size() - 1, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void closeServersideHandlers(MessageContext msgContext)
/*     */   {
/* 255 */     if (this.processor == null)
/* 256 */       return;
/* 257 */     if (this.remedyActionTaken)
/*     */     {
/* 261 */       this.processor.closeHandlers(msgContext, this.processor.getIndex(), this.handlers.size() - 1);
/* 262 */       this.processor.setIndex(-1);
/*     */ 
/* 264 */       this.remedyActionTaken = false;
/*     */     }
/*     */     else
/*     */     {
/* 269 */       this.processor.closeHandlers(msgContext, 0, this.handlers.size() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract void callHandlersOnResponse(MessageUpdatableContext paramMessageUpdatableContext, boolean paramBoolean);
/*     */ 
/*     */   abstract boolean callHandlersOnRequest(MessageUpdatableContext paramMessageUpdatableContext, boolean paramBoolean);
/*     */ 
/*     */   private boolean checkOneWay(Packet packet)
/*     */   {
/* 279 */     if (this.port != null)
/*     */     {
/* 281 */       return packet.getMessage().isOneWay(this.port);
/*     */     }
/*     */ 
/* 287 */     return (packet.expectReply == null) || (!packet.expectReply.booleanValue());
/*     */   }
/*     */ 
/*     */   abstract void setUpProcessor();
/*     */ 
/*     */   public final boolean isHandlerChainEmpty() {
/* 293 */     return this.handlers.isEmpty();
/*     */   }
/*     */   abstract MessageUpdatableContext getContext(Packet paramPacket);
/*     */ 
/*     */   private boolean isHandleFault(Packet packet) {
/* 298 */     if (this.cousinTube != null) {
/* 299 */       return this.exchange.isHandleFault();
/*     */     }
/* 301 */     boolean isFault = packet.getMessage().isFault();
/* 302 */     this.exchange.setHandleFault(isFault);
/* 303 */     return isFault;
/*     */   }
/*     */ 
/*     */   final void setHandleFault()
/*     */   {
/* 308 */     this.exchange.setHandleFault(true);
/*     */   }
/*     */ 
/*     */   private boolean isHandleFalse() {
/* 312 */     return this.exchange.isHandleFalse();
/*     */   }
/*     */ 
/*     */   final void setHandleFalse() {
/* 316 */     this.exchange.setHandleFalse();
/*     */   }
/*     */ 
/*     */   private void setupExchange() {
/* 320 */     if (this.exchange == null) {
/* 321 */       this.exchange = new HandlerTubeExchange();
/* 322 */       if (this.cousinTube != null) {
/* 323 */         this.cousinTube.exchange = this.exchange;
/*     */       }
/*     */     }
/* 326 */     else if (this.cousinTube != null) {
/* 327 */       this.cousinTube.exchange = this.exchange;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class HandlerTubeExchange
/*     */   {
/*     */     private boolean handleFalse;
/*     */     private boolean handleFault;
/*     */ 
/*     */     boolean isHandleFault()
/*     */     {
/* 343 */       return this.handleFault;
/*     */     }
/*     */ 
/*     */     void setHandleFault(boolean isFault) {
/* 347 */       this.handleFault = isFault;
/*     */     }
/*     */ 
/*     */     public boolean isHandleFalse() {
/* 351 */       return this.handleFalse;
/*     */     }
/*     */ 
/*     */     void setHandleFalse() {
/* 355 */       this.handleFalse = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.HandlerTube
 * JD-Core Version:    0.6.2
 */