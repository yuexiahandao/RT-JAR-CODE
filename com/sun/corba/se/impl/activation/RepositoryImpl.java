/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ActivationSystemException;
/*     */ import com.sun.corba.se.spi.activation.BadServerDefinition;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyInstalled;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyRegistered;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyUninstalled;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import com.sun.corba.se.spi.activation._RepositoryImplBase;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketManager;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.transport.SocketOrChannelAcceptor;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.ServerSocket;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class RepositoryImpl extends _RepositoryImplBase
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8458417785209341858L;
/* 395 */   private transient boolean debug = false;
/*     */   static final int illegalServerId = -1;
/* 399 */   private transient RepositoryDB db = null;
/*     */ 
/* 401 */   transient ORB orb = null;
/*     */   transient ActivationSystemException wrapper;
/*     */ 
/*     */   RepositoryImpl(ORB paramORB, File paramFile, boolean paramBoolean)
/*     */   {
/*  73 */     this.debug = paramBoolean;
/*  74 */     this.orb = paramORB;
/*  75 */     this.wrapper = ActivationSystemException.get(paramORB, "orbd.repository");
/*     */ 
/*  78 */     File localFile = new File(paramFile, "servers.db");
/*  79 */     if (!localFile.exists()) {
/*  80 */       this.db = new RepositoryDB(localFile);
/*  81 */       this.db.flush();
/*     */     } else {
/*     */       try {
/*  84 */         FileInputStream localFileInputStream = new FileInputStream(localFile);
/*  85 */         ObjectInputStream localObjectInputStream = new ObjectInputStream(localFileInputStream);
/*  86 */         this.db = ((RepositoryDB)localObjectInputStream.readObject());
/*  87 */         localObjectInputStream.close();
/*     */       } catch (Exception localException) {
/*  89 */         throw this.wrapper.cannotReadRepositoryDb(localException);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  94 */     paramORB.connect(this);
/*     */   }
/*     */ 
/*     */   private String printServerDef(ServerDef paramServerDef)
/*     */   {
/*  99 */     return "ServerDef[applicationName=" + paramServerDef.applicationName + " serverName=" + paramServerDef.serverName + " serverClassPath=" + paramServerDef.serverClassPath + " serverArgs=" + paramServerDef.serverArgs + " serverVmArgs=" + paramServerDef.serverVmArgs + "]";
/*     */   }
/*     */ 
/*     */   public int registerServer(ServerDef paramServerDef, int paramInt)
/*     */     throws ServerAlreadyRegistered
/*     */   {
/* 111 */     DBServerDef localDBServerDef = null;
/*     */ 
/* 113 */     synchronized (this.db)
/*     */     {
/* 116 */       Enumeration localEnumeration = this.db.serverTable.elements();
/* 117 */       while (localEnumeration.hasMoreElements()) {
/* 118 */         localDBServerDef = (DBServerDef)localEnumeration.nextElement();
/* 119 */         if (paramServerDef.applicationName.equals(localDBServerDef.applicationName)) {
/* 120 */           if (this.debug) {
/* 121 */             System.out.println("RepositoryImpl: registerServer called to register ServerDef " + printServerDef(paramServerDef) + " with " + (paramInt == -1 ? "a new server Id" : new StringBuilder().append("server Id ").append(paramInt).toString()) + " FAILED because it is already registered.");
/*     */           }
/*     */ 
/* 129 */           throw new ServerAlreadyRegistered(localDBServerDef.id);
/*     */         }
/*     */       }
/*     */       int i;
/* 134 */       if (paramInt == -1)
/* 135 */         i = this.db.incrementServerIdCounter();
/*     */       else {
/* 137 */         i = paramInt;
/*     */       }
/*     */ 
/* 140 */       localDBServerDef = new DBServerDef(paramServerDef, i);
/* 141 */       this.db.serverTable.put(new Integer(i), localDBServerDef);
/* 142 */       this.db.flush();
/*     */ 
/* 144 */       if (this.debug) {
/* 145 */         if (paramInt == -1) {
/* 146 */           System.out.println("RepositoryImpl: registerServer called to register ServerDef " + printServerDef(paramServerDef) + " with new serverId " + i);
/*     */         }
/*     */         else
/*     */         {
/* 150 */           System.out.println("RepositoryImpl: registerServer called to register ServerDef " + printServerDef(paramServerDef) + " with assigned serverId " + i);
/*     */         }
/*     */       }
/*     */ 
/* 154 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int registerServer(ServerDef paramServerDef)
/*     */     throws ServerAlreadyRegistered, BadServerDefinition
/*     */   {
/* 162 */     LegacyServerSocketEndPointInfo localLegacyServerSocketEndPointInfo = this.orb.getLegacyServerSocketManager().legacyGetEndpoint("BOOT_NAMING");
/*     */ 
/* 165 */     int i = ((SocketOrChannelAcceptor)localLegacyServerSocketEndPointInfo).getServerSocket().getLocalPort();
/*     */ 
/* 167 */     ServerTableEntry localServerTableEntry = new ServerTableEntry(this.wrapper, -1, paramServerDef, i, "", true, this.debug);
/*     */ 
/* 170 */     switch (localServerTableEntry.verify()) {
/*     */     case 0:
/* 172 */       break;
/*     */     case 1:
/* 174 */       throw new BadServerDefinition("main class not found.");
/*     */     case 2:
/* 176 */       throw new BadServerDefinition("no main method found.");
/*     */     case 3:
/* 178 */       throw new BadServerDefinition("server application error.");
/*     */     default:
/* 180 */       throw new BadServerDefinition("unknown Exception.");
/*     */     }
/*     */ 
/* 183 */     return registerServer(paramServerDef, -1);
/*     */   }
/*     */ 
/*     */   public void unregisterServer(int paramInt) throws ServerNotRegistered
/*     */   {
/* 188 */     DBServerDef localDBServerDef = null;
/* 189 */     Integer localInteger = new Integer(paramInt);
/*     */ 
/* 191 */     synchronized (this.db)
/*     */     {
/* 194 */       localDBServerDef = (DBServerDef)this.db.serverTable.get(localInteger);
/* 195 */       if (localDBServerDef == null) {
/* 196 */         if (this.debug) {
/* 197 */           System.out.println("RepositoryImpl: unregisterServer for serverId " + paramInt + " called: server not registered");
/*     */         }
/*     */ 
/* 201 */         throw new ServerNotRegistered();
/*     */       }
/*     */ 
/* 205 */       this.db.serverTable.remove(localInteger);
/* 206 */       this.db.flush();
/*     */     }
/*     */ 
/* 209 */     if (this.debug)
/* 210 */       System.out.println("RepositoryImpl: unregisterServer for serverId " + paramInt + " called");
/*     */   }
/*     */ 
/*     */   private DBServerDef getDBServerDef(int paramInt)
/*     */     throws ServerNotRegistered
/*     */   {
/* 217 */     Integer localInteger = new Integer(paramInt);
/* 218 */     DBServerDef localDBServerDef = (DBServerDef)this.db.serverTable.get(localInteger);
/*     */ 
/* 220 */     if (localDBServerDef == null) {
/* 221 */       throw new ServerNotRegistered(paramInt);
/*     */     }
/* 223 */     return localDBServerDef;
/*     */   }
/*     */ 
/*     */   public ServerDef getServer(int paramInt) throws ServerNotRegistered
/*     */   {
/* 228 */     DBServerDef localDBServerDef = getDBServerDef(paramInt);
/*     */ 
/* 230 */     ServerDef localServerDef = new ServerDef(localDBServerDef.applicationName, localDBServerDef.name, localDBServerDef.classPath, localDBServerDef.args, localDBServerDef.vmArgs);
/*     */ 
/* 233 */     if (this.debug) {
/* 234 */       System.out.println("RepositoryImpl: getServer for serverId " + paramInt + " returns " + printServerDef(localServerDef));
/*     */     }
/*     */ 
/* 238 */     return localServerDef;
/*     */   }
/*     */ 
/*     */   public boolean isInstalled(int paramInt) throws ServerNotRegistered {
/* 242 */     DBServerDef localDBServerDef = getDBServerDef(paramInt);
/* 243 */     return localDBServerDef.isInstalled;
/*     */   }
/*     */ 
/*     */   public void install(int paramInt)
/*     */     throws ServerNotRegistered, ServerAlreadyInstalled
/*     */   {
/* 249 */     DBServerDef localDBServerDef = getDBServerDef(paramInt);
/*     */ 
/* 251 */     if (localDBServerDef.isInstalled) {
/* 252 */       throw new ServerAlreadyInstalled(paramInt);
/*     */     }
/* 254 */     localDBServerDef.isInstalled = true;
/* 255 */     this.db.flush();
/*     */   }
/*     */ 
/*     */   public void uninstall(int paramInt)
/*     */     throws ServerNotRegistered, ServerAlreadyUninstalled
/*     */   {
/* 262 */     DBServerDef localDBServerDef = getDBServerDef(paramInt);
/*     */ 
/* 264 */     if (!localDBServerDef.isInstalled) {
/* 265 */       throw new ServerAlreadyUninstalled(paramInt);
/*     */     }
/* 267 */     localDBServerDef.isInstalled = false;
/* 268 */     this.db.flush();
/*     */   }
/*     */ 
/*     */   public int[] listRegisteredServers()
/*     */   {
/* 273 */     synchronized (this.db) {
/* 274 */       int i = 0;
/*     */ 
/* 276 */       int[] arrayOfInt = new int[this.db.serverTable.size()];
/*     */ 
/* 278 */       Enumeration localEnumeration = this.db.serverTable.elements();
/*     */       Object localObject1;
/* 280 */       while (localEnumeration.hasMoreElements()) {
/* 281 */         localObject1 = (DBServerDef)localEnumeration.nextElement();
/* 282 */         arrayOfInt[(i++)] = ((DBServerDef)localObject1).id;
/*     */       }
/*     */ 
/* 285 */       if (this.debug) {
/* 286 */         localObject1 = new StringBuffer();
/* 287 */         for (int j = 0; j < arrayOfInt.length; j++) {
/* 288 */           ((StringBuffer)localObject1).append(' ');
/* 289 */           ((StringBuffer)localObject1).append(arrayOfInt[j]);
/*     */         }
/*     */ 
/* 292 */         System.out.println("RepositoryImpl: listRegisteredServers returns" + ((StringBuffer)localObject1).toString());
/*     */       }
/*     */ 
/* 297 */       return arrayOfInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getServerID(String paramString) throws ServerNotRegistered {
/* 302 */     synchronized (this.db) {
/* 303 */       int i = -1;
/*     */ 
/* 305 */       Enumeration localEnumeration = this.db.serverTable.keys();
/* 306 */       while (localEnumeration.hasMoreElements())
/*     */       {
/* 308 */         Integer localInteger = (Integer)localEnumeration.nextElement();
/* 309 */         DBServerDef localDBServerDef = (DBServerDef)this.db.serverTable.get(localInteger);
/*     */ 
/* 312 */         if (localDBServerDef.applicationName.equals(paramString)) {
/* 313 */           i = localInteger.intValue();
/* 314 */           break;
/*     */         }
/*     */       }
/*     */ 
/* 318 */       if (this.debug) {
/* 319 */         System.out.println("RepositoryImpl: getServerID for " + paramString + " is " + i);
/*     */       }
/*     */ 
/* 322 */       if (i == -1) {
/* 323 */         throw new ServerNotRegistered();
/*     */       }
/* 325 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] getApplicationNames()
/*     */   {
/* 331 */     synchronized (this.db) {
/* 332 */       Vector localVector = new Vector();
/* 333 */       Object localObject1 = this.db.serverTable.keys();
/* 334 */       while (((Enumeration)localObject1).hasMoreElements())
/*     */       {
/* 336 */         Integer localInteger = (Integer)((Enumeration)localObject1).nextElement();
/*     */ 
/* 338 */         DBServerDef localDBServerDef = (DBServerDef)this.db.serverTable.get(localInteger);
/*     */ 
/* 341 */         if (!localDBServerDef.applicationName.equals("")) {
/* 342 */           localVector.addElement(localDBServerDef.applicationName);
/*     */         }
/*     */       }
/* 345 */       localObject1 = new String[localVector.size()];
/* 346 */       for (int i = 0; i < localVector.size(); i++) {
/* 347 */         localObject1[i] = ((String)localVector.elementAt(i));
/*     */       }
/*     */ 
/* 350 */       if (this.debug) {
/* 351 */         StringBuffer localStringBuffer = new StringBuffer();
/* 352 */         for (int j = 0; j < localObject1.length; j++) {
/* 353 */           localStringBuffer.append(' ');
/* 354 */           localStringBuffer.append(localObject1[j]);
/*     */         }
/*     */ 
/* 357 */         System.out.println("RepositoryImpl: getApplicationNames returns " + localStringBuffer.toString());
/*     */       }
/*     */ 
/* 361 */       return localObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 369 */     boolean bool = false;
/* 370 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 371 */       if (paramArrayOfString[i].equals("-debug")) {
/* 372 */         bool = true;
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 377 */       Properties localProperties = new Properties();
/* 378 */       localProperties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
/*     */ 
/* 380 */       ORB localORB = (ORB)ORB.init(paramArrayOfString, localProperties);
/*     */ 
/* 383 */       String str = System.getProperty("com.sun.CORBA.activation.db", "db");
/*     */ 
/* 385 */       RepositoryImpl localRepositoryImpl = new RepositoryImpl(localORB, new File(str), bool);
/*     */ 
/* 389 */       localORB.run();
/*     */     } catch (Exception localException) {
/* 391 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   class DBServerDef
/*     */     implements Serializable
/*     */   {
/*     */     String applicationName;
/*     */     String name;
/*     */     String classPath;
/*     */     String args;
/*     */     String vmArgs;
/*     */     boolean isInstalled;
/*     */     int id;
/*     */ 
/*     */     public String toString()
/*     */     {
/* 447 */       return "DBServerDef(applicationName=" + this.applicationName + ", name=" + this.name + ", classPath=" + this.classPath + ", args=" + this.args + ", vmArgs=" + this.vmArgs + ", id=" + this.id + ", isInstalled=" + this.isInstalled + ")";
/*     */     }
/*     */ 
/*     */     DBServerDef(ServerDef paramInt, int arg3)
/*     */     {
/* 457 */       this.applicationName = paramInt.applicationName;
/* 458 */       this.name = paramInt.serverName;
/* 459 */       this.classPath = paramInt.serverClassPath;
/* 460 */       this.args = paramInt.serverArgs;
/* 461 */       this.vmArgs = paramInt.serverVmArgs;
/*     */       int i;
/* 462 */       this.id = i;
/* 463 */       this.isInstalled = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   class RepositoryDB
/*     */     implements Serializable
/*     */   {
/*     */     File db;
/*     */     Hashtable serverTable;
/*     */     Integer serverIdCounter;
/*     */ 
/*     */     RepositoryDB(File arg2)
/*     */     {
/*     */       Object localObject;
/* 413 */       this.db = localObject;
/*     */ 
/* 417 */       this.serverTable = new Hashtable(255);
/* 418 */       this.serverIdCounter = new Integer(256);
/*     */     }
/*     */ 
/*     */     int incrementServerIdCounter()
/*     */     {
/* 423 */       int i = this.serverIdCounter.intValue();
/* 424 */       this.serverIdCounter = new Integer(++i);
/*     */ 
/* 426 */       return i;
/*     */     }
/*     */ 
/*     */     void flush()
/*     */     {
/*     */       try {
/* 432 */         this.db.delete();
/* 433 */         FileOutputStream localFileOutputStream = new FileOutputStream(this.db);
/* 434 */         ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localFileOutputStream);
/* 435 */         localObjectOutputStream.writeObject(this);
/* 436 */         localObjectOutputStream.flush();
/* 437 */         localObjectOutputStream.close();
/*     */       } catch (Exception localException) {
/* 439 */         throw RepositoryImpl.this.wrapper.cannotWriteRepositoryDb(localException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.RepositoryImpl
 * JD-Core Version:    0.6.2
 */