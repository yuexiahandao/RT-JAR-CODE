/*     */ package sun.management;
/*     */ 
/*     */ import java.lang.management.MemoryNotificationInfo;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataSupport;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ 
/*     */ public class MemoryNotifInfoCompositeData extends LazyCompositeData
/*     */ {
/*     */   private final MemoryNotificationInfo memoryNotifInfo;
/*     */   private static final CompositeType memoryNotifInfoCompositeType;
/*     */   private static final String POOL_NAME = "poolName";
/*     */   private static final String USAGE = "usage";
/*     */   private static final String COUNT = "count";
/*  90 */   private static final String[] memoryNotifInfoItemNames = { "poolName", "usage", "count" };
/*     */   private static final long serialVersionUID = -1805123446483771291L;
/*     */ 
/*     */   private MemoryNotifInfoCompositeData(MemoryNotificationInfo paramMemoryNotificationInfo)
/*     */   {
/*  44 */     this.memoryNotifInfo = paramMemoryNotificationInfo;
/*     */   }
/*     */ 
/*     */   public MemoryNotificationInfo getMemoryNotifInfo() {
/*  48 */     return this.memoryNotifInfo;
/*     */   }
/*     */ 
/*     */   public static CompositeData toCompositeData(MemoryNotificationInfo paramMemoryNotificationInfo) {
/*  52 */     MemoryNotifInfoCompositeData localMemoryNotifInfoCompositeData = new MemoryNotifInfoCompositeData(paramMemoryNotificationInfo);
/*     */ 
/*  54 */     return localMemoryNotifInfoCompositeData.getCompositeData();
/*     */   }
/*     */ 
/*     */   protected CompositeData getCompositeData()
/*     */   {
/*  60 */     Object[] arrayOfObject = { this.memoryNotifInfo.getPoolName(), MemoryUsageCompositeData.toCompositeData(this.memoryNotifInfo.getUsage()), new Long(this.memoryNotifInfo.getCount()) };
/*     */     try
/*     */     {
/*  67 */       return new CompositeDataSupport(memoryNotifInfoCompositeType, memoryNotifInfoItemNames, arrayOfObject);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException)
/*     */     {
/*  72 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getPoolName(CompositeData paramCompositeData)
/*     */   {
/*  98 */     String str = getString(paramCompositeData, "poolName");
/*  99 */     if (str == null) {
/* 100 */       throw new IllegalArgumentException("Invalid composite data: Attribute poolName has null value");
/*     */     }
/*     */ 
/* 103 */     return str;
/*     */   }
/*     */ 
/*     */   public static MemoryUsage getUsage(CompositeData paramCompositeData) {
/* 107 */     CompositeData localCompositeData = (CompositeData)paramCompositeData.get("usage");
/* 108 */     return MemoryUsage.from(localCompositeData);
/*     */   }
/*     */ 
/*     */   public static long getCount(CompositeData paramCompositeData) {
/* 112 */     return getLong(paramCompositeData, "count");
/*     */   }
/*     */ 
/*     */   public static void validateCompositeData(CompositeData paramCompositeData)
/*     */   {
/* 120 */     if (paramCompositeData == null) {
/* 121 */       throw new NullPointerException("Null CompositeData");
/*     */     }
/*     */ 
/* 124 */     if (!isTypeMatched(memoryNotifInfoCompositeType, paramCompositeData.getCompositeType()))
/* 125 */       throw new IllegalArgumentException("Unexpected composite type for MemoryNotificationInfo");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  79 */       memoryNotifInfoCompositeType = (CompositeType)MappedMXBeanType.toOpenType(MemoryNotificationInfo.class);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException)
/*     */     {
/*  83 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.MemoryNotifInfoCompositeData
 * JD-Core Version:    0.6.2
 */