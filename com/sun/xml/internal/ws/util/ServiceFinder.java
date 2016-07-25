/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
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
/*     */ public final class ServiceFinder<T>
/*     */   implements Iterable<T>
/*     */ {
/*     */   private static final String prefix = "META-INF/services/";
/*     */   private final Class<T> serviceClass;
/*     */ 
/*     */   @Nullable
/*     */   private final ClassLoader classLoader;
/*     */ 
/*     */   public static <T> ServiceFinder<T> find(@NotNull Class<T> service, @Nullable ClassLoader loader)
/*     */   {
/* 163 */     return new ServiceFinder(service, loader);
/*     */   }
/*     */ 
/*     */   public static <T> ServiceFinder<T> find(Class<T> service)
/*     */   {
/* 183 */     return find(service, Thread.currentThread().getContextClassLoader());
/*     */   }
/*     */ 
/*     */   private ServiceFinder(Class<T> service, ClassLoader loader) {
/* 187 */     this.serviceClass = service;
/* 188 */     this.classLoader = loader;
/*     */   }
/*     */ 
/*     */   public Iterator<T> iterator()
/*     */   {
/* 201 */     return new LazyIterator(this.serviceClass, this.classLoader, null);
/*     */   }
/*     */ 
/*     */   public T[] toArray()
/*     */   {
/* 213 */     List result = new ArrayList();
/* 214 */     for (Iterator i$ = iterator(); i$.hasNext(); ) { Object t = i$.next();
/* 215 */       result.add(t);
/*     */     }
/* 217 */     return result.toArray((Object[])Array.newInstance(this.serviceClass, result.size()));
/*     */   }
/*     */ 
/*     */   private static void fail(Class service, String msg, Throwable cause) throws ServiceConfigurationError
/*     */   {
/* 222 */     ServiceConfigurationError sce = new ServiceConfigurationError(service.getName() + ": " + msg);
/*     */ 
/* 224 */     sce.initCause(cause);
/* 225 */     throw sce;
/*     */   }
/*     */ 
/*     */   private static void fail(Class service, String msg) throws ServiceConfigurationError
/*     */   {
/* 230 */     throw new ServiceConfigurationError(service.getName() + ": " + msg);
/*     */   }
/*     */ 
/*     */   private static void fail(Class service, URL u, int line, String msg) throws ServiceConfigurationError
/*     */   {
/* 235 */     fail(service, u + ":" + line + ": " + msg);
/*     */   }
/*     */ 
/*     */   private static int parseLine(Class service, URL u, BufferedReader r, int lc, List<String> names, Set<String> returned)
/*     */     throws IOException, ServiceConfigurationError
/*     */   {
/* 246 */     String ln = r.readLine();
/* 247 */     if (ln == null) {
/* 248 */       return -1;
/*     */     }
/* 250 */     int ci = ln.indexOf('#');
/* 251 */     if (ci >= 0) ln = ln.substring(0, ci);
/* 252 */     ln = ln.trim();
/* 253 */     int n = ln.length();
/* 254 */     if (n != 0) {
/* 255 */       if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
/* 256 */         fail(service, u, lc, "Illegal configuration-file syntax");
/* 257 */       int cp = ln.codePointAt(0);
/* 258 */       if (!Character.isJavaIdentifierStart(cp))
/* 259 */         fail(service, u, lc, "Illegal provider-class name: " + ln);
/* 260 */       for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
/* 261 */         cp = ln.codePointAt(i);
/* 262 */         if ((!Character.isJavaIdentifierPart(cp)) && (cp != 46))
/* 263 */           fail(service, u, lc, "Illegal provider-class name: " + ln);
/*     */       }
/* 265 */       if (!returned.contains(ln)) {
/* 266 */         names.add(ln);
/* 267 */         returned.add(ln);
/*     */       }
/*     */     }
/* 270 */     return lc + 1;
/*     */   }
/*     */ 
/*     */   private static Iterator<String> parse(Class service, URL u, Set<String> returned)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 291 */     InputStream in = null;
/* 292 */     BufferedReader r = null;
/* 293 */     ArrayList names = new ArrayList();
/*     */     try { in = u.openStream();
/* 296 */       r = new BufferedReader(new InputStreamReader(in, "utf-8"));
/* 297 */       int lc = 1;
/* 298 */       while ((lc = parseLine(service, u, r, lc, names, returned)) >= 0); } catch (IOException x) { fail(service, ": " + x);
/*     */     } finally {
/*     */       try {
/* 303 */         if (r != null) r.close();
/* 304 */         if (in != null) in.close(); 
/*     */       }
/* 306 */       catch (IOException y) { fail(service, ": " + y); }
/*     */ 
/*     */     }
/* 309 */     return names.iterator();
/*     */   }
/*     */ 
/*     */   private static class LazyIterator<T>
/*     */     implements Iterator<T>
/*     */   {
/*     */     Class<T> service;
/*     */ 
/*     */     @Nullable
/*     */     ClassLoader loader;
/* 319 */     Enumeration<URL> configs = null;
/* 320 */     Iterator<String> pending = null;
/* 321 */     Set<String> returned = new TreeSet();
/* 322 */     String nextName = null;
/* 323 */     URL currentConfig = null;
/*     */ 
/*     */     private LazyIterator(Class<T> service, ClassLoader loader) {
/* 326 */       this.service = service;
/* 327 */       this.loader = loader;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() throws ServiceConfigurationError {
/* 331 */       if (this.nextName != null) {
/* 332 */         return true;
/*     */       }
/* 334 */       if (this.configs == null) {
/*     */         try {
/* 336 */           String fullName = "META-INF/services/" + this.service.getName();
/* 337 */           if (this.loader == null)
/* 338 */             this.configs = ClassLoader.getSystemResources(fullName);
/*     */           else
/* 340 */             this.configs = this.loader.getResources(fullName);
/*     */         } catch (IOException x) {
/* 342 */           ServiceFinder.fail(this.service, ": " + x);
/*     */         }
/*     */       }
/* 345 */       while ((this.pending == null) || (!this.pending.hasNext())) {
/* 346 */         if (!this.configs.hasMoreElements()) {
/* 347 */           return false;
/*     */         }
/* 349 */         this.currentConfig = ((URL)this.configs.nextElement());
/* 350 */         this.pending = ServiceFinder.parse(this.service, this.currentConfig, this.returned);
/*     */       }
/* 352 */       this.nextName = ((String)this.pending.next());
/* 353 */       return true;
/*     */     }
/*     */ 
/*     */     public T next() throws ServiceConfigurationError {
/* 357 */       if (!hasNext()) {
/* 358 */         throw new NoSuchElementException();
/*     */       }
/* 360 */       String cn = this.nextName;
/* 361 */       this.nextName = null;
/*     */       try {
/* 363 */         return this.service.cast(Class.forName(cn, true, this.loader).newInstance());
/*     */       } catch (ClassNotFoundException x) {
/* 365 */         ServiceFinder.fail(this.service, "Provider " + cn + " is specified in " + this.currentConfig + " but not found");
/*     */       }
/*     */       catch (Exception x) {
/* 368 */         ServiceFinder.fail(this.service, "Provider " + cn + " is specified in " + this.currentConfig + "but could not be instantiated: " + x, x);
/*     */       }
/*     */ 
/* 371 */       return null;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 375 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.ServiceFinder
 * JD-Core Version:    0.6.2
 */