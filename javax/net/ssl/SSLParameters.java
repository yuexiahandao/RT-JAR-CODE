/*     */ package javax.net.ssl;
/*     */ 
/*     */ import java.security.AlgorithmConstraints;
/*     */ 
/*     */ public class SSLParameters
/*     */ {
/*     */   private String[] cipherSuites;
/*     */   private String[] protocols;
/*     */   private boolean wantClientAuth;
/*     */   private boolean needClientAuth;
/*     */   private String identificationAlgorithm;
/*     */   private AlgorithmConstraints algorithmConstraints;
/*     */ 
/*     */   public SSLParameters()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SSLParameters(String[] paramArrayOfString)
/*     */   {
/*  87 */     setCipherSuites(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public SSLParameters(String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/* 102 */     setCipherSuites(paramArrayOfString1);
/* 103 */     setProtocols(paramArrayOfString2);
/*     */   }
/*     */ 
/*     */   private static String[] clone(String[] paramArrayOfString) {
/* 107 */     return paramArrayOfString == null ? null : (String[])paramArrayOfString.clone();
/*     */   }
/*     */ 
/*     */   public String[] getCipherSuites()
/*     */   {
/* 118 */     return clone(this.cipherSuites);
/*     */   }
/*     */ 
/*     */   public void setCipherSuites(String[] paramArrayOfString)
/*     */   {
/* 127 */     this.cipherSuites = clone(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public String[] getProtocols()
/*     */   {
/* 138 */     return clone(this.protocols);
/*     */   }
/*     */ 
/*     */   public void setProtocols(String[] paramArrayOfString)
/*     */   {
/* 147 */     this.protocols = clone(paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public boolean getWantClientAuth()
/*     */   {
/* 156 */     return this.wantClientAuth;
/*     */   }
/*     */ 
/*     */   public void setWantClientAuth(boolean paramBoolean)
/*     */   {
/* 166 */     this.wantClientAuth = paramBoolean;
/* 167 */     this.needClientAuth = false;
/*     */   }
/*     */ 
/*     */   public boolean getNeedClientAuth()
/*     */   {
/* 176 */     return this.needClientAuth;
/*     */   }
/*     */ 
/*     */   public void setNeedClientAuth(boolean paramBoolean)
/*     */   {
/* 186 */     this.wantClientAuth = false;
/* 187 */     this.needClientAuth = paramBoolean;
/*     */   }
/*     */ 
/*     */   public AlgorithmConstraints getAlgorithmConstraints()
/*     */   {
/* 201 */     return this.algorithmConstraints;
/*     */   }
/*     */ 
/*     */   public void setAlgorithmConstraints(AlgorithmConstraints paramAlgorithmConstraints)
/*     */   {
/* 218 */     this.algorithmConstraints = paramAlgorithmConstraints;
/*     */   }
/*     */ 
/*     */   public String getEndpointIdentificationAlgorithm()
/*     */   {
/* 233 */     return this.identificationAlgorithm;
/*     */   }
/*     */ 
/*     */   public void setEndpointIdentificationAlgorithm(String paramString)
/*     */   {
/* 254 */     this.identificationAlgorithm = paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLParameters
 * JD-Core Version:    0.6.2
 */