/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.CorbaResourceUtil;
/*     */ import com.sun.corba.se.spi.activation.Activator;
/*     */ import com.sun.corba.se.spi.activation.ActivatorHelper;
/*     */ import com.sun.corba.se.spi.activation.BadServerDefinition;
/*     */ import com.sun.corba.se.spi.activation.Repository;
/*     */ import com.sun.corba.se.spi.activation.RepositoryHelper;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyActive;
/*     */ import com.sun.corba.se.spi.activation.ServerAlreadyRegistered;
/*     */ import com.sun.corba.se.spi.activation.ServerHeldDown;
/*     */ import com.sun.corba.se.spi.activation.ServerNotRegistered;
/*     */ import java.io.PrintStream;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class RegisterServer
/*     */   implements CommandHandler
/*     */ {
/*     */   public String getCommandName()
/*     */   {
/* 264 */     return "register";
/*     */   }
/*     */ 
/*     */   public void printCommandHelp(PrintStream paramPrintStream, boolean paramBoolean) {
/* 268 */     if (!paramBoolean)
/* 269 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.register"));
/*     */     else
/* 271 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.register1"));
/*     */   }
/*     */ 
/*     */   public boolean processCommand(String[] paramArrayOfString, ORB paramORB, PrintStream paramPrintStream)
/*     */   {
/* 277 */     int i = 0;
/* 278 */     String str1 = "";
/* 279 */     String str2 = "";
/* 280 */     String str3 = "";
/* 281 */     String str4 = "";
/* 282 */     String str5 = "";
/* 283 */     int j = 0;
/*     */ 
/* 287 */     while (i < paramArrayOfString.length)
/*     */     {
/* 289 */       String str6 = paramArrayOfString[(i++)];
/*     */ 
/* 291 */       if (str6.equals("-server")) {
/* 292 */         if (i < paramArrayOfString.length) str2 = paramArrayOfString[(i++)]; else
/* 293 */           return true;
/* 294 */       } else if (str6.equals("-applicationName")) {
/* 295 */         if (i < paramArrayOfString.length) str1 = paramArrayOfString[(i++)]; else
/* 296 */           return true;
/* 297 */       } else if (str6.equals("-classpath")) {
/* 298 */         if (i < paramArrayOfString.length) str3 = paramArrayOfString[(i++)]; else
/* 299 */           return true;
/* 300 */       } else if (str6.equals("-args")) {
/* 301 */         while ((i < paramArrayOfString.length) && (!paramArrayOfString[i].equals("-vmargs"))) {
/* 302 */           str4 = str4 + " " + paramArrayOfString[i];
/*     */ 
/* 304 */           i++;
/*     */         }
/* 306 */         if (str4.equals("")) return true; 
/*     */       }
/* 307 */       else if (str6.equals("-vmargs")) {
/* 308 */         while ((i < paramArrayOfString.length) && (!paramArrayOfString[i].equals("-args"))) {
/* 309 */           str5 = str5 + " " + paramArrayOfString[i];
/*     */ 
/* 311 */           i++;
/*     */         }
/* 313 */         if (str5.equals("")) return true; 
/*     */       } else { return true; }
/*     */ 
/*     */     }
/*     */ 
/* 318 */     if (str2.equals("")) return true;
/*     */ 
/*     */     try
/*     */     {
/* 323 */       Repository localRepository = RepositoryHelper.narrow(paramORB.resolve_initial_references("ServerRepository"));
/*     */ 
/* 326 */       ServerDef localServerDef = new ServerDef(str1, str2, str3, str4, str5);
/* 327 */       j = localRepository.registerServer(localServerDef);
/*     */ 
/* 330 */       Activator localActivator = ActivatorHelper.narrow(paramORB.resolve_initial_references("ServerActivator"));
/*     */ 
/* 332 */       localActivator.activate(j);
/* 333 */       localActivator.install(j);
/*     */ 
/* 336 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.register2", j));
/*     */     } catch (ServerNotRegistered localServerNotRegistered) {
/*     */     } catch (ServerAlreadyActive localServerAlreadyActive) {
/*     */     } catch (ServerHeldDown localServerHeldDown) {
/* 340 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.register3", j));
/*     */     } catch (ServerAlreadyRegistered localServerAlreadyRegistered) {
/* 342 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.register4", j));
/*     */     } catch (BadServerDefinition localBadServerDefinition) {
/* 344 */       paramPrintStream.println(CorbaResourceUtil.getText("servertool.baddef", localBadServerDefinition.reason));
/*     */     } catch (Exception localException) {
/* 346 */       localException.printStackTrace();
/*     */     }
/*     */ 
/* 349 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.RegisterServer
 * JD-Core Version:    0.6.2
 */