/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.FontFormatException;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ 
/*     */ public class NativeFont extends PhysicalFont
/*     */ {
/*     */   public NativeFont(String paramString, boolean paramBoolean)
/*     */     throws FontFormatException
/*     */   {
/*  49 */     throw new FontFormatException("NativeFont not used on Windows");
/*     */   }
/*     */ 
/*     */   static boolean hasExternalBitmaps(String paramString) {
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   public CharToGlyphMapper getMapper() {
/*  57 */     return null;
/*     */   }
/*     */ 
/*     */   PhysicalFont getDelegateFont() {
/*  61 */     return null;
/*     */   }
/*     */ 
/*     */   FontStrike createStrike(FontStrikeDesc paramFontStrikeDesc) {
/*  65 */     return null;
/*     */   }
/*     */ 
/*     */   public Rectangle2D getMaxCharBounds(FontRenderContext paramFontRenderContext) {
/*  69 */     return null;
/*     */   }
/*     */ 
/*     */   StrikeMetrics getFontMetrics(long paramLong) {
/*  73 */     return null;
/*     */   }
/*     */ 
/*     */   public GeneralPath getGlyphOutline(long paramLong, int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */   public GeneralPath getGlyphVectorOutline(long paramLong, int[] paramArrayOfInt, int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */   long getGlyphImage(long paramLong, int paramInt)
/*     */   {
/*  90 */     return 0L;
/*     */   }
/*     */ 
/*     */   void getGlyphMetrics(long paramLong, int paramInt, Point2D.Float paramFloat)
/*     */   {
/*     */   }
/*     */ 
/*     */   float getGlyphAdvance(long paramLong, int paramInt)
/*     */   {
/* 100 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   Rectangle2D.Float getGlyphOutlineBounds(long paramLong, int paramInt)
/*     */   {
/* 105 */     return new Rectangle2D.Float(0.0F, 0.0F, 0.0F, 0.0F);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.NativeFont
 * JD-Core Version:    0.6.2
 */