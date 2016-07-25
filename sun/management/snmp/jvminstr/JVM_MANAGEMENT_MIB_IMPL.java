/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpCounter64;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpOidRecord;
/*     */ import com.sun.jmx.snmp.SnmpOidTable;
/*     */ import com.sun.jmx.snmp.SnmpParameters;
/*     */ import com.sun.jmx.snmp.SnmpPeer;
/*     */ import com.sun.jmx.snmp.SnmpString;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import com.sun.jmx.snmp.SnmpVarBindList;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibTable;
/*     */ import com.sun.jmx.snmp.daemon.SnmpAdaptorServer;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.MemoryNotificationInfo;
/*     */ import java.lang.management.MemoryPoolMXBean;
/*     */ import java.lang.management.MemoryUsage;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationEmitter;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.openmbean.CompositeData;
/*     */ import sun.management.snmp.jvmmib.JVM_MANAGEMENT_MIB;
/*     */ import sun.management.snmp.jvmmib.JVM_MANAGEMENT_MIBOidTable;
/*     */ import sun.management.snmp.jvmmib.JvmCompilationMeta;
/*     */ import sun.management.snmp.jvmmib.JvmMemoryMeta;
/*     */ import sun.management.snmp.jvmmib.JvmRuntimeMeta;
/*     */ import sun.management.snmp.jvmmib.JvmThreadingMeta;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ import sun.management.snmp.util.SnmpCachedData;
/*     */ import sun.management.snmp.util.SnmpTableHandler;
/*     */ 
/*     */ public class JVM_MANAGEMENT_MIB_IMPL extends JVM_MANAGEMENT_MIB
/*     */ {
/*     */   private static final long serialVersionUID = -8104825586888859831L;
/*  84 */   private static final MibLogger log = new MibLogger(JVM_MANAGEMENT_MIB_IMPL.class);
/*     */   private static WeakReference<SnmpOidTable> tableRef;
/* 200 */   private ArrayList<NotificationTarget> notificationTargets = new ArrayList();
/*     */   private final NotificationEmitter emitter;
/*     */   private final NotificationHandler handler;
/*     */   private static final int DISPLAY_STRING_MAX_LENGTH = 255;
/*     */   private static final int JAVA_OBJECT_NAME_MAX_LENGTH = 1023;
/*     */   private static final int PATH_ELEMENT_MAX_LENGTH = 1023;
/*     */   private static final int ARG_VALUE_MAX_LENGTH = 1023;
/*     */   private static final int DEFAULT_CACHE_VALIDITY_PERIOD = 1000;
/*     */ 
/*     */   public static SnmpOidTable getOidTable()
/*     */   {
/*  90 */     Object localObject = null;
/*  91 */     if (tableRef == null) {
/*  92 */       localObject = new JVM_MANAGEMENT_MIBOidTable();
/*  93 */       tableRef = new WeakReference(localObject);
/*  94 */       return localObject;
/*     */     }
/*     */ 
/*  97 */     localObject = (SnmpOidTable)tableRef.get();
/*  98 */     if (localObject == null) {
/*  99 */       localObject = new JVM_MANAGEMENT_MIBOidTable();
/* 100 */       tableRef = new WeakReference(localObject);
/*     */     }
/*     */ 
/* 103 */     return localObject;
/*     */   }
/*     */ 
/*     */   public JVM_MANAGEMENT_MIB_IMPL()
/*     */   {
/* 212 */     this.handler = new NotificationHandler(null);
/* 213 */     this.emitter = ((NotificationEmitter)ManagementFactory.getMemoryMXBean());
/* 214 */     this.emitter.addNotificationListener(this.handler, null, null);
/*     */   }
/*     */ 
/*     */   private synchronized void sendTrap(SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList) {
/* 218 */     Iterator localIterator = this.notificationTargets.iterator();
/* 219 */     SnmpAdaptorServer localSnmpAdaptorServer = (SnmpAdaptorServer)getSnmpAdaptor();
/*     */ 
/* 222 */     if (localSnmpAdaptorServer == null) {
/* 223 */       log.error("sendTrap", "Cannot send trap: adaptor is null.");
/* 224 */       return;
/*     */     }
/*     */ 
/* 227 */     if (!localSnmpAdaptorServer.isActive()) {
/* 228 */       log.config("sendTrap", "Adaptor is not active: trap not sent.");
/* 229 */       return;
/*     */     }
/*     */ 
/* 232 */     while (localIterator.hasNext()) {
/* 233 */       NotificationTarget localNotificationTarget = null;
/*     */       try {
/* 235 */         localNotificationTarget = (NotificationTarget)localIterator.next();
/* 236 */         SnmpPeer localSnmpPeer = new SnmpPeer(localNotificationTarget.getAddress(), localNotificationTarget.getPort());
/*     */ 
/* 238 */         SnmpParameters localSnmpParameters = new SnmpParameters();
/* 239 */         localSnmpParameters.setRdCommunity(localNotificationTarget.getCommunity());
/* 240 */         localSnmpPeer.setParams(localSnmpParameters);
/* 241 */         log.debug("handleNotification", "Sending trap to " + localNotificationTarget.getAddress() + ":" + localNotificationTarget.getPort());
/*     */ 
/* 243 */         localSnmpAdaptorServer.snmpV2Trap(localSnmpPeer, paramSnmpOid, paramSnmpVarBindList, null);
/*     */       } catch (Exception localException) {
/* 245 */         log.error("sendTrap", "Exception occured while sending trap to [" + localNotificationTarget + "]. Exception : " + localException);
/*     */ 
/* 248 */         log.debug("sendTrap", localException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void addTarget(NotificationTarget paramNotificationTarget)
/*     */     throws IllegalArgumentException
/*     */   {
/* 260 */     if (paramNotificationTarget == null) {
/* 261 */       throw new IllegalArgumentException("Target is null");
/*     */     }
/* 263 */     this.notificationTargets.add(paramNotificationTarget);
/*     */   }
/*     */ 
/*     */   public void terminate()
/*     */   {
/*     */     try
/*     */     {
/* 271 */       this.emitter.removeNotificationListener(this.handler);
/*     */     } catch (ListenerNotFoundException localListenerNotFoundException) {
/* 273 */       log.error("terminate", "Listener Not found : " + localListenerNotFoundException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void addTargets(List<NotificationTarget> paramList)
/*     */     throws IllegalArgumentException
/*     */   {
/* 285 */     if (paramList == null) {
/* 286 */       throw new IllegalArgumentException("Target list is null");
/*     */     }
/* 288 */     this.notificationTargets.addAll(paramList);
/*     */   }
/*     */ 
/*     */   protected Object createJvmMemoryMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 317 */     if (paramMBeanServer != null) {
/* 318 */       return new JvmMemoryImpl(this, paramMBeanServer);
/*     */     }
/* 320 */     return new JvmMemoryImpl(this);
/*     */   }
/*     */ 
/*     */   protected JvmMemoryMeta createJvmMemoryMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 342 */     return new JvmMemoryMetaImpl(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected JvmThreadingMeta createJvmThreadingMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 364 */     return new JvmThreadingMetaImpl(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected Object createJvmThreadingMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 394 */     if (paramMBeanServer != null) {
/* 395 */       return new JvmThreadingImpl(this, paramMBeanServer);
/*     */     }
/* 397 */     return new JvmThreadingImpl(this);
/*     */   }
/*     */ 
/*     */   protected JvmRuntimeMeta createJvmRuntimeMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 419 */     return new JvmRuntimeMetaImpl(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected Object createJvmRuntimeMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 449 */     if (paramMBeanServer != null) {
/* 450 */       return new JvmRuntimeImpl(this, paramMBeanServer);
/*     */     }
/* 452 */     return new JvmRuntimeImpl(this);
/*     */   }
/*     */ 
/*     */   protected JvmCompilationMeta createJvmCompilationMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 478 */     if (ManagementFactory.getCompilationMXBean() == null) return null;
/* 479 */     return super.createJvmCompilationMetaNode(paramString1, paramString2, paramObjectName, paramMBeanServer);
/*     */   }
/*     */ 
/*     */   protected Object createJvmCompilationMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 508 */     if (paramMBeanServer != null) {
/* 509 */       return new JvmCompilationImpl(this, paramMBeanServer);
/*     */     }
/* 511 */     return new JvmCompilationImpl(this);
/*     */   }
/*     */ 
/*     */   protected Object createJvmOSMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 539 */     if (paramMBeanServer != null) {
/* 540 */       return new JvmOSImpl(this, paramMBeanServer);
/*     */     }
/* 542 */     return new JvmOSImpl(this);
/*     */   }
/*     */ 
/*     */   protected Object createJvmClassLoadingMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 573 */     if (paramMBeanServer != null) {
/* 574 */       return new JvmClassLoadingImpl(this, paramMBeanServer);
/*     */     }
/* 576 */     return new JvmClassLoadingImpl(this);
/*     */   }
/*     */ 
/*     */   static String validDisplayStringTC(String paramString)
/*     */   {
/* 581 */     if (paramString == null) return "";
/*     */ 
/* 583 */     if (paramString.length() > 255) {
/* 584 */       return paramString.substring(0, 255);
/*     */     }
/*     */ 
/* 587 */     return paramString;
/*     */   }
/*     */ 
/*     */   static String validJavaObjectNameTC(String paramString)
/*     */   {
/* 592 */     if (paramString == null) return "";
/*     */ 
/* 594 */     if (paramString.length() > 1023) {
/* 595 */       return paramString.substring(0, 1023);
/*     */     }
/*     */ 
/* 598 */     return paramString;
/*     */   }
/*     */ 
/*     */   static String validPathElementTC(String paramString)
/*     */   {
/* 603 */     if (paramString == null) return "";
/*     */ 
/* 605 */     if (paramString.length() > 1023) {
/* 606 */       return paramString.substring(0, 1023);
/*     */     }
/*     */ 
/* 609 */     return paramString;
/*     */   }
/*     */ 
/*     */   static String validArgValueTC(String paramString) {
/* 613 */     if (paramString == null) return "";
/*     */ 
/* 615 */     if (paramString.length() > 1023) {
/* 616 */       return paramString.substring(0, 1023);
/*     */     }
/*     */ 
/* 619 */     return paramString;
/*     */   }
/*     */ 
/*     */   private SnmpTableHandler getJvmMemPoolTableHandler(Object paramObject)
/*     */   {
/* 626 */     SnmpMibTable localSnmpMibTable = getRegisteredTableMeta("JvmMemPoolTable");
/*     */ 
/* 628 */     if (!(localSnmpMibTable instanceof JvmMemPoolTableMetaImpl)) {
/* 629 */       localObject = "Bad metadata class for JvmMemPoolTable: " + localSnmpMibTable.getClass().getName();
/*     */ 
/* 632 */       log.error("getJvmMemPoolTableHandler", (String)localObject);
/* 633 */       return null;
/*     */     }
/* 635 */     Object localObject = (JvmMemPoolTableMetaImpl)localSnmpMibTable;
/*     */ 
/* 637 */     return ((JvmMemPoolTableMetaImpl)localObject).getHandler(paramObject);
/*     */   }
/*     */ 
/*     */   private int findInCache(SnmpTableHandler paramSnmpTableHandler, String paramString)
/*     */   {
/* 646 */     if (!(paramSnmpTableHandler instanceof SnmpCachedData)) {
/* 647 */       if (paramSnmpTableHandler != null) {
/* 648 */         localObject = "Bad class for JvmMemPoolTable datas: " + paramSnmpTableHandler.getClass().getName();
/*     */ 
/* 650 */         log.error("getJvmMemPoolEntry", (String)localObject);
/*     */       }
/* 652 */       return -1;
/*     */     }
/*     */ 
/* 655 */     Object localObject = (SnmpCachedData)paramSnmpTableHandler;
/* 656 */     int i = ((SnmpCachedData)localObject).datas.length;
/* 657 */     for (int j = 0; j < ((SnmpCachedData)localObject).datas.length; j++) {
/* 658 */       MemoryPoolMXBean localMemoryPoolMXBean = (MemoryPoolMXBean)localObject.datas[j];
/* 659 */       if (paramString.equals(localMemoryPoolMXBean.getName())) return j;
/*     */     }
/* 661 */     return -1;
/*     */   }
/*     */ 
/*     */   private SnmpOid getJvmMemPoolEntryIndex(SnmpTableHandler paramSnmpTableHandler, String paramString)
/*     */   {
/* 669 */     int i = findInCache(paramSnmpTableHandler, paramString);
/* 670 */     if (i < 0) return null;
/* 671 */     return ((SnmpCachedData)paramSnmpTableHandler).indexes[i];
/*     */   }
/*     */ 
/*     */   private SnmpOid getJvmMemPoolEntryIndex(String paramString) {
/* 675 */     return getJvmMemPoolEntryIndex(getJvmMemPoolTableHandler(null), paramString);
/*     */   }
/*     */ 
/*     */   public long validity()
/*     */   {
/* 685 */     return 1000L;
/*     */   }
/*     */ 
/*     */   private class NotificationHandler
/*     */     implements NotificationListener
/*     */   {
/*     */     private NotificationHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void handleNotification(Notification paramNotification, Object paramObject)
/*     */     {
/* 113 */       JVM_MANAGEMENT_MIB_IMPL.log.debug("handleNotification", "Received notification [ " + paramNotification.getType() + "]");
/*     */ 
/* 116 */       String str = paramNotification.getType();
/* 117 */       if ((str.equals("java.management.memory.threshold.exceeded")) || (str.equals("java.management.memory.collection.threshold.exceeded")))
/*     */       {
/* 120 */         MemoryNotificationInfo localMemoryNotificationInfo = MemoryNotificationInfo.from((CompositeData)paramNotification.getUserData());
/*     */ 
/* 122 */         SnmpCounter64 localSnmpCounter641 = new SnmpCounter64(localMemoryNotificationInfo.getCount());
/* 123 */         SnmpCounter64 localSnmpCounter642 = new SnmpCounter64(localMemoryNotificationInfo.getUsage().getUsed());
/*     */ 
/* 125 */         SnmpString localSnmpString = new SnmpString(localMemoryNotificationInfo.getPoolName());
/* 126 */         SnmpOid localSnmpOid1 = JVM_MANAGEMENT_MIB_IMPL.this.getJvmMemPoolEntryIndex(localMemoryNotificationInfo.getPoolName());
/*     */ 
/* 129 */         if (localSnmpOid1 == null) {
/* 130 */           JVM_MANAGEMENT_MIB_IMPL.log.error("handleNotification", "Error: Can't find entry index for Memory Pool: " + localMemoryNotificationInfo.getPoolName() + ": " + "No trap emitted for " + str);
/*     */ 
/* 134 */           return;
/*     */         }
/*     */ 
/* 137 */         SnmpOid localSnmpOid2 = null;
/*     */ 
/* 139 */         SnmpOidTable localSnmpOidTable = JVM_MANAGEMENT_MIB_IMPL.getOidTable();
/*     */         try {
/* 141 */           SnmpOid localSnmpOid3 = null;
/* 142 */           SnmpOid localSnmpOid4 = null;
/*     */ 
/* 144 */           if (str.equals("java.management.memory.threshold.exceeded"))
/*     */           {
/* 146 */             localSnmpOid2 = new SnmpOid(localSnmpOidTable.resolveVarName("jvmLowMemoryPoolUsageNotif").getOid());
/*     */ 
/* 148 */             localSnmpOid3 = new SnmpOid(localSnmpOidTable.resolveVarName("jvmMemPoolUsed").getOid() + "." + localSnmpOid1);
/*     */ 
/* 152 */             localSnmpOid4 = new SnmpOid(localSnmpOidTable.resolveVarName("jvmMemPoolThreshdCount").getOid() + "." + localSnmpOid1);
/*     */           }
/* 156 */           else if (str.equals("java.management.memory.collection.threshold.exceeded"))
/*     */           {
/* 158 */             localSnmpOid2 = new SnmpOid(localSnmpOidTable.resolveVarName("jvmLowMemoryPoolCollectNotif").getOid());
/*     */ 
/* 161 */             localSnmpOid3 = new SnmpOid(localSnmpOidTable.resolveVarName("jvmMemPoolCollectUsed").getOid() + "." + localSnmpOid1);
/*     */ 
/* 165 */             localSnmpOid4 = new SnmpOid(localSnmpOidTable.resolveVarName("jvmMemPoolCollectThreshdCount").getOid() + "." + localSnmpOid1);
/*     */           }
/*     */ 
/* 173 */           SnmpVarBindList localSnmpVarBindList = new SnmpVarBindList();
/* 174 */           SnmpOid localSnmpOid5 = new SnmpOid(localSnmpOidTable.resolveVarName("jvmMemPoolName").getOid() + "." + localSnmpOid1);
/*     */ 
/* 179 */           SnmpVarBind localSnmpVarBind1 = new SnmpVarBind(localSnmpOid4, localSnmpCounter641);
/* 180 */           SnmpVarBind localSnmpVarBind2 = new SnmpVarBind(localSnmpOid3, localSnmpCounter642);
/* 181 */           SnmpVarBind localSnmpVarBind3 = new SnmpVarBind(localSnmpOid5, localSnmpString);
/*     */ 
/* 184 */           localSnmpVarBindList.add(localSnmpVarBind3);
/* 185 */           localSnmpVarBindList.add(localSnmpVarBind1);
/* 186 */           localSnmpVarBindList.add(localSnmpVarBind2);
/*     */ 
/* 188 */           JVM_MANAGEMENT_MIB_IMPL.this.sendTrap(localSnmpOid2, localSnmpVarBindList);
/*     */         } catch (Exception localException) {
/* 190 */           JVM_MANAGEMENT_MIB_IMPL.log.error("handleNotification", "Exception occured : " + localException);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JVM_MANAGEMENT_MIB_IMPL
 * JD-Core Version:    0.6.2
 */