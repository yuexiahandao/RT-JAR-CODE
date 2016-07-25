/*     */ package com.sun.jmx.remote.internal;
/*     */ 
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerDelegate;
/*     */ import javax.management.MBeanServerNotification;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationBroadcaster;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationFilterSupport;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.QueryEval;
/*     */ import javax.management.QueryExp;
/*     */ import javax.management.remote.NotificationResult;
/*     */ import javax.management.remote.TargetedNotification;
/*     */ 
/*     */ public class ArrayNotificationBuffer
/*     */   implements NotificationBuffer
/*     */ {
/* 112 */   private boolean disposed = false;
/*     */ 
/* 116 */   private static final Object globalLock = new Object();
/*     */ 
/* 118 */   private static final HashMap<MBeanServer, ArrayNotificationBuffer> mbsToBuffer = new HashMap(1);
/*     */ 
/* 120 */   private final Collection<ShareBuffer> sharers = new HashSet(1);
/*     */ 
/* 775 */   private final NotificationListener bufferListener = new BufferListener(null);
/*     */ 
/* 786 */   private static final QueryExp broadcasterQuery = new BroadcasterQuery(null);
/*     */ 
/* 792 */   private static final NotificationFilter creationFilter = localNotificationFilterSupport;
/*     */ 
/* 795 */   private final NotificationListener creationListener = new NotificationListener()
/*     */   {
/*     */     public void handleNotification(Notification paramAnonymousNotification, Object paramAnonymousObject)
/*     */     {
/* 799 */       ArrayNotificationBuffer.logger.debug("creationListener", "handleNotification called");
/* 800 */       ArrayNotificationBuffer.this.createdNotification((MBeanServerNotification)paramAnonymousNotification);
/*     */     }
/* 795 */   };
/*     */ 
/* 839 */   private static final ClassLogger logger = new ClassLogger("javax.management.remote.misc", "ArrayNotificationBuffer");
/*     */   private final MBeanServer mBeanServer;
/*     */   private final ArrayQueue<NamedNotification> queue;
/*     */   private int queueSize;
/*     */   private long earliestSequenceNumber;
/*     */   private long nextSequenceNumber;
/*     */   private Set<ObjectName> createdDuringQuery;
/* 850 */   static final String broadcasterClass = NotificationBroadcaster.class.getName();
/*     */ 
/*     */   public static NotificationBuffer getNotificationBuffer(MBeanServer paramMBeanServer, Map<String, ?> paramMap)
/*     */   {
/* 125 */     if (paramMap == null) {
/* 126 */       paramMap = Collections.emptyMap();
/*     */     }
/*     */ 
/* 129 */     int i = EnvHelp.getNotifBufferSize(paramMap);
/*     */     ArrayNotificationBuffer localArrayNotificationBuffer;
/*     */     int j;
/*     */     ShareBuffer localShareBuffer;
/* 134 */     synchronized (globalLock) {
/* 135 */       localArrayNotificationBuffer = (ArrayNotificationBuffer)mbsToBuffer.get(paramMBeanServer);
/* 136 */       j = localArrayNotificationBuffer == null ? 1 : 0;
/* 137 */       if (j != 0) {
/* 138 */         localArrayNotificationBuffer = new ArrayNotificationBuffer(paramMBeanServer, i);
/* 139 */         mbsToBuffer.put(paramMBeanServer, localArrayNotificationBuffer);
/*     */       }
/*     */       ArrayNotificationBuffer tmp71_70 = localArrayNotificationBuffer; tmp71_70.getClass(); localShareBuffer = new ShareBuffer(i);
/*     */     }
/*     */ 
/* 152 */     if (j != 0)
/* 153 */       localArrayNotificationBuffer.createListeners();
/* 154 */     return localShareBuffer;
/*     */   }
/*     */ 
/*     */   static void removeNotificationBuffer(MBeanServer paramMBeanServer)
/*     */   {
/* 162 */     synchronized (globalLock) {
/* 163 */       mbsToBuffer.remove(paramMBeanServer);
/*     */     }
/*     */   }
/*     */ 
/*     */   void addSharer(ShareBuffer paramShareBuffer) {
/* 168 */     synchronized (globalLock) {
/* 169 */       synchronized (this) {
/* 170 */         if (paramShareBuffer.getSize() > this.queueSize)
/* 171 */           resize(paramShareBuffer.getSize());
/*     */       }
/* 173 */       this.sharers.add(paramShareBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeSharer(ShareBuffer paramShareBuffer)
/*     */   {
/*     */     boolean bool;
/* 179 */     synchronized (globalLock) {
/* 180 */       this.sharers.remove(paramShareBuffer);
/* 181 */       bool = this.sharers.isEmpty();
/* 182 */       if (bool) {
/* 183 */         removeNotificationBuffer(this.mBeanServer);
/*     */       } else {
/* 185 */         int i = 0;
/* 186 */         for (ShareBuffer localShareBuffer : this.sharers) {
/* 187 */           int j = localShareBuffer.getSize();
/* 188 */           if (j > i)
/* 189 */             i = j;
/*     */         }
/* 191 */         if (i < this.queueSize)
/* 192 */           resize(i);
/*     */       }
/*     */     }
/* 195 */     if (bool) {
/* 196 */       synchronized (this) {
/* 197 */         this.disposed = true;
/*     */ 
/* 199 */         notifyAll();
/*     */       }
/* 201 */       destroyListeners();
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void resize(int paramInt) {
/* 206 */     if (paramInt == this.queueSize)
/* 207 */       return;
/* 208 */     while (this.queue.size() > paramInt)
/* 209 */       dropNotification();
/* 210 */     this.queue.resize(paramInt);
/* 211 */     this.queueSize = paramInt;
/*     */   }
/*     */ 
/*     */   private ArrayNotificationBuffer(MBeanServer paramMBeanServer, int paramInt)
/*     */   {
/* 246 */     if (logger.traceOn()) {
/* 247 */       logger.trace("Constructor", "queueSize=" + paramInt);
/*     */     }
/* 249 */     if ((paramMBeanServer == null) || (paramInt < 1)) {
/* 250 */       throw new IllegalArgumentException("Bad args");
/*     */     }
/* 252 */     this.mBeanServer = paramMBeanServer;
/* 253 */     this.queueSize = paramInt;
/* 254 */     this.queue = new ArrayQueue(paramInt);
/* 255 */     this.earliestSequenceNumber = System.currentTimeMillis();
/* 256 */     this.nextSequenceNumber = this.earliestSequenceNumber;
/*     */ 
/* 258 */     logger.trace("Constructor", "ends");
/*     */   }
/*     */ 
/*     */   private synchronized boolean isDisposed() {
/* 262 */     return this.disposed;
/*     */   }
/*     */ 
/*     */   public void dispose()
/*     */   {
/* 269 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public NotificationResult fetchNotifications(NotificationBufferFilter paramNotificationBufferFilter, long paramLong1, long paramLong2, int paramInt)
/*     */     throws InterruptedException
/*     */   {
/* 306 */     logger.trace("fetchNotifications", "starts");
/*     */ 
/* 308 */     if ((paramLong1 < 0L) || (isDisposed())) {
/* 309 */       synchronized (this) {
/* 310 */         return new NotificationResult(earliestSequenceNumber(), nextSequenceNumber(), new TargetedNotification[0]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 317 */     if ((paramNotificationBufferFilter == null) || (paramLong1 < 0L) || (paramLong2 < 0L) || (paramInt < 0))
/*     */     {
/* 320 */       logger.trace("fetchNotifications", "Bad args");
/* 321 */       throw new IllegalArgumentException("Bad args to fetch");
/*     */     }
/*     */ 
/* 324 */     if (logger.debugOn()) {
/* 325 */       logger.trace("fetchNotifications", "filter=" + paramNotificationBufferFilter + "; startSeq=" + paramLong1 + "; timeout=" + paramLong2 + "; max=" + paramInt);
/*     */     }
/*     */ 
/* 331 */     if (paramLong1 > nextSequenceNumber()) {
/* 332 */       ??? = "Start sequence number too big: " + paramLong1 + " > " + nextSequenceNumber();
/*     */ 
/* 334 */       logger.trace("fetchNotifications", (String)???);
/* 335 */       throw new IllegalArgumentException((String)???);
/*     */     }
/*     */ 
/* 343 */     long l1 = System.currentTimeMillis() + paramLong2;
/* 344 */     if (l1 < 0L) {
/* 345 */       l1 = 9223372036854775807L;
/*     */     }
/* 347 */     if (logger.debugOn()) {
/* 348 */       logger.debug("fetchNotifications", "endTime=" + l1);
/*     */     }
/*     */ 
/* 354 */     long l2 = -1L;
/* 355 */     long l3 = paramLong1;
/* 356 */     ArrayList localArrayList1 = new ArrayList();
/*     */     while (true)
/*     */     {
/* 362 */       logger.debug("fetchNotifications", "main loop starts");
/*     */       NamedNotification localNamedNotification;
/* 368 */       label791: synchronized (this)
/*     */       {
/* 372 */         if (l2 < 0L) {
/* 373 */           l2 = earliestSequenceNumber();
/* 374 */           if (logger.debugOn()) {
/* 375 */             logger.debug("fetchNotifications", "earliestSeq=" + l2);
/*     */           }
/*     */ 
/* 378 */           if (l3 < l2) {
/* 379 */             l3 = l2;
/* 380 */             logger.debug("fetchNotifications", "nextSeq=earliestSeq");
/*     */           }
/*     */         }
/*     */         else {
/* 384 */           l2 = earliestSequenceNumber();
/*     */         }
/*     */ 
/* 391 */         if (l3 < l2) {
/* 392 */           logger.trace("fetchNotifications", "nextSeq=" + l3 + " < " + "earliestSeq=" + l2 + " so may have lost notifs");
/*     */ 
/* 395 */           break;
/*     */         }
/*     */ 
/* 398 */         if (l3 < nextSequenceNumber()) {
/* 399 */           localNamedNotification = notificationAt(l3);
/*     */ 
/* 401 */           if (!(paramNotificationBufferFilter instanceof ServerNotifForwarder.NotifForwarderBufferFilter)) {
/*     */             try {
/* 403 */               ServerNotifForwarder.checkMBeanPermission(this.mBeanServer, localNamedNotification.getObjectName(), "addNotificationListener");
/*     */             }
/*     */             catch (InstanceNotFoundException|SecurityException localInstanceNotFoundException) {
/* 406 */               if (logger.debugOn()) {
/* 407 */                 logger.debug("fetchNotifications", "candidate: " + localNamedNotification + " skipped. exception " + localInstanceNotFoundException);
/*     */               }
/* 409 */               l3 += 1L;
/* 410 */             }continue;
/*     */           }
/*     */ 
/* 414 */           if (!logger.debugOn()) break label791;
/* 415 */           logger.debug("fetchNotifications", "candidate: " + localNamedNotification);
/*     */ 
/* 417 */           logger.debug("fetchNotifications", "nextSeq now " + l3); break label791;
/*     */         }
/*     */ 
/* 425 */         if (localArrayList1.size() > 0) {
/* 426 */           logger.debug("fetchNotifications", "no more notifs but have some so don't wait");
/*     */ 
/* 428 */           break;
/*     */         }
/* 430 */         long l4 = l1 - System.currentTimeMillis();
/* 431 */         if (l4 <= 0L) {
/* 432 */           logger.debug("fetchNotifications", "timeout");
/* 433 */           break;
/*     */         }
/*     */ 
/* 437 */         if (isDisposed()) {
/* 438 */           if (logger.debugOn()) {
/* 439 */             logger.debug("fetchNotifications", "dispose callled, no wait");
/*     */           }
/* 441 */           return new NotificationResult(earliestSequenceNumber(), nextSequenceNumber(), new TargetedNotification[0]);
/*     */         }
/*     */ 
/* 446 */         if (logger.debugOn()) {
/* 447 */           logger.debug("fetchNotifications", "wait(" + l4 + ")");
/*     */         }
/* 449 */         wait(l4);
/*     */ 
/* 451 */         continue;
/*     */       }
/*     */ 
/* 460 */       ??? = localNamedNotification.getObjectName();
/* 461 */       localObject2 = localNamedNotification.getNotification();
/* 462 */       ArrayList localArrayList2 = new ArrayList();
/*     */ 
/* 464 */       logger.debug("fetchNotifications", "applying filter to candidate");
/*     */ 
/* 466 */       paramNotificationBufferFilter.apply(localArrayList2, (ObjectName)???, (Notification)localObject2);
/*     */ 
/* 468 */       if (localArrayList2.size() > 0)
/*     */       {
/* 474 */         if (paramInt <= 0) {
/* 475 */           logger.debug("fetchNotifications", "reached maxNotifications");
/*     */ 
/* 477 */           break;
/*     */         }
/* 479 */         paramInt--;
/* 480 */         if (logger.debugOn()) {
/* 481 */           logger.debug("fetchNotifications", "add: " + localArrayList2);
/*     */         }
/* 483 */         localArrayList1.addAll(localArrayList2);
/*     */       }
/*     */ 
/* 486 */       l3 += 1L;
/*     */     }
/*     */ 
/* 490 */     int i = localArrayList1.size();
/* 491 */     ??? = new TargetedNotification[i];
/*     */ 
/* 493 */     localArrayList1.toArray((Object[])???);
/* 494 */     Object localObject2 = new NotificationResult(l2, l3, (TargetedNotification[])???);
/*     */ 
/* 496 */     if (logger.debugOn())
/* 497 */       logger.debug("fetchNotifications", ((NotificationResult)localObject2).toString());
/* 498 */     logger.trace("fetchNotifications", "ends");
/*     */ 
/* 500 */     return localObject2;
/*     */   }
/*     */ 
/*     */   synchronized long earliestSequenceNumber() {
/* 504 */     return this.earliestSequenceNumber;
/*     */   }
/*     */ 
/*     */   synchronized long nextSequenceNumber() {
/* 508 */     return this.nextSequenceNumber;
/*     */   }
/*     */ 
/*     */   synchronized void addNotification(NamedNotification paramNamedNotification) {
/* 512 */     if (logger.traceOn()) {
/* 513 */       logger.trace("addNotification", paramNamedNotification.toString());
/*     */     }
/* 515 */     while (this.queue.size() >= this.queueSize) {
/* 516 */       dropNotification();
/* 517 */       if (logger.debugOn()) {
/* 518 */         logger.debug("addNotification", "dropped oldest notif, earliestSeq=" + this.earliestSequenceNumber);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 523 */     this.queue.add(paramNamedNotification);
/* 524 */     this.nextSequenceNumber += 1L;
/* 525 */     if (logger.debugOn())
/* 526 */       logger.debug("addNotification", "nextSeq=" + this.nextSequenceNumber);
/* 527 */     notifyAll();
/*     */   }
/*     */ 
/*     */   private void dropNotification() {
/* 531 */     this.queue.remove(0);
/* 532 */     this.earliestSequenceNumber += 1L;
/*     */   }
/*     */ 
/*     */   synchronized NamedNotification notificationAt(long paramLong) {
/* 536 */     long l = paramLong - this.earliestSequenceNumber;
/* 537 */     if ((l < 0L) || (l > 2147483647L)) {
/* 538 */       String str = "Bad sequence number: " + paramLong + " (earliest " + this.earliestSequenceNumber + ")";
/*     */ 
/* 540 */       logger.trace("notificationAt", str);
/* 541 */       throw new IllegalArgumentException(str);
/*     */     }
/* 543 */     return (NamedNotification)this.queue.get((int)l);
/*     */   }
/*     */ 
/*     */   private void createListeners()
/*     */   {
/* 601 */     logger.debug("createListeners", "starts");
/*     */ 
/* 603 */     synchronized (this) {
/* 604 */       this.createdDuringQuery = new HashSet();
/*     */     }
/*     */     Object localObject3;
/*     */     try {
/* 608 */       addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, this.creationListener, creationFilter, null);
/*     */ 
/* 610 */       logger.debug("createListeners", "added creationListener");
/*     */     }
/*     */     catch (Exception localException) {
/* 613 */       localObject3 = new IllegalArgumentException("Can't add listener to MBean server delegate: " + localException);
/* 614 */       EnvHelp.initCause((Throwable)localObject3, localException);
/* 615 */       logger.fine("createListeners", "Can't add listener to MBean server delegate: " + localException);
/* 616 */       logger.debug("createListeners", localException);
/* 617 */       throw ((Throwable)localObject3);
/*     */     }
/*     */ 
/* 622 */     Object localObject1 = queryNames(null, broadcasterQuery);
/* 623 */     localObject1 = new HashSet((Collection)localObject1);
/*     */ 
/* 625 */     synchronized (this) {
/* 626 */       ((Set)localObject1).addAll(this.createdDuringQuery);
/* 627 */       this.createdDuringQuery = null;
/*     */     }
/*     */ 
/* 630 */     for (??? = ((Set)localObject1).iterator(); ((Iterator)???).hasNext(); ) { localObject3 = (ObjectName)((Iterator)???).next();
/* 631 */       addBufferListener((ObjectName)localObject3); }
/* 632 */     logger.debug("createListeners", "ends");
/*     */   }
/*     */ 
/*     */   private void addBufferListener(ObjectName paramObjectName) {
/* 636 */     checkNoLocks();
/* 637 */     if (logger.debugOn())
/* 638 */       logger.debug("addBufferListener", paramObjectName.toString());
/*     */     try {
/* 640 */       addNotificationListener(paramObjectName, this.bufferListener, null, paramObjectName);
/*     */     } catch (Exception localException) {
/* 642 */       logger.trace("addBufferListener", localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeBufferListener(ObjectName paramObjectName)
/*     */   {
/* 650 */     checkNoLocks();
/* 651 */     if (logger.debugOn())
/* 652 */       logger.debug("removeBufferListener", paramObjectName.toString());
/*     */     try {
/* 654 */       removeNotificationListener(paramObjectName, this.bufferListener);
/*     */     } catch (Exception localException) {
/* 656 */       logger.trace("removeBufferListener", localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addNotificationListener(final ObjectName paramObjectName, final NotificationListener paramNotificationListener, final NotificationFilter paramNotificationFilter, final Object paramObject)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 666 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Void run() throws InstanceNotFoundException {
/* 668 */           ArrayNotificationBuffer.this.mBeanServer.addNotificationListener(paramObjectName, paramNotificationListener, paramNotificationFilter, paramObject);
/*     */ 
/* 672 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (Exception localException) {
/* 676 */       throw extractException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeNotificationListener(final ObjectName paramObjectName, final NotificationListener paramNotificationListener) throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 684 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Void run() throws Exception {
/* 686 */           ArrayNotificationBuffer.this.mBeanServer.removeNotificationListener(paramObjectName, paramNotificationListener);
/* 687 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (Exception localException) {
/* 691 */       throw extractException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Set<ObjectName> queryNames(final ObjectName paramObjectName, final QueryExp paramQueryExp)
/*     */   {
/* 697 */     PrivilegedAction local3 = new PrivilegedAction()
/*     */     {
/*     */       public Set<ObjectName> run() {
/* 700 */         return ArrayNotificationBuffer.this.mBeanServer.queryNames(paramObjectName, paramQueryExp);
/*     */       }
/*     */     };
/*     */     try {
/* 704 */       return (Set)AccessController.doPrivileged(local3);
/*     */     } catch (RuntimeException localRuntimeException) {
/* 706 */       logger.fine("queryNames", "Failed to query names: " + localRuntimeException);
/* 707 */       logger.debug("queryNames", localRuntimeException);
/* 708 */       throw localRuntimeException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isInstanceOf(MBeanServer paramMBeanServer, final ObjectName paramObjectName, final String paramString)
/*     */   {
/* 715 */     PrivilegedExceptionAction local4 = new PrivilegedExceptionAction()
/*     */     {
/*     */       public Boolean run() throws InstanceNotFoundException {
/* 718 */         return Boolean.valueOf(this.val$mbs.isInstanceOf(paramObjectName, paramString));
/*     */       }
/*     */     };
/*     */     try {
/* 722 */       return ((Boolean)AccessController.doPrivileged(local4)).booleanValue();
/*     */     } catch (Exception localException) {
/* 724 */       logger.fine("isInstanceOf", "failed: " + localException);
/* 725 */       logger.debug("isInstanceOf", localException);
/* 726 */     }return false;
/*     */   }
/*     */ 
/*     */   private void createdNotification(MBeanServerNotification paramMBeanServerNotification)
/*     */   {
/* 741 */     if (!paramMBeanServerNotification.getType().equals("JMX.mbean.registered")) {
/* 742 */       logger.warning("createNotification", "bad type: " + paramMBeanServerNotification.getType());
/* 743 */       return;
/*     */     }
/*     */ 
/* 746 */     ObjectName localObjectName = paramMBeanServerNotification.getMBeanName();
/* 747 */     if (logger.debugOn()) {
/* 748 */       logger.debug("createdNotification", "for: " + localObjectName);
/*     */     }
/* 750 */     synchronized (this) {
/* 751 */       if (this.createdDuringQuery != null) {
/* 752 */         this.createdDuringQuery.add(localObjectName);
/* 753 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 757 */     if (isInstanceOf(this.mBeanServer, localObjectName, broadcasterClass)) {
/* 758 */       addBufferListener(localObjectName);
/* 759 */       if (isDisposed())
/* 760 */         removeBufferListener(localObjectName);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void destroyListeners()
/*     */   {
/* 805 */     checkNoLocks();
/* 806 */     logger.debug("destroyListeners", "starts");
/*     */     try {
/* 808 */       removeNotificationListener(MBeanServerDelegate.DELEGATE_NAME, this.creationListener);
/*     */     }
/*     */     catch (Exception localException) {
/* 811 */       logger.warning("remove listener from MBeanServer delegate", localException);
/*     */     }
/* 813 */     Set localSet = queryNames(null, broadcasterQuery);
/* 814 */     for (ObjectName localObjectName : localSet) {
/* 815 */       if (logger.debugOn()) {
/* 816 */         logger.debug("destroyListeners", "remove listener from " + localObjectName);
/*     */       }
/* 818 */       removeBufferListener(localObjectName);
/*     */     }
/* 820 */     logger.debug("destroyListeners", "ends");
/*     */   }
/*     */ 
/*     */   private void checkNoLocks() {
/* 824 */     if ((Thread.holdsLock(this)) || (Thread.holdsLock(globalLock)))
/* 825 */       logger.warning("checkNoLocks", "lock protocol violation");
/*     */   }
/*     */ 
/*     */   private static Exception extractException(Exception paramException)
/*     */   {
/* 833 */     while ((paramException instanceof PrivilegedActionException)) {
/* 834 */       paramException = ((PrivilegedActionException)paramException).getException();
/*     */     }
/* 836 */     return paramException;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 790 */     NotificationFilterSupport localNotificationFilterSupport = new NotificationFilterSupport();
/* 791 */     localNotificationFilterSupport.enableType("JMX.mbean.registered");
/*     */   }
/*     */ 
/*     */   private static class BroadcasterQuery extends QueryEval
/*     */     implements QueryExp
/*     */   {
/*     */     private static final long serialVersionUID = 7378487660587592048L;
/*     */ 
/*     */     public boolean apply(ObjectName paramObjectName)
/*     */     {
/* 782 */       MBeanServer localMBeanServer = QueryEval.getMBeanServer();
/* 783 */       return ArrayNotificationBuffer.isInstanceOf(localMBeanServer, paramObjectName, ArrayNotificationBuffer.broadcasterClass);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class BufferListener
/*     */     implements NotificationListener
/*     */   {
/*     */     private BufferListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void handleNotification(Notification paramNotification, Object paramObject)
/*     */     {
/* 766 */       if (ArrayNotificationBuffer.logger.debugOn()) {
/* 767 */         ArrayNotificationBuffer.logger.debug("BufferListener.handleNotification", "notif=" + paramNotification + "; handback=" + paramObject);
/*     */       }
/*     */ 
/* 770 */       ObjectName localObjectName = (ObjectName)paramObject;
/* 771 */       ArrayNotificationBuffer.this.addNotification(new ArrayNotificationBuffer.NamedNotification(localObjectName, paramNotification));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class NamedNotification
/*     */   {
/*     */     private final ObjectName sender;
/*     */     private final Notification notification;
/*     */ 
/*     */     NamedNotification(ObjectName paramObjectName, Notification paramNotification)
/*     */     {
/* 548 */       this.sender = paramObjectName;
/* 549 */       this.notification = paramNotification;
/*     */     }
/*     */ 
/*     */     ObjectName getObjectName() {
/* 553 */       return this.sender;
/*     */     }
/*     */ 
/*     */     Notification getNotification() {
/* 557 */       return this.notification;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 561 */       return "NamedNotification(" + this.sender + ", " + this.notification + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ShareBuffer
/*     */     implements NotificationBuffer
/*     */   {
/*     */     private final int size;
/*     */ 
/*     */     ShareBuffer(int arg2)
/*     */     {
/*     */       int i;
/* 216 */       this.size = i;
/* 217 */       ArrayNotificationBuffer.this.addSharer(this);
/*     */     }
/*     */ 
/*     */     public NotificationResult fetchNotifications(NotificationBufferFilter paramNotificationBufferFilter, long paramLong1, long paramLong2, int paramInt)
/*     */       throws InterruptedException
/*     */     {
/* 226 */       ArrayNotificationBuffer localArrayNotificationBuffer = ArrayNotificationBuffer.this;
/* 227 */       return localArrayNotificationBuffer.fetchNotifications(paramNotificationBufferFilter, paramLong1, paramLong2, paramInt);
/*     */     }
/*     */ 
/*     */     public void dispose()
/*     */     {
/* 232 */       ArrayNotificationBuffer.this.removeSharer(this);
/*     */     }
/*     */ 
/*     */     int getSize() {
/* 236 */       return this.size;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.remote.internal.ArrayNotificationBuffer
 * JD-Core Version:    0.6.2
 */