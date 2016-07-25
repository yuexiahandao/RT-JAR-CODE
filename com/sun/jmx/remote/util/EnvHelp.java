/*     */ package com.sun.jmx.remote.util;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import com.sun.jmx.remote.security.NotificationAccessController;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class EnvHelp
/*     */ {
/*     */   private static final String DEFAULT_CLASS_LOADER = "jmx.remote.default.class.loader";
/*     */   private static final String DEFAULT_CLASS_LOADER_NAME = "jmx.remote.default.class.loader.name";
/*     */   public static final String BUFFER_SIZE_PROPERTY = "jmx.remote.x.notification.buffer.size";
/*     */   public static final String MAX_FETCH_NOTIFS = "jmx.remote.x.notification.fetch.max";
/*     */   public static final String FETCH_TIMEOUT = "jmx.remote.x.notification.fetch.timeout";
/*     */   public static final String NOTIF_ACCESS_CONTROLLER = "com.sun.jmx.remote.notification.access.controller";
/*     */   public static final String DEFAULT_ORB = "java.naming.corba.orb";
/*     */   public static final String HIDDEN_ATTRIBUTES = "jmx.remote.x.hidden.attributes";
/*     */   public static final String DEFAULT_HIDDEN_ATTRIBUTES = "java.naming.security.* jmx.remote.authenticator jmx.remote.context jmx.remote.default.class.loader jmx.remote.message.connection.server jmx.remote.object.wrapping jmx.remote.rmi.client.socket.factory jmx.remote.rmi.server.socket.factory jmx.remote.sasl.callback.handler jmx.remote.tls.socket.factory jmx.remote.x.access.file jmx.remote.x.password.file ";
/* 528 */   private static final SortedSet<String> defaultHiddenStrings = new TreeSet();
/*     */ 
/* 530 */   private static final SortedSet<String> defaultHiddenPrefixes = new TreeSet();
/*     */   public static final String SERVER_CONNECTION_TIMEOUT = "jmx.remote.x.server.connection.timeout";
/*     */   public static final String CLIENT_CONNECTION_CHECK_PERIOD = "jmx.remote.x.client.connection.check.period";
/*     */   public static final String JMX_SERVER_DAEMON = "jmx.remote.x.daemon";
/* 766 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "EnvHelp");
/*     */ 
/*     */   public static ClassLoader resolveServerClassLoader(Map<String, ?> paramMap, MBeanServer paramMBeanServer)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 125 */     if (paramMap == null) {
/* 126 */       return Thread.currentThread().getContextClassLoader();
/*     */     }
/* 128 */     Object localObject1 = paramMap.get("jmx.remote.default.class.loader");
/* 129 */     Object localObject2 = paramMap.get("jmx.remote.default.class.loader.name");
/*     */ 
/* 131 */     if ((localObject1 != null) && (localObject2 != null))
/*     */     {
/* 136 */       throw new IllegalArgumentException("Only one of jmx.remote.default.class.loader or jmx.remote.default.class.loader.name should be specified.");
/*     */     }
/*     */ 
/* 139 */     if ((localObject1 == null) && (localObject2 == null))
/* 140 */       return Thread.currentThread().getContextClassLoader();
/*     */     Object localObject3;
/* 142 */     if (localObject1 != null) {
/* 143 */       if ((localObject1 instanceof ClassLoader)) {
/* 144 */         return (ClassLoader)localObject1;
/*     */       }
/* 146 */       localObject3 = "ClassLoader object is not an instance of " + ClassLoader.class.getName() + " : " + localObject1.getClass().getName();
/*     */ 
/* 150 */       throw new IllegalArgumentException((String)localObject3);
/*     */     }
/*     */ 
/* 155 */     if ((localObject2 instanceof ObjectName)) {
/* 156 */       localObject3 = (ObjectName)localObject2;
/*     */     } else {
/* 158 */       String str = "ClassLoader name is not an instance of " + ObjectName.class.getName() + " : " + localObject2.getClass().getName();
/*     */ 
/* 162 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 165 */     if (paramMBeanServer == null) {
/* 166 */       throw new IllegalArgumentException("Null MBeanServer object");
/*     */     }
/* 168 */     return paramMBeanServer.getClassLoader((ObjectName)localObject3);
/*     */   }
/*     */ 
/*     */   public static ClassLoader resolveClientClassLoader(Map<String, ?> paramMap)
/*     */   {
/* 200 */     if (paramMap == null) {
/* 201 */       return Thread.currentThread().getContextClassLoader();
/*     */     }
/* 203 */     Object localObject = paramMap.get("jmx.remote.default.class.loader");
/*     */ 
/* 205 */     if (localObject == null) {
/* 206 */       return Thread.currentThread().getContextClassLoader();
/*     */     }
/* 208 */     if ((localObject instanceof ClassLoader)) {
/* 209 */       return (ClassLoader)localObject;
/*     */     }
/* 211 */     String str = "ClassLoader object is not an instance of " + ClassLoader.class.getName() + " : " + localObject.getClass().getName();
/*     */ 
/* 215 */     throw new IllegalArgumentException(str);
/*     */   }
/*     */ 
/*     */   public static <T extends Throwable> T initCause(T paramT, Throwable paramThrowable)
/*     */   {
/* 228 */     paramT.initCause(paramThrowable);
/* 229 */     return paramT;
/*     */   }
/*     */ 
/*     */   public static Throwable getCause(Throwable paramThrowable)
/*     */   {
/* 241 */     Throwable localThrowable = paramThrowable;
/*     */     try
/*     */     {
/* 244 */       Method localMethod = paramThrowable.getClass().getMethod("getCause", (Class[])null);
/*     */ 
/* 246 */       localThrowable = (Throwable)localMethod.invoke(paramThrowable, (Object[])null);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 252 */     return localThrowable != null ? localThrowable : paramThrowable;
/*     */   }
/*     */ 
/*     */   public static int getNotifBufferSize(Map<String, ?> paramMap)
/*     */   {
/* 269 */     RuntimeException localRuntimeException1 = 1000;
/*     */     try
/*     */     {
/* 278 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.remote.x.notification.buffer.size");
/* 279 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 280 */       if (str != null) {
/* 281 */         localRuntimeException1 = Integer.parseInt(str);
/*     */       } else {
/* 283 */         localGetPropertyAction = new GetPropertyAction("jmx.remote.x.buffer.size");
/* 284 */         str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 285 */         if (str != null)
/* 286 */           localRuntimeException1 = Integer.parseInt(str);
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException2) {
/* 290 */       logger.warning("getNotifBufferSize", "Can't use System property jmx.remote.x.notification.buffer.size: " + localRuntimeException2);
/*     */ 
/* 293 */       logger.debug("getNotifBufferSize", localRuntimeException2);
/* 296 */     }
/*     */ localRuntimeException2 = localRuntimeException1;
/*     */     int i;
/*     */     try {
/* 299 */       if (paramMap.containsKey("jmx.remote.x.notification.buffer.size")) {
/* 300 */         i = (int)getIntegerAttribute(paramMap, "jmx.remote.x.notification.buffer.size", localRuntimeException1, 0L, 2147483647L);
/*     */       }
/*     */       else
/*     */       {
/* 304 */         i = (int)getIntegerAttribute(paramMap, "jmx.remote.x.buffer.size", localRuntimeException1, 0L, 2147483647L);
/*     */       }
/*     */     }
/*     */     catch (RuntimeException localRuntimeException3)
/*     */     {
/* 309 */       logger.warning("getNotifBufferSize", "Can't determine queuesize (using default): " + localRuntimeException3);
/*     */ 
/* 312 */       logger.debug("getNotifBufferSize", localRuntimeException3);
/*     */     }
/*     */ 
/* 315 */     return i;
/*     */   }
/*     */ 
/*     */   public static int getMaxFetchNotifNumber(Map<String, ?> paramMap)
/*     */   {
/* 332 */     return (int)getIntegerAttribute(paramMap, "jmx.remote.x.notification.fetch.max", 1000L, 1L, 2147483647L);
/*     */   }
/*     */ 
/*     */   public static long getFetchTimeout(Map<String, ?> paramMap)
/*     */   {
/* 349 */     return getIntegerAttribute(paramMap, "jmx.remote.x.notification.fetch.timeout", 60000L, 0L, 9223372036854775807L);
/*     */   }
/*     */ 
/*     */   public static NotificationAccessController getNotificationAccessController(Map<String, ?> paramMap)
/*     */   {
/* 366 */     return paramMap == null ? null : (NotificationAccessController)paramMap.get("com.sun.jmx.remote.notification.access.controller");
/*     */   }
/*     */ 
/*     */   public static long getIntegerAttribute(Map<String, ?> paramMap, String paramString, long paramLong1, long paramLong2, long paramLong3)
/*     */   {
/*     */     Object localObject;
/* 387 */     if ((paramMap == null) || ((localObject = paramMap.get(paramString)) == null))
/* 388 */       return paramLong1;
/*     */     long l;
/*     */     String str;
/* 392 */     if ((localObject instanceof Number)) {
/* 393 */       l = ((Number)localObject).longValue();
/* 394 */     } else if ((localObject instanceof String)) {
/* 395 */       l = Long.parseLong((String)localObject);
/*     */     }
/*     */     else
/*     */     {
/* 399 */       str = "Attribute " + paramString + " value must be Integer or String: " + localObject;
/*     */ 
/* 401 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 404 */     if (l < paramLong2) {
/* 405 */       str = "Attribute " + paramString + " value must be at least " + paramLong2 + ": " + l;
/*     */ 
/* 408 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 411 */     if (l > paramLong3) {
/* 412 */       str = "Attribute " + paramString + " value must be at most " + paramLong3 + ": " + l;
/*     */ 
/* 415 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 418 */     return l;
/*     */   }
/*     */ 
/*     */   public static void checkAttributes(Map<?, ?> paramMap)
/*     */   {
/* 426 */     for (Iterator localIterator = paramMap.keySet().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 427 */       if (!(localObject instanceof String)) {
/* 428 */         String str = "Attributes contain key that is not a string: " + localObject;
/*     */ 
/* 430 */         throw new IllegalArgumentException(str);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static <V> Map<String, V> filterAttributes(Map<String, V> paramMap)
/*     */   {
/* 440 */     if (logger.traceOn()) {
/* 441 */       logger.trace("filterAttributes", "starts");
/*     */     }
/*     */ 
/* 444 */     TreeMap localTreeMap = new TreeMap(paramMap);
/* 445 */     purgeUnserializable(localTreeMap.values());
/* 446 */     hideAttributes(localTreeMap);
/* 447 */     return localTreeMap;
/*     */   }
/*     */ 
/*     */   private static void purgeUnserializable(Collection<?> paramCollection)
/*     */   {
/* 455 */     logger.trace("purgeUnserializable", "starts");
/* 456 */     ObjectOutputStream localObjectOutputStream = null;
/* 457 */     int i = 0;
/* 458 */     for (Iterator localIterator = paramCollection.iterator(); localIterator.hasNext(); i++) {
/* 459 */       Object localObject = localIterator.next();
/*     */ 
/* 461 */       if ((localObject == null) || ((localObject instanceof String))) {
/* 462 */         if (logger.traceOn()) {
/* 463 */           logger.trace("purgeUnserializable", "Value trivially serializable: " + localObject);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*     */         try
/*     */         {
/* 470 */           if (localObjectOutputStream == null)
/* 471 */             localObjectOutputStream = new ObjectOutputStream(new SinkOutputStream(null));
/* 472 */           localObjectOutputStream.writeObject(localObject);
/* 473 */           if (logger.traceOn())
/* 474 */             logger.trace("purgeUnserializable", "Value serializable: " + localObject);
/*     */         }
/*     */         catch (IOException localIOException)
/*     */         {
/* 478 */           if (logger.traceOn()) {
/* 479 */             logger.trace("purgeUnserializable", "Value not serializable: " + localObject + ": " + localIOException);
/*     */           }
/*     */ 
/* 483 */           localIterator.remove();
/* 484 */           localObjectOutputStream = null;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void hideAttributes(SortedMap<String, ?> paramSortedMap)
/*     */   {
/* 534 */     if (paramSortedMap.isEmpty()) {
/* 535 */       return;
/*     */     }
/*     */ 
/* 540 */     String str1 = (String)paramSortedMap.get("jmx.remote.x.hidden.attributes");
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 541 */     if (str1 != null) {
/* 542 */       if (str1.startsWith("="))
/* 543 */         str1 = str1.substring(1);
/*     */       else
/* 545 */         str1 = str1 + " java.naming.security.* jmx.remote.authenticator jmx.remote.context jmx.remote.default.class.loader jmx.remote.message.connection.server jmx.remote.object.wrapping jmx.remote.rmi.client.socket.factory jmx.remote.rmi.server.socket.factory jmx.remote.sasl.callback.handler jmx.remote.tls.socket.factory jmx.remote.x.access.file jmx.remote.x.password.file ";
/* 546 */       localObject1 = new TreeSet();
/* 547 */       localObject2 = new TreeSet();
/* 548 */       parseHiddenAttributes(str1, (SortedSet)localObject1, (SortedSet)localObject2);
/*     */     } else {
/* 550 */       str1 = "java.naming.security.* jmx.remote.authenticator jmx.remote.context jmx.remote.default.class.loader jmx.remote.message.connection.server jmx.remote.object.wrapping jmx.remote.rmi.client.socket.factory jmx.remote.rmi.server.socket.factory jmx.remote.sasl.callback.handler jmx.remote.tls.socket.factory jmx.remote.x.access.file jmx.remote.x.password.file ";
/* 551 */       synchronized (defaultHiddenStrings) {
/* 552 */         if (defaultHiddenStrings.isEmpty()) {
/* 553 */           parseHiddenAttributes(str1, defaultHiddenStrings, defaultHiddenPrefixes);
/*     */         }
/*     */ 
/* 557 */         localObject1 = defaultHiddenStrings;
/* 558 */         localObject2 = defaultHiddenPrefixes;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 566 */     ??? = (String)paramSortedMap.lastKey() + "X";
/* 567 */     Iterator localIterator1 = paramSortedMap.keySet().iterator();
/* 568 */     Iterator localIterator2 = ((SortedSet)localObject1).iterator();
/* 569 */     Iterator localIterator3 = ((SortedSet)localObject2).iterator();
/*     */     Object localObject4;
/* 572 */     if (localIterator2.hasNext())
/* 573 */       localObject4 = (String)localIterator2.next();
/*     */     else
/* 575 */       localObject4 = ???;
/*     */     Object localObject5;
/* 577 */     if (localIterator3.hasNext())
/* 578 */       localObject5 = (String)localIterator3.next();
/*     */     else {
/* 580 */       localObject5 = ???;
/*     */     }
/*     */ 
/* 585 */     label405: while (localIterator1.hasNext()) {
/* 586 */       String str2 = (String)localIterator1.next();
/*     */ 
/* 591 */       int i = 1;
/* 592 */       while ((i = ((String)localObject4).compareTo(str2)) < 0) {
/* 593 */         if (localIterator2.hasNext())
/* 594 */           localObject4 = (String)localIterator2.next();
/*     */         else
/* 596 */           localObject4 = ???;
/*     */       }
/* 598 */       if (i == 0) {
/* 599 */         localIterator1.remove();
/*     */       }
/*     */       else
/*     */       {
/*     */         while (true)
/*     */         {
/* 606 */           if (((String)localObject5).compareTo(str2) > 0) break label405;
/* 607 */           if (str2.startsWith((String)localObject5)) {
/* 608 */             localIterator1.remove();
/* 609 */             break;
/*     */           }
/* 611 */           if (localIterator3.hasNext())
/* 612 */             localObject5 = (String)localIterator3.next();
/*     */           else
/* 614 */             localObject5 = ???;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void parseHiddenAttributes(String paramString, SortedSet<String> paramSortedSet1, SortedSet<String> paramSortedSet2)
/*     */   {
/* 622 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
/* 623 */     while (localStringTokenizer.hasMoreTokens()) {
/* 624 */       String str = localStringTokenizer.nextToken();
/* 625 */       if (str.endsWith("*"))
/* 626 */         paramSortedSet2.add(str.substring(0, str.length() - 1));
/*     */       else
/* 628 */         paramSortedSet1.add(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static long getServerConnectionTimeout(Map<String, ?> paramMap)
/*     */   {
/* 644 */     return getIntegerAttribute(paramMap, "jmx.remote.x.server.connection.timeout", 120000L, 0L, 9223372036854775807L);
/*     */   }
/*     */ 
/*     */   public static long getConnectionCheckPeriod(Map<String, ?> paramMap)
/*     */   {
/* 660 */     return getIntegerAttribute(paramMap, "jmx.remote.x.client.connection.check.period", 60000L, 0L, 9223372036854775807L);
/*     */   }
/*     */ 
/*     */   public static boolean computeBooleanFromString(String paramString)
/*     */   {
/* 689 */     return computeBooleanFromString(paramString, false);
/*     */   }
/*     */ 
/*     */   public static boolean computeBooleanFromString(String paramString, boolean paramBoolean)
/*     */   {
/* 719 */     if (paramString == null)
/* 720 */       return paramBoolean;
/* 721 */     if (paramString.equalsIgnoreCase("true"))
/* 722 */       return true;
/* 723 */     if (paramString.equalsIgnoreCase("false")) {
/* 724 */       return false;
/*     */     }
/* 726 */     throw new IllegalArgumentException("Property value must be \"true\" or \"false\" instead of \"" + paramString + "\"");
/*     */   }
/*     */ 
/*     */   public static <K, V> Hashtable<K, V> mapToHashtable(Map<K, V> paramMap)
/*     */   {
/* 736 */     HashMap localHashMap = new HashMap(paramMap);
/* 737 */     if (localHashMap.containsKey(null)) localHashMap.remove(null);
/* 738 */     for (Iterator localIterator = localHashMap.values().iterator(); localIterator.hasNext(); )
/* 739 */       if (localIterator.next() == null) localIterator.remove();
/* 740 */     return new Hashtable(localHashMap);
/*     */   }
/*     */ 
/*     */   public static boolean isServerDaemon(Map<String, ?> paramMap)
/*     */   {
/* 757 */     return (paramMap != null) && ("true".equalsIgnoreCase((String)paramMap.get("jmx.remote.x.daemon")));
/*     */   }
/*     */ 
/*     */   private static final class SinkOutputStream extends OutputStream
/*     */   {
/*     */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void write(int paramInt)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.util.EnvHelp
 * JD-Core Version:    0.6.2
 */