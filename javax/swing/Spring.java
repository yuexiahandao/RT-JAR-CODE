/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ 
/*     */ public abstract class Spring
/*     */ {
/*     */   public static final int UNSET = -2147483648;
/*     */ 
/*     */   public abstract int getMinimumValue();
/*     */ 
/*     */   public abstract int getPreferredValue();
/*     */ 
/*     */   public abstract int getMaximumValue();
/*     */ 
/*     */   public abstract int getValue();
/*     */ 
/*     */   public abstract void setValue(int paramInt);
/*     */ 
/*     */   private double range(boolean paramBoolean)
/*     */   {
/* 189 */     return paramBoolean ? getPreferredValue() - getMinimumValue() : getMaximumValue() - getPreferredValue();
/*     */   }
/*     */ 
/*     */   double getStrain()
/*     */   {
/* 194 */     double d = getValue() - getPreferredValue();
/* 195 */     return d / range(getValue() < getPreferredValue());
/*     */   }
/*     */ 
/*     */   void setStrain(double paramDouble) {
/* 199 */     setValue(getPreferredValue() + (int)(paramDouble * range(paramDouble < 0.0D)));
/*     */   }
/*     */ 
/*     */   boolean isCyclic(SpringLayout paramSpringLayout) {
/* 203 */     return false;
/*     */   }
/*     */ 
/*     */   public static Spring constant(int paramInt)
/*     */   {
/* 526 */     return constant(paramInt, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   public static Spring constant(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 544 */     return new StaticSpring(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public static Spring minus(Spring paramSpring)
/*     */   {
/* 556 */     return new NegativeSpring(paramSpring);
/*     */   }
/*     */ 
/*     */   public static Spring sum(Spring paramSpring1, Spring paramSpring2)
/*     */   {
/* 589 */     return new SumSpring(paramSpring1, paramSpring2);
/*     */   }
/*     */ 
/*     */   public static Spring max(Spring paramSpring1, Spring paramSpring2)
/*     */   {
/* 601 */     return new MaxSpring(paramSpring1, paramSpring2);
/*     */   }
/*     */ 
/*     */   static Spring difference(Spring paramSpring1, Spring paramSpring2)
/*     */   {
/* 608 */     return sum(paramSpring1, minus(paramSpring2));
/*     */   }
/*     */ 
/*     */   public static Spring scale(Spring paramSpring, float paramFloat)
/*     */   {
/* 636 */     checkArg(paramSpring);
/* 637 */     return new ScaleSpring(paramSpring, paramFloat, null);
/*     */   }
/*     */ 
/*     */   public static Spring width(Component paramComponent)
/*     */   {
/* 657 */     checkArg(paramComponent);
/* 658 */     return new WidthSpring(paramComponent);
/*     */   }
/*     */ 
/*     */   public static Spring height(Component paramComponent)
/*     */   {
/* 678 */     checkArg(paramComponent);
/* 679 */     return new HeightSpring(paramComponent);
/*     */   }
/*     */ 
/*     */   private static void checkArg(Object paramObject)
/*     */   {
/* 687 */     if (paramObject == null)
/* 688 */       throw new NullPointerException("Argument must not be null");
/*     */   }
/*     */ 
/*     */   static abstract class AbstractSpring extends Spring
/*     */   {
/* 207 */     protected int size = -2147483648;
/*     */ 
/*     */     public int getValue() {
/* 210 */       return this.size != -2147483648 ? this.size : getPreferredValue();
/*     */     }
/*     */ 
/*     */     public final void setValue(int paramInt) {
/* 214 */       if (this.size == paramInt) {
/* 215 */         return;
/*     */       }
/* 217 */       if (paramInt == -2147483648)
/* 218 */         clear();
/*     */       else
/* 220 */         setNonClearValue(paramInt);
/*     */     }
/*     */ 
/*     */     protected void clear()
/*     */     {
/* 225 */       this.size = -2147483648;
/*     */     }
/*     */ 
/*     */     protected void setNonClearValue(int paramInt) {
/* 229 */       this.size = paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class CompoundSpring extends Spring.StaticSpring
/*     */   {
/*     */     protected Spring s1;
/*     */     protected Spring s2;
/*     */ 
/*     */     public CompoundSpring(Spring paramSpring1, Spring paramSpring2)
/*     */     {
/* 430 */       super();
/* 431 */       this.s1 = paramSpring1;
/* 432 */       this.s2 = paramSpring2;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 436 */       return "CompoundSpring of " + this.s1 + " and " + this.s2;
/*     */     }
/*     */ 
/*     */     protected void clear() {
/* 440 */       super.clear();
/* 441 */       this.min = (this.pref = this.max = -2147483648);
/* 442 */       this.s1.setValue(-2147483648);
/* 443 */       this.s2.setValue(-2147483648);
/*     */     }
/*     */ 
/*     */     protected abstract int op(int paramInt1, int paramInt2);
/*     */ 
/*     */     public int getMinimumValue() {
/* 449 */       if (this.min == -2147483648) {
/* 450 */         this.min = op(this.s1.getMinimumValue(), this.s2.getMinimumValue());
/*     */       }
/* 452 */       return this.min;
/*     */     }
/*     */ 
/*     */     public int getPreferredValue() {
/* 456 */       if (this.pref == -2147483648) {
/* 457 */         this.pref = op(this.s1.getPreferredValue(), this.s2.getPreferredValue());
/*     */       }
/* 459 */       return this.pref;
/*     */     }
/*     */ 
/*     */     public int getMaximumValue() {
/* 463 */       if (this.max == -2147483648) {
/* 464 */         this.max = op(this.s1.getMaximumValue(), this.s2.getMaximumValue());
/*     */       }
/* 466 */       return this.max;
/*     */     }
/*     */ 
/*     */     public int getValue() {
/* 470 */       if (this.size == -2147483648) {
/* 471 */         this.size = op(this.s1.getValue(), this.s2.getValue());
/*     */       }
/* 473 */       return this.size;
/*     */     }
/*     */ 
/*     */     boolean isCyclic(SpringLayout paramSpringLayout) {
/* 477 */       return (paramSpringLayout.isCyclic(this.s1)) || (paramSpringLayout.isCyclic(this.s2));
/*     */     }
/*     */   }
/*     */ 
/*     */   static class HeightSpring extends Spring.AbstractSpring
/*     */   {
/*     */     Component c;
/*     */ 
/*     */     public HeightSpring(Component paramComponent)
/*     */     {
/* 367 */       this.c = paramComponent;
/*     */     }
/*     */ 
/*     */     public int getMinimumValue() {
/* 371 */       return this.c.getMinimumSize().height;
/*     */     }
/*     */ 
/*     */     public int getPreferredValue() {
/* 375 */       return this.c.getPreferredSize().height;
/*     */     }
/*     */ 
/*     */     public int getMaximumValue() {
/* 379 */       return Math.min(32767, this.c.getMaximumSize().height);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MaxSpring extends Spring.CompoundSpring
/*     */   {
/*     */     public MaxSpring(Spring paramSpring1, Spring paramSpring2)
/*     */     {
/* 500 */       super(paramSpring2);
/*     */     }
/*     */ 
/*     */     protected int op(int paramInt1, int paramInt2) {
/* 504 */       return Math.max(paramInt1, paramInt2);
/*     */     }
/*     */ 
/*     */     protected void setNonClearValue(int paramInt) {
/* 508 */       super.setNonClearValue(paramInt);
/* 509 */       this.s1.setValue(paramInt);
/* 510 */       this.s2.setValue(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class NegativeSpring extends Spring
/*     */   {
/*     */     private Spring s;
/*     */ 
/*     */     public NegativeSpring(Spring paramSpring)
/*     */     {
/* 269 */       this.s = paramSpring;
/*     */     }
/*     */ 
/*     */     public int getMinimumValue()
/*     */     {
/* 276 */       return -this.s.getMaximumValue();
/*     */     }
/*     */ 
/*     */     public int getPreferredValue() {
/* 280 */       return -this.s.getPreferredValue();
/*     */     }
/*     */ 
/*     */     public int getMaximumValue() {
/* 284 */       return -this.s.getMinimumValue();
/*     */     }
/*     */ 
/*     */     public int getValue() {
/* 288 */       return -this.s.getValue();
/*     */     }
/*     */ 
/*     */     public void setValue(int paramInt)
/*     */     {
/* 294 */       this.s.setValue(-paramInt);
/*     */     }
/*     */ 
/*     */     boolean isCyclic(SpringLayout paramSpringLayout) {
/* 298 */       return this.s.isCyclic(paramSpringLayout);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ScaleSpring extends Spring {
/*     */     private Spring s;
/*     */     private float factor;
/*     */ 
/* 307 */     private ScaleSpring(Spring paramSpring, float paramFloat) { this.s = paramSpring;
/* 308 */       this.factor = paramFloat; }
/*     */ 
/*     */     public int getMinimumValue()
/*     */     {
/* 312 */       return Math.round((this.factor < 0.0F ? this.s.getMaximumValue() : this.s.getMinimumValue()) * this.factor);
/*     */     }
/*     */ 
/*     */     public int getPreferredValue() {
/* 316 */       return Math.round(this.s.getPreferredValue() * this.factor);
/*     */     }
/*     */ 
/*     */     public int getMaximumValue() {
/* 320 */       return Math.round((this.factor < 0.0F ? this.s.getMinimumValue() : this.s.getMaximumValue()) * this.factor);
/*     */     }
/*     */ 
/*     */     public int getValue() {
/* 324 */       return Math.round(this.s.getValue() * this.factor);
/*     */     }
/*     */ 
/*     */     public void setValue(int paramInt) {
/* 328 */       if (paramInt == -2147483648)
/* 329 */         this.s.setValue(-2147483648);
/*     */       else
/* 331 */         this.s.setValue(Math.round(paramInt / this.factor));
/*     */     }
/*     */ 
/*     */     boolean isCyclic(SpringLayout paramSpringLayout)
/*     */     {
/* 336 */       return this.s.isCyclic(paramSpringLayout);
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class SpringMap extends Spring
/*     */   {
/*     */     private Spring s;
/*     */ 
/*     */     public SpringMap(Spring paramSpring)
/*     */     {
/* 387 */       this.s = paramSpring;
/*     */     }
/*     */ 
/*     */     protected abstract int map(int paramInt);
/*     */ 
/*     */     protected abstract int inv(int paramInt);
/*     */ 
/*     */     public int getMinimumValue() {
/* 395 */       return map(this.s.getMinimumValue());
/*     */     }
/*     */ 
/*     */     public int getPreferredValue() {
/* 399 */       return map(this.s.getPreferredValue());
/*     */     }
/*     */ 
/*     */     public int getMaximumValue() {
/* 403 */       return Math.min(32767, map(this.s.getMaximumValue()));
/*     */     }
/*     */ 
/*     */     public int getValue() {
/* 407 */       return map(this.s.getValue());
/*     */     }
/*     */ 
/*     */     public void setValue(int paramInt) {
/* 411 */       if (paramInt == -2147483648)
/* 412 */         this.s.setValue(-2147483648);
/*     */       else
/* 414 */         this.s.setValue(inv(paramInt));
/*     */     }
/*     */ 
/*     */     boolean isCyclic(SpringLayout paramSpringLayout)
/*     */     {
/* 419 */       return this.s.isCyclic(paramSpringLayout);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class StaticSpring extends Spring.AbstractSpring
/*     */   {
/*     */     protected int min;
/*     */     protected int pref;
/*     */     protected int max;
/*     */ 
/*     */     public StaticSpring(int paramInt)
/*     */     {
/* 239 */       this(paramInt, paramInt, paramInt);
/*     */     }
/*     */ 
/*     */     public StaticSpring(int paramInt1, int paramInt2, int paramInt3) {
/* 243 */       this.min = paramInt1;
/* 244 */       this.pref = paramInt2;
/* 245 */       this.max = paramInt3;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 249 */       return "StaticSpring [" + this.min + ", " + this.pref + ", " + this.max + "]";
/*     */     }
/*     */ 
/*     */     public int getMinimumValue() {
/* 253 */       return this.min;
/*     */     }
/*     */ 
/*     */     public int getPreferredValue() {
/* 257 */       return this.pref;
/*     */     }
/*     */ 
/*     */     public int getMaximumValue() {
/* 261 */       return this.max;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SumSpring extends Spring.CompoundSpring
/*     */   {
/*     */     public SumSpring(Spring paramSpring1, Spring paramSpring2)
/*     */     {
/* 483 */       super(paramSpring2);
/*     */     }
/*     */ 
/*     */     protected int op(int paramInt1, int paramInt2) {
/* 487 */       return paramInt1 + paramInt2;
/*     */     }
/*     */ 
/*     */     protected void setNonClearValue(int paramInt) {
/* 491 */       super.setNonClearValue(paramInt);
/* 492 */       this.s1.setStrain(getStrain());
/* 493 */       this.s2.setValue(paramInt - this.s1.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   static class WidthSpring extends Spring.AbstractSpring
/*     */   {
/*     */     Component c;
/*     */ 
/*     */     public WidthSpring(Component paramComponent)
/*     */     {
/* 344 */       this.c = paramComponent;
/*     */     }
/*     */ 
/*     */     public int getMinimumValue() {
/* 348 */       return this.c.getMinimumSize().width;
/*     */     }
/*     */ 
/*     */     public int getPreferredValue() {
/* 352 */       return this.c.getPreferredSize().width;
/*     */     }
/*     */ 
/*     */     public int getMaximumValue()
/*     */     {
/* 359 */       return Math.min(32767, this.c.getMaximumSize().width);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.Spring
 * JD-Core Version:    0.6.2
 */