/*     */ package javax.management.remote.rmi;
/*     */ 
/*     */ import com.sun.jmx.remote.internal.IIOPHelper;
/*     */ import com.sun.jmx.remote.security.MBeanServerFileAccessController;
/*     */ import com.sun.jmx.remote.util.ClassLogger;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.remote.JMXConnector;
/*     */ import javax.management.remote.JMXConnectorServer;
/*     */ import javax.management.remote.JMXServiceURL;
/*     */ import javax.management.remote.MBeanServerForwarder;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public class RMIConnectorServer extends JMXConnectorServer
/*     */ {
/*     */   public static final String JNDI_REBIND_ATTRIBUTE = "jmx.remote.jndi.rebind";
/*     */   public static final String RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE = "jmx.remote.rmi.client.socket.factory";
/*     */   public static final String RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE = "jmx.remote.rmi.server.socket.factory";
/* 812 */   private static final char[] intToAlpha = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/* 834 */   private static ClassLogger logger = new ClassLogger("javax.management.remote.rmi", "RMIConnectorServer");
/*     */   private JMXServiceURL address;
/*     */   private RMIServerImpl rmiServerImpl;
/*     */   private final Map<String, ?> attributes;
/* 840 */   private ClassLoader defaultClassLoader = null;
/*     */   private String boundJndiUrl;
/*     */   private static final int CREATED = 0;
/*     */   private static final int STARTED = 1;
/*     */   private static final int STOPPED = 2;
/* 849 */   private int state = 0;
/* 850 */   private static final Set<RMIConnectorServer> openedServers = new HashSet();
/*     */ 
/*     */   public RMIConnectorServer(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 129 */     this(paramJMXServiceURL, paramMap, (MBeanServer)null);
/*     */   }
/*     */ 
/*     */   public RMIConnectorServer(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap, MBeanServer paramMBeanServer)
/*     */     throws IOException
/*     */   {
/* 164 */     this(paramJMXServiceURL, paramMap, (RMIServerImpl)null, paramMBeanServer);
/*     */   }
/*     */ 
/*     */   public RMIConnectorServer(JMXServiceURL paramJMXServiceURL, Map<String, ?> paramMap, RMIServerImpl paramRMIServerImpl, MBeanServer paramMBeanServer)
/*     */     throws IOException
/*     */   {
/* 205 */     super(paramMBeanServer);
/*     */ 
/* 207 */     if (paramJMXServiceURL == null) throw new IllegalArgumentException("Null JMXServiceURL");
/*     */ 
/* 209 */     if (paramRMIServerImpl == null) {
/* 210 */       String str1 = paramJMXServiceURL.getProtocol();
/* 211 */       if ((str1 == null) || ((!str1.equals("rmi")) && (!str1.equals("iiop")))) {
/* 212 */         str2 = "Invalid protocol type: " + str1;
/* 213 */         throw new MalformedURLException(str2);
/*     */       }
/* 215 */       String str2 = paramJMXServiceURL.getURLPath();
/* 216 */       if ((!str2.equals("")) && (!str2.equals("/")) && (!str2.startsWith("/jndi/")))
/*     */       {
/* 221 */         throw new MalformedURLException("URL path must be empty or start with /jndi/");
/*     */       }
/*     */     }
/*     */ 
/* 225 */     if (paramMap == null) {
/* 226 */       this.attributes = Collections.emptyMap();
/*     */     } else {
/* 228 */       EnvHelp.checkAttributes(paramMap);
/* 229 */       this.attributes = Collections.unmodifiableMap(paramMap);
/*     */     }
/*     */ 
/* 232 */     this.address = paramJMXServiceURL;
/* 233 */     this.rmiServerImpl = paramRMIServerImpl;
/*     */   }
/*     */ 
/*     */   public JMXConnector toJMXConnector(Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 262 */     if (!isActive()) throw new IllegalStateException("Connector is not active");
/*     */ 
/* 266 */     Object localObject = new HashMap(this.attributes == null ? Collections.emptyMap() : this.attributes);
/*     */ 
/* 270 */     if (paramMap != null) {
/* 271 */       EnvHelp.checkAttributes(paramMap);
/* 272 */       ((Map)localObject).putAll(paramMap);
/*     */     }
/*     */ 
/* 275 */     localObject = EnvHelp.filterAttributes((Map)localObject);
/*     */ 
/* 277 */     RMIServer localRMIServer = (RMIServer)this.rmiServerImpl.toStub();
/*     */ 
/* 279 */     return new RMIConnector(localRMIServer, (Map)localObject);
/*     */   }
/*     */ 
/*     */   public synchronized void start()
/*     */     throws IOException
/*     */   {
/* 343 */     boolean bool1 = logger.traceOn();
/*     */ 
/* 345 */     if (this.state == 1) {
/* 346 */       if (bool1) logger.trace("start", "already started");
/* 347 */       return;
/* 348 */     }if (this.state == 2) {
/* 349 */       if (bool1) logger.trace("start", "already stopped");
/* 350 */       throw new IOException("The server has been stopped.");
/*     */     }
/*     */ 
/* 353 */     if (getMBeanServer() == null)
/* 354 */       throw new IllegalStateException("This connector server is not attached to an MBean server");
/*     */     Object localObject1;
/* 360 */     if (this.attributes != null)
/*     */     {
/* 363 */       String str1 = (String)this.attributes.get("jmx.remote.x.access.file");
/*     */ 
/* 365 */       if (str1 != null)
/*     */       {
/*     */         try
/*     */         {
/* 371 */           localObject1 = new MBeanServerFileAccessController(str1);
/*     */         } catch (IOException localIOException) {
/* 373 */           throw ((IllegalArgumentException)EnvHelp.initCause(new IllegalArgumentException(localIOException.getMessage()), localIOException));
/*     */         }
/*     */ 
/* 378 */         setMBeanServerForwarder((MBeanServerForwarder)localObject1);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 383 */       if (bool1) logger.trace("start", "setting default class loader");
/* 384 */       this.defaultClassLoader = EnvHelp.resolveServerClassLoader(this.attributes, getMBeanServer());
/*     */     }
/*     */     catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 387 */       localObject1 = new IllegalArgumentException("ClassLoader not found: " + localInstanceNotFoundException);
/*     */ 
/* 389 */       throw ((IllegalArgumentException)EnvHelp.initCause((Throwable)localObject1, localInstanceNotFoundException));
/*     */     }
/*     */ 
/* 392 */     if (bool1) logger.trace("start", "setting RMIServer object");
/*     */     RMIServerImpl localRMIServerImpl;
/* 395 */     if (this.rmiServerImpl != null)
/* 396 */       localRMIServerImpl = this.rmiServerImpl;
/*     */     else {
/* 398 */       localRMIServerImpl = newServer();
/*     */     }
/* 400 */     localRMIServerImpl.setMBeanServer(getMBeanServer());
/* 401 */     localRMIServerImpl.setDefaultClassLoader(this.defaultClassLoader);
/* 402 */     localRMIServerImpl.setRMIConnectorServer(this);
/* 403 */     localRMIServerImpl.export();
/*     */     try
/*     */     {
/* 406 */       if (bool1) logger.trace("start", "getting RMIServer object to export");
/* 407 */       localObject1 = objectToBind(localRMIServerImpl, this.attributes);
/*     */ 
/* 409 */       if ((this.address != null) && (this.address.getURLPath().startsWith("/jndi/"))) {
/* 410 */         String str2 = this.address.getURLPath().substring(6);
/*     */ 
/* 412 */         if (bool1) {
/* 413 */           logger.trace("start", "Using external directory: " + str2);
/*     */         }
/* 415 */         String str3 = (String)this.attributes.get("jmx.remote.jndi.rebind");
/* 416 */         boolean bool2 = EnvHelp.computeBooleanFromString(str3);
/*     */ 
/* 418 */         if (bool1)
/* 419 */           logger.trace("start", "jmx.remote.jndi.rebind=" + bool2);
/*     */         try
/*     */         {
/* 422 */           if (bool1) logger.trace("start", "binding to " + str2);
/*     */ 
/* 424 */           Hashtable localHashtable = EnvHelp.mapToHashtable(this.attributes);
/*     */ 
/* 426 */           bind(str2, localHashtable, (RMIServer)localObject1, bool2);
/*     */ 
/* 428 */           this.boundJndiUrl = str2;
/*     */         }
/*     */         catch (NamingException localNamingException) {
/* 431 */           throw newIOException("Cannot bind to URL [" + str2 + "]: " + localNamingException, localNamingException);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 436 */         if (bool1) logger.trace("start", "Encoding URL");
/*     */ 
/* 438 */         encodeStubInAddress((RMIServer)localObject1, this.attributes);
/*     */ 
/* 440 */         if (bool1) logger.trace("start", "Encoded URL: " + this.address); 
/*     */       }
/*     */     }
/*     */     catch (Exception localException1) {
/*     */       try { localRMIServerImpl.close();
/*     */       } catch (Exception localException2)
/*     */       {
/*     */       }
/* 448 */       if ((localException1 instanceof RuntimeException))
/* 449 */         throw ((RuntimeException)localException1);
/* 450 */       if ((localException1 instanceof IOException)) {
/* 451 */         throw ((IOException)localException1);
/*     */       }
/* 453 */       throw newIOException("Got unexpected exception while starting the connector server: " + localException1, localException1);
/*     */     }
/*     */ 
/* 458 */     this.rmiServerImpl = localRMIServerImpl;
/*     */ 
/* 460 */     synchronized (openedServers) {
/* 461 */       openedServers.add(this);
/*     */     }
/*     */ 
/* 464 */     this.state = 1;
/*     */ 
/* 466 */     if (bool1) {
/* 467 */       logger.trace("start", "Connector Server Address = " + this.address);
/* 468 */       logger.trace("start", "started.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */     throws IOException
/*     */   {
/* 518 */     boolean bool = logger.traceOn();
/*     */ 
/* 520 */     synchronized (this) {
/* 521 */       if (this.state == 2) {
/* 522 */         if (bool) logger.trace("stop", "already stopped.");
/* 523 */         return;
/* 524 */       }if ((this.state == 0) && 
/* 525 */         (bool)) logger.trace("stop", "not started yet.");
/*     */ 
/* 528 */       if (bool) logger.trace("stop", "stopping.");
/* 529 */       this.state = 2;
/*     */     }
/*     */ 
/* 532 */     synchronized (openedServers) {
/* 533 */       openedServers.remove(this);
/*     */     }
/*     */ 
/* 536 */     ??? = null;
/*     */ 
/* 539 */     if (this.rmiServerImpl != null) {
/*     */       try {
/* 541 */         if (bool) logger.trace("stop", "closing RMI server.");
/* 542 */         this.rmiServerImpl.close();
/*     */       } catch (IOException localIOException) {
/* 544 */         if (bool) logger.trace("stop", "failed to close RMI server: " + localIOException);
/* 545 */         if (logger.debugOn()) logger.debug("stop", localIOException);
/* 546 */         ??? = localIOException;
/*     */       }
/*     */     }
/*     */ 
/* 550 */     if (this.boundJndiUrl != null) {
/*     */       try {
/* 552 */         if (bool) {
/* 553 */           logger.trace("stop", "unbind from external directory: " + this.boundJndiUrl);
/*     */         }
/*     */ 
/* 556 */         Hashtable localHashtable = EnvHelp.mapToHashtable(this.attributes);
/*     */ 
/* 558 */         InitialContext localInitialContext = new InitialContext(localHashtable);
/*     */ 
/* 561 */         localInitialContext.unbind(this.boundJndiUrl);
/*     */ 
/* 563 */         localInitialContext.close();
/*     */       } catch (NamingException localNamingException) {
/* 565 */         if (bool) logger.trace("stop", "failed to unbind RMI server: " + localNamingException);
/* 566 */         if (logger.debugOn()) logger.debug("stop", localNamingException);
/*     */ 
/* 568 */         if (??? == null) {
/* 569 */           ??? = newIOException("Cannot bind to URL: " + localNamingException, localNamingException);
/*     */         }
/*     */       }
/*     */     }
/* 573 */     if (??? != null) throw ((Throwable)???);
/*     */ 
/* 575 */     if (bool) logger.trace("stop", "stopped"); 
/*     */   }
/*     */ 
/*     */   public synchronized boolean isActive()
/*     */   {
/* 579 */     return this.state == 1;
/*     */   }
/*     */ 
/*     */   public JMXServiceURL getAddress() {
/* 583 */     if (!isActive())
/* 584 */       return null;
/* 585 */     return this.address;
/*     */   }
/*     */ 
/*     */   public Map<String, ?> getAttributes() {
/* 589 */     Map localMap = EnvHelp.filterAttributes(this.attributes);
/* 590 */     return Collections.unmodifiableMap(localMap);
/*     */   }
/*     */ 
/*     */   public synchronized void setMBeanServerForwarder(MBeanServerForwarder paramMBeanServerForwarder)
/*     */   {
/* 596 */     super.setMBeanServerForwarder(paramMBeanServerForwarder);
/* 597 */     if (this.rmiServerImpl != null)
/* 598 */       this.rmiServerImpl.setMBeanServer(getMBeanServer());
/*     */   }
/*     */ 
/*     */   protected void connectionOpened(String paramString1, String paramString2, Object paramObject)
/*     */   {
/* 608 */     super.connectionOpened(paramString1, paramString2, paramObject);
/*     */   }
/*     */ 
/*     */   protected void connectionClosed(String paramString1, String paramString2, Object paramObject)
/*     */   {
/* 614 */     super.connectionClosed(paramString1, paramString2, paramObject);
/*     */   }
/*     */ 
/*     */   protected void connectionFailed(String paramString1, String paramString2, Object paramObject)
/*     */   {
/* 620 */     super.connectionFailed(paramString1, paramString2, paramObject);
/*     */   }
/*     */ 
/*     */   void bind(String paramString, Hashtable<?, ?> paramHashtable, RMIServer paramRMIServer, boolean paramBoolean)
/*     */     throws NamingException, MalformedURLException
/*     */   {
/* 637 */     InitialContext localInitialContext = new InitialContext(paramHashtable);
/*     */ 
/* 640 */     if (paramBoolean)
/* 641 */       localInitialContext.rebind(paramString, paramRMIServer);
/*     */     else
/* 643 */       localInitialContext.bind(paramString, paramRMIServer);
/* 644 */     localInitialContext.close();
/*     */   }
/*     */ 
/*     */   RMIServerImpl newServer()
/*     */     throws IOException
/*     */   {
/* 651 */     boolean bool = isIiopURL(this.address, true);
/*     */     int i;
/* 653 */     if (this.address == null)
/* 654 */       i = 0;
/*     */     else
/* 656 */       i = this.address.getPort();
/* 657 */     if (bool) {
/* 658 */       return newIIOPServer(this.attributes);
/*     */     }
/* 660 */     return newJRMPServer(this.attributes, i);
/*     */   }
/*     */ 
/*     */   private void encodeStubInAddress(RMIServer paramRMIServer, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/*     */     String str1;
/*     */     String str2;
/*     */     int i;
/* 676 */     if (this.address == null) {
/* 677 */       if (IIOPHelper.isStub(paramRMIServer))
/* 678 */         str1 = "iiop";
/*     */       else
/* 680 */         str1 = "rmi";
/* 681 */       str2 = null;
/* 682 */       i = 0;
/*     */     } else {
/* 684 */       str1 = this.address.getProtocol();
/* 685 */       str2 = this.address.getHost().equals("") ? null : this.address.getHost();
/* 686 */       i = this.address.getPort();
/*     */     }
/*     */ 
/* 689 */     String str3 = encodeStub(paramRMIServer, paramMap);
/*     */ 
/* 691 */     this.address = new JMXServiceURL(str1, str2, i, str3);
/*     */   }
/*     */ 
/*     */   static boolean isIiopURL(JMXServiceURL paramJMXServiceURL, boolean paramBoolean) throws MalformedURLException
/*     */   {
/* 696 */     String str = paramJMXServiceURL.getProtocol();
/* 697 */     if (str.equals("rmi"))
/* 698 */       return false;
/* 699 */     if (str.equals("iiop"))
/* 700 */       return true;
/* 701 */     if (paramBoolean)
/*     */     {
/* 703 */       throw new MalformedURLException("URL must have protocol \"rmi\" or \"iiop\": \"" + str + "\"");
/*     */     }
/*     */ 
/* 707 */     return false;
/*     */   }
/*     */ 
/*     */   static String encodeStub(RMIServer paramRMIServer, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 715 */     if (IIOPHelper.isStub(paramRMIServer)) {
/* 716 */       return "/ior/" + encodeIIOPStub(paramRMIServer, paramMap);
/*     */     }
/* 718 */     return "/stub/" + encodeJRMPStub(paramRMIServer, paramMap);
/*     */   }
/*     */ 
/*     */   static String encodeJRMPStub(RMIServer paramRMIServer, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 724 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 725 */     ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
/* 726 */     localObjectOutputStream.writeObject(paramRMIServer);
/* 727 */     localObjectOutputStream.close();
/* 728 */     byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
/* 729 */     return byteArrayToBase64(arrayOfByte);
/*     */   }
/*     */ 
/*     */   static String encodeIIOPStub(RMIServer paramRMIServer, Map<String, ?> paramMap) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 736 */       Object localObject = IIOPHelper.getOrb(paramRMIServer);
/* 737 */       return IIOPHelper.objectToString(localObject, paramRMIServer);
/*     */     } catch (RuntimeException localRuntimeException) {
/* 739 */       throw newIOException(localRuntimeException.getMessage(), localRuntimeException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static RMIServer objectToBind(RMIServerImpl paramRMIServerImpl, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/* 750 */     return RMIConnector.connectStub((RMIServer)paramRMIServerImpl.toStub(), paramMap);
/*     */   }
/*     */ 
/*     */   private static RMIServerImpl newJRMPServer(Map<String, ?> paramMap, int paramInt)
/*     */     throws IOException
/*     */   {
/* 756 */     RMIClientSocketFactory localRMIClientSocketFactory = (RMIClientSocketFactory)paramMap.get("jmx.remote.rmi.client.socket.factory");
/*     */ 
/* 758 */     RMIServerSocketFactory localRMIServerSocketFactory = (RMIServerSocketFactory)paramMap.get("jmx.remote.rmi.server.socket.factory");
/*     */ 
/* 760 */     return new RMIJRMPServerImpl(paramInt, localRMIClientSocketFactory, localRMIServerSocketFactory, paramMap);
/*     */   }
/*     */ 
/*     */   private static RMIServerImpl newIIOPServer(Map<String, ?> paramMap) throws IOException
/*     */   {
/* 765 */     return new RMIIIOPServerImpl(paramMap);
/*     */   }
/*     */ 
/*     */   private static String byteArrayToBase64(byte[] paramArrayOfByte) {
/* 769 */     int i = paramArrayOfByte.length;
/* 770 */     int j = i / 3;
/* 771 */     int k = i - 3 * j;
/* 772 */     int m = 4 * ((i + 2) / 3);
/* 773 */     StringBuilder localStringBuilder = new StringBuilder(m);
/*     */ 
/* 776 */     int n = 0;
/*     */     int i2;
/* 777 */     for (int i1 = 0; i1 < j; i1++) {
/* 778 */       i2 = paramArrayOfByte[(n++)] & 0xFF;
/* 779 */       int i3 = paramArrayOfByte[(n++)] & 0xFF;
/* 780 */       int i4 = paramArrayOfByte[(n++)] & 0xFF;
/* 781 */       localStringBuilder.append(intToAlpha[(i2 >> 2)]);
/* 782 */       localStringBuilder.append(intToAlpha[(i2 << 4 & 0x3F | i3 >> 4)]);
/* 783 */       localStringBuilder.append(intToAlpha[(i3 << 2 & 0x3F | i4 >> 6)]);
/* 784 */       localStringBuilder.append(intToAlpha[(i4 & 0x3F)]);
/*     */     }
/*     */ 
/* 788 */     if (k != 0) {
/* 789 */       i1 = paramArrayOfByte[(n++)] & 0xFF;
/* 790 */       localStringBuilder.append(intToAlpha[(i1 >> 2)]);
/* 791 */       if (k == 1) {
/* 792 */         localStringBuilder.append(intToAlpha[(i1 << 4 & 0x3F)]);
/* 793 */         localStringBuilder.append("==");
/*     */       }
/*     */       else {
/* 796 */         i2 = paramArrayOfByte[(n++)] & 0xFF;
/* 797 */         localStringBuilder.append(intToAlpha[(i1 << 4 & 0x3F | i2 >> 4)]);
/* 798 */         localStringBuilder.append(intToAlpha[(i2 << 2 & 0x3F)]);
/* 799 */         localStringBuilder.append('=');
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 804 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static IOException newIOException(String paramString, Throwable paramThrowable)
/*     */   {
/* 826 */     IOException localIOException = new IOException(paramString);
/* 827 */     return (IOException)EnvHelp.initCause(localIOException, paramThrowable);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIConnectorServer
 * JD-Core Version:    0.6.2
 */