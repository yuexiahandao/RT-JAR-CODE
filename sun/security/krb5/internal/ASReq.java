/*    */ package sun.security.krb5.internal;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import sun.security.krb5.Asn1Exception;
/*    */ import sun.security.krb5.KrbException;
/*    */ import sun.security.util.DerValue;
/*    */ 
/*    */ public class ASReq extends KDCReq
/*    */ {
/*    */   public ASReq(PAData[] paramArrayOfPAData, KDCReqBody paramKDCReqBody)
/*    */     throws IOException
/*    */   {
/* 40 */     super(paramArrayOfPAData, paramKDCReqBody, 10);
/*    */   }
/*    */ 
/*    */   public ASReq(byte[] paramArrayOfByte) throws Asn1Exception, KrbException, IOException {
/* 44 */     init(new DerValue(paramArrayOfByte));
/*    */   }
/*    */ 
/*    */   public ASReq(DerValue paramDerValue) throws Asn1Exception, KrbException, IOException {
/* 48 */     init(paramDerValue);
/*    */   }
/*    */ 
/*    */   private void init(DerValue paramDerValue) throws Asn1Exception, IOException, KrbException {
/* 52 */     super.init(paramDerValue, 10);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ASReq
 * JD-Core Version:    0.6.2
 */