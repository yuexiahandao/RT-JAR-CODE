/*     */ package sun.security.rsa;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.InvalidParameterException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.ProviderException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.SignatureException;
/*     */ import java.security.SignatureSpi;
/*     */ import java.security.interfaces.RSAKey;
/*     */ import java.security.interfaces.RSAPrivateKey;
/*     */ import java.security.interfaces.RSAPublicKey;
/*     */ import java.util.Arrays;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public abstract class RSASignature extends SignatureSpi
/*     */ {
/*     */   private static final int baseLength = 8;
/*     */   private final ObjectIdentifier digestOID;
/*     */   private final int encodedLength;
/*     */   private final MessageDigest md;
/*     */   private boolean digestReset;
/*     */   private RSAPrivateKey privateKey;
/*     */   private RSAPublicKey publicKey;
/*     */   private RSAPadding padding;
/*     */ 
/*     */   RSASignature(String paramString, ObjectIdentifier paramObjectIdentifier, int paramInt)
/*     */   {
/*  79 */     this.digestOID = paramObjectIdentifier;
/*     */     try {
/*  81 */       this.md = MessageDigest.getInstance(paramString);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  83 */       throw new ProviderException(localNoSuchAlgorithmException);
/*     */     }
/*  85 */     this.digestReset = true;
/*  86 */     this.encodedLength = (8 + paramInt + this.md.getDigestLength());
/*     */   }
/*     */ 
/*     */   protected void engineInitVerify(PublicKey paramPublicKey)
/*     */     throws InvalidKeyException
/*     */   {
/*  92 */     RSAPublicKey localRSAPublicKey = (RSAPublicKey)RSAKeyFactory.toRSAKey(paramPublicKey);
/*  93 */     this.privateKey = null;
/*  94 */     this.publicKey = localRSAPublicKey;
/*  95 */     initCommon(localRSAPublicKey, null);
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(PrivateKey paramPrivateKey)
/*     */     throws InvalidKeyException
/*     */   {
/* 101 */     engineInitSign(paramPrivateKey, null);
/*     */   }
/*     */ 
/*     */   protected void engineInitSign(PrivateKey paramPrivateKey, SecureRandom paramSecureRandom)
/*     */     throws InvalidKeyException
/*     */   {
/* 107 */     RSAPrivateKey localRSAPrivateKey = (RSAPrivateKey)RSAKeyFactory.toRSAKey(paramPrivateKey);
/*     */ 
/* 109 */     this.privateKey = localRSAPrivateKey;
/* 110 */     this.publicKey = null;
/* 111 */     initCommon(localRSAPrivateKey, paramSecureRandom);
/*     */   }
/*     */ 
/*     */   private void initCommon(RSAKey paramRSAKey, SecureRandom paramSecureRandom)
/*     */     throws InvalidKeyException
/*     */   {
/* 119 */     resetDigest();
/* 120 */     int i = RSACore.getByteLength(paramRSAKey);
/*     */     try {
/* 122 */       this.padding = RSAPadding.getInstance(1, i, paramSecureRandom);
/*     */     }
/*     */     catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/* 125 */       throw new InvalidKeyException(localInvalidAlgorithmParameterException.getMessage());
/*     */     }
/* 127 */     int j = this.padding.getMaxDataSize();
/* 128 */     if (this.encodedLength > j)
/* 129 */       throw new InvalidKeyException("Key is too short for this signature algorithm");
/*     */   }
/*     */ 
/*     */   private void resetDigest()
/*     */   {
/* 138 */     if (!this.digestReset) {
/* 139 */       this.md.reset();
/* 140 */       this.digestReset = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] getDigestValue()
/*     */   {
/* 148 */     this.digestReset = true;
/* 149 */     return this.md.digest();
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte paramByte) throws SignatureException
/*     */   {
/* 154 */     this.md.update(paramByte);
/* 155 */     this.digestReset = false;
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws SignatureException
/*     */   {
/* 161 */     this.md.update(paramArrayOfByte, paramInt1, paramInt2);
/* 162 */     this.digestReset = false;
/*     */   }
/*     */ 
/*     */   protected void engineUpdate(ByteBuffer paramByteBuffer)
/*     */   {
/* 167 */     this.md.update(paramByteBuffer);
/* 168 */     this.digestReset = false;
/*     */   }
/*     */ 
/*     */   protected byte[] engineSign() throws SignatureException
/*     */   {
/* 173 */     byte[] arrayOfByte1 = getDigestValue();
/*     */     try {
/* 175 */       byte[] arrayOfByte2 = encodeSignature(this.digestOID, arrayOfByte1);
/* 176 */       byte[] arrayOfByte3 = this.padding.pad(arrayOfByte2);
/* 177 */       return RSACore.rsa(arrayOfByte3, this.privateKey, true);
/*     */     }
/*     */     catch (GeneralSecurityException localGeneralSecurityException) {
/* 180 */       throw new SignatureException("Could not sign data", localGeneralSecurityException);
/*     */     } catch (IOException localIOException) {
/* 182 */       throw new SignatureException("Could not encode data", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean engineVerify(byte[] paramArrayOfByte) throws SignatureException
/*     */   {
/* 188 */     if (paramArrayOfByte.length != RSACore.getByteLength(this.publicKey)) {
/* 189 */       throw new SignatureException("Signature length not correct: got " + paramArrayOfByte.length + " but was expecting " + RSACore.getByteLength(this.publicKey));
/*     */     }
/*     */ 
/* 193 */     byte[] arrayOfByte1 = getDigestValue();
/*     */     try {
/* 195 */       byte[] arrayOfByte2 = RSACore.rsa(paramArrayOfByte, this.publicKey);
/* 196 */       byte[] arrayOfByte3 = this.padding.unpad(arrayOfByte2);
/* 197 */       byte[] arrayOfByte4 = decodeSignature(this.digestOID, arrayOfByte3);
/* 198 */       return Arrays.equals(arrayOfByte1, arrayOfByte4);
/*     */     }
/*     */     catch (BadPaddingException localBadPaddingException)
/*     */     {
/* 204 */       return false;
/*     */     } catch (GeneralSecurityException localGeneralSecurityException) {
/* 206 */       throw new SignatureException("Signature verification failed", localGeneralSecurityException);
/*     */     } catch (IOException localIOException) {
/* 208 */       throw new SignatureException("Signature encoding error", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static byte[] encodeSignature(ObjectIdentifier paramObjectIdentifier, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 218 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 219 */     new AlgorithmId(paramObjectIdentifier).encode(localDerOutputStream);
/* 220 */     localDerOutputStream.putOctetString(paramArrayOfByte);
/* 221 */     DerValue localDerValue = new DerValue((byte)48, localDerOutputStream.toByteArray());
/*     */ 
/* 223 */     return localDerValue.toByteArray();
/*     */   }
/*     */ 
/*     */   public static byte[] decodeSignature(ObjectIdentifier paramObjectIdentifier, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 232 */     DerInputStream localDerInputStream = new DerInputStream(paramArrayOfByte);
/* 233 */     DerValue[] arrayOfDerValue = localDerInputStream.getSequence(2);
/* 234 */     if ((arrayOfDerValue.length != 2) || (localDerInputStream.available() != 0)) {
/* 235 */       throw new IOException("SEQUENCE length error");
/*     */     }
/* 237 */     AlgorithmId localAlgorithmId = AlgorithmId.parse(arrayOfDerValue[0]);
/* 238 */     if (!localAlgorithmId.getOID().equals(paramObjectIdentifier)) {
/* 239 */       throw new IOException("ObjectIdentifier mismatch: " + localAlgorithmId.getOID());
/*     */     }
/*     */ 
/* 242 */     if (localAlgorithmId.getEncodedParams() != null) {
/* 243 */       throw new IOException("Unexpected AlgorithmId parameters");
/*     */     }
/* 245 */     byte[] arrayOfByte = arrayOfDerValue[1].getOctetString();
/* 246 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   protected void engineSetParameter(String paramString, Object paramObject)
/*     */     throws InvalidParameterException
/*     */   {
/* 252 */     throw new UnsupportedOperationException("setParameter() not supported");
/*     */   }
/*     */ 
/*     */   protected Object engineGetParameter(String paramString)
/*     */     throws InvalidParameterException
/*     */   {
/* 258 */     throw new UnsupportedOperationException("getParameter() not supported");
/*     */   }
/*     */ 
/*     */   public static final class MD2withRSA extends RSASignature
/*     */   {
/*     */     public MD2withRSA() {
/* 264 */       super(AlgorithmId.MD2_oid, 10);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class MD5withRSA extends RSASignature
/*     */   {
/*     */     public MD5withRSA() {
/* 271 */       super(AlgorithmId.MD5_oid, 10);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SHA1withRSA extends RSASignature
/*     */   {
/*     */     public SHA1withRSA() {
/* 278 */       super(AlgorithmId.SHA_oid, 7);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SHA256withRSA extends RSASignature
/*     */   {
/*     */     public SHA256withRSA() {
/* 285 */       super(AlgorithmId.SHA256_oid, 11);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SHA384withRSA extends RSASignature
/*     */   {
/*     */     public SHA384withRSA() {
/* 292 */       super(AlgorithmId.SHA384_oid, 11);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class SHA512withRSA extends RSASignature
/*     */   {
/*     */     public SHA512withRSA() {
/* 299 */       super(AlgorithmId.SHA512_oid, 11);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.rsa.RSASignature
 * JD-Core Version:    0.6.2
 */