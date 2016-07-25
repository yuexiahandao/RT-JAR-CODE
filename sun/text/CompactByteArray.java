/*     */ package sun.text;
/*     */ 
/*     */ public final class CompactByteArray
/*     */   implements Cloneable
/*     */ {
/*     */   public static final int UNICODECOUNT = 65536;
/*     */   private static final int BLOCKSHIFT = 7;
/*     */   private static final int BLOCKCOUNT = 128;
/*     */   private static final int INDEXSHIFT = 9;
/*     */   private static final int INDEXCOUNT = 512;
/*     */   private static final int BLOCKMASK = 127;
/*     */   private byte[] values;
/*     */   private short[] indices;
/*     */   private boolean isCompact;
/*     */   private int[] hashes;
/*     */ 
/*     */   public CompactByteArray(byte paramByte)
/*     */   {
/*  77 */     this.values = new byte[65536];
/*  78 */     this.indices = new short[512];
/*  79 */     this.hashes = new int[512];
/*  80 */     for (int i = 0; i < 65536; i++) {
/*  81 */       this.values[i] = paramByte;
/*     */     }
/*  83 */     for (i = 0; i < 512; i++) {
/*  84 */       this.indices[i] = ((short)(i << 7));
/*  85 */       this.hashes[i] = 0;
/*     */     }
/*  87 */     this.isCompact = false;
/*     */   }
/*     */ 
/*     */   public CompactByteArray(short[] paramArrayOfShort, byte[] paramArrayOfByte)
/*     */   {
/* 100 */     if (paramArrayOfShort.length != 512)
/* 101 */       throw new IllegalArgumentException("Index out of bounds!");
/* 102 */     for (int i = 0; i < 512; i++) {
/* 103 */       int j = paramArrayOfShort[i];
/* 104 */       if ((j < 0) || (j >= paramArrayOfByte.length + 128))
/* 105 */         throw new IllegalArgumentException("Index out of bounds!");
/*     */     }
/* 107 */     this.indices = paramArrayOfShort;
/* 108 */     this.values = paramArrayOfByte;
/* 109 */     this.isCompact = true;
/*     */   }
/*     */ 
/*     */   public byte elementAt(char paramChar)
/*     */   {
/* 119 */     return this.values[((this.indices[(paramChar >> '\007')] & 0xFFFF) + (paramChar & 0x7F))];
/*     */   }
/*     */ 
/*     */   public void setElementAt(char paramChar, byte paramByte)
/*     */   {
/* 130 */     if (this.isCompact)
/* 131 */       expand();
/* 132 */     this.values[paramChar] = paramByte;
/* 133 */     touchBlock(paramChar >> '\007', paramByte);
/*     */   }
/*     */ 
/*     */   public void setElementAt(char paramChar1, char paramChar2, byte paramByte)
/*     */   {
/* 145 */     if (this.isCompact) {
/* 146 */       expand();
/*     */     }
/* 148 */     for (char c = paramChar1; c <= paramChar2; c++) {
/* 149 */       this.values[c] = paramByte;
/* 150 */       touchBlock(c >> '\007', paramByte);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void compact()
/*     */   {
/* 159 */     if (!this.isCompact) {
/* 160 */       int i = 0;
/* 161 */       int j = 0;
/* 162 */       int k = -1;
/*     */ 
/* 164 */       for (int m = 0; m < this.indices.length; j += 128) {
/* 165 */         this.indices[m] = -1;
/* 166 */         boolean bool = blockTouched(m);
/* 167 */         if ((!bool) && (k != -1))
/*     */         {
/* 171 */           this.indices[m] = k;
/*     */         } else {
/* 173 */           int n = 0;
/* 174 */           int i1 = 0;
/* 175 */           for (i1 = 0; i1 < i; 
/* 176 */             n += 128) {
/* 177 */             if ((this.hashes[m] == this.hashes[i1]) && (arrayRegionMatches(this.values, j, this.values, n, 128)))
/*     */             {
/* 180 */               this.indices[m] = ((short)n);
/* 181 */               break;
/*     */             }
/* 176 */             i1++;
/*     */           }
/*     */ 
/* 184 */           if (this.indices[m] == -1)
/*     */           {
/* 186 */             System.arraycopy(this.values, j, this.values, n, 128);
/*     */ 
/* 188 */             this.indices[m] = ((short)n);
/* 189 */             this.hashes[i1] = this.hashes[m];
/* 190 */             i++;
/*     */ 
/* 192 */             if (!bool)
/*     */             {
/* 195 */               k = (short)n;
/*     */             }
/*     */           }
/*     */         }
/* 164 */         m++;
/*     */       }
/*     */ 
/* 201 */       m = i * 128;
/* 202 */       byte[] arrayOfByte = new byte[m];
/* 203 */       System.arraycopy(this.values, 0, arrayOfByte, 0, m);
/* 204 */       this.values = arrayOfByte;
/* 205 */       this.isCompact = true;
/* 206 */       this.hashes = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final boolean arrayRegionMatches(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
/*     */   {
/* 219 */     int i = paramInt1 + paramInt3;
/* 220 */     int j = paramInt2 - paramInt1;
/* 221 */     for (int k = paramInt1; k < i; k++) {
/* 222 */       if (paramArrayOfByte1[k] != paramArrayOfByte2[(k + j)])
/* 223 */         return false;
/*     */     }
/* 225 */     return true;
/*     */   }
/*     */ 
/*     */   private final void touchBlock(int paramInt1, int paramInt2)
/*     */   {
/* 233 */     this.hashes[paramInt1] = (this.hashes[paramInt1] + (paramInt2 << 1) | 0x1);
/*     */   }
/*     */ 
/*     */   private final boolean blockTouched(int paramInt)
/*     */   {
/* 241 */     return this.hashes[paramInt] != 0;
/*     */   }
/*     */ 
/*     */   public short[] getIndexArray()
/*     */   {
/* 249 */     return this.indices;
/*     */   }
/*     */ 
/*     */   public byte[] getStringArray()
/*     */   {
/* 257 */     return this.values;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 266 */       CompactByteArray localCompactByteArray = (CompactByteArray)super.clone();
/* 267 */       localCompactByteArray.values = ((byte[])this.values.clone());
/* 268 */       localCompactByteArray.indices = ((short[])this.indices.clone());
/* 269 */       if (this.hashes != null) localCompactByteArray.hashes = ((int[])this.hashes.clone());
/* 270 */       return localCompactByteArray; } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 272 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 283 */     if (paramObject == null) return false;
/* 284 */     if (this == paramObject)
/* 285 */       return true;
/* 286 */     if (getClass() != paramObject.getClass())
/* 287 */       return false;
/* 288 */     CompactByteArray localCompactByteArray = (CompactByteArray)paramObject;
/* 289 */     for (int i = 0; i < 65536; i++)
/*     */     {
/* 291 */       if (elementAt((char)i) != localCompactByteArray.elementAt((char)i))
/* 292 */         return false;
/*     */     }
/* 294 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 302 */     int i = 0;
/* 303 */     int j = Math.min(3, this.values.length / 16);
/* 304 */     for (int k = 0; k < this.values.length; k += j) {
/* 305 */       i = i * 37 + this.values[k];
/*     */     }
/* 307 */     return i;
/*     */   }
/*     */ 
/*     */   private void expand()
/*     */   {
/* 319 */     if (this.isCompact)
/*     */     {
/* 321 */       this.hashes = new int[512];
/* 322 */       byte[] arrayOfByte = new byte[65536];
/* 323 */       for (int i = 0; i < 65536; i++) {
/* 324 */         int j = elementAt((char)i);
/* 325 */         arrayOfByte[i] = j;
/* 326 */         touchBlock(i >> 7, j);
/*     */       }
/* 328 */       for (i = 0; i < 512; i++) {
/* 329 */         this.indices[i] = ((short)(i << 7));
/*     */       }
/* 331 */       this.values = null;
/* 332 */       this.values = arrayOfByte;
/* 333 */       this.isCompact = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] getArray()
/*     */   {
/* 339 */     return this.values;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.CompactByteArray
 * JD-Core Version:    0.6.2
 */