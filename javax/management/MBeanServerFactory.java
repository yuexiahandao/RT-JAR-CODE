/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.loading.ClassLoaderRepository;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class MBeanServerFactory
/*     */ {
/* 101 */   private static MBeanServerBuilder builder = null;
/*     */ 
/* 432 */   private static final ArrayList<MBeanServer> mBeanServerList = new ArrayList();
/*     */ 
/*     */   public static void releaseMBeanServer(MBeanServer paramMBeanServer)
/*     */   {
/* 152 */     checkPermission("releaseMBeanServer");
/*     */ 
/* 154 */     removeMBeanServer(paramMBeanServer);
/*     */   }
/*     */ 
/*     */   public static MBeanServer createMBeanServer()
/*     */   {
/* 192 */     return createMBeanServer(null);
/*     */   }
/*     */ 
/*     */   public static MBeanServer createMBeanServer(String paramString)
/*     */   {
/* 229 */     checkPermission("createMBeanServer");
/*     */ 
/* 231 */     MBeanServer localMBeanServer = newMBeanServer(paramString);
/* 232 */     addMBeanServer(localMBeanServer);
/* 233 */     return localMBeanServer;
/*     */   }
/*     */ 
/*     */   public static MBeanServer newMBeanServer()
/*     */   {
/* 273 */     return newMBeanServer(null);
/*     */   }
/*     */ 
/*     */   public static MBeanServer newMBeanServer(String paramString)
/*     */   {
/* 312 */     checkPermission("newMBeanServer");
/*     */ 
/* 316 */     MBeanServerBuilder localMBeanServerBuilder = getNewMBeanServerBuilder();
/*     */ 
/* 319 */     synchronized (localMBeanServerBuilder) {
/* 320 */       MBeanServerDelegate localMBeanServerDelegate = localMBeanServerBuilder.newMBeanServerDelegate();
/*     */ 
/* 322 */       if (localMBeanServerDelegate == null)
/*     */       {
/* 326 */         throw new JMRuntimeException("MBeanServerBuilder.newMBeanServerDelegate() returned null");
/*     */       }
/* 328 */       MBeanServer localMBeanServer = localMBeanServerBuilder.newMBeanServer(paramString, null, localMBeanServerDelegate);
/*     */ 
/* 330 */       if (localMBeanServer == null)
/*     */       {
/* 333 */         throw new JMRuntimeException("MBeanServerBuilder.newMBeanServer() returned null");
/*     */       }
/* 335 */       return localMBeanServer;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static synchronized ArrayList<MBeanServer> findMBeanServer(String paramString)
/*     */   {
/* 361 */     checkPermission("findMBeanServer");
/*     */ 
/* 363 */     if (paramString == null) {
/* 364 */       return new ArrayList(mBeanServerList);
/*     */     }
/* 366 */     ArrayList localArrayList = new ArrayList();
/* 367 */     for (MBeanServer localMBeanServer : mBeanServerList) {
/* 368 */       String str = mBeanServerId(localMBeanServer);
/* 369 */       if (paramString.equals(str))
/* 370 */         localArrayList.add(localMBeanServer);
/*     */     }
/* 372 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static ClassLoaderRepository getClassLoaderRepository(MBeanServer paramMBeanServer)
/*     */   {
/* 394 */     return paramMBeanServer.getClassLoaderRepository();
/*     */   }
/*     */ 
/*     */   private static String mBeanServerId(MBeanServer paramMBeanServer) {
/*     */     try {
/* 399 */       return (String)paramMBeanServer.getAttribute(MBeanServerDelegate.DELEGATE_NAME, "MBeanServerId");
/*     */     }
/*     */     catch (JMException localJMException) {
/* 402 */       JmxProperties.MISC_LOGGER.finest("Ignoring exception while getting MBeanServerId: " + localJMException);
/*     */     }
/* 404 */     return null;
/*     */   }
/*     */ 
/*     */   private static void checkPermission(String paramString)
/*     */     throws SecurityException
/*     */   {
/* 410 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 411 */     if (localSecurityManager != null) {
/* 412 */       MBeanServerPermission localMBeanServerPermission = new MBeanServerPermission(paramString);
/* 413 */       localSecurityManager.checkPermission(localMBeanServerPermission);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized void addMBeanServer(MBeanServer paramMBeanServer) {
/* 418 */     mBeanServerList.add(paramMBeanServer);
/*     */   }
/*     */ 
/*     */   private static synchronized void removeMBeanServer(MBeanServer paramMBeanServer) {
/* 422 */     boolean bool = mBeanServerList.remove(paramMBeanServer);
/* 423 */     if (!bool) {
/* 424 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, MBeanServerFactory.class.getName(), "removeMBeanServer(MBeanServer)", "MBeanServer was not in list!");
/*     */ 
/* 428 */       throw new IllegalArgumentException("MBeanServer was not in list!");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Class<?> loadBuilderClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 441 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*     */ 
/* 444 */     if (localClassLoader != null)
/*     */     {
/* 446 */       return localClassLoader.loadClass(paramString);
/*     */     }
/*     */ 
/* 450 */     return ReflectUtil.forName(paramString);
/*     */   }
/*     */ 
/*     */   private static MBeanServerBuilder newBuilder(Class<?> paramClass)
/*     */   {
/*     */     try
/*     */     {
/* 461 */       Object localObject = paramClass.newInstance();
/* 462 */       return (MBeanServerBuilder)localObject;
/*     */     } catch (RuntimeException localRuntimeException) {
/* 464 */       throw localRuntimeException;
/*     */     } catch (Exception localException) {
/* 466 */       String str = "Failed to instantiate a MBeanServerBuilder from " + paramClass + ": " + localException;
/*     */ 
/* 469 */       throw new JMRuntimeException(str, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized void checkMBeanServerBuilder()
/*     */   {
/*     */     try
/*     */     {
/* 479 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("javax.management.builder.initial");
/*     */ 
/* 481 */       localObject1 = (String)AccessController.doPrivileged(localGetPropertyAction);
/*     */       try
/*     */       {
/*     */         Object localObject2;
/* 485 */         if ((localObject1 == null) || (((String)localObject1).length() == 0))
/* 486 */           localObject2 = MBeanServerBuilder.class;
/*     */         else {
/* 488 */           localObject2 = loadBuilderClass((String)localObject1);
/*     */         }
/*     */ 
/* 491 */         if (builder != null) {
/* 492 */           localObject3 = builder.getClass();
/* 493 */           if (localObject2 == localObject3) {
/* 494 */             return;
/*     */           }
/*     */         }
/*     */ 
/* 498 */         builder = newBuilder((Class)localObject2);
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 500 */         Object localObject3 = "Failed to load MBeanServerBuilder class " + (String)localObject1 + ": " + localClassNotFoundException;
/*     */ 
/* 503 */         throw new JMRuntimeException((String)localObject3, localClassNotFoundException);
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/*     */       Object localObject1;
/* 506 */       if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
/* 507 */         localObject1 = new StringBuilder().append("Failed to instantiate MBeanServerBuilder: ").append(localRuntimeException).append("\n\t\tCheck the value of the ").append("javax.management.builder.initial").append(" property.");
/*     */ 
/* 511 */         JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanServerFactory.class.getName(), "checkMBeanServerBuilder", ((StringBuilder)localObject1).toString());
/*     */       }
/*     */ 
/* 516 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized MBeanServerBuilder getNewMBeanServerBuilder()
/*     */   {
/* 539 */     checkMBeanServerBuilder();
/* 540 */     return builder;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanServerFactory
 * JD-Core Version:    0.6.2
 */