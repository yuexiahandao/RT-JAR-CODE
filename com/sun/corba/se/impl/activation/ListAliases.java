/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Repository;
/*     */ import com.sun.corba.se.spi.activation.RepositoryHelper;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class ListAliases
/*     */   implements CommandHandler
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/* 755 */     return "listappnames";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 759 */     if (!paramBoolean)
/* 760 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.listappnames"));
/*     */     else
/* 762 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.listappnames1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/*     */     try
/*     */     {
/* 769 */       Repository localRepository = RepositoryHelper.narrow(paramORB.resolve_initial_references("ServerRepository"));
/*     */ 
/* 772 */       String[] arrayOfString = localRepository.getApplicationNames();
/*     */ 
/* 774 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.listappnames2"));
/* 775 */       paramPrintStream.println();
/* 776 */       for (int i = 0; i < arrayOfString.length; i++)
/* 777 */         paramPrintStream.println("\t" + arrayOfString[i]);
/*     */     } catch (Exception localException) {
/* 779 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 782 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ListAliases
 * JD-Core Version:    0.6.2
 */