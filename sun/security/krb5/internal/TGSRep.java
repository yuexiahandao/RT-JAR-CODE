/*    */ package sun.security.krb5.internal;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import sun.security.krb5.Asn1Exception;
/*    */ import sun.security.krb5.EncryptedData;
/*    */ import sun.security.krb5.PrincipalName;
/*    */ import sun.security.krb5.Realm;
/*    */ import sun.security.krb5.RealmException;
/*    */ import sun.security.util.DerValue;
/*    */ 
/*    */ public class TGSRep extends KDCRep
/*    */ {
/*    */   public TGSRep(PAData[] paramArrayOfPAData, Realm paramRealm, PrincipalName paramPrincipalName, Ticket paramTicket, EncryptedData paramEncryptedData)
/*    */     throws IOException
/*    */   {
/* 50 */     super(paramArrayOfPAData, paramRealm, paramPrincipalName, paramTicket, paramEncryptedData, 13);
/*    */   }
/*    */ 
/*    */   public TGSRep(byte[] paramArrayOfByte)
/*    */     throws Asn1Exception, RealmException, KrbApErrException, IOException
/*    */   {
/* 56 */     init(new DerValue(paramArrayOfByte));
/*    */   }
/*    */ 
/*    */   public TGSRep(DerValue paramDerValue) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*    */   {
/* 61 */     init(paramDerValue);
/*    */   }
/*    */ 
/*    */   private void init(DerValue paramDerValue) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*    */   {
/* 66 */     init(paramDerValue, 13);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.TGSRep
 * JD-Core Version:    0.6.2
 */