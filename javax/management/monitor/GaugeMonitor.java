/*     */ package javax.management.monitor;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class GaugeMonitor extends Monitor
/*     */   implements GaugeMonitorMBean
/*     */ {
/* 145 */   private Number highThreshold = INTEGER_ZERO;
/*     */ 
/* 152 */   private Number lowThreshold = INTEGER_ZERO;
/*     */ 
/* 160 */   private boolean notifyHigh = false;
/*     */ 
/* 168 */   private boolean notifyLow = false;
/*     */ 
/* 179 */   private boolean differenceMode = false;
/*     */ 
/* 181 */   private static final String[] types = { "jmx.monitor.error.runtime", "jmx.monitor.error.mbean", "jmx.monitor.error.attribute", "jmx.monitor.error.type", "jmx.monitor.error.threshold", "jmx.monitor.gauge.high", "jmx.monitor.gauge.low" };
/*     */ 
/* 191 */   private static final MBeanNotificationInfo[] notifsInfo = { new MBeanNotificationInfo(types, "javax.management.monitor.MonitorNotification", "Notifications sent by the GaugeMonitor MBean") };
/*     */   private static final int RISING = 0;
/*     */   private static final int FALLING = 1;
/*     */   private static final int RISING_OR_FALLING = 2;
/*     */ 
/*     */   public synchronized void start()
/*     */   {
/* 226 */     if (isActive()) {
/* 227 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINER, GaugeMonitor.class.getName(), "start", "the monitor is already active");
/*     */ 
/* 229 */       return;
/*     */     }
/*     */ 
/* 233 */     for (Monitor.ObservedObject localObservedObject : this.observedObjects) {
/* 234 */       GaugeMonitorObservedObject localGaugeMonitorObservedObject = (GaugeMonitorObservedObject)localObservedObject;
/*     */ 
/* 236 */       localGaugeMonitorObservedObject.setStatus(2);
/* 237 */       localGaugeMonitorObservedObject.setPreviousScanGauge(null);
/*     */     }
/* 239 */     doStart();
/*     */   }
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 246 */     doStop();
/*     */   }
/*     */ 
/*     */   public synchronized Number getDerivedGauge(ObjectName paramObjectName)
/*     */   {
/* 263 */     return (Number)super.getDerivedGauge(paramObjectName);
/*     */   }
/*     */ 
/*     */   public synchronized long getDerivedGaugeTimeStamp(ObjectName paramObjectName)
/*     */   {
/* 279 */     return super.getDerivedGaugeTimeStamp(paramObjectName);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized Number getDerivedGauge()
/*     */   {
/* 293 */     if (this.observedObjects.isEmpty()) {
/* 294 */       return null;
/*     */     }
/* 296 */     return (Number)((Monitor.ObservedObject)this.observedObjects.get(0)).getDerivedGauge();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized long getDerivedGaugeTimeStamp()
/*     */   {
/* 311 */     if (this.observedObjects.isEmpty()) {
/* 312 */       return 0L;
/*     */     }
/* 314 */     return ((Monitor.ObservedObject)this.observedObjects.get(0)).getDerivedGaugeTimeStamp();
/*     */   }
/*     */ 
/*     */   public synchronized Number getHighThreshold()
/*     */   {
/* 326 */     return this.highThreshold;
/*     */   }
/*     */ 
/*     */   public synchronized Number getLowThreshold()
/*     */   {
/* 337 */     return this.lowThreshold;
/*     */   }
/*     */ 
/*     */   public synchronized void setThresholds(Number paramNumber1, Number paramNumber2)
/*     */     throws IllegalArgumentException
/*     */   {
/* 358 */     if ((paramNumber1 == null) || (paramNumber2 == null)) {
/* 359 */       throw new IllegalArgumentException("Null threshold value");
/*     */     }
/*     */ 
/* 362 */     if (paramNumber1.getClass() != paramNumber2.getClass()) {
/* 363 */       throw new IllegalArgumentException("Different type threshold values");
/*     */     }
/*     */ 
/* 367 */     if (isFirstStrictlyGreaterThanLast(paramNumber2, paramNumber1, paramNumber1.getClass().getName()))
/*     */     {
/* 369 */       throw new IllegalArgumentException("High threshold less than low threshold");
/*     */     }
/*     */ 
/* 373 */     if ((this.highThreshold.equals(paramNumber1)) && (this.lowThreshold.equals(paramNumber2)))
/* 374 */       return;
/* 375 */     this.highThreshold = paramNumber1;
/* 376 */     this.lowThreshold = paramNumber2;
/*     */ 
/* 380 */     int i = 0;
/* 381 */     for (Monitor.ObservedObject localObservedObject : this.observedObjects) {
/* 382 */       resetAlreadyNotified(localObservedObject, i++, 16);
/* 383 */       GaugeMonitorObservedObject localGaugeMonitorObservedObject = (GaugeMonitorObservedObject)localObservedObject;
/*     */ 
/* 385 */       localGaugeMonitorObservedObject.setStatus(2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean getNotifyHigh()
/*     */   {
/* 399 */     return this.notifyHigh;
/*     */   }
/*     */ 
/*     */   public synchronized void setNotifyHigh(boolean paramBoolean)
/*     */   {
/* 411 */     if (this.notifyHigh == paramBoolean)
/* 412 */       return;
/* 413 */     this.notifyHigh = paramBoolean;
/*     */   }
/*     */ 
/*     */   public synchronized boolean getNotifyLow()
/*     */   {
/* 426 */     return this.notifyLow;
/*     */   }
/*     */ 
/*     */   public synchronized void setNotifyLow(boolean paramBoolean)
/*     */   {
/* 438 */     if (this.notifyLow == paramBoolean)
/* 439 */       return;
/* 440 */     this.notifyLow = paramBoolean;
/*     */   }
/*     */ 
/*     */   public synchronized boolean getDifferenceMode()
/*     */   {
/* 452 */     return this.differenceMode;
/*     */   }
/*     */ 
/*     */   public synchronized void setDifferenceMode(boolean paramBoolean)
/*     */   {
/* 463 */     if (this.differenceMode == paramBoolean)
/* 464 */       return;
/* 465 */     this.differenceMode = paramBoolean;
/*     */ 
/* 469 */     for (Monitor.ObservedObject localObservedObject : this.observedObjects) {
/* 470 */       GaugeMonitorObservedObject localGaugeMonitorObservedObject = (GaugeMonitorObservedObject)localObservedObject;
/*     */ 
/* 472 */       localGaugeMonitorObservedObject.setStatus(2);
/* 473 */       localGaugeMonitorObservedObject.setPreviousScanGauge(null);
/*     */     }
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 484 */     return (MBeanNotificationInfo[])notifsInfo.clone();
/*     */   }
/*     */ 
/*     */   private synchronized boolean updateDerivedGauge(Object paramObject, GaugeMonitorObservedObject paramGaugeMonitorObservedObject)
/*     */   {
/*     */     boolean bool;
/* 511 */     if (this.differenceMode)
/*     */     {
/* 515 */       if (paramGaugeMonitorObservedObject.getPreviousScanGauge() != null) {
/* 516 */         setDerivedGaugeWithDifference((Number)paramObject, paramGaugeMonitorObservedObject);
/* 517 */         bool = true;
/*     */       }
/*     */       else
/*     */       {
/* 523 */         bool = false;
/*     */       }
/* 525 */       paramGaugeMonitorObservedObject.setPreviousScanGauge((Number)paramObject);
/*     */     }
/*     */     else
/*     */     {
/* 530 */       paramGaugeMonitorObservedObject.setDerivedGauge((Number)paramObject);
/* 531 */       bool = true;
/*     */     }
/*     */ 
/* 534 */     return bool;
/*     */   }
/*     */ 
/*     */   private synchronized MonitorNotification updateNotifications(GaugeMonitorObservedObject paramGaugeMonitorObservedObject)
/*     */   {
/* 546 */     MonitorNotification localMonitorNotification = null;
/*     */ 
/* 551 */     if (paramGaugeMonitorObservedObject.getStatus() == 2) {
/* 552 */       if (isFirstGreaterThanLast((Number)paramGaugeMonitorObservedObject.getDerivedGauge(), this.highThreshold, paramGaugeMonitorObservedObject.getType()))
/*     */       {
/* 555 */         if (this.notifyHigh) {
/* 556 */           localMonitorNotification = new MonitorNotification("jmx.monitor.gauge.high", this, 0L, 0L, "", null, null, null, this.highThreshold);
/*     */         }
/*     */ 
/* 567 */         paramGaugeMonitorObservedObject.setStatus(1);
/* 568 */       } else if (isFirstGreaterThanLast(this.lowThreshold, (Number)paramGaugeMonitorObservedObject.getDerivedGauge(), paramGaugeMonitorObservedObject.getType()))
/*     */       {
/* 571 */         if (this.notifyLow) {
/* 572 */           localMonitorNotification = new MonitorNotification("jmx.monitor.gauge.low", this, 0L, 0L, "", null, null, null, this.lowThreshold);
/*     */         }
/*     */ 
/* 583 */         paramGaugeMonitorObservedObject.setStatus(0);
/*     */       }
/*     */     }
/* 586 */     else if (paramGaugeMonitorObservedObject.getStatus() == 0) {
/* 587 */       if (isFirstGreaterThanLast((Number)paramGaugeMonitorObservedObject.getDerivedGauge(), this.highThreshold, paramGaugeMonitorObservedObject.getType()))
/*     */       {
/* 590 */         if (this.notifyHigh) {
/* 591 */           localMonitorNotification = new MonitorNotification("jmx.monitor.gauge.high", this, 0L, 0L, "", null, null, null, this.highThreshold);
/*     */         }
/*     */ 
/* 602 */         paramGaugeMonitorObservedObject.setStatus(1);
/*     */       }
/* 604 */     } else if ((paramGaugeMonitorObservedObject.getStatus() == 1) && 
/* 605 */       (isFirstGreaterThanLast(this.lowThreshold, (Number)paramGaugeMonitorObservedObject.getDerivedGauge(), paramGaugeMonitorObservedObject.getType())))
/*     */     {
/* 608 */       if (this.notifyLow) {
/* 609 */         localMonitorNotification = new MonitorNotification("jmx.monitor.gauge.low", this, 0L, 0L, "", null, null, null, this.lowThreshold);
/*     */       }
/*     */ 
/* 620 */       paramGaugeMonitorObservedObject.setStatus(0);
/*     */     }
/*     */ 
/* 625 */     return localMonitorNotification;
/*     */   }
/*     */ 
/*     */   private synchronized void setDerivedGaugeWithDifference(Number paramNumber, GaugeMonitorObservedObject paramGaugeMonitorObservedObject)
/*     */   {
/* 638 */     Number localNumber = paramGaugeMonitorObservedObject.getPreviousScanGauge();
/*     */     Object localObject;
/* 640 */     switch (1.$SwitchMap$javax$management$monitor$Monitor$NumericalType[paramGaugeMonitorObservedObject.getType().ordinal()]) {
/*     */     case 1:
/* 642 */       localObject = Integer.valueOf(((Integer)paramNumber).intValue() - ((Integer)localNumber).intValue());
/*     */ 
/* 644 */       break;
/*     */     case 2:
/* 646 */       localObject = Byte.valueOf((byte)(((Byte)paramNumber).byteValue() - ((Byte)localNumber).byteValue()));
/*     */ 
/* 648 */       break;
/*     */     case 3:
/* 650 */       localObject = Short.valueOf((short)(((Short)paramNumber).shortValue() - ((Short)localNumber).shortValue()));
/*     */ 
/* 652 */       break;
/*     */     case 4:
/* 654 */       localObject = Long.valueOf(((Long)paramNumber).longValue() - ((Long)localNumber).longValue());
/*     */ 
/* 656 */       break;
/*     */     case 5:
/* 658 */       localObject = Float.valueOf(((Float)paramNumber).floatValue() - ((Float)localNumber).floatValue());
/*     */ 
/* 660 */       break;
/*     */     case 6:
/* 662 */       localObject = Double.valueOf(((Double)paramNumber).doubleValue() - ((Double)localNumber).doubleValue());
/*     */ 
/* 664 */       break;
/*     */     default:
/* 667 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, GaugeMonitor.class.getName(), "setDerivedGaugeWithDifference", "the threshold type is invalid");
/*     */ 
/* 670 */       return;
/*     */     }
/* 672 */     paramGaugeMonitorObservedObject.setDerivedGauge(localObject);
/*     */   }
/*     */ 
/*     */   private boolean isFirstGreaterThanLast(Number paramNumber1, Number paramNumber2, Monitor.NumericalType paramNumericalType)
/*     */   {
/* 690 */     switch (1.$SwitchMap$javax$management$monitor$Monitor$NumericalType[paramNumericalType.ordinal()]) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/* 695 */       return paramNumber1.longValue() >= paramNumber2.longValue();
/*     */     case 5:
/*     */     case 6:
/* 698 */       return paramNumber1.doubleValue() >= paramNumber2.doubleValue();
/*     */     }
/*     */ 
/* 701 */     JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, GaugeMonitor.class.getName(), "isFirstGreaterThanLast", "the threshold type is invalid");
/*     */ 
/* 704 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean isFirstStrictlyGreaterThanLast(Number paramNumber1, Number paramNumber2, String paramString)
/*     */   {
/* 722 */     if ((paramString.equals("java.lang.Integer")) || (paramString.equals("java.lang.Byte")) || (paramString.equals("java.lang.Short")) || (paramString.equals("java.lang.Long")))
/*     */     {
/* 727 */       return paramNumber1.longValue() > paramNumber2.longValue();
/*     */     }
/* 729 */     if ((paramString.equals("java.lang.Float")) || (paramString.equals("java.lang.Double")))
/*     */     {
/* 732 */       return paramNumber1.doubleValue() > paramNumber2.doubleValue();
/*     */     }
/*     */ 
/* 736 */     JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, GaugeMonitor.class.getName(), "isFirstStrictlyGreaterThanLast", "the threshold type is invalid");
/*     */ 
/* 739 */     return false;
/*     */   }
/*     */ 
/*     */   Monitor.ObservedObject createObservedObject(ObjectName paramObjectName)
/*     */   {
/* 756 */     GaugeMonitorObservedObject localGaugeMonitorObservedObject = new GaugeMonitorObservedObject(paramObjectName);
/*     */ 
/* 758 */     localGaugeMonitorObservedObject.setStatus(2);
/* 759 */     localGaugeMonitorObservedObject.setPreviousScanGauge(null);
/* 760 */     return localGaugeMonitorObservedObject;
/*     */   }
/*     */ 
/*     */   synchronized boolean isComparableTypeValid(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*     */   {
/* 773 */     GaugeMonitorObservedObject localGaugeMonitorObservedObject = (GaugeMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 775 */     if (localGaugeMonitorObservedObject == null) {
/* 776 */       return false;
/*     */     }
/*     */ 
/* 781 */     if ((paramComparable instanceof Integer))
/* 782 */       localGaugeMonitorObservedObject.setType(Monitor.NumericalType.INTEGER);
/* 783 */     else if ((paramComparable instanceof Byte))
/* 784 */       localGaugeMonitorObservedObject.setType(Monitor.NumericalType.BYTE);
/* 785 */     else if ((paramComparable instanceof Short))
/* 786 */       localGaugeMonitorObservedObject.setType(Monitor.NumericalType.SHORT);
/* 787 */     else if ((paramComparable instanceof Long))
/* 788 */       localGaugeMonitorObservedObject.setType(Monitor.NumericalType.LONG);
/* 789 */     else if ((paramComparable instanceof Float))
/* 790 */       localGaugeMonitorObservedObject.setType(Monitor.NumericalType.FLOAT);
/* 791 */     else if ((paramComparable instanceof Double))
/* 792 */       localGaugeMonitorObservedObject.setType(Monitor.NumericalType.DOUBLE);
/*     */     else {
/* 794 */       return false;
/*     */     }
/* 796 */     return true;
/*     */   }
/*     */ 
/*     */   synchronized Comparable<?> getDerivedGaugeFromComparable(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*     */   {
/* 804 */     GaugeMonitorObservedObject localGaugeMonitorObservedObject = (GaugeMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 806 */     if (localGaugeMonitorObservedObject == null) {
/* 807 */       return null;
/*     */     }
/*     */ 
/* 816 */     localGaugeMonitorObservedObject.setDerivedGaugeValid(updateDerivedGauge(paramComparable, localGaugeMonitorObservedObject));
/*     */ 
/* 818 */     return (Comparable)localGaugeMonitorObservedObject.getDerivedGauge();
/*     */   }
/*     */ 
/*     */   synchronized void onErrorNotification(MonitorNotification paramMonitorNotification)
/*     */   {
/* 823 */     GaugeMonitorObservedObject localGaugeMonitorObservedObject = (GaugeMonitorObservedObject)getObservedObject(paramMonitorNotification.getObservedObject());
/*     */ 
/* 825 */     if (localGaugeMonitorObservedObject == null) {
/* 826 */       return;
/*     */     }
/*     */ 
/* 830 */     localGaugeMonitorObservedObject.setStatus(2);
/* 831 */     localGaugeMonitorObservedObject.setPreviousScanGauge(null);
/*     */   }
/*     */ 
/*     */   synchronized MonitorNotification buildAlarmNotification(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*     */   {
/* 839 */     GaugeMonitorObservedObject localGaugeMonitorObservedObject = (GaugeMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 841 */     if (localGaugeMonitorObservedObject == null)
/* 842 */       return null;
/*     */     MonitorNotification localMonitorNotification;
/* 848 */     if (localGaugeMonitorObservedObject.getDerivedGaugeValid())
/* 849 */       localMonitorNotification = updateNotifications(localGaugeMonitorObservedObject);
/*     */     else
/* 851 */       localMonitorNotification = null;
/* 852 */     return localMonitorNotification;
/*     */   }
/*     */ 
/*     */   synchronized boolean isThresholdTypeValid(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*     */   {
/* 875 */     GaugeMonitorObservedObject localGaugeMonitorObservedObject = (GaugeMonitorObservedObject)getObservedObject(paramObjectName);
/*     */ 
/* 877 */     if (localGaugeMonitorObservedObject == null) {
/* 878 */       return false;
/*     */     }
/* 880 */     Class localClass = classForType(localGaugeMonitorObservedObject.getType());
/* 881 */     return (isValidForType(this.highThreshold, localClass)) && (isValidForType(this.lowThreshold, localClass));
/*     */   }
/*     */ 
/*     */   static class GaugeMonitorObservedObject extends Monitor.ObservedObject
/*     */   {
/*     */     private boolean derivedGaugeValid;
/*     */     private Monitor.NumericalType type;
/*     */     private Number previousScanGauge;
/*     */     private int status;
/*     */ 
/*     */     public GaugeMonitorObservedObject(ObjectName paramObjectName)
/*     */     {
/*  98 */       super();
/*     */     }
/*     */ 
/*     */     public final synchronized boolean getDerivedGaugeValid() {
/* 102 */       return this.derivedGaugeValid;
/*     */     }
/*     */ 
/*     */     public final synchronized void setDerivedGaugeValid(boolean paramBoolean) {
/* 106 */       this.derivedGaugeValid = paramBoolean;
/*     */     }
/*     */     public final synchronized Monitor.NumericalType getType() {
/* 109 */       return this.type;
/*     */     }
/*     */     public final synchronized void setType(Monitor.NumericalType paramNumericalType) {
/* 112 */       this.type = paramNumericalType;
/*     */     }
/*     */     public final synchronized Number getPreviousScanGauge() {
/* 115 */       return this.previousScanGauge;
/*     */     }
/*     */ 
/*     */     public final synchronized void setPreviousScanGauge(Number paramNumber) {
/* 119 */       this.previousScanGauge = paramNumber;
/*     */     }
/*     */     public final synchronized int getStatus() {
/* 122 */       return this.status;
/*     */     }
/*     */     public final synchronized void setStatus(int paramInt) {
/* 125 */       this.status = paramInt;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.monitor.GaugeMonitor
 * JD-Core Version:    0.6.2
 */