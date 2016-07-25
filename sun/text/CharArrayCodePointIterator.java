/*     */ package sun.text;
/*     */ 
/*     */ final class CharArrayCodePointIterator extends CodePointIterator
/*     */ {
/*     */   private char[] text;
/*     */   private int start;
/*     */   private int limit;
/*     */   private int index;
/*     */ 
/*     */   public CharArrayCodePointIterator(char[] paramArrayOfChar)
/*     */   {
/*  75 */     this.text = paramArrayOfChar;
/*  76 */     this.limit = paramArrayOfChar.length;
/*     */   }
/*     */ 
/*     */   public CharArrayCodePointIterator(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/*  80 */     if ((paramInt1 < 0) || (paramInt2 < paramInt1) || (paramInt2 > paramArrayOfChar.length)) {
/*  81 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*  84 */     this.text = paramArrayOfChar;
/*  85 */     this.start = (this.index = paramInt1);
/*  86 */     this.limit = paramInt2;
/*     */   }
/*     */ 
/*     */   public void setToStart() {
/*  90 */     this.index = this.start;
/*     */   }
/*     */ 
/*     */   public void setToLimit() {
/*  94 */     this.index = this.limit;
/*     */   }
/*     */ 
/*     */   public int next() {
/*  98 */     if (this.index < this.limit) {
/*  99 */       char c1 = this.text[(this.index++)];
/* 100 */       if ((Character.isHighSurrogate(c1)) && (this.index < this.limit)) {
/* 101 */         char c2 = this.text[this.index];
/* 102 */         if (Character.isLowSurrogate(c2)) {
/* 103 */           this.index += 1;
/* 104 */           return Character.toCodePoint(c1, c2);
/*     */         }
/*     */       }
/* 107 */       return c1;
/*     */     }
/* 109 */     return -1;
/*     */   }
/*     */ 
/*     */   public int prev() {
/* 113 */     if (this.index > this.start) {
/* 114 */       char c1 = this.text[(--this.index)];
/* 115 */       if ((Character.isLowSurrogate(c1)) && (this.index > this.start)) {
/* 116 */         char c2 = this.text[(this.index - 1)];
/* 117 */         if (Character.isHighSurrogate(c2)) {
/* 118 */           this.index -= 1;
/* 119 */           return Character.toCodePoint(c2, c1);
/*     */         }
/*     */       }
/* 122 */       return c1;
/*     */     }
/* 124 */     return -1;
/*     */   }
/*     */ 
/*     */   public int charIndex() {
/* 128 */     return this.index;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.CharArrayCodePointIterator
 * JD-Core Version:    0.6.2
 */