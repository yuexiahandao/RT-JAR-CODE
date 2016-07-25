/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.EndPointInfo;
/*     */ import com.sun.corba.se.spi.activation.InvalidORBid;
/*     */ import com.sun.corba.se.spi.activation.Locator;
/*     */ import com.sun.corba.se.spi.activation.LocatorHelper;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;
/*     */ import com.sun.corba.se.spi.activation.ServerHeldDown;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class LocateServerForORB
/*     */   implements CommandHandler
/*     */ {
/*     */   static final int illegalServerId = -1;
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/* 487 */     return "locateperorb";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 491 */     if (!paramBoolean)
/* 492 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.locateorb"));
/*     */     else
/* 494 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.locateorb1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 502 */     int i = -1;
/*     */ 
/* 504 */     String str1 = "";
/*     */     try
/*     */     {
/* 509 */       int j = 0;
/* 510 */       while (j < paramArrayOfString.length)
/*     */       {
/* 512 */         String str2 = paramArrayOfString[(j++)];
/*     */ 
/* 514 */         if (str2.equals("-serverid")) {
/* 515 */           if (j < paramArrayOfString.length)
/* 516 */             i = Integer.valueOf(paramArrayOfString[(j++)]).intValue();
/*     */           else
/* 518 */             return true;
/* 519 */         } else if (str2.equals("-applicationName")) {
/* 520 */           if (j < paramArrayOfString.length)
/* 521 */             i = ServerTool.getServerIdForAlias(paramORB, paramArrayOfString[(j++)]);
/*     */           else
/* 523 */             return true;
/* 524 */         } else if ((str2.equals("-orbid")) && 
/* 525 */           (j < paramArrayOfString.length)) {
/* 526 */           str1 = paramArrayOfString[(j++)];
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 531 */       if (i == -1) {
/* 532 */         return true;
/*     */       }
/*     */ 
/* 536 */       Locator localLocator = LocatorHelper.narrow(paramORB.resolve_initial_references("ServerLocator"));
/*     */ 
/* 539 */       ServerLocationPerORB localServerLocationPerORB = localLocator.locateServerForORB(i, str1);
/*     */ 
/* 543 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.locateorb2", localServerLocationPerORB.hostname));
/* 544 */       int k = localServerLocationPerORB.ports.length;
/* 545 */       for (j = 0; j < k; j++) {
/* 546 */         EndPointInfo localEndPointInfo = localServerLocationPerORB.ports[j];
/* 547 */         paramPrintStream.println("\t\t" + localEndPointInfo.port + "\t\t" + localEndPointInfo.endpointType + "\t\t" + str1);
/*     */       }
/*     */     } catch (InvalidORBid localInvalidORBid) {
/* 550 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.nosuchorb"));
/*     */     } catch (ServerHeldDown localServerHeldDown) {
/* 552 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.helddown"));
/*     */     } catch (ServerNotRegistered localServerNotRegistered) {
/* 554 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
/*     */     } catch (Exception localException) {
/* 556 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 559 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.LocateServerForORB
 * JD-Core Version:    0.6.2
 */