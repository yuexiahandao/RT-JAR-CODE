/*    */ package sun.management;
/*    */ 
/*    */ import java.lang.management.MemoryManagerMXBean;
/*    */ import java.lang.management.MemoryPoolMXBean;
/*    */ import javax.management.MBeanNotificationInfo;
/*    */ import javax.management.ObjectName;
/*    */ 
/*    */ class MemoryManagerImpl extends NotificationEmitterSupport
/*    */   implements MemoryManagerMXBean
/*    */ {
/*    */   private final String name;
/*    */   private final boolean isValid;
/*    */   private MemoryPoolMXBean[] pools;
/* 81 */   private MBeanNotificationInfo[] notifInfo = null;
/*    */ 
/*    */   MemoryManagerImpl(String paramString)
/*    */   {
/* 50 */     this.name = paramString;
/* 51 */     this.isValid = true;
/* 52 */     this.pools = null;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 56 */     return this.name;
/*    */   }
/*    */ 
/*    */   public boolean isValid() {
/* 60 */     return this.isValid;
/*    */   }
/*    */ 
/*    */   public String[] getMemoryPoolNames() {
/* 64 */     MemoryPoolMXBean[] arrayOfMemoryPoolMXBean = getMemoryPools();
/*    */ 
/* 66 */     String[] arrayOfString = new String[arrayOfMemoryPoolMXBean.length];
/* 67 */     for (int i = 0; i < arrayOfMemoryPoolMXBean.length; i++) {
/* 68 */       arrayOfString[i] = arrayOfMemoryPoolMXBean[i].getName();
/*    */     }
/* 70 */     return arrayOfString;
/*    */   }
/*    */ 
/*    */   synchronized MemoryPoolMXBean[] getMemoryPools() {
/* 74 */     if (this.pools == null) {
/* 75 */       this.pools = getMemoryPools0();
/*    */     }
/* 77 */     return this.pools;
/*    */   }
/*    */ 
/*    */   private native MemoryPoolMXBean[] getMemoryPools0();
/*    */ 
/*    */   public MBeanNotificationInfo[] getNotificationInfo() {
/* 83 */     synchronized (this) {
/* 84 */       if (this.notifInfo == null) {
/* 85 */         this.notifInfo = new MBeanNotificationInfo[0];
/*    */       }
/*    */     }
/* 88 */     return this.notifInfo;
/*    */   }
/*    */ 
/*    */   public ObjectName getObjectName() {
/* 92 */     return Util.newObjectName("java.lang:type=MemoryManager", getName());
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.MemoryManagerImpl
 * JD-Core Version:    0.6.2
 */