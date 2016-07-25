/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ public class KeyUsage
/*    */ {
/*    */   public static final int KU_UNKNOWN = 0;
/*    */   public static final int KU_PA_ENC_TS = 1;
/*    */   public static final int KU_TICKET = 2;
/*    */   public static final int KU_ENC_AS_REP_PART = 3;
/*    */   public static final int KU_TGS_REQ_AUTH_DATA_SESSKEY = 4;
/*    */   public static final int KU_TGS_REQ_AUTH_DATA_SUBKEY = 5;
/*    */   public static final int KU_PA_TGS_REQ_CKSUM = 6;
/*    */   public static final int KU_PA_TGS_REQ_AUTHENTICATOR = 7;
/*    */   public static final int KU_ENC_TGS_REP_PART_SESSKEY = 8;
/*    */   public static final int KU_ENC_TGS_REP_PART_SUBKEY = 9;
/*    */   public static final int KU_AUTHENTICATOR_CKSUM = 10;
/*    */   public static final int KU_AP_REQ_AUTHENTICATOR = 11;
/*    */   public static final int KU_ENC_AP_REP_PART = 12;
/*    */   public static final int KU_ENC_KRB_PRIV_PART = 13;
/*    */   public static final int KU_ENC_KRB_CRED_PART = 14;
/*    */   public static final int KU_KRB_SAFE_CKSUM = 15;
/*    */   public static final int KU_AD_KDC_ISSUED_CKSUM = 19;
/*    */ 
/*    */   public static final boolean isValid(int paramInt)
/*    */   {
/* 60 */     return paramInt >= 0;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.KeyUsage
 * JD-Core Version:    0.6.2
 */