/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ 
/*     */ public class SpanClipRenderer
/*     */   implements CompositePipe
/*     */ {
/*     */   CompositePipe outpipe;
/*  42 */   static Class RegionClass = Region.class;
/*  43 */   static Class RegionIteratorClass = RegionIterator.class;
/*     */ 
/*     */   static native void initIDs(Class paramClass1, Class paramClass2);
/*     */ 
/*     */   public SpanClipRenderer(CompositePipe paramCompositePipe)
/*     */   {
/*  52 */     this.outpipe = paramCompositePipe;
/*     */   }
/*     */ 
/*     */   public Object startSequence(SunGraphics2D paramSunGraphics2D, Shape paramShape, Rectangle paramRectangle, int[] paramArrayOfInt)
/*     */   {
/*  70 */     RegionIterator localRegionIterator = paramSunGraphics2D.clipRegion.getIterator();
/*     */ 
/*  72 */     return new SCRcontext(localRegionIterator, this.outpipe.startSequence(paramSunGraphics2D, paramShape, paramRectangle, paramArrayOfInt));
/*     */   }
/*     */ 
/*     */   public boolean needTile(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  76 */     SCRcontext localSCRcontext = (SCRcontext)paramObject;
/*  77 */     return this.outpipe.needTile(localSCRcontext.outcontext, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void renderPathTile(Object paramObject, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, ShapeSpanIterator paramShapeSpanIterator)
/*     */   {
/*  84 */     renderPathTile(paramObject, paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void renderPathTile(Object paramObject, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*  90 */     SCRcontext localSCRcontext = (SCRcontext)paramObject;
/*  91 */     RegionIterator localRegionIterator = localSCRcontext.iterator.createCopy();
/*  92 */     int[] arrayOfInt = localSCRcontext.band;
/*  93 */     arrayOfInt[0] = paramInt3;
/*  94 */     arrayOfInt[1] = paramInt4;
/*  95 */     arrayOfInt[2] = (paramInt3 + paramInt5);
/*  96 */     arrayOfInt[3] = (paramInt4 + paramInt6);
/*  97 */     if (paramArrayOfByte == null) {
/*  98 */       int i = paramInt5 * paramInt6;
/*  99 */       paramArrayOfByte = localSCRcontext.tile;
/* 100 */       if ((paramArrayOfByte != null) && (paramArrayOfByte.length < i)) {
/* 101 */         paramArrayOfByte = null;
/*     */       }
/* 103 */       if (paramArrayOfByte == null) {
/* 104 */         paramArrayOfByte = new byte[i];
/* 105 */         localSCRcontext.tile = paramArrayOfByte;
/*     */       }
/* 107 */       paramInt1 = 0;
/* 108 */       paramInt2 = paramInt5;
/* 109 */       fillTile(localRegionIterator, paramArrayOfByte, paramInt1, paramInt2, arrayOfInt);
/*     */     } else {
/* 111 */       eraseTile(localRegionIterator, paramArrayOfByte, paramInt1, paramInt2, arrayOfInt);
/*     */     }
/*     */ 
/* 114 */     if ((arrayOfInt[2] > arrayOfInt[0]) && (arrayOfInt[3] > arrayOfInt[1])) {
/* 115 */       paramInt1 += (arrayOfInt[1] - paramInt4) * paramInt2 + (arrayOfInt[0] - paramInt3);
/* 116 */       this.outpipe.renderPathTile(localSCRcontext.outcontext, paramArrayOfByte, paramInt1, paramInt2, arrayOfInt[0], arrayOfInt[1], arrayOfInt[2] - arrayOfInt[0], arrayOfInt[3] - arrayOfInt[1]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public native void fillTile(RegionIterator paramRegionIterator, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt);
/*     */ 
/*     */   public native void eraseTile(RegionIterator paramRegionIterator, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt);
/*     */ 
/*     */   public void skipTile(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/* 133 */     SCRcontext localSCRcontext = (SCRcontext)paramObject;
/* 134 */     this.outpipe.skipTile(localSCRcontext.outcontext, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void endSequence(Object paramObject) {
/* 138 */     SCRcontext localSCRcontext = (SCRcontext)paramObject;
/* 139 */     this.outpipe.endSequence(localSCRcontext.outcontext);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  46 */     initIDs(RegionClass, RegionIteratorClass);
/*     */   }
/*     */ 
/*     */   class SCRcontext
/*     */   {
/*     */     RegionIterator iterator;
/*     */     Object outcontext;
/*     */     int[] band;
/*     */     byte[] tile;
/*     */ 
/*     */     public SCRcontext(RegionIterator paramObject, Object arg3)
/*     */     {
/*  62 */       this.iterator = paramObject;
/*     */       Object localObject;
/*  63 */       this.outcontext = localObject;
/*  64 */       this.band = new int[4];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.SpanClipRenderer
 * JD-Core Version:    0.6.2
 */