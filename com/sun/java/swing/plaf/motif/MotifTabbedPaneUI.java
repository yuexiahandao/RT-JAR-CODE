/*     */ package com.sun.java.swing.plaf.motif;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTabbedPaneUI;
/*     */ 
/*     */ public class MotifTabbedPaneUI extends BasicTabbedPaneUI
/*     */ {
/*     */   protected Color unselectedTabBackground;
/*     */   protected Color unselectedTabForeground;
/*     */   protected Color unselectedTabShadow;
/*     */   protected Color unselectedTabHighlight;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  63 */     return new MotifTabbedPaneUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  71 */     super.installDefaults();
/*     */ 
/*  73 */     this.unselectedTabBackground = UIManager.getColor("TabbedPane.unselectedTabBackground");
/*  74 */     this.unselectedTabForeground = UIManager.getColor("TabbedPane.unselectedTabForeground");
/*  75 */     this.unselectedTabShadow = UIManager.getColor("TabbedPane.unselectedTabShadow");
/*  76 */     this.unselectedTabHighlight = UIManager.getColor("TabbedPane.unselectedTabHighlight");
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults() {
/*  80 */     super.uninstallDefaults();
/*     */ 
/*  82 */     this.unselectedTabBackground = null;
/*  83 */     this.unselectedTabForeground = null;
/*  84 */     this.unselectedTabShadow = null;
/*  85 */     this.unselectedTabHighlight = null;
/*     */   }
/*     */ 
/*     */   protected void paintContentBorderTopEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/*  93 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*     */ 
/*  95 */     paramGraphics.setColor(this.lightHighlight);
/*     */ 
/* 100 */     if ((paramInt1 != 1) || (paramInt2 < 0) || (localRectangle.x < paramInt3) || (localRectangle.x > paramInt3 + paramInt5))
/*     */     {
/* 102 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3 + paramInt5 - 2, paramInt4);
/*     */     }
/*     */     else {
/* 105 */       paramGraphics.drawLine(paramInt3, paramInt4, localRectangle.x - 1, paramInt4);
/* 106 */       if (localRectangle.x + localRectangle.width < paramInt3 + paramInt5 - 2)
/* 107 */         paramGraphics.drawLine(localRectangle.x + localRectangle.width, paramInt4, paramInt3 + paramInt5 - 2, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintContentBorderBottomEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 116 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*     */ 
/* 118 */     paramGraphics.setColor(this.shadow);
/*     */ 
/* 123 */     if ((paramInt1 != 3) || (paramInt2 < 0) || (localRectangle.x < paramInt3) || (localRectangle.x > paramInt3 + paramInt5))
/*     */     {
/* 125 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/*     */     }
/*     */     else {
/* 128 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + paramInt6 - 1, localRectangle.x - 1, paramInt4 + paramInt6 - 1);
/* 129 */       if (localRectangle.x + localRectangle.width < paramInt3 + paramInt5 - 2)
/* 130 */         paramGraphics.drawLine(localRectangle.x + localRectangle.width, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintContentBorderRightEdge(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*     */   {
/* 138 */     Rectangle localRectangle = paramInt2 < 0 ? null : getTabBounds(paramInt2, this.calcRect);
/*     */ 
/* 140 */     paramGraphics.setColor(this.shadow);
/*     */ 
/* 144 */     if ((paramInt1 != 4) || (paramInt2 < 0) || (localRectangle.y < paramInt4) || (localRectangle.y > paramInt4 + paramInt6))
/*     */     {
/* 146 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4 + 1, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/*     */     }
/*     */     else {
/* 149 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4 + 1, paramInt3 + paramInt5 - 1, localRectangle.y - 1);
/* 150 */       if (localRectangle.y + localRectangle.height < paramInt4 + paramInt6 - 2)
/* 151 */         paramGraphics.drawLine(paramInt3 + paramInt5 - 1, localRectangle.y + localRectangle.height, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintTabBackground(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*     */   {
/* 161 */     paramGraphics.setColor(paramBoolean ? this.tabPane.getBackgroundAt(paramInt2) : this.unselectedTabBackground);
/* 162 */     switch (paramInt1) {
/*     */     case 2:
/* 164 */       paramGraphics.fillRect(paramInt3 + 1, paramInt4 + 1, paramInt5 - 1, paramInt6 - 2);
/* 165 */       break;
/*     */     case 4:
/* 167 */       paramGraphics.fillRect(paramInt3, paramInt4 + 1, paramInt5 - 1, paramInt6 - 2);
/* 168 */       break;
/*     */     case 3:
/* 170 */       paramGraphics.fillRect(paramInt3 + 1, paramInt4, paramInt5 - 2, paramInt6 - 3);
/* 171 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4 + paramInt6 - 3, paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 3);
/* 172 */       paramGraphics.drawLine(paramInt3 + 3, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 4, paramInt4 + paramInt6 - 2);
/* 173 */       break;
/*     */     case 1:
/*     */     default:
/* 176 */       paramGraphics.fillRect(paramInt3 + 1, paramInt4 + 3, paramInt5 - 2, paramInt6 - 3);
/* 177 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4 + 2, paramInt3 + paramInt5 - 3, paramInt4 + 2);
/* 178 */       paramGraphics.drawLine(paramInt3 + 3, paramInt4 + 1, paramInt3 + paramInt5 - 4, paramInt4 + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintTabBorder(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean)
/*     */   {
/* 187 */     paramGraphics.setColor(paramBoolean ? this.lightHighlight : this.unselectedTabHighlight);
/*     */ 
/* 189 */     switch (paramInt1) {
/*     */     case 2:
/* 191 */       paramGraphics.drawLine(paramInt3, paramInt4 + 2, paramInt3, paramInt4 + paramInt6 - 3);
/* 192 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + 1, paramInt3 + 1, paramInt4 + 2);
/* 193 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4, paramInt3 + 2, paramInt4 + 1);
/* 194 */       paramGraphics.drawLine(paramInt3 + 3, paramInt4, paramInt3 + paramInt5 - 1, paramInt4);
/* 195 */       paramGraphics.setColor(paramBoolean ? this.shadow : this.unselectedTabShadow);
/* 196 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + paramInt6 - 3, paramInt3 + 1, paramInt4 + paramInt6 - 2);
/* 197 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4 + paramInt6 - 2, paramInt3 + 2, paramInt4 + paramInt6 - 1);
/* 198 */       paramGraphics.drawLine(paramInt3 + 3, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/* 199 */       break;
/*     */     case 4:
/* 201 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3 + paramInt5 - 3, paramInt4);
/* 202 */       paramGraphics.setColor(paramBoolean ? this.shadow : this.unselectedTabShadow);
/* 203 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 3, paramInt4, paramInt3 + paramInt5 - 3, paramInt4 + 1);
/* 204 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 1, paramInt3 + paramInt5 - 2, paramInt4 + 2);
/* 205 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4 + 2, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 3);
/* 206 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 3, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2);
/* 207 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 1);
/* 208 */       paramGraphics.drawLine(paramInt3, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 1);
/* 209 */       break;
/*     */     case 3:
/* 211 */       paramGraphics.drawLine(paramInt3, paramInt4, paramInt3, paramInt4 + paramInt6 - 3);
/* 212 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + paramInt6 - 3, paramInt3 + 1, paramInt4 + paramInt6 - 2);
/* 213 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4 + paramInt6 - 2, paramInt3 + 2, paramInt4 + paramInt6 - 1);
/* 214 */       paramGraphics.setColor(paramBoolean ? this.shadow : this.unselectedTabShadow);
/* 215 */       paramGraphics.drawLine(paramInt3 + 3, paramInt4 + paramInt6 - 1, paramInt3 + paramInt5 - 4, paramInt4 + paramInt6 - 1);
/* 216 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 2, paramInt3 + paramInt5 - 3, paramInt4 + paramInt6 - 1);
/* 217 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 3, paramInt3 + paramInt5 - 2, paramInt4 + paramInt6 - 2);
/* 218 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 3);
/* 219 */       break;
/*     */     case 1:
/*     */     default:
/* 222 */       paramGraphics.drawLine(paramInt3, paramInt4 + 2, paramInt3, paramInt4 + paramInt6 - 1);
/* 223 */       paramGraphics.drawLine(paramInt3 + 1, paramInt4 + 1, paramInt3 + 1, paramInt4 + 2);
/* 224 */       paramGraphics.drawLine(paramInt3 + 2, paramInt4, paramInt3 + 2, paramInt4 + 1);
/* 225 */       paramGraphics.drawLine(paramInt3 + 3, paramInt4, paramInt3 + paramInt5 - 4, paramInt4);
/* 226 */       paramGraphics.setColor(paramBoolean ? this.shadow : this.unselectedTabShadow);
/* 227 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 3, paramInt4, paramInt3 + paramInt5 - 3, paramInt4 + 1);
/* 228 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 2, paramInt4 + 1, paramInt3 + paramInt5 - 2, paramInt4 + 2);
/* 229 */       paramGraphics.drawLine(paramInt3 + paramInt5 - 1, paramInt4 + 2, paramInt3 + paramInt5 - 1, paramInt4 + paramInt6 - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintFocusIndicator(Graphics paramGraphics, int paramInt1, Rectangle[] paramArrayOfRectangle, int paramInt2, Rectangle paramRectangle1, Rectangle paramRectangle2, boolean paramBoolean)
/*     */   {
/* 238 */     Rectangle localRectangle = paramArrayOfRectangle[paramInt2];
/* 239 */     if ((this.tabPane.hasFocus()) && (paramBoolean))
/*     */     {
/* 241 */       paramGraphics.setColor(this.focus);
/*     */       int i;
/*     */       int j;
/*     */       int k;
/*     */       int m;
/* 242 */       switch (paramInt1) {
/*     */       case 2:
/* 244 */         i = localRectangle.x + 3;
/* 245 */         j = localRectangle.y + 3;
/* 246 */         k = localRectangle.width - 6;
/* 247 */         m = localRectangle.height - 7;
/* 248 */         break;
/*     */       case 4:
/* 250 */         i = localRectangle.x + 2;
/* 251 */         j = localRectangle.y + 3;
/* 252 */         k = localRectangle.width - 6;
/* 253 */         m = localRectangle.height - 7;
/* 254 */         break;
/*     */       case 3:
/* 256 */         i = localRectangle.x + 3;
/* 257 */         j = localRectangle.y + 2;
/* 258 */         k = localRectangle.width - 7;
/* 259 */         m = localRectangle.height - 6;
/* 260 */         break;
/*     */       case 1:
/*     */       default:
/* 263 */         i = localRectangle.x + 3;
/* 264 */         j = localRectangle.y + 3;
/* 265 */         k = localRectangle.width - 7;
/* 266 */         m = localRectangle.height - 6;
/*     */       }
/* 268 */       paramGraphics.drawRect(i, j, k, m);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int getTabRunIndent(int paramInt1, int paramInt2) {
/* 273 */     return paramInt2 * 3;
/*     */   }
/*     */ 
/*     */   protected int getTabRunOverlay(int paramInt) {
/* 277 */     this.tabRunOverlay = ((paramInt == 2) || (paramInt == 4) ? (int)Math.round(this.maxTabWidth * 0.1D) : (int)Math.round(this.maxTabHeight * 0.22D));
/*     */ 
/* 283 */     switch (paramInt) {
/*     */     case 2:
/* 285 */       if (this.tabRunOverlay > this.tabInsets.right - 2)
/* 286 */         this.tabRunOverlay = (this.tabInsets.right - 2); break;
/*     */     case 4:
/* 289 */       if (this.tabRunOverlay > this.tabInsets.left - 2)
/* 290 */         this.tabRunOverlay = (this.tabInsets.left - 2); break;
/*     */     case 1:
/* 293 */       if (this.tabRunOverlay > this.tabInsets.bottom - 2)
/* 294 */         this.tabRunOverlay = (this.tabInsets.bottom - 2); break;
/*     */     case 3:
/* 297 */       if (this.tabRunOverlay > this.tabInsets.top - 2) {
/* 298 */         this.tabRunOverlay = (this.tabInsets.top - 2);
/*     */       }
/*     */       break;
/*     */     }
/*     */ 
/* 303 */     return this.tabRunOverlay;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifTabbedPaneUI
 * JD-Core Version:    0.6.2
 */