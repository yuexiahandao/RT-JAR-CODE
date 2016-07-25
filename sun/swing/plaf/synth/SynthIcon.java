/*     */ package sun.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.plaf.synth.SynthContext;
/*     */ 
/*     */ public abstract class SynthIcon
/*     */   implements Icon
/*     */ {
/*     */   public static int getIconWidth(Icon paramIcon, SynthContext paramSynthContext)
/*     */   {
/*  42 */     if (paramIcon == null) {
/*  43 */       return 0;
/*     */     }
/*  45 */     if ((paramIcon instanceof SynthIcon)) {
/*  46 */       return ((SynthIcon)paramIcon).getIconWidth(paramSynthContext);
/*     */     }
/*  48 */     return paramIcon.getIconWidth();
/*     */   }
/*     */ 
/*     */   public static int getIconHeight(Icon paramIcon, SynthContext paramSynthContext) {
/*  52 */     if (paramIcon == null) {
/*  53 */       return 0;
/*     */     }
/*  55 */     if ((paramIcon instanceof SynthIcon)) {
/*  56 */       return ((SynthIcon)paramIcon).getIconHeight(paramSynthContext);
/*     */     }
/*  58 */     return paramIcon.getIconHeight();
/*     */   }
/*     */ 
/*     */   public static void paintIcon(Icon paramIcon, SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  63 */     if ((paramIcon instanceof SynthIcon)) {
/*  64 */       ((SynthIcon)paramIcon).paintIcon(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*  66 */     else if (paramIcon != null)
/*  67 */       paramIcon.paintIcon(paramSynthContext.getComponent(), paramGraphics, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public abstract void paintIcon(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*     */ 
/*     */   public abstract int getIconWidth(SynthContext paramSynthContext);
/*     */ 
/*     */   public abstract int getIconHeight(SynthContext paramSynthContext);
/*     */ 
/*     */   public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/* 104 */     paintIcon(null, paramGraphics, paramInt1, paramInt2, 0, 0);
/*     */   }
/*     */ 
/*     */   public int getIconWidth()
/*     */   {
/* 114 */     return getIconWidth(null);
/*     */   }
/*     */ 
/*     */   public int getIconHeight()
/*     */   {
/* 124 */     return getIconHeight(null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.plaf.synth.SynthIcon
 * JD-Core Version:    0.6.2
 */