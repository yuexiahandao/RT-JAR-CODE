/*     */ package javax.xml.transform;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Properties;
/*     */ 
/*     */ class FactoryFinder
/*     */ {
/*     */   private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xalan.internal.";
/*  51 */   private static boolean debug = false;
/*     */ 
/*  56 */   static Properties cacheProps = new Properties();
/*     */ 
/*  62 */   static volatile boolean firstTime = true;
/*     */ 
/*  68 */   static SecuritySupport ss = new SecuritySupport();
/*     */ 
/*     */   private static void dPrint(String msg)
/*     */   {
/*  85 */     if (debug)
/*  86 */       System.err.println("JAXP: " + msg);
/*     */   }
/*     */ 
/*     */   private static Class getProviderClass(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 105 */       if (cl == null) {
/* 106 */         if (useBSClsLoader) {
/* 107 */           return Class.forName(className, true, FactoryFinder.class.getClassLoader());
/*     */         }
/* 109 */         cl = ss.getContextClassLoader();
/* 110 */         if (cl == null) {
/* 111 */           throw new ClassNotFoundException();
/*     */         }
/*     */ 
/* 114 */         return cl.loadClass(className);
/*     */       }
/*     */ 
/* 119 */       return cl.loadClass(className);
/*     */     }
/*     */     catch (ClassNotFoundException e1)
/*     */     {
/* 123 */       if (doFallback)
/*     */       {
/* 125 */         return Class.forName(className, true, FactoryFinder.class.getClassLoader());
/*     */       }
/*     */ 
/* 128 */       throw e1;
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 149 */     return newInstance(className, cl, doFallback, false, false);
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback, boolean useBSClsLoader, boolean useServicesMechanism)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 174 */     if ((System.getSecurityManager() != null) && 
/* 175 */       (className != null) && (className.startsWith("com.sun.org.apache.xalan.internal."))) {
/* 176 */       cl = null;
/* 177 */       useBSClsLoader = true;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 182 */       Class providerClass = getProviderClass(className, cl, doFallback, useBSClsLoader);
/* 183 */       Object instance = null;
/* 184 */       if (!useServicesMechanism) {
/* 185 */         instance = newInstanceNoServiceLoader(providerClass);
/*     */       }
/* 187 */       if (instance == null) {
/* 188 */         instance = providerClass.newInstance();
/*     */       }
/* 190 */       if (debug) {
/* 191 */         dPrint("created new instance of " + providerClass + " using ClassLoader: " + cl);
/*     */       }
/*     */ 
/* 194 */       return instance;
/*     */     }
/*     */     catch (ClassNotFoundException x) {
/* 197 */       throw new ConfigurationError("Provider " + className + " not found", x);
/*     */     }
/*     */     catch (Exception x)
/*     */     {
/* 201 */       throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object newInstanceNoServiceLoader(Class<?> providerClass)
/*     */   {
/* 214 */     if (System.getSecurityManager() == null)
/* 215 */       return null;
/*     */     try
/*     */     {
/* 218 */       Method creationMethod = providerClass.getDeclaredMethod("newTransformerFactoryNoServiceLoader", new Class[0]);
/*     */ 
/* 222 */       return creationMethod.invoke(null, (Object[])null);
/*     */     } catch (NoSuchMethodException exc) {
/* 224 */       return null; } catch (Exception exc) {
/*     */     }
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId, String fallbackClassName)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 244 */     dPrint("find factoryId =" + factoryId);
/*     */     try
/*     */     {
/* 247 */       String systemProp = ss.getSystemProperty(factoryId);
/* 248 */       if (systemProp != null) {
/* 249 */         dPrint("found system property, value=" + systemProp);
/* 250 */         return newInstance(systemProp, null, true, false, true);
/*     */       }
/*     */     }
/*     */     catch (SecurityException se) {
/* 254 */       if (debug) se.printStackTrace();
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 259 */       String factoryClassName = null;
/* 260 */       if (firstTime) {
/* 261 */         synchronized (cacheProps) {
/* 262 */           if (firstTime) {
/* 263 */             String configFile = ss.getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
/*     */ 
/* 265 */             File f = new File(configFile);
/* 266 */             firstTime = false;
/* 267 */             if (ss.doesFileExist(f)) {
/* 268 */               dPrint("Read properties file " + f);
/* 269 */               cacheProps.load(ss.getFileInputStream(f));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 274 */       factoryClassName = cacheProps.getProperty(factoryId);
/*     */ 
/* 276 */       if (factoryClassName != null) {
/* 277 */         dPrint("found in $java.home/jaxp.properties, value=" + factoryClassName);
/* 278 */         return newInstance(factoryClassName, null, true, false, true);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 282 */       if (debug) ex.printStackTrace();
/*     */ 
/*     */     }
/*     */ 
/* 286 */     Object provider = findJarServiceProvider(factoryId);
/* 287 */     if (provider != null) {
/* 288 */       return provider;
/*     */     }
/* 290 */     if (fallbackClassName == null) {
/* 291 */       throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/* 295 */     dPrint("loaded from fallback value: " + fallbackClassName);
/* 296 */     return newInstance(fallbackClassName, null, true, false, true);
/*     */   }
/*     */ 
/*     */   private static Object findJarServiceProvider(String factoryId)
/*     */     throws FactoryFinder.ConfigurationError
/*     */   {
/* 307 */     String serviceId = "META-INF/services/" + factoryId;
/* 308 */     InputStream is = null;
/*     */ 
/* 311 */     ClassLoader cl = ss.getContextClassLoader();
/* 312 */     boolean useBSClsLoader = false;
/* 313 */     if (cl != null) {
/* 314 */       is = ss.getResourceAsStream(cl, serviceId);
/*     */ 
/* 317 */       if (is == null) {
/* 318 */         cl = FactoryFinder.class.getClassLoader();
/* 319 */         is = ss.getResourceAsStream(cl, serviceId);
/* 320 */         useBSClsLoader = true;
/*     */       }
/*     */     }
/*     */     else {
/* 324 */       cl = FactoryFinder.class.getClassLoader();
/* 325 */       is = ss.getResourceAsStream(cl, serviceId);
/* 326 */       useBSClsLoader = true;
/*     */     }
/*     */ 
/* 329 */     if (is == null)
/*     */     {
/* 331 */       return null;
/*     */     }
/*     */ 
/* 334 */     if (debug) {
/* 335 */       dPrint("found jar resource=" + serviceId + " using ClassLoader: " + cl);
/*     */     }
/*     */     BufferedReader rd;
/*     */     try
/*     */     {
/* 340 */       rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */     }
/*     */     catch (UnsupportedEncodingException e) {
/* 343 */       rd = new BufferedReader(new InputStreamReader(is));
/*     */     }
/*     */ 
/* 346 */     String factoryClassName = null;
/*     */     try
/*     */     {
/* 350 */       factoryClassName = rd.readLine();
/* 351 */       rd.close();
/*     */     }
/*     */     catch (IOException x) {
/* 354 */       return null;
/*     */     }
/*     */ 
/* 357 */     if ((factoryClassName != null) && (!"".equals(factoryClassName))) {
/* 358 */       dPrint("found in resource, value=" + factoryClassName);
/*     */ 
/* 364 */       return newInstance(factoryClassName, cl, false, useBSClsLoader, true);
/*     */     }
/*     */ 
/* 368 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  75 */       String val = ss.getSystemProperty("jaxp.debug");
/*     */ 
/*  77 */       debug = (val != null) && (!"false".equals(val));
/*     */     }
/*     */     catch (SecurityException se) {
/*  80 */       debug = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ConfigurationError extends Error
/*     */   {
/*     */     private Exception exception;
/*     */ 
/*     */     ConfigurationError(String msg, Exception x)
/*     */     {
/* 379 */       super();
/* 380 */       this.exception = x;
/*     */     }
/*     */ 
/*     */     Exception getException() {
/* 384 */       return this.exception;
/*     */     }
/*     */ 
/*     */     public Throwable getCause()
/*     */     {
/* 391 */       return this.exception;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.FactoryFinder
 * JD-Core Version:    0.6.2
 */