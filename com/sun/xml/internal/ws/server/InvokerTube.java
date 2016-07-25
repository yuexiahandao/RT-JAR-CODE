/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
/*     */ import com.sun.xml.internal.ws.api.server.AsyncProviderCallback;
/*     */ import com.sun.xml.internal.ws.api.server.Invoker;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.api.server.WSWebServiceContext;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.xml.ws.WebServiceContext;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public abstract class InvokerTube<T> extends AbstractTubeImpl
/*     */ {
/*     */   private final Invoker invoker;
/*     */   private WSEndpoint endpoint;
/* 111 */   private static final ThreadLocal<Packet> packets = new ThreadLocal();
/*     */ 
/* 130 */   private final Invoker wrapper = new Invoker()
/*     */   {
/*     */     public Object invoke(Packet p, Method m, Object[] args) throws InvocationTargetException, IllegalAccessException {
/* 133 */       Packet old = set(p);
/*     */       try {
/* 135 */         return InvokerTube.this.invoker.invoke(p, m, args);
/*     */       } finally {
/* 137 */         set(old);
/*     */       }
/*     */     }
/*     */ 
/*     */     public <T> T invokeProvider(Packet p, T arg) throws IllegalAccessException, InvocationTargetException
/*     */     {
/* 143 */       Packet old = set(p);
/*     */       try {
/* 145 */         return InvokerTube.this.invoker.invokeProvider(p, arg);
/*     */       } finally {
/* 147 */         set(old);
/*     */       }
/*     */     }
/*     */ 
/*     */     public <T> void invokeAsyncProvider(Packet p, T arg, AsyncProviderCallback cbak, WebServiceContext ctxt) throws IllegalAccessException, InvocationTargetException
/*     */     {
/* 153 */       Packet old = set(p);
/*     */       try {
/* 155 */         InvokerTube.this.invoker.invokeAsyncProvider(p, arg, cbak, ctxt);
/*     */       } finally {
/* 157 */         set(old);
/*     */       }
/*     */     }
/*     */ 
/*     */     private Packet set(Packet p) {
/* 162 */       Packet old = (Packet)InvokerTube.packets.get();
/* 163 */       InvokerTube.packets.set(p);
/* 164 */       return old;
/*     */     }
/* 130 */   };
/*     */ 
/*     */   protected InvokerTube(Invoker invoker)
/*     */   {
/*  58 */     this.invoker = invoker;
/*     */   }
/*     */ 
/*     */   public void setEndpoint(WSEndpoint endpoint) {
/*  62 */     this.endpoint = endpoint;
/*  63 */     WSWebServiceContext webServiceContext = new AbstractWebServiceContext(endpoint) {
/*  65 */       @Nullable
/*     */       public Packet getRequestPacket() { Packet p = (Packet)InvokerTube.packets.get();
/*  66 */         return p;
/*     */       }
/*     */     };
/*  69 */     this.invoker.start(webServiceContext, endpoint);
/*     */   }
/*     */ 
/*     */   protected WSEndpoint getEndpoint() {
/*  73 */     return this.endpoint;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public final Invoker getInvoker(Packet request)
/*     */   {
/*  90 */     return this.wrapper;
/*     */   }
/*     */ 
/*     */   public final AbstractTubeImpl copy(TubeCloner cloner)
/*     */   {
/*  99 */     cloner.add(this, this);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   public void preDestroy() {
/* 104 */     this.invoker.dispose();
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static Packet getCurrentPacket()
/*     */   {
/* 121 */     Packet packet = (Packet)packets.get();
/* 122 */     if (packet == null)
/* 123 */       throw new WebServiceException(ServerMessages.NO_CURRENT_PACKET());
/* 124 */     return packet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.InvokerTube
 * JD-Core Version:    0.6.2
 */