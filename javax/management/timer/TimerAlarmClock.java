/*      */ package javax.management.timer;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import java.util.Date;
/*      */ import java.util.TimerTask;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ class TimerAlarmClock extends TimerTask
/*      */ {
/* 1252 */   Timer listener = null;
/* 1253 */   long timeout = 10000L;
/* 1254 */   Date next = null;
/*      */ 
/*      */   public TimerAlarmClock(Timer paramTimer, long paramLong)
/*      */   {
/* 1263 */     this.listener = paramTimer;
/* 1264 */     this.timeout = Math.max(0L, paramLong);
/*      */   }
/*      */ 
/*      */   public TimerAlarmClock(Timer paramTimer, Date paramDate) {
/* 1268 */     this.listener = paramTimer;
/* 1269 */     this.next = paramDate;
/*      */   }
/*      */ 
/*      */   public void run()
/*      */   {
/*      */     try
/*      */     {
/* 1285 */       TimerAlarmClockNotification localTimerAlarmClockNotification = new TimerAlarmClockNotification(this);
/* 1286 */       this.listener.notifyAlarmClock(localTimerAlarmClockNotification);
/*      */     } catch (Exception localException) {
/* 1288 */       JmxProperties.TIMER_LOGGER.logp(Level.FINEST, Timer.class.getName(), "run", "Got unexpected exception when sending a notification", localException);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.timer.TimerAlarmClock
 * JD-Core Version:    0.6.2
 */