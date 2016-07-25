/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetAddress;
/*     */ import org.ietf.jgss.ChannelBinding;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import sun.security.krb5.Checksum;
/*     */ import sun.security.krb5.Credentials;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbApReq;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.internal.AuthorizationData;
/*     */ import sun.security.krb5.internal.KerberosTime;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ class InitSecContextToken extends InitialToken
/*     */ {
/*  39 */   private KrbApReq apReq = null;
/*     */ 
/*     */   InitSecContextToken(Krb5Context paramKrb5Context, Credentials paramCredentials1, Credentials paramCredentials2)
/*     */     throws KrbException, IOException, GSSException
/*     */   {
/*  55 */     boolean bool1 = paramKrb5Context.getMutualAuthState();
/*  56 */     boolean bool2 = true;
/*  57 */     boolean bool3 = true;
/*     */ 
/*  59 */     InitialToken.OverloadedChecksum localOverloadedChecksum = new InitialToken.OverloadedChecksum(this, paramKrb5Context, paramCredentials1, paramCredentials2);
/*     */ 
/*  62 */     Checksum localChecksum = localOverloadedChecksum.getChecksum();
/*     */ 
/*  64 */     paramKrb5Context.setTktFlags(paramCredentials2.getFlags());
/*  65 */     paramKrb5Context.setAuthTime(new KerberosTime(paramCredentials2.getAuthTime()).toString());
/*     */ 
/*  67 */     this.apReq = new KrbApReq(paramCredentials2, bool1, bool2, bool3, localChecksum);
/*     */ 
/*  73 */     paramKrb5Context.resetMySequenceNumber(this.apReq.getSeqNumber().intValue());
/*     */ 
/*  75 */     EncryptionKey localEncryptionKey = this.apReq.getSubKey();
/*  76 */     if (localEncryptionKey != null)
/*  77 */       paramKrb5Context.setKey(1, localEncryptionKey);
/*     */     else {
/*  79 */       paramKrb5Context.setKey(0, paramCredentials2.getSessionKey());
/*     */     }
/*  81 */     if (!bool1)
/*  82 */       paramKrb5Context.resetPeerSequenceNumber(0);
/*     */   }
/*     */ 
/*     */   InitSecContextToken(Krb5Context paramKrb5Context, EncryptionKey[] paramArrayOfEncryptionKey, InputStream paramInputStream)
/*     */     throws IOException, GSSException, KrbException
/*     */   {
/*  93 */     int i = paramInputStream.read() << 8 | paramInputStream.read();
/*     */ 
/*  95 */     if (i != 256) {
/*  96 */       throw new GSSException(10, -1, "AP_REQ token id does not match!");
/*     */     }
/*     */ 
/* 100 */     byte[] arrayOfByte = new DerValue(paramInputStream).toByteArray();
/*     */ 
/* 104 */     InetAddress localInetAddress = null;
/* 105 */     if (paramKrb5Context.getChannelBinding() != null) {
/* 106 */       localInetAddress = paramKrb5Context.getChannelBinding().getInitiatorAddress();
/*     */     }
/* 108 */     this.apReq = new KrbApReq(arrayOfByte, paramArrayOfEncryptionKey, localInetAddress);
/*     */ 
/* 111 */     EncryptionKey localEncryptionKey1 = this.apReq.getCreds().getSessionKey();
/*     */ 
/* 118 */     EncryptionKey localEncryptionKey2 = this.apReq.getSubKey();
/* 119 */     if (localEncryptionKey2 != null) {
/* 120 */       paramKrb5Context.setKey(1, localEncryptionKey2);
/*     */     }
/*     */     else
/*     */     {
/* 126 */       paramKrb5Context.setKey(0, localEncryptionKey1);
/*     */     }
/*     */ 
/* 130 */     InitialToken.OverloadedChecksum localOverloadedChecksum = new InitialToken.OverloadedChecksum(this, paramKrb5Context, this.apReq.getChecksum(), localEncryptionKey1, localEncryptionKey2);
/*     */ 
/* 132 */     localOverloadedChecksum.setContextFlags(paramKrb5Context);
/* 133 */     Credentials localCredentials = localOverloadedChecksum.getDelegatedCreds();
/* 134 */     if (localCredentials != null) {
/* 135 */       localObject = Krb5InitCredential.getInstance((Krb5NameElement)paramKrb5Context.getSrcName(), localCredentials);
/*     */ 
/* 139 */       paramKrb5Context.setDelegCred((Krb5CredElement)localObject);
/*     */     }
/*     */ 
/* 142 */     Object localObject = this.apReq.getSeqNumber();
/* 143 */     int j = localObject != null ? ((Integer)localObject).intValue() : 0;
/*     */ 
/* 146 */     paramKrb5Context.resetPeerSequenceNumber(j);
/* 147 */     if (!paramKrb5Context.getMutualAuthState())
/*     */     {
/* 150 */       paramKrb5Context.resetMySequenceNumber(j);
/* 151 */     }paramKrb5Context.setAuthTime(new KerberosTime(this.apReq.getCreds().getAuthTime()).toString());
/*     */ 
/* 153 */     paramKrb5Context.setTktFlags(this.apReq.getCreds().getFlags());
/* 154 */     AuthorizationData localAuthorizationData = this.apReq.getCreds().getAuthzData();
/* 155 */     if (localAuthorizationData == null) {
/* 156 */       paramKrb5Context.setAuthzData(null);
/*     */     } else {
/* 158 */       com.sun.security.jgss.AuthorizationDataEntry[] arrayOfAuthorizationDataEntry = new com.sun.security.jgss.AuthorizationDataEntry[localAuthorizationData.count()];
/*     */ 
/* 160 */       for (int k = 0; k < localAuthorizationData.count(); k++) {
/* 161 */         arrayOfAuthorizationDataEntry[k] = new com.sun.security.jgss.AuthorizationDataEntry(localAuthorizationData.item(k).adType, localAuthorizationData.item(k).adData);
/*     */       }
/*     */ 
/* 164 */       paramKrb5Context.setAuthzData(arrayOfAuthorizationDataEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final KrbApReq getKrbApReq() {
/* 169 */     return this.apReq;
/*     */   }
/*     */ 
/*     */   public final byte[] encode() throws IOException {
/* 173 */     byte[] arrayOfByte1 = this.apReq.getMessage();
/* 174 */     byte[] arrayOfByte2 = new byte[2 + arrayOfByte1.length];
/* 175 */     writeInt(256, arrayOfByte2, 0);
/* 176 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 2, arrayOfByte1.length);
/*     */ 
/* 179 */     return arrayOfByte2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.InitSecContextToken
 * JD-Core Version:    0.6.2
 */