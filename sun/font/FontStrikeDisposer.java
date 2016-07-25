/*    */ package sun.font;
/*    */ 
/*    */ import sun.java2d.Disposer.PollDisposable;
/*    */ import sun.java2d.DisposerRecord;
/*    */ 
/*    */ class FontStrikeDisposer
/*    */   implements DisposerRecord, Disposer.PollDisposable
/*    */ {
/*    */   Font2D font2D;
/*    */   FontStrikeDesc desc;
/*    */   long[] longGlyphImages;
/*    */   int[] intGlyphImages;
/*    */   int[][] segIntGlyphImages;
/*    */   long[][] segLongGlyphImages;
/* 62 */   long pScalerContext = 0L;
/* 63 */   boolean disposed = false;
/* 64 */   boolean comp = false;
/*    */ 
/*    */   public FontStrikeDisposer(Font2D paramFont2D, FontStrikeDesc paramFontStrikeDesc, long paramLong, int[] paramArrayOfInt)
/*    */   {
/* 68 */     this.font2D = paramFont2D;
/* 69 */     this.desc = paramFontStrikeDesc;
/* 70 */     this.pScalerContext = paramLong;
/* 71 */     this.intGlyphImages = paramArrayOfInt;
/*    */   }
/*    */ 
/*    */   public FontStrikeDisposer(Font2D paramFont2D, FontStrikeDesc paramFontStrikeDesc, long paramLong, long[] paramArrayOfLong)
/*    */   {
/* 76 */     this.font2D = paramFont2D;
/* 77 */     this.desc = paramFontStrikeDesc;
/* 78 */     this.pScalerContext = paramLong;
/* 79 */     this.longGlyphImages = paramArrayOfLong;
/*    */   }
/*    */ 
/*    */   public FontStrikeDisposer(Font2D paramFont2D, FontStrikeDesc paramFontStrikeDesc, long paramLong)
/*    */   {
/* 84 */     this.font2D = paramFont2D;
/* 85 */     this.desc = paramFontStrikeDesc;
/* 86 */     this.pScalerContext = paramLong;
/*    */   }
/*    */ 
/*    */   public FontStrikeDisposer(Font2D paramFont2D, FontStrikeDesc paramFontStrikeDesc) {
/* 90 */     this.font2D = paramFont2D;
/* 91 */     this.desc = paramFontStrikeDesc;
/* 92 */     this.comp = true;
/*    */   }
/*    */ 
/*    */   public synchronized void dispose() {
/* 96 */     if (!this.disposed) {
/* 97 */       this.font2D.removeFromCache(this.desc);
/* 98 */       StrikeCache.disposeStrike(this);
/* 99 */       this.disposed = true;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FontStrikeDisposer
 * JD-Core Version:    0.6.2
 */