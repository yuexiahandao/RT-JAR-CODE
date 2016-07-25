/*     */ package sun.management;
/*     */ 
/*     */ import com.sun.management.GarbageCollectionNotificationInfo;
/*     */ import com.sun.management.GarbageCollectorMXBean;
/*     */ import com.sun.management.GcInfo;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.MemoryPoolMXBean;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ 
/*     */ class GarbageCollectorImpl extends MemoryManagerImpl
/*     */   implements GarbageCollectorMXBean
/*     */ {
/*  70 */   private String[] poolNames = null;
/*     */   private GcInfoBuilder gcInfoBuilder;
/*     */   private static final String notifName = "javax.management.Notification";
/* 104 */   private static final String[] gcNotifTypes = { "com.sun.management.gc.notification" };
/*     */ 
/* 108 */   private MBeanNotificationInfo[] notifInfo = null;
/*     */ 
/* 121 */   private static long seqNumber = 0L;
/*     */ 
/*     */   GarbageCollectorImpl(String paramString)
/*     */   {
/*  60 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public native long getCollectionCount();
/*     */ 
/*     */   public native long getCollectionTime();
/*     */ 
/*     */   synchronized String[] getAllPoolNames()
/*     */   {
/*  72 */     if (this.poolNames == null) {
/*  73 */       List localList = ManagementFactory.getMemoryPoolMXBeans();
/*  74 */       this.poolNames = new String[localList.size()];
/*  75 */       int i = 0;
/*  76 */       ListIterator localListIterator = localList.listIterator();
/*     */ 
/*  78 */       for (; localListIterator.hasNext(); 
/*  78 */         i++) {
/*  79 */         MemoryPoolMXBean localMemoryPoolMXBean = (MemoryPoolMXBean)localListIterator.next();
/*  80 */         this.poolNames[i] = localMemoryPoolMXBean.getName();
/*     */       }
/*     */     }
/*  83 */     return this.poolNames;
/*     */   }
/*     */ 
/*     */   private synchronized GcInfoBuilder getGcInfoBuilder()
/*     */   {
/*  90 */     if (this.gcInfoBuilder == null) {
/*  91 */       this.gcInfoBuilder = new GcInfoBuilder(this, getAllPoolNames());
/*     */     }
/*  93 */     return this.gcInfoBuilder;
/*     */   }
/*     */ 
/*     */   public GcInfo getLastGcInfo() {
/*  97 */     GcInfo localGcInfo = getGcInfoBuilder().getLastGcInfo();
/*  98 */     return localGcInfo;
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 110 */     synchronized (this) {
/* 111 */       if (this.notifInfo == null) {
/* 112 */         this.notifInfo = new MBeanNotificationInfo[1];
/* 113 */         this.notifInfo[0] = new MBeanNotificationInfo(gcNotifTypes, "javax.management.Notification", "GC Notification");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 118 */     return this.notifInfo;
/*     */   }
/*     */ 
/*     */   private static long getNextSeqNumber()
/*     */   {
/* 123 */     return ++seqNumber;
/*     */   }
/*     */ 
/*     */   void createGCNotification(long paramLong, String paramString1, String paramString2, String paramString3, GcInfo paramGcInfo)
/*     */   {
/* 132 */     if (!hasListeners()) {
/* 133 */       return;
/*     */     }
/*     */ 
/* 136 */     Notification localNotification = new Notification("com.sun.management.gc.notification", getObjectName(), getNextSeqNumber(), paramLong, paramString1);
/*     */ 
/* 141 */     GarbageCollectionNotificationInfo localGarbageCollectionNotificationInfo = new GarbageCollectionNotificationInfo(paramString1, paramString2, paramString3, paramGcInfo);
/*     */ 
/* 147 */     CompositeData localCompositeData = GarbageCollectionNotifInfoCompositeData.toCompositeData(localGarbageCollectionNotificationInfo);
/*     */ 
/* 149 */     localNotification.setUserData(localCompositeData);
/* 150 */     sendNotification(localNotification);
/*     */   }
/*     */ 
/*     */   public synchronized void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */   {
/* 157 */     boolean bool1 = hasListeners();
/* 158 */     super.addNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/* 159 */     boolean bool2 = hasListeners();
/* 160 */     if ((!bool1) && (bool2))
/* 161 */       setNotificationEnabled(this, true);
/*     */   }
/*     */ 
/*     */   public synchronized void removeNotificationListener(NotificationListener paramNotificationListener)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 167 */     boolean bool1 = hasListeners();
/* 168 */     super.removeNotificationListener(paramNotificationListener);
/* 169 */     boolean bool2 = hasListeners();
/* 170 */     if ((bool1) && (!bool2))
/* 171 */       setNotificationEnabled(this, false);
/*     */   }
/*     */ 
/*     */   public synchronized void removeNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 180 */     boolean bool1 = hasListeners();
/* 181 */     super.removeNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/* 182 */     boolean bool2 = hasListeners();
/* 183 */     if ((bool1) && (!bool2))
/* 184 */       setNotificationEnabled(this, false);
/*     */   }
/*     */ 
/*     */   public ObjectName getObjectName()
/*     */   {
/* 189 */     return Util.newObjectName("java.lang:type=GarbageCollector", getName());
/*     */   }
/*     */ 
/*     */   native void setNotificationEnabled(GarbageCollectorMXBean paramGarbageCollectorMXBean, boolean paramBoolean);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.GarbageCollectorImpl
 * JD-Core Version:    0.6.2
 */