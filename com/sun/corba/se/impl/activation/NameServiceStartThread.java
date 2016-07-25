/*    */ package com.sun.corba.se.impl.activation;
/*    */ 
/*    */ import com.sun.corba.se.impl.naming.pcosnaming.NameService;
/*    */ import com.sun.corba.se.spi.orb.ORB;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import org.omg.CosNaming.NamingContext;
/*    */ 
/*    */ public class NameServiceStartThread extends Thread
/*    */ {
/*    */   private ORB orb;
/*    */   private File dbDir;
/*    */ 
/*    */   public NameServiceStartThread(ORB paramORB, File paramFile)
/*    */   {
/* 45 */     this.orb = paramORB;
/* 46 */     this.dbDir = paramFile;
/*    */   }
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 53 */       NameService localNameService = new NameService(this.orb, this.dbDir);
/* 54 */       NamingContext localNamingContext = localNameService.initialNamingContext();
/* 55 */       this.orb.register_initial_reference("NameService", localNamingContext);
/*    */     }
/*    */     catch (Exception localException) {
/* 58 */       System.err.println("NameService did not start successfully");
/*    */ 
/* 60 */       localException.printStackTrace();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.NameServiceStartThread
 * JD-Core Version:    0.6.2
 */