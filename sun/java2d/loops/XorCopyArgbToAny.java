/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.WritableRaster;
/*     */ import sun.awt.image.IntegerComponentRaster;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ import sun.java2d.pipe.SpanIterator;
/*     */ 
/*     */ class XorCopyArgbToAny extends Blit
/*     */ {
/*     */   XorCopyArgbToAny()
/*     */   {
/* 221 */     super(SurfaceType.IntArgb, CompositeType.Xor, SurfaceType.Any);
/*     */   }
/*     */ 
/*     */   public void Blit(SurfaceData paramSurfaceData1, SurfaceData paramSurfaceData2, Composite paramComposite, Region paramRegion, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 230 */     Raster localRaster = paramSurfaceData1.getRaster(paramInt1, paramInt2, paramInt5, paramInt6);
/* 231 */     IntegerComponentRaster localIntegerComponentRaster = (IntegerComponentRaster)localRaster;
/* 232 */     int[] arrayOfInt1 = localIntegerComponentRaster.getDataStorage();
/*     */ 
/* 234 */     WritableRaster localWritableRaster = (WritableRaster)paramSurfaceData2.getRaster(paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 236 */     ColorModel localColorModel = paramSurfaceData2.getColorModel();
/*     */ 
/* 238 */     Region localRegion = CustomComponent.getRegionOfInterest(paramSurfaceData1, paramSurfaceData2, paramRegion, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */ 
/* 241 */     SpanIterator localSpanIterator = localRegion.getSpanIterator();
/*     */ 
/* 243 */     int i = ((XORComposite)paramComposite).getXorColor().getRGB();
/* 244 */     Object localObject1 = localColorModel.getDataElements(i, null);
/*     */ 
/* 246 */     Object localObject2 = null;
/* 247 */     Object localObject3 = null;
/*     */ 
/* 249 */     int j = localIntegerComponentRaster.getScanlineStride();
/*     */ 
/* 251 */     paramInt1 -= paramInt3;
/* 252 */     paramInt2 -= paramInt4;
/* 253 */     int[] arrayOfInt2 = new int[4];
/* 254 */     while (localSpanIterator.nextSpan(arrayOfInt2)) {
/* 255 */       int k = localIntegerComponentRaster.getDataOffset(0) + (paramInt2 + arrayOfInt2[1]) * j + (paramInt1 + arrayOfInt2[0]);
/*     */ 
/* 258 */       for (int m = arrayOfInt2[1]; m < arrayOfInt2[3]; m++) {
/* 259 */         int n = k;
/* 260 */         for (int i1 = arrayOfInt2[0]; i1 < arrayOfInt2[2]; i1++)
/*     */         {
/* 264 */           localObject2 = localColorModel.getDataElements(arrayOfInt1[(n++)], localObject2);
/* 265 */           localObject3 = localWritableRaster.getDataElements(i1, m, localObject3);
/*     */ 
/* 267 */           switch (localColorModel.getTransferType()) {
/*     */           case 0:
/* 269 */             byte[] arrayOfByte1 = (byte[])localObject2;
/* 270 */             byte[] arrayOfByte2 = (byte[])localObject3;
/* 271 */             byte[] arrayOfByte3 = (byte[])localObject1;
/* 272 */             for (int i2 = 0; i2 < arrayOfByte2.length; i2++)
/*     */             {
/*     */               int tmp325_323 = i2;
/*     */               byte[] tmp325_321 = arrayOfByte2; tmp325_321[tmp325_323] = ((byte)(tmp325_321[tmp325_323] ^ (arrayOfByte1[i2] ^ arrayOfByte3[i2])));
/*     */             }
/* 275 */             break;
/*     */           case 1:
/*     */           case 2:
/* 278 */             short[] arrayOfShort1 = (short[])localObject2;
/* 279 */             short[] arrayOfShort2 = (short[])localObject3;
/* 280 */             short[] arrayOfShort3 = (short[])localObject1;
/* 281 */             for (int i3 = 0; i3 < arrayOfShort2.length; i3++)
/*     */             {
/*     */               int tmp395_393 = i3;
/*     */               short[] tmp395_391 = arrayOfShort2; tmp395_391[tmp395_393] = ((short)(tmp395_391[tmp395_393] ^ (arrayOfShort1[i3] ^ arrayOfShort3[i3])));
/*     */             }
/* 284 */             break;
/*     */           case 3:
/* 286 */             int[] arrayOfInt3 = (int[])localObject2;
/* 287 */             int[] arrayOfInt4 = (int[])localObject3;
/* 288 */             int[] arrayOfInt5 = (int[])localObject1;
/* 289 */             for (int i4 = 0; i4 < arrayOfInt4.length; i4++) {
/* 290 */               arrayOfInt4[i4] ^= arrayOfInt3[i4] ^ arrayOfInt5[i4];
/*     */             }
/* 292 */             break;
/*     */           case 4:
/* 294 */             float[] arrayOfFloat1 = (float[])localObject2;
/* 295 */             float[] arrayOfFloat2 = (float[])localObject3;
/* 296 */             float[] arrayOfFloat3 = (float[])localObject1;
/* 297 */             for (int i5 = 0; i5 < arrayOfFloat2.length; i5++) {
/* 298 */               int i6 = Float.floatToIntBits(arrayOfFloat2[i5]) ^ Float.floatToIntBits(arrayOfFloat1[i5]) ^ Float.floatToIntBits(arrayOfFloat3[i5]);
/*     */ 
/* 301 */               arrayOfFloat2[i5] = Float.intBitsToFloat(i6);
/*     */             }
/* 303 */             break;
/*     */           case 5:
/* 305 */             double[] arrayOfDouble1 = (double[])localObject2;
/* 306 */             double[] arrayOfDouble2 = (double[])localObject3;
/* 307 */             double[] arrayOfDouble3 = (double[])localObject1;
/* 308 */             for (int i7 = 0; i7 < arrayOfDouble2.length; i7++) {
/* 309 */               long l = Double.doubleToLongBits(arrayOfDouble2[i7]) ^ Double.doubleToLongBits(arrayOfDouble1[i7]) ^ Double.doubleToLongBits(arrayOfDouble3[i7]);
/*     */ 
/* 312 */               arrayOfDouble2[i7] = Double.longBitsToDouble(l);
/*     */             }
/* 314 */             break;
/*     */           default:
/* 316 */             throw new InternalError("Unsupported XOR pixel type");
/*     */           }
/* 318 */           localWritableRaster.setDataElements(i1, m, localObject3);
/*     */         }
/* 320 */         k += j;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.XorCopyArgbToAny
 * JD-Core Version:    0.6.2
 */