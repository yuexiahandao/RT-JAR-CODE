/*    */ package sun.java2d.pipe;
/*    */ 
/*    */ import sun.font.GlyphList;
/*    */ import sun.java2d.SunGraphics2D;
/*    */ import sun.java2d.loops.DrawGlyphList;
/*    */ import sun.java2d.loops.DrawGlyphListAA;
/*    */ import sun.java2d.loops.DrawGlyphListLCD;
/*    */ import sun.java2d.loops.RenderLoops;
/*    */ 
/*    */ public abstract class GlyphListLoopPipe extends GlyphListPipe
/*    */   implements LoopBasedPipe
/*    */ {
/*    */   protected void drawGlyphList(SunGraphics2D paramSunGraphics2D, GlyphList paramGlyphList, int paramInt)
/*    */   {
/* 44 */     switch (paramInt) {
/*    */     case 1:
/* 46 */       paramSunGraphics2D.loops.drawGlyphListLoop.DrawGlyphList(paramSunGraphics2D, paramSunGraphics2D.surfaceData, paramGlyphList);
/*    */ 
/* 48 */       return;
/*    */     case 2:
/* 50 */       paramSunGraphics2D.loops.drawGlyphListAALoop.DrawGlyphListAA(paramSunGraphics2D, paramSunGraphics2D.surfaceData, paramGlyphList);
/*    */ 
/* 52 */       return;
/*    */     case 4:
/*    */     case 6:
/* 55 */       paramSunGraphics2D.loops.drawGlyphListLCDLoop.DrawGlyphListLCD(paramSunGraphics2D, paramSunGraphics2D.surfaceData, paramGlyphList);
/*    */ 
/* 57 */       return;
/*    */     case 3:
/*    */     case 5:
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.GlyphListLoopPipe
 * JD-Core Version:    0.6.2
 */