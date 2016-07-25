/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ public class MetalCheckBoxIcon
/*     */   implements Icon, UIResource, Serializable
/*     */ {
/*     */   protected int getControlSize()
/*     */   {
/*  51 */     return 13;
/*     */   }
/*     */ 
/*     */   public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  55 */     JCheckBox localJCheckBox = (JCheckBox)paramComponent;
/*  56 */     ButtonModel localButtonModel = localJCheckBox.getModel();
/*  57 */     int i = getControlSize();
/*     */ 
/*  59 */     boolean bool = localButtonModel.isSelected();
/*     */ 
/*  61 */     if (localButtonModel.isEnabled()) {
/*  62 */       if (localJCheckBox.isBorderPaintedFlat()) {
/*  63 */         paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/*  64 */         paramGraphics.drawRect(paramInt1 + 1, paramInt2, i - 1, i - 1);
/*     */       }
/*  66 */       if ((localButtonModel.isPressed()) && (localButtonModel.isArmed())) {
/*  67 */         if (localJCheckBox.isBorderPaintedFlat()) {
/*  68 */           paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/*  69 */           paramGraphics.fillRect(paramInt1 + 2, paramInt2 + 1, i - 2, i - 2);
/*     */         } else {
/*  71 */           paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/*  72 */           paramGraphics.fillRect(paramInt1, paramInt2, i - 1, i - 1);
/*  73 */           MetalUtils.drawPressed3DBorder(paramGraphics, paramInt1, paramInt2, i, i);
/*     */         }
/*  75 */       } else if (!localJCheckBox.isBorderPaintedFlat()) {
/*  76 */         MetalUtils.drawFlush3DBorder(paramGraphics, paramInt1, paramInt2, i, i);
/*     */       }
/*  78 */       paramGraphics.setColor(MetalLookAndFeel.getControlInfo());
/*     */     } else {
/*  80 */       paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/*  81 */       paramGraphics.drawRect(paramInt1, paramInt2, i - 1, i - 1);
/*     */     }
/*     */ 
/*  85 */     if (bool) {
/*  86 */       if (localJCheckBox.isBorderPaintedFlat()) {
/*  87 */         paramInt1++;
/*     */       }
/*  89 */       drawCheck(paramComponent, paramGraphics, paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void drawCheck(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  94 */     int i = getControlSize();
/*  95 */     paramGraphics.fillRect(paramInt1 + 3, paramInt2 + 5, 2, i - 8);
/*  96 */     paramGraphics.drawLine(paramInt1 + (i - 4), paramInt2 + 3, paramInt1 + 5, paramInt2 + (i - 6));
/*  97 */     paramGraphics.drawLine(paramInt1 + (i - 4), paramInt2 + 4, paramInt1 + 5, paramInt2 + (i - 5));
/*     */   }
/*     */ 
/*     */   public int getIconWidth() {
/* 101 */     return getControlSize();
/*     */   }
/*     */ 
/*     */   public int getIconHeight() {
/* 105 */     return getControlSize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalCheckBoxIcon
 * JD-Core Version:    0.6.2
 */