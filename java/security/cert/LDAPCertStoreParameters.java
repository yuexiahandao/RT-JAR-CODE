/*     */ package java.security.cert;
/*     */ 
/*     */ public class LDAPCertStoreParameters
/*     */   implements CertStoreParameters
/*     */ {
/*     */   private static final int LDAP_DEFAULT_PORT = 389;
/*     */   private int port;
/*     */   private String serverName;
/*     */ 
/*     */   public LDAPCertStoreParameters(String paramString, int paramInt)
/*     */   {
/*  71 */     if (paramString == null)
/*  72 */       throw new NullPointerException();
/*  73 */     this.serverName = paramString;
/*  74 */     this.port = paramInt;
/*     */   }
/*     */ 
/*     */   public LDAPCertStoreParameters(String paramString)
/*     */   {
/*  86 */     this(paramString, 389);
/*     */   }
/*     */ 
/*     */   public LDAPCertStoreParameters()
/*     */   {
/*  94 */     this("localhost", 389);
/*     */   }
/*     */ 
/*     */   public String getServerName()
/*     */   {
/* 103 */     return this.serverName;
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 112 */     return this.port;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 128 */       return super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 131 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 141 */     StringBuffer localStringBuffer = new StringBuffer();
/* 142 */     localStringBuffer.append("LDAPCertStoreParameters: [\n");
/*     */ 
/* 144 */     localStringBuffer.append("  serverName: " + this.serverName + "\n");
/* 145 */     localStringBuffer.append("  port: " + this.port + "\n");
/* 146 */     localStringBuffer.append("]");
/* 147 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.LDAPCertStoreParameters
 * JD-Core Version:    0.6.2
 */