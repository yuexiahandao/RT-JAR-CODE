/*     */ package sun.io;
/*     */ 
/*     */ public class CharToByteISO8859_1 extends CharToByteConverter
/*     */ {
/*     */   private char highHalfZoneCode;
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/*  32 */     return "ISO8859_1";
/*     */   }
/*     */ 
/*     */   public int flush(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws MalformedInputException
/*     */   {
/*  40 */     if (this.highHalfZoneCode != 0) {
/*  41 */       this.highHalfZoneCode = '\000';
/*  42 */       throw new MalformedInputException("String ends with <High Half Zone code> of UTF16");
/*     */     }
/*     */ 
/*  45 */     this.byteOff = (this.charOff = 0);
/*  46 */     return 0;
/*     */   }
/*     */ 
/*     */   public int convert(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */     throws MalformedInputException, UnknownCharacterException, ConversionBufferFullException
/*     */   {
/*  62 */     byte[] arrayOfByte2 = new byte[1];
/*     */ 
/*  67 */     this.charOff = paramInt1;
/*  68 */     this.byteOff = paramInt3;
/*     */     int i;
/*     */     int k;
/*     */     int m;
/*  70 */     if (this.highHalfZoneCode != 0) {
/*  71 */       i = this.highHalfZoneCode;
/*  72 */       this.highHalfZoneCode = '\000';
/*  73 */       if ((paramArrayOfChar[paramInt1] >= 56320) && (paramArrayOfChar[paramInt1] <= 57343))
/*     */       {
/*  75 */         if (this.subMode) {
/*  76 */           k = this.subBytes.length;
/*  77 */           if (this.byteOff + k > paramInt4)
/*  78 */             throw new ConversionBufferFullException();
/*  79 */           for (m = 0; m < k; m++)
/*  80 */             paramArrayOfByte[(this.byteOff++)] = this.subBytes[m];
/*  81 */           this.charOff += 1;
/*     */         } else {
/*  83 */           this.badInputLength = 1;
/*  84 */           throw new UnknownCharacterException();
/*     */         }
/*     */       }
/*     */       else {
/*  88 */         this.badInputLength = 0;
/*  89 */         throw new MalformedInputException("Previous converted string ends with <High Half Zone Code> of UTF16 , but this string is not begin with <Low Half Zone>");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  97 */     while (this.charOff < paramInt2) {
/*  98 */       byte[] arrayOfByte1 = arrayOfByte2;
/*     */ 
/* 101 */       i = paramArrayOfChar[this.charOff];
/*     */ 
/* 104 */       k = 1;
/*     */ 
/* 107 */       int j = 1;
/*     */ 
/* 110 */       if ((i >= 55296) && (i <= 56319))
/*     */       {
/* 112 */         if (this.charOff + 1 == paramInt2) {
/* 113 */           this.highHalfZoneCode = i;
/* 114 */           break;
/*     */         }
/*     */ 
/* 118 */         i = paramArrayOfChar[(this.charOff + 1)];
/* 119 */         if ((i >= 56320) && (i <= 57343))
/*     */         {
/* 122 */           if (this.subMode) {
/* 123 */             arrayOfByte1 = this.subBytes;
/* 124 */             k = this.subBytes.length;
/* 125 */             j = 2;
/*     */           } else {
/* 127 */             this.badInputLength = 2;
/* 128 */             throw new UnknownCharacterException();
/*     */           }
/*     */         }
/*     */         else {
/* 132 */           this.badInputLength = 1;
/* 133 */           throw new MalformedInputException();
/*     */         }
/*     */       }
/*     */       else {
/* 137 */         if ((i >= 56320) && (i <= 57343)) {
/* 138 */           this.badInputLength = 1;
/* 139 */           throw new MalformedInputException();
/*     */         }
/*     */ 
/* 144 */         if (i <= 255) {
/* 145 */           arrayOfByte1[0] = ((byte)i);
/*     */         }
/* 148 */         else if (this.subMode) {
/* 149 */           arrayOfByte1 = this.subBytes;
/* 150 */           k = this.subBytes.length;
/*     */         } else {
/* 152 */           this.badInputLength = 1;
/* 153 */           throw new UnknownCharacterException();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 159 */       if (this.byteOff + k > paramInt4) {
/* 160 */         throw new ConversionBufferFullException();
/*     */       }
/*     */ 
/* 163 */       for (m = 0; m < k; m++) {
/* 164 */         paramArrayOfByte[(this.byteOff++)] = arrayOfByte1[m];
/*     */       }
/* 166 */       this.charOff += j;
/*     */     }
/*     */ 
/* 170 */     return this.byteOff - paramInt3;
/*     */   }
/*     */ 
/*     */   public boolean canConvert(char paramChar)
/*     */   {
/* 176 */     return paramChar <= 'ÿ';
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 182 */     this.byteOff = (this.charOff = 0);
/* 183 */     this.highHalfZoneCode = '\000';
/*     */   }
/*     */ 
/*     */   public int getMaxBytesPerChar()
/*     */   {
/* 191 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteISO8859_1
 * JD-Core Version:    0.6.2
 */