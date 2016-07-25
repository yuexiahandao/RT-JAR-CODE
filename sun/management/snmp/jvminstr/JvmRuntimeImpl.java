/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import java.io.File;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.management.MBeanServer;
/*     */ import sun.management.snmp.jvmmib.EnumJvmRTBootClassPathSupport;
/*     */ import sun.management.snmp.jvmmib.JvmRuntimeMBean;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ 
/*     */ public class JvmRuntimeImpl
/*     */   implements JvmRuntimeMBean
/*     */ {
/*  67 */   static final EnumJvmRTBootClassPathSupport JvmRTBootClassPathSupportSupported = new EnumJvmRTBootClassPathSupport("supported");
/*     */ 
/*  70 */   static final EnumJvmRTBootClassPathSupport JvmRTBootClassPathSupportUnSupported = new EnumJvmRTBootClassPathSupport("unsupported");
/*     */ 
/*     */   public JvmRuntimeImpl(SnmpMib paramSnmpMib)
/*     */   {
/*     */   }
/*     */ 
/*     */   public JvmRuntimeImpl(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ 
/*     */   static RuntimeMXBean getRuntimeMXBean()
/*     */   {
/*  93 */     return ManagementFactory.getRuntimeMXBean();
/*     */   }
/*     */ 
/*     */   private static String validDisplayStringTC(String paramString) {
/*  97 */     return JVM_MANAGEMENT_MIB_IMPL.validDisplayStringTC(paramString);
/*     */   }
/*     */ 
/*     */   private static String validPathElementTC(String paramString) {
/* 101 */     return JVM_MANAGEMENT_MIB_IMPL.validPathElementTC(paramString);
/*     */   }
/*     */ 
/*     */   private static String validJavaObjectNameTC(String paramString) {
/* 105 */     return JVM_MANAGEMENT_MIB_IMPL.validJavaObjectNameTC(paramString);
/*     */   }
/*     */ 
/*     */   static String[] splitPath(String paramString)
/*     */   {
/* 110 */     String[] arrayOfString = paramString.split(File.pathSeparator);
/*     */ 
/* 114 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   static String[] getClassPath(Object paramObject) {
/* 118 */     Map localMap = (Map)Util.cast((paramObject instanceof Map) ? paramObject : null);
/*     */ 
/* 124 */     if (localMap != null) {
/* 125 */       arrayOfString = (String[])localMap.get("JvmRuntime.getClassPath");
/* 126 */       if (arrayOfString != null) return arrayOfString;
/*     */     }
/*     */ 
/* 129 */     String[] arrayOfString = splitPath(getRuntimeMXBean().getClassPath());
/*     */ 
/* 131 */     if (localMap != null) localMap.put("JvmRuntime.getClassPath", arrayOfString);
/* 132 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   static String[] getBootClassPath(Object paramObject) {
/* 136 */     if (!getRuntimeMXBean().isBootClassPathSupported()) {
/* 137 */       return new String[0];
/*     */     }
/* 139 */     Map localMap = (Map)Util.cast((paramObject instanceof Map) ? paramObject : null);
/*     */ 
/* 145 */     if (localMap != null) {
/* 146 */       arrayOfString = (String[])localMap.get("JvmRuntime.getBootClassPath");
/* 147 */       if (arrayOfString != null) return arrayOfString;
/*     */     }
/*     */ 
/* 150 */     String[] arrayOfString = splitPath(getRuntimeMXBean().getBootClassPath());
/*     */ 
/* 152 */     if (localMap != null) localMap.put("JvmRuntime.getBootClassPath", arrayOfString);
/* 153 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   static String[] getLibraryPath(Object paramObject) {
/* 157 */     Map localMap = (Map)Util.cast((paramObject instanceof Map) ? paramObject : null);
/*     */ 
/* 163 */     if (localMap != null) {
/* 164 */       arrayOfString = (String[])localMap.get("JvmRuntime.getLibraryPath");
/* 165 */       if (arrayOfString != null) return arrayOfString;
/*     */     }
/*     */ 
/* 168 */     String[] arrayOfString = splitPath(getRuntimeMXBean().getLibraryPath());
/*     */ 
/* 170 */     if (localMap != null) localMap.put("JvmRuntime.getLibraryPath", arrayOfString);
/* 171 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   static String[] getInputArguments(Object paramObject) {
/* 175 */     Map localMap = (Map)Util.cast((paramObject instanceof Map) ? paramObject : null);
/*     */ 
/* 181 */     if (localMap != null) {
/* 182 */       localObject = (String[])localMap.get("JvmRuntime.getInputArguments");
/* 183 */       if (localObject != null) return localObject;
/*     */     }
/*     */ 
/* 186 */     Object localObject = getRuntimeMXBean().getInputArguments();
/* 187 */     String[] arrayOfString = (String[])((List)localObject).toArray(new String[0]);
/*     */ 
/* 189 */     if (localMap != null) localMap.put("JvmRuntime.getInputArguments", arrayOfString);
/* 190 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public String getJvmRTSpecVendor()
/*     */     throws SnmpStatusException
/*     */   {
/* 197 */     return validDisplayStringTC(getRuntimeMXBean().getSpecVendor());
/*     */   }
/*     */ 
/*     */   public String getJvmRTSpecName()
/*     */     throws SnmpStatusException
/*     */   {
/* 204 */     return validDisplayStringTC(getRuntimeMXBean().getSpecName());
/*     */   }
/*     */ 
/*     */   public String getJvmRTVMVersion()
/*     */     throws SnmpStatusException
/*     */   {
/* 211 */     return validDisplayStringTC(getRuntimeMXBean().getVmVersion());
/*     */   }
/*     */ 
/*     */   public String getJvmRTVMVendor()
/*     */     throws SnmpStatusException
/*     */   {
/* 218 */     return validDisplayStringTC(getRuntimeMXBean().getVmVendor());
/*     */   }
/*     */ 
/*     */   public String getJvmRTManagementSpecVersion()
/*     */     throws SnmpStatusException
/*     */   {
/* 225 */     return validDisplayStringTC(getRuntimeMXBean().getManagementSpecVersion());
/*     */   }
/*     */ 
/*     */   public String getJvmRTVMName()
/*     */     throws SnmpStatusException
/*     */   {
/* 233 */     return validJavaObjectNameTC(getRuntimeMXBean().getVmName());
/*     */   }
/*     */ 
/*     */   public Integer getJvmRTInputArgsCount()
/*     */     throws SnmpStatusException
/*     */   {
/* 242 */     String[] arrayOfString = getInputArguments(JvmContextFactory.getUserData());
/*     */ 
/* 244 */     return new Integer(arrayOfString.length);
/*     */   }
/*     */ 
/*     */   public EnumJvmRTBootClassPathSupport getJvmRTBootClassPathSupport()
/*     */     throws SnmpStatusException
/*     */   {
/* 252 */     if (getRuntimeMXBean().isBootClassPathSupported()) {
/* 253 */       return JvmRTBootClassPathSupportSupported;
/*     */     }
/* 255 */     return JvmRTBootClassPathSupportUnSupported;
/*     */   }
/*     */ 
/*     */   public Long getJvmRTUptimeMs()
/*     */     throws SnmpStatusException
/*     */   {
/* 262 */     return new Long(getRuntimeMXBean().getUptime());
/*     */   }
/*     */ 
/*     */   public Long getJvmRTStartTimeMs()
/*     */     throws SnmpStatusException
/*     */   {
/* 269 */     return new Long(getRuntimeMXBean().getStartTime());
/*     */   }
/*     */ 
/*     */   public String getJvmRTSpecVersion()
/*     */     throws SnmpStatusException
/*     */   {
/* 276 */     return validDisplayStringTC(getRuntimeMXBean().getSpecVersion());
/*     */   }
/*     */ 
/*     */   public String getJvmRTName()
/*     */     throws SnmpStatusException
/*     */   {
/* 283 */     return validDisplayStringTC(getRuntimeMXBean().getName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.JvmRuntimeImpl
 * JD-Core Version:    0.6.2
 */