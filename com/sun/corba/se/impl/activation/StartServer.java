/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyActive;
/*     */ import com.sun.corba.se.spi.activation.ServerHeldDown;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class StartServer
/*     */   implements CommandHandler
/*     */ {
/*     */   static final int illegalServerId = -1;
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/* 836 */     return "startup";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 840 */     if (!paramBoolean)
/* 841 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.startserver"));
/*     */     else
/* 843 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.startserver1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 851 */     int i = -1;
/*     */     try
/*     */     {
/* 855 */       if (paramArrayOfString.length == 2) {
/* 856 */         if (paramArrayOfString[0].equals("-serverid"))
/* 857 */           i = Integer.valueOf(paramArrayOfString[1]).intValue();
/* 858 */         else if (paramArrayOfString[0].equals("-applicationName"))
/* 859 */           i = ServerTool.getServerIdForAlias(paramORB, paramArrayOfString[1]);
/*     */       }
/* 861 */       if (i == -1) {
/* 862 */         return true;
/*     */       }
/*     */ 
/* 865 */       Activator localActivator = ActivatorHelper.narrow(paramORB.resolve_initial_references("ServerActivator"));
/*     */ 
/* 867 */       localActivator.activate(i);
/*     */ 
/* 869 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.startserver2"));
/*     */     } catch (ServerNotRegistered localServerNotRegistered) {
/* 871 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
/*     */     } catch (ServerAlreadyActive localServerAlreadyActive) {
/* 873 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.serverup"));
/*     */     } catch (ServerHeldDown localServerHeldDown) {
/* 875 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.helddown"));
/*     */     } catch (Exception localException) {
/* 877 */       localException.printStackTrace();
/*     */     }
/* 879 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.StartServer
 * JD-Core Version:    0.6.2
 */