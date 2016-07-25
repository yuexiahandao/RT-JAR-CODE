/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicOptionPaneUI;
/*     */ import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonAreaLayout;
/*     */ 
/*     */ public class MotifOptionPaneUI extends BasicOptionPaneUI
/*     */ {
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     return new MotifOptionPaneUI();
/*     */   }
/*     */ 
/*     */   protected Container createButtonArea()
/*     */   {
/*  65 */     Container localContainer = super.createButtonArea();
/*     */ 
/*  67 */     if ((localContainer != null) && ((localContainer.getLayout() instanceof BasicOptionPaneUI.ButtonAreaLayout))) {
/*  68 */       ((BasicOptionPaneUI.ButtonAreaLayout)localContainer.getLayout()).setCentersChildren(false);
/*     */     }
/*  70 */     return localContainer;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumOptionPaneSize()
/*     */   {
/*  77 */     return null;
/*     */   }
/*     */ 
/*     */   protected Container createSeparator() {
/*  81 */     return new JPanel()
/*     */     {
/*     */       public Dimension getPreferredSize() {
/*  84 */         return new Dimension(10, 2);
/*     */       }
/*     */ 
/*     */       public void paint(Graphics paramAnonymousGraphics) {
/*  88 */         int i = getWidth();
/*  89 */         paramAnonymousGraphics.setColor(Color.darkGray);
/*  90 */         paramAnonymousGraphics.drawLine(0, 0, i, 0);
/*  91 */         paramAnonymousGraphics.setColor(Color.white);
/*  92 */         paramAnonymousGraphics.drawLine(0, 1, i, 1);
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   protected void addIcon(Container paramContainer)
/*     */   {
/* 104 */     Icon localIcon = getIcon();
/*     */ 
/* 106 */     if (localIcon != null) {
/* 107 */       JLabel localJLabel = new JLabel(localIcon);
/*     */ 
/* 109 */       localJLabel.setVerticalAlignment(0);
/* 110 */       paramContainer.add(localJLabel, "West");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifOptionPaneUI
 * JD-Core Version:    0.6.2
 */