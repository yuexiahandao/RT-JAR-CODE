/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.TextHitInfo;
/*     */ import java.awt.font.TextLayout;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ 
/*     */ class GlyphPainter2 extends GlyphView.GlyphPainter
/*     */ {
/*     */   TextLayout layout;
/*     */ 
/*     */   public GlyphPainter2(TextLayout paramTextLayout)
/*     */   {
/*  54 */     this.layout = paramTextLayout;
/*     */   }
/*     */ 
/*     */   public GlyphView.GlyphPainter getPainter(GlyphView paramGlyphView, int paramInt1, int paramInt2)
/*     */   {
/*  61 */     return null;
/*     */   }
/*     */ 
/*     */   public float getSpan(GlyphView paramGlyphView, int paramInt1, int paramInt2, TabExpander paramTabExpander, float paramFloat)
/*     */   {
/*  73 */     if ((paramInt1 == paramGlyphView.getStartOffset()) && (paramInt2 == paramGlyphView.getEndOffset())) {
/*  74 */       return this.layout.getAdvance();
/*     */     }
/*  76 */     int i = paramGlyphView.getStartOffset();
/*  77 */     int j = paramInt1 - i;
/*  78 */     int k = paramInt2 - i;
/*     */ 
/*  80 */     TextHitInfo localTextHitInfo1 = TextHitInfo.afterOffset(j);
/*  81 */     TextHitInfo localTextHitInfo2 = TextHitInfo.beforeOffset(k);
/*  82 */     float[] arrayOfFloat = this.layout.getCaretInfo(localTextHitInfo1);
/*  83 */     float f1 = arrayOfFloat[0];
/*  84 */     arrayOfFloat = this.layout.getCaretInfo(localTextHitInfo2);
/*  85 */     float f2 = arrayOfFloat[0];
/*  86 */     return f2 > f1 ? f2 - f1 : f1 - f2;
/*     */   }
/*     */ 
/*     */   public float getHeight(GlyphView paramGlyphView) {
/*  90 */     return this.layout.getAscent() + this.layout.getDescent() + this.layout.getLeading();
/*     */   }
/*     */ 
/*     */   public float getAscent(GlyphView paramGlyphView)
/*     */   {
/*  98 */     return this.layout.getAscent();
/*     */   }
/*     */ 
/*     */   public float getDescent(GlyphView paramGlyphView)
/*     */   {
/* 106 */     return this.layout.getDescent();
/*     */   }
/*     */ 
/*     */   public void paint(GlyphView paramGlyphView, Graphics paramGraphics, Shape paramShape, int paramInt1, int paramInt2)
/*     */   {
/* 116 */     if ((paramGraphics instanceof Graphics2D)) {
/* 117 */       Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 118 */       Graphics2D localGraphics2D = (Graphics2D)paramGraphics;
/* 119 */       float f1 = (float)localRectangle2D.getY() + this.layout.getAscent() + this.layout.getLeading();
/* 120 */       float f2 = (float)localRectangle2D.getX();
/* 121 */       if ((paramInt1 > paramGlyphView.getStartOffset()) || (paramInt2 < paramGlyphView.getEndOffset()))
/*     */       {
/*     */         try
/*     */         {
/* 125 */           Shape localShape1 = paramGlyphView.modelToView(paramInt1, Position.Bias.Forward, paramInt2, Position.Bias.Backward, paramShape);
/*     */ 
/* 127 */           Shape localShape2 = paramGraphics.getClip();
/* 128 */           localGraphics2D.clip(localShape1);
/* 129 */           this.layout.draw(localGraphics2D, f2, f1);
/* 130 */           paramGraphics.setClip(localShape2); } catch (BadLocationException localBadLocationException) {
/*     */         }
/*     */       }
/* 133 */       else this.layout.draw(localGraphics2D, f2, f1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Shape modelToView(GlyphView paramGlyphView, int paramInt, Position.Bias paramBias, Shape paramShape)
/*     */     throws BadLocationException
/*     */   {
/* 140 */     int i = paramInt - paramGlyphView.getStartOffset();
/* 141 */     Rectangle2D localRectangle2D = paramShape.getBounds2D();
/* 142 */     TextHitInfo localTextHitInfo = paramBias == Position.Bias.Forward ? TextHitInfo.afterOffset(i) : TextHitInfo.beforeOffset(i);
/*     */ 
/* 144 */     float[] arrayOfFloat = this.layout.getCaretInfo(localTextHitInfo);
/*     */ 
/* 148 */     localRectangle2D.setRect(localRectangle2D.getX() + arrayOfFloat[0], localRectangle2D.getY(), 1.0D, localRectangle2D.getHeight());
/* 149 */     return localRectangle2D;
/*     */   }
/*     */ 
/*     */   public int viewToModel(GlyphView paramGlyphView, float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 170 */     Rectangle2D localRectangle2D = (paramShape instanceof Rectangle2D) ? (Rectangle2D)paramShape : paramShape.getBounds2D();
/*     */ 
/* 173 */     TextHitInfo localTextHitInfo = this.layout.hitTestChar(paramFloat1 - (float)localRectangle2D.getX(), 0.0F);
/* 174 */     int i = localTextHitInfo.getInsertionIndex();
/*     */ 
/* 176 */     if (i == paramGlyphView.getEndOffset()) {
/* 177 */       i--;
/*     */     }
/*     */ 
/* 180 */     paramArrayOfBias[0] = (localTextHitInfo.isLeadingEdge() ? Position.Bias.Forward : Position.Bias.Backward);
/* 181 */     return i + paramGlyphView.getStartOffset();
/*     */   }
/*     */ 
/*     */   public int getBoundedPosition(GlyphView paramGlyphView, int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/* 204 */     if (paramFloat2 < 0.0F)
/* 205 */       throw new IllegalArgumentException("Length must be >= 0.");
/*     */     TextHitInfo localTextHitInfo;
/* 209 */     if (this.layout.isLeftToRight())
/* 210 */       localTextHitInfo = this.layout.hitTestChar(paramFloat2, 0.0F);
/*     */     else {
/* 212 */       localTextHitInfo = this.layout.hitTestChar(this.layout.getAdvance() - paramFloat2, 0.0F);
/*     */     }
/* 214 */     return paramGlyphView.getStartOffset() + localTextHitInfo.getCharIndex();
/*     */   }
/*     */ 
/*     */   public int getNextVisualPositionFrom(GlyphView paramGlyphView, int paramInt1, Position.Bias paramBias, Shape paramShape, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */     throws BadLocationException
/*     */   {
/* 242 */     int i = paramGlyphView.getStartOffset();
/* 243 */     int j = paramGlyphView.getEndOffset();
/*     */     AbstractDocument localAbstractDocument;
/*     */     boolean bool;
/*     */     Segment localSegment;
/*     */     char c;
/*     */     TextHitInfo localTextHitInfo1;
/*     */     TextHitInfo localTextHitInfo2;
/* 249 */     switch (paramInt2) {
/*     */     case 1:
/* 251 */       break;
/*     */     case 5:
/* 253 */       break;
/*     */     case 3:
/* 255 */       localAbstractDocument = (AbstractDocument)paramGlyphView.getDocument();
/* 256 */       bool = localAbstractDocument.isLeftToRight(i, j);
/*     */ 
/* 258 */       if (i == localAbstractDocument.getLength()) {
/* 259 */         if (paramInt1 == -1) {
/* 260 */           paramArrayOfBias[0] = Position.Bias.Forward;
/* 261 */           return i;
/*     */         }
/*     */ 
/* 265 */         return -1;
/*     */       }
/* 267 */       if (paramInt1 == -1)
/*     */       {
/* 269 */         if (bool) {
/* 270 */           paramArrayOfBias[0] = Position.Bias.Forward;
/* 271 */           return i;
/*     */         }
/* 273 */         localSegment = paramGlyphView.getText(j - 1, j);
/* 274 */         c = localSegment.array[localSegment.offset];
/* 275 */         SegmentCache.releaseSharedSegment(localSegment);
/* 276 */         if (c == '\n') {
/* 277 */           paramArrayOfBias[0] = Position.Bias.Forward;
/* 278 */           return j - 1;
/*     */         }
/* 280 */         paramArrayOfBias[0] = Position.Bias.Backward;
/* 281 */         return j;
/*     */       }
/*     */ 
/* 284 */       if (paramBias == Position.Bias.Forward)
/* 285 */         localTextHitInfo1 = TextHitInfo.afterOffset(paramInt1 - i);
/*     */       else
/* 287 */         localTextHitInfo1 = TextHitInfo.beforeOffset(paramInt1 - i);
/* 288 */       localTextHitInfo2 = this.layout.getNextRightHit(localTextHitInfo1);
/* 289 */       if (localTextHitInfo2 == null) {
/* 290 */         return -1;
/*     */       }
/* 292 */       if (bool != this.layout.isLeftToRight())
/*     */       {
/* 296 */         localTextHitInfo2 = this.layout.getVisualOtherHit(localTextHitInfo2);
/*     */       }
/* 298 */       paramInt1 = localTextHitInfo2.getInsertionIndex() + i;
/*     */ 
/* 300 */       if (paramInt1 == j)
/*     */       {
/* 303 */         localSegment = paramGlyphView.getText(j - 1, j);
/* 304 */         c = localSegment.array[localSegment.offset];
/* 305 */         SegmentCache.releaseSharedSegment(localSegment);
/* 306 */         if (c == '\n') {
/* 307 */           return -1;
/*     */         }
/* 309 */         paramArrayOfBias[0] = Position.Bias.Backward;
/*     */       }
/*     */       else {
/* 312 */         paramArrayOfBias[0] = Position.Bias.Forward;
/*     */       }
/* 314 */       return paramInt1;
/*     */     case 7:
/* 316 */       localAbstractDocument = (AbstractDocument)paramGlyphView.getDocument();
/* 317 */       bool = localAbstractDocument.isLeftToRight(i, j);
/*     */ 
/* 319 */       if (i == localAbstractDocument.getLength()) {
/* 320 */         if (paramInt1 == -1) {
/* 321 */           paramArrayOfBias[0] = Position.Bias.Forward;
/* 322 */           return i;
/*     */         }
/*     */ 
/* 326 */         return -1;
/*     */       }
/* 328 */       if (paramInt1 == -1)
/*     */       {
/* 330 */         if (bool) {
/* 331 */           localSegment = paramGlyphView.getText(j - 1, j);
/* 332 */           c = localSegment.array[localSegment.offset];
/* 333 */           SegmentCache.releaseSharedSegment(localSegment);
/* 334 */           if ((c == '\n') || (Character.isSpaceChar(c))) {
/* 335 */             paramArrayOfBias[0] = Position.Bias.Forward;
/* 336 */             return j - 1;
/*     */           }
/* 338 */           paramArrayOfBias[0] = Position.Bias.Backward;
/* 339 */           return j;
/*     */         }
/* 341 */         paramArrayOfBias[0] = Position.Bias.Forward;
/* 342 */         return i;
/*     */       }
/*     */ 
/* 345 */       if (paramBias == Position.Bias.Forward)
/* 346 */         localTextHitInfo1 = TextHitInfo.afterOffset(paramInt1 - i);
/*     */       else
/* 348 */         localTextHitInfo1 = TextHitInfo.beforeOffset(paramInt1 - i);
/* 349 */       localTextHitInfo2 = this.layout.getNextLeftHit(localTextHitInfo1);
/* 350 */       if (localTextHitInfo2 == null) {
/* 351 */         return -1;
/*     */       }
/* 353 */       if (bool != this.layout.isLeftToRight())
/*     */       {
/* 357 */         localTextHitInfo2 = this.layout.getVisualOtherHit(localTextHitInfo2);
/*     */       }
/* 359 */       paramInt1 = localTextHitInfo2.getInsertionIndex() + i;
/*     */ 
/* 361 */       if (paramInt1 == j)
/*     */       {
/* 364 */         localSegment = paramGlyphView.getText(j - 1, j);
/* 365 */         c = localSegment.array[localSegment.offset];
/* 366 */         SegmentCache.releaseSharedSegment(localSegment);
/* 367 */         if (c == '\n') {
/* 368 */           return -1;
/*     */         }
/* 370 */         paramArrayOfBias[0] = Position.Bias.Backward;
/*     */       }
/*     */       else {
/* 373 */         paramArrayOfBias[0] = Position.Bias.Forward;
/*     */       }
/* 375 */       return paramInt1;
/*     */     case 2:
/*     */     case 4:
/*     */     case 6:
/*     */     default:
/* 377 */       throw new IllegalArgumentException("Bad direction: " + paramInt2);
/*     */     }
/* 379 */     return paramInt1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.GlyphPainter2
 * JD-Core Version:    0.6.2
 */