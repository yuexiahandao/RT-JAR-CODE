/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.geom.Dimension2D;
/*     */ import java.beans.Transient;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Dimension extends Dimension2D
/*     */   implements Serializable
/*     */ {
/*     */   public int width;
/*     */   public int height;
/*     */   private static final long serialVersionUID = 4723952579491349524L;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public Dimension()
/*     */   {
/*  99 */     this(0, 0);
/*     */   }
/*     */ 
/*     */   public Dimension(Dimension paramDimension)
/*     */   {
/* 111 */     this(paramDimension.width, paramDimension.height);
/*     */   }
/*     */ 
/*     */   public Dimension(int paramInt1, int paramInt2)
/*     */   {
/* 122 */     this.width = paramInt1;
/* 123 */     this.height = paramInt2;
/*     */   }
/*     */ 
/*     */   public double getWidth()
/*     */   {
/* 131 */     return this.width;
/*     */   }
/*     */ 
/*     */   public double getHeight()
/*     */   {
/* 139 */     return this.height;
/*     */   }
/*     */ 
/*     */   public void setSize(double paramDouble1, double paramDouble2)
/*     */   {
/* 154 */     this.width = ((int)Math.ceil(paramDouble1));
/* 155 */     this.height = ((int)Math.ceil(paramDouble2));
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public Dimension getSize()
/*     */   {
/* 171 */     return new Dimension(this.width, this.height);
/*     */   }
/*     */ 
/*     */   public void setSize(Dimension paramDimension)
/*     */   {
/* 184 */     setSize(paramDimension.width, paramDimension.height);
/*     */   }
/*     */ 
/*     */   public void setSize(int paramInt1, int paramInt2)
/*     */   {
/* 200 */     this.width = paramInt1;
/* 201 */     this.height = paramInt2;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 208 */     if ((paramObject instanceof Dimension)) {
/* 209 */       Dimension localDimension = (Dimension)paramObject;
/* 210 */       return (this.width == localDimension.width) && (this.height == localDimension.height);
/*     */     }
/* 212 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 221 */     int i = this.width + this.height;
/* 222 */     return i * (i + 1) / 2 + this.width;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 237 */     return getClass().getName() + "[width=" + this.width + ",height=" + this.height + "]";
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  88 */     Toolkit.loadLibraries();
/*  89 */     if (!GraphicsEnvironment.isHeadless())
/*  90 */       initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Dimension
 * JD-Core Version:    0.6.2
 */