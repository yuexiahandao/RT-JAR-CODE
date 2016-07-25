/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.util.Arrays;
/*     */ import sun.misc.BASE64Encoder;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ import sun.security.x509.X500Name;
/*     */ import sun.security.x509.X509Key;
/*     */ 
/*     */ public class PKCS10
/*     */ {
/*     */   private X500Name subject;
/*     */   private PublicKey subjectPublicKeyInfo;
/*     */   private PKCS10Attributes attributeSet;
/*     */   private byte[] encoded;
/*     */ 
/*     */   public PKCS10(PublicKey paramPublicKey)
/*     */   {
/*  89 */     this.subjectPublicKeyInfo = paramPublicKey;
/*  90 */     this.attributeSet = new PKCS10Attributes();
/*     */   }
/*     */ 
/*     */   public PKCS10(PublicKey paramPublicKey, PKCS10Attributes paramPKCS10Attributes)
/*     */   {
/* 104 */     this.subjectPublicKeyInfo = paramPublicKey;
/* 105 */     this.attributeSet = paramPKCS10Attributes;
/*     */   }
/*     */ 
/*     */   public PKCS10(byte[] paramArrayOfByte)
/*     */     throws IOException, SignatureException, NoSuchAlgorithmException
/*     */   {
/* 128 */     this.encoded = paramArrayOfByte;
/*     */ 
/* 134 */     DerInputStream localDerInputStream = new DerInputStream(paramArrayOfByte);
/* 135 */     DerValue[] arrayOfDerValue = localDerInputStream.getSequence(3);
/*     */ 
/* 137 */     if (arrayOfDerValue.length != 3) {
/* 138 */       throw new IllegalArgumentException("not a PKCS #10 request");
/*     */     }
/* 140 */     paramArrayOfByte = arrayOfDerValue[0].toByteArray();
/* 141 */     AlgorithmId localAlgorithmId = AlgorithmId.parse(arrayOfDerValue[1]);
/* 142 */     byte[] arrayOfByte = arrayOfDerValue[2].getBitString();
/*     */ 
/* 150 */     BigInteger localBigInteger = arrayOfDerValue[0].data.getBigInteger();
/* 151 */     if (!localBigInteger.equals(BigInteger.ZERO)) {
/* 152 */       throw new IllegalArgumentException("not PKCS #10 v1");
/*     */     }
/* 154 */     this.subject = new X500Name(arrayOfDerValue[0].data);
/* 155 */     this.subjectPublicKeyInfo = X509Key.parse(arrayOfDerValue[0].data.getDerValue());
/*     */ 
/* 158 */     if (arrayOfDerValue[0].data.available() != 0)
/* 159 */       this.attributeSet = new PKCS10Attributes(arrayOfDerValue[0].data);
/*     */     else {
/* 161 */       this.attributeSet = new PKCS10Attributes();
/*     */     }
/* 163 */     if (arrayOfDerValue[0].data.available() != 0) {
/* 164 */       throw new IllegalArgumentException("illegal PKCS #10 data");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 171 */       Signature localSignature = Signature.getInstance(localAlgorithmId.getName());
/* 172 */       localSignature.initVerify(this.subjectPublicKeyInfo);
/* 173 */       localSignature.update(paramArrayOfByte);
/* 174 */       if (!localSignature.verify(arrayOfByte))
/* 175 */         throw new SignatureException("Invalid PKCS #10 signature");
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 177 */       throw new SignatureException("invalid key");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void encodeAndSign(X500Name paramX500Name, Signature paramSignature)
/*     */     throws CertificateException, IOException, SignatureException
/*     */   {
/* 197 */     if (this.encoded != null) {
/* 198 */       throw new SignatureException("request is already signed");
/*     */     }
/* 200 */     this.subject = paramX500Name;
/*     */ 
/* 205 */     Object localObject = new DerOutputStream();
/* 206 */     ((DerOutputStream)localObject).putInteger(BigInteger.ZERO);
/* 207 */     paramX500Name.encode((DerOutputStream)localObject);
/* 208 */     ((DerOutputStream)localObject).write(this.subjectPublicKeyInfo.getEncoded());
/* 209 */     this.attributeSet.encode((OutputStream)localObject);
/*     */ 
/* 211 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 212 */     localDerOutputStream.write((byte)48, (DerOutputStream)localObject);
/* 213 */     byte[] arrayOfByte1 = localDerOutputStream.toByteArray();
/* 214 */     localObject = localDerOutputStream;
/*     */ 
/* 219 */     paramSignature.update(arrayOfByte1, 0, arrayOfByte1.length);
/*     */ 
/* 221 */     byte[] arrayOfByte2 = paramSignature.sign();
/*     */ 
/* 226 */     AlgorithmId localAlgorithmId = null;
/*     */     try {
/* 228 */       localAlgorithmId = AlgorithmId.getAlgorithmId(paramSignature.getAlgorithm());
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 230 */       throw new SignatureException(localNoSuchAlgorithmException);
/*     */     }
/* 232 */     localAlgorithmId.encode((DerOutputStream)localObject);
/* 233 */     ((DerOutputStream)localObject).putBitString(arrayOfByte2);
/*     */ 
/* 238 */     localDerOutputStream = new DerOutputStream();
/* 239 */     localDerOutputStream.write((byte)48, (DerOutputStream)localObject);
/* 240 */     this.encoded = localDerOutputStream.toByteArray();
/*     */   }
/*     */ 
/*     */   public X500Name getSubjectName()
/*     */   {
/* 246 */     return this.subject;
/*     */   }
/*     */ 
/*     */   public PublicKey getSubjectPublicKeyInfo()
/*     */   {
/* 252 */     return this.subjectPublicKeyInfo;
/*     */   }
/*     */ 
/*     */   public PKCS10Attributes getAttributes()
/*     */   {
/* 258 */     return this.attributeSet;
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */   {
/* 268 */     if (this.encoded != null) {
/* 269 */       return (byte[])this.encoded.clone();
/*     */     }
/* 271 */     return null;
/*     */   }
/*     */ 
/*     */   public void print(PrintStream paramPrintStream)
/*     */     throws IOException, SignatureException
/*     */   {
/* 290 */     if (this.encoded == null) {
/* 291 */       throw new SignatureException("Cert request was not signed");
/*     */     }
/* 293 */     BASE64Encoder localBASE64Encoder = new BASE64Encoder();
/*     */ 
/* 295 */     paramPrintStream.println("-----BEGIN NEW CERTIFICATE REQUEST-----");
/* 296 */     localBASE64Encoder.encodeBuffer(this.encoded, paramPrintStream);
/* 297 */     paramPrintStream.println("-----END NEW CERTIFICATE REQUEST-----");
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 304 */     return "[PKCS #10 certificate request:\n" + this.subjectPublicKeyInfo.toString() + " subject: <" + this.subject + ">" + "\n" + " attributes: " + this.attributeSet.toString() + "\n]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 323 */     if (this == paramObject)
/* 324 */       return true;
/* 325 */     if (!(paramObject instanceof PKCS10))
/* 326 */       return false;
/* 327 */     if (this.encoded == null)
/* 328 */       return false;
/* 329 */     byte[] arrayOfByte = ((PKCS10)paramObject).getEncoded();
/* 330 */     if (arrayOfByte == null) {
/* 331 */       return false;
/*     */     }
/* 333 */     return Arrays.equals(this.encoded, arrayOfByte);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 343 */     int i = 0;
/* 344 */     if (this.encoded != null)
/* 345 */       for (int j = 1; j < this.encoded.length; j++)
/* 346 */         i += this.encoded[j] * j;
/* 347 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.PKCS10
 * JD-Core Version:    0.6.2
 */