/*     */ package sun.management;
/*     */ 
/*     */ import java.lang.management.MonitorInfo;
/*     */ import java.util.Set;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import javax.management.openmbean.CompositeDataSupport;
/*     */ import javax.management.openmbean.CompositeType;
/*     */ import javax.management.openmbean.OpenDataException;
/*     */ 
/*     */ public class MonitorInfoCompositeData extends LazyCompositeData
/*     */ {
/*     */   private final MonitorInfo lock;
/*     */   private static final CompositeType monitorInfoCompositeType;
/*     */   private static final String[] monitorInfoItemNames;
/*     */   private static final String CLASS_NAME = "className";
/*     */   private static final String IDENTITY_HASH_CODE = "identityHashCode";
/*     */   private static final String LOCKED_STACK_FRAME = "lockedStackFrame";
/*     */   private static final String LOCKED_STACK_DEPTH = "lockedStackDepth";
/*     */   private static final long serialVersionUID = -5825215591822908529L;
/*     */ 
/*     */   private MonitorInfoCompositeData(MonitorInfo paramMonitorInfo)
/*     */   {
/*  44 */     this.lock = paramMonitorInfo;
/*     */   }
/*     */ 
/*     */   public MonitorInfo getMonitorInfo() {
/*  48 */     return this.lock;
/*     */   }
/*     */ 
/*     */   public static CompositeData toCompositeData(MonitorInfo paramMonitorInfo) {
/*  52 */     MonitorInfoCompositeData localMonitorInfoCompositeData = new MonitorInfoCompositeData(paramMonitorInfo);
/*  53 */     return localMonitorInfoCompositeData.getCompositeData();
/*     */   }
/*     */ 
/*     */   protected CompositeData getCompositeData()
/*     */   {
/*  60 */     int i = monitorInfoItemNames.length;
/*  61 */     Object[] arrayOfObject = new Object[i];
/*  62 */     CompositeData localCompositeData = LockDataConverter.toLockInfoCompositeData(this.lock);
/*     */ 
/*  64 */     for (int j = 0; j < i; j++) {
/*  65 */       String str = monitorInfoItemNames[j];
/*  66 */       if (str.equals("lockedStackFrame")) {
/*  67 */         StackTraceElement localStackTraceElement = this.lock.getLockedStackFrame();
/*  68 */         arrayOfObject[j] = (localStackTraceElement != null ? StackTraceElementCompositeData.toCompositeData(localStackTraceElement) : null);
/*     */       }
/*  71 */       else if (str.equals("lockedStackDepth")) {
/*  72 */         arrayOfObject[j] = new Integer(this.lock.getLockedStackDepth());
/*     */       } else {
/*  74 */         arrayOfObject[j] = localCompositeData.get(str);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/*  79 */       return new CompositeDataSupport(monitorInfoCompositeType, monitorInfoItemNames, arrayOfObject);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException)
/*     */     {
/*  84 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static CompositeType getMonitorInfoCompositeType()
/*     */   {
/* 103 */     return monitorInfoCompositeType;
/*     */   }
/*     */ 
/*     */   public static String getClassName(CompositeData paramCompositeData)
/*     */   {
/* 112 */     return getString(paramCompositeData, "className");
/*     */   }
/*     */ 
/*     */   public static int getIdentityHashCode(CompositeData paramCompositeData) {
/* 116 */     return getInt(paramCompositeData, "identityHashCode");
/*     */   }
/*     */ 
/*     */   public static StackTraceElement getLockedStackFrame(CompositeData paramCompositeData) {
/* 120 */     CompositeData localCompositeData = (CompositeData)paramCompositeData.get("lockedStackFrame");
/* 121 */     if (localCompositeData != null) {
/* 122 */       return StackTraceElementCompositeData.from(localCompositeData);
/*     */     }
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   public static int getLockedStackDepth(CompositeData paramCompositeData)
/*     */   {
/* 129 */     return getInt(paramCompositeData, "lockedStackDepth");
/*     */   }
/*     */ 
/*     */   public static void validateCompositeData(CompositeData paramCompositeData)
/*     */   {
/* 137 */     if (paramCompositeData == null) {
/* 138 */       throw new NullPointerException("Null CompositeData");
/*     */     }
/*     */ 
/* 141 */     if (!isTypeMatched(monitorInfoCompositeType, paramCompositeData.getCompositeType()))
/* 142 */       throw new IllegalArgumentException("Unexpected composite type for MonitorInfo");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  92 */       monitorInfoCompositeType = (CompositeType)MappedMXBeanType.toOpenType(MonitorInfo.class);
/*     */ 
/*  94 */       Set localSet = monitorInfoCompositeType.keySet();
/*  95 */       monitorInfoItemNames = (String[])localSet.toArray(new String[0]);
/*     */     }
/*     */     catch (OpenDataException localOpenDataException) {
/*  98 */       throw new AssertionError(localOpenDataException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.MonitorInfoCompositeData
 * JD-Core Version:    0.6.2
 */