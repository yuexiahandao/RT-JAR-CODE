/*     */ package java.awt.print;
/*     */ 
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Double;
/*     */ 
/*     */ public class Paper
/*     */   implements Cloneable
/*     */ {
/*     */   private static final int INCH = 72;
/*     */   private static final double LETTER_WIDTH = 612.0D;
/*     */   private static final double LETTER_HEIGHT = 792.0D;
/*     */   private double mHeight;
/*     */   private double mWidth;
/*     */   private Rectangle2D mImageableArea;
/*     */ 
/*     */   public Paper()
/*     */   {
/*  88 */     this.mHeight = 792.0D;
/*  89 */     this.mWidth = 612.0D;
/*  90 */     this.mImageableArea = new Rectangle2D.Double(72.0D, 72.0D, this.mWidth - 144.0D, this.mHeight - 144.0D);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     Paper localPaper;
/*     */     try
/*     */     {
/* 111 */       localPaper = (Paper)super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 114 */       localCloneNotSupportedException.printStackTrace();
/* 115 */       localPaper = null;
/*     */     }
/*     */ 
/* 118 */     return localPaper;
/*     */   }
/*     */ 
/*     */   public double getHeight()
/*     */   {
/* 127 */     return this.mHeight;
/*     */   }
/*     */ 
/*     */   public void setSize(double paramDouble1, double paramDouble2)
/*     */   {
/* 142 */     this.mWidth = paramDouble1;
/* 143 */     this.mHeight = paramDouble2;
/*     */   }
/*     */ 
/*     */   public double getWidth()
/*     */   {
/* 153 */     return this.mWidth;
/*     */   }
/*     */ 
/*     */   public void setImageableArea(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
/*     */   {
/* 171 */     this.mImageableArea = new Rectangle2D.Double(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
/*     */   }
/*     */ 
/*     */   public double getImageableX()
/*     */   {
/* 180 */     return this.mImageableArea.getX();
/*     */   }
/*     */ 
/*     */   public double getImageableY()
/*     */   {
/* 189 */     return this.mImageableArea.getY();
/*     */   }
/*     */ 
/*     */   public double getImageableWidth()
/*     */   {
/* 198 */     return this.mImageableArea.getWidth();
/*     */   }
/*     */ 
/*     */   public double getImageableHeight()
/*     */   {
/* 207 */     return this.mImageableArea.getHeight();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.print.Paper
 * JD-Core Version:    0.6.2
 */