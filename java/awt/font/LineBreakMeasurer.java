/*     */ package java.awt.font;
/*     */ 
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.BreakIterator;
/*     */ 
/*     */ public final class LineBreakMeasurer
/*     */ {
/*     */   private BreakIterator breakIter;
/*     */   private int start;
/*     */   private int pos;
/*     */   private int limit;
/*     */   private TextMeasurer measurer;
/*     */   private CharArrayIterator charIter;
/*     */ 
/*     */   public LineBreakMeasurer(AttributedCharacterIterator paramAttributedCharacterIterator, FontRenderContext paramFontRenderContext)
/*     */   {
/* 277 */     this(paramAttributedCharacterIterator, BreakIterator.getLineInstance(), paramFontRenderContext);
/*     */   }
/*     */ 
/*     */   public LineBreakMeasurer(AttributedCharacterIterator paramAttributedCharacterIterator, BreakIterator paramBreakIterator, FontRenderContext paramFontRenderContext)
/*     */   {
/* 305 */     if (paramAttributedCharacterIterator.getEndIndex() - paramAttributedCharacterIterator.getBeginIndex() < 1) {
/* 306 */       throw new IllegalArgumentException("Text must contain at least one character.");
/*     */     }
/*     */ 
/* 309 */     this.breakIter = paramBreakIterator;
/* 310 */     this.measurer = new TextMeasurer(paramAttributedCharacterIterator, paramFontRenderContext);
/* 311 */     this.limit = paramAttributedCharacterIterator.getEndIndex();
/* 312 */     this.pos = (this.start = paramAttributedCharacterIterator.getBeginIndex());
/*     */ 
/* 314 */     this.charIter = new CharArrayIterator(this.measurer.getChars(), this.start);
/* 315 */     this.breakIter.setText(this.charIter);
/*     */   }
/*     */ 
/*     */   public int nextOffset(float paramFloat)
/*     */   {
/* 328 */     return nextOffset(paramFloat, this.limit, false);
/*     */   }
/*     */ 
/*     */   public int nextOffset(float paramFloat, int paramInt, boolean paramBoolean)
/*     */   {
/* 351 */     int i = this.pos;
/*     */ 
/* 353 */     if (this.pos < this.limit) {
/* 354 */       if (paramInt <= this.pos) {
/* 355 */         throw new IllegalArgumentException("offsetLimit must be after current position");
/*     */       }
/*     */ 
/* 358 */       int j = this.measurer.getLineBreakIndex(this.pos, paramFloat);
/*     */ 
/* 361 */       if (j == this.limit) {
/* 362 */         i = this.limit;
/*     */       }
/* 364 */       else if (Character.isWhitespace(this.measurer.getChars()[(j - this.start)])) {
/* 365 */         i = this.breakIter.following(j);
/*     */       }
/*     */       else
/*     */       {
/* 375 */         int k = j + 1;
/* 376 */         if (k == this.limit) {
/* 377 */           this.breakIter.last();
/* 378 */           i = this.breakIter.previous();
/*     */         }
/*     */         else {
/* 381 */           i = this.breakIter.preceding(k);
/*     */         }
/*     */ 
/* 384 */         if (i <= this.pos)
/*     */         {
/* 386 */           if (paramBoolean) {
/* 387 */             i = this.pos;
/*     */           }
/*     */           else {
/* 390 */             i = Math.max(this.pos + 1, j);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 396 */     if (i > paramInt) {
/* 397 */       i = paramInt;
/*     */     }
/*     */ 
/* 400 */     return i;
/*     */   }
/*     */ 
/*     */   public TextLayout nextLayout(float paramFloat)
/*     */   {
/* 413 */     return nextLayout(paramFloat, this.limit, false);
/*     */   }
/*     */ 
/*     */   public TextLayout nextLayout(float paramFloat, int paramInt, boolean paramBoolean)
/*     */   {
/* 439 */     if (this.pos < this.limit) {
/* 440 */       int i = nextOffset(paramFloat, paramInt, paramBoolean);
/* 441 */       if (i == this.pos) {
/* 442 */         return null;
/*     */       }
/*     */ 
/* 445 */       TextLayout localTextLayout = this.measurer.getLayout(this.pos, i);
/* 446 */       this.pos = i;
/*     */ 
/* 448 */       return localTextLayout;
/*     */     }
/* 450 */     return null;
/*     */   }
/*     */ 
/*     */   public int getPosition()
/*     */   {
/* 461 */     return this.pos;
/*     */   }
/*     */ 
/*     */   public void setPosition(int paramInt)
/*     */   {
/* 475 */     if ((paramInt < this.start) || (paramInt > this.limit)) {
/* 476 */       throw new IllegalArgumentException("position is out of range");
/*     */     }
/* 478 */     this.pos = paramInt;
/*     */   }
/*     */ 
/*     */   public void insertChar(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt)
/*     */   {
/* 499 */     this.measurer.insertChar(paramAttributedCharacterIterator, paramInt);
/*     */ 
/* 501 */     this.limit = paramAttributedCharacterIterator.getEndIndex();
/* 502 */     this.pos = (this.start = paramAttributedCharacterIterator.getBeginIndex());
/*     */ 
/* 504 */     this.charIter.reset(this.measurer.getChars(), paramAttributedCharacterIterator.getBeginIndex());
/* 505 */     this.breakIter.setText(this.charIter);
/*     */   }
/*     */ 
/*     */   public void deleteChar(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt)
/*     */   {
/* 525 */     this.measurer.deleteChar(paramAttributedCharacterIterator, paramInt);
/*     */ 
/* 527 */     this.limit = paramAttributedCharacterIterator.getEndIndex();
/* 528 */     this.pos = (this.start = paramAttributedCharacterIterator.getBeginIndex());
/*     */ 
/* 530 */     this.charIter.reset(this.measurer.getChars(), this.start);
/* 531 */     this.breakIter.setText(this.charIter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.LineBreakMeasurer
 * JD-Core Version:    0.6.2
 */