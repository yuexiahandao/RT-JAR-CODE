/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Vector;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ class ImageFetcher extends Thread
/*     */ {
/*     */   static final int HIGH_PRIORITY = 8;
/*     */   static final int LOW_PRIORITY = 3;
/*     */   static final int ANIM_PRIORITY = 2;
/*     */   static final int TIMEOUT = 5000;
/*     */ 
/*     */   private ImageFetcher(ThreadGroup paramThreadGroup, int paramInt)
/*     */   {
/*  57 */     super(paramThreadGroup, "Image Fetcher " + paramInt);
/*  58 */     setDaemon(true);
/*     */   }
/*     */ 
/*     */   public static boolean add(ImageFetchable paramImageFetchable)
/*     */   {
/*  68 */     FetcherInfo localFetcherInfo = FetcherInfo.getFetcherInfo();
/*  69 */     synchronized (localFetcherInfo.waitList) {
/*  70 */       if (!localFetcherInfo.waitList.contains(paramImageFetchable)) {
/*  71 */         localFetcherInfo.waitList.addElement(paramImageFetchable);
/*  72 */         if ((localFetcherInfo.numWaiting == 0) && (localFetcherInfo.numFetchers < localFetcherInfo.fetchers.length))
/*     */         {
/*  74 */           createFetchers(localFetcherInfo);
/*     */         }
/*     */ 
/*  84 */         if (localFetcherInfo.numFetchers > 0) {
/*  85 */           localFetcherInfo.waitList.notify();
/*     */         } else {
/*  87 */           localFetcherInfo.waitList.removeElement(paramImageFetchable);
/*  88 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   public static void remove(ImageFetchable paramImageFetchable)
/*     */   {
/*  99 */     FetcherInfo localFetcherInfo = FetcherInfo.getFetcherInfo();
/* 100 */     synchronized (localFetcherInfo.waitList) {
/* 101 */       if (localFetcherInfo.waitList.contains(paramImageFetchable))
/* 102 */         localFetcherInfo.waitList.removeElement(paramImageFetchable);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean isFetcher(Thread paramThread)
/*     */   {
/* 111 */     FetcherInfo localFetcherInfo = FetcherInfo.getFetcherInfo();
/* 112 */     synchronized (localFetcherInfo.waitList) {
/* 113 */       for (int i = 0; i < localFetcherInfo.fetchers.length; i++) {
/* 114 */         if (localFetcherInfo.fetchers[i] == paramThread) {
/* 115 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean amFetcher()
/*     */   {
/* 126 */     return isFetcher(Thread.currentThread());
/*     */   }
/*     */ 
/*     */   private static ImageFetchable nextImage()
/*     */   {
/* 135 */     FetcherInfo localFetcherInfo = FetcherInfo.getFetcherInfo();
/* 136 */     synchronized (localFetcherInfo.waitList) {
/* 137 */       ImageFetchable localImageFetchable1 = null;
/* 138 */       long l1 = System.currentTimeMillis() + 5000L;
/* 139 */       while (localImageFetchable1 == null) {
/* 140 */         while (localFetcherInfo.waitList.size() == 0) {
/* 141 */           long l2 = System.currentTimeMillis();
/* 142 */           if (l2 >= l1)
/* 143 */             return null;
/*     */           try
/*     */           {
/* 146 */             localFetcherInfo.numWaiting += 1;
/* 147 */             localFetcherInfo.waitList.wait(l1 - l2);
/*     */           }
/*     */           catch (InterruptedException localInterruptedException) {
/* 150 */             ImageFetchable localImageFetchable2 = null;
/*     */ 
/* 152 */             localFetcherInfo.numWaiting -= 1; return localImageFetchable2; } finally { localFetcherInfo.numWaiting -= 1; }
/*     */ 
/*     */         }
/* 155 */         localImageFetchable1 = (ImageFetchable)localFetcherInfo.waitList.elementAt(0);
/* 156 */         localFetcherInfo.waitList.removeElement(localImageFetchable1);
/*     */       }
/* 158 */       return localImageFetchable1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 167 */     FetcherInfo localFetcherInfo = FetcherInfo.getFetcherInfo();
/*     */     try {
/* 169 */       fetchloop();
/*     */ 
/* 173 */       synchronized (localFetcherInfo.waitList) {
/* 174 */         localThread1 = Thread.currentThread();
/* 175 */         for (i = 0; i < localFetcherInfo.fetchers.length; i++)
/* 176 */           if (localFetcherInfo.fetchers[i] == localThread1) {
/* 177 */             localFetcherInfo.fetchers[i] = null;
/* 178 */             localFetcherInfo.numFetchers -= 1;
/*     */           }
/*     */       }
/*     */     }
/*     */     catch (Exception )
/*     */     {
/*     */       Thread localThread1;
/*     */       int i;
/* 171 */       ???.printStackTrace();
/*     */ 
/* 173 */       synchronized (localFetcherInfo.waitList) {
/* 174 */         localThread1 = Thread.currentThread();
/* 175 */         for (i = 0; i < localFetcherInfo.fetchers.length; i++)
/* 176 */           if (localFetcherInfo.fetchers[i] == localThread1) {
/* 177 */             localFetcherInfo.fetchers[i] = null;
/* 178 */             localFetcherInfo.numFetchers -= 1;
/*     */           }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 173 */       synchronized (localFetcherInfo.waitList) {
/* 174 */         Thread localThread2 = Thread.currentThread();
/* 175 */         for (int j = 0; j < localFetcherInfo.fetchers.length; j++)
/* 176 */           if (localFetcherInfo.fetchers[j] == localThread2) {
/* 177 */             localFetcherInfo.fetchers[j] = null;
/* 178 */             localFetcherInfo.numFetchers -= 1;
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void fetchloop()
/*     */   {
/* 191 */     Thread localThread = Thread.currentThread();
/* 192 */     while (isFetcher(localThread))
/*     */     {
/* 198 */       Thread.interrupted();
/* 199 */       localThread.setPriority(8);
/* 200 */       ImageFetchable localImageFetchable = nextImage();
/* 201 */       if (localImageFetchable == null)
/* 202 */         return;
/*     */       try
/*     */       {
/* 205 */         localImageFetchable.doFetch();
/*     */       } catch (Exception localException) {
/* 207 */         System.err.println("Uncaught error fetching image:");
/* 208 */         localException.printStackTrace();
/*     */       }
/* 210 */       stoppingAnimation(localThread);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void startingAnimation()
/*     */   {
/* 221 */     FetcherInfo localFetcherInfo = FetcherInfo.getFetcherInfo();
/* 222 */     Thread localThread = Thread.currentThread();
/* 223 */     synchronized (localFetcherInfo.waitList) {
/* 224 */       for (int i = 0; i < localFetcherInfo.fetchers.length; i++) {
/* 225 */         if (localFetcherInfo.fetchers[i] == localThread) {
/* 226 */           localFetcherInfo.fetchers[i] = null;
/* 227 */           localFetcherInfo.numFetchers -= 1;
/* 228 */           localThread.setName("Image Animator " + i);
/* 229 */           if (localFetcherInfo.waitList.size() > localFetcherInfo.numWaiting) {
/* 230 */             createFetchers(localFetcherInfo);
/*     */           }
/* 232 */           return;
/*     */         }
/*     */       }
/*     */     }
/* 236 */     localThread.setPriority(2);
/* 237 */     localThread.setName("Image Animator");
/*     */   }
/*     */ 
/*     */   private static void stoppingAnimation(Thread paramThread)
/*     */   {
/* 249 */     FetcherInfo localFetcherInfo = FetcherInfo.getFetcherInfo();
/* 250 */     synchronized (localFetcherInfo.waitList) {
/* 251 */       int i = -1;
/* 252 */       for (int j = 0; j < localFetcherInfo.fetchers.length; j++) {
/* 253 */         if (localFetcherInfo.fetchers[j] == paramThread) {
/* 254 */           return;
/*     */         }
/* 256 */         if (localFetcherInfo.fetchers[j] == null) {
/* 257 */           i = j;
/*     */         }
/*     */       }
/* 260 */       if (i >= 0) {
/* 261 */         localFetcherInfo.fetchers[i] = paramThread;
/* 262 */         localFetcherInfo.numFetchers += 1;
/* 263 */         paramThread.setName("Image Fetcher " + i);
/* 264 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void createFetchers(FetcherInfo paramFetcherInfo)
/*     */   {
/* 276 */     AppContext localAppContext = AppContext.getAppContext();
/* 277 */     Object localObject1 = localAppContext.getThreadGroup();
/*     */     Object localObject2;
/*     */     try
/*     */     {
/* 280 */       if (((ThreadGroup)localObject1).getParent() != null)
/*     */       {
/* 282 */         localObject2 = localObject1;
/*     */       }
/*     */       else
/*     */       {
/* 289 */         localObject1 = Thread.currentThread().getThreadGroup();
/* 290 */         ThreadGroup localThreadGroup = ((ThreadGroup)localObject1).getParent();
/*     */ 
/* 292 */         while ((localThreadGroup != null) && (localThreadGroup.getParent() != null)) {
/* 293 */           localObject1 = localThreadGroup;
/* 294 */           localThreadGroup = ((ThreadGroup)localObject1).getParent();
/*     */         }
/* 296 */         localObject2 = localObject1;
/*     */       }
/*     */     }
/*     */     catch (SecurityException localSecurityException)
/*     */     {
/* 301 */       localObject2 = localAppContext.getThreadGroup();
/*     */     }
/* 303 */     final Object localObject3 = localObject2;
/*     */ 
/* 305 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 308 */         for (int i = 0; i < this.val$info.fetchers.length; i++)
/* 309 */           if (this.val$info.fetchers[i] == null) {
/* 310 */             ImageFetcher localImageFetcher = new ImageFetcher(localObject3, i, null);
/*     */             try
/*     */             {
/* 313 */               localImageFetcher.start();
/* 314 */               this.val$info.fetchers[i] = localImageFetcher;
/* 315 */               this.val$info.numFetchers += 1;
/*     */             }
/*     */             catch (Error localError)
/*     */             {
/*     */             }
/*     */           }
/* 321 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ImageFetcher
 * JD-Core Version:    0.6.2
 */