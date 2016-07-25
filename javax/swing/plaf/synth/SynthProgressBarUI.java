/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicProgressBarUI;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public class SynthProgressBarUI extends BasicProgressBarUI
/*     */   implements SynthUI, PropertyChangeListener
/*     */ {
/*     */   private SynthStyle style;
/*     */   private int progressPadding;
/*     */   private boolean rotateText;
/*     */   private boolean paintOutsideClip;
/*     */   private boolean tileWhenIndeterminate;
/*     */   private int tileWidth;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  60 */     return new SynthProgressBarUI();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  68 */     super.installListeners();
/*  69 */     this.progressBar.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/*  77 */     super.uninstallListeners();
/*  78 */     this.progressBar.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  86 */     updateStyle(this.progressBar);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JProgressBar paramJProgressBar) {
/*  90 */     SynthContext localSynthContext = getContext(paramJProgressBar, 1);
/*  91 */     SynthStyle localSynthStyle = this.style;
/*  92 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  93 */     setCellLength(this.style.getInt(localSynthContext, "ProgressBar.cellLength", 1));
/*  94 */     setCellSpacing(this.style.getInt(localSynthContext, "ProgressBar.cellSpacing", 0));
/*  95 */     this.progressPadding = this.style.getInt(localSynthContext, "ProgressBar.progressPadding", 0);
/*     */ 
/*  97 */     this.paintOutsideClip = this.style.getBoolean(localSynthContext, "ProgressBar.paintOutsideClip", false);
/*     */ 
/*  99 */     this.rotateText = this.style.getBoolean(localSynthContext, "ProgressBar.rotateText", false);
/*     */ 
/* 101 */     this.tileWhenIndeterminate = this.style.getBoolean(localSynthContext, "ProgressBar.tileWhenIndeterminate", false);
/* 102 */     this.tileWidth = this.style.getInt(localSynthContext, "ProgressBar.tileWidth", 15);
/*     */ 
/* 106 */     String str = (String)this.progressBar.getClientProperty("JComponent.sizeVariant");
/*     */ 
/* 108 */     if (str != null) {
/* 109 */       if ("large".equals(str))
/* 110 */         this.tileWidth = ((int)(this.tileWidth * 1.15D));
/* 111 */       else if ("small".equals(str))
/* 112 */         this.tileWidth = ((int)(this.tileWidth * 0.857D));
/* 113 */       else if ("mini".equals(str)) {
/* 114 */         this.tileWidth = ((int)(this.tileWidth * 0.784D));
/*     */       }
/*     */     }
/* 117 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 125 */     SynthContext localSynthContext = getContext(this.progressBar, 1);
/*     */ 
/* 127 */     this.style.uninstallDefaults(localSynthContext);
/* 128 */     localSynthContext.dispose();
/* 129 */     this.style = null;
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 137 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 141 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 146 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 154 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/* 155 */     if ((this.progressBar.isStringPainted()) && (this.progressBar.getOrientation() == 0))
/*     */     {
/* 157 */       SynthContext localSynthContext = getContext(paramJComponent);
/* 158 */       Font localFont = localSynthContext.getStyle().getFont(localSynthContext);
/* 159 */       FontMetrics localFontMetrics = this.progressBar.getFontMetrics(localFont);
/* 160 */       localSynthContext.dispose();
/* 161 */       return (paramInt2 - localFontMetrics.getAscent() - localFontMetrics.getDescent()) / 2 + localFontMetrics.getAscent();
/*     */     }
/*     */ 
/* 164 */     return -1;
/*     */   }
/*     */ 
/*     */   protected Rectangle getBox(Rectangle paramRectangle)
/*     */   {
/* 172 */     if (this.tileWhenIndeterminate) {
/* 173 */       return SwingUtilities.calculateInnerArea(this.progressBar, paramRectangle);
/*     */     }
/* 175 */     return super.getBox(paramRectangle);
/*     */   }
/*     */ 
/*     */   protected void setAnimationIndex(int paramInt)
/*     */   {
/* 184 */     if (this.paintOutsideClip) {
/* 185 */       if (getAnimationIndex() == paramInt) {
/* 186 */         return;
/*     */       }
/* 188 */       super.setAnimationIndex(paramInt);
/* 189 */       this.progressBar.repaint();
/*     */     } else {
/* 191 */       super.setAnimationIndex(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 209 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 211 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 212 */     localSynthContext.getPainter().paintProgressBarBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), this.progressBar.getOrientation());
/*     */ 
/* 215 */     paint(localSynthContext, paramGraphics);
/* 216 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 230 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 232 */     paint(localSynthContext, paramGraphics);
/* 233 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 244 */     JProgressBar localJProgressBar = (JProgressBar)paramSynthContext.getComponent();
/* 245 */     int i = 0; int j = 0; int k = 0; int m = 0;
/* 246 */     if (!localJProgressBar.isIndeterminate()) {
/* 247 */       Insets localInsets = localJProgressBar.getInsets();
/* 248 */       double d2 = localJProgressBar.getPercentComplete();
/* 249 */       if (d2 != 0.0D) {
/* 250 */         if (localJProgressBar.getOrientation() == 0) {
/* 251 */           i = localInsets.left + this.progressPadding;
/* 252 */           j = localInsets.top + this.progressPadding;
/* 253 */           k = (int)(d2 * (localJProgressBar.getWidth() - (localInsets.left + this.progressPadding + localInsets.right + this.progressPadding)));
/*     */ 
/* 256 */           m = localJProgressBar.getHeight() - (localInsets.top + this.progressPadding + localInsets.bottom + this.progressPadding);
/*     */ 
/* 260 */           if (!SynthLookAndFeel.isLeftToRight(localJProgressBar))
/* 261 */             i = localJProgressBar.getWidth() - localInsets.right - k - this.progressPadding;
/*     */         }
/*     */         else
/*     */         {
/* 265 */           i = localInsets.left + this.progressPadding;
/* 266 */           k = localJProgressBar.getWidth() - (localInsets.left + this.progressPadding + localInsets.right + this.progressPadding);
/*     */ 
/* 269 */           m = (int)(d2 * (localJProgressBar.getHeight() - (localInsets.top + this.progressPadding + localInsets.bottom + this.progressPadding)));
/*     */ 
/* 272 */           j = localJProgressBar.getHeight() - localInsets.bottom - m - this.progressPadding;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 281 */       this.boxRect = getBox(this.boxRect);
/* 282 */       i = this.boxRect.x + this.progressPadding;
/* 283 */       j = this.boxRect.y + this.progressPadding;
/* 284 */       k = this.boxRect.width - this.progressPadding - this.progressPadding;
/* 285 */       m = this.boxRect.height - this.progressPadding - this.progressPadding;
/*     */     }
/*     */ 
/* 291 */     if ((this.tileWhenIndeterminate) && (localJProgressBar.isIndeterminate())) {
/* 292 */       double d1 = getAnimationIndex() / getFrameCount();
/* 293 */       int n = (int)(d1 * this.tileWidth);
/* 294 */       Shape localShape = paramGraphics.getClip();
/* 295 */       paramGraphics.clipRect(i, j, k, m);
/*     */       int i1;
/* 296 */       if (localJProgressBar.getOrientation() == 0)
/*     */       {
/* 298 */         for (i1 = i - this.tileWidth + n; i1 <= k; i1 += this.tileWidth) {
/* 299 */           paramSynthContext.getPainter().paintProgressBarForeground(paramSynthContext, paramGraphics, i1, j, this.tileWidth, m, localJProgressBar.getOrientation());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 304 */         for (i1 = j - n; i1 < m + this.tileWidth; i1 += this.tileWidth) {
/* 305 */           paramSynthContext.getPainter().paintProgressBarForeground(paramSynthContext, paramGraphics, i, i1, k, this.tileWidth, localJProgressBar.getOrientation());
/*     */         }
/*     */       }
/*     */ 
/* 309 */       paramGraphics.setClip(localShape);
/*     */     } else {
/* 311 */       paramSynthContext.getPainter().paintProgressBarForeground(paramSynthContext, paramGraphics, i, j, k, m, localJProgressBar.getOrientation());
/*     */     }
/*     */ 
/* 315 */     if (localJProgressBar.isStringPainted())
/* 316 */       paintText(paramSynthContext, paramGraphics, localJProgressBar.getString());
/*     */   }
/*     */ 
/*     */   protected void paintText(SynthContext paramSynthContext, Graphics paramGraphics, String paramString)
/*     */   {
/* 328 */     if (this.progressBar.isStringPainted()) {
/* 329 */       SynthStyle localSynthStyle = paramSynthContext.getStyle();
/* 330 */       Font localFont = localSynthStyle.getFont(paramSynthContext);
/* 331 */       FontMetrics localFontMetrics = SwingUtilities2.getFontMetrics(this.progressBar, paramGraphics, localFont);
/*     */ 
/* 333 */       int i = localSynthStyle.getGraphicsUtils(paramSynthContext).computeStringWidth(paramSynthContext, localFont, localFontMetrics, paramString);
/*     */ 
/* 335 */       Rectangle localRectangle = this.progressBar.getBounds();
/*     */       Object localObject;
/* 337 */       if ((this.rotateText) && (this.progressBar.getOrientation() == 1))
/*     */       {
/* 339 */         localObject = (Graphics2D)paramGraphics;
/*     */         AffineTransform localAffineTransform;
/*     */         Point localPoint;
/* 343 */         if (this.progressBar.getComponentOrientation().isLeftToRight()) {
/* 344 */           localAffineTransform = AffineTransform.getRotateInstance(-1.570796326794897D);
/* 345 */           localPoint = new Point((localRectangle.width + localFontMetrics.getAscent() - localFontMetrics.getDescent()) / 2, (localRectangle.height + i) / 2);
/*     */         }
/*     */         else
/*     */         {
/* 349 */           localAffineTransform = AffineTransform.getRotateInstance(1.570796326794897D);
/* 350 */           localPoint = new Point((localRectangle.width - localFontMetrics.getAscent() + localFontMetrics.getDescent()) / 2, (localRectangle.height - i) / 2);
/*     */         }
/*     */ 
/* 356 */         if (localPoint.x < 0) {
/* 357 */           return;
/*     */         }
/*     */ 
/* 361 */         localFont = localFont.deriveFont(localAffineTransform);
/* 362 */         ((Graphics2D)localObject).setFont(localFont);
/* 363 */         ((Graphics2D)localObject).setColor(localSynthStyle.getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/* 364 */         localSynthStyle.getGraphicsUtils(paramSynthContext).paintText(paramSynthContext, paramGraphics, paramString, localPoint.x, localPoint.y, -1);
/*     */       }
/*     */       else
/*     */       {
/* 368 */         localObject = new Rectangle(localRectangle.width / 2 - i / 2, (localRectangle.height - (localFontMetrics.getAscent() + localFontMetrics.getDescent())) / 2, 0, 0);
/*     */ 
/* 375 */         if (((Rectangle)localObject).y < 0) {
/* 376 */           return;
/*     */         }
/*     */ 
/* 380 */         paramGraphics.setColor(localSynthStyle.getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/* 381 */         paramGraphics.setFont(localFont);
/* 382 */         localSynthStyle.getGraphicsUtils(paramSynthContext).paintText(paramSynthContext, paramGraphics, paramString, ((Rectangle)localObject).x, ((Rectangle)localObject).y, -1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 394 */     paramSynthContext.getPainter().paintProgressBarBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.progressBar.getOrientation());
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 403 */     if ((SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) || ("indeterminate".equals(paramPropertyChangeEvent.getPropertyName())))
/*     */     {
/* 405 */       updateStyle((JProgressBar)paramPropertyChangeEvent.getSource());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 414 */     Dimension localDimension = null;
/* 415 */     Insets localInsets = this.progressBar.getInsets();
/* 416 */     FontMetrics localFontMetrics = this.progressBar.getFontMetrics(this.progressBar.getFont());
/* 417 */     String str1 = this.progressBar.getString();
/* 418 */     int i = localFontMetrics.getHeight() + localFontMetrics.getDescent();
/*     */     int j;
/* 420 */     if (this.progressBar.getOrientation() == 0) {
/* 421 */       localDimension = new Dimension(getPreferredInnerHorizontal());
/* 422 */       if (this.progressBar.isStringPainted())
/*     */       {
/* 424 */         if (i > localDimension.height) {
/* 425 */           localDimension.height = i;
/*     */         }
/*     */ 
/* 429 */         j = SwingUtilities2.stringWidth(this.progressBar, localFontMetrics, str1);
/*     */ 
/* 431 */         if (j > localDimension.width)
/* 432 */           localDimension.width = j;
/*     */       }
/*     */     }
/*     */     else {
/* 436 */       localDimension = new Dimension(getPreferredInnerVertical());
/* 437 */       if (this.progressBar.isStringPainted())
/*     */       {
/* 439 */         if (i > localDimension.width) {
/* 440 */           localDimension.width = i;
/*     */         }
/*     */ 
/* 444 */         j = SwingUtilities2.stringWidth(this.progressBar, localFontMetrics, str1);
/*     */ 
/* 446 */         if (j > localDimension.height) {
/* 447 */           localDimension.height = j;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 455 */     String str2 = (String)this.progressBar.getClientProperty("JComponent.sizeVariant");
/*     */ 
/* 457 */     if (str2 != null) {
/* 458 */       if ("large".equals(str2))
/*     */       {
/*     */         Dimension tmp221_220 = localDimension; tmp221_220.width = ((int)(tmp221_220.width * 1.15F));
/*     */         Dimension tmp234_233 = localDimension; tmp234_233.height = ((int)(tmp234_233.height * 1.15F));
/* 461 */       } else if ("small".equals(str2))
/*     */       {
/*     */         Dimension tmp260_259 = localDimension; tmp260_259.width = ((int)(tmp260_259.width * 0.9F));
/*     */         Dimension tmp273_272 = localDimension; tmp273_272.height = ((int)(tmp273_272.height * 0.9F));
/* 464 */       } else if ("mini".equals(str2))
/*     */       {
/*     */         Dimension tmp299_298 = localDimension; tmp299_298.width = ((int)(tmp299_298.width * 0.784F));
/*     */         Dimension tmp312_311 = localDimension; tmp312_311.height = ((int)(tmp312_311.height * 0.784F));
/*     */       }
/*     */     }
/*     */ 
/* 470 */     localDimension.width += localInsets.left + localInsets.right;
/* 471 */     localDimension.height += localInsets.top + localInsets.bottom;
/*     */ 
/* 473 */     return localDimension;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthProgressBarUI
 * JD-Core Version:    0.6.2
 */