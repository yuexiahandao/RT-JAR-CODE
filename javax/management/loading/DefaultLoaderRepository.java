/*     */ package javax.management.loading;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.ArrayList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerFactory;
/*     */ 
/*     */ @Deprecated
/*     */ public class DefaultLoaderRepository
/*     */ {
/*     */   public static Class<?> loadClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  74 */     JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, DefaultLoaderRepository.class.getName(), "loadClass", paramString);
/*     */ 
/*  77 */     return load(null, paramString);
/*     */   }
/*     */ 
/*     */   public static Class<?> loadClassWithout(ClassLoader paramClassLoader, String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/*  99 */     JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, DefaultLoaderRepository.class.getName(), "loadClassWithout", paramString);
/*     */ 
/* 102 */     return load(paramClassLoader, paramString);
/*     */   }
/*     */ 
/*     */   private static Class<?> load(ClassLoader paramClassLoader, String paramString) throws ClassNotFoundException
/*     */   {
/* 107 */     ArrayList localArrayList = MBeanServerFactory.findMBeanServer(null);
/*     */ 
/* 109 */     for (MBeanServer localMBeanServer : localArrayList) {
/* 110 */       ClassLoaderRepository localClassLoaderRepository = localMBeanServer.getClassLoaderRepository();
/*     */       try {
/* 112 */         return localClassLoaderRepository.loadClassWithout(paramClassLoader, paramString);
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {
/*     */       }
/*     */     }
/* 117 */     throw new ClassNotFoundException(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.loading.DefaultLoaderRepository
 * JD-Core Version:    0.6.2
 */