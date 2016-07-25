/*     */ package com.sun.org.apache.xalan.internal.utils;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class ObjectFactory
/*     */ {
/*     */   private static final String JAXP_INTERNAL = "com.sun.org.apache";
/*     */   private static final String STAX_INTERNAL = "com.sun.xml.internal";
/*     */   private static final String DEFAULT_PROPERTIES_FILENAME = "xalan.properties";
/*     */   private static final String SERVICES_PATH = "META-INF/services/";
/*     */   private static final boolean DEBUG = false;
/*  74 */   private static Properties fXalanProperties = null;
/*     */ 
/*  81 */   private static long fLastModified = -1L;
/*     */ 
/*     */   public static Object createObject(String factoryId, String fallbackClassName)
/*     */     throws ConfigurationError
/*     */   {
/* 107 */     return createObject(factoryId, null, fallbackClassName);
/*     */   }
/*     */ 
/*     */   static Object createObject(String factoryId, String propertiesFilename, String fallbackClassName)
/*     */     throws ConfigurationError
/*     */   {
/* 137 */     Class factoryClass = lookUpFactoryClass(factoryId, propertiesFilename, fallbackClassName);
/*     */ 
/* 141 */     if (factoryClass == null) {
/* 142 */       throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 147 */       return factoryClass.newInstance();
/*     */     }
/*     */     catch (Exception x)
/*     */     {
/* 151 */       throw new ConfigurationError("Provider for factory " + factoryId + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Class lookUpFactoryClass(String factoryId)
/*     */     throws ConfigurationError
/*     */   {
/* 182 */     return lookUpFactoryClass(factoryId, null, null);
/*     */   }
/*     */ 
/*     */   public static Class lookUpFactoryClass(String factoryId, String propertiesFilename, String fallbackClassName)
/*     */     throws ConfigurationError
/*     */   {
/* 212 */     String factoryClassName = lookUpFactoryClassName(factoryId, propertiesFilename, fallbackClassName);
/*     */ 
/* 215 */     ClassLoader cl = findClassLoader();
/*     */ 
/* 217 */     if (factoryClassName == null) {
/* 218 */       factoryClassName = fallbackClassName;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 223 */       return findProviderClass(factoryClassName, cl, true);
/*     */     }
/*     */     catch (ClassNotFoundException x)
/*     */     {
/* 230 */       throw new ConfigurationError("Provider " + factoryClassName + " not found", x);
/*     */     }
/*     */     catch (Exception x) {
/* 233 */       throw new ConfigurationError("Provider " + factoryClassName + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static String lookUpFactoryClassName(String factoryId, String propertiesFilename, String fallbackClassName)
/*     */   {
/*     */     try
/*     */     {
/* 267 */       String systemProp = SecuritySupport.getSystemProperty(factoryId);
/* 268 */       if (systemProp != null)
/*     */       {
/* 270 */         return systemProp;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/*     */     }
/*     */ 
/* 278 */     String factoryClassName = null;
/*     */ 
/* 281 */     if (propertiesFilename == null) {
/* 282 */       File propertiesFile = null;
/* 283 */       boolean propertiesFileExists = false;
/*     */       try {
/* 285 */         String javah = SecuritySupport.getSystemProperty("java.home");
/* 286 */         propertiesFilename = javah + File.separator + "lib" + File.separator + "xalan.properties";
/*     */ 
/* 288 */         propertiesFile = new File(propertiesFilename);
/* 289 */         propertiesFileExists = SecuritySupport.getFileExists(propertiesFile);
/*     */       }
/*     */       catch (SecurityException e) {
/* 292 */         fLastModified = -1L;
/* 293 */         fXalanProperties = null;
/*     */       }
/*     */ 
/* 296 */       synchronized (ObjectFactory.class) {
/* 297 */         boolean loadProperties = false;
/* 298 */         FileInputStream fis = null;
/*     */         try
/*     */         {
/* 301 */           if (fLastModified >= 0L) {
/* 302 */             if ((propertiesFileExists) && (fLastModified < (ObjectFactory.fLastModified = SecuritySupport.getLastModified(propertiesFile))))
/*     */             {
/* 304 */               loadProperties = true;
/*     */             }
/* 307 */             else if (!propertiesFileExists) {
/* 308 */               fLastModified = -1L;
/* 309 */               fXalanProperties = null;
/*     */             }
/*     */ 
/*     */           }
/* 314 */           else if (propertiesFileExists) {
/* 315 */             loadProperties = true;
/* 316 */             fLastModified = SecuritySupport.getLastModified(propertiesFile);
/*     */           }
/*     */ 
/* 319 */           if (loadProperties)
/*     */           {
/* 322 */             fXalanProperties = new Properties();
/* 323 */             fis = SecuritySupport.getFileInputStream(propertiesFile);
/* 324 */             fXalanProperties.load(fis);
/*     */           }
/*     */         } catch (Exception x) {
/* 327 */           fXalanProperties = null;
/* 328 */           fLastModified = -1L;
/*     */         }
/*     */         finally
/*     */         {
/* 335 */           if (fis != null)
/*     */             try {
/* 337 */               fis.close();
/*     */             }
/*     */             catch (IOException exc)
/*     */             {
/*     */             }
/*     */         }
/*     */       }
/* 344 */       if (fXalanProperties != null)
/* 345 */         factoryClassName = fXalanProperties.getProperty(factoryId);
/*     */     }
/*     */     else {
/* 348 */       FileInputStream fis = null;
/*     */       try {
/* 350 */         fis = SecuritySupport.getFileInputStream(new File(propertiesFilename));
/* 351 */         Properties props = new Properties();
/* 352 */         props.load(fis);
/* 353 */         factoryClassName = props.getProperty(factoryId);
/*     */       }
/*     */       catch (Exception x)
/*     */       {
/*     */       }
/*     */       finally
/*     */       {
/* 361 */         if (fis != null)
/*     */           try {
/* 363 */             fis.close();
/*     */           }
/*     */           catch (IOException exc)
/*     */           {
/*     */           }
/*     */       }
/*     */     }
/* 370 */     if (factoryClassName != null)
/*     */     {
/* 373 */       return factoryClassName;
/*     */     }
/*     */ 
/* 377 */     return findJarServiceProviderName(factoryId);
/*     */   }
/*     */ 
/*     */   private static void debugPrintln(String msg)
/*     */   {
/*     */   }
/*     */ 
/*     */   public static ClassLoader findClassLoader()
/*     */     throws ConfigurationError
/*     */   {
/* 398 */     if (System.getSecurityManager() != null)
/*     */     {
/* 400 */       return null;
/*     */     }
/*     */ 
/* 405 */     ClassLoader context = SecuritySupport.getContextClassLoader();
/* 406 */     ClassLoader system = SecuritySupport.getSystemClassLoader();
/*     */ 
/* 408 */     ClassLoader chain = system;
/*     */     while (true) {
/* 410 */       if (context == chain)
/*     */       {
/* 419 */         ClassLoader current = ObjectFactory.class.getClassLoader();
/*     */ 
/* 421 */         chain = system;
/*     */         while (true) {
/* 423 */           if (current == chain)
/*     */           {
/* 426 */             return system;
/*     */           }
/* 428 */           if (chain == null) {
/*     */             break;
/*     */           }
/* 431 */           chain = SecuritySupport.getParentClassLoader(chain);
/*     */         }
/*     */ 
/* 436 */         return current;
/*     */       }
/*     */ 
/* 439 */       if (chain == null)
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 446 */       chain = SecuritySupport.getParentClassLoader(chain);
/*     */     }
/*     */ 
/* 451 */     return context;
/*     */   }
/*     */ 
/*     */   public static Object newInstance(String className, boolean doFallback)
/*     */     throws ConfigurationError
/*     */   {
/* 461 */     if (System.getSecurityManager() != null) {
/* 462 */       return newInstance(className, null, doFallback);
/*     */     }
/* 464 */     return newInstance(className, findClassLoader(), doFallback);
/*     */   }
/*     */ 
/*     */   static Object newInstance(String className, ClassLoader cl, boolean doFallback)
/*     */     throws ConfigurationError
/*     */   {
/*     */     try
/*     */     {
/* 478 */       Class providerClass = findProviderClass(className, cl, doFallback);
/* 479 */       return providerClass.newInstance();
/*     */     }
/*     */     catch (ClassNotFoundException x)
/*     */     {
/* 484 */       throw new ConfigurationError("Provider " + className + " not found", x);
/*     */     }
/*     */     catch (Exception x) {
/* 487 */       throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Class findProviderClass(String className, boolean doFallback)
/*     */     throws ClassNotFoundException, ConfigurationError
/*     */   {
/* 500 */     return findProviderClass(className, findClassLoader(), doFallback);
/*     */   }
/*     */ 
/*     */   static Class findProviderClass(String className, ClassLoader cl, boolean doFallback)
/*     */     throws ClassNotFoundException, ConfigurationError
/*     */   {
/* 513 */     SecurityManager security = System.getSecurityManager();
/*     */     try {
/* 515 */       if (security != null)
/* 516 */         if ((className.startsWith("com.sun.org.apache")) || (className.startsWith("com.sun.xml.internal")))
/*     */         {
/* 518 */           cl = null;
/*     */         } else {
/* 520 */           int lastDot = className.lastIndexOf(".");
/* 521 */           String packageName = className;
/* 522 */           if (lastDot != -1) packageName = className.substring(0, lastDot);
/* 523 */           security.checkPackageAccess(packageName);
/*     */         }
/*     */     }
/*     */     catch (SecurityException e) {
/* 527 */       throw e;
/*     */     }
/*     */     Class providerClass;
/* 531 */     if (cl == null)
/* 532 */       providerClass = Class.forName(className, false, ObjectFactory.class.getClassLoader());
/*     */     else
/*     */       try {
/* 535 */         providerClass = cl.loadClass(className);
/*     */       }
/*     */       catch (ClassNotFoundException x)
/*     */       {
/*     */         Class providerClass;
/*     */         Class providerClass;
/* 537 */         if (doFallback)
/*     */         {
/* 539 */           ClassLoader current = ObjectFactory.class.getClassLoader();
/* 540 */           if (current == null) {
/* 541 */             providerClass = Class.forName(className);
/*     */           }
/*     */           else
/*     */           {
/*     */             Class providerClass;
/* 542 */             if (cl != current) {
/* 543 */               cl = current;
/* 544 */               providerClass = cl.loadClass(className);
/*     */             } else {
/* 546 */               throw x;
/*     */             }
/*     */           }
/*     */         } else { throw x; }
/*     */ 
/*     */       }
/*     */     Class providerClass;
/* 554 */     return providerClass;
/*     */   }
/*     */ 
/*     */   private static String findJarServiceProviderName(String factoryId)
/*     */   {
/* 564 */     String serviceId = "META-INF/services/" + factoryId;
/* 565 */     InputStream is = null;
/*     */ 
/* 568 */     ClassLoader cl = findClassLoader();
/*     */ 
/* 570 */     is = SecuritySupport.getResourceAsStream(cl, serviceId);
/*     */ 
/* 573 */     if (is == null) {
/* 574 */       ClassLoader current = ObjectFactory.class.getClassLoader();
/* 575 */       if (cl != current) {
/* 576 */         cl = current;
/* 577 */         is = SecuritySupport.getResourceAsStream(cl, serviceId);
/*     */       }
/*     */     }
/*     */ 
/* 581 */     if (is == null)
/*     */     {
/* 583 */       return null;
/*     */     }
/*     */ 
/*     */     BufferedReader rd;
/*     */     try
/*     */     {
/* 607 */       rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */     } catch (UnsupportedEncodingException e) {
/* 609 */       rd = new BufferedReader(new InputStreamReader(is));
/*     */     }
/*     */ 
/* 612 */     String factoryClassName = null;
/*     */     try
/*     */     {
/* 616 */       factoryClassName = rd.readLine();
/*     */     }
/*     */     catch (IOException x) {
/* 619 */       return null;
/*     */     }
/*     */     finally
/*     */     {
/*     */       try {
/* 624 */         rd.close();
/*     */       }
/*     */       catch (IOException exc)
/*     */       {
/*     */       }
/*     */     }
/* 630 */     if ((factoryClassName != null) && (!"".equals(factoryClassName)))
/*     */     {
/* 639 */       return factoryClassName;
/*     */     }
/*     */ 
/* 643 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.utils.ObjectFactory
 * JD-Core Version:    0.6.2
 */