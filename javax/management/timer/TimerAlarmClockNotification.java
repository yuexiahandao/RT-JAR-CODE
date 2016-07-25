/*    */ package javax.management.timer;
/*    */ 
/*    */ import javax.management.Notification;
/*    */ 
/*    */ class TimerAlarmClockNotification extends Notification
/*    */ {
/*    */   private static final long serialVersionUID = -4841061275673620641L;
/*    */ 
/*    */   public TimerAlarmClockNotification(TimerAlarmClock paramTimerAlarmClock)
/*    */   {
/* 50 */     super("", paramTimerAlarmClock, 0L);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.timer.TimerAlarmClockNotification
 * JD-Core Version:    0.6.2
 */