/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Security;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import java.util.Arrays;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class X509Key
/*     */   implements PublicKey
/*     */ {
/*     */   private static final long serialVersionUID = -5359250853002055002L;
/*     */   protected AlgorithmId algid;
/*     */ 
/*     */   @Deprecated
/*  75 */   protected byte[] key = null;
/*     */ 
/*  83 */   private int unusedBits = 0;
/*     */ 
/*  86 */   private BitArray bitStringKey = null;
/*     */   protected byte[] encodedKey;
/*     */ 
/*     */   public X509Key()
/*     */   {
/*     */   }
/*     */ 
/*     */   private X509Key(AlgorithmId paramAlgorithmId, BitArray paramBitArray)
/*     */     throws InvalidKeyException
/*     */   {
/* 105 */     this.algid = paramAlgorithmId;
/* 106 */     setKey(paramBitArray);
/* 107 */     encode();
/*     */   }
/*     */ 
/*     */   protected void setKey(BitArray paramBitArray)
/*     */   {
/* 114 */     this.bitStringKey = ((BitArray)paramBitArray.clone());
/*     */ 
/* 120 */     this.key = paramBitArray.toByteArray();
/* 121 */     int i = paramBitArray.length() % 8;
/* 122 */     this.unusedBits = (i == 0 ? 0 : 8 - i);
/*     */   }
/*     */ 
/*     */   protected BitArray getKey()
/*     */   {
/* 139 */     this.bitStringKey = new BitArray(this.key.length * 8 - this.unusedBits, this.key);
/*     */ 
/* 143 */     return (BitArray)this.bitStringKey.clone();
/*     */   }
/*     */ 
/*     */   public static PublicKey parse(DerValue paramDerValue)
/*     */     throws IOException
/*     */   {
/* 165 */     if (paramDerValue.tag != 48) {
/* 166 */       throw new IOException("corrupt subject key");
/* 168 */     }AlgorithmId localAlgorithmId = AlgorithmId.parse(paramDerValue.data.getDerValue());
/*     */     PublicKey localPublicKey;
/*     */     try { localPublicKey = buildX509Key(localAlgorithmId, paramDerValue.data.getUnalignedBitString()); }
/*     */     catch (InvalidKeyException localInvalidKeyException)
/*     */     {
/* 174 */       throw new IOException("subject key, " + localInvalidKeyException.getMessage(), localInvalidKeyException);
/*     */     }
/*     */ 
/* 177 */     if (paramDerValue.data.available() != 0)
/* 178 */       throw new IOException("excess subject key");
/* 179 */     return localPublicKey;
/*     */   }
/*     */ 
/*     */   protected void parseKeyBits()
/*     */     throws IOException, InvalidKeyException
/*     */   {
/* 197 */     encode();
/*     */   }
/*     */ 
/*     */   static PublicKey buildX509Key(AlgorithmId paramAlgorithmId, BitArray paramBitArray)
/*     */     throws IOException, InvalidKeyException
/*     */   {
/* 213 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 214 */     encode(localDerOutputStream, paramAlgorithmId, paramBitArray);
/* 215 */     X509EncodedKeySpec localX509EncodedKeySpec = new X509EncodedKeySpec(localDerOutputStream.toByteArray());
/*     */     try
/*     */     {
/* 220 */       KeyFactory localKeyFactory = KeyFactory.getInstance(paramAlgorithmId.getName());
/*     */ 
/* 223 */       return localKeyFactory.generatePublic(localX509EncodedKeySpec);
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*     */     }
/*     */     catch (InvalidKeySpecException localInvalidKeySpecException) {
/* 227 */       throw new InvalidKeyException(localInvalidKeySpecException.getMessage(), localInvalidKeySpecException);
/*     */     }
/*     */ 
/* 233 */     String str = "";
/*     */     try
/*     */     {
/* 239 */       Provider localProvider = Security.getProvider("SUN");
/* 240 */       if (localProvider == null)
/* 241 */         throw new InstantiationException();
/* 242 */       str = localProvider.getProperty("PublicKey.X.509." + paramAlgorithmId.getName());
/*     */ 
/* 244 */       if (str == null) {
/* 245 */         throw new InstantiationException();
/* 248 */       }
/*     */ Class localClass = null;
/*     */       Object localObject2;
/*     */       try { localClass = Class.forName(str);
/*     */       } catch (ClassNotFoundException localClassNotFoundException2) {
/* 252 */         localObject2 = ClassLoader.getSystemClassLoader();
/* 253 */         if (localObject2 != null) {
/* 254 */           localClass = ((ClassLoader)localObject2).loadClass(str);
/*     */         }
/*     */       }
/*     */ 
/* 258 */       Object localObject1 = null;
/*     */ 
/* 261 */       if (localClass != null)
/* 262 */         localObject1 = localClass.newInstance();
/* 263 */       if ((localObject1 instanceof X509Key)) {
/* 264 */         localObject2 = (X509Key)localObject1;
/* 265 */         ((X509Key)localObject2).algid = paramAlgorithmId;
/* 266 */         ((X509Key)localObject2).setKey(paramBitArray);
/* 267 */         ((X509Key)localObject2).parseKeyBits();
/* 268 */         return localObject2;
/*     */       }
/*     */     } catch (ClassNotFoundException localClassNotFoundException1) {
/*     */     } catch (InstantiationException localInstantiationException) {
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 274 */       throw new IOException(str + " [internal error]");
/*     */     }
/*     */ 
/* 277 */     X509Key localX509Key = new X509Key(paramAlgorithmId, paramBitArray);
/* 278 */     return localX509Key;
/*     */   }
/*     */ 
/*     */   public String getAlgorithm()
/*     */   {
/* 285 */     return this.algid.getName();
/*     */   }
/*     */ 
/*     */   public AlgorithmId getAlgorithmId()
/*     */   {
/* 291 */     return this.algid;
/*     */   }
/*     */ 
/*     */   public final void encode(DerOutputStream paramDerOutputStream)
/*     */     throws IOException
/*     */   {
/* 300 */     encode(paramDerOutputStream, this.algid, getKey());
/*     */   }
/*     */ 
/*     */   public byte[] getEncoded()
/*     */   {
/*     */     try
/*     */     {
/* 308 */       return (byte[])getEncodedInternal().clone();
/*     */     }
/*     */     catch (InvalidKeyException localInvalidKeyException) {
/*     */     }
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedInternal() throws InvalidKeyException {
/* 316 */     byte[] arrayOfByte = this.encodedKey;
/* 317 */     if (arrayOfByte == null) {
/*     */       try {
/* 319 */         DerOutputStream localDerOutputStream = new DerOutputStream();
/* 320 */         encode(localDerOutputStream);
/* 321 */         arrayOfByte = localDerOutputStream.toByteArray();
/*     */       } catch (IOException localIOException) {
/* 323 */         throw new InvalidKeyException("IOException : " + localIOException.getMessage());
/*     */       }
/*     */ 
/* 326 */       this.encodedKey = arrayOfByte;
/*     */     }
/* 328 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public String getFormat()
/*     */   {
/* 335 */     return "X.509";
/*     */   }
/*     */ 
/*     */   public byte[] encode()
/*     */     throws InvalidKeyException
/*     */   {
/* 344 */     return (byte[])getEncodedInternal().clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 352 */     HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/*     */ 
/* 354 */     return "algorithm = " + this.algid.toString() + ", unparsed keybits = \n" + localHexDumpEncoder.encodeBuffer(this.key);
/*     */   }
/*     */ 
/*     */   public void decode(InputStream paramInputStream)
/*     */     throws InvalidKeyException
/*     */   {
/*     */     try
/*     */     {
/* 385 */       DerValue localDerValue = new DerValue(paramInputStream);
/* 386 */       if (localDerValue.tag != 48) {
/* 387 */         throw new InvalidKeyException("invalid key format");
/*     */       }
/* 389 */       this.algid = AlgorithmId.parse(localDerValue.data.getDerValue());
/* 390 */       setKey(localDerValue.data.getUnalignedBitString());
/* 391 */       parseKeyBits();
/* 392 */       if (localDerValue.data.available() != 0)
/* 393 */         throw new InvalidKeyException("excess key data");
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 397 */       throw new InvalidKeyException("IOException: " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void decode(byte[] paramArrayOfByte) throws InvalidKeyException
/*     */   {
/* 403 */     decode(new ByteArrayInputStream(paramArrayOfByte));
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 411 */     paramObjectOutputStream.write(getEncoded());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 420 */       decode(paramObjectInputStream);
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/* 422 */       localInvalidKeyException.printStackTrace();
/* 423 */       throw new IOException("deserialized key is invalid: " + localInvalidKeyException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 429 */     if (this == paramObject) {
/* 430 */       return true;
/*     */     }
/* 432 */     if (!(paramObject instanceof Key))
/* 433 */       return false;
/*     */     try
/*     */     {
/* 436 */       byte[] arrayOfByte1 = getEncodedInternal();
/*     */       byte[] arrayOfByte2;
/* 438 */       if ((paramObject instanceof X509Key))
/* 439 */         arrayOfByte2 = ((X509Key)paramObject).getEncodedInternal();
/*     */       else {
/* 441 */         arrayOfByte2 = ((Key)paramObject).getEncoded();
/*     */       }
/* 443 */       return Arrays.equals(arrayOfByte1, arrayOfByte2); } catch (InvalidKeyException localInvalidKeyException) {
/*     */     }
/* 445 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*     */     try
/*     */     {
/* 455 */       byte[] arrayOfByte = getEncodedInternal();
/* 456 */       int i = arrayOfByte.length;
/* 457 */       for (int j = 0; j < arrayOfByte.length; j++) {
/* 458 */         i += (arrayOfByte[j] & 0xFF) * 37;
/*     */       }
/* 460 */       return i;
/*     */     } catch (InvalidKeyException localInvalidKeyException) {
/*     */     }
/* 463 */     return 0;
/*     */   }
/*     */ 
/*     */   static void encode(DerOutputStream paramDerOutputStream, AlgorithmId paramAlgorithmId, BitArray paramBitArray)
/*     */     throws IOException
/*     */   {
/* 472 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 473 */     paramAlgorithmId.encode(localDerOutputStream);
/* 474 */     localDerOutputStream.putUnalignedBitString(paramBitArray);
/* 475 */     paramDerOutputStream.write((byte)48, localDerOutputStream);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.X509Key
 * JD-Core Version:    0.6.2
 */