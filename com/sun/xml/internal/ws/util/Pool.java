/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ 
/*     */ public abstract class Pool<T>
/*     */ {
/*     */   private volatile WeakReference<ConcurrentLinkedQueue<T>> queue;
/*     */ 
/*     */   public final T take()
/*     */   {
/*  65 */     Object t = getQueue().poll();
/*  66 */     if (t == null)
/*  67 */       return create();
/*  68 */     return t;
/*     */   }
/*     */ 
/*     */   private ConcurrentLinkedQueue<T> getQueue() {
/*  72 */     WeakReference q = this.queue;
/*  73 */     if (q != null) {
/*  74 */       ConcurrentLinkedQueue d = (ConcurrentLinkedQueue)q.get();
/*  75 */       if (d != null) {
/*  76 */         return d;
/*     */       }
/*     */     }
/*     */ 
/*  80 */     ConcurrentLinkedQueue d = new ConcurrentLinkedQueue();
/*  81 */     this.queue = new WeakReference(d);
/*     */ 
/*  83 */     return d;
/*     */   }
/*     */ 
/*     */   public final void recycle(T t)
/*     */   {
/*  90 */     getQueue().offer(t);
/*     */   }
/*     */ 
/*     */   protected abstract T create();
/*     */ 
/*     */   public static final class Marshaller extends Pool<Marshaller>
/*     */   {
/*     */     private final JAXBContext context;
/*     */ 
/*     */     public Marshaller(JAXBContext context)
/*     */     {
/* 114 */       this.context = context;
/*     */     }
/*     */ 
/*     */     protected Marshaller create() {
/*     */       try {
/* 119 */         return this.context.createMarshaller();
/*     */       }
/*     */       catch (JAXBException e) {
/* 122 */         throw new AssertionError(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class TubePool extends Pool<Tube>
/*     */   {
/*     */     private final Tube master;
/*     */ 
/*     */     public TubePool(Tube master)
/*     */     {
/* 154 */       this.master = master;
/* 155 */       recycle(master);
/*     */     }
/*     */ 
/*     */     protected Tube create() {
/* 159 */       return TubeCloner.clone(this.master);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class Unmarshaller extends Pool<Unmarshaller>
/*     */   {
/*     */     private final JAXBContext context;
/*     */ 
/*     */     public Unmarshaller(JAXBContext context)
/*     */     {
/* 134 */       this.context = context;
/*     */     }
/*     */ 
/*     */     protected Unmarshaller create() {
/*     */       try {
/* 139 */         return this.context.createUnmarshaller();
/*     */       }
/*     */       catch (JAXBException e) {
/* 142 */         throw new AssertionError(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.Pool
 * JD-Core Version:    0.6.2
 */