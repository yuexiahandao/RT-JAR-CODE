/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Repository;
/*     */ import com.sun.corba.se.spi.activation.RepositoryHelper;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class GetServerID
/*     */   implements CommandHandler
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/* 565 */     return "getserverid";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 569 */     if (!paramBoolean)
/* 570 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.getserverid"));
/*     */     else
/* 572 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.getserverid1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 578 */     if ((paramArrayOfString.length == 2) && (paramArrayOfString[0].equals("-applicationName"))) {
/* 579 */       String str = paramArrayOfString[1];
/*     */       try
/*     */       {
/* 582 */         Repository localRepository = RepositoryHelper.narrow(paramORB.resolve_initial_references("ServerRepository"));
/*     */         try
/*     */         {
/* 586 */           int i = localRepository.getServerID(str);
/* 587 */           paramPrintStream.println();
/* 588 */           paramPrintStream.println(CorbaResourceUtil.getText("servertool.getserverid2", str, Integer.toString(i)));
/* 589 */           paramPrintStream.println();
/*     */         } catch (ServerNotRegistered localServerNotRegistered) {
/* 591 */           paramPrintStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
/*     */         }
/*     */       } catch (Exception localException) {
/* 594 */         localException.printStackTrace();
/*     */       }
/*     */ 
/* 597 */       return false;
/*     */     }
/* 599 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.GetServerID
 * JD-Core Version:    0.6.2
 */