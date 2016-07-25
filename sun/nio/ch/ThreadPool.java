/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import sun.misc.InnocuousThread;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class ThreadPool
/*     */ {
/*     */   private static final String DEFAULT_THREAD_POOL_THREAD_FACTORY = "java.nio.channels.DefaultThreadPool.threadFactory";
/*     */   private static final String DEFAULT_THREAD_POOL_INITIAL_SIZE = "java.nio.channels.DefaultThreadPool.initialSize";
/*     */   private final ExecutorService executor;
/*     */   private final boolean isFixed;
/*     */   private final int poolSize;
/*     */ 
/*     */   private ThreadPool(ExecutorService paramExecutorService, boolean paramBoolean, int paramInt)
/*     */   {
/*  57 */     this.executor = paramExecutorService;
/*  58 */     this.isFixed = paramBoolean;
/*  59 */     this.poolSize = paramInt;
/*     */   }
/*     */ 
/*     */   ExecutorService executor() {
/*  63 */     return this.executor;
/*     */   }
/*     */ 
/*     */   boolean isFixedThreadPool() {
/*  67 */     return this.isFixed;
/*     */   }
/*     */ 
/*     */   int poolSize() {
/*  71 */     return this.poolSize;
/*     */   }
/*     */ 
/*     */   static ThreadFactory defaultThreadFactory() {
/*  75 */     if (System.getSecurityManager() == null) {
/*  76 */       return new ThreadFactory()
/*     */       {
/*     */         public Thread newThread(Runnable paramAnonymousRunnable) {
/*  79 */           Thread localThread = new Thread(paramAnonymousRunnable);
/*  80 */           localThread.setDaemon(true);
/*  81 */           return localThread;
/*     */         }
/*     */       };
/*     */     }
/*  85 */     return new ThreadFactory()
/*     */     {
/*     */       public Thread newThread(final Runnable paramAnonymousRunnable) {
/*  88 */         return (Thread)AccessController.doPrivileged(new PrivilegedAction() {
/*     */           public Object run() {
/*  90 */             InnocuousThread localInnocuousThread = new InnocuousThread(paramAnonymousRunnable);
/*  91 */             localInnocuousThread.setDaemon(true);
/*  92 */             return localInnocuousThread;
/*     */           }
/*     */         });
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   static ThreadPool getDefault()
/*     */   {
/* 106 */     return DefaultThreadPoolHolder.defaultThreadPool;
/*     */   }
/*     */ 
/*     */   static ThreadPool createDefault()
/*     */   {
/* 112 */     int i = getDefaultThreadPoolInitialSize();
/* 113 */     if (i < 0) {
/* 114 */       i = Runtime.getRuntime().availableProcessors();
/*     */     }
/* 116 */     ThreadFactory localThreadFactory = getDefaultThreadPoolThreadFactory();
/* 117 */     if (localThreadFactory == null) {
/* 118 */       localThreadFactory = defaultThreadFactory();
/*     */     }
/* 120 */     ThreadPoolExecutor localThreadPoolExecutor = new ThreadPoolExecutor(0, 2147483647, 9223372036854775807L, TimeUnit.MILLISECONDS, new SynchronousQueue(), localThreadFactory);
/*     */ 
/* 125 */     return new ThreadPool(localThreadPoolExecutor, false, i);
/*     */   }
/*     */ 
/*     */   static ThreadPool create(int paramInt, ThreadFactory paramThreadFactory)
/*     */   {
/* 130 */     if (paramInt <= 0)
/* 131 */       throw new IllegalArgumentException("'nThreads' must be > 0");
/* 132 */     ExecutorService localExecutorService = Executors.newFixedThreadPool(paramInt, paramThreadFactory);
/* 133 */     return new ThreadPool(localExecutorService, true, paramInt);
/*     */   }
/*     */ 
/*     */   public static ThreadPool wrap(ExecutorService paramExecutorService, int paramInt)
/*     */   {
/* 138 */     if (paramExecutorService == null) {
/* 139 */       throw new NullPointerException("'executor' is null");
/*     */     }
/* 141 */     if ((paramExecutorService instanceof ThreadPoolExecutor)) {
/* 142 */       int i = ((ThreadPoolExecutor)paramExecutorService).getMaximumPoolSize();
/* 143 */       if (i == 2147483647) {
/* 144 */         if (paramInt < 0) {
/* 145 */           paramInt = Runtime.getRuntime().availableProcessors();
/*     */         }
/*     */         else {
/* 148 */           paramInt = 0;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/* 153 */     else if (paramInt < 0) {
/* 154 */       paramInt = 0;
/*     */     }
/* 156 */     return new ThreadPool(paramExecutorService, false, paramInt);
/*     */   }
/*     */ 
/*     */   private static int getDefaultThreadPoolInitialSize() {
/* 160 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.nio.channels.DefaultThreadPool.initialSize"));
/*     */ 
/* 162 */     if (str != null) {
/*     */       try {
/* 164 */         return Integer.parseInt(str);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/* 166 */         throw new Error("Value of property 'java.nio.channels.DefaultThreadPool.initialSize' is invalid: " + localNumberFormatException);
/*     */       }
/*     */     }
/*     */ 
/* 170 */     return -1;
/*     */   }
/*     */ 
/*     */   private static ThreadFactory getDefaultThreadPoolThreadFactory() {
/* 174 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("java.nio.channels.DefaultThreadPool.threadFactory"));
/*     */ 
/* 176 */     if (str != null) {
/*     */       try {
/* 178 */         Class localClass = Class.forName(str, true, ClassLoader.getSystemClassLoader());
/*     */ 
/* 180 */         return (ThreadFactory)localClass.newInstance();
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 182 */         throw new Error(localClassNotFoundException);
/*     */       } catch (InstantiationException localInstantiationException) {
/* 184 */         throw new Error(localInstantiationException);
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 186 */         throw new Error(localIllegalAccessException);
/*     */       }
/*     */     }
/* 189 */     return null;
/*     */   }
/*     */ 
/*     */   private static class DefaultThreadPoolHolder
/*     */   {
/* 101 */     static final ThreadPool defaultThreadPool = ThreadPool.createDefault();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.ThreadPool
 * JD-Core Version:    0.6.2
 */