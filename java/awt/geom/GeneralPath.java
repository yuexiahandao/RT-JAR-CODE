/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.awt.Shape;
/*     */ 
/*     */ public final class GeneralPath extends Path2D.Float
/*     */ {
/*     */   private static final long serialVersionUID = -8327096662768731142L;
/*     */ 
/*     */   public GeneralPath()
/*     */   {
/*  59 */     super(1, 20);
/*     */   }
/*     */ 
/*     */   public GeneralPath(int paramInt)
/*     */   {
/*  73 */     super(paramInt, 20);
/*     */   }
/*     */ 
/*     */   public GeneralPath(int paramInt1, int paramInt2)
/*     */   {
/*  92 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public GeneralPath(Shape paramShape)
/*     */   {
/* 105 */     super(paramShape, null);
/*     */   }
/*     */ 
/*     */   GeneralPath(int paramInt1, byte[] paramArrayOfByte, int paramInt2, float[] paramArrayOfFloat, int paramInt3)
/*     */   {
/* 116 */     this.windingRule = paramInt1;
/* 117 */     this.pointTypes = paramArrayOfByte;
/* 118 */     this.numTypes = paramInt2;
/* 119 */     this.floatCoords = paramArrayOfFloat;
/* 120 */     this.numCoords = paramInt3;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.GeneralPath
 * JD-Core Version:    0.6.2
 */