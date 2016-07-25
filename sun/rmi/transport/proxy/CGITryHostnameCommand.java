/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ final class CGITryHostnameCommand
/*     */   implements CGICommandHandler
/*     */ {
/*     */   public String getName()
/*     */   {
/* 384 */     return "tryhostname";
/*     */   }
/*     */ 
/*     */   public void execute(String paramString)
/*     */   {
/* 389 */     System.out.println("Status: 200 OK");
/* 390 */     System.out.println("Content-type: text/html");
/* 391 */     System.out.println("");
/* 392 */     System.out.println("<HTML><HEAD><TITLE>Java RMI Server Hostname Info</TITLE></HEAD><BODY>");
/*     */ 
/* 396 */     System.out.println("<H1>Java RMI Server Hostname Info</H1>");
/* 397 */     System.out.println("<H2>Local host name available to Java VM:</H2>");
/* 398 */     System.out.print("<P>InetAddress.getLocalHost().getHostName()");
/*     */     try {
/* 400 */       String str = InetAddress.getLocalHost().getHostName();
/*     */ 
/* 402 */       System.out.println(" = " + str);
/*     */     } catch (UnknownHostException localUnknownHostException) {
/* 404 */       System.out.println(" threw java.net.UnknownHostException");
/*     */     }
/*     */ 
/* 407 */     System.out.println("<H2>Server host information obtained through CGI interface from HTTP server:</H2>");
/* 408 */     System.out.println("<P>SERVER_NAME = " + CGIHandler.ServerName);
/* 409 */     System.out.println("<P>SERVER_PORT = " + CGIHandler.ServerPort);
/* 410 */     System.out.println("</BODY></HTML>");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.CGITryHostnameCommand
 * JD-Core Version:    0.6.2
 */