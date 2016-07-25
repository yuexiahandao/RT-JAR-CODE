/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingConstants;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ class SynthArrowButton extends JButton
/*     */   implements SwingConstants, UIResource
/*     */ {
/*     */   private int direction;
/*     */ 
/*     */   public SynthArrowButton(int paramInt)
/*     */   {
/*  41 */     super.setFocusable(false);
/*  42 */     setDirection(paramInt);
/*  43 */     setDefaultCapable(false);
/*     */   }
/*     */ 
/*     */   public String getUIClassID() {
/*  47 */     return "ArrowButtonUI";
/*     */   }
/*     */ 
/*     */   public void updateUI() {
/*  51 */     setUI(new SynthArrowButtonUI(null));
/*     */   }
/*     */ 
/*     */   public void setDirection(int paramInt) {
/*  55 */     this.direction = paramInt;
/*  56 */     putClientProperty("__arrow_direction__", Integer.valueOf(paramInt));
/*  57 */     repaint();
/*     */   }
/*     */ 
/*     */   public int getDirection() {
/*  61 */     return this.direction;
/*     */   }
/*     */   public void setFocusable(boolean paramBoolean) {
/*     */   }
/*     */ 
/*     */   private static class SynthArrowButtonUI extends SynthButtonUI {
/*     */     protected void installDefaults(AbstractButton paramAbstractButton) {
/*  68 */       super.installDefaults(paramAbstractButton);
/*  69 */       updateStyle(paramAbstractButton);
/*     */     }
/*     */ 
/*     */     protected void paint(SynthContext paramSynthContext, Graphics paramGraphics) {
/*  73 */       SynthArrowButton localSynthArrowButton = (SynthArrowButton)paramSynthContext.getComponent();
/*     */ 
/*  75 */       paramSynthContext.getPainter().paintArrowButtonForeground(paramSynthContext, paramGraphics, 0, 0, localSynthArrowButton.getWidth(), localSynthArrowButton.getHeight(), localSynthArrowButton.getDirection());
/*     */     }
/*     */ 
/*     */     void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent)
/*     */     {
/*  81 */       paramSynthContext.getPainter().paintArrowButtonBackground(paramSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */     }
/*     */ 
/*     */     public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/*  87 */       paramSynthContext.getPainter().paintArrowButtonBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     public Dimension getMinimumSize() {
/*  91 */       return new Dimension(5, 5);
/*     */     }
/*     */ 
/*     */     public Dimension getMaximumSize() {
/*  95 */       return new Dimension(2147483647, 2147483647);
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize(JComponent paramJComponent) {
/*  99 */       SynthContext localSynthContext = getContext(paramJComponent);
/* 100 */       Dimension localDimension = null;
/* 101 */       if (localSynthContext.getComponent().getName() == "ScrollBar.button")
/*     */       {
/* 105 */         localDimension = (Dimension)localSynthContext.getStyle().get(localSynthContext, "ScrollBar.buttonSize");
/*     */       }
/*     */ 
/* 108 */       if (localDimension == null)
/*     */       {
/* 112 */         int i = localSynthContext.getStyle().getInt(localSynthContext, "ArrowButton.size", 16);
/*     */ 
/* 114 */         localDimension = new Dimension(i, i);
/*     */       }
/*     */ 
/* 120 */       Container localContainer = localSynthContext.getComponent().getParent();
/* 121 */       if (((localContainer instanceof JComponent)) && (!(localContainer instanceof JComboBox))) {
/* 122 */         Object localObject = ((JComponent)localContainer).getClientProperty("JComponent.sizeVariant");
/*     */ 
/* 124 */         if (localObject != null) {
/* 125 */           if ("large".equals(localObject)) {
/* 126 */             localDimension = new Dimension((int)(localDimension.width * 1.15D), (int)(localDimension.height * 1.15D));
/*     */           }
/* 129 */           else if ("small".equals(localObject)) {
/* 130 */             localDimension = new Dimension((int)(localDimension.width * 0.857D), (int)(localDimension.height * 0.857D));
/*     */           }
/* 133 */           else if ("mini".equals(localObject)) {
/* 134 */             localDimension = new Dimension((int)(localDimension.width * 0.714D), (int)(localDimension.height * 0.714D));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 141 */       localSynthContext.dispose();
/* 142 */       return localDimension;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthArrowButton
 * JD-Core Version:    0.6.2
 */