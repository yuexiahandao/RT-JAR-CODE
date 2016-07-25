/*     */ package sun.io;
/*     */ 
/*     */ public class CharToByteASCII extends CharToByteConverter
/*     */ {
/*     */   private char highHalfZoneCode;
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/*  33 */     return "ASCII";
/*     */   }
/*     */ 
/*     */   public int flush(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws MalformedInputException
/*     */   {
/*  41 */     if (this.highHalfZoneCode != 0) {
/*  42 */       this.highHalfZoneCode = '\000';
/*  43 */       throw new MalformedInputException("String ends with <High Half Zone code> of UTF16");
/*     */     }
/*     */ 
/*  46 */     this.byteOff = (this.charOff = 0);
/*  47 */     return 0;
/*     */   }
/*     */ 
/*     */   public int convert(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */     throws MalformedInputException, UnknownCharacterException, ConversionBufferFullException
/*     */   {
/*  61 */     byte[] arrayOfByte2 = new byte[1];
/*     */ 
/*  66 */     this.charOff = paramInt1;
/*  67 */     this.byteOff = paramInt3;
/*     */     int i;
/*  69 */     if (this.highHalfZoneCode != 0) {
/*  70 */       i = this.highHalfZoneCode;
/*  71 */       this.highHalfZoneCode = '\000';
/*  72 */       if ((paramArrayOfChar[paramInt1] >= 56320) && (paramArrayOfChar[paramInt1] <= 57343))
/*     */       {
/*  74 */         this.badInputLength = 1;
/*  75 */         throw new UnknownCharacterException();
/*     */       }
/*     */ 
/*  78 */       this.badInputLength = 0;
/*  79 */       throw new MalformedInputException("Previous converted string ends with <High Half Zone Code> of UTF16 , but this string is not begin with <Low Half Zone>");
/*     */     }
/*     */ 
/*  87 */     while (this.charOff < paramInt2) {
/*  88 */       byte[] arrayOfByte1 = arrayOfByte2;
/*     */ 
/*  91 */       i = paramArrayOfChar[this.charOff];
/*     */ 
/*  94 */       int k = 1;
/*     */ 
/*  97 */       int j = 1;
/*     */ 
/* 100 */       if ((i >= 55296) && (i <= 56319))
/*     */       {
/* 102 */         if (this.charOff + 1 == paramInt2) {
/* 103 */           this.highHalfZoneCode = i;
/* 104 */           break;
/*     */         }
/*     */ 
/* 108 */         i = paramArrayOfChar[(this.charOff + 1)];
/* 109 */         if ((i >= 56320) && (i <= 57343))
/*     */         {
/* 112 */           if (this.subMode) {
/* 113 */             arrayOfByte1 = this.subBytes;
/* 114 */             k = this.subBytes.length;
/* 115 */             j = 2;
/*     */           } else {
/* 117 */             this.badInputLength = 2;
/* 118 */             throw new UnknownCharacterException();
/*     */           }
/*     */         }
/*     */         else {
/* 122 */           this.badInputLength = 1;
/* 123 */           throw new MalformedInputException();
/*     */         }
/*     */       }
/*     */       else {
/* 127 */         if ((i >= 56320) && (i <= 57343)) {
/* 128 */           this.badInputLength = 1;
/* 129 */           throw new MalformedInputException();
/*     */         }
/*     */ 
/* 134 */         if (i <= 127) {
/* 135 */           arrayOfByte1[0] = ((byte)i);
/*     */         }
/* 138 */         else if (this.subMode) {
/* 139 */           arrayOfByte1 = this.subBytes;
/* 140 */           k = this.subBytes.length;
/*     */         } else {
/* 142 */           this.badInputLength = 1;
/* 143 */           throw new UnknownCharacterException();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 149 */       if (this.byteOff + k > paramInt4) {
/* 150 */         throw new ConversionBufferFullException();
/*     */       }
/*     */ 
/* 153 */       for (int m = 0; m < k; m++) {
/* 154 */         paramArrayOfByte[(this.byteOff++)] = arrayOfByte1[m];
/*     */       }
/* 156 */       this.charOff += j;
/*     */     }
/*     */ 
/* 160 */     return this.byteOff - paramInt3;
/*     */   }
/*     */ 
/*     */   public boolean canConvert(char paramChar)
/*     */   {
/* 166 */     return paramChar <= '';
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 172 */     this.byteOff = (this.charOff = 0);
/* 173 */     this.highHalfZoneCode = '\000';
/*     */   }
/*     */ 
/*     */   public int getMaxBytesPerChar()
/*     */   {
/* 181 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteASCII
 * JD-Core Version:    0.6.2
 */