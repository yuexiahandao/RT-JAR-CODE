/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class Quit
/*     */   implements CommandHandler
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/* 885 */     return "quit";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 889 */     if (!paramBoolean)
/* 890 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.quit"));
/*     */     else
/* 892 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.quit1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 898 */     System.exit(0);
/*     */ 
/* 900 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.Quit
 * JD-Core Version:    0.6.2
 */