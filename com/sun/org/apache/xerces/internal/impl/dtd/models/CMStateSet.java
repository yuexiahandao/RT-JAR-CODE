/*     */ package com.sun.org.apache.xerces.internal.impl.dtd.models;
/*     */ 
/*     */ public class CMStateSet
/*     */ {
/*     */   int fBitCount;
/*     */   int fByteCount;
/*     */   int fBits1;
/*     */   int fBits2;
/*     */   byte[] fByteArray;
/*     */ 
/*     */   public CMStateSet(int bitCount)
/*     */   {
/*  92 */     this.fBitCount = bitCount;
/*  93 */     if (this.fBitCount < 0) {
/*  94 */       throw new RuntimeException("ImplementationMessages.VAL_CMSI");
/*     */     }
/*     */ 
/* 100 */     if (this.fBitCount > 64)
/*     */     {
/* 102 */       this.fByteCount = (this.fBitCount / 8);
/* 103 */       if (this.fBitCount % 8 != 0)
/* 104 */         this.fByteCount += 1;
/* 105 */       this.fByteArray = new byte[this.fByteCount];
/*     */     }
/*     */ 
/* 109 */     zeroBits();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     StringBuffer strRet = new StringBuffer();
/*     */     try
/*     */     {
/* 121 */       strRet.append("{");
/* 122 */       for (int index = 0; index < this.fBitCount; index++)
/*     */       {
/* 124 */         if (getBit(index))
/* 125 */           strRet.append(" " + index);
/*     */       }
/* 127 */       strRet.append(" }");
/*     */     }
/*     */     catch (RuntimeException exToCatch)
/*     */     {
/*     */     }
/*     */ 
/* 137 */     return strRet.toString();
/*     */   }
/*     */ 
/*     */   public final void intersection(CMStateSet setToAnd)
/*     */   {
/* 147 */     if (this.fBitCount < 65)
/*     */     {
/* 149 */       this.fBits1 &= setToAnd.fBits1;
/* 150 */       this.fBits2 &= setToAnd.fBits2;
/*     */     }
/*     */     else
/*     */     {
/* 154 */       for (int index = this.fByteCount - 1; index >= 0; index--)
/*     */       {
/*     */         int tmp54_53 = index;
/*     */         byte[] tmp54_50 = this.fByteArray; tmp54_50[tmp54_53] = ((byte)(tmp54_50[tmp54_53] & setToAnd.fByteArray[index]));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean getBit(int bitToGet) {
/* 161 */     if (bitToGet >= this.fBitCount) {
/* 162 */       throw new RuntimeException("ImplementationMessages.VAL_CMSI");
/*     */     }
/* 164 */     if (this.fBitCount < 65)
/*     */     {
/* 166 */       int mask = 1 << bitToGet % 32;
/* 167 */       if (bitToGet < 32) {
/* 168 */         return (this.fBits1 & mask) != 0;
/*     */       }
/* 170 */       return (this.fBits2 & mask) != 0;
/*     */     }
/*     */ 
/* 175 */     byte mask = (byte)(1 << bitToGet % 8);
/* 176 */     int ofs = bitToGet >> 3;
/*     */ 
/* 179 */     return (this.fByteArray[ofs] & mask) != 0;
/*     */   }
/*     */ 
/*     */   public final boolean isEmpty()
/*     */   {
/* 185 */     if (this.fBitCount < 65)
/*     */     {
/* 187 */       return (this.fBits1 == 0) && (this.fBits2 == 0);
/*     */     }
/*     */ 
/* 191 */     for (int index = this.fByteCount - 1; index >= 0; index--)
/*     */     {
/* 193 */       if (this.fByteArray[index] != 0) {
/* 194 */         return false;
/*     */       }
/*     */     }
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */   final boolean isSameSet(CMStateSet setToCompare)
/*     */   {
/* 202 */     if (this.fBitCount != setToCompare.fBitCount) {
/* 203 */       return false;
/*     */     }
/* 205 */     if (this.fBitCount < 65)
/*     */     {
/* 207 */       return (this.fBits1 == setToCompare.fBits1) && (this.fBits2 == setToCompare.fBits2);
/*     */     }
/*     */ 
/* 211 */     for (int index = this.fByteCount - 1; index >= 0; index--)
/*     */     {
/* 213 */       if (this.fByteArray[index] != setToCompare.fByteArray[index])
/* 214 */         return false;
/*     */     }
/* 216 */     return true;
/*     */   }
/*     */ 
/*     */   public final void union(CMStateSet setToOr)
/*     */   {
/* 222 */     if (this.fBitCount < 65)
/*     */     {
/* 224 */       this.fBits1 |= setToOr.fBits1;
/* 225 */       this.fBits2 |= setToOr.fBits2;
/*     */     }
/*     */     else
/*     */     {
/* 229 */       for (int index = this.fByteCount - 1; index >= 0; index--)
/*     */       {
/*     */         int tmp54_53 = index;
/*     */         byte[] tmp54_50 = this.fByteArray; tmp54_50[tmp54_53] = ((byte)(tmp54_50[tmp54_53] | setToOr.fByteArray[index]));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void setBit(int bitToSet) {
/* 236 */     if (bitToSet >= this.fBitCount) {
/* 237 */       throw new RuntimeException("ImplementationMessages.VAL_CMSI");
/*     */     }
/* 239 */     if (this.fBitCount < 65)
/*     */     {
/* 241 */       int mask = 1 << bitToSet % 32;
/* 242 */       if (bitToSet < 32)
/*     */       {
/* 244 */         this.fBits1 &= (mask ^ 0xFFFFFFFF);
/* 245 */         this.fBits1 |= mask;
/*     */       }
/*     */       else
/*     */       {
/* 249 */         this.fBits2 &= (mask ^ 0xFFFFFFFF);
/* 250 */         this.fBits2 |= mask;
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 256 */       byte mask = (byte)(1 << bitToSet % 8);
/* 257 */       int ofs = bitToSet >> 3;
/*     */       int tmp107_106 = ofs;
/*     */       byte[] tmp107_103 = this.fByteArray; tmp107_103[tmp107_106] = ((byte)(tmp107_103[tmp107_106] & (mask ^ 0xFFFFFFFF)));
/*     */       int tmp120_119 = ofs;
/*     */       byte[] tmp120_116 = this.fByteArray; tmp120_116[tmp120_119] = ((byte)(tmp120_116[tmp120_119] | mask));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void setTo(CMStateSet srcSet)
/*     */   {
/* 269 */     if (this.fBitCount != srcSet.fBitCount) {
/* 270 */       throw new RuntimeException("ImplementationMessages.VAL_CMSI");
/*     */     }
/* 272 */     if (this.fBitCount < 65)
/*     */     {
/* 274 */       this.fBits1 = srcSet.fBits1;
/* 275 */       this.fBits2 = srcSet.fBits2;
/*     */     }
/*     */     else
/*     */     {
/* 279 */       for (int index = this.fByteCount - 1; index >= 0; index--)
/* 280 */         this.fByteArray[index] = srcSet.fByteArray[index];
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void zeroBits()
/*     */   {
/* 288 */     if (this.fBitCount < 65)
/*     */     {
/* 290 */       this.fBits1 = 0;
/* 291 */       this.fBits2 = 0;
/*     */     }
/*     */     else
/*     */     {
/* 295 */       for (int index = this.fByteCount - 1; index >= 0; index--)
/* 296 */         this.fByteArray[index] = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 325 */     if (!(o instanceof CMStateSet)) return false;
/* 326 */     return isSameSet((CMStateSet)o);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 330 */     if (this.fBitCount < 65)
/*     */     {
/* 332 */       return this.fBits1 + this.fBits2 * 31;
/*     */     }
/*     */ 
/* 336 */     int hash = 0;
/* 337 */     for (int index = this.fByteCount - 1; index >= 0; index--)
/* 338 */       hash = this.fByteArray[index] + hash * 31;
/* 339 */     return hash;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dtd.models.CMStateSet
 * JD-Core Version:    0.6.2
 */