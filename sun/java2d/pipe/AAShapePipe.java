/*     */ package sun.java2d.pipe;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Double;
/*     */ import sun.java2d.SunGraphics2D;
/*     */ 
/*     */ public class AAShapePipe
/*     */   implements ShapeDrawPipe, ParallelogramPipe
/*     */ {
/*  46 */   static RenderingEngine renderengine = RenderingEngine.getInstance();
/*     */   CompositePipe outpipe;
/*     */   private static byte[] theTile;
/*     */ 
/*     */   public AAShapePipe(CompositePipe paramCompositePipe)
/*     */   {
/*  51 */     this.outpipe = paramCompositePipe;
/*     */   }
/*     */ 
/*     */   public void draw(SunGraphics2D paramSunGraphics2D, Shape paramShape)
/*     */   {
/*     */     BasicStroke localBasicStroke;
/*  57 */     if ((paramSunGraphics2D.stroke instanceof BasicStroke)) {
/*  58 */       localBasicStroke = (BasicStroke)paramSunGraphics2D.stroke;
/*     */     } else {
/*  60 */       paramShape = paramSunGraphics2D.stroke.createStrokedShape(paramShape);
/*  61 */       localBasicStroke = null;
/*     */     }
/*     */ 
/*  64 */     renderPath(paramSunGraphics2D, paramShape, localBasicStroke);
/*     */   }
/*     */ 
/*     */   public void fill(SunGraphics2D paramSunGraphics2D, Shape paramShape) {
/*  68 */     renderPath(paramSunGraphics2D, paramShape, null);
/*     */   }
/*     */ 
/*     */   private static Rectangle2D computeBBox(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/*  74 */     if (paramDouble3 -= paramDouble1 < 0.0D) {
/*  75 */       paramDouble1 += paramDouble3;
/*  76 */       paramDouble3 = -paramDouble3;
/*     */     }
/*  78 */     if (paramDouble4 -= paramDouble2 < 0.0D) {
/*  79 */       paramDouble2 += paramDouble4;
/*  80 */       paramDouble4 = -paramDouble4;
/*     */     }
/*  82 */     return new Rectangle2D.Double(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */   }
/*     */ 
/*     */   public void fillParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10)
/*     */   {
/*  92 */     Region localRegion = paramSunGraphics2D.getCompClip();
/*  93 */     int[] arrayOfInt = new int[4];
/*  94 */     AATileGenerator localAATileGenerator = renderengine.getAATileGenerator(paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, 0.0D, 0.0D, localRegion, arrayOfInt);
/*     */ 
/*  97 */     if (localAATileGenerator == null)
/*     */     {
/*  99 */       return;
/*     */     }
/*     */ 
/* 102 */     renderTiles(paramSunGraphics2D, computeBBox(paramDouble1, paramDouble2, paramDouble3, paramDouble4), localAATileGenerator, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public void drawParallelogram(SunGraphics2D paramSunGraphics2D, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8, double paramDouble9, double paramDouble10, double paramDouble11, double paramDouble12)
/*     */   {
/* 113 */     Region localRegion = paramSunGraphics2D.getCompClip();
/* 114 */     int[] arrayOfInt = new int[4];
/* 115 */     AATileGenerator localAATileGenerator = renderengine.getAATileGenerator(paramDouble5, paramDouble6, paramDouble7, paramDouble8, paramDouble9, paramDouble10, paramDouble11, paramDouble12, localRegion, arrayOfInt);
/*     */ 
/* 118 */     if (localAATileGenerator == null)
/*     */     {
/* 120 */       return;
/*     */     }
/*     */ 
/* 125 */     renderTiles(paramSunGraphics2D, computeBBox(paramDouble1, paramDouble2, paramDouble3, paramDouble4), localAATileGenerator, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public static synchronized byte[] getAlphaTile(int paramInt)
/*     */   {
/* 131 */     byte[] arrayOfByte = theTile;
/* 132 */     if ((arrayOfByte == null) || (arrayOfByte.length < paramInt))
/* 133 */       arrayOfByte = new byte[paramInt];
/*     */     else {
/* 135 */       theTile = null;
/*     */     }
/* 137 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static synchronized void dropAlphaTile(byte[] paramArrayOfByte) {
/* 141 */     theTile = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public void renderPath(SunGraphics2D paramSunGraphics2D, Shape paramShape, BasicStroke paramBasicStroke) {
/* 145 */     boolean bool1 = (paramBasicStroke != null) && (paramSunGraphics2D.strokeHint != 2);
/*     */ 
/* 147 */     boolean bool2 = paramSunGraphics2D.strokeState <= 1;
/*     */ 
/* 149 */     Region localRegion = paramSunGraphics2D.getCompClip();
/* 150 */     int[] arrayOfInt = new int[4];
/* 151 */     AATileGenerator localAATileGenerator = renderengine.getAATileGenerator(paramShape, paramSunGraphics2D.transform, localRegion, paramBasicStroke, bool2, bool1, arrayOfInt);
/*     */ 
/* 154 */     if (localAATileGenerator == null)
/*     */     {
/* 156 */       return;
/*     */     }
/*     */ 
/* 159 */     renderTiles(paramSunGraphics2D, paramShape, localAATileGenerator, arrayOfInt);
/*     */   }
/*     */ 
/*     */   public void renderTiles(SunGraphics2D paramSunGraphics2D, Shape paramShape, AATileGenerator paramAATileGenerator, int[] paramArrayOfInt)
/*     */   {
/* 165 */     Object localObject1 = null;
/* 166 */     byte[] arrayOfByte1 = null;
/*     */     try {
/* 168 */       localObject1 = this.outpipe.startSequence(paramSunGraphics2D, paramShape, new Rectangle(paramArrayOfInt[0], paramArrayOfInt[1], paramArrayOfInt[2] - paramArrayOfInt[0], paramArrayOfInt[3] - paramArrayOfInt[1]), paramArrayOfInt);
/*     */ 
/* 174 */       int i = paramAATileGenerator.getTileWidth();
/* 175 */       int j = paramAATileGenerator.getTileHeight();
/* 176 */       arrayOfByte1 = getAlphaTile(i * j);
/*     */ 
/* 180 */       for (int k = paramArrayOfInt[1]; k < paramArrayOfInt[3]; k += j)
/* 181 */         for (int m = paramArrayOfInt[0]; m < paramArrayOfInt[2]; m += i) {
/* 182 */           int n = Math.min(i, paramArrayOfInt[2] - m);
/* 183 */           int i1 = Math.min(j, paramArrayOfInt[3] - k);
/*     */ 
/* 185 */           int i2 = paramAATileGenerator.getTypicalAlpha();
/* 186 */           if ((i2 == 0) || (!this.outpipe.needTile(localObject1, m, k, n, i1)))
/*     */           {
/* 189 */             paramAATileGenerator.nextTile();
/* 190 */             this.outpipe.skipTile(localObject1, m, k);
/*     */           }
/*     */           else
/*     */           {
/*     */             byte[] arrayOfByte2;
/* 193 */             if (i2 == 255) {
/* 194 */               arrayOfByte2 = null;
/* 195 */               paramAATileGenerator.nextTile();
/*     */             } else {
/* 197 */               arrayOfByte2 = arrayOfByte1;
/* 198 */               paramAATileGenerator.getAlpha(arrayOfByte1, 0, i);
/*     */             }
/*     */ 
/* 201 */             this.outpipe.renderPathTile(localObject1, arrayOfByte2, 0, i, m, k, n, i1);
/*     */           }
/*     */         }
/*     */     }
/*     */     finally {
/* 206 */       paramAATileGenerator.dispose();
/* 207 */       if (localObject1 != null) {
/* 208 */         this.outpipe.endSequence(localObject1);
/*     */       }
/* 210 */       if (arrayOfByte1 != null)
/* 211 */         dropAlphaTile(arrayOfByte1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.AAShapePipe
 * JD-Core Version:    0.6.2
 */