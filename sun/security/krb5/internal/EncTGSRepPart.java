/*    */ package sun.security.krb5.internal;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import sun.security.krb5.Asn1Exception;
/*    */ import sun.security.krb5.EncryptionKey;
/*    */ import sun.security.krb5.KrbException;
/*    */ import sun.security.krb5.PrincipalName;
/*    */ import sun.security.krb5.Realm;
/*    */ import sun.security.util.DerValue;
/*    */ 
/*    */ public class EncTGSRepPart extends EncKDCRepPart
/*    */ {
/*    */   public EncTGSRepPart(EncryptionKey paramEncryptionKey, LastReq paramLastReq, int paramInt, KerberosTime paramKerberosTime1, TicketFlags paramTicketFlags, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, KerberosTime paramKerberosTime5, Realm paramRealm, PrincipalName paramPrincipalName, HostAddresses paramHostAddresses)
/*    */   {
/* 51 */     super(paramEncryptionKey, paramLastReq, paramInt, paramKerberosTime1, paramTicketFlags, paramKerberosTime2, paramKerberosTime3, paramKerberosTime4, paramKerberosTime5, paramRealm, paramPrincipalName, paramHostAddresses, 26);
/*    */   }
/*    */ 
/*    */   public EncTGSRepPart(byte[] paramArrayOfByte)
/*    */     throws Asn1Exception, IOException, KrbException
/*    */   {
/* 69 */     init(new DerValue(paramArrayOfByte));
/*    */   }
/*    */ 
/*    */   public EncTGSRepPart(DerValue paramDerValue) throws Asn1Exception, IOException, KrbException
/*    */   {
/* 74 */     init(paramDerValue);
/*    */   }
/*    */ 
/*    */   private void init(DerValue paramDerValue) throws Asn1Exception, IOException, KrbException
/*    */   {
/* 79 */     init(paramDerValue, 26);
/*    */   }
/*    */ 
/*    */   public byte[] asn1Encode() throws Asn1Exception, IOException
/*    */   {
/* 84 */     return asn1Encode(26);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.EncTGSRepPart
 * JD-Core Version:    0.6.2
 */