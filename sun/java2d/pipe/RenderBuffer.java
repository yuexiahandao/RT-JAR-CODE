/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class RenderBuffer
/*     */ {
/*     */   protected static final long SIZEOF_BYTE = 1L;
/*     */   protected static final long SIZEOF_SHORT = 2L;
/*     */   protected static final long SIZEOF_INT = 4L;
/*     */   protected static final long SIZEOF_FLOAT = 4L;
/*     */   protected static final long SIZEOF_LONG = 8L;
/*     */   protected static final long SIZEOF_DOUBLE = 8L;
/*     */   private static final int COPY_FROM_ARRAY_THRESHOLD = 6;
/*     */   protected final Unsafe unsafe;
/*     */   protected final long baseAddress;
/*     */   protected final long endAddress;
/*     */   protected long curAddress;
/*     */   protected final int capacity;
/*     */ 
/*     */   protected RenderBuffer(int paramInt)
/*     */   {
/*  75 */     this.unsafe = Unsafe.getUnsafe();
/*  76 */     this.curAddress = (this.baseAddress = this.unsafe.allocateMemory(paramInt));
/*  77 */     this.endAddress = (this.baseAddress + paramInt);
/*  78 */     this.capacity = paramInt;
/*     */   }
/*     */ 
/*     */   public static RenderBuffer allocate(int paramInt)
/*     */   {
/*  85 */     return new RenderBuffer(paramInt);
/*     */   }
/*     */ 
/*     */   public final long getAddress()
/*     */   {
/*  92 */     return this.baseAddress;
/*     */   }
/*     */ 
/*     */   public final int capacity()
/*     */   {
/* 101 */     return this.capacity;
/*     */   }
/*     */ 
/*     */   public final int remaining() {
/* 105 */     return (int)(this.endAddress - this.curAddress);
/*     */   }
/*     */ 
/*     */   public final int position() {
/* 109 */     return (int)(this.curAddress - this.baseAddress);
/*     */   }
/*     */ 
/*     */   public final void position(long paramLong) {
/* 113 */     this.curAddress = (this.baseAddress + paramLong);
/*     */   }
/*     */ 
/*     */   public final void clear() {
/* 117 */     this.curAddress = this.baseAddress;
/*     */   }
/*     */ 
/*     */   public final RenderBuffer skip(long paramLong) {
/* 121 */     this.curAddress += paramLong;
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   public final RenderBuffer putByte(byte paramByte)
/*     */   {
/* 130 */     this.unsafe.putByte(this.curAddress, paramByte);
/* 131 */     this.curAddress += 1L;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(byte[] paramArrayOfByte) {
/* 136 */     return put(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 140 */     if (paramInt2 > 6) {
/* 141 */       long l1 = paramInt1 * 1L + Unsafe.ARRAY_BYTE_BASE_OFFSET;
/* 142 */       long l2 = paramInt2 * 1L;
/* 143 */       this.unsafe.copyMemory(paramArrayOfByte, l1, null, this.curAddress, l2);
/* 144 */       position(position() + l2);
/*     */     } else {
/* 146 */       int i = paramInt1 + paramInt2;
/* 147 */       for (int j = paramInt1; j < i; j++) {
/* 148 */         putByte(paramArrayOfByte[j]);
/*     */       }
/*     */     }
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */   public final RenderBuffer putShort(short paramShort)
/*     */   {
/* 160 */     this.unsafe.putShort(this.curAddress, paramShort);
/* 161 */     this.curAddress += 2L;
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(short[] paramArrayOfShort) {
/* 166 */     return put(paramArrayOfShort, 0, paramArrayOfShort.length);
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/* 171 */     if (paramInt2 > 6) {
/* 172 */       long l1 = paramInt1 * 2L + Unsafe.ARRAY_SHORT_BASE_OFFSET;
/* 173 */       long l2 = paramInt2 * 2L;
/* 174 */       this.unsafe.copyMemory(paramArrayOfShort, l1, null, this.curAddress, l2);
/* 175 */       position(position() + l2);
/*     */     } else {
/* 177 */       int i = paramInt1 + paramInt2;
/* 178 */       for (int j = paramInt1; j < i; j++) {
/* 179 */         putShort(paramArrayOfShort[j]);
/*     */       }
/*     */     }
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   public final RenderBuffer putInt(int paramInt1, int paramInt2)
/*     */   {
/* 191 */     this.unsafe.putInt(this.baseAddress + paramInt1, paramInt2);
/* 192 */     return this;
/*     */   }
/*     */ 
/*     */   public final RenderBuffer putInt(int paramInt)
/*     */   {
/* 197 */     this.unsafe.putInt(this.curAddress, paramInt);
/* 198 */     this.curAddress += 4L;
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(int[] paramArrayOfInt) {
/* 203 */     return put(paramArrayOfInt, 0, paramArrayOfInt.length);
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 208 */     if (paramInt2 > 6) {
/* 209 */       long l1 = paramInt1 * 4L + Unsafe.ARRAY_INT_BASE_OFFSET;
/* 210 */       long l2 = paramInt2 * 4L;
/* 211 */       this.unsafe.copyMemory(paramArrayOfInt, l1, null, this.curAddress, l2);
/* 212 */       position(position() + l2);
/*     */     } else {
/* 214 */       int i = paramInt1 + paramInt2;
/* 215 */       for (int j = paramInt1; j < i; j++) {
/* 216 */         putInt(paramArrayOfInt[j]);
/*     */       }
/*     */     }
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */   public final RenderBuffer putFloat(float paramFloat)
/*     */   {
/* 228 */     this.unsafe.putFloat(this.curAddress, paramFloat);
/* 229 */     this.curAddress += 4L;
/* 230 */     return this;
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(float[] paramArrayOfFloat) {
/* 234 */     return put(paramArrayOfFloat, 0, paramArrayOfFloat.length);
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*     */   {
/* 239 */     if (paramInt2 > 6) {
/* 240 */       long l1 = paramInt1 * 4L + Unsafe.ARRAY_FLOAT_BASE_OFFSET;
/* 241 */       long l2 = paramInt2 * 4L;
/* 242 */       this.unsafe.copyMemory(paramArrayOfFloat, l1, null, this.curAddress, l2);
/* 243 */       position(position() + l2);
/*     */     } else {
/* 245 */       int i = paramInt1 + paramInt2;
/* 246 */       for (int j = paramInt1; j < i; j++) {
/* 247 */         putFloat(paramArrayOfFloat[j]);
/*     */       }
/*     */     }
/* 250 */     return this;
/*     */   }
/*     */ 
/*     */   public final RenderBuffer putLong(long paramLong)
/*     */   {
/* 259 */     this.unsafe.putLong(this.curAddress, paramLong);
/* 260 */     this.curAddress += 8L;
/* 261 */     return this;
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(long[] paramArrayOfLong) {
/* 265 */     return put(paramArrayOfLong, 0, paramArrayOfLong.length);
/*     */   }
/*     */ 
/*     */   public RenderBuffer put(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 270 */     if (paramInt2 > 6) {
/* 271 */       long l1 = paramInt1 * 8L + Unsafe.ARRAY_LONG_BASE_OFFSET;
/* 272 */       long l2 = paramInt2 * 8L;
/* 273 */       this.unsafe.copyMemory(paramArrayOfLong, l1, null, this.curAddress, l2);
/* 274 */       position(position() + l2);
/*     */     } else {
/* 276 */       int i = paramInt1 + paramInt2;
/* 277 */       for (int j = paramInt1; j < i; j++) {
/* 278 */         putLong(paramArrayOfLong[j]);
/*     */       }
/*     */     }
/* 281 */     return this;
/*     */   }
/*     */ 
/*     */   public final RenderBuffer putDouble(double paramDouble)
/*     */   {
/* 290 */     this.unsafe.putDouble(this.curAddress, paramDouble);
/* 291 */     this.curAddress += 8L;
/* 292 */     return this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.RenderBuffer
 * JD-Core Version:    0.6.2
 */