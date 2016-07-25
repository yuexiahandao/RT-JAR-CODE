/*     */ package java.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ 
/*     */ public final class ServiceLoader<S>
/*     */   implements Iterable<S>
/*     */ {
/*     */   private static final String PREFIX = "META-INF/services/";
/*     */   private Class<S> service;
/*     */   private ClassLoader loader;
/* 194 */   private LinkedHashMap<String, S> providers = new LinkedHashMap();
/*     */   private ServiceLoader<S>.LazyIterator lookupIterator;
/*     */ 
/*     */   public void reload()
/*     */   {
/* 211 */     this.providers.clear();
/* 212 */     this.lookupIterator = new LazyIterator(this.service, this.loader, null);
/*     */   }
/*     */ 
/*     */   private ServiceLoader(Class<S> paramClass, ClassLoader paramClassLoader) {
/* 216 */     this.service = paramClass;
/* 217 */     this.loader = paramClassLoader;
/* 218 */     reload();
/*     */   }
/*     */ 
/*     */   private static void fail(Class paramClass, String paramString, Throwable paramThrowable)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 224 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString, paramThrowable);
/*     */   }
/*     */ 
/*     */   private static void fail(Class paramClass, String paramString)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 231 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString);
/*     */   }
/*     */ 
/*     */   private static void fail(Class paramClass, URL paramURL, int paramInt, String paramString)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 237 */     fail(paramClass, paramURL + ":" + paramInt + ": " + paramString);
/*     */   }
/*     */ 
/*     */   private int parseLine(Class paramClass, URL paramURL, BufferedReader paramBufferedReader, int paramInt, List<String> paramList)
/*     */     throws IOException, ServiceConfigurationError
/*     */   {
/* 247 */     String str = paramBufferedReader.readLine();
/* 248 */     if (str == null) {
/* 249 */       return -1;
/*     */     }
/* 251 */     int i = str.indexOf('#');
/* 252 */     if (i >= 0) str = str.substring(0, i);
/* 253 */     str = str.trim();
/* 254 */     int j = str.length();
/* 255 */     if (j != 0) {
/* 256 */       if ((str.indexOf(' ') >= 0) || (str.indexOf('\t') >= 0))
/* 257 */         fail(paramClass, paramURL, paramInt, "Illegal configuration-file syntax");
/* 258 */       int k = str.codePointAt(0);
/* 259 */       if (!Character.isJavaIdentifierStart(k))
/* 260 */         fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);
/* 261 */       for (int m = Character.charCount(k); m < j; m += Character.charCount(k)) {
/* 262 */         k = str.codePointAt(m);
/* 263 */         if ((!Character.isJavaIdentifierPart(k)) && (k != 46))
/* 264 */           fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);
/*     */       }
/* 266 */       if ((!this.providers.containsKey(str)) && (!paramList.contains(str)))
/* 267 */         paramList.add(str);
/*     */     }
/* 269 */     return paramInt + 1;
/*     */   }
/*     */ 
/*     */   private Iterator<String> parse(Class paramClass, URL paramURL)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 292 */     InputStream localInputStream = null;
/* 293 */     BufferedReader localBufferedReader = null;
/* 294 */     ArrayList localArrayList = new ArrayList();
/*     */     try { localInputStream = paramURL.openStream();
/* 297 */       localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "utf-8"));
/* 298 */       int i = 1;
/* 299 */       while ((i = parseLine(paramClass, paramURL, localBufferedReader, i, localArrayList)) >= 0); } catch (IOException localIOException2) { fail(paramClass, "Error reading configuration file", localIOException2);
/*     */     } finally {
/*     */       try {
/* 304 */         if (localBufferedReader != null) localBufferedReader.close();
/* 305 */         if (localInputStream != null) localInputStream.close(); 
/*     */       }
/* 307 */       catch (IOException localIOException4) { fail(paramClass, "Error closing configuration file", localIOException4); }
/*     */ 
/*     */     }
/* 310 */     return localArrayList.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<S> iterator()
/*     */   {
/* 431 */     return new Iterator()
/*     */     {
/* 433 */       Iterator<Map.Entry<String, S>> knownProviders = ServiceLoader.this.providers.entrySet().iterator();
/*     */ 
/*     */       public boolean hasNext()
/*     */       {
/* 437 */         if (this.knownProviders.hasNext())
/* 438 */           return true;
/* 439 */         return ServiceLoader.this.lookupIterator.hasNext();
/*     */       }
/*     */ 
/*     */       public S next() {
/* 443 */         if (this.knownProviders.hasNext())
/* 444 */           return ((Map.Entry)this.knownProviders.next()).getValue();
/* 445 */         return ServiceLoader.this.lookupIterator.next();
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 449 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public static <S> ServiceLoader<S> load(Class<S> paramClass, ClassLoader paramClassLoader)
/*     */   {
/* 473 */     return new ServiceLoader(paramClass, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public static <S> ServiceLoader<S> load(Class<S> paramClass)
/*     */   {
/* 498 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 499 */     return load(paramClass, localClassLoader);
/*     */   }
/*     */ 
/*     */   public static <S> ServiceLoader<S> loadInstalled(Class<S> paramClass)
/*     */   {
/* 527 */     ClassLoader localClassLoader1 = ClassLoader.getSystemClassLoader();
/* 528 */     ClassLoader localClassLoader2 = null;
/* 529 */     while (localClassLoader1 != null) {
/* 530 */       localClassLoader2 = localClassLoader1;
/* 531 */       localClassLoader1 = localClassLoader1.getParent();
/*     */     }
/* 533 */     return load(paramClass, localClassLoader2);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 542 */     return "java.util.ServiceLoader[" + this.service.getName() + "]";
/*     */   }
/*     */ 
/*     */   private class LazyIterator
/*     */     implements Iterator<S>
/*     */   {
/*     */     Class<S> service;
/*     */     ClassLoader loader;
/* 321 */     Enumeration<URL> configs = null;
/* 322 */     Iterator<String> pending = null;
/* 323 */     String nextName = null;
/*     */ 
/*     */     private LazyIterator(ClassLoader arg2)
/*     */     {
/*     */       Object localObject1;
/* 326 */       this.service = localObject1;
/*     */       Object localObject2;
/* 327 */       this.loader = localObject2;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 331 */       if (this.nextName != null) {
/* 332 */         return true;
/*     */       }
/* 334 */       if (this.configs == null) {
/*     */         try {
/* 336 */           String str = "META-INF/services/" + this.service.getName();
/* 337 */           if (this.loader == null)
/* 338 */             this.configs = ClassLoader.getSystemResources(str);
/*     */           else
/* 340 */             this.configs = this.loader.getResources(str);
/*     */         } catch (IOException localIOException) {
/* 342 */           ServiceLoader.fail(this.service, "Error locating configuration files", localIOException);
/*     */         }
/*     */       }
/* 345 */       while ((this.pending == null) || (!this.pending.hasNext())) {
/* 346 */         if (!this.configs.hasMoreElements()) {
/* 347 */           return false;
/*     */         }
/* 349 */         this.pending = ServiceLoader.this.parse(this.service, (URL)this.configs.nextElement());
/*     */       }
/* 351 */       this.nextName = ((String)this.pending.next());
/* 352 */       return true;
/*     */     }
/*     */ 
/*     */     public S next() {
/* 356 */       if (!hasNext()) {
/* 357 */         throw new NoSuchElementException();
/*     */       }
/* 359 */       String str = this.nextName;
/* 360 */       this.nextName = null;
/* 361 */       Class localClass = null;
/*     */       try {
/* 363 */         localClass = Class.forName(str, false, this.loader);
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 365 */         ServiceLoader.fail(this.service, "Provider " + str + " not found");
/*     */       }
/*     */ 
/* 368 */       if (!this.service.isAssignableFrom(localClass)) {
/* 369 */         ServiceLoader.fail(this.service, "Provider " + str + " not a subtype");
/*     */       }
/*     */       try
/*     */       {
/* 373 */         Object localObject = this.service.cast(localClass.newInstance());
/* 374 */         ServiceLoader.this.providers.put(str, localObject);
/* 375 */         return localObject;
/*     */       } catch (Throwable localThrowable) {
/* 377 */         ServiceLoader.fail(this.service, "Provider " + str + " could not be instantiated", localThrowable);
/*     */       }
/*     */ 
/* 381 */       throw new Error();
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 385 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.ServiceLoader
 * JD-Core Version:    0.6.2
 */