/*     */ package java.security;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ public abstract class MessageDigest extends MessageDigestSpi
/*     */ {
/*     */   private String algorithm;
/*     */   private static final int INITIAL = 0;
/*     */   private static final int IN_PROGRESS = 1;
/* 111 */   private int state = 0;
/*     */   private Provider provider;
/*     */ 
/*     */   protected MessageDigest(String paramString)
/*     */   {
/* 126 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public static MessageDigest getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/*     */     try
/*     */     {
/* 159 */       Object[] arrayOfObject = Security.getImpl(paramString, "MessageDigest", (String)null);
/*     */ 
/* 161 */       if ((arrayOfObject[0] instanceof MessageDigest)) {
/* 162 */         localObject = (MessageDigest)arrayOfObject[0];
/* 163 */         ((MessageDigest)localObject).provider = ((Provider)arrayOfObject[1]);
/* 164 */         return localObject;
/*     */       }
/* 166 */       Object localObject = new Delegate((MessageDigestSpi)arrayOfObject[0], paramString);
/*     */ 
/* 168 */       ((MessageDigest)localObject).provider = ((Provider)arrayOfObject[1]);
/* 169 */       return localObject;
/*     */     } catch (NoSuchProviderException localNoSuchProviderException) {
/*     */     }
/* 172 */     throw new NoSuchAlgorithmException(paramString + " not found");
/*     */   }
/*     */ 
/*     */   public static MessageDigest getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 213 */     if ((paramString2 == null) || (paramString2.length() == 0))
/* 214 */       throw new IllegalArgumentException("missing provider");
/* 215 */     Object[] arrayOfObject = Security.getImpl(paramString1, "MessageDigest", paramString2);
/* 216 */     if ((arrayOfObject[0] instanceof MessageDigest)) {
/* 217 */       localObject = (MessageDigest)arrayOfObject[0];
/* 218 */       ((MessageDigest)localObject).provider = ((Provider)arrayOfObject[1]);
/* 219 */       return localObject;
/*     */     }
/* 221 */     Object localObject = new Delegate((MessageDigestSpi)arrayOfObject[0], paramString1);
/*     */ 
/* 223 */     ((MessageDigest)localObject).provider = ((Provider)arrayOfObject[1]);
/* 224 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static MessageDigest getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 261 */     if (paramProvider == null)
/* 262 */       throw new IllegalArgumentException("missing provider");
/* 263 */     Object[] arrayOfObject = Security.getImpl(paramString, "MessageDigest", paramProvider);
/* 264 */     if ((arrayOfObject[0] instanceof MessageDigest)) {
/* 265 */       localObject = (MessageDigest)arrayOfObject[0];
/* 266 */       ((MessageDigest)localObject).provider = ((Provider)arrayOfObject[1]);
/* 267 */       return localObject;
/*     */     }
/* 269 */     Object localObject = new Delegate((MessageDigestSpi)arrayOfObject[0], paramString);
/*     */ 
/* 271 */     ((MessageDigest)localObject).provider = ((Provider)arrayOfObject[1]);
/* 272 */     return localObject;
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 282 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public void update(byte paramByte)
/*     */   {
/* 291 */     engineUpdate(paramByte);
/* 292 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 307 */     if (paramArrayOfByte == null) {
/* 308 */       throw new IllegalArgumentException("No input buffer given");
/*     */     }
/* 310 */     if (paramArrayOfByte.length - paramInt1 < paramInt2) {
/* 311 */       throw new IllegalArgumentException("Input buffer too short");
/*     */     }
/* 313 */     engineUpdate(paramArrayOfByte, paramInt1, paramInt2);
/* 314 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   public void update(byte[] paramArrayOfByte)
/*     */   {
/* 323 */     engineUpdate(paramArrayOfByte, 0, paramArrayOfByte.length);
/* 324 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   public final void update(ByteBuffer paramByteBuffer)
/*     */   {
/* 338 */     if (paramByteBuffer == null) {
/* 339 */       throw new NullPointerException();
/*     */     }
/* 341 */     engineUpdate(paramByteBuffer);
/* 342 */     this.state = 1;
/*     */   }
/*     */ 
/*     */   public byte[] digest()
/*     */   {
/* 353 */     byte[] arrayOfByte = engineDigest();
/* 354 */     this.state = 0;
/* 355 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int digest(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws DigestException
/*     */   {
/* 373 */     if (paramArrayOfByte == null) {
/* 374 */       throw new IllegalArgumentException("No output buffer given");
/*     */     }
/* 376 */     if (paramArrayOfByte.length - paramInt1 < paramInt2) {
/* 377 */       throw new IllegalArgumentException("Output buffer too small for specified offset and length");
/*     */     }
/*     */ 
/* 380 */     int i = engineDigest(paramArrayOfByte, paramInt1, paramInt2);
/* 381 */     this.state = 0;
/* 382 */     return i;
/*     */   }
/*     */ 
/*     */   public byte[] digest(byte[] paramArrayOfByte)
/*     */   {
/* 398 */     update(paramArrayOfByte);
/* 399 */     return digest();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 406 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 407 */     PrintStream localPrintStream = new PrintStream(localByteArrayOutputStream);
/* 408 */     localPrintStream.print(this.algorithm + " Message Digest from " + this.provider.getName() + ", ");
/* 409 */     switch (this.state) {
/*     */     case 0:
/* 411 */       localPrintStream.print("<initialized>");
/* 412 */       break;
/*     */     case 1:
/* 414 */       localPrintStream.print("<in progress>");
/*     */     }
/*     */ 
/* 417 */     localPrintStream.println();
/* 418 */     return localByteArrayOutputStream.toString();
/*     */   }
/*     */ 
/*     */   public static boolean isEqual(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 431 */     if (paramArrayOfByte1.length != paramArrayOfByte2.length) {
/* 432 */       return false;
/*     */     }
/*     */ 
/* 435 */     int i = 0;
/*     */ 
/* 437 */     for (int j = 0; j < paramArrayOfByte1.length; j++) {
/* 438 */       i |= paramArrayOfByte1[j] ^ paramArrayOfByte2[j];
/*     */     }
/* 440 */     return i == 0;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 447 */     engineReset();
/* 448 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 463 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public final int getDigestLength()
/*     */   {
/* 476 */     int i = engineGetDigestLength();
/* 477 */     if (i == 0) {
/*     */       try {
/* 479 */         MessageDigest localMessageDigest = (MessageDigest)clone();
/* 480 */         byte[] arrayOfByte = localMessageDigest.digest();
/* 481 */         return arrayOfByte.length;
/*     */       } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 483 */         return i;
/*     */       }
/*     */     }
/* 486 */     return i;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 498 */     if ((this instanceof Cloneable)) {
/* 499 */       return super.clone();
/*     */     }
/* 501 */     throw new CloneNotSupportedException();
/*     */   }
/*     */ 
/*     */   static class Delegate extends MessageDigest
/*     */   {
/*     */     private MessageDigestSpi digestSpi;
/*     */ 
/*     */     public Delegate(MessageDigestSpi paramMessageDigestSpi, String paramString)
/*     */     {
/* 529 */       super();
/* 530 */       this.digestSpi = paramMessageDigestSpi;
/*     */     }
/*     */ 
/*     */     public Object clone()
/*     */       throws CloneNotSupportedException
/*     */     {
/* 542 */       if ((this.digestSpi instanceof Cloneable)) {
/* 543 */         MessageDigestSpi localMessageDigestSpi = (MessageDigestSpi)this.digestSpi.clone();
/*     */ 
/* 548 */         Delegate localDelegate = new Delegate(localMessageDigestSpi, this.algorithm);
/*     */ 
/* 551 */         localDelegate.provider = this.provider;
/* 552 */         localDelegate.state = this.state;
/* 553 */         return localDelegate;
/*     */       }
/* 555 */       throw new CloneNotSupportedException();
/*     */     }
/*     */ 
/*     */     protected int engineGetDigestLength()
/*     */     {
/* 560 */       return this.digestSpi.engineGetDigestLength();
/*     */     }
/*     */ 
/*     */     protected void engineUpdate(byte paramByte) {
/* 564 */       this.digestSpi.engineUpdate(paramByte);
/*     */     }
/*     */ 
/*     */     protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 568 */       this.digestSpi.engineUpdate(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected void engineUpdate(ByteBuffer paramByteBuffer) {
/* 572 */       this.digestSpi.engineUpdate(paramByteBuffer);
/*     */     }
/*     */ 
/*     */     protected byte[] engineDigest() {
/* 576 */       return this.digestSpi.engineDigest();
/*     */     }
/*     */ 
/*     */     protected int engineDigest(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws DigestException
/*     */     {
/* 581 */       return this.digestSpi.engineDigest(paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected void engineReset() {
/* 585 */       this.digestSpi.engineReset();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.MessageDigest
 * JD-Core Version:    0.6.2
 */