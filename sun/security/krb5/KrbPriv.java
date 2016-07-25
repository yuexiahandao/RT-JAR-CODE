/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.krb5.internal.EncKrbPrivPart;
/*     */ import sun.security.krb5.internal.HostAddress;
/*     */ import sun.security.krb5.internal.KRBPriv;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.SeqNumber;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ class KrbPriv extends KrbAppMessage
/*     */ {
/*     */   private byte[] obuf;
/*     */   private byte[] userData;
/*     */ 
/*     */   private KrbPriv(byte[] paramArrayOfByte, Credentials paramCredentials, EncryptionKey paramEncryptionKey, KerberosTime paramKerberosTime, SeqNumber paramSeqNumber, HostAddress paramHostAddress1, HostAddress paramHostAddress2)
/*     */     throws KrbException, IOException
/*     */   {
/*  53 */     EncryptionKey localEncryptionKey = null;
/*  54 */     if (paramEncryptionKey != null)
/*  55 */       localEncryptionKey = paramEncryptionKey;
/*     */     else {
/*  57 */       localEncryptionKey = paramCredentials.key;
/*     */     }
/*  59 */     this.obuf = mk_priv(paramArrayOfByte, localEncryptionKey, paramKerberosTime, paramSeqNumber, paramHostAddress1, paramHostAddress2);
/*     */   }
/*     */ 
/*     */   private KrbPriv(byte[] paramArrayOfByte, Credentials paramCredentials, EncryptionKey paramEncryptionKey, SeqNumber paramSeqNumber, HostAddress paramHostAddress1, HostAddress paramHostAddress2, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws KrbException, IOException
/*     */   {
/*  79 */     KRBPriv localKRBPriv = new KRBPriv(paramArrayOfByte);
/*  80 */     EncryptionKey localEncryptionKey = null;
/*  81 */     if (paramEncryptionKey != null)
/*  82 */       localEncryptionKey = paramEncryptionKey;
/*     */     else
/*  84 */       localEncryptionKey = paramCredentials.key;
/*  85 */     this.userData = rd_priv(localKRBPriv, localEncryptionKey, paramSeqNumber, paramHostAddress1, paramHostAddress2, paramBoolean1, paramBoolean2, paramCredentials.client, paramCredentials.client.getRealm());
/*     */   }
/*     */ 
/*     */   public byte[] getMessage()
/*     */     throws KrbException
/*     */   {
/*  98 */     return this.obuf;
/*     */   }
/*     */ 
/*     */   public byte[] getData() {
/* 102 */     return this.userData;
/*     */   }
/*     */ 
/*     */   private byte[] mk_priv(byte[] paramArrayOfByte, EncryptionKey paramEncryptionKey, KerberosTime paramKerberosTime, SeqNumber paramSeqNumber, HostAddress paramHostAddress1, HostAddress paramHostAddress2)
/*     */     throws Asn1Exception, IOException, KdcErrException, KrbCryptoException
/*     */   {
/* 114 */     Integer localInteger1 = null;
/* 115 */     Integer localInteger2 = null;
/*     */ 
/* 117 */     if (paramKerberosTime != null) {
/* 118 */       localInteger1 = new Integer(paramKerberosTime.getMicroSeconds());
/*     */     }
/* 120 */     if (paramSeqNumber != null) {
/* 121 */       localInteger2 = new Integer(paramSeqNumber.current());
/* 122 */       paramSeqNumber.step();
/*     */     }
/*     */ 
/* 125 */     EncKrbPrivPart localEncKrbPrivPart = new EncKrbPrivPart(paramArrayOfByte, paramKerberosTime, localInteger1, localInteger2, paramHostAddress1, paramHostAddress2);
/*     */ 
/* 134 */     byte[] arrayOfByte = localEncKrbPrivPart.asn1Encode();
/*     */ 
/* 136 */     EncryptedData localEncryptedData = new EncryptedData(paramEncryptionKey, arrayOfByte, 13);
/*     */ 
/* 140 */     KRBPriv localKRBPriv = new KRBPriv(localEncryptedData);
/*     */ 
/* 142 */     arrayOfByte = localKRBPriv.asn1Encode();
/*     */ 
/* 144 */     return localKRBPriv.asn1Encode();
/*     */   }
/*     */ 
/*     */   private byte[] rd_priv(KRBPriv paramKRBPriv, EncryptionKey paramEncryptionKey, SeqNumber paramSeqNumber, HostAddress paramHostAddress1, HostAddress paramHostAddress2, boolean paramBoolean1, boolean paramBoolean2, PrincipalName paramPrincipalName, Realm paramRealm)
/*     */     throws Asn1Exception, KdcErrException, KrbApErrException, IOException, KrbCryptoException
/*     */   {
/* 159 */     byte[] arrayOfByte1 = paramKRBPriv.encPart.decrypt(paramEncryptionKey, 13);
/*     */ 
/* 161 */     byte[] arrayOfByte2 = paramKRBPriv.encPart.reset(arrayOfByte1);
/* 162 */     DerValue localDerValue = new DerValue(arrayOfByte2);
/* 163 */     EncKrbPrivPart localEncKrbPrivPart = new EncKrbPrivPart(localDerValue);
/*     */ 
/* 165 */     check(localEncKrbPrivPart.timestamp, localEncKrbPrivPart.usec, localEncKrbPrivPart.seqNumber, localEncKrbPrivPart.sAddress, localEncKrbPrivPart.rAddress, paramSeqNumber, paramHostAddress1, paramHostAddress2, paramBoolean1, paramBoolean2, paramPrincipalName, paramRealm);
/*     */ 
/* 179 */     return localEncKrbPrivPart.userData;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbPriv
 * JD-Core Version:    0.6.2
 */