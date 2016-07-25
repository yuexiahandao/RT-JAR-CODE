/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import sun.security.krb5.Credentials;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbApRep;
/*     */ import sun.security.krb5.KrbApReq;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ class AcceptSecContextToken extends InitialToken
/*     */ {
/*  37 */   private KrbApRep apRep = null;
/*     */ 
/*     */   public AcceptSecContextToken(Krb5Context paramKrb5Context, KrbApReq paramKrbApReq)
/*     */     throws KrbException, IOException
/*     */   {
/*  57 */     boolean bool1 = false;
/*     */ 
/*  59 */     boolean bool2 = true;
/*     */ 
/*  61 */     this.apRep = new KrbApRep(paramKrbApReq, bool2, bool1);
/*     */ 
/*  63 */     paramKrb5Context.resetMySequenceNumber(this.apRep.getSeqNumber().intValue());
/*     */   }
/*     */ 
/*     */   public AcceptSecContextToken(Krb5Context paramKrb5Context, Credentials paramCredentials, KrbApReq paramKrbApReq, InputStream paramInputStream)
/*     */     throws IOException, GSSException, KrbException
/*     */   {
/*  80 */     int i = paramInputStream.read() << 8 | paramInputStream.read();
/*     */ 
/*  82 */     if (i != 512) {
/*  83 */       throw new GSSException(10, -1, "AP_REP token id does not match!");
/*     */     }
/*     */ 
/*  86 */     byte[] arrayOfByte = new DerValue(paramInputStream).toByteArray();
/*     */ 
/*  89 */     KrbApRep localKrbApRep = new KrbApRep(arrayOfByte, paramCredentials, paramKrbApReq);
/*     */ 
/*  95 */     EncryptionKey localEncryptionKey = localKrbApRep.getSubKey();
/*  96 */     if (localEncryptionKey != null) {
/*  97 */       paramKrb5Context.setKey(2, localEncryptionKey);
/*     */     }
/*     */ 
/* 104 */     Integer localInteger = localKrbApRep.getSeqNumber();
/* 105 */     int j = localInteger != null ? localInteger.intValue() : 0;
/*     */ 
/* 108 */     paramKrb5Context.resetPeerSequenceNumber(j);
/*     */   }
/*     */ 
/*     */   public final byte[] encode() throws IOException {
/* 112 */     byte[] arrayOfByte1 = this.apRep.getMessage();
/* 113 */     byte[] arrayOfByte2 = new byte[2 + arrayOfByte1.length];
/* 114 */     writeInt(512, arrayOfByte2, 0);
/* 115 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 2, arrayOfByte1.length);
/* 116 */     return arrayOfByte2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.AcceptSecContextToken
 * JD-Core Version:    0.6.2
 */