/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibTable;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.io.Serializable;
/*     */ import java.util.Hashtable;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public abstract class JVM_MANAGEMENT_MIB extends SnmpMib
/*     */   implements Serializable
/*     */ {
/* 680 */   private boolean isInitialized = false;
/*     */   protected SnmpStandardObjectServer objectserver;
/* 684 */   protected final Hashtable<String, SnmpMibTable> metadatas = new Hashtable();
/*     */ 
/*     */   public JVM_MANAGEMENT_MIB()
/*     */   {
/*  60 */     this.mibName = "JVM_MANAGEMENT_MIB";
/*     */   }
/*     */ 
/*     */   public void init()
/*     */     throws IllegalAccessException
/*     */   {
/*  69 */     if (this.isInitialized == true) {
/*  70 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  74 */       populate(null, null);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*  76 */       throw localIllegalAccessException;
/*     */     } catch (RuntimeException localRuntimeException) {
/*  78 */       throw localRuntimeException;
/*     */     } catch (Exception localException) {
/*  80 */       throw new Error(localException.getMessage());
/*     */     }
/*     */ 
/*  83 */     this.isInitialized = true;
/*     */   }
/*     */ 
/*     */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*     */     throws Exception
/*     */   {
/*  93 */     if (this.isInitialized == true) {
/*  94 */       throw new InstanceAlreadyExistsException();
/*     */     }
/*     */ 
/*  99 */     this.server = paramMBeanServer;
/*     */ 
/* 101 */     populate(paramMBeanServer, paramObjectName);
/*     */ 
/* 103 */     this.isInitialized = true;
/* 104 */     return paramObjectName;
/*     */   }
/*     */ 
/*     */   public void populate(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*     */     throws Exception
/*     */   {
/* 114 */     if (this.isInitialized == true) {
/* 115 */       return;
/*     */     }
/*     */ 
/* 118 */     if (this.objectserver == null) {
/* 119 */       this.objectserver = new SnmpStandardObjectServer();
/*     */     }
/*     */ 
/* 125 */     initJvmOS(paramMBeanServer);
/*     */ 
/* 131 */     initJvmCompilation(paramMBeanServer);
/*     */ 
/* 137 */     initJvmRuntime(paramMBeanServer);
/*     */ 
/* 143 */     initJvmThreading(paramMBeanServer);
/*     */ 
/* 149 */     initJvmMemory(paramMBeanServer);
/*     */ 
/* 155 */     initJvmClassLoading(paramMBeanServer);
/*     */ 
/* 157 */     this.isInitialized = true;
/*     */   }
/*     */ 
/*     */   protected void initJvmOS(MBeanServer paramMBeanServer)
/*     */     throws Exception
/*     */   {
/* 179 */     String str = getGroupOid("JvmOS", "1.3.6.1.4.1.42.2.145.3.163.1.1.6");
/* 180 */     ObjectName localObjectName = null;
/* 181 */     if (paramMBeanServer != null) {
/* 182 */       localObjectName = getGroupObjectName("JvmOS", str, this.mibName + ":name=sun.management.snmp.jvmmib.JvmOS");
/*     */     }
/* 184 */     JvmOSMeta localJvmOSMeta = createJvmOSMetaNode("JvmOS", str, localObjectName, paramMBeanServer);
/* 185 */     if (localJvmOSMeta != null) {
/* 186 */       localJvmOSMeta.registerTableNodes(this, paramMBeanServer);
/*     */ 
/* 192 */       JvmOSMBean localJvmOSMBean = (JvmOSMBean)createJvmOSMBean("JvmOS", str, localObjectName, paramMBeanServer);
/* 193 */       localJvmOSMeta.setInstance(localJvmOSMBean);
/* 194 */       registerGroupNode("JvmOS", str, localObjectName, localJvmOSMeta, localJvmOSMBean, paramMBeanServer);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmOSMeta createJvmOSMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 216 */     return new JvmOSMeta(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected abstract Object createJvmOSMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer);
/*     */ 
/*     */   protected void initJvmCompilation(MBeanServer paramMBeanServer)
/*     */     throws Exception
/*     */   {
/* 260 */     String str = getGroupOid("JvmCompilation", "1.3.6.1.4.1.42.2.145.3.163.1.1.5");
/* 261 */     ObjectName localObjectName = null;
/* 262 */     if (paramMBeanServer != null) {
/* 263 */       localObjectName = getGroupObjectName("JvmCompilation", str, this.mibName + ":name=sun.management.snmp.jvmmib.JvmCompilation");
/*     */     }
/* 265 */     JvmCompilationMeta localJvmCompilationMeta = createJvmCompilationMetaNode("JvmCompilation", str, localObjectName, paramMBeanServer);
/* 266 */     if (localJvmCompilationMeta != null) {
/* 267 */       localJvmCompilationMeta.registerTableNodes(this, paramMBeanServer);
/*     */ 
/* 273 */       JvmCompilationMBean localJvmCompilationMBean = (JvmCompilationMBean)createJvmCompilationMBean("JvmCompilation", str, localObjectName, paramMBeanServer);
/* 274 */       localJvmCompilationMeta.setInstance(localJvmCompilationMBean);
/* 275 */       registerGroupNode("JvmCompilation", str, localObjectName, localJvmCompilationMeta, localJvmCompilationMBean, paramMBeanServer);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmCompilationMeta createJvmCompilationMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 297 */     return new JvmCompilationMeta(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected abstract Object createJvmCompilationMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer);
/*     */ 
/*     */   protected void initJvmRuntime(MBeanServer paramMBeanServer)
/*     */     throws Exception
/*     */   {
/* 341 */     String str = getGroupOid("JvmRuntime", "1.3.6.1.4.1.42.2.145.3.163.1.1.4");
/* 342 */     ObjectName localObjectName = null;
/* 343 */     if (paramMBeanServer != null) {
/* 344 */       localObjectName = getGroupObjectName("JvmRuntime", str, this.mibName + ":name=sun.management.snmp.jvmmib.JvmRuntime");
/*     */     }
/* 346 */     JvmRuntimeMeta localJvmRuntimeMeta = createJvmRuntimeMetaNode("JvmRuntime", str, localObjectName, paramMBeanServer);
/* 347 */     if (localJvmRuntimeMeta != null) {
/* 348 */       localJvmRuntimeMeta.registerTableNodes(this, paramMBeanServer);
/*     */ 
/* 354 */       JvmRuntimeMBean localJvmRuntimeMBean = (JvmRuntimeMBean)createJvmRuntimeMBean("JvmRuntime", str, localObjectName, paramMBeanServer);
/* 355 */       localJvmRuntimeMeta.setInstance(localJvmRuntimeMBean);
/* 356 */       registerGroupNode("JvmRuntime", str, localObjectName, localJvmRuntimeMeta, localJvmRuntimeMBean, paramMBeanServer);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmRuntimeMeta createJvmRuntimeMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 378 */     return new JvmRuntimeMeta(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected abstract Object createJvmRuntimeMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer);
/*     */ 
/*     */   protected void initJvmThreading(MBeanServer paramMBeanServer)
/*     */     throws Exception
/*     */   {
/* 422 */     String str = getGroupOid("JvmThreading", "1.3.6.1.4.1.42.2.145.3.163.1.1.3");
/* 423 */     ObjectName localObjectName = null;
/* 424 */     if (paramMBeanServer != null) {
/* 425 */       localObjectName = getGroupObjectName("JvmThreading", str, this.mibName + ":name=sun.management.snmp.jvmmib.JvmThreading");
/*     */     }
/* 427 */     JvmThreadingMeta localJvmThreadingMeta = createJvmThreadingMetaNode("JvmThreading", str, localObjectName, paramMBeanServer);
/* 428 */     if (localJvmThreadingMeta != null) {
/* 429 */       localJvmThreadingMeta.registerTableNodes(this, paramMBeanServer);
/*     */ 
/* 435 */       JvmThreadingMBean localJvmThreadingMBean = (JvmThreadingMBean)createJvmThreadingMBean("JvmThreading", str, localObjectName, paramMBeanServer);
/* 436 */       localJvmThreadingMeta.setInstance(localJvmThreadingMBean);
/* 437 */       registerGroupNode("JvmThreading", str, localObjectName, localJvmThreadingMeta, localJvmThreadingMBean, paramMBeanServer);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmThreadingMeta createJvmThreadingMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 459 */     return new JvmThreadingMeta(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected abstract Object createJvmThreadingMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer);
/*     */ 
/*     */   protected void initJvmMemory(MBeanServer paramMBeanServer)
/*     */     throws Exception
/*     */   {
/* 503 */     String str = getGroupOid("JvmMemory", "1.3.6.1.4.1.42.2.145.3.163.1.1.2");
/* 504 */     ObjectName localObjectName = null;
/* 505 */     if (paramMBeanServer != null) {
/* 506 */       localObjectName = getGroupObjectName("JvmMemory", str, this.mibName + ":name=sun.management.snmp.jvmmib.JvmMemory");
/*     */     }
/* 508 */     JvmMemoryMeta localJvmMemoryMeta = createJvmMemoryMetaNode("JvmMemory", str, localObjectName, paramMBeanServer);
/* 509 */     if (localJvmMemoryMeta != null) {
/* 510 */       localJvmMemoryMeta.registerTableNodes(this, paramMBeanServer);
/*     */ 
/* 516 */       JvmMemoryMBean localJvmMemoryMBean = (JvmMemoryMBean)createJvmMemoryMBean("JvmMemory", str, localObjectName, paramMBeanServer);
/* 517 */       localJvmMemoryMeta.setInstance(localJvmMemoryMBean);
/* 518 */       registerGroupNode("JvmMemory", str, localObjectName, localJvmMemoryMeta, localJvmMemoryMBean, paramMBeanServer);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmMemoryMeta createJvmMemoryMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 540 */     return new JvmMemoryMeta(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected abstract Object createJvmMemoryMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer);
/*     */ 
/*     */   protected void initJvmClassLoading(MBeanServer paramMBeanServer)
/*     */     throws Exception
/*     */   {
/* 584 */     String str = getGroupOid("JvmClassLoading", "1.3.6.1.4.1.42.2.145.3.163.1.1.1");
/* 585 */     ObjectName localObjectName = null;
/* 586 */     if (paramMBeanServer != null) {
/* 587 */       localObjectName = getGroupObjectName("JvmClassLoading", str, this.mibName + ":name=sun.management.snmp.jvmmib.JvmClassLoading");
/*     */     }
/* 589 */     JvmClassLoadingMeta localJvmClassLoadingMeta = createJvmClassLoadingMetaNode("JvmClassLoading", str, localObjectName, paramMBeanServer);
/* 590 */     if (localJvmClassLoadingMeta != null) {
/* 591 */       localJvmClassLoadingMeta.registerTableNodes(this, paramMBeanServer);
/*     */ 
/* 597 */       JvmClassLoadingMBean localJvmClassLoadingMBean = (JvmClassLoadingMBean)createJvmClassLoadingMBean("JvmClassLoading", str, localObjectName, paramMBeanServer);
/* 598 */       localJvmClassLoadingMeta.setInstance(localJvmClassLoadingMBean);
/* 599 */       registerGroupNode("JvmClassLoading", str, localObjectName, localJvmClassLoadingMeta, localJvmClassLoadingMBean, paramMBeanServer);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected JvmClassLoadingMeta createJvmClassLoadingMetaNode(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer)
/*     */   {
/* 621 */     return new JvmClassLoadingMeta(this, this.objectserver);
/*     */   }
/*     */ 
/*     */   protected abstract Object createJvmClassLoadingMBean(String paramString1, String paramString2, ObjectName paramObjectName, MBeanServer paramMBeanServer);
/*     */ 
/*     */   public void registerTableMeta(String paramString, SnmpMibTable paramSnmpMibTable)
/*     */   {
/* 655 */     if (this.metadatas == null) return;
/* 656 */     if (paramString == null) return;
/* 657 */     this.metadatas.put(paramString, paramSnmpMibTable);
/*     */   }
/*     */ 
/*     */   public SnmpMibTable getRegisteredTableMeta(String paramString)
/*     */   {
/* 669 */     if (this.metadatas == null) return null;
/* 670 */     if (paramString == null) return null;
/* 671 */     return (SnmpMibTable)this.metadatas.get(paramString);
/*     */   }
/*     */ 
/*     */   public SnmpStandardObjectServer getStandardObjectServer() {
/* 675 */     if (this.objectserver == null)
/* 676 */       this.objectserver = new SnmpStandardObjectServer();
/* 677 */     return this.objectserver;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JVM_MANAGEMENT_MIB
 * JD-Core Version:    0.6.2
 */