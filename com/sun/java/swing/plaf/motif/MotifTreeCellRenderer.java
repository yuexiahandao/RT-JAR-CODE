/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.IconUIResource;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ 
/*     */ public class MotifTreeCellRenderer extends DefaultTreeCellRenderer
/*     */ {
/*     */   static final int LEAF_SIZE = 13;
/*  52 */   static final Icon LEAF_ICON = new IconUIResource(new TreeLeafIcon());
/*     */ 
/*     */   public static Icon loadLeafIcon()
/*     */   {
/*  59 */     return LEAF_ICON;
/*     */   }
/*     */ 
/*     */   public static class TreeLeafIcon
/*     */     implements Icon, Serializable
/*     */   {
/*     */     Color bg;
/*     */     Color shadow;
/*     */     Color highlight;
/*     */ 
/*     */     public TreeLeafIcon()
/*     */     {
/*  79 */       this.bg = UIManager.getColor("Tree.iconBackground");
/*  80 */       this.shadow = UIManager.getColor("Tree.iconShadow");
/*  81 */       this.highlight = UIManager.getColor("Tree.iconHighlight");
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/*  85 */       paramGraphics.setColor(this.bg);
/*     */ 
/*  87 */       paramInt2 -= 3;
/*  88 */       paramGraphics.fillRect(paramInt1 + 4, paramInt2 + 7, 5, 5);
/*     */ 
/*  90 */       paramGraphics.drawLine(paramInt1 + 6, paramInt2 + 6, paramInt1 + 6, paramInt2 + 6);
/*  91 */       paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 9, paramInt1 + 3, paramInt2 + 9);
/*  92 */       paramGraphics.drawLine(paramInt1 + 6, paramInt2 + 12, paramInt1 + 6, paramInt2 + 12);
/*  93 */       paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 9, paramInt1 + 9, paramInt2 + 9);
/*     */ 
/*  95 */       paramGraphics.setColor(this.highlight);
/*  96 */       paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 9, paramInt1 + 5, paramInt2 + 6);
/*  97 */       paramGraphics.drawLine(paramInt1 + 3, paramInt2 + 10, paramInt1 + 5, paramInt2 + 12);
/*     */ 
/*  99 */       paramGraphics.setColor(this.shadow);
/* 100 */       paramGraphics.drawLine(paramInt1 + 6, paramInt2 + 13, paramInt1 + 10, paramInt2 + 9);
/* 101 */       paramGraphics.drawLine(paramInt1 + 9, paramInt2 + 8, paramInt1 + 7, paramInt2 + 6);
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 105 */       return 13;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 109 */       return 13;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifTreeCellRenderer
 * JD-Core Version:    0.6.2
 */