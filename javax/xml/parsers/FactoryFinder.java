/*     */ package javax.xml.parsers;
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
/*  49 */   private static boolean debug = false;
/*     */ 
/*  54 */   static Properties cacheProps = new Properties();
/*     */ 
/*  60 */   static volatile boolean firstTime = true;
/*     */ 
/*  66 */   static SecuritySupport ss = new SecuritySupport();
/*     */ 
/*     */   private static void dPrint(String msg)
/*     */   {
/*  83 */     if (debug)
/*  84 */       System.err.println("JAXP: " + msg);
/*     */   }
/*     */ 
/*     */   private static Class getProviderClass(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 103 */       if (cl == null) {
/* 104 */         if (useBSClsLoader) {
/* 105 */           return Class.forName(className, true, FactoryFinder.class.getClassLoader());
/*     */         }
/* 107 */         cl = ss.getContextClassLoader();
/* 108 */         if (cl == null) {
/* 109 */           throw new ClassNotFoundException();
/*     */         }
/*     */ 
/* 112 */         return cl.loadClass(className);
/*     */       }
/*     */ 
/* 117 */       return cl.loadClass(className);
/*     */     }
/*     */     catch (ClassNotFoundException e1)
/*     */     {
/* 121 */       if (doFallback)
/*     */       {
/* 123 */         return Class.forName(className, true, FactoryFinder.class.getClassLoader());
/*     */       }
/*     */ 
/* 126 */       throw e1;
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 147 */     return newInstance(className, cl, doFallback, false);
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 170 */     if ((System.getSecurityManager() != null) && 
/* 171 */       (className != null) && (className.startsWith("com.sun.org.apache.xerces.internal"))) {
/* 172 */       cl = null;
/* 173 */       useBSClsLoader = true;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 178 */       Class providerClass = getProviderClass(className, cl, doFallback, useBSClsLoader);
/* 179 */       Object instance = providerClass.newInstance();
/* 180 */       if (debug) {
/* 181 */         dPrint("created new instance of " + providerClass + " using ClassLoader: " + cl);
/*     */       }
/*     */ 
/* 184 */       return instance;
/*     */     }
/*     */     catch (ClassNotFoundException x) {
/* 187 */       throw new ConfigurationError("Provider " + className + " not found", x);
/*     */     }
/*     */     catch (Exception x)
/*     */     {
/* 191 */       throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId, String fallbackClassName)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 212 */     dPrint("find factoryId =" + factoryId);
/*     */     try
/*     */     {
/* 216 */       String systemProp = ss.getSystemProperty(factoryId);
/* 217 */       if (systemProp != null) {
/* 218 */         dPrint("found system property, value=" + systemProp);
/* 219 */         return newInstance(systemProp, null, true);
/*     */       }
/*     */     }
/*     */     catch (SecurityException se) {
/* 223 */       if (debug) se.printStackTrace();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 228 */       String factoryClassName = null;
/* 229 */       if (firstTime) {
/* 230 */         synchronized (cacheProps) {
/* 231 */           if (firstTime) {
/* 232 */             String configFile = ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
/*     */ 
/* 234 */             File f = new File(configFile);
/* 235 */             firstTime = false;
/* 236 */             if (ss.doesFileExist(f)) {
/* 237 */               dPrint("Read properties file " + f);
/* 238 */               cacheProps.load(ss.getFileInputStream(f));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 243 */       factoryClassName = cacheProps.getProperty(factoryId);
/*     */ 
/* 245 */       if (factoryClassName != null) {
/* 246 */         dPrint("found in $java.home/jaxp.properties, value=" + factoryClassName);
/* 247 */         return newInstance(factoryClassName, null, true);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 251 */       if (debug) ex.printStackTrace();
/*     */ 
/*     */     }
/*     */ 
/* 255 */     Object provider = findJarServiceProvider(factoryId);
/* 256 */     if (provider != null) {
/* 257 */       return provider;
/*     */     }
/* 259 */     if (fallbackClassName == null) {
/* 260 */       throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/* 264 */     dPrint("loaded from fallback value: " + fallbackClassName);
/* 265 */     return newInstance(fallbackClassName, null, true);
/*     */   }
/*     */ 
/*     */   private static Object findJarServiceProvider(String factoryId)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 276 */     String serviceId = "META-INF/services/" + factoryId;
/* 277 */     InputStream is = null;
/*     */ 
/* 280 */     ClassLoader cl = ss.getContextClassLoader();
/* 281 */     boolean useBSClsLoader = false;
/* 282 */     if (cl != null) {
/* 283 */       is = ss.getResourceAsStream(cl, serviceId);
/*     */ 
/* 286 */       if (is == null) {
/* 287 */         cl = FactoryFinder.class.getClassLoader();
/* 288 */         is = ss.getResourceAsStream(cl, serviceId);
/* 289 */         useBSClsLoader = true;
/*     */       }
/*     */     }
/*     */     else {
/* 293 */       cl = FactoryFinder.class.getClassLoader();
/* 294 */       is = ss.getResourceAsStream(cl, serviceId);
/* 295 */       useBSClsLoader = true;
/*     */     }
/*     */ 
/* 298 */     if (is == null)
/*     */     {
/* 300 */       return null;
/*     */     }
/*     */ 
/* 303 */     if (debug) {
/* 304 */       dPrint("found jar resource=" + serviceId + " using ClassLoader: " + cl);
/*     */     }
/*     */     BufferedReader rd;
/*     */     try
/*     */     {
/* 309 */       rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 312 */       rd = new BufferedReader(new InputStreamReader(is));
/*     */     }
/*     */ 
/* 315 */     String factoryClassName = null;
/*     */     try
/*     */     {
/* 319 */       factoryClassName = rd.readLine();
/* 320 */       rd.close();
/*     */     }
/*     */     catch (IOException x) {
/* 323 */       return null;
/*     */     }
/*     */ 
/* 326 */     if ((factoryClassName != null) && (!"".equals(factoryClassName))) {
/* 327 */       dPrint("found in resource, value=" + factoryClassName);
/*     */ 
/* 333 */       return newInstance(factoryClassName, cl, false, useBSClsLoader);
/*     */     }
/*     */ 
/* 337 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  73 */       String val = ss.getSystemProperty("jaxp.debug");
/*     */ 
/*  75 */       debug = (val != null) && (!"false".equals(val));
/*     */     }
/*     */     catch (SecurityException se) {
/*  78 */       debug = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ConfigurationError extends Error
/*     */   {
/*     */     private Exception exception;
/*     */ 
/*     */     ConfigurationError(String msg, Exception x)
/*     */     {
/* 348 */       super();
/* 349 */       this.exception = x;
/*     */     }
/*     */ 
/*     */     Exception getException() {
/* 353 */       return this.exception;
/*     */     }
/*     */ 
/*     */     public Throwable getCause()
/*     */     {
/* 360 */       return this.exception;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.parsers.FactoryFinder
 * JD-Core Version:    0.6.2
 */