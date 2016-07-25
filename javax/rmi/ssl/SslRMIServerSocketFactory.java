/*     */ package javax.rmi.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ public class SslRMIServerSocketFactory
/*     */   implements RMIServerSocketFactory
/*     */ {
/* 363 */   private static SSLSocketFactory defaultSSLSocketFactory = null;
/*     */   private final String[] enabledCipherSuites;
/*     */   private final String[] enabledProtocols;
/*     */   private final boolean needClientAuth;
/*     */   private List<String> enabledCipherSuitesList;
/*     */   private List<String> enabledProtocolsList;
/*     */   private SSLContext context;
/*     */ 
/*     */   public SslRMIServerSocketFactory()
/*     */   {
/*  80 */     this(null, null, false);
/*     */   }
/*     */ 
/*     */   public SslRMIServerSocketFactory(String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean)
/*     */     throws IllegalArgumentException
/*     */   {
/* 118 */     this(null, paramArrayOfString1, paramArrayOfString2, paramBoolean);
/*     */   }
/*     */ 
/*     */   public SslRMIServerSocketFactory(SSLContext paramSSLContext, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean)
/*     */     throws IllegalArgumentException
/*     */   {
/* 167 */     this.enabledCipherSuites = (paramArrayOfString1 == null ? null : (String[])paramArrayOfString1.clone());
/*     */ 
/* 169 */     this.enabledProtocols = (paramArrayOfString2 == null ? null : (String[])paramArrayOfString2.clone());
/*     */ 
/* 171 */     this.needClientAuth = paramBoolean;
/*     */ 
/* 177 */     this.context = paramSSLContext;
/* 178 */     SSLSocketFactory localSSLSocketFactory = paramSSLContext == null ? getDefaultSSLSocketFactory() : paramSSLContext.getSocketFactory();
/*     */ 
/* 181 */     SSLSocket localSSLSocket = null;
/* 182 */     if ((this.enabledCipherSuites != null) || (this.enabledProtocols != null)) {
/*     */       try {
/* 184 */         localSSLSocket = (SSLSocket)localSSLSocketFactory.createSocket();
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 188 */         throw ((IllegalArgumentException)new IllegalArgumentException("Unable to check if the cipher suites and protocols to enable are supported").initCause(localException));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 197 */     if (this.enabledCipherSuites != null) {
/* 198 */       localSSLSocket.setEnabledCipherSuites(this.enabledCipherSuites);
/* 199 */       this.enabledCipherSuitesList = Arrays.asList(this.enabledCipherSuites);
/*     */     }
/* 201 */     if (this.enabledProtocols != null) {
/* 202 */       localSSLSocket.setEnabledProtocols(this.enabledProtocols);
/* 203 */       this.enabledProtocolsList = Arrays.asList(this.enabledProtocols);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final String[] getEnabledCipherSuites()
/*     */   {
/* 218 */     return this.enabledCipherSuites == null ? null : (String[])this.enabledCipherSuites.clone();
/*     */   }
/*     */ 
/*     */   public final String[] getEnabledProtocols()
/*     */   {
/* 234 */     return this.enabledProtocols == null ? null : (String[])this.enabledProtocols.clone();
/*     */   }
/*     */ 
/*     */   public final boolean getNeedClientAuth()
/*     */   {
/* 248 */     return this.needClientAuth;
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/* 257 */     final SSLSocketFactory localSSLSocketFactory = this.context == null ? getDefaultSSLSocketFactory() : this.context.getSocketFactory();
/*     */ 
/* 260 */     return new ServerSocket(paramInt) {
/*     */       public Socket accept() throws IOException {
/* 262 */         Socket localSocket = super.accept();
/* 263 */         SSLSocket localSSLSocket = (SSLSocket)localSSLSocketFactory.createSocket(localSocket, localSocket.getInetAddress().getHostName(), localSocket.getPort(), true);
/*     */ 
/* 266 */         localSSLSocket.setUseClientMode(false);
/* 267 */         if (SslRMIServerSocketFactory.this.enabledCipherSuites != null) {
/* 268 */           localSSLSocket.setEnabledCipherSuites(SslRMIServerSocketFactory.this.enabledCipherSuites);
/*     */         }
/* 270 */         if (SslRMIServerSocketFactory.this.enabledProtocols != null) {
/* 271 */           localSSLSocket.setEnabledProtocols(SslRMIServerSocketFactory.this.enabledProtocols);
/*     */         }
/* 273 */         localSSLSocket.setNeedClientAuth(SslRMIServerSocketFactory.this.needClientAuth);
/* 274 */         return localSSLSocket;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 291 */     if (paramObject == null) return false;
/* 292 */     if (paramObject == this) return true;
/* 293 */     if (!(paramObject instanceof SslRMIServerSocketFactory))
/* 294 */       return false;
/* 295 */     SslRMIServerSocketFactory localSslRMIServerSocketFactory = (SslRMIServerSocketFactory)paramObject;
/* 296 */     return (getClass().equals(localSslRMIServerSocketFactory.getClass())) && (checkParameters(localSslRMIServerSocketFactory));
/*     */   }
/*     */ 
/*     */   private boolean checkParameters(SslRMIServerSocketFactory paramSslRMIServerSocketFactory)
/*     */   {
/* 302 */     if (this.context == null ? paramSslRMIServerSocketFactory.context != null : !this.context.equals(paramSslRMIServerSocketFactory.context)) {
/* 303 */       return false;
/*     */     }
/*     */ 
/* 307 */     if (this.needClientAuth != paramSslRMIServerSocketFactory.needClientAuth) {
/* 308 */       return false;
/*     */     }
/*     */ 
/* 312 */     if (((this.enabledCipherSuites == null) && (paramSslRMIServerSocketFactory.enabledCipherSuites != null)) || ((this.enabledCipherSuites != null) && (paramSslRMIServerSocketFactory.enabledCipherSuites == null)))
/*     */     {
/* 314 */       return false;
/*     */     }
/*     */     List localList;
/* 315 */     if ((this.enabledCipherSuites != null) && (paramSslRMIServerSocketFactory.enabledCipherSuites != null)) {
/* 316 */       localList = Arrays.asList(paramSslRMIServerSocketFactory.enabledCipherSuites);
/*     */ 
/* 318 */       if (!this.enabledCipherSuitesList.equals(localList)) {
/* 319 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 324 */     if (((this.enabledProtocols == null) && (paramSslRMIServerSocketFactory.enabledProtocols != null)) || ((this.enabledProtocols != null) && (paramSslRMIServerSocketFactory.enabledProtocols == null)))
/*     */     {
/* 326 */       return false;
/* 327 */     }if ((this.enabledProtocols != null) && (paramSslRMIServerSocketFactory.enabledProtocols != null)) {
/* 328 */       localList = Arrays.asList(paramSslRMIServerSocketFactory.enabledProtocols);
/*     */ 
/* 330 */       if (!this.enabledProtocolsList.equals(localList)) {
/* 331 */         return false;
/*     */       }
/*     */     }
/* 334 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 345 */     return getClass().hashCode() + (this.context == null ? 0 : this.context.hashCode()) + (this.needClientAuth ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode()) + (this.enabledCipherSuites == null ? 0 : this.enabledCipherSuitesList.hashCode()) + (this.enabledProtocols == null ? 0 : this.enabledProtocolsList.hashCode());
/*     */   }
/*     */ 
/*     */   private static synchronized SSLSocketFactory getDefaultSSLSocketFactory()
/*     */   {
/* 366 */     if (defaultSSLSocketFactory == null) {
/* 367 */       defaultSSLSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */     }
/* 369 */     return defaultSSLSocketFactory;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.ssl.SslRMIServerSocketFactory
 * JD-Core Version:    0.6.2
 */