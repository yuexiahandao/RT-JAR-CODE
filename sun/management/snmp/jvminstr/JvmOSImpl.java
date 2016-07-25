/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import java.io.Serializable;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.OperatingSystemMXBean;
/*     */ import javax.management.MBeanServer;
/*     */ import sun.management.snmp.jvmmib.JvmOSMBean;
/*     */ 
/*     */ public class JvmOSImpl
/*     */   implements JvmOSMBean, Serializable
/*     */ {
/*     */   public JvmOSImpl(SnmpMib paramSnmpMib)
/*     */   {
/*     */   }
/*     */ 
/*     */   public JvmOSImpl(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ 
/*     */   static OperatingSystemMXBean getOSMBean()
/*     */   {
/*  68 */     return ManagementFactory.getOperatingSystemMXBean();
/*     */   }
/*     */ 
/*     */   private static String validDisplayStringTC(String paramString) {
/*  72 */     return JVM_MANAGEMENT_MIB_IMPL.validDisplayStringTC(paramString);
/*     */   }
/*     */ 
/*     */   private static String validJavaObjectNameTC(String paramString) {
/*  76 */     return JVM_MANAGEMENT_MIB_IMPL.validJavaObjectNameTC(paramString);
/*     */   }
/*     */ 
/*     */   public Integer getJvmOSProcessorCount()
/*     */     throws SnmpStatusException
/*     */   {
/*  83 */     return new Integer(getOSMBean().getAvailableProcessors());
/*     */   }
/*     */ 
/*     */   public String getJvmOSVersion()
/*     */     throws SnmpStatusException
/*     */   {
/*  91 */     return validDisplayStringTC(getOSMBean().getVersion());
/*     */   }
/*     */ 
/*     */   public String getJvmOSArch()
/*     */     throws SnmpStatusException
/*     */   {
/*  98 */     return validDisplayStringTC(getOSMBean().getArch());
/*     */   }
/*     */ 
/*     */   public String getJvmOSName()
/*     */     throws SnmpStatusException
/*     */   {
/* 105 */     return validJavaObjectNameTC(getOSMBean().getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmOSImpl
 * JD-Core Version:    0.6.2
 */