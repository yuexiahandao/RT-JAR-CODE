/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI.PropertyChangeHandler;
/*     */ 
/*     */ public class MetalScrollBarUI extends BasicScrollBarUI
/*     */ {
/*     */   private static Color shadowColor;
/*     */   private static Color highlightColor;
/*     */   private static Color darkShadowColor;
/*     */   private static Color thumbColor;
/*     */   private static Color thumbShadow;
/*     */   private static Color thumbHighlightColor;
/*     */   protected MetalBumps bumps;
/*     */   protected MetalScrollButton increaseButton;
/*     */   protected MetalScrollButton decreaseButton;
/*     */   protected int scrollBarWidth;
/*     */   public static final String FREE_STANDING_PROP = "JScrollBar.isFreeStanding";
/*     */   protected boolean isFreeStanding;
/*     */ 
/*     */   public MetalScrollBarUI()
/*     */   {
/*  82 */     this.isFreeStanding = true;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent) {
/*  86 */     return new MetalScrollBarUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults() {
/*  90 */     this.scrollBarWidth = ((Integer)UIManager.get("ScrollBar.width")).intValue();
/*  91 */     super.installDefaults();
/*  92 */     this.bumps = new MetalBumps(10, 10, thumbHighlightColor, thumbShadow, thumbColor);
/*     */   }
/*     */ 
/*     */   protected void installListeners() {
/*  96 */     super.installListeners();
/*  97 */     ((ScrollBarListener)this.propertyChangeListener).handlePropertyChange(this.scrollbar.getClientProperty("JScrollBar.isFreeStanding"));
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/* 101 */     return new ScrollBarListener();
/*     */   }
/*     */ 
/*     */   protected void configureScrollBarColors()
/*     */   {
/* 106 */     super.configureScrollBarColors();
/* 107 */     shadowColor = UIManager.getColor("ScrollBar.shadow");
/* 108 */     highlightColor = UIManager.getColor("ScrollBar.highlight");
/* 109 */     darkShadowColor = UIManager.getColor("ScrollBar.darkShadow");
/* 110 */     thumbColor = UIManager.getColor("ScrollBar.thumb");
/* 111 */     thumbShadow = UIManager.getColor("ScrollBar.thumbShadow");
/* 112 */     thumbHighlightColor = UIManager.getColor("ScrollBar.thumbHighlight");
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 119 */     if (this.scrollbar.getOrientation() == 1)
/*     */     {
/* 121 */       return new Dimension(this.scrollBarWidth, this.scrollBarWidth * 3 + 10);
/*     */     }
/*     */ 
/* 125 */     return new Dimension(this.scrollBarWidth * 3 + 10, this.scrollBarWidth);
/*     */   }
/*     */ 
/*     */   protected JButton createDecreaseButton(int paramInt)
/*     */   {
/* 134 */     this.decreaseButton = new MetalScrollButton(paramInt, this.scrollBarWidth, this.isFreeStanding);
/* 135 */     return this.decreaseButton;
/*     */   }
/*     */ 
/*     */   protected JButton createIncreaseButton(int paramInt)
/*     */   {
/* 141 */     this.increaseButton = new MetalScrollButton(paramInt, this.scrollBarWidth, this.isFreeStanding);
/* 142 */     return this.increaseButton;
/*     */   }
/*     */ 
/*     */   protected void paintTrack(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*     */   {
/* 147 */     paramGraphics.translate(paramRectangle.x, paramRectangle.y);
/*     */ 
/* 149 */     boolean bool = MetalUtils.isLeftToRight(paramJComponent);
/*     */     int i;
/* 151 */     if (this.scrollbar.getOrientation() == 1)
/*     */     {
/* 153 */       if (!this.isFreeStanding) {
/* 154 */         paramRectangle.width += 2;
/* 155 */         if (!bool) {
/* 156 */           paramGraphics.translate(-1, 0);
/*     */         }
/*     */       }
/*     */ 
/* 160 */       if (paramJComponent.isEnabled()) {
/* 161 */         paramGraphics.setColor(darkShadowColor);
/* 162 */         paramGraphics.drawLine(0, 0, 0, paramRectangle.height - 1);
/* 163 */         paramGraphics.drawLine(paramRectangle.width - 2, 0, paramRectangle.width - 2, paramRectangle.height - 1);
/* 164 */         paramGraphics.drawLine(2, paramRectangle.height - 1, paramRectangle.width - 1, paramRectangle.height - 1);
/* 165 */         paramGraphics.drawLine(2, 0, paramRectangle.width - 2, 0);
/*     */ 
/* 167 */         paramGraphics.setColor(shadowColor);
/*     */ 
/* 169 */         paramGraphics.drawLine(1, 1, 1, paramRectangle.height - 2);
/* 170 */         paramGraphics.drawLine(1, 1, paramRectangle.width - 3, 1);
/* 171 */         if (this.scrollbar.getValue() != this.scrollbar.getMaximum()) {
/* 172 */           i = this.thumbRect.y + this.thumbRect.height - paramRectangle.y;
/* 173 */           paramGraphics.drawLine(1, i, paramRectangle.width - 1, i);
/*     */         }
/* 175 */         paramGraphics.setColor(highlightColor);
/* 176 */         paramGraphics.drawLine(paramRectangle.width - 1, 0, paramRectangle.width - 1, paramRectangle.height - 1);
/*     */       } else {
/* 178 */         MetalUtils.drawDisabledBorder(paramGraphics, 0, 0, paramRectangle.width, paramRectangle.height);
/*     */       }
/*     */ 
/* 181 */       if (!this.isFreeStanding) {
/* 182 */         paramRectangle.width -= 2;
/* 183 */         if (!bool) {
/* 184 */           paramGraphics.translate(1, 0);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 190 */       if (!this.isFreeStanding) {
/* 191 */         paramRectangle.height += 2;
/*     */       }
/*     */ 
/* 194 */       if (paramJComponent.isEnabled()) {
/* 195 */         paramGraphics.setColor(darkShadowColor);
/* 196 */         paramGraphics.drawLine(0, 0, paramRectangle.width - 1, 0);
/* 197 */         paramGraphics.drawLine(0, 2, 0, paramRectangle.height - 2);
/* 198 */         paramGraphics.drawLine(0, paramRectangle.height - 2, paramRectangle.width - 1, paramRectangle.height - 2);
/* 199 */         paramGraphics.drawLine(paramRectangle.width - 1, 2, paramRectangle.width - 1, paramRectangle.height - 1);
/*     */ 
/* 201 */         paramGraphics.setColor(shadowColor);
/*     */ 
/* 203 */         paramGraphics.drawLine(1, 1, paramRectangle.width - 2, 1);
/* 204 */         paramGraphics.drawLine(1, 1, 1, paramRectangle.height - 3);
/* 205 */         paramGraphics.drawLine(0, paramRectangle.height - 1, paramRectangle.width - 1, paramRectangle.height - 1);
/* 206 */         if (this.scrollbar.getValue() != this.scrollbar.getMaximum()) {
/* 207 */           i = this.thumbRect.x + this.thumbRect.width - paramRectangle.x;
/* 208 */           paramGraphics.drawLine(i, 1, i, paramRectangle.height - 1);
/*     */         }
/*     */       } else {
/* 211 */         MetalUtils.drawDisabledBorder(paramGraphics, 0, 0, paramRectangle.width, paramRectangle.height);
/*     */       }
/*     */ 
/* 214 */       if (!this.isFreeStanding) {
/* 215 */         paramRectangle.height -= 2;
/*     */       }
/*     */     }
/*     */ 
/* 219 */     paramGraphics.translate(-paramRectangle.x, -paramRectangle.y);
/*     */   }
/*     */ 
/*     */   protected void paintThumb(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*     */   {
/* 224 */     if (!paramJComponent.isEnabled()) {
/* 225 */       return;
/*     */     }
/*     */ 
/* 228 */     if (MetalLookAndFeel.usingOcean()) {
/* 229 */       oceanPaintThumb(paramGraphics, paramJComponent, paramRectangle);
/* 230 */       return;
/*     */     }
/*     */ 
/* 233 */     boolean bool = MetalUtils.isLeftToRight(paramJComponent);
/*     */ 
/* 235 */     paramGraphics.translate(paramRectangle.x, paramRectangle.y);
/*     */ 
/* 237 */     if (this.scrollbar.getOrientation() == 1)
/*     */     {
/* 239 */       if (!this.isFreeStanding) {
/* 240 */         paramRectangle.width += 2;
/* 241 */         if (!bool) {
/* 242 */           paramGraphics.translate(-1, 0);
/*     */         }
/*     */       }
/*     */ 
/* 246 */       paramGraphics.setColor(thumbColor);
/* 247 */       paramGraphics.fillRect(0, 0, paramRectangle.width - 2, paramRectangle.height - 1);
/*     */ 
/* 249 */       paramGraphics.setColor(thumbShadow);
/* 250 */       paramGraphics.drawRect(0, 0, paramRectangle.width - 2, paramRectangle.height - 1);
/*     */ 
/* 252 */       paramGraphics.setColor(thumbHighlightColor);
/* 253 */       paramGraphics.drawLine(1, 1, paramRectangle.width - 3, 1);
/* 254 */       paramGraphics.drawLine(1, 1, 1, paramRectangle.height - 2);
/*     */ 
/* 256 */       this.bumps.setBumpArea(paramRectangle.width - 6, paramRectangle.height - 7);
/* 257 */       this.bumps.paintIcon(paramJComponent, paramGraphics, 3, 4);
/*     */ 
/* 259 */       if (!this.isFreeStanding) {
/* 260 */         paramRectangle.width -= 2;
/* 261 */         if (!bool) {
/* 262 */           paramGraphics.translate(1, 0);
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 268 */       if (!this.isFreeStanding) {
/* 269 */         paramRectangle.height += 2;
/*     */       }
/*     */ 
/* 272 */       paramGraphics.setColor(thumbColor);
/* 273 */       paramGraphics.fillRect(0, 0, paramRectangle.width - 1, paramRectangle.height - 2);
/*     */ 
/* 275 */       paramGraphics.setColor(thumbShadow);
/* 276 */       paramGraphics.drawRect(0, 0, paramRectangle.width - 1, paramRectangle.height - 2);
/*     */ 
/* 278 */       paramGraphics.setColor(thumbHighlightColor);
/* 279 */       paramGraphics.drawLine(1, 1, paramRectangle.width - 3, 1);
/* 280 */       paramGraphics.drawLine(1, 1, 1, paramRectangle.height - 3);
/*     */ 
/* 282 */       this.bumps.setBumpArea(paramRectangle.width - 7, paramRectangle.height - 6);
/* 283 */       this.bumps.paintIcon(paramJComponent, paramGraphics, 4, 3);
/*     */ 
/* 285 */       if (!this.isFreeStanding) {
/* 286 */         paramRectangle.height -= 2;
/*     */       }
/*     */     }
/*     */ 
/* 290 */     paramGraphics.translate(-paramRectangle.x, -paramRectangle.y);
/*     */   }
/*     */ 
/*     */   private void oceanPaintThumb(Graphics paramGraphics, JComponent paramJComponent, Rectangle paramRectangle)
/*     */   {
/* 295 */     boolean bool = MetalUtils.isLeftToRight(paramJComponent);
/*     */ 
/* 297 */     paramGraphics.translate(paramRectangle.x, paramRectangle.y);
/*     */     int i;
/*     */     int j;
/*     */     int k;
/* 299 */     if (this.scrollbar.getOrientation() == 1) {
/* 300 */       if (!this.isFreeStanding) {
/* 301 */         paramRectangle.width += 2;
/* 302 */         if (!bool) {
/* 303 */           paramGraphics.translate(-1, 0);
/*     */         }
/*     */       }
/*     */ 
/* 307 */       if (thumbColor != null) {
/* 308 */         paramGraphics.setColor(thumbColor);
/* 309 */         paramGraphics.fillRect(0, 0, paramRectangle.width - 2, paramRectangle.height - 1);
/*     */       }
/*     */ 
/* 312 */       paramGraphics.setColor(thumbShadow);
/* 313 */       paramGraphics.drawRect(0, 0, paramRectangle.width - 2, paramRectangle.height - 1);
/*     */ 
/* 315 */       paramGraphics.setColor(thumbHighlightColor);
/* 316 */       paramGraphics.drawLine(1, 1, paramRectangle.width - 3, 1);
/* 317 */       paramGraphics.drawLine(1, 1, 1, paramRectangle.height - 2);
/*     */ 
/* 319 */       MetalUtils.drawGradient(paramJComponent, paramGraphics, "ScrollBar.gradient", 2, 2, paramRectangle.width - 4, paramRectangle.height - 3, false);
/*     */ 
/* 323 */       i = paramRectangle.width - 8;
/* 324 */       if ((i > 2) && (paramRectangle.height >= 10)) {
/* 325 */         paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 326 */         j = paramRectangle.height / 2 - 2;
/* 327 */         for (k = 0; k < 6; k += 2) {
/* 328 */           paramGraphics.fillRect(4, k + j, i, 1);
/*     */         }
/*     */ 
/* 331 */         paramGraphics.setColor(MetalLookAndFeel.getWhite());
/* 332 */         j++;
/* 333 */         for (k = 0; k < 6; k += 2) {
/* 334 */           paramGraphics.fillRect(5, k + j, i, 1);
/*     */         }
/*     */       }
/* 337 */       if (!this.isFreeStanding) {
/* 338 */         paramRectangle.width -= 2;
/* 339 */         if (!bool)
/* 340 */           paramGraphics.translate(1, 0);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 345 */       if (!this.isFreeStanding) {
/* 346 */         paramRectangle.height += 2;
/*     */       }
/*     */ 
/* 349 */       if (thumbColor != null) {
/* 350 */         paramGraphics.setColor(thumbColor);
/* 351 */         paramGraphics.fillRect(0, 0, paramRectangle.width - 1, paramRectangle.height - 2);
/*     */       }
/*     */ 
/* 354 */       paramGraphics.setColor(thumbShadow);
/* 355 */       paramGraphics.drawRect(0, 0, paramRectangle.width - 1, paramRectangle.height - 2);
/*     */ 
/* 357 */       paramGraphics.setColor(thumbHighlightColor);
/* 358 */       paramGraphics.drawLine(1, 1, paramRectangle.width - 2, 1);
/* 359 */       paramGraphics.drawLine(1, 1, 1, paramRectangle.height - 3);
/*     */ 
/* 361 */       MetalUtils.drawGradient(paramJComponent, paramGraphics, "ScrollBar.gradient", 2, 2, paramRectangle.width - 3, paramRectangle.height - 4, true);
/*     */ 
/* 365 */       i = paramRectangle.height - 8;
/* 366 */       if ((i > 2) && (paramRectangle.width >= 10)) {
/* 367 */         paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 368 */         j = paramRectangle.width / 2 - 2;
/* 369 */         for (k = 0; k < 6; k += 2) {
/* 370 */           paramGraphics.fillRect(j + k, 4, 1, i);
/*     */         }
/*     */ 
/* 373 */         paramGraphics.setColor(MetalLookAndFeel.getWhite());
/* 374 */         j++;
/* 375 */         for (k = 0; k < 6; k += 2) {
/* 376 */           paramGraphics.fillRect(j + k, 5, 1, i);
/*     */         }
/*     */       }
/*     */ 
/* 380 */       if (!this.isFreeStanding) {
/* 381 */         paramRectangle.height -= 2;
/*     */       }
/*     */     }
/*     */ 
/* 385 */     paramGraphics.translate(-paramRectangle.x, -paramRectangle.y);
/*     */   }
/*     */ 
/*     */   protected Dimension getMinimumThumbSize()
/*     */   {
/* 390 */     return new Dimension(this.scrollBarWidth, this.scrollBarWidth);
/*     */   }
/*     */ 
/*     */   protected void setThumbBounds(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 401 */     if ((this.thumbRect.x == paramInt1) && (this.thumbRect.y == paramInt2) && (this.thumbRect.width == paramInt3) && (this.thumbRect.height == paramInt4))
/*     */     {
/* 405 */       return;
/*     */     }
/*     */ 
/* 411 */     int i = Math.min(paramInt1, this.thumbRect.x);
/* 412 */     int j = Math.min(paramInt2, this.thumbRect.y);
/* 413 */     int k = Math.max(paramInt1 + paramInt3, this.thumbRect.x + this.thumbRect.width);
/* 414 */     int m = Math.max(paramInt2 + paramInt4, this.thumbRect.y + this.thumbRect.height);
/*     */ 
/* 416 */     this.thumbRect.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/* 417 */     this.scrollbar.repaint(i, j, k - i + 1, m - j + 1);
/*     */   }
/*     */ 
/*     */   class ScrollBarListener extends BasicScrollBarUI.PropertyChangeHandler {
/*     */     ScrollBarListener() {
/* 422 */       super();
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 426 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 427 */       if (str.equals("JScrollBar.isFreeStanding"))
/*     */       {
/* 429 */         handlePropertyChange(paramPropertyChangeEvent.getNewValue());
/*     */       }
/*     */       else
/* 432 */         super.propertyChange(paramPropertyChangeEvent);
/*     */     }
/*     */ 
/*     */     public void handlePropertyChange(Object paramObject)
/*     */     {
/* 438 */       if (paramObject != null)
/*     */       {
/* 440 */         boolean bool = ((Boolean)paramObject).booleanValue();
/* 441 */         int i = (!bool) && (MetalScrollBarUI.this.isFreeStanding == true) ? 1 : 0;
/* 442 */         int j = (bool == true) && (!MetalScrollBarUI.this.isFreeStanding) ? 1 : 0;
/*     */ 
/* 444 */         MetalScrollBarUI.this.isFreeStanding = bool;
/*     */ 
/* 446 */         if (i != 0) {
/* 447 */           toFlush();
/*     */         }
/* 449 */         else if (j != 0) {
/* 450 */           toFreeStanding();
/*     */         }
/*     */ 
/*     */       }
/* 456 */       else if (!MetalScrollBarUI.this.isFreeStanding) {
/* 457 */         MetalScrollBarUI.this.isFreeStanding = true;
/* 458 */         toFreeStanding();
/*     */       }
/*     */ 
/* 470 */       if (MetalScrollBarUI.this.increaseButton != null)
/*     */       {
/* 472 */         MetalScrollBarUI.this.increaseButton.setFreeStanding(MetalScrollBarUI.this.isFreeStanding);
/*     */       }
/* 474 */       if (MetalScrollBarUI.this.decreaseButton != null)
/*     */       {
/* 476 */         MetalScrollBarUI.this.decreaseButton.setFreeStanding(MetalScrollBarUI.this.isFreeStanding);
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void toFlush() {
/* 481 */       MetalScrollBarUI.this.scrollBarWidth -= 2;
/*     */     }
/*     */ 
/*     */     protected void toFreeStanding() {
/* 485 */       MetalScrollBarUI.this.scrollBarWidth += 2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalScrollBarUI
 * JD-Core Version:    0.6.2
 */