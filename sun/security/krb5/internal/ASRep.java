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
/*    */ public class ASRep extends KDCRep
/*    */ {
/*    */   public ASRep(PAData[] paramArrayOfPAData, Realm paramRealm, PrincipalName paramPrincipalName, Ticket paramTicket, EncryptedData paramEncryptedData)
/*    */     throws IOException
/*    */   {
/* 49 */     super(paramArrayOfPAData, paramRealm, paramPrincipalName, paramTicket, paramEncryptedData, 11);
/*    */   }
/*    */ 
/*    */   public ASRep(byte[] paramArrayOfByte)
/*    */     throws Asn1Exception, RealmException, KrbApErrException, IOException
/*    */   {
/* 55 */     init(new DerValue(paramArrayOfByte));
/*    */   }
/*    */ 
/*    */   public ASRep(DerValue paramDerValue) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*    */   {
/* 60 */     init(paramDerValue);
/*    */   }
/*    */ 
/*    */   private void init(DerValue paramDerValue) throws Asn1Exception, RealmException, KrbApErrException, IOException
/*    */   {
/* 65 */     init(paramDerValue, 11);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ASRep
 * JD-Core Version:    0.6.2
 */