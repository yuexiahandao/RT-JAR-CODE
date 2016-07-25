/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract class CompoundControl extends Control
/*     */ {
/*     */   private Control[] controls;
/*     */ 
/*     */   protected CompoundControl(Type paramType, Control[] paramArrayOfControl)
/*     */   {
/*  63 */     super(paramType);
/*  64 */     this.controls = paramArrayOfControl;
/*     */   }
/*     */ 
/*     */   public Control[] getMemberControls()
/*     */   {
/*  78 */     Control[] arrayOfControl = new Control[this.controls.length];
/*     */ 
/*  80 */     for (int i = 0; i < this.controls.length; i++) {
/*  81 */       arrayOfControl[i] = this.controls[i];
/*     */     }
/*     */ 
/*  84 */     return arrayOfControl;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  97 */     StringBuffer localStringBuffer = new StringBuffer();
/*  98 */     for (int i = 0; i < this.controls.length; i++) {
/*  99 */       if (i != 0) {
/* 100 */         localStringBuffer.append(", ");
/* 101 */         if (i + 1 == this.controls.length) {
/* 102 */           localStringBuffer.append("and ");
/*     */         }
/*     */       }
/* 105 */       localStringBuffer.append(this.controls[i].getType());
/*     */     }
/*     */ 
/* 108 */     return new String(getType() + " Control containing " + localStringBuffer + " Controls.");
/*     */   }
/*     */ 
/*     */   public static class Type extends Control.Type
/*     */   {
/*     */     protected Type(String paramString)
/*     */     {
/* 136 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.CompoundControl
 * JD-Core Version:    0.6.2
 */