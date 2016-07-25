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
/*    */ public class EncASRepPart extends EncKDCRepPart
/*    */ {
/*    */   public EncASRepPart(EncryptionKey paramEncryptionKey, LastReq paramLastReq, int paramInt, KerberosTime paramKerberosTime1, TicketFlags paramTicketFlags, KerberosTime paramKerberosTime2, KerberosTime paramKerberosTime3, KerberosTime paramKerberosTime4, KerberosTime paramKerberosTime5, Realm paramRealm, PrincipalName paramPrincipalName, HostAddresses paramHostAddresses)
/*    */   {
/* 52 */     super(paramEncryptionKey, paramLastReq, paramInt, paramKerberosTime1, paramTicketFlags, paramKerberosTime2, paramKerberosTime3, paramKerberosTime4, paramKerberosTime5, paramRealm, paramPrincipalName, paramHostAddresses, 25);
/*    */   }
/*    */ 
/*    */   public EncASRepPart(byte[] paramArrayOfByte)
/*    */     throws Asn1Exception, IOException, KrbException
/*    */   {
/* 73 */     init(new DerValue(paramArrayOfByte));
/*    */   }
/*    */ 
/*    */   public EncASRepPart(DerValue paramDerValue) throws Asn1Exception, IOException, KrbException
/*    */   {
/* 78 */     init(paramDerValue);
/*    */   }
/*    */ 
/*    */   private void init(DerValue paramDerValue) throws Asn1Exception, IOException, KrbException
/*    */   {
/* 83 */     init(paramDerValue, 25);
/*    */   }
/*    */ 
/*    */   public byte[] asn1Encode() throws Asn1Exception, IOException
/*    */   {
/* 88 */     return asn1Encode(25);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.EncASRepPart
 * JD-Core Version:    0.6.2
 */