/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicProgressBarUI;
/*     */ 
/*     */ public class WindowsProgressBarUI extends BasicProgressBarUI
/*     */ {
/*     */   private Rectangle previousFullBox;
/*     */   private Insets indeterminateInsets;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  56 */     return new WindowsProgressBarUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  61 */     super.installDefaults();
/*     */ 
/*  63 */     if (XPStyle.getXP() != null) {
/*  64 */       LookAndFeel.installProperty(this.progressBar, "opaque", Boolean.FALSE);
/*  65 */       this.progressBar.setBorder(null);
/*  66 */       this.indeterminateInsets = UIManager.getInsets("ProgressBar.indeterminateInsets");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/*  79 */     int i = super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*  80 */     if ((XPStyle.getXP() != null) && (this.progressBar.isStringPainted()) && (this.progressBar.getOrientation() == 0))
/*     */     {
/*  82 */       FontMetrics localFontMetrics = this.progressBar.getFontMetrics(this.progressBar.getFont());
/*     */ 
/*  84 */       int j = this.progressBar.getInsets().top;
/*  85 */       if (this.progressBar.isIndeterminate()) {
/*  86 */         j = -1;
/*  87 */         paramInt2--;
/*     */       }
/*     */       else {
/*  90 */         j = 0;
/*  91 */         paramInt2 -= 3;
/*     */       }
/*  93 */       i = j + (paramInt2 + localFontMetrics.getAscent() - localFontMetrics.getLeading() - localFontMetrics.getDescent()) / 2;
/*     */     }
/*     */ 
/*  97 */     return i;
/*     */   }
/*     */ 
/*     */   protected Dimension getPreferredInnerHorizontal() {
/* 101 */     XPStyle localXPStyle = XPStyle.getXP();
/* 102 */     if (localXPStyle != null) {
/* 103 */       XPStyle.Skin localSkin = localXPStyle.getSkin(this.progressBar, TMSchema.Part.PP_BAR);
/* 104 */       return new Dimension((int)super.getPreferredInnerHorizontal().getWidth(), localSkin.getHeight());
/*     */     }
/*     */ 
/* 108 */     return super.getPreferredInnerHorizontal();
/*     */   }
/*     */ 
/*     */   protected Dimension getPreferredInnerVertical() {
/* 112 */     XPStyle localXPStyle = XPStyle.getXP();
/* 113 */     if (localXPStyle != null) {
/* 114 */       XPStyle.Skin localSkin = localXPStyle.getSkin(this.progressBar, TMSchema.Part.PP_BARVERT);
/* 115 */       return new Dimension(localSkin.getWidth(), (int)super.getPreferredInnerVertical().getHeight());
/*     */     }
/*     */ 
/* 119 */     return super.getPreferredInnerVertical();
/*     */   }
/*     */ 
/*     */   protected void paintDeterminate(Graphics paramGraphics, JComponent paramJComponent) {
/* 123 */     XPStyle localXPStyle = XPStyle.getXP();
/* 124 */     if (localXPStyle != null) {
/* 125 */       boolean bool1 = this.progressBar.getOrientation() == 1;
/* 126 */       boolean bool2 = WindowsGraphicsUtils.isLeftToRight(paramJComponent);
/* 127 */       int i = this.progressBar.getWidth();
/* 128 */       int j = this.progressBar.getHeight() - 1;
/*     */ 
/* 130 */       int k = getAmountFull(null, i, j);
/*     */ 
/* 132 */       paintXPBackground(paramGraphics, bool1, i, j);
/*     */       Object localObject;
/* 134 */       if (this.progressBar.isStringPainted())
/*     */       {
/* 137 */         paramGraphics.setColor(this.progressBar.getForeground());
/* 138 */         j -= 2;
/* 139 */         i -= 2;
/* 140 */         localObject = (Graphics2D)paramGraphics;
/* 141 */         ((Graphics2D)localObject).setStroke(new BasicStroke(bool1 ? i : j, 0, 2));
/*     */ 
/* 143 */         if (!bool1) {
/* 144 */           if (bool2) {
/* 145 */             ((Graphics2D)localObject).drawLine(2, j / 2 + 1, k - 2, j / 2 + 1);
/*     */           }
/*     */           else {
/* 148 */             ((Graphics2D)localObject).drawLine(2 + i, j / 2 + 1, 2 + i - (k - 2), j / 2 + 1);
/*     */           }
/*     */ 
/* 153 */           paintString(paramGraphics, 0, 0, i, j, k, null);
/*     */         } else {
/* 155 */           ((Graphics2D)localObject).drawLine(i / 2 + 1, j + 1, i / 2 + 1, j + 1 - k + 2);
/*     */ 
/* 157 */           paintString(paramGraphics, 2, 2, i, j, k, null);
/*     */         }
/*     */       }
/*     */       else {
/* 161 */         localObject = localXPStyle.getSkin(this.progressBar, bool1 ? TMSchema.Part.PP_CHUNKVERT : TMSchema.Part.PP_CHUNK);
/*     */         int m;
/* 163 */         if (bool1)
/* 164 */           m = i - 5;
/*     */         else {
/* 166 */           m = j - 5;
/*     */         }
/*     */ 
/* 169 */         int n = localXPStyle.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSCHUNKSIZE, 2);
/* 170 */         int i1 = localXPStyle.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSSPACESIZE, 0);
/* 171 */         int i2 = (k - 4) / (n + i1);
/*     */ 
/* 174 */         if ((i1 > 0) && (i2 * (n + i1) + n < k - 4)) {
/* 175 */           i2++;
/*     */         }
/*     */ 
/* 178 */         for (int i3 = 0; i3 < i2; i3++) {
/* 179 */           if (bool1) {
/* 180 */             ((XPStyle.Skin)localObject).paintSkin(paramGraphics, 3, j - i3 * (n + i1) - n - 2, m, n, null);
/*     */           }
/* 184 */           else if (bool2) {
/* 185 */             ((XPStyle.Skin)localObject).paintSkin(paramGraphics, 4 + i3 * (n + i1), 2, n, m, null);
/*     */           }
/*     */           else
/*     */           {
/* 189 */             ((XPStyle.Skin)localObject).paintSkin(paramGraphics, i - (2 + (i3 + 1) * (n + i1)), 2, n, m, null);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 197 */       super.paintDeterminate(paramGraphics, paramJComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setAnimationIndex(int paramInt)
/*     */   {
/* 207 */     super.setAnimationIndex(paramInt);
/* 208 */     XPStyle localXPStyle = XPStyle.getXP();
/* 209 */     if (localXPStyle != null)
/* 210 */       if (this.boxRect != null)
/*     */       {
/* 213 */         Rectangle localRectangle = getFullChunkBounds(this.boxRect);
/* 214 */         if (this.previousFullBox != null) {
/* 215 */           localRectangle.add(this.previousFullBox);
/*     */         }
/* 217 */         this.progressBar.repaint(localRectangle);
/*     */       } else {
/* 219 */         this.progressBar.repaint();
/*     */       }
/*     */   }
/*     */ 
/*     */   protected int getBoxLength(int paramInt1, int paramInt2)
/*     */   {
/* 230 */     XPStyle localXPStyle = XPStyle.getXP();
/* 231 */     if (localXPStyle != null) {
/* 232 */       return 6;
/*     */     }
/* 234 */     return super.getBoxLength(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected Rectangle getBox(Rectangle paramRectangle)
/*     */   {
/* 242 */     Rectangle localRectangle = super.getBox(paramRectangle);
/*     */ 
/* 244 */     XPStyle localXPStyle = XPStyle.getXP();
/* 245 */     if (localXPStyle != null) {
/* 246 */       int i = this.progressBar.getOrientation() == 1 ? 1 : 0;
/*     */ 
/* 248 */       TMSchema.Part localPart = i != 0 ? TMSchema.Part.PP_BARVERT : TMSchema.Part.PP_BAR;
/* 249 */       Insets localInsets = this.indeterminateInsets;
/*     */ 
/* 251 */       int j = getAnimationIndex();
/* 252 */       int k = getFrameCount() / 2;
/*     */ 
/* 254 */       int m = localXPStyle.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSSPACESIZE, 0);
/*     */ 
/* 256 */       j %= k;
/*     */       int n;
/*     */       double d;
/* 266 */       if (i == 0) {
/* 267 */         localRectangle.y += localInsets.top;
/* 268 */         localRectangle.height = (this.progressBar.getHeight() - localInsets.top - localInsets.bottom);
/* 269 */         n = this.progressBar.getWidth() - localInsets.left - localInsets.right;
/* 270 */         n += (localRectangle.width + m) * 2;
/* 271 */         d = n / k;
/* 272 */         localRectangle.x = ((int)(d * j) + localInsets.left);
/*     */       } else {
/* 274 */         localRectangle.x += localInsets.left;
/* 275 */         localRectangle.width = (this.progressBar.getWidth() - localInsets.left - localInsets.right);
/* 276 */         n = this.progressBar.getHeight() - localInsets.top - localInsets.bottom;
/* 277 */         n += (localRectangle.height + m) * 2;
/* 278 */         d = n / k;
/* 279 */         localRectangle.y = ((int)(d * j) + localInsets.top);
/*     */       }
/*     */     }
/* 282 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   protected void paintIndeterminate(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 287 */     XPStyle localXPStyle = XPStyle.getXP();
/* 288 */     if (localXPStyle != null) {
/* 289 */       boolean bool = this.progressBar.getOrientation() == 1;
/*     */ 
/* 291 */       int i = this.progressBar.getWidth();
/* 292 */       int j = this.progressBar.getHeight();
/* 293 */       paintXPBackground(paramGraphics, bool, i, j);
/*     */ 
/* 296 */       this.boxRect = getBox(this.boxRect);
/* 297 */       if (this.boxRect != null) {
/* 298 */         paramGraphics.setColor(this.progressBar.getForeground());
/* 299 */         if (!(paramGraphics instanceof Graphics2D)) {
/* 300 */           return;
/*     */         }
/* 302 */         paintIndeterminateFrame(this.boxRect, (Graphics2D)paramGraphics, bool, i, j);
/*     */ 
/* 304 */         if (this.progressBar.isStringPainted())
/* 305 */           if (!bool)
/* 306 */             paintString(paramGraphics, -1, -1, i, j, 0, null);
/*     */           else
/* 308 */             paintString(paramGraphics, 1, 1, i, j, 0, null);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 313 */       super.paintIndeterminate(paramGraphics, paramJComponent);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Rectangle getFullChunkBounds(Rectangle paramRectangle) {
/* 318 */     int i = this.progressBar.getOrientation() == 1 ? 1 : 0;
/* 319 */     XPStyle localXPStyle = XPStyle.getXP();
/* 320 */     int j = localXPStyle != null ? localXPStyle.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSSPACESIZE, 0) : 0;
/*     */ 
/* 324 */     if (i == 0) {
/* 325 */       k = paramRectangle.width + j;
/* 326 */       return new Rectangle(paramRectangle.x - k * 2, paramRectangle.y, k * 3, paramRectangle.height);
/*     */     }
/* 328 */     int k = paramRectangle.height + j;
/* 329 */     return new Rectangle(paramRectangle.x, paramRectangle.y - k * 2, paramRectangle.width, k * 3);
/*     */   }
/*     */ 
/*     */   private void paintIndeterminateFrame(Rectangle paramRectangle, Graphics2D paramGraphics2D, boolean paramBoolean, int paramInt1, int paramInt2)
/*     */   {
/* 336 */     XPStyle localXPStyle = XPStyle.getXP();
/* 337 */     if (localXPStyle == null) {
/* 338 */       return;
/*     */     }
/*     */ 
/* 342 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics2D.create();
/*     */ 
/* 344 */     TMSchema.Part localPart1 = paramBoolean ? TMSchema.Part.PP_BARVERT : TMSchema.Part.PP_BAR;
/* 345 */     TMSchema.Part localPart2 = paramBoolean ? TMSchema.Part.PP_CHUNKVERT : TMSchema.Part.PP_CHUNK;
/*     */ 
/* 348 */     int i = localXPStyle.getInt(this.progressBar, TMSchema.Part.PP_PROGRESS, null, TMSchema.Prop.PROGRESSSPACESIZE, 0);
/*     */ 
/* 350 */     int j = 0;
/* 351 */     int k = 0;
/* 352 */     if (!paramBoolean) {
/* 353 */       j = -paramRectangle.width - i;
/* 354 */       k = 0;
/*     */     } else {
/* 356 */       j = 0;
/* 357 */       k = -paramRectangle.height - i;
/*     */     }
/*     */ 
/* 361 */     Rectangle localRectangle1 = getFullChunkBounds(paramRectangle);
/*     */ 
/* 364 */     this.previousFullBox = localRectangle1;
/*     */ 
/* 367 */     Insets localInsets = this.indeterminateInsets;
/* 368 */     Rectangle localRectangle2 = new Rectangle(localInsets.left, localInsets.top, paramInt1 - localInsets.left - localInsets.right, paramInt2 - localInsets.top - localInsets.bottom);
/*     */ 
/* 373 */     Rectangle localRectangle3 = localRectangle2.intersection(localRectangle1);
/*     */ 
/* 376 */     localGraphics2D.clip(localRectangle3);
/*     */ 
/* 379 */     XPStyle.Skin localSkin = localXPStyle.getSkin(this.progressBar, localPart2);
/*     */ 
/* 382 */     localGraphics2D.setComposite(AlphaComposite.getInstance(3, 0.8F));
/* 383 */     localSkin.paintSkin(localGraphics2D, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, null);
/* 384 */     paramRectangle.translate(j, k);
/* 385 */     localGraphics2D.setComposite(AlphaComposite.getInstance(3, 0.5F));
/* 386 */     localSkin.paintSkin(localGraphics2D, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, null);
/* 387 */     paramRectangle.translate(j, k);
/* 388 */     localGraphics2D.setComposite(AlphaComposite.getInstance(3, 0.2F));
/* 389 */     localSkin.paintSkin(localGraphics2D, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, null);
/*     */ 
/* 392 */     localGraphics2D.dispose();
/*     */   }
/*     */ 
/*     */   private void paintXPBackground(Graphics paramGraphics, boolean paramBoolean, int paramInt1, int paramInt2)
/*     */   {
/* 397 */     XPStyle localXPStyle = XPStyle.getXP();
/* 398 */     if (localXPStyle == null) {
/* 399 */       return;
/*     */     }
/* 401 */     TMSchema.Part localPart = paramBoolean ? TMSchema.Part.PP_BARVERT : TMSchema.Part.PP_BAR;
/* 402 */     XPStyle.Skin localSkin = localXPStyle.getSkin(this.progressBar, localPart);
/*     */ 
/* 405 */     localSkin.paintSkin(paramGraphics, 0, 0, paramInt1, paramInt2, null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsProgressBarUI
 * JD-Core Version:    0.6.2
 */