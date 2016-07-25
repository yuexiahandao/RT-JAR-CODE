/*     */ package com.sun.org.apache.xerces.internal.utils;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public final class ObjectFactory
/*     */ {
/*     */   private static final String JAXP_INTERNAL = "com.sun.org.apache";
/*     */   private static final String STAX_INTERNAL = "com.sun.xml.internal";
/*     */   private static final String DEFAULT_PROPERTIES_FILENAME = "xerces.properties";
/*  58 */   private static final boolean DEBUG = isDebugEnabled();
/*     */   private static final int DEFAULT_LINE_LENGTH = 80;
/*  70 */   private static Properties fXercesProperties = null;
/*     */ 
/*  77 */   private static long fLastModified = -1L;
/*     */ 
/*     */   public static Object createObject(String factoryId, String fallbackClassName)
/*     */     throws ConfigurationError
/*     */   {
/* 103 */     return createObject(factoryId, null, fallbackClassName);
/*     */   }
/*     */ 
/*     */   public static Object createObject(String factoryId, String propertiesFilename, String fallbackClassName)
/*     */     throws ConfigurationError
/*     */   {
/* 133 */     if (DEBUG) debugPrintln("debug is on");
/*     */ 
/* 135 */     ClassLoader cl = findClassLoader();
/*     */     try
/*     */     {
/* 139 */       String systemProp = SecuritySupport.getSystemProperty(factoryId);
/* 140 */       if ((systemProp != null) && (systemProp.length() > 0)) {
/* 141 */         if (DEBUG) debugPrintln("found system property, value=" + systemProp);
/* 142 */         return newInstance(systemProp, cl, true);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/*     */     }
/*     */ 
/* 152 */     if (fallbackClassName == null) {
/* 153 */       throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/* 157 */     if (DEBUG) debugPrintln("using fallback, value=" + fallbackClassName);
/* 158 */     return newInstance(fallbackClassName, cl, true);
/*     */   }
/*     */ 
/*     */   private static boolean isDebugEnabled()
/*     */   {
/*     */     try
/*     */     {
/* 169 */       String val = SecuritySupport.getSystemProperty("xerces.debug");
/*     */ 
/* 171 */       return (val != null) && (!"false".equals(val));
/*     */     } catch (SecurityException se) {
/*     */     }
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */   private static void debugPrintln(String msg)
/*     */   {
/* 179 */     if (DEBUG)
/* 180 */       System.err.println("XERCES: " + msg);
/*     */   }
/*     */ 
/*     */   public static ClassLoader findClassLoader()
/*     */     throws ConfigurationError
/*     */   {
/* 191 */     if (System.getSecurityManager() != null)
/*     */     {
/* 193 */       return null;
/*     */     }
/*     */ 
/* 197 */     ClassLoader context = SecuritySupport.getContextClassLoader();
/* 198 */     ClassLoader system = SecuritySupport.getSystemClassLoader();
/*     */ 
/* 200 */     ClassLoader chain = system;
/*     */     while (true) {
/* 202 */       if (context == chain)
/*     */       {
/* 211 */         ClassLoader current = ObjectFactory.class.getClassLoader();
/*     */ 
/* 213 */         chain = system;
/*     */         while (true) {
/* 215 */           if (current == chain)
/*     */           {
/* 218 */             return system;
/*     */           }
/* 220 */           if (chain == null) {
/*     */             break;
/*     */           }
/* 223 */           chain = SecuritySupport.getParentClassLoader(chain);
/*     */         }
/*     */ 
/* 228 */         return current;
/*     */       }
/*     */ 
/* 231 */       if (chain == null)
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 238 */       chain = SecuritySupport.getParentClassLoader(chain);
/*     */     }
/*     */ 
/* 243 */     return context;
/*     */   }
/*     */ 
/*     */   public static Object newInstance(String className, boolean doFallback)
/*     */     throws ConfigurationError
/*     */   {
/* 253 */     if (System.getSecurityManager() != null) {
/* 254 */       return newInstance(className, null, doFallback);
/*     */     }
/* 256 */     return newInstance(className, findClassLoader(), doFallback);
/*     */   }
/*     */ 
/*     */   public static Object newInstance(String className, ClassLoader cl, boolean doFallback)
/*     */     throws ConfigurationError
/*     */   {
/*     */     try
/*     */     {
/* 270 */       Class providerClass = findProviderClass(className, cl, doFallback);
/* 271 */       Object instance = providerClass.newInstance();
/* 272 */       if (DEBUG) debugPrintln("created new instance of " + providerClass + " using ClassLoader: " + cl);
/*     */ 
/* 274 */       return instance;
/*     */     } catch (ClassNotFoundException x) {
/* 276 */       throw new ConfigurationError("Provider " + className + " not found", x);
/*     */     }
/*     */     catch (Exception x) {
/* 279 */       throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Class findProviderClass(String className, boolean doFallback)
/*     */     throws ClassNotFoundException, ConfigurationError
/*     */   {
/* 292 */     return findProviderClass(className, findClassLoader(), doFallback);
/*     */   }
/*     */ 
/*     */   public static Class findProviderClass(String className, ClassLoader cl, boolean doFallback)
/*     */     throws ClassNotFoundException, ConfigurationError
/*     */   {
/* 304 */     SecurityManager security = System.getSecurityManager();
/* 305 */     if (security != null)
/* 306 */       if ((className.startsWith("com.sun.org.apache")) || (className.startsWith("com.sun.xml.internal")))
/*     */       {
/* 308 */         cl = null;
/*     */       } else {
/* 310 */         int lastDot = className.lastIndexOf(".");
/* 311 */         String packageName = className;
/* 312 */         if (lastDot != -1) packageName = className.substring(0, lastDot);
/* 313 */         security.checkPackageAccess(packageName);
/*     */       }
/*     */     Class providerClass;
/* 317 */     if (cl == null)
/*     */     {
/* 319 */       providerClass = Class.forName(className, false, ObjectFactory.class.getClassLoader());
/*     */     }
/*     */     else try {
/* 322 */         providerClass = cl.loadClass(className);
/*     */       }
/*     */       catch (ClassNotFoundException x)
/*     */       {
/*     */         Class providerClass;
/*     */         Class providerClass;
/* 324 */         if (doFallback)
/*     */         {
/* 326 */           ClassLoader current = ObjectFactory.class.getClassLoader();
/* 327 */           if (current == null) {
/* 328 */             providerClass = Class.forName(className);
/*     */           }
/*     */           else
/*     */           {
/*     */             Class providerClass;
/* 329 */             if (cl != current) {
/* 330 */               cl = current;
/* 331 */               providerClass = cl.loadClass(className);
/*     */             } else {
/* 333 */               throw x;
/*     */             }
/*     */           }
/*     */         } else { throw x; }
/*     */ 
/*     */       }
/*     */     Class providerClass;
/* 341 */     return providerClass;
/*     */   }
/*     */ 
/*     */   private static Object findJarServiceProvider(String factoryId)
/*     */     throws ConfigurationError
/*     */   {
/* 352 */     String serviceId = "META-INF/services/" + factoryId;
/* 353 */     InputStream is = null;
/*     */ 
/* 356 */     ClassLoader cl = findClassLoader();
/*     */ 
/* 358 */     is = SecuritySupport.getResourceAsStream(cl, serviceId);
/*     */ 
/* 361 */     if (is == null) {
/* 362 */       ClassLoader current = ObjectFactory.class.getClassLoader();
/* 363 */       if (cl != current) {
/* 364 */         cl = current;
/* 365 */         is = SecuritySupport.getResourceAsStream(cl, serviceId);
/*     */       }
/*     */     }
/*     */ 
/* 369 */     if (is == null)
/*     */     {
/* 371 */       return null;
/*     */     }
/*     */ 
/* 374 */     if (DEBUG) debugPrintln("found jar resource=" + serviceId + " using ClassLoader: " + cl);
/*     */ 
/*     */     BufferedReader rd;
/*     */     try
/*     */     {
/* 395 */       rd = new BufferedReader(new InputStreamReader(is, "UTF-8"), 80);
/*     */     } catch (UnsupportedEncodingException e) {
/* 397 */       rd = new BufferedReader(new InputStreamReader(is), 80);
/*     */     }
/*     */ 
/* 400 */     String factoryClassName = null;
/*     */     try
/*     */     {
/* 404 */       factoryClassName = rd.readLine();
/*     */     }
/*     */     catch (IOException x) {
/* 407 */       return null;
/*     */     }
/*     */     finally
/*     */     {
/*     */       try {
/* 412 */         rd.close();
/*     */       }
/*     */       catch (IOException exc)
/*     */       {
/*     */       }
/*     */     }
/* 418 */     if ((factoryClassName != null) && (!"".equals(factoryClassName)))
/*     */     {
/* 420 */       if (DEBUG) debugPrintln("found in resource, value=" + factoryClassName);
/*     */ 
/* 427 */       return newInstance(factoryClassName, cl, false);
/*     */     }
/*     */ 
/* 431 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.utils.ObjectFactory
 * JD-Core Version:    0.6.2
 */