/*     */ package javax.xml.stream;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Properties;
/*     */ 
/*     */ class FactoryFinder
/*     */ {
/*     */   private static final String DEFAULT_PACKAGE = "com.sun.xml.internal.";
/*  50 */   private static boolean debug = false;
/*     */ 
/*  55 */   static Properties cacheProps = new Properties();
/*     */ 
/*  61 */   static volatile boolean firstTime = true;
/*     */ 
/*  67 */   static SecuritySupport ss = new SecuritySupport();
/*     */ 
/*     */   private static void dPrint(String msg)
/*     */   {
/*  84 */     if (debug)
/*  85 */       System.err.println("JAXP: " + msg);
/*     */   }
/*     */ 
/*     */   private static Class getProviderClass(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 104 */       if (cl == null) {
/* 105 */         if (useBSClsLoader) {
/* 106 */           return Class.forName(className, true, FactoryFinder.class.getClassLoader());
/*     */         }
/* 108 */         cl = ss.getContextClassLoader();
/* 109 */         if (cl == null) {
/* 110 */           throw new ClassNotFoundException();
/*     */         }
/*     */ 
/* 113 */         return cl.loadClass(className);
/*     */       }
/*     */ 
/* 118 */       return cl.loadClass(className);
/*     */     }
/*     */     catch (ClassNotFoundException e1)
/*     */     {
/* 122 */       if (doFallback)
/*     */       {
/* 124 */         return Class.forName(className, true, FactoryFinder.class.getClassLoader());
/*     */       }
/*     */ 
/* 127 */       throw e1;
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 148 */     return newInstance(className, cl, doFallback, false);
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 171 */     if ((System.getSecurityManager() != null) && 
/* 172 */       (className != null) && (className.startsWith("com.sun.xml.internal."))) {
/* 173 */       cl = null;
/* 174 */       useBSClsLoader = true;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 179 */       Class providerClass = getProviderClass(className, cl, doFallback, useBSClsLoader);
/* 180 */       Object instance = providerClass.newInstance();
/* 181 */       if (debug) {
/* 182 */         dPrint("created new instance of " + providerClass + " using ClassLoader: " + cl);
/*     */       }
/*     */ 
/* 185 */       return instance;
/*     */     }
/*     */     catch (ClassNotFoundException x) {
/* 188 */       throw new ConfigurationError("Provider " + className + " not found", x);
/*     */     }
/*     */     catch (Exception x)
/*     */     {
/* 192 */       throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId, String fallbackClassName, boolean standardId)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 215 */     return find(factoryId, null, fallbackClassName, standardId);
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId, ClassLoader cl, String fallbackClassName, boolean standardId)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 241 */     dPrint("find factoryId =" + factoryId);
/*     */     try
/*     */     {
/*     */       String systemProp;
/*     */       String systemProp;
/* 246 */       if (standardId)
/* 247 */         systemProp = ss.getSystemProperty(factoryId);
/*     */       else {
/* 249 */         systemProp = System.getProperty(factoryId);
/*     */       }
/*     */ 
/* 252 */       if (systemProp != null) {
/* 253 */         dPrint("found system property, value=" + systemProp);
/* 254 */         return newInstance(systemProp, null, true);
/*     */       }
/*     */     }
/*     */     catch (SecurityException se) {
/* 258 */       throw new ConfigurationError("Failed to read factoryId '" + factoryId + "'", se);
/*     */     }
/*     */ 
/* 264 */     String configFile = null;
/*     */     try {
/* 266 */       String factoryClassName = null;
/* 267 */       if (firstTime) {
/* 268 */         synchronized (cacheProps) {
/* 269 */           if (firstTime) {
/* 270 */             configFile = ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "stax.properties";
/*     */ 
/* 272 */             File f = new File(configFile);
/* 273 */             firstTime = false;
/* 274 */             if (ss.doesFileExist(f)) {
/* 275 */               dPrint("Read properties file " + f);
/* 276 */               cacheProps.load(ss.getFileInputStream(f));
/*     */             }
/*     */             else {
/* 279 */               configFile = ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
/*     */ 
/* 281 */               f = new File(configFile);
/* 282 */               if (ss.doesFileExist(f)) {
/* 283 */                 dPrint("Read properties file " + f);
/* 284 */                 cacheProps.load(ss.getFileInputStream(f));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 290 */       factoryClassName = cacheProps.getProperty(factoryId);
/*     */ 
/* 292 */       if (factoryClassName != null) {
/* 293 */         dPrint("found in " + configFile + " value=" + factoryClassName);
/* 294 */         return newInstance(factoryClassName, null, true);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 298 */       if (debug) ex.printStackTrace();
/*     */ 
/*     */     }
/*     */ 
/* 302 */     Object provider = findJarServiceProvider(factoryId);
/* 303 */     if (provider != null) {
/* 304 */       return provider;
/*     */     }
/* 306 */     if (fallbackClassName == null) {
/* 307 */       throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/* 311 */     dPrint("loaded from fallback value: " + fallbackClassName);
/* 312 */     return newInstance(fallbackClassName, cl, true);
/*     */   }
/*     */ 
/*     */   private static Object findJarServiceProvider(String factoryId)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 323 */     String serviceId = "META-INF/services/" + factoryId;
/* 324 */     InputStream is = null;
/*     */ 
/* 327 */     ClassLoader cl = ss.getContextClassLoader();
/* 328 */     boolean useBSClsLoader = false;
/* 329 */     if (cl != null) {
/* 330 */       is = ss.getResourceAsStream(cl, serviceId);
/*     */ 
/* 333 */       if (is == null) {
/* 334 */         cl = FactoryFinder.class.getClassLoader();
/* 335 */         is = ss.getResourceAsStream(cl, serviceId);
/* 336 */         useBSClsLoader = true;
/*     */       }
/*     */     }
/*     */     else {
/* 340 */       cl = FactoryFinder.class.getClassLoader();
/* 341 */       is = ss.getResourceAsStream(cl, serviceId);
/* 342 */       useBSClsLoader = true;
/*     */     }
/*     */ 
/* 345 */     if (is == null)
/*     */     {
/* 347 */       return null;
/*     */     }
/*     */ 
/* 350 */     if (debug) {
/* 351 */       dPrint("found jar resource=" + serviceId + " using ClassLoader: " + cl);
/*     */     }
/*     */     BufferedReader rd;
/*     */     try
/*     */     {
/* 356 */       rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 359 */       rd = new BufferedReader(new InputStreamReader(is));
/*     */     }
/*     */ 
/* 362 */     String factoryClassName = null;
/*     */     try
/*     */     {
/* 366 */       factoryClassName = rd.readLine();
/* 367 */       rd.close();
/*     */     }
/*     */     catch (IOException x) {
/* 370 */       return null;
/*     */     }
/*     */ 
/* 373 */     if ((factoryClassName != null) && (!"".equals(factoryClassName))) {
/* 374 */       dPrint("found in resource, value=" + factoryClassName);
/*     */ 
/* 380 */       return newInstance(factoryClassName, cl, false, useBSClsLoader);
/*     */     }
/*     */ 
/* 384 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  74 */       String val = ss.getSystemProperty("jaxp.debug");
/*     */ 
/*  76 */       debug = (val != null) && (!"false".equals(val));
/*     */     }
/*     */     catch (SecurityException se) {
/*  79 */       debug = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ConfigurationError extends Error
/*     */   {
/*     */     private Exception exception;
/*     */ 
/*     */     ConfigurationError(String msg, Exception x)
/*     */     {
/* 395 */       super();
/* 396 */       this.exception = x;
/*     */     }
/*     */ 
/*     */     Exception getException() {
/* 400 */       return this.exception;
/*     */     }
/*     */ 
/*     */     public Throwable getCause()
/*     */     {
/* 407 */       return this.exception;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.stream.FactoryFinder
 * JD-Core Version:    0.6.2
 */