/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import com.sun.corba.se.spi.activation.Repository;
/*     */ import com.sun.corba.se.spi.activation.RepositoryHelper;
/*     */ import com.sun.corba.se.spi.activation.ServerHeldDown;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class UnRegisterServer
/*     */   implements CommandHandler
/*     */ {
/*     */   static final int illegalServerId = -1;
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/* 355 */     return "unregister";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 359 */     if (!paramBoolean)
/* 360 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.unregister"));
/*     */     else
/* 362 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.unregister1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 370 */     int i = -1;
/*     */     try
/*     */     {
/* 373 */       if (paramArrayOfString.length == 2) {
/* 374 */         if (paramArrayOfString[0].equals("-serverid"))
/* 375 */           i = Integer.valueOf(paramArrayOfString[1]).intValue();
/* 376 */         else if (paramArrayOfString[0].equals("-applicationName")) {
/* 377 */           i = ServerTool.getServerIdForAlias(paramORB, paramArrayOfString[1]);
/*     */         }
/*     */       }
/*     */ 
/* 381 */       if (i == -1) {
/* 382 */         return true;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 387 */         Activator localActivator = ActivatorHelper.narrow(paramORB.resolve_initial_references("ServerActivator"));
/*     */ 
/* 389 */         localActivator.uninstall(i);
/*     */       }
/*     */       catch (ServerHeldDown localServerHeldDown) {
/*     */       }
/* 393 */       Repository localRepository = RepositoryHelper.narrow(paramORB.resolve_initial_references("ServerRepository"));
/*     */ 
/* 395 */       localRepository.unregisterServer(i);
/*     */ 
/* 398 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.unregister2"));
/*     */     } catch (ServerNotRegistered localServerNotRegistered) {
/* 400 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
/*     */     } catch (Exception localException) {
/* 402 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 405 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.UnRegisterServer
 * JD-Core Version:    0.6.2
 */