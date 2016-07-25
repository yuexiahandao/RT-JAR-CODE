/*    */ package sun.java2d.pipe;
/*    */ 
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.Shape;
/*    */ import sun.font.GlyphList;
/*    */ import sun.java2d.SunGraphics2D;
/*    */ 
/*    */ public class TextRenderer extends GlyphListPipe
/*    */ {
/*    */   CompositePipe outpipe;
/*    */ 
/*    */   public TextRenderer(CompositePipe paramCompositePipe)
/*    */   {
/* 45 */     this.outpipe = paramCompositePipe;
/*    */   }
/*    */ 
/*    */   protected void drawGlyphList(SunGraphics2D paramSunGraphics2D, GlyphList paramGlyphList) {
/* 49 */     int i = paramGlyphList.getNumGlyphs();
/* 50 */     Region localRegion = paramSunGraphics2D.getCompClip();
/* 51 */     int j = localRegion.getLoX();
/* 52 */     int k = localRegion.getLoY();
/* 53 */     int m = localRegion.getHiX();
/* 54 */     int n = localRegion.getHiY();
/* 55 */     Object localObject1 = null;
/*    */     try {
/* 57 */       int[] arrayOfInt1 = paramGlyphList.getBounds();
/* 58 */       Rectangle localRectangle = new Rectangle(arrayOfInt1[0], arrayOfInt1[1], arrayOfInt1[2] - arrayOfInt1[0], arrayOfInt1[3] - arrayOfInt1[1]);
/*    */ 
/* 61 */       Shape localShape = paramSunGraphics2D.untransformShape(localRectangle);
/* 62 */       localObject1 = this.outpipe.startSequence(paramSunGraphics2D, localShape, localRectangle, arrayOfInt1);
/* 63 */       for (int i1 = 0; i1 < i; i1++) {
/* 64 */         paramGlyphList.setGlyphIndex(i1);
/* 65 */         int[] arrayOfInt2 = paramGlyphList.getMetrics();
/* 66 */         int i2 = arrayOfInt2[0];
/* 67 */         int i3 = arrayOfInt2[1];
/* 68 */         int i4 = arrayOfInt2[2];
/* 69 */         int i5 = i2 + i4;
/* 70 */         int i6 = i3 + arrayOfInt2[3];
/* 71 */         int i7 = 0;
/* 72 */         if (i2 < j) {
/* 73 */           i7 = j - i2;
/* 74 */           i2 = j;
/*    */         }
/* 76 */         if (i3 < k) {
/* 77 */           i7 += (k - i3) * i4;
/* 78 */           i3 = k;
/*    */         }
/* 80 */         if (i5 > m) i5 = m;
/* 81 */         if (i6 > n) i6 = n;
/* 82 */         if ((i5 > i2) && (i6 > i3) && (this.outpipe.needTile(localObject1, i2, i3, i5 - i2, i6 - i3)))
/*    */         {
/* 85 */           byte[] arrayOfByte = paramGlyphList.getGrayBits();
/* 86 */           this.outpipe.renderPathTile(localObject1, arrayOfByte, i7, i4, i2, i3, i5 - i2, i6 - i3);
/*    */         }
/*    */         else {
/* 89 */           this.outpipe.skipTile(localObject1, i2, i3);
/*    */         }
/*    */       }
/*    */     } finally {
/* 93 */       if (localObject1 != null)
/* 94 */         this.outpipe.endSequence(localObject1);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.TextRenderer
 * JD-Core Version:    0.6.2
 */