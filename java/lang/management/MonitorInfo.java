/*     */ package java.lang.management;
/*     */ 
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import sun.management.MonitorInfoCompositeData;
/*     */ 
/*     */ public class MonitorInfo extends LockInfo
/*     */ {
/*     */   private int stackDepth;
/*     */   private StackTraceElement stackFrame;
/*     */ 
/*     */   public MonitorInfo(String paramString, int paramInt1, int paramInt2, StackTraceElement paramStackTraceElement)
/*     */   {
/*  66 */     super(paramString, paramInt1);
/*  67 */     if ((paramInt2 >= 0) && (paramStackTraceElement == null)) {
/*  68 */       throw new IllegalArgumentException("Parameter stackDepth is " + paramInt2 + " but stackFrame is null");
/*     */     }
/*     */ 
/*  71 */     if ((paramInt2 < 0) && (paramStackTraceElement != null)) {
/*  72 */       throw new IllegalArgumentException("Parameter stackDepth is " + paramInt2 + " but stackFrame is not null");
/*     */     }
/*     */ 
/*  75 */     this.stackDepth = paramInt2;
/*  76 */     this.stackFrame = paramStackTraceElement;
/*     */   }
/*     */ 
/*     */   public int getLockedStackDepth()
/*     */   {
/*  88 */     return this.stackDepth;
/*     */   }
/*     */ 
/*     */   public StackTraceElement getLockedStackFrame()
/*     */   {
/*  98 */     return this.stackFrame;
/*     */   }
/*     */ 
/*     */   public static MonitorInfo from(CompositeData paramCompositeData)
/*     */   {
/* 140 */     if (paramCompositeData == null) {
/* 141 */       return null;
/*     */     }
/*     */ 
/* 144 */     if ((paramCompositeData instanceof MonitorInfoCompositeData)) {
/* 145 */       return ((MonitorInfoCompositeData)paramCompositeData).getMonitorInfo();
/*     */     }
/* 147 */     MonitorInfoCompositeData.validateCompositeData(paramCompositeData);
/* 148 */     String str = MonitorInfoCompositeData.getClassName(paramCompositeData);
/* 149 */     int i = MonitorInfoCompositeData.getIdentityHashCode(paramCompositeData);
/* 150 */     int j = MonitorInfoCompositeData.getLockedStackDepth(paramCompositeData);
/* 151 */     StackTraceElement localStackTraceElement = MonitorInfoCompositeData.getLockedStackFrame(paramCompositeData);
/* 152 */     return new MonitorInfo(str, i, j, localStackTraceElement);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.management.MonitorInfo
 * JD-Core Version:    0.6.2
 */