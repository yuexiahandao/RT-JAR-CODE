/*     */ package sun.text;
/*     */ 
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ final class CharacterIteratorCodePointIterator extends CodePointIterator
/*     */ {
/*     */   private CharacterIterator iter;
/*     */ 
/*     */   public CharacterIteratorCodePointIterator(CharacterIterator paramCharacterIterator)
/*     */   {
/* 188 */     this.iter = paramCharacterIterator;
/*     */   }
/*     */ 
/*     */   public void setToStart() {
/* 192 */     this.iter.setIndex(this.iter.getBeginIndex());
/*     */   }
/*     */ 
/*     */   public void setToLimit() {
/* 196 */     this.iter.setIndex(this.iter.getEndIndex());
/*     */   }
/*     */ 
/*     */   public int next() {
/* 200 */     int i = this.iter.current();
/* 201 */     if (i != 65535) {
/* 202 */       int j = this.iter.next();
/* 203 */       if ((Character.isHighSurrogate(i)) && (j != 65535) && 
/* 204 */         (Character.isLowSurrogate(j))) {
/* 205 */         this.iter.next();
/* 206 */         return Character.toCodePoint(i, j);
/*     */       }
/*     */ 
/* 209 */       return i;
/*     */     }
/* 211 */     return -1;
/*     */   }
/*     */ 
/*     */   public int prev() {
/* 215 */     int i = this.iter.previous();
/* 216 */     if (i != 65535) {
/* 217 */       if (Character.isLowSurrogate(i)) {
/* 218 */         char c = this.iter.previous();
/* 219 */         if (Character.isHighSurrogate(c)) {
/* 220 */           return Character.toCodePoint(c, i);
/*     */         }
/* 222 */         this.iter.next();
/*     */       }
/* 224 */       return i;
/*     */     }
/* 226 */     return -1;
/*     */   }
/*     */ 
/*     */   public int charIndex() {
/* 230 */     return this.iter.getIndex();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.CharacterIteratorCodePointIterator
 * JD-Core Version:    0.6.2
 */