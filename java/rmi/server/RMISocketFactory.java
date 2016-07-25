/*     */ package java.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import sun.rmi.transport.proxy.RMIMasterSocketFactory;
/*     */ 
/*     */ public abstract class RMISocketFactory
/*     */   implements RMIClientSocketFactory, RMIServerSocketFactory
/*     */ {
/*  55 */   private static RMISocketFactory factory = null;
/*     */   private static RMISocketFactory defaultSocketFactory;
/*  59 */   private static RMIFailureHandler handler = null;
/*     */ 
/*     */   public abstract Socket createSocket(String paramString, int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract ServerSocket createServerSocket(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   public static synchronized void setSocketFactory(RMISocketFactory paramRMISocketFactory)
/*     */     throws IOException
/*     */   {
/* 110 */     if (factory != null) {
/* 111 */       throw new SocketException("factory already defined");
/*     */     }
/* 113 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 114 */     if (localSecurityManager != null) {
/* 115 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 117 */     factory = paramRMISocketFactory;
/*     */   }
/*     */ 
/*     */   public static synchronized RMISocketFactory getSocketFactory()
/*     */   {
/* 130 */     return factory;
/*     */   }
/*     */ 
/*     */   public static synchronized RMISocketFactory getDefaultSocketFactory()
/*     */   {
/* 142 */     if (defaultSocketFactory == null) {
/* 143 */       defaultSocketFactory = new RMIMasterSocketFactory();
/*     */     }
/*     */ 
/* 146 */     return defaultSocketFactory;
/*     */   }
/*     */ 
/*     */   public static synchronized void setFailureHandler(RMIFailureHandler paramRMIFailureHandler)
/*     */   {
/* 170 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 171 */     if (localSecurityManager != null) {
/* 172 */       localSecurityManager.checkSetFactory();
/*     */     }
/* 174 */     handler = paramRMIFailureHandler;
/*     */   }
/*     */ 
/*     */   public static synchronized RMIFailureHandler getFailureHandler()
/*     */   {
/* 186 */     return handler;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.RMISocketFactory
 * JD-Core Version:    0.6.2
 */