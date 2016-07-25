/*     */ package sun.management.snmp;
/*     */ 
/*     */ import com.sun.jmx.snmp.IPAcl.SnmpAcl;
/*     */ import com.sun.jmx.snmp.InetAddressAcl;
/*     */ import com.sun.jmx.snmp.daemon.CommunicationException;
/*     */ import com.sun.jmx.snmp.daemon.SnmpAdaptorServer;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import sun.management.Agent;
/*     */ import sun.management.AgentConfigurationError;
/*     */ import sun.management.FileSystem;
/*     */ import sun.management.snmp.jvminstr.JVM_MANAGEMENT_MIB_IMPL;
/*     */ import sun.management.snmp.jvminstr.NotificationTarget;
/*     */ import sun.management.snmp.jvminstr.NotificationTargetImpl;
/*     */ import sun.management.snmp.util.JvmContextFactory;
/*     */ import sun.management.snmp.util.MibLogger;
/*     */ 
/*     */ public final class AdaptorBootstrap
/*     */ {
/*  61 */   private static final MibLogger log = new MibLogger(AdaptorBootstrap.class);
/*     */   private SnmpAdaptorServer adaptor;
/*     */   private JVM_MANAGEMENT_MIB_IMPL jvmmib;
/*     */ 
/*     */   private AdaptorBootstrap(SnmpAdaptorServer paramSnmpAdaptorServer, JVM_MANAGEMENT_MIB_IMPL paramJVM_MANAGEMENT_MIB_IMPL)
/*     */   {
/* 103 */     this.jvmmib = paramJVM_MANAGEMENT_MIB_IMPL;
/* 104 */     this.adaptor = paramSnmpAdaptorServer;
/*     */   }
/*     */ 
/*     */   private static String getDefaultFileName(String paramString)
/*     */   {
/* 113 */     String str = File.separator;
/* 114 */     return System.getProperty("java.home") + str + "lib" + str + "management" + str + paramString;
/*     */   }
/*     */ 
/*     */   private static List<NotificationTarget> getTargetList(InetAddressAcl paramInetAddressAcl, int paramInt)
/*     */   {
/* 123 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 125 */     if (paramInetAddressAcl != null) {
/* 126 */       if (log.isDebugOn()) {
/* 127 */         log.debug("getTargetList", Agent.getText("jmxremote.AdaptorBootstrap.getTargetList.processing"));
/*     */       }
/* 129 */       Enumeration localEnumeration1 = paramInetAddressAcl.getTrapDestinations();
/* 130 */       while (localEnumeration1.hasMoreElements()) {
/* 131 */         InetAddress localInetAddress = (InetAddress)localEnumeration1.nextElement();
/* 132 */         Enumeration localEnumeration2 = paramInetAddressAcl.getTrapCommunities(localInetAddress);
/*     */ 
/* 134 */         while (localEnumeration2.hasMoreElements()) {
/* 135 */           String str = (String)localEnumeration2.nextElement();
/* 136 */           NotificationTargetImpl localNotificationTargetImpl = new NotificationTargetImpl(localInetAddress, paramInt, str);
/*     */ 
/* 140 */           if (log.isDebugOn()) {
/* 141 */             log.debug("getTargetList", Agent.getText("jmxremote.AdaptorBootstrap.getTargetList.adding", new String[] { localNotificationTargetImpl.toString() }));
/*     */           }
/*     */ 
/* 144 */           localArrayList.add(localNotificationTargetImpl);
/*     */         }
/*     */       }
/*     */     }
/* 148 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public static synchronized AdaptorBootstrap initialize()
/*     */   {
/* 161 */     Properties localProperties = Agent.loadManagementProperties();
/* 162 */     if (localProperties == null) return null;
/*     */ 
/* 164 */     String str = localProperties.getProperty("com.sun.management.snmp.port");
/*     */ 
/* 166 */     return initialize(str, localProperties);
/*     */   }
/*     */ 
/*     */   public static synchronized AdaptorBootstrap initialize(String paramString, Properties paramProperties)
/*     */   {
/* 176 */     if (paramString.length() == 0) paramString = "161"; int i;
/*     */     try
/*     */     {
/* 179 */       i = Integer.parseInt(paramString);
/*     */     } catch (NumberFormatException localNumberFormatException1) {
/* 181 */       throw new AgentConfigurationError("agent.err.invalid.snmp.port", localNumberFormatException1, new String[] { paramString });
/*     */     }
/*     */ 
/* 184 */     if (i < 0) {
/* 185 */       throw new AgentConfigurationError("agent.err.invalid.snmp.port", new String[] { paramString });
/*     */     }
/*     */ 
/* 189 */     String str1 = paramProperties.getProperty("com.sun.management.snmp.trap", "162");
/*     */     int j;
/*     */     try
/*     */     {
/* 195 */       j = Integer.parseInt(str1);
/*     */     } catch (NumberFormatException localNumberFormatException2) {
/* 197 */       throw new AgentConfigurationError("agent.err.invalid.snmp.trap.port", localNumberFormatException2, new String[] { str1 });
/*     */     }
/*     */ 
/* 200 */     if (j < 0) {
/* 201 */       throw new AgentConfigurationError("agent.err.invalid.snmp.trap.port", new String[] { str1 });
/*     */     }
/*     */ 
/* 205 */     String str2 = paramProperties.getProperty("com.sun.management.snmp.interface", "localhost");
/*     */ 
/* 210 */     String str3 = getDefaultFileName("snmp.acl");
/*     */ 
/* 212 */     String str4 = paramProperties.getProperty("com.sun.management.snmp.acl.file", str3);
/*     */ 
/* 215 */     String str5 = paramProperties.getProperty("com.sun.management.snmp.acl", "true");
/*     */ 
/* 217 */     boolean bool = Boolean.valueOf(str5).booleanValue();
/*     */ 
/* 220 */     if (bool) checkAclFile(str4);
/*     */ 
/* 222 */     AdaptorBootstrap localAdaptorBootstrap = null;
/*     */     try {
/* 224 */       localAdaptorBootstrap = getAdaptorBootstrap(i, j, str2, bool, str4);
/*     */     }
/*     */     catch (Exception localException) {
/* 227 */       throw new AgentConfigurationError("agent.err.exception", localException, new String[] { localException.getMessage() });
/*     */     }
/* 229 */     return localAdaptorBootstrap;
/*     */   }
/*     */ 
/*     */   private static AdaptorBootstrap getAdaptorBootstrap(int paramInt1, int paramInt2, String paramString1, boolean paramBoolean, String paramString2)
/*     */   {
/*     */     InetAddress localInetAddress;
/*     */     try
/*     */     {
/* 238 */       localInetAddress = InetAddress.getByName(paramString1);
/*     */     } catch (UnknownHostException localUnknownHostException1) {
/* 240 */       throw new AgentConfigurationError("agent.err.unknown.snmp.interface", localUnknownHostException1, new String[] { paramString1 });
/*     */     }
/* 242 */     if (log.isDebugOn()) {
/* 243 */       log.debug("initialize", Agent.getText("jmxremote.AdaptorBootstrap.getTargetList.starting\n\tcom.sun.management.snmp.port=" + paramInt1 + "\n\t" + "com.sun.management.snmp.trap" + "=" + paramInt2 + "\n\t" + "com.sun.management.snmp.interface" + "=" + localInetAddress + (paramBoolean ? "\n\tcom.sun.management.snmp.acl.file=" + paramString2 : "\n\tNo ACL") + ""));
/*     */     }
/*     */ 
/*     */     InetAddressAcl localInetAddressAcl;
/*     */     try
/*     */     {
/* 255 */       localInetAddressAcl = paramBoolean ? new SnmpAcl(System.getProperty("user.name"), paramString2) : null;
/*     */     }
/*     */     catch (UnknownHostException localUnknownHostException2) {
/* 258 */       throw new AgentConfigurationError("agent.err.unknown.snmp.interface", localUnknownHostException2, new String[] { localUnknownHostException2.getMessage() });
/*     */     }
/*     */ 
/* 262 */     SnmpAdaptorServer localSnmpAdaptorServer = new SnmpAdaptorServer(localInetAddressAcl, paramInt1, localInetAddress);
/*     */ 
/* 264 */     localSnmpAdaptorServer.setUserDataFactory(new JvmContextFactory());
/* 265 */     localSnmpAdaptorServer.setTrapPort(paramInt2);
/*     */ 
/* 269 */     JVM_MANAGEMENT_MIB_IMPL localJVM_MANAGEMENT_MIB_IMPL = new JVM_MANAGEMENT_MIB_IMPL();
/*     */     try {
/* 271 */       localJVM_MANAGEMENT_MIB_IMPL.init();
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 273 */       throw new AgentConfigurationError("agent.err.snmp.mib.init.failed", localIllegalAccessException, new String[] { localIllegalAccessException.getMessage() });
/*     */     }
/*     */ 
/* 278 */     localJVM_MANAGEMENT_MIB_IMPL.addTargets(getTargetList(localInetAddressAcl, paramInt2));
/*     */     try
/*     */     {
/* 288 */       localSnmpAdaptorServer.start(9223372036854775807L);
/*     */     } catch (Exception localException) {
/* 290 */       Object localObject = localException;
/* 291 */       if ((localException instanceof CommunicationException)) {
/* 292 */         Throwable localThrowable = ((Throwable)localObject).getCause();
/* 293 */         if (localThrowable != null) localObject = localThrowable;
/*     */       }
/* 295 */       throw new AgentConfigurationError("agent.err.snmp.adaptor.start.failed", (Throwable)localObject, new String[] { localInetAddress + ":" + paramInt1, "(" + ((Throwable)localObject).getMessage() + ")" });
/*     */     }
/*     */ 
/* 303 */     if (!localSnmpAdaptorServer.isActive()) {
/* 304 */       throw new AgentConfigurationError("agent.err.snmp.adaptor.start.failed", new String[] { localInetAddress + ":" + paramInt1 });
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 311 */       localSnmpAdaptorServer.addMib(localJVM_MANAGEMENT_MIB_IMPL);
/*     */ 
/* 315 */       localJVM_MANAGEMENT_MIB_IMPL.setSnmpAdaptor(localSnmpAdaptorServer);
/*     */     } catch (RuntimeException localRuntimeException) {
/* 317 */       new AdaptorBootstrap(localSnmpAdaptorServer, localJVM_MANAGEMENT_MIB_IMPL).terminate();
/* 318 */       throw localRuntimeException;
/*     */     }
/*     */ 
/* 321 */     log.debug("initialize", Agent.getText("jmxremote.AdaptorBootstrap.getTargetList.initialize1"));
/*     */ 
/* 323 */     log.config("initialize", Agent.getText("jmxremote.AdaptorBootstrap.getTargetList.initialize2", new String[] { localInetAddress.toString(), Integer.toString(localSnmpAdaptorServer.getPort()) }));
/*     */ 
/* 326 */     return new AdaptorBootstrap(localSnmpAdaptorServer, localJVM_MANAGEMENT_MIB_IMPL);
/*     */   }
/*     */ 
/*     */   private static void checkAclFile(String paramString) {
/* 330 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 331 */       throw new AgentConfigurationError("agent.err.acl.file.notset");
/*     */     }
/* 333 */     File localFile = new File(paramString);
/* 334 */     if (!localFile.exists()) {
/* 335 */       throw new AgentConfigurationError("agent.err.acl.file.notfound", new String[] { paramString });
/*     */     }
/* 337 */     if (!localFile.canRead()) {
/* 338 */       throw new AgentConfigurationError("agent.err.acl.file.not.readable", new String[] { paramString });
/*     */     }
/*     */ 
/* 341 */     FileSystem localFileSystem = FileSystem.open();
/*     */     try {
/* 343 */       if ((localFileSystem.supportsFileSecurity(localFile)) && 
/* 344 */         (!localFileSystem.isAccessUserOnly(localFile))) {
/* 345 */         throw new AgentConfigurationError("agent.err.acl.file.access.notrestricted", new String[] { paramString });
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 350 */       throw new AgentConfigurationError("agent.err.acl.file.read.failed", new String[] { paramString });
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized int getPort()
/*     */   {
/* 362 */     if (this.adaptor != null) return this.adaptor.getPort();
/* 363 */     return 0;
/*     */   }
/*     */ 
/*     */   public synchronized void terminate()
/*     */   {
/* 370 */     if (this.adaptor == null) return;
/*     */ 
/*     */     try
/*     */     {
/* 376 */       this.jvmmib.terminate();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 380 */       log.debug("jmxremote.AdaptorBootstrap.getTargetList.terminate", localException.toString());
/*     */     }
/*     */     finally {
/* 383 */       this.jvmmib = null;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 389 */       this.adaptor.stop();
/*     */     } finally {
/* 391 */       this.adaptor = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface DefaultValues
/*     */   {
/*     */     public static final String PORT = "161";
/*     */     public static final String CONFIG_FILE_NAME = "management.properties";
/*     */     public static final String TRAP_PORT = "162";
/*     */     public static final String USE_ACL = "true";
/*     */     public static final String ACL_FILE_NAME = "snmp.acl";
/*     */     public static final String BIND_ADDRESS = "localhost";
/*     */   }
/*     */ 
/*     */   public static abstract interface PropertyNames
/*     */   {
/*     */     public static final String PORT = "com.sun.management.snmp.port";
/*     */     public static final String CONFIG_FILE_NAME = "com.sun.management.config.file";
/*     */     public static final String TRAP_PORT = "com.sun.management.snmp.trap";
/*     */     public static final String USE_ACL = "com.sun.management.snmp.acl";
/*     */     public static final String ACL_FILE_NAME = "com.sun.management.snmp.acl.file";
/*     */     public static final String BIND_ADDRESS = "com.sun.management.snmp.interface";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.AdaptorBootstrap
 * JD-Core Version:    0.6.2
 */