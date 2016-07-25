/*     */ package sun.management.jmxremote;
/*     */ 
/*     */ import com.sun.jmx.remote.internal.RMIExporter;
/*     */ import com.sun.jmx.remote.security.JMXPluggableAuthenticator;
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.net.InetAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.RemoteObject;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import java.security.KeyStore;
/*     */ import java.security.Principal;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.remote.JMXAuthenticator;
/*     */ import javax.management.remote.JMXConnectorServer;
/*     */ import javax.management.remote.JMXConnectorServerFactory;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.rmi.ssl.SslRMIClientSocketFactory;
/*     */ import javax.rmi.ssl.SslRMIServerSocketFactory;
/*     */ import javax.security.auth.Subject;
/*     */ import sun.management.Agent;
/*     */ import sun.management.AgentConfigurationError;
/*     */ import sun.management.ConnectorAddressLink;
/*     */ import sun.management.FileSystem;
/*     */ import sun.rmi.server.UnicastRef;
/*     */ import sun.rmi.server.UnicastServerRef;
/*     */ import sun.rmi.server.UnicastServerRef2;
/*     */ import sun.rmi.transport.LiveRef;
/*     */ 
/*     */ public final class ConnectorBootstrap
/*     */ {
/* 268 */   private static Registry registry = null;
/*     */ 
/* 823 */   private static final ClassLogger log = new ClassLogger(ConnectorBootstrap.class.getPackage().getName(), "ConnectorBootstrap");
/*     */ 
/*     */   public static void unexportRegistry()
/*     */   {
/*     */     try
/*     */     {
/* 273 */       if (registry != null) {
/* 274 */         UnicastRemoteObject.unexportObject(registry, true);
/* 275 */         registry = null;
/*     */       }
/*     */     }
/*     */     catch (NoSuchObjectException localNoSuchObjectException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static synchronized JMXConnectorServer initialize()
/*     */   {
/* 296 */     Properties localProperties = Agent.loadManagementProperties();
/* 297 */     if (localProperties == null) {
/* 298 */       return null;
/*     */     }
/*     */ 
/* 301 */     String str = localProperties.getProperty("com.sun.management.jmxremote.port");
/* 302 */     return startRemoteConnectorServer(str, localProperties);
/*     */   }
/*     */ 
/*     */   public static synchronized JMXConnectorServer initialize(String paramString, Properties paramProperties)
/*     */   {
/* 312 */     return startRemoteConnectorServer(paramString, paramProperties);
/*     */   }
/*     */ 
/*     */   public static synchronized JMXConnectorServer startRemoteConnectorServer(String paramString, Properties paramProperties)
/*     */   {
/*     */     int i;
/*     */     try
/*     */     {
/* 324 */       i = Integer.parseInt(paramString);
/*     */     } catch (NumberFormatException localNumberFormatException1) {
/* 326 */       throw new AgentConfigurationError("agent.err.invalid.jmxremote.port", localNumberFormatException1, new String[] { paramString });
/*     */     }
/* 328 */     if (i < 0) {
/* 329 */       throw new AgentConfigurationError("agent.err.invalid.jmxremote.port", new String[] { paramString });
/*     */     }
/*     */ 
/* 335 */     int j = 0;
/* 336 */     String str1 = paramProperties.getProperty("com.sun.management.jmxremote.rmi.port");
/*     */     try {
/* 338 */       if (str1 != null)
/* 339 */         j = Integer.parseInt(str1);
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException2) {
/* 342 */       throw new AgentConfigurationError("agent.err.invalid.jmxremote.rmi.port", localNumberFormatException2, new String[] { str1 });
/*     */     }
/* 344 */     if (j < 0) {
/* 345 */       throw new AgentConfigurationError("agent.err.invalid.jmxremote.rmi.port", new String[] { str1 });
/*     */     }
/*     */ 
/* 349 */     String str2 = paramProperties.getProperty("com.sun.management.jmxremote.authenticate", "true");
/*     */ 
/* 352 */     boolean bool1 = Boolean.valueOf(str2).booleanValue();
/*     */ 
/* 356 */     String str3 = paramProperties.getProperty("com.sun.management.jmxremote.ssl", "true");
/*     */ 
/* 359 */     boolean bool2 = Boolean.valueOf(str3).booleanValue();
/*     */ 
/* 363 */     String str4 = paramProperties.getProperty("com.sun.management.jmxremote.registry.ssl", "false");
/*     */ 
/* 366 */     boolean bool3 = Boolean.valueOf(str4).booleanValue();
/*     */ 
/* 369 */     String str5 = paramProperties.getProperty("com.sun.management.jmxremote.ssl.enabled.cipher.suites");
/*     */ 
/* 371 */     String[] arrayOfString1 = null;
/* 372 */     if (str5 != null) {
/* 373 */       localObject1 = new StringTokenizer(str5, ",");
/* 374 */       int k = ((StringTokenizer)localObject1).countTokens();
/* 375 */       arrayOfString1 = new String[k];
/* 376 */       for (int m = 0; m < k; m++) {
/* 377 */         arrayOfString1[m] = ((StringTokenizer)localObject1).nextToken();
/*     */       }
/*     */     }
/*     */ 
/* 381 */     Object localObject1 = paramProperties.getProperty("com.sun.management.jmxremote.ssl.enabled.protocols");
/*     */ 
/* 383 */     String[] arrayOfString2 = null;
/* 384 */     if (localObject1 != null) {
/* 385 */       localObject2 = new StringTokenizer((String)localObject1, ",");
/* 386 */       int n = ((StringTokenizer)localObject2).countTokens();
/* 387 */       arrayOfString2 = new String[n];
/* 388 */       for (int i1 = 0; i1 < n; i1++) {
/* 389 */         arrayOfString2[i1] = ((StringTokenizer)localObject2).nextToken();
/*     */       }
/*     */     }
/*     */ 
/* 393 */     Object localObject2 = paramProperties.getProperty("com.sun.management.jmxremote.ssl.need.client.auth", "false");
/*     */ 
/* 396 */     boolean bool4 = Boolean.valueOf((String)localObject2).booleanValue();
/*     */ 
/* 400 */     String str6 = paramProperties.getProperty("com.sun.management.jmxremote.ssl.config.file");
/*     */ 
/* 403 */     String str7 = null;
/* 404 */     String str8 = null;
/* 405 */     String str9 = null;
/*     */ 
/* 408 */     if (bool1)
/*     */     {
/* 411 */       str7 = paramProperties.getProperty("com.sun.management.jmxremote.login.config");
/*     */ 
/* 414 */       if (str7 == null)
/*     */       {
/* 416 */         str8 = paramProperties.getProperty("com.sun.management.jmxremote.password.file", getDefaultFileName("jmxremote.password"));
/*     */ 
/* 419 */         checkPasswordFile(str8);
/*     */       }
/*     */ 
/* 423 */       str9 = paramProperties.getProperty("com.sun.management.jmxremote.access.file", getDefaultFileName("jmxremote.access"));
/*     */ 
/* 425 */       checkAccessFile(str9);
/*     */     }
/*     */ 
/* 428 */     if (log.debugOn()) {
/* 429 */       log.debug("startRemoteConnectorServer", Agent.getText("jmxremote.ConnectorBootstrap.starting") + "\n\t" + "com.sun.management.jmxremote.port" + "=" + i + "\n\t" + "com.sun.management.jmxremote.rmi.port" + "=" + j + "\n\t" + "com.sun.management.jmxremote.ssl" + "=" + bool2 + "\n\t" + "com.sun.management.jmxremote.registry.ssl" + "=" + bool3 + "\n\t" + "com.sun.management.jmxremote.ssl.config.file" + "=" + str6 + "\n\t" + "com.sun.management.jmxremote.ssl.enabled.cipher.suites" + "=" + str5 + "\n\t" + "com.sun.management.jmxremote.ssl.enabled.protocols" + "=" + (String)localObject1 + "\n\t" + "com.sun.management.jmxremote.ssl.need.client.auth" + "=" + bool4 + "\n\t" + "com.sun.management.jmxremote.authenticate" + "=" + bool1 + (bool1 ? "\n\tcom.sun.management.jmxremote.login.config=" + str7 : str7 == null ? "\n\tcom.sun.management.jmxremote.password.file=" + str8 : new StringBuilder().append("\n\t").append(Agent.getText("jmxremote.ConnectorBootstrap.noAuthentication")).toString()) + (bool1 ? "\n\tcom.sun.management.jmxremote.access.file=" + str9 : "") + "");
/*     */     }
/*     */ 
/* 453 */     MBeanServer localMBeanServer = ManagementFactory.getPlatformMBeanServer();
/* 454 */     JMXConnectorServer localJMXConnectorServer = null;
/* 455 */     JMXServiceURL localJMXServiceURL = null;
/*     */     try {
/* 457 */       JMXConnectorServerData localJMXConnectorServerData = exportMBeanServer(localMBeanServer, i, j, bool2, bool3, str6, arrayOfString1, arrayOfString2, bool4, bool1, str7, str8, str9);
/*     */ 
/* 463 */       localJMXConnectorServer = localJMXConnectorServerData.jmxConnectorServer;
/* 464 */       localJMXServiceURL = localJMXConnectorServerData.jmxRemoteURL;
/* 465 */       log.config("startRemoteConnectorServer", Agent.getText("jmxremote.ConnectorBootstrap.ready", new String[] { localJMXServiceURL.toString() }));
/*     */     }
/*     */     catch (Exception localException1)
/*     */     {
/* 469 */       throw new AgentConfigurationError("agent.err.exception", localException1, new String[] { localException1.toString() });
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 474 */       HashMap localHashMap = new HashMap();
/* 475 */       localHashMap.put("remoteAddress", localJMXServiceURL.toString());
/* 476 */       localHashMap.put("authenticate", str2);
/* 477 */       localHashMap.put("ssl", str3);
/* 478 */       localHashMap.put("sslRegistry", str4);
/* 479 */       localHashMap.put("sslNeedClientAuth", localObject2);
/* 480 */       ConnectorAddressLink.exportRemote(localHashMap);
/*     */     }
/*     */     catch (Exception localException2)
/*     */     {
/* 485 */       log.debug("startRemoteConnectorServer", localException2);
/*     */     }
/* 487 */     return localJMXConnectorServer;
/*     */   }
/*     */ 
/*     */   public static JMXConnectorServer startLocalConnectorServer()
/*     */   {
/* 497 */     System.setProperty("java.rmi.server.randomIDs", "true");
/*     */ 
/* 500 */     HashMap localHashMap = new HashMap();
/* 501 */     localHashMap.put("com.sun.jmx.remote.rmi.exporter", new PermanentExporter(null));
/*     */ 
/* 505 */     String str1 = "localhost";
/* 506 */     InetAddress localInetAddress = null;
/*     */     try {
/* 508 */       localInetAddress = InetAddress.getByName(str1);
/* 509 */       str1 = localInetAddress.getHostAddress();
/*     */     }
/*     */     catch (UnknownHostException localUnknownHostException)
/*     */     {
/*     */     }
/*     */ 
/* 515 */     if ((localInetAddress == null) || (!localInetAddress.isLoopbackAddress())) {
/* 516 */       str1 = "127.0.0.1";
/*     */     }
/*     */ 
/* 519 */     MBeanServer localMBeanServer = ManagementFactory.getPlatformMBeanServer();
/*     */     try {
/* 521 */       JMXServiceURL localJMXServiceURL = new JMXServiceURL("rmi", str1, 0);
/*     */ 
/* 523 */       Properties localProperties = Agent.getManagementProperties();
/* 524 */       if (localProperties == null) {
/* 525 */         localProperties = new Properties();
/*     */       }
/* 527 */       String str2 = localProperties.getProperty("com.sun.management.jmxremote.local.only", "true");
/*     */ 
/* 529 */       boolean bool = Boolean.valueOf(str2).booleanValue();
/* 530 */       if (bool) {
/* 531 */         localHashMap.put("jmx.remote.rmi.server.socket.factory", new LocalRMIServerSocketFactory());
/*     */       }
/*     */ 
/* 534 */       JMXConnectorServer localJMXConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(localJMXServiceURL, localHashMap, localMBeanServer);
/*     */ 
/* 536 */       localJMXConnectorServer.start();
/* 537 */       return localJMXConnectorServer;
/*     */     } catch (Exception localException) {
/* 539 */       throw new AgentConfigurationError("agent.err.exception", localException, new String[] { localException.toString() });
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkPasswordFile(String paramString) {
/* 544 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 545 */       throw new AgentConfigurationError("agent.err.password.file.notset");
/*     */     }
/* 547 */     File localFile = new File(paramString);
/* 548 */     if (!localFile.exists()) {
/* 549 */       throw new AgentConfigurationError("agent.err.password.file.notfound", new String[] { paramString });
/*     */     }
/*     */ 
/* 552 */     if (!localFile.canRead()) {
/* 553 */       throw new AgentConfigurationError("agent.err.password.file.not.readable", new String[] { paramString });
/*     */     }
/*     */ 
/* 556 */     FileSystem localFileSystem = FileSystem.open();
/*     */     try {
/* 558 */       if ((localFileSystem.supportsFileSecurity(localFile)) && 
/* 559 */         (!localFileSystem.isAccessUserOnly(localFile))) {
/* 560 */         String str = Agent.getText("jmxremote.ConnectorBootstrap.password.readonly", new String[] { paramString });
/*     */ 
/* 562 */         log.config("startRemoteConnectorServer", str);
/* 563 */         throw new AgentConfigurationError("agent.err.password.file.access.notrestricted", new String[] { paramString });
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 568 */       throw new AgentConfigurationError("agent.err.password.file.read.failed", localIOException, new String[] { paramString });
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void checkAccessFile(String paramString)
/*     */   {
/* 574 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 575 */       throw new AgentConfigurationError("agent.err.access.file.notset");
/*     */     }
/* 577 */     File localFile = new File(paramString);
/* 578 */     if (!localFile.exists()) {
/* 579 */       throw new AgentConfigurationError("agent.err.access.file.notfound", new String[] { paramString });
/*     */     }
/*     */ 
/* 582 */     if (!localFile.canRead())
/* 583 */       throw new AgentConfigurationError("agent.err.access.file.not.readable", new String[] { paramString });
/*     */   }
/*     */ 
/*     */   private static void checkRestrictedFile(String paramString)
/*     */   {
/* 588 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 589 */       throw new AgentConfigurationError("agent.err.file.not.set");
/*     */     }
/* 591 */     File localFile = new File(paramString);
/* 592 */     if (!localFile.exists()) {
/* 593 */       throw new AgentConfigurationError("agent.err.file.not.found", new String[] { paramString });
/*     */     }
/* 595 */     if (!localFile.canRead()) {
/* 596 */       throw new AgentConfigurationError("agent.err.file.not.readable", new String[] { paramString });
/*     */     }
/* 598 */     FileSystem localFileSystem = FileSystem.open();
/*     */     try {
/* 600 */       if ((localFileSystem.supportsFileSecurity(localFile)) && 
/* 601 */         (!localFileSystem.isAccessUserOnly(localFile))) {
/* 602 */         String str = Agent.getText("jmxremote.ConnectorBootstrap.file.readonly", new String[] { paramString });
/*     */ 
/* 605 */         log.config("startRemoteConnectorServer", str);
/* 606 */         throw new AgentConfigurationError("agent.err.file.access.not.restricted", new String[] { paramString });
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 611 */       throw new AgentConfigurationError("agent.err.file.read.failed", localIOException, new String[] { paramString });
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getDefaultFileName(String paramString)
/*     */   {
/* 622 */     String str = File.separator;
/* 623 */     return System.getProperty("java.home") + str + "lib" + str + "management" + str + paramString;
/*     */   }
/*     */ 
/*     */   private static SslRMIServerSocketFactory createSslRMIServerSocketFactory(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean)
/*     */   {
/* 633 */     if (paramString == null) {
/* 634 */       return new SslRMIServerSocketFactory(paramArrayOfString1, paramArrayOfString2, paramBoolean);
/*     */     }
/*     */ 
/* 639 */     checkRestrictedFile(paramString);
/*     */     try
/*     */     {
/* 642 */       Properties localProperties = new Properties();
/* 643 */       FileInputStream localFileInputStream = new FileInputStream(paramString);
/*     */       try {
/* 645 */         localObject1 = new BufferedInputStream(localFileInputStream);
/* 646 */         localProperties.load((InputStream)localObject1);
/*     */       } finally {
/* 648 */         localFileInputStream.close();
/*     */       }
/* 650 */       Object localObject1 = localProperties.getProperty("javax.net.ssl.keyStore");
/*     */ 
/* 652 */       String str1 = localProperties.getProperty("javax.net.ssl.keyStorePassword", "");
/*     */ 
/* 654 */       String str2 = localProperties.getProperty("javax.net.ssl.trustStore");
/*     */ 
/* 656 */       String str3 = localProperties.getProperty("javax.net.ssl.trustStorePassword", "");
/*     */ 
/* 659 */       char[] arrayOfChar1 = null;
/* 660 */       if (str1.length() != 0) {
/* 661 */         arrayOfChar1 = str1.toCharArray();
/*     */       }
/*     */ 
/* 664 */       char[] arrayOfChar2 = null;
/* 665 */       if (str3.length() != 0) {
/* 666 */         arrayOfChar2 = str3.toCharArray();
/*     */       }
/*     */ 
/* 669 */       KeyStore localKeyStore1 = null;
/* 670 */       if (localObject1 != null) {
/* 671 */         localKeyStore1 = KeyStore.getInstance(KeyStore.getDefaultType());
/* 672 */         localObject3 = new FileInputStream((String)localObject1);
/*     */         try {
/* 674 */           localKeyStore1.load((InputStream)localObject3, arrayOfChar1);
/*     */         } finally {
/* 676 */           ((FileInputStream)localObject3).close();
/*     */         }
/*     */       }
/* 679 */       Object localObject3 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
/*     */ 
/* 681 */       ((KeyManagerFactory)localObject3).init(localKeyStore1, arrayOfChar1);
/*     */ 
/* 683 */       KeyStore localKeyStore2 = null;
/* 684 */       if (str2 != null) {
/* 685 */         localKeyStore2 = KeyStore.getInstance(KeyStore.getDefaultType());
/* 686 */         localObject5 = new FileInputStream(str2);
/*     */         try {
/* 688 */           localKeyStore2.load((InputStream)localObject5, arrayOfChar2);
/*     */         } finally {
/* 690 */           ((FileInputStream)localObject5).close();
/*     */         }
/*     */       }
/* 693 */       Object localObject5 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */ 
/* 695 */       ((TrustManagerFactory)localObject5).init(localKeyStore2);
/*     */ 
/* 697 */       SSLContext localSSLContext = SSLContext.getInstance("SSL");
/* 698 */       localSSLContext.init(((KeyManagerFactory)localObject3).getKeyManagers(), ((TrustManagerFactory)localObject5).getTrustManagers(), null);
/*     */ 
/* 700 */       return new SslRMIServerSocketFactory(localSSLContext, paramArrayOfString1, paramArrayOfString2, paramBoolean);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 706 */       throw new AgentConfigurationError("agent.err.exception", localException, new String[] { localException.toString() });
/*     */     }
/*     */   }
/*     */ 
/*     */   private static JMXConnectorServerData exportMBeanServer(MBeanServer paramMBeanServer, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, String paramString1, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean3, boolean paramBoolean4, String paramString2, String paramString3, String paramString4)
/*     */     throws IOException, MalformedURLException
/*     */   {
/* 730 */     System.setProperty("java.rmi.server.randomIDs", "true");
/*     */ 
/* 732 */     JMXServiceURL localJMXServiceURL1 = new JMXServiceURL("rmi", null, paramInt2);
/*     */ 
/* 734 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 736 */     PermanentExporter localPermanentExporter = new PermanentExporter(null);
/*     */ 
/* 738 */     localHashMap.put("com.sun.jmx.remote.rmi.exporter", localPermanentExporter);
/*     */ 
/* 740 */     if (paramBoolean4) {
/* 741 */       if (paramString2 != null) {
/* 742 */         localHashMap.put("jmx.remote.x.login.config", paramString2);
/*     */       }
/* 744 */       if (paramString3 != null) {
/* 745 */         localHashMap.put("jmx.remote.x.password.file", paramString3);
/*     */       }
/*     */ 
/* 748 */       localHashMap.put("jmx.remote.x.access.file", paramString4);
/*     */ 
/* 750 */       if ((localHashMap.get("jmx.remote.x.password.file") != null) || (localHashMap.get("jmx.remote.x.login.config") != null))
/*     */       {
/* 752 */         localHashMap.put("jmx.remote.authenticator", new AccessFileCheckerAuthenticator(localHashMap));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 757 */     SslRMIClientSocketFactory localSslRMIClientSocketFactory = null;
/* 758 */     SslRMIServerSocketFactory localSslRMIServerSocketFactory = null;
/*     */ 
/* 760 */     if ((paramBoolean1) || (paramBoolean2)) {
/* 761 */       localSslRMIClientSocketFactory = new SslRMIClientSocketFactory();
/* 762 */       localSslRMIServerSocketFactory = createSslRMIServerSocketFactory(paramString1, paramArrayOfString1, paramArrayOfString2, paramBoolean3);
/*     */     }
/*     */ 
/* 767 */     if (paramBoolean1) {
/* 768 */       localHashMap.put("jmx.remote.rmi.client.socket.factory", localSslRMIClientSocketFactory);
/*     */ 
/* 770 */       localHashMap.put("jmx.remote.rmi.server.socket.factory", localSslRMIServerSocketFactory);
/*     */     }
/*     */ 
/* 774 */     JMXConnectorServer localJMXConnectorServer = null;
/*     */     try {
/* 776 */       localJMXConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(localJMXServiceURL1, localHashMap, paramMBeanServer);
/*     */ 
/* 778 */       localJMXConnectorServer.start();
/*     */     } catch (IOException localIOException) {
/* 780 */       if (localJMXConnectorServer == null) {
/* 781 */         throw new AgentConfigurationError("agent.err.connector.server.io.error", localIOException, new String[] { localJMXServiceURL1.toString() });
/*     */       }
/*     */ 
/* 784 */       throw new AgentConfigurationError("agent.err.connector.server.io.error", localIOException, new String[] { localJMXConnectorServer.getAddress().toString() });
/*     */     }
/*     */ 
/* 789 */     if (paramBoolean2) {
/* 790 */       registry = new SingleEntryRegistry(paramInt1, localSslRMIClientSocketFactory, localSslRMIServerSocketFactory, "jmxrmi", localPermanentExporter.firstExported);
/*     */     }
/*     */     else
/*     */     {
/* 794 */       registry = new SingleEntryRegistry(paramInt1, "jmxrmi", localPermanentExporter.firstExported);
/*     */     }
/*     */ 
/* 800 */     int i = ((UnicastRef)((RemoteObject)registry).getRef()).getLiveRef().getPort();
/*     */ 
/* 802 */     String str = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", new Object[] { localJMXServiceURL1.getHost(), Integer.valueOf(i) });
/*     */ 
/* 804 */     JMXServiceURL localJMXServiceURL2 = new JMXServiceURL(str);
/*     */ 
/* 814 */     return new JMXConnectorServerData(localJMXConnectorServer, localJMXServiceURL2);
/*     */   }
/*     */ 
/*     */   private static class AccessFileCheckerAuthenticator
/*     */     implements JMXAuthenticator
/*     */   {
/*     */     private final Map<String, Object> environment;
/*     */     private final Properties properties;
/*     */     private final String accessFile;
/*     */ 
/*     */     public AccessFileCheckerAuthenticator(Map<String, Object> paramMap)
/*     */       throws IOException
/*     */     {
/* 211 */       this.environment = paramMap;
/* 212 */       this.accessFile = ((String)paramMap.get("jmx.remote.x.access.file"));
/* 213 */       this.properties = propertiesFromFile(this.accessFile);
/*     */     }
/*     */ 
/*     */     public Subject authenticate(Object paramObject) {
/* 217 */       JMXPluggableAuthenticator localJMXPluggableAuthenticator = new JMXPluggableAuthenticator(this.environment);
/*     */ 
/* 219 */       Subject localSubject = localJMXPluggableAuthenticator.authenticate(paramObject);
/* 220 */       checkAccessFileEntries(localSubject);
/* 221 */       return localSubject;
/*     */     }
/*     */ 
/*     */     private void checkAccessFileEntries(Subject paramSubject) {
/* 225 */       if (paramSubject == null) {
/* 226 */         throw new SecurityException("Access denied! No matching entries found in the access file [" + this.accessFile + "] as the " + "authenticated Subject is null");
/*     */       }
/*     */ 
/* 231 */       Set localSet = paramSubject.getPrincipals();
/* 232 */       for (Object localObject1 = localSet.iterator(); ((Iterator)localObject1).hasNext(); ) {
/* 233 */         localObject2 = (Principal)((Iterator)localObject1).next();
/* 234 */         if (this.properties.containsKey(((Principal)localObject2).getName())) {
/* 235 */           return;
/*     */         }
/*     */       }
/* 238 */       localObject1 = new HashSet();
/* 239 */       for (Object localObject2 = localSet.iterator(); ((Iterator)localObject2).hasNext(); ) {
/* 240 */         Principal localPrincipal = (Principal)((Iterator)localObject2).next();
/* 241 */         ((Set)localObject1).add(localPrincipal.getName());
/*     */       }
/* 243 */       throw new SecurityException("Access denied! No entries found in the access file [" + this.accessFile + "] for any of the authenticated identities " + localObject1);
/*     */     }
/*     */ 
/*     */     private static Properties propertiesFromFile(String paramString)
/*     */       throws IOException
/*     */     {
/* 251 */       Properties localProperties = new Properties();
/* 252 */       if (paramString == null) {
/* 253 */         return localProperties;
/*     */       }
/* 255 */       FileInputStream localFileInputStream = new FileInputStream(paramString);
/* 256 */       localProperties.load(localFileInputStream);
/* 257 */       localFileInputStream.close();
/* 258 */       return localProperties;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface DefaultValues
/*     */   {
/*     */     public static final String PORT = "0";
/*     */     public static final String CONFIG_FILE_NAME = "management.properties";
/*     */     public static final String USE_SSL = "true";
/*     */     public static final String USE_LOCAL_ONLY = "true";
/*     */     public static final String USE_REGISTRY_SSL = "false";
/*     */     public static final String USE_AUTHENTICATION = "true";
/*     */     public static final String PASSWORD_FILE_NAME = "jmxremote.password";
/*     */     public static final String ACCESS_FILE_NAME = "jmxremote.access";
/*     */     public static final String SSL_NEED_CLIENT_AUTH = "false";
/*     */   }
/*     */ 
/*     */   private static class JMXConnectorServerData
/*     */   {
/*     */     JMXConnectorServer jmxConnectorServer;
/*     */     JMXServiceURL jmxRemoteURL;
/*     */ 
/*     */     public JMXConnectorServerData(JMXConnectorServer paramJMXConnectorServer, JMXServiceURL paramJMXServiceURL)
/*     */     {
/* 146 */       this.jmxConnectorServer = paramJMXConnectorServer;
/* 147 */       this.jmxRemoteURL = paramJMXServiceURL;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class PermanentExporter
/*     */     implements RMIExporter
/*     */   {
/*     */     Remote firstExported;
/*     */ 
/*     */     public Remote exportObject(Remote paramRemote, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */       throws RemoteException
/*     */     {
/* 179 */       synchronized (this) {
/* 180 */         if (this.firstExported == null) {
/* 181 */           this.firstExported = paramRemote;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 186 */       if ((paramRMIClientSocketFactory == null) && (paramRMIServerSocketFactory == null))
/* 187 */         ??? = new UnicastServerRef(paramInt);
/*     */       else {
/* 189 */         ??? = new UnicastServerRef2(paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*     */       }
/* 191 */       return ((UnicastServerRef)???).exportObject(paramRemote, null, true);
/*     */     }
/*     */ 
/*     */     public boolean unexportObject(Remote paramRemote, boolean paramBoolean)
/*     */       throws NoSuchObjectException
/*     */     {
/* 197 */       return UnicastRemoteObject.unexportObject(paramRemote, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface PropertyNames
/*     */   {
/*     */     public static final String PORT = "com.sun.management.jmxremote.port";
/*     */     public static final String RMI_PORT = "com.sun.management.jmxremote.rmi.port";
/*     */     public static final String CONFIG_FILE_NAME = "com.sun.management.config.file";
/*     */     public static final String USE_LOCAL_ONLY = "com.sun.management.jmxremote.local.only";
/*     */     public static final String USE_SSL = "com.sun.management.jmxremote.ssl";
/*     */     public static final String USE_REGISTRY_SSL = "com.sun.management.jmxremote.registry.ssl";
/*     */     public static final String USE_AUTHENTICATION = "com.sun.management.jmxremote.authenticate";
/*     */     public static final String PASSWORD_FILE_NAME = "com.sun.management.jmxremote.password.file";
/*     */     public static final String ACCESS_FILE_NAME = "com.sun.management.jmxremote.access.file";
/*     */     public static final String LOGIN_CONFIG_NAME = "com.sun.management.jmxremote.login.config";
/*     */     public static final String SSL_ENABLED_CIPHER_SUITES = "com.sun.management.jmxremote.ssl.enabled.cipher.suites";
/*     */     public static final String SSL_ENABLED_PROTOCOLS = "com.sun.management.jmxremote.ssl.enabled.protocols";
/*     */     public static final String SSL_NEED_CLIENT_AUTH = "com.sun.management.jmxremote.ssl.need.client.auth";
/*     */     public static final String SSL_CONFIG_FILE_NAME = "com.sun.management.jmxremote.ssl.config.file";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jmxremote.ConnectorBootstrap
 * JD-Core Version:    0.6.2
 */