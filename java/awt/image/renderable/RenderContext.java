/*     */ package java.awt.image.renderable;
/*     */ 
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ 
/*     */ public class RenderContext
/*     */   implements Cloneable
/*     */ {
/*     */   RenderingHints hints;
/*     */   AffineTransform usr2dev;
/*     */   Shape aoi;
/*     */ 
/*     */   public RenderContext(AffineTransform paramAffineTransform, Shape paramShape, RenderingHints paramRenderingHints)
/*     */   {
/*  88 */     this.hints = paramRenderingHints;
/*  89 */     this.aoi = paramShape;
/*  90 */     this.usr2dev = ((AffineTransform)paramAffineTransform.clone());
/*     */   }
/*     */ 
/*     */   public RenderContext(AffineTransform paramAffineTransform)
/*     */   {
/* 101 */     this(paramAffineTransform, null, null);
/*     */   }
/*     */ 
/*     */   public RenderContext(AffineTransform paramAffineTransform, RenderingHints paramRenderingHints)
/*     */   {
/* 112 */     this(paramAffineTransform, null, paramRenderingHints);
/*     */   }
/*     */ 
/*     */   public RenderContext(AffineTransform paramAffineTransform, Shape paramShape)
/*     */   {
/* 124 */     this(paramAffineTransform, paramShape, null);
/*     */   }
/*     */ 
/*     */   public RenderingHints getRenderingHints()
/*     */   {
/* 134 */     return this.hints;
/*     */   }
/*     */ 
/*     */   public void setRenderingHints(RenderingHints paramRenderingHints)
/*     */   {
/* 144 */     this.hints = paramRenderingHints;
/*     */   }
/*     */ 
/*     */   public void setTransform(AffineTransform paramAffineTransform)
/*     */   {
/* 155 */     this.usr2dev = ((AffineTransform)paramAffineTransform.clone());
/*     */   }
/*     */ 
/*     */   public void preConcatenateTransform(AffineTransform paramAffineTransform)
/*     */   {
/* 170 */     preConcetenateTransform(paramAffineTransform);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void preConcetenateTransform(AffineTransform paramAffineTransform)
/*     */   {
/* 190 */     this.usr2dev.preConcatenate(paramAffineTransform);
/*     */   }
/*     */ 
/*     */   public void concatenateTransform(AffineTransform paramAffineTransform)
/*     */   {
/* 205 */     concetenateTransform(paramAffineTransform);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void concetenateTransform(AffineTransform paramAffineTransform)
/*     */   {
/* 225 */     this.usr2dev.concatenate(paramAffineTransform);
/*     */   }
/*     */ 
/*     */   public AffineTransform getTransform()
/*     */   {
/* 235 */     return (AffineTransform)this.usr2dev.clone();
/*     */   }
/*     */ 
/*     */   public void setAreaOfInterest(Shape paramShape)
/*     */   {
/* 245 */     this.aoi = paramShape;
/*     */   }
/*     */ 
/*     */   public Shape getAreaOfInterest()
/*     */   {
/* 257 */     return this.aoi;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 268 */     RenderContext localRenderContext = new RenderContext(this.usr2dev, this.aoi, this.hints);
/*     */ 
/* 270 */     return localRenderContext;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.renderable.RenderContext
 * JD-Core Version:    0.6.2
 */