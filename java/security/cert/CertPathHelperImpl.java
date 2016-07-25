/*    */ package java.security.cert;
/*    */ 
/*    */ import java.util.Date;
/*    */ import java.util.Set;
/*    */ import sun.security.provider.certpath.CertPathHelper;
/*    */ import sun.security.x509.GeneralNameInterface;
/*    */ 
/*    */ class CertPathHelperImpl extends CertPathHelper
/*    */ {
/*    */   static synchronized void initialize()
/*    */   {
/* 53 */     if (CertPathHelper.instance == null)
/* 54 */       CertPathHelper.instance = new CertPathHelperImpl();
/*    */   }
/*    */ 
/*    */   protected void implSetPathToNames(X509CertSelector paramX509CertSelector, Set<GeneralNameInterface> paramSet)
/*    */   {
/* 60 */     paramX509CertSelector.setPathToNamesInternal(paramSet);
/*    */   }
/*    */ 
/*    */   protected void implSetDateAndTime(X509CRLSelector paramX509CRLSelector, Date paramDate, long paramLong) {
/* 64 */     paramX509CRLSelector.setDateAndTime(paramDate, paramLong);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertPathHelperImpl
 * JD-Core Version:    0.6.2
 */