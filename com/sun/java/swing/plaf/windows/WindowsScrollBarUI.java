/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.HashMap;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicArrowButton;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI.ArrowButtonListener;
/*     */ 
/*     */ public class WindowsScrollBarUI extends BasicScrollBarUI
/*     */ {
/*     */   private Grid thumbGrid;
/*     */   private Grid highlightGrid;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  62 */     return new WindowsScrollBarUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/*  66 */     super.installDefaults();
/*     */ 
/*  68 */     if (XPStyle.getXP() != null)
/*  69 */       this.scrollbar.setBorder(null);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  74 */     super.uninstallUI(paramJComponent);
/*  75 */     this.thumbGrid = (this.highlightGrid = null);
/*     */   }
/*     */ 
/*     */   protected void configureScrollBarColors() {
/*  79 */     super.configureScrollBarColors();
/*  80 */     Color localColor = UIManager.getColor("ScrollBar.trackForeground");
/*  81 */     if ((localColor != null) && (this.trackColor != null)) {
/*  82 */       this.thumbGrid = Grid.getGrid(localColor, this.trackColor);
/*     */     }
/*     */ 
/*  85 */     localColor = UIManager.getColor("ScrollBar.trackHighlightForeground");
/*  86 */     if ((localColor != null) && (this.trackHighlightColor != null))
/*  87 */       this.highlightGrid = Grid.getGrid(localColor, this.trackHighlightColor);
/*     */   }
/*     */ 
/*     */   protected JButton createDecreaseButton(int paramInt)
/*     */   {
/*  92 */     return new WindowsArrowButton(paramInt, UIManager.getColor("ScrollBar.thumb"), UIManager.getColor("ScrollBar.thumbShadow"), UIManager.getColor("ScrollBar.thumbDarkShadow"), UIManager.getColor("ScrollBar.thumbHighlight"));
/*     */   }
/*     */ 
/*     */   protected JButton createIncreaseButton(int paramInt)
/*     */   {
/* 100 */     return new WindowsArrowButton(paramInt, UIManager.getColor("ScrollBar.thumb"), UIManager.getColor("ScrollBar.thumbShadow"), UIManager.getColor("ScrollBar.thumbDarkShadow"), UIManager.getColor("ScrollBar.thumbHighlight"));
/*     */   }
/*     */ 
/*     */   protected BasicScrollBarUI.ArrowButtonListener createArrowButtonListener()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 269	com/sun/java/swing/plaf/windows/XPStyle:isVista	()Z
/*     */     //   3: ifeq +12 -> 15
/*     */     //   6: new 123	com/sun/java/swing/plaf/windows/WindowsScrollBarUI$1
/*     */     //   9: dup
/*     */     //   10: aload_0
/*     */     //   11: invokespecial 265	com/sun/java/swing/plaf/windows/WindowsScrollBarUI$1:<init>	(Lcom/sun/java/swing/plaf/windows/WindowsScrollBarUI;)V
/*     */     //   14: areturn
/*     */     //   15: aload_0
/*     */     //   16: invokespecial 295	javax/swing/plaf/basic/BasicScrollBarUI:createArrowButtonListener	()Ljavax/swing/plaf/basic/BasicScrollBarUI$ArrowButtonListener;
/*     */     //   19: areturn
/*     */   }
/*     */ 
/*     */   protected void paintTrack(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*     */   {
/* 135 */     int i = this.scrollbar.getOrientation() == 1 ? 1 : 0;
/*     */ 
/* 137 */     XPStyle localXPStyle = XPStyle.getXP();
/* 138 */     if (localXPStyle != null) {
/* 139 */       JScrollBar localJScrollBar = (JScrollBar)paramJComponent;
/* 140 */       TMSchema.State localState = TMSchema.State.NORMAL;
/*     */ 
/* 142 */       if (!localJScrollBar.isEnabled()) {
/* 143 */         localState = TMSchema.State.DISABLED;
/*     */       }
/* 145 */       TMSchema.Part localPart = i != 0 ? TMSchema.Part.SBP_LOWERTRACKVERT : TMSchema.Part.SBP_LOWERTRACKHORZ;
/* 146 */       localXPStyle.getSkin(localJScrollBar, localPart).paintSkin(paramGraphics, paramRectangle, localState);
/* 147 */     } else if (this.thumbGrid == null) {
/* 148 */       super.paintTrack(paramGraphics, paramJComponent, paramRectangle);
/*     */     }
/*     */     else {
/* 151 */       this.thumbGrid.paint(paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*     */ 
/* 153 */       if (this.trackHighlight == 1) {
/* 154 */         paintDecreaseHighlight(paramGraphics);
/*     */       }
/* 156 */       else if (this.trackHighlight == 2)
/* 157 */         paintIncreaseHighlight(paramGraphics);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintThumb(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*     */   {
/* 163 */     int i = this.scrollbar.getOrientation() == 1 ? 1 : 0;
/*     */ 
/* 165 */     XPStyle localXPStyle = XPStyle.getXP();
/* 166 */     if (localXPStyle != null) {
/* 167 */       JScrollBar localJScrollBar = (JScrollBar)paramJComponent;
/* 168 */       TMSchema.State localState = TMSchema.State.NORMAL;
/* 169 */       if (!localJScrollBar.isEnabled())
/* 170 */         localState = TMSchema.State.DISABLED;
/* 171 */       else if (this.isDragging)
/* 172 */         localState = TMSchema.State.PRESSED;
/* 173 */       else if (isThumbRollover())
/* 174 */         localState = TMSchema.State.HOT;
/* 175 */       else if ((XPStyle.isVista()) && (
/* 176 */         ((this.incrButton != null) && (this.incrButton.getModel().isRollover())) || ((this.decrButton != null) && (this.decrButton.getModel().isRollover()))))
/*     */       {
/* 178 */         localState = TMSchema.State.HOVER;
/*     */       }
/*     */ 
/* 182 */       TMSchema.Part localPart1 = i != 0 ? TMSchema.Part.SBP_THUMBBTNVERT : TMSchema.Part.SBP_THUMBBTNHORZ;
/* 183 */       localXPStyle.getSkin(localJScrollBar, localPart1).paintSkin(paramGraphics, paramRectangle, localState);
/*     */ 
/* 185 */       TMSchema.Part localPart2 = i != 0 ? TMSchema.Part.SBP_GRIPPERVERT : TMSchema.Part.SBP_GRIPPERHORZ;
/* 186 */       XPStyle.Skin localSkin = localXPStyle.getSkin(localJScrollBar, localPart2);
/* 187 */       Insets localInsets = localXPStyle.getMargin(paramJComponent, localPart1, null, TMSchema.Prop.CONTENTMARGINS);
/* 188 */       if ((localInsets == null) || ((i != 0) && (paramRectangle.height - localInsets.top - localInsets.bottom >= localSkin.getHeight())) || ((i == 0) && (paramRectangle.width - localInsets.left - localInsets.right >= localSkin.getWidth())))
/*     */       {
/* 193 */         localSkin.paintSkin(paramGraphics, paramRectangle.x + (paramRectangle.width - localSkin.getWidth()) / 2, paramRectangle.y + (paramRectangle.height - localSkin.getHeight()) / 2, localSkin.getWidth(), localSkin.getHeight(), localState);
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 199 */       super.paintThumb(paramGraphics, paramJComponent, paramRectangle);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintDecreaseHighlight(Graphics paramGraphics)
/*     */   {
/* 205 */     if (this.highlightGrid == null) {
/* 206 */       super.paintDecreaseHighlight(paramGraphics);
/*     */     }
/*     */     else {
/* 209 */       Insets localInsets = this.scrollbar.getInsets();
/* 210 */       Rectangle localRectangle = getThumbBounds();
/*     */       int i;
/*     */       int j;
/*     */       int k;
/*     */       int m;
/* 213 */       if (this.scrollbar.getOrientation() == 1) {
/* 214 */         i = localInsets.left;
/* 215 */         j = this.decrButton.getY() + this.decrButton.getHeight();
/* 216 */         k = this.scrollbar.getWidth() - (localInsets.left + localInsets.right);
/* 217 */         m = localRectangle.y - j;
/*     */       }
/*     */       else {
/* 220 */         i = this.decrButton.getX() + this.decrButton.getHeight();
/* 221 */         j = localInsets.top;
/* 222 */         k = localRectangle.x - i;
/* 223 */         m = this.scrollbar.getHeight() - (localInsets.top + localInsets.bottom);
/*     */       }
/* 225 */       this.highlightGrid.paint(paramGraphics, i, j, k, m);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintIncreaseHighlight(Graphics paramGraphics)
/*     */   {
/* 231 */     if (this.highlightGrid == null) {
/* 232 */       super.paintDecreaseHighlight(paramGraphics);
/*     */     }
/*     */     else {
/* 235 */       Insets localInsets = this.scrollbar.getInsets();
/* 236 */       Rectangle localRectangle = getThumbBounds();
/*     */       int i;
/*     */       int j;
/*     */       int k;
/*     */       int m;
/* 239 */       if (this.scrollbar.getOrientation() == 1) {
/* 240 */         i = localInsets.left;
/* 241 */         j = localRectangle.y + localRectangle.height;
/* 242 */         k = this.scrollbar.getWidth() - (localInsets.left + localInsets.right);
/* 243 */         m = this.incrButton.getY() - j;
/*     */       }
/*     */       else {
/* 246 */         i = localRectangle.x + localRectangle.width;
/* 247 */         j = localInsets.top;
/* 248 */         k = this.incrButton.getX() - i;
/* 249 */         m = this.scrollbar.getHeight() - (localInsets.top + localInsets.bottom);
/*     */       }
/* 251 */       this.highlightGrid.paint(paramGraphics, i, j, k, m);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setThumbRollover(boolean paramBoolean)
/*     */   {
/* 262 */     boolean bool = isThumbRollover();
/* 263 */     super.setThumbRollover(paramBoolean);
/*     */ 
/* 266 */     if ((XPStyle.isVista()) && (paramBoolean != bool))
/* 267 */       this.scrollbar.repaint();
/*     */   }
/*     */ 
/*     */   private static class Grid
/*     */   {
/*     */     private static final int BUFFER_SIZE = 64;
/* 377 */     private static HashMap<String, WeakReference<Grid>> map = new HashMap();
/*     */     private BufferedImage image;
/*     */ 
/*     */     public static Grid getGrid(Color paramColor1, Color paramColor2)
/*     */     {
/* 381 */       String str = paramColor1.getRGB() + " " + paramColor2.getRGB();
/* 382 */       WeakReference localWeakReference = (WeakReference)map.get(str);
/* 383 */       Grid localGrid = localWeakReference == null ? null : (Grid)localWeakReference.get();
/* 384 */       if (localGrid == null) {
/* 385 */         localGrid = new Grid(paramColor1, paramColor2);
/* 386 */         map.put(str, new WeakReference(localGrid));
/*     */       }
/* 388 */       return localGrid;
/*     */     }
/*     */ 
/*     */     public Grid(Color paramColor1, Color paramColor2) {
/* 392 */       int[] arrayOfInt = { paramColor1.getRGB(), paramColor2.getRGB() };
/* 393 */       IndexColorModel localIndexColorModel = new IndexColorModel(8, 2, arrayOfInt, 0, false, -1, 0);
/*     */ 
/* 395 */       this.image = new BufferedImage(64, 64, 13, localIndexColorModel);
/*     */ 
/* 397 */       Graphics localGraphics = this.image.getGraphics();
/*     */       try {
/* 399 */         localGraphics.setClip(0, 0, 64, 64);
/* 400 */         paintGrid(localGraphics, paramColor1, paramColor2);
/*     */       }
/*     */       finally {
/* 403 */         localGraphics.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 412 */       Rectangle localRectangle = paramGraphics.getClipBounds();
/* 413 */       int i = Math.max(paramInt1, localRectangle.x);
/* 414 */       int j = Math.max(paramInt2, localRectangle.y);
/* 415 */       int k = Math.min(localRectangle.x + localRectangle.width, paramInt1 + paramInt3);
/* 416 */       int m = Math.min(localRectangle.y + localRectangle.height, paramInt2 + paramInt4);
/*     */ 
/* 418 */       if ((k <= i) || (m <= j)) {
/* 419 */         return;
/*     */       }
/* 421 */       int n = (i - paramInt1) % 2;
/* 422 */       for (int i1 = i; i1 < k; 
/* 423 */         i1 += 64) {
/* 424 */         int i2 = (j - paramInt2) % 2;
/* 425 */         int i3 = Math.min(64 - n, k - i1);
/*     */ 
/* 428 */         for (int i4 = j; i4 < m; 
/* 429 */           i4 += 64) {
/* 430 */           int i5 = Math.min(64 - i2, m - i4);
/*     */ 
/* 433 */           paramGraphics.drawImage(this.image, i1, i4, i1 + i3, i4 + i5, n, i2, n + i3, i2 + i5, null);
/*     */ 
/* 437 */           if (i2 != 0) {
/* 438 */             i4 -= i2;
/* 439 */             i2 = 0;
/*     */           }
/*     */         }
/* 442 */         if (n != 0) {
/* 443 */           i1 -= n;
/* 444 */           n = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private void paintGrid(Graphics paramGraphics, Color paramColor1, Color paramColor2)
/*     */     {
/* 453 */       Rectangle localRectangle = paramGraphics.getClipBounds();
/* 454 */       paramGraphics.setColor(paramColor2);
/* 455 */       paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */ 
/* 457 */       paramGraphics.setColor(paramColor1);
/* 458 */       paramGraphics.translate(localRectangle.x, localRectangle.y);
/* 459 */       int i = localRectangle.width;
/* 460 */       int j = localRectangle.height;
/* 461 */       int k = localRectangle.x % 2;
/* 462 */       for (int m = i - j; k < m; k += 2) {
/* 463 */         paramGraphics.drawLine(k, 0, k + j, j);
/*     */       }
/* 465 */       for (m = i; k < m; k += 2) {
/* 466 */         paramGraphics.drawLine(k, 0, i, i - k);
/*     */       }
/*     */ 
/* 469 */       m = localRectangle.x % 2 == 0 ? 2 : 1;
/* 470 */       for (int n = j - i; m < n; m += 2) {
/* 471 */         paramGraphics.drawLine(0, m, i, m + i);
/*     */       }
/* 473 */       for (n = j; m < n; m += 2) {
/* 474 */         paramGraphics.drawLine(0, m, j - m, j);
/*     */       }
/* 476 */       paramGraphics.translate(-localRectangle.x, -localRectangle.y);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class WindowsArrowButton extends BasicArrowButton
/*     */   {
/*     */     public WindowsArrowButton(int paramColor1, Color paramColor2, Color paramColor3, Color paramColor4, Color arg6)
/*     */     {
/* 280 */       super(paramColor2, paramColor3, paramColor4, localColor);
/*     */     }
/*     */ 
/*     */     public WindowsArrowButton(int arg2) {
/* 284 */       super();
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics) {
/* 288 */       XPStyle localXPStyle = XPStyle.getXP();
/* 289 */       if (localXPStyle != null) {
/* 290 */         ButtonModel localButtonModel = getModel();
/* 291 */         XPStyle.Skin localSkin = localXPStyle.getSkin(this, TMSchema.Part.SBP_ARROWBTN);
/* 292 */         TMSchema.State localState = null;
/*     */ 
/* 294 */         int i = (XPStyle.isVista()) && ((WindowsScrollBarUI.this.isThumbRollover()) || ((this == WindowsScrollBarUI.this.incrButton) && (WindowsScrollBarUI.this.decrButton.getModel().isRollover())) || ((this == WindowsScrollBarUI.this.decrButton) && (WindowsScrollBarUI.this.incrButton.getModel().isRollover()))) ? 1 : 0;
/*     */ 
/* 299 */         if ((localButtonModel.isArmed()) && (localButtonModel.isPressed()))
/* 300 */           switch (this.direction) { case 1:
/* 301 */             localState = TMSchema.State.UPPRESSED; break;
/*     */           case 5:
/* 302 */             localState = TMSchema.State.DOWNPRESSED; break;
/*     */           case 7:
/* 303 */             localState = TMSchema.State.LEFTPRESSED; break;
/*     */           case 3:
/* 304 */             localState = TMSchema.State.RIGHTPRESSED;
/*     */           case 2:
/*     */           case 4:
/* 306 */           case 6: }  else if (!localButtonModel.isEnabled())
/* 307 */           switch (this.direction) { case 1:
/* 308 */             localState = TMSchema.State.UPDISABLED; break;
/*     */           case 5:
/* 309 */             localState = TMSchema.State.DOWNDISABLED; break;
/*     */           case 7:
/* 310 */             localState = TMSchema.State.LEFTDISABLED; break;
/*     */           case 3:
/* 311 */             localState = TMSchema.State.RIGHTDISABLED;
/*     */           case 2:
/*     */           case 4:
/* 313 */           case 6: }  else if ((localButtonModel.isRollover()) || (localButtonModel.isPressed()))
/* 314 */           switch (this.direction) { case 1:
/* 315 */             localState = TMSchema.State.UPHOT; break;
/*     */           case 5:
/* 316 */             localState = TMSchema.State.DOWNHOT; break;
/*     */           case 7:
/* 317 */             localState = TMSchema.State.LEFTHOT; break;
/*     */           case 3:
/* 318 */             localState = TMSchema.State.RIGHTHOT;
/*     */           case 2:
/*     */           case 4:
/* 320 */           case 6: }  else if (i != 0)
/* 321 */           switch (this.direction) { case 1:
/* 322 */             localState = TMSchema.State.UPHOVER; break;
/*     */           case 5:
/* 323 */             localState = TMSchema.State.DOWNHOVER; break;
/*     */           case 7:
/* 324 */             localState = TMSchema.State.LEFTHOVER; break;
/*     */           case 3:
/* 325 */             localState = TMSchema.State.RIGHTHOVER;
/*     */           case 2:
/*     */           case 4:
/* 328 */           case 6: }  else switch (this.direction) { case 1:
/* 329 */             localState = TMSchema.State.UPNORMAL; break;
/*     */           case 5:
/* 330 */             localState = TMSchema.State.DOWNNORMAL; break;
/*     */           case 7:
/* 331 */             localState = TMSchema.State.LEFTNORMAL; break;
/*     */           case 3:
/* 332 */             localState = TMSchema.State.RIGHTNORMAL;
/*     */           case 2:
/*     */           case 4:
/*     */           case 6: }
/* 336 */         localSkin.paintSkin(paramGraphics, 0, 0, getWidth(), getHeight(), localState);
/*     */       } else {
/* 338 */         super.paint(paramGraphics);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize() {
/* 343 */       int i = 16;
/* 344 */       if (WindowsScrollBarUI.this.scrollbar != null) {
/* 345 */         switch (WindowsScrollBarUI.this.scrollbar.getOrientation()) {
/*     */         case 1:
/* 347 */           i = WindowsScrollBarUI.this.scrollbar.getWidth();
/* 348 */           break;
/*     */         case 0:
/* 350 */           i = WindowsScrollBarUI.this.scrollbar.getHeight();
/*     */         }
/*     */ 
/* 353 */         i = Math.max(i, 5);
/*     */       }
/* 355 */       return new Dimension(i, i);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsScrollBarUI
 * JD-Core Version:    0.6.2
 */