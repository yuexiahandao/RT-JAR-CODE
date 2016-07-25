/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.util.CompletedFuture;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.FutureTask;
/*     */ import javax.xml.ws.AsyncHandler;
/*     */ import javax.xml.ws.Response;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class AsyncResponseImpl<T> extends FutureTask<T>
/*     */   implements Response<T>, ResponseContextReceiver
/*     */ {
/*     */   private final AsyncHandler<T> handler;
/*     */   private ResponseContext responseContext;
/*     */   private final Runnable callable;
/*     */ 
/*     */   public AsyncResponseImpl(Runnable runnable, @Nullable AsyncHandler<T> handler)
/*     */   {
/*  63 */     super(runnable, null);
/*  64 */     this.callable = runnable;
/*  65 */     this.handler = handler;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  73 */       this.callable.run();
/*     */     }
/*     */     catch (WebServiceException e)
/*     */     {
/*  77 */       set(null, e);
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*  81 */       set(null, new WebServiceException(e));
/*     */     }
/*     */   }
/*     */ 
/*     */   public ResponseContext getContext()
/*     */   {
/*  87 */     return this.responseContext;
/*     */   }
/*     */ 
/*     */   public void setResponseContext(ResponseContext rc) {
/*  91 */     this.responseContext = rc;
/*     */   }
/*     */ 
/*     */   public void set(T v, Throwable t)
/*     */   {
/*  96 */     if (this.handler != null)
/*     */     {
/*     */       try
/*     */       {
/* 114 */         this.handler.handleResponse(new CompletedFuture(v, t)
/*     */         {
/*     */           public Map<String, Object> getContext()
/*     */           {
/* 111 */             return AsyncResponseImpl.this.getContext();
/*     */           }
/*     */         });
/*     */       }
/*     */       catch (Throwable e) {
/* 116 */         super.setException(e);
/* 117 */         return;
/*     */       }
/*     */     }
/* 120 */     if (t != null)
/* 121 */       super.setException(t);
/*     */     else
/* 123 */       super.set(v);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.AsyncResponseImpl
 * JD-Core Version:    0.6.2
 */