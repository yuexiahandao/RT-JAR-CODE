/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.SecureRandomSpi;
/*     */ 
/*     */ public final class SecureRandom extends SecureRandomSpi
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3581829991155417889L;
/*     */   private static final int DIGEST_SIZE = 20;
/*     */   private transient MessageDigest digest;
/*     */   private byte[] state;
/*     */   private byte[] remainder;
/*     */   private int remCount;
/*     */ 
/*     */   public SecureRandom()
/*     */   {
/*  79 */     init(null);
/*     */   }
/*     */ 
/*     */   private SecureRandom(byte[] paramArrayOfByte)
/*     */   {
/*  89 */     init(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   private void init(byte[] paramArrayOfByte)
/*     */   {
/*     */     try
/*     */     {
/* 102 */       this.digest = MessageDigest.getInstance("SHA", "SUN");
/*     */     }
/*     */     catch (NoSuchProviderException|NoSuchAlgorithmException localNoSuchProviderException) {
/*     */       try {
/* 106 */         this.digest = MessageDigest.getInstance("SHA");
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 108 */         throw new InternalError("internal error: SHA-1 not available.");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 113 */     if (paramArrayOfByte != null)
/* 114 */       engineSetSeed(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public byte[] engineGenerateSeed(int paramInt)
/*     */   {
/* 135 */     byte[] arrayOfByte = new byte[paramInt];
/* 136 */     SeedGenerator.generateSeed(arrayOfByte);
/* 137 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public synchronized void engineSetSeed(byte[] paramArrayOfByte)
/*     */   {
/* 148 */     if (this.state != null) {
/* 149 */       this.digest.update(this.state);
/* 150 */       for (int i = 0; i < this.state.length; i++)
/* 151 */         this.state[i] = 0;
/*     */     }
/* 153 */     this.state = this.digest.digest(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   private static void updateState(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2) {
/* 157 */     int i = 1;
/* 158 */     int j = 0;
/* 159 */     int k = 0;
/* 160 */     int m = 0;
/*     */ 
/* 163 */     for (int n = 0; n < paramArrayOfByte1.length; n++)
/*     */     {
/* 165 */       j = paramArrayOfByte1[n] + paramArrayOfByte2[n] + i;
/*     */ 
/* 167 */       k = (byte)j;
/*     */ 
/* 169 */       m |= (paramArrayOfByte1[n] != k ? 1 : 0);
/* 170 */       paramArrayOfByte1[n] = k;
/*     */ 
/* 172 */       i = j >> 8;
/*     */     }
/*     */ 
/* 176 */     if (m == 0)
/*     */     {
/*     */       int tmp79_78 = 0; paramArrayOfByte1[tmp79_78] = ((byte)(paramArrayOfByte1[tmp79_78] + 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void engineNextBytes(byte[] paramArrayOfByte)
/*     */   {
/* 208 */     int i = 0;
/*     */ 
/* 210 */     byte[] arrayOfByte1 = this.remainder;
/*     */ 
/* 212 */     if (this.state == null) {
/* 213 */       byte[] arrayOfByte2 = new byte[20];
/* 214 */       SeederHolder.seeder.engineNextBytes(arrayOfByte2);
/* 215 */       this.state = this.digest.digest(arrayOfByte2);
/*     */     }
/*     */ 
/* 219 */     int k = this.remCount;
/*     */     int j;
/*     */     int m;
/* 220 */     if (k > 0)
/*     */     {
/* 222 */       j = paramArrayOfByte.length - i < 20 - k ? paramArrayOfByte.length - i : 20 - k;
/*     */ 
/* 225 */       for (m = 0; m < j; m++) {
/* 226 */         paramArrayOfByte[m] = arrayOfByte1[k];
/* 227 */         arrayOfByte1[(k++)] = 0;
/*     */       }
/* 229 */       this.remCount += j;
/* 230 */       i += j;
/*     */     }
/*     */ 
/* 234 */     while (i < paramArrayOfByte.length)
/*     */     {
/* 236 */       this.digest.update(this.state);
/* 237 */       arrayOfByte1 = this.digest.digest();
/* 238 */       updateState(this.state, arrayOfByte1);
/*     */ 
/* 241 */       j = paramArrayOfByte.length - i > 20 ? 20 : paramArrayOfByte.length - i;
/*     */ 
/* 244 */       for (m = 0; m < j; m++) {
/* 245 */         paramArrayOfByte[(i++)] = arrayOfByte1[m];
/* 246 */         arrayOfByte1[m] = 0;
/*     */       }
/* 248 */       this.remCount += j;
/*     */     }
/*     */ 
/* 252 */     this.remainder = arrayOfByte1;
/* 253 */     this.remCount %= 20;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 269 */     paramObjectInputStream.defaultReadObject();
/*     */     try
/*     */     {
/* 276 */       this.digest = MessageDigest.getInstance("SHA", "SUN");
/*     */     }
/*     */     catch (NoSuchProviderException|NoSuchAlgorithmException localNoSuchProviderException) {
/*     */       try {
/* 280 */         this.digest = MessageDigest.getInstance("SHA");
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 282 */         throw new InternalError("internal error: SHA-1 not available.");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SeederHolder
/*     */   {
/* 195 */     private static final SecureRandom seeder = new SecureRandom(SeedGenerator.getSystemEntropy(), null);
/*     */ 
/* 196 */     static { byte[] arrayOfByte = new byte[20];
/* 197 */       SeedGenerator.generateSeed(arrayOfByte);
/* 198 */       seeder.engineSetSeed(arrayOfByte);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.SecureRandom
 * JD-Core Version:    0.6.2
 */