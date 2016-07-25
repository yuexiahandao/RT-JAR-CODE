/*     */ package sun.security.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.krb5.internal.APRep;
/*     */ import sun.security.krb5.internal.EncAPRepPart;
/*     */ import sun.security.krb5.internal.KRBError;
/*     */ import sun.security.krb5.internal.KdcErrException;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.krb5.internal.KrbApErrException;
/*     */ import sun.security.krb5.internal.LocalSeqNumber;
/*     */ import sun.security.krb5.internal.SeqNumber;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class KrbApRep
/*     */ {
/*     */   private byte[] obuf;
/*     */   private byte[] ibuf;
/*     */   private EncAPRepPart encPart;
/*     */   private APRep apRepMessg;
/*     */ 
/*     */   public KrbApRep(KrbApReq paramKrbApReq, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws KrbException, IOException
/*     */   {
/*  59 */     EncryptionKey localEncryptionKey = paramBoolean2 ? new EncryptionKey(paramKrbApReq.getCreds().getSessionKey()) : null;
/*     */ 
/*  62 */     LocalSeqNumber localLocalSeqNumber = new LocalSeqNumber();
/*     */ 
/*  64 */     init(paramKrbApReq, localEncryptionKey, localLocalSeqNumber);
/*     */   }
/*     */ 
/*     */   public KrbApRep(byte[] paramArrayOfByte, Credentials paramCredentials, KrbApReq paramKrbApReq)
/*     */     throws KrbException, IOException
/*     */   {
/*  75 */     this(paramArrayOfByte, paramCredentials);
/*  76 */     authenticate(paramKrbApReq);
/*     */   }
/*     */ 
/*     */   private void init(KrbApReq paramKrbApReq, EncryptionKey paramEncryptionKey, SeqNumber paramSeqNumber)
/*     */     throws KrbException, IOException
/*     */   {
/*  83 */     createMessage(paramKrbApReq.getCreds().key, paramKrbApReq.getCtime(), paramKrbApReq.cusec(), paramEncryptionKey, paramSeqNumber);
/*     */ 
/*  89 */     this.obuf = this.apRepMessg.asn1Encode();
/*     */   }
/*     */ 
/*     */   private KrbApRep(byte[] paramArrayOfByte, Credentials paramCredentials)
/*     */     throws KrbException, IOException
/*     */   {
/* 102 */     this(new DerValue(paramArrayOfByte), paramCredentials);
/*     */   }
/*     */ 
/*     */   private KrbApRep(DerValue paramDerValue, Credentials paramCredentials)
/*     */     throws KrbException, IOException
/*     */   {
/* 114 */     APRep localAPRep = null;
/*     */     try {
/* 116 */       localAPRep = new APRep(paramDerValue);
/*     */     } catch (Asn1Exception localAsn1Exception) {
/* 118 */       localAPRep = null;
/* 119 */       localObject = new KRBError(paramDerValue);
/* 120 */       String str1 = ((KRBError)localObject).getErrorString();
/*     */       String str2;
/* 122 */       if (str1.charAt(str1.length() - 1) == 0)
/* 123 */         str2 = str1.substring(0, str1.length() - 1);
/*     */       else
/* 125 */         str2 = str1;
/* 126 */       KrbException localKrbException = new KrbException(((KRBError)localObject).getErrorCode(), str2);
/* 127 */       localKrbException.initCause(localAsn1Exception);
/* 128 */       throw localKrbException;
/*     */     }
/*     */ 
/* 131 */     byte[] arrayOfByte = localAPRep.encPart.decrypt(paramCredentials.key, 12);
/*     */ 
/* 133 */     Object localObject = localAPRep.encPart.reset(arrayOfByte);
/*     */ 
/* 135 */     paramDerValue = new DerValue((byte[])localObject);
/* 136 */     this.encPart = new EncAPRepPart(paramDerValue);
/*     */   }
/*     */ 
/*     */   private void authenticate(KrbApReq paramKrbApReq) throws KrbException, IOException
/*     */   {
/* 141 */     if ((this.encPart.ctime.getSeconds() != paramKrbApReq.getCtime().getSeconds()) || (this.encPart.cusec != paramKrbApReq.getCtime().getMicroSeconds()))
/*     */     {
/* 143 */       throw new KrbApErrException(46);
/*     */     }
/*     */   }
/*     */ 
/*     */   public EncryptionKey getSubKey()
/*     */   {
/* 153 */     return this.encPart.getSubKey();
/*     */   }
/*     */ 
/*     */   public Integer getSeqNumber()
/*     */   {
/* 163 */     return this.encPart.getSeqNumber();
/*     */   }
/*     */ 
/*     */   public byte[] getMessage()
/*     */   {
/* 170 */     return this.obuf;
/*     */   }
/*     */ 
/*     */   private void createMessage(EncryptionKey paramEncryptionKey1, KerberosTime paramKerberosTime, int paramInt, EncryptionKey paramEncryptionKey2, SeqNumber paramSeqNumber)
/*     */     throws Asn1Exception, IOException, KdcErrException, KrbCryptoException
/*     */   {
/* 182 */     Integer localInteger = null;
/*     */ 
/* 184 */     if (paramSeqNumber != null) {
/* 185 */       localInteger = new Integer(paramSeqNumber.current());
/*     */     }
/* 187 */     this.encPart = new EncAPRepPart(paramKerberosTime, paramInt, paramEncryptionKey2, localInteger);
/*     */ 
/* 192 */     byte[] arrayOfByte = this.encPart.asn1Encode();
/*     */ 
/* 194 */     EncryptedData localEncryptedData = new EncryptedData(paramEncryptionKey1, arrayOfByte, 12);
/*     */ 
/* 197 */     this.apRepMessg = new APRep(localEncryptedData);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbApRep
 * JD-Core Version:    0.6.2
 */