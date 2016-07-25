/*     */ package sun.management;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.MemoryMXBean;
/*     */ import java.lang.management.MemoryManagerMXBean;
/*     */ import java.lang.management.MemoryNotificationInfo;
/*     */ import java.lang.management.MemoryPoolMXBean;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.Notification;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import sun.misc.VM;
/*     */ 
/*     */ class MemoryImpl extends NotificationEmitterSupport
/*     */   implements MemoryMXBean
/*     */ {
/*     */   private final VMManagement jvm;
/*  51 */   private static MemoryPoolMXBean[] pools = null;
/*  52 */   private static MemoryManagerMXBean[] mgrs = null;
/*     */   private static final String notifName = "javax.management.Notification";
/* 109 */   private static final String[] notifTypes = { "java.management.memory.threshold.exceeded", "java.management.memory.collection.threshold.exceeded" };
/*     */ 
/* 113 */   private static final String[] notifMsgs = { "Memory usage exceeds usage threshold", "Memory usage exceeds collection usage threshold" };
/*     */ 
/* 118 */   private MBeanNotificationInfo[] notifInfo = null;
/*     */ 
/* 140 */   private static long seqNumber = 0L;
/*     */ 
/*     */   MemoryImpl(VMManagement paramVMManagement)
/*     */   {
/*  58 */     this.jvm = paramVMManagement;
/*     */   }
/*     */ 
/*     */   public int getObjectPendingFinalizationCount() {
/*  62 */     return VM.getFinalRefCount();
/*     */   }
/*     */ 
/*     */   public void gc() {
/*  66 */     Runtime.getRuntime().gc();
/*     */   }
/*     */ 
/*     */   public MemoryUsage getHeapMemoryUsage()
/*     */   {
/*  71 */     return getMemoryUsage0(true);
/*     */   }
/*     */ 
/*     */   public MemoryUsage getNonHeapMemoryUsage() {
/*  75 */     return getMemoryUsage0(false);
/*     */   }
/*     */ 
/*     */   public boolean isVerbose() {
/*  79 */     return this.jvm.getVerboseGC();
/*     */   }
/*     */ 
/*     */   public void setVerbose(boolean paramBoolean) {
/*  83 */     Util.checkControlAccess();
/*     */ 
/*  85 */     setVerboseGC(paramBoolean);
/*     */   }
/*     */ 
/*     */   static synchronized MemoryPoolMXBean[] getMemoryPools()
/*     */   {
/*  91 */     if (pools == null) {
/*  92 */       pools = getMemoryPools0();
/*     */     }
/*  94 */     return pools;
/*     */   }
/*     */   static synchronized MemoryManagerMXBean[] getMemoryManagers() {
/*  97 */     if (mgrs == null) {
/*  98 */       mgrs = getMemoryManagers0();
/*     */     }
/* 100 */     return mgrs;
/*     */   }
/*     */ 
/*     */   private static native MemoryPoolMXBean[] getMemoryPools0();
/*     */ 
/*     */   private static native MemoryManagerMXBean[] getMemoryManagers0();
/*     */ 
/*     */   private native MemoryUsage getMemoryUsage0(boolean paramBoolean);
/*     */ 
/*     */   private native void setVerboseGC(boolean paramBoolean);
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 120 */     synchronized (this) {
/* 121 */       if (this.notifInfo == null) {
/* 122 */         this.notifInfo = new MBeanNotificationInfo[1];
/* 123 */         this.notifInfo[0] = new MBeanNotificationInfo(notifTypes, "javax.management.Notification", "Memory Notification");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 128 */     return this.notifInfo;
/*     */   }
/*     */ 
/*     */   private static String getNotifMsg(String paramString) {
/* 132 */     for (int i = 0; i < notifTypes.length; i++) {
/* 133 */       if (paramString == notifTypes[i]) {
/* 134 */         return notifMsgs[i];
/*     */       }
/*     */     }
/* 137 */     return "Unknown message";
/*     */   }
/*     */ 
/*     */   private static long getNextSeqNumber()
/*     */   {
/* 142 */     return ++seqNumber;
/*     */   }
/*     */ 
/*     */   static void createNotification(String paramString1, String paramString2, MemoryUsage paramMemoryUsage, long paramLong)
/*     */   {
/* 149 */     MemoryImpl localMemoryImpl = (MemoryImpl)ManagementFactory.getMemoryMXBean();
/* 150 */     if (!localMemoryImpl.hasListeners())
/*     */     {
/* 152 */       return;
/*     */     }
/* 154 */     long l = System.currentTimeMillis();
/* 155 */     String str = getNotifMsg(paramString1);
/* 156 */     Notification localNotification = new Notification(paramString1, localMemoryImpl.getObjectName(), getNextSeqNumber(), l, str);
/*     */ 
/* 161 */     MemoryNotificationInfo localMemoryNotificationInfo = new MemoryNotificationInfo(paramString2, paramMemoryUsage, paramLong);
/*     */ 
/* 165 */     CompositeData localCompositeData = MemoryNotifInfoCompositeData.toCompositeData(localMemoryNotificationInfo);
/*     */ 
/* 167 */     localNotification.setUserData(localCompositeData);
/* 168 */     localMemoryImpl.sendNotification(localNotification);
/*     */   }
/*     */ 
/*     */   public ObjectName getObjectName() {
/* 172 */     return Util.newObjectName("java.lang:type=Memory");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.MemoryImpl
 * JD-Core Version:    0.6.2
 */