/*     */ package sun.java2d.loops;
/*     */ 
/*     */ import sun.font.GlyphList;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ import sun.java2d.SurfaceData;
/*     */ import sun.java2d.pipe.Region;
/*     */ 
/*     */ public class DrawGlyphListAA extends GraphicsPrimitive
/*     */ {
/*  42 */   public static final String methodSignature = "DrawGlyphListAA(...)".toString();
/*     */ 
/*  44 */   public static final int primTypeID = makePrimTypeID();
/*     */ 
/*     */   public static DrawGlyphListAA locate(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  50 */     return (DrawGlyphListAA)GraphicsPrimitiveMgr.locate(primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   protected DrawGlyphListAA(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  59 */     super(methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public DrawGlyphListAA(long paramLong, SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  67 */     super(paramLong, methodSignature, primTypeID, paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public native void DrawGlyphListAA(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, GlyphList paramGlyphList);
/*     */ 
/*     */   public GraphicsPrimitive makePrimitive(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */   {
/*  81 */     return new General(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */   }
/*     */ 
/*     */   public GraphicsPrimitive traceWrap()
/*     */   {
/* 135 */     return new TraceDrawGlyphListAA(this);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  74 */     GraphicsPrimitiveMgr.registerGeneral(new DrawGlyphListAA(null, null, null));
/*     */   }
/*     */ 
/*     */   public static class General extends DrawGlyphListAA
/*     */   {
/*     */     MaskFill maskop;
/*     */ 
/*     */     public General(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*     */     {
/*  91 */       super(paramCompositeType, paramSurfaceType2);
/*  92 */       this.maskop = MaskFill.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*     */     }
/*     */ 
/*     */     public void DrawGlyphListAA(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, GlyphList paramGlyphList)
/*     */     {
/*  98 */       paramGlyphList.getBounds();
/*  99 */       int i = paramGlyphList.getNumGlyphs();
/* 100 */       Region localRegion = paramSunGraphics2D.getCompClip();
/* 101 */       int j = localRegion.getLoX();
/* 102 */       int k = localRegion.getLoY();
/* 103 */       int m = localRegion.getHiX();
/* 104 */       int n = localRegion.getHiY();
/* 105 */       for (int i1 = 0; i1 < i; i1++) {
/* 106 */         paramGlyphList.setGlyphIndex(i1);
/* 107 */         int[] arrayOfInt = paramGlyphList.getMetrics();
/* 108 */         int i2 = arrayOfInt[0];
/* 109 */         int i3 = arrayOfInt[1];
/* 110 */         int i4 = arrayOfInt[2];
/* 111 */         int i5 = i2 + i4;
/* 112 */         int i6 = i3 + arrayOfInt[3];
/* 113 */         int i7 = 0;
/* 114 */         if (i2 < j) {
/* 115 */           i7 = j - i2;
/* 116 */           i2 = j;
/*     */         }
/* 118 */         if (i3 < k) {
/* 119 */           i7 += (k - i3) * i4;
/* 120 */           i3 = k;
/*     */         }
/* 122 */         if (i5 > m) i5 = m;
/* 123 */         if (i6 > n) i6 = n;
/* 124 */         if ((i5 > i2) && (i6 > i3)) {
/* 125 */           byte[] arrayOfByte = paramGlyphList.getGrayBits();
/* 126 */           this.maskop.MaskFill(paramSunGraphics2D, paramSurfaceData, paramSunGraphics2D.composite, i2, i3, i5 - i2, i6 - i3, arrayOfByte, i7, i4);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class TraceDrawGlyphListAA extends DrawGlyphListAA
/*     */   {
/*     */     DrawGlyphListAA target;
/*     */ 
/*     */     public TraceDrawGlyphListAA(DrawGlyphListAA paramDrawGlyphListAA)
/*     */     {
/* 142 */       super(paramDrawGlyphListAA.getCompositeType(), paramDrawGlyphListAA.getDestType());
/*     */ 
/* 145 */       this.target = paramDrawGlyphListAA;
/*     */     }
/*     */ 
/*     */     public GraphicsPrimitive traceWrap() {
/* 149 */       return this;
/*     */     }
/*     */ 
/*     */     public void DrawGlyphListAA(SunGraphics2D paramSunGraphics2D, SurfaceData paramSurfaceData, GlyphList paramGlyphList)
/*     */     {
/* 155 */       tracePrimitive(this.target);
/* 156 */       this.target.DrawGlyphListAA(paramSunGraphics2D, paramSurfaceData, paramGlyphList);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.loops.DrawGlyphListAA
 * JD-Core Version:    0.6.2
 */