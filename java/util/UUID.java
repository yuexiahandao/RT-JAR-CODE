/*     */ package java.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ 
/*     */ public final class UUID
/*     */   implements Serializable, Comparable<UUID>
/*     */ {
/*     */   private static final long serialVersionUID = -4856846361193249489L;
/*     */   private final long mostSigBits;
/*     */   private final long leastSigBits;
/*     */ 
/*     */   private UUID(byte[] paramArrayOfByte)
/*     */   {
/* 105 */     long l1 = 0L;
/* 106 */     long l2 = 0L;
/* 107 */     assert (paramArrayOfByte.length == 16) : "data must be 16 bytes in length";
/* 108 */     for (int i = 0; i < 8; i++)
/* 109 */       l1 = l1 << 8 | paramArrayOfByte[i] & 0xFF;
/* 110 */     for (i = 8; i < 16; i++)
/* 111 */       l2 = l2 << 8 | paramArrayOfByte[i] & 0xFF;
/* 112 */     this.mostSigBits = l1;
/* 113 */     this.leastSigBits = l2;
/*     */   }
/*     */ 
/*     */   public UUID(long paramLong1, long paramLong2)
/*     */   {
/* 129 */     this.mostSigBits = paramLong1;
/* 130 */     this.leastSigBits = paramLong2;
/*     */   }
/*     */ 
/*     */   public static UUID randomUUID()
/*     */   {
/* 142 */     SecureRandom localSecureRandom = Holder.numberGenerator;
/*     */ 
/* 144 */     byte[] arrayOfByte = new byte[16];
/* 145 */     localSecureRandom.nextBytes(arrayOfByte);
/*     */     byte[] tmp17_14 = arrayOfByte; tmp17_14[6] = ((byte)(tmp17_14[6] & 0xF));
/*     */     byte[] tmp27_24 = arrayOfByte; tmp27_24[6] = ((byte)(tmp27_24[6] | 0x40));
/*     */     byte[] tmp37_34 = arrayOfByte; tmp37_34[8] = ((byte)(tmp37_34[8] & 0x3F));
/*     */     byte[] tmp47_44 = arrayOfByte; tmp47_44[8] = ((byte)(tmp47_44[8] | 0x80));
/* 150 */     return new UUID(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public static UUID nameUUIDFromBytes(byte[] paramArrayOfByte)
/*     */   {
/*     */     MessageDigest localMessageDigest;
/*     */     try
/*     */     {
/* 165 */       localMessageDigest = MessageDigest.getInstance("MD5");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 167 */       throw new InternalError("MD5 not supported");
/*     */     }
/* 169 */     byte[] arrayOfByte = localMessageDigest.digest(paramArrayOfByte);
/*     */     byte[] tmp29_26 = arrayOfByte; tmp29_26[6] = ((byte)(tmp29_26[6] & 0xF));
/*     */     byte[] tmp39_36 = arrayOfByte; tmp39_36[6] = ((byte)(tmp39_36[6] | 0x30));
/*     */     byte[] tmp49_46 = arrayOfByte; tmp49_46[8] = ((byte)(tmp49_46[8] & 0x3F));
/*     */     byte[] tmp59_56 = arrayOfByte; tmp59_56[8] = ((byte)(tmp59_56[8] | 0x80));
/* 174 */     return new UUID(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public static UUID fromString(String paramString)
/*     */   {
/* 192 */     String[] arrayOfString = paramString.split("-");
/* 193 */     if (arrayOfString.length != 5)
/* 194 */       throw new IllegalArgumentException("Invalid UUID string: " + paramString);
/* 195 */     for (int i = 0; i < 5; i++) {
/* 196 */       arrayOfString[i] = ("0x" + arrayOfString[i]);
/*     */     }
/* 198 */     long l1 = Long.decode(arrayOfString[0]).longValue();
/* 199 */     l1 <<= 16;
/* 200 */     l1 |= Long.decode(arrayOfString[1]).longValue();
/* 201 */     l1 <<= 16;
/* 202 */     l1 |= Long.decode(arrayOfString[2]).longValue();
/*     */ 
/* 204 */     long l2 = Long.decode(arrayOfString[3]).longValue();
/* 205 */     l2 <<= 48;
/* 206 */     l2 |= Long.decode(arrayOfString[4]).longValue();
/*     */ 
/* 208 */     return new UUID(l1, l2);
/*     */   }
/*     */ 
/*     */   public long getLeastSignificantBits()
/*     */   {
/* 219 */     return this.leastSigBits;
/*     */   }
/*     */ 
/*     */   public long getMostSignificantBits()
/*     */   {
/* 228 */     return this.mostSigBits;
/*     */   }
/*     */ 
/*     */   public int version()
/*     */   {
/* 247 */     return (int)(this.mostSigBits >> 12 & 0xF);
/*     */   }
/*     */ 
/*     */   public int variant()
/*     */   {
/* 271 */     return (int)(this.leastSigBits >>> (int)(64L - (this.leastSigBits >>> 62)) & this.leastSigBits >> 63);
/*     */   }
/*     */ 
/*     */   public long timestamp()
/*     */   {
/* 291 */     if (version() != 1) {
/* 292 */       throw new UnsupportedOperationException("Not a time-based UUID");
/*     */     }
/*     */ 
/* 295 */     return (this.mostSigBits & 0xFFF) << 48 | (this.mostSigBits >> 16 & 0xFFFF) << 32 | this.mostSigBits >>> 32;
/*     */   }
/*     */ 
/*     */   public int clockSequence()
/*     */   {
/* 317 */     if (version() != 1) {
/* 318 */       throw new UnsupportedOperationException("Not a time-based UUID");
/*     */     }
/*     */ 
/* 321 */     return (int)((this.leastSigBits & 0x0) >>> 48);
/*     */   }
/*     */ 
/*     */   public long node()
/*     */   {
/* 341 */     if (version() != 1) {
/* 342 */       throw new UnsupportedOperationException("Not a time-based UUID");
/*     */     }
/*     */ 
/* 345 */     return this.leastSigBits & 0xFFFFFFFF;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 375 */     return digits(this.mostSigBits >> 32, 8) + "-" + digits(this.mostSigBits >> 16, 4) + "-" + digits(this.mostSigBits, 4) + "-" + digits(this.leastSigBits >> 48, 4) + "-" + digits(this.leastSigBits, 12);
/*     */   }
/*     */ 
/*     */   private static String digits(long paramLong, int paramInt)
/*     */   {
/* 384 */     long l = 1L << paramInt * 4;
/* 385 */     return Long.toHexString(l | paramLong & l - 1L).substring(1);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 394 */     long l = this.mostSigBits ^ this.leastSigBits;
/* 395 */     return (int)(l >> 32) ^ (int)l;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 411 */     if ((null == paramObject) || (paramObject.getClass() != UUID.class))
/* 412 */       return false;
/* 413 */     UUID localUUID = (UUID)paramObject;
/* 414 */     return (this.mostSigBits == localUUID.mostSigBits) && (this.leastSigBits == localUUID.leastSigBits);
/*     */   }
/*     */ 
/*     */   public int compareTo(UUID paramUUID)
/*     */   {
/* 437 */     return this.leastSigBits > paramUUID.leastSigBits ? 1 : this.leastSigBits < paramUUID.leastSigBits ? -1 : this.mostSigBits > paramUUID.mostSigBits ? 1 : this.mostSigBits < paramUUID.mostSigBits ? -1 : 0;
/*     */   }
/*     */ 
/*     */   private static class Holder
/*     */   {
/*  96 */     static final SecureRandom numberGenerator = new SecureRandom();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.UUID
 * JD-Core Version:    0.6.2
 */