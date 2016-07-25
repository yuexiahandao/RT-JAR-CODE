/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Window;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.SwingConstants;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.border.MatteBorder;
/*     */ import javax.swing.plaf.BorderUIResource.CompoundBorderUIResource;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicBorders.MarginBorder;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import sun.swing.StringUIClientPropertyKey;
/*     */ 
/*     */ public class MetalBorders
/*     */ {
/*  56 */   static Object NO_BUTTON_ROLLOVER = new StringUIClientPropertyKey("NoButtonRollover");
/*     */   private static Border buttonBorder;
/*     */   private static Border textBorder;
/*     */   private static Border textFieldBorder;
/*     */   private static Border toggleButtonBorder;
/*     */ 
/*     */   public static Border getButtonBorder()
/*     */   {
/* 773 */     if (buttonBorder == null) {
/* 774 */       buttonBorder = new BorderUIResource.CompoundBorderUIResource(new ButtonBorder(), new BasicBorders.MarginBorder());
/*     */     }
/*     */ 
/* 778 */     return buttonBorder;
/*     */   }
/*     */ 
/*     */   public static Border getTextBorder()
/*     */   {
/* 788 */     if (textBorder == null) {
/* 789 */       textBorder = new BorderUIResource.CompoundBorderUIResource(new Flush3DBorder(), new BasicBorders.MarginBorder());
/*     */     }
/*     */ 
/* 793 */     return textBorder;
/*     */   }
/*     */ 
/*     */   public static Border getTextFieldBorder()
/*     */   {
/* 803 */     if (textFieldBorder == null) {
/* 804 */       textFieldBorder = new BorderUIResource.CompoundBorderUIResource(new TextFieldBorder(), new BasicBorders.MarginBorder());
/*     */     }
/*     */ 
/* 808 */     return textFieldBorder;
/*     */   }
/*     */ 
/*     */   public static Border getToggleButtonBorder()
/*     */   {
/* 884 */     if (toggleButtonBorder == null) {
/* 885 */       toggleButtonBorder = new BorderUIResource.CompoundBorderUIResource(new ToggleButtonBorder(), new BasicBorders.MarginBorder());
/*     */     }
/*     */ 
/* 889 */     return toggleButtonBorder;
/*     */   }
/*     */ 
/*     */   public static Border getDesktopIconBorder()
/*     */   {
/* 954 */     return new BorderUIResource.CompoundBorderUIResource(new LineBorder(MetalLookAndFeel.getControlDarkShadow(), 1), new MatteBorder(2, 2, 1, 2, MetalLookAndFeel.getControl()));
/*     */   }
/*     */ 
/*     */   static Border getToolBarRolloverBorder()
/*     */   {
/* 960 */     if (MetalLookAndFeel.usingOcean()) {
/* 961 */       return new CompoundBorder(new ButtonBorder(), new RolloverMarginBorder());
/*     */     }
/*     */ 
/* 965 */     return new CompoundBorder(new RolloverButtonBorder(), new RolloverMarginBorder());
/*     */   }
/*     */ 
/*     */   static Border getToolBarNonrolloverBorder()
/*     */   {
/* 970 */     if (MetalLookAndFeel.usingOcean()) {
/* 971 */       new CompoundBorder(new ButtonBorder(), new RolloverMarginBorder());
/*     */     }
/*     */ 
/* 975 */     return new CompoundBorder(new ButtonBorder(), new RolloverMarginBorder());
/*     */   }
/*     */ 
/*     */   public static class ButtonBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*  78 */     protected static Insets borderInsets = new Insets(3, 3, 3, 3);
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  81 */       if (!(paramComponent instanceof AbstractButton)) {
/*  82 */         return;
/*     */       }
/*  84 */       if (MetalLookAndFeel.usingOcean()) {
/*  85 */         paintOceanBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*  86 */         return;
/*     */       }
/*  88 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/*  89 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/*  91 */       if (localButtonModel.isEnabled()) {
/*  92 */         int i = (localButtonModel.isPressed()) && (localButtonModel.isArmed()) ? 1 : 0;
/*  93 */         int j = ((localAbstractButton instanceof JButton)) && (((JButton)localAbstractButton).isDefaultButton()) ? 1 : 0;
/*     */ 
/*  95 */         if ((i != 0) && (j != 0))
/*  96 */           MetalUtils.drawDefaultButtonPressedBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*  97 */         else if (i != 0)
/*  98 */           MetalUtils.drawPressed3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*  99 */         else if (j != 0)
/* 100 */           MetalUtils.drawDefaultButtonBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, false);
/*     */         else
/* 102 */           MetalUtils.drawButtonBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4, false);
/*     */       }
/*     */       else {
/* 105 */         MetalUtils.drawDisabledBorder(paramGraphics, paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void paintOceanBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 111 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 112 */       ButtonModel localButtonModel = ((AbstractButton)paramComponent).getModel();
/*     */ 
/* 114 */       paramGraphics.translate(paramInt1, paramInt2);
/* 115 */       if (MetalUtils.isToolBarButton(localAbstractButton)) {
/* 116 */         if (localButtonModel.isEnabled()) {
/* 117 */           if (localButtonModel.isPressed()) {
/* 118 */             paramGraphics.setColor(MetalLookAndFeel.getWhite());
/* 119 */             paramGraphics.fillRect(1, paramInt4 - 1, paramInt3 - 1, 1);
/* 120 */             paramGraphics.fillRect(paramInt3 - 1, 1, 1, paramInt4 - 1);
/* 121 */             paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 122 */             paramGraphics.drawRect(0, 0, paramInt3 - 2, paramInt4 - 2);
/* 123 */             paramGraphics.fillRect(1, 1, paramInt3 - 3, 1);
/*     */           }
/* 125 */           else if ((localButtonModel.isSelected()) || (localButtonModel.isRollover())) {
/* 126 */             paramGraphics.setColor(MetalLookAndFeel.getWhite());
/* 127 */             paramGraphics.fillRect(1, paramInt4 - 1, paramInt3 - 1, 1);
/* 128 */             paramGraphics.fillRect(paramInt3 - 1, 1, 1, paramInt4 - 1);
/* 129 */             paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 130 */             paramGraphics.drawRect(0, 0, paramInt3 - 2, paramInt4 - 2);
/*     */           }
/*     */           else {
/* 133 */             paramGraphics.setColor(MetalLookAndFeel.getWhite());
/* 134 */             paramGraphics.drawRect(1, 1, paramInt3 - 2, paramInt4 - 2);
/* 135 */             paramGraphics.setColor(UIManager.getColor("Button.toolBarBorderBackground"));
/*     */ 
/* 137 */             paramGraphics.drawRect(0, 0, paramInt3 - 2, paramInt4 - 2);
/*     */           }
/*     */         }
/*     */         else {
/* 141 */           paramGraphics.setColor(UIManager.getColor("Button.disabledToolBarBorderBackground"));
/*     */ 
/* 143 */           paramGraphics.drawRect(0, 0, paramInt3 - 2, paramInt4 - 2);
/*     */         }
/*     */       }
/* 146 */       else if (localButtonModel.isEnabled()) {
/* 147 */         boolean bool1 = localButtonModel.isPressed();
/* 148 */         boolean bool2 = localButtonModel.isArmed();
/*     */ 
/* 150 */         if (((paramComponent instanceof JButton)) && (((JButton)paramComponent).isDefaultButton())) {
/* 151 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 152 */           paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/* 153 */           paramGraphics.drawRect(1, 1, paramInt3 - 3, paramInt4 - 3);
/*     */         }
/* 155 */         else if (bool1) {
/* 156 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 157 */           paramGraphics.fillRect(0, 0, paramInt3, 2);
/* 158 */           paramGraphics.fillRect(0, 2, 2, paramInt4 - 2);
/* 159 */           paramGraphics.fillRect(paramInt3 - 1, 1, 1, paramInt4 - 1);
/* 160 */           paramGraphics.fillRect(1, paramInt4 - 1, paramInt3 - 2, 1);
/*     */         }
/* 162 */         else if ((localButtonModel.isRollover()) && (localAbstractButton.getClientProperty(MetalBorders.NO_BUTTON_ROLLOVER) == null))
/*     */         {
/* 164 */           paramGraphics.setColor(MetalLookAndFeel.getPrimaryControl());
/* 165 */           paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/* 166 */           paramGraphics.drawRect(2, 2, paramInt3 - 5, paramInt4 - 5);
/* 167 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 168 */           paramGraphics.drawRect(1, 1, paramInt3 - 3, paramInt4 - 3);
/*     */         }
/*     */         else {
/* 171 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 172 */           paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/*     */         }
/*     */       }
/*     */       else {
/* 176 */         paramGraphics.setColor(MetalLookAndFeel.getInactiveControlTextColor());
/* 177 */         paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/* 178 */         if (((paramComponent instanceof JButton)) && (((JButton)paramComponent).isDefaultButton()))
/* 179 */           paramGraphics.drawRect(1, 1, paramInt3 - 3, paramInt4 - 3);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 185 */       paramInsets.set(3, 3, 3, 3);
/* 186 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class DialogBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     private static final int corner = 14;
/*     */ 
/*     */     protected Color getActiveBackground()
/*     */     {
/* 319 */       return MetalLookAndFeel.getPrimaryControlDarkShadow();
/*     */     }
/*     */ 
/*     */     protected Color getActiveHighlight()
/*     */     {
/* 324 */       return MetalLookAndFeel.getPrimaryControlShadow();
/*     */     }
/*     */ 
/*     */     protected Color getActiveShadow()
/*     */     {
/* 329 */       return MetalLookAndFeel.getPrimaryControlInfo();
/*     */     }
/*     */ 
/*     */     protected Color getInactiveBackground()
/*     */     {
/* 334 */       return MetalLookAndFeel.getControlDarkShadow();
/*     */     }
/*     */ 
/*     */     protected Color getInactiveHighlight()
/*     */     {
/* 339 */       return MetalLookAndFeel.getControlShadow();
/*     */     }
/*     */ 
/*     */     protected Color getInactiveShadow()
/*     */     {
/* 344 */       return MetalLookAndFeel.getControlInfo();
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 353 */       Window localWindow = SwingUtilities.getWindowAncestor(paramComponent);
/*     */       Color localColor1;
/*     */       Color localColor2;
/*     */       Color localColor3;
/* 354 */       if ((localWindow != null) && (localWindow.isActive())) {
/* 355 */         localColor1 = getActiveBackground();
/* 356 */         localColor2 = getActiveHighlight();
/* 357 */         localColor3 = getActiveShadow();
/*     */       } else {
/* 359 */         localColor1 = getInactiveBackground();
/* 360 */         localColor2 = getInactiveHighlight();
/* 361 */         localColor3 = getInactiveShadow();
/*     */       }
/*     */ 
/* 364 */       paramGraphics.setColor(localColor1);
/*     */ 
/* 366 */       paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 0, paramInt1 + paramInt3 - 2, paramInt2 + 0);
/* 367 */       paramGraphics.drawLine(paramInt1 + 0, paramInt2 + 1, paramInt1 + 0, paramInt2 + paramInt4 - 2);
/* 368 */       paramGraphics.drawLine(paramInt1 + paramInt3 - 1, paramInt2 + 1, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 2);
/* 369 */       paramGraphics.drawLine(paramInt1 + 1, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - 2, paramInt2 + paramInt4 - 1);
/*     */ 
/* 372 */       for (int i = 1; i < 5; i++) {
/* 373 */         paramGraphics.drawRect(paramInt1 + i, paramInt2 + i, paramInt3 - i * 2 - 1, paramInt4 - i * 2 - 1);
/*     */       }
/*     */ 
/* 377 */       if (((localWindow instanceof Dialog)) && (((Dialog)localWindow).isResizable())) {
/* 378 */         paramGraphics.setColor(localColor2);
/*     */ 
/* 380 */         paramGraphics.drawLine(15, 3, paramInt3 - 14, 3);
/* 381 */         paramGraphics.drawLine(3, 15, 3, paramInt4 - 14);
/* 382 */         paramGraphics.drawLine(paramInt3 - 2, 15, paramInt3 - 2, paramInt4 - 14);
/* 383 */         paramGraphics.drawLine(15, paramInt4 - 2, paramInt3 - 14, paramInt4 - 2);
/*     */ 
/* 385 */         paramGraphics.setColor(localColor3);
/*     */ 
/* 387 */         paramGraphics.drawLine(14, 2, paramInt3 - 14 - 1, 2);
/* 388 */         paramGraphics.drawLine(2, 14, 2, paramInt4 - 14 - 1);
/* 389 */         paramGraphics.drawLine(paramInt3 - 3, 14, paramInt3 - 3, paramInt4 - 14 - 1);
/* 390 */         paramGraphics.drawLine(14, paramInt4 - 3, paramInt3 - 14 - 1, paramInt4 - 3);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 397 */       paramInsets.set(5, 5, 5, 5);
/* 398 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class ErrorDialogBorder extends MetalBorders.DialogBorder
/*     */     implements UIResource
/*     */   {
/*     */     protected Color getActiveBackground()
/*     */     {
/* 409 */       return UIManager.getColor("OptionPane.errorDialog.border.background");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Flush3DBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/*  63 */       if (paramComponent.isEnabled())
/*  64 */         MetalUtils.drawFlush3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       else
/*  66 */         MetalUtils.drawDisabledBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/*  71 */       paramInsets.set(2, 2, 2, 2);
/*  72 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class FrameBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     private static final int corner = 14;
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 261 */       Window localWindow = SwingUtilities.getWindowAncestor(paramComponent);
/*     */       ColorUIResource localColorUIResource1;
/*     */       ColorUIResource localColorUIResource2;
/*     */       ColorUIResource localColorUIResource3;
/* 262 */       if ((localWindow != null) && (localWindow.isActive())) {
/* 263 */         localColorUIResource1 = MetalLookAndFeel.getPrimaryControlDarkShadow();
/* 264 */         localColorUIResource2 = MetalLookAndFeel.getPrimaryControlShadow();
/* 265 */         localColorUIResource3 = MetalLookAndFeel.getPrimaryControlInfo();
/*     */       } else {
/* 267 */         localColorUIResource1 = MetalLookAndFeel.getControlDarkShadow();
/* 268 */         localColorUIResource2 = MetalLookAndFeel.getControlShadow();
/* 269 */         localColorUIResource3 = MetalLookAndFeel.getControlInfo();
/*     */       }
/*     */ 
/* 272 */       paramGraphics.setColor(localColorUIResource1);
/*     */ 
/* 274 */       paramGraphics.drawLine(paramInt1 + 1, paramInt2 + 0, paramInt1 + paramInt3 - 2, paramInt2 + 0);
/* 275 */       paramGraphics.drawLine(paramInt1 + 0, paramInt2 + 1, paramInt1 + 0, paramInt2 + paramInt4 - 2);
/* 276 */       paramGraphics.drawLine(paramInt1 + paramInt3 - 1, paramInt2 + 1, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 2);
/* 277 */       paramGraphics.drawLine(paramInt1 + 1, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - 2, paramInt2 + paramInt4 - 1);
/*     */ 
/* 280 */       for (int i = 1; i < 5; i++) {
/* 281 */         paramGraphics.drawRect(paramInt1 + i, paramInt2 + i, paramInt3 - i * 2 - 1, paramInt4 - i * 2 - 1);
/*     */       }
/*     */ 
/* 284 */       if (((localWindow instanceof Frame)) && (((Frame)localWindow).isResizable())) {
/* 285 */         paramGraphics.setColor(localColorUIResource2);
/*     */ 
/* 287 */         paramGraphics.drawLine(15, 3, paramInt3 - 14, 3);
/* 288 */         paramGraphics.drawLine(3, 15, 3, paramInt4 - 14);
/* 289 */         paramGraphics.drawLine(paramInt3 - 2, 15, paramInt3 - 2, paramInt4 - 14);
/* 290 */         paramGraphics.drawLine(15, paramInt4 - 2, paramInt3 - 14, paramInt4 - 2);
/*     */ 
/* 292 */         paramGraphics.setColor(localColorUIResource3);
/*     */ 
/* 294 */         paramGraphics.drawLine(14, 2, paramInt3 - 14 - 1, 2);
/* 295 */         paramGraphics.drawLine(2, 14, 2, paramInt4 - 14 - 1);
/* 296 */         paramGraphics.drawLine(paramInt3 - 3, 14, paramInt3 - 3, paramInt4 - 14 - 1);
/* 297 */         paramGraphics.drawLine(14, paramInt4 - 3, paramInt3 - 14 - 1, paramInt4 - 3);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 304 */       paramInsets.set(5, 5, 5, 5);
/* 305 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class InternalFrameBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     private static final int corner = 14;
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/*     */       ColorUIResource localColorUIResource1;
/*     */       ColorUIResource localColorUIResource2;
/*     */       ColorUIResource localColorUIResource3;
/* 200 */       if (((paramComponent instanceof JInternalFrame)) && (((JInternalFrame)paramComponent).isSelected())) {
/* 201 */         localColorUIResource1 = MetalLookAndFeel.getPrimaryControlDarkShadow();
/* 202 */         localColorUIResource2 = MetalLookAndFeel.getPrimaryControlShadow();
/* 203 */         localColorUIResource3 = MetalLookAndFeel.getPrimaryControlInfo();
/*     */       } else {
/* 205 */         localColorUIResource1 = MetalLookAndFeel.getControlDarkShadow();
/* 206 */         localColorUIResource2 = MetalLookAndFeel.getControlShadow();
/* 207 */         localColorUIResource3 = MetalLookAndFeel.getControlInfo();
/*     */       }
/*     */ 
/* 210 */       paramGraphics.setColor(localColorUIResource1);
/*     */ 
/* 212 */       paramGraphics.drawLine(1, 0, paramInt3 - 2, 0);
/* 213 */       paramGraphics.drawLine(0, 1, 0, paramInt4 - 2);
/* 214 */       paramGraphics.drawLine(paramInt3 - 1, 1, paramInt3 - 1, paramInt4 - 2);
/* 215 */       paramGraphics.drawLine(1, paramInt4 - 1, paramInt3 - 2, paramInt4 - 1);
/*     */ 
/* 218 */       for (int i = 1; i < 5; i++) {
/* 219 */         paramGraphics.drawRect(paramInt1 + i, paramInt2 + i, paramInt3 - i * 2 - 1, paramInt4 - i * 2 - 1);
/*     */       }
/*     */ 
/* 222 */       if (((paramComponent instanceof JInternalFrame)) && (((JInternalFrame)paramComponent).isResizable()))
/*     */       {
/* 224 */         paramGraphics.setColor(localColorUIResource2);
/*     */ 
/* 226 */         paramGraphics.drawLine(15, 3, paramInt3 - 14, 3);
/* 227 */         paramGraphics.drawLine(3, 15, 3, paramInt4 - 14);
/* 228 */         paramGraphics.drawLine(paramInt3 - 2, 15, paramInt3 - 2, paramInt4 - 14);
/* 229 */         paramGraphics.drawLine(15, paramInt4 - 2, paramInt3 - 14, paramInt4 - 2);
/*     */ 
/* 231 */         paramGraphics.setColor(localColorUIResource3);
/*     */ 
/* 233 */         paramGraphics.drawLine(14, 2, paramInt3 - 14 - 1, 2);
/* 234 */         paramGraphics.drawLine(2, 14, 2, paramInt4 - 14 - 1);
/* 235 */         paramGraphics.drawLine(paramInt3 - 3, 14, paramInt3 - 3, paramInt4 - 14 - 1);
/* 236 */         paramGraphics.drawLine(14, paramInt4 - 3, paramInt3 - 14 - 1, paramInt4 - 3);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 242 */       paramInsets.set(5, 5, 5, 5);
/* 243 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MenuBarBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/* 528 */     protected static Insets borderInsets = new Insets(1, 0, 1, 0);
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 531 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 533 */       if (MetalLookAndFeel.usingOcean())
/*     */       {
/* 536 */         if (((paramComponent instanceof JMenuBar)) && (!MetalToolBarUI.doesMenuBarBorderToolBar((JMenuBar)paramComponent))) {
/* 537 */           paramGraphics.setColor(MetalLookAndFeel.getControl());
/* 538 */           paramGraphics.drawLine(0, paramInt4 - 2, paramInt3, paramInt4 - 2);
/* 539 */           paramGraphics.setColor(UIManager.getColor("MenuBar.borderColor"));
/* 540 */           paramGraphics.drawLine(0, paramInt4 - 1, paramInt3, paramInt4 - 1);
/*     */         }
/*     */       }
/*     */       else {
/* 544 */         paramGraphics.setColor(MetalLookAndFeel.getControlShadow());
/* 545 */         paramGraphics.drawLine(0, paramInt4 - 1, paramInt3, paramInt4 - 1);
/*     */       }
/*     */ 
/* 548 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 553 */       if (MetalLookAndFeel.usingOcean()) {
/* 554 */         paramInsets.set(0, 0, 2, 0);
/*     */       }
/*     */       else {
/* 557 */         paramInsets.set(1, 0, 1, 0);
/*     */       }
/* 559 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class MenuItemBorder extends AbstractBorder implements UIResource {
/* 564 */     protected static Insets borderInsets = new Insets(2, 2, 2, 2);
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 567 */       if (!(paramComponent instanceof JMenuItem)) {
/* 568 */         return;
/*     */       }
/* 570 */       JMenuItem localJMenuItem = (JMenuItem)paramComponent;
/* 571 */       ButtonModel localButtonModel = localJMenuItem.getModel();
/*     */ 
/* 573 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 575 */       if ((paramComponent.getParent() instanceof JMenuBar)) {
/* 576 */         if ((localButtonModel.isArmed()) || (localButtonModel.isSelected())) {
/* 577 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 578 */           paramGraphics.drawLine(0, 0, paramInt3 - 2, 0);
/* 579 */           paramGraphics.drawLine(0, 0, 0, paramInt4 - 1);
/* 580 */           paramGraphics.drawLine(paramInt3 - 2, 2, paramInt3 - 2, paramInt4 - 1);
/*     */ 
/* 582 */           paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/* 583 */           paramGraphics.drawLine(paramInt3 - 1, 1, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/* 585 */           paramGraphics.setColor(MetalLookAndFeel.getMenuBackground());
/* 586 */           paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 1, 0);
/*     */         }
/*     */       }
/* 589 */       else if ((localButtonModel.isArmed()) || (((paramComponent instanceof JMenu)) && (localButtonModel.isSelected()))) {
/* 590 */         paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 591 */         paramGraphics.drawLine(0, 0, paramInt3 - 1, 0);
/*     */ 
/* 593 */         paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/* 594 */         paramGraphics.drawLine(0, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/*     */       } else {
/* 596 */         paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/* 597 */         paramGraphics.drawLine(0, 0, 0, paramInt4 - 1);
/*     */       }
/*     */ 
/* 601 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 605 */       paramInsets.set(2, 2, 2, 2);
/* 606 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class OptionDialogBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/* 466 */     int titleHeight = 0;
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 470 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 472 */       int i = -1;
/*     */       Object localObject;
/* 473 */       if ((paramComponent instanceof JInternalFrame)) {
/* 474 */         localObject = ((JInternalFrame)paramComponent).getClientProperty("JInternalFrame.messageType");
/*     */ 
/* 476 */         if ((localObject instanceof Integer)) {
/* 477 */           i = ((Integer)localObject).intValue();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 483 */       switch (i) {
/*     */       case 0:
/* 485 */         localObject = UIManager.getColor("OptionPane.errorDialog.border.background");
/*     */ 
/* 487 */         break;
/*     */       case 3:
/* 489 */         localObject = UIManager.getColor("OptionPane.questionDialog.border.background");
/*     */ 
/* 491 */         break;
/*     */       case 2:
/* 493 */         localObject = UIManager.getColor("OptionPane.warningDialog.border.background");
/*     */ 
/* 495 */         break;
/*     */       case -1:
/*     */       case 1:
/*     */       default:
/* 499 */         localObject = MetalLookAndFeel.getPrimaryControlDarkShadow();
/*     */       }
/*     */ 
/* 503 */       paramGraphics.setColor((Color)localObject);
/*     */ 
/* 506 */       paramGraphics.drawLine(1, 0, paramInt3 - 2, 0);
/* 507 */       paramGraphics.drawLine(0, 1, 0, paramInt4 - 2);
/* 508 */       paramGraphics.drawLine(paramInt3 - 1, 1, paramInt3 - 1, paramInt4 - 2);
/* 509 */       paramGraphics.drawLine(1, paramInt4 - 1, paramInt3 - 2, paramInt4 - 1);
/*     */ 
/* 512 */       for (int j = 1; j < 3; j++) {
/* 513 */         paramGraphics.drawRect(j, j, paramInt3 - j * 2 - 1, paramInt4 - j * 2 - 1);
/*     */       }
/*     */ 
/* 516 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 521 */       paramInsets.set(3, 3, 3, 3);
/* 522 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class PaletteBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/* 444 */     int titleHeight = 0;
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 448 */       paramGraphics.translate(paramInt1, paramInt2);
/* 449 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 450 */       paramGraphics.drawLine(0, 1, 0, paramInt4 - 2);
/* 451 */       paramGraphics.drawLine(1, paramInt4 - 1, paramInt3 - 2, paramInt4 - 1);
/* 452 */       paramGraphics.drawLine(paramInt3 - 1, 1, paramInt3 - 1, paramInt4 - 2);
/* 453 */       paramGraphics.drawLine(1, 0, paramInt3 - 2, 0);
/* 454 */       paramGraphics.drawRect(1, 1, paramInt3 - 3, paramInt4 - 3);
/* 455 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 460 */       paramInsets.set(1, 1, 1, 1);
/* 461 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class PopupMenuBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/* 611 */     protected static Insets borderInsets = new Insets(3, 1, 2, 1);
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 614 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 616 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlDarkShadow());
/* 617 */       paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/* 619 */       paramGraphics.setColor(MetalLookAndFeel.getPrimaryControlHighlight());
/* 620 */       paramGraphics.drawLine(1, 1, paramInt3 - 2, 1);
/* 621 */       paramGraphics.drawLine(1, 2, 1, 2);
/* 622 */       paramGraphics.drawLine(1, paramInt4 - 2, 1, paramInt4 - 2);
/*     */ 
/* 624 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 629 */       paramInsets.set(3, 1, 2, 1);
/* 630 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class QuestionDialogBorder extends MetalBorders.DialogBorder
/*     */     implements UIResource
/*     */   {
/*     */     protected Color getActiveBackground()
/*     */     {
/* 422 */       return UIManager.getColor("OptionPane.questionDialog.border.background");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class RolloverButtonBorder extends MetalBorders.ButtonBorder
/*     */   {
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 638 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 639 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/*     */ 
/* 641 */       if ((localButtonModel.isRollover()) && ((!localButtonModel.isPressed()) || (localButtonModel.isArmed())))
/* 642 */         super.paintBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class RolloverMarginBorder extends EmptyBorder
/*     */   {
/*     */     public RolloverMarginBorder()
/*     */     {
/* 658 */       super(3, 3, 3);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 662 */       Insets localInsets = null;
/*     */ 
/* 664 */       if ((paramComponent instanceof AbstractButton)) {
/* 665 */         localInsets = ((AbstractButton)paramComponent).getMargin();
/*     */       }
/* 667 */       if ((localInsets == null) || ((localInsets instanceof UIResource)))
/*     */       {
/* 669 */         paramInsets.left = this.left;
/* 670 */         paramInsets.top = this.top;
/* 671 */         paramInsets.right = this.right;
/* 672 */         paramInsets.bottom = this.bottom;
/*     */       }
/*     */       else {
/* 675 */         paramInsets.left = localInsets.left;
/* 676 */         paramInsets.top = localInsets.top;
/* 677 */         paramInsets.right = localInsets.right;
/* 678 */         paramInsets.bottom = localInsets.bottom;
/*     */       }
/* 680 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ScrollPaneBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 839 */       if (!(paramComponent instanceof JScrollPane)) {
/* 840 */         return;
/*     */       }
/* 842 */       JScrollPane localJScrollPane = (JScrollPane)paramComponent;
/* 843 */       JViewport localJViewport1 = localJScrollPane.getColumnHeader();
/* 844 */       int i = 0;
/* 845 */       if (localJViewport1 != null) {
/* 846 */         i = localJViewport1.getHeight();
/*     */       }
/* 848 */       JViewport localJViewport2 = localJScrollPane.getRowHeader();
/* 849 */       int j = 0;
/* 850 */       if (localJViewport2 != null) {
/* 851 */         j = localJViewport2.getWidth();
/*     */       }
/*     */ 
/* 854 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 856 */       paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 857 */       paramGraphics.drawRect(0, 0, paramInt3 - 2, paramInt4 - 2);
/* 858 */       paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/*     */ 
/* 860 */       paramGraphics.drawLine(paramInt3 - 1, 1, paramInt3 - 1, paramInt4 - 1);
/* 861 */       paramGraphics.drawLine(1, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/*     */ 
/* 863 */       paramGraphics.setColor(MetalLookAndFeel.getControl());
/* 864 */       paramGraphics.drawLine(paramInt3 - 2, 2 + i, paramInt3 - 2, 2 + i);
/* 865 */       paramGraphics.drawLine(1 + j, paramInt4 - 2, 1 + j, paramInt4 - 2);
/*     */ 
/* 867 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 872 */       paramInsets.set(1, 1, 2, 2);
/* 873 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class TableHeaderBorder extends AbstractBorder
/*     */   {
/* 928 */     protected Insets editorBorderInsets = new Insets(2, 2, 2, 0);
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 931 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 933 */       paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 934 */       paramGraphics.drawLine(paramInt3 - 1, 0, paramInt3 - 1, paramInt4 - 1);
/* 935 */       paramGraphics.drawLine(1, paramInt4 - 1, paramInt3 - 1, paramInt4 - 1);
/* 936 */       paramGraphics.setColor(MetalLookAndFeel.getControlHighlight());
/* 937 */       paramGraphics.drawLine(0, 0, paramInt3 - 2, 0);
/* 938 */       paramGraphics.drawLine(0, 0, 0, paramInt4 - 2);
/*     */ 
/* 940 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 944 */       paramInsets.set(2, 2, 2, 0);
/* 945 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class TextFieldBorder extends MetalBorders.Flush3DBorder
/*     */   {
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 816 */       if (!(paramComponent instanceof JTextComponent))
/*     */       {
/* 818 */         if (paramComponent.isEnabled())
/* 819 */           MetalUtils.drawFlush3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */         else {
/* 821 */           MetalUtils.drawDisabledBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */         }
/* 823 */         return;
/*     */       }
/*     */ 
/* 826 */       if ((paramComponent.isEnabled()) && (((JTextComponent)paramComponent).isEditable()))
/* 827 */         MetalUtils.drawFlush3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       else
/* 829 */         MetalUtils.drawDisabledBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ToggleButtonBorder extends MetalBorders.ButtonBorder
/*     */   {
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 897 */       AbstractButton localAbstractButton = (AbstractButton)paramComponent;
/* 898 */       ButtonModel localButtonModel = localAbstractButton.getModel();
/* 899 */       if (MetalLookAndFeel.usingOcean()) {
/* 900 */         if ((localButtonModel.isArmed()) || (!localAbstractButton.isEnabled())) {
/* 901 */           super.paintBorder(paramComponent, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */         }
/*     */         else {
/* 904 */           paramGraphics.setColor(MetalLookAndFeel.getControlDarkShadow());
/* 905 */           paramGraphics.drawRect(0, 0, paramInt3 - 1, paramInt4 - 1);
/*     */         }
/* 907 */         return;
/*     */       }
/* 909 */       if (!paramComponent.isEnabled()) {
/* 910 */         MetalUtils.drawDisabledBorder(paramGraphics, paramInt1, paramInt2, paramInt3 - 1, paramInt4 - 1);
/*     */       }
/* 912 */       else if ((localButtonModel.isPressed()) && (localButtonModel.isArmed()))
/* 913 */         MetalUtils.drawPressed3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/* 914 */       else if (localButtonModel.isSelected())
/* 915 */         MetalUtils.drawDark3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       else
/* 917 */         MetalUtils.drawFlush3DBorder(paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ToolBarBorder extends AbstractBorder
/*     */     implements UIResource, SwingConstants
/*     */   {
/* 686 */     protected MetalBumps bumps = new MetalBumps(10, 10, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), UIManager.getColor("ToolBar.background"));
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 693 */       if (!(paramComponent instanceof JToolBar)) {
/* 694 */         return;
/*     */       }
/* 696 */       paramGraphics.translate(paramInt1, paramInt2);
/*     */ 
/* 698 */       if (((JToolBar)paramComponent).isFloatable())
/*     */       {
/* 700 */         if (((JToolBar)paramComponent).getOrientation() == 0)
/*     */         {
/* 702 */           int i = MetalLookAndFeel.usingOcean() ? -1 : 0;
/* 703 */           this.bumps.setBumpArea(10, paramInt4 - 4);
/* 704 */           if (MetalUtils.isLeftToRight(paramComponent))
/* 705 */             this.bumps.paintIcon(paramComponent, paramGraphics, 2, 2 + i);
/*     */           else {
/* 707 */             this.bumps.paintIcon(paramComponent, paramGraphics, paramInt3 - 12, 2 + i);
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 713 */           this.bumps.setBumpArea(paramInt3 - 4, 10);
/* 714 */           this.bumps.paintIcon(paramComponent, paramGraphics, 2, 2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 719 */       if ((((JToolBar)paramComponent).getOrientation() == 0) && (MetalLookAndFeel.usingOcean()))
/*     */       {
/* 721 */         paramGraphics.setColor(MetalLookAndFeel.getControl());
/* 722 */         paramGraphics.drawLine(0, paramInt4 - 2, paramInt3, paramInt4 - 2);
/* 723 */         paramGraphics.setColor(UIManager.getColor("ToolBar.borderColor"));
/* 724 */         paramGraphics.drawLine(0, paramInt4 - 1, paramInt3, paramInt4 - 1);
/*     */       }
/*     */ 
/* 727 */       paramGraphics.translate(-paramInt1, -paramInt2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets) {
/* 731 */       if (MetalLookAndFeel.usingOcean()) {
/* 732 */         paramInsets.set(1, 2, 3, 2);
/*     */       }
/*     */       else {
/* 735 */         paramInsets.top = (paramInsets.left = paramInsets.bottom = paramInsets.right = 2);
/*     */       }
/*     */ 
/* 738 */       if (!(paramComponent instanceof JToolBar)) {
/* 739 */         return paramInsets;
/*     */       }
/* 741 */       if (((JToolBar)paramComponent).isFloatable()) {
/* 742 */         if (((JToolBar)paramComponent).getOrientation() == 0) {
/* 743 */           if (paramComponent.getComponentOrientation().isLeftToRight())
/* 744 */             paramInsets.left = 16;
/*     */           else
/* 746 */             paramInsets.right = 16;
/*     */         }
/*     */         else {
/* 749 */           paramInsets.top = 16;
/*     */         }
/*     */       }
/*     */ 
/* 753 */       Insets localInsets = ((JToolBar)paramComponent).getMargin();
/*     */ 
/* 755 */       if (localInsets != null) {
/* 756 */         paramInsets.left += localInsets.left;
/* 757 */         paramInsets.top += localInsets.top;
/* 758 */         paramInsets.right += localInsets.right;
/* 759 */         paramInsets.bottom += localInsets.bottom;
/*     */       }
/*     */ 
/* 762 */       return paramInsets;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class WarningDialogBorder extends MetalBorders.DialogBorder
/*     */     implements UIResource
/*     */   {
/*     */     protected Color getActiveBackground()
/*     */     {
/* 434 */       return UIManager.getColor("OptionPane.warningDialog.border.background");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalBorders
 * JD-Core Version:    0.6.2
 */