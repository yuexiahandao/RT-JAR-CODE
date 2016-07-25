/*     */ package javax.management.remote;
/*     */ 
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.IOException;
/*     */ import java.net.MalformedURLException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.management.MBeanServer;
/*     */ 
/*     */ public class JMXConnectorServerFactory
/*     */ {
/*     */   public static final String DEFAULT_CLASS_LOADER = "jmx.remote.default.class.loader";
/*     */   public static final String DEFAULT_CLASS_LOADER_NAME = "jmx.remote.default.class.loader.name";
/*     */   public static final String PROTOCOL_PROVIDER_PACKAGES = "jmx.remote.protocol.provider.pkgs";
/*     */   public static final String PROTOCOL_PROVIDER_CLASS_LOADER = "jmx.remote.protocol.provider.class.loader";
/*     */   private static final String PROTOCOL_PROVIDER_DEFAULT_PACKAGE = "com.sun.jmx.remote.protocol";
/* 200 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "JMXConnectorServerFactory");
/*     */ 
/*     */   private static JMXConnectorServer getConnectorServerAsService(ClassLoader paramClassLoader, JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap, MBeanServer paramMBeanServer)
/*     */     throws IOException
/*     */   {
/* 213 */     Iterator localIterator = JMXConnectorFactory.getProviderIterator(JMXConnectorServerProvider.class, paramClassLoader);
/*     */ 
/* 217 */     IOException localIOException = null;
/* 218 */     while (localIterator.hasNext()) {
/*     */       try {
/* 220 */         return ((JMXConnectorServerProvider)localIterator.next()).newJMXConnectorServer(paramJMXServiceURL, paramMap, paramMBeanServer);
/*     */       } catch (JMXProviderException localJMXProviderException) {
/* 222 */         throw localJMXProviderException;
/*     */       } catch (Exception localException) {
/* 224 */         if (logger.traceOn()) {
/* 225 */           logger.trace("getConnectorAsService", "URL[" + paramJMXServiceURL + "] Service provider exception: " + localException);
/*     */         }
/*     */ 
/* 228 */         if ((!(localException instanceof MalformedURLException)) && 
/* 229 */           (localIOException == null)) {
/* 230 */           if ((localException instanceof IOException))
/* 231 */             localIOException = (IOException)localException;
/*     */           else {
/* 233 */             localIOException = (IOException)EnvHelp.initCause(new IOException(localException.getMessage()), localException);
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 241 */     if (localIOException == null) {
/* 242 */       return null;
/*     */     }
/* 244 */     throw localIOException;
/*     */   }
/*     */ 
/*     */   public static JMXConnectorServer newJMXConnectorServer(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap, MBeanServer paramMBeanServer)
/*     */     throws IOException
/*     */   {
/* 290 */     if (paramMap == null) {
/* 291 */       localObject1 = new HashMap();
/*     */     } else {
/* 293 */       EnvHelp.checkAttributes(paramMap);
/* 294 */       localObject1 = new HashMap(paramMap);
/*     */     }
/*     */ 
/* 297 */     JMXConnectorServerProvider localJMXConnectorServerProvider1 = JMXConnectorServerProvider.class;
/*     */ 
/* 299 */     ClassLoader localClassLoader = JMXConnectorFactory.resolveClassLoader((Map)localObject1);
/*     */ 
/* 301 */     String str = paramJMXServiceURL.getProtocol();
/*     */ 
/* 304 */     JMXConnectorServerProvider localJMXConnectorServerProvider2 = (JMXConnectorServerProvider)JMXConnectorFactory.getProvider(paramJMXServiceURL, (Map)localObject1, "ServerProvider", localJMXConnectorServerProvider1, localClassLoader);
/*     */ 
/* 311 */     Object localObject2 = null;
/* 312 */     if (localJMXConnectorServerProvider2 == null)
/*     */     {
/* 317 */       if (localClassLoader != null) {
/*     */         try {
/* 319 */           JMXConnectorServer localJMXConnectorServer = getConnectorServerAsService(localClassLoader, paramJMXServiceURL, (Map)localObject1, paramMBeanServer);
/*     */ 
/* 324 */           if (localJMXConnectorServer != null)
/* 325 */             return localJMXConnectorServer;
/*     */         } catch (JMXProviderException localJMXProviderException) {
/* 327 */           throw localJMXProviderException;
/*     */         } catch (IOException localIOException) {
/* 329 */           localObject2 = localIOException;
/*     */         }
/*     */       }
/* 332 */       localJMXConnectorServerProvider2 = (JMXConnectorServerProvider)JMXConnectorFactory.getProvider(str, "com.sun.jmx.remote.protocol", JMXConnectorFactory.class.getClassLoader(), "ServerProvider", localJMXConnectorServerProvider1);
/*     */     }
/*     */ 
/* 341 */     if (localJMXConnectorServerProvider2 == null) {
/* 342 */       MalformedURLException localMalformedURLException = new MalformedURLException("Unsupported protocol: " + str);
/*     */ 
/* 344 */       if (localObject2 == null) {
/* 345 */         throw localMalformedURLException;
/*     */       }
/* 347 */       throw ((MalformedURLException)EnvHelp.initCause(localMalformedURLException, localObject2));
/*     */     }
/*     */ 
/* 351 */     Object localObject1 = Collections.unmodifiableMap((Map)localObject1);
/*     */ 
/* 353 */     return localJMXConnectorServerProvider2.newJMXConnectorServer(paramJMXServiceURL, (Map)localObject1, paramMBeanServer);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXConnectorServerFactory
 * JD-Core Version:    0.6.2
 */