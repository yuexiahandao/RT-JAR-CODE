/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ 
/*     */ abstract class ShadowEffect extends Effect
/*     */ {
/*  35 */   protected Color color = Color.BLACK;
/*     */ 
/*  37 */   protected float opacity = 0.75F;
/*     */ 
/*  39 */   protected int angle = 135;
/*     */ 
/*  41 */   protected int distance = 5;
/*     */ 
/*  43 */   protected int spread = 0;
/*     */ 
/*  45 */   protected int size = 5;
/*     */ 
/*     */   Color getColor()
/*     */   {
/*  51 */     return this.color;
/*     */   }
/*     */ 
/*     */   void setColor(Color paramColor) {
/*  55 */     Color localColor = getColor();
/*  56 */     this.color = paramColor;
/*     */   }
/*     */ 
/*     */   float getOpacity() {
/*  60 */     return this.opacity;
/*     */   }
/*     */ 
/*     */   void setOpacity(float paramFloat) {
/*  64 */     float f = getOpacity();
/*  65 */     this.opacity = paramFloat;
/*     */   }
/*     */ 
/*     */   int getAngle() {
/*  69 */     return this.angle;
/*     */   }
/*     */ 
/*     */   void setAngle(int paramInt) {
/*  73 */     int i = getAngle();
/*  74 */     this.angle = paramInt;
/*     */   }
/*     */ 
/*     */   int getDistance() {
/*  78 */     return this.distance;
/*     */   }
/*     */ 
/*     */   void setDistance(int paramInt) {
/*  82 */     int i = getDistance();
/*  83 */     this.distance = paramInt;
/*     */   }
/*     */ 
/*     */   int getSpread() {
/*  87 */     return this.spread;
/*     */   }
/*     */ 
/*     */   void setSpread(int paramInt) {
/*  91 */     int i = getSpread();
/*  92 */     this.spread = paramInt;
/*     */   }
/*     */ 
/*     */   int getSize() {
/*  96 */     return this.size;
/*     */   }
/*     */ 
/*     */   void setSize(int paramInt) {
/* 100 */     int i = getSize();
/* 101 */     this.size = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.ShadowEffect
 * JD-Core Version:    0.6.2
 */