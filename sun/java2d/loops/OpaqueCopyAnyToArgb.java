/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Composite;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import sun.awt.image.IntegerComponentRaster;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.java2d.pipe.SpanIterator;
/*     */ 
/*     */ class OpaqueCopyAnyToArgb extends Blit
/*     */ {
/*     */   OpaqueCopyAnyToArgb()
/*     */   {
/* 111 */     super(SurfaceType.Any, CompositeType.SrcNoEa, SurfaceType.IntArgb);
/*     */   }
/*     */ 
/*     */   public void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 120 */     Raster localRaster1 = paramSurfaceData1.getRaster(paramInt1, paramInt2, paramInt5, paramInt6);
/* 121 */     ColorModel localColorModel = paramSurfaceData1.getColorModel();
/*     */ 
/* 123 */     Raster localRaster2 = paramSurfaceData2.getRaster(paramInt3, paramInt4, paramInt5, paramInt6);
/* 124 */     IntegerComponentRaster localIntegerComponentRaster = (IntegerComponentRaster)localRaster2;
/* 125 */     int[] arrayOfInt1 = localIntegerComponentRaster.getDataStorage();
/*     */ 
/* 127 */     Region localRegion = CustomComponent.getRegionOfInterest(paramSurfaceData1, paramSurfaceData2, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 130 */     SpanIterator localSpanIterator = localRegion.getSpanIterator();
/*     */ 
/* 132 */     Object localObject = null;
/*     */ 
/* 134 */     int i = localIntegerComponentRaster.getScanlineStride();
/*     */ 
/* 136 */     paramInt1 -= paramInt3;
/* 137 */     paramInt2 -= paramInt4;
/* 138 */     int[] arrayOfInt2 = new int[4];
/* 139 */     while (localSpanIterator.nextSpan(arrayOfInt2)) {
/* 140 */       int j = localIntegerComponentRaster.getDataOffset(0) + arrayOfInt2[1] * i + arrayOfInt2[0];
/* 141 */       for (int k = arrayOfInt2[1]; k < arrayOfInt2[3]; k++) {
/* 142 */         int m = j;
/* 143 */         for (int n = arrayOfInt2[0]; n < arrayOfInt2[2]; n++) {
/* 144 */           localObject = localRaster1.getDataElements(n + paramInt1, k + paramInt2, localObject);
/* 145 */           arrayOfInt1[(m++)] = localColorModel.getRGB(localObject);
/*     */         }
/* 147 */         j += i;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 152 */     localIntegerComponentRaster.markDirty();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.OpaqueCopyAnyToArgb
 * JD-Core Version:    0.6.2
 */