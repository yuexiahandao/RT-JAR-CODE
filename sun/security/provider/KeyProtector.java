/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.util.Arrays;
/*     */ import sun.security.pkcs.EncryptedPrivateKeyInfo;
/*     */ import sun.security.pkcs.PKCS8Key;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ final class KeyProtector
/*     */ {
/*     */   private static final int SALT_LEN = 20;
/*     */   private static final String DIGEST_ALG = "SHA";
/*     */   private static final int DIGEST_LEN = 20;
/*     */   private static final String KEY_PROTECTOR_OID = "1.3.6.1.4.1.42.2.17.1.1";
/*     */   private byte[] passwdBytes;
/*     */   private MessageDigest md;
/*     */ 
/*     */   public KeyProtector(char[] paramArrayOfChar)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 134 */     if (paramArrayOfChar == null) {
/* 135 */       throw new IllegalArgumentException("password can't be null");
/*     */     }
/* 137 */     this.md = MessageDigest.getInstance("SHA");
/*     */ 
/* 139 */     this.passwdBytes = new byte[paramArrayOfChar.length * 2];
/* 140 */     int i = 0; for (int j = 0; i < paramArrayOfChar.length; i++) {
/* 141 */       this.passwdBytes[(j++)] = ((byte)(paramArrayOfChar[i] >> '\b'));
/* 142 */       this.passwdBytes[(j++)] = ((byte)paramArrayOfChar[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */   {
/* 151 */     if (this.passwdBytes != null) {
/* 152 */       Arrays.fill(this.passwdBytes, (byte)0);
/* 153 */       this.passwdBytes = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte[] protect(Key paramKey)
/*     */     throws KeyStoreException
/*     */   {
/* 167 */     int m = 0;
/*     */ 
/* 169 */     if (paramKey == null) {
/* 170 */       throw new IllegalArgumentException("plaintext key can't be null");
/*     */     }
/*     */ 
/* 173 */     if (!"PKCS#8".equalsIgnoreCase(paramKey.getFormat())) {
/* 174 */       throw new KeyStoreException("Cannot get key bytes, not PKCS#8 encoded");
/*     */     }
/*     */ 
/* 178 */     byte[] arrayOfByte2 = paramKey.getEncoded();
/* 179 */     if (arrayOfByte2 == null) {
/* 180 */       throw new KeyStoreException("Cannot get key bytes, encoding not supported");
/*     */     }
/*     */ 
/* 185 */     int j = arrayOfByte2.length / 20;
/* 186 */     if (arrayOfByte2.length % 20 != 0) {
/* 187 */       j++;
/*     */     }
/*     */ 
/* 190 */     byte[] arrayOfByte3 = new byte[20];
/* 191 */     SecureRandom localSecureRandom = new SecureRandom();
/* 192 */     localSecureRandom.nextBytes(arrayOfByte3);
/*     */ 
/* 195 */     byte[] arrayOfByte4 = new byte[arrayOfByte2.length];
/*     */ 
/* 198 */     int i = 0; int k = 0; byte[] arrayOfByte1 = arrayOfByte3;
/*     */ 
/* 200 */     for (; i < j; 
/* 200 */       k += 20) {
/* 201 */       this.md.update(this.passwdBytes);
/* 202 */       this.md.update(arrayOfByte1);
/* 203 */       arrayOfByte1 = this.md.digest();
/* 204 */       this.md.reset();
/*     */ 
/* 206 */       if (i < j - 1) {
/* 207 */         System.arraycopy(arrayOfByte1, 0, arrayOfByte4, k, arrayOfByte1.length);
/*     */       }
/*     */       else
/* 210 */         System.arraycopy(arrayOfByte1, 0, arrayOfByte4, k, arrayOfByte4.length - k);
/* 200 */       i++;
/*     */     }
/*     */ 
/* 216 */     byte[] arrayOfByte5 = new byte[arrayOfByte2.length];
/* 217 */     for (i = 0; i < arrayOfByte5.length; i++) {
/* 218 */       arrayOfByte5[i] = ((byte)(arrayOfByte2[i] ^ arrayOfByte4[i]));
/*     */     }
/*     */ 
/* 222 */     byte[] arrayOfByte6 = new byte[arrayOfByte3.length + arrayOfByte5.length + 20];
/* 223 */     System.arraycopy(arrayOfByte3, 0, arrayOfByte6, m, arrayOfByte3.length);
/* 224 */     m += arrayOfByte3.length;
/* 225 */     System.arraycopy(arrayOfByte5, 0, arrayOfByte6, m, arrayOfByte5.length);
/* 226 */     m += arrayOfByte5.length;
/*     */ 
/* 229 */     this.md.update(this.passwdBytes);
/* 230 */     Arrays.fill(this.passwdBytes, (byte)0);
/* 231 */     this.passwdBytes = null;
/* 232 */     this.md.update(arrayOfByte2);
/* 233 */     arrayOfByte1 = this.md.digest();
/* 234 */     this.md.reset();
/* 235 */     System.arraycopy(arrayOfByte1, 0, arrayOfByte6, m, arrayOfByte1.length);
/*     */     try
/*     */     {
/* 241 */       AlgorithmId localAlgorithmId = new AlgorithmId(new ObjectIdentifier("1.3.6.1.4.1.42.2.17.1.1"));
/* 242 */       return new EncryptedPrivateKeyInfo(localAlgorithmId, arrayOfByte6).getEncoded();
/*     */     } catch (IOException localIOException) {
/* 244 */       throw new KeyStoreException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Key recover(EncryptedPrivateKeyInfo paramEncryptedPrivateKeyInfo)
/*     */     throws UnrecoverableKeyException
/*     */   {
/* 262 */     AlgorithmId localAlgorithmId = paramEncryptedPrivateKeyInfo.getAlgorithm();
/* 263 */     if (!localAlgorithmId.getOID().toString().equals("1.3.6.1.4.1.42.2.17.1.1")) {
/* 264 */       throw new UnrecoverableKeyException("Unsupported key protection algorithm");
/*     */     }
/*     */ 
/* 268 */     byte[] arrayOfByte2 = paramEncryptedPrivateKeyInfo.getEncryptedData();
/*     */ 
/* 274 */     byte[] arrayOfByte3 = new byte[20];
/* 275 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, 20);
/*     */ 
/* 278 */     int m = arrayOfByte2.length - 20 - 20;
/* 279 */     int j = m / 20;
/* 280 */     if (m % 20 != 0) j++;
/*     */ 
/* 283 */     byte[] arrayOfByte4 = new byte[m];
/* 284 */     System.arraycopy(arrayOfByte2, 20, arrayOfByte4, 0, m);
/*     */ 
/* 287 */     byte[] arrayOfByte5 = new byte[arrayOfByte4.length];
/*     */ 
/* 290 */     int i = 0; int k = 0; byte[] arrayOfByte1 = arrayOfByte3;
/*     */ 
/* 292 */     for (; i < j; 
/* 292 */       k += 20) {
/* 293 */       this.md.update(this.passwdBytes);
/* 294 */       this.md.update(arrayOfByte1);
/* 295 */       arrayOfByte1 = this.md.digest();
/* 296 */       this.md.reset();
/*     */ 
/* 298 */       if (i < j - 1) {
/* 299 */         System.arraycopy(arrayOfByte1, 0, arrayOfByte5, k, arrayOfByte1.length);
/*     */       }
/*     */       else
/* 302 */         System.arraycopy(arrayOfByte1, 0, arrayOfByte5, k, arrayOfByte5.length - k);
/* 292 */       i++;
/*     */     }
/*     */ 
/* 308 */     byte[] arrayOfByte6 = new byte[arrayOfByte4.length];
/* 309 */     for (i = 0; i < arrayOfByte6.length; i++) {
/* 310 */       arrayOfByte6[i] = ((byte)(arrayOfByte4[i] ^ arrayOfByte5[i]));
/*     */     }
/*     */ 
/* 320 */     this.md.update(this.passwdBytes);
/* 321 */     Arrays.fill(this.passwdBytes, (byte)0);
/* 322 */     this.passwdBytes = null;
/* 323 */     this.md.update(arrayOfByte6);
/* 324 */     arrayOfByte1 = this.md.digest();
/* 325 */     this.md.reset();
/* 326 */     for (i = 0; i < arrayOfByte1.length; i++) {
/* 327 */       if (arrayOfByte1[i] != arrayOfByte2[(20 + m + i)]) {
/* 328 */         throw new UnrecoverableKeyException("Cannot recover key");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 336 */       return PKCS8Key.parseKey(new DerValue(arrayOfByte6));
/*     */     } catch (IOException localIOException) {
/* 338 */       throw new UnrecoverableKeyException(localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.KeyProtector
 * JD-Core Version:    0.6.2
 */