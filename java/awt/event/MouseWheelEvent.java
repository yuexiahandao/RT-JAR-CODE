/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.Component;
/*     */ 
/*     */ public class MouseWheelEvent extends MouseEvent
/*     */ {
/*     */   public static final int WHEEL_UNIT_SCROLL = 0;
/*     */   public static final int WHEEL_BLOCK_SCROLL = 1;
/*     */   int scrollType;
/*     */   int scrollAmount;
/*     */   int wheelRotation;
/*     */   double preciseWheelRotation;
/*     */   private static final long serialVersionUID = 6459879390515399677L;
/*     */ 
/*     */   public MouseWheelEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, int paramInt6, int paramInt7, int paramInt8)
/*     */   {
/* 199 */     this(paramComponent, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, 0, 0, paramInt5, paramBoolean, paramInt6, paramInt7, paramInt8);
/*     */   }
/*     */ 
/*     */   public MouseWheelEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, int paramInt8, int paramInt9, int paramInt10)
/*     */   {
/* 246 */     this(paramComponent, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramBoolean, paramInt8, paramInt9, paramInt10, paramInt10);
/*     */   }
/*     */ 
/*     */   public MouseWheelEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, int paramInt8, int paramInt9, int paramInt10, double paramDouble)
/*     */   {
/* 300 */     super(paramComponent, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramBoolean, 0);
/*     */ 
/* 303 */     this.scrollType = paramInt8;
/* 304 */     this.scrollAmount = paramInt9;
/* 305 */     this.wheelRotation = paramInt10;
/* 306 */     this.preciseWheelRotation = paramDouble;
/*     */   }
/*     */ 
/*     */   public int getScrollType()
/*     */   {
/* 327 */     return this.scrollType;
/*     */   }
/*     */ 
/*     */   public int getScrollAmount()
/*     */   {
/* 342 */     return this.scrollAmount;
/*     */   }
/*     */ 
/*     */   public int getWheelRotation()
/*     */   {
/* 356 */     return this.wheelRotation;
/*     */   }
/*     */ 
/*     */   public double getPreciseWheelRotation()
/*     */   {
/* 371 */     return this.preciseWheelRotation;
/*     */   }
/*     */ 
/*     */   public int getUnitsToScroll()
/*     */   {
/* 422 */     return this.scrollAmount * this.wheelRotation;
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/* 432 */     String str = null;
/*     */ 
/* 434 */     if (getScrollType() == 0) {
/* 435 */       str = "WHEEL_UNIT_SCROLL";
/*     */     }
/* 437 */     else if (getScrollType() == 1) {
/* 438 */       str = "WHEEL_BLOCK_SCROLL";
/*     */     }
/*     */     else {
/* 441 */       str = "unknown scroll type";
/*     */     }
/* 443 */     return super.paramString() + ",scrollType=" + str + ",scrollAmount=" + getScrollAmount() + ",wheelRotation=" + getWheelRotation() + ",preciseWheelRotation=" + getPreciseWheelRotation();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.MouseWheelEvent
 * JD-Core Version:    0.6.2
 */