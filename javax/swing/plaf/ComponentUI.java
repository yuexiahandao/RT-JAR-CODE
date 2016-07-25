/*     */ package javax.swing.plaf;
/*     */ 
/*     */ import java.awt.Component.BaselineResizeBehavior;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.SwingUtilities;
/*     */ 
/*     */ public abstract class ComponentUI
/*     */ {
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 157 */     if (paramJComponent.isOpaque()) {
/* 158 */       paramGraphics.setColor(paramJComponent.getBackground());
/* 159 */       paramGraphics.fillRect(0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */     }
/* 161 */     paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 204 */     return getPreferredSize(paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 225 */     return getPreferredSize(paramJComponent);
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 248 */     return paramJComponent.inside(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 262 */     throw new Error("ComponentUI.createUI not implemented.");
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 288 */     if (paramJComponent == null) {
/* 289 */       throw new NullPointerException("Component must be non-null");
/*     */     }
/* 291 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/* 292 */       throw new IllegalArgumentException("Width and height must be >= 0");
/*     */     }
/*     */ 
/* 295 */     return -1;
/*     */   }
/*     */ 
/*     */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*     */   {
/* 315 */     if (paramJComponent == null) {
/* 316 */       throw new NullPointerException("Component must be non-null");
/*     */     }
/* 318 */     return Component.BaselineResizeBehavior.OTHER;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 338 */     return SwingUtilities.getAccessibleChildrenCount(paramJComponent);
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 358 */     return SwingUtilities.getAccessibleChild(paramJComponent, paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.ComponentUI
 * JD-Core Version:    0.6.2
 */