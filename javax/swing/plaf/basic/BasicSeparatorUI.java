/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.SeparatorUI;
/*     */ 
/*     */ public class BasicSeparatorUI extends SeparatorUI
/*     */ {
/*     */   protected Color shadow;
/*     */   protected Color highlight;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  53 */     return new BasicSeparatorUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  58 */     installDefaults((JSeparator)paramJComponent);
/*  59 */     installListeners((JSeparator)paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  64 */     uninstallDefaults((JSeparator)paramJComponent);
/*  65 */     uninstallListeners((JSeparator)paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JSeparator paramJSeparator)
/*     */   {
/*  70 */     LookAndFeel.installColors(paramJSeparator, "Separator.background", "Separator.foreground");
/*  71 */     LookAndFeel.installProperty(paramJSeparator, "opaque", Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JSeparator paramJSeparator)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void installListeners(JSeparator paramJSeparator)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JSeparator paramJSeparator)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*  88 */     Dimension localDimension = paramJComponent.getSize();
/*     */ 
/*  90 */     if (((JSeparator)paramJComponent).getOrientation() == 1)
/*     */     {
/*  92 */       paramGraphics.setColor(paramJComponent.getForeground());
/*  93 */       paramGraphics.drawLine(0, 0, 0, localDimension.height);
/*     */ 
/*  95 */       paramGraphics.setColor(paramJComponent.getBackground());
/*  96 */       paramGraphics.drawLine(1, 0, 1, localDimension.height);
/*     */     }
/*     */     else
/*     */     {
/* 100 */       paramGraphics.setColor(paramJComponent.getForeground());
/* 101 */       paramGraphics.drawLine(0, 0, localDimension.width, 0);
/*     */ 
/* 103 */       paramGraphics.setColor(paramJComponent.getBackground());
/* 104 */       paramGraphics.drawLine(0, 1, localDimension.width, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 110 */     if (((JSeparator)paramJComponent).getOrientation() == 1) {
/* 111 */       return new Dimension(2, 0);
/*     */     }
/* 113 */     return new Dimension(0, 2);
/*     */   }
/*     */   public Dimension getMinimumSize(JComponent paramJComponent) {
/* 116 */     return null; } 
/* 117 */   public Dimension getMaximumSize(JComponent paramJComponent) { return null; }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicSeparatorUI
 * JD-Core Version:    0.6.2
 */