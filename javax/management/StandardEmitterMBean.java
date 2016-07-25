/*     */ package javax.management;
/*     */ 
/*     */ public class StandardEmitterMBean extends StandardMBean
/*     */   implements NotificationEmitter
/*     */ {
/*  67 */   private static final MBeanNotificationInfo[] NO_NOTIFICATION_INFO = new MBeanNotificationInfo[0];
/*     */   private final NotificationEmitter emitter;
/*     */   private final MBeanNotificationInfo[] notificationInfo;
/*     */ 
/*     */   public <T> StandardEmitterMBean(T paramT, Class<T> paramClass, NotificationEmitter paramNotificationEmitter)
/*     */   {
/* 105 */     this(paramT, paramClass, false, paramNotificationEmitter);
/*     */   }
/*     */ 
/*     */   public <T> StandardEmitterMBean(T paramT, Class<T> paramClass, boolean paramBoolean, NotificationEmitter paramNotificationEmitter)
/*     */   {
/* 146 */     super(paramT, paramClass, paramBoolean);
/* 147 */     if (paramNotificationEmitter == null)
/* 148 */       throw new IllegalArgumentException("Null emitter");
/* 149 */     this.emitter = paramNotificationEmitter;
/* 150 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = paramNotificationEmitter.getNotificationInfo();
/* 151 */     if ((arrayOfMBeanNotificationInfo == null) || (arrayOfMBeanNotificationInfo.length == 0))
/* 152 */       this.notificationInfo = NO_NOTIFICATION_INFO;
/*     */     else
/* 154 */       this.notificationInfo = ((MBeanNotificationInfo[])arrayOfMBeanNotificationInfo.clone());
/*     */   }
/*     */ 
/*     */   protected StandardEmitterMBean(Class<?> paramClass, NotificationEmitter paramNotificationEmitter)
/*     */   {
/* 191 */     this(paramClass, false, paramNotificationEmitter);
/*     */   }
/*     */ 
/*     */   protected StandardEmitterMBean(Class<?> paramClass, boolean paramBoolean, NotificationEmitter paramNotificationEmitter)
/*     */   {
/* 230 */     super(paramClass, paramBoolean);
/* 231 */     if (paramNotificationEmitter == null)
/* 232 */       throw new IllegalArgumentException("Null emitter");
/* 233 */     this.emitter = paramNotificationEmitter;
/* 234 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = paramNotificationEmitter.getNotificationInfo();
/* 235 */     if ((arrayOfMBeanNotificationInfo == null) || (arrayOfMBeanNotificationInfo.length == 0))
/* 236 */       this.notificationInfo = NO_NOTIFICATION_INFO;
/*     */     else
/* 238 */       this.notificationInfo = ((MBeanNotificationInfo[])arrayOfMBeanNotificationInfo.clone());
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(NotificationListener paramNotificationListener)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 244 */     this.emitter.removeNotificationListener(paramNotificationListener);
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 251 */     this.emitter.removeNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */   {
/* 257 */     this.emitter.addNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 263 */     if (this.notificationInfo == null) {
/* 264 */       return NO_NOTIFICATION_INFO;
/*     */     }
/* 266 */     if (this.notificationInfo.length == 0) {
/* 267 */       return this.notificationInfo;
/*     */     }
/* 269 */     return (MBeanNotificationInfo[])this.notificationInfo.clone();
/*     */   }
/*     */ 
/*     */   public void sendNotification(Notification paramNotification)
/*     */   {
/* 288 */     if ((this.emitter instanceof NotificationBroadcasterSupport)) {
/* 289 */       ((NotificationBroadcasterSupport)this.emitter).sendNotification(paramNotification);
/*     */     } else {
/* 291 */       String str = "Cannot sendNotification when emitter is not an instance of NotificationBroadcasterSupport: " + this.emitter.getClass().getName();
/*     */ 
/* 295 */       throw new ClassCastException(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   MBeanNotificationInfo[] getNotifications(MBeanInfo paramMBeanInfo)
/*     */   {
/* 311 */     return getNotificationInfo();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.StandardEmitterMBean
 * JD-Core Version:    0.6.2
 */