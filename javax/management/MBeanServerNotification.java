/*     */ package javax.management;
/*     */ 
/*     */ public class MBeanServerNotification extends Notification
/*     */ {
/*     */   private static final long serialVersionUID = 2876477500475969677L;
/*     */   public static final String REGISTRATION_NOTIFICATION = "JMX.mbean.registered";
/*     */   public static final String UNREGISTRATION_NOTIFICATION = "JMX.mbean.unregistered";
/*     */   private final ObjectName objectName;
/*     */ 
/*     */   public MBeanServerNotification(String paramString, Object paramObject, long paramLong, ObjectName paramObjectName)
/*     */   {
/* 139 */     super(paramString, paramObject, paramLong);
/* 140 */     this.objectName = paramObjectName;
/*     */   }
/*     */ 
/*     */   public ObjectName getMBeanName()
/*     */   {
/* 149 */     return this.objectName;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     return super.toString() + "[mbeanName=" + this.objectName + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanServerNotification
 * JD-Core Version:    0.6.2
 */