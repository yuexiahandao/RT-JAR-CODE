/*     */ package sun.management;
/*     */ 
/*     */ import java.lang.management.RuntimeMXBean;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ class RuntimeImpl
/*     */   implements RuntimeMXBean
/*     */ {
/*     */   private final VMManagement jvm;
/*     */   private final long vmStartupTime;
/*     */ 
/*     */   RuntimeImpl(VMManagement paramVMManagement)
/*     */   {
/*  54 */     this.jvm = paramVMManagement;
/*  55 */     this.vmStartupTime = this.jvm.getStartupTime();
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  59 */     return this.jvm.getVmId();
/*     */   }
/*     */ 
/*     */   public String getManagementSpecVersion() {
/*  63 */     return this.jvm.getManagementVersion();
/*     */   }
/*     */ 
/*     */   public String getVmName() {
/*  67 */     return this.jvm.getVmName();
/*     */   }
/*     */ 
/*     */   public String getVmVendor() {
/*  71 */     return this.jvm.getVmVendor();
/*     */   }
/*     */ 
/*     */   public String getVmVersion() {
/*  75 */     return this.jvm.getVmVersion();
/*     */   }
/*     */ 
/*     */   public String getSpecName() {
/*  79 */     return this.jvm.getVmSpecName();
/*     */   }
/*     */ 
/*     */   public String getSpecVendor() {
/*  83 */     return this.jvm.getVmSpecVendor();
/*     */   }
/*     */ 
/*     */   public String getSpecVersion() {
/*  87 */     return this.jvm.getVmSpecVersion();
/*     */   }
/*     */ 
/*     */   public String getClassPath() {
/*  91 */     return this.jvm.getClassPath();
/*     */   }
/*     */ 
/*     */   public String getLibraryPath() {
/*  95 */     return this.jvm.getLibraryPath();
/*     */   }
/*     */ 
/*     */   public String getBootClassPath() {
/*  99 */     if (!isBootClassPathSupported()) {
/* 100 */       throw new UnsupportedOperationException("Boot class path mechanism is not supported");
/*     */     }
/*     */ 
/* 103 */     Util.checkMonitorAccess();
/* 104 */     return this.jvm.getBootClassPath();
/*     */   }
/*     */ 
/*     */   public List<String> getInputArguments() {
/* 108 */     Util.checkMonitorAccess();
/* 109 */     return this.jvm.getVmArguments();
/*     */   }
/*     */ 
/*     */   public long getUptime() {
/* 113 */     long l = System.currentTimeMillis();
/*     */ 
/* 118 */     return l - this.vmStartupTime;
/*     */   }
/*     */ 
/*     */   public long getStartTime() {
/* 122 */     return this.vmStartupTime;
/*     */   }
/*     */ 
/*     */   public boolean isBootClassPathSupported() {
/* 126 */     return this.jvm.isBootClassPathSupported();
/*     */   }
/*     */ 
/*     */   public Map<String, String> getSystemProperties() {
/* 130 */     Properties localProperties = System.getProperties();
/* 131 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 136 */     Set localSet = localProperties.stringPropertyNames();
/* 137 */     for (String str1 : localSet) {
/* 138 */       String str2 = localProperties.getProperty(str1);
/* 139 */       localHashMap.put(str1, str2);
/*     */     }
/*     */ 
/* 142 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   public ObjectName getObjectName() {
/* 146 */     return Util.newObjectName("java.lang:type=Runtime");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.RuntimeImpl
 * JD-Core Version:    0.6.2
 */