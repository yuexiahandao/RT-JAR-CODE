/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTabbedPaneUI;
/*     */ 
/*     */ public class WindowsTabbedPaneUI extends BasicTabbedPaneUI
/*     */ {
/*     */   private static Set<KeyStroke> managingFocusForwardTraversalKeys;
/*     */   private static Set<KeyStroke> managingFocusBackwardTraversalKeys;
/*  64 */   private boolean contentOpaque = true;
/*     */ 
/*     */   protected void installDefaults() {
/*  67 */     super.installDefaults();
/*  68 */     this.contentOpaque = UIManager.getBoolean("TabbedPane.contentOpaque");
/*     */ 
/*  71 */     if (managingFocusForwardTraversalKeys == null) {
/*  72 */       managingFocusForwardTraversalKeys = new HashSet();
/*  73 */       managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 0));
/*     */     }
/*  75 */     this.tabPane.setFocusTraversalKeys(0, managingFocusForwardTraversalKeys);
/*     */ 
/*  77 */     if (managingFocusBackwardTraversalKeys == null) {
/*  78 */       managingFocusBackwardTraversalKeys = new HashSet();
/*  79 */       managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 1));
/*     */     }
/*  81 */     this.tabPane.setFocusTraversalKeys(1, managingFocusBackwardTraversalKeys);
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*  87 */     this.tabPane.setFocusTraversalKeys(0, null);
/*  88 */     this.tabPane.setFocusTraversalKeys(1, null);
/*  89 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  93 */     return new WindowsTabbedPaneUI();
/*     */   }
/*     */ 
/*     */   protected void setRolloverTab(int paramInt)
/*     */   {
/*  98 */     if (XPStyle.getXP() != null) {
/*  99 */       int i = getRolloverTab();
/* 100 */       super.setRolloverTab(paramInt);
/* 101 */       Rectangle localRectangle1 = null;
/* 102 */       Rectangle localRectangle2 = null;
/* 103 */       if ((i >= 0) && (i < this.tabPane.getTabCount())) {
/* 104 */         localRectangle1 = getTabBounds(this.tabPane, i);
/*     */       }
/* 106 */       if (paramInt >= 0) {
/* 107 */         localRectangle2 = getTabBounds(this.tabPane, paramInt);
/*     */       }
/* 109 */       if (localRectangle1 != null) {
/* 110 */         if (localRectangle2 != null)
/* 111 */           this.tabPane.repaint(localRectangle1.union(localRectangle2));
/*     */         else
/* 113 */           this.tabPane.repaint(localRectangle1);
/*     */       }
/* 115 */       else if (localRectangle2 != null)
/* 116 */         this.tabPane.repaint(localRectangle2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintContentBorder(Graphics paramGraphics, int paramInt1, int paramInt2)
/*     */   {
/* 122 */     XPStyle localXPStyle = XPStyle.getXP();
/* 123 */     if ((localXPStyle != null) && ((this.contentOpaque) || (this.tabPane.isOpaque()))) {
/* 124 */       XPStyle.Skin localSkin = localXPStyle.getSkin(this.tabPane, TMSchema.Part.TABP_PANE);
/* 125 */       if (localSkin != null) {
/* 126 */         Insets localInsets1 = this.tabPane.getInsets();
/*     */ 
/* 129 */         Insets localInsets2 = UIManager.getInsets("TabbedPane.tabAreaInsets");
/* 130 */         int i = localInsets1.left;
/* 131 */         int j = localInsets1.top;
/* 132 */         int k = this.tabPane.getWidth() - localInsets1.right - localInsets1.left;
/* 133 */         int m = this.tabPane.getHeight() - localInsets1.top - localInsets1.bottom;
/*     */         int n;
/* 136 */         if ((paramInt1 == 2) || (paramInt1 == 4)) {
/* 137 */           n = calculateTabAreaWidth(paramInt1, this.runCount, this.maxTabWidth);
/* 138 */           if (paramInt1 == 2) {
/* 139 */             i += n - localInsets2.bottom;
/*     */           }
/* 141 */           k -= n - localInsets2.bottom;
/*     */         } else {
/* 143 */           n = calculateTabAreaHeight(paramInt1, this.runCount, this.maxTabHeight);
/* 144 */           if (paramInt1 == 1) {
/* 145 */             j += n - localInsets2.bottom;
/*     */           }
/* 147 */           m -= n - localInsets2.bottom;
/*     */         }
/*     */ 
/* 150 */         paintRotatedSkin(paramGraphics, localSkin, paramInt1, i, j, k, m, null);
/* 151 */         return;
/*     */       }
/*     */     }
/* 154 */     super.paintContentBorder(paramGraphics, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected void paintTabBackground(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*     */   {
/* 159 */     if (XPStyle.getXP() == null)
/* 160 */       super.paintTabBackground(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void paintTabBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*     */   {
/* 166 */     XPStyle localXPStyle = XPStyle.getXP();
/* 167 */     if (localXPStyle != null)
/*     */     {
/* 170 */       int i = this.tabPane.getTabCount();
/* 171 */       int j = getRunForTab(i, paramInt2);
/*     */       TMSchema.Part localPart;
/* 172 */       if (this.tabRuns[j] == paramInt2) {
/* 173 */         localPart = TMSchema.Part.TABP_TABITEMLEFTEDGE;
/* 174 */       } else if ((i > 1) && (lastTabInRun(i, j) == paramInt2)) {
/* 175 */         localPart = TMSchema.Part.TABP_TABITEMRIGHTEDGE;
/* 176 */         if (paramBoolean)
/*     */         {
/* 178 */           if ((paramInt1 == 1) || (paramInt1 == 3))
/* 179 */             paramInt5++;
/*     */           else
/* 181 */             paramInt6++;
/*     */         }
/*     */       }
/*     */       else {
/* 185 */         localPart = TMSchema.Part.TABP_TABITEM;
/*     */       }
/*     */ 
/* 188 */       TMSchema.State localState = TMSchema.State.NORMAL;
/* 189 */       if (paramBoolean)
/* 190 */         localState = TMSchema.State.SELECTED;
/* 191 */       else if (paramInt2 == getRolloverTab()) {
/* 192 */         localState = TMSchema.State.HOT;
/*     */       }
/*     */ 
/* 195 */       paintRotatedSkin(paramGraphics, localXPStyle.getSkin(this.tabPane, localPart), paramInt1, paramInt3, paramInt4, paramInt5, paramInt6, localState);
/*     */     } else {
/* 197 */       super.paintTabBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void paintRotatedSkin(Graphics paramGraphics, XPStyle.Skin paramSkin, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, TMSchema.State paramState)
/*     */   {
/* 203 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
/* 204 */     localGraphics2D.translate(paramInt2, paramInt3);
/* 205 */     switch (paramInt1) { case 4:
/* 206 */       localGraphics2D.translate(paramInt4, 0);
/* 207 */       localGraphics2D.rotate(Math.toRadians(90.0D));
/* 208 */       paramSkin.paintSkin(localGraphics2D, 0, 0, paramInt5, paramInt4, paramState);
/* 209 */       break;
/*     */     case 2:
/* 211 */       localGraphics2D.scale(-1.0D, 1.0D);
/* 212 */       localGraphics2D.rotate(Math.toRadians(90.0D));
/* 213 */       paramSkin.paintSkin(localGraphics2D, 0, 0, paramInt5, paramInt4, paramState);
/* 214 */       break;
/*     */     case 3:
/* 216 */       localGraphics2D.translate(0, paramInt5);
/* 217 */       localGraphics2D.scale(-1.0D, 1.0D);
/* 218 */       localGraphics2D.rotate(Math.toRadians(180.0D));
/* 219 */       paramSkin.paintSkin(localGraphics2D, 0, 0, paramInt4, paramInt5, paramState);
/* 220 */       break;
/*     */     case 1:
/*     */     default:
/* 223 */       paramSkin.paintSkin(localGraphics2D, 0, 0, paramInt4, paramInt5, paramState);
/*     */     }
/* 225 */     localGraphics2D.dispose();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI
 * JD-Core Version:    0.6.2
 */