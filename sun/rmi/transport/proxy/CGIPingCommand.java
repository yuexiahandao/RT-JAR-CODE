/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ final class CGIPingCommand
/*     */   implements CGICommandHandler
/*     */ {
/*     */   public String getName()
/*     */   {
/* 365 */     return "ping";
/*     */   }
/*     */ 
/*     */   public void execute(String paramString)
/*     */   {
/* 370 */     System.out.println("Status: 200 OK");
/* 371 */     System.out.println("Content-type: application/octet-stream");
/* 372 */     System.out.println("Content-length: 0");
/* 373 */     System.out.println("");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.CGIPingCommand
 * JD-Core Version:    0.6.2
 */