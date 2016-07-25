/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import com.sun.corba.se.spi.activation._ServerImplBase;
/*     */ import java.lang.reflect.Method;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ class ServerCallback extends _ServerImplBase
/*     */ {
/*     */   private ORB orb;
/*     */   private transient Method installMethod;
/*     */   private transient Method uninstallMethod;
/*     */   private transient Method shutdownMethod;
/*     */   private Object[] methodArgs;
/*     */ 
/*     */   ServerCallback(ORB paramORB, Method paramMethod1, Method paramMethod2, Method paramMethod3)
/*     */   {
/* 333 */     this.orb = paramORB;
/* 334 */     this.installMethod = paramMethod1;
/* 335 */     this.uninstallMethod = paramMethod2;
/* 336 */     this.shutdownMethod = paramMethod3;
/*     */ 
/* 338 */     paramORB.connect(this);
/*     */ 
/* 340 */     this.methodArgs = new Object[] { paramORB };
/*     */   }
/*     */ 
/*     */   private void invokeMethod(Method paramMethod)
/*     */   {
/* 345 */     if (paramMethod != null)
/*     */       try {
/* 347 */         paramMethod.invoke(null, this.methodArgs);
/*     */       } catch (Exception localException) {
/* 349 */         ServerMain.logError("could not invoke " + paramMethod.getName() + " method: " + localException.getMessage());
/*     */       }
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 357 */     ServerMain.logInformation("Shutdown starting");
/*     */ 
/* 359 */     invokeMethod(this.shutdownMethod);
/*     */ 
/* 361 */     this.orb.shutdown(true);
/*     */ 
/* 363 */     ServerMain.logTerminal("Shutdown completed", 0);
/*     */   }
/*     */ 
/*     */   public void install()
/*     */   {
/* 368 */     ServerMain.logInformation("Install starting");
/*     */ 
/* 370 */     invokeMethod(this.installMethod);
/*     */ 
/* 372 */     ServerMain.logInformation("Install completed");
/*     */   }
/*     */ 
/*     */   public void uninstall()
/*     */   {
/* 377 */     ServerMain.logInformation("uninstall starting");
/*     */ 
/* 379 */     invokeMethod(this.uninstallMethod);
/*     */ 
/* 381 */     ServerMain.logInformation("uninstall completed");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ServerCallback
 * JD-Core Version:    0.6.2
 */