/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class ListORBs
/*     */   implements CommandHandler
/*     */ {
/*     */   static final int illegalServerId = -1;
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/* 925 */     return "orblist";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 929 */     if (!paramBoolean)
/* 930 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.orbidmap"));
/*     */     else
/* 932 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.orbidmap1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 941 */     int i = -1;
/*     */     try
/*     */     {
/* 944 */       if (paramArrayOfString.length == 2) {
/* 945 */         if (paramArrayOfString[0].equals("-serverid"))
/* 946 */           i = Integer.valueOf(paramArrayOfString[1]).intValue();
/* 947 */         else if (paramArrayOfString[0].equals("-applicationName")) {
/* 948 */           i = ServerTool.getServerIdForAlias(paramORB, paramArrayOfString[1]);
/*     */         }
/*     */       }
/*     */ 
/* 952 */       if (i == -1) {
/* 953 */         return true;
/*     */       }
/* 955 */       Activator localActivator = ActivatorHelper.narrow(paramORB.resolve_initial_references("ServerActivator"));
/*     */ 
/* 958 */       String[] arrayOfString = localActivator.getORBNames(i);
/*     */ 
/* 960 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.orbidmap2"));
/*     */ 
/* 962 */       for (int j = 0; j < arrayOfString.length; j++)
/* 963 */         paramPrintStream.println("\t " + arrayOfString[j]);
/*     */     }
/*     */     catch (ServerNotRegistered localServerNotRegistered) {
/* 966 */       paramPrintStream.println("\tno such server found.");
/*     */     } catch (Exception localException) {
/* 968 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 971 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ListORBs
 * JD-Core Version:    0.6.2
 */