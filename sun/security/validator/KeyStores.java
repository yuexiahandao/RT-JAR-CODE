/*     */ package sun.security.validator;
/*     */ 
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class KeyStores
/*     */ {
/*     */   public static Set<X509Certificate> getTrustedCerts(KeyStore paramKeyStore)
/*     */   {
/*  97 */     HashSet localHashSet = new HashSet();
/*     */     Enumeration localEnumeration;
/*     */     try
/*     */     {
/*  99 */       for (localEnumeration = paramKeyStore.aliases(); localEnumeration.hasMoreElements(); ) {
/* 100 */         String str = (String)localEnumeration.nextElement();
/*     */         Object localObject;
/* 101 */         if (paramKeyStore.isCertificateEntry(str)) {
/* 102 */           localObject = paramKeyStore.getCertificate(str);
/* 103 */           if ((localObject instanceof X509Certificate))
/* 104 */             localHashSet.add((X509Certificate)localObject);
/*     */         }
/* 106 */         else if (paramKeyStore.isKeyEntry(str)) {
/* 107 */           localObject = paramKeyStore.getCertificateChain(str);
/* 108 */           if ((localObject != null) && (localObject.length > 0) && ((localObject[0] instanceof X509Certificate)))
/*     */           {
/* 110 */             localHashSet.add((X509Certificate)localObject[0]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (KeyStoreException localKeyStoreException) {
/*     */     }
/* 117 */     return localHashSet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.validator.KeyStores
 * JD-Core Version:    0.6.2
 */