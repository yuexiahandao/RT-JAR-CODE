/*     */ package com.sun.xml.internal.ws.server.provider;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Fiber;
/*     */ import com.sun.xml.internal.ws.api.pipe.NextAction;
/*     */ import com.sun.xml.internal.ws.api.server.AsyncProviderCallback;
/*     */ import com.sun.xml.internal.ws.api.server.Invoker;
/*     */ import com.sun.xml.internal.ws.api.server.TransportBackChannel;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.server.AbstractWebServiceContext;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ class AsyncProviderInvokerTube<T> extends ProviderInvokerTube<T>
/*     */ {
/*  50 */   private static final Logger LOGGER = Logger.getLogger("com.sun.xml.internal.ws.server.AsyncProviderInvokerTube");
/*     */ 
/*     */   public AsyncProviderInvokerTube(Invoker invoker, ProviderArgumentsBuilder<T> argsBuilder)
/*     */   {
/*  54 */     super(invoker, argsBuilder);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public NextAction processRequest(@NotNull Packet request)
/*     */   {
/*  64 */     Object param = this.argsBuilder.getParameter(request);
/*  65 */     AsyncProviderCallback callback = new AsyncProviderCallbackImpl(request);
/*  66 */     AsyncWebServiceContext ctxt = new AsyncWebServiceContext(getEndpoint(), request);
/*     */ 
/*  68 */     LOGGER.fine("Invoking AsyncProvider Endpoint");
/*     */     try {
/*  70 */       getInvoker(request).invokeAsyncProvider(request, param, callback, ctxt);
/*     */     } catch (Exception e) {
/*  72 */       LOGGER.log(Level.SEVERE, e.getMessage(), e);
/*  73 */       return doThrow(e);
/*     */     }
/*     */ 
/*  77 */     return doSuspend();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public NextAction processResponse(@NotNull Packet response)
/*     */   {
/* 128 */     return doReturnWith(response);
/*     */   }
/*     */   @NotNull
/*     */   public NextAction processException(@NotNull Throwable t) {
/* 132 */     throw new IllegalStateException("AsyncProviderInvokerTube's processException shouldn't be called.");
/*     */   }
/*     */ 
/*     */   private class AsyncProviderCallbackImpl
/*     */     implements AsyncProviderCallback<T>
/*     */   {
/*     */     private final Packet request;
/*     */     private final Fiber fiber;
/*     */ 
/*     */     public AsyncProviderCallbackImpl(Packet request)
/*     */     {
/*  85 */       this.request = request;
/*  86 */       this.fiber = Fiber.current();
/*     */     }
/*     */ 
/*     */     public void send(@Nullable T param) {
/*  90 */       if ((param == null) && 
/*  91 */         (this.request.transportBackChannel != null)) {
/*  92 */         this.request.transportBackChannel.close();
/*     */       }
/*     */ 
/*  95 */       Packet packet = AsyncProviderInvokerTube.this.argsBuilder.getResponse(this.request, param, AsyncProviderInvokerTube.this.getEndpoint().getPort(), AsyncProviderInvokerTube.this.getEndpoint().getBinding());
/*  96 */       this.fiber.resume(packet);
/*     */     }
/*     */ 
/*     */     public void sendError(@NotNull Throwable t)
/*     */     {
/*     */       Exception e;
/*     */       Exception e;
/* 101 */       if ((t instanceof RuntimeException))
/* 102 */         e = (RuntimeException)t;
/*     */       else {
/* 104 */         e = new RuntimeException(t);
/*     */       }
/* 106 */       Packet packet = AsyncProviderInvokerTube.this.argsBuilder.getResponse(this.request, e, AsyncProviderInvokerTube.this.getEndpoint().getPort(), AsyncProviderInvokerTube.this.getEndpoint().getBinding());
/* 107 */       this.fiber.resume(packet);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class AsyncWebServiceContext extends AbstractWebServiceContext
/*     */   {
/*     */     final Packet packet;
/*     */ 
/*     */     AsyncWebServiceContext(WSEndpoint endpoint, Packet packet)
/*     */     {
/* 118 */       super();
/* 119 */       this.packet = packet;
/*     */     }
/*     */     @NotNull
/*     */     public Packet getRequestPacket() {
/* 123 */       return this.packet;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.provider.AsyncProviderInvokerTube
 * JD-Core Version:    0.6.2
 */