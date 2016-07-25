/*     */ package java.awt.geom;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Vector;
/*     */ import sun.awt.geom.Curve;
/*     */ 
/*     */ class AreaIterator
/*     */   implements PathIterator
/*     */ {
/*     */   private AffineTransform transform;
/*     */   private Vector curves;
/*     */   private int index;
/*     */   private Curve prevcurve;
/*     */   private Curve thiscurve;
/*     */ 
/*     */   public AreaIterator(Vector paramVector, AffineTransform paramAffineTransform)
/*     */   {
/* 667 */     this.curves = paramVector;
/* 668 */     this.transform = paramAffineTransform;
/* 669 */     if (paramVector.size() >= 1)
/* 670 */       this.thiscurve = ((Curve)paramVector.get(0));
/*     */   }
/*     */ 
/*     */   public int getWindingRule()
/*     */   {
/* 678 */     return 1;
/*     */   }
/*     */ 
/*     */   public boolean isDone() {
/* 682 */     return (this.prevcurve == null) && (this.thiscurve == null);
/*     */   }
/*     */ 
/*     */   public void next() {
/* 686 */     if (this.prevcurve != null) {
/* 687 */       this.prevcurve = null;
/*     */     } else {
/* 689 */       this.prevcurve = this.thiscurve;
/* 690 */       this.index += 1;
/* 691 */       if (this.index < this.curves.size()) {
/* 692 */         this.thiscurve = ((Curve)this.curves.get(this.index));
/* 693 */         if ((this.thiscurve.getOrder() != 0) && (this.prevcurve.getX1() == this.thiscurve.getX0()) && (this.prevcurve.getY1() == this.thiscurve.getY0()))
/*     */         {
/* 697 */           this.prevcurve = null;
/*     */         }
/*     */       } else {
/* 700 */         this.thiscurve = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public int currentSegment(float[] paramArrayOfFloat) {
/* 706 */     double[] arrayOfDouble = new double[6];
/* 707 */     int i = currentSegment(arrayOfDouble);
/* 708 */     int j = i == 3 ? 3 : i == 2 ? 2 : i == 4 ? 0 : 1;
/*     */ 
/* 712 */     for (int k = 0; k < j * 2; k++) {
/* 713 */       paramArrayOfFloat[k] = ((float)arrayOfDouble[k]);
/*     */     }
/* 715 */     return i;
/*     */   }
/*     */ 
/*     */   public int currentSegment(double[] paramArrayOfDouble)
/*     */   {
/*     */     int i;
/*     */     int j;
/* 721 */     if (this.prevcurve != null)
/*     */     {
/* 723 */       if ((this.thiscurve == null) || (this.thiscurve.getOrder() == 0)) {
/* 724 */         return 4;
/*     */       }
/* 726 */       paramArrayOfDouble[0] = this.thiscurve.getX0();
/* 727 */       paramArrayOfDouble[1] = this.thiscurve.getY0();
/* 728 */       i = 1;
/* 729 */       j = 1; } else {
/* 730 */       if (this.thiscurve == null) {
/* 731 */         throw new NoSuchElementException("area iterator out of bounds");
/*     */       }
/* 733 */       i = this.thiscurve.getSegment(paramArrayOfDouble);
/* 734 */       j = this.thiscurve.getOrder();
/* 735 */       if (j == 0) {
/* 736 */         j = 1;
/*     */       }
/*     */     }
/* 739 */     if (this.transform != null) {
/* 740 */       this.transform.transform(paramArrayOfDouble, 0, paramArrayOfDouble, 0, j);
/*     */     }
/* 742 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.geom.AreaIterator
 * JD-Core Version:    0.6.2
 */