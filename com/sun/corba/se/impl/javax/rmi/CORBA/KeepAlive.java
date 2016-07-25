/*     */ package com.sun.corba.se.impl.javax.rmi.CORBA;
/*     */ 
/*     */ class KeepAlive extends Thread
/*     */ {
/* 755 */   boolean quit = false;
/*     */ 
/*     */   public KeepAlive()
/*     */   {
/* 759 */     setDaemon(false);
/*     */   }
/*     */ 
/*     */   public synchronized void run()
/*     */   {
/* 764 */     while (!this.quit)
/*     */       try {
/* 766 */         wait();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/*     */   }
/*     */ 
/*     */   public synchronized void quit() {
/* 773 */     this.quit = true;
/* 774 */     notifyAll();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.javax.rmi.CORBA.KeepAlive
 * JD-Core Version:    0.6.2
 */