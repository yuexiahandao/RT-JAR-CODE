/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.Executor;
/*     */ 
/*     */ public class NotificationBroadcasterSupport
/*     */   implements NotificationEmitter
/*     */ {
/* 314 */   private List<ListenerInfo> listenerList = new CopyOnWriteArrayList();
/*     */   private final Executor executor;
/*     */   private final MBeanNotificationInfo[] notifInfo;
/* 321 */   private static final Executor defaultExecutor = new Executor()
/*     */   {
/*     */     public void execute(Runnable paramAnonymousRunnable) {
/* 324 */       paramAnonymousRunnable.run();
/*     */     }
/* 321 */   };
/*     */ 
/* 328 */   private static final MBeanNotificationInfo[] NO_NOTIFICATION_INFO = new MBeanNotificationInfo[0];
/*     */ 
/* 352 */   private static final ClassLogger logger = new ClassLogger("javax.management", "NotificationBroadcasterSupport");
/*     */ 
/*     */   public NotificationBroadcasterSupport()
/*     */   {
/*  69 */     this(null, (MBeanNotificationInfo[])null);
/*     */   }
/*     */ 
/*     */   public NotificationBroadcasterSupport(Executor paramExecutor)
/*     */   {
/*  91 */     this(paramExecutor, (MBeanNotificationInfo[])null);
/*     */   }
/*     */ 
/*     */   public NotificationBroadcasterSupport(MBeanNotificationInfo[] paramArrayOfMBeanNotificationInfo)
/*     */   {
/* 116 */     this(null, paramArrayOfMBeanNotificationInfo);
/*     */   }
/*     */ 
/*     */   public NotificationBroadcasterSupport(Executor paramExecutor, MBeanNotificationInfo[] paramArrayOfMBeanNotificationInfo)
/*     */   {
/* 154 */     this.executor = (paramExecutor != null ? paramExecutor : defaultExecutor);
/*     */ 
/* 156 */     this.notifInfo = (paramArrayOfMBeanNotificationInfo == null ? NO_NOTIFICATION_INFO : (MBeanNotificationInfo[])paramArrayOfMBeanNotificationInfo.clone());
/*     */   }
/*     */ 
/*     */   public void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */   {
/* 178 */     if (paramNotificationListener == null) {
/* 179 */       throw new IllegalArgumentException("Listener can't be null");
/*     */     }
/*     */ 
/* 182 */     this.listenerList.add(new ListenerInfo(paramNotificationListener, paramNotificationFilter, paramObject));
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(NotificationListener paramNotificationListener)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 188 */     WildcardListenerInfo localWildcardListenerInfo = new WildcardListenerInfo(paramNotificationListener);
/* 189 */     boolean bool = this.listenerList.removeAll(Collections.singleton(localWildcardListenerInfo));
/*     */ 
/* 191 */     if (!bool)
/* 192 */       throw new ListenerNotFoundException("Listener not registered");
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 200 */     ListenerInfo localListenerInfo = new ListenerInfo(paramNotificationListener, paramNotificationFilter, paramObject);
/* 201 */     boolean bool = this.listenerList.remove(localListenerInfo);
/* 202 */     if (!bool)
/* 203 */       throw new ListenerNotFoundException("Listener not registered (with this filter and handback)");
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 211 */     if (this.notifInfo.length == 0) {
/* 212 */       return this.notifInfo;
/*     */     }
/* 214 */     return (MBeanNotificationInfo[])this.notifInfo.clone();
/*     */   }
/*     */ 
/*     */   public void sendNotification(Notification paramNotification)
/*     */   {
/* 228 */     if (paramNotification == null) {
/* 229 */       return;
/*     */     }
/*     */ 
/* 234 */     for (ListenerInfo localListenerInfo : this.listenerList) {
/*     */       int i;
/*     */       try { i = (localListenerInfo.filter == null) || (localListenerInfo.filter.isNotificationEnabled(paramNotification)) ? 1 : 0;
/*     */       } catch (Exception localException)
/*     */       {
/* 239 */         if (logger.debugOn()) {
/* 240 */           logger.debug("sendNotification", localException);
/*     */         }
/*     */       }
/* 243 */       continue;
/*     */ 
/* 246 */       if (i != 0)
/* 247 */         this.executor.execute(new SendNotifJob(paramNotification, localListenerInfo));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void handleNotification(NotificationListener paramNotificationListener, Notification paramNotification, Object paramObject)
/*     */   {
/* 274 */     paramNotificationListener.handleNotification(paramNotification, paramObject);
/*     */   }
/*     */ 
/*     */   private static class ListenerInfo
/*     */   {
/*     */     NotificationListener listener;
/*     */     NotificationFilter filter;
/*     */     Object handback;
/*     */ 
/*     */     ListenerInfo(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject) {
/* 286 */       this.listener = paramNotificationListener;
/* 287 */       this.filter = paramNotificationFilter;
/* 288 */       this.handback = paramObject;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 292 */       if (!(paramObject instanceof ListenerInfo))
/* 293 */         return false;
/* 294 */       ListenerInfo localListenerInfo = (ListenerInfo)paramObject;
/* 295 */       if ((localListenerInfo instanceof NotificationBroadcasterSupport.WildcardListenerInfo)) {
/* 296 */         return localListenerInfo.listener == this.listener;
/*     */       }
/* 298 */       return (localListenerInfo.listener == this.listener) && (localListenerInfo.filter == this.filter) && (localListenerInfo.handback == this.handback);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SendNotifJob
/*     */     implements Runnable
/*     */   {
/*     */     private final Notification notif;
/*     */     private final NotificationBroadcasterSupport.ListenerInfo listenerInfo;
/*     */ 
/*     */     public SendNotifJob(Notification paramListenerInfo, NotificationBroadcasterSupport.ListenerInfo arg3)
/*     */     {
/* 333 */       this.notif = paramListenerInfo;
/*     */       Object localObject;
/* 334 */       this.listenerInfo = localObject;
/*     */     }
/*     */ 
/*     */     public void run() {
/*     */       try {
/* 339 */         NotificationBroadcasterSupport.this.handleNotification(this.listenerInfo.listener, this.notif, this.listenerInfo.handback);
/*     */       }
/*     */       catch (Exception localException) {
/* 342 */         if (NotificationBroadcasterSupport.logger.debugOn())
/* 343 */           NotificationBroadcasterSupport.logger.debug("SendNotifJob-run", localException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class WildcardListenerInfo extends NotificationBroadcasterSupport.ListenerInfo
/*     */   {
/*     */     WildcardListenerInfo(NotificationListener paramNotificationListener)
/*     */     {
/* 305 */       super(null, null);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 309 */       assert (!(paramObject instanceof WildcardListenerInfo));
/* 310 */       return paramObject.equals(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.NotificationBroadcasterSupport
 * JD-Core Version:    0.6.2
 */