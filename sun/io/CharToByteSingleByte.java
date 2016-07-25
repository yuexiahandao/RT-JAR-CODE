/*     */ package sun.io;
/*     */ 
/*     */ public abstract class CharToByteSingleByte extends CharToByteConverter
/*     */ {
/*     */   protected char[] index1;
/*     */   protected char[] index2;
/*     */   protected int mask1;
/*     */   protected int mask2;
/*     */   protected int shift;
/*     */   private char highHalfZoneCode;
/*     */ 
/*     */   public char[] getIndex1()
/*     */   {
/*  73 */     return this.index1;
/*     */   }
/*     */ 
/*     */   public char[] getIndex2() {
/*  77 */     return this.index2;
/*     */   }
/*     */ 
/*     */   public int flush(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws MalformedInputException
/*     */   {
/*  82 */     if (this.highHalfZoneCode != 0) {
/*  83 */       this.highHalfZoneCode = '\000';
/*  84 */       this.badInputLength = 0;
/*  85 */       throw new MalformedInputException();
/*     */     }
/*  87 */     this.byteOff = (this.charOff = 0);
/*  88 */     return 0;
/*     */   }
/*     */ 
/*     */   public int convert(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */     throws MalformedInputException, UnknownCharacterException, ConversionBufferFullException
/*     */   {
/* 120 */     byte[] arrayOfByte2 = new byte[1];
/*     */ 
/* 123 */     this.charOff = paramInt1;
/* 124 */     this.byteOff = paramInt3;
/*     */     int i;
/* 126 */     if (this.highHalfZoneCode != 0) {
/* 127 */       i = this.highHalfZoneCode;
/* 128 */       this.highHalfZoneCode = '\000';
/* 129 */       if ((paramArrayOfChar[paramInt1] >= 56320) && (paramArrayOfChar[paramInt1] <= 57343))
/*     */       {
/* 131 */         this.badInputLength = 1;
/* 132 */         throw new UnknownCharacterException();
/*     */       }
/*     */ 
/* 135 */       this.badInputLength = 0;
/* 136 */       throw new MalformedInputException();
/*     */     }
/*     */ 
/* 141 */     while (this.charOff < paramInt2)
/*     */     {
/* 143 */       byte[] arrayOfByte1 = arrayOfByte2;
/*     */ 
/* 146 */       i = paramArrayOfChar[this.charOff];
/*     */ 
/* 149 */       int k = 1;
/*     */ 
/* 152 */       int j = 1;
/*     */ 
/* 155 */       if ((i >= 55296) && (i <= 56319))
/*     */       {
/* 157 */         if (this.charOff + 1 >= paramInt2) {
/* 158 */           this.highHalfZoneCode = i;
/* 159 */           break;
/*     */         }
/*     */ 
/* 163 */         i = paramArrayOfChar[(this.charOff + 1)];
/* 164 */         if ((i >= 56320) && (i <= 57343))
/*     */         {
/* 167 */           if (this.subMode) {
/* 168 */             arrayOfByte1 = this.subBytes;
/* 169 */             k = this.subBytes.length;
/* 170 */             j = 2;
/*     */           } else {
/* 172 */             this.badInputLength = 2;
/* 173 */             throw new UnknownCharacterException();
/*     */           }
/*     */         }
/*     */         else {
/* 177 */           this.badInputLength = 1;
/* 178 */           throw new MalformedInputException();
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 183 */         if ((i >= 56320) && (i <= 57343)) {
/* 184 */           this.badInputLength = 1;
/* 185 */           throw new MalformedInputException();
/*     */         }
/*     */ 
/* 191 */         arrayOfByte1[0] = getNative(i);
/*     */ 
/* 194 */         if (arrayOfByte1[0] == 0)
/*     */         {
/* 197 */           if (paramArrayOfChar[this.charOff] != 0)
/*     */           {
/* 200 */             if (this.subMode) {
/* 201 */               arrayOfByte1 = this.subBytes;
/* 202 */               k = this.subBytes.length;
/*     */             } else {
/* 204 */               this.badInputLength = 1;
/* 205 */               throw new UnknownCharacterException();
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 212 */       if (this.byteOff + k > paramInt4) {
/* 213 */         throw new ConversionBufferFullException();
/*     */       }
/*     */ 
/* 216 */       for (int m = 0; m < k; m++) {
/* 217 */         paramArrayOfByte[(this.byteOff++)] = arrayOfByte1[m];
/*     */       }
/* 219 */       this.charOff += j;
/*     */     }
/*     */ 
/* 224 */     return this.byteOff - paramInt3;
/*     */   }
/*     */ 
/*     */   public int getMaxBytesPerChar()
/*     */   {
/* 232 */     return 1;
/*     */   }
/*     */ 
/*     */   int encodeChar(char paramChar) {
/* 236 */     int i = this.index1[(paramChar >> '\b')];
/* 237 */     if (i == 65533)
/* 238 */       return 65533;
/* 239 */     return this.index2[(i + (paramChar & 0xFF))];
/*     */   }
/*     */ 
/*     */   public byte getNative(char paramChar) {
/* 243 */     int i = encodeChar(paramChar);
/* 244 */     if (i == 65533)
/* 245 */       return 0;
/* 246 */     return (byte)i;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 254 */     this.byteOff = (this.charOff = 0);
/* 255 */     this.highHalfZoneCode = '\000';
/*     */   }
/*     */ 
/*     */   public boolean canConvert(char paramChar)
/*     */   {
/* 263 */     return encodeChar(paramChar) != 65533;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteSingleByte
 * JD-Core Version:    0.6.2
 */