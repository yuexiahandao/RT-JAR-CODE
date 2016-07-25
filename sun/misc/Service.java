/*     */ package sun.misc;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public final class Service
/*     */ {
/*     */   private static final String prefix = "META-INF/services/";
/*     */ 
/*     */   private static void fail(Class paramClass, String paramString, Throwable paramThrowable)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 137 */     ServiceConfigurationError localServiceConfigurationError = new ServiceConfigurationError(paramClass.getName() + ": " + paramString);
/*     */ 
/* 139 */     localServiceConfigurationError.initCause(paramThrowable);
/* 140 */     throw localServiceConfigurationError;
/*     */   }
/*     */ 
/*     */   private static void fail(Class paramClass, String paramString)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 146 */     throw new ServiceConfigurationError(paramClass.getName() + ": " + paramString);
/*     */   }
/*     */ 
/*     */   private static void fail(Class paramClass, URL paramURL, int paramInt, String paramString)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 152 */     fail(paramClass, paramURL + ":" + paramInt + ": " + paramString);
/*     */   }
/*     */ 
/*     */   private static int parseLine(Class paramClass, URL paramURL, BufferedReader paramBufferedReader, int paramInt, List paramList, Set paramSet)
/*     */     throws IOException, ServiceConfigurationError
/*     */   {
/* 164 */     String str = paramBufferedReader.readLine();
/* 165 */     if (str == null) {
/* 166 */       return -1;
/*     */     }
/* 168 */     int i = str.indexOf('#');
/* 169 */     if (i >= 0) str = str.substring(0, i);
/* 170 */     str = str.trim();
/* 171 */     int j = str.length();
/* 172 */     if (j != 0) {
/* 173 */       if ((str.indexOf(' ') >= 0) || (str.indexOf('\t') >= 0))
/* 174 */         fail(paramClass, paramURL, paramInt, "Illegal configuration-file syntax");
/* 175 */       int k = str.codePointAt(0);
/* 176 */       if (!Character.isJavaIdentifierStart(k))
/* 177 */         fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);
/* 178 */       for (int m = Character.charCount(k); m < j; m += Character.charCount(k)) {
/* 179 */         k = str.codePointAt(m);
/* 180 */         if ((!Character.isJavaIdentifierPart(k)) && (k != 46))
/* 181 */           fail(paramClass, paramURL, paramInt, "Illegal provider-class name: " + str);
/*     */       }
/* 183 */       if (!paramSet.contains(str)) {
/* 184 */         paramList.add(str);
/* 185 */         paramSet.add(str);
/*     */       }
/*     */     }
/* 188 */     return paramInt + 1;
/*     */   }
/*     */ 
/*     */   private static Iterator parse(Class paramClass, URL paramURL, Set paramSet)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 217 */     InputStream localInputStream = null;
/* 218 */     BufferedReader localBufferedReader = null;
/* 219 */     ArrayList localArrayList = new ArrayList();
/*     */     try { localInputStream = paramURL.openStream();
/* 222 */       localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream, "utf-8"));
/* 223 */       int i = 1;
/* 224 */       while ((i = parseLine(paramClass, paramURL, localBufferedReader, i, localArrayList, paramSet)) >= 0); } catch (IOException localIOException2) { fail(paramClass, ": " + localIOException2);
/*     */     } finally {
/*     */       try {
/* 229 */         if (localBufferedReader != null) localBufferedReader.close();
/* 230 */         if (localInputStream != null) localInputStream.close(); 
/*     */       }
/* 232 */       catch (IOException localIOException4) { fail(paramClass, ": " + localIOException4); }
/*     */ 
/*     */     }
/* 235 */     return localArrayList.iterator();
/*     */   }
/*     */ 
/*     */   public static Iterator providers(Class paramClass, ClassLoader paramClassLoader)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 356 */     return new LazyIterator(paramClass, paramClassLoader, null);
/*     */   }
/*     */ 
/*     */   public static Iterator providers(Class paramClass)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 388 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 389 */     return providers(paramClass, localClassLoader);
/*     */   }
/*     */ 
/*     */   public static Iterator installedProviders(Class paramClass)
/*     */     throws ServiceConfigurationError
/*     */   {
/* 425 */     ClassLoader localClassLoader1 = ClassLoader.getSystemClassLoader();
/* 426 */     ClassLoader localClassLoader2 = null;
/* 427 */     while (localClassLoader1 != null) {
/* 428 */       localClassLoader2 = localClassLoader1;
/* 429 */       localClassLoader1 = localClassLoader1.getParent();
/*     */     }
/* 431 */     return providers(paramClass, localClassLoader2);
/*     */   }
/*     */ 
/*     */   private static class LazyIterator
/*     */     implements Iterator
/*     */   {
/*     */     Class service;
/*     */     ClassLoader loader;
/* 246 */     Enumeration configs = null;
/* 247 */     Iterator pending = null;
/* 248 */     Set returned = new TreeSet();
/* 249 */     String nextName = null;
/*     */ 
/*     */     private LazyIterator(Class paramClass, ClassLoader paramClassLoader) {
/* 252 */       this.service = paramClass;
/* 253 */       this.loader = paramClassLoader;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() throws ServiceConfigurationError {
/* 257 */       if (this.nextName != null) {
/* 258 */         return true;
/*     */       }
/* 260 */       if (this.configs == null) {
/*     */         try {
/* 262 */           String str = "META-INF/services/" + this.service.getName();
/* 263 */           if (this.loader == null)
/* 264 */             this.configs = ClassLoader.getSystemResources(str);
/*     */           else
/* 266 */             this.configs = this.loader.getResources(str);
/*     */         } catch (IOException localIOException) {
/* 268 */           Service.fail(this.service, ": " + localIOException);
/*     */         }
/*     */       }
/* 271 */       while ((this.pending == null) || (!this.pending.hasNext())) {
/* 272 */         if (!this.configs.hasMoreElements()) {
/* 273 */           return false;
/*     */         }
/* 275 */         this.pending = Service.parse(this.service, (URL)this.configs.nextElement(), this.returned);
/*     */       }
/* 277 */       this.nextName = ((String)this.pending.next());
/* 278 */       return true;
/*     */     }
/*     */ 
/*     */     public Object next() throws ServiceConfigurationError {
/* 282 */       if (!hasNext()) {
/* 283 */         throw new NoSuchElementException();
/*     */       }
/* 285 */       String str = this.nextName;
/* 286 */       this.nextName = null;
/* 287 */       Class localClass = null;
/*     */       try {
/* 289 */         localClass = Class.forName(str, false, this.loader);
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 291 */         Service.fail(this.service, "Provider " + str + " not found");
/*     */       }
/*     */ 
/* 294 */       if (!this.service.isAssignableFrom(localClass)) {
/* 295 */         Service.fail(this.service, "Provider " + str + " not a subtype");
/*     */       }
/*     */       try
/*     */       {
/* 299 */         return this.service.cast(localClass.newInstance());
/*     */       } catch (Throwable localThrowable) {
/* 301 */         Service.fail(this.service, "Provider " + str + " could not be instantiated", localThrowable);
/*     */       }
/*     */ 
/* 305 */       return null;
/*     */     }
/*     */ 
/*     */     public void remove() {
/* 309 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Service
 * JD-Core Version:    0.6.2
 */