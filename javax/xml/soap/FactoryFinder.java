/*     */ package javax.xml.soap;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Properties;
/*     */ 
/*     */ class FactoryFinder
/*     */ {
/*     */   private static Object newInstance(String className, ClassLoader classLoader)
/*     */     throws SOAPException
/*     */   {
/*     */     try
/*     */     {
/*  46 */       Class spiClass = safeLoadClass(className, classLoader);
/*  47 */       return spiClass.newInstance();
/*     */     }
/*     */     catch (ClassNotFoundException x) {
/*  50 */       throw new SOAPException("Provider " + className + " not found", x);
/*     */     } catch (Exception x) {
/*  52 */       throw new SOAPException("Provider " + className + " could not be instantiated: " + x, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId)
/*     */     throws SOAPException
/*     */   {
/*  72 */     return find(factoryId, null, false);
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId, String fallbackClassName)
/*     */     throws SOAPException
/*     */   {
/*  98 */     return find(factoryId, fallbackClassName, true);
/*     */   }
/*     */ 
/*     */   static Object find(String factoryId, String defaultClassName, boolean tryFallback)
/*     */     throws SOAPException
/*     */   {
/*     */     ClassLoader classLoader;
/*     */     try
/*     */     {
/* 129 */       classLoader = Thread.currentThread().getContextClassLoader();
/*     */     } catch (Exception x) {
/* 131 */       throw new SOAPException(x.toString(), x);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 136 */       String systemProp = System.getProperty(factoryId);
/*     */ 
/* 138 */       if (systemProp != null)
/* 139 */         return newInstance(systemProp, classLoader);
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/*     */     }
/*     */     try
/*     */     {
/* 146 */       String javah = System.getProperty("java.home");
/* 147 */       String configFile = javah + File.separator + "lib" + File.separator + "jaxm.properties";
/*     */ 
/* 149 */       File f = new File(configFile);
/* 150 */       if (f.exists()) {
/* 151 */         Properties props = new Properties();
/* 152 */         props.load(new FileInputStream(f));
/* 153 */         String factoryClassName = props.getProperty(factoryId);
/* 154 */         return newInstance(factoryClassName, classLoader);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/*     */     }
/* 159 */     String serviceId = "META-INF/services/" + factoryId;
/*     */     try
/*     */     {
/* 162 */       InputStream is = null;
/* 163 */       if (classLoader == null)
/* 164 */         is = ClassLoader.getSystemResourceAsStream(serviceId);
/*     */       else {
/* 166 */         is = classLoader.getResourceAsStream(serviceId);
/*     */       }
/*     */ 
/* 169 */       if (is != null) {
/* 170 */         BufferedReader rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
/*     */ 
/* 173 */         String factoryClassName = rd.readLine();
/* 174 */         rd.close();
/*     */ 
/* 176 */         if ((factoryClassName != null) && (!"".equals(factoryClassName)))
/*     */         {
/* 178 */           return newInstance(factoryClassName, classLoader);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*     */     }
/* 185 */     if (!tryFallback) {
/* 186 */       return null;
/*     */     }
/*     */ 
/* 190 */     if (defaultClassName == null) {
/* 191 */       throw new SOAPException("Provider for " + factoryId + " cannot be found", null);
/*     */     }
/*     */ 
/* 194 */     return newInstance(defaultClassName, classLoader);
/*     */   }
/*     */ 
/*     */   private static Class safeLoadClass(String className, ClassLoader classLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 208 */       SecurityManager s = System.getSecurityManager();
/* 209 */       if (s != null) {
/* 210 */         int i = className.lastIndexOf('.');
/* 211 */         if (i != -1) {
/* 212 */           s.checkPackageAccess(className.substring(0, i));
/*     */         }
/*     */       }
/*     */ 
/* 216 */       if (classLoader == null) {
/* 217 */         return Class.forName(className);
/*     */       }
/* 219 */       return classLoader.loadClass(className);
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/* 223 */       if (isDefaultImplementation(className)) {
/* 224 */         return Class.forName(className);
/*     */       }
/* 226 */       throw se;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isDefaultImplementation(String className) {
/* 231 */     return ("com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl".equals(className)) || ("com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl".equals(className)) || ("com.sun.xml.internal.messaging.saaj.client.p2p.HttpSOAPConnectionFactory".equals(className)) || ("com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl".equals(className));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.FactoryFinder
 * JD-Core Version:    0.6.2
 */