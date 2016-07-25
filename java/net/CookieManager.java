/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ public class CookieManager extends CookieHandler
/*     */ {
/*     */   private CookiePolicy policyCallback;
/* 124 */   private CookieStore cookieJar = null;
/*     */ 
/*     */   public CookieManager()
/*     */   {
/* 137 */     this(null, null);
/*     */   }
/*     */ 
/*     */   public CookieManager(CookieStore paramCookieStore, CookiePolicy paramCookiePolicy)
/*     */   {
/* 156 */     this.policyCallback = (paramCookiePolicy == null ? CookiePolicy.ACCEPT_ORIGINAL_SERVER : paramCookiePolicy);
/*     */ 
/* 160 */     if (paramCookieStore == null)
/* 161 */       this.cookieJar = new InMemoryCookieStore();
/*     */     else
/* 163 */       this.cookieJar = paramCookieStore;
/*     */   }
/*     */ 
/*     */   public void setCookiePolicy(CookiePolicy paramCookiePolicy)
/*     */   {
/* 181 */     if (paramCookiePolicy != null) this.policyCallback = paramCookiePolicy;
/*     */   }
/*     */ 
/*     */   public CookieStore getCookieStore()
/*     */   {
/* 191 */     return this.cookieJar;
/*     */   }
/*     */ 
/*     */   public Map<String, List<String>> get(URI paramURI, Map<String, List<String>> paramMap)
/*     */     throws IOException
/*     */   {
/* 200 */     if ((paramURI == null) || (paramMap == null)) {
/* 201 */       throw new IllegalArgumentException("Argument is null");
/*     */     }
/*     */ 
/* 204 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 207 */     if (this.cookieJar == null) {
/* 208 */       return Collections.unmodifiableMap(localHashMap);
/*     */     }
/* 210 */     boolean bool = "https".equalsIgnoreCase(paramURI.getScheme());
/* 211 */     ArrayList localArrayList = new ArrayList();
/* 212 */     String str1 = paramURI.getPath();
/* 213 */     if ((str1 == null) || (str1.isEmpty())) {
/* 214 */       str1 = "/";
/*     */     }
/* 216 */     for (Object localObject = this.cookieJar.get(paramURI).iterator(); ((Iterator)localObject).hasNext(); ) { HttpCookie localHttpCookie = (HttpCookie)((Iterator)localObject).next();
/*     */ 
/* 220 */       if ((pathMatches(str1, localHttpCookie.getPath())) && ((bool) || (!localHttpCookie.getSecure())))
/*     */       {
/*     */         String str2;
/* 223 */         if (localHttpCookie.isHttpOnly()) {
/* 224 */           str2 = paramURI.getScheme();
/* 225 */           if ((!"http".equalsIgnoreCase(str2)) && (!"https".equalsIgnoreCase(str2)));
/*     */         }
/*     */         else
/*     */         {
/* 230 */           str2 = localHttpCookie.getPortlist();
/* 231 */           if ((str2 != null) && (!str2.isEmpty())) {
/* 232 */             int i = paramURI.getPort();
/* 233 */             if (i == -1) {
/* 234 */               i = "https".equals(paramURI.getScheme()) ? 443 : 80;
/*     */             }
/* 236 */             if (isInPortList(str2, i))
/* 237 */               localArrayList.add(localHttpCookie);
/*     */           }
/*     */           else {
/* 240 */             localArrayList.add(localHttpCookie);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 246 */     localObject = sortByPath(localArrayList);
/*     */ 
/* 248 */     localHashMap.put("Cookie", localObject);
/* 249 */     return Collections.unmodifiableMap(localHashMap);
/*     */   }
/*     */ 
/*     */   public void put(URI paramURI, Map<String, List<String>> paramMap)
/*     */     throws IOException
/*     */   {
/* 258 */     if ((paramURI == null) || (paramMap == null)) {
/* 259 */       throw new IllegalArgumentException("Argument is null");
/*     */     }
/*     */ 
/* 264 */     if (this.cookieJar == null) {
/* 265 */       return;
/*     */     }
/* 267 */     PlatformLogger localPlatformLogger = PlatformLogger.getLogger("java.net.CookieManager");
/* 268 */     for (String str1 : paramMap.keySet())
/*     */     {
/* 271 */       if ((str1 != null) && ((str1.equalsIgnoreCase("Set-Cookie2")) || (str1.equalsIgnoreCase("Set-Cookie"))))
/*     */       {
/* 280 */         for (String str2 : (List)paramMap.get(str1))
/*     */           try {
/*     */             List localList;
/*     */             try {
/* 284 */               localList = HttpCookie.parse(str2);
/*     */             }
/*     */             catch (IllegalArgumentException localIllegalArgumentException2) {
/* 287 */               localList = Collections.EMPTY_LIST;
/* 288 */               if (localPlatformLogger.isLoggable(1000)) {
/* 289 */                 localPlatformLogger.severe("Invalid cookie for " + paramURI + ": " + str2);
/*     */               }
/*     */             }
/* 292 */             for (HttpCookie localHttpCookie : localList)
/*     */             {
/*     */               int i;
/* 293 */               if (localHttpCookie.getPath() == null)
/*     */               {
/* 296 */                 str3 = paramURI.getPath();
/* 297 */                 if (!str3.endsWith("/")) {
/* 298 */                   i = str3.lastIndexOf("/");
/* 299 */                   if (i > 0)
/* 300 */                     str3 = str3.substring(0, i + 1);
/*     */                   else {
/* 302 */                     str3 = "/";
/*     */                   }
/*     */                 }
/* 305 */                 localHttpCookie.setPath(str3);
/*     */               }
/*     */ 
/* 312 */               if (localHttpCookie.getDomain() == null) {
/* 313 */                 localHttpCookie.setDomain(paramURI.getHost());
/*     */               }
/* 315 */               String str3 = localHttpCookie.getPortlist();
/* 316 */               if (str3 != null) {
/* 317 */                 i = paramURI.getPort();
/* 318 */                 if (i == -1) {
/* 319 */                   i = "https".equals(paramURI.getScheme()) ? 443 : 80;
/*     */                 }
/* 321 */                 if (str3.isEmpty())
/*     */                 {
/* 324 */                   localHttpCookie.setPortlist("" + i);
/* 325 */                   if (shouldAcceptInternal(paramURI, localHttpCookie)) {
/* 326 */                     this.cookieJar.add(paramURI, localHttpCookie);
/*     */                   }
/*     */ 
/*     */                 }
/* 332 */                 else if ((isInPortList(str3, i)) && (shouldAcceptInternal(paramURI, localHttpCookie)))
/*     */                 {
/* 334 */                   this.cookieJar.add(paramURI, localHttpCookie);
/*     */                 }
/*     */ 
/*     */               }
/* 338 */               else if (shouldAcceptInternal(paramURI, localHttpCookie)) {
/* 339 */                 this.cookieJar.add(paramURI, localHttpCookie);
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (IllegalArgumentException localIllegalArgumentException1)
/*     */           {
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean shouldAcceptInternal(URI paramURI, HttpCookie paramHttpCookie)
/*     */   {
/*     */     try
/*     */     {
/* 357 */       return this.policyCallback.shouldAccept(paramURI, paramHttpCookie); } catch (Exception localException) {
/*     */     }
/* 359 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean isInPortList(String paramString, int paramInt)
/*     */   {
/* 365 */     int i = paramString.indexOf(",");
/* 366 */     int j = -1;
/* 367 */     while (i > 0) {
/*     */       try {
/* 369 */         j = Integer.parseInt(paramString.substring(0, i));
/* 370 */         if (j == paramInt)
/* 371 */           return true;
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException1) {
/*     */       }
/* 375 */       paramString = paramString.substring(i + 1);
/* 376 */       i = paramString.indexOf(",");
/*     */     }
/* 378 */     if (!paramString.isEmpty())
/*     */       try {
/* 380 */         j = Integer.parseInt(paramString);
/* 381 */         if (j == paramInt)
/* 382 */           return true;
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException2)
/*     */       {
/*     */       }
/* 387 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean pathMatches(String paramString1, String paramString2)
/*     */   {
/* 394 */     if (paramString1 == paramString2)
/* 395 */       return true;
/* 396 */     if ((paramString1 == null) || (paramString2 == null))
/* 397 */       return false;
/* 398 */     if (paramString1.startsWith(paramString2)) {
/* 399 */       return true;
/*     */     }
/* 401 */     return false;
/*     */   }
/*     */ 
/*     */   private List<String> sortByPath(List<HttpCookie> paramList)
/*     */   {
/* 410 */     Collections.sort(paramList, new CookiePathComparator());
/*     */ 
/* 412 */     ArrayList localArrayList = new ArrayList();
/* 413 */     for (HttpCookie localHttpCookie : paramList)
/*     */     {
/* 418 */       if ((paramList.indexOf(localHttpCookie) == 0) && (localHttpCookie.getVersion() > 0)) {
/* 419 */         localArrayList.add("$Version=\"1\"");
/*     */       }
/*     */ 
/* 422 */       localArrayList.add(localHttpCookie.toString());
/*     */     }
/* 424 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   static class CookiePathComparator implements Comparator<HttpCookie>
/*     */   {
/*     */     public int compare(HttpCookie paramHttpCookie1, HttpCookie paramHttpCookie2) {
/* 430 */       if (paramHttpCookie1 == paramHttpCookie2) return 0;
/* 431 */       if (paramHttpCookie1 == null) return -1;
/* 432 */       if (paramHttpCookie2 == null) return 1;
/*     */ 
/* 435 */       if (!paramHttpCookie1.getName().equals(paramHttpCookie2.getName())) return 0;
/*     */ 
/* 438 */       if (paramHttpCookie1.getPath().startsWith(paramHttpCookie2.getPath()))
/* 439 */         return -1;
/* 440 */       if (paramHttpCookie2.getPath().startsWith(paramHttpCookie1.getPath())) {
/* 441 */         return 1;
/*     */       }
/* 443 */       return 0;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.CookieManager
 * JD-Core Version:    0.6.2
 */