/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class BitArray
/*     */ {
/*     */   private byte[] repn;
/*     */   private int length;
/*     */   private static final int BITS_PER_UNIT = 8;
/* 219 */   private static final byte[][] NYBBLE = { { 48, 48, 48, 48 }, { 48, 48, 48, 49 }, { 48, 48, 49, 48 }, { 48, 48, 49, 49 }, { 48, 49, 48, 48 }, { 48, 49, 48, 49 }, { 48, 49, 49, 48 }, { 48, 49, 49, 49 }, { 49, 48, 48, 48 }, { 49, 48, 48, 49 }, { 49, 48, 49, 48 }, { 49, 48, 49, 49 }, { 49, 49, 48, 48 }, { 49, 49, 48, 49 }, { 49, 49, 49, 48 }, { 49, 49, 49, 49 } };
/*     */   private static final int BYTES_PER_LINE = 8;
/*     */ 
/*     */   private static int subscript(int paramInt)
/*     */   {
/*  46 */     return paramInt / 8;
/*     */   }
/*     */ 
/*     */   private static int position(int paramInt) {
/*  50 */     return 1 << 7 - paramInt % 8;
/*     */   }
/*     */ 
/*     */   public BitArray(int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/*  57 */     if (paramInt < 0) {
/*  58 */       throw new IllegalArgumentException("Negative length for BitArray");
/*     */     }
/*     */ 
/*  61 */     this.length = paramInt;
/*     */ 
/*  63 */     this.repn = new byte[(paramInt + 8 - 1) / 8];
/*     */   }
/*     */ 
/*     */   public BitArray(int paramInt, byte[] paramArrayOfByte)
/*     */     throws IllegalArgumentException
/*     */   {
/*  76 */     if (paramInt < 0) {
/*  77 */       throw new IllegalArgumentException("Negative length for BitArray");
/*     */     }
/*  79 */     if (paramArrayOfByte.length * 8 < paramInt) {
/*  80 */       throw new IllegalArgumentException("Byte array too short to represent bit array of given length");
/*     */     }
/*     */ 
/*  84 */     this.length = paramInt;
/*     */ 
/*  86 */     int i = (paramInt + 8 - 1) / 8;
/*  87 */     int j = i * 8 - paramInt;
/*  88 */     int k = (byte)(255 << j);
/*     */ 
/*  95 */     this.repn = new byte[i];
/*  96 */     System.arraycopy(paramArrayOfByte, 0, this.repn, 0, i);
/*  97 */     if (i > 0)
/*     */     {
/*     */       int tmp98_97 = (i - 1);
/*     */       byte[] tmp98_92 = this.repn; tmp98_92[tmp98_97] = ((byte)(tmp98_92[tmp98_97] & k));
/*     */     }
/*     */   }
/*     */ 
/*     */   public BitArray(boolean[] paramArrayOfBoolean)
/*     */   {
/* 107 */     this.length = paramArrayOfBoolean.length;
/* 108 */     this.repn = new byte[(this.length + 7) / 8];
/*     */ 
/* 110 */     for (int i = 0; i < this.length; i++)
/* 111 */       set(i, paramArrayOfBoolean[i]);
/*     */   }
/*     */ 
/*     */   private BitArray(BitArray paramBitArray)
/*     */   {
/* 120 */     this.length = paramBitArray.length;
/* 121 */     this.repn = ((byte[])paramBitArray.repn.clone());
/*     */   }
/*     */ 
/*     */   public boolean get(int paramInt)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 128 */     if ((paramInt < 0) || (paramInt >= this.length)) {
/* 129 */       throw new ArrayIndexOutOfBoundsException(Integer.toString(paramInt));
/*     */     }
/*     */ 
/* 132 */     return (this.repn[subscript(paramInt)] & position(paramInt)) != 0;
/*     */   }
/*     */ 
/*     */   public void set(int paramInt, boolean paramBoolean)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 140 */     if ((paramInt < 0) || (paramInt >= this.length)) {
/* 141 */       throw new ArrayIndexOutOfBoundsException(Integer.toString(paramInt));
/*     */     }
/* 143 */     int i = subscript(paramInt);
/* 144 */     int j = position(paramInt);
/*     */ 
/* 146 */     if (paramBoolean)
/*     */     {
/*     */       int tmp44_43 = i;
/*     */       byte[] tmp44_40 = this.repn; tmp44_40[tmp44_43] = ((byte)(tmp44_40[tmp44_43] | j));
/*     */     }
/*     */     else
/*     */     {
/*     */       int tmp59_58 = i;
/*     */       byte[] tmp59_55 = this.repn; tmp59_55[tmp59_58] = ((byte)(tmp59_55[tmp59_58] & (j ^ 0xFFFFFFFF)));
/*     */     }
/*     */   }
/*     */ 
/*     */   public int length()
/*     */   {
/* 157 */     return this.length;
/*     */   }
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/* 170 */     return (byte[])this.repn.clone();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 174 */     if (paramObject == this) return true;
/* 175 */     if ((paramObject == null) || (!(paramObject instanceof BitArray))) return false;
/*     */ 
/* 177 */     BitArray localBitArray = (BitArray)paramObject;
/*     */ 
/* 179 */     if (localBitArray.length != this.length) return false;
/*     */ 
/* 181 */     for (int i = 0; i < this.repn.length; i++) {
/* 182 */       if (this.repn[i] != localBitArray.repn[i]) return false;
/*     */     }
/* 184 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean[] toBooleanArray()
/*     */   {
/* 191 */     boolean[] arrayOfBoolean = new boolean[this.length];
/*     */ 
/* 193 */     for (int i = 0; i < this.length; i++) {
/* 194 */       arrayOfBoolean[i] = get(i);
/*     */     }
/* 196 */     return arrayOfBoolean;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 205 */     int i = 0;
/*     */ 
/* 207 */     for (int j = 0; j < this.repn.length; j++) {
/* 208 */       i = 31 * i + this.repn[j];
/*     */     }
/* 210 */     return i ^ this.length;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 215 */     return new BitArray(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 244 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/* 246 */     for (int i = 0; i < this.repn.length - 1; i++) {
/* 247 */       localByteArrayOutputStream.write(NYBBLE[(this.repn[i] >> 4 & 0xF)], 0, 4);
/* 248 */       localByteArrayOutputStream.write(NYBBLE[(this.repn[i] & 0xF)], 0, 4);
/*     */ 
/* 250 */       if (i % 8 == 7)
/* 251 */         localByteArrayOutputStream.write(10);
/*     */       else {
/* 253 */         localByteArrayOutputStream.write(32);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 258 */     for (i = 8 * (this.repn.length - 1); i < this.length; i++) {
/* 259 */       localByteArrayOutputStream.write(get(i) ? 49 : 48);
/*     */     }
/*     */ 
/* 262 */     return new String(localByteArrayOutputStream.toByteArray());
/*     */   }
/*     */ 
/*     */   public BitArray truncate()
/*     */   {
/* 267 */     for (int i = this.length - 1; i >= 0; i--) {
/* 268 */       if (get(i)) {
/* 269 */         return new BitArray(i + 1, Arrays.copyOf(this.repn, (i + 8) / 8));
/*     */       }
/*     */     }
/* 272 */     return new BitArray(1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.BitArray
 * JD-Core Version:    0.6.2
 */