/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ import sun.security.jgss.GSSHeader;
/*     */ import sun.security.jgss.GSSToken;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ abstract class MessageToken extends Krb5Token
/*     */ {
/*     */   private static final int TOKEN_NO_CKSUM_SIZE = 16;
/*     */   private static final int FILLER = 65535;
/*     */   static final int SGN_ALG_DES_MAC_MD5 = 0;
/*     */   static final int SGN_ALG_DES_MAC = 512;
/*     */   static final int SGN_ALG_HMAC_SHA1_DES3_KD = 1024;
/*     */   static final int SEAL_ALG_NONE = 65535;
/*     */   static final int SEAL_ALG_DES = 0;
/*     */   static final int SEAL_ALG_DES3_KD = 512;
/*     */   static final int SEAL_ALG_ARCFOUR_HMAC = 4096;
/*     */   static final int SGN_ALG_HMAC_MD5_ARCFOUR = 4352;
/*     */   private static final int TOKEN_ID_POS = 0;
/*     */   private static final int SIGN_ALG_POS = 2;
/*     */   private static final int SEAL_ALG_POS = 4;
/*     */   private int seqNumber;
/* 130 */   private boolean confState = true;
/* 131 */   private boolean initiator = true;
/*     */ 
/* 133 */   private int tokenId = 0;
/* 134 */   private GSSHeader gssHeader = null;
/* 135 */   private MessageTokenHeader tokenHeader = null;
/* 136 */   private byte[] checksum = null;
/* 137 */   private byte[] encSeqNumber = null;
/* 138 */   private byte[] seqNumberData = null;
/*     */ 
/* 141 */   CipherHelper cipherHelper = null;
/*     */ 
/*     */   MessageToken(int paramInt1, Krb5Context paramKrb5Context, byte[] paramArrayOfByte, int paramInt2, int paramInt3, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 161 */     this(paramInt1, paramKrb5Context, new ByteArrayInputStream(paramArrayOfByte, paramInt2, paramInt3), paramMessageProp);
/*     */   }
/*     */ 
/*     */   MessageToken(int paramInt, Krb5Context paramKrb5Context, InputStream paramInputStream, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 182 */     init(paramInt, paramKrb5Context);
/*     */     try
/*     */     {
/* 185 */       this.gssHeader = new GSSHeader(paramInputStream);
/*     */ 
/* 187 */       if (!this.gssHeader.getOid().equals(OID)) {
/* 188 */         throw new GSSException(10, -1, getTokenName(paramInt));
/*     */       }
/*     */ 
/* 191 */       if (!this.confState) {
/* 192 */         paramMessageProp.setPrivacy(false);
/*     */       }
/*     */ 
/* 195 */       this.tokenHeader = new MessageTokenHeader(paramInputStream, paramMessageProp);
/*     */ 
/* 197 */       this.encSeqNumber = new byte[8];
/* 198 */       readFully(paramInputStream, this.encSeqNumber);
/*     */ 
/* 203 */       this.checksum = new byte[this.cipherHelper.getChecksumLength()];
/* 204 */       readFully(paramInputStream, this.checksum);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 211 */       throw new GSSException(10, -1, getTokenName(paramInt) + ":" + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public final GSSHeader getGSSHeader()
/*     */   {
/* 221 */     return this.gssHeader;
/*     */   }
/*     */ 
/*     */   public final int getTokenId()
/*     */   {
/* 229 */     return this.tokenId;
/*     */   }
/*     */ 
/*     */   public final byte[] getEncSeqNumber()
/*     */   {
/* 237 */     return this.encSeqNumber;
/*     */   }
/*     */ 
/*     */   public final byte[] getChecksum()
/*     */   {
/* 245 */     return this.checksum;
/*     */   }
/*     */ 
/*     */   public final boolean getConfState()
/*     */   {
/* 254 */     return this.confState;
/*     */   }
/*     */ 
/*     */   public void genSignAndSeqNumber(MessageProp paramMessageProp, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte3)
/*     */     throws GSSException
/*     */   {
/* 299 */     int i = paramMessageProp.getQOP();
/* 300 */     if (i != 0) {
/* 301 */       i = 0;
/* 302 */       paramMessageProp.setQOP(i);
/*     */     }
/*     */ 
/* 305 */     if (!this.confState) {
/* 306 */       paramMessageProp.setPrivacy(false);
/*     */     }
/*     */ 
/* 311 */     this.tokenHeader = new MessageTokenHeader(this.tokenId, paramMessageProp.getPrivacy(), i);
/*     */ 
/* 316 */     this.checksum = getChecksum(paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2, paramArrayOfByte3);
/*     */ 
/* 324 */     this.seqNumberData = new byte[8];
/*     */ 
/* 328 */     if (this.cipherHelper.isArcFour()) {
/* 329 */       writeBigEndian(this.seqNumber, this.seqNumberData);
/*     */     }
/*     */     else {
/* 332 */       writeLittleEndian(this.seqNumber, this.seqNumberData);
/*     */     }
/* 334 */     if (!this.initiator) {
/* 335 */       this.seqNumberData[4] = -1;
/* 336 */       this.seqNumberData[5] = -1;
/* 337 */       this.seqNumberData[6] = -1;
/* 338 */       this.seqNumberData[7] = -1;
/*     */     }
/*     */ 
/* 341 */     this.encSeqNumber = this.cipherHelper.encryptSeq(this.checksum, this.seqNumberData, 0, 8);
/*     */   }
/*     */ 
/*     */   public final boolean verifySignAndSeqNumber(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte3)
/*     */     throws GSSException
/*     */   {
/* 375 */     byte[] arrayOfByte = getChecksum(paramArrayOfByte1, paramArrayOfByte2, paramInt1, paramInt2, paramArrayOfByte3);
/*     */ 
/* 381 */     if (MessageDigest.isEqual(this.checksum, arrayOfByte))
/*     */     {
/* 383 */       this.seqNumberData = this.cipherHelper.decryptSeq(this.checksum, this.encSeqNumber, 0, 8);
/*     */ 
/* 395 */       int i = 0;
/* 396 */       if (this.initiator) {
/* 397 */         i = -1;
/*     */       }
/* 399 */       if ((this.seqNumberData[4] == i) && (this.seqNumberData[5] == i) && (this.seqNumberData[6] == i) && (this.seqNumberData[7] == i))
/*     */       {
/* 403 */         return true;
/*     */       }
/*     */     }
/* 406 */     return false;
/*     */   }
/*     */ 
/*     */   public final int getSequenceNumber()
/*     */   {
/* 411 */     int i = 0;
/* 412 */     if (this.cipherHelper.isArcFour())
/* 413 */       i = readBigEndian(this.seqNumberData, 0, 4);
/*     */     else {
/* 415 */       i = readLittleEndian(this.seqNumberData, 0, 4);
/*     */     }
/* 417 */     return i;
/*     */   }
/*     */ 
/*     */   private byte[] getChecksum(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt1, int paramInt2, byte[] paramArrayOfByte3)
/*     */     throws GSSException
/*     */   {
/* 454 */     byte[] arrayOfByte1 = this.tokenHeader.getBytes();
/* 455 */     byte[] arrayOfByte2 = paramArrayOfByte1;
/* 456 */     byte[] arrayOfByte3 = arrayOfByte1;
/*     */ 
/* 458 */     if (arrayOfByte2 != null) {
/* 459 */       arrayOfByte3 = new byte[arrayOfByte1.length + arrayOfByte2.length];
/*     */ 
/* 461 */       System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, arrayOfByte1.length);
/*     */ 
/* 463 */       System.arraycopy(arrayOfByte2, 0, arrayOfByte3, arrayOfByte1.length, arrayOfByte2.length);
/*     */     }
/*     */ 
/* 468 */     return this.cipherHelper.calculateChecksum(this.tokenHeader.getSignAlg(), arrayOfByte3, paramArrayOfByte3, paramArrayOfByte2, paramInt1, paramInt2, this.tokenId);
/*     */   }
/*     */ 
/*     */   MessageToken(int paramInt, Krb5Context paramKrb5Context)
/*     */     throws GSSException
/*     */   {
/* 491 */     init(paramInt, paramKrb5Context);
/* 492 */     this.seqNumber = paramKrb5Context.incrementMySequenceNumber();
/*     */   }
/*     */ 
/*     */   private void init(int paramInt, Krb5Context paramKrb5Context) throws GSSException {
/* 496 */     this.tokenId = paramInt;
/*     */ 
/* 498 */     this.confState = paramKrb5Context.getConfState();
/*     */ 
/* 500 */     this.initiator = paramKrb5Context.isInitiator();
/*     */ 
/* 502 */     this.cipherHelper = paramKrb5Context.getCipherHelper(null);
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException, GSSException
/*     */   {
/* 513 */     this.gssHeader = new GSSHeader(OID, getKrb5TokenSize());
/* 514 */     this.gssHeader.encode(paramOutputStream);
/* 515 */     this.tokenHeader.encode(paramOutputStream);
/*     */ 
/* 517 */     paramOutputStream.write(this.encSeqNumber);
/*     */ 
/* 519 */     paramOutputStream.write(this.checksum);
/*     */   }
/*     */ 
/*     */   protected int getKrb5TokenSize()
/*     */     throws GSSException
/*     */   {
/* 528 */     return getTokenSize();
/*     */   }
/*     */ 
/*     */   protected final int getTokenSize() throws GSSException {
/* 532 */     return 16 + this.cipherHelper.getChecksumLength();
/*     */   }
/*     */ 
/*     */   protected static final int getTokenSize(CipherHelper paramCipherHelper) throws GSSException
/*     */   {
/* 537 */     return 16 + paramCipherHelper.getChecksumLength();
/*     */   }
/*     */ 
/*     */   protected abstract int getSealAlg(boolean paramBoolean, int paramInt)
/*     */     throws GSSException;
/*     */ 
/*     */   protected int getSgnAlg(int paramInt)
/*     */     throws GSSException
/*     */   {
/* 721 */     return this.cipherHelper.getSgnAlg();
/*     */   }
/*     */ 
/*     */   class MessageTokenHeader
/*     */   {
/*     */     private int tokenId;
/*     */     private int signAlg;
/*     */     private int sealAlg;
/* 595 */     private byte[] bytes = new byte[8];
/*     */ 
/*     */     public MessageTokenHeader(int paramBoolean, boolean paramInt1, int arg4)
/*     */       throws GSSException
/*     */     {
/* 610 */       this.tokenId = paramBoolean;
/*     */       int i;
/* 612 */       this.signAlg = MessageToken.this.getSgnAlg(i);
/*     */ 
/* 614 */       this.sealAlg = MessageToken.this.getSealAlg(paramInt1, i);
/*     */ 
/* 616 */       this.bytes[0] = ((byte)(paramBoolean >>> true));
/* 617 */       this.bytes[1] = ((byte)paramBoolean);
/*     */ 
/* 619 */       this.bytes[2] = ((byte)(this.signAlg >>> 8));
/* 620 */       this.bytes[3] = ((byte)this.signAlg);
/*     */ 
/* 622 */       this.bytes[4] = ((byte)(this.sealAlg >>> 8));
/* 623 */       this.bytes[5] = ((byte)this.sealAlg);
/*     */ 
/* 625 */       this.bytes[6] = -1;
/* 626 */       this.bytes[7] = -1;
/*     */     }
/*     */ 
/*     */     public MessageTokenHeader(InputStream paramMessageProp, MessageProp arg3)
/*     */       throws IOException
/*     */     {
/* 641 */       GSSToken.readFully(paramMessageProp, this.bytes);
/* 642 */       this.tokenId = GSSToken.readInt(this.bytes, 0);
/* 643 */       this.signAlg = GSSToken.readInt(this.bytes, 2);
/* 644 */       this.sealAlg = GSSToken.readInt(this.bytes, 4);
/*     */ 
/* 648 */       int i = GSSToken.readInt(this.bytes, 6);
/*     */       Object localObject;
/* 652 */       switch (this.sealAlg) {
/*     */       case 0:
/*     */       case 512:
/*     */       case 4096:
/* 656 */         localObject.setPrivacy(true);
/* 657 */         break;
/*     */       default:
/* 660 */         localObject.setPrivacy(false);
/*     */       }
/*     */ 
/* 663 */       localObject.setQOP(0);
/*     */     }
/*     */ 
/*     */     public final void encode(OutputStream paramOutputStream)
/*     */       throws IOException
/*     */     {
/* 672 */       paramOutputStream.write(this.bytes);
/*     */     }
/*     */ 
/*     */     public final int getTokenId()
/*     */     {
/* 683 */       return this.tokenId;
/*     */     }
/*     */ 
/*     */     public final int getSignAlg()
/*     */     {
/* 693 */       return this.signAlg;
/*     */     }
/*     */ 
/*     */     public final int getSealAlg()
/*     */     {
/* 703 */       return this.sealAlg;
/*     */     }
/*     */ 
/*     */     public final byte[] getBytes()
/*     */     {
/* 711 */       return this.bytes;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.MessageToken
 * JD-Core Version:    0.6.2
 */