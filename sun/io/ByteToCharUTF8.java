/*     */ package sun.io;
/*     */ 
/*     */ public class ByteToCharUTF8 extends ByteToCharConverter
/*     */ {
/*     */   private int savedSize;
/*     */   private byte[] savedBytes;
/*     */ 
/*     */   public ByteToCharUTF8()
/*     */   {
/*  41 */     this.savedSize = 0;
/*  42 */     this.savedBytes = new byte[5];
/*     */   }
/*     */ 
/*     */   public int flush(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */     throws MalformedInputException
/*     */   {
/*  48 */     if (this.savedSize != 0) {
/*  49 */       this.savedSize = 0;
/*  50 */       this.badInputLength = 0;
/*  51 */       throw new MalformedInputException();
/*     */     }
/*  53 */     this.byteOff = (this.charOff = 0);
/*  54 */     return 0;
/*     */   }
/*     */ 
/*     */   public int convert(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4)
/*     */     throws MalformedInputException, ConversionBufferFullException
/*     */   {
/*  65 */     char[] arrayOfChar = new char[2];
/*     */ 
/*  67 */     int i1 = 0;
/*     */     int i3;
/*  69 */     if (this.savedSize != 0)
/*     */     {
/*  71 */       byte[] arrayOfByte = new byte[paramInt2 - paramInt1 + this.savedSize];
/*  72 */       for (i3 = 0; i3 < this.savedSize; i3++) {
/*  73 */         arrayOfByte[i3] = this.savedBytes[i3];
/*     */       }
/*  75 */       System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, this.savedSize, paramInt2 - paramInt1);
/*  76 */       paramArrayOfByte = arrayOfByte;
/*  77 */       paramInt1 = 0;
/*  78 */       paramInt2 = arrayOfByte.length;
/*  79 */       i1 = -this.savedSize;
/*  80 */       this.savedSize = 0;
/*     */     }
/*     */ 
/*  83 */     this.charOff = paramInt3;
/*  84 */     this.byteOff = paramInt1;
/*     */ 
/*  87 */     while (this.byteOff < paramInt2)
/*     */     {
/*  89 */       int i2 = this.byteOff;
/*  90 */       int i = paramArrayOfByte[(this.byteOff++)] & 0xFF;
/*     */       int n;
/*  92 */       if ((i & 0x80) == 0) {
/*  93 */         arrayOfChar[0] = ((char)i);
/*  94 */         n = 1;
/*     */       }
/*     */       else
/*     */       {
/*     */         int j;
/*  95 */         if ((i & 0xE0) == 192) {
/*  96 */           if (this.byteOff >= paramInt2) {
/*  97 */             this.savedSize = 1;
/*  98 */             this.savedBytes[0] = ((byte)i);
/*  99 */             break;
/*     */           }
/* 101 */           j = paramArrayOfByte[(this.byteOff++)] & 0xFF;
/* 102 */           if ((j & 0xC0) != 128) {
/* 103 */             this.badInputLength = 2;
/* 104 */             this.byteOff += i1;
/* 105 */             throw new MalformedInputException();
/*     */           }
/* 107 */           arrayOfChar[0] = ((char)((i & 0x1F) << 6 | j & 0x3F));
/* 108 */           n = 1;
/*     */         }
/*     */         else
/*     */         {
/*     */           int k;
/* 109 */           if ((i & 0xF0) == 224) {
/* 110 */             if (this.byteOff + 1 >= paramInt2) {
/* 111 */               this.savedBytes[0] = ((byte)i);
/* 112 */               if (this.byteOff >= paramInt2) {
/* 113 */                 this.savedSize = 1; break;
/*     */               }
/* 115 */               this.savedSize = 2;
/* 116 */               this.savedBytes[1] = paramArrayOfByte[(this.byteOff++)];
/*     */ 
/* 118 */               break;
/*     */             }
/* 120 */             j = paramArrayOfByte[(this.byteOff++)] & 0xFF;
/* 121 */             k = paramArrayOfByte[(this.byteOff++)] & 0xFF;
/* 122 */             if (((j & 0xC0) != 128) || ((k & 0xC0) != 128)) {
/* 123 */               this.badInputLength = 3;
/* 124 */               this.byteOff += i1;
/* 125 */               throw new MalformedInputException();
/*     */             }
/* 127 */             arrayOfChar[0] = ((char)((i & 0xF) << 12 | (j & 0x3F) << 6 | k & 0x3F));
/*     */ 
/* 130 */             n = 1;
/* 131 */           } else if ((i & 0xF8) == 240) {
/* 132 */             if (this.byteOff + 2 >= paramInt2) {
/* 133 */               this.savedBytes[0] = ((byte)i);
/* 134 */               if (this.byteOff >= paramInt2) {
/* 135 */                 this.savedSize = 1; break;
/* 136 */               }if (this.byteOff + 1 >= paramInt2) {
/* 137 */                 this.savedSize = 2;
/* 138 */                 this.savedBytes[1] = paramArrayOfByte[(this.byteOff++)]; break;
/*     */               }
/* 140 */               this.savedSize = 3;
/* 141 */               this.savedBytes[1] = paramArrayOfByte[(this.byteOff++)];
/* 142 */               this.savedBytes[2] = paramArrayOfByte[(this.byteOff++)];
/*     */ 
/* 144 */               break;
/*     */             }
/* 146 */             j = paramArrayOfByte[(this.byteOff++)] & 0xFF;
/* 147 */             k = paramArrayOfByte[(this.byteOff++)] & 0xFF;
/* 148 */             int m = paramArrayOfByte[(this.byteOff++)] & 0xFF;
/* 149 */             if (((j & 0xC0) != 128) || ((k & 0xC0) != 128) || ((m & 0xC0) != 128))
/*     */             {
/* 152 */               this.badInputLength = 4;
/* 153 */               this.byteOff += i1;
/* 154 */               throw new MalformedInputException();
/*     */             }
/*     */ 
/* 157 */             i3 = (0x7 & i) << 18 | (0x3F & j) << 12 | (0x3F & k) << 6 | 0x3F & m;
/*     */ 
/* 161 */             arrayOfChar[0] = ((char)((i3 - 65536) / 1024 + 55296));
/* 162 */             arrayOfChar[1] = ((char)((i3 - 65536) % 1024 + 56320));
/* 163 */             n = 2;
/*     */           } else {
/* 165 */             this.badInputLength = 1;
/* 166 */             this.byteOff += i1;
/* 167 */             throw new MalformedInputException();
/*     */           }
/*     */         }
/*     */       }
/* 170 */       if (this.charOff + n > paramInt4) {
/* 171 */         this.byteOff = i2;
/* 172 */         this.byteOff += i1;
/* 173 */         throw new ConversionBufferFullException();
/*     */       }
/*     */ 
/* 176 */       for (i3 = 0; i3 < n; i3++) {
/* 177 */         paramArrayOfChar[(this.charOff + i3)] = arrayOfChar[i3];
/*     */       }
/* 179 */       this.charOff += n;
/*     */     }
/*     */ 
/* 182 */     this.byteOff += i1;
/* 183 */     return this.charOff - paramInt3;
/*     */   }
/*     */ 
/*     */   public String getCharacterEncoding()
/*     */   {
/* 190 */     return "UTF8";
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 197 */     this.byteOff = (this.charOff = 0);
/* 198 */     this.savedSize = 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharUTF8
 * JD-Core Version:    0.6.2
 */