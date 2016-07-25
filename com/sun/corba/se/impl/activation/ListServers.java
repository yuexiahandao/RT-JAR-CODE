/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Repository;
/*     */ import com.sun.corba.se.spi.activation.RepositoryHelper;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class ListServers
/*     */   implements CommandHandler
/*     */ {
/*     */   static final int illegalServerId = -1;
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/* 605 */     return "list";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 609 */     if (!paramBoolean)
/* 610 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.list"));
/*     */     else
/* 612 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.list1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 620 */     int i = -1;
/* 621 */     int j = 0;
/*     */ 
/* 625 */     j = paramArrayOfString.length != 0 ? 1 : 0;
/* 626 */     if ((paramArrayOfString.length == 2) && (paramArrayOfString[0].equals("-serverid"))) {
/* 627 */       i = Integer.valueOf(paramArrayOfString[1]).intValue();
/*     */     }
/* 629 */     if ((i == -1) && (j != 0)) {
/* 630 */       return true;
/*     */     }
/*     */     try
/*     */     {
/* 634 */       Repository localRepository = RepositoryHelper.narrow(paramORB.resolve_initial_references("ServerRepository"));
/*     */       ServerDef localServerDef;
/* 637 */       if (j != 0)
/*     */       {
/*     */         try {
/* 640 */           localServerDef = localRepository.getServer(i);
/* 641 */           paramPrintStream.println();
/* 642 */           printServerDef(localServerDef, i, paramPrintStream);
/* 643 */           paramPrintStream.println();
/*     */         } catch (ServerNotRegistered localServerNotRegistered1) {
/* 645 */           paramPrintStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
/*     */         }
/*     */       }
/*     */       else {
/* 649 */         int[] arrayOfInt = localRepository.listRegisteredServers();
/* 650 */         paramPrintStream.println(CorbaResourceUtil.getText("servertool.list2"));
/*     */ 
/* 652 */         sortServers(arrayOfInt);
/* 653 */         for (int k = 0; k < arrayOfInt.length; k++)
/*     */           try {
/* 655 */             localServerDef = localRepository.getServer(arrayOfInt[k]);
/* 656 */             paramPrintStream.println("\t   " + arrayOfInt[k] + "\t\t" + localServerDef.serverName + "\t\t" + localServerDef.applicationName);
/*     */           }
/*     */           catch (ServerNotRegistered localServerNotRegistered2)
/*     */           {
/*     */           }
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/* 664 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 667 */     return false;
/*     */   }
/*     */ 
/*     */   static void printServerDef(ServerDef paramServerDef, int paramInt, PrintStream paramPrintStream)
/*     */   {
/* 673 */     paramPrintStream.println(CorbaResourceUtil.getText("servertool.appname", paramServerDef.applicationName));
/* 674 */     paramPrintStream.println(CorbaResourceUtil.getText("servertool.name", paramServerDef.serverName));
/* 675 */     paramPrintStream.println(CorbaResourceUtil.getText("servertool.classpath", paramServerDef.serverClassPath));
/* 676 */     paramPrintStream.println(CorbaResourceUtil.getText("servertool.args", paramServerDef.serverArgs));
/* 677 */     paramPrintStream.println(CorbaResourceUtil.getText("servertool.vmargs", paramServerDef.serverVmArgs));
/* 678 */     paramPrintStream.println(CorbaResourceUtil.getText("servertool.serverid", paramInt));
/*     */   }
/*     */ 
/*     */   static void sortServers(int[] paramArrayOfInt)
/*     */   {
/* 687 */     int i = paramArrayOfInt.length;
/*     */ 
/* 690 */     for (int k = 0; k < i; k++)
/*     */     {
/* 692 */       int j = k;
/*     */ 
/* 694 */       for (int m = k + 1; m < i; m++) {
/* 695 */         if (paramArrayOfInt[m] < paramArrayOfInt[j]) j = m;
/*     */       }
/*     */ 
/* 698 */       if (j != k) {
/* 699 */         m = paramArrayOfInt[k];
/* 700 */         paramArrayOfInt[k] = paramArrayOfInt[j];
/* 701 */         paramArrayOfInt[j] = m;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ListServers
 * JD-Core Version:    0.6.2
 */