/*     */ package sun.text;
/*     */ 
/*     */ final class CharSequenceCodePointIterator extends CodePointIterator
/*     */ {
/*     */   private CharSequence text;
/*     */   private int index;
/*     */ 
/*     */   public CharSequenceCodePointIterator(CharSequence paramCharSequence)
/*     */   {
/* 137 */     this.text = paramCharSequence;
/*     */   }
/*     */ 
/*     */   public void setToStart() {
/* 141 */     this.index = 0;
/*     */   }
/*     */ 
/*     */   public void setToLimit() {
/* 145 */     this.index = this.text.length();
/*     */   }
/*     */ 
/*     */   public int next() {
/* 149 */     if (this.index < this.text.length()) {
/* 150 */       char c1 = this.text.charAt(this.index++);
/* 151 */       if ((Character.isHighSurrogate(c1)) && (this.index < this.text.length())) {
/* 152 */         char c2 = this.text.charAt(this.index + 1);
/* 153 */         if (Character.isLowSurrogate(c2)) {
/* 154 */           this.index += 1;
/* 155 */           return Character.toCodePoint(c1, c2);
/*     */         }
/*     */       }
/* 158 */       return c1;
/*     */     }
/* 160 */     return -1;
/*     */   }
/*     */ 
/*     */   public int prev() {
/* 164 */     if (this.index > 0) {
/* 165 */       char c1 = this.text.charAt(--this.index);
/* 166 */       if ((Character.isLowSurrogate(c1)) && (this.index > 0)) {
/* 167 */         char c2 = this.text.charAt(this.index - 1);
/* 168 */         if (Character.isHighSurrogate(c2)) {
/* 169 */           this.index -= 1;
/* 170 */           return Character.toCodePoint(c2, c1);
/*     */         }
/*     */       }
/* 173 */       return c1;
/*     */     }
/* 175 */     return -1;
/*     */   }
/*     */ 
/*     */   public int charIndex() {
/* 179 */     return this.index;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.CharSequenceCodePointIterator
 * JD-Core Version:    0.6.2
 */