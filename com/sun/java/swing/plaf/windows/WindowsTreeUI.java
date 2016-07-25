/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTreeUI;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ 
/*     */ public class WindowsTreeUI extends BasicTreeUI
/*     */ {
/*     */   protected static final int HALF_SIZE = 4;
/*     */   protected static final int SIZE = 9;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  60 */     return new WindowsTreeUI();
/*     */   }
/*     */ 
/*     */   protected void ensureRowsAreVisible(int paramInt1, int paramInt2)
/*     */   {
/*  69 */     if ((this.tree != null) && (paramInt1 >= 0) && (paramInt2 < getRowCount(this.tree))) {
/*  70 */       Rectangle localRectangle1 = this.tree.getVisibleRect();
/*     */       Rectangle localRectangle2;
/*  71 */       if (paramInt1 == paramInt2) {
/*  72 */         localRectangle2 = getPathBounds(this.tree, getPathForRow(this.tree, paramInt1));
/*     */ 
/*  75 */         if (localRectangle2 != null) {
/*  76 */           localRectangle2.x = localRectangle1.x;
/*  77 */           localRectangle2.width = localRectangle1.width;
/*  78 */           this.tree.scrollRectToVisible(localRectangle2);
/*     */         }
/*     */       }
/*     */       else {
/*  82 */         localRectangle2 = getPathBounds(this.tree, getPathForRow(this.tree, paramInt1));
/*     */ 
/*  84 */         if (localRectangle2 != null) {
/*  85 */           Rectangle localRectangle3 = localRectangle2;
/*  86 */           int i = localRectangle2.y;
/*  87 */           int j = i + localRectangle1.height;
/*     */ 
/*  89 */           for (int k = paramInt1 + 1; k <= paramInt2; k++) {
/*  90 */             localRectangle3 = getPathBounds(this.tree, getPathForRow(this.tree, k));
/*     */ 
/*  92 */             if ((localRectangle3 != null) && (localRectangle3.y + localRectangle3.height > j)) {
/*  93 */               k = paramInt2;
/*     */             }
/*     */           }
/*     */ 
/*  97 */           if (localRectangle3 == null) {
/*  98 */             return;
/*     */           }
/*     */ 
/* 101 */           this.tree.scrollRectToVisible(new Rectangle(localRectangle1.x, i, 1, localRectangle3.y + localRectangle3.height - i));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected TreeCellRenderer createDefaultCellRenderer()
/*     */   {
/* 117 */     return new WindowsTreeCellRenderer();
/*     */   }
/*     */ 
/*     */   public static class CollapsedIcon extends WindowsTreeUI.ExpandedIcon
/*     */   {
/*     */     public static Icon createCollapsedIcon()
/*     */     {
/* 184 */       return new CollapsedIcon();
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 188 */       XPStyle.Skin localSkin = getSkin(paramComponent);
/* 189 */       if (localSkin != null) {
/* 190 */         localSkin.paintSkin(paramGraphics, paramInt1, paramInt2, TMSchema.State.CLOSED);
/*     */       } else {
/* 192 */         super.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/* 193 */         paramGraphics.drawLine(paramInt1 + 4, paramInt2 + 2, paramInt1 + 4, paramInt2 + 6);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ExpandedIcon
/*     */     implements Icon, Serializable
/*     */   {
/*     */     public static Icon createExpandedIcon()
/*     */     {
/* 133 */       return new ExpandedIcon();
/*     */     }
/*     */ 
/*     */     XPStyle.Skin getSkin(Component paramComponent) {
/* 137 */       XPStyle localXPStyle = XPStyle.getXP();
/* 138 */       return localXPStyle != null ? localXPStyle.getSkin(paramComponent, TMSchema.Part.TVP_GLYPH) : null;
/*     */     }
/*     */ 
/*     */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 142 */       XPStyle.Skin localSkin = getSkin(paramComponent);
/* 143 */       if (localSkin != null) {
/* 144 */         localSkin.paintSkin(paramGraphics, paramInt1, paramInt2, TMSchema.State.OPENED);
/* 145 */         return;
/*     */       }
/*     */ 
/* 148 */       Color localColor = paramComponent.getBackground();
/*     */ 
/* 150 */       if (localColor != null)
/* 151 */         paramGraphics.setColor(localColor);
/*     */       else
/* 153 */         paramGraphics.setColor(Color.white);
/* 154 */       paramGraphics.fillRect(paramInt1, paramInt2, 8, 8);
/* 155 */       paramGraphics.setColor(Color.gray);
/* 156 */       paramGraphics.drawRect(paramInt1, paramInt2, 8, 8);
/* 157 */       paramGraphics.setColor(Color.black);
/* 158 */       paramGraphics.drawLine(paramInt1 + 2, paramInt2 + 4, paramInt1 + 6, paramInt2 + 4);
/*     */     }
/*     */ 
/*     */     public int getIconWidth() {
/* 162 */       XPStyle.Skin localSkin = getSkin(null);
/* 163 */       return localSkin != null ? localSkin.getWidth() : 9;
/*     */     }
/*     */ 
/*     */     public int getIconHeight() {
/* 167 */       XPStyle.Skin localSkin = getSkin(null);
/* 168 */       return localSkin != null ? localSkin.getHeight() : 9;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class WindowsTreeCellRenderer extends DefaultTreeCellRenderer
/*     */   {
/*     */     public WindowsTreeCellRenderer()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Component getTreeCellRendererComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4)
/*     */     {
/* 213 */       super.getTreeCellRendererComponent(paramJTree, paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, paramBoolean4);
/*     */ 
/* 217 */       if (!paramJTree.isEnabled()) {
/* 218 */         setEnabled(false);
/* 219 */         if (paramBoolean3)
/* 220 */           setDisabledIcon(getLeafIcon());
/* 221 */         else if (paramBoolean1)
/* 222 */           setDisabledIcon(getOpenIcon());
/*     */         else
/* 224 */           setDisabledIcon(getClosedIcon());
/*     */       }
/*     */       else
/*     */       {
/* 228 */         setEnabled(true);
/* 229 */         if (paramBoolean3)
/* 230 */           setIcon(getLeafIcon());
/* 231 */         else if (paramBoolean1)
/* 232 */           setIcon(getOpenIcon());
/*     */         else {
/* 234 */           setIcon(getClosedIcon());
/*     */         }
/*     */       }
/* 237 */       return this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsTreeUI
 * JD-Core Version:    0.6.2
 */