/*     */ package javax.security.auth.kerberos;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Objects;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.RealmException;
/*     */ 
/*     */ public final class KeyTab
/*     */ {
/*     */   private final File file;
/*     */ 
/*     */   private KeyTab(File paramFile)
/*     */   {
/*  85 */     this.file = paramFile;
/*     */   }
/*     */ 
/*     */   public static KeyTab getInstance(File paramFile)
/*     */   {
/*  98 */     if (paramFile == null) {
/*  99 */       throw new NullPointerException("file must be non null");
/*     */     }
/* 101 */     return new KeyTab(paramFile);
/*     */   }
/*     */ 
/*     */   public static KeyTab getInstance()
/*     */   {
/* 113 */     return new KeyTab(null);
/*     */   }
/*     */ 
/*     */   private sun.security.krb5.internal.ktab.KeyTab takeSnapshot()
/*     */   {
/*     */     try {
/* 119 */       return sun.security.krb5.internal.ktab.KeyTab.getInstance(this.file);
/*     */     } catch (AccessControlException localAccessControlException1) {
/* 121 */       if (this.file != null)
/*     */       {
/* 123 */         throw localAccessControlException1;
/*     */       }
/* 125 */       AccessControlException localAccessControlException2 = new AccessControlException("Access to default keytab denied (modified exception)");
/*     */ 
/* 127 */       localAccessControlException2.setStackTrace(localAccessControlException1.getStackTrace());
/* 128 */       throw localAccessControlException2;
/*     */     }
/*     */   }
/*     */ 
/*     */   public KerberosKey[] getKeys(KerberosPrincipal paramKerberosPrincipal)
/*     */   {
/*     */     try
/*     */     {
/* 173 */       EncryptionKey[] arrayOfEncryptionKey = takeSnapshot().readServiceKeys(new PrincipalName(paramKerberosPrincipal.getName()));
/*     */ 
/* 175 */       KerberosKey[] arrayOfKerberosKey = new KerberosKey[arrayOfEncryptionKey.length];
/* 176 */       for (int i = 0; i < arrayOfKerberosKey.length; i++) {
/* 177 */         Integer localInteger = arrayOfEncryptionKey[i].getKeyVersionNumber();
/* 178 */         arrayOfKerberosKey[i] = new KerberosKey(paramKerberosPrincipal, arrayOfEncryptionKey[i].getBytes(), arrayOfEncryptionKey[i].getEType(), localInteger == null ? 0 : localInteger.intValue());
/*     */ 
/* 183 */         arrayOfEncryptionKey[i].destroy();
/*     */       }
/* 185 */       return arrayOfKerberosKey; } catch (RealmException localRealmException) {
/*     */     }
/* 187 */     return new KerberosKey[0];
/*     */   }
/*     */ 
/*     */   EncryptionKey[] getEncryptionKeys(PrincipalName paramPrincipalName)
/*     */   {
/* 192 */     return takeSnapshot().readServiceKeys(paramPrincipalName);
/*     */   }
/*     */ 
/*     */   public boolean exists()
/*     */   {
/* 207 */     return !takeSnapshot().isMissing();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 211 */     return this.file == null ? "Default keytab" : this.file.toString();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 220 */     return Objects.hash(new Object[] { this.file });
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 233 */     if (paramObject == this) {
/* 234 */       return true;
/*     */     }
/* 236 */     if (!(paramObject instanceof KeyTab)) {
/* 237 */       return false;
/*     */     }
/*     */ 
/* 240 */     KeyTab localKeyTab = (KeyTab)paramObject;
/* 241 */     return Objects.equals(localKeyTab.file, this.file);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  80 */     SharedSecrets.setJavaxSecurityAuthKerberosAccess(new JavaxSecurityAuthKerberosAccessImpl());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.KeyTab
 * JD-Core Version:    0.6.2
 */