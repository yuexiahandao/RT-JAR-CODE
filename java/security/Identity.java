/*     */ package java.security;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Vector;
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class Identity
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3609922007826600659L;
/*     */   private String name;
/*     */   private PublicKey publicKey;
/*  84 */   String info = "No further information available.";
/*     */   IdentityScope scope;
/*     */   Vector<Certificate> certificates;
/*     */ 
/*     */   protected Identity()
/*     */   {
/* 104 */     this("restoring...");
/*     */   }
/*     */ 
/*     */   public Identity(String paramString, IdentityScope paramIdentityScope)
/*     */     throws KeyManagementException
/*     */   {
/* 118 */     this(paramString);
/* 119 */     if (paramIdentityScope != null) {
/* 120 */       paramIdentityScope.addIdentity(this);
/*     */     }
/* 122 */     this.scope = paramIdentityScope;
/*     */   }
/*     */ 
/*     */   public Identity(String paramString)
/*     */   {
/* 131 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 140 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final IdentityScope getScope()
/*     */   {
/* 149 */     return this.scope;
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKey()
/*     */   {
/* 160 */     return this.publicKey;
/*     */   }
/*     */ 
/*     */   public void setPublicKey(PublicKey paramPublicKey)
/*     */     throws KeyManagementException
/*     */   {
/* 186 */     check("setIdentityPublicKey");
/* 187 */     this.publicKey = paramPublicKey;
/* 188 */     this.certificates = new Vector();
/*     */   }
/*     */ 
/*     */   public void setInfo(String paramString)
/*     */   {
/* 208 */     check("setIdentityInfo");
/* 209 */     this.info = paramString;
/*     */   }
/*     */ 
/*     */   public String getInfo()
/*     */   {
/* 220 */     return this.info;
/*     */   }
/*     */ 
/*     */   public void addCertificate(Certificate paramCertificate)
/*     */     throws KeyManagementException
/*     */   {
/* 248 */     check("addIdentityCertificate");
/*     */ 
/* 250 */     if (this.certificates == null) {
/* 251 */       this.certificates = new Vector();
/*     */     }
/* 253 */     if (this.publicKey != null) {
/* 254 */       if (!keyEquals(this.publicKey, paramCertificate.getPublicKey())) {
/* 255 */         throw new KeyManagementException("public key different from cert public key");
/*     */       }
/*     */     }
/*     */     else {
/* 259 */       this.publicKey = paramCertificate.getPublicKey();
/*     */     }
/* 261 */     this.certificates.addElement(paramCertificate);
/*     */   }
/*     */ 
/*     */   private boolean keyEquals(Key paramKey1, Key paramKey2) {
/* 265 */     String str1 = paramKey1.getFormat();
/* 266 */     String str2 = paramKey2.getFormat();
/* 267 */     if (((str1 == null ? 1 : 0) ^ (str2 == null ? 1 : 0)) != 0)
/* 268 */       return false;
/* 269 */     if ((str1 != null) && (str2 != null) && 
/* 270 */       (!str1.equalsIgnoreCase(str2)))
/* 271 */       return false;
/* 272 */     return Arrays.equals(paramKey1.getEncoded(), paramKey2.getEncoded());
/*     */   }
/*     */ 
/*     */   public void removeCertificate(Certificate paramCertificate)
/*     */     throws KeyManagementException
/*     */   {
/* 297 */     check("removeIdentityCertificate");
/* 298 */     if (this.certificates != null)
/* 299 */       this.certificates.removeElement(paramCertificate);
/*     */   }
/*     */ 
/*     */   public Certificate[] certificates()
/*     */   {
/* 309 */     if (this.certificates == null) {
/* 310 */       return new Certificate[0];
/*     */     }
/* 312 */     int i = this.certificates.size();
/* 313 */     Certificate[] arrayOfCertificate = new Certificate[i];
/* 314 */     this.certificates.copyInto(arrayOfCertificate);
/* 315 */     return arrayOfCertificate;
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object paramObject)
/*     */   {
/* 335 */     if (paramObject == this) {
/* 336 */       return true;
/*     */     }
/*     */ 
/* 339 */     if ((paramObject instanceof Identity)) {
/* 340 */       Identity localIdentity = (Identity)paramObject;
/* 341 */       if (fullName().equals(localIdentity.fullName())) {
/* 342 */         return true;
/*     */       }
/* 344 */       return identityEquals(localIdentity);
/*     */     }
/*     */ 
/* 347 */     return false;
/*     */   }
/*     */ 
/*     */   protected boolean identityEquals(Identity paramIdentity)
/*     */   {
/* 364 */     if (!this.name.equalsIgnoreCase(paramIdentity.name)) {
/* 365 */       return false;
/*     */     }
/* 367 */     if (((this.publicKey == null ? 1 : 0) ^ (paramIdentity.publicKey == null ? 1 : 0)) != 0) {
/* 368 */       return false;
/*     */     }
/* 370 */     if ((this.publicKey != null) && (paramIdentity.publicKey != null) && 
/* 371 */       (!this.publicKey.equals(paramIdentity.publicKey))) {
/* 372 */       return false;
/*     */     }
/* 374 */     return true;
/*     */   }
/*     */ 
/*     */   String fullName()
/*     */   {
/* 382 */     String str = this.name;
/* 383 */     if (this.scope != null) {
/* 384 */       str = str + "." + this.scope.getName();
/*     */     }
/* 386 */     return str;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 407 */     check("printIdentity");
/* 408 */     String str = this.name;
/* 409 */     if (this.scope != null) {
/* 410 */       str = str + "[" + this.scope.getName() + "]";
/*     */     }
/* 412 */     return str;
/*     */   }
/*     */ 
/*     */   public String toString(boolean paramBoolean)
/*     */   {
/* 438 */     String str = toString();
/* 439 */     if (paramBoolean) {
/* 440 */       str = str + "\n";
/* 441 */       str = str + printKeys();
/* 442 */       str = str + "\n" + printCertificates();
/* 443 */       if (this.info != null)
/* 444 */         str = str + "\n\t" + this.info;
/*     */       else {
/* 446 */         str = str + "\n\tno additional information available.";
/*     */       }
/*     */     }
/* 449 */     return str;
/*     */   }
/*     */ 
/*     */   String printKeys() {
/* 453 */     String str = "";
/* 454 */     if (this.publicKey != null)
/* 455 */       str = "\tpublic key initialized";
/*     */     else {
/* 457 */       str = "\tno public key";
/*     */     }
/* 459 */     return str;
/*     */   }
/*     */ 
/*     */   String printCertificates() {
/* 463 */     String str = "";
/* 464 */     if (this.certificates == null) {
/* 465 */       return "\tno certificates";
/*     */     }
/* 467 */     str = str + "\tcertificates: \n";
/*     */ 
/* 469 */     int i = 1;
/* 470 */     for (Certificate localCertificate : this.certificates) {
/* 471 */       str = str + "\tcertificate " + i++ + "\tfor  : " + localCertificate.getPrincipal() + "\n";
/*     */ 
/* 473 */       str = str + "\t\t\tfrom : " + localCertificate.getGuarantor() + "\n";
/*     */     }
/*     */ 
/* 477 */     return str;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 486 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */   private static void check(String paramString) {
/* 490 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 491 */     if (localSecurityManager != null)
/* 492 */       localSecurityManager.checkSecurityAccess(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.Identity
 * JD-Core Version:    0.6.2
 */