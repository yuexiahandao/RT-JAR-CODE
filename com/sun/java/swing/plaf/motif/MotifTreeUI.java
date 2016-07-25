/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTreeUI;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ 
/*     */ public class MotifTreeUI extends BasicTreeUI
/*     */ {
/*     */   static final int HALF_SIZE = 7;
/*     */   static final int SIZE = 14;
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/*  64 */     super.installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void paintVerticalLine(Graphics paramGraphics, JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  71 */     if (this.tree.getComponentOrientation().isLeftToRight())
/*  72 */       paramGraphics.fillRect(paramInt1, paramInt2, 2, paramInt3 - paramInt2 + 2);
/*     */     else
/*  74 */       paramGraphics.fillRect(paramInt1 - 1, paramInt2, 2, paramInt3 - paramInt2 + 2);
/*     */   }
/*     */ 
/*     */   protected void paintHorizontalLine(Graphics paramGraphics, JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  80 */     paramGraphics.fillRect(paramInt2, paramInt1, paramInt3 - paramInt2 + 1, 2);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 155 */     return new MotifTreeUI();
/*     */   }
/*     */ 
/*     */   public TreeCellRenderer createDefaultCellRenderer()
/*     */   {
/* 163 */     return new MotifTreeCellRenderer();
/*     */   }
/*     */ 
/*     */   public static class MotifCollapsedIcon extends MotifTreeUI.MotifExpandedIcon
/*     */   {
/*     */     public static Icon createCollapsedIcon()
/*     */     {
/* 144 */       return new MotifCollapsedIcon();
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 148 */       super.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/* 149 */       paramGraphics.drawLine(paramInt1 + 7 - 1, paramInt2 + 3, paramInt1 + 7 - 1, paramInt2 + 10);
/* 150 */       paramGraphics.drawLine(paramInt1 + 7, paramInt2 + 3, paramInt1 + 7, paramInt2 + 10);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MotifExpandedIcon
/*     */     implements Icon, Serializable
/*     */   {
/*     */     static Color bg;
/*     */     static Color fg;
/*     */     static Color highlight;
/*     */     static Color shadow;
/*     */ 
/*     */     public MotifExpandedIcon()
/*     */     {
/* 101 */       bg = UIManager.getColor("Tree.iconBackground");
/* 102 */       fg = UIManager.getColor("Tree.iconForeground");
/* 103 */       highlight = UIManager.getColor("Tree.iconHighlight");
/* 104 */       shadow = UIManager.getColor("Tree.iconShadow");
/*     */     }
/*     */ 
/*     */     public static Icon createExpandedIcon() {
/* 108 */       return new MotifExpandedIcon();
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 112 */       paramGraphics.setColor(highlight);
/* 113 */       paramGraphics.drawLine(paramInt1, paramInt2, paramInt1 + 14 - 1, paramInt2);
/* 114 */       paramGraphics.drawLine(paramInt1, paramInt2 + 1, paramInt1, paramInt2 + 14 - 1);
/*     */ 
/* 116 */       paramGraphics.setColor(shadow);
/* 117 */       paramGraphics.drawLine(paramInt1 + 14 - 1, paramInt2 + 1, paramInt1 + 14 - 1, paramInt2 + 14 - 1);
/* 118 */       paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 14 - 1, paramInt1 + 14 - 1, paramInt2 + 14 - 1);
/*     */ 
/* 120 */       paramGraphics.setColor(bg);
/* 121 */       paramGraphics.fillRect(paramInt1 + 1, paramInt2 + 1, 12, 12);
/*     */ 
/* 123 */       paramGraphics.setColor(fg);
/* 124 */       paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 7 - 1, paramInt1 + 14 - 4, paramInt2 + 7 - 1);
/* 125 */       paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 7, paramInt1 + 14 - 4, paramInt2 + 7);
/*     */     }
/*     */     public int getIconWidth() {
/* 128 */       return 14; } 
/* 129 */     public int getIconHeight() { return 14; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifTreeUI
 * JD-Core Version:    0.6.2
 */