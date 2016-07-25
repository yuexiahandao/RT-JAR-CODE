/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import com.sun.corba.se.spi.activation.ServerNotActive;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class ShutdownServer
/*     */   implements CommandHandler
/*     */ {
/*     */   static final int illegalServerId = -1;
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/* 788 */     return "shutdown";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 792 */     if (!paramBoolean)
/* 793 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.shutdown"));
/*     */     else
/* 795 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.shutdown1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 803 */     int i = -1;
/*     */     try
/*     */     {
/* 807 */       if (paramArrayOfString.length == 2) {
/* 808 */         if (paramArrayOfString[0].equals("-serverid"))
/* 809 */           i = Integer.valueOf(paramArrayOfString[1]).intValue();
/* 810 */         else if (paramArrayOfString[0].equals("-applicationName"))
/* 811 */           i = ServerTool.getServerIdForAlias(paramORB, paramArrayOfString[1]);
/*     */       }
/* 813 */       if (i == -1) {
/* 814 */         return true;
/*     */       }
/*     */ 
/* 817 */       Activator localActivator = ActivatorHelper.narrow(paramORB.resolve_initial_references("ServerActivator"));
/*     */ 
/* 819 */       localActivator.shutdown(i);
/*     */ 
/* 821 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.shutdown2"));
/*     */     } catch (ServerNotActive localServerNotActive) {
/* 823 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.servernotrunning"));
/*     */     } catch (ServerNotRegistered localServerNotRegistered) {
/* 825 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
/*     */     } catch (Exception localException) {
/* 827 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 830 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ShutdownServer
 * JD-Core Version:    0.6.2
 */