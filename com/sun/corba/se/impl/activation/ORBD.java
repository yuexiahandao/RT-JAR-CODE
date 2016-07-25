/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.legacy.connection.SocketFactoryAcceptorImpl;
/*     */ import com.sun.corba.se.impl.naming.cosnaming.TransientNameService;
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.impl.transport.SocketOrChannelAcceptorImpl;
/*     */ import com.sun.corba.se.pept.transport.Acceptor;
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import com.sun.corba.se.spi.activation.Locator;
/*     */ import com.sun.corba.se.spi.activation.LocatorHelper;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.transport.CorbaTransportManager;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.COMM_FAILURE;
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ 
/*     */ public class ORBD
/*     */ {
/*     */   private int initSvcPort;
/*     */   protected File dbDir;
/*     */   private String dbDirName;
/*     */   protected Locator locator;
/*     */   protected Activator activator;
/*     */   protected RepositoryImpl repository;
/* 351 */   private static String[][] orbServers = { { "" } };
/*     */ 
/*     */   protected void initializeBootNaming(ORB paramORB)
/*     */   {
/*  71 */     this.initSvcPort = paramORB.getORBData().getORBInitialPort();
/*     */     Object localObject;
/*  75 */     if (paramORB.getORBData().getLegacySocketFactory() == null) {
/*  76 */       localObject = new SocketOrChannelAcceptorImpl(paramORB, this.initSvcPort, "BOOT_NAMING", "IIOP_CLEAR_TEXT");
/*     */     }
/*     */     else
/*     */     {
/*  83 */       localObject = new SocketFactoryAcceptorImpl(paramORB, this.initSvcPort, "BOOT_NAMING", "IIOP_CLEAR_TEXT");
/*     */     }
/*     */ 
/*  90 */     paramORB.getCorbaTransportManager().registerAcceptor((Acceptor)localObject);
/*     */   }
/*     */ 
/*     */   protected ORB createORB(String[] paramArrayOfString)
/*     */   {
/*  95 */     Properties localProperties = System.getProperties();
/*     */ 
/* 101 */     localProperties.put("com.sun.CORBA.POA.ORBServerId", "1000");
/* 102 */     localProperties.put("com.sun.CORBA.POA.ORBPersistentServerPort", localProperties.getProperty("com.sun.CORBA.activation.Port", Integer.toString(1049)));
/*     */ 
/* 109 */     localProperties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
/*     */ 
/* 112 */     return (ORB)ORB.init(paramArrayOfString, localProperties);
/*     */   }
/*     */ 
/*     */   private void run(String[] paramArrayOfString)
/*     */   {
/*     */     try
/*     */     {
/* 120 */       processArgs(paramArrayOfString);
/*     */ 
/* 122 */       ORB localORB = createORB(paramArrayOfString);
/*     */ 
/* 124 */       if (localORB.orbdDebugFlag) {
/* 125 */         System.out.println("ORBD begins initialization.");
/*     */       }
/* 127 */       boolean bool = createSystemDirs("orb.db");
/*     */ 
/* 129 */       startActivationObjects(localORB);
/*     */ 
/* 131 */       if (bool) {
/* 132 */         installOrbServers(getRepository(), getActivator());
/*     */       }
/* 134 */       if (localORB.orbdDebugFlag) {
/* 135 */         System.out.println("ORBD is ready.");
/* 136 */         System.out.println("ORBD serverid: " + System.getProperty("com.sun.CORBA.POA.ORBServerId"));
/*     */ 
/* 138 */         System.out.println("activation dbdir: " + System.getProperty("com.sun.CORBA.activation.DbDir"));
/*     */ 
/* 140 */         System.out.println("activation port: " + System.getProperty("com.sun.CORBA.activation.Port"));
/*     */ 
/* 143 */         localObject = System.getProperty("com.sun.CORBA.activation.ServerPollingTime");
/*     */ 
/* 145 */         if (localObject == null) {
/* 146 */           localObject = Integer.toString(1000);
/*     */         }
/*     */ 
/* 149 */         System.out.println("activation Server Polling Time: " + (String)localObject + " milli-seconds ");
/*     */ 
/* 152 */         String str = System.getProperty("com.sun.CORBA.activation.ServerStartupDelay");
/*     */ 
/* 154 */         if (str == null) {
/* 155 */           str = Integer.toString(1000);
/*     */         }
/*     */ 
/* 158 */         System.out.println("activation Server Startup Delay: " + str + " milli-seconds ");
/*     */       }
/*     */ 
/* 163 */       Object localObject = new NameServiceStartThread(localORB, this.dbDir);
/*     */ 
/* 165 */       ((NameServiceStartThread)localObject).start();
/*     */ 
/* 167 */       localORB.run();
/*     */     } catch (COMM_FAILURE localCOMM_FAILURE) {
/* 169 */       System.out.println(CorbaResourceUtil.getText("orbd.commfailure"));
/* 170 */       System.out.println(localCOMM_FAILURE);
/* 171 */       localCOMM_FAILURE.printStackTrace();
/*     */     } catch (INTERNAL localINTERNAL) {
/* 173 */       System.out.println(CorbaResourceUtil.getText("orbd.internalexception"));
/*     */ 
/* 175 */       System.out.println(localINTERNAL);
/* 176 */       localINTERNAL.printStackTrace();
/*     */     } catch (Exception localException) {
/* 178 */       System.out.println(CorbaResourceUtil.getText("orbd.usage", "orbd"));
/*     */ 
/* 180 */       System.out.println(localException);
/* 181 */       localException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processArgs(String[] paramArrayOfString)
/*     */   {
/* 187 */     Properties localProperties = System.getProperties();
/* 188 */     for (int i = 0; i < paramArrayOfString.length; i++)
/* 189 */       if (paramArrayOfString[i].equals("-port")) {
/* 190 */         if (i + 1 < paramArrayOfString.length)
/* 191 */           localProperties.put("com.sun.CORBA.activation.Port", paramArrayOfString[(++i)]);
/*     */         else {
/* 193 */           System.out.println(CorbaResourceUtil.getText("orbd.usage", "orbd"));
/*     */         }
/*     */       }
/* 196 */       else if (paramArrayOfString[i].equals("-defaultdb")) {
/* 197 */         if (i + 1 < paramArrayOfString.length)
/* 198 */           localProperties.put("com.sun.CORBA.activation.DbDir", paramArrayOfString[(++i)]);
/*     */         else {
/* 200 */           System.out.println(CorbaResourceUtil.getText("orbd.usage", "orbd"));
/*     */         }
/*     */       }
/* 203 */       else if (paramArrayOfString[i].equals("-serverid")) {
/* 204 */         if (i + 1 < paramArrayOfString.length)
/* 205 */           localProperties.put("com.sun.CORBA.POA.ORBServerId", paramArrayOfString[(++i)]);
/*     */         else {
/* 207 */           System.out.println(CorbaResourceUtil.getText("orbd.usage", "orbd"));
/*     */         }
/*     */       }
/* 210 */       else if (paramArrayOfString[i].equals("-serverPollingTime")) {
/* 211 */         if (i + 1 < paramArrayOfString.length)
/* 212 */           localProperties.put("com.sun.CORBA.activation.ServerPollingTime", paramArrayOfString[(++i)]);
/*     */         else {
/* 214 */           System.out.println(CorbaResourceUtil.getText("orbd.usage", "orbd"));
/*     */         }
/*     */       }
/* 217 */       else if (paramArrayOfString[i].equals("-serverStartupDelay"))
/* 218 */         if (i + 1 < paramArrayOfString.length)
/* 219 */           localProperties.put("com.sun.CORBA.activation.ServerStartupDelay", paramArrayOfString[(++i)]);
/*     */         else
/* 221 */           System.out.println(CorbaResourceUtil.getText("orbd.usage", "orbd"));
/*     */   }
/*     */ 
/*     */   protected boolean createSystemDirs(String paramString)
/*     */   {
/* 234 */     boolean bool = false;
/* 235 */     Properties localProperties = System.getProperties();
/* 236 */     String str = localProperties.getProperty("file.separator");
/*     */ 
/* 239 */     this.dbDir = new File(localProperties.getProperty("com.sun.CORBA.activation.DbDir", localProperties.getProperty("user.dir") + str + paramString));
/*     */ 
/* 243 */     this.dbDirName = this.dbDir.getAbsolutePath();
/* 244 */     localProperties.put("com.sun.CORBA.activation.DbDir", this.dbDirName);
/* 245 */     if (!this.dbDir.exists()) {
/* 246 */       this.dbDir.mkdir();
/* 247 */       bool = true;
/*     */     }
/*     */ 
/* 250 */     File localFile = new File(this.dbDir, "logs");
/* 251 */     if (!localFile.exists()) localFile.mkdir();
/*     */ 
/* 253 */     return bool;
/*     */   }
/*     */ 
/*     */   protected File getDbDir()
/*     */   {
/* 259 */     return this.dbDir;
/*     */   }
/*     */ 
/*     */   protected String getDbDirName()
/*     */   {
/* 265 */     return this.dbDirName;
/*     */   }
/*     */ 
/*     */   protected void startActivationObjects(ORB paramORB)
/*     */     throws Exception
/*     */   {
/* 271 */     initializeBootNaming(paramORB);
/*     */ 
/* 274 */     this.repository = new RepositoryImpl(paramORB, this.dbDir, paramORB.orbdDebugFlag);
/* 275 */     paramORB.register_initial_reference("ServerRepository", this.repository);
/*     */ 
/* 278 */     ServerManagerImpl localServerManagerImpl = new ServerManagerImpl(paramORB, paramORB.getCorbaTransportManager(), this.repository, getDbDirName(), paramORB.orbdDebugFlag);
/*     */ 
/* 285 */     this.locator = LocatorHelper.narrow(localServerManagerImpl);
/* 286 */     paramORB.register_initial_reference("ServerLocator", this.locator);
/*     */ 
/* 288 */     this.activator = ActivatorHelper.narrow(localServerManagerImpl);
/* 289 */     paramORB.register_initial_reference("ServerActivator", this.activator);
/*     */ 
/* 292 */     TransientNameService localTransientNameService = new TransientNameService(paramORB, "TNameService");
/*     */   }
/*     */ 
/*     */   protected Locator getLocator()
/*     */   {
/* 299 */     return this.locator;
/*     */   }
/*     */ 
/*     */   protected Activator getActivator()
/*     */   {
/* 305 */     return this.activator;
/*     */   }
/*     */ 
/*     */   protected RepositoryImpl getRepository()
/*     */   {
/* 311 */     return this.repository;
/*     */   }
/*     */ 
/*     */   protected void installOrbServers(RepositoryImpl paramRepositoryImpl, Activator paramActivator)
/*     */   {
/* 325 */     for (int j = 0; j < orbServers.length; j++)
/*     */       try {
/* 327 */         String[] arrayOfString = orbServers[j];
/* 328 */         ServerDef localServerDef = new ServerDef(arrayOfString[1], arrayOfString[2], arrayOfString[3], arrayOfString[4], arrayOfString[5]);
/*     */ 
/* 331 */         int i = Integer.valueOf(orbServers[j][0]).intValue();
/*     */ 
/* 333 */         paramRepositoryImpl.registerServer(localServerDef, i);
/*     */ 
/* 335 */         paramActivator.activate(i);
/*     */       }
/*     */       catch (Exception localException) {
/*     */       }
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString) {
/* 342 */     ORBD localORBD = new ORBD();
/* 343 */     localORBD.run(paramArrayOfString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ORBD
 * JD-Core Version:    0.6.2
 */