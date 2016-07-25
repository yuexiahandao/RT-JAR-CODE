/*     */ package javax.swing.border;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Component.BaselineResizeBehavior;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.geom.Path2D;
/*     */ import java.awt.geom.Path2D.Float;
/*     */ import java.beans.ConstructorProperties;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ public class TitledBorder extends AbstractBorder
/*     */ {
/*     */   protected String title;
/*     */   protected Border border;
/*     */   protected int titlePosition;
/*     */   protected int titleJustification;
/*     */   protected Font titleFont;
/*     */   protected Color titleColor;
/*     */   private final JLabel label;
/*     */   public static final int DEFAULT_POSITION = 0;
/*     */   public static final int ABOVE_TOP = 1;
/*     */   public static final int TOP = 2;
/*     */   public static final int BELOW_TOP = 3;
/*     */   public static final int ABOVE_BOTTOM = 4;
/*     */   public static final int BOTTOM = 5;
/*     */   public static final int BELOW_BOTTOM = 6;
/*     */   public static final int DEFAULT_JUSTIFICATION = 0;
/*     */   public static final int LEFT = 1;
/*     */   public static final int CENTER = 2;
/*     */   public static final int RIGHT = 3;
/*     */   public static final int LEADING = 4;
/*     */   public static final int TRAILING = 5;
/*     */   protected static final int EDGE_SPACING = 2;
/*     */   protected static final int TEXT_SPACING = 2;
/*     */   protected static final int TEXT_INSET_H = 5;
/*     */ 
/*     */   public TitledBorder(String paramString)
/*     */   {
/* 134 */     this(null, paramString, 4, 0, null, null);
/*     */   }
/*     */ 
/*     */   public TitledBorder(Border paramBorder)
/*     */   {
/* 144 */     this(paramBorder, "", 4, 0, null, null);
/*     */   }
/*     */ 
/*     */   public TitledBorder(Border paramBorder, String paramString)
/*     */   {
/* 155 */     this(paramBorder, paramString, 4, 0, null, null);
/*     */   }
/*     */ 
/*     */   public TitledBorder(Border paramBorder, String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 171 */     this(paramBorder, paramString, paramInt1, paramInt2, null, null);
/*     */   }
/*     */ 
/*     */   public TitledBorder(Border paramBorder, String paramString, int paramInt1, int paramInt2, Font paramFont)
/*     */   {
/* 190 */     this(paramBorder, paramString, paramInt1, paramInt2, paramFont, null);
/*     */   }
/*     */ 
/*     */   @ConstructorProperties({"border", "title", "titleJustification", "titlePosition", "titleFont", "titleColor"})
/*     */   public TitledBorder(Border paramBorder, String paramString, int paramInt1, int paramInt2, Font paramFont, Color paramColor)
/*     */   {
/* 213 */     this.title = paramString;
/* 214 */     this.border = paramBorder;
/* 215 */     this.titleFont = paramFont;
/* 216 */     this.titleColor = paramColor;
/*     */ 
/* 218 */     setTitleJustification(paramInt1);
/* 219 */     setTitlePosition(paramInt2);
/*     */ 
/* 221 */     this.label = new JLabel();
/* 222 */     this.label.setOpaque(false);
/* 223 */     this.label.putClientProperty("html", null);
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 237 */     Border localBorder = getBorder();
/* 238 */     String str = getTitle();
/* 239 */     if ((str != null) && (!str.isEmpty())) {
/* 240 */       int i = (localBorder instanceof TitledBorder) ? 0 : 2;
/* 241 */       JLabel localJLabel = getLabel(paramComponent);
/* 242 */       Dimension localDimension = localJLabel.getPreferredSize();
/* 243 */       Insets localInsets = getBorderInsets(localBorder, paramComponent, new Insets(0, 0, 0, 0));
/*     */ 
/* 245 */       int j = paramInt1 + i;
/* 246 */       int k = paramInt2 + i;
/* 247 */       int m = paramInt3 - i - i;
/* 248 */       int n = paramInt4 - i - i;
/*     */ 
/* 250 */       int i1 = paramInt2;
/* 251 */       int i2 = localDimension.height;
/* 252 */       int i3 = getPosition();
/* 253 */       switch (i3) {
/*     */       case 1:
/* 255 */         localInsets.left = 0;
/* 256 */         localInsets.right = 0;
/* 257 */         k += i2 - i;
/* 258 */         n -= i2 - i;
/* 259 */         break;
/*     */       case 2:
/* 261 */         localInsets.top = (i + localInsets.top / 2 - i2 / 2);
/* 262 */         if (localInsets.top < i) {
/* 263 */           k -= localInsets.top;
/* 264 */           n += localInsets.top;
/*     */         }
/*     */         else {
/* 267 */           i1 += localInsets.top;
/*     */         }
/* 269 */         break;
/*     */       case 3:
/* 271 */         i1 += localInsets.top + i;
/* 272 */         break;
/*     */       case 4:
/* 274 */         i1 += paramInt4 - i2 - localInsets.bottom - i;
/* 275 */         break;
/*     */       case 5:
/* 277 */         i1 += paramInt4 - i2;
/* 278 */         localInsets.bottom = (i + (localInsets.bottom - i2) / 2);
/* 279 */         if (localInsets.bottom < i) {
/* 280 */           n += localInsets.bottom;
/*     */         }
/*     */         else {
/* 283 */           i1 -= localInsets.bottom;
/*     */         }
/* 285 */         break;
/*     */       case 6:
/* 287 */         localInsets.left = 0;
/* 288 */         localInsets.right = 0;
/* 289 */         i1 += paramInt4 - i2;
/* 290 */         n -= i2 - i;
/*     */       }
/*     */ 
/* 293 */       localInsets.left += i + 5;
/* 294 */       localInsets.right += i + 5;
/*     */ 
/* 296 */       int i4 = paramInt1;
/* 297 */       int i5 = paramInt3 - localInsets.left - localInsets.right;
/* 298 */       if (i5 > localDimension.width) {
/* 299 */         i5 = localDimension.width;
/*     */       }
/* 301 */       switch (getJustification(paramComponent)) {
/*     */       case 1:
/* 303 */         i4 += localInsets.left;
/* 304 */         break;
/*     */       case 3:
/* 306 */         i4 += paramInt3 - localInsets.right - i5;
/* 307 */         break;
/*     */       case 2:
/* 309 */         i4 += (paramInt3 - i5) / 2;
/*     */       }
/*     */ 
/* 313 */       if (localBorder != null) {
/* 314 */         if ((i3 != 2) && (i3 != 5)) {
/* 315 */           localBorder.paintBorder(paramComponent, paramGraphics, j, k, m, n);
/*     */         }
/*     */         else {
/* 318 */           Graphics localGraphics = paramGraphics.create();
/* 319 */           if ((localGraphics instanceof Graphics2D)) {
/* 320 */             Graphics2D localGraphics2D = (Graphics2D)localGraphics;
/* 321 */             Path2D.Float localFloat = new Path2D.Float();
/* 322 */             localFloat.append(new Rectangle(j, k, m, i1 - k), false);
/* 323 */             localFloat.append(new Rectangle(j, i1, i4 - j - 2, i2), false);
/* 324 */             localFloat.append(new Rectangle(i4 + i5 + 2, i1, j - i4 + m - i5 - 2, i2), false);
/* 325 */             localFloat.append(new Rectangle(j, i1 + i2, m, k - i1 + n - i2), false);
/* 326 */             localGraphics2D.clip(localFloat);
/*     */           }
/* 328 */           localBorder.paintBorder(paramComponent, localGraphics, j, k, m, n);
/* 329 */           localGraphics.dispose();
/*     */         }
/*     */       }
/* 332 */       paramGraphics.translate(i4, i1);
/* 333 */       localJLabel.setSize(i5, i2);
/* 334 */       localJLabel.paint(paramGraphics);
/* 335 */       paramGraphics.translate(-i4, -i1);
/*     */     }
/* 337 */     else if (localBorder != null) {
/* 338 */       localBorder.paintBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/* 348 */     Border localBorder = getBorder();
/* 349 */     paramInsets = getBorderInsets(localBorder, paramComponent, paramInsets);
/*     */ 
/* 351 */     String str = getTitle();
/* 352 */     if ((str != null) && (!str.isEmpty())) {
/* 353 */       int i = (localBorder instanceof TitledBorder) ? 0 : 2;
/* 354 */       JLabel localJLabel = getLabel(paramComponent);
/* 355 */       Dimension localDimension = localJLabel.getPreferredSize();
/*     */ 
/* 357 */       switch (getPosition()) {
/*     */       case 1:
/* 359 */         paramInsets.top += localDimension.height - i;
/* 360 */         break;
/*     */       case 2:
/* 362 */         if (paramInsets.top < localDimension.height)
/* 363 */           paramInsets.top = (localDimension.height - i); break;
/*     */       case 3:
/* 368 */         paramInsets.top += localDimension.height;
/* 369 */         break;
/*     */       case 4:
/* 371 */         paramInsets.bottom += localDimension.height;
/* 372 */         break;
/*     */       case 5:
/* 374 */         if (paramInsets.bottom < localDimension.height)
/* 375 */           paramInsets.bottom = (localDimension.height - i); break;
/*     */       case 6:
/* 380 */         paramInsets.bottom += localDimension.height - i;
/*     */       }
/*     */ 
/* 383 */       paramInsets.top += i + 2;
/* 384 */       paramInsets.left += i + 2;
/* 385 */       paramInsets.right += i + 2;
/* 386 */       paramInsets.bottom += i + 2;
/*     */     }
/* 388 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public boolean isBorderOpaque()
/*     */   {
/* 395 */     return false;
/*     */   }
/*     */ 
/*     */   public String getTitle()
/*     */   {
/* 404 */     return this.title;
/*     */   }
/*     */ 
/*     */   public Border getBorder()
/*     */   {
/* 413 */     return this.border != null ? this.border : UIManager.getBorder("TitledBorder.border");
/*     */   }
/*     */ 
/*     */   public int getTitlePosition()
/*     */   {
/* 424 */     return this.titlePosition;
/*     */   }
/*     */ 
/*     */   public int getTitleJustification()
/*     */   {
/* 433 */     return this.titleJustification;
/*     */   }
/*     */ 
/*     */   public Font getTitleFont()
/*     */   {
/* 442 */     return this.titleFont;
/*     */   }
/*     */ 
/*     */   public Color getTitleColor()
/*     */   {
/* 451 */     return this.titleColor;
/*     */   }
/*     */ 
/*     */   public void setTitle(String paramString)
/*     */   {
/* 462 */     this.title = paramString;
/*     */   }
/*     */ 
/*     */   public void setBorder(Border paramBorder)
/*     */   {
/* 470 */     this.border = paramBorder;
/*     */   }
/*     */ 
/*     */   public void setTitlePosition(int paramInt)
/*     */   {
/* 478 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/* 486 */       this.titlePosition = paramInt;
/* 487 */       break;
/*     */     default:
/* 489 */       throw new IllegalArgumentException(paramInt + " is not a valid title position.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setTitleJustification(int paramInt)
/*     */   {
/* 499 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 506 */       this.titleJustification = paramInt;
/* 507 */       break;
/*     */     default:
/* 509 */       throw new IllegalArgumentException(paramInt + " is not a valid title justification.");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setTitleFont(Font paramFont)
/*     */   {
/* 519 */     this.titleFont = paramFont;
/*     */   }
/*     */ 
/*     */   public void setTitleColor(Color paramColor)
/*     */   {
/* 527 */     this.titleColor = paramColor;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(Component paramComponent)
/*     */   {
/* 537 */     Insets localInsets = getBorderInsets(paramComponent);
/* 538 */     Dimension localDimension1 = new Dimension(localInsets.right + localInsets.left, localInsets.top + localInsets.bottom);
/*     */ 
/* 540 */     String str = getTitle();
/* 541 */     if ((str != null) && (!str.isEmpty())) {
/* 542 */       JLabel localJLabel = getLabel(paramComponent);
/* 543 */       Dimension localDimension2 = localJLabel.getPreferredSize();
/*     */ 
/* 545 */       int i = getPosition();
/* 546 */       if ((i != 1) && (i != 6)) {
/* 547 */         localDimension1.width += localDimension2.width;
/*     */       }
/* 549 */       else if (localDimension1.width < localDimension2.width) {
/* 550 */         localDimension1.width += localDimension2.width;
/*     */       }
/*     */     }
/* 553 */     return localDimension1;
/*     */   }
/*     */ 
/*     */   public int getBaseline(Component paramComponent, int paramInt1, int paramInt2)
/*     */   {
/* 565 */     if (paramComponent == null) {
/* 566 */       throw new NullPointerException("Must supply non-null component");
/*     */     }
/* 568 */     if (paramInt1 < 0) {
/* 569 */       throw new IllegalArgumentException("Width must be >= 0");
/*     */     }
/* 571 */     if (paramInt2 < 0) {
/* 572 */       throw new IllegalArgumentException("Height must be >= 0");
/*     */     }
/* 574 */     Border localBorder = getBorder();
/* 575 */     String str = getTitle();
/* 576 */     if ((str != null) && (!str.isEmpty())) {
/* 577 */       int i = (localBorder instanceof TitledBorder) ? 0 : 2;
/* 578 */       JLabel localJLabel = getLabel(paramComponent);
/* 579 */       Dimension localDimension = localJLabel.getPreferredSize();
/* 580 */       Insets localInsets = getBorderInsets(localBorder, paramComponent, new Insets(0, 0, 0, 0));
/*     */ 
/* 582 */       int j = localJLabel.getBaseline(localDimension.width, localDimension.height);
/* 583 */       switch (getPosition()) {
/*     */       case 1:
/* 585 */         return j;
/*     */       case 2:
/* 587 */         localInsets.top = (i + (localInsets.top - localDimension.height) / 2);
/* 588 */         return localInsets.top < i ? j : j + localInsets.top;
/*     */       case 3:
/* 592 */         return j + localInsets.top + i;
/*     */       case 4:
/* 594 */         return j + paramInt2 - localDimension.height - localInsets.bottom - i;
/*     */       case 5:
/* 596 */         localInsets.bottom = (i + (localInsets.bottom - localDimension.height) / 2);
/* 597 */         return localInsets.bottom < i ? j + paramInt2 - localDimension.height : j + paramInt2 - localDimension.height + localInsets.bottom;
/*     */       case 6:
/* 601 */         return j + paramInt2 - localDimension.height;
/*     */       }
/*     */     }
/* 604 */     return -1;
/*     */   }
/*     */ 
/*     */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(Component paramComponent)
/*     */   {
/* 617 */     super.getBaselineResizeBehavior(paramComponent);
/* 618 */     switch (getPosition()) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 622 */       return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/* 626 */       return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
/*     */     }
/* 628 */     return Component.BaselineResizeBehavior.OTHER;
/*     */   }
/*     */ 
/*     */   private int getPosition() {
/* 632 */     int i = getTitlePosition();
/* 633 */     if (i != 0) {
/* 634 */       return i;
/*     */     }
/* 636 */     Object localObject = UIManager.get("TitledBorder.position");
/* 637 */     if ((localObject instanceof Integer)) {
/* 638 */       int j = ((Integer)localObject).intValue();
/* 639 */       if ((0 < j) && (j <= 6)) {
/* 640 */         return j;
/*     */       }
/*     */     }
/* 643 */     else if ((localObject instanceof String)) {
/* 644 */       String str = (String)localObject;
/* 645 */       if (str.equalsIgnoreCase("ABOVE_TOP")) {
/* 646 */         return 1;
/*     */       }
/* 648 */       if (str.equalsIgnoreCase("TOP")) {
/* 649 */         return 2;
/*     */       }
/* 651 */       if (str.equalsIgnoreCase("BELOW_TOP")) {
/* 652 */         return 3;
/*     */       }
/* 654 */       if (str.equalsIgnoreCase("ABOVE_BOTTOM")) {
/* 655 */         return 4;
/*     */       }
/* 657 */       if (str.equalsIgnoreCase("BOTTOM")) {
/* 658 */         return 5;
/*     */       }
/* 660 */       if (str.equalsIgnoreCase("BELOW_BOTTOM")) {
/* 661 */         return 6;
/*     */       }
/*     */     }
/* 664 */     return 2;
/*     */   }
/*     */ 
/*     */   private int getJustification(Component paramComponent) {
/* 668 */     int i = getTitleJustification();
/* 669 */     if ((i == 4) || (i == 0)) {
/* 670 */       return paramComponent.getComponentOrientation().isLeftToRight() ? 1 : 3;
/*     */     }
/* 672 */     if (i == 5) {
/* 673 */       return paramComponent.getComponentOrientation().isLeftToRight() ? 3 : 1;
/*     */     }
/* 675 */     return i;
/*     */   }
/*     */ 
/*     */   protected Font getFont(Component paramComponent) {
/* 679 */     Font localFont = getTitleFont();
/* 680 */     if (localFont != null) {
/* 681 */       return localFont;
/*     */     }
/* 683 */     localFont = UIManager.getFont("TitledBorder.font");
/* 684 */     if (localFont != null) {
/* 685 */       return localFont;
/*     */     }
/* 687 */     if (paramComponent != null) {
/* 688 */       localFont = paramComponent.getFont();
/* 689 */       if (localFont != null) {
/* 690 */         return localFont;
/*     */       }
/*     */     }
/* 693 */     return new Font("Dialog", 0, 12);
/*     */   }
/*     */ 
/*     */   private Color getColor(Component paramComponent) {
/* 697 */     Color localColor = getTitleColor();
/* 698 */     if (localColor != null) {
/* 699 */       return localColor;
/*     */     }
/* 701 */     localColor = UIManager.getColor("TitledBorder.titleColor");
/* 702 */     if (localColor != null) {
/* 703 */       return localColor;
/*     */     }
/* 705 */     return paramComponent != null ? paramComponent.getForeground() : null;
/*     */   }
/*     */ 
/*     */   private JLabel getLabel(Component paramComponent)
/*     */   {
/* 711 */     this.label.setText(getTitle());
/* 712 */     this.label.setFont(getFont(paramComponent));
/* 713 */     this.label.setForeground(getColor(paramComponent));
/* 714 */     this.label.setComponentOrientation(paramComponent.getComponentOrientation());
/* 715 */     this.label.setEnabled(paramComponent.isEnabled());
/* 716 */     return this.label;
/*     */   }
/*     */ 
/*     */   private static Insets getBorderInsets(Border paramBorder, Component paramComponent, Insets paramInsets) {
/* 720 */     if (paramBorder == null) {
/* 721 */       paramInsets.set(0, 0, 0, 0);
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject;
/* 723 */       if ((paramBorder instanceof AbstractBorder)) {
/* 724 */         localObject = (AbstractBorder)paramBorder;
/* 725 */         paramInsets = ((AbstractBorder)localObject).getBorderInsets(paramComponent, paramInsets);
/*     */       }
/*     */       else {
/* 728 */         localObject = paramBorder.getBorderInsets(paramComponent);
/* 729 */         paramInsets.set(((Insets)localObject).top, ((Insets)localObject).left, ((Insets)localObject).bottom, ((Insets)localObject).right);
/*     */       }
/*     */     }
/* 731 */     return paramInsets;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.border.TitledBorder
 * JD-Core Version:    0.6.2
 */