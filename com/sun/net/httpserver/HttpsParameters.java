/*     */ package com.sun.net.httpserver;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ 
/*     */ public abstract class HttpsParameters
/*     */ {
/*     */   private String[] cipherSuites;
/*     */   private String[] protocols;
/*     */   private boolean wantClientAuth;
/*     */   private boolean needClientAuth;
/*     */ 
/*     */   public abstract HttpsConfigurator getHttpsConfigurator();
/*     */ 
/*     */   public abstract InetSocketAddress getClientAddress();
/*     */ 
/*     */   public abstract void setSSLParameters(SSLParameters paramSSLParameters);
/*     */ 
/*     */   public String[] getCipherSuites()
/*     */   {
/*  95 */     return this.cipherSuites != null ? (String[])this.cipherSuites.clone() : null;
/*     */   }
/*     */ 
/*     */   public void setCipherSuites(String[] paramArrayOfString)
/*     */   {
/* 104 */     this.cipherSuites = (paramArrayOfString != null ? (String[])paramArrayOfString.clone() : null);
/*     */   }
/*     */ 
/*     */   public String[] getProtocols()
/*     */   {
/* 115 */     return this.protocols != null ? (String[])this.protocols.clone() : null;
/*     */   }
/*     */ 
/*     */   public void setProtocols(String[] paramArrayOfString)
/*     */   {
/* 124 */     this.protocols = (paramArrayOfString != null ? (String[])paramArrayOfString.clone() : null);
/*     */   }
/*     */ 
/*     */   public boolean getWantClientAuth()
/*     */   {
/* 133 */     return this.wantClientAuth;
/*     */   }
/*     */ 
/*     */   public void setWantClientAuth(boolean paramBoolean)
/*     */   {
/* 143 */     this.wantClientAuth = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getNeedClientAuth()
/*     */   {
/* 152 */     return this.needClientAuth;
/*     */   }
/*     */ 
/*     */   public void setNeedClientAuth(boolean paramBoolean)
/*     */   {
/* 162 */     this.needClientAuth = paramBoolean;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.net.httpserver.HttpsParameters
 * JD-Core Version:    0.6.2
 */