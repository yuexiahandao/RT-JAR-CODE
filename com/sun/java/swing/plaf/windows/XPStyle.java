/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.DataBufferInt;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.security.AccessController;
/*     */ import java.util.HashMap;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.InsetsUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import sun.awt.image.SunWritableRaster;
/*     */ import sun.awt.windows.ThemeReader;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ import sun.swing.CachedPainter;
/*     */ 
/*     */ class XPStyle
/*     */ {
/*     */   private static XPStyle xp;
/*     */   private static SkinPainter skinPainter;
/*     */   private static Boolean themeActive;
/*     */   private HashMap<String, Border> borderMap;
/*     */   private HashMap<String, Color> colorMap;
/*     */   private boolean flatMenus;
/*     */ 
/*     */   static synchronized void invalidateStyle()
/*     */   {
/*  88 */     xp = null;
/*  89 */     themeActive = null;
/*  90 */     skinPainter.flush();
/*     */   }
/*     */ 
/*     */   static synchronized XPStyle getXP()
/*     */   {
/*  99 */     if (themeActive == null) {
/* 100 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 101 */       themeActive = (Boolean)localToolkit.getDesktopProperty("win.xpstyle.themeActive");
/*     */ 
/* 103 */       if (themeActive == null) {
/* 104 */         themeActive = Boolean.FALSE;
/*     */       }
/* 106 */       if (themeActive.booleanValue()) {
/* 107 */         GetPropertyAction localGetPropertyAction = new GetPropertyAction("swing.noxp");
/*     */ 
/* 109 */         if ((AccessController.doPrivileged(localGetPropertyAction) == null) && (ThemeReader.isThemed()) && (!(UIManager.getLookAndFeel() instanceof WindowsClassicLookAndFeel)))
/*     */         {
/* 114 */           xp = new XPStyle();
/*     */         }
/*     */       }
/*     */     }
/* 118 */     return ThemeReader.isXPStyleEnabled() ? xp : null;
/*     */   }
/*     */ 
/*     */   static boolean isVista() {
/* 122 */     XPStyle localXPStyle = getXP();
/* 123 */     return (localXPStyle != null) && (localXPStyle.isSkinDefined(null, TMSchema.Part.CP_DROPDOWNBUTTONRIGHT));
/*     */   }
/*     */ 
/*     */   String getString(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp)
/*     */   {
/* 138 */     return getTypeEnumName(paramComponent, paramPart, paramState, paramProp);
/*     */   }
/*     */ 
/*     */   TMSchema.TypeEnum getTypeEnum(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp) {
/* 142 */     int i = ThemeReader.getEnum(paramPart.getControlName(paramComponent), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState), paramProp.getValue());
/*     */ 
/* 145 */     return TMSchema.TypeEnum.getTypeEnum(paramProp, i);
/*     */   }
/*     */ 
/*     */   private static String getTypeEnumName(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp) {
/* 149 */     int i = ThemeReader.getEnum(paramPart.getControlName(paramComponent), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState), paramProp.getValue());
/*     */ 
/* 152 */     if (i == -1) {
/* 153 */       return null;
/*     */     }
/* 155 */     return TMSchema.TypeEnum.getTypeEnum(paramProp, i).getName();
/*     */   }
/*     */ 
/*     */   int getInt(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp, int paramInt)
/*     */   {
/* 168 */     return ThemeReader.getInt(paramPart.getControlName(paramComponent), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState), paramProp.getValue());
/*     */   }
/*     */ 
/*     */   Dimension getDimension(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp)
/*     */   {
/* 183 */     Dimension localDimension = ThemeReader.getPosition(paramPart.getControlName(paramComponent), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState), paramProp.getValue());
/*     */ 
/* 186 */     return localDimension != null ? localDimension : new Dimension();
/*     */   }
/*     */ 
/*     */   Point getPoint(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp)
/*     */   {
/* 200 */     Dimension localDimension = ThemeReader.getPosition(paramPart.getControlName(paramComponent), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState), paramProp.getValue());
/*     */ 
/* 203 */     return localDimension != null ? new Point(localDimension.width, localDimension.height) : new Point();
/*     */   }
/*     */ 
/*     */   Insets getMargin(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp)
/*     */   {
/* 217 */     Insets localInsets = ThemeReader.getThemeMargins(paramPart.getControlName(paramComponent), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState), paramProp.getValue());
/*     */ 
/* 220 */     return localInsets != null ? localInsets : new Insets(0, 0, 0, 0);
/*     */   }
/*     */ 
/*     */   synchronized Color getColor(Skin paramSkin, TMSchema.Prop paramProp, Color paramColor)
/*     */   {
/* 231 */     String str = paramSkin.toString() + "." + paramProp.name();
/* 232 */     TMSchema.Part localPart = paramSkin.part;
/* 233 */     Object localObject = (Color)this.colorMap.get(str);
/* 234 */     if (localObject == null) {
/* 235 */       localObject = ThemeReader.getColor(localPart.getControlName(null), localPart.getValue(), TMSchema.State.getValue(localPart, paramSkin.state), paramProp.getValue());
/*     */ 
/* 238 */       if (localObject != null) {
/* 239 */         localObject = new ColorUIResource((Color)localObject);
/* 240 */         this.colorMap.put(str, localObject);
/*     */       }
/*     */     }
/* 243 */     return localObject != null ? localObject : paramColor;
/*     */   }
/*     */ 
/*     */   Color getColor(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp, Color paramColor) {
/* 247 */     return getColor(new Skin(paramComponent, paramPart, paramState), paramProp, paramColor);
/*     */   }
/*     */ 
/*     */   synchronized Border getBorder(Component paramComponent, TMSchema.Part paramPart)
/*     */   {
/* 260 */     if (paramPart == TMSchema.Part.MENU)
/*     */     {
/* 262 */       if (this.flatMenus)
/*     */       {
/* 266 */         return new XPFillBorder(UIManager.getColor("InternalFrame.borderShadow"), 1);
/*     */       }
/*     */ 
/* 269 */       return null;
/*     */     }
/*     */ 
/* 272 */     Skin localSkin = new Skin(paramComponent, paramPart, null);
/* 273 */     Object localObject = (Border)this.borderMap.get(localSkin.string);
/* 274 */     if (localObject == null) {
/* 275 */       String str = getTypeEnumName(paramComponent, paramPart, null, TMSchema.Prop.BGTYPE);
/* 276 */       if ("borderfill".equalsIgnoreCase(str)) {
/* 277 */         int i = getInt(paramComponent, paramPart, null, TMSchema.Prop.BORDERSIZE, 1);
/* 278 */         Color localColor = getColor(localSkin, TMSchema.Prop.BORDERCOLOR, Color.black);
/* 279 */         localObject = new XPFillBorder(localColor, i);
/* 280 */         if (paramPart == TMSchema.Part.CP_COMBOBOX)
/* 281 */           localObject = new XPStatefulFillBorder(localColor, i, paramPart, TMSchema.Prop.BORDERCOLOR);
/*     */       }
/* 283 */       else if ("imagefile".equalsIgnoreCase(str)) {
/* 284 */         Insets localInsets = getMargin(paramComponent, paramPart, null, TMSchema.Prop.SIZINGMARGINS);
/* 285 */         if (localInsets != null) {
/* 286 */           if (getBoolean(paramComponent, paramPart, null, TMSchema.Prop.BORDERONLY))
/* 287 */             localObject = new XPImageBorder(paramComponent, paramPart);
/* 288 */           else if (paramPart == TMSchema.Part.CP_COMBOBOX) {
/* 289 */             localObject = new EmptyBorder(1, 1, 1, 1);
/*     */           }
/* 291 */           else if (paramPart == TMSchema.Part.TP_BUTTON)
/* 292 */             localObject = new XPEmptyBorder(new Insets(3, 3, 3, 3));
/*     */           else {
/* 294 */             localObject = new XPEmptyBorder(localInsets);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 299 */       if (localObject != null) {
/* 300 */         this.borderMap.put(localSkin.string, localObject);
/*     */       }
/*     */     }
/* 303 */     return localObject;
/*     */   }
/*     */ 
/*     */   boolean isSkinDefined(Component paramComponent, TMSchema.Part paramPart)
/*     */   {
/* 439 */     return (paramPart.getValue() == 0) || (ThemeReader.isThemePartDefined(paramPart.getControlName(paramComponent), paramPart.getValue(), 0));
/*     */   }
/*     */ 
/*     */   synchronized Skin getSkin(Component paramComponent, TMSchema.Part paramPart)
/*     */   {
/* 452 */     assert (isSkinDefined(paramComponent, paramPart)) : ("part " + paramPart + " is not defined");
/* 453 */     return new Skin(paramComponent, paramPart, null);
/*     */   }
/*     */ 
/*     */   long getThemeTransitionDuration(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState1, TMSchema.State paramState2, TMSchema.Prop paramProp)
/*     */   {
/* 459 */     return ThemeReader.getThemeTransitionDuration(paramPart.getControlName(paramComponent), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState1), TMSchema.State.getValue(paramPart, paramState2), paramProp != null ? paramProp.getValue() : 0);
/*     */   }
/*     */ 
/*     */   private XPStyle()
/*     */   {
/* 737 */     this.flatMenus = getSysBoolean(TMSchema.Prop.FLATMENUS);
/*     */ 
/* 739 */     this.colorMap = new HashMap();
/* 740 */     this.borderMap = new HashMap();
/*     */   }
/*     */ 
/*     */   private boolean getBoolean(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState, TMSchema.Prop paramProp)
/*     */   {
/* 746 */     return ThemeReader.getBoolean(paramPart.getControlName(paramComponent), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState), paramProp.getValue());
/*     */   }
/*     */ 
/*     */   static Dimension getPartSize(TMSchema.Part paramPart, TMSchema.State paramState)
/*     */   {
/* 754 */     return ThemeReader.getPartSize(paramPart.getControlName(null), paramPart.getValue(), TMSchema.State.getValue(paramPart, paramState));
/*     */   }
/*     */ 
/*     */   private static boolean getSysBoolean(TMSchema.Prop paramProp)
/*     */   {
/* 760 */     return ThemeReader.getSysBoolean("window", paramProp.getValue());
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  71 */     skinPainter = new SkinPainter();
/*     */ 
/*  73 */     themeActive = null;
/*     */ 
/*  81 */     invalidateStyle();
/*     */   }
/*     */ 
/*     */   static class GlyphButton extends JButton
/*     */   {
/*     */     private XPStyle.Skin skin;
/*     */ 
/*     */     public GlyphButton(Component paramComponent, TMSchema.Part paramPart)
/*     */     {
/* 689 */       XPStyle localXPStyle = XPStyle.getXP();
/* 690 */       this.skin = (localXPStyle != null ? localXPStyle.getSkin(paramComponent, paramPart) : null);
/* 691 */       setBorder(null);
/* 692 */       setContentAreaFilled(false);
/* 693 */       setMinimumSize(new Dimension(5, 5));
/* 694 */       setPreferredSize(new Dimension(16, 16));
/* 695 */       setMaximumSize(new Dimension(2147483647, 2147483647));
/*     */     }
/*     */ 
/*     */     public boolean isFocusTraversable() {
/* 699 */       return false;
/*     */     }
/*     */ 
/*     */     protected TMSchema.State getState() {
/* 703 */       TMSchema.State localState = TMSchema.State.NORMAL;
/* 704 */       if (!isEnabled())
/* 705 */         localState = TMSchema.State.DISABLED;
/* 706 */       else if (getModel().isPressed())
/* 707 */         localState = TMSchema.State.PRESSED;
/* 708 */       else if (getModel().isRollover()) {
/* 709 */         localState = TMSchema.State.HOT;
/*     */       }
/* 711 */       return localState;
/*     */     }
/*     */ 
/*     */     public void paintComponent(Graphics paramGraphics) {
/* 715 */       if ((XPStyle.getXP() == null) || (this.skin == null)) {
/* 716 */         return;
/*     */       }
/* 718 */       Dimension localDimension = getSize();
/* 719 */       this.skin.paintSkin(paramGraphics, 0, 0, localDimension.width, localDimension.height, getState());
/*     */     }
/*     */ 
/*     */     public void setPart(Component paramComponent, TMSchema.Part paramPart) {
/* 723 */       XPStyle localXPStyle = XPStyle.getXP();
/* 724 */       this.skin = (localXPStyle != null ? localXPStyle.getSkin(paramComponent, paramPart) : null);
/* 725 */       revalidate();
/* 726 */       repaint();
/*     */     }
/*     */ 
/*     */     protected void paintBorder(Graphics paramGraphics)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Skin
/*     */   {
/*     */     final Component component;
/*     */     final TMSchema.Part part;
/*     */     final TMSchema.State state;
/*     */     private final String string;
/* 477 */     private Dimension size = null;
/*     */ 
/*     */     Skin(Component paramComponent, TMSchema.Part paramPart) {
/* 480 */       this(paramComponent, paramPart, null);
/*     */     }
/*     */ 
/*     */     Skin(TMSchema.Part paramPart, TMSchema.State paramState) {
/* 484 */       this(null, paramPart, paramState);
/*     */     }
/*     */ 
/*     */     Skin(Component paramComponent, TMSchema.Part paramPart, TMSchema.State paramState) {
/* 488 */       this.component = paramComponent;
/* 489 */       this.part = paramPart;
/* 490 */       this.state = paramState;
/*     */ 
/* 492 */       String str = paramPart.getControlName(paramComponent) + "." + paramPart.name();
/* 493 */       if (paramState != null) {
/* 494 */         str = str + "(" + paramState.name() + ")";
/*     */       }
/* 496 */       this.string = str;
/*     */     }
/*     */ 
/*     */     Insets getContentMargin()
/*     */     {
/* 503 */       int i = 100;
/* 504 */       int j = 100;
/*     */ 
/* 506 */       Insets localInsets = ThemeReader.getThemeBackgroundContentMargins(this.part.getControlName(null), this.part.getValue(), 0, i, j);
/*     */ 
/* 509 */       return localInsets != null ? localInsets : new Insets(0, 0, 0, 0);
/*     */     }
/*     */ 
/*     */     private int getWidth(TMSchema.State paramState) {
/* 513 */       if (this.size == null) {
/* 514 */         this.size = XPStyle.getPartSize(this.part, paramState);
/*     */       }
/* 516 */       return this.size != null ? this.size.width : 0;
/*     */     }
/*     */ 
/*     */     int getWidth() {
/* 520 */       return getWidth(this.state != null ? this.state : TMSchema.State.NORMAL);
/*     */     }
/*     */ 
/*     */     private int getHeight(TMSchema.State paramState) {
/* 524 */       if (this.size == null) {
/* 525 */         this.size = XPStyle.getPartSize(this.part, paramState);
/*     */       }
/* 527 */       return this.size != null ? this.size.height : 0;
/*     */     }
/*     */ 
/*     */     int getHeight() {
/* 531 */       return getHeight(this.state != null ? this.state : TMSchema.State.NORMAL);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 535 */       return this.string;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 539 */       return ((paramObject instanceof Skin)) && (((Skin)paramObject).string.equals(this.string));
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 543 */       return this.string.hashCode();
/*     */     }
/*     */ 
/*     */     void paintSkin(Graphics paramGraphics, int paramInt1, int paramInt2, TMSchema.State paramState)
/*     */     {
/* 554 */       if (paramState == null) {
/* 555 */         paramState = this.state;
/*     */       }
/* 557 */       paintSkin(paramGraphics, paramInt1, paramInt2, getWidth(paramState), getHeight(paramState), paramState);
/*     */     }
/*     */ 
/*     */     void paintSkin(Graphics paramGraphics, Rectangle paramRectangle, TMSchema.State paramState)
/*     */     {
/* 568 */       paintSkin(paramGraphics, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, paramState);
/*     */     }
/*     */ 
/*     */     void paintSkin(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, TMSchema.State paramState)
/*     */     {
/* 584 */       if (XPStyle.getXP() == null) {
/* 585 */         return;
/*     */       }
/* 587 */       if ((ThemeReader.isGetThemeTransitionDurationDefined()) && ((this.component instanceof JComponent)) && (SwingUtilities.getAncestorOfClass(CellRendererPane.class, this.component) == null))
/*     */       {
/* 591 */         AnimationController.paintSkin((JComponent)this.component, this, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramState);
/*     */       }
/*     */       else
/* 594 */         paintSkinRaw(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, paramState);
/*     */     }
/*     */ 
/*     */     void paintSkinRaw(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, TMSchema.State paramState)
/*     */     {
/* 612 */       if (XPStyle.getXP() == null) {
/* 613 */         return;
/*     */       }
/* 615 */       XPStyle.skinPainter.paint(null, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, new Object[] { this, paramState });
/*     */     }
/*     */ 
/*     */     void paintSkin(Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4, TMSchema.State paramState, boolean paramBoolean)
/*     */     {
/* 633 */       if (XPStyle.getXP() == null) {
/* 634 */         return;
/*     */       }
/* 636 */       if ((paramBoolean) && ("borderfill".equals(XPStyle.getTypeEnumName(this.component, this.part, paramState, TMSchema.Prop.BGTYPE))))
/*     */       {
/* 638 */         return;
/*     */       }
/* 640 */       XPStyle.skinPainter.paint(null, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, new Object[] { this, paramState });
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SkinPainter extends CachedPainter {
/*     */     SkinPainter() {
/* 646 */       super();
/* 647 */       flush();
/*     */     }
/*     */ 
/*     */     public void flush() {
/* 651 */       super.flush();
/*     */     }
/*     */ 
/*     */     protected void paintToImage(Component paramComponent, Image paramImage, Graphics paramGraphics, int paramInt1, int paramInt2, Object[] paramArrayOfObject)
/*     */     {
/* 656 */       int i = 0;
/* 657 */       XPStyle.Skin localSkin = (XPStyle.Skin)paramArrayOfObject[0];
/* 658 */       TMSchema.Part localPart = localSkin.part;
/* 659 */       TMSchema.State localState = (TMSchema.State)paramArrayOfObject[1];
/* 660 */       if (localState == null) {
/* 661 */         localState = localSkin.state;
/*     */       }
/* 663 */       if (paramComponent == null) {
/* 664 */         paramComponent = localSkin.component;
/*     */       }
/* 666 */       BufferedImage localBufferedImage = (BufferedImage)paramImage;
/*     */ 
/* 668 */       WritableRaster localWritableRaster = localBufferedImage.getRaster();
/* 669 */       DataBufferInt localDataBufferInt = (DataBufferInt)localWritableRaster.getDataBuffer();
/*     */ 
/* 672 */       ThemeReader.paintBackground(SunWritableRaster.stealData(localDataBufferInt, 0), localPart.getControlName(paramComponent), localPart.getValue(), TMSchema.State.getValue(localPart, localState), 0, 0, paramInt1, paramInt2, paramInt1);
/*     */ 
/* 676 */       SunWritableRaster.markDirty(localDataBufferInt);
/*     */     }
/*     */ 
/*     */     protected Image createImage(Component paramComponent, int paramInt1, int paramInt2, GraphicsConfiguration paramGraphicsConfiguration, Object[] paramArrayOfObject)
/*     */     {
/* 681 */       return new BufferedImage(paramInt1, paramInt2, 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class XPEmptyBorder extends EmptyBorder
/*     */     implements UIResource
/*     */   {
/*     */     XPEmptyBorder(Insets arg2)
/*     */     {
/* 402 */       super(localObject.left + 2, localObject.bottom + 2, localObject.right + 2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 406 */       paramInsets = super.getBorderInsets(paramComponent, paramInsets);
/*     */ 
/* 408 */       Object localObject = null;
/* 409 */       if ((paramComponent instanceof AbstractButton)) {
/* 410 */         Insets localInsets = ((AbstractButton)paramComponent).getMargin();
/*     */ 
/* 413 */         if (((paramComponent.getParent() instanceof JToolBar)) && (!(paramComponent instanceof JRadioButton)) && (!(paramComponent instanceof JCheckBox)) && ((localInsets instanceof InsetsUIResource)))
/*     */         {
/* 417 */           paramInsets.top -= 2;
/* 418 */           paramInsets.left -= 2;
/* 419 */           paramInsets.bottom -= 2;
/* 420 */           paramInsets.right -= 2;
/*     */         } else {
/* 422 */           localObject = localInsets;
/*     */         }
/* 424 */       } else if ((paramComponent instanceof JToolBar)) {
/* 425 */         localObject = ((JToolBar)paramComponent).getMargin();
/* 426 */       } else if ((paramComponent instanceof JTextComponent)) {
/* 427 */         localObject = ((JTextComponent)paramComponent).getMargin();
/*     */       }
/* 429 */       if (localObject != null) {
/* 430 */         paramInsets.top = (((Insets)localObject).top + 2);
/* 431 */         paramInsets.left = (((Insets)localObject).left + 2);
/* 432 */         paramInsets.bottom = (((Insets)localObject).bottom + 2);
/* 433 */         paramInsets.right = (((Insets)localObject).right + 2);
/*     */       }
/* 435 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class XPFillBorder extends LineBorder
/*     */     implements UIResource
/*     */   {
/*     */     XPFillBorder(Color paramInt, int arg3)
/*     */     {
/* 308 */       super(i);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 312 */       Insets localInsets = null;
/*     */ 
/* 318 */       if ((paramComponent instanceof AbstractButton))
/* 319 */         localInsets = ((AbstractButton)paramComponent).getMargin();
/* 320 */       else if ((paramComponent instanceof JToolBar))
/* 321 */         localInsets = ((JToolBar)paramComponent).getMargin();
/* 322 */       else if ((paramComponent instanceof JTextComponent)) {
/* 323 */         localInsets = ((JTextComponent)paramComponent).getMargin();
/*     */       }
/* 325 */       paramInsets.top = ((localInsets != null ? localInsets.top : 0) + this.thickness);
/* 326 */       paramInsets.left = ((localInsets != null ? localInsets.left : 0) + this.thickness);
/* 327 */       paramInsets.bottom = ((localInsets != null ? localInsets.bottom : 0) + this.thickness);
/* 328 */       paramInsets.right = ((localInsets != null ? localInsets.right : 0) + this.thickness);
/*     */ 
/* 330 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class XPImageBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     XPStyle.Skin skin;
/*     */ 
/*     */     XPImageBorder(Component paramPart, TMSchema.Part arg3)
/*     */     {
/*     */       TMSchema.Part localPart;
/* 365 */       this.skin = XPStyle.this.getSkin(paramPart, localPart);
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 370 */       this.skin.paintSkin(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 374 */       Insets localInsets1 = null;
/* 375 */       Insets localInsets2 = this.skin.getContentMargin();
/* 376 */       if (localInsets2 == null) {
/* 377 */         localInsets2 = new Insets(0, 0, 0, 0);
/*     */       }
/*     */ 
/* 384 */       if ((paramComponent instanceof AbstractButton))
/* 385 */         localInsets1 = ((AbstractButton)paramComponent).getMargin();
/* 386 */       else if ((paramComponent instanceof JToolBar))
/* 387 */         localInsets1 = ((JToolBar)paramComponent).getMargin();
/* 388 */       else if ((paramComponent instanceof JTextComponent)) {
/* 389 */         localInsets1 = ((JTextComponent)paramComponent).getMargin();
/*     */       }
/* 391 */       paramInsets.top = ((localInsets1 != null ? localInsets1.top : 0) + localInsets2.top);
/* 392 */       paramInsets.left = ((localInsets1 != null ? localInsets1.left : 0) + localInsets2.left);
/* 393 */       paramInsets.bottom = ((localInsets1 != null ? localInsets1.bottom : 0) + localInsets2.bottom);
/* 394 */       paramInsets.right = ((localInsets1 != null ? localInsets1.right : 0) + localInsets2.right);
/*     */ 
/* 396 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class XPStatefulFillBorder extends XPStyle.XPFillBorder
/*     */   {
/*     */     private final TMSchema.Part part;
/*     */     private final TMSchema.Prop prop;
/*     */ 
/*     */     XPStatefulFillBorder(Color paramInt, int paramPart, TMSchema.Part paramProp, TMSchema.Prop arg5)
/*     */     {
/* 338 */       super(paramInt, paramPart);
/* 339 */       this.part = paramProp;
/*     */       Object localObject;
/* 340 */       this.prop = localObject;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 344 */       TMSchema.State localState = TMSchema.State.NORMAL;
/*     */ 
/* 347 */       if ((paramComponent instanceof JComboBox)) {
/* 348 */         JComboBox localJComboBox = (JComboBox)paramComponent;
/*     */ 
/* 351 */         if ((localJComboBox.getUI() instanceof WindowsComboBoxUI)) {
/* 352 */           WindowsComboBoxUI localWindowsComboBoxUI = (WindowsComboBoxUI)localJComboBox.getUI();
/* 353 */           localState = localWindowsComboBoxUI.getXPComboBoxState(localJComboBox);
/*     */         }
/*     */       }
/* 356 */       this.lineColor = XPStyle.this.getColor(paramComponent, this.part, localState, this.prop, Color.black);
/* 357 */       super.paintBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.XPStyle
 * JD-Core Version:    0.6.2
 */