/*     */ package java.awt.geom;
/*     */ 
/*     */ public abstract class Dimension2D
/*     */   implements Cloneable
/*     */ {
/*     */   public abstract double getWidth();
/*     */ 
/*     */   public abstract double getHeight();
/*     */ 
/*     */   public abstract void setSize(double paramDouble1, double paramDouble2);
/*     */ 
/*     */   public void setSize(Dimension2D paramDimension2D)
/*     */   {
/*  95 */     setSize(paramDimension2D.getWidth(), paramDimension2D.getHeight());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 108 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 111 */     throw new InternalError();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.Dimension2D
 * JD-Core Version:    0.6.2
 */