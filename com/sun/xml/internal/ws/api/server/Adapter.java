/*     */ package com.sun.xml.internal.ws.api.server;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.config.management.Reconfigurable;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.util.Pool;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class Adapter<TK extends Toolkit>
/*     */   implements Reconfigurable, EndpointComponent
/*     */ {
/*     */   protected final WSEndpoint<?> endpoint;
/* 104 */   protected volatile Pool<TK> pool = new Pool() {
/*     */     protected TK create() {
/* 106 */       return Adapter.this.createToolkit();
/*     */     }
/* 104 */   };
/*     */ 
/*     */   protected Adapter(WSEndpoint endpoint)
/*     */   {
/* 115 */     assert (endpoint != null);
/* 116 */     this.endpoint = endpoint;
/*     */ 
/* 118 */     endpoint.getComponentRegistry().add(this);
/*     */   }
/*     */ 
/*     */   public void reconfigure()
/*     */   {
/* 125 */     this.pool = new Pool() {
/*     */       protected TK create() {
/* 127 */         return Adapter.this.createToolkit();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public <T> T getSPI(Class<T> spiType) {
/* 133 */     if (spiType.isAssignableFrom(Reconfigurable.class)) {
/* 134 */       return spiType.cast(this);
/*     */     }
/*     */ 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   public WSEndpoint<?> getEndpoint()
/*     */   {
/* 148 */     return this.endpoint;
/*     */   }
/*     */ 
/*     */   protected Pool<TK> getPool()
/*     */   {
/* 162 */     return this.pool;
/*     */   }
/*     */ 
/*     */   protected abstract TK createToolkit();
/*     */ 
/*     */   public class Toolkit
/*     */   {
/*     */     public final Codec codec;
/*     */     public final WSEndpoint.PipeHead head;
/*     */ 
/*     */     public Toolkit()
/*     */     {
/*  92 */       this.codec = Adapter.this.endpoint.createCodec();
/*  93 */       this.head = Adapter.this.endpoint.createPipeHead();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.Adapter
 * JD-Core Version:    0.6.2
 */