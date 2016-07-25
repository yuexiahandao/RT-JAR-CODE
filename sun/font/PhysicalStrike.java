/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public abstract class PhysicalStrike extends FontStrike
/*     */ {
/*     */   static final long INTMASK = 4294967295L;
/*     */   static boolean longAddresses;
/*     */   private PhysicalFont physicalFont;
/*     */   protected CharToGlyphMapper mapper;
/*     */   protected long pScalerContext;
/*     */   protected long[] longGlyphImages;
/*     */   protected int[] intGlyphImages;
/*     */   ConcurrentHashMap<Integer, Point2D.Float> glyphPointMapCache;
/*     */   protected boolean getImageWithAdvance;
/*     */   protected static final int complexTX = 124;
/*     */ 
/*     */   PhysicalStrike(PhysicalFont paramPhysicalFont, FontStrikeDesc paramFontStrikeDesc)
/*     */   {
/*  84 */     this.physicalFont = paramPhysicalFont;
/*  85 */     this.desc = paramFontStrikeDesc;
/*     */   }
/*     */ 
/*     */   protected PhysicalStrike()
/*     */   {
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs()
/*     */   {
/*  95 */     return this.physicalFont.getNumGlyphs();
/*     */   }
/*     */ 
/*     */   StrikeMetrics getFontMetrics()
/*     */   {
/* 102 */     if (this.strikeMetrics == null) {
/* 103 */       this.strikeMetrics = this.physicalFont.getFontMetrics(this.pScalerContext);
/*     */     }
/*     */ 
/* 106 */     return this.strikeMetrics;
/*     */   }
/*     */ 
/*     */   float getCodePointAdvance(int paramInt) {
/* 110 */     return getGlyphAdvance(this.physicalFont.getMapper().charToGlyph(paramInt));
/*     */   }
/*     */ 
/*     */   Point2D.Float getCharMetrics(char paramChar) {
/* 114 */     return getGlyphMetrics(this.physicalFont.getMapper().charToGlyph(paramChar));
/*     */   }
/*     */ 
/*     */   int getSlot0GlyphImagePtrs(int[] paramArrayOfInt, long[] paramArrayOfLong, int paramInt) {
/* 118 */     return 0;
/*     */   }
/*     */ 
/*     */   Point2D.Float getGlyphPoint(int paramInt1, int paramInt2)
/*     */   {
/* 124 */     Point2D.Float localFloat = null;
/* 125 */     Integer localInteger = Integer.valueOf(paramInt1 << 16 | paramInt2);
/* 126 */     if (this.glyphPointMapCache == null) {
/* 127 */       synchronized (this) {
/* 128 */         if (this.glyphPointMapCache == null) {
/* 129 */           this.glyphPointMapCache = new ConcurrentHashMap();
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 134 */       localFloat = (Point2D.Float)this.glyphPointMapCache.get(localInteger);
/*     */     }
/*     */ 
/* 137 */     if (localFloat == null) {
/* 138 */       localFloat = this.physicalFont.getGlyphPoint(this.pScalerContext, paramInt1, paramInt2);
/* 139 */       adjustPoint(localFloat);
/* 140 */       this.glyphPointMapCache.put(localInteger, localFloat);
/*     */     }
/* 142 */     return localFloat;
/*     */   }
/*     */ 
/*     */   protected void adjustPoint(Point2D.Float paramFloat)
/*     */   {
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  41 */     switch (StrikeCache.nativeAddressSize) { case 8:
/*  42 */       longAddresses = true; break;
/*     */     case 4:
/*  43 */       longAddresses = false; break;
/*     */     default:
/*  44 */       throw new RuntimeException("Unexpected address size");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.PhysicalStrike
 * JD-Core Version:    0.6.2
 */