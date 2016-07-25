/*     */ package java.security;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import sun.security.jca.JCAUtil;
/*     */ 
/*     */ public abstract class SignatureSpi
/*     */ {
/*  57 */   protected SecureRandom appRandom = null;
/*     */ 
/*     */   protected abstract void engineInitVerify(PublicKey paramPublicKey)
/*     */     throws InvalidKeyException;
/*     */ 
/*     */   protected abstract void engineInitSign(PrivateKey paramPrivateKey)
/*     */     throws InvalidKeyException;
/*     */ 
/*     */   protected void engineInitSign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
/*     */     throws InvalidKeyException
/*     */   {
/* 102 */     this.appRandom = paramSecureRandom;
/* 103 */     engineInitSign(paramPrivateKey);
/*     */   }
/*     */ 
/*     */   protected abstract void engineUpdate(byte paramByte)
/*     */     throws SignatureException;
/*     */ 
/*     */   protected abstract void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SignatureException;
/*     */ 
/*     */   protected void engineUpdate(ByteBuffer paramByteBuffer)
/*     */   {
/* 142 */     if (!paramByteBuffer.hasRemaining())
/* 143 */       return;
/*     */     try
/*     */     {
/*     */       int k;
/* 146 */       if (paramByteBuffer.hasArray()) {
/* 147 */         byte[] arrayOfByte1 = paramByteBuffer.array();
/* 148 */         int j = paramByteBuffer.arrayOffset();
/* 149 */         k = paramByteBuffer.position();
/* 150 */         int m = paramByteBuffer.limit();
/* 151 */         engineUpdate(arrayOfByte1, j + k, m - k);
/* 152 */         paramByteBuffer.position(m);
/*     */       } else {
/* 154 */         int i = paramByteBuffer.remaining();
/* 155 */         byte[] arrayOfByte2 = new byte[JCAUtil.getTempArraySize(i)];
/* 156 */         while (i > 0) {
/* 157 */           k = Math.min(i, arrayOfByte2.length);
/* 158 */           paramByteBuffer.get(arrayOfByte2, 0, k);
/* 159 */           engineUpdate(arrayOfByte2, 0, k);
/* 160 */           i -= k;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (SignatureException localSignatureException)
/*     */     {
/* 166 */       throw new ProviderException("update() failed", localSignatureException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract byte[] engineSign()
/*     */     throws SignatureException;
/*     */ 
/*     */   protected int engineSign(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SignatureException
/*     */   {
/* 226 */     byte[] arrayOfByte = engineSign();
/* 227 */     if (paramInt2 < arrayOfByte.length) {
/* 228 */       throw new SignatureException("partial signatures not returned");
/*     */     }
/*     */ 
/* 231 */     if (paramArrayOfByte.length - paramInt1 < arrayOfByte.length) {
/* 232 */       throw new SignatureException("insufficient space in the output buffer to store the signature");
/*     */     }
/*     */ 
/* 236 */     System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, arrayOfByte.length);
/* 237 */     return arrayOfByte.length;
/*     */   }
/*     */ 
/*     */   protected abstract boolean engineVerify(byte[] paramArrayOfByte)
/*     */     throws SignatureException;
/*     */ 
/*     */   protected boolean engineVerify(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SignatureException
/*     */   {
/* 276 */     byte[] arrayOfByte = new byte[paramInt2];
/* 277 */     System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
/* 278 */     return engineVerify(arrayOfByte);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected abstract void engineSetParameter(String paramString, Object paramObject)
/*     */     throws InvalidParameterException;
/*     */ 
/*     */   protected void engineSetParameter(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 324 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   protected AlgorithmParameters engineGetParameters()
/*     */   {
/* 346 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected abstract Object engineGetParameter(String paramString)
/*     */     throws InvalidParameterException;
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 384 */     if ((this instanceof Cloneable)) {
/* 385 */       return super.clone();
/*     */     }
/* 387 */     throw new CloneNotSupportedException();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.SignatureSpi
 * JD-Core Version:    0.6.2
 */