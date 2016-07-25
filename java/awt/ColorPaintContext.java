/*    */ package java.awt;
/*    */ 
/*    */ import java.awt.image.ColorModel;
/*    */ import java.awt.image.Raster;
/*    */ import java.awt.image.WritableRaster;
/*    */ import java.util.Arrays;
/*    */ import sun.awt.image.IntegerComponentRaster;
/*    */ 
/*    */ class ColorPaintContext
/*    */   implements PaintContext
/*    */ {
/*    */   int color;
/*    */   WritableRaster savedTile;
/*    */ 
/*    */   protected ColorPaintContext(int paramInt, ColorModel paramColorModel)
/*    */   {
/* 41 */     this.color = paramInt;
/*    */   }
/*    */ 
/*    */   public void dispose()
/*    */   {
/*    */   }
/*    */ 
/*    */   int getRGB()
/*    */   {
/* 60 */     return this.color;
/*    */   }
/*    */ 
/*    */   public ColorModel getColorModel() {
/* 64 */     return ColorModel.getRGBdefault();
/*    */   }
/*    */ 
/*    */   public synchronized Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 68 */     WritableRaster localWritableRaster = this.savedTile;
/*    */ 
/* 70 */     if ((localWritableRaster == null) || (paramInt3 > localWritableRaster.getWidth()) || (paramInt4 > localWritableRaster.getHeight())) {
/* 71 */       localWritableRaster = getColorModel().createCompatibleWritableRaster(paramInt3, paramInt4);
/* 72 */       IntegerComponentRaster localIntegerComponentRaster = (IntegerComponentRaster)localWritableRaster;
/* 73 */       Arrays.fill(localIntegerComponentRaster.getDataStorage(), this.color);
/*    */ 
/* 75 */       localIntegerComponentRaster.markDirty();
/* 76 */       if ((paramInt3 <= 64) && (paramInt4 <= 64)) {
/* 77 */         this.savedTile = localWritableRaster;
/*    */       }
/*    */     }
/*    */ 
/* 81 */     return localWritableRaster;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.ColorPaintContext
 * JD-Core Version:    0.6.2
 */