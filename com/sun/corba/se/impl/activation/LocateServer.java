/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Locator;
/*     */ import com.sun.corba.se.spi.activation.LocatorHelper;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
/*     */ import com.sun.corba.se.spi.activation.NoSuchEndPoint;
/*     */ import com.sun.corba.se.spi.activation.ORBPortInfo;
/*     */ import com.sun.corba.se.spi.activation.ServerHeldDown;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class LocateServer
/*     */   implements CommandHandler
/*     */ {
/*     */   static final int illegalServerId = -1;
/*     */ 
/*     */   public String getCommandName()
/*     */   {
/* 411 */     return "locate";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 415 */     if (!paramBoolean)
/* 416 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.locate"));
/*     */     else
/* 418 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.locate1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 426 */     int i = -1;
/*     */ 
/* 428 */     String str1 = "IIOP_CLEAR_TEXT";
/*     */     try
/*     */     {
/* 433 */       int j = 0;
/* 434 */       while (j < paramArrayOfString.length)
/*     */       {
/* 436 */         String str2 = paramArrayOfString[(j++)];
/*     */ 
/* 438 */         if (str2.equals("-serverid")) {
/* 439 */           if (j < paramArrayOfString.length)
/* 440 */             i = Integer.valueOf(paramArrayOfString[(j++)]).intValue();
/*     */           else
/* 442 */             return true;
/* 443 */         } else if (str2.equals("-applicationName")) {
/* 444 */           if (j < paramArrayOfString.length)
/* 445 */             i = ServerTool.getServerIdForAlias(paramORB, paramArrayOfString[(j++)]);
/*     */           else
/* 447 */             return true;
/* 448 */         } else if ((str2.equals("-endpointType")) && 
/* 449 */           (j < paramArrayOfString.length)) {
/* 450 */           str1 = paramArrayOfString[(j++)];
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 455 */       if (i == -1) {
/* 456 */         return true;
/*     */       }
/*     */ 
/* 460 */       Locator localLocator = LocatorHelper.narrow(paramORB.resolve_initial_references("ServerLocator"));
/*     */ 
/* 463 */       ServerLocation localServerLocation = localLocator.locateServer(i, str1);
/*     */ 
/* 466 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.locate2", localServerLocation.hostname));
/* 467 */       int k = localServerLocation.ports.length;
/* 468 */       for (j = 0; j < k; j++) {
/* 469 */         ORBPortInfo localORBPortInfo = localServerLocation.ports[j];
/* 470 */         paramPrintStream.println("\t\t" + localORBPortInfo.port + "\t\t" + str1 + "\t\t" + localORBPortInfo.orbId);
/*     */       }
/*     */     } catch (NoSuchEndPoint localNoSuchEndPoint) {
/*     */     } catch (ServerHeldDown localServerHeldDown) {
/* 474 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.helddown"));
/*     */     } catch (ServerNotRegistered localServerNotRegistered) {
/* 476 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.nosuchserver"));
/*     */     } catch (Exception localException) {
/* 478 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 481 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.LocateServer
 * JD-Core Version:    0.6.2
 */