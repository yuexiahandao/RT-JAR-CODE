/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.image.ColorModel;
/*     */ import sun.java2d.SunCompositeContext;
/*     */ 
/*     */ public final class AlphaComposite
/*     */   implements Composite
/*     */ {
/*     */   public static final int CLEAR = 1;
/*     */   public static final int SRC = 2;
/*     */   public static final int DST = 9;
/*     */   public static final int SRC_OVER = 3;
/*     */   public static final int DST_OVER = 4;
/*     */   public static final int SRC_IN = 5;
/*     */   public static final int DST_IN = 6;
/*     */   public static final int SRC_OUT = 7;
/*     */   public static final int DST_OUT = 8;
/*     */   public static final int SRC_ATOP = 10;
/*     */   public static final int DST_ATOP = 11;
/*     */   public static final int XOR = 12;
/* 523 */   public static final AlphaComposite Clear = new AlphaComposite(1);
/*     */ 
/* 530 */   public static final AlphaComposite Src = new AlphaComposite(2);
/*     */ 
/* 538 */   public static final AlphaComposite Dst = new AlphaComposite(9);
/*     */ 
/* 545 */   public static final AlphaComposite SrcOver = new AlphaComposite(3);
/*     */ 
/* 552 */   public static final AlphaComposite DstOver = new AlphaComposite(4);
/*     */ 
/* 559 */   public static final AlphaComposite SrcIn = new AlphaComposite(5);
/*     */ 
/* 566 */   public static final AlphaComposite DstIn = new AlphaComposite(6);
/*     */ 
/* 573 */   public static final AlphaComposite SrcOut = new AlphaComposite(7);
/*     */ 
/* 580 */   public static final AlphaComposite DstOut = new AlphaComposite(8);
/*     */ 
/* 588 */   public static final AlphaComposite SrcAtop = new AlphaComposite(10);
/*     */ 
/* 596 */   public static final AlphaComposite DstAtop = new AlphaComposite(11);
/*     */ 
/* 604 */   public static final AlphaComposite Xor = new AlphaComposite(12);
/*     */   private static final int MIN_RULE = 1;
/*     */   private static final int MAX_RULE = 12;
/*     */   float extraAlpha;
/*     */   int rule;
/*     */ 
/*     */   private AlphaComposite(int paramInt)
/*     */   {
/* 613 */     this(paramInt, 1.0F);
/*     */   }
/*     */ 
/*     */   private AlphaComposite(int paramInt, float paramFloat) {
/* 617 */     if ((paramInt < 1) || (paramInt > 12)) {
/* 618 */       throw new IllegalArgumentException("unknown composite rule");
/*     */     }
/* 620 */     if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F)) {
/* 621 */       this.rule = paramInt;
/* 622 */       this.extraAlpha = paramFloat;
/*     */     } else {
/* 624 */       throw new IllegalArgumentException("alpha value out of range");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static AlphaComposite getInstance(int paramInt)
/*     */   {
/* 638 */     switch (paramInt) {
/*     */     case 1:
/* 640 */       return Clear;
/*     */     case 2:
/* 642 */       return Src;
/*     */     case 9:
/* 644 */       return Dst;
/*     */     case 3:
/* 646 */       return SrcOver;
/*     */     case 4:
/* 648 */       return DstOver;
/*     */     case 5:
/* 650 */       return SrcIn;
/*     */     case 6:
/* 652 */       return DstIn;
/*     */     case 7:
/* 654 */       return SrcOut;
/*     */     case 8:
/* 656 */       return DstOut;
/*     */     case 10:
/* 658 */       return SrcAtop;
/*     */     case 11:
/* 660 */       return DstAtop;
/*     */     case 12:
/* 662 */       return Xor;
/*     */     }
/* 664 */     throw new IllegalArgumentException("unknown composite rule");
/*     */   }
/*     */ 
/*     */   public static AlphaComposite getInstance(int paramInt, float paramFloat)
/*     */   {
/* 686 */     if (paramFloat == 1.0F) {
/* 687 */       return getInstance(paramInt);
/*     */     }
/* 689 */     return new AlphaComposite(paramInt, paramFloat);
/*     */   }
/*     */ 
/*     */   public CompositeContext createContext(ColorModel paramColorModel1, ColorModel paramColorModel2, RenderingHints paramRenderingHints)
/*     */   {
/* 704 */     return new SunCompositeContext(this, paramColorModel1, paramColorModel2);
/*     */   }
/*     */ 
/*     */   public float getAlpha()
/*     */   {
/* 713 */     return this.extraAlpha;
/*     */   }
/*     */ 
/*     */   public int getRule()
/*     */   {
/* 721 */     return this.rule;
/*     */   }
/*     */ 
/*     */   public AlphaComposite derive(int paramInt)
/*     */   {
/* 741 */     return this.rule == paramInt ? this : getInstance(paramInt, this.extraAlpha);
/*     */   }
/*     */ 
/*     */   public AlphaComposite derive(float paramFloat)
/*     */   {
/* 761 */     return this.extraAlpha == paramFloat ? this : getInstance(this.rule, paramFloat);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 771 */     return Float.floatToIntBits(this.extraAlpha) * 31 + this.rule;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 788 */     if (!(paramObject instanceof AlphaComposite)) {
/* 789 */       return false;
/*     */     }
/*     */ 
/* 792 */     AlphaComposite localAlphaComposite = (AlphaComposite)paramObject;
/*     */ 
/* 794 */     if (this.rule != localAlphaComposite.rule) {
/* 795 */       return false;
/*     */     }
/*     */ 
/* 798 */     if (this.extraAlpha != localAlphaComposite.extraAlpha) {
/* 799 */       return false;
/*     */     }
/*     */ 
/* 802 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.AlphaComposite
 * JD-Core Version:    0.6.2
 */