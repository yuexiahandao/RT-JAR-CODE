/*     */ package sun.io;
/*     */ 
/*     */ public abstract class ByteToCharSingleByte extends ByteToCharConverter
/*     */ {
/*     */   protected String byteToCharTable;
/*     */ 
/*     */   public String getByteToCharTable()
/*     */   {
/*  43 */     return this.byteToCharTable;
/*     */   }
/*     */ 
/*     */   public int flush(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/*  47 */     this.byteOff = (this.charOff = 0);
/*  48 */     return 0;
/*     */   }
/*     */ 
/*     */   public int convert(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4)
/*     */     throws UnknownCharacterException, MalformedInputException, ConversionBufferFullException
/*     */   {
/*  84 */     this.charOff = paramInt3;
/*  85 */     this.byteOff = paramInt1;
/*     */ 
/*  88 */     while (this.byteOff < paramInt2)
/*     */     {
/*  90 */       int j = paramArrayOfByte[this.byteOff];
/*     */ 
/*  96 */       int i = getUnicode(j);
/*     */ 
/*  99 */       if (i == 65533) {
/* 100 */         if (this.subMode) {
/* 101 */           i = this.subChars[0];
/*     */         } else {
/* 103 */           this.badInputLength = 1;
/* 104 */           throw new UnknownCharacterException();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 109 */       if (this.charOff >= paramInt4) {
/* 110 */         throw new ConversionBufferFullException();
/*     */       }
/*     */ 
/* 113 */       paramArrayOfChar[this.charOff] = i;
/* 114 */       this.charOff += 1;
/* 115 */       this.byteOff += 1;
/*     */     }
/*     */ 
/* 119 */     return this.charOff - paramInt3;
/*     */   }
/*     */ 
/*     */   protected char getUnicode(int paramInt) {
/* 123 */     int i = paramInt + 128;
/* 124 */     if ((i >= this.byteToCharTable.length()) || (i < 0))
/* 125 */       return 65533;
/* 126 */     return this.byteToCharTable.charAt(i);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 134 */     this.byteOff = (this.charOff = 0);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharSingleByte
 * JD-Core Version:    0.6.2
 */