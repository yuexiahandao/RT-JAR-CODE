/*    */ package java.security.cert;
/*    */ 
/*    */ public enum PKIXReason
/*    */   implements CertPathValidatorException.Reason
/*    */ {
/* 40 */   NAME_CHAINING, 
/*    */ 
/* 45 */   INVALID_KEY_USAGE, 
/*    */ 
/* 50 */   INVALID_POLICY, 
/*    */ 
/* 55 */   NO_TRUST_ANCHOR, 
/*    */ 
/* 61 */   UNRECOGNIZED_CRIT_EXT, 
/*    */ 
/* 66 */   NOT_CA_CERT, 
/*    */ 
/* 71 */   PATH_TOO_LONG, 
/*    */ 
/* 76 */   INVALID_NAME;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.PKIXReason
 * JD-Core Version:    0.6.2
 */