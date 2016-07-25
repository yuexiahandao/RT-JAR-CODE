/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ 
/*     */ public final class CompositeStrike extends FontStrike
/*     */ {
/*     */   static final int SLOTMASK = 16777215;
/*     */   private CompositeFont compFont;
/*     */   private PhysicalStrike[] strikes;
/*  48 */   int numGlyphs = 0;
/*     */ 
/*     */   CompositeStrike(CompositeFont paramCompositeFont, FontStrikeDesc paramFontStrikeDesc) {
/*  51 */     this.compFont = paramCompositeFont;
/*  52 */     this.desc = paramFontStrikeDesc;
/*  53 */     this.disposer = new FontStrikeDisposer(this.compFont, paramFontStrikeDesc);
/*  54 */     if (paramFontStrikeDesc.style != this.compFont.style) {
/*  55 */       this.algoStyle = true;
/*  56 */       if (((paramFontStrikeDesc.style & 0x1) == 1) && ((this.compFont.style & 0x1) == 0))
/*     */       {
/*  58 */         this.boldness = 1.33F;
/*     */       }
/*  60 */       if (((paramFontStrikeDesc.style & 0x2) == 2) && ((this.compFont.style & 0x2) == 0))
/*     */       {
/*  62 */         this.italic = 0.7F;
/*     */       }
/*     */     }
/*  65 */     this.strikes = new PhysicalStrike[this.compFont.numSlots];
/*     */   }
/*     */ 
/*     */   PhysicalStrike getStrikeForGlyph(int paramInt)
/*     */   {
/*  70 */     return getStrikeForSlot(paramInt >>> 24);
/*     */   }
/*     */ 
/*     */   PhysicalStrike getStrikeForSlot(int paramInt)
/*     */   {
/*  75 */     PhysicalStrike localPhysicalStrike = this.strikes[paramInt];
/*  76 */     if (localPhysicalStrike == null) {
/*  77 */       localPhysicalStrike = (PhysicalStrike)this.compFont.getSlotFont(paramInt).getStrike(this.desc);
/*     */ 
/*  80 */       this.strikes[paramInt] = localPhysicalStrike;
/*     */     }
/*  82 */     return localPhysicalStrike;
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs() {
/*  86 */     return this.compFont.getNumGlyphs();
/*     */   }
/*     */ 
/*     */   StrikeMetrics getFontMetrics() {
/*  90 */     if (this.strikeMetrics == null) {
/*  91 */       StrikeMetrics localStrikeMetrics = new StrikeMetrics();
/*  92 */       for (int i = 0; i < this.compFont.numMetricsSlots; i++) {
/*  93 */         localStrikeMetrics.merge(getStrikeForSlot(i).getFontMetrics());
/*     */       }
/*  95 */       this.strikeMetrics = localStrikeMetrics;
/*     */     }
/*  97 */     return this.strikeMetrics;
/*     */   }
/*     */ 
/*     */   void getGlyphImagePtrs(int[] paramArrayOfInt, long[] paramArrayOfLong, int paramInt)
/*     */   {
/* 114 */     PhysicalStrike localPhysicalStrike = getStrikeForSlot(0);
/* 115 */     int i = localPhysicalStrike.getSlot0GlyphImagePtrs(paramArrayOfInt, paramArrayOfLong, paramInt);
/* 116 */     if (i == paramInt) {
/* 117 */       return;
/*     */     }
/* 119 */     for (int j = i; j < paramInt; j++) {
/* 120 */       localPhysicalStrike = getStrikeForGlyph(paramArrayOfInt[j]);
/* 121 */       paramArrayOfLong[j] = localPhysicalStrike.getGlyphImagePtr(paramArrayOfInt[j] & 0xFFFFFF);
/*     */     }
/*     */   }
/*     */ 
/*     */   long getGlyphImagePtr(int paramInt)
/*     */   {
/* 127 */     PhysicalStrike localPhysicalStrike = getStrikeForGlyph(paramInt);
/* 128 */     return localPhysicalStrike.getGlyphImagePtr(paramInt & 0xFFFFFF);
/*     */   }
/*     */ 
/*     */   void getGlyphImageBounds(int paramInt, Point2D.Float paramFloat, Rectangle paramRectangle) {
/* 132 */     PhysicalStrike localPhysicalStrike = getStrikeForGlyph(paramInt);
/* 133 */     localPhysicalStrike.getGlyphImageBounds(paramInt & 0xFFFFFF, paramFloat, paramRectangle);
/*     */   }
/*     */ 
/*     */   Point2D.Float getGlyphMetrics(int paramInt) {
/* 137 */     PhysicalStrike localPhysicalStrike = getStrikeForGlyph(paramInt);
/* 138 */     return localPhysicalStrike.getGlyphMetrics(paramInt & 0xFFFFFF);
/*     */   }
/*     */ 
/*     */   Point2D.Float getCharMetrics(char paramChar) {
/* 142 */     return getGlyphMetrics(this.compFont.getMapper().charToGlyph(paramChar));
/*     */   }
/*     */ 
/*     */   float getGlyphAdvance(int paramInt) {
/* 146 */     PhysicalStrike localPhysicalStrike = getStrikeForGlyph(paramInt);
/* 147 */     return localPhysicalStrike.getGlyphAdvance(paramInt & 0xFFFFFF);
/*     */   }
/*     */ 
/*     */   float getCodePointAdvance(int paramInt)
/*     */   {
/* 158 */     return getGlyphAdvance(this.compFont.getMapper().charToGlyph(paramInt));
/*     */   }
/*     */ 
/*     */   Rectangle2D.Float getGlyphOutlineBounds(int paramInt) {
/* 162 */     PhysicalStrike localPhysicalStrike = getStrikeForGlyph(paramInt);
/* 163 */     return localPhysicalStrike.getGlyphOutlineBounds(paramInt & 0xFFFFFF);
/*     */   }
/*     */ 
/*     */   GeneralPath getGlyphOutline(int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/* 168 */     PhysicalStrike localPhysicalStrike = getStrikeForGlyph(paramInt);
/* 169 */     GeneralPath localGeneralPath = localPhysicalStrike.getGlyphOutline(paramInt & 0xFFFFFF, paramFloat1, paramFloat2);
/* 170 */     if (localGeneralPath == null) {
/* 171 */       return new GeneralPath();
/*     */     }
/* 173 */     return localGeneralPath;
/*     */   }
/*     */ 
/*     */   GeneralPath getGlyphVectorOutline(int[] paramArrayOfInt, float paramFloat1, float paramFloat2)
/*     */   {
/* 185 */     Object localObject = null;
/*     */ 
/* 187 */     int i = 0;
/*     */ 
/* 190 */     while (i < paramArrayOfInt.length) {
/* 191 */       int j = i;
/* 192 */       int k = paramArrayOfInt[i] >>> 24;
/* 193 */       while ((i < paramArrayOfInt.length) && (paramArrayOfInt[(i + 1)] >>> 24 == k))
/*     */       {
/* 195 */         i++;
/*     */       }
/* 197 */       int m = i - j + 1;
/* 198 */       int[] arrayOfInt = new int[m];
/* 199 */       for (int n = 0; n < m; n++) {
/* 200 */         paramArrayOfInt[n] &= 16777215;
/*     */       }
/* 202 */       GeneralPath localGeneralPath = getStrikeForSlot(k).getGlyphVectorOutline(arrayOfInt, paramFloat1, paramFloat2);
/* 203 */       if (localObject == null)
/* 204 */         localObject = localGeneralPath;
/* 205 */       else if (localGeneralPath != null) {
/* 206 */         localObject.append(localGeneralPath, false);
/*     */       }
/*     */     }
/* 209 */     if (localObject == null) {
/* 210 */       return new GeneralPath();
/*     */     }
/* 212 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.CompositeStrike
 * JD-Core Version:    0.6.2
 */