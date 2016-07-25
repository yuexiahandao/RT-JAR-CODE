/*     */ package javax.swing.tree;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.JTree.DropLocation;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicGraphicsUtils;
/*     */ import sun.swing.DefaultLookup;
/*     */ 
/*     */ public class DefaultTreeCellRenderer extends JLabel
/*     */   implements TreeCellRenderer
/*     */ {
/*     */   private JTree tree;
/*     */   protected boolean selected;
/*     */   protected boolean hasFocus;
/*     */   private boolean drawsFocusBorderAroundIcon;
/*     */   private boolean drawDashedFocusIndicator;
/*     */   private Color treeBGColor;
/*     */   private Color focusBGColor;
/*     */   protected transient Icon closedIcon;
/*     */   protected transient Icon leafIcon;
/*     */   protected transient Icon openIcon;
/*     */   protected Color textSelectionColor;
/*     */   protected Color textNonSelectionColor;
/*     */   protected Color backgroundSelectionColor;
/*     */   protected Color backgroundNonSelectionColor;
/*     */   protected Color borderSelectionColor;
/*     */   private boolean isDropCell;
/* 159 */   private boolean fillBackground = true;
/*     */   private boolean inited;
/*     */ 
/*     */   public DefaultTreeCellRenderer()
/*     */   {
/* 171 */     this.inited = true;
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 180 */     super.updateUI();
/*     */ 
/* 188 */     if ((!this.inited) || ((getLeafIcon() instanceof UIResource))) {
/* 189 */       setLeafIcon(DefaultLookup.getIcon(this, this.ui, "Tree.leafIcon"));
/*     */     }
/* 191 */     if ((!this.inited) || ((getClosedIcon() instanceof UIResource))) {
/* 192 */       setClosedIcon(DefaultLookup.getIcon(this, this.ui, "Tree.closedIcon"));
/*     */     }
/* 194 */     if ((!this.inited) || ((getOpenIcon() instanceof UIManager))) {
/* 195 */       setOpenIcon(DefaultLookup.getIcon(this, this.ui, "Tree.openIcon"));
/*     */     }
/* 197 */     if ((!this.inited) || ((getTextSelectionColor() instanceof UIResource))) {
/* 198 */       setTextSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.selectionForeground"));
/*     */     }
/*     */ 
/* 201 */     if ((!this.inited) || ((getTextNonSelectionColor() instanceof UIResource))) {
/* 202 */       setTextNonSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.textForeground"));
/*     */     }
/*     */ 
/* 205 */     if ((!this.inited) || ((getBackgroundSelectionColor() instanceof UIResource))) {
/* 206 */       setBackgroundSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.selectionBackground"));
/*     */     }
/*     */ 
/* 209 */     if ((!this.inited) || ((getBackgroundNonSelectionColor() instanceof UIResource)))
/*     */     {
/* 211 */       setBackgroundNonSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.textBackground"));
/*     */     }
/*     */ 
/* 214 */     if ((!this.inited) || ((getBorderSelectionColor() instanceof UIResource))) {
/* 215 */       setBorderSelectionColor(DefaultLookup.getColor(this, this.ui, "Tree.selectionBorderColor"));
/*     */     }
/*     */ 
/* 218 */     this.drawsFocusBorderAroundIcon = DefaultLookup.getBoolean(this, this.ui, "Tree.drawsFocusBorderAroundIcon", false);
/*     */ 
/* 220 */     this.drawDashedFocusIndicator = DefaultLookup.getBoolean(this, this.ui, "Tree.drawDashedFocusIndicator", false);
/*     */ 
/* 223 */     this.fillBackground = DefaultLookup.getBoolean(this, this.ui, "Tree.rendererFillBackground", true);
/* 224 */     Insets localInsets = DefaultLookup.getInsets(this, this.ui, "Tree.rendererMargins");
/* 225 */     if (localInsets != null) {
/* 226 */       setBorder(new EmptyBorder(localInsets.top, localInsets.left, localInsets.bottom, localInsets.right));
/*     */     }
/*     */ 
/* 230 */     setName("Tree.cellRenderer");
/*     */   }
/*     */ 
/*     */   public Icon getDefaultOpenIcon()
/*     */   {
/* 239 */     return DefaultLookup.getIcon(this, this.ui, "Tree.openIcon");
/*     */   }
/*     */ 
/*     */   public Icon getDefaultClosedIcon()
/*     */   {
/* 247 */     return DefaultLookup.getIcon(this, this.ui, "Tree.closedIcon");
/*     */   }
/*     */ 
/*     */   public Icon getDefaultLeafIcon()
/*     */   {
/* 255 */     return DefaultLookup.getIcon(this, this.ui, "Tree.leafIcon");
/*     */   }
/*     */ 
/*     */   public void setOpenIcon(Icon paramIcon)
/*     */   {
/* 262 */     this.openIcon = paramIcon;
/*     */   }
/*     */ 
/*     */   public Icon getOpenIcon()
/*     */   {
/* 269 */     return this.openIcon;
/*     */   }
/*     */ 
/*     */   public void setClosedIcon(Icon paramIcon)
/*     */   {
/* 276 */     this.closedIcon = paramIcon;
/*     */   }
/*     */ 
/*     */   public Icon getClosedIcon()
/*     */   {
/* 284 */     return this.closedIcon;
/*     */   }
/*     */ 
/*     */   public void setLeafIcon(Icon paramIcon)
/*     */   {
/* 291 */     this.leafIcon = paramIcon;
/*     */   }
/*     */ 
/*     */   public Icon getLeafIcon()
/*     */   {
/* 298 */     return this.leafIcon;
/*     */   }
/*     */ 
/*     */   public void setTextSelectionColor(Color paramColor)
/*     */   {
/* 305 */     this.textSelectionColor = paramColor;
/*     */   }
/*     */ 
/*     */   public Color getTextSelectionColor()
/*     */   {
/* 312 */     return this.textSelectionColor;
/*     */   }
/*     */ 
/*     */   public void setTextNonSelectionColor(Color paramColor)
/*     */   {
/* 319 */     this.textNonSelectionColor = paramColor;
/*     */   }
/*     */ 
/*     */   public Color getTextNonSelectionColor()
/*     */   {
/* 326 */     return this.textNonSelectionColor;
/*     */   }
/*     */ 
/*     */   public void setBackgroundSelectionColor(Color paramColor)
/*     */   {
/* 333 */     this.backgroundSelectionColor = paramColor;
/*     */   }
/*     */ 
/*     */   public Color getBackgroundSelectionColor()
/*     */   {
/* 341 */     return this.backgroundSelectionColor;
/*     */   }
/*     */ 
/*     */   public void setBackgroundNonSelectionColor(Color paramColor)
/*     */   {
/* 348 */     this.backgroundNonSelectionColor = paramColor;
/*     */   }
/*     */ 
/*     */   public Color getBackgroundNonSelectionColor()
/*     */   {
/* 355 */     return this.backgroundNonSelectionColor;
/*     */   }
/*     */ 
/*     */   public void setBorderSelectionColor(Color paramColor)
/*     */   {
/* 362 */     this.borderSelectionColor = paramColor;
/*     */   }
/*     */ 
/*     */   public Color getBorderSelectionColor()
/*     */   {
/* 369 */     return this.borderSelectionColor;
/*     */   }
/*     */ 
/*     */   public void setFont(Font paramFont)
/*     */   {
/* 380 */     if ((paramFont instanceof FontUIResource))
/* 381 */       paramFont = null;
/* 382 */     super.setFont(paramFont);
/*     */   }
/*     */ 
/*     */   public Font getFont()
/*     */   {
/* 391 */     Font localFont = super.getFont();
/*     */ 
/* 393 */     if ((localFont == null) && (this.tree != null))
/*     */     {
/* 396 */       localFont = this.tree.getFont();
/*     */     }
/* 398 */     return localFont;
/*     */   }
/*     */ 
/*     */   public void setBackground(Color paramColor)
/*     */   {
/* 410 */     if ((paramColor instanceof ColorUIResource))
/* 411 */       paramColor = null;
/* 412 */     super.setBackground(paramColor);
/*     */   }
/*     */ 
/*     */   public Component getTreeCellRendererComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4)
/*     */   {
/* 429 */     String str = paramJTree.convertValueToText(paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, paramBoolean4);
/*     */ 
/* 432 */     this.tree = paramJTree;
/* 433 */     this.hasFocus = paramBoolean4;
/* 434 */     setText(str);
/*     */ 
/* 436 */     Object localObject1 = null;
/* 437 */     this.isDropCell = false;
/*     */ 
/* 439 */     JTree.DropLocation localDropLocation = paramJTree.getDropLocation();
/* 440 */     if ((localDropLocation != null) && (localDropLocation.getChildIndex() == -1) && (paramJTree.getRowForPath(localDropLocation.getPath()) == paramInt))
/*     */     {
/* 444 */       localObject2 = DefaultLookup.getColor(this, this.ui, "Tree.dropCellForeground");
/* 445 */       if (localObject2 != null)
/* 446 */         localObject1 = localObject2;
/*     */       else {
/* 448 */         localObject1 = getTextSelectionColor();
/*     */       }
/*     */ 
/* 451 */       this.isDropCell = true;
/* 452 */     } else if (paramBoolean1) {
/* 453 */       localObject1 = getTextSelectionColor();
/*     */     } else {
/* 455 */       localObject1 = getTextNonSelectionColor();
/*     */     }
/*     */ 
/* 458 */     setForeground((Color)localObject1);
/*     */ 
/* 460 */     Object localObject2 = null;
/* 461 */     if (paramBoolean3)
/* 462 */       localObject2 = getLeafIcon();
/* 463 */     else if (paramBoolean2)
/* 464 */       localObject2 = getOpenIcon();
/*     */     else {
/* 466 */       localObject2 = getClosedIcon();
/*     */     }
/*     */ 
/* 469 */     if (!paramJTree.isEnabled()) {
/* 470 */       setEnabled(false);
/* 471 */       LookAndFeel localLookAndFeel = UIManager.getLookAndFeel();
/* 472 */       Icon localIcon = localLookAndFeel.getDisabledIcon(paramJTree, (Icon)localObject2);
/* 473 */       if (localIcon != null) localObject2 = localIcon;
/* 474 */       setDisabledIcon((Icon)localObject2);
/*     */     } else {
/* 476 */       setEnabled(true);
/* 477 */       setIcon((Icon)localObject2);
/*     */     }
/* 479 */     setComponentOrientation(paramJTree.getComponentOrientation());
/*     */ 
/* 481 */     this.selected = paramBoolean1;
/*     */ 
/* 483 */     return this;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics)
/*     */   {
/*     */     Color localColor;
/* 492 */     if (this.isDropCell) {
/* 493 */       localColor = DefaultLookup.getColor(this, this.ui, "Tree.dropCellBackground");
/* 494 */       if (localColor == null)
/* 495 */         localColor = getBackgroundSelectionColor();
/*     */     }
/* 497 */     else if (this.selected) {
/* 498 */       localColor = getBackgroundSelectionColor();
/*     */     } else {
/* 500 */       localColor = getBackgroundNonSelectionColor();
/* 501 */       if (localColor == null) {
/* 502 */         localColor = getBackground();
/*     */       }
/*     */     }
/*     */ 
/* 506 */     int i = -1;
/* 507 */     if ((localColor != null) && (this.fillBackground)) {
/* 508 */       i = getLabelStart();
/* 509 */       paramGraphics.setColor(localColor);
/* 510 */       if (getComponentOrientation().isLeftToRight()) {
/* 511 */         paramGraphics.fillRect(i, 0, getWidth() - i, getHeight());
/*     */       }
/*     */       else {
/* 514 */         paramGraphics.fillRect(0, 0, getWidth() - i, getHeight());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 519 */     if (this.hasFocus) {
/* 520 */       if (this.drawsFocusBorderAroundIcon) {
/* 521 */         i = 0;
/*     */       }
/* 523 */       else if (i == -1) {
/* 524 */         i = getLabelStart();
/*     */       }
/* 526 */       if (getComponentOrientation().isLeftToRight()) {
/* 527 */         paintFocus(paramGraphics, i, 0, getWidth() - i, getHeight(), localColor);
/*     */       }
/*     */       else {
/* 530 */         paintFocus(paramGraphics, 0, 0, getWidth() - i, getHeight(), localColor);
/*     */       }
/*     */     }
/* 533 */     super.paint(paramGraphics);
/*     */   }
/*     */ 
/*     */   private void paintFocus(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor) {
/* 537 */     Color localColor = getBorderSelectionColor();
/*     */ 
/* 539 */     if ((localColor != null) && ((this.selected) || (!this.drawDashedFocusIndicator))) {
/* 540 */       paramGraphics.setColor(localColor);
/* 541 */       paramGraphics.drawRect(paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */     }
/* 543 */     if ((this.drawDashedFocusIndicator) && (paramColor != null)) {
/* 544 */       if (this.treeBGColor != paramColor) {
/* 545 */         this.treeBGColor = paramColor;
/* 546 */         this.focusBGColor = new Color(paramColor.getRGB() ^ 0xFFFFFFFF);
/*     */       }
/* 548 */       paramGraphics.setColor(this.focusBGColor);
/* 549 */       BasicGraphicsUtils.drawDashedRect(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getLabelStart() {
/* 554 */     Icon localIcon = getIcon();
/* 555 */     if ((localIcon != null) && (getText() != null)) {
/* 556 */       return localIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
/*     */     }
/* 558 */     return 0;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 566 */     Dimension localDimension = super.getPreferredSize();
/*     */ 
/* 568 */     if (localDimension != null) {
/* 569 */       localDimension = new Dimension(localDimension.width + 3, localDimension.height);
/*     */     }
/* 571 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void validate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void invalidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void revalidate()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void repaint(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void repaint(Rectangle paramRectangle)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void repaint()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 627 */     if ((paramString == "text") || (((paramString == "font") || (paramString == "foreground")) && (paramObject1 != paramObject2) && (getClientProperty("html") != null)))
/*     */     {
/* 632 */       super.firePropertyChange(paramString, paramObject1, paramObject2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, byte paramByte1, byte paramByte2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, char paramChar1, char paramChar2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, short paramShort1, short paramShort2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, long paramLong1, long paramLong2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, float paramFloat1, float paramFloat2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, double paramDouble1, double paramDouble2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.DefaultTreeCellRenderer
 * JD-Core Version:    0.6.2
 */