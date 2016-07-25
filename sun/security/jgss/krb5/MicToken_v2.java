/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ 
/*     */ class MicToken_v2 extends MessageToken_v2
/*     */ {
/*     */   public MicToken_v2(Krb5Context paramKrb5Context, byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/*  49 */     super(1028, paramKrb5Context, paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/*     */   }
/*     */ 
/*     */   public MicToken_v2(Krb5Context paramKrb5Context, InputStream paramInputStream, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/*  55 */     super(1028, paramKrb5Context, paramInputStream, paramMessageProp);
/*     */   }
/*     */ 
/*     */   public void verify(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws GSSException {
/*  59 */     if (!verifySign(paramArrayOfByte, paramInt1, paramInt2))
/*  60 */       throw new GSSException(6, -1, "Corrupt checksum or sequence number in MIC token");
/*     */   }
/*     */ 
/*     */   public void verify(InputStream paramInputStream) throws GSSException
/*     */   {
/*  65 */     byte[] arrayOfByte = null;
/*     */     try {
/*  67 */       arrayOfByte = new byte[paramInputStream.available()];
/*  68 */       paramInputStream.read(arrayOfByte);
/*     */     }
/*     */     catch (IOException localIOException) {
/*  71 */       throw new GSSException(6, -1, "Corrupt checksum or sequence number in MIC token");
/*     */     }
/*     */ 
/*  74 */     verify(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public MicToken_v2(Krb5Context paramKrb5Context, MessageProp paramMessageProp, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws GSSException
/*     */   {
/*  80 */     super(1028, paramKrb5Context);
/*     */ 
/*  84 */     if (paramMessageProp == null) paramMessageProp = new MessageProp(0, false);
/*  85 */     genSignAndSeqNumber(paramMessageProp, paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public MicToken_v2(Krb5Context paramKrb5Context, MessageProp paramMessageProp, InputStream paramInputStream)
/*     */     throws GSSException, IOException
/*     */   {
/*  91 */     super(1028, paramKrb5Context);
/*  92 */     byte[] arrayOfByte = new byte[paramInputStream.available()];
/*  93 */     paramInputStream.read(arrayOfByte);
/*     */ 
/*  97 */     if (paramMessageProp == null) paramMessageProp = new MessageProp(0, false);
/*  98 */     genSignAndSeqNumber(paramMessageProp, arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public byte[] encode() throws IOException
/*     */   {
/* 103 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(50);
/* 104 */     encode(localByteArrayOutputStream);
/* 105 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public int encode(byte[] paramArrayOfByte, int paramInt) throws IOException {
/* 109 */     byte[] arrayOfByte = encode();
/* 110 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, arrayOfByte.length);
/* 111 */     return arrayOfByte.length;
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream) throws IOException {
/* 115 */     encodeHeader(paramOutputStream);
/* 116 */     paramOutputStream.write(this.checksum);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.MicToken_v2
 * JD-Core Version:    0.6.2
 */