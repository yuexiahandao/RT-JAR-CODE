/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import com.sun.corba.se.spi.activation.Repository;
/*     */ import com.sun.corba.se.spi.activation.RepositoryHelper;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class ListActiveServers
/*     */   implements CommandHandler
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/* 709 */     return "listactive";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 713 */     if (!paramBoolean)
/* 714 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.listactive"));
/*     */     else
/* 716 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.listactive1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/*     */     try
/*     */     {
/* 726 */       Repository localRepository = RepositoryHelper.narrow(paramORB.resolve_initial_references("ServerRepository"));
/*     */ 
/* 729 */       Activator localActivator = ActivatorHelper.narrow(paramORB.resolve_initial_references("ServerActivator"));
/*     */ 
/* 732 */       int[] arrayOfInt = localActivator.getActiveServers();
/*     */ 
/* 734 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.list2"));
/*     */ 
/* 736 */       ListServers.sortServers(arrayOfInt);
/* 737 */       for (int i = 0; i < arrayOfInt.length; i++)
/*     */         try {
/* 739 */           ServerDef localServerDef = localRepository.getServer(arrayOfInt[i]);
/* 740 */           paramPrintStream.println("\t   " + arrayOfInt[i] + "\t\t" + localServerDef.serverName + "\t\t" + localServerDef.applicationName);
/*     */         }
/*     */         catch (ServerNotRegistered localServerNotRegistered) {
/*     */         }
/*     */     }
/*     */     catch (Exception localException) {
/* 746 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 749 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ListActiveServers
 * JD-Core Version:    0.6.2
 */