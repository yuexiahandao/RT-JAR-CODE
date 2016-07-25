/*      */ package javax.management.timer;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.InstanceNotFoundException;
/*      */ import javax.management.MBeanNotificationInfo;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.NotificationBroadcasterSupport;
/*      */ import javax.management.ObjectName;
/*      */ 
/*      */ public class Timer extends NotificationBroadcasterSupport
/*      */   implements TimerMBean, MBeanRegistration
/*      */ {
/*      */   public static final long ONE_SECOND = 1000L;
/*      */   public static final long ONE_MINUTE = 60000L;
/*      */   public static final long ONE_HOUR = 3600000L;
/*      */   public static final long ONE_DAY = 86400000L;
/*      */   public static final long ONE_WEEK = 604800000L;
/*  131 */   private Map<Integer, Object[]> timerTable = new Hashtable();
/*      */ 
/*  139 */   private boolean sendPastNotifications = false;
/*      */ 
/*  145 */   private transient boolean isActive = false;
/*      */ 
/*  151 */   private transient long sequenceNumber = 0L;
/*      */   private static final int TIMER_NOTIF_INDEX = 0;
/*      */   private static final int TIMER_DATE_INDEX = 1;
/*      */   private static final int TIMER_PERIOD_INDEX = 2;
/*      */   private static final int TIMER_NB_OCCUR_INDEX = 3;
/*      */   private static final int ALARM_CLOCK_INDEX = 4;
/*      */   private static final int FIXED_RATE_INDEX = 5;
/*  166 */   private int counterID = 0;
/*      */   private java.util.Timer timer;
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/*  203 */     return paramObjectName;
/*      */   }
/*      */ 
/*      */   public void postRegister(Boolean paramBoolean)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {
/*  225 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "preDeregister", "stop the timer");
/*      */ 
/*  230 */     stop();
/*      */   }
/*      */ 
/*      */   public void postDeregister()
/*      */   {
/*      */   }
/*      */ 
/*      */   public synchronized MBeanNotificationInfo[] getNotificationInfo()
/*      */   {
/*  251 */     TreeSet localTreeSet = new TreeSet();
/*  252 */     for (Object localObject = this.timerTable.values().iterator(); ((Iterator)localObject).hasNext(); ) { Object[] arrayOfObject = (Object[])((Iterator)localObject).next();
/*  253 */       TimerNotification localTimerNotification = (TimerNotification)arrayOfObject[0];
/*      */ 
/*  255 */       localTreeSet.add(localTimerNotification.getType());
/*      */     }
/*  257 */     localObject = (String[])localTreeSet.toArray(new String[0]);
/*      */ 
/*  259 */     return new MBeanNotificationInfo[] { new MBeanNotificationInfo((String[])localObject, TimerNotification.class.getName(), "Notification sent by Timer MBean") };
/*      */   }
/*      */ 
/*      */   public synchronized void start()
/*      */   {
/*  277 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "start", "starting the timer");
/*      */ 
/*  282 */     if (!this.isActive)
/*      */     {
/*  284 */       this.timer = new java.util.Timer();
/*      */ 
/*  289 */       Date localDate2 = new Date();
/*      */ 
/*  295 */       sendPastNotifications(localDate2, this.sendPastNotifications);
/*      */ 
/*  300 */       for (Object[] arrayOfObject : this.timerTable.values())
/*      */       {
/*  304 */         Date localDate1 = (Date)arrayOfObject[1];
/*      */ 
/*  308 */         boolean bool = ((Boolean)arrayOfObject[5]).booleanValue();
/*      */         TimerAlarmClock localTimerAlarmClock;
/*  309 */         if (bool)
/*      */         {
/*  311 */           localTimerAlarmClock = new TimerAlarmClock(this, localDate1);
/*  312 */           arrayOfObject[4] = localTimerAlarmClock;
/*  313 */           this.timer.schedule(localTimerAlarmClock, localTimerAlarmClock.next);
/*      */         }
/*      */         else
/*      */         {
/*  317 */           localTimerAlarmClock = new TimerAlarmClock(this, localDate1.getTime() - localDate2.getTime());
/*  318 */           arrayOfObject[4] = localTimerAlarmClock;
/*  319 */           this.timer.schedule(localTimerAlarmClock, localTimerAlarmClock.timeout);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  325 */       this.isActive = true;
/*      */ 
/*  327 */       JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "start", "timer started");
/*      */     }
/*      */     else {
/*  330 */       JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "start", "the timer is already activated");
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void stop()
/*      */   {
/*  340 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "stop", "stopping the timer");
/*      */ 
/*  345 */     if (this.isActive == true)
/*      */     {
/*  347 */       for (Object[] arrayOfObject : this.timerTable.values())
/*      */       {
/*  351 */         TimerAlarmClock localTimerAlarmClock = (TimerAlarmClock)arrayOfObject[4];
/*  352 */         if (localTimerAlarmClock != null)
/*      */         {
/*  364 */           localTimerAlarmClock.cancel();
/*      */         }
/*      */       }
/*      */ 
/*  368 */       this.timer.cancel();
/*      */ 
/*  372 */       this.isActive = false;
/*      */ 
/*  374 */       JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "stop", "timer stopped");
/*      */     }
/*      */     else {
/*  377 */       JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "stop", "the timer is already deactivated");
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized Integer addNotification(String paramString1, String paramString2, Object paramObject, Date paramDate, long paramLong1, long paramLong2, boolean paramBoolean)
/*      */     throws IllegalArgumentException
/*      */   {
/*  427 */     if (paramDate == null) {
/*  428 */       throw new IllegalArgumentException("Timer notification date cannot be null.");
/*      */     }
/*      */ 
/*  437 */     if ((paramLong1 < 0L) || (paramLong2 < 0L)) {
/*  438 */       throw new IllegalArgumentException("Negative values for the periodicity");
/*      */     }
/*      */ 
/*  441 */     Date localDate1 = new Date();
/*      */ 
/*  445 */     if (localDate1.after(paramDate))
/*      */     {
/*  447 */       paramDate.setTime(localDate1.getTime());
/*  448 */       if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
/*  449 */         JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "addNotification", "update timer notification to add with:\n\tNotification date = " + paramDate);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  458 */     Integer localInteger = Integer.valueOf(++this.counterID);
/*      */ 
/*  463 */     TimerNotification localTimerNotification = new TimerNotification(paramString1, this, 0L, 0L, paramString2, localInteger);
/*  464 */     localTimerNotification.setUserData(paramObject);
/*      */ 
/*  466 */     Object[] arrayOfObject = new Object[6];
/*      */     TimerAlarmClock localTimerAlarmClock;
/*  469 */     if (paramBoolean)
/*      */     {
/*  471 */       localTimerAlarmClock = new TimerAlarmClock(this, paramDate);
/*      */     }
/*      */     else
/*      */     {
/*  475 */       localTimerAlarmClock = new TimerAlarmClock(this, paramDate.getTime() - localDate1.getTime());
/*      */     }
/*      */ 
/*  481 */     Date localDate2 = new Date(paramDate.getTime());
/*      */ 
/*  483 */     arrayOfObject[0] = localTimerNotification;
/*  484 */     arrayOfObject[1] = localDate2;
/*  485 */     arrayOfObject[2] = Long.valueOf(paramLong1);
/*  486 */     arrayOfObject[3] = Long.valueOf(paramLong2);
/*  487 */     arrayOfObject[4] = localTimerAlarmClock;
/*  488 */     arrayOfObject[5] = Boolean.valueOf(paramBoolean);
/*      */ 
/*  490 */     if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
/*  491 */       StringBuilder localStringBuilder = new StringBuilder().append("adding timer notification:\n\t").append("Notification source = ").append(localTimerNotification.getSource()).append("\n\tNotification type = ").append(localTimerNotification.getType()).append("\n\tNotification ID = ").append(localInteger).append("\n\tNotification date = ").append(localDate2).append("\n\tNotification period = ").append(paramLong1).append("\n\tNotification nb of occurrences = ").append(paramLong2).append("\n\tNotification executes at fixed rate = ").append(paramBoolean);
/*      */ 
/*  507 */       JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "addNotification", localStringBuilder.toString());
/*      */     }
/*      */ 
/*  511 */     this.timerTable.put(localInteger, arrayOfObject);
/*      */ 
/*  515 */     if (this.isActive == true) {
/*  516 */       if (paramBoolean)
/*      */       {
/*  518 */         this.timer.schedule(localTimerAlarmClock, localTimerAlarmClock.next);
/*      */       }
/*      */       else
/*      */       {
/*  522 */         this.timer.schedule(localTimerAlarmClock, localTimerAlarmClock.timeout);
/*      */       }
/*      */     }
/*      */ 
/*  526 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "addNotification", "timer notification added");
/*      */ 
/*  528 */     return localInteger;
/*      */   }
/*      */ 
/*      */   public synchronized Integer addNotification(String paramString1, String paramString2, Object paramObject, Date paramDate, long paramLong1, long paramLong2)
/*      */     throws IllegalArgumentException
/*      */   {
/*  572 */     return addNotification(paramString1, paramString2, paramObject, paramDate, paramLong1, paramLong2, false);
/*      */   }
/*      */ 
/*      */   public synchronized Integer addNotification(String paramString1, String paramString2, Object paramObject, Date paramDate, long paramLong)
/*      */     throws IllegalArgumentException
/*      */   {
/*  610 */     return addNotification(paramString1, paramString2, paramObject, paramDate, paramLong, 0L);
/*      */   }
/*      */ 
/*      */   public synchronized Integer addNotification(String paramString1, String paramString2, Object paramObject, Date paramDate)
/*      */     throws IllegalArgumentException
/*      */   {
/*  642 */     return addNotification(paramString1, paramString2, paramObject, paramDate, 0L, 0L);
/*      */   }
/*      */ 
/*      */   public synchronized void removeNotification(Integer paramInteger)
/*      */     throws InstanceNotFoundException
/*      */   {
/*  657 */     if (!this.timerTable.containsKey(paramInteger)) {
/*  658 */       throw new InstanceNotFoundException("Timer notification to remove not in the list of notifications");
/*      */     }
/*      */ 
/*  663 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/*  664 */     TimerAlarmClock localTimerAlarmClock = (TimerAlarmClock)arrayOfObject[4];
/*  665 */     if (localTimerAlarmClock != null)
/*      */     {
/*  676 */       localTimerAlarmClock.cancel();
/*      */     }
/*      */ 
/*  681 */     if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
/*  682 */       StringBuilder localStringBuilder = new StringBuilder().append("removing timer notification:").append("\n\tNotification source = ").append(((TimerNotification)arrayOfObject[0]).getSource()).append("\n\tNotification type = ").append(((TimerNotification)arrayOfObject[0]).getType()).append("\n\tNotification ID = ").append(((TimerNotification)arrayOfObject[0]).getNotificationID()).append("\n\tNotification date = ").append(arrayOfObject[1]).append("\n\tNotification period = ").append(arrayOfObject[2]).append("\n\tNotification nb of occurrences = ").append(arrayOfObject[3]).append("\n\tNotification executes at fixed rate = ").append(arrayOfObject[5]);
/*      */ 
/*  698 */       JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeNotification", localStringBuilder.toString());
/*      */     }
/*      */ 
/*  702 */     this.timerTable.remove(paramInteger);
/*      */ 
/*  704 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeNotification", "timer notification removed");
/*      */   }
/*      */ 
/*      */   public synchronized void removeNotifications(String paramString)
/*      */     throws InstanceNotFoundException
/*      */   {
/*  718 */     Vector localVector = getNotificationIDs(paramString);
/*      */ 
/*  720 */     if (localVector.isEmpty()) {
/*  721 */       throw new InstanceNotFoundException("Timer notifications to remove not in the list of notifications");
/*      */     }
/*  723 */     for (Integer localInteger : localVector)
/*  724 */       removeNotification(localInteger);
/*      */   }
/*      */ 
/*      */   public synchronized void removeAllNotifications()
/*      */   {
/*  735 */     for (Object[] arrayOfObject : this.timerTable.values())
/*      */     {
/*  739 */       TimerAlarmClock localTimerAlarmClock = (TimerAlarmClock)arrayOfObject[4];
/*      */ 
/*  752 */       localTimerAlarmClock.cancel();
/*      */     }
/*      */ 
/*  756 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeAllNotifications", "removing all timer notifications");
/*      */ 
/*  759 */     this.timerTable.clear();
/*      */ 
/*  761 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeAllNotifications", "all timer notifications removed");
/*      */ 
/*  765 */     this.counterID = 0;
/*      */ 
/*  767 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "removeAllNotifications", "timer notification counter ID reset");
/*      */   }
/*      */ 
/*      */   public int getNbNotifications()
/*      */   {
/*  780 */     return this.timerTable.size();
/*      */   }
/*      */ 
/*      */   public synchronized Vector<Integer> getAllNotificationIDs()
/*      */   {
/*  790 */     return new Vector(this.timerTable.keySet());
/*      */   }
/*      */ 
/*      */   public synchronized Vector<Integer> getNotificationIDs(String paramString)
/*      */   {
/*  807 */     Vector localVector = new Vector();
/*      */ 
/*  809 */     for (Map.Entry localEntry : this.timerTable.entrySet()) {
/*  810 */       Object[] arrayOfObject = (Object[])localEntry.getValue();
/*  811 */       String str = ((TimerNotification)arrayOfObject[0]).getType();
/*  812 */       if (paramString == null ? str == null : paramString.equals(str))
/*  813 */         localVector.addElement(localEntry.getKey());
/*      */     }
/*  815 */     return localVector;
/*      */   }
/*      */ 
/*      */   public String getNotificationType(Integer paramInteger)
/*      */   {
/*  829 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/*  830 */     if (arrayOfObject != null) {
/*  831 */       return ((TimerNotification)arrayOfObject[0]).getType();
/*      */     }
/*  833 */     return null;
/*      */   }
/*      */ 
/*      */   public String getNotificationMessage(Integer paramInteger)
/*      */   {
/*  846 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/*  847 */     if (arrayOfObject != null) {
/*  848 */       return ((TimerNotification)arrayOfObject[0]).getMessage();
/*      */     }
/*  850 */     return null;
/*      */   }
/*      */ 
/*      */   public Object getNotificationUserData(Integer paramInteger)
/*      */   {
/*  866 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/*  867 */     if (arrayOfObject != null) {
/*  868 */       return ((TimerNotification)arrayOfObject[0]).getUserData();
/*      */     }
/*  870 */     return null;
/*      */   }
/*      */ 
/*      */   public Date getDate(Integer paramInteger)
/*      */   {
/*  883 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/*  884 */     if (arrayOfObject != null) {
/*  885 */       Date localDate = (Date)arrayOfObject[1];
/*  886 */       return new Date(localDate.getTime());
/*      */     }
/*  888 */     return null;
/*      */   }
/*      */ 
/*      */   public Long getPeriod(Integer paramInteger)
/*      */   {
/*  901 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/*  902 */     if (arrayOfObject != null) {
/*  903 */       return (Long)arrayOfObject[2];
/*      */     }
/*  905 */     return null;
/*      */   }
/*      */ 
/*      */   public Long getNbOccurences(Integer paramInteger)
/*      */   {
/*  918 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/*  919 */     if (arrayOfObject != null) {
/*  920 */       return (Long)arrayOfObject[3];
/*      */     }
/*  922 */     return null;
/*      */   }
/*      */ 
/*      */   public Boolean getFixedRate(Integer paramInteger)
/*      */   {
/*  936 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/*  937 */     if (arrayOfObject != null) {
/*  938 */       Boolean localBoolean = (Boolean)arrayOfObject[5];
/*  939 */       return Boolean.valueOf(localBoolean.booleanValue());
/*      */     }
/*  941 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean getSendPastNotifications()
/*      */   {
/*  953 */     return this.sendPastNotifications;
/*      */   }
/*      */ 
/*      */   public void setSendPastNotifications(boolean paramBoolean)
/*      */   {
/*  965 */     this.sendPastNotifications = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean isActive()
/*      */   {
/*  977 */     return this.isActive;
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  986 */     return this.timerTable.isEmpty();
/*      */   }
/*      */ 
/*      */   private synchronized void sendPastNotifications(Date paramDate, boolean paramBoolean)
/*      */   {
/* 1007 */     ArrayList localArrayList = new ArrayList(this.timerTable.values());
/*      */ 
/* 1010 */     for (Iterator localIterator = localArrayList.iterator(); localIterator.hasNext(); 
/* 1053 */       goto 70)
/*      */     {
/* 1010 */       Object[] arrayOfObject = (Object[])localIterator.next();
/*      */ 
/* 1014 */       TimerNotification localTimerNotification = (TimerNotification)arrayOfObject[0];
/* 1015 */       Integer localInteger = localTimerNotification.getNotificationID();
/* 1016 */       Date localDate = (Date)arrayOfObject[1];
/*      */ 
/* 1022 */       if ((paramDate.after(localDate)) && (this.timerTable.containsKey(localInteger)))
/*      */       {
/* 1024 */         if (paramBoolean == true) {
/* 1025 */           if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
/* 1026 */             StringBuilder localStringBuilder = new StringBuilder().append("sending past timer notification:").append("\n\tNotification source = ").append(localTimerNotification.getSource()).append("\n\tNotification type = ").append(localTimerNotification.getType()).append("\n\tNotification ID = ").append(localTimerNotification.getNotificationID()).append("\n\tNotification date = ").append(localDate).append("\n\tNotification period = ").append(arrayOfObject[2]).append("\n\tNotification nb of occurrences = ").append(arrayOfObject[3]).append("\n\tNotification executes at fixed rate = ").append(arrayOfObject[5]);
/*      */ 
/* 1042 */             JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "sendPastNotifications", localStringBuilder.toString());
/*      */           }
/*      */ 
/* 1045 */           sendNotification(localDate, localTimerNotification);
/*      */ 
/* 1047 */           JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "sendPastNotifications", "past timer notification sent");
/*      */         }
/*      */ 
/* 1053 */         updateTimerTable(localTimerNotification.getNotificationID());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void updateTimerTable(Integer paramInteger)
/*      */   {
/* 1075 */     Object[] arrayOfObject = (Object[])this.timerTable.get(paramInteger);
/* 1076 */     Date localDate = (Date)arrayOfObject[1];
/* 1077 */     Long localLong1 = (Long)arrayOfObject[2];
/* 1078 */     Long localLong2 = (Long)arrayOfObject[3];
/* 1079 */     Boolean localBoolean = (Boolean)arrayOfObject[5];
/* 1080 */     TimerAlarmClock localTimerAlarmClock = (TimerAlarmClock)arrayOfObject[4];
/*      */ 
/* 1082 */     if (localLong1.longValue() != 0L)
/*      */     {
/* 1091 */       if ((localLong2.longValue() == 0L) || (localLong2.longValue() > 1L))
/*      */       {
/* 1093 */         localDate.setTime(localDate.getTime() + localLong1.longValue());
/* 1094 */         arrayOfObject[3] = Long.valueOf(Math.max(0L, localLong2.longValue() - 1L));
/* 1095 */         localLong2 = (Long)arrayOfObject[3];
/*      */ 
/* 1097 */         if (this.isActive == true) {
/* 1098 */           if (localBoolean.booleanValue())
/*      */           {
/* 1100 */             localTimerAlarmClock = new TimerAlarmClock(this, localDate);
/* 1101 */             arrayOfObject[4] = localTimerAlarmClock;
/* 1102 */             this.timer.schedule(localTimerAlarmClock, localTimerAlarmClock.next);
/*      */           }
/*      */           else
/*      */           {
/* 1106 */             localTimerAlarmClock = new TimerAlarmClock(this, localLong1.longValue());
/* 1107 */             arrayOfObject[4] = localTimerAlarmClock;
/* 1108 */             this.timer.schedule(localTimerAlarmClock, localTimerAlarmClock.timeout);
/*      */           }
/*      */         }
/* 1111 */         if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
/* 1112 */           TimerNotification localTimerNotification = (TimerNotification)arrayOfObject[0];
/* 1113 */           StringBuilder localStringBuilder = new StringBuilder().append("update timer notification with:").append("\n\tNotification source = ").append(localTimerNotification.getSource()).append("\n\tNotification type = ").append(localTimerNotification.getType()).append("\n\tNotification ID = ").append(paramInteger).append("\n\tNotification date = ").append(localDate).append("\n\tNotification period = ").append(localLong1).append("\n\tNotification nb of occurrences = ").append(localLong2).append("\n\tNotification executes at fixed rate = ").append(localBoolean);
/*      */ 
/* 1129 */           JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "updateTimerTable", localStringBuilder.toString());
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1134 */         if (localTimerAlarmClock != null)
/*      */         {
/* 1143 */           localTimerAlarmClock.cancel();
/*      */         }
/* 1145 */         this.timerTable.remove(paramInteger);
/*      */       }
/*      */     }
/*      */     else {
/* 1149 */       if (localTimerAlarmClock != null)
/*      */       {
/* 1159 */         localTimerAlarmClock.cancel();
/*      */       }
/* 1161 */       this.timerTable.remove(paramInteger);
/*      */     }
/*      */   }
/*      */ 
/*      */   void notifyAlarmClock(TimerAlarmClockNotification paramTimerAlarmClockNotification)
/*      */   {
/* 1180 */     TimerNotification localTimerNotification = null;
/* 1181 */     Date localDate = null;
/*      */ 
/* 1185 */     TimerAlarmClock localTimerAlarmClock = (TimerAlarmClock)paramTimerAlarmClockNotification.getSource();
/*      */ 
/* 1187 */     for (Object[] arrayOfObject : this.timerTable.values()) {
/* 1188 */       if (arrayOfObject[4] == localTimerAlarmClock) {
/* 1189 */         localTimerNotification = (TimerNotification)arrayOfObject[0];
/* 1190 */         localDate = (Date)arrayOfObject[1];
/* 1191 */         break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1197 */     sendNotification(localDate, localTimerNotification);
/*      */ 
/* 1201 */     updateTimerTable(localTimerNotification.getNotificationID());
/*      */   }
/*      */ 
/*      */   void sendNotification(Date paramDate, TimerNotification paramTimerNotification)
/*      */   {
/* 1213 */     if (JmxProperties.TIMER_LOGGER.isLoggable(Level.FINER)) {
/* 1214 */       StringBuilder localStringBuilder = new StringBuilder().append("sending timer notification:").append("\n\tNotification source = ").append(paramTimerNotification.getSource()).append("\n\tNotification type = ").append(paramTimerNotification.getType()).append("\n\tNotification ID = ").append(paramTimerNotification.getNotificationID()).append("\n\tNotification date = ").append(paramDate);
/*      */ 
/* 1224 */       JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "sendNotification", localStringBuilder.toString());
/*      */     }
/*      */     long l;
/* 1228 */     synchronized (this) {
/* 1229 */       this.sequenceNumber += 1L;
/* 1230 */       l = this.sequenceNumber;
/*      */     }
/* 1232 */     synchronized (paramTimerNotification) {
/* 1233 */       paramTimerNotification.setTimeStamp(paramDate.getTime());
/* 1234 */       paramTimerNotification.setSequenceNumber(l);
/* 1235 */       sendNotification((TimerNotification)paramTimerNotification.cloneTimerNotification());
/*      */     }
/*      */ 
/* 1238 */     JmxProperties.TIMER_LOGGER.logp(Level.FINER, Timer.class.getName(), "sendNotification", "timer notification sent");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.timer.Timer
 * JD-Core Version:    0.6.2
 */