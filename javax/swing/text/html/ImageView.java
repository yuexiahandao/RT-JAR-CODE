/*      */ package javax.swing.text.html;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Container;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Dictionary;
/*      */ import javax.swing.GrayFilter;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.GlyphView.GlyphPainter;
/*      */ import javax.swing.text.Highlighter;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.LayeredHighlighter;
/*      */ import javax.swing.text.Position.Bias;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.text.StyledDocument;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ 
/*      */ public class ImageView extends View
/*      */ {
/*   57 */   private static boolean sIsInc = false;
/*      */ 
/*   61 */   private static int sIncRate = 100;
/*      */   private static final String PENDING_IMAGE = "html.pendingImage";
/*      */   private static final String MISSING_IMAGE = "html.missingImage";
/*      */   private static final String IMAGE_CACHE_PROPERTY = "imageCache";
/*      */   private static final int DEFAULT_WIDTH = 38;
/*      */   private static final int DEFAULT_HEIGHT = 38;
/*      */   private static final int DEFAULT_BORDER = 2;
/*      */   private static final int LOADING_FLAG = 1;
/*      */   private static final int LINK_FLAG = 2;
/*      */   private static final int WIDTH_FLAG = 4;
/*      */   private static final int HEIGHT_FLAG = 8;
/*      */   private static final int RELOAD_FLAG = 16;
/*      */   private static final int RELOAD_IMAGE_FLAG = 32;
/*      */   private static final int SYNC_LOAD_FLAG = 64;
/*      */   private AttributeSet attr;
/*      */   private Image image;
/*      */   private Image disabledImage;
/*      */   private int width;
/*      */   private int height;
/*      */   private int state;
/*      */   private Container container;
/*      */   private Rectangle fBounds;
/*      */   private Color borderColor;
/*      */   private short borderSize;
/*      */   private short leftInset;
/*      */   private short rightInset;
/*      */   private short topInset;
/*      */   private short bottomInset;
/*      */   private ImageObserver imageObserver;
/*      */   private View altView;
/*      */   private float vAlign;
/*      */ 
/*      */   public ImageView(Element paramElement)
/*      */   {
/*  137 */     super(paramElement);
/*  138 */     this.fBounds = new Rectangle();
/*  139 */     this.imageObserver = new ImageHandler(null);
/*  140 */     this.state = 48;
/*      */   }
/*      */ 
/*      */   public String getAltText()
/*      */   {
/*  149 */     return (String)getElement().getAttributes().getAttribute(HTML.Attribute.ALT);
/*      */   }
/*      */ 
/*      */   public URL getImageURL()
/*      */   {
/*  158 */     String str = (String)getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
/*      */ 
/*  160 */     if (str == null) {
/*  161 */       return null;
/*      */     }
/*      */ 
/*  164 */     URL localURL1 = ((HTMLDocument)getDocument()).getBase();
/*      */     try {
/*  166 */       return new URL(localURL1, str);
/*      */     } catch (MalformedURLException localMalformedURLException) {
/*      */     }
/*  169 */     return null;
/*      */   }
/*      */ 
/*      */   public Icon getNoImageIcon()
/*      */   {
/*  177 */     return (Icon)UIManager.getLookAndFeelDefaults().get("html.missingImage");
/*      */   }
/*      */ 
/*      */   public Icon getLoadingImageIcon()
/*      */   {
/*  184 */     return (Icon)UIManager.getLookAndFeelDefaults().get("html.pendingImage");
/*      */   }
/*      */ 
/*      */   public Image getImage()
/*      */   {
/*  191 */     sync();
/*  192 */     return this.image;
/*      */   }
/*      */ 
/*      */   private Image getImage(boolean paramBoolean) {
/*  196 */     Image localImage = getImage();
/*  197 */     if (!paramBoolean) {
/*  198 */       if (this.disabledImage == null) {
/*  199 */         this.disabledImage = GrayFilter.createDisabledImage(localImage);
/*      */       }
/*  201 */       localImage = this.disabledImage;
/*      */     }
/*  203 */     return localImage;
/*      */   }
/*      */ 
/*      */   public void setLoadsSynchronously(boolean paramBoolean)
/*      */   {
/*  213 */     synchronized (this) {
/*  214 */       if (paramBoolean) {
/*  215 */         this.state |= 64;
/*      */       }
/*      */       else
/*  218 */         this.state = ((this.state | 0x40) ^ 0x40);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getLoadsSynchronously()
/*      */   {
/*  227 */     return (this.state & 0x40) != 0;
/*      */   }
/*      */ 
/*      */   protected StyleSheet getStyleSheet()
/*      */   {
/*  234 */     HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/*  235 */     return localHTMLDocument.getStyleSheet();
/*      */   }
/*      */ 
/*      */   public AttributeSet getAttributes()
/*      */   {
/*  244 */     sync();
/*  245 */     return this.attr;
/*      */   }
/*      */ 
/*      */   public String getToolTipText(float paramFloat1, float paramFloat2, Shape paramShape)
/*      */   {
/*  256 */     return getAltText();
/*      */   }
/*      */ 
/*      */   protected void setPropertiesFromAttributes()
/*      */   {
/*  263 */     StyleSheet localStyleSheet = getStyleSheet();
/*  264 */     this.attr = localStyleSheet.getViewAttributes(this);
/*      */ 
/*  267 */     this.borderSize = ((short)getIntAttr(HTML.Attribute.BORDER, isLink() ? 2 : 0));
/*      */ 
/*  270 */     this.leftInset = (this.rightInset = (short)(getIntAttr(HTML.Attribute.HSPACE, 0) + this.borderSize));
/*      */ 
/*  272 */     this.topInset = (this.bottomInset = (short)(getIntAttr(HTML.Attribute.VSPACE, 0) + this.borderSize));
/*      */ 
/*  275 */     this.borderColor = ((StyledDocument)getDocument()).getForeground(getAttributes());
/*      */ 
/*  278 */     AttributeSet localAttributeSet1 = getElement().getAttributes();
/*      */ 
/*  283 */     Object localObject1 = localAttributeSet1.getAttribute(HTML.Attribute.ALIGN);
/*      */ 
/*  285 */     this.vAlign = 1.0F;
/*  286 */     if (localObject1 != null) {
/*  287 */       localObject1 = localObject1.toString();
/*  288 */       if ("top".equals(localObject1)) {
/*  289 */         this.vAlign = 0.0F;
/*      */       }
/*  291 */       else if ("middle".equals(localObject1)) {
/*  292 */         this.vAlign = 0.5F;
/*      */       }
/*      */     }
/*      */ 
/*  296 */     AttributeSet localAttributeSet2 = (AttributeSet)localAttributeSet1.getAttribute(HTML.Tag.A);
/*  297 */     if ((localAttributeSet2 != null) && (localAttributeSet2.isDefined(HTML.Attribute.HREF)))
/*      */     {
/*  299 */       synchronized (this) {
/*  300 */         this.state |= 2;
/*      */       }
/*      */     }
/*      */     else
/*  304 */       synchronized (this) {
/*  305 */         this.state = ((this.state | 0x2) ^ 0x2);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void setParent(View paramView)
/*      */   {
/*  315 */     View localView = getParent();
/*  316 */     super.setParent(paramView);
/*  317 */     this.container = (paramView != null ? getContainer() : null);
/*  318 */     if (localView != paramView)
/*  319 */       synchronized (this) {
/*  320 */         this.state |= 16;
/*      */       }
/*      */   }
/*      */ 
/*      */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*      */   {
/*  329 */     super.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*      */ 
/*  331 */     synchronized (this) {
/*  332 */       this.state |= 48;
/*      */     }
/*      */ 
/*  336 */     preferenceChanged(null, true, true);
/*      */   }
/*      */ 
/*      */   public void paint(Graphics paramGraphics, Shape paramShape)
/*      */   {
/*  347 */     sync();
/*      */ 
/*  349 */     Rectangle localRectangle1 = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*      */ 
/*  351 */     Rectangle localRectangle2 = paramGraphics.getClipBounds();
/*      */ 
/*  353 */     this.fBounds.setBounds(localRectangle1);
/*  354 */     paintHighlights(paramGraphics, paramShape);
/*  355 */     paintBorder(paramGraphics, localRectangle1);
/*  356 */     if (localRectangle2 != null) {
/*  357 */       paramGraphics.clipRect(localRectangle1.x + this.leftInset, localRectangle1.y + this.topInset, localRectangle1.width - this.leftInset - this.rightInset, localRectangle1.height - this.topInset - this.bottomInset);
/*      */     }
/*      */ 
/*  362 */     Container localContainer = getContainer();
/*  363 */     Image localImage = getImage((localContainer == null) || (localContainer.isEnabled()));
/*      */     Icon localIcon;
/*  364 */     if (localImage != null) {
/*  365 */       if (!hasPixels(localImage))
/*      */       {
/*  367 */         localIcon = getLoadingImageIcon();
/*  368 */         if (localIcon != null) {
/*  369 */           localIcon.paintIcon(localContainer, paramGraphics, localRectangle1.x + this.leftInset, localRectangle1.y + this.topInset);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  375 */         paramGraphics.drawImage(localImage, localRectangle1.x + this.leftInset, localRectangle1.y + this.topInset, this.width, this.height, this.imageObserver);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  380 */       localIcon = getNoImageIcon();
/*  381 */       if (localIcon != null) {
/*  382 */         localIcon.paintIcon(localContainer, paramGraphics, localRectangle1.x + this.leftInset, localRectangle1.y + this.topInset);
/*      */       }
/*      */ 
/*  385 */       View localView = getAltView();
/*      */ 
/*  387 */       if ((localView != null) && (((this.state & 0x4) == 0) || (this.width > 38)))
/*      */       {
/*  390 */         Rectangle localRectangle3 = new Rectangle(localRectangle1.x + this.leftInset + 38, localRectangle1.y + this.topInset, localRectangle1.width - this.leftInset - this.rightInset - 38, localRectangle1.height - this.topInset - this.bottomInset);
/*      */ 
/*  395 */         localView.paint(paramGraphics, localRectangle3);
/*      */       }
/*      */     }
/*  398 */     if (localRectangle2 != null)
/*      */     {
/*  400 */       paramGraphics.setClip(localRectangle2.x, localRectangle2.y, localRectangle2.width, localRectangle2.height);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintHighlights(Graphics paramGraphics, Shape paramShape) {
/*  405 */     if ((this.container instanceof JTextComponent)) {
/*  406 */       JTextComponent localJTextComponent = (JTextComponent)this.container;
/*  407 */       Highlighter localHighlighter = localJTextComponent.getHighlighter();
/*  408 */       if ((localHighlighter instanceof LayeredHighlighter))
/*  409 */         ((LayeredHighlighter)localHighlighter).paintLayeredHighlights(paramGraphics, getStartOffset(), getEndOffset(), paramShape, localJTextComponent, this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void paintBorder(Graphics paramGraphics, Rectangle paramRectangle)
/*      */   {
/*  416 */     Color localColor = this.borderColor;
/*      */ 
/*  418 */     if (((this.borderSize > 0) || (this.image == null)) && (localColor != null)) {
/*  419 */       int i = this.leftInset - this.borderSize;
/*  420 */       int j = this.topInset - this.borderSize;
/*  421 */       paramGraphics.setColor(localColor);
/*  422 */       int k = this.image == null ? 1 : this.borderSize;
/*  423 */       for (int m = 0; m < k; m++)
/*  424 */         paramGraphics.drawRect(paramRectangle.x + i + m, paramRectangle.y + j + m, paramRectangle.width - m - m - i - i - 1, paramRectangle.height - m - m - j - j - 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public float getPreferredSpan(int paramInt)
/*      */   {
/*  443 */     sync();
/*      */ 
/*  446 */     if ((paramInt == 0) && ((this.state & 0x4) == 4)) {
/*  447 */       getPreferredSpanFromAltView(paramInt);
/*  448 */       return this.width + this.leftInset + this.rightInset;
/*      */     }
/*  450 */     if ((paramInt == 1) && ((this.state & 0x8) == 8)) {
/*  451 */       getPreferredSpanFromAltView(paramInt);
/*  452 */       return this.height + this.topInset + this.bottomInset;
/*      */     }
/*      */ 
/*  455 */     Image localImage = getImage();
/*      */ 
/*  457 */     if (localImage != null) {
/*  458 */       switch (paramInt) {
/*      */       case 0:
/*  460 */         return this.width + this.leftInset + this.rightInset;
/*      */       case 1:
/*  462 */         return this.height + this.topInset + this.bottomInset;
/*      */       }
/*  464 */       throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*      */     }
/*      */ 
/*  468 */     View localView = getAltView();
/*  469 */     float f = 0.0F;
/*      */ 
/*  471 */     if (localView != null) {
/*  472 */       f = localView.getPreferredSpan(paramInt);
/*      */     }
/*  474 */     switch (paramInt) {
/*      */     case 0:
/*  476 */       return f + (this.width + this.leftInset + this.rightInset);
/*      */     case 1:
/*  478 */       return f + (this.height + this.topInset + this.bottomInset);
/*      */     }
/*  480 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*      */   }
/*      */ 
/*      */   public float getAlignment(int paramInt)
/*      */   {
/*  499 */     switch (paramInt) {
/*      */     case 1:
/*  501 */       return this.vAlign;
/*      */     }
/*  503 */     return super.getAlignment(paramInt);
/*      */   }
/*      */ 
/*      */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*      */     throws BadLocationException
/*      */   {
/*  519 */     int i = getStartOffset();
/*  520 */     int j = getEndOffset();
/*  521 */     if ((paramInt >= i) && (paramInt <= j)) {
/*  522 */       Rectangle localRectangle = paramShape.getBounds();
/*  523 */       if (paramInt == j) {
/*  524 */         localRectangle.x += localRectangle.width;
/*      */       }
/*  526 */       localRectangle.width = 0;
/*  527 */       return localRectangle;
/*      */     }
/*  529 */     return null;
/*      */   }
/*      */ 
/*      */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*      */   {
/*  544 */     Rectangle localRectangle = (Rectangle)paramShape;
/*  545 */     if (paramFloat1 < localRectangle.x + localRectangle.width) {
/*  546 */       paramArrayOfBias[0] = Position.Bias.Forward;
/*  547 */       return getStartOffset();
/*      */     }
/*  549 */     paramArrayOfBias[0] = Position.Bias.Backward;
/*  550 */     return getEndOffset();
/*      */   }
/*      */ 
/*      */   public void setSize(float paramFloat1, float paramFloat2)
/*      */   {
/*  561 */     sync();
/*      */ 
/*  563 */     if (getImage() == null) {
/*  564 */       View localView = getAltView();
/*      */ 
/*  566 */       if (localView != null)
/*  567 */         localView.setSize(Math.max(0.0F, paramFloat1 - (38 + this.leftInset + this.rightInset)), Math.max(0.0F, paramFloat2 - (this.topInset + this.bottomInset)));
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isLink()
/*      */   {
/*  577 */     return (this.state & 0x2) == 2;
/*      */   }
/*      */ 
/*      */   private boolean hasPixels(Image paramImage)
/*      */   {
/*  584 */     return (paramImage != null) && (paramImage.getHeight(this.imageObserver) > 0) && (paramImage.getWidth(this.imageObserver) > 0);
/*      */   }
/*      */ 
/*      */   private float getPreferredSpanFromAltView(int paramInt)
/*      */   {
/*  594 */     if (getImage() == null) {
/*  595 */       View localView = getAltView();
/*      */ 
/*  597 */       if (localView != null) {
/*  598 */         return localView.getPreferredSpan(paramInt);
/*      */       }
/*      */     }
/*  601 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   private void repaint(long paramLong)
/*      */   {
/*  609 */     if ((this.container != null) && (this.fBounds != null))
/*  610 */       this.container.repaint(paramLong, this.fBounds.x, this.fBounds.y, this.fBounds.width, this.fBounds.height);
/*      */   }
/*      */ 
/*      */   private int getIntAttr(HTML.Attribute paramAttribute, int paramInt)
/*      */   {
/*  620 */     AttributeSet localAttributeSet = getElement().getAttributes();
/*  621 */     if (localAttributeSet.isDefined(paramAttribute))
/*      */     {
/*  623 */       String str = (String)localAttributeSet.getAttribute(paramAttribute);
/*      */       int i;
/*  624 */       if (str == null)
/*  625 */         i = paramInt;
/*      */       else {
/*      */         try
/*      */         {
/*  629 */           i = Math.max(0, Integer.parseInt(str));
/*      */         } catch (NumberFormatException localNumberFormatException) {
/*  631 */           i = paramInt;
/*      */         }
/*      */       }
/*  634 */       return i;
/*      */     }
/*  636 */     return paramInt;
/*      */   }
/*      */ 
/*      */   private void sync()
/*      */   {
/*  643 */     int i = this.state;
/*  644 */     if ((i & 0x20) != 0) {
/*  645 */       refreshImage();
/*      */     }
/*  647 */     i = this.state;
/*  648 */     if ((i & 0x10) != 0) {
/*  649 */       synchronized (this) {
/*  650 */         this.state = ((this.state | 0x10) ^ 0x10);
/*      */       }
/*  652 */       setPropertiesFromAttributes();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void refreshImage()
/*      */   {
/*  662 */     synchronized (this)
/*      */     {
/*  664 */       this.state = ((this.state | 0x1 | 0x20 | 0x4 | 0x8) ^ 0x2C);
/*      */ 
/*  667 */       this.image = null;
/*  668 */       this.width = (this.height = 0);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  673 */       loadImage();
/*      */ 
/*  676 */       updateImageSize();
/*      */     }
/*      */     finally {
/*  679 */       synchronized (this)
/*      */       {
/*  681 */         this.state = ((this.state | 0x1) ^ 0x1);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void loadImage()
/*      */   {
/*  691 */     URL localURL = getImageURL();
/*  692 */     Image localImage = null;
/*  693 */     if (localURL != null) {
/*  694 */       Dictionary localDictionary = (Dictionary)getDocument().getProperty("imageCache");
/*      */ 
/*  696 */       if (localDictionary != null) {
/*  697 */         localImage = (Image)localDictionary.get(localURL);
/*      */       }
/*      */       else {
/*  700 */         localImage = Toolkit.getDefaultToolkit().createImage(localURL);
/*  701 */         if ((localImage != null) && (getLoadsSynchronously()))
/*      */         {
/*  703 */           ImageIcon localImageIcon = new ImageIcon();
/*  704 */           localImageIcon.setImage(localImage);
/*      */         }
/*      */       }
/*      */     }
/*  708 */     this.image = localImage;
/*      */   }
/*      */ 
/*      */   private void updateImageSize()
/*      */   {
/*  716 */     int i = 0;
/*  717 */     int j = 0;
/*  718 */     int k = 0;
/*  719 */     Image localImage = getImage();
/*      */ 
/*  721 */     if (localImage != null) {
/*  722 */       Element localElement = getElement();
/*  723 */       AttributeSet localAttributeSet = localElement.getAttributes();
/*      */ 
/*  728 */       i = getIntAttr(HTML.Attribute.WIDTH, -1);
/*  729 */       if (i > 0) {
/*  730 */         k |= 4;
/*      */       }
/*  732 */       j = getIntAttr(HTML.Attribute.HEIGHT, -1);
/*  733 */       if (j > 0) {
/*  734 */         k |= 8;
/*      */       }
/*      */ 
/*  737 */       if (i <= 0) {
/*  738 */         i = localImage.getWidth(this.imageObserver);
/*  739 */         if (i <= 0) {
/*  740 */           i = 38;
/*      */         }
/*      */       }
/*      */ 
/*  744 */       if (j <= 0) {
/*  745 */         j = localImage.getHeight(this.imageObserver);
/*  746 */         if (j <= 0) {
/*  747 */           j = 38;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  752 */       if ((k & 0xC) != 0) {
/*  753 */         Toolkit.getDefaultToolkit().prepareImage(localImage, i, j, this.imageObserver);
/*      */       }
/*      */       else
/*      */       {
/*  758 */         Toolkit.getDefaultToolkit().prepareImage(localImage, -1, -1, this.imageObserver);
/*      */       }
/*      */ 
/*  762 */       int m = 0;
/*  763 */       synchronized (this)
/*      */       {
/*  767 */         if (this.image != null) {
/*  768 */           if (((k & 0x4) == 4) || (this.width == 0)) {
/*  769 */             this.width = i;
/*      */           }
/*  771 */           if (((k & 0x8) == 8) || (this.height == 0))
/*      */           {
/*  773 */             this.height = j;
/*      */           }
/*      */         }
/*      */         else {
/*  777 */           m = 1;
/*  778 */           if ((k & 0x4) == 4) {
/*  779 */             this.width = i;
/*      */           }
/*  781 */           if ((k & 0x8) == 8) {
/*  782 */             this.height = j;
/*      */           }
/*      */         }
/*  785 */         this.state |= k;
/*  786 */         this.state = ((this.state | 0x1) ^ 0x1);
/*      */       }
/*  788 */       if (m != 0)
/*      */       {
/*  790 */         updateAltTextView();
/*      */       }
/*      */     }
/*      */     else {
/*  794 */       this.width = (this.height = 38);
/*  795 */       updateAltTextView();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateAltTextView()
/*      */   {
/*  803 */     String str = getAltText();
/*      */ 
/*  805 */     if (str != null)
/*      */     {
/*  808 */       ImageLabelView localImageLabelView = new ImageLabelView(getElement(), str);
/*  809 */       synchronized (this) {
/*  810 */         this.altView = localImageLabelView;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private View getAltView()
/*      */   {
/*      */     View localView;
/*  821 */     synchronized (this) {
/*  822 */       localView = this.altView;
/*      */     }
/*  824 */     if ((localView != null) && (localView.getParent() == null)) {
/*  825 */       localView.setParent(getParent());
/*      */     }
/*  827 */     return localView;
/*      */   }
/*      */ 
/*      */   private void safePreferenceChanged()
/*      */   {
/*  835 */     if (SwingUtilities.isEventDispatchThread()) {
/*  836 */       Document localDocument = getDocument();
/*  837 */       if ((localDocument instanceof AbstractDocument)) {
/*  838 */         ((AbstractDocument)localDocument).readLock();
/*      */       }
/*  840 */       preferenceChanged(null, true, true);
/*  841 */       if ((localDocument instanceof AbstractDocument))
/*  842 */         ((AbstractDocument)localDocument).readUnlock();
/*      */     }
/*      */     else
/*      */     {
/*  846 */       SwingUtilities.invokeLater(new Runnable() {
/*      */         public void run() {
/*  848 */           ImageView.this.safePreferenceChanged();
/*      */         }
/*      */       });
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ImageHandler
/*      */     implements ImageObserver
/*      */   {
/*      */     private ImageHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     {
/*  866 */       if (((paramImage != ImageView.this.image) && (paramImage != ImageView.this.disabledImage)) || (ImageView.this.image == null) || (ImageView.this.getParent() == null))
/*      */       {
/*  869 */         return false;
/*      */       }
/*      */ 
/*  873 */       if ((paramInt1 & 0xC0) != 0) {
/*  874 */         ImageView.this.repaint(0L);
/*  875 */         synchronized (ImageView.this) {
/*  876 */           if (ImageView.this.image == paramImage)
/*      */           {
/*  879 */             ImageView.this.image = null;
/*  880 */             if ((ImageView.this.state & 0x4) != 4) {
/*  881 */               ImageView.this.width = 38;
/*      */             }
/*  883 */             if ((ImageView.this.state & 0x8) != 8)
/*  884 */               ImageView.this.height = 38;
/*      */           }
/*      */           else {
/*  887 */             ImageView.this.disabledImage = null;
/*      */           }
/*  889 */           if ((ImageView.this.state & 0x1) == 1)
/*      */           {
/*  892 */             return false;
/*      */           }
/*      */         }
/*  895 */         ImageView.this.updateAltTextView();
/*  896 */         ImageView.this.safePreferenceChanged();
/*  897 */         return false;
/*      */       }
/*      */ 
/*  900 */       if (ImageView.this.image == paramImage)
/*      */       {
/*  902 */         int i = 0;
/*  903 */         if (((paramInt1 & 0x2) != 0) && (!ImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.HEIGHT)))
/*      */         {
/*  905 */           i = (short)(i | 0x1);
/*      */         }
/*  907 */         if (((paramInt1 & 0x1) != 0) && (!ImageView.this.getElement().getAttributes().isDefined(HTML.Attribute.WIDTH)))
/*      */         {
/*  909 */           i = (short)(i | 0x2);
/*      */         }
/*      */ 
/*  912 */         synchronized (ImageView.this) {
/*  913 */           if (((i & 0x1) == 1) && ((ImageView.this.state & 0x4) == 0)) {
/*  914 */             ImageView.this.width = paramInt4;
/*      */           }
/*  916 */           if (((i & 0x2) == 2) && ((ImageView.this.state & 0x8) == 0)) {
/*  917 */             ImageView.this.height = paramInt5;
/*      */           }
/*  919 */           if ((ImageView.this.state & 0x1) == 1)
/*      */           {
/*  922 */             return true;
/*      */           }
/*      */         }
/*  925 */         if (i != 0)
/*      */         {
/*  927 */           ImageView.this.safePreferenceChanged();
/*  928 */           return true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  933 */       if ((paramInt1 & 0x30) != 0) {
/*  934 */         ImageView.this.repaint(0L);
/*      */       }
/*  936 */       else if (((paramInt1 & 0x8) != 0) && (ImageView.sIsInc)) {
/*  937 */         ImageView.this.repaint(ImageView.sIncRate);
/*      */       }
/*  939 */       return (paramInt1 & 0x20) == 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ImageLabelView extends InlineView
/*      */   {
/*      */     private Segment segment;
/*      */     private Color fg;
/*      */ 
/*      */     ImageLabelView(Element paramString, String arg3)
/*      */     {
/*  954 */       super();
/*      */       String str;
/*  955 */       reset(str);
/*      */     }
/*      */ 
/*      */     public void reset(String paramString) {
/*  959 */       this.segment = new Segment(paramString.toCharArray(), 0, paramString.length());
/*      */     }
/*      */ 
/*      */     public void paint(Graphics paramGraphics, Shape paramShape)
/*      */     {
/*  965 */       GlyphView.GlyphPainter localGlyphPainter = getGlyphPainter();
/*      */ 
/*  967 */       if (localGlyphPainter != null) {
/*  968 */         paramGraphics.setColor(getForeground());
/*  969 */         localGlyphPainter.paint(this, paramGraphics, paramShape, getStartOffset(), getEndOffset());
/*      */       }
/*      */     }
/*      */ 
/*      */     public Segment getText(int paramInt1, int paramInt2) {
/*  974 */       if ((paramInt1 < 0) || (paramInt2 > this.segment.array.length)) {
/*  975 */         throw new RuntimeException("ImageLabelView: Stale view");
/*      */       }
/*  977 */       this.segment.offset = paramInt1;
/*  978 */       this.segment.count = (paramInt2 - paramInt1);
/*  979 */       return this.segment;
/*      */     }
/*      */ 
/*      */     public int getStartOffset() {
/*  983 */       return 0;
/*      */     }
/*      */ 
/*      */     public int getEndOffset() {
/*  987 */       return this.segment.array.length;
/*      */     }
/*      */ 
/*      */     public View breakView(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
/*      */     {
/*  992 */       return this;
/*      */     }
/*      */ 
/*      */     public Color getForeground()
/*      */     {
/*      */       View localView;
/*  997 */       if ((this.fg == null) && ((localView = getParent()) != null)) {
/*  998 */         Document localDocument = getDocument();
/*  999 */         AttributeSet localAttributeSet = localView.getAttributes();
/*      */ 
/* 1001 */         if ((localAttributeSet != null) && ((localDocument instanceof StyledDocument))) {
/* 1002 */           this.fg = ((StyledDocument)localDocument).getForeground(localAttributeSet);
/*      */         }
/*      */       }
/* 1005 */       return this.fg;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.ImageView
 * JD-Core Version:    0.6.2
 */