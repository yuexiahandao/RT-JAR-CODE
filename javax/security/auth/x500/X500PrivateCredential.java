/*     */ package javax.security.auth.x500;
/*     */ 
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.security.auth.Destroyable;
/*     */ 
/*     */ public final class X500PrivateCredential
/*     */   implements Destroyable
/*     */ {
/*     */   private X509Certificate cert;
/*     */   private PrivateKey key;
/*     */   private String alias;
/*     */ 
/*     */   public X500PrivateCredential(X509Certificate paramX509Certificate, PrivateKey paramPrivateKey)
/*     */   {
/*  57 */     if ((paramX509Certificate == null) || (paramPrivateKey == null))
/*  58 */       throw new IllegalArgumentException();
/*  59 */     this.cert = paramX509Certificate;
/*  60 */     this.key = paramPrivateKey;
/*  61 */     this.alias = null;
/*     */   }
/*     */ 
/*     */   public X500PrivateCredential(X509Certificate paramX509Certificate, PrivateKey paramPrivateKey, String paramString)
/*     */   {
/*  77 */     if ((paramX509Certificate == null) || (paramPrivateKey == null) || (paramString == null))
/*  78 */       throw new IllegalArgumentException();
/*  79 */     this.cert = paramX509Certificate;
/*  80 */     this.key = paramPrivateKey;
/*  81 */     this.alias = paramString;
/*     */   }
/*     */ 
/*     */   public X509Certificate getCertificate()
/*     */   {
/*  91 */     return this.cert;
/*     */   }
/*     */ 
/*     */   public PrivateKey getPrivateKey()
/*     */   {
/* 100 */     return this.key;
/*     */   }
/*     */ 
/*     */   public String getAlias()
/*     */   {
/* 110 */     return this.alias;
/*     */   }
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 119 */     this.cert = null;
/* 120 */     this.key = null;
/* 121 */     this.alias = null;
/*     */   }
/*     */ 
/*     */   public boolean isDestroyed()
/*     */   {
/* 132 */     return (this.cert == null) && (this.key == null) && (this.alias == null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.x500.X500PrivateCredential
 * JD-Core Version:    0.6.2
 */