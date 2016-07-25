/*     */ package javax.xml.ws.spi;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Properties;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ class FactoryFinder
/*     */ {
/*     */   private static Object newInstance(String className, ClassLoader classLoader)
/*     */   {
/*     */     try
/*     */     {
/*  50 */       Class spiClass = safeLoadClass(className, classLoader);
/*  51 */       return spiClass.newInstance();
/*     */     } catch (ClassNotFoundException x) {
/*  53 */       throw new WebServiceException("Provider " + className + " not found", x);
/*     */     }
/*     */     catch (Exception x) {
/*  56 */       throw new WebServiceException("Provider " + className + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId, String fallbackClassName)
/*     */   {
/*     */     ClassLoader classLoader;
/*     */     try
/*     */     {
/*  86 */       classLoader = Thread.currentThread().getContextClassLoader();
/*     */     } catch (Exception x) {
/*  88 */       throw new WebServiceException(x.toString(), x);
/*     */     }
/*     */ 
/*  91 */     String serviceId = "META-INF/services/" + factoryId;
/*     */     try
/*     */     {
/*  94 */       InputStream is = null;
/*  95 */       if (classLoader == null)
/*  96 */         is = ClassLoader.getSystemResourceAsStream(serviceId);
/*     */       else {
/*  98 */         is = classLoader.getResourceAsStream(serviceId);
/*     */       }
/*     */ 
/* 101 */       if (is != null) {
/* 102 */         BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */ 
/* 105 */         String factoryClassName = rd.readLine();
/* 106 */         rd.close();
/*     */ 
/* 108 */         if ((factoryClassName != null) && (!"".equals(factoryClassName)))
/*     */         {
/* 110 */           return newInstance(factoryClassName, classLoader);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */     }
/*     */     try
/*     */     {
/* 119 */       String javah = System.getProperty("java.home");
/* 120 */       String configFile = javah + File.separator + "lib" + File.separator + "jaxws.properties";
/*     */ 
/* 122 */       File f = new File(configFile);
/* 123 */       if (f.exists()) {
/* 124 */         Properties props = new Properties();
/* 125 */         props.load(new FileInputStream(f));
/* 126 */         String factoryClassName = props.getProperty(factoryId);
/* 127 */         return newInstance(factoryClassName, classLoader);
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */     }
/*     */     try
/*     */     {
/* 135 */       String systemProp = System.getProperty(factoryId);
/*     */ 
/* 137 */       if (systemProp != null)
/* 138 */         return newInstance(systemProp, classLoader);
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/*     */     }
/* 143 */     if (fallbackClassName == null) {
/* 144 */       throw new WebServiceException("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/* 148 */     return newInstance(fallbackClassName, classLoader);
/*     */   }
/*     */ 
/*     */   private static Class safeLoadClass(String className, ClassLoader classLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 158 */       SecurityManager s = System.getSecurityManager();
/* 159 */       if (s != null) {
/* 160 */         int i = className.lastIndexOf('.');
/* 161 */         if (i != -1) {
/* 162 */           s.checkPackageAccess(className.substring(0, i));
/*     */         }
/*     */       }
/*     */ 
/* 166 */       if (classLoader == null) {
/* 167 */         return Class.forName(className);
/*     */       }
/* 169 */       return classLoader.loadClass(className);
/*     */     }
/*     */     catch (SecurityException se) {
/* 172 */       if ("com.sun.xml.internal.ws.spi.ProviderImpl".equals(className))
/* 173 */         return Class.forName(className);
/* 174 */       throw se;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.spi.FactoryFinder
 * JD-Core Version:    0.6.2
 */