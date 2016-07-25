/*     */ package sun.security.jgss.krb5;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.MessageProp;
/*     */ 
/*     */ class MicToken extends MessageToken
/*     */ {
/*     */   public MicToken(Krb5Context paramKrb5Context, byte[] paramArrayOfByte, int paramInt1, int paramInt2, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/*  40 */     super(257, paramKrb5Context, paramArrayOfByte, paramInt1, paramInt2, paramMessageProp);
/*     */   }
/*     */ 
/*     */   public MicToken(Krb5Context paramKrb5Context, InputStream paramInputStream, MessageProp paramMessageProp)
/*     */     throws GSSException
/*     */   {
/*  47 */     super(257, paramKrb5Context, paramInputStream, paramMessageProp);
/*     */   }
/*     */ 
/*     */   public void verify(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws GSSException {
/*  51 */     if (!verifySignAndSeqNumber(null, paramArrayOfByte, paramInt1, paramInt2, null))
/*  52 */       throw new GSSException(6, -1, "Corrupt checksum or sequence number in MIC token");
/*     */   }
/*     */ 
/*     */   public void verify(InputStream paramInputStream) throws GSSException
/*     */   {
/*  57 */     byte[] arrayOfByte = null;
/*     */     try {
/*  59 */       arrayOfByte = new byte[paramInputStream.available()];
/*  60 */       paramInputStream.read(arrayOfByte);
/*     */     }
/*     */     catch (IOException localIOException) {
/*  63 */       throw new GSSException(6, -1, "Corrupt checksum or sequence number in MIC token");
/*     */     }
/*     */ 
/*  66 */     verify(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public MicToken(Krb5Context paramKrb5Context, MessageProp paramMessageProp, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws GSSException
/*     */   {
/*  72 */     super(257, paramKrb5Context);
/*     */ 
/*  76 */     if (paramMessageProp == null) paramMessageProp = new MessageProp(0, false);
/*  77 */     genSignAndSeqNumber(paramMessageProp, null, paramArrayOfByte, paramInt1, paramInt2, null);
/*     */   }
/*     */ 
/*     */   public MicToken(Krb5Context paramKrb5Context, MessageProp paramMessageProp, InputStream paramInputStream)
/*     */     throws GSSException, IOException
/*     */   {
/*  83 */     super(257, paramKrb5Context);
/*  84 */     byte[] arrayOfByte = new byte[paramInputStream.available()];
/*  85 */     paramInputStream.read(arrayOfByte);
/*     */ 
/*  89 */     if (paramMessageProp == null) paramMessageProp = new MessageProp(0, false);
/*  90 */     genSignAndSeqNumber(paramMessageProp, null, arrayOfByte, 0, arrayOfByte.length, null);
/*     */   }
/*     */ 
/*     */   protected int getSealAlg(boolean paramBoolean, int paramInt) {
/*  94 */     return 65535;
/*     */   }
/*     */ 
/*     */   public int encode(byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException, GSSException
/*     */   {
/* 100 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 101 */     super.encode(localByteArrayOutputStream);
/* 102 */     byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
/* 103 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, arrayOfByte.length);
/* 104 */     return arrayOfByte.length;
/*     */   }
/*     */ 
/*     */   public byte[] encode() throws IOException, GSSException
/*     */   {
/* 109 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(50);
/* 110 */     encode(localByteArrayOutputStream);
/* 111 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.krb5.MicToken
 * JD-Core Version:    0.6.2
 */