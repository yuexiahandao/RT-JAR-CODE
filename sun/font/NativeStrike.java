/*    */ package sun.font;
/*    */ 
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.geom.GeneralPath;
/*    */ import java.awt.geom.Point2D.Float;
/*    */ import java.awt.geom.Rectangle2D.Float;
/*    */ 
/*    */ public class NativeStrike extends PhysicalStrike
/*    */ {
/*    */   NativeFont nativeFont;
/*    */ 
/*    */   NativeStrike(NativeFont paramNativeFont, FontStrikeDesc paramFontStrikeDesc)
/*    */   {
/* 38 */     super(paramNativeFont, paramFontStrikeDesc);
/*    */ 
/* 40 */     throw new RuntimeException("NativeFont not used on Windows");
/*    */   }
/*    */ 
/*    */   NativeStrike(NativeFont paramNativeFont, FontStrikeDesc paramFontStrikeDesc, boolean paramBoolean)
/*    */   {
/* 45 */     super(paramNativeFont, paramFontStrikeDesc);
/*    */ 
/* 47 */     throw new RuntimeException("NativeFont not used on Windows");
/*    */   }
/*    */ 
/*    */   void getGlyphImagePtrs(int[] paramArrayOfInt, long[] paramArrayOfLong, int paramInt)
/*    */   {
/*    */   }
/*    */ 
/*    */   long getGlyphImagePtr(int paramInt) {
/* 55 */     return 0L;
/*    */   }
/*    */ 
/*    */   long getGlyphImagePtrNoCache(int paramInt) {
/* 59 */     return 0L;
/*    */   }
/*    */ 
/*    */   void getGlyphImageBounds(int paramInt, Point2D.Float paramFloat, Rectangle paramRectangle)
/*    */   {
/*    */   }
/*    */ 
/*    */   Point2D.Float getGlyphMetrics(int paramInt)
/*    */   {
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   float getGlyphAdvance(int paramInt) {
/* 72 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   Rectangle2D.Float getGlyphOutlineBounds(int paramInt) {
/* 76 */     return null;
/*    */   }
/*    */   GeneralPath getGlyphOutline(int paramInt, float paramFloat1, float paramFloat2) {
/* 79 */     return null;
/*    */   }
/*    */ 
/*    */   GeneralPath getGlyphVectorOutline(int[] paramArrayOfInt, float paramFloat1, float paramFloat2) {
/* 83 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.NativeStrike
 * JD-Core Version:    0.6.2
 */