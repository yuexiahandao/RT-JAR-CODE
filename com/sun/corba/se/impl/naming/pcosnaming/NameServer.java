/*    */ package com.sun.corba.se.impl.naming.pcosnaming;
/*    */ 
/*    */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*    */ import com.sun.corba.se.spi.activation.InitialNameService;
/*    */ import com.sun.corba.se.spi.activation.InitialNameServiceHelper;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Properties;
/*    */ import org.omg.CosNaming.NamingContext;
/*    */ 
/*    */ public class NameServer
/*    */ {
/*    */   private com.sun.corba.se.spi.orb.ORB orb;
/*    */   private File dbDir;
/*    */   private static final String dbName = "names.db";
/*    */ 
/*    */   public static void main(String[] paramArrayOfString)
/*    */   {
/* 57 */     NameServer localNameServer = new NameServer(paramArrayOfString);
/* 58 */     localNameServer.run();
/*    */   }
/*    */ 
/*    */   protected NameServer(String[] paramArrayOfString)
/*    */   {
/* 64 */     Properties localProperties = System.getProperties();
/* 65 */     localProperties.put("com.sun.CORBA.POA.ORBServerId", "1000");
/* 66 */     localProperties.put("org.omg.CORBA.ORBClass", "com.sun.corba.se.impl.orb.ORBImpl");
/*    */ 
/* 68 */     this.orb = ((com.sun.corba.se.spi.orb.ORB)org.omg.CORBA.ORB.init(paramArrayOfString, localProperties));
/*    */ 
/* 71 */     String str = localProperties.getProperty("com.sun.CORBA.activation.DbDir") + localProperties.getProperty("file.separator") + "names.db" + localProperties.getProperty("file.separator");
/*    */ 
/* 75 */     this.dbDir = new File(str);
/* 76 */     if (!this.dbDir.exists()) this.dbDir.mkdir();
/*    */   }
/*    */ 
/*    */   protected void run()
/*    */   {
/*    */     try
/*    */     {
/* 84 */       NameService localNameService = new NameService(this.orb, this.dbDir);
/*    */ 
/* 87 */       NamingContext localNamingContext = localNameService.initialNamingContext();
/* 88 */       InitialNameService localInitialNameService = InitialNameServiceHelper.narrow(this.orb.resolve_initial_references("InitialNameService"));
/*    */ 
/* 91 */       localInitialNameService.bind("NameService", localNamingContext, true);
/* 92 */       System.out.println(CorbaResourceUtil.getText("pnameserv.success"));
/*    */ 
/* 95 */       this.orb.run();
/*    */     }
/*    */     catch (Exception localException)
/*    */     {
/* 99 */       localException.printStackTrace(System.err);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.pcosnaming.NameServer
 * JD-Core Version:    0.6.2
 */