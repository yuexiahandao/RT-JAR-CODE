/*     */ package sun.security.pkcs;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyRep;
/*     */ import java.security.KeyRep.Type;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.x509.AlgorithmId;
/*     */ 
/*     */ public class PKCS8Key
/*     */   implements PrivateKey
/*     */ {
/*     */   private static final long serialVersionUID = -3836890099307167124L;
/*     */   protected AlgorithmId algid;
/*     */   protected byte[] key;
/*     */   protected byte[] encodedKey;
/*  67 */   public static final BigInteger version = BigInteger.ZERO;
/*     */ 
/*     */   public PKCS8Key()
/*     */   {
/*     */   }
/*     */ 
/*     */   private PKCS8Key(AlgorithmId paramAlgorithmId, byte[] paramArrayOfByte)
/*     */     throws InvalidKeyException
/*     */   {
/*  83 */     this.algid = paramAlgorithmId;
/*  84 */     this.key = paramArrayOfByte;
/*  85 */     encode();
/*     */   }
/*     */ 
/*     */   public static PKCS8Key parse(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/*  94 */     PrivateKey localPrivateKey = parseKey(paramDerValue);
/*  95 */     if ((localPrivateKey instanceof PKCS8Key)) {
/*  96 */       return (PKCS8Key)localPrivateKey;
/*     */     }
/*  98 */     throw new IOException("Provider did not return PKCS8Key");
/*     */   }
/*     */ 
/*     */   public static PrivateKey parseKey(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 120 */     if (paramDerValue.tag != 48) {
/* 121 */       throw new IOException("corrupt private key");
/*     */     }
/* 123 */     BigInteger localBigInteger = paramDerValue.data.getBigInteger();
/* 124 */     if (!version.equals(localBigInteger)) {
/* 125 */       throw new IOException("version mismatch: (supported: " + Debug.toHexString(version) + ", parsed: " + Debug.toHexString(localBigInteger));
/*     */     }
/*     */ 
/* 131 */     AlgorithmId localAlgorithmId = AlgorithmId.parse(paramDerValue.data.getDerValue());
/*     */     PrivateKey localPrivateKey;
/*     */     try
/*     */     {
/* 134 */       localPrivateKey = buildPKCS8Key(localAlgorithmId, paramDerValue.data.getOctetString());
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 137 */       throw new IOException("corrupt private key");
/*     */     }
/*     */ 
/* 140 */     if (paramDerValue.data.available() != 0)
/* 141 */       throw new IOException("excess private key");
/* 142 */     return localPrivateKey;
/*     */   }
/*     */ 
/*     */   protected void parseKeyBits()
/*     */     throws IOException, InvalidKeyException
/*     */   {
/* 160 */     encode();
/*     */   }
/*     */ 
/*     */   static PrivateKey buildPKCS8Key(AlgorithmId paramAlgorithmId, byte[] paramArrayOfByte)
/*     */     throws IOException, InvalidKeyException
/*     */   {
/* 176 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 177 */     encode(localDerOutputStream, paramAlgorithmId, paramArrayOfByte);
/* 178 */     PKCS8EncodedKeySpec localPKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(localDerOutputStream.toByteArray());
/*     */     try
/*     */     {
/* 183 */       KeyFactory localKeyFactory = KeyFactory.getInstance(paramAlgorithmId.getName());
/*     */ 
/* 186 */       return localKeyFactory.generatePrivate(localPKCS8EncodedKeySpec);
/*     */     }
/*     */     catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */     {
/*     */     }
/*     */     catch (InvalidKeySpecException localInvalidKeySpecException)
/*     */     {
/*     */     }
/*     */ 
/* 196 */     String str = "";
/*     */     try
/*     */     {
/* 202 */       Provider localProvider = Security.getProvider("SUN");
/* 203 */       if (localProvider == null)
/* 204 */         throw new InstantiationException();
/* 205 */       str = localProvider.getProperty("PrivateKey.PKCS#8." + paramAlgorithmId.getName());
/*     */ 
/* 207 */       if (str == null) {
/* 208 */         throw new InstantiationException();
/* 211 */       }
/*     */ Class localClass = null;
/*     */       Object localObject2;
/*     */       try { localClass = Class.forName(str);
/*     */       } catch (ClassNotFoundException localClassNotFoundException2) {
/* 215 */         localObject2 = ClassLoader.getSystemClassLoader();
/* 216 */         if (localObject2 != null) {
/* 217 */           localClass = ((ClassLoader)localObject2).loadClass(str);
/*     */         }
/*     */       }
/*     */ 
/* 221 */       Object localObject1 = null;
/*     */ 
/* 224 */       if (localClass != null)
/* 225 */         localObject1 = localClass.newInstance();
/* 226 */       if ((localObject1 instanceof PKCS8Key)) {
/* 227 */         localObject2 = (PKCS8Key)localObject1;
/* 228 */         ((PKCS8Key)localObject2).algid = paramAlgorithmId;
/* 229 */         ((PKCS8Key)localObject2).key = paramArrayOfByte;
/* 230 */         ((PKCS8Key)localObject2).parseKeyBits();
/* 231 */         return localObject2;
/*     */       }
/*     */     } catch (ClassNotFoundException localClassNotFoundException1) {
/*     */     } catch (InstantiationException localInstantiationException) {
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 237 */       throw new IOException(str + " [internal error]");
/*     */     }
/*     */ 
/* 240 */     PKCS8Key localPKCS8Key = new PKCS8Key();
/* 241 */     localPKCS8Key.algid = paramAlgorithmId;
/* 242 */     localPKCS8Key.key = paramArrayOfByte;
/* 243 */     return localPKCS8Key;
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/* 250 */     return this.algid.getName();
/*     */   }
/*     */ 
/*     */   public AlgorithmId getAlgorithmId()
/*     */   {
/* 256 */     return this.algid;
/*     */   }
/*     */ 
/*     */   public final void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 263 */     encode(paramDerOutputStream, this.algid, this.key);
/*     */   }
/*     */ 
/*     */   public synchronized byte[] getEncoded()
/*     */   {
/* 270 */     byte[] arrayOfByte = null;
/*     */     try {
/* 272 */       arrayOfByte = encode();
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/*     */     }
/* 275 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public String getFormat()
/*     */   {
/* 282 */     return "PKCS#8";
/*     */   }
/*     */ 
/*     */   public byte[] encode()
/*     */     throws InvalidKeyException
/*     */   {
/* 291 */     if (this.encodedKey == null)
/*     */     {
/*     */       try
/*     */       {
/* 295 */         DerOutputStream localDerOutputStream = new DerOutputStream();
/* 296 */         encode(localDerOutputStream);
/* 297 */         this.encodedKey = localDerOutputStream.toByteArray();
/*     */       }
/*     */       catch (IOException localIOException) {
/* 300 */         throw new InvalidKeyException("IOException : " + localIOException.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 304 */     return (byte[])this.encodedKey.clone();
/*     */   }
/*     */ 
/*     */   public void decode(InputStream paramInputStream)
/*     */     throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/* 328 */       DerValue localDerValue = new DerValue(paramInputStream);
/* 329 */       if (localDerValue.tag != 48) {
/* 330 */         throw new InvalidKeyException("invalid key format");
/*     */       }
/*     */ 
/* 333 */       BigInteger localBigInteger = localDerValue.data.getBigInteger();
/* 334 */       if (!localBigInteger.equals(version)) {
/* 335 */         throw new IOException("version mismatch: (supported: " + Debug.toHexString(version) + ", parsed: " + Debug.toHexString(localBigInteger));
/*     */       }
/*     */ 
/* 340 */       this.algid = AlgorithmId.parse(localDerValue.data.getDerValue());
/* 341 */       this.key = localDerValue.data.getOctetString();
/* 342 */       parseKeyBits();
/*     */ 
/* 344 */       if (localDerValue.data.available() == 0);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 350 */       throw new InvalidKeyException("IOException : " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void decode(byte[] paramArrayOfByte) throws InvalidKeyException
/*     */   {
/* 356 */     decode(new ByteArrayInputStream(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   protected Object writeReplace() throws ObjectStreamException {
/* 360 */     return new KeyRep(KeyRep.Type.PRIVATE, getAlgorithm(), getFormat(), getEncoded());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 374 */       decode(paramObjectInputStream);
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/* 377 */       localInvalidKeyException.printStackTrace();
/* 378 */       throw new IOException("deserialized key is invalid: " + localInvalidKeyException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   static void encode(DerOutputStream paramDerOutputStream, AlgorithmId paramAlgorithmId, byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 388 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 389 */     localDerOutputStream.putInteger(version);
/* 390 */     paramAlgorithmId.encode(localDerOutputStream);
/* 391 */     localDerOutputStream.putOctetString(paramArrayOfByte);
/* 392 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 406 */     if (this == paramObject) {
/* 407 */       return true;
/*     */     }
/*     */ 
/* 410 */     if ((paramObject instanceof Key))
/*     */     {
/*     */       byte[] arrayOfByte1;
/* 414 */       if (this.encodedKey != null)
/* 415 */         arrayOfByte1 = this.encodedKey;
/*     */       else {
/* 417 */         arrayOfByte1 = getEncoded();
/*     */       }
/*     */ 
/* 421 */       byte[] arrayOfByte2 = ((Key)paramObject).getEncoded();
/*     */ 
/* 425 */       if (arrayOfByte1.length != arrayOfByte2.length)
/* 426 */         return false;
/* 427 */       for (int i = 0; i < arrayOfByte1.length; i++) {
/* 428 */         if (arrayOfByte1[i] != arrayOfByte2[i]) {
/* 429 */           return false;
/*     */         }
/*     */       }
/* 432 */       return true;
/*     */     }
/*     */ 
/* 435 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 443 */     int i = 0;
/* 444 */     byte[] arrayOfByte = getEncoded();
/*     */ 
/* 446 */     for (int j = 1; j < arrayOfByte.length; j++) {
/* 447 */       i += arrayOfByte[j] * j;
/*     */     }
/* 449 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.pkcs.PKCS8Key
 * JD-Core Version:    0.6.2
 */