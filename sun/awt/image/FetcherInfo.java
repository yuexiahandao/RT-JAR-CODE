/*     */ package sun.awt.image;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ class FetcherInfo
/*     */ {
/*     */   static final int MAX_NUM_FETCHERS_PER_APPCONTEXT = 4;
/*     */   Thread[] fetchers;
/*     */   int numFetchers;
/*     */   int numWaiting;
/*     */   Vector waitList;
/* 350 */   private static final Object FETCHER_INFO_KEY = new StringBuffer("FetcherInfo");
/*     */ 
/*     */   private FetcherInfo()
/*     */   {
/* 343 */     this.fetchers = new Thread[4];
/* 344 */     this.numFetchers = 0;
/* 345 */     this.numWaiting = 0;
/* 346 */     this.waitList = new Vector();
/*     */   }
/*     */ 
/*     */   static FetcherInfo getFetcherInfo()
/*     */   {
/* 354 */     AppContext localAppContext = AppContext.getAppContext();
/* 355 */     synchronized (localAppContext) {
/* 356 */       FetcherInfo localFetcherInfo = (FetcherInfo)localAppContext.get(FETCHER_INFO_KEY);
/* 357 */       if (localFetcherInfo == null) {
/* 358 */         localFetcherInfo = new FetcherInfo();
/* 359 */         localAppContext.put(FETCHER_INFO_KEY, localFetcherInfo);
/*     */       }
/* 361 */       return localFetcherInfo;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.FetcherInfo
 * JD-Core Version:    0.6.2
 */