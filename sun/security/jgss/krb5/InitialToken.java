/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ import javax.security.auth.kerberos.DelegationPermission;
/*     */ import org.ietf.jgss.ChannelBinding;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import sun.security.jgss.GSSToken;
/*     */ import sun.security.krb5.Checksum;
/*     */ import sun.security.krb5.Credentials;
/*     */ import sun.security.krb5.EncryptionKey;
/*     */ import sun.security.krb5.KrbCred;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ 
/*     */ abstract class InitialToken extends Krb5Token
/*     */ {
/*     */   private static final int CHECKSUM_TYPE = 32771;
/*     */   private static final int CHECKSUM_LENGTH_SIZE = 4;
/*     */   private static final int CHECKSUM_BINDINGS_SIZE = 16;
/*     */   private static final int CHECKSUM_FLAGS_SIZE = 4;
/*     */   private static final int CHECKSUM_DELEG_OPT_SIZE = 2;
/*     */   private static final int CHECKSUM_DELEG_LGTH_SIZE = 2;
/*     */   private static final int CHECKSUM_DELEG_FLAG = 1;
/*     */   private static final int CHECKSUM_MUTUAL_FLAG = 2;
/*     */   private static final int CHECKSUM_REPLAY_FLAG = 4;
/*     */   private static final int CHECKSUM_SEQUENCE_FLAG = 8;
/*     */   private static final int CHECKSUM_CONF_FLAG = 16;
/*     */   private static final int CHECKSUM_INTEG_FLAG = 32;
/*     */   private final byte[] CHECKSUM_FIRST_BYTES;
/*     */   private static final int CHANNEL_BINDING_AF_INET = 2;
/*     */   private static final int CHANNEL_BINDING_AF_INET6 = 24;
/*     */   private static final int CHANNEL_BINDING_AF_NULL_ADDR = 255;
/*     */   private static final int Inet4_ADDRSZ = 4;
/*     */   private static final int Inet6_ADDRSZ = 16;
/*     */ 
/*     */   InitialToken()
/*     */   {
/*  57 */     this.CHECKSUM_FIRST_BYTES = new byte[] { 16, 0, 0, 0 };
/*     */   }
/*     */ 
/*     */   private int getAddrType(InetAddress paramInetAddress)
/*     */   {
/* 344 */     int i = 255;
/*     */ 
/* 346 */     if ((paramInetAddress instanceof Inet4Address))
/* 347 */       i = 2;
/* 348 */     else if ((paramInetAddress instanceof Inet6Address))
/* 349 */       i = 24;
/* 350 */     return i;
/*     */   }
/*     */ 
/*     */   private byte[] getAddrBytes(InetAddress paramInetAddress) throws GSSException {
/* 354 */     int i = getAddrType(paramInetAddress);
/* 355 */     byte[] arrayOfByte = paramInetAddress.getAddress();
/* 356 */     if (arrayOfByte != null) {
/* 357 */       switch (i) {
/*     */       case 2:
/* 359 */         if (arrayOfByte.length != 4) {
/* 360 */           throw new GSSException(11, -1, "Incorrect AF-INET address length in ChannelBinding.");
/*     */         }
/*     */ 
/* 363 */         return arrayOfByte;
/*     */       case 24:
/* 365 */         if (arrayOfByte.length != 16) {
/* 366 */           throw new GSSException(11, -1, "Incorrect AF-INET6 address length in ChannelBinding.");
/*     */         }
/*     */ 
/* 369 */         return arrayOfByte;
/*     */       }
/* 371 */       throw new GSSException(11, -1, "Cannot handle non AF-INET addresses in ChannelBinding.");
/*     */     }
/*     */ 
/* 375 */     return null;
/*     */   }
/*     */ 
/*     */   private byte[] computeChannelBinding(ChannelBinding paramChannelBinding)
/*     */     throws GSSException
/*     */   {
/* 381 */     InetAddress localInetAddress1 = paramChannelBinding.getInitiatorAddress();
/* 382 */     InetAddress localInetAddress2 = paramChannelBinding.getAcceptorAddress();
/* 383 */     int i = 20;
/*     */ 
/* 385 */     int j = getAddrType(localInetAddress1);
/* 386 */     int k = getAddrType(localInetAddress2);
/*     */ 
/* 388 */     byte[] arrayOfByte1 = null;
/* 389 */     if (localInetAddress1 != null) {
/* 390 */       arrayOfByte1 = getAddrBytes(localInetAddress1);
/* 391 */       i += arrayOfByte1.length;
/*     */     }
/*     */ 
/* 394 */     byte[] arrayOfByte2 = null;
/* 395 */     if (localInetAddress2 != null) {
/* 396 */       arrayOfByte2 = getAddrBytes(localInetAddress2);
/* 397 */       i += arrayOfByte2.length;
/*     */     }
/*     */ 
/* 400 */     byte[] arrayOfByte3 = paramChannelBinding.getApplicationData();
/* 401 */     if (arrayOfByte3 != null) {
/* 402 */       i += arrayOfByte3.length;
/*     */     }
/*     */ 
/* 405 */     byte[] arrayOfByte4 = new byte[i];
/*     */ 
/* 407 */     int m = 0;
/*     */ 
/* 409 */     writeLittleEndian(j, arrayOfByte4, m);
/* 410 */     m += 4;
/*     */ 
/* 412 */     if (arrayOfByte1 != null) {
/* 413 */       writeLittleEndian(arrayOfByte1.length, arrayOfByte4, m);
/* 414 */       m += 4;
/* 415 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte4, m, arrayOfByte1.length);
/*     */ 
/* 417 */       m += arrayOfByte1.length;
/*     */     }
/*     */     else {
/* 420 */       m += 4;
/*     */     }
/*     */ 
/* 423 */     writeLittleEndian(k, arrayOfByte4, m);
/* 424 */     m += 4;
/*     */ 
/* 426 */     if (arrayOfByte2 != null) {
/* 427 */       writeLittleEndian(arrayOfByte2.length, arrayOfByte4, m);
/* 428 */       m += 4;
/* 429 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte4, m, arrayOfByte2.length);
/*     */ 
/* 431 */       m += arrayOfByte2.length;
/*     */     }
/*     */     else {
/* 434 */       m += 4;
/*     */     }
/*     */ 
/* 437 */     if (arrayOfByte3 != null) {
/* 438 */       writeLittleEndian(arrayOfByte3.length, arrayOfByte4, m);
/* 439 */       m += 4;
/* 440 */       System.arraycopy(arrayOfByte3, 0, arrayOfByte4, m, arrayOfByte3.length);
/*     */ 
/* 442 */       m += arrayOfByte3.length;
/*     */     }
/*     */     else {
/* 445 */       m += 4;
/*     */     }
/*     */     try
/*     */     {
/* 449 */       MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
/* 450 */       return localMessageDigest.digest(arrayOfByte4);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 452 */       throw new GSSException(11, -1, "Could not get MD5 Message Digest - " + localNoSuchAlgorithmException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract byte[] encode()
/*     */     throws IOException;
/*     */ 
/*     */   protected class OverloadedChecksum
/*     */   {
/*  69 */     private byte[] checksumBytes = null;
/*  70 */     private Credentials delegCreds = null;
/*  71 */     private int flags = 0;
/*     */ 
/*     */     public OverloadedChecksum(Krb5Context paramCredentials1, Credentials paramCredentials2, Credentials arg4)
/*     */       throws KrbException, IOException, GSSException
/*     */     {
/*  82 */       byte[] arrayOfByte = null;
/*  83 */       int i = 0;
/*  84 */       int j = 24;
/*     */       Credentials localCredentials;
/*  87 */       if (!paramCredentials2.isForwardable()) {
/*  88 */         paramCredentials1.setCredDelegState(false);
/*  89 */         paramCredentials1.setDelegPolicyState(false);
/*  90 */       } else if (paramCredentials1.getCredDelegState()) {
/*  91 */         if ((paramCredentials1.getDelegPolicyState()) && 
/*  92 */           (!localCredentials.checkDelegate()))
/*     */         {
/*  94 */           paramCredentials1.setDelegPolicyState(false);
/*     */         }
/*     */       }
/*  97 */       else if (paramCredentials1.getDelegPolicyState()) {
/*  98 */         if (localCredentials.checkDelegate())
/*  99 */           paramCredentials1.setCredDelegState(true);
/*     */         else {
/* 101 */           paramCredentials1.setDelegPolicyState(false);
/*     */         }
/*     */       }
/*     */ 
/* 105 */       if (paramCredentials1.getCredDelegState()) {
/* 106 */         localObject1 = null;
/* 107 */         localObject2 = paramCredentials1.getCipherHelper(localCredentials.getSessionKey());
/*     */ 
/* 109 */         if (useNullKey((CipherHelper)localObject2)) {
/* 110 */           localObject1 = new KrbCred(paramCredentials2, localCredentials, EncryptionKey.NULL_KEY);
/*     */         }
/*     */         else {
/* 113 */           localObject1 = new KrbCred(paramCredentials2, localCredentials, localCredentials.getSessionKey());
/*     */         }
/*     */ 
/* 116 */         arrayOfByte = ((KrbCred)localObject1).getMessage();
/* 117 */         j += 4 + arrayOfByte.length;
/*     */       }
/*     */ 
/* 122 */       this.checksumBytes = new byte[j];
/*     */ 
/* 124 */       this.checksumBytes[(i++)] = InitialToken.this.CHECKSUM_FIRST_BYTES[0];
/* 125 */       this.checksumBytes[(i++)] = InitialToken.this.CHECKSUM_FIRST_BYTES[1];
/* 126 */       this.checksumBytes[(i++)] = InitialToken.this.CHECKSUM_FIRST_BYTES[2];
/* 127 */       this.checksumBytes[(i++)] = InitialToken.this.CHECKSUM_FIRST_BYTES[3];
/*     */ 
/* 129 */       Object localObject1 = paramCredentials1.getChannelBinding();
/* 130 */       if (localObject1 != null) {
/* 131 */         localObject2 = InitialToken.this.computeChannelBinding(paramCredentials1.getChannelBinding());
/*     */ 
/* 133 */         System.arraycopy(localObject2, 0, this.checksumBytes, i, localObject2.length);
/*     */       }
/*     */ 
/* 139 */       i += 16;
/*     */ 
/* 141 */       if (paramCredentials1.getCredDelegState())
/* 142 */         this.flags |= 1;
/* 143 */       if (paramCredentials1.getMutualAuthState())
/* 144 */         this.flags |= 2;
/* 145 */       if (paramCredentials1.getReplayDetState())
/* 146 */         this.flags |= 4;
/* 147 */       if (paramCredentials1.getSequenceDetState())
/* 148 */         this.flags |= 8;
/* 149 */       if (paramCredentials1.getIntegState())
/* 150 */         this.flags |= 32;
/* 151 */       if (paramCredentials1.getConfState()) {
/* 152 */         this.flags |= 16;
/*     */       }
/* 154 */       Object localObject2 = new byte[4];
/* 155 */       GSSToken.writeLittleEndian(this.flags, (byte[])localObject2);
/* 156 */       this.checksumBytes[(i++)] = localObject2[0];
/* 157 */       this.checksumBytes[(i++)] = localObject2[1];
/* 158 */       this.checksumBytes[(i++)] = localObject2[2];
/* 159 */       this.checksumBytes[(i++)] = localObject2[3];
/*     */ 
/* 161 */       if (paramCredentials1.getCredDelegState())
/*     */       {
/* 163 */         PrincipalName localPrincipalName = localCredentials.getServer();
/*     */ 
/* 167 */         StringBuffer localStringBuffer = new StringBuffer("\"");
/* 168 */         localStringBuffer.append(localPrincipalName.getName()).append('"');
/* 169 */         String str = localPrincipalName.getRealmAsString();
/* 170 */         localStringBuffer.append(" \"krbtgt/").append(str).append('@');
/* 171 */         localStringBuffer.append(str).append('"');
/* 172 */         SecurityManager localSecurityManager = System.getSecurityManager();
/* 173 */         if (localSecurityManager != null) {
/* 174 */           DelegationPermission localDelegationPermission = new DelegationPermission(localStringBuffer.toString());
/*     */ 
/* 176 */           localSecurityManager.checkPermission(localDelegationPermission);
/*     */         }
/*     */ 
/* 185 */         this.checksumBytes[(i++)] = 1;
/* 186 */         this.checksumBytes[(i++)] = 0;
/*     */ 
/* 193 */         if (arrayOfByte.length > 65535) {
/* 194 */           throw new GSSException(11, -1, "Incorrect messsage length");
/*     */         }
/*     */ 
/* 197 */         GSSToken.writeLittleEndian(arrayOfByte.length, (byte[])localObject2);
/* 198 */         this.checksumBytes[(i++)] = localObject2[0];
/* 199 */         this.checksumBytes[(i++)] = localObject2[1];
/* 200 */         System.arraycopy(arrayOfByte, 0, this.checksumBytes, i, arrayOfByte.length);
/*     */       }
/*     */     }
/*     */ 
/*     */     public OverloadedChecksum(Krb5Context paramChecksum, Checksum paramEncryptionKey1, EncryptionKey paramEncryptionKey2, EncryptionKey arg5)
/*     */       throws GSSException, KrbException, IOException
/*     */     {
/* 217 */       int i = 0;
/*     */ 
/* 219 */       if (paramEncryptionKey1 == null) {
/* 220 */         localObject1 = new GSSException(11, -1, "No cksum in AP_REQ's authenticator");
/*     */ 
/* 222 */         ((GSSException)localObject1).initCause(new KrbException(50));
/* 223 */         throw ((Throwable)localObject1);
/*     */       }
/* 225 */       this.checksumBytes = paramEncryptionKey1.getBytes();
/*     */ 
/* 227 */       if ((this.checksumBytes[0] != InitialToken.this.CHECKSUM_FIRST_BYTES[0]) || (this.checksumBytes[1] != InitialToken.this.CHECKSUM_FIRST_BYTES[1]) || (this.checksumBytes[2] != InitialToken.this.CHECKSUM_FIRST_BYTES[2]) || (this.checksumBytes[3] != InitialToken.this.CHECKSUM_FIRST_BYTES[3]))
/*     */       {
/* 231 */         throw new GSSException(11, -1, "Incorrect checksum");
/*     */       }
/*     */ 
/* 235 */       Object localObject1 = paramChecksum.getChannelBinding();
/*     */       byte[] arrayOfByte2;
/*     */       Object localObject2;
/* 246 */       if (localObject1 != null) {
/* 247 */         byte[] arrayOfByte1 = new byte[16];
/* 248 */         System.arraycopy(this.checksumBytes, 4, arrayOfByte1, 0, 16);
/*     */ 
/* 251 */         arrayOfByte2 = new byte[16];
/* 252 */         if (!Arrays.equals(arrayOfByte2, arrayOfByte1)) {
/* 253 */           localObject2 = InitialToken.this.computeChannelBinding((ChannelBinding)localObject1);
/*     */ 
/* 255 */           if (!Arrays.equals((byte[])localObject2, arrayOfByte1))
/*     */           {
/* 257 */             throw new GSSException(1, -1, "Bytes mismatch!");
/*     */           }
/*     */         }
/*     */         else {
/* 261 */           throw new GSSException(1, -1, "Token missing ChannelBinding!");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 266 */       this.flags = GSSToken.readLittleEndian(this.checksumBytes, 20, 4);
/*     */ 
/* 268 */       if ((this.flags & 0x1) > 0)
/*     */       {
/* 276 */         int j = GSSToken.readLittleEndian(this.checksumBytes, 26, 2);
/* 277 */         arrayOfByte2 = new byte[j];
/* 278 */         System.arraycopy(this.checksumBytes, 28, arrayOfByte2, 0, j);
/*     */ 
/* 280 */         localObject2 = paramChecksum.getCipherHelper(paramEncryptionKey2);
/* 281 */         if (useNullKey((CipherHelper)localObject2)) {
/* 282 */           this.delegCreds = new KrbCred(arrayOfByte2, EncryptionKey.NULL_KEY).getDelegatedCreds()[0];
/*     */         }
/*     */         else
/*     */         {
/*     */           KrbCred localKrbCred;
/*     */           try {
/* 288 */             localKrbCred = new KrbCred(arrayOfByte2, paramEncryptionKey2);
/*     */           }
/*     */           catch (KrbException localKrbException)
/*     */           {
/*     */             EncryptionKey localEncryptionKey;
/* 290 */             if (localEncryptionKey != null)
/* 291 */               localKrbCred = new KrbCred(arrayOfByte2, localEncryptionKey);
/*     */             else {
/* 293 */               throw localKrbException;
/*     */             }
/*     */           }
/* 296 */           this.delegCreds = localKrbCred.getDelegatedCreds()[0];
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private boolean useNullKey(CipherHelper paramCipherHelper)
/*     */     {
/* 303 */       boolean bool = true;
/*     */ 
/* 305 */       if ((paramCipherHelper.getProto() == 1) || (paramCipherHelper.isArcFour())) {
/* 306 */         bool = false;
/*     */       }
/* 308 */       return bool;
/*     */     }
/*     */ 
/*     */     public Checksum getChecksum() throws KrbException {
/* 312 */       return new Checksum(this.checksumBytes, 32771);
/*     */     }
/*     */ 
/*     */     public Credentials getDelegatedCreds() {
/* 316 */       return this.delegCreds;
/*     */     }
/*     */ 
/*     */     public void setContextFlags(Krb5Context paramKrb5Context)
/*     */     {
/* 322 */       if ((this.flags & 0x1) > 0) {
/* 323 */         paramKrb5Context.setCredDelegState(true);
/*     */       }
/* 325 */       if ((this.flags & 0x2) == 0) {
/* 326 */         paramKrb5Context.setMutualAuthState(false);
/*     */       }
/* 328 */       if ((this.flags & 0x4) == 0) {
/* 329 */         paramKrb5Context.setReplayDetState(false);
/*     */       }
/* 331 */       if ((this.flags & 0x8) == 0) {
/* 332 */         paramKrb5Context.setSequenceDetState(false);
/*     */       }
/* 334 */       if ((this.flags & 0x10) == 0) {
/* 335 */         paramKrb5Context.setConfState(false);
/*     */       }
/* 337 */       if ((this.flags & 0x20) == 0)
/* 338 */         paramKrb5Context.setIntegState(false);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.InitialToken
 * JD-Core Version:    0.6.2
 */