/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import java.lang.management.CompilationMXBean;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import javax.management.MBeanServer;
/*     */ import sun.management.snmp.jvmmib.EnumJvmJITCompilerTimeMonitoring;
/*     */ import sun.management.snmp.jvmmib.JvmCompilationMBean;
/*     */ 
/*     */ public class JvmCompilationImpl
/*     */   implements JvmCompilationMBean
/*     */ {
/*  64 */   static final EnumJvmJITCompilerTimeMonitoring JvmJITCompilerTimeMonitoringSupported = new EnumJvmJITCompilerTimeMonitoring("supported");
/*     */ 
/*  67 */   static final EnumJvmJITCompilerTimeMonitoring JvmJITCompilerTimeMonitoringUnsupported = new EnumJvmJITCompilerTimeMonitoring("unsupported");
/*     */ 
/*     */   public JvmCompilationImpl(SnmpMib paramSnmpMib)
/*     */   {
/*     */   }
/*     */ 
/*     */   public JvmCompilationImpl(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ 
/*     */   private static CompilationMXBean getCompilationMXBean()
/*     */   {
/*  89 */     return ManagementFactory.getCompilationMXBean();
/*     */   }
/*     */ 
/*     */   public EnumJvmJITCompilerTimeMonitoring getJvmJITCompilerTimeMonitoring()
/*     */     throws SnmpStatusException
/*     */   {
/* 103 */     if (getCompilationMXBean().isCompilationTimeMonitoringSupported()) {
/* 104 */       return JvmJITCompilerTimeMonitoringSupported;
/*     */     }
/* 106 */     return JvmJITCompilerTimeMonitoringUnsupported;
/*     */   }
/*     */ 
/*     */   public Long getJvmJITCompilerTimeMs()
/*     */     throws SnmpStatusException
/*     */   {
/*     */     long l;
/* 114 */     if (getCompilationMXBean().isCompilationTimeMonitoringSupported())
/* 115 */       l = getCompilationMXBean().getTotalCompilationTime();
/*     */     else
/* 117 */       l = 0L;
/* 118 */     return new Long(l);
/*     */   }
/*     */ 
/*     */   public String getJvmJITCompilerName()
/*     */     throws SnmpStatusException
/*     */   {
/* 125 */     return JVM_MANAGEMENT_MIB_IMPL.validJavaObjectNameTC(getCompilationMXBean().getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmCompilationImpl
 * JD-Core Version:    0.6.2
 */