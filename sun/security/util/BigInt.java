/*     */ package sun.security.util;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public final class BigInt
/*     */ {
/*     */   private byte[] places;
/*     */   private static final String digits = "0123456789abcdef";
/*     */ 
/*     */   public BigInt(byte[] paramArrayOfByte)
/*     */   {
/*  56 */     this.places = ((byte[])paramArrayOfByte.clone());
/*     */   }
/*     */ 
/*     */   public BigInt(BigInteger paramBigInteger)
/*     */   {
/*  63 */     byte[] arrayOfByte = paramBigInteger.toByteArray();
/*     */ 
/*  65 */     if ((arrayOfByte[0] & 0x80) != 0) {
/*  66 */       throw new IllegalArgumentException("negative BigInteger");
/*     */     }
/*     */ 
/*  70 */     if (arrayOfByte[0] != 0) {
/*  71 */       this.places = arrayOfByte;
/*     */     } else {
/*  73 */       this.places = new byte[arrayOfByte.length - 1];
/*  74 */       for (int i = 1; i < arrayOfByte.length; i++)
/*  75 */         this.places[(i - 1)] = arrayOfByte[i];
/*     */     }
/*     */   }
/*     */ 
/*     */   public BigInt(int paramInt)
/*     */   {
/*  85 */     if (paramInt < 256) {
/*  86 */       this.places = new byte[1];
/*  87 */       this.places[0] = ((byte)paramInt);
/*  88 */     } else if (paramInt < 65536) {
/*  89 */       this.places = new byte[2];
/*  90 */       this.places[0] = ((byte)(paramInt >> 8));
/*  91 */       this.places[1] = ((byte)paramInt);
/*  92 */     } else if (paramInt < 16777216) {
/*  93 */       this.places = new byte[3];
/*  94 */       this.places[0] = ((byte)(paramInt >> 16));
/*  95 */       this.places[1] = ((byte)(paramInt >> 8));
/*  96 */       this.places[2] = ((byte)paramInt);
/*     */     } else {
/*  98 */       this.places = new byte[4];
/*  99 */       this.places[0] = ((byte)(paramInt >> 24));
/* 100 */       this.places[1] = ((byte)(paramInt >> 16));
/* 101 */       this.places[2] = ((byte)(paramInt >> 8));
/* 102 */       this.places[3] = ((byte)paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int toInt()
/*     */   {
/* 112 */     if (this.places.length > 4)
/* 113 */       throw new NumberFormatException("BigInt.toLong, too big");
/* 114 */     int i = 0; for (int j = 0; 
/* 115 */       j < this.places.length; j++)
/* 116 */       i = (i << 8) + (this.places[j] & 0xFF);
/* 117 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 126 */     return hexify();
/*     */   }
/*     */ 
/*     */   public BigInteger toBigInteger()
/*     */   {
/* 133 */     return new BigInteger(1, this.places);
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/* 139 */     return (byte[])this.places.clone();
/*     */   }
/*     */ 
/*     */   private String hexify() {
/* 143 */     if (this.places.length == 0) {
/* 144 */       return "  0  ";
/*     */     }
/* 146 */     StringBuffer localStringBuffer = new StringBuffer(this.places.length * 2);
/* 147 */     localStringBuffer.append("    ");
/* 148 */     for (int i = 0; i < this.places.length; i++) {
/* 149 */       localStringBuffer.append("0123456789abcdef".charAt(this.places[i] >> 4 & 0xF));
/* 150 */       localStringBuffer.append("0123456789abcdef".charAt(this.places[i] & 0xF));
/* 151 */       if ((i + 1) % 32 == 0) {
/* 152 */         if (i + 1 != this.places.length)
/* 153 */           localStringBuffer.append("\n    ");
/* 154 */       } else if ((i + 1) % 4 == 0)
/* 155 */         localStringBuffer.append(' ');
/*     */     }
/* 157 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 167 */     if ((paramObject instanceof BigInt))
/* 168 */       return equals((BigInt)paramObject);
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(BigInt paramBigInt)
/*     */   {
/* 178 */     if (this == paramBigInt) {
/* 179 */       return true;
/*     */     }
/* 181 */     byte[] arrayOfByte = paramBigInt.toByteArray();
/* 182 */     if (this.places.length != arrayOfByte.length)
/* 183 */       return false;
/* 184 */     for (int i = 0; i < this.places.length; i++)
/* 185 */       if (this.places[i] != arrayOfByte[i])
/* 186 */         return false;
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 196 */     return hexify().hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.BigInt
 * JD-Core Version:    0.6.2
 */