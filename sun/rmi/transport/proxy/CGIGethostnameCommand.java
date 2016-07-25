/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ final class CGIGethostnameCommand
/*     */   implements CGICommandHandler
/*     */ {
/*     */   public String getName()
/*     */   {
/* 343 */     return "gethostname";
/*     */   }
/*     */ 
/*     */   public void execute(String paramString)
/*     */   {
/* 348 */     System.out.println("Status: 200 OK");
/* 349 */     System.out.println("Content-type: application/octet-stream");
/* 350 */     System.out.println("Content-length: " + CGIHandler.ServerName.length());
/*     */ 
/* 352 */     System.out.println("");
/* 353 */     System.out.print(CGIHandler.ServerName);
/* 354 */     System.out.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.CGIGethostnameCommand
 * JD-Core Version:    0.6.2
 */