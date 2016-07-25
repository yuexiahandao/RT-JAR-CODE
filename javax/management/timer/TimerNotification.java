/*     */ package javax.management.timer;
/*     */ 
/*     */ import javax.management.Notification;
/*     */ 
/*     */ public class TimerNotification extends Notification
/*     */ {
/*     */   private static final long serialVersionUID = 1798492029603825750L;
/*     */   private Integer notificationID;
/*     */ 
/*     */   public TimerNotification(String paramString1, Object paramObject, long paramLong1, long paramLong2, String paramString2, Integer paramInteger)
/*     */   {
/*  75 */     super(paramString1, paramObject, paramLong1, paramLong2, paramString2);
/*  76 */     this.notificationID = paramInteger;
/*     */   }
/*     */ 
/*     */   public Integer getNotificationID()
/*     */   {
/*  94 */     return this.notificationID;
/*     */   }
/*     */ 
/*     */   Object cloneTimerNotification()
/*     */   {
/* 109 */     TimerNotification localTimerNotification = new TimerNotification(getType(), getSource(), getSequenceNumber(), getTimeStamp(), getMessage(), this.notificationID);
/*     */ 
/* 111 */     localTimerNotification.setUserData(getUserData());
/* 112 */     return localTimerNotification;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.timer.TimerNotification
 * JD-Core Version:    0.6.2
 */