/*     */ package sun.rmi.transport.tcp;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import sun.rmi.runtime.NewThreadAction;
/*     */ import sun.rmi.transport.Connection;
/*     */ 
/*     */ class ConnectionAcceptor
/*     */   implements Runnable
/*     */ {
/*     */   private TCPTransport transport;
/* 483 */   private List<Connection> queue = new ArrayList();
/*     */ 
/* 486 */   private static int threadNum = 0;
/*     */ 
/*     */   public ConnectionAcceptor(TCPTransport paramTCPTransport)
/*     */   {
/* 493 */     this.transport = paramTCPTransport;
/*     */   }
/*     */ 
/*     */   public void startNewAcceptor()
/*     */   {
/* 500 */     Thread localThread = (Thread)AccessController.doPrivileged(new NewThreadAction(this, "Multiplex Accept-" + ++threadNum, true));
/*     */ 
/* 504 */     localThread.start();
/*     */   }
/*     */ 
/*     */   public void accept(Connection paramConnection)
/*     */   {
/* 511 */     synchronized (this.queue) {
/* 512 */       this.queue.add(paramConnection);
/* 513 */       this.queue.notify();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     Connection localConnection;
/* 523 */     synchronized (this.queue) {
/* 524 */       while (this.queue.size() == 0)
/*     */         try {
/* 526 */           this.queue.wait();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/*     */         }
/* 530 */       startNewAcceptor();
/* 531 */       localConnection = (Connection)this.queue.remove(0);
/*     */     }
/*     */ 
/* 534 */     this.transport.handleMessages(localConnection, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.tcp.ConnectionAcceptor
 * JD-Core Version:    0.6.2
 */