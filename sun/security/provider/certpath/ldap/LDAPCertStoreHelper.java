/*    */ package sun.security.provider.certpath.ldap;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URI;
/*    */ import java.security.InvalidAlgorithmParameterException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.cert.CertStore;
/*    */ import java.security.cert.X509CRLSelector;
/*    */ import java.security.cert.X509CertSelector;
/*    */ import java.util.Collection;
/*    */ import javax.security.auth.x500.X500Principal;
/*    */ import sun.security.provider.certpath.CertStoreHelper;
/*    */ 
/*    */ public class LDAPCertStoreHelper
/*    */   implements CertStoreHelper
/*    */ {
/*    */   public CertStore getCertStore(URI paramURI)
/*    */     throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
/*    */   {
/* 53 */     return LDAPCertStore.getInstance(LDAPCertStore.getParameters(paramURI));
/*    */   }
/*    */ 
/*    */   public X509CertSelector wrap(X509CertSelector paramX509CertSelector, X500Principal paramX500Principal, String paramString)
/*    */     throws IOException
/*    */   {
/* 62 */     return new LDAPCertStore.LDAPCertSelector(paramX509CertSelector, paramX500Principal, paramString);
/*    */   }
/*    */ 
/*    */   public X509CRLSelector wrap(X509CRLSelector paramX509CRLSelector, Collection<X500Principal> paramCollection, String paramString)
/*    */     throws IOException
/*    */   {
/* 71 */     return new LDAPCertStore.LDAPCRLSelector(paramX509CRLSelector, paramCollection, paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.ldap.LDAPCertStoreHelper
 * JD-Core Version:    0.6.2
 */