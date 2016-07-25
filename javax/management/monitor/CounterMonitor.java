/*     */ package javax.management.monitor;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class CounterMonitor extends Monitor
/*     */   implements CounterMonitorMBean
/*     */ {
/* 160 */   private Number modulus = INTEGER_ZERO;
/*     */ 
/* 166 */   private Number offset = INTEGER_ZERO;
/*     */ 
/* 173 */   private boolean notify = false;
/*     */ 
/* 182 */   private boolean differenceMode = false;
/*     */ 
/* 190 */   private Number initThreshold = INTEGER_ZERO;
/*     */ 
/* 192 */   private static final String[] types = { "jmx.monitor.error.runtime", "jmx.monitor.error.mbean", "jmx.monitor.error.attribute", "jmx.monitor.error.type", "jmx.monitor.error.threshold", "jmx.monitor.counter.threshold" };
/*     */ 
/* 201 */   private static final MBeanNotificationInfo[] notifsInfo = { new MBeanNotificationInfo(types, "javax.management.monitor.MonitorNotification", "Notifications sent by the CounterMonitor MBean") };
/*     */ 
/*     */   public synchronized void start()
/*     */   {
/* 230 */     if (isActive()) {
/* 231 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINER, CounterMonitor.class.getName(), "start", "the monitor is already active");
/*     */ 
/* 233 */       return;
/*     */     }
/*     */ 
/* 237 */     for (Monitor.ObservedObject localObservedObject : this.observedObjects) {
/* 238 */       CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)localObservedObject;
/*     */ 
/* 240 */       localCounterMonitorObservedObject.setThreshold(this.initThreshold);
/* 241 */       localCounterMonitorObservedObject.setModulusExceeded(false);
/* 242 */       localCounterMonitorObservedObject.setEventAlreadyNotified(false);
/* 243 */       localCounterMonitorObservedObject.setPreviousScanCounter(null);
/*     */     }
/* 245 */     doStart();
/*     */   }
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 252 */     doStop();
/*     */   }
/*     */ 
/*     */   public synchronized Number getDerivedGauge(ObjectName paramObjectName)
/*     */   {
/* 270 */     return (Number)super.getDerivedGauge(paramObjectName);
/*     */   }
/*     */ 
/*     */   public synchronized long getDerivedGaugeTimeStamp(ObjectName paramObjectName)
/*     */   {
/* 286 */     return super.getDerivedGaugeTimeStamp(paramObjectName);
/*     */   }
/*     */ 
/*     */   public synchronized Number getThreshold(ObjectName paramObjectName)
/*     */   {
/* 301 */     CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 303 */     if (localCounterMonitorObservedObject == null) {
/* 304 */       return null;
/*     */     }
/*     */ 
/* 314 */     if ((this.offset.longValue() > 0L) && (this.modulus.longValue() > 0L) && (localCounterMonitorObservedObject.getThreshold().longValue() > this.modulus.longValue()))
/*     */     {
/* 317 */       return this.initThreshold;
/*     */     }
/* 319 */     return localCounterMonitorObservedObject.getThreshold();
/*     */   }
/*     */ 
/*     */   public synchronized Number getInitThreshold()
/*     */   {
/* 332 */     return this.initThreshold;
/*     */   }
/*     */ 
/*     */   public synchronized void setInitThreshold(Number paramNumber)
/*     */     throws IllegalArgumentException
/*     */   {
/* 352 */     if (paramNumber == null) {
/* 353 */       throw new IllegalArgumentException("Null threshold");
/*     */     }
/* 355 */     if (paramNumber.longValue() < 0L) {
/* 356 */       throw new IllegalArgumentException("Negative threshold");
/*     */     }
/*     */ 
/* 359 */     if (this.initThreshold.equals(paramNumber))
/* 360 */       return;
/* 361 */     this.initThreshold = paramNumber;
/*     */ 
/* 365 */     int i = 0;
/* 366 */     for (Monitor.ObservedObject localObservedObject : this.observedObjects) {
/* 367 */       resetAlreadyNotified(localObservedObject, i++, 16);
/* 368 */       CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)localObservedObject;
/*     */ 
/* 370 */       localCounterMonitorObservedObject.setThreshold(paramNumber);
/* 371 */       localCounterMonitorObservedObject.setModulusExceeded(false);
/* 372 */       localCounterMonitorObservedObject.setEventAlreadyNotified(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized Number getDerivedGauge()
/*     */   {
/* 387 */     if (this.observedObjects.isEmpty()) {
/* 388 */       return null;
/*     */     }
/* 390 */     return (Number)((Monitor.ObservedObject)this.observedObjects.get(0)).getDerivedGauge();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized long getDerivedGaugeTimeStamp()
/*     */   {
/* 405 */     if (this.observedObjects.isEmpty()) {
/* 406 */       return 0L;
/*     */     }
/* 408 */     return ((Monitor.ObservedObject)this.observedObjects.get(0)).getDerivedGaugeTimeStamp();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized Number getThreshold()
/*     */   {
/* 424 */     return getThreshold(getObservedObject());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void setThreshold(Number paramNumber)
/*     */     throws IllegalArgumentException
/*     */   {
/* 442 */     setInitThreshold(paramNumber);
/*     */   }
/*     */ 
/*     */   public synchronized Number getOffset()
/*     */   {
/* 453 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public synchronized void setOffset(Number paramNumber)
/*     */     throws IllegalArgumentException
/*     */   {
/* 469 */     if (paramNumber == null) {
/* 470 */       throw new IllegalArgumentException("Null offset");
/*     */     }
/* 472 */     if (paramNumber.longValue() < 0L) {
/* 473 */       throw new IllegalArgumentException("Negative offset");
/*     */     }
/*     */ 
/* 476 */     if (this.offset.equals(paramNumber))
/* 477 */       return;
/* 478 */     this.offset = paramNumber;
/*     */ 
/* 480 */     int i = 0;
/* 481 */     for (Monitor.ObservedObject localObservedObject : this.observedObjects)
/* 482 */       resetAlreadyNotified(localObservedObject, i++, 16);
/*     */   }
/*     */ 
/*     */   public synchronized Number getModulus()
/*     */   {
/* 494 */     return this.modulus;
/*     */   }
/*     */ 
/*     */   public synchronized void setModulus(Number paramNumber)
/*     */     throws IllegalArgumentException
/*     */   {
/* 510 */     if (paramNumber == null) {
/* 511 */       throw new IllegalArgumentException("Null modulus");
/*     */     }
/* 513 */     if (paramNumber.longValue() < 0L) {
/* 514 */       throw new IllegalArgumentException("Negative modulus");
/*     */     }
/*     */ 
/* 517 */     if (this.modulus.equals(paramNumber))
/* 518 */       return;
/* 519 */     this.modulus = paramNumber;
/*     */ 
/* 523 */     int i = 0;
/* 524 */     for (Monitor.ObservedObject localObservedObject : this.observedObjects) {
/* 525 */       resetAlreadyNotified(localObservedObject, i++, 16);
/* 526 */       CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)localObservedObject;
/*     */ 
/* 528 */       localCounterMonitorObservedObject.setModulusExceeded(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean getNotify()
/*     */   {
/* 542 */     return this.notify;
/*     */   }
/*     */ 
/*     */   public synchronized void setNotify(boolean paramBoolean)
/*     */   {
/* 554 */     if (this.notify == paramBoolean)
/* 555 */       return;
/* 556 */     this.notify = paramBoolean;
/*     */   }
/*     */ 
/*     */   public synchronized boolean getDifferenceMode()
/*     */   {
/* 568 */     return this.differenceMode;
/*     */   }
/*     */ 
/*     */   public synchronized void setDifferenceMode(boolean paramBoolean)
/*     */   {
/* 579 */     if (this.differenceMode == paramBoolean)
/* 580 */       return;
/* 581 */     this.differenceMode = paramBoolean;
/*     */ 
/* 585 */     for (Monitor.ObservedObject localObservedObject : this.observedObjects) {
/* 586 */       CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)localObservedObject;
/*     */ 
/* 588 */       localCounterMonitorObservedObject.setThreshold(this.initThreshold);
/* 589 */       localCounterMonitorObservedObject.setModulusExceeded(false);
/* 590 */       localCounterMonitorObservedObject.setEventAlreadyNotified(false);
/* 591 */       localCounterMonitorObservedObject.setPreviousScanCounter(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 602 */     return (MBeanNotificationInfo[])notifsInfo.clone();
/*     */   }
/*     */ 
/*     */   private synchronized boolean updateDerivedGauge(Object paramObject, CounterMonitorObservedObject paramCounterMonitorObservedObject)
/*     */   {
/*     */     boolean bool;
/* 629 */     if (this.differenceMode)
/*     */     {
/* 633 */       if (paramCounterMonitorObservedObject.getPreviousScanCounter() != null) {
/* 634 */         setDerivedGaugeWithDifference((Number)paramObject, null, paramCounterMonitorObservedObject);
/*     */ 
/* 640 */         if (((Number)paramCounterMonitorObservedObject.getDerivedGauge()).longValue() < 0L) {
/* 641 */           if (this.modulus.longValue() > 0L) {
/* 642 */             setDerivedGaugeWithDifference((Number)paramObject, this.modulus, paramCounterMonitorObservedObject);
/*     */           }
/*     */ 
/* 645 */           paramCounterMonitorObservedObject.setThreshold(this.initThreshold);
/* 646 */           paramCounterMonitorObservedObject.setEventAlreadyNotified(false);
/*     */         }
/* 648 */         bool = true;
/*     */       }
/*     */       else
/*     */       {
/* 654 */         bool = false;
/*     */       }
/* 656 */       paramCounterMonitorObservedObject.setPreviousScanCounter((Number)paramObject);
/*     */     }
/*     */     else
/*     */     {
/* 661 */       paramCounterMonitorObservedObject.setDerivedGauge((Number)paramObject);
/* 662 */       bool = true;
/*     */     }
/* 664 */     return bool;
/*     */   }
/*     */ 
/*     */   private synchronized MonitorNotification updateNotifications(CounterMonitorObservedObject paramCounterMonitorObservedObject)
/*     */   {
/* 676 */     MonitorNotification localMonitorNotification = null;
/*     */ 
/* 680 */     if (!paramCounterMonitorObservedObject.getEventAlreadyNotified()) {
/* 681 */       if (((Number)paramCounterMonitorObservedObject.getDerivedGauge()).longValue() >= paramCounterMonitorObservedObject.getThreshold().longValue())
/*     */       {
/* 683 */         if (this.notify) {
/* 684 */           localMonitorNotification = new MonitorNotification("jmx.monitor.counter.threshold", this, 0L, 0L, "", null, null, null, paramCounterMonitorObservedObject.getThreshold());
/*     */         }
/*     */ 
/* 694 */         if (!this.differenceMode) {
/* 695 */           paramCounterMonitorObservedObject.setEventAlreadyNotified(true);
/*     */         }
/*     */       }
/*     */     }
/* 699 */     else if (JmxProperties.MONITOR_LOGGER.isLoggable(Level.FINER)) {
/* 700 */       StringBuilder localStringBuilder = new StringBuilder().append("The notification:").append("\n\tNotification observed object = ").append(paramCounterMonitorObservedObject.getObservedObject()).append("\n\tNotification observed attribute = ").append(getObservedAttribute()).append("\n\tNotification threshold level = ").append(paramCounterMonitorObservedObject.getThreshold()).append("\n\tNotification derived gauge = ").append(paramCounterMonitorObservedObject.getDerivedGauge()).append("\nhas already been sent");
/*     */ 
/* 711 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINER, CounterMonitor.class.getName(), "updateNotifications", localStringBuilder.toString());
/*     */     }
/*     */ 
/* 716 */     return localMonitorNotification;
/*     */   }
/*     */ 
/*     */   private synchronized void updateThreshold(CounterMonitorObservedObject paramCounterMonitorObservedObject)
/*     */   {
/* 728 */     if (((Number)paramCounterMonitorObservedObject.getDerivedGauge()).longValue() >= paramCounterMonitorObservedObject.getThreshold().longValue())
/*     */     {
/* 731 */       if (this.offset.longValue() > 0L)
/*     */       {
/* 736 */         long l = paramCounterMonitorObservedObject.getThreshold().longValue();
/* 737 */         while (((Number)paramCounterMonitorObservedObject.getDerivedGauge()).longValue() >= l)
/*     */         {
/* 739 */           l += this.offset.longValue();
/*     */         }
/*     */ 
/* 744 */         switch (1.$SwitchMap$javax$management$monitor$Monitor$NumericalType[paramCounterMonitorObservedObject.getType().ordinal()]) {
/*     */         case 1:
/* 746 */           paramCounterMonitorObservedObject.setThreshold(Integer.valueOf((int)l));
/* 747 */           break;
/*     */         case 2:
/* 749 */           paramCounterMonitorObservedObject.setThreshold(Byte.valueOf((byte)(int)l));
/* 750 */           break;
/*     */         case 3:
/* 752 */           paramCounterMonitorObservedObject.setThreshold(Short.valueOf((short)(int)l));
/* 753 */           break;
/*     */         case 4:
/* 755 */           paramCounterMonitorObservedObject.setThreshold(Long.valueOf(l));
/* 756 */           break;
/*     */         default:
/* 759 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, CounterMonitor.class.getName(), "updateThreshold", "the threshold type is invalid");
/*     */         }
/*     */ 
/* 771 */         if ((!this.differenceMode) && 
/* 772 */           (this.modulus.longValue() > 0L) && 
/* 773 */           (paramCounterMonitorObservedObject.getThreshold().longValue() > this.modulus.longValue()))
/*     */         {
/* 775 */           paramCounterMonitorObservedObject.setModulusExceeded(true);
/* 776 */           paramCounterMonitorObservedObject.setDerivedGaugeExceeded((Number)paramCounterMonitorObservedObject.getDerivedGauge());
/*     */         }
/*     */ 
/* 784 */         paramCounterMonitorObservedObject.setEventAlreadyNotified(false);
/*     */       } else {
/* 786 */         paramCounterMonitorObservedObject.setModulusExceeded(true);
/* 787 */         paramCounterMonitorObservedObject.setDerivedGaugeExceeded((Number)paramCounterMonitorObservedObject.getDerivedGauge());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void setDerivedGaugeWithDifference(Number paramNumber1, Number paramNumber2, CounterMonitorObservedObject paramCounterMonitorObservedObject)
/*     */   {
/* 810 */     long l = paramNumber1.longValue() - paramCounterMonitorObservedObject.getPreviousScanCounter().longValue();
/*     */ 
/* 812 */     if (paramNumber2 != null) {
/* 813 */       l += this.modulus.longValue();
/*     */     }
/* 815 */     switch (1.$SwitchMap$javax$management$monitor$Monitor$NumericalType[paramCounterMonitorObservedObject.getType().ordinal()]) { case 1:
/* 816 */       paramCounterMonitorObservedObject.setDerivedGauge(Integer.valueOf((int)l)); break;
/*     */     case 2:
/* 817 */       paramCounterMonitorObservedObject.setDerivedGauge(Byte.valueOf((byte)(int)l)); break;
/*     */     case 3:
/* 818 */       paramCounterMonitorObservedObject.setDerivedGauge(Short.valueOf((short)(int)l)); break;
/*     */     case 4:
/* 819 */       paramCounterMonitorObservedObject.setDerivedGauge(Long.valueOf(l)); break;
/*     */     default:
/* 822 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, CounterMonitor.class.getName(), "setDerivedGaugeWithDifference", "the threshold type is invalid");
/*     */     }
/*     */   }
/*     */ 
/*     */   Monitor.ObservedObject createObservedObject(ObjectName paramObjectName)
/*     */   {
/* 842 */     CounterMonitorObservedObject localCounterMonitorObservedObject = new CounterMonitorObservedObject(paramObjectName);
/*     */ 
/* 844 */     localCounterMonitorObservedObject.setThreshold(this.initThreshold);
/* 845 */     localCounterMonitorObservedObject.setModulusExceeded(false);
/* 846 */     localCounterMonitorObservedObject.setEventAlreadyNotified(false);
/* 847 */     localCounterMonitorObservedObject.setPreviousScanCounter(null);
/* 848 */     return localCounterMonitorObservedObject;
/*     */   }
/*     */ 
/*     */   synchronized boolean isComparableTypeValid(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*     */   {
/* 861 */     CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 863 */     if (localCounterMonitorObservedObject == null) {
/* 864 */       return false;
/*     */     }
/*     */ 
/* 868 */     if ((paramComparable instanceof Integer))
/* 869 */       localCounterMonitorObservedObject.setType(Monitor.NumericalType.INTEGER);
/* 870 */     else if ((paramComparable instanceof Byte))
/* 871 */       localCounterMonitorObservedObject.setType(Monitor.NumericalType.BYTE);
/* 872 */     else if ((paramComparable instanceof Short))
/* 873 */       localCounterMonitorObservedObject.setType(Monitor.NumericalType.SHORT);
/* 874 */     else if ((paramComparable instanceof Long))
/* 875 */       localCounterMonitorObservedObject.setType(Monitor.NumericalType.LONG);
/*     */     else {
/* 877 */       return false;
/*     */     }
/* 879 */     return true;
/*     */   }
/*     */ 
/*     */   synchronized Comparable<?> getDerivedGaugeFromComparable(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*     */   {
/* 887 */     CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 889 */     if (localCounterMonitorObservedObject == null) {
/* 890 */       return null;
/*     */     }
/*     */ 
/* 894 */     if ((localCounterMonitorObservedObject.getModulusExceeded()) && 
/* 895 */       (((Number)localCounterMonitorObservedObject.getDerivedGauge()).longValue() < localCounterMonitorObservedObject.getDerivedGaugeExceeded().longValue()))
/*     */     {
/* 897 */       localCounterMonitorObservedObject.setThreshold(this.initThreshold);
/* 898 */       localCounterMonitorObservedObject.setModulusExceeded(false);
/* 899 */       localCounterMonitorObservedObject.setEventAlreadyNotified(false);
/*     */     }
/*     */ 
/* 910 */     localCounterMonitorObservedObject.setDerivedGaugeValid(updateDerivedGauge(paramComparable, localCounterMonitorObservedObject));
/*     */ 
/* 912 */     return (Comparable)localCounterMonitorObservedObject.getDerivedGauge();
/*     */   }
/*     */ 
/*     */   synchronized void onErrorNotification(MonitorNotification paramMonitorNotification)
/*     */   {
/* 917 */     CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)getObservedObject(paramMonitorNotification.getObservedObject());
/*     */ 
/* 919 */     if (localCounterMonitorObservedObject == null) {
/* 920 */       return;
/*     */     }
/*     */ 
/* 924 */     localCounterMonitorObservedObject.setModulusExceeded(false);
/* 925 */     localCounterMonitorObservedObject.setEventAlreadyNotified(false);
/* 926 */     localCounterMonitorObservedObject.setPreviousScanCounter(null);
/*     */   }
/*     */ 
/*     */   synchronized MonitorNotification buildAlarmNotification(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*     */   {
/* 934 */     CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 936 */     if (localCounterMonitorObservedObject == null)
/* 937 */       return null;
/*     */     MonitorNotification localMonitorNotification;
/* 943 */     if (localCounterMonitorObservedObject.getDerivedGaugeValid()) {
/* 944 */       localMonitorNotification = updateNotifications(localCounterMonitorObservedObject);
/* 945 */       updateThreshold(localCounterMonitorObservedObject);
/*     */     } else {
/* 947 */       localMonitorNotification = null;
/*     */     }
/* 949 */     return localMonitorNotification;
/*     */   }
/*     */ 
/*     */   synchronized boolean isThresholdTypeValid(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*     */   {
/* 971 */     CounterMonitorObservedObject localCounterMonitorObservedObject = (CounterMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 973 */     if (localCounterMonitorObservedObject == null) {
/* 974 */       return false;
/*     */     }
/* 976 */     Class localClass = classForType(localCounterMonitorObservedObject.getType());
/* 977 */     return (localClass.isInstance(localCounterMonitorObservedObject.getThreshold())) && (isValidForType(this.offset, localClass)) && (isValidForType(this.modulus, localClass));
/*     */   }
/*     */ 
/*     */   static class CounterMonitorObservedObject extends Monitor.ObservedObject
/*     */   {
/*     */     private Number threshold;
/*     */     private Number previousScanCounter;
/*     */     private boolean modulusExceeded;
/*     */     private Number derivedGaugeExceeded;
/*     */     private boolean derivedGaugeValid;
/*     */     private boolean eventAlreadyNotified;
/*     */     private Monitor.NumericalType type;
/*     */ 
/*     */     public CounterMonitorObservedObject(ObjectName paramObjectName)
/*     */     {
/*  90 */       super();
/*     */     }
/*     */ 
/*     */     public final synchronized Number getThreshold() {
/*  94 */       return this.threshold;
/*     */     }
/*     */     public final synchronized void setThreshold(Number paramNumber) {
/*  97 */       this.threshold = paramNumber;
/*     */     }
/*     */     public final synchronized Number getPreviousScanCounter() {
/* 100 */       return this.previousScanCounter;
/*     */     }
/*     */ 
/*     */     public final synchronized void setPreviousScanCounter(Number paramNumber) {
/* 104 */       this.previousScanCounter = paramNumber;
/*     */     }
/*     */     public final synchronized boolean getModulusExceeded() {
/* 107 */       return this.modulusExceeded;
/*     */     }
/*     */ 
/*     */     public final synchronized void setModulusExceeded(boolean paramBoolean) {
/* 111 */       this.modulusExceeded = paramBoolean;
/*     */     }
/*     */     public final synchronized Number getDerivedGaugeExceeded() {
/* 114 */       return this.derivedGaugeExceeded;
/*     */     }
/*     */ 
/*     */     public final synchronized void setDerivedGaugeExceeded(Number paramNumber) {
/* 118 */       this.derivedGaugeExceeded = paramNumber;
/*     */     }
/*     */     public final synchronized boolean getDerivedGaugeValid() {
/* 121 */       return this.derivedGaugeValid;
/*     */     }
/*     */ 
/*     */     public final synchronized void setDerivedGaugeValid(boolean paramBoolean) {
/* 125 */       this.derivedGaugeValid = paramBoolean;
/*     */     }
/*     */     public final synchronized boolean getEventAlreadyNotified() {
/* 128 */       return this.eventAlreadyNotified;
/*     */     }
/*     */ 
/*     */     public final synchronized void setEventAlreadyNotified(boolean paramBoolean) {
/* 132 */       this.eventAlreadyNotified = paramBoolean;
/*     */     }
/*     */     public final synchronized Monitor.NumericalType getType() {
/* 135 */       return this.type;
/*     */     }
/*     */     public final synchronized void setType(Monitor.NumericalType paramNumericalType) {
/* 138 */       this.type = paramNumericalType;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.monitor.CounterMonitor
 * JD-Core Version:    0.6.2
 */