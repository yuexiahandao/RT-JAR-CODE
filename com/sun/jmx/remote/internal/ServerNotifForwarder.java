/*     */ package com.sun.jmx.remote.internal;
/*     */ 
/*     */ import com.sun.jmx.remote.security.NotificationAccessController;
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanPermission;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerDelegate;
/*     */ import javax.management.MBeanServerNotification;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationBroadcaster;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.ObjectInstance;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.remote.NotificationResult;
/*     */ import javax.management.remote.TargetedNotification;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ public class ServerNotifForwarder
/*     */ {
/* 230 */   private final NotifForwarderBufferFilter bufferFilter = new NotifForwarderBufferFilter();
/*     */   private MBeanServer mbeanServer;
/*     */   private final String connectionId;
/*     */   private final long connectionTimeout;
/* 483 */   private static int listenerCounter = 0;
/* 484 */   private static final int[] listenerCounterLock = new int[0];
/*     */   private NotificationBuffer notifBuffer;
/* 487 */   private final Map<ObjectName, Set<IdAndFilter>> listenerMap = new HashMap();
/*     */ 
/* 490 */   private boolean terminated = false;
/* 491 */   private final int[] terminationLock = new int[0];
/*     */ 
/* 493 */   static final String broadcasterClass = NotificationBroadcaster.class.getName();
/*     */   private final boolean checkNotificationEmission;
/*     */   private final NotificationAccessController notificationAccessController;
/* 500 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ServerNotifForwarder");
/*     */ 
/*     */   public ServerNotifForwarder(MBeanServer paramMBeanServer, Map<String, ?> paramMap, NotificationBuffer paramNotificationBuffer, String paramString)
/*     */   {
/*  67 */     this.mbeanServer = paramMBeanServer;
/*  68 */     this.notifBuffer = paramNotificationBuffer;
/*  69 */     this.connectionId = paramString;
/*  70 */     this.connectionTimeout = EnvHelp.getServerConnectionTimeout(paramMap);
/*     */ 
/*  72 */     String str = (String)paramMap.get("jmx.remote.x.check.notification.emission");
/*  73 */     this.checkNotificationEmission = EnvHelp.computeBooleanFromString(str);
/*  74 */     this.notificationAccessController = EnvHelp.getNotificationAccessController(paramMap);
/*     */   }
/*     */ 
/*     */   public Integer addNotificationListener(final ObjectName paramObjectName, NotificationFilter paramNotificationFilter)
/*     */     throws InstanceNotFoundException, IOException
/*     */   {
/*  82 */     if (logger.traceOn()) {
/*  83 */       logger.trace("addNotificationListener", "Add a listener at " + paramObjectName);
/*     */     }
/*     */ 
/*  87 */     checkState();
/*     */ 
/*  91 */     checkMBeanPermission(paramObjectName, "addNotificationListener");
/*  92 */     if (this.notificationAccessController != null) {
/*  93 */       this.notificationAccessController.addNotificationListener(this.connectionId, paramObjectName, getSubject());
/*     */     }
/*     */     try
/*     */     {
/*  97 */       boolean bool = ((Boolean)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Boolean run() throws InstanceNotFoundException
/*     */         {
/* 101 */           return Boolean.valueOf(ServerNotifForwarder.this.mbeanServer.isInstanceOf(paramObjectName, ServerNotifForwarder.broadcasterClass));
/*     */         }
/*     */       })).booleanValue();
/*     */ 
/* 104 */       if (!bool) {
/* 105 */         throw new IllegalArgumentException("The specified MBean [" + paramObjectName + "] is not a " + "NotificationBroadcaster " + "object.");
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 111 */       throw ((InstanceNotFoundException)extractException(localPrivilegedActionException));
/*     */     }
/*     */ 
/* 114 */     Integer localInteger = getListenerID();
/*     */ 
/* 117 */     ObjectName localObjectName = paramObjectName;
/*     */     Object localObject1;
/* 118 */     if ((paramObjectName.getDomain() == null) || (paramObjectName.getDomain().equals(""))) {
/*     */       try {
/* 120 */         localObjectName = ObjectName.getInstance(this.mbeanServer.getDefaultDomain(), paramObjectName.getKeyPropertyList());
/*     */       }
/*     */       catch (MalformedObjectNameException localMalformedObjectNameException)
/*     */       {
/* 124 */         localObject1 = new IOException(localMalformedObjectNameException.getMessage());
/* 125 */         ((IOException)localObject1).initCause(localMalformedObjectNameException);
/* 126 */         throw ((Throwable)localObject1);
/*     */       }
/*     */     }
/*     */ 
/* 130 */     synchronized (this.listenerMap) {
/* 131 */       localObject1 = new IdAndFilter(localInteger, paramNotificationFilter);
/* 132 */       Object localObject2 = (Set)this.listenerMap.get(localObjectName);
/*     */ 
/* 135 */       if (localObject2 == null) {
/* 136 */         localObject2 = Collections.singleton(localObject1);
/*     */       } else {
/* 138 */         if (((Set)localObject2).size() == 1)
/* 139 */           localObject2 = new HashSet((Collection)localObject2);
/* 140 */         ((Set)localObject2).add(localObject1);
/*     */       }
/* 142 */       this.listenerMap.put(localObjectName, localObject2);
/*     */     }
/*     */ 
/* 145 */     return localInteger;
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(ObjectName paramObjectName, Integer[] paramArrayOfInteger)
/*     */     throws Exception
/*     */   {
/* 152 */     if (logger.traceOn()) {
/* 153 */       logger.trace("removeNotificationListener", "Remove some listeners from " + paramObjectName);
/*     */     }
/*     */ 
/* 157 */     checkState();
/*     */ 
/* 161 */     checkMBeanPermission(paramObjectName, "removeNotificationListener");
/* 162 */     if (this.notificationAccessController != null) {
/* 163 */       this.notificationAccessController.removeNotificationListener(this.connectionId, paramObjectName, getSubject());
/*     */     }
/*     */ 
/* 167 */     Object localObject = null;
/* 168 */     for (int i = 0; i < paramArrayOfInteger.length; i++) {
/*     */       try {
/* 170 */         removeNotificationListener(paramObjectName, paramArrayOfInteger[i]);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 174 */         if (localObject != null) {
/* 175 */           localObject = localException;
/*     */         }
/*     */       }
/*     */     }
/* 179 */     if (localObject != null)
/* 180 */       throw localObject;
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(ObjectName paramObjectName, Integer paramInteger)
/*     */     throws InstanceNotFoundException, ListenerNotFoundException, IOException
/*     */   {
/* 190 */     if (logger.traceOn()) {
/* 191 */       logger.trace("removeNotificationListener", "Remove the listener " + paramInteger + " from " + paramObjectName);
/*     */     }
/*     */ 
/* 195 */     checkState();
/*     */ 
/* 197 */     if ((paramObjectName != null) && (!paramObjectName.isPattern()) && 
/* 198 */       (!this.mbeanServer.isRegistered(paramObjectName))) {
/* 199 */       throw new InstanceNotFoundException("The MBean " + paramObjectName + " is not registered.");
/*     */     }
/*     */ 
/* 204 */     synchronized (this.listenerMap)
/*     */     {
/* 207 */       Set localSet = (Set)this.listenerMap.get(paramObjectName);
/* 208 */       IdAndFilter localIdAndFilter = new IdAndFilter(paramInteger, null);
/* 209 */       if ((localSet == null) || (!localSet.contains(localIdAndFilter)))
/* 210 */         throw new ListenerNotFoundException("Listener not found");
/* 211 */       if (localSet.size() == 1)
/* 212 */         this.listenerMap.remove(paramObjectName);
/*     */       else
/* 214 */         localSet.remove(localIdAndFilter);
/*     */     }
/*     */   }
/*     */ 
/*     */   public NotificationResult fetchNotifs(long paramLong1, long paramLong2, int paramInt)
/*     */   {
/* 265 */     if (logger.traceOn()) {
/* 266 */       logger.trace("fetchNotifs", "Fetching notifications, the startSequenceNumber is " + paramLong1 + ", the timeout is " + paramLong2 + ", the maxNotifications is " + paramInt);
/*     */     }
/*     */ 
/* 273 */     long l = Math.min(this.connectionTimeout, paramLong2);
/*     */     NotificationResult localNotificationResult;
/*     */     try
/*     */     {
/* 275 */       localNotificationResult = this.notifBuffer.fetchNotifications(this.bufferFilter, paramLong1, l, paramInt);
/*     */ 
/* 278 */       snoopOnUnregister(localNotificationResult);
/*     */     } catch (InterruptedException localInterruptedException) {
/* 280 */       localNotificationResult = new NotificationResult(0L, 0L, new TargetedNotification[0]);
/*     */     }
/*     */ 
/* 283 */     if (logger.traceOn()) {
/* 284 */       logger.trace("fetchNotifs", "Forwarding the notifs: " + localNotificationResult);
/*     */     }
/*     */ 
/* 287 */     return localNotificationResult;
/*     */   }
/*     */ 
/*     */   private void snoopOnUnregister(NotificationResult paramNotificationResult)
/*     */   {
/* 295 */     Set localSet = (Set)this.listenerMap.get(MBeanServerDelegate.DELEGATE_NAME);
/* 296 */     if ((localSet == null) || (localSet.isEmpty()))
/*     */       return;
/*     */     TargetedNotification localTargetedNotification;
/*     */     Integer localInteger;
/* 299 */     for (localTargetedNotification : paramNotificationResult.getTargetedNotifications()) {
/* 300 */       localInteger = localTargetedNotification.getListenerID();
/* 301 */       for (IdAndFilter localIdAndFilter : localSet)
/* 302 */         if (localIdAndFilter.id == localInteger)
/*     */         {
/* 304 */           Notification localNotification = localTargetedNotification.getNotification();
/* 305 */           if (((localNotification instanceof MBeanServerNotification)) && (localNotification.getType().equals("JMX.mbean.unregistered")))
/*     */           {
/* 307 */             MBeanServerNotification localMBeanServerNotification = (MBeanServerNotification)localNotification;
/* 308 */             ObjectName localObjectName = localMBeanServerNotification.getMBeanName();
/* 309 */             synchronized (this.listenerMap) {
/* 310 */               this.listenerMap.remove(localObjectName);
/*     */             }
/*     */           }
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void terminate()
/*     */   {
/* 319 */     if (logger.traceOn()) {
/* 320 */       logger.trace("terminate", "Be called.");
/*     */     }
/*     */ 
/* 323 */     synchronized (this.terminationLock) {
/* 324 */       if (this.terminated) {
/* 325 */         return;
/*     */       }
/*     */ 
/* 328 */       this.terminated = true;
/*     */ 
/* 330 */       synchronized (this.listenerMap) {
/* 331 */         this.listenerMap.clear();
/*     */       }
/*     */     }
/*     */ 
/* 335 */     if (logger.traceOn())
/* 336 */       logger.trace("terminate", "Terminated.");
/*     */   }
/*     */ 
/*     */   private Subject getSubject()
/*     */   {
/* 345 */     return Subject.getSubject(AccessController.getContext());
/*     */   }
/*     */ 
/*     */   private void checkState() throws IOException {
/* 349 */     synchronized (this.terminationLock) {
/* 350 */       if (this.terminated)
/* 351 */         throw new IOException("The connection has been terminated.");
/*     */     }
/*     */   }
/*     */ 
/*     */   private Integer getListenerID()
/*     */   {
/* 357 */     synchronized (listenerCounterLock) {
/* 358 */       return Integer.valueOf(listenerCounter++);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void checkMBeanPermission(ObjectName paramObjectName, String paramString)
/*     */     throws InstanceNotFoundException, SecurityException
/*     */   {
/* 369 */     checkMBeanPermission(this.mbeanServer, paramObjectName, paramString);
/*     */   }
/*     */ 
/*     */   static void checkMBeanPermission(MBeanServer paramMBeanServer, final ObjectName paramObjectName, String paramString)
/*     */     throws InstanceNotFoundException, SecurityException
/*     */   {
/* 376 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 377 */     if (localSecurityManager != null) { AccessControlContext localAccessControlContext = AccessController.getContext();
/*     */       ObjectInstance localObjectInstance;
/*     */       try {
/* 381 */         localObjectInstance = (ObjectInstance)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public ObjectInstance run() throws InstanceNotFoundException
/*     */           {
/* 385 */             return this.val$mbs.getObjectInstance(paramObjectName);
/*     */           } } );
/*     */       }
/*     */       catch (PrivilegedActionException localPrivilegedActionException) {
/* 389 */         throw ((InstanceNotFoundException)extractException(localPrivilegedActionException));
/*     */       }
/* 391 */       String str = localObjectInstance.getClassName();
/* 392 */       MBeanPermission localMBeanPermission = new MBeanPermission(str, null, paramObjectName, paramString);
/*     */ 
/* 397 */       localSecurityManager.checkPermission(localMBeanPermission, localAccessControlContext);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean allowNotificationEmission(ObjectName paramObjectName, TargetedNotification paramTargetedNotification)
/*     */   {
/*     */     try
/*     */     {
/* 407 */       if (this.checkNotificationEmission) {
/* 408 */         checkMBeanPermission(paramObjectName, "addNotificationListener");
/*     */       }
/* 410 */       if (this.notificationAccessController != null) {
/* 411 */         this.notificationAccessController.fetchNotification(this.connectionId, paramObjectName, paramTargetedNotification.getNotification(), getSubject());
/*     */       }
/*     */ 
/* 414 */       return true;
/*     */     } catch (SecurityException localSecurityException) {
/* 416 */       if (logger.debugOn()) {
/* 417 */         logger.debug("fetchNotifs", "Notification " + paramTargetedNotification.getNotification() + " not forwarded: the " + "caller didn't have the required access rights");
/*     */       }
/*     */ 
/* 421 */       return false;
/*     */     } catch (Exception localException) {
/* 423 */       if (logger.debugOn()) {
/* 424 */         logger.debug("fetchNotifs", "Notification " + paramTargetedNotification.getNotification() + " not forwarded: " + "got an unexpected exception: " + localException);
/*     */       }
/*     */     }
/*     */ 
/* 428 */     return false;
/*     */   }
/*     */ 
/*     */   private static Exception extractException(Exception paramException)
/*     */   {
/* 437 */     while ((paramException instanceof PrivilegedActionException)) {
/* 438 */       paramException = ((PrivilegedActionException)paramException).getException();
/*     */     }
/* 440 */     return paramException;
/*     */   }
/*     */   private static class IdAndFilter {
/*     */     private Integer id;
/*     */     private NotificationFilter filter;
/*     */ 
/*     */     IdAndFilter(Integer paramInteger, NotificationFilter paramNotificationFilter) {
/* 448 */       this.id = paramInteger;
/* 449 */       this.filter = paramNotificationFilter;
/*     */     }
/*     */ 
/*     */     Integer getId() {
/* 453 */       return this.id;
/*     */     }
/*     */ 
/*     */     NotificationFilter getFilter() {
/* 457 */       return this.filter;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 462 */       return this.id.hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 467 */       return ((paramObject instanceof IdAndFilter)) && (((IdAndFilter)paramObject).getId().equals(getId()));
/*     */     }
/*     */   }
/*     */ 
/*     */   final class NotifForwarderBufferFilter
/*     */     implements NotificationBufferFilter
/*     */   {
/*     */     NotifForwarderBufferFilter()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void apply(List<TargetedNotification> paramList, ObjectName paramObjectName, Notification paramNotification)
/*     */     {
/*     */       ServerNotifForwarder.IdAndFilter[] arrayOfIdAndFilter;
/* 238 */       synchronized (ServerNotifForwarder.this.listenerMap) {
/* 239 */         Set localSet = (Set)ServerNotifForwarder.this.listenerMap.get(paramObjectName);
/* 240 */         if (localSet == null) {
/* 241 */           ServerNotifForwarder.logger.debug("bufferFilter", "no listeners for this name");
/* 242 */           return;
/*     */         }
/* 244 */         arrayOfIdAndFilter = new ServerNotifForwarder.IdAndFilter[localSet.size()];
/* 245 */         localSet.toArray(arrayOfIdAndFilter);
/*     */       }
/*     */ 
/* 249 */       for (Object localObject2 : arrayOfIdAndFilter) {
/* 250 */         NotificationFilter localNotificationFilter = localObject2.getFilter();
/* 251 */         if ((localNotificationFilter == null) || (localNotificationFilter.isNotificationEnabled(paramNotification))) {
/* 252 */           ServerNotifForwarder.logger.debug("bufferFilter", "filter matches");
/* 253 */           TargetedNotification localTargetedNotification = new TargetedNotification(paramNotification, localObject2.getId());
/*     */ 
/* 255 */           if (ServerNotifForwarder.this.allowNotificationEmission(paramObjectName, localTargetedNotification))
/* 256 */             paramList.add(localTargetedNotification);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.ServerNotifForwarder
 * JD-Core Version:    0.6.2
 */