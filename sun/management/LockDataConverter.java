/*     */ package sun.management;
/*     */ 
/*     */ import java.lang.management.LockInfo;
/*     */ import java.lang.management.ThreadInfo;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.StandardMBean;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ 
/*     */ class LockDataConverter extends StandardMBean
/*     */   implements LockDataConverterMXBean
/*     */ {
/*     */   private LockInfo lockInfo;
/*     */   private LockInfo[] lockedSyncs;
/*     */ 
/*     */   private LockDataConverter()
/*     */   {
/*  46 */     super(LockDataConverterMXBean.class, true);
/*  47 */     this.lockInfo = null;
/*  48 */     this.lockedSyncs = null;
/*     */   }
/*     */ 
/*     */   private LockDataConverter(ThreadInfo paramThreadInfo) {
/*  52 */     super(LockDataConverterMXBean.class, true);
/*  53 */     this.lockInfo = paramThreadInfo.getLockInfo();
/*  54 */     this.lockedSyncs = paramThreadInfo.getLockedSynchronizers();
/*     */   }
/*     */ 
/*     */   public void setLockInfo(LockInfo paramLockInfo) {
/*  58 */     this.lockInfo = paramLockInfo;
/*     */   }
/*     */ 
/*     */   public LockInfo getLockInfo() {
/*  62 */     return this.lockInfo;
/*     */   }
/*     */ 
/*     */   public void setLockedSynchronizers(LockInfo[] paramArrayOfLockInfo) {
/*  66 */     this.lockedSyncs = paramArrayOfLockInfo;
/*     */   }
/*     */ 
/*     */   public LockInfo[] getLockedSynchronizers() {
/*  70 */     return this.lockedSyncs;
/*     */   }
/*     */ 
/*     */   CompositeData toLockInfoCompositeData()
/*     */   {
/*     */     try {
/*  76 */       return (CompositeData)getAttribute("LockInfo");
/*     */     } catch (Exception localException) {
/*  78 */       throw new AssertionError(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   CompositeData[] toLockedSynchronizersCompositeData() {
/*     */     try {
/*  84 */       return (CompositeData[])getAttribute("LockedSynchronizers");
/*     */     } catch (Exception localException) {
/*  86 */       throw new AssertionError(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   LockInfo toLockInfo(CompositeData paramCompositeData) {
/*     */     try {
/*  92 */       setAttribute(new Attribute("LockInfo", paramCompositeData));
/*     */     } catch (Exception localException) {
/*  94 */       throw new AssertionError(localException);
/*     */     }
/*  96 */     return getLockInfo();
/*     */   }
/*     */ 
/*     */   LockInfo[] toLockedSynchronizers(CompositeData[] paramArrayOfCompositeData) {
/*     */     try {
/* 101 */       setAttribute(new Attribute("LockedSynchronizers", paramArrayOfCompositeData));
/*     */     } catch (Exception localException) {
/* 103 */       throw new AssertionError(localException);
/*     */     }
/* 105 */     return getLockedSynchronizers();
/*     */   }
/*     */ 
/*     */   static CompositeData toLockInfoCompositeData(LockInfo paramLockInfo) {
/* 109 */     LockDataConverter localLockDataConverter = newLockDataConverter();
/* 110 */     localLockDataConverter.setLockInfo(paramLockInfo);
/* 111 */     return localLockDataConverter.toLockInfoCompositeData();
/*     */   }
/*     */ 
/*     */   static LockDataConverter newLockDataConverter() {
/* 115 */     return (LockDataConverter)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public LockDataConverter run() {
/* 117 */         return new LockDataConverter(null);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static LockDataConverter newLockDataConverter(ThreadInfo paramThreadInfo) {
/* 123 */     LockDataConverter localLockDataConverter = newLockDataConverter();
/* 124 */     localLockDataConverter.lockInfo = paramThreadInfo.getLockInfo();
/* 125 */     localLockDataConverter.lockedSyncs = paramThreadInfo.getLockedSynchronizers();
/* 126 */     return localLockDataConverter;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.LockDataConverter
 * JD-Core Version:    0.6.2
 */