/*     */ package java.security;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class Signer extends Identity
/*     */ {
/*     */   private static final long serialVersionUID = -1763464102261361480L;
/*     */   private PrivateKey privateKey;
/*     */ 
/*     */   protected Signer()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Signer(String paramString)
/*     */   {
/*  74 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public Signer(String paramString, IdentityScope paramIdentityScope)
/*     */     throws KeyManagementException
/*     */   {
/*  89 */     super(paramString, paramIdentityScope);
/*     */   }
/*     */ 
/*     */   public PrivateKey getPrivateKey()
/*     */   {
/* 109 */     check("getSignerPrivateKey");
/* 110 */     return this.privateKey;
/*     */   }
/*     */ 
/*     */   public final void setKeyPair(KeyPair paramKeyPair)
/*     */     throws InvalidParameterException, KeyException
/*     */   {
/* 134 */     check("setSignerKeyPair");
/* 135 */     final PublicKey localPublicKey = paramKeyPair.getPublic();
/* 136 */     PrivateKey localPrivateKey = paramKeyPair.getPrivate();
/*     */ 
/* 138 */     if ((localPublicKey == null) || (localPrivateKey == null))
/* 139 */       throw new InvalidParameterException();
/*     */     try
/*     */     {
/* 142 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws KeyManagementException {
/* 145 */           Signer.this.setPublicKey(localPublicKey);
/* 146 */           return null;
/*     */         } } );
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 150 */       throw ((KeyManagementException)localPrivilegedActionException.getException());
/*     */     }
/* 152 */     this.privateKey = localPrivateKey;
/*     */   }
/*     */ 
/*     */   String printKeys() {
/* 156 */     String str = "";
/* 157 */     PublicKey localPublicKey = getPublicKey();
/* 158 */     if ((localPublicKey != null) && (this.privateKey != null)) {
/* 159 */       str = "\tpublic and private keys initialized";
/*     */     }
/*     */     else {
/* 162 */       str = "\tno keys";
/*     */     }
/* 164 */     return str;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 173 */     return "[Signer]" + super.toString();
/*     */   }
/*     */ 
/*     */   private static void check(String paramString) {
/* 177 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 178 */     if (localSecurityManager != null)
/* 179 */       localSecurityManager.checkSecurityAccess(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Signer
 * JD-Core Version:    0.6.2
 */