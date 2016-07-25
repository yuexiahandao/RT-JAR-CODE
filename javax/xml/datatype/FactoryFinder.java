/*     */ package javax.xml.datatype;
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
/*     */   private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xerces.internal";
/*  52 */   private static boolean debug = false;
/*     */ 
/*  57 */   static Properties cacheProps = new Properties();
/*     */ 
/*  63 */   static volatile boolean firstTime = true;
/*     */ 
/*  69 */   static SecuritySupport ss = new SecuritySupport();
/*     */ 
/*     */   private static void dPrint(String msg)
/*     */   {
/*  86 */     if (debug)
/*  87 */       System.err.println("JAXP: " + msg);
/*     */   }
/*     */ 
/*     */   private static Class getProviderClass(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 106 */       if (cl == null) {
/* 107 */         if (useBSClsLoader) {
/* 108 */           return Class.forName(className, true, FactoryFinder.class.getClassLoader());
/*     */         }
/* 110 */         cl = ss.getContextClassLoader();
/* 111 */         if (cl == null) {
/* 112 */           throw new ClassNotFoundException();
/*     */         }
/*     */ 
/* 115 */         return cl.loadClass(className);
/*     */       }
/*     */ 
/* 120 */       return cl.loadClass(className);
/*     */     }
/*     */     catch (ClassNotFoundException e1)
/*     */     {
/* 124 */       if (doFallback)
/*     */       {
/* 126 */         return Class.forName(className, true, FactoryFinder.class.getClassLoader());
/*     */       }
/*     */ 
/* 129 */       throw e1;
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 150 */     return newInstance(className, cl, doFallback, false);
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 173 */     if ((System.getSecurityManager() != null) && 
/* 174 */       (className != null) && (className.startsWith("com.sun.org.apache.xerces.internal"))) {
/* 175 */       cl = null;
/* 176 */       useBSClsLoader = true;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 181 */       Class providerClass = getProviderClass(className, cl, doFallback, useBSClsLoader);
/* 182 */       Object instance = providerClass.newInstance();
/* 183 */       if (debug) {
/* 184 */         dPrint("created new instance of " + providerClass + " using ClassLoader: " + cl);
/*     */       }
/*     */ 
/* 187 */       return instance;
/*     */     }
/*     */     catch (ClassNotFoundException x) {
/* 190 */       throw new ConfigurationError("Provider " + className + " not found", x);
/*     */     }
/*     */     catch (Exception x)
/*     */     {
/* 194 */       throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId, String fallbackClassName)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 215 */     dPrint("find factoryId =" + factoryId);
/*     */     try
/*     */     {
/* 219 */       String systemProp = ss.getSystemProperty(factoryId);
/* 220 */       if (systemProp != null) {
/* 221 */         dPrint("found system property, value=" + systemProp);
/* 222 */         return newInstance(systemProp, null, true);
/*     */       }
/*     */     }
/*     */     catch (SecurityException se) {
/* 226 */       if (debug) se.printStackTrace();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 231 */       String factoryClassName = null;
/* 232 */       if (firstTime) {
/* 233 */         synchronized (cacheProps) {
/* 234 */           if (firstTime) {
/* 235 */             String configFile = ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
/*     */ 
/* 237 */             File f = new File(configFile);
/* 238 */             firstTime = false;
/* 239 */             if (ss.doesFileExist(f)) {
/* 240 */               dPrint("Read properties file " + f);
/* 241 */               cacheProps.load(ss.getFileInputStream(f));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 246 */       factoryClassName = cacheProps.getProperty(factoryId);
/*     */ 
/* 248 */       if (factoryClassName != null) {
/* 249 */         dPrint("found in $java.home/jaxp.properties, value=" + factoryClassName);
/* 250 */         return newInstance(factoryClassName, null, true);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 254 */       if (debug) ex.printStackTrace();
/*     */ 
/*     */     }
/*     */ 
/* 258 */     Object provider = findJarServiceProvider(factoryId);
/* 259 */     if (provider != null) {
/* 260 */       return provider;
/*     */     }
/* 262 */     if (fallbackClassName == null) {
/* 263 */       throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/* 267 */     dPrint("loaded from fallback value: " + fallbackClassName);
/* 268 */     return newInstance(fallbackClassName, null, true);
/*     */   }
/*     */ 
/*     */   private static Object findJarServiceProvider(String factoryId)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 279 */     String serviceId = "META-INF/services/" + factoryId;
/* 280 */     InputStream is = null;
/*     */ 
/* 283 */     ClassLoader cl = ss.getContextClassLoader();
/* 284 */     boolean useBSClsLoader = false;
/* 285 */     if (cl != null) {
/* 286 */       is = ss.getResourceAsStream(cl, serviceId);
/*     */ 
/* 289 */       if (is == null) {
/* 290 */         cl = FactoryFinder.class.getClassLoader();
/* 291 */         is = ss.getResourceAsStream(cl, serviceId);
/* 292 */         useBSClsLoader = true;
/*     */       }
/*     */     }
/*     */     else {
/* 296 */       cl = FactoryFinder.class.getClassLoader();
/* 297 */       is = ss.getResourceAsStream(cl, serviceId);
/* 298 */       useBSClsLoader = true;
/*     */     }
/*     */ 
/* 301 */     if (is == null)
/*     */     {
/* 303 */       return null;
/*     */     }
/*     */ 
/* 306 */     if (debug) {
/* 307 */       dPrint("found jar resource=" + serviceId + " using ClassLoader: " + cl);
/*     */     }
/*     */     BufferedReader rd;
/*     */     try
/*     */     {
/* 312 */       rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 315 */       rd = new BufferedReader(new InputStreamReader(is));
/*     */     }
/*     */ 
/* 318 */     String factoryClassName = null;
/*     */     try
/*     */     {
/* 322 */       factoryClassName = rd.readLine();
/* 323 */       rd.close();
/*     */     }
/*     */     catch (IOException x) {
/* 326 */       return null;
/*     */     }
/*     */ 
/* 329 */     if ((factoryClassName != null) && (!"".equals(factoryClassName))) {
/* 330 */       dPrint("found in resource, value=" + factoryClassName);
/*     */ 
/* 336 */       return newInstance(factoryClassName, cl, false, useBSClsLoader);
/*     */     }
/*     */ 
/* 340 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  76 */       String val = ss.getSystemProperty("jaxp.debug");
/*     */ 
/*  78 */       debug = (val != null) && (!"false".equals(val));
/*     */     }
/*     */     catch (SecurityException se) {
/*  81 */       debug = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ConfigurationError extends Error
/*     */   {
/*     */     private Exception exception;
/*     */ 
/*     */     ConfigurationError(String msg, Exception x)
/*     */     {
/* 351 */       super();
/* 352 */       this.exception = x;
/*     */     }
/*     */ 
/*     */     Exception getException() {
/* 356 */       return this.exception;
/*     */     }
/*     */ 
/*     */     public Throwable getCause()
/*     */     {
/* 363 */       return this.exception;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.datatype.FactoryFinder
 * JD-Core Version:    0.6.2
 */