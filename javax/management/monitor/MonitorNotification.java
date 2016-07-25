/*     */ package javax.management.monitor;
/*     */ 
/*     */ import javax.management.Notification;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class MonitorNotification extends Notification
/*     */ {
/*     */   public static final String OBSERVED_OBJECT_ERROR = "jmx.monitor.error.mbean";
/*     */   public static final String OBSERVED_ATTRIBUTE_ERROR = "jmx.monitor.error.attribute";
/*     */   public static final String OBSERVED_ATTRIBUTE_TYPE_ERROR = "jmx.monitor.error.type";
/*     */   public static final String THRESHOLD_ERROR = "jmx.monitor.error.threshold";
/*     */   public static final String RUNTIME_ERROR = "jmx.monitor.error.runtime";
/*     */   public static final String THRESHOLD_VALUE_EXCEEDED = "jmx.monitor.counter.threshold";
/*     */   public static final String THRESHOLD_HIGH_VALUE_EXCEEDED = "jmx.monitor.gauge.high";
/*     */   public static final String THRESHOLD_LOW_VALUE_EXCEEDED = "jmx.monitor.gauge.low";
/*     */   public static final String STRING_TO_COMPARE_VALUE_MATCHED = "jmx.monitor.string.matches";
/*     */   public static final String STRING_TO_COMPARE_VALUE_DIFFERED = "jmx.monitor.string.differs";
/*     */   private static final long serialVersionUID = -4608189663661929204L;
/* 165 */   private ObjectName observedObject = null;
/*     */ 
/* 170 */   private String observedAttribute = null;
/*     */ 
/* 175 */   private Object derivedGauge = null;
/*     */ 
/* 182 */   private Object trigger = null;
/*     */ 
/*     */   MonitorNotification(String paramString1, Object paramObject1, long paramLong1, long paramLong2, String paramString2, ObjectName paramObjectName, String paramString3, Object paramObject2, Object paramObject3)
/*     */   {
/* 207 */     super(paramString1, paramObject1, paramLong1, paramLong2, paramString2);
/* 208 */     this.observedObject = paramObjectName;
/* 209 */     this.observedAttribute = paramString3;
/* 210 */     this.derivedGauge = paramObject2;
/* 211 */     this.trigger = paramObject3;
/*     */   }
/*     */ 
/*     */   public ObjectName getObservedObject()
/*     */   {
/* 229 */     return this.observedObject;
/*     */   }
/*     */ 
/*     */   public String getObservedAttribute()
/*     */   {
/* 238 */     return this.observedAttribute;
/*     */   }
/*     */ 
/*     */   public Object getDerivedGauge()
/*     */   {
/* 247 */     return this.derivedGauge;
/*     */   }
/*     */ 
/*     */   public Object getTrigger()
/*     */   {
/* 256 */     return this.trigger;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.monitor.MonitorNotification
 * JD-Core Version:    0.6.2
 */