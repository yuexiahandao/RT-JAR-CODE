/*     */ package com.sun.imageio.stream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ 
/*     */ public class StreamCloser
/*     */ {
/*     */   private static WeakHashMap<CloseAction, Object> toCloseQueue;
/*     */   private static Thread streamCloser;
/*     */ 
/*     */   public static void addToQueue(CloseAction paramCloseAction)
/*     */   {
/*  50 */     synchronized (StreamCloser.class) {
/*  51 */       if (toCloseQueue == null) {
/*  52 */         toCloseQueue = new WeakHashMap();
/*     */       }
/*     */ 
/*  56 */       toCloseQueue.put(paramCloseAction, null);
/*     */ 
/*  58 */       if (streamCloser == null) {
/*  59 */         Runnable local1 = new Runnable() {
/*     */           public void run() {
/*  61 */             if (StreamCloser.toCloseQueue != null)
/*  62 */               synchronized (StreamCloser.class) {
/*  63 */                 Set localSet = StreamCloser.toCloseQueue.keySet();
/*     */ 
/*  68 */                 StreamCloser.CloseAction[] arrayOfCloseAction1 = new StreamCloser.CloseAction[localSet.size()];
/*     */ 
/*  70 */                 arrayOfCloseAction1 = (StreamCloser.CloseAction[])localSet.toArray(arrayOfCloseAction1);
/*  71 */                 for (StreamCloser.CloseAction localCloseAction : arrayOfCloseAction1)
/*  72 */                   if (localCloseAction != null)
/*     */                     try {
/*  74 */                       localCloseAction.performAction();
/*     */                     }
/*     */                     catch (IOException localIOException)
/*     */                     {
/*     */                     }
/*     */               }
/*     */           }
/*     */         };
/*  84 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run()
/*     */           {
/*  91 */             Object localObject1 = Thread.currentThread().getThreadGroup();
/*     */ 
/*  93 */             for (Object localObject2 = localObject1; 
/*  94 */               localObject2 != null; 
/*  95 */               localObject2 = ((ThreadGroup)localObject1).getParent()) localObject1 = localObject2;
/*  96 */             StreamCloser.access$102(new Thread((ThreadGroup)localObject1, this.val$streamCloserRunnable));
/*     */ 
/* 100 */             StreamCloser.streamCloser.setContextClassLoader(null);
/* 101 */             Runtime.getRuntime().addShutdownHook(StreamCloser.streamCloser);
/* 102 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void removeFromQueue(CloseAction paramCloseAction) {
/* 110 */     synchronized (StreamCloser.class) {
/* 111 */       if (toCloseQueue != null)
/* 112 */         toCloseQueue.remove(paramCloseAction);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static CloseAction createCloseAction(ImageInputStream paramImageInputStream)
/*     */   {
/* 118 */     return new CloseAction(paramImageInputStream, null);
/*     */   }
/*     */ 
/*     */   public static final class CloseAction {
/*     */     private ImageInputStream iis;
/*     */ 
/*     */     private CloseAction(ImageInputStream paramImageInputStream) {
/* 125 */       this.iis = paramImageInputStream;
/*     */     }
/*     */ 
/*     */     public void performAction() throws IOException {
/* 129 */       if (this.iis != null)
/* 130 */         this.iis.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.stream.StreamCloser
 * JD-Core Version:    0.6.2
 */