/*     */ package com.sun.xml.internal.ws.policy.privateutil;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.lang.reflect.Array;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ final class ServiceFinder<T>
/*     */   implements Iterable<T>
/*     */ {
/* 129 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(ServiceFinder.class);
/*     */   private static final String prefix = "META-INF/services/";
/*     */   private final Class<T> serviceClass;
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */   static <T> ServiceFinder<T> find(Class<T> service, ClassLoader loader)
/*     */   {
/* 162 */     if (null == service) {
/* 163 */       throw ((NullPointerException)LOGGER.logSevereException(new NullPointerException(LocalizationMessages.WSP_0032_SERVICE_CAN_NOT_BE_NULL())));
/*     */     }
/* 165 */     return new ServiceFinder(service, loader);
/*     */   }
/*     */ 
/*     */   public static <T> ServiceFinder<T> find(Class<T> service)
/*     */   {
/* 185 */     return find(service, Thread.currentThread().getContextClassLoader());
/*     */   }
/*     */ 
/*     */   private ServiceFinder(Class<T> service, ClassLoader loader) {
/* 189 */     this.serviceClass = service;
/* 190 */     this.classLoader = loader;
/*     */   }
/*     */ 
/*     */   public Iterator<T> iterator()
/*     */   {
/* 203 */     return new LazyIterator(this.serviceClass, this.classLoader, null);
/*     */   }
/*     */ 
/*     */   public T[] toArray()
/*     */   {
/* 216 */     List result = new ArrayList();
/* 217 */     for (Iterator i$ = iterator(); i$.hasNext(); ) { Object t = i$.next();
/* 218 */       result.add(t);
/*     */     }
/* 220 */     return result.toArray((Object[])Array.newInstance(this.serviceClass, result.size()));
/*     */   }
/*     */ 
/*     */   private static void fail(Class service, String msg, Throwable cause) throws ServiceConfigurationError
/*     */   {
/* 225 */     ServiceConfigurationError sce = new ServiceConfigurationError(LocalizationMessages.WSP_0025_SPI_FAIL_SERVICE_MSG(service.getName(), msg));
/*     */ 
/* 227 */     if (null != cause) {
/* 228 */       sce.initCause(cause);
/*     */     }
/*     */ 
/* 231 */     throw ((ServiceConfigurationError)LOGGER.logSevereException(sce));
/*     */   }
/*     */ 
/*     */   private static void fail(Class service, URL u, int line, String msg, Throwable cause)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 241 */     fail(service, LocalizationMessages.WSP_0024_SPI_FAIL_SERVICE_URL_LINE_MSG(u, Integer.valueOf(line), msg), cause);
/*     */   }
/*     */ 
/*     */   private static int parseLine(Class service, URL u, BufferedReader r, int lc, List<String> names, Set<String> returned)
/*     */     throws IOException, ServiceConfigurationError
/*     */   {
/* 252 */     String ln = r.readLine();
/* 253 */     if (ln == null) {
/* 254 */       return -1;
/*     */     }
/* 256 */     int ci = ln.indexOf('#');
/* 257 */     if (ci >= 0) ln = ln.substring(0, ci);
/* 258 */     ln = ln.trim();
/* 259 */     int n = ln.length();
/* 260 */     if (n != 0) {
/* 261 */       if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
/* 262 */         fail(service, u, lc, LocalizationMessages.WSP_0067_ILLEGAL_CFG_FILE_SYNTAX(), null);
/* 263 */       int cp = ln.codePointAt(0);
/* 264 */       if (!Character.isJavaIdentifierStart(cp))
/* 265 */         fail(service, u, lc, LocalizationMessages.WSP_0066_ILLEGAL_PROVIDER_CLASSNAME(ln), null);
/* 266 */       for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
/* 267 */         cp = ln.codePointAt(i);
/* 268 */         if ((!Character.isJavaIdentifierPart(cp)) && (cp != 46))
/* 269 */           fail(service, u, lc, LocalizationMessages.WSP_0066_ILLEGAL_PROVIDER_CLASSNAME(ln), null);
/*     */       }
/* 271 */       if (!returned.contains(ln)) {
/* 272 */         names.add(ln);
/* 273 */         returned.add(ln);
/*     */       }
/*     */     }
/* 276 */     return lc + 1;
/*     */   }
/*     */ 
/*     */   private static Iterator<String> parse(Class service, URL u, Set<String> returned)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 297 */     InputStream in = null;
/* 298 */     BufferedReader r = null;
/* 299 */     ArrayList names = new ArrayList();
/*     */     try { in = u.openStream();
/* 302 */       r = new BufferedReader(new InputStreamReader(in, "utf-8"));
/* 303 */       int lc = 1;
/* 304 */       while ((lc = parseLine(service, u, r, lc, names, returned)) >= 0); } catch (IOException x) { fail(service, ": " + x, x);
/*     */     } finally {
/*     */       try {
/* 309 */         if (r != null) r.close();
/* 310 */         if (in != null) in.close(); 
/*     */       }
/* 312 */       catch (IOException y) { fail(service, ": " + y, y); }
/*     */ 
/*     */     }
/* 315 */     return names.iterator();
/*     */   }
/*     */ 
/*     */   private static class LazyIterator<T>
/*     */     implements Iterator<T>
/*     */   {
/*     */     Class<T> service;
/*     */     ClassLoader loader;
/* 325 */     Enumeration<URL> configs = null;
/* 326 */     Iterator<String> pending = null;
/* 327 */     Set<String> returned = new TreeSet();
/* 328 */     String nextName = null;
/*     */ 
/*     */     private LazyIterator(Class<T> service, ClassLoader loader) {
/* 331 */       this.service = service;
/* 332 */       this.loader = loader;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() throws ServiceConfigurationError {
/* 336 */       if (this.nextName != null) {
/* 337 */         return true;
/*     */       }
/* 339 */       if (this.configs == null) {
/*     */         try {
/* 341 */           String fullName = "META-INF/services/" + this.service.getName();
/* 342 */           if (this.loader == null)
/* 343 */             this.configs = ClassLoader.getSystemResources(fullName);
/*     */           else
/* 345 */             this.configs = this.loader.getResources(fullName);
/*     */         } catch (IOException x) {
/* 347 */           ServiceFinder.fail(this.service, ": " + x, x);
/*     */         }
/*     */       }
/* 350 */       while ((this.pending == null) || (!this.pending.hasNext())) {
/* 351 */         if (!this.configs.hasMoreElements()) {
/* 352 */           return false;
/*     */         }
/* 354 */         this.pending = ServiceFinder.parse(this.service, (URL)this.configs.nextElement(), this.returned);
/*     */       }
/* 356 */       this.nextName = ((String)this.pending.next());
/* 357 */       return true;
/*     */     }
/*     */ 
/*     */     public T next() throws ServiceConfigurationError {
/* 361 */       if (!hasNext()) {
/* 362 */         throw new NoSuchElementException();
/*     */       }
/* 364 */       String cn = this.nextName;
/* 365 */       this.nextName = null;
/*     */       try {
/* 367 */         return this.service.cast(Class.forName(cn, true, this.loader).newInstance());
/*     */       } catch (ClassNotFoundException x) {
/* 369 */         ServiceFinder.fail(this.service, LocalizationMessages.WSP_0027_SERVICE_PROVIDER_NOT_FOUND(cn), x);
/*     */       } catch (Exception x) {
/* 371 */         ServiceFinder.fail(this.service, LocalizationMessages.WSP_0028_SERVICE_PROVIDER_COULD_NOT_BE_INSTANTIATED(cn), x);
/*     */       }
/* 373 */       return null;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 377 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.privateutil.ServiceFinder
 * JD-Core Version:    0.6.2
 */