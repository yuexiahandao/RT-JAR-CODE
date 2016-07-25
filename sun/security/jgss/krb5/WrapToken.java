/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ import sun.security.jgss.GSSHeader;
/*     */ import sun.security.krb5.Confounder;
/*     */ 
/*     */ class WrapToken extends MessageToken
/*     */ {
/*     */   static final int CONFOUNDER_SIZE = 8;
/*  62 */   static final byte[][] pads = { null, { 1 }, { 2, 2 }, { 3, 3, 3 }, { 4, 4, 4, 4 }, { 5, 5, 5, 5, 5 }, { 6, 6, 6, 6, 6, 6 }, { 7, 7, 7, 7, 7, 7, 7 }, { 8, 8, 8, 8, 8, 8, 8, 8 } };
/*     */ 
/*  81 */   private boolean readTokenFromInputStream = true;
/*  82 */   private InputStream is = null;
/*  83 */   private byte[] tokenBytes = null;
/*  84 */   private int tokenOffset = 0;
/*  85 */   private int tokenLen = 0;
/*     */ 
/*  94 */   private byte[] dataBytes = null;
/*  95 */   private int dataOffset = 0;
/*  96 */   private int dataLen = 0;
/*     */ 
/*  99 */   private int dataSize = 0;
/*     */ 
/* 102 */   byte[] confounder = null;
/* 103 */   byte[] padding = null;
/*     */ 
/* 105 */   private boolean privacy = false;
/*     */ 
/*     */   public WrapToken(Krb5Context paramKrb5Context, byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 124 */     super(513, paramKrb5Context, paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/*     */ 
/* 127 */     this.readTokenFromInputStream = false;
/*     */ 
/* 130 */     this.tokenBytes = paramArrayOfByte;
/* 131 */     this.tokenOffset = paramInt1;
/* 132 */     this.tokenLen = paramInt2;
/* 133 */     this.privacy = paramMessageProp.getPrivacy();
/* 134 */     this.dataSize = (getGSSHeader().getMechTokenLength() - getKrb5TokenSize());
/*     */   }
/*     */ 
/*     */   public WrapToken(Krb5Context paramKrb5Context, InputStream paramInputStream, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/* 154 */     super(513, paramKrb5Context, paramInputStream, paramMessageProp);
/*     */ 
/* 157 */     this.is = paramInputStream;
/* 158 */     this.privacy = paramMessageProp.getPrivacy();
/*     */ 
/* 166 */     this.dataSize = (getGSSHeader().getMechTokenLength() - getTokenSize());
/*     */   }
/*     */ 
/*     */   public byte[] getData()
/*     */     throws GSSException
/*     */   {
/* 181 */     byte[] arrayOfByte1 = new byte[this.dataSize];
/* 182 */     getData(arrayOfByte1, 0);
/*     */ 
/* 185 */     byte[] arrayOfByte2 = new byte[this.dataSize - this.confounder.length - this.padding.length];
/*     */ 
/* 187 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte2.length);
/*     */ 
/* 189 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   public int getData(byte[] paramArrayOfByte, int paramInt)
/*     */     throws GSSException
/*     */   {
/* 206 */     if (this.readTokenFromInputStream)
/* 207 */       getDataFromStream(paramArrayOfByte, paramInt);
/*     */     else {
/* 209 */       getDataFromBuffer(paramArrayOfByte, paramInt);
/*     */     }
/* 211 */     return this.dataSize - this.confounder.length - this.padding.length;
/*     */   }
/*     */ 
/*     */   private void getDataFromBuffer(byte[] paramArrayOfByte, int paramInt)
/*     */     throws GSSException
/*     */   {
/* 227 */     GSSHeader localGSSHeader = getGSSHeader();
/* 228 */     int i = this.tokenOffset + localGSSHeader.getLength() + getTokenSize();
/*     */ 
/* 231 */     if (i + this.dataSize > this.tokenOffset + this.tokenLen) {
/* 232 */       throw new GSSException(10, -1, "Insufficient data in " + getTokenName(getTokenId()));
/*     */     }
/*     */ 
/* 239 */     this.confounder = new byte[8];
/*     */ 
/* 243 */     if (this.privacy) {
/* 244 */       this.cipherHelper.decryptData(this, this.tokenBytes, i, this.dataSize, paramArrayOfByte, paramInt);
/*     */     }
/*     */     else
/*     */     {
/* 259 */       System.arraycopy(this.tokenBytes, i, this.confounder, 0, 8);
/*     */ 
/* 261 */       int j = this.tokenBytes[(i + this.dataSize - 1)];
/* 262 */       if (j < 0)
/* 263 */         j = 0;
/* 264 */       if (j > 8) {
/* 265 */         j %= 8;
/*     */       }
/* 267 */       this.padding = pads[j];
/*     */ 
/* 270 */       System.arraycopy(this.tokenBytes, i + 8, paramArrayOfByte, paramInt, this.dataSize - 8 - j);
/*     */     }
/*     */ 
/* 284 */     if (!verifySignAndSeqNumber(this.confounder, paramArrayOfByte, paramInt, this.dataSize - 8 - this.padding.length, this.padding))
/*     */     {
/* 289 */       throw new GSSException(6, -1, "Corrupt checksum or sequence number in Wrap token");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void getDataFromStream(byte[] paramArrayOfByte, int paramInt)
/*     */     throws GSSException
/*     */   {
/* 306 */     GSSHeader localGSSHeader = getGSSHeader();
/*     */ 
/* 313 */     this.confounder = new byte[8];
/*     */     try
/*     */     {
/* 319 */       if (this.privacy) {
/* 320 */         this.cipherHelper.decryptData(this, this.is, this.dataSize, paramArrayOfByte, paramInt);
/*     */       }
/*     */       else
/*     */       {
/* 334 */         readFully(this.is, this.confounder);
/*     */ 
/* 336 */         if (this.cipherHelper.isArcFour()) {
/* 337 */           this.padding = pads[1];
/* 338 */           readFully(this.is, paramArrayOfByte, paramInt, this.dataSize - 8 - 1);
/*     */         }
/*     */         else
/*     */         {
/* 342 */           int i = (this.dataSize - 8) / 8 - 1;
/* 343 */           int j = paramInt;
/* 344 */           for (int k = 0; k < i; k++) {
/* 345 */             readFully(this.is, paramArrayOfByte, j, 8);
/* 346 */             j += 8;
/*     */           }
/*     */ 
/* 349 */           byte[] arrayOfByte = new byte[8];
/* 350 */           readFully(this.is, arrayOfByte);
/*     */ 
/* 352 */           int m = arrayOfByte[7];
/* 353 */           this.padding = pads[m];
/*     */ 
/* 356 */           System.arraycopy(arrayOfByte, 0, paramArrayOfByte, j, arrayOfByte.length - m);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException) {
/* 361 */       throw new GSSException(10, -1, getTokenName(getTokenId()) + ": " + localIOException.getMessage());
/*     */     }
/*     */ 
/* 370 */     if (!verifySignAndSeqNumber(this.confounder, paramArrayOfByte, paramInt, this.dataSize - 8 - this.padding.length, this.padding))
/*     */     {
/* 375 */       throw new GSSException(6, -1, "Corrupt checksum or sequence number in Wrap token");
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] getPadding(int paramInt)
/*     */   {
/* 388 */     int i = 0;
/*     */ 
/* 391 */     if (this.cipherHelper.isArcFour()) {
/* 392 */       i = 1;
/*     */     } else {
/* 394 */       i = paramInt % 8;
/* 395 */       i = 8 - i;
/*     */     }
/* 397 */     return pads[i];
/*     */   }
/*     */ 
/*     */   public WrapToken(Krb5Context paramKrb5Context, MessageProp paramMessageProp, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws GSSException
/*     */   {
/* 404 */     super(513, paramKrb5Context);
/*     */ 
/* 406 */     this.confounder = Confounder.bytes(8);
/*     */ 
/* 408 */     this.padding = getPadding(paramInt2);
/* 409 */     this.dataSize = (this.confounder.length + paramInt2 + this.padding.length);
/* 410 */     this.dataBytes = paramArrayOfByte;
/* 411 */     this.dataOffset = paramInt1;
/* 412 */     this.dataLen = paramInt2;
/*     */ 
/* 422 */     genSignAndSeqNumber(paramMessageProp, this.confounder, paramArrayOfByte, paramInt1, paramInt2, this.padding);
/*     */ 
/* 433 */     if (!paramKrb5Context.getConfState()) {
/* 434 */       paramMessageProp.setPrivacy(false);
/*     */     }
/* 436 */     this.privacy = paramMessageProp.getPrivacy();
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream) throws IOException, GSSException
/*     */   {
/* 441 */     super.encode(paramOutputStream);
/*     */ 
/* 444 */     if (!this.privacy)
/*     */     {
/* 447 */       paramOutputStream.write(this.confounder);
/*     */ 
/* 450 */       paramOutputStream.write(this.dataBytes, this.dataOffset, this.dataLen);
/*     */ 
/* 453 */       paramOutputStream.write(this.padding);
/*     */     }
/*     */     else
/*     */     {
/* 457 */       this.cipherHelper.encryptData(this, this.confounder, this.dataBytes, this.dataOffset, this.dataLen, this.padding, paramOutputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] encode()
/*     */     throws IOException, GSSException
/*     */   {
/* 465 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(this.dataSize + 50);
/* 466 */     encode(localByteArrayOutputStream);
/* 467 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public int encode(byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException, GSSException
/*     */   {
/* 474 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 475 */     super.encode(localByteArrayOutputStream);
/* 476 */     byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
/* 477 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, arrayOfByte.length);
/* 478 */     paramInt += arrayOfByte.length;
/*     */ 
/* 481 */     if (!this.privacy)
/*     */     {
/* 484 */       System.arraycopy(this.confounder, 0, paramArrayOfByte, paramInt, this.confounder.length);
/*     */ 
/* 486 */       paramInt += this.confounder.length;
/*     */ 
/* 489 */       System.arraycopy(this.dataBytes, this.dataOffset, paramArrayOfByte, paramInt, this.dataLen);
/*     */ 
/* 491 */       paramInt += this.dataLen;
/*     */ 
/* 494 */       System.arraycopy(this.padding, 0, paramArrayOfByte, paramInt, this.padding.length);
/*     */     }
/*     */     else
/*     */     {
/* 498 */       this.cipherHelper.encryptData(this, this.confounder, this.dataBytes, this.dataOffset, this.dataLen, this.padding, paramArrayOfByte, paramInt);
/*     */     }
/*     */ 
/* 507 */     return arrayOfByte.length + this.confounder.length + this.dataLen + this.padding.length;
/*     */   }
/*     */ 
/*     */   protected int getKrb5TokenSize() throws GSSException
/*     */   {
/* 512 */     return getTokenSize() + this.dataSize;
/*     */   }
/*     */ 
/*     */   protected int getSealAlg(boolean paramBoolean, int paramInt) throws GSSException {
/* 516 */     if (!paramBoolean) {
/* 517 */       return 65535;
/*     */     }
/*     */ 
/* 521 */     return this.cipherHelper.getSealAlg();
/*     */   }
/*     */ 
/*     */   static int getSizeLimit(int paramInt1, boolean paramBoolean, int paramInt2, CipherHelper paramCipherHelper)
/*     */     throws GSSException
/*     */   {
/* 528 */     return GSSHeader.getMaxMechTokenSize(OID, paramInt2) - (getTokenSize(paramCipherHelper) + 8) - 8;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.WrapToken
 * JD-Core Version:    0.6.2
 */