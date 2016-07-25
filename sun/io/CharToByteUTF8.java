/*     */ package sun.io;
/*     */ 
/*     */ public class CharToByteUTF8 extends CharToByteConverter
/*     */ {
/*     */   private char highHalfZoneCode;
/*     */ 
/*     */   public int flush(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws MalformedInputException
/*     */   {
/*  50 */     if (this.highHalfZoneCode != 0) {
/*  51 */       this.highHalfZoneCode = '\000';
/*  52 */       this.badInputLength = 0;
/*  53 */       throw new MalformedInputException();
/*     */     }
/*  55 */     this.byteOff = (this.charOff = 0);
/*  56 */     return 0;
/*     */   }
/*     */ 
/*     */   public int convert(char[] paramArrayOfChar, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3, int paramInt4)
/*     */     throws ConversionBufferFullException, MalformedInputException
/*     */   {
/*  67 */     byte[] arrayOfByte = new byte[6];
/*     */ 
/*  71 */     this.charOff = paramInt1;
/*  72 */     this.byteOff = paramInt3;
/*     */     int i;
/*     */     int m;
/*  74 */     if (this.highHalfZoneCode != 0) {
/*  75 */       i = this.highHalfZoneCode;
/*  76 */       this.highHalfZoneCode = '\000';
/*  77 */       if ((paramArrayOfChar[paramInt1] >= 56320) && (paramArrayOfChar[paramInt1] <= 57343))
/*     */       {
/*  79 */         m = (this.highHalfZoneCode - 55296) * 1024 + (paramArrayOfChar[paramInt1] - 56320) + 65536;
/*     */ 
/*  81 */         paramArrayOfByte[0] = ((byte)(0xF0 | m >> 18 & 0x7));
/*  82 */         paramArrayOfByte[1] = ((byte)(0x80 | m >> 12 & 0x3F));
/*  83 */         paramArrayOfByte[2] = ((byte)(0x80 | m >> 6 & 0x3F));
/*  84 */         paramArrayOfByte[3] = ((byte)(0x80 | m & 0x3F));
/*  85 */         this.charOff += 1;
/*  86 */         this.highHalfZoneCode = '\000';
/*     */       }
/*     */       else {
/*  89 */         this.badInputLength = 0;
/*  90 */         throw new MalformedInputException();
/*     */       }
/*     */     }
/*     */ 
/*  94 */     while (this.charOff < paramInt2) {
/*  95 */       i = paramArrayOfChar[this.charOff];
/*     */       int j;
/*     */       int k;
/*  96 */       if (i < 128) {
/*  97 */         arrayOfByte[0] = ((byte)i);
/*  98 */         j = 1;
/*  99 */         k = 1;
/* 100 */       } else if (i < 2048) {
/* 101 */         arrayOfByte[0] = ((byte)(0xC0 | i >> 6 & 0x1F));
/* 102 */         arrayOfByte[1] = ((byte)(0x80 | i & 0x3F));
/* 103 */         j = 1;
/* 104 */         k = 2;
/* 105 */       } else if ((i >= 55296) && (i <= 56319))
/*     */       {
/* 107 */         if (this.charOff + 1 >= paramInt2) {
/* 108 */           this.highHalfZoneCode = i;
/* 109 */           break;
/*     */         }
/*     */ 
/* 112 */         m = paramArrayOfChar[(this.charOff + 1)];
/* 113 */         if ((m < 56320) || (m > 57343)) {
/* 114 */           this.badInputLength = 1;
/* 115 */           throw new MalformedInputException();
/*     */         }
/* 117 */         int n = (i - 55296) * 1024 + (m - 56320) + 65536;
/*     */ 
/* 119 */         arrayOfByte[0] = ((byte)(0xF0 | n >> 18 & 0x7));
/* 120 */         arrayOfByte[1] = ((byte)(0x80 | n >> 12 & 0x3F));
/* 121 */         arrayOfByte[2] = ((byte)(0x80 | n >> 6 & 0x3F));
/* 122 */         arrayOfByte[3] = ((byte)(0x80 | n & 0x3F));
/* 123 */         k = 4;
/* 124 */         j = 2;
/*     */       } else {
/* 126 */         arrayOfByte[0] = ((byte)(0xE0 | i >> 12 & 0xF));
/* 127 */         arrayOfByte[1] = ((byte)(0x80 | i >> 6 & 0x3F));
/* 128 */         arrayOfByte[2] = ((byte)(0x80 | i & 0x3F));
/* 129 */         j = 1;
/* 130 */         k = 3;
/*     */       }
/* 132 */       if (this.byteOff + k > paramInt4) {
/* 133 */         throw new ConversionBufferFullException();
/*     */       }
/* 135 */       for (m = 0; m < k; m++) {
/* 136 */         paramArrayOfByte[(this.byteOff++)] = arrayOfByte[m];
/*     */       }
/* 138 */       this.charOff += j;
/*     */     }
/* 140 */     return this.byteOff - paramInt3;
/*     */   }
/*     */ 
/*     */   public boolean canConvert(char paramChar) {
/* 144 */     return true;
/*     */   }
/*     */ 
/*     */   public int getMaxBytesPerChar() {
/* 148 */     return 3;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 152 */     this.byteOff = (this.charOff = 0);
/* 153 */     this.highHalfZoneCode = '\000';
/*     */   }
/*     */ 
/*     */   public String getCharacterEncoding() {
/* 157 */     return "UTF8";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.CharToByteUTF8
 * JD-Core Version:    0.6.2
 */