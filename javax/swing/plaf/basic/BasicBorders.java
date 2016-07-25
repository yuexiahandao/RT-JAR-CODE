/*     */ package javax.swing.plaf.basic;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.UIDefaults;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.BevelBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;
/*     */ import javax.swing.plaf.BorderUIResource.LineBorderUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ public class BasicBorders
/*     */ {
/*     */   public static Border getButtonBorder()
/*     */   {
/*  49 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  50 */     BorderUIResource.CompoundBorderUIResource localCompoundBorderUIResource = new BorderUIResource.CompoundBorderUIResource(new ButtonBorder(localUIDefaults.getColor("Button.shadow"), localUIDefaults.getColor("Button.darkShadow"), localUIDefaults.getColor("Button.light"), localUIDefaults.getColor("Button.highlight")), new MarginBorder());
/*     */ 
/*  57 */     return localCompoundBorderUIResource;
/*     */   }
/*     */ 
/*     */   public static Border getRadioButtonBorder() {
/*  61 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  62 */     BorderUIResource.CompoundBorderUIResource localCompoundBorderUIResource = new BorderUIResource.CompoundBorderUIResource(new RadioButtonBorder(localUIDefaults.getColor("RadioButton.shadow"), localUIDefaults.getColor("RadioButton.darkShadow"), localUIDefaults.getColor("RadioButton.light"), localUIDefaults.getColor("RadioButton.highlight")), new MarginBorder());
/*     */ 
/*  69 */     return localCompoundBorderUIResource;
/*     */   }
/*     */ 
/*     */   public static Border getToggleButtonBorder() {
/*  73 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  74 */     BorderUIResource.CompoundBorderUIResource localCompoundBorderUIResource = new BorderUIResource.CompoundBorderUIResource(new ToggleButtonBorder(localUIDefaults.getColor("ToggleButton.shadow"), localUIDefaults.getColor("ToggleButton.darkShadow"), localUIDefaults.getColor("ToggleButton.light"), localUIDefaults.getColor("ToggleButton.highlight")), new MarginBorder());
/*     */ 
/*  81 */     return localCompoundBorderUIResource;
/*     */   }
/*     */ 
/*     */   public static Border getMenuBarBorder() {
/*  85 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  86 */     MenuBarBorder localMenuBarBorder = new MenuBarBorder(localUIDefaults.getColor("MenuBar.shadow"), localUIDefaults.getColor("MenuBar.highlight"));
/*     */ 
/*  90 */     return localMenuBarBorder;
/*     */   }
/*     */ 
/*     */   public static Border getSplitPaneBorder() {
/*  94 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/*  95 */     SplitPaneBorder localSplitPaneBorder = new SplitPaneBorder(localUIDefaults.getColor("SplitPane.highlight"), localUIDefaults.getColor("SplitPane.darkShadow"));
/*     */ 
/*  98 */     return localSplitPaneBorder;
/*     */   }
/*     */ 
/*     */   public static Border getSplitPaneDividerBorder()
/*     */   {
/* 106 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 107 */     SplitPaneDividerBorder localSplitPaneDividerBorder = new SplitPaneDividerBorder(localUIDefaults.getColor("SplitPane.highlight"), localUIDefaults.getColor("SplitPane.darkShadow"));
/*     */ 
/* 110 */     return localSplitPaneDividerBorder;
/*     */   }
/*     */ 
/*     */   public static Border getTextFieldBorder() {
/* 114 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 115 */     FieldBorder localFieldBorder = new FieldBorder(localUIDefaults.getColor("TextField.shadow"), localUIDefaults.getColor("TextField.darkShadow"), localUIDefaults.getColor("TextField.light"), localUIDefaults.getColor("TextField.highlight"));
/*     */ 
/* 120 */     return localFieldBorder;
/*     */   }
/*     */ 
/*     */   public static Border getProgressBarBorder() {
/* 124 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 125 */     BorderUIResource.LineBorderUIResource localLineBorderUIResource = new BorderUIResource.LineBorderUIResource(Color.green, 2);
/* 126 */     return localLineBorderUIResource;
/*     */   }
/*     */ 
/*     */   public static Border getInternalFrameBorder() {
/* 130 */     UIDefaults localUIDefaults = UIManager.getLookAndFeelDefaults();
/* 131 */     BorderUIResource.CompoundBorderUIResource localCompoundBorderUIResource = new BorderUIResource.CompoundBorderUIResource(new BevelBorder(0, localUIDefaults.getColor("InternalFrame.borderLight"), localUIDefaults.getColor("InternalFrame.borderHighlight"), localUIDefaults.getColor("InternalFrame.borderDarkShadow"), localUIDefaults.getColor("InternalFrame.borderShadow")), BorderFactory.createLineBorder(localUIDefaults.getColor("InternalFrame.borderColor"), 1));
/*     */ 
/* 140 */     return localCompoundBorderUIResource;
/*     */   }
/*     */ 
/*     */   public static class ButtonBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     protected Color shadow;
/*     */     protected Color darkShadow;
/*     */     protected Color highlight;
/*     */     protected Color lightHighlight;
/*     */ 
/*     */     public ButtonBorder(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 236 */       this.shadow = paramColor1;
/* 237 */       this.darkShadow = paramColor2;
/* 238 */       this.highlight = paramColor3;
/* 239 */       this.lightHighlight = paramColor4;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 244 */       boolean bool1 = false;
/* 245 */       boolean bool2 = false;
/*     */ 
/* 247 */       if ((paramComponent instanceof AbstractButton)) {
/* 248 */         AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 249 */         ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 251 */         bool1 = (localButtonModel.isPressed()) && (localButtonModel.isArmed());
/*     */ 
/* 253 */         if ((paramComponent instanceof JButton)) {
/* 254 */           bool2 = ((JButton)paramComponent).isDefaultButton();
/*     */         }
/*     */       }
/* 257 */       BasicGraphicsUtils.drawBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, bool1, bool2, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 264 */       paramInsets.set(2, 3, 3, 3);
/* 265 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class FieldBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     protected Color shadow;
/*     */     protected Color darkShadow;
/*     */     protected Color highlight;
/*     */     protected Color lightHighlight;
/*     */ 
/*     */     public FieldBorder(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 388 */       this.shadow = paramColor1;
/* 389 */       this.highlight = paramColor3;
/* 390 */       this.darkShadow = paramColor2;
/* 391 */       this.lightHighlight = paramColor4;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 396 */       BasicGraphicsUtils.drawEtchedRect(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 402 */       Insets localInsets = null;
/* 403 */       if ((paramComponent instanceof JTextComponent)) {
/* 404 */         localInsets = ((JTextComponent)paramComponent).getMargin();
/*     */       }
/* 406 */       paramInsets.top = (localInsets != null ? 2 + localInsets.top : 2);
/* 407 */       paramInsets.left = (localInsets != null ? 2 + localInsets.left : 2);
/* 408 */       paramInsets.bottom = (localInsets != null ? 2 + localInsets.bottom : 2);
/* 409 */       paramInsets.right = (localInsets != null ? 2 + localInsets.right : 2);
/*     */ 
/* 411 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MarginBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 355 */       Insets localInsets = null;
/*     */       Object localObject;
/* 361 */       if ((paramComponent instanceof AbstractButton)) {
/* 362 */         localObject = (AbstractButton)paramComponent;
/* 363 */         localInsets = ((AbstractButton)localObject).getMargin();
/* 364 */       } else if ((paramComponent instanceof JToolBar)) {
/* 365 */         localObject = (JToolBar)paramComponent;
/* 366 */         localInsets = ((JToolBar)localObject).getMargin();
/* 367 */       } else if ((paramComponent instanceof JTextComponent)) {
/* 368 */         localObject = (JTextComponent)paramComponent;
/* 369 */         localInsets = ((JTextComponent)localObject).getMargin();
/*     */       }
/* 371 */       paramInsets.top = (localInsets != null ? localInsets.top : 0);
/* 372 */       paramInsets.left = (localInsets != null ? localInsets.left : 0);
/* 373 */       paramInsets.bottom = (localInsets != null ? localInsets.bottom : 0);
/* 374 */       paramInsets.right = (localInsets != null ? localInsets.right : 0);
/*     */ 
/* 376 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MenuBarBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     private Color shadow;
/*     */     private Color highlight;
/*     */ 
/*     */     public MenuBarBorder(Color paramColor1, Color paramColor2)
/*     */     {
/* 332 */       this.shadow = paramColor1;
/* 333 */       this.highlight = paramColor2;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 337 */       Color localColor = paramGraphics.getColor();
/* 338 */       paramGraphics.translate(paramInt1, paramInt2);
/* 339 */       paramGraphics.setColor(this.shadow);
/* 340 */       paramGraphics.drawLine(0, paramInt4 - 2, paramInt3, paramInt4 - 2);
/* 341 */       paramGraphics.setColor(this.highlight);
/* 342 */       paramGraphics.drawLine(0, paramInt4 - 1, paramInt3, paramInt4 - 1);
/* 343 */       paramGraphics.translate(-paramInt1, -paramInt2);
/* 344 */       paramGraphics.setColor(localColor);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 348 */       paramInsets.set(0, 0, 2, 0);
/* 349 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class RadioButtonBorder extends BasicBorders.ButtonBorder
/*     */   {
/*     */     public RadioButtonBorder(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 295 */       super(paramColor2, paramColor3, paramColor4);
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 301 */       if ((paramComponent instanceof AbstractButton)) {
/* 302 */         AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 303 */         ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 305 */         if (((localButtonModel.isArmed()) && (localButtonModel.isPressed())) || (localButtonModel.isSelected())) {
/* 306 */           BasicGraphicsUtils.drawLoweredBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
/*     */         }
/*     */         else
/*     */         {
/* 310 */           BasicGraphicsUtils.drawBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, false, (localAbstractButton.isFocusPainted()) && (localAbstractButton.hasFocus()), this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 316 */         BasicGraphicsUtils.drawBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, false, false, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 322 */       paramInsets.set(2, 2, 2, 2);
/* 323 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class RolloverButtonBorder extends BasicBorders.ButtonBorder
/*     */   {
/*     */     public RolloverButtonBorder(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 151 */       super(paramColor2, paramColor3, paramColor4);
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 155 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 156 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 158 */       Color localColor1 = this.shadow;
/* 159 */       Container localContainer = localAbstractButton.getParent();
/* 160 */       if ((localContainer != null) && (localContainer.getBackground().equals(this.shadow))) {
/* 161 */         localColor1 = this.darkShadow;
/*     */       }
/*     */ 
/* 164 */       if (((localButtonModel.isRollover()) && ((!localButtonModel.isPressed()) || (localButtonModel.isArmed()))) || (localButtonModel.isSelected()))
/*     */       {
/* 167 */         Color localColor2 = paramGraphics.getColor();
/* 168 */         paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 170 */         if (((localButtonModel.isPressed()) && (localButtonModel.isArmed())) || (localButtonModel.isSelected()))
/*     */         {
/* 172 */           paramGraphics.setColor(localColor1);
/* 173 */           paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/* 174 */           paramGraphics.setColor(this.lightHighlight);
/* 175 */           paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 1, paramInt4 - 1);
/* 176 */           paramGraphics.drawLine(0, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/*     */         }
/*     */         else {
/* 179 */           paramGraphics.setColor(this.lightHighlight);
/* 180 */           paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/* 181 */           paramGraphics.setColor(localColor1);
/* 182 */           paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 1, paramInt4 - 1);
/* 183 */           paramGraphics.drawLine(0, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/*     */         }
/* 185 */         paramGraphics.translate(-paramInt1, -paramInt2);
/* 186 */         paramGraphics.setColor(localColor2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class RolloverMarginBorder extends EmptyBorder
/*     */   {
/*     */     public RolloverMarginBorder()
/*     */     {
/* 202 */       super(3, 3, 3);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 206 */       Insets localInsets = null;
/*     */ 
/* 208 */       if ((paramComponent instanceof AbstractButton)) {
/* 209 */         localInsets = ((AbstractButton)paramComponent).getMargin();
/*     */       }
/* 211 */       if ((localInsets == null) || ((localInsets instanceof UIResource)))
/*     */       {
/* 213 */         paramInsets.left = this.left;
/* 214 */         paramInsets.top = this.top;
/* 215 */         paramInsets.right = this.right;
/* 216 */         paramInsets.bottom = this.bottom;
/*     */       }
/*     */       else {
/* 219 */         paramInsets.left = localInsets.left;
/* 220 */         paramInsets.top = localInsets.top;
/* 221 */         paramInsets.right = localInsets.right;
/* 222 */         paramInsets.bottom = localInsets.bottom;
/*     */       }
/* 224 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class SplitPaneBorder
/*     */     implements Border, UIResource
/*     */   {
/*     */     protected Color highlight;
/*     */     protected Color shadow;
/*     */ 
/*     */     public SplitPaneBorder(Color paramColor1, Color paramColor2)
/*     */     {
/* 508 */       this.highlight = paramColor1;
/* 509 */       this.shadow = paramColor2;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 514 */       if (!(paramComponent instanceof JSplitPane)) {
/* 515 */         return;
/*     */       }
/*     */ 
/* 536 */       JSplitPane localJSplitPane = (JSplitPane)paramComponent;
/*     */ 
/* 538 */       Component localComponent = localJSplitPane.getLeftComponent();
/*     */ 
/* 541 */       paramGraphics.setColor(paramComponent.getBackground());
/* 542 */       paramGraphics.drawRect(paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */       Rectangle localRectangle;
/*     */       int i;
/*     */       int j;
/* 543 */       if (localJSplitPane.getOrientation() == 1) {
/* 544 */         if (localComponent != null) {
/* 545 */           localRectangle = localComponent.getBounds();
/* 546 */           paramGraphics.setColor(this.shadow);
/* 547 */           paramGraphics.drawLine(0, 0, localRectangle.width + 1, 0);
/* 548 */           paramGraphics.drawLine(0, 1, 0, localRectangle.height + 1);
/*     */ 
/* 550 */           paramGraphics.setColor(this.highlight);
/* 551 */           paramGraphics.drawLine(0, localRectangle.height + 1, localRectangle.width + 1, localRectangle.height + 1);
/*     */         }
/*     */ 
/* 554 */         localComponent = localJSplitPane.getRightComponent();
/* 555 */         if (localComponent != null) {
/* 556 */           localRectangle = localComponent.getBounds();
/*     */ 
/* 558 */           i = localRectangle.x + localRectangle.width;
/* 559 */           j = localRectangle.y + localRectangle.height;
/*     */ 
/* 561 */           paramGraphics.setColor(this.shadow);
/* 562 */           paramGraphics.drawLine(localRectangle.x - 1, 0, i, 0);
/* 563 */           paramGraphics.setColor(this.highlight);
/* 564 */           paramGraphics.drawLine(localRectangle.x - 1, j, i, j);
/* 565 */           paramGraphics.drawLine(i, 0, i, j + 1);
/*     */         }
/*     */       } else {
/* 568 */         if (localComponent != null) {
/* 569 */           localRectangle = localComponent.getBounds();
/* 570 */           paramGraphics.setColor(this.shadow);
/* 571 */           paramGraphics.drawLine(0, 0, localRectangle.width + 1, 0);
/* 572 */           paramGraphics.drawLine(0, 1, 0, localRectangle.height);
/* 573 */           paramGraphics.setColor(this.highlight);
/* 574 */           paramGraphics.drawLine(1 + localRectangle.width, 0, 1 + localRectangle.width, localRectangle.height + 1);
/*     */ 
/* 576 */           paramGraphics.drawLine(0, localRectangle.height + 1, 0, localRectangle.height + 1);
/*     */         }
/* 578 */         localComponent = localJSplitPane.getRightComponent();
/* 579 */         if (localComponent != null) {
/* 580 */           localRectangle = localComponent.getBounds();
/*     */ 
/* 582 */           i = localRectangle.x + localRectangle.width;
/* 583 */           j = localRectangle.y + localRectangle.height;
/*     */ 
/* 585 */           paramGraphics.setColor(this.shadow);
/* 586 */           paramGraphics.drawLine(0, localRectangle.y - 1, 0, j);
/* 587 */           paramGraphics.drawLine(i, localRectangle.y - 1, i, localRectangle.y - 1);
/* 588 */           paramGraphics.setColor(this.highlight);
/* 589 */           paramGraphics.drawLine(0, j, localRectangle.width + 1, j);
/* 590 */           paramGraphics.drawLine(i, localRectangle.y, i, j);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 595 */     public Insets getBorderInsets(Component paramComponent) { return new Insets(1, 1, 1, 1); } 
/*     */     public boolean isBorderOpaque() {
/* 597 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SplitPaneDividerBorder
/*     */     implements Border, UIResource
/*     */   {
/*     */     Color highlight;
/*     */     Color shadow;
/*     */ 
/*     */     SplitPaneDividerBorder(Color paramColor1, Color paramColor2)
/*     */     {
/* 426 */       this.highlight = paramColor1;
/* 427 */       this.shadow = paramColor2;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 432 */       if (!(paramComponent instanceof BasicSplitPaneDivider)) {
/* 433 */         return;
/*     */       }
/*     */ 
/* 437 */       JSplitPane localJSplitPane = ((BasicSplitPaneDivider)paramComponent).getBasicSplitPaneUI().getSplitPane();
/*     */ 
/* 439 */       Dimension localDimension = paramComponent.getSize();
/*     */ 
/* 441 */       Component localComponent = localJSplitPane.getLeftComponent();
/*     */ 
/* 444 */       paramGraphics.setColor(paramComponent.getBackground());
/* 445 */       paramGraphics.drawRect(paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/* 446 */       if (localJSplitPane.getOrientation() == 1) {
/* 447 */         if (localComponent != null) {
/* 448 */           paramGraphics.setColor(this.highlight);
/* 449 */           paramGraphics.drawLine(0, 0, 0, localDimension.height);
/*     */         }
/* 451 */         localComponent = localJSplitPane.getRightComponent();
/* 452 */         if (localComponent != null) {
/* 453 */           paramGraphics.setColor(this.shadow);
/* 454 */           paramGraphics.drawLine(localDimension.width - 1, 0, localDimension.width - 1, localDimension.height);
/*     */         }
/*     */       } else {
/* 457 */         if (localComponent != null) {
/* 458 */           paramGraphics.setColor(this.highlight);
/* 459 */           paramGraphics.drawLine(0, 0, localDimension.width, 0);
/*     */         }
/* 461 */         localComponent = localJSplitPane.getRightComponent();
/* 462 */         if (localComponent != null) {
/* 463 */           paramGraphics.setColor(this.shadow);
/* 464 */           paramGraphics.drawLine(0, localDimension.height - 1, localDimension.width, localDimension.height - 1);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent) {
/* 470 */       Insets localInsets = new Insets(0, 0, 0, 0);
/* 471 */       if ((paramComponent instanceof BasicSplitPaneDivider)) {
/* 472 */         BasicSplitPaneUI localBasicSplitPaneUI = ((BasicSplitPaneDivider)paramComponent).getBasicSplitPaneUI();
/*     */ 
/* 475 */         if (localBasicSplitPaneUI != null) {
/* 476 */           JSplitPane localJSplitPane = localBasicSplitPaneUI.getSplitPane();
/*     */ 
/* 478 */           if (localJSplitPane != null) {
/* 479 */             if (localJSplitPane.getOrientation() == 1)
/*     */             {
/* 481 */               localInsets.top = (localInsets.bottom = 0);
/* 482 */               localInsets.left = (localInsets.right = 1);
/* 483 */               return localInsets;
/*     */             }
/*     */ 
/* 486 */             localInsets.top = (localInsets.bottom = 1);
/* 487 */             localInsets.left = (localInsets.right = 0);
/* 488 */             return localInsets;
/*     */           }
/*     */         }
/*     */       }
/* 492 */       localInsets.top = (localInsets.bottom = localInsets.left = localInsets.right = 1);
/* 493 */       return localInsets;
/*     */     }
/* 495 */     public boolean isBorderOpaque() { return true; }
/*     */ 
/*     */   }
/*     */ 
/*     */   public static class ToggleButtonBorder extends BasicBorders.ButtonBorder
/*     */   {
/*     */     public ToggleButtonBorder(Color paramColor1, Color paramColor2, Color paramColor3, Color paramColor4)
/*     */     {
/* 274 */       super(paramColor2, paramColor3, paramColor4);
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 279 */       BasicGraphicsUtils.drawBezel(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, false, false, this.shadow, this.darkShadow, this.highlight, this.lightHighlight);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 286 */       paramInsets.set(2, 2, 2, 2);
/* 287 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicBorders
 * JD-Core Version:    0.6.2
 */