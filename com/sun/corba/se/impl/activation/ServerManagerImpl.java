/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ActivationSystemException;
/*     */ import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
/*     */ import com.sun.corba.se.spi.activation.EndPointInfo;
/*     */ import com.sun.corba.se.spi.activation.InvalidORBid;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;
/*     */ import com.sun.corba.se.spi.activation.NoSuchEndPoint;
/*     */ import com.sun.corba.se.spi.activation.ORBAlreadyRegistered;
/*     */ import com.sun.corba.se.spi.activation.ORBPortInfo;
/*     */ import com.sun.corba.se.spi.activation.Repository;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
/*     */ import com.sun.corba.se.spi.activation.Server;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyActive;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyInstalled;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyUninstalled;
/*     */ import com.sun.corba.se.spi.activation.ServerHeldDown;
/*     */ import com.sun.corba.se.spi.activation.ServerNotActive;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import com.sun.corba.se.spi.activation._ServerManagerImplBase;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.IORTemplate;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.protocol.ForwardException;
/*     */ import com.sun.corba.se.spi.transport.CorbaTransportManager;
/*     */ import com.sun.corba.se.spi.transport.SocketOrChannelAcceptor;
/*     */ import java.io.PrintStream;
/*     */ import java.net.ServerSocket;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ServerManagerImpl extends _ServerManagerImplBase
/*     */   implements BadServerIdHandler
/*     */ {
/*     */   HashMap serverTable;
/*     */   Repository repository;
/*     */   CorbaTransportManager transportManager;
/*     */   int initialPort;
/*     */   ORB orb;
/*     */   ActivationSystemException wrapper;
/*     */   String dbDirName;
/* 100 */   boolean debug = false;
/*     */   private int serverStartupDelay;
/*     */ 
/*     */   ServerManagerImpl(ORB paramORB, CorbaTransportManager paramCorbaTransportManager, Repository paramRepository, String paramString, boolean paramBoolean)
/*     */   {
/* 107 */     this.orb = paramORB;
/* 108 */     this.wrapper = ActivationSystemException.get(paramORB, "orbd.activator");
/*     */ 
/* 110 */     this.transportManager = paramCorbaTransportManager;
/* 111 */     this.repository = paramRepository;
/* 112 */     this.dbDirName = paramString;
/* 113 */     this.debug = paramBoolean;
/*     */ 
/* 115 */     LegacyServerSocketEndPointInfo localLegacyServerSocketEndPointInfo = paramORB.getLegacyServerSocketManager().legacyGetEndpoint("BOOT_NAMING");
/*     */ 
/* 119 */     this.initialPort = ((SocketOrChannelAcceptor)localLegacyServerSocketEndPointInfo).getServerSocket().getLocalPort();
/*     */ 
/* 121 */     this.serverTable = new HashMap(256);
/*     */ 
/* 126 */     this.serverStartupDelay = 1000;
/* 127 */     String str = System.getProperty("com.sun.CORBA.activation.ServerStartupDelay");
/* 128 */     if (str != null) {
/*     */       try {
/* 130 */         this.serverStartupDelay = Integer.parseInt(str);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */     }
/* 136 */     Class localClass = paramORB.getORBData().getBadServerIdHandler();
/* 137 */     if (localClass == null)
/* 138 */       paramORB.setBadServerIdHandler(this);
/*     */     else {
/* 140 */       paramORB.initBadServerIdHandler();
/*     */     }
/*     */ 
/* 143 */     paramORB.connect(this);
/* 144 */     ProcessMonitorThread.start(this.serverTable);
/*     */   }
/*     */ 
/*     */   public void activate(int paramInt)
/*     */     throws ServerAlreadyActive, ServerNotRegistered, ServerHeldDown
/*     */   {
/* 153 */     Integer localInteger = new Integer(paramInt);
/*     */     ServerTableEntry localServerTableEntry;
/* 155 */     synchronized (this.serverTable) {
/* 156 */       localServerTableEntry = (ServerTableEntry)this.serverTable.get(localInteger);
/*     */     }
/*     */ 
/* 159 */     if ((localServerTableEntry != null) && (localServerTableEntry.isActive())) {
/* 160 */       if (this.debug) {
/* 161 */         System.out.println("ServerManagerImpl: activate for server Id " + paramInt + " failed because server is already active. " + "entry = " + localServerTableEntry);
/*     */       }
/*     */ 
/* 165 */       throw new ServerAlreadyActive(paramInt);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 174 */       localServerTableEntry = getEntry(paramInt);
/*     */ 
/* 176 */       if (this.debug) {
/* 177 */         System.out.println("ServerManagerImpl: locateServer called with  serverId=" + paramInt + " endpointType=" + "IIOP_CLEAR_TEXT" + " block=false");
/*     */       }
/*     */ 
/* 181 */       ServerLocation localServerLocation = locateServer(localServerTableEntry, "IIOP_CLEAR_TEXT", false);
/*     */ 
/* 183 */       if (this.debug)
/* 184 */         System.out.println("ServerManagerImpl: activate for server Id " + paramInt + " found location " + localServerLocation.hostname + " and activated it");
/*     */     }
/*     */     catch (NoSuchEndPoint localNoSuchEndPoint)
/*     */     {
/* 188 */       if (this.debug)
/* 189 */         System.out.println("ServerManagerImpl: activate for server Id  threw NoSuchEndpoint exception, which was ignored");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void active(int paramInt, Server paramServer)
/*     */     throws ServerNotRegistered
/*     */   {
/* 197 */     Integer localInteger = new Integer(paramInt);
/*     */ 
/* 199 */     synchronized (this.serverTable) {
/* 200 */       ServerTableEntry localServerTableEntry = (ServerTableEntry)this.serverTable.get(localInteger);
/*     */ 
/* 202 */       if (localServerTableEntry == null) {
/* 203 */         if (this.debug) {
/* 204 */           System.out.println("ServerManagerImpl: active for server Id " + paramInt + " called, but no such server is registered.");
/*     */         }
/*     */ 
/* 207 */         throw this.wrapper.serverNotExpectedToRegister();
/*     */       }
/* 209 */       if (this.debug) {
/* 210 */         System.out.println("ServerManagerImpl: active for server Id " + paramInt + " called.  This server is now active.");
/*     */       }
/*     */ 
/* 213 */       localServerTableEntry.register(paramServer);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void registerEndpoints(int paramInt, String paramString, EndPointInfo[] paramArrayOfEndPointInfo)
/*     */     throws NoSuchEndPoint, ServerNotRegistered, ORBAlreadyRegistered
/*     */   {
/* 224 */     Integer localInteger = new Integer(paramInt);
/*     */ 
/* 226 */     synchronized (this.serverTable) {
/* 227 */       ServerTableEntry localServerTableEntry = (ServerTableEntry)this.serverTable.get(localInteger);
/*     */ 
/* 229 */       if (localServerTableEntry == null) {
/* 230 */         if (this.debug) {
/* 231 */           System.out.println("ServerManagerImpl: registerEndpoint for server Id " + paramInt + " called, but no such server is registered.");
/*     */         }
/*     */ 
/* 235 */         throw this.wrapper.serverNotExpectedToRegister();
/*     */       }
/* 237 */       if (this.debug) {
/* 238 */         System.out.println("ServerManagerImpl: registerEndpoints for server Id " + paramInt + " called.  This server is now active.");
/*     */       }
/*     */ 
/* 242 */       localServerTableEntry.registerPorts(paramString, paramArrayOfEndPointInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] getActiveServers()
/*     */   {
/* 251 */     int[] arrayOfInt = null;
/*     */ 
/* 253 */     synchronized (this.serverTable) {
/* 256 */       ArrayList localArrayList = new ArrayList(0);
/*     */ 
/* 258 */       Iterator localIterator = this.serverTable.keySet().iterator();
/*     */       ServerTableEntry localServerTableEntry;
/*     */       try {
/* 261 */         while (localIterator.hasNext()) {
/* 262 */           Integer localInteger = (Integer)localIterator.next();
/*     */ 
/* 264 */           localServerTableEntry = (ServerTableEntry)this.serverTable.get(localInteger);
/*     */ 
/* 266 */           if ((localServerTableEntry.isValid()) && (localServerTableEntry.isActive())) {
/* 267 */             localArrayList.add(localServerTableEntry);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (NoSuchElementException localNoSuchElementException)
/*     */       {
/*     */       }
/*     */ 
/* 275 */       arrayOfInt = new int[localArrayList.size()];
/* 276 */       for (int j = 0; j < localArrayList.size(); j++) {
/* 277 */         localServerTableEntry = (ServerTableEntry)localArrayList.get(j);
/* 278 */         arrayOfInt[j] = localServerTableEntry.getServerId();
/*     */       }
/*     */     }
/*     */ 
/* 282 */     if (this.debug) {
/* 283 */       ??? = new StringBuffer();
/* 284 */       for (int i = 0; i < arrayOfInt.length; i++) {
/* 285 */         ((StringBuffer)???).append(' ');
/* 286 */         ((StringBuffer)???).append(arrayOfInt[i]);
/*     */       }
/*     */ 
/* 289 */       System.out.println("ServerManagerImpl: getActiveServers returns" + ((StringBuffer)???).toString());
/*     */     }
/*     */ 
/* 293 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public void shutdown(int paramInt)
/*     */     throws ServerNotActive
/*     */   {
/* 299 */     Integer localInteger = new Integer(paramInt);
/*     */ 
/* 301 */     synchronized (this.serverTable) {
/* 302 */       ServerTableEntry localServerTableEntry = (ServerTableEntry)this.serverTable.remove(localInteger);
/*     */ 
/* 304 */       if (localServerTableEntry == null) {
/* 305 */         if (this.debug) {
/* 306 */           System.out.println("ServerManagerImpl: shutdown for server Id " + paramInt + " throws ServerNotActive.");
/*     */         }
/*     */ 
/* 309 */         throw new ServerNotActive(paramInt);
/*     */       }
/*     */       try
/*     */       {
/* 313 */         localServerTableEntry.destroy();
/*     */ 
/* 315 */         if (this.debug)
/* 316 */           System.out.println("ServerManagerImpl: shutdown for server Id " + paramInt + " completed.");
/*     */       }
/*     */       catch (Exception localException) {
/* 319 */         if (this.debug)
/* 320 */           System.out.println("ServerManagerImpl: shutdown for server Id " + paramInt + " threw exception " + localException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private ServerTableEntry getEntry(int paramInt)
/*     */     throws ServerNotRegistered
/*     */   {
/* 329 */     Integer localInteger = new Integer(paramInt);
/* 330 */     ServerTableEntry localServerTableEntry = null;
/*     */ 
/* 332 */     synchronized (this.serverTable) {
/* 333 */       localServerTableEntry = (ServerTableEntry)this.serverTable.get(localInteger);
/*     */ 
/* 335 */       if (this.debug) {
/* 336 */         if (localServerTableEntry == null) {
/* 337 */           System.out.println("ServerManagerImpl: getEntry: no active server found.");
/*     */         }
/*     */         else {
/* 340 */           System.out.println("ServerManagerImpl: getEntry:  active server found " + localServerTableEntry + ".");
/*     */         }
/*     */       }
/*     */ 
/* 344 */       if ((localServerTableEntry != null) && (!localServerTableEntry.isValid())) {
/* 345 */         this.serverTable.remove(localInteger);
/* 346 */         localServerTableEntry = null;
/*     */       }
/*     */ 
/* 349 */       if (localServerTableEntry == null) {
/* 350 */         ServerDef localServerDef = this.repository.getServer(paramInt);
/*     */ 
/* 352 */         localServerTableEntry = new ServerTableEntry(this.wrapper, paramInt, localServerDef, this.initialPort, this.dbDirName, false, this.debug);
/*     */ 
/* 354 */         this.serverTable.put(localInteger, localServerTableEntry);
/* 355 */         localServerTableEntry.activate();
/*     */       }
/*     */     }
/*     */ 
/* 359 */     return localServerTableEntry;
/*     */   }
/*     */ 
/*     */   private ServerLocation locateServer(ServerTableEntry paramServerTableEntry, String paramString, boolean paramBoolean)
/*     */     throws NoSuchEndPoint, ServerNotRegistered, ServerHeldDown
/*     */   {
/* 366 */     ServerLocation localServerLocation = new ServerLocation();
/*     */ 
/* 372 */     if (paramBoolean) {
/*     */       ORBPortInfo[] arrayOfORBPortInfo;
/*     */       try { arrayOfORBPortInfo = paramServerTableEntry.lookup(paramString);
/*     */       } catch (Exception localException) {
/* 376 */         if (this.debug) {
/* 377 */           System.out.println("ServerManagerImpl: locateServer: server held down");
/*     */         }
/*     */ 
/* 380 */         throw new ServerHeldDown(paramServerTableEntry.getServerId());
/*     */       }
/*     */ 
/* 383 */       String str = this.orb.getLegacyServerSocketManager().legacyGetEndpoint("DEFAULT_ENDPOINT").getHostName();
/*     */ 
/* 386 */       localServerLocation.hostname = str;
/*     */       int i;
/* 388 */       if (arrayOfORBPortInfo != null)
/* 389 */         i = arrayOfORBPortInfo.length;
/*     */       else {
/* 391 */         i = 0;
/*     */       }
/* 393 */       localServerLocation.ports = new ORBPortInfo[i];
/* 394 */       for (int j = 0; j < i; j++) {
/* 395 */         localServerLocation.ports[j] = new ORBPortInfo(arrayOfORBPortInfo[j].orbId, arrayOfORBPortInfo[j].port);
/*     */ 
/* 398 */         if (this.debug) {
/* 399 */           System.out.println("ServerManagerImpl: locateServer: server located at location " + localServerLocation.hostname + " ORBid  " + arrayOfORBPortInfo[j].orbId + " Port " + arrayOfORBPortInfo[j].port);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 407 */     return localServerLocation;
/*     */   }
/*     */ 
/*     */   private ServerLocationPerORB locateServerForORB(ServerTableEntry paramServerTableEntry, String paramString, boolean paramBoolean)
/*     */     throws InvalidORBid, ServerNotRegistered, ServerHeldDown
/*     */   {
/* 414 */     ServerLocationPerORB localServerLocationPerORB = new ServerLocationPerORB();
/*     */ 
/* 420 */     if (paramBoolean) {
/*     */       EndPointInfo[] arrayOfEndPointInfo;
/*     */       try { arrayOfEndPointInfo = paramServerTableEntry.lookupForORB(paramString);
/*     */       } catch (InvalidORBid localInvalidORBid) {
/* 424 */         throw localInvalidORBid;
/*     */       } catch (Exception localException) {
/* 426 */         if (this.debug) {
/* 427 */           System.out.println("ServerManagerImpl: locateServerForORB: server held down");
/*     */         }
/*     */ 
/* 430 */         throw new ServerHeldDown(paramServerTableEntry.getServerId());
/*     */       }
/*     */ 
/* 433 */       String str = this.orb.getLegacyServerSocketManager().legacyGetEndpoint("DEFAULT_ENDPOINT").getHostName();
/*     */ 
/* 436 */       localServerLocationPerORB.hostname = str;
/*     */       int i;
/* 438 */       if (arrayOfEndPointInfo != null)
/* 439 */         i = arrayOfEndPointInfo.length;
/*     */       else {
/* 441 */         i = 0;
/*     */       }
/* 443 */       localServerLocationPerORB.ports = new EndPointInfo[i];
/* 444 */       for (int j = 0; j < i; j++) {
/* 445 */         localServerLocationPerORB.ports[j] = new EndPointInfo(arrayOfEndPointInfo[j].endpointType, arrayOfEndPointInfo[j].port);
/*     */ 
/* 448 */         if (this.debug) {
/* 449 */           System.out.println("ServerManagerImpl: locateServer: server located at location " + localServerLocationPerORB.hostname + " endpointType  " + arrayOfEndPointInfo[j].endpointType + " Port " + arrayOfEndPointInfo[j].port);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 457 */     return localServerLocationPerORB;
/*     */   }
/*     */ 
/*     */   public String[] getORBNames(int paramInt) throws ServerNotRegistered
/*     */   {
/*     */     try
/*     */     {
/* 464 */       ServerTableEntry localServerTableEntry = getEntry(paramInt);
/* 465 */       return localServerTableEntry.getORBList(); } catch (Exception localException) {
/*     */     }
/* 467 */     throw new ServerNotRegistered(paramInt);
/*     */   }
/*     */ 
/*     */   private ServerTableEntry getRunningEntry(int paramInt)
/*     */     throws ServerNotRegistered
/*     */   {
/* 474 */     ServerTableEntry localServerTableEntry = getEntry(paramInt);
/*     */     try
/*     */     {
/* 478 */       ORBPortInfo[] arrayOfORBPortInfo = localServerTableEntry.lookup("IIOP_CLEAR_TEXT");
/*     */     } catch (Exception localException) {
/* 480 */       return null;
/*     */     }
/* 482 */     return localServerTableEntry;
/*     */   }
/*     */ 
/*     */   public void install(int paramInt)
/*     */     throws ServerNotRegistered, ServerHeldDown, ServerAlreadyInstalled
/*     */   {
/* 489 */     ServerTableEntry localServerTableEntry = getRunningEntry(paramInt);
/* 490 */     if (localServerTableEntry != null) {
/* 491 */       this.repository.install(paramInt);
/* 492 */       localServerTableEntry.install();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void uninstall(int paramInt)
/*     */     throws ServerNotRegistered, ServerHeldDown, ServerAlreadyUninstalled
/*     */   {
/* 499 */     ServerTableEntry localServerTableEntry = (ServerTableEntry)this.serverTable.get(new Integer(paramInt));
/*     */ 
/* 502 */     if (localServerTableEntry != null)
/*     */     {
/* 504 */       localServerTableEntry = (ServerTableEntry)this.serverTable.remove(new Integer(paramInt));
/*     */ 
/* 507 */       if (localServerTableEntry == null) {
/* 508 */         if (this.debug) {
/* 509 */           System.out.println("ServerManagerImpl: shutdown for server Id " + paramInt + " throws ServerNotActive.");
/*     */         }
/*     */ 
/* 512 */         throw new ServerHeldDown(paramInt);
/*     */       }
/*     */ 
/* 515 */       localServerTableEntry.uninstall();
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServerLocation locateServer(int paramInt, String paramString)
/*     */     throws NoSuchEndPoint, ServerNotRegistered, ServerHeldDown
/*     */   {
/* 522 */     ServerTableEntry localServerTableEntry = getEntry(paramInt);
/* 523 */     if (this.debug) {
/* 524 */       System.out.println("ServerManagerImpl: locateServer called with  serverId=" + paramInt + " endpointType=" + paramString + " block=true");
/*     */     }
/*     */ 
/* 531 */     return locateServer(localServerTableEntry, paramString, true);
/*     */   }
/*     */ 
/*     */   public ServerLocationPerORB locateServerForORB(int paramInt, String paramString)
/*     */     throws InvalidORBid, ServerNotRegistered, ServerHeldDown
/*     */   {
/* 540 */     ServerTableEntry localServerTableEntry = getEntry(paramInt);
/*     */ 
/* 545 */     if (this.debug) {
/* 546 */       System.out.println("ServerManagerImpl: locateServerForORB called with  serverId=" + paramInt + " orbId=" + paramString + " block=true");
/*     */     }
/*     */ 
/* 549 */     return locateServerForORB(localServerTableEntry, paramString, true);
/*     */   }
/*     */ 
/*     */   public void handle(ObjectKey paramObjectKey)
/*     */   {
/* 555 */     IOR localIOR = null;
/*     */ 
/* 559 */     ObjectKeyTemplate localObjectKeyTemplate = paramObjectKey.getTemplate();
/* 560 */     int i = localObjectKeyTemplate.getServerId();
/* 561 */     String str = localObjectKeyTemplate.getORBId();
/*     */     try
/*     */     {
/* 566 */       ServerTableEntry localServerTableEntry = getEntry(i);
/* 567 */       ServerLocationPerORB localServerLocationPerORB = locateServerForORB(localServerTableEntry, str, true);
/*     */ 
/* 569 */       if (this.debug) {
/* 570 */         System.out.println("ServerManagerImpl: handle called for server id" + i + "  orbid  " + str);
/*     */       }
/*     */ 
/* 578 */       int j = 0;
/* 579 */       EndPointInfo[] arrayOfEndPointInfo = localServerLocationPerORB.ports;
/* 580 */       for (int k = 0; k < arrayOfEndPointInfo.length; k++) {
/* 581 */         if (arrayOfEndPointInfo[k].endpointType.equals("IIOP_CLEAR_TEXT")) {
/* 582 */           j = arrayOfEndPointInfo[k].port;
/* 583 */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 589 */       IIOPAddress localIIOPAddress = IIOPFactories.makeIIOPAddress(this.orb, localServerLocationPerORB.hostname, j);
/*     */ 
/* 591 */       IIOPProfileTemplate localIIOPProfileTemplate = IIOPFactories.makeIIOPProfileTemplate(this.orb, GIOPVersion.V1_2, localIIOPAddress);
/*     */ 
/* 594 */       if (GIOPVersion.V1_2.supportsIORIIOPProfileComponents()) {
/* 595 */         localIIOPProfileTemplate.add(IIOPFactories.makeCodeSetsComponent(this.orb));
/* 596 */         localIIOPProfileTemplate.add(IIOPFactories.makeMaxStreamFormatVersionComponent());
/*     */       }
/* 598 */       IORTemplate localIORTemplate = IORFactories.makeIORTemplate(localObjectKeyTemplate);
/* 599 */       localIORTemplate.add(localIIOPProfileTemplate);
/*     */ 
/* 601 */       localIOR = localIORTemplate.makeIOR(this.orb, "IDL:org/omg/CORBA/Object:1.0", paramObjectKey.getId());
/*     */     }
/*     */     catch (Exception localException1) {
/* 604 */       throw this.wrapper.errorInBadServerIdHandler(localException1);
/*     */     }
/*     */ 
/* 607 */     if (this.debug) {
/* 608 */       System.out.println("ServerManagerImpl: handle throws ForwardException");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 617 */       Thread.sleep(this.serverStartupDelay);
/*     */     } catch (Exception localException2) {
/* 619 */       System.out.println("Exception = " + localException2);
/* 620 */       localException2.printStackTrace();
/*     */     }
/*     */ 
/* 623 */     throw new ForwardException(this.orb, localIOR);
/*     */   }
/*     */ 
/*     */   public int getEndpoint(String paramString) throws NoSuchEndPoint
/*     */   {
/* 628 */     return this.orb.getLegacyServerSocketManager().legacyGetTransientServerPort(paramString);
/*     */   }
/*     */ 
/*     */   public int getServerPortForType(ServerLocationPerORB paramServerLocationPerORB, String paramString)
/*     */     throws NoSuchEndPoint
/*     */   {
/* 636 */     EndPointInfo[] arrayOfEndPointInfo = paramServerLocationPerORB.ports;
/* 637 */     for (int i = 0; i < arrayOfEndPointInfo.length; i++) {
/* 638 */       if (arrayOfEndPointInfo[i].endpointType.equals(paramString)) {
/* 639 */         return arrayOfEndPointInfo[i].port;
/*     */       }
/*     */     }
/* 642 */     throw new NoSuchEndPoint();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ServerManagerImpl
 * JD-Core Version:    0.6.2
 */