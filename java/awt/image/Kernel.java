/*     */ package java.awt.image;
/*     */ 
/*     */ public class Kernel
/*     */   implements Cloneable
/*     */ {
/*     */   private int width;
/*     */   private int height;
/*     */   private int xOrigin;
/*     */   private int yOrigin;
/*     */   private float[] data;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public Kernel(int paramInt1, int paramInt2, float[] paramArrayOfFloat)
/*     */   {
/*  67 */     this.width = paramInt1;
/*  68 */     this.height = paramInt2;
/*  69 */     this.xOrigin = (paramInt1 - 1 >> 1);
/*  70 */     this.yOrigin = (paramInt2 - 1 >> 1);
/*  71 */     int i = paramInt1 * paramInt2;
/*  72 */     if (paramArrayOfFloat.length < i) {
/*  73 */       throw new IllegalArgumentException("Data array too small (is " + paramArrayOfFloat.length + " and should be " + i);
/*     */     }
/*     */ 
/*  77 */     this.data = new float[i];
/*  78 */     System.arraycopy(paramArrayOfFloat, 0, this.data, 0, i);
/*     */   }
/*     */ 
/*     */   public final int getXOrigin()
/*     */   {
/*  87 */     return this.xOrigin;
/*     */   }
/*     */ 
/*     */   public final int getYOrigin()
/*     */   {
/*  95 */     return this.yOrigin;
/*     */   }
/*     */ 
/*     */   public final int getWidth()
/*     */   {
/* 103 */     return this.width;
/*     */   }
/*     */ 
/*     */   public final int getHeight()
/*     */   {
/* 111 */     return this.height;
/*     */   }
/*     */ 
/*     */   public final float[] getKernelData(float[] paramArrayOfFloat)
/*     */   {
/* 127 */     if (paramArrayOfFloat == null) {
/* 128 */       paramArrayOfFloat = new float[this.data.length];
/*     */     }
/* 130 */     else if (paramArrayOfFloat.length < this.data.length) {
/* 131 */       throw new IllegalArgumentException("Data array too small (should be " + this.data.length + " but is " + paramArrayOfFloat.length + " )");
/*     */     }
/*     */ 
/* 136 */     System.arraycopy(this.data, 0, paramArrayOfFloat, 0, this.data.length);
/*     */ 
/* 138 */     return paramArrayOfFloat;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 147 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 150 */     throw new InternalError();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  48 */     ColorModel.loadLibraries();
/*  49 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.Kernel
 * JD-Core Version:    0.6.2
 */