/*     */ package sun.management;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.management.ThreadMXBean;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.Properties;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.management.remote.JMXConnectorServer;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import sun.management.jdp.JdpController;
/*     */ import sun.management.jdp.JdpException;
/*     */ import sun.management.jmxremote.ConnectorBootstrap;
/*     */ import sun.misc.VMSupport;
/*     */ 
/*     */ public class Agent
/*     */ {
/*     */   private static Properties mgmtProps;
/*     */   private static ResourceBundle messageRB;
/*     */   private static final String CONFIG_FILE = "com.sun.management.config.file";
/*     */   private static final String SNMP_PORT = "com.sun.management.snmp.port";
/*     */   private static final String JMXREMOTE = "com.sun.management.jmxremote";
/*     */   private static final String JMXREMOTE_PORT = "com.sun.management.jmxremote.port";
/*     */   private static final String RMI_PORT = "com.sun.management.jmxremote.rmi.port";
/*     */   private static final String ENABLE_THREAD_CONTENTION_MONITORING = "com.sun.management.enableThreadContentionMonitoring";
/*     */   private static final String LOCAL_CONNECTOR_ADDRESS_PROP = "com.sun.management.jmxremote.localConnectorAddress";
/*     */   private static final String SNMP_ADAPTOR_BOOTSTRAP_CLASS_NAME = "sun.management.snmp.AdaptorBootstrap";
/*     */   private static final String JDP_DEFAULT_ADDRESS = "224.0.23.178";
/*     */   private static final int JDP_DEFAULT_PORT = 7095;
/*  89 */   private static JMXConnectorServer jmxServer = null;
/*     */ 
/*     */   private static Properties parseString(String paramString)
/*     */   {
/*  95 */     Properties localProperties = new Properties();
/*  96 */     if (paramString != null) {
/*  97 */       for (String str1 : paramString.split(",")) {
/*  98 */         String[] arrayOfString2 = str1.split("=", 2);
/*  99 */         String str2 = arrayOfString2[0].trim();
/* 100 */         String str3 = arrayOfString2.length > 1 ? arrayOfString2[1].trim() : "";
/*     */ 
/* 102 */         if (!str2.startsWith("com.sun.management.")) {
/* 103 */           error("agent.err.invalid.option", str2);
/*     */         }
/*     */ 
/* 106 */         localProperties.setProperty(str2, str3);
/*     */       }
/*     */     }
/*     */ 
/* 110 */     return localProperties;
/*     */   }
/*     */ 
/*     */   public static void premain(String paramString)
/*     */     throws Exception
/*     */   {
/* 116 */     agentmain(paramString);
/*     */   }
/*     */ 
/*     */   public static void agentmain(String paramString) throws Exception
/*     */   {
/* 121 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 122 */       paramString = "com.sun.management.jmxremote";
/*     */     }
/*     */ 
/* 125 */     Properties localProperties1 = parseString(paramString);
/*     */ 
/* 128 */     Properties localProperties2 = new Properties();
/* 129 */     String str = localProperties1.getProperty("com.sun.management.config.file");
/* 130 */     readConfiguration(str, localProperties2);
/*     */ 
/* 133 */     localProperties2.putAll(localProperties1);
/* 134 */     startAgent(localProperties2);
/*     */   }
/*     */ 
/*     */   private static synchronized void startLocalManagementAgent()
/*     */   {
/* 140 */     Properties localProperties = VMSupport.getAgentProperties();
/*     */ 
/* 143 */     if (localProperties.get("com.sun.management.jmxremote.localConnectorAddress") == null) {
/* 144 */       JMXConnectorServer localJMXConnectorServer = ConnectorBootstrap.startLocalConnectorServer();
/* 145 */       String str = localJMXConnectorServer.getAddress().toString();
/*     */ 
/* 147 */       localProperties.put("com.sun.management.jmxremote.localConnectorAddress", str);
/*     */       try
/*     */       {
/* 151 */         ConnectorAddressLink.export(str);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 155 */         warning("agent.err.exportaddress.failed", localException.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized void startRemoteManagementAgent(String paramString)
/*     */     throws Exception
/*     */   {
/* 165 */     if (jmxServer != null) {
/* 166 */       throw new RuntimeException(getText("agent.err.invalid.state", new String[] { "Agent already started" }));
/*     */     }
/*     */ 
/* 169 */     Properties localProperties1 = parseString(paramString);
/* 170 */     Properties localProperties2 = new Properties();
/*     */ 
/* 176 */     String str1 = System.getProperty("com.sun.management.config.file");
/* 177 */     readConfiguration(str1, localProperties2);
/*     */ 
/* 181 */     Properties localProperties3 = System.getProperties();
/* 182 */     synchronized (localProperties3) {
/* 183 */       localProperties2.putAll(localProperties3);
/*     */     }
/*     */ 
/* 189 */     ??? = localProperties1.getProperty("com.sun.management.config.file");
/* 190 */     if (??? != null) {
/* 191 */       readConfiguration((String)???, localProperties2);
/*     */     }
/*     */ 
/* 197 */     localProperties2.putAll(localProperties1);
/*     */ 
/* 202 */     String str2 = localProperties2.getProperty("com.sun.management.enableThreadContentionMonitoring");
/*     */ 
/* 205 */     if (str2 != null) {
/* 206 */       ManagementFactory.getThreadMXBean().setThreadContentionMonitoringEnabled(true);
/*     */     }
/*     */ 
/* 210 */     String str3 = localProperties2.getProperty("com.sun.management.jmxremote.port");
/* 211 */     if (str3 != null) {
/* 212 */       jmxServer = ConnectorBootstrap.startRemoteConnectorServer(str3, localProperties2);
/*     */ 
/* 214 */       startDiscoveryService(localProperties2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized void stopRemoteManagementAgent() throws Exception
/*     */   {
/* 220 */     JdpController.stopDiscoveryService();
/* 221 */     if (jmxServer != null) {
/* 222 */       ConnectorBootstrap.unexportRegistry();
/*     */ 
/* 226 */       jmxServer.stop();
/* 227 */       jmxServer = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void startAgent(Properties paramProperties) throws Exception {
/* 232 */     String str1 = paramProperties.getProperty("com.sun.management.snmp.port");
/* 233 */     String str2 = paramProperties.getProperty("com.sun.management.jmxremote");
/* 234 */     String str3 = paramProperties.getProperty("com.sun.management.jmxremote.port");
/*     */ 
/* 237 */     String str4 = paramProperties.getProperty("com.sun.management.enableThreadContentionMonitoring");
/*     */ 
/* 239 */     if (str4 != null) {
/* 240 */       ManagementFactory.getThreadMXBean().setThreadContentionMonitoringEnabled(true);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 245 */       if (str1 != null) {
/* 246 */         loadSnmpAgent(str1, paramProperties);
/*     */       }
/*     */ 
/* 258 */       if ((str2 != null) || (str3 != null)) {
/* 259 */         if (str3 != null) {
/* 260 */           jmxServer = ConnectorBootstrap.startRemoteConnectorServer(str3, paramProperties);
/*     */ 
/* 262 */           startDiscoveryService(paramProperties);
/*     */         }
/* 264 */         startLocalManagementAgent();
/*     */       }
/*     */     }
/*     */     catch (AgentConfigurationError localAgentConfigurationError) {
/* 268 */       error(localAgentConfigurationError.getError(), localAgentConfigurationError.getParams());
/*     */     } catch (Exception localException) {
/* 270 */       error(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void startDiscoveryService(Properties paramProperties)
/*     */     throws IOException
/*     */   {
/* 277 */     String str1 = paramProperties.getProperty("com.sun.management.jdp.port");
/* 278 */     String str2 = paramProperties.getProperty("com.sun.management.jdp.address");
/* 279 */     String str3 = paramProperties.getProperty("com.sun.management.jmxremote.autodiscovery");
/*     */ 
/* 285 */     boolean bool = false;
/* 286 */     if (str3 == null)
/* 287 */       bool = str1 != null;
/*     */     else {
/*     */       try
/*     */       {
/* 291 */         bool = Boolean.parseBoolean(str3);
/*     */       } catch (NumberFormatException localNumberFormatException1) {
/* 293 */         throw new AgentConfigurationError("Couldn't parse autodiscovery argument");
/*     */       }
/*     */     }
/*     */ 
/* 297 */     if (bool)
/*     */     {
/*     */       InetAddress localInetAddress;
/*     */       try {
/* 301 */         localInetAddress = str2 == null ? InetAddress.getByName("224.0.23.178") : InetAddress.getByName(str2);
/*     */       }
/*     */       catch (UnknownHostException localUnknownHostException) {
/* 304 */         throw new AgentConfigurationError("Unable to broadcast to requested address", localUnknownHostException);
/*     */       }
/*     */ 
/* 307 */       int i = 7095;
/* 308 */       if (str1 != null) {
/*     */         try {
/* 310 */           i = Integer.parseInt(str1);
/*     */         } catch (NumberFormatException localNumberFormatException2) {
/* 312 */           throw new AgentConfigurationError("Couldn't parse JDP port argument");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 317 */       String str4 = paramProperties.getProperty("com.sun.management.jmxremote.port");
/* 318 */       String str5 = paramProperties.getProperty("com.sun.management.jmxremote.rmi.port");
/*     */ 
/* 320 */       JMXServiceURL localJMXServiceURL = jmxServer.getAddress();
/* 321 */       String str6 = localJMXServiceURL.getHost();
/*     */ 
/* 323 */       String str7 = str5 != null ? String.format("service:jmx:rmi://%s:%s/jndi/rmi://%s:%s/jmxrmi", new Object[] { str6, str5, str6, str4 }) : String.format("service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi", new Object[] { str6, str4 });
/*     */ 
/* 330 */       String str8 = System.getProperty("com.sun.management.jdp.name");
/*     */       try
/*     */       {
/* 333 */         JdpController.startDiscoveryService(localInetAddress, i, str8, str7);
/*     */       }
/*     */       catch (JdpException localJdpException) {
/* 336 */         throw new AgentConfigurationError("Couldn't start JDP service", localJdpException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Properties loadManagementProperties() {
/* 342 */     Properties localProperties1 = new Properties();
/*     */ 
/* 346 */     String str = System.getProperty("com.sun.management.config.file");
/* 347 */     readConfiguration(str, localProperties1);
/*     */ 
/* 351 */     Properties localProperties2 = System.getProperties();
/* 352 */     synchronized (localProperties2) {
/* 353 */       localProperties1.putAll(localProperties2);
/*     */     }
/*     */ 
/* 356 */     return localProperties1;
/*     */   }
/*     */ 
/*     */   public static synchronized Properties getManagementProperties() {
/* 360 */     if (mgmtProps == null) {
/* 361 */       String str1 = System.getProperty("com.sun.management.config.file");
/* 362 */       String str2 = System.getProperty("com.sun.management.snmp.port");
/* 363 */       String str3 = System.getProperty("com.sun.management.jmxremote");
/* 364 */       String str4 = System.getProperty("com.sun.management.jmxremote.port");
/*     */ 
/* 366 */       if ((str1 == null) && (str2 == null) && (str3 == null) && (str4 == null))
/*     */       {
/* 369 */         return null;
/*     */       }
/* 371 */       mgmtProps = loadManagementProperties();
/*     */     }
/* 373 */     return mgmtProps;
/*     */   }
/*     */ 
/*     */   private static void loadSnmpAgent(String paramString, Properties paramProperties)
/*     */   {
/*     */     try
/*     */     {
/* 380 */       Class localClass = Class.forName("sun.management.snmp.AdaptorBootstrap", true, null);
/*     */ 
/* 382 */       localObject = localClass.getMethod("initialize", new Class[] { String.class, Properties.class });
/*     */ 
/* 385 */       ((Method)localObject).invoke(null, new Object[] { paramString, paramProperties });
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 388 */       throw new UnsupportedOperationException("Unsupported management property: com.sun.management.snmp.port", localClassNotFoundException);
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException) {
/* 391 */       throw new UnsupportedOperationException("Unsupported management property: com.sun.management.snmp.port", localNoSuchMethodException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 393 */       Object localObject = localInvocationTargetException.getCause();
/* 394 */       if ((localObject instanceof RuntimeException))
/* 395 */         throw ((RuntimeException)localObject);
/* 396 */       if ((localObject instanceof Error)) {
/* 397 */         throw ((Error)localObject);
/*     */       }
/* 399 */       throw new UnsupportedOperationException("Unsupported management property: com.sun.management.snmp.port", (Throwable)localObject);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 402 */       throw new UnsupportedOperationException("Unsupported management property: com.sun.management.snmp.port", localIllegalAccessException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void readConfiguration(String paramString, Properties paramProperties)
/*     */   {
/* 408 */     if (paramString == null) {
/* 409 */       localObject1 = System.getProperty("java.home");
/* 410 */       if (localObject1 == null) {
/* 411 */         throw new Error("Can't find java.home ??");
/*     */       }
/* 413 */       localObject2 = new StringBuffer((String)localObject1);
/* 414 */       ((StringBuffer)localObject2).append(File.separator).append("lib");
/* 415 */       ((StringBuffer)localObject2).append(File.separator).append("management");
/* 416 */       ((StringBuffer)localObject2).append(File.separator).append("management.properties");
/*     */ 
/* 418 */       paramString = ((StringBuffer)localObject2).toString();
/*     */     }
/* 420 */     Object localObject1 = new File(paramString);
/* 421 */     if (!((File)localObject1).exists()) {
/* 422 */       error("agent.err.configfile.notfound", paramString);
/*     */     }
/*     */ 
/* 425 */     Object localObject2 = null;
/*     */     try {
/* 427 */       localObject2 = new FileInputStream((File)localObject1);
/* 428 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream((InputStream)localObject2);
/* 429 */       paramProperties.load(localBufferedInputStream);
/*     */     } catch (FileNotFoundException localFileNotFoundException) {
/* 431 */       error("agent.err.configfile.failed", localFileNotFoundException.getMessage());
/*     */     } catch (IOException localIOException3) {
/* 433 */       error("agent.err.configfile.failed", localIOException3.getMessage());
/*     */     } catch (SecurityException localSecurityException) {
/* 435 */       error("agent.err.configfile.access.denied", paramString);
/*     */     } finally {
/* 437 */       if (localObject2 != null)
/*     */         try {
/* 439 */           ((InputStream)localObject2).close();
/*     */         } catch (IOException localIOException6) {
/* 441 */           error("agent.err.configfile.closed.failed", paramString);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void startAgent() throws Exception
/*     */   {
/* 448 */     String str1 = System.getProperty("com.sun.management.agent.class");
/*     */ 
/* 452 */     if (str1 == null)
/*     */     {
/* 454 */       localObject1 = getManagementProperties();
/* 455 */       if (localObject1 != null) {
/* 456 */         startAgent((Properties)localObject1);
/*     */       }
/* 458 */       return;
/*     */     }
/*     */ 
/* 462 */     Object localObject1 = str1.split(":");
/* 463 */     if ((localObject1.length < 1) || (localObject1.length > 2)) {
/* 464 */       error("agent.err.invalid.agentclass", "\"" + str1 + "\"");
/*     */     }
/* 466 */     String str2 = localObject1[0];
/* 467 */     Object localObject2 = localObject1.length == 2 ? localObject1[1] : null;
/*     */ 
/* 469 */     if ((str2 == null) || (str2.length() == 0)) {
/* 470 */       error("agent.err.invalid.agentclass", "\"" + str1 + "\"");
/*     */     }
/*     */ 
/* 473 */     if (str2 != null)
/*     */     {
/*     */       try
/*     */       {
/* 477 */         Class localClass = ClassLoader.getSystemClassLoader().loadClass(str2);
/* 478 */         localObject3 = localClass.getMethod("premain", new Class[] { String.class });
/*     */ 
/* 480 */         ((Method)localObject3).invoke(null, new Object[] { localObject2 });
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException) {
/* 483 */         error("agent.err.agentclass.notfound", "\"" + str2 + "\"");
/*     */       } catch (NoSuchMethodException localNoSuchMethodException) {
/* 485 */         error("agent.err.premain.notfound", "\"" + str2 + "\"");
/*     */       } catch (SecurityException localSecurityException) {
/* 487 */         error("agent.err.agentclass.access.denied");
/*     */       } catch (Exception localException) {
/* 489 */         Object localObject3 = localException.getCause() == null ? localException.getMessage() : localException.getCause().getMessage();
/*     */ 
/* 492 */         error("agent.err.agentclass.failed", (String)localObject3);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void error(String paramString) {
/* 498 */     String str = getText(paramString);
/* 499 */     System.err.print(getText("agent.err.error") + ": " + str);
/* 500 */     throw new RuntimeException(str);
/*     */   }
/*     */ 
/*     */   public static void error(String paramString, String[] paramArrayOfString) {
/* 504 */     if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
/* 505 */       error(paramString);
/*     */     } else {
/* 507 */       StringBuffer localStringBuffer = new StringBuffer(paramArrayOfString[0]);
/* 508 */       for (int i = 1; i < paramArrayOfString.length; i++) {
/* 509 */         localStringBuffer.append(" " + paramArrayOfString[i]);
/*     */       }
/* 511 */       error(paramString, localStringBuffer.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void error(String paramString1, String paramString2)
/*     */   {
/* 517 */     String str = getText(paramString1);
/* 518 */     System.err.print(getText("agent.err.error") + ": " + str);
/* 519 */     System.err.println(": " + paramString2);
/* 520 */     throw new RuntimeException(str);
/*     */   }
/*     */ 
/*     */   public static void error(Exception paramException) {
/* 524 */     paramException.printStackTrace();
/* 525 */     System.err.println(getText("agent.err.exception") + ": " + paramException.toString());
/* 526 */     throw new RuntimeException(paramException);
/*     */   }
/*     */ 
/*     */   public static void warning(String paramString1, String paramString2) {
/* 530 */     System.err.print(getText("agent.err.warning") + ": " + getText(paramString1));
/* 531 */     System.err.println(": " + paramString2);
/*     */   }
/*     */ 
/*     */   private static void initResource() {
/*     */     try {
/* 536 */       messageRB = ResourceBundle.getBundle("sun.management.resources.agent");
/*     */     }
/*     */     catch (MissingResourceException localMissingResourceException) {
/* 539 */       throw new Error("Fatal: Resource for management agent is missing");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString) {
/* 544 */     if (messageRB == null)
/* 545 */       initResource();
/*     */     try
/*     */     {
/* 548 */       return messageRB.getString(paramString); } catch (MissingResourceException localMissingResourceException) {
/*     */     }
/* 550 */     return "Missing management agent resource bundle: key = \"" + paramString + "\"";
/*     */   }
/*     */ 
/*     */   public static String getText(String paramString, String[] paramArrayOfString)
/*     */   {
/* 555 */     if (messageRB == null) {
/* 556 */       initResource();
/*     */     }
/* 558 */     String str = messageRB.getString(paramString);
/* 559 */     if (str == null) {
/* 560 */       str = "missing resource key: key = \"" + paramString + "\", " + "arguments = \"{0}\", \"{1}\", \"{2}\"";
/*     */     }
/*     */ 
/* 563 */     return MessageFormat.format(str, (Object[])paramArrayOfString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.Agent
 * JD-Core Version:    0.6.2
 */