/*     */ package java.net;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ class InMemoryCookieStore
/*     */   implements CookieStore
/*     */ {
/*  48 */   private List<HttpCookie> cookieJar = null;
/*     */ 
/*  54 */   private Map<String, List<HttpCookie>> domainIndex = null;
/*  55 */   private Map<URI, List<HttpCookie>> uriIndex = null;
/*     */ 
/*  58 */   private ReentrantLock lock = null;
/*     */ 
/*     */   public InMemoryCookieStore()
/*     */   {
/*  65 */     this.cookieJar = new ArrayList();
/*  66 */     this.domainIndex = new HashMap();
/*  67 */     this.uriIndex = new HashMap();
/*     */ 
/*  69 */     this.lock = new ReentrantLock(false);
/*     */   }
/*     */ 
/*     */   public void add(URI paramURI, HttpCookie paramHttpCookie)
/*     */   {
/*  77 */     if (paramHttpCookie == null) {
/*  78 */       throw new NullPointerException("cookie is null");
/*     */     }
/*     */ 
/*  82 */     this.lock.lock();
/*     */     try
/*     */     {
/*  85 */       this.cookieJar.remove(paramHttpCookie);
/*     */ 
/*  88 */       if (paramHttpCookie.getMaxAge() != 0L) {
/*  89 */         this.cookieJar.add(paramHttpCookie);
/*     */ 
/*  91 */         if (paramHttpCookie.getDomain() != null) {
/*  92 */           addIndex(this.domainIndex, paramHttpCookie.getDomain(), paramHttpCookie);
/*     */         }
/*  94 */         if (paramURI != null)
/*     */         {
/*  96 */           addIndex(this.uriIndex, getEffectiveURI(paramURI), paramHttpCookie);
/*     */         }
/*     */       }
/*     */     } finally {
/* 100 */       this.lock.unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<HttpCookie> get(URI paramURI)
/*     */   {
/* 114 */     if (paramURI == null) {
/* 115 */       throw new NullPointerException("uri is null");
/*     */     }
/*     */ 
/* 118 */     ArrayList localArrayList = new ArrayList();
/* 119 */     boolean bool = "https".equalsIgnoreCase(paramURI.getScheme());
/* 120 */     this.lock.lock();
/*     */     try
/*     */     {
/* 123 */       getInternal1(localArrayList, this.domainIndex, paramURI.getHost(), bool);
/*     */ 
/* 125 */       getInternal2(localArrayList, this.uriIndex, getEffectiveURI(paramURI), bool);
/*     */     } finally {
/* 127 */       this.lock.unlock();
/*     */     }
/*     */ 
/* 130 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public List<HttpCookie> getCookies()
/*     */   {
/* 139 */     this.lock.lock();
/*     */     List localList;
/*     */     try
/*     */     {
/* 141 */       Iterator localIterator = this.cookieJar.iterator();
/* 142 */       while (localIterator.hasNext())
/* 143 */         if (((HttpCookie)localIterator.next()).hasExpired())
/* 144 */           localIterator.remove();
/*     */     }
/*     */     finally
/*     */     {
/* 148 */       localList = Collections.unmodifiableList(this.cookieJar);
/* 149 */       this.lock.unlock();
/*     */     }
/*     */ 
/* 152 */     return localList;
/*     */   }
/*     */ 
/*     */   public List<URI> getURIs()
/*     */   {
/* 160 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 162 */     this.lock.lock();
/*     */     try {
/* 164 */       Iterator localIterator = this.uriIndex.keySet().iterator();
/* 165 */       while (localIterator.hasNext()) {
/* 166 */         URI localURI = (URI)localIterator.next();
/* 167 */         List localList = (List)this.uriIndex.get(localURI);
/* 168 */         if ((localList == null) || (localList.size() == 0))
/*     */         {
/* 171 */           localIterator.remove();
/*     */         }
/*     */       }
/*     */     } finally {
/* 175 */       localArrayList.addAll(this.uriIndex.keySet());
/* 176 */       this.lock.unlock();
/*     */     }
/*     */ 
/* 179 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public boolean remove(URI paramURI, HttpCookie paramHttpCookie)
/*     */   {
/* 188 */     if (paramHttpCookie == null) {
/* 189 */       throw new NullPointerException("cookie is null");
/*     */     }
/*     */ 
/* 192 */     boolean bool = false;
/* 193 */     this.lock.lock();
/*     */     try {
/* 195 */       bool = this.cookieJar.remove(paramHttpCookie);
/*     */     } finally {
/* 197 */       this.lock.unlock();
/*     */     }
/*     */ 
/* 200 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean removeAll()
/*     */   {
/* 208 */     this.lock.lock();
/*     */     try {
/* 210 */       this.cookieJar.clear();
/* 211 */       this.domainIndex.clear();
/* 212 */       this.uriIndex.clear();
/*     */     } finally {
/* 214 */       this.lock.unlock();
/*     */     }
/*     */ 
/* 217 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean netscapeDomainMatches(String paramString1, String paramString2)
/*     */   {
/* 237 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 238 */       return false;
/*     */     }
/*     */ 
/* 242 */     boolean bool = ".local".equalsIgnoreCase(paramString1);
/* 243 */     int i = paramString1.indexOf('.');
/* 244 */     if (i == 0) {
/* 245 */       i = paramString1.indexOf('.', 1);
/*     */     }
/* 247 */     if ((!bool) && ((i == -1) || (i == paramString1.length() - 1))) {
/* 248 */       return false;
/*     */     }
/*     */ 
/* 252 */     int j = paramString2.indexOf('.');
/* 253 */     if ((j == -1) && (bool)) {
/* 254 */       return true;
/*     */     }
/*     */ 
/* 257 */     int k = paramString1.length();
/* 258 */     int m = paramString2.length() - k;
/* 259 */     if (m == 0)
/*     */     {
/* 261 */       return paramString2.equalsIgnoreCase(paramString1);
/* 262 */     }if (m > 0)
/*     */     {
/* 264 */       String str1 = paramString2.substring(0, m);
/* 265 */       String str2 = paramString2.substring(m);
/*     */ 
/* 267 */       return str2.equalsIgnoreCase(paramString1);
/* 268 */     }if (m == -1)
/*     */     {
/* 270 */       return (paramString1.charAt(0) == '.') && (paramString2.equalsIgnoreCase(paramString1.substring(1)));
/*     */     }
/*     */ 
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */   private void getInternal1(List<HttpCookie> paramList, Map<String, List<HttpCookie>> paramMap, String paramString, boolean paramBoolean)
/*     */   {
/* 281 */     ArrayList localArrayList = new ArrayList();
/* 282 */     for (Map.Entry localEntry : paramMap.entrySet()) {
/* 283 */       String str = (String)localEntry.getKey();
/* 284 */       List localList = (List)localEntry.getValue();
/* 285 */       for (Iterator localIterator2 = localList.iterator(); localIterator2.hasNext(); ) { localHttpCookie = (HttpCookie)localIterator2.next();
/* 286 */         if (((localHttpCookie.getVersion() == 0) && (netscapeDomainMatches(str, paramString))) || ((localHttpCookie.getVersion() == 1) && (HttpCookie.domainMatches(str, paramString))))
/*     */         {
/* 288 */           if (this.cookieJar.indexOf(localHttpCookie) != -1)
/*     */           {
/* 290 */             if (!localHttpCookie.hasExpired())
/*     */             {
/* 293 */               if (((paramBoolean) || (!localHttpCookie.getSecure())) && (!paramList.contains(localHttpCookie)))
/*     */               {
/* 295 */                 paramList.add(localHttpCookie);
/*     */               }
/*     */             }
/* 298 */             else localArrayList.add(localHttpCookie);
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 303 */             localArrayList.add(localHttpCookie);
/*     */           }
/*     */         }
/*     */       }
/* 308 */       HttpCookie localHttpCookie;
/* 308 */       for (localIterator2 = localArrayList.iterator(); localIterator2.hasNext(); ) { localHttpCookie = (HttpCookie)localIterator2.next();
/* 309 */         localList.remove(localHttpCookie);
/* 310 */         this.cookieJar.remove(localHttpCookie);
/*     */       }
/*     */ 
/* 313 */       localArrayList.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T> void getInternal2(List<HttpCookie> paramList, Map<T, List<HttpCookie>> paramMap, Comparable<T> paramComparable, boolean paramBoolean)
/*     */   {
/* 325 */     for (Iterator localIterator1 = paramMap.keySet().iterator(); localIterator1.hasNext(); ) { Object localObject = localIterator1.next();
/* 326 */       if (paramComparable.compareTo(localObject) == 0) {
/* 327 */         List localList = (List)paramMap.get(localObject);
/*     */ 
/* 329 */         if (localList != null) {
/* 330 */           Iterator localIterator2 = localList.iterator();
/* 331 */           while (localIterator2.hasNext()) {
/* 332 */             HttpCookie localHttpCookie = (HttpCookie)localIterator2.next();
/* 333 */             if (this.cookieJar.indexOf(localHttpCookie) != -1)
/*     */             {
/* 335 */               if (!localHttpCookie.hasExpired())
/*     */               {
/* 337 */                 if (((paramBoolean) || (!localHttpCookie.getSecure())) && (!paramList.contains(localHttpCookie)))
/*     */                 {
/* 339 */                   paramList.add(localHttpCookie);
/*     */                 }
/*     */               } else { localIterator2.remove();
/* 342 */                 this.cookieJar.remove(localHttpCookie);
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/* 347 */               localIterator2.remove();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T> void addIndex(Map<T, List<HttpCookie>> paramMap, T paramT, HttpCookie paramHttpCookie)
/*     */   {
/* 360 */     if (paramT != null) {
/* 361 */       Object localObject = (List)paramMap.get(paramT);
/* 362 */       if (localObject != null)
/*     */       {
/* 364 */         ((List)localObject).remove(paramHttpCookie);
/*     */ 
/* 366 */         ((List)localObject).add(paramHttpCookie);
/*     */       } else {
/* 368 */         localObject = new ArrayList();
/* 369 */         ((List)localObject).add(paramHttpCookie);
/* 370 */         paramMap.put(paramT, localObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private URI getEffectiveURI(URI paramURI)
/*     */   {
/* 381 */     URI localURI = null;
/*     */     try {
/* 383 */       localURI = new URI("http", paramURI.getHost(), null, null, null);
/*     */     }
/*     */     catch (URISyntaxException localURISyntaxException)
/*     */     {
/* 390 */       localURI = paramURI;
/*     */     }
/*     */ 
/* 393 */     return localURI;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.InMemoryCookieStore
 * JD-Core Version:    0.6.2
 */