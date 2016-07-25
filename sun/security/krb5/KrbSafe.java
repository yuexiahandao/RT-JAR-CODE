/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.krb5.internal.HostAddress;
/*     */ import sun.security.krb5.internal.KRBSafe;
/*     */ import sun.security.krb5.internal.KRBSafeBody;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.SeqNumber;
/*     */ 
/*     */ class KrbSafe extends KrbAppMessage
/*     */ {
/*     */   private byte[] obuf;
/*     */   private byte[] userData;
/*     */ 
/*     */   public KrbSafe(byte[] paramArrayOfByte, Credentials paramCredentials, EncryptionKey paramEncryptionKey, KerberosTime paramKerberosTime, SeqNumber paramSeqNumber, HostAddress paramHostAddress1, HostAddress paramHostAddress2)
/*     */     throws KrbException, IOException
/*     */   {
/*  52 */     EncryptionKey localEncryptionKey = null;
/*  53 */     if (paramEncryptionKey != null)
/*  54 */       localEncryptionKey = paramEncryptionKey;
/*     */     else {
/*  56 */       localEncryptionKey = paramCredentials.key;
/*     */     }
/*  58 */     this.obuf = mk_safe(paramArrayOfByte, localEncryptionKey, paramKerberosTime, paramSeqNumber, paramHostAddress1, paramHostAddress2);
/*     */   }
/*     */ 
/*     */   public KrbSafe(byte[] paramArrayOfByte, Credentials paramCredentials, EncryptionKey paramEncryptionKey, SeqNumber paramSeqNumber, HostAddress paramHostAddress1, HostAddress paramHostAddress2, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws KrbException, IOException
/*     */   {
/*  77 */     KRBSafe localKRBSafe = new KRBSafe(paramArrayOfByte);
/*     */ 
/*  79 */     EncryptionKey localEncryptionKey = null;
/*  80 */     if (paramEncryptionKey != null)
/*  81 */       localEncryptionKey = paramEncryptionKey;
/*     */     else {
/*  83 */       localEncryptionKey = paramCredentials.key;
/*     */     }
/*  85 */     this.userData = rd_safe(localKRBSafe, localEncryptionKey, paramSeqNumber, paramHostAddress1, paramHostAddress2, paramBoolean1, paramBoolean2, paramCredentials.client, paramCredentials.client.getRealm());
/*     */   }
/*     */ 
/*     */   public byte[] getMessage()
/*     */   {
/*  99 */     return this.obuf;
/*     */   }
/*     */ 
/*     */   public byte[] getData() {
/* 103 */     return this.userData;
/*     */   }
/*     */ 
/*     */   private byte[] mk_safe(byte[] paramArrayOfByte, EncryptionKey paramEncryptionKey, KerberosTime paramKerberosTime, SeqNumber paramSeqNumber, HostAddress paramHostAddress1, HostAddress paramHostAddress2)
/*     */     throws Asn1Exception, IOException, KdcErrException, KrbApErrException, KrbCryptoException
/*     */   {
/* 115 */     Integer localInteger1 = null;
/* 116 */     Integer localInteger2 = null;
/*     */ 
/* 118 */     if (paramKerberosTime != null) {
/* 119 */       localInteger1 = new Integer(paramKerberosTime.getMicroSeconds());
/*     */     }
/* 121 */     if (paramSeqNumber != null) {
/* 122 */       localInteger2 = new Integer(paramSeqNumber.current());
/* 123 */       paramSeqNumber.step();
/*     */     }
/*     */ 
/* 126 */     KRBSafeBody localKRBSafeBody = new KRBSafeBody(paramArrayOfByte, paramKerberosTime, localInteger1, localInteger2, paramHostAddress1, paramHostAddress2);
/*     */ 
/* 135 */     byte[] arrayOfByte = localKRBSafeBody.asn1Encode();
/* 136 */     Checksum localChecksum = new Checksum(Checksum.SAFECKSUMTYPE_DEFAULT, arrayOfByte, paramEncryptionKey, 15);
/*     */ 
/* 143 */     KRBSafe localKRBSafe = new KRBSafe(localKRBSafeBody, localChecksum);
/*     */ 
/* 145 */     arrayOfByte = localKRBSafe.asn1Encode();
/*     */ 
/* 147 */     return localKRBSafe.asn1Encode();
/*     */   }
/*     */ 
/*     */   private byte[] rd_safe(KRBSafe paramKRBSafe, EncryptionKey paramEncryptionKey, SeqNumber paramSeqNumber, HostAddress paramHostAddress1, HostAddress paramHostAddress2, boolean paramBoolean1, boolean paramBoolean2, PrincipalName paramPrincipalName, Realm paramRealm)
/*     */     throws Asn1Exception, KdcErrException, KrbApErrException, IOException, KrbCryptoException
/*     */   {
/* 162 */     byte[] arrayOfByte = paramKRBSafe.safeBody.asn1Encode();
/*     */ 
/* 164 */     if (!paramKRBSafe.cksum.verifyKeyedChecksum(arrayOfByte, paramEncryptionKey, 15))
/*     */     {
/* 166 */       throw new KrbApErrException(41);
/*     */     }
/*     */ 
/* 170 */     check(paramKRBSafe.safeBody.timestamp, paramKRBSafe.safeBody.usec, paramKRBSafe.safeBody.seqNumber, paramKRBSafe.safeBody.sAddress, paramKRBSafe.safeBody.rAddress, paramSeqNumber, paramHostAddress1, paramHostAddress2, paramBoolean1, paramBoolean2, paramPrincipalName, paramRealm);
/*     */ 
/* 184 */     return paramKRBSafe.safeBody.userData;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbSafe
 * JD-Core Version:    0.6.2
 */