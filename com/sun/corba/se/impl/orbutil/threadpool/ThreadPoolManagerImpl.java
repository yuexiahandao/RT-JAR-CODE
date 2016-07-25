/*     */ package com.sun.corba.se.impl.orbutil.threadpool;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.NoSuchThreadPoolException;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolChooser;
/*     */ import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ public class ThreadPoolManagerImpl
/*     */   implements ThreadPoolManager
/*     */ {
/*     */   private ThreadPool threadPool;
/*     */   private ThreadGroup threadGroup;
/*  55 */   private static final ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.transport");
/*     */ 
/*  64 */   private static AtomicInteger tgCount = new AtomicInteger();
/*     */ 
/*     */   public ThreadPoolManagerImpl()
/*     */   {
/*  59 */     this.threadGroup = getThreadGroup();
/*  60 */     this.threadPool = new ThreadPoolImpl(this.threadGroup, "default-threadpool");
/*     */   }
/*     */ 
/*     */   private ThreadGroup getThreadGroup()
/*     */   {
/*     */     ThreadGroup localThreadGroup;
/*     */     try
/*     */     {
/*  88 */       localThreadGroup = (ThreadGroup)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public ThreadGroup run() {
/*  91 */           Object localObject1 = Thread.currentThread().getThreadGroup();
/*  92 */           Object localObject2 = localObject1;
/*     */           try {
/*  94 */             while (localObject2 != null) {
/*  95 */               localObject1 = localObject2;
/*  96 */               localObject2 = ((ThreadGroup)localObject1).getParent();
/*     */             }
/*     */           }
/*     */           catch (SecurityException localSecurityException) {
/*     */           }
/* 101 */           return new ThreadGroup((ThreadGroup)localObject1, "ORB ThreadGroup " + ThreadPoolManagerImpl.tgCount.getAndIncrement());
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/* 107 */       localThreadGroup = Thread.currentThread().getThreadGroup();
/*     */     }
/*     */ 
/* 110 */     return localThreadGroup;
/*     */   }
/*     */ 
/*     */   public void close() {
/*     */     try {
/* 115 */       this.threadPool.close();
/*     */     } catch (IOException localIOException) {
/* 117 */       wrapper.threadPoolCloseError();
/*     */     }
/*     */     try
/*     */     {
/* 121 */       boolean bool = this.threadGroup.isDestroyed();
/* 122 */       int i = this.threadGroup.activeCount();
/* 123 */       int j = this.threadGroup.activeGroupCount();
/*     */ 
/* 125 */       if (bool) {
/* 126 */         wrapper.threadGroupIsDestroyed(this.threadGroup);
/*     */       } else {
/* 128 */         if (i > 0) {
/* 129 */           wrapper.threadGroupHasActiveThreadsInClose(this.threadGroup, Integer.valueOf(i));
/*     */         }
/* 131 */         if (j > 0) {
/* 132 */           wrapper.threadGroupHasSubGroupsInClose(this.threadGroup, Integer.valueOf(j));
/*     */         }
/* 134 */         this.threadGroup.destroy();
/*     */       }
/*     */     } catch (IllegalThreadStateException localIllegalThreadStateException) {
/* 137 */       wrapper.threadGroupDestroyFailed(localIllegalThreadStateException, this.threadGroup);
/*     */     }
/*     */ 
/* 140 */     this.threadGroup = null;
/*     */   }
/*     */ 
/*     */   public ThreadPool getThreadPool(String paramString)
/*     */     throws NoSuchThreadPoolException
/*     */   {
/* 153 */     return this.threadPool;
/*     */   }
/*     */ 
/*     */   public ThreadPool getThreadPool(int paramInt)
/*     */     throws NoSuchThreadPoolException
/*     */   {
/* 167 */     return this.threadPool;
/*     */   }
/*     */ 
/*     */   public int getThreadPoolNumericId(String paramString)
/*     */   {
/* 177 */     return 0;
/*     */   }
/*     */ 
/*     */   public String getThreadPoolStringId(int paramInt)
/*     */   {
/* 185 */     return "";
/*     */   }
/*     */ 
/*     */   public ThreadPool getDefaultThreadPool()
/*     */   {
/* 192 */     return this.threadPool;
/*     */   }
/*     */ 
/*     */   public ThreadPoolChooser getThreadPoolChooser(String paramString)
/*     */   {
/* 202 */     return null;
/*     */   }
/*     */ 
/*     */   public ThreadPoolChooser getThreadPoolChooser(int paramInt)
/*     */   {
/* 212 */     return null;
/*     */   }
/*     */ 
/*     */   public void setThreadPoolChooser(String paramString, ThreadPoolChooser paramThreadPoolChooser)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getThreadPoolChooserNumericId(String paramString)
/*     */   {
/* 232 */     return 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.threadpool.ThreadPoolManagerImpl
 * JD-Core Version:    0.6.2
 */