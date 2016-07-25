/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ class AdaptiveCoding
/*     */   implements CodingMethod
/*     */ {
/*     */   CodingMethod headCoding;
/*     */   int headLength;
/*     */   CodingMethod tailCoding;
/*     */   public static final int KX_MIN = 0;
/*     */   public static final int KX_MAX = 3;
/*     */   public static final int KX_LG2BASE = 4;
/*     */   public static final int KX_BASE = 16;
/*     */   public static final int KB_MIN = 0;
/*     */   public static final int KB_MAX = 255;
/*     */   public static final int KB_OFFSET = 1;
/*     */   public static final int KB_DEFAULT = 3;
/*     */ 
/*     */   public AdaptiveCoding(int paramInt, CodingMethod paramCodingMethod1, CodingMethod paramCodingMethod2)
/*     */   {
/*  45 */     assert (isCodableLength(paramInt));
/*  46 */     this.headLength = paramInt;
/*  47 */     this.headCoding = paramCodingMethod1;
/*  48 */     this.tailCoding = paramCodingMethod2;
/*     */   }
/*     */ 
/*     */   public void setHeadCoding(CodingMethod paramCodingMethod) {
/*  52 */     this.headCoding = paramCodingMethod;
/*     */   }
/*     */   public void setHeadLength(int paramInt) {
/*  55 */     assert (isCodableLength(paramInt));
/*  56 */     this.headLength = paramInt;
/*     */   }
/*     */   public void setTailCoding(CodingMethod paramCodingMethod) {
/*  59 */     this.tailCoding = paramCodingMethod;
/*     */   }
/*     */ 
/*     */   public boolean isTrivial() {
/*  63 */     return this.headCoding == this.tailCoding;
/*     */   }
/*     */ 
/*     */   public void writeArrayTo(OutputStream paramOutputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException
/*     */   {
/*  68 */     writeArray(this, paramOutputStream, paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private static void writeArray(AdaptiveCoding paramAdaptiveCoding, OutputStream paramOutputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException {
/*     */     while (true) {
/*  73 */       int i = paramInt1 + paramAdaptiveCoding.headLength;
/*  74 */       assert (i <= paramInt2);
/*  75 */       paramAdaptiveCoding.headCoding.writeArrayTo(paramOutputStream, paramArrayOfInt, paramInt1, i);
/*  76 */       paramInt1 = i;
/*  77 */       if (!(paramAdaptiveCoding.tailCoding instanceof AdaptiveCoding)) break;
/*  78 */       paramAdaptiveCoding = (AdaptiveCoding)paramAdaptiveCoding.tailCoding;
/*     */     }
/*     */ 
/*  83 */     paramAdaptiveCoding.tailCoding.writeArrayTo(paramOutputStream, paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void readArrayFrom(InputStream paramInputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException {
/*  87 */     readArray(this, paramInputStream, paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */   private static void readArray(AdaptiveCoding paramAdaptiveCoding, InputStream paramInputStream, int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException {
/*     */     while (true) {
/*  91 */       int i = paramInt1 + paramAdaptiveCoding.headLength;
/*  92 */       assert (i <= paramInt2);
/*  93 */       paramAdaptiveCoding.headCoding.readArrayFrom(paramInputStream, paramArrayOfInt, paramInt1, i);
/*  94 */       paramInt1 = i;
/*  95 */       if (!(paramAdaptiveCoding.tailCoding instanceof AdaptiveCoding)) break;
/*  96 */       paramAdaptiveCoding = (AdaptiveCoding)paramAdaptiveCoding.tailCoding;
/*     */     }
/*     */ 
/* 101 */     paramAdaptiveCoding.tailCoding.readArrayFrom(paramInputStream, paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   static int getKXOf(int paramInt)
/*     */   {
/* 115 */     for (int i = 0; i <= 3; i++) {
/* 116 */       if ((paramInt - 1 & 0xFFFFFF00) == 0)
/* 117 */         return i;
/* 118 */       paramInt >>>= 4;
/*     */     }
/* 120 */     return -1;
/*     */   }
/*     */ 
/*     */   static int getKBOf(int paramInt) {
/* 124 */     int i = getKXOf(paramInt);
/* 125 */     if (i < 0) return -1;
/* 126 */     paramInt >>>= i * 4;
/* 127 */     return paramInt - 1;
/*     */   }
/*     */ 
/*     */   static int decodeK(int paramInt1, int paramInt2) {
/* 131 */     assert ((0 <= paramInt1) && (paramInt1 <= 3));
/* 132 */     assert ((0 <= paramInt2) && (paramInt2 <= 255));
/* 133 */     return paramInt2 + 1 << paramInt1 * 4;
/*     */   }
/*     */ 
/*     */   static int getNextK(int paramInt) {
/* 137 */     if (paramInt <= 0) return 1;
/* 138 */     int i = getKXOf(paramInt);
/* 139 */     if (i < 0) return 2147483647;
/*     */ 
/* 141 */     int j = 1 << i * 4;
/* 142 */     int k = 255 << i * 4;
/* 143 */     int m = paramInt + j;
/* 144 */     m &= (j - 1 ^ 0xFFFFFFFF);
/* 145 */     if ((m - j & (k ^ 0xFFFFFFFF)) == 0) {
/* 146 */       assert (getKXOf(m) == i);
/* 147 */       return m;
/*     */     }
/* 149 */     if (i == 3) return 2147483647;
/* 150 */     i++;
/* 151 */     int n = 255 << i * 4;
/* 152 */     m |= k & (n ^ 0xFFFFFFFF);
/* 153 */     m += j;
/* 154 */     assert (getKXOf(m) == i);
/* 155 */     return m;
/*     */   }
/*     */ 
/*     */   public static boolean isCodableLength(int paramInt)
/*     */   {
/* 160 */     int i = getKXOf(paramInt);
/* 161 */     if (i < 0) return false;
/* 162 */     int j = 1 << i * 4;
/* 163 */     int k = 255 << i * 4;
/* 164 */     return (paramInt - j & (k ^ 0xFFFFFFFF)) == 0;
/*     */   }
/*     */ 
/*     */   public byte[] getMetaCoding(Coding paramCoding)
/*     */   {
/* 170 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(10);
/*     */     try {
/* 172 */       makeMetaCoding(this, paramCoding, localByteArrayOutputStream);
/*     */     } catch (IOException localIOException) {
/* 174 */       throw new RuntimeException(localIOException);
/*     */     }
/* 176 */     return localByteArrayOutputStream.toByteArray();
/*     */   }
/*     */   private static void makeMetaCoding(AdaptiveCoding paramAdaptiveCoding, Coding paramCoding, ByteArrayOutputStream paramByteArrayOutputStream) throws IOException {
/*     */     CodingMethod localCodingMethod2;
/*     */     int m;
/*     */     while (true) { CodingMethod localCodingMethod1 = paramAdaptiveCoding.headCoding;
/* 183 */       int i = paramAdaptiveCoding.headLength;
/* 184 */       localCodingMethod2 = paramAdaptiveCoding.tailCoding;
/* 185 */       int j = i;
/* 186 */       assert (isCodableLength(j));
/* 187 */       int k = localCodingMethod1 == paramCoding ? 1 : 0;
/* 188 */       m = localCodingMethod2 == paramCoding ? 1 : 0;
/* 189 */       if (k + m > 1) m = 0;
/* 190 */       int n = 1 * k + 2 * m;
/* 191 */       assert (n < 3);
/* 192 */       int i1 = getKXOf(j);
/* 193 */       int i2 = getKBOf(j);
/* 194 */       assert (decodeK(i1, i2) == j);
/* 195 */       int i3 = i2 != 3 ? 1 : 0;
/* 196 */       paramByteArrayOutputStream.write(117 + i1 + 4 * i3 + 8 * n);
/* 197 */       if (i3 != 0) paramByteArrayOutputStream.write(i2);
/* 198 */       if (k == 0) paramByteArrayOutputStream.write(localCodingMethod1.getMetaCoding(paramCoding));
/* 199 */       if (!(localCodingMethod2 instanceof AdaptiveCoding)) break;
/* 200 */       paramAdaptiveCoding = (AdaptiveCoding)localCodingMethod2;
/*     */     }
/*     */ 
/* 203 */     if (m == 0) paramByteArrayOutputStream.write(localCodingMethod2.getMetaCoding(paramCoding));
/*     */   }
/*     */ 
/*     */   public static int parseMetaCoding(byte[] paramArrayOfByte, int paramInt, Coding paramCoding, CodingMethod[] paramArrayOfCodingMethod)
/*     */   {
/* 208 */     int i = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 209 */     if ((i < 117) || (i >= 141)) return paramInt - 1;
/* 210 */     Object localObject = null;
/* 211 */     for (int j = 1; j != 0; ) {
/* 212 */       j = 0;
/* 213 */       assert (i >= 117);
/* 214 */       i -= 117;
/* 215 */       int k = i % 4;
/* 216 */       int m = i / 4 % 2;
/* 217 */       int n = i / 8;
/* 218 */       assert (n < 3);
/* 219 */       int i1 = n & 0x1;
/* 220 */       int i2 = n & 0x2;
/* 221 */       CodingMethod[] arrayOfCodingMethod1 = { paramCoding }; CodingMethod[] arrayOfCodingMethod2 = { paramCoding };
/* 222 */       int i3 = 3;
/* 223 */       if (m != 0)
/* 224 */         i3 = paramArrayOfByte[(paramInt++)] & 0xFF;
/* 225 */       if (i1 == 0) {
/* 226 */         paramInt = BandStructure.parseMetaCoding(paramArrayOfByte, paramInt, paramCoding, arrayOfCodingMethod1);
/*     */       }
/* 228 */       if ((i2 == 0) && ((i = paramArrayOfByte[paramInt] & 0xFF) >= 117) && (i < 141))
/*     */       {
/* 230 */         paramInt++;
/* 231 */         j = 1;
/* 232 */       } else if (i2 == 0) {
/* 233 */         paramInt = BandStructure.parseMetaCoding(paramArrayOfByte, paramInt, paramCoding, arrayOfCodingMethod2);
/*     */       }
/* 235 */       AdaptiveCoding localAdaptiveCoding = new AdaptiveCoding(decodeK(k, i3), arrayOfCodingMethod1[0], arrayOfCodingMethod2[0]);
/*     */ 
/* 237 */       if (localObject == null)
/* 238 */         paramArrayOfCodingMethod[0] = localAdaptiveCoding;
/*     */       else {
/* 240 */         localObject.tailCoding = localAdaptiveCoding;
/*     */       }
/* 242 */       localObject = localAdaptiveCoding;
/*     */     }
/* 244 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private String keyString(CodingMethod paramCodingMethod) {
/* 248 */     if ((paramCodingMethod instanceof Coding))
/* 249 */       return ((Coding)paramCodingMethod).keyString();
/* 250 */     return paramCodingMethod.toString();
/*     */   }
/*     */   public String toString() {
/* 253 */     StringBuilder localStringBuilder = new StringBuilder(20);
/* 254 */     AdaptiveCoding localAdaptiveCoding = this;
/* 255 */     localStringBuilder.append("run(");
/*     */     while (true) {
/* 257 */       localStringBuilder.append(localAdaptiveCoding.headLength).append("*");
/* 258 */       localStringBuilder.append(keyString(localAdaptiveCoding.headCoding));
/* 259 */       if (!(localAdaptiveCoding.tailCoding instanceof AdaptiveCoding)) break;
/* 260 */       localAdaptiveCoding = (AdaptiveCoding)localAdaptiveCoding.tailCoding;
/* 261 */       localStringBuilder.append(" ");
/*     */     }
/*     */ 
/* 266 */     localStringBuilder.append(" **").append(keyString(localAdaptiveCoding.tailCoding));
/* 267 */     localStringBuilder.append(")");
/* 268 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.AdaptiveCoding
 * JD-Core Version:    0.6.2
 */