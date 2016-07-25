/*     */ package sun.rmi.transport.tcp;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.rmi.ConnectIOException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.RMISocketFactory;
/*     */ import java.security.AccessController;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.runtime.NewThreadAction;
/*     */ import sun.rmi.transport.Channel;
/*     */ import sun.rmi.transport.Endpoint;
/*     */ import sun.rmi.transport.Target;
/*     */ import sun.rmi.transport.Transport;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class TCPEndpoint
/*     */   implements Endpoint
/*     */ {
/*     */   private String host;
/*     */   private int port;
/*     */   private final RMIClientSocketFactory csf;
/*     */   private final RMIServerSocketFactory ssf;
/*  74 */   private int listenPort = -1;
/*     */ 
/*  76 */   private TCPTransport transport = null;
/*     */   private static String localHost;
/* 107 */   private static boolean localHostKnown = true;
/*     */ 
/* 150 */   private static final Map<TCPEndpoint, LinkedList<TCPEndpoint>> localEndpoints = new HashMap();
/*     */   private static final int FORMAT_HOST_PORT = 0;
/*     */   private static final int FORMAT_HOST_PORT_FACTORY = 1;
/*     */ 
/*     */   private static int getInt(String paramString, int paramInt)
/*     */   {
/*  85 */     return ((Integer)AccessController.doPrivileged(new GetIntegerAction(paramString, paramInt))).intValue();
/*     */   }
/*     */ 
/*     */   private static boolean getBoolean(String paramString)
/*     */   {
/*  90 */     return ((Boolean)AccessController.doPrivileged(new GetBooleanAction(paramString))).booleanValue();
/*     */   }
/*     */ 
/*     */   private static String getHostnameProperty()
/*     */   {
/*  97 */     return (String)AccessController.doPrivileged(new GetPropertyAction("java.rmi.server.hostname"));
/*     */   }
/*     */ 
/*     */   public TCPEndpoint(String paramString, int paramInt)
/*     */   {
/* 159 */     this(paramString, paramInt, null, null);
/*     */   }
/*     */ 
/*     */   public TCPEndpoint(String paramString, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */   {
/* 170 */     if (paramString == null)
/* 171 */       paramString = "";
/* 172 */     this.host = paramString;
/* 173 */     this.port = paramInt;
/* 174 */     this.csf = paramRMIClientSocketFactory;
/* 175 */     this.ssf = paramRMIServerSocketFactory;
/*     */   }
/*     */ 
/*     */   public static TCPEndpoint getLocalEndpoint(int paramInt)
/*     */   {
/* 184 */     return getLocalEndpoint(paramInt, null, null);
/*     */   }
/*     */ 
/*     */   public static TCPEndpoint getLocalEndpoint(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */   {
/* 196 */     TCPEndpoint localTCPEndpoint1 = null;
/*     */ 
/* 198 */     synchronized (localEndpoints) {
/* 199 */       TCPEndpoint localTCPEndpoint2 = new TCPEndpoint(null, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/* 200 */       LinkedList localLinkedList = (LinkedList)localEndpoints.get(localTCPEndpoint2);
/* 201 */       String str1 = resampleLocalHost();
/*     */ 
/* 203 */       if (localLinkedList == null)
/*     */       {
/* 207 */         localTCPEndpoint1 = new TCPEndpoint(str1, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/* 208 */         localLinkedList = new LinkedList();
/* 209 */         localLinkedList.add(localTCPEndpoint1);
/* 210 */         localTCPEndpoint1.listenPort = paramInt;
/* 211 */         localTCPEndpoint1.transport = new TCPTransport(localLinkedList);
/* 212 */         localEndpoints.put(localTCPEndpoint2, localLinkedList);
/*     */ 
/* 214 */         if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
/* 215 */           TCPTransport.tcpLog.log(Log.BRIEF, "created local endpoint for socket factory " + paramRMIServerSocketFactory + " on port " + paramInt);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 220 */         synchronized (localLinkedList) {
/* 221 */           localTCPEndpoint1 = (TCPEndpoint)localLinkedList.getLast();
/* 222 */           String str2 = localTCPEndpoint1.host;
/* 223 */           int i = localTCPEndpoint1.port;
/* 224 */           TCPTransport localTCPTransport = localTCPEndpoint1.transport;
/*     */ 
/* 226 */           if ((str1 != null) && (!str1.equals(str2)))
/*     */           {
/* 231 */             if (i != 0)
/*     */             {
/* 236 */               localLinkedList.clear();
/*     */             }
/* 238 */             localTCPEndpoint1 = new TCPEndpoint(str1, i, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/* 239 */             localTCPEndpoint1.listenPort = paramInt;
/* 240 */             localTCPEndpoint1.transport = localTCPTransport;
/* 241 */             localLinkedList.add(localTCPEndpoint1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 247 */     return localTCPEndpoint1;
/*     */   }
/*     */ 
/*     */   private static String resampleLocalHost()
/*     */   {
/* 256 */     String str = getHostnameProperty();
/*     */ 
/* 258 */     synchronized (localEndpoints)
/*     */     {
/* 261 */       if (str != null) {
/* 262 */         if (!localHostKnown)
/*     */         {
/* 267 */           setLocalHost(str);
/* 268 */         } else if (!str.equals(localHost))
/*     */         {
/* 273 */           localHost = str;
/*     */ 
/* 275 */           if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
/* 276 */             TCPTransport.tcpLog.log(Log.BRIEF, "updated local hostname to: " + localHost);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 281 */       return localHost;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void setLocalHost(String paramString)
/*     */   {
/* 291 */     synchronized (localEndpoints)
/*     */     {
/* 296 */       if (!localHostKnown) {
/* 297 */         localHost = paramString;
/* 298 */         localHostKnown = true;
/*     */ 
/* 300 */         if (TCPTransport.tcpLog.isLoggable(Log.BRIEF)) {
/* 301 */           TCPTransport.tcpLog.log(Log.BRIEF, "local host set to " + paramString);
/*     */         }
/*     */ 
/* 304 */         for (LinkedList localLinkedList : localEndpoints.values())
/*     */         {
/* 306 */           synchronized (localLinkedList) {
/* 307 */             for (TCPEndpoint localTCPEndpoint : localLinkedList)
/* 308 */               localTCPEndpoint.host = paramString;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void setDefaultPort(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */   {
/* 325 */     TCPEndpoint localTCPEndpoint1 = new TCPEndpoint(null, 0, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*     */ 
/* 327 */     synchronized (localEndpoints) {
/* 328 */       LinkedList localLinkedList = (LinkedList)localEndpoints.get(localTCPEndpoint1);
/*     */ 
/* 330 */       synchronized (localLinkedList) {
/* 331 */         int i = localLinkedList.size();
/* 332 */         TCPEndpoint localTCPEndpoint2 = (TCPEndpoint)localLinkedList.getLast();
/*     */ 
/* 334 */         for (TCPEndpoint localTCPEndpoint3 : localLinkedList) {
/* 335 */           localTCPEndpoint3.port = paramInt;
/*     */         }
/* 337 */         if (i > 1)
/*     */         {
/* 342 */           localLinkedList.clear();
/* 343 */           localLinkedList.add(localTCPEndpoint2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 351 */       ??? = new TCPEndpoint(null, paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/* 352 */       localEndpoints.put(???, localLinkedList);
/*     */ 
/* 354 */       if (TCPTransport.tcpLog.isLoggable(Log.BRIEF))
/* 355 */         TCPTransport.tcpLog.log(Log.BRIEF, "default port for server socket factory " + paramRMIServerSocketFactory + " and client socket factory " + paramRMIClientSocketFactory + " set to " + paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Transport getOutboundTransport()
/*     */   {
/* 368 */     TCPEndpoint localTCPEndpoint = getLocalEndpoint(0, null, null);
/* 369 */     return localTCPEndpoint.transport;
/*     */   }
/*     */ 
/*     */   private static Collection<TCPTransport> allKnownTransports()
/*     */   {
/*     */     HashSet localHashSet;
/* 381 */     synchronized (localEndpoints)
/*     */     {
/* 383 */       localHashSet = new HashSet(localEndpoints.size());
/* 384 */       for (LinkedList localLinkedList : localEndpoints.values())
/*     */       {
/* 390 */         TCPEndpoint localTCPEndpoint = (TCPEndpoint)localLinkedList.getFirst();
/* 391 */         localHashSet.add(localTCPEndpoint.transport);
/*     */       }
/*     */     }
/* 394 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   public static void shedConnectionCaches()
/*     */   {
/* 402 */     for (TCPTransport localTCPTransport : allKnownTransports())
/* 403 */       localTCPTransport.shedConnectionCaches();
/*     */   }
/*     */ 
/*     */   public void exportObject(Target paramTarget)
/*     */     throws RemoteException
/*     */   {
/* 411 */     this.transport.exportObject(paramTarget);
/*     */   }
/*     */ 
/*     */   public Channel getChannel()
/*     */   {
/* 418 */     return getOutboundTransport().getChannel(this);
/*     */   }
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 425 */     return this.host;
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 436 */     return this.port;
/*     */   }
/*     */ 
/*     */   public int getListenPort()
/*     */   {
/* 447 */     return this.listenPort;
/*     */   }
/*     */ 
/*     */   public Transport getInboundTransport()
/*     */   {
/* 456 */     return this.transport;
/*     */   }
/*     */ 
/*     */   public RMIClientSocketFactory getClientSocketFactory()
/*     */   {
/* 463 */     return this.csf;
/*     */   }
/*     */ 
/*     */   public RMIServerSocketFactory getServerSocketFactory()
/*     */   {
/* 470 */     return this.ssf;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 477 */     return "[" + this.host + ":" + this.port + (this.ssf != null ? "," + this.ssf : "") + (this.csf != null ? "," + this.csf : "") + "]";
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 484 */     return this.port;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 488 */     if ((paramObject != null) && ((paramObject instanceof TCPEndpoint))) {
/* 489 */       TCPEndpoint localTCPEndpoint = (TCPEndpoint)paramObject;
/* 490 */       if ((this.port != localTCPEndpoint.port) || (!this.host.equals(localTCPEndpoint.host)))
/* 491 */         return false;
/* 492 */       if (((this.csf == null ? 1 : 0) ^ (localTCPEndpoint.csf == null ? 1 : 0)) == 0) { if (((this.ssf == null ? 1 : 0) ^ (localTCPEndpoint.ssf == null ? 1 : 0)) == 0);
/*     */       } else {
/* 494 */         return false;
/*     */       }
/*     */ 
/* 501 */       if ((this.csf != null) && ((this.csf.getClass() != localTCPEndpoint.csf.getClass()) || (!this.csf.equals(localTCPEndpoint.csf))))
/*     */       {
/* 503 */         return false;
/* 504 */       }if ((this.ssf != null) && ((this.ssf.getClass() != localTCPEndpoint.ssf.getClass()) || (!this.ssf.equals(localTCPEndpoint.ssf))))
/*     */       {
/* 506 */         return false;
/* 507 */       }return true;
/*     */     }
/* 509 */     return false;
/*     */   }
/*     */ 
/*     */   public void write(ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/* 521 */     if (this.csf == null) {
/* 522 */       paramObjectOutput.writeByte(0);
/* 523 */       paramObjectOutput.writeUTF(this.host);
/* 524 */       paramObjectOutput.writeInt(this.port);
/*     */     } else {
/* 526 */       paramObjectOutput.writeByte(1);
/* 527 */       paramObjectOutput.writeUTF(this.host);
/* 528 */       paramObjectOutput.writeInt(this.port);
/* 529 */       paramObjectOutput.writeObject(this.csf);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static TCPEndpoint read(ObjectInput paramObjectInput)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 543 */     RMIClientSocketFactory localRMIClientSocketFactory = null;
/*     */ 
/* 545 */     int j = paramObjectInput.readByte();
/*     */     String str;
/*     */     int i;
/* 546 */     switch (j) {
/*     */     case 0:
/* 548 */       str = paramObjectInput.readUTF();
/* 549 */       i = paramObjectInput.readInt();
/* 550 */       break;
/*     */     case 1:
/* 553 */       str = paramObjectInput.readUTF();
/* 554 */       i = paramObjectInput.readInt();
/* 555 */       localRMIClientSocketFactory = (RMIClientSocketFactory)paramObjectInput.readObject();
/* 556 */       break;
/*     */     default:
/* 559 */       throw new IOException("invalid endpoint format");
/*     */     }
/* 561 */     return new TCPEndpoint(str, i, localRMIClientSocketFactory, null);
/*     */   }
/*     */ 
/*     */   public void writeHostPortFormat(DataOutput paramDataOutput)
/*     */     throws IOException
/*     */   {
/* 569 */     if (this.csf != null) {
/* 570 */       throw new InternalError("TCPEndpoint.writeHostPortFormat: called for endpoint with non-null socket factory");
/*     */     }
/*     */ 
/* 573 */     paramDataOutput.writeUTF(this.host);
/* 574 */     paramDataOutput.writeInt(this.port);
/*     */   }
/*     */ 
/*     */   public static TCPEndpoint readHostPortFormat(DataInput paramDataInput)
/*     */     throws IOException
/*     */   {
/* 584 */     String str = paramDataInput.readUTF();
/* 585 */     int i = paramDataInput.readInt();
/* 586 */     return new TCPEndpoint(str, i);
/*     */   }
/*     */ 
/*     */   private static RMISocketFactory chooseFactory() {
/* 590 */     RMISocketFactory localRMISocketFactory = RMISocketFactory.getSocketFactory();
/* 591 */     if (localRMISocketFactory == null) {
/* 592 */       localRMISocketFactory = TCPTransport.defaultSocketFactory;
/*     */     }
/* 594 */     return localRMISocketFactory;
/*     */   }
/*     */ 
/*     */   Socket newSocket()
/*     */     throws RemoteException
/*     */   {
/* 601 */     if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
/* 602 */       TCPTransport.tcpLog.log(Log.VERBOSE, "opening socket to " + this);
/*     */     }
/*     */ 
/*     */     Socket localSocket;
/*     */     try
/*     */     {
/* 609 */       Object localObject = this.csf;
/* 610 */       if (localObject == null) {
/* 611 */         localObject = chooseFactory();
/*     */       }
/* 613 */       localSocket = ((RMIClientSocketFactory)localObject).createSocket(this.host, this.port);
/*     */     }
/*     */     catch (java.net.UnknownHostException localUnknownHostException) {
/* 616 */       throw new java.rmi.UnknownHostException("Unknown host: " + this.host, localUnknownHostException);
/*     */     }
/*     */     catch (java.net.ConnectException localConnectException) {
/* 619 */       throw new java.rmi.ConnectException("Connection refused to host: " + this.host, localConnectException);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */       try {
/* 624 */         shedConnectionCaches();
/*     */       }
/*     */       catch (OutOfMemoryError|Exception localOutOfMemoryError)
/*     */       {
/*     */       }
/*     */ 
/* 631 */       throw new ConnectIOException("Exception creating connection to: " + this.host, localIOException);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 638 */       localSocket.setTcpNoDelay(true);
/*     */     }
/*     */     catch (Exception localException1)
/*     */     {
/*     */     }
/*     */     try
/*     */     {
/* 645 */       localSocket.setKeepAlive(true);
/*     */     }
/*     */     catch (Exception localException2)
/*     */     {
/*     */     }
/* 650 */     return localSocket;
/*     */   }
/*     */ 
/*     */   ServerSocket newServerSocket()
/*     */     throws IOException
/*     */   {
/* 657 */     if (TCPTransport.tcpLog.isLoggable(Log.VERBOSE)) {
/* 658 */       TCPTransport.tcpLog.log(Log.VERBOSE, "creating server socket on " + this);
/*     */     }
/*     */ 
/* 662 */     Object localObject = this.ssf;
/* 663 */     if (localObject == null) {
/* 664 */       localObject = chooseFactory();
/*     */     }
/* 666 */     ServerSocket localServerSocket = ((RMIServerSocketFactory)localObject).createServerSocket(this.listenPort);
/*     */ 
/* 670 */     if (this.listenPort == 0) {
/* 671 */       setDefaultPort(localServerSocket.getLocalPort(), this.csf, this.ssf);
/*     */     }
/* 673 */     return localServerSocket;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 108 */     localHost = getHostnameProperty();
/*     */ 
/* 111 */     if (localHost == null) {
/*     */       try {
/* 113 */         InetAddress localInetAddress = InetAddress.getLocalHost();
/* 114 */         byte[] arrayOfByte = localInetAddress.getAddress();
/* 115 */         if ((arrayOfByte[0] == 127) && (arrayOfByte[1] == 0) && (arrayOfByte[2] == 0) && (arrayOfByte[3] == 1))
/*     */         {
/* 119 */           localHostKnown = false;
/*     */         }
/*     */ 
/* 125 */         if (getBoolean("java.rmi.server.useLocalHostName")) {
/* 126 */           localHost = FQDN.attemptFQDN(localInetAddress);
/*     */         }
/*     */         else
/*     */         {
/* 131 */           localHost = localInetAddress.getHostAddress();
/*     */         }
/*     */       } catch (Exception localException) {
/* 134 */         localHostKnown = false;
/* 135 */         localHost = null;
/*     */       }
/*     */     }
/*     */ 
/* 139 */     if (TCPTransport.tcpLog.isLoggable(Log.BRIEF))
/* 140 */       TCPTransport.tcpLog.log(Log.BRIEF, "localHostKnown = " + localHostKnown + ", localHost = " + localHost);
/*     */   }
/*     */ 
/*     */   private static class FQDN
/*     */     implements Runnable
/*     */   {
/*     */     private String reverseLookup;
/*     */     private String hostAddress;
/*     */ 
/*     */     private FQDN(String paramString)
/*     */     {
/* 693 */       this.hostAddress = paramString;
/*     */     }
/*     */ 
/*     */     static String attemptFQDN(InetAddress paramInetAddress)
/*     */       throws java.net.UnknownHostException
/*     */     {
/* 717 */       Object localObject1 = paramInetAddress.getHostName();
/*     */ 
/* 719 */       if (((String)localObject1).indexOf('.') < 0)
/*     */       {
/* 721 */         String str = paramInetAddress.getHostAddress();
/* 722 */         FQDN localFQDN = new FQDN(str);
/*     */ 
/* 724 */         int i = TCPEndpoint.getInt("sun.rmi.transport.tcp.localHostNameTimeOut", 10000);
/*     */         try
/*     */         {
/* 729 */           synchronized (localFQDN) {
/* 730 */             localFQDN.getFQDN();
/*     */ 
/* 733 */             localFQDN.wait(i);
/*     */           }
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/* 737 */           Thread.currentThread().interrupt();
/*     */         }
/* 739 */         localObject1 = localFQDN.getHost();
/*     */ 
/* 741 */         if ((localObject1 == null) || (((String)localObject1).equals("")) || (((String)localObject1).indexOf('.') < 0))
/*     */         {
/* 744 */           localObject1 = str;
/*     */         }
/*     */       }
/* 747 */       return localObject1;
/*     */     }
/*     */ 
/*     */     private void getFQDN()
/*     */     {
/* 759 */       Thread localThread = (Thread)AccessController.doPrivileged(new NewThreadAction(this, "FQDN Finder", true));
/*     */ 
/* 761 */       localThread.start();
/*     */     }
/*     */ 
/*     */     private synchronized String getHost() {
/* 765 */       return this.reverseLookup;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 773 */       String str = null;
/*     */       try
/*     */       {
/* 776 */         str = InetAddress.getByName(this.hostAddress).getHostName();
/*     */       } catch (java.net.UnknownHostException ) {
/*     */       } finally {
/* 779 */         synchronized (this) {
/* 780 */           this.reverseLookup = str;
/* 781 */           notify();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.tcp.TCPEndpoint
 * JD-Core Version:    0.6.2
 */