/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import javax.management.MBeanServer;
/*     */ import sun.management.snmp.jvmmib.EnumJvmThreadContentionMonitoring;
/*     */ import sun.management.snmp.jvmmib.EnumJvmThreadCpuTimeMonitoring;
/*     */ import sun.management.snmp.jvmmib.JvmThreadingMBean;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ 
/*     */ public class JvmThreadingImpl
/*     */   implements JvmThreadingMBean
/*     */ {
/*  78 */   static final EnumJvmThreadCpuTimeMonitoring JvmThreadCpuTimeMonitoringUnsupported = new EnumJvmThreadCpuTimeMonitoring("unsupported");
/*     */ 
/*  81 */   static final EnumJvmThreadCpuTimeMonitoring JvmThreadCpuTimeMonitoringEnabled = new EnumJvmThreadCpuTimeMonitoring("enabled");
/*     */ 
/*  84 */   static final EnumJvmThreadCpuTimeMonitoring JvmThreadCpuTimeMonitoringDisabled = new EnumJvmThreadCpuTimeMonitoring("disabled");
/*     */ 
/* 111 */   static final EnumJvmThreadContentionMonitoring JvmThreadContentionMonitoringUnsupported = new EnumJvmThreadContentionMonitoring("unsupported");
/*     */ 
/* 114 */   static final EnumJvmThreadContentionMonitoring JvmThreadContentionMonitoringEnabled = new EnumJvmThreadContentionMonitoring("enabled");
/*     */ 
/* 117 */   static final EnumJvmThreadContentionMonitoring JvmThreadContentionMonitoringDisabled = new EnumJvmThreadContentionMonitoring("disabled");
/*     */ 
/* 361 */   private long jvmThreadPeakCountReset = 0L;
/*     */ 
/* 363 */   static final MibLogger log = new MibLogger(JvmThreadingImpl.class);
/*     */ 
/*     */   public JvmThreadingImpl(SnmpMib paramSnmpMib)
/*     */   {
/* 126 */     log.debug("JvmThreadingImpl", "Constructor");
/*     */   }
/*     */ 
/*     */   public JvmThreadingImpl(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 136 */     log.debug("JvmThreadingImpl", "Constructor with server");
/*     */   }
/*     */ 
/*     */   static ThreadMXBean getThreadMXBean()
/*     */   {
/* 145 */     return ManagementFactory.getThreadMXBean();
/*     */   }
/*     */ 
/*     */   public EnumJvmThreadCpuTimeMonitoring getJvmThreadCpuTimeMonitoring()
/*     */     throws SnmpStatusException
/*     */   {
/* 154 */     ThreadMXBean localThreadMXBean = getThreadMXBean();
/*     */ 
/* 156 */     if (!localThreadMXBean.isThreadCpuTimeSupported()) {
/* 157 */       log.debug("getJvmThreadCpuTimeMonitoring", "Unsupported ThreadCpuTimeMonitoring");
/*     */ 
/* 159 */       return JvmThreadCpuTimeMonitoringUnsupported;
/*     */     }
/*     */     try
/*     */     {
/* 163 */       if (localThreadMXBean.isThreadCpuTimeEnabled()) {
/* 164 */         log.debug("getJvmThreadCpuTimeMonitoring", "Enabled ThreadCpuTimeMonitoring");
/*     */ 
/* 166 */         return JvmThreadCpuTimeMonitoringEnabled;
/*     */       }
/* 168 */       log.debug("getJvmThreadCpuTimeMonitoring", "Disabled ThreadCpuTimeMonitoring");
/*     */ 
/* 170 */       return JvmThreadCpuTimeMonitoringDisabled;
/*     */     }
/*     */     catch (UnsupportedOperationException localUnsupportedOperationException) {
/* 173 */       log.debug("getJvmThreadCpuTimeMonitoring", "Newly unsupported ThreadCpuTimeMonitoring");
/*     */     }
/*     */ 
/* 176 */     return JvmThreadCpuTimeMonitoringUnsupported;
/*     */   }
/*     */ 
/*     */   public void setJvmThreadCpuTimeMonitoring(EnumJvmThreadCpuTimeMonitoring paramEnumJvmThreadCpuTimeMonitoring)
/*     */     throws SnmpStatusException
/*     */   {
/* 186 */     ThreadMXBean localThreadMXBean = getThreadMXBean();
/*     */ 
/* 190 */     if (JvmThreadCpuTimeMonitoringEnabled.intValue() == paramEnumJvmThreadCpuTimeMonitoring.intValue())
/* 191 */       localThreadMXBean.setThreadCpuTimeEnabled(true);
/*     */     else
/* 193 */       localThreadMXBean.setThreadCpuTimeEnabled(false);
/*     */   }
/*     */ 
/*     */   public void checkJvmThreadCpuTimeMonitoring(EnumJvmThreadCpuTimeMonitoring paramEnumJvmThreadCpuTimeMonitoring)
/*     */     throws SnmpStatusException
/*     */   {
/* 204 */     if (JvmThreadCpuTimeMonitoringUnsupported.intValue() == paramEnumJvmThreadCpuTimeMonitoring.intValue()) {
/* 205 */       log.debug("checkJvmThreadCpuTimeMonitoring", "Try to set to illegal unsupported value");
/*     */ 
/* 207 */       throw new SnmpStatusException(10);
/*     */     }
/*     */ 
/* 210 */     if ((JvmThreadCpuTimeMonitoringEnabled.intValue() == paramEnumJvmThreadCpuTimeMonitoring.intValue()) || (JvmThreadCpuTimeMonitoringDisabled.intValue() == paramEnumJvmThreadCpuTimeMonitoring.intValue()))
/*     */     {
/* 214 */       ThreadMXBean localThreadMXBean = getThreadMXBean();
/* 215 */       if (localThreadMXBean.isThreadCpuTimeSupported()) return;
/*     */ 
/* 218 */       log.debug("checkJvmThreadCpuTimeMonitoring", "Unsupported operation, can't set state");
/*     */ 
/* 220 */       throw new SnmpStatusException(12);
/*     */     }
/*     */ 
/* 225 */     log.debug("checkJvmThreadCpuTimeMonitoring", "unknown enum value ");
/*     */ 
/* 227 */     throw new SnmpStatusException(10);
/*     */   }
/*     */ 
/*     */   public EnumJvmThreadContentionMonitoring getJvmThreadContentionMonitoring()
/*     */     throws SnmpStatusException
/*     */   {
/* 236 */     ThreadMXBean localThreadMXBean = getThreadMXBean();
/*     */ 
/* 238 */     if (!localThreadMXBean.isThreadContentionMonitoringSupported()) {
/* 239 */       log.debug("getJvmThreadContentionMonitoring", "Unsupported ThreadContentionMonitoring");
/*     */ 
/* 241 */       return JvmThreadContentionMonitoringUnsupported;
/*     */     }
/*     */ 
/* 244 */     if (localThreadMXBean.isThreadContentionMonitoringEnabled()) {
/* 245 */       log.debug("getJvmThreadContentionMonitoring", "Enabled ThreadContentionMonitoring");
/*     */ 
/* 247 */       return JvmThreadContentionMonitoringEnabled;
/*     */     }
/* 249 */     log.debug("getJvmThreadContentionMonitoring", "Disabled ThreadContentionMonitoring");
/*     */ 
/* 251 */     return JvmThreadContentionMonitoringDisabled;
/*     */   }
/*     */ 
/*     */   public void setJvmThreadContentionMonitoring(EnumJvmThreadContentionMonitoring paramEnumJvmThreadContentionMonitoring)
/*     */     throws SnmpStatusException
/*     */   {
/* 261 */     ThreadMXBean localThreadMXBean = getThreadMXBean();
/*     */ 
/* 265 */     if (JvmThreadContentionMonitoringEnabled.intValue() == paramEnumJvmThreadContentionMonitoring.intValue())
/* 266 */       localThreadMXBean.setThreadContentionMonitoringEnabled(true);
/*     */     else
/* 268 */       localThreadMXBean.setThreadContentionMonitoringEnabled(false);
/*     */   }
/*     */ 
/*     */   public void checkJvmThreadContentionMonitoring(EnumJvmThreadContentionMonitoring paramEnumJvmThreadContentionMonitoring)
/*     */     throws SnmpStatusException
/*     */   {
/* 278 */     if (JvmThreadContentionMonitoringUnsupported.intValue() == paramEnumJvmThreadContentionMonitoring.intValue()) {
/* 279 */       log.debug("checkJvmThreadContentionMonitoring", "Try to set to illegal unsupported value");
/*     */ 
/* 281 */       throw new SnmpStatusException(10);
/*     */     }
/*     */ 
/* 284 */     if ((JvmThreadContentionMonitoringEnabled.intValue() == paramEnumJvmThreadContentionMonitoring.intValue()) || (JvmThreadContentionMonitoringDisabled.intValue() == paramEnumJvmThreadContentionMonitoring.intValue()))
/*     */     {
/* 288 */       ThreadMXBean localThreadMXBean = getThreadMXBean();
/* 289 */       if (localThreadMXBean.isThreadContentionMonitoringSupported()) return;
/*     */ 
/* 291 */       log.debug("checkJvmThreadContentionMonitoring", "Unsupported operation, can't set state");
/*     */ 
/* 293 */       throw new SnmpStatusException(12);
/*     */     }
/*     */ 
/* 297 */     log.debug("checkJvmThreadContentionMonitoring", "Try to set to unknown value");
/*     */ 
/* 299 */     throw new SnmpStatusException(10);
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadTotalStartedCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 306 */     return new Long(getThreadMXBean().getTotalStartedThreadCount());
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadPeakCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 313 */     return new Long(getThreadMXBean().getPeakThreadCount());
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadDaemonCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 320 */     return new Long(getThreadMXBean().getDaemonThreadCount());
/*     */   }
/*     */ 
/*     */   public Long getJvmThreadCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 327 */     return new Long(getThreadMXBean().getThreadCount());
/*     */   }
/*     */ 
/*     */   public synchronized Long getJvmThreadPeakCountReset()
/*     */     throws SnmpStatusException
/*     */   {
/* 335 */     return new Long(this.jvmThreadPeakCountReset);
/*     */   }
/*     */ 
/*     */   public synchronized void setJvmThreadPeakCountReset(Long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 343 */     long l1 = paramLong.longValue();
/* 344 */     if (l1 > this.jvmThreadPeakCountReset) {
/* 345 */       long l2 = System.currentTimeMillis();
/* 346 */       getThreadMXBean().resetPeakThreadCount();
/* 347 */       this.jvmThreadPeakCountReset = l2;
/* 348 */       log.debug("setJvmThreadPeakCountReset", "jvmThreadPeakCountReset=" + l2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void checkJvmThreadPeakCountReset(Long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmThreadingImpl
 * JD-Core Version:    0.6.2
 */