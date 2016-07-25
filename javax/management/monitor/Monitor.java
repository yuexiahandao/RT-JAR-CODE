/*      */ package javax.management.monitor;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*      */ import com.sun.jmx.mbeanserver.Introspector;
/*      */ import java.io.IOException;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.ProtectionDomain;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.WeakHashMap;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.ThreadFactory;
/*      */ import java.util.concurrent.ThreadPoolExecutor;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.AttributeNotFoundException;
/*      */ import javax.management.InstanceNotFoundException;
/*      */ import javax.management.IntrospectionException;
/*      */ import javax.management.MBeanAttributeInfo;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanInfo;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MBeanServerConnection;
/*      */ import javax.management.NotificationBroadcasterSupport;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.ReflectionException;
/*      */ 
/*      */ public abstract class Monitor extends NotificationBroadcasterSupport
/*      */   implements MonitorMBean, MBeanRegistration
/*      */ {
/*      */   private String observedAttribute;
/*      */   private long granularityPeriod;
/*      */   private boolean isActive;
/*      */   private final AtomicLong sequenceNumber;
/*      */   private boolean isComplexTypeAttribute;
/*      */   private String firstAttribute;
/*      */   private final List<String> remainingAttributes;
/*  169 */   private static final AccessControlContext noPermissionsACC = new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, null) });
/*      */   private volatile AccessControlContext acc;
/*  177 */   private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("Scheduler"));
/*      */ 
/*  184 */   private static final Map<ThreadPoolExecutor, Void> executors = new WeakHashMap();
/*      */ 
/*  190 */   private static final Object executorsLock = new Object();
/*      */   private static final int maximumPoolSize;
/*      */   private Future<?> monitorFuture;
/*      */   private final SchedulerTask schedulerTask;
/*      */   private ScheduledFuture<?> schedulerFuture;
/*      */   protected static final int capacityIncrement = 16;
/*      */   protected int elementCount;
/*      */ 
/*      */   @Deprecated
/*      */   protected int alreadyNotified;
/*      */   protected int[] alreadyNotifieds;
/*      */   protected MBeanServer server;
/*      */   protected static final int RESET_FLAGS_ALREADY_NOTIFIED = 0;
/*      */   protected static final int OBSERVED_OBJECT_ERROR_NOTIFIED = 1;
/*      */   protected static final int OBSERVED_ATTRIBUTE_ERROR_NOTIFIED = 2;
/*      */   protected static final int OBSERVED_ATTRIBUTE_TYPE_ERROR_NOTIFIED = 4;
/*      */   protected static final int RUNTIME_ERROR_NOTIFIED = 8;
/*      */ 
/*      */   @Deprecated
/*      */   protected String dbgTag;
/*      */   final List<ObservedObject> observedObjects;
/*      */   static final int THRESHOLD_ERROR_NOTIFIED = 16;
/*  368 */   static final Integer INTEGER_ZERO = Integer.valueOf(0);
/*      */ 
/*      */   public Monitor()
/*      */   {
/*  135 */     this.granularityPeriod = 10000L;
/*      */ 
/*  141 */     this.isActive = false;
/*      */ 
/*  147 */     this.sequenceNumber = new AtomicLong();
/*      */ 
/*  153 */     this.isComplexTypeAttribute = false;
/*      */ 
/*  163 */     this.remainingAttributes = new CopyOnWriteArrayList();
/*      */ 
/*  172 */     this.acc = noPermissionsACC;
/*      */ 
/*  235 */     this.schedulerTask = new SchedulerTask();
/*      */ 
/*  259 */     this.elementCount = 0;
/*      */ 
/*  265 */     this.alreadyNotified = 0;
/*      */ 
/*  278 */     this.alreadyNotifieds = new int[16];
/*      */ 
/*  337 */     this.dbgTag = Monitor.class.getName();
/*      */ 
/*  349 */     this.observedObjects = new CopyOnWriteArrayList();
/*      */   }
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/*  394 */     JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "preRegister(MBeanServer, ObjectName)", "initialize the reference on the MBean server");
/*      */ 
/*  398 */     this.server = paramMBeanServer;
/*  399 */     return paramObjectName;
/*      */   }
/*      */ 
/*      */   public void postRegister(Boolean paramBoolean)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {
/*  422 */     JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "preDeregister()", "stop the monitor");
/*      */ 
/*  427 */     stop();
/*      */   }
/*      */ 
/*      */   public void postDeregister()
/*      */   {
/*      */   }
/*      */ 
/*      */   public abstract void start();
/*      */ 
/*      */   public abstract void stop();
/*      */ 
/*      */   @Deprecated
/*      */   public synchronized ObjectName getObservedObject()
/*      */   {
/*  464 */     if (this.observedObjects.isEmpty()) {
/*  465 */       return null;
/*      */     }
/*  467 */     return ((ObservedObject)this.observedObjects.get(0)).getObservedObject();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public synchronized void setObservedObject(ObjectName paramObjectName)
/*      */     throws IllegalArgumentException
/*      */   {
/*  486 */     if (paramObjectName == null)
/*  487 */       throw new IllegalArgumentException("Null observed object");
/*  488 */     if ((this.observedObjects.size() == 1) && (containsObservedObject(paramObjectName)))
/*  489 */       return;
/*  490 */     this.observedObjects.clear();
/*  491 */     addObservedObject(paramObjectName);
/*      */   }
/*      */ 
/*      */   public synchronized void addObservedObject(ObjectName paramObjectName)
/*      */     throws IllegalArgumentException
/*      */   {
/*  505 */     if (paramObjectName == null) {
/*  506 */       throw new IllegalArgumentException("Null observed object");
/*      */     }
/*      */ 
/*  511 */     if (containsObservedObject(paramObjectName)) {
/*  512 */       return;
/*      */     }
/*      */ 
/*  516 */     ObservedObject localObservedObject = createObservedObject(paramObjectName);
/*  517 */     localObservedObject.setAlreadyNotified(0);
/*  518 */     localObservedObject.setDerivedGauge(INTEGER_ZERO);
/*  519 */     localObservedObject.setDerivedGaugeTimeStamp(System.currentTimeMillis());
/*  520 */     this.observedObjects.add(localObservedObject);
/*      */ 
/*  524 */     createAlreadyNotified();
/*      */   }
/*      */ 
/*      */   public synchronized void removeObservedObject(ObjectName paramObjectName)
/*      */   {
/*  536 */     if (paramObjectName == null) {
/*  537 */       return;
/*      */     }
/*  539 */     ObservedObject localObservedObject = getObservedObject(paramObjectName);
/*  540 */     if (localObservedObject != null)
/*      */     {
/*  543 */       this.observedObjects.remove(localObservedObject);
/*      */ 
/*  546 */       createAlreadyNotified();
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized boolean containsObservedObject(ObjectName paramObjectName)
/*      */   {
/*  559 */     return getObservedObject(paramObjectName) != null;
/*      */   }
/*      */ 
/*      */   public synchronized ObjectName[] getObservedObjects()
/*      */   {
/*  569 */     ObjectName[] arrayOfObjectName = new ObjectName[this.observedObjects.size()];
/*  570 */     for (int i = 0; i < arrayOfObjectName.length; i++)
/*  571 */       arrayOfObjectName[i] = ((ObservedObject)this.observedObjects.get(i)).getObservedObject();
/*  572 */     return arrayOfObjectName;
/*      */   }
/*      */ 
/*      */   public synchronized String getObservedAttribute()
/*      */   {
/*  584 */     return this.observedAttribute;
/*      */   }
/*      */ 
/*      */   public void setObservedAttribute(String paramString)
/*      */     throws IllegalArgumentException
/*      */   {
/*  600 */     if (paramString == null)
/*  601 */       throw new IllegalArgumentException("Null observed attribute");
/*      */     int i;
/*  606 */     synchronized (this) {
/*  607 */       if ((this.observedAttribute != null) && (this.observedAttribute.equals(paramString)))
/*      */       {
/*  609 */         return;
/*  610 */       }this.observedAttribute = paramString;
/*      */ 
/*  615 */       cleanupIsComplexTypeAttribute();
/*      */ 
/*  617 */       i = 0;
/*  618 */       for (ObservedObject localObservedObject : this.observedObjects)
/*  619 */         resetAlreadyNotified(localObservedObject, i++, 6);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized long getGranularityPeriod()
/*      */   {
/*  635 */     return this.granularityPeriod;
/*      */   }
/*      */ 
/*      */   public synchronized void setGranularityPeriod(long paramLong)
/*      */     throws IllegalArgumentException
/*      */   {
/*  651 */     if (paramLong <= 0L) {
/*  652 */       throw new IllegalArgumentException("Nonpositive granularity period");
/*      */     }
/*      */ 
/*  656 */     if (this.granularityPeriod == paramLong)
/*  657 */       return;
/*  658 */     this.granularityPeriod = paramLong;
/*      */ 
/*  662 */     if (isActive()) {
/*  663 */       cleanupFutures();
/*  664 */       this.schedulerFuture = scheduler.schedule(this.schedulerTask, paramLong, TimeUnit.MILLISECONDS);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized boolean isActive()
/*      */   {
/*  683 */     return this.isActive;
/*      */   }
/*      */ 
/*      */   void doStart()
/*      */   {
/*  696 */     JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "doStart()", "start the monitor");
/*      */ 
/*  699 */     synchronized (this) {
/*  700 */       if (isActive()) {
/*  701 */         JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "doStart()", "the monitor is already active");
/*      */ 
/*  703 */         return;
/*      */       }
/*      */ 
/*  706 */       this.isActive = true;
/*      */ 
/*  711 */       cleanupIsComplexTypeAttribute();
/*      */ 
/*  716 */       this.acc = AccessController.getContext();
/*      */ 
/*  720 */       cleanupFutures();
/*  721 */       this.schedulerTask.setMonitorTask(new MonitorTask());
/*  722 */       this.schedulerFuture = scheduler.schedule(this.schedulerTask, getGranularityPeriod(), TimeUnit.MILLISECONDS);
/*      */     }
/*      */   }
/*      */ 
/*      */   void doStop()
/*      */   {
/*  732 */     JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "doStop()", "stop the monitor");
/*      */ 
/*  735 */     synchronized (this) {
/*  736 */       if (!isActive()) {
/*  737 */         JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "doStop()", "the monitor is not active");
/*      */ 
/*  739 */         return;
/*      */       }
/*      */ 
/*  742 */       this.isActive = false;
/*      */ 
/*  747 */       cleanupFutures();
/*      */ 
/*  751 */       this.acc = noPermissionsACC;
/*      */ 
/*  756 */       cleanupIsComplexTypeAttribute();
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized Object getDerivedGauge(ObjectName paramObjectName)
/*      */   {
/*  772 */     ObservedObject localObservedObject = getObservedObject(paramObjectName);
/*  773 */     return localObservedObject == null ? null : localObservedObject.getDerivedGauge();
/*      */   }
/*      */ 
/*      */   synchronized long getDerivedGaugeTimeStamp(ObjectName paramObjectName)
/*      */   {
/*  788 */     ObservedObject localObservedObject = getObservedObject(paramObjectName);
/*  789 */     return localObservedObject == null ? 0L : localObservedObject.getDerivedGaugeTimeStamp();
/*      */   }
/*      */ 
/*      */   Object getAttribute(MBeanServerConnection paramMBeanServerConnection, ObjectName paramObjectName, String paramString)
/*      */     throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException
/*      */   {
/*      */     int i;
/*  806 */     synchronized (this) {
/*  807 */       if (!isActive()) {
/*  808 */         throw new IllegalArgumentException("The monitor has been stopped");
/*      */       }
/*  810 */       if (!paramString.equals(getObservedAttribute())) {
/*  811 */         throw new IllegalArgumentException("The observed attribute has been changed");
/*      */       }
/*  813 */       i = (this.firstAttribute == null) && (paramString.indexOf('.') != -1) ? 1 : 0;
/*      */     }
/*      */ 
/*  820 */     if (i != 0)
/*      */       try {
/*  822 */         ??? = paramMBeanServerConnection.getMBeanInfo(paramObjectName);
/*      */       } catch (IntrospectionException localIntrospectionException) {
/*  824 */         throw new IllegalArgumentException(localIntrospectionException);
/*      */       }
/*      */     else
/*  827 */       ??? = null;
/*      */     String str;
/*  833 */     synchronized (this) {
/*  834 */       if (!isActive()) {
/*  835 */         throw new IllegalArgumentException("The monitor has been stopped");
/*      */       }
/*  837 */       if (!paramString.equals(getObservedAttribute())) {
/*  838 */         throw new IllegalArgumentException("The observed attribute has been changed");
/*      */       }
/*  840 */       if (this.firstAttribute == null) {
/*  841 */         if (paramString.indexOf('.') != -1) {
/*  842 */           MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = ((MBeanInfo)???).getAttributes();
/*  843 */           for (Object localObject3 : arrayOfMBeanAttributeInfo) {
/*  844 */             if (paramString.equals(localObject3.getName())) {
/*  845 */               this.firstAttribute = paramString;
/*  846 */               break;
/*      */             }
/*      */           }
/*  849 */           if (this.firstAttribute == null) {
/*  850 */             ??? = paramString.split("\\.", -1);
/*  851 */             this.firstAttribute = ???[0];
/*  852 */             for (??? = 1; ??? < ???.length; ???++)
/*  853 */               this.remainingAttributes.add(???[???]);
/*  854 */             this.isComplexTypeAttribute = true;
/*      */           }
/*      */         } else {
/*  857 */           this.firstAttribute = paramString;
/*      */         }
/*      */       }
/*  860 */       str = this.firstAttribute;
/*      */     }
/*  862 */     return paramMBeanServerConnection.getAttribute(paramObjectName, str);
/*      */   }
/*      */ 
/*      */   Comparable<?> getComparableFromAttribute(ObjectName paramObjectName, String paramString, Object paramObject)
/*      */     throws AttributeNotFoundException
/*      */   {
/*  869 */     if (this.isComplexTypeAttribute) {
/*  870 */       Object localObject = paramObject;
/*  871 */       for (String str : this.remainingAttributes)
/*  872 */         localObject = Introspector.elementFromComplex(localObject, str);
/*  873 */       return (Comparable)localObject;
/*      */     }
/*  875 */     return (Comparable)paramObject;
/*      */   }
/*      */ 
/*      */   boolean isComparableTypeValid(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*      */   {
/*  882 */     return true;
/*      */   }
/*      */ 
/*      */   String buildErrorNotification(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*      */   {
/*  888 */     return null;
/*      */   }
/*      */ 
/*      */   void onErrorNotification(MonitorNotification paramMonitorNotification)
/*      */   {
/*      */   }
/*      */ 
/*      */   Comparable<?> getDerivedGaugeFromComparable(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*      */   {
/*  897 */     return paramComparable;
/*      */   }
/*      */ 
/*      */   MonitorNotification buildAlarmNotification(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*      */   {
/*  903 */     return null;
/*      */   }
/*      */ 
/*      */   boolean isThresholdTypeValid(ObjectName paramObjectName, String paramString, Comparable<?> paramComparable)
/*      */   {
/*  909 */     return true;
/*      */   }
/*      */ 
/*      */   static Class<? extends Number> classForType(NumericalType paramNumericalType) {
/*  913 */     switch (1.$SwitchMap$javax$management$monitor$Monitor$NumericalType[paramNumericalType.ordinal()]) {
/*      */     case 1:
/*  915 */       return Byte.class;
/*      */     case 2:
/*  917 */       return Short.class;
/*      */     case 3:
/*  919 */       return Integer.class;
/*      */     case 4:
/*  921 */       return Long.class;
/*      */     case 5:
/*  923 */       return Float.class;
/*      */     case 6:
/*  925 */       return Double.class;
/*      */     }
/*  927 */     throw new IllegalArgumentException("Unsupported numerical type");
/*      */   }
/*      */ 
/*      */   static boolean isValidForType(Object paramObject, Class<? extends Number> paramClass)
/*      */   {
/*  933 */     return (paramObject == INTEGER_ZERO) || (paramClass.isInstance(paramObject));
/*      */   }
/*      */ 
/*      */   synchronized ObservedObject getObservedObject(ObjectName paramObjectName)
/*      */   {
/*  949 */     for (ObservedObject localObservedObject : this.observedObjects)
/*  950 */       if (localObservedObject.getObservedObject().equals(paramObjectName))
/*  951 */         return localObservedObject;
/*  952 */     return null;
/*      */   }
/*      */ 
/*      */   ObservedObject createObservedObject(ObjectName paramObjectName)
/*      */   {
/*  961 */     return new ObservedObject(paramObjectName);
/*      */   }
/*      */ 
/*      */   synchronized void createAlreadyNotified()
/*      */   {
/*  971 */     this.elementCount = this.observedObjects.size();
/*      */ 
/*  975 */     this.alreadyNotifieds = new int[this.elementCount];
/*  976 */     for (int i = 0; i < this.elementCount; i++) {
/*  977 */       this.alreadyNotifieds[i] = ((ObservedObject)this.observedObjects.get(i)).getAlreadyNotified();
/*      */     }
/*  979 */     updateDeprecatedAlreadyNotified();
/*      */   }
/*      */ 
/*      */   synchronized void updateDeprecatedAlreadyNotified()
/*      */   {
/*  986 */     if (this.elementCount > 0)
/*  987 */       this.alreadyNotified = this.alreadyNotifieds[0];
/*      */     else
/*  989 */       this.alreadyNotified = 0;
/*      */   }
/*      */ 
/*      */   synchronized void updateAlreadyNotified(ObservedObject paramObservedObject, int paramInt)
/*      */   {
/*  999 */     this.alreadyNotifieds[paramInt] = paramObservedObject.getAlreadyNotified();
/* 1000 */     if (paramInt == 0)
/* 1001 */       updateDeprecatedAlreadyNotified();
/*      */   }
/*      */ 
/*      */   synchronized boolean isAlreadyNotified(ObservedObject paramObservedObject, int paramInt)
/*      */   {
/* 1009 */     return (paramObservedObject.getAlreadyNotified() & paramInt) != 0;
/*      */   }
/*      */ 
/*      */   synchronized void setAlreadyNotified(ObservedObject paramObservedObject, int paramInt1, int paramInt2, int[] paramArrayOfInt)
/*      */   {
/* 1019 */     int i = computeAlreadyNotifiedIndex(paramObservedObject, paramInt1, paramArrayOfInt);
/* 1020 */     if (i == -1)
/* 1021 */       return;
/* 1022 */     paramObservedObject.setAlreadyNotified(paramObservedObject.getAlreadyNotified() | paramInt2);
/* 1023 */     updateAlreadyNotified(paramObservedObject, i);
/*      */   }
/*      */ 
/*      */   synchronized void resetAlreadyNotified(ObservedObject paramObservedObject, int paramInt1, int paramInt2)
/*      */   {
/* 1033 */     paramObservedObject.setAlreadyNotified(paramObservedObject.getAlreadyNotified() & (paramInt2 ^ 0xFFFFFFFF));
/* 1034 */     updateAlreadyNotified(paramObservedObject, paramInt1);
/*      */   }
/*      */ 
/*      */   synchronized void resetAllAlreadyNotified(ObservedObject paramObservedObject, int paramInt, int[] paramArrayOfInt)
/*      */   {
/* 1044 */     int i = computeAlreadyNotifiedIndex(paramObservedObject, paramInt, paramArrayOfInt);
/* 1045 */     if (i == -1)
/* 1046 */       return;
/* 1047 */     paramObservedObject.setAlreadyNotified(0);
/* 1048 */     updateAlreadyNotified(paramObservedObject, paramInt);
/*      */   }
/*      */ 
/*      */   synchronized int computeAlreadyNotifiedIndex(ObservedObject paramObservedObject, int paramInt, int[] paramArrayOfInt)
/*      */   {
/* 1057 */     if (paramArrayOfInt == this.alreadyNotifieds) {
/* 1058 */       return paramInt;
/*      */     }
/* 1060 */     return this.observedObjects.indexOf(paramObservedObject);
/*      */   }
/*      */ 
/*      */   private void sendNotification(String paramString1, long paramLong, String paramString2, Object paramObject1, Object paramObject2, ObjectName paramObjectName, boolean paramBoolean)
/*      */   {
/* 1089 */     if (!isActive()) {
/* 1090 */       return;
/*      */     }
/* 1092 */     if (JmxProperties.MONITOR_LOGGER.isLoggable(Level.FINER)) {
/* 1093 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "sendNotification", "send notification: \n\tNotification observed object = " + paramObjectName + "\n\tNotification observed attribute = " + this.observedAttribute + "\n\tNotification derived gauge = " + paramObject1);
/*      */     }
/*      */ 
/* 1100 */     long l = this.sequenceNumber.getAndIncrement();
/*      */ 
/* 1102 */     MonitorNotification localMonitorNotification = new MonitorNotification(paramString1, this, l, paramLong, paramString2, paramObjectName, this.observedAttribute, paramObject1, paramObject2);
/*      */ 
/* 1112 */     if (paramBoolean)
/* 1113 */       onErrorNotification(localMonitorNotification);
/* 1114 */     sendNotification(localMonitorNotification);
/*      */   }
/*      */ 
/*      */   private void monitor(ObservedObject paramObservedObject, int paramInt, int[] paramArrayOfInt)
/*      */   {
/* 1125 */     String str2 = null;
/* 1126 */     String str3 = null;
/* 1127 */     Comparable localComparable1 = null;
/* 1128 */     Object localObject1 = null;
/*      */ 
/* 1130 */     Comparable localComparable2 = null;
/* 1131 */     MonitorNotification localMonitorNotification = null;
/*      */ 
/* 1133 */     if (!isActive())
/*      */       return;
/*      */     ObjectName localObjectName;
/*      */     String str1;
/* 1142 */     synchronized (this) {
/* 1143 */       localObjectName = paramObservedObject.getObservedObject();
/* 1144 */       str1 = getObservedAttribute();
/* 1145 */       if ((localObjectName == null) || (str1 == null)) {
/* 1146 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1154 */     ??? = null;
/*      */     try {
/* 1156 */       ??? = getAttribute(this.server, localObjectName, str1);
/* 1157 */       if (??? == null) {
/* 1158 */         if (isAlreadyNotified(paramObservedObject, 4))
/*      */         {
/* 1160 */           return;
/*      */         }
/* 1162 */         str2 = "jmx.monitor.error.type";
/* 1163 */         setAlreadyNotified(paramObservedObject, paramInt, 4, paramArrayOfInt);
/*      */ 
/* 1165 */         str3 = "The observed attribute value is null.";
/* 1166 */         JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */       }
/*      */     }
/*      */     catch (NullPointerException localNullPointerException) {
/* 1170 */       if (isAlreadyNotified(paramObservedObject, 8)) {
/* 1171 */         return;
/*      */       }
/* 1173 */       str2 = "jmx.monitor.error.runtime";
/* 1174 */       setAlreadyNotified(paramObservedObject, paramInt, 8, paramArrayOfInt);
/* 1175 */       str3 = "The monitor must be registered in the MBean server or an MBeanServerConnection must be explicitly supplied.";
/*      */ 
/* 1179 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1181 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localNullPointerException.toString());
/*      */     }
/*      */     catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */     {
/* 1185 */       if (isAlreadyNotified(paramObservedObject, 1)) {
/* 1186 */         return;
/*      */       }
/* 1188 */       str2 = "jmx.monitor.error.mbean";
/* 1189 */       setAlreadyNotified(paramObservedObject, paramInt, 1, paramArrayOfInt);
/*      */ 
/* 1191 */       str3 = "The observed object must be accessible in the MBeanServerConnection.";
/*      */ 
/* 1194 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1196 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localInstanceNotFoundException.toString());
/*      */     }
/*      */     catch (AttributeNotFoundException localAttributeNotFoundException1)
/*      */     {
/* 1200 */       if (isAlreadyNotified(paramObservedObject, 2)) {
/* 1201 */         return;
/*      */       }
/* 1203 */       str2 = "jmx.monitor.error.attribute";
/* 1204 */       setAlreadyNotified(paramObservedObject, paramInt, 2, paramArrayOfInt);
/*      */ 
/* 1206 */       str3 = "The observed attribute must be accessible in the observed object.";
/*      */ 
/* 1209 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1211 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localAttributeNotFoundException1.toString());
/*      */     }
/*      */     catch (MBeanException localMBeanException)
/*      */     {
/* 1215 */       if (isAlreadyNotified(paramObservedObject, 8)) {
/* 1216 */         return;
/*      */       }
/* 1218 */       str2 = "jmx.monitor.error.runtime";
/* 1219 */       setAlreadyNotified(paramObservedObject, paramInt, 8, paramArrayOfInt);
/* 1220 */       str3 = localMBeanException.getMessage() == null ? "" : localMBeanException.getMessage();
/* 1221 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1223 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localMBeanException.toString());
/*      */     }
/*      */     catch (ReflectionException localReflectionException)
/*      */     {
/* 1227 */       if (isAlreadyNotified(paramObservedObject, 8)) {
/* 1228 */         return;
/*      */       }
/* 1230 */       str2 = "jmx.monitor.error.runtime";
/* 1231 */       setAlreadyNotified(paramObservedObject, paramInt, 8, paramArrayOfInt);
/* 1232 */       str3 = localReflectionException.getMessage() == null ? "" : localReflectionException.getMessage();
/* 1233 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1235 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localReflectionException.toString());
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1239 */       if (isAlreadyNotified(paramObservedObject, 8)) {
/* 1240 */         return;
/*      */       }
/* 1242 */       str2 = "jmx.monitor.error.runtime";
/* 1243 */       setAlreadyNotified(paramObservedObject, paramInt, 8, paramArrayOfInt);
/* 1244 */       str3 = localIOException.getMessage() == null ? "" : localIOException.getMessage();
/* 1245 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1247 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localIOException.toString());
/*      */     }
/*      */     catch (RuntimeException localRuntimeException1)
/*      */     {
/* 1251 */       if (isAlreadyNotified(paramObservedObject, 8)) {
/* 1252 */         return;
/*      */       }
/* 1254 */       str2 = "jmx.monitor.error.runtime";
/* 1255 */       setAlreadyNotified(paramObservedObject, paramInt, 8, paramArrayOfInt);
/* 1256 */       str3 = localRuntimeException1.getMessage() == null ? "" : localRuntimeException1.getMessage();
/* 1257 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1259 */       JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localRuntimeException1.toString());
/*      */     }
/*      */ 
/* 1264 */     synchronized (this)
/*      */     {
/* 1268 */       if (!isActive()) {
/* 1269 */         return;
/*      */       }
/*      */ 
/* 1278 */       if (!str1.equals(getObservedAttribute())) {
/* 1279 */         return;
/*      */       }
/*      */ 
/* 1284 */       if (str3 == null) {
/*      */         try {
/* 1286 */           localComparable2 = getComparableFromAttribute(localObjectName, str1, ???);
/*      */         }
/*      */         catch (ClassCastException localClassCastException)
/*      */         {
/* 1290 */           if (isAlreadyNotified(paramObservedObject, 4))
/*      */           {
/* 1292 */             return;
/*      */           }
/* 1294 */           str2 = "jmx.monitor.error.type";
/* 1295 */           setAlreadyNotified(paramObservedObject, paramInt, 4, paramArrayOfInt);
/*      */ 
/* 1297 */           str3 = "The observed attribute value does not implement the Comparable interface.";
/*      */ 
/* 1300 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1302 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localClassCastException.toString());
/*      */         }
/*      */         catch (AttributeNotFoundException localAttributeNotFoundException2)
/*      */         {
/* 1306 */           if (isAlreadyNotified(paramObservedObject, 2)) {
/* 1307 */             return;
/*      */           }
/* 1309 */           str2 = "jmx.monitor.error.attribute";
/* 1310 */           setAlreadyNotified(paramObservedObject, paramInt, 2, paramArrayOfInt);
/*      */ 
/* 1312 */           str3 = "The observed attribute must be accessible in the observed object.";
/*      */ 
/* 1315 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1317 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localAttributeNotFoundException2.toString());
/*      */         }
/*      */         catch (RuntimeException localRuntimeException2)
/*      */         {
/* 1321 */           if (isAlreadyNotified(paramObservedObject, 8)) {
/* 1322 */             return;
/*      */           }
/* 1324 */           str2 = "jmx.monitor.error.runtime";
/* 1325 */           setAlreadyNotified(paramObservedObject, paramInt, 8, paramArrayOfInt);
/*      */ 
/* 1327 */           str3 = localRuntimeException2.getMessage() == null ? "" : localRuntimeException2.getMessage();
/* 1328 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */ 
/* 1330 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", localRuntimeException2.toString());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1339 */       if ((str3 == null) && 
/* 1340 */         (!isComparableTypeValid(localObjectName, str1, localComparable2))) {
/* 1341 */         if (isAlreadyNotified(paramObservedObject, 4))
/*      */         {
/* 1343 */           return;
/*      */         }
/* 1345 */         str2 = "jmx.monitor.error.type";
/* 1346 */         setAlreadyNotified(paramObservedObject, paramInt, 4, paramArrayOfInt);
/*      */ 
/* 1348 */         str3 = "The observed attribute type is not valid.";
/* 1349 */         JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */       }
/*      */ 
/* 1357 */       if ((str3 == null) && 
/* 1358 */         (!isThresholdTypeValid(localObjectName, str1, localComparable2))) {
/* 1359 */         if (isAlreadyNotified(paramObservedObject, 16)) {
/* 1360 */           return;
/*      */         }
/* 1362 */         str2 = "jmx.monitor.error.threshold";
/* 1363 */         setAlreadyNotified(paramObservedObject, paramInt, 16, paramArrayOfInt);
/*      */ 
/* 1365 */         str3 = "The threshold type is not valid.";
/* 1366 */         JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */       }
/*      */ 
/* 1375 */       if (str3 == null) {
/* 1376 */         str3 = buildErrorNotification(localObjectName, str1, localComparable2);
/* 1377 */         if (str3 != null) {
/* 1378 */           if (isAlreadyNotified(paramObservedObject, 8)) {
/* 1379 */             return;
/*      */           }
/* 1381 */           str2 = "jmx.monitor.error.runtime";
/* 1382 */           setAlreadyNotified(paramObservedObject, paramInt, 8, paramArrayOfInt);
/*      */ 
/* 1384 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINEST, Monitor.class.getName(), "monitor", str3);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1393 */       if (str3 == null)
/*      */       {
/* 1396 */         resetAllAlreadyNotified(paramObservedObject, paramInt, paramArrayOfInt);
/*      */ 
/* 1400 */         localComparable1 = getDerivedGaugeFromComparable(localObjectName, str1, localComparable2);
/*      */ 
/* 1404 */         paramObservedObject.setDerivedGauge(localComparable1);
/* 1405 */         paramObservedObject.setDerivedGaugeTimeStamp(System.currentTimeMillis());
/*      */ 
/* 1409 */         localMonitorNotification = buildAlarmNotification(localObjectName, str1, (Comparable)localComparable1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1418 */     if (str3 != null) {
/* 1419 */       sendNotification(str2, System.currentTimeMillis(), str3, localComparable1, localObject1, localObjectName, true);
/*      */     }
/*      */ 
/* 1429 */     if ((localMonitorNotification != null) && (localMonitorNotification.getType() != null))
/* 1430 */       sendNotification(localMonitorNotification.getType(), System.currentTimeMillis(), localMonitorNotification.getMessage(), localComparable1, localMonitorNotification.getTrigger(), localObjectName, false);
/*      */   }
/*      */ 
/*      */   private synchronized void cleanupFutures()
/*      */   {
/* 1443 */     if (this.schedulerFuture != null) {
/* 1444 */       this.schedulerFuture.cancel(false);
/* 1445 */       this.schedulerFuture = null;
/*      */     }
/* 1447 */     if (this.monitorFuture != null) {
/* 1448 */       this.monitorFuture.cancel(false);
/* 1449 */       this.monitorFuture = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void cleanupIsComplexTypeAttribute()
/*      */   {
/* 1457 */     this.firstAttribute = null;
/* 1458 */     this.remainingAttributes.clear();
/* 1459 */     this.isComplexTypeAttribute = false;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  198 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("jmx.x.monitor.maximum.pool.size"));
/*      */ 
/*  200 */     if ((str == null) || (str.trim().length() == 0))
/*      */     {
/*  202 */       maximumPoolSize = 10;
/*      */     } else {
/*  204 */       int i = 10;
/*      */       try {
/*  206 */         i = Integer.parseInt(str);
/*      */       } catch (NumberFormatException localNumberFormatException) {
/*  208 */         if (JmxProperties.MONITOR_LOGGER.isLoggable(Level.FINER)) {
/*  209 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "<static initializer>", "Wrong value for jmx.x.monitor.maximum.pool.size system property", localNumberFormatException);
/*      */ 
/*  213 */           JmxProperties.MONITOR_LOGGER.logp(Level.FINER, Monitor.class.getName(), "<static initializer>", "jmx.x.monitor.maximum.pool.size defaults to 10");
/*      */         }
/*      */ 
/*  217 */         i = 10;
/*      */       }
/*  219 */       if (i < 1)
/*  220 */         maximumPoolSize = 1;
/*      */       else
/*  222 */         maximumPoolSize = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class DaemonThreadFactory
/*      */     implements ThreadFactory
/*      */   {
/*      */     final ThreadGroup group;
/* 1618 */     final AtomicInteger threadNumber = new AtomicInteger(1);
/*      */     final String namePrefix;
/*      */     static final String nameSuffix = "]";
/*      */ 
/*      */     public DaemonThreadFactory(String paramString)
/*      */     {
/* 1623 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 1624 */       this.group = (localSecurityManager != null ? localSecurityManager.getThreadGroup() : Thread.currentThread().getThreadGroup());
/*      */ 
/* 1626 */       this.namePrefix = ("JMX Monitor " + paramString + " Pool [Thread-");
/*      */     }
/*      */ 
/*      */     public DaemonThreadFactory(String paramString, ThreadGroup paramThreadGroup) {
/* 1630 */       this.group = paramThreadGroup;
/* 1631 */       this.namePrefix = ("JMX Monitor " + paramString + " Pool [Thread-");
/*      */     }
/*      */ 
/*      */     public ThreadGroup getThreadGroup() {
/* 1635 */       return this.group;
/*      */     }
/*      */ 
/*      */     public Thread newThread(Runnable paramRunnable) {
/* 1639 */       Thread localThread = new Thread(this.group, paramRunnable, this.namePrefix + this.threadNumber.getAndIncrement() + "]", 0L);
/*      */ 
/* 1645 */       localThread.setDaemon(true);
/* 1646 */       if (localThread.getPriority() != 5)
/* 1647 */         localThread.setPriority(5);
/* 1648 */       return localThread;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MonitorTask
/*      */     implements Runnable
/*      */   {
/*      */     private ThreadPoolExecutor executor;
/*      */ 
/*      */     public MonitorTask()
/*      */     {
/* 1527 */       SecurityManager localSecurityManager = System.getSecurityManager();
/* 1528 */       ThreadGroup localThreadGroup1 = localSecurityManager != null ? localSecurityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
/*      */ 
/* 1530 */       synchronized (Monitor.executorsLock) {
/* 1531 */         for (ThreadPoolExecutor localThreadPoolExecutor : Monitor.executors.keySet()) {
/* 1532 */           Monitor.DaemonThreadFactory localDaemonThreadFactory = (Monitor.DaemonThreadFactory)localThreadPoolExecutor.getThreadFactory();
/*      */ 
/* 1534 */           ThreadGroup localThreadGroup2 = localDaemonThreadFactory.getThreadGroup();
/* 1535 */           if (localThreadGroup2 == localThreadGroup1) {
/* 1536 */             this.executor = localThreadPoolExecutor;
/* 1537 */             break;
/*      */           }
/*      */         }
/* 1540 */         if (this.executor == null) {
/* 1541 */           this.executor = new ThreadPoolExecutor(Monitor.maximumPoolSize, Monitor.maximumPoolSize, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new Monitor.DaemonThreadFactory("ThreadGroup<" + localThreadGroup1.getName() + "> Executor", localThreadGroup1));
/*      */ 
/* 1549 */           this.executor.allowCoreThreadTimeOut(true);
/* 1550 */           Monitor.executors.put(this.executor, null);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public Future<?> submit()
/*      */     {
/* 1562 */       return this.executor.submit(this);
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/*      */       ScheduledFuture localScheduledFuture;
/*      */       AccessControlContext localAccessControlContext;
/* 1568 */       synchronized (Monitor.this) {
/* 1569 */         localScheduledFuture = Monitor.this.schedulerFuture;
/* 1570 */         localAccessControlContext = Monitor.this.acc;
/*      */       }
/* 1572 */       ??? = new PrivilegedAction()
/*      */       {
/*      */         public Void run()
/*      */         {
/*      */           int[] arrayOfInt;
/*      */           int i;
/* 1574 */           if (Monitor.this.isActive()) {
/* 1575 */             arrayOfInt = Monitor.this.alreadyNotifieds;
/* 1576 */             i = 0;
/* 1577 */             for (Monitor.ObservedObject localObservedObject : Monitor.this.observedObjects) {
/* 1578 */               if (Monitor.this.isActive()) {
/* 1579 */                 Monitor.this.monitor(localObservedObject, i++, arrayOfInt);
/*      */               }
/*      */             }
/*      */           }
/* 1583 */           return null;
/*      */         }
/*      */       };
/* 1586 */       if (localAccessControlContext == null) {
/* 1587 */         throw new SecurityException("AccessControlContext cannot be null");
/*      */       }
/* 1589 */       AccessController.doPrivileged((PrivilegedAction)???, localAccessControlContext);
/* 1590 */       synchronized (Monitor.this) {
/* 1591 */         if ((Monitor.this.isActive()) && (Monitor.this.schedulerFuture == localScheduledFuture))
/*      */         {
/* 1593 */           Monitor.this.monitorFuture = null;
/* 1594 */           Monitor.this.schedulerFuture = Monitor.scheduler.schedule(Monitor.this.schedulerTask, Monitor.this.getGranularityPeriod(), TimeUnit.MILLISECONDS);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static enum NumericalType
/*      */   {
/*  363 */     BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE;
/*      */   }
/*      */ 
/*      */   static class ObservedObject
/*      */   {
/*      */     private final ObjectName observedObject;
/*      */     private int alreadyNotified;
/*      */     private Object derivedGauge;
/*      */     private long derivedGaugeTimeStamp;
/*      */ 
/*      */     public ObservedObject(ObjectName paramObjectName)
/*      */     {
/*   88 */       this.observedObject = paramObjectName;
/*      */     }
/*      */ 
/*      */     public final ObjectName getObservedObject() {
/*   92 */       return this.observedObject;
/*      */     }
/*      */     public final synchronized int getAlreadyNotified() {
/*   95 */       return this.alreadyNotified;
/*      */     }
/*      */     public final synchronized void setAlreadyNotified(int paramInt) {
/*   98 */       this.alreadyNotified = paramInt;
/*      */     }
/*      */     public final synchronized Object getDerivedGauge() {
/*  101 */       return this.derivedGauge;
/*      */     }
/*      */     public final synchronized void setDerivedGauge(Object paramObject) {
/*  104 */       this.derivedGauge = paramObject;
/*      */     }
/*      */     public final synchronized long getDerivedGaugeTimeStamp() {
/*  107 */       return this.derivedGaugeTimeStamp;
/*      */     }
/*      */ 
/*      */     public final synchronized void setDerivedGaugeTimeStamp(long paramLong) {
/*  111 */       this.derivedGaugeTimeStamp = paramLong;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class SchedulerTask
/*      */     implements Runnable
/*      */   {
/*      */     private Monitor.MonitorTask task;
/*      */ 
/*      */     public SchedulerTask()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setMonitorTask(Monitor.MonitorTask paramMonitorTask)
/*      */     {
/* 1488 */       this.task = paramMonitorTask;
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/* 1498 */       synchronized (Monitor.this) {
/* 1499 */         Monitor.this.monitorFuture = this.task.submit();
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.monitor.Monitor
 * JD-Core Version:    0.6.2
 */