/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.ButtonModel;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxEditor.UIResource;
/*     */ import javax.swing.plaf.basic.BasicComboBoxRenderer.UIResource;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI.ComboBoxLayoutManager;
/*     */ import javax.swing.plaf.basic.BasicComboPopup;
/*     */ import javax.swing.plaf.basic.BasicComboPopup.InvocationKeyHandler;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ import sun.swing.DefaultLookup;
/*     */ import sun.swing.StringUIClientPropertyKey;
/*     */ 
/*     */ public class WindowsComboBoxUI extends BasicComboBoxUI
/*     */ {
/*  61 */   private static final MouseListener rolloverListener = new MouseAdapter()
/*     */   {
/*     */     private void handleRollover(MouseEvent paramAnonymousMouseEvent, boolean paramAnonymousBoolean) {
/*  64 */       JComboBox localJComboBox = getComboBox(paramAnonymousMouseEvent);
/*  65 */       WindowsComboBoxUI localWindowsComboBoxUI = getWindowsComboBoxUI(paramAnonymousMouseEvent);
/*  66 */       if ((localJComboBox == null) || (localWindowsComboBoxUI == null)) {
/*  67 */         return;
/*     */       }
/*  69 */       if (!localJComboBox.isEditable())
/*     */       {
/*  72 */         ButtonModel localButtonModel = null;
/*  73 */         if (localWindowsComboBoxUI.arrowButton != null) {
/*  74 */           localButtonModel = localWindowsComboBoxUI.arrowButton.getModel();
/*     */         }
/*  76 */         if (localButtonModel != null) {
/*  77 */           localButtonModel.setRollover(paramAnonymousBoolean);
/*     */         }
/*     */       }
/*  80 */       localWindowsComboBoxUI.isRollover = paramAnonymousBoolean;
/*  81 */       localJComboBox.repaint();
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent paramAnonymousMouseEvent) {
/*  85 */       handleRollover(paramAnonymousMouseEvent, true);
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent paramAnonymousMouseEvent) {
/*  89 */       handleRollover(paramAnonymousMouseEvent, false);
/*     */     }
/*     */ 
/*     */     private JComboBox getComboBox(MouseEvent paramAnonymousMouseEvent) {
/*  93 */       Object localObject = paramAnonymousMouseEvent.getSource();
/*  94 */       JComboBox localJComboBox = null;
/*  95 */       if ((localObject instanceof JComboBox))
/*  96 */         localJComboBox = (JComboBox)localObject;
/*  97 */       else if ((localObject instanceof WindowsComboBoxUI.XPComboBoxButton)) {
/*  98 */         localJComboBox = ((WindowsComboBoxUI.XPComboBoxButton)localObject).getWindowsComboBoxUI().comboBox;
/*     */       }
/*     */ 
/* 101 */       return localJComboBox;
/*     */     }
/*     */ 
/*     */     private WindowsComboBoxUI getWindowsComboBoxUI(MouseEvent paramAnonymousMouseEvent) {
/* 105 */       JComboBox localJComboBox = getComboBox(paramAnonymousMouseEvent);
/* 106 */       WindowsComboBoxUI localWindowsComboBoxUI = null;
/* 107 */       if ((localJComboBox != null) && ((localJComboBox.getUI() instanceof WindowsComboBoxUI)))
/*     */       {
/* 109 */         localWindowsComboBoxUI = (WindowsComboBoxUI)localJComboBox.getUI();
/*     */       }
/* 111 */       return localWindowsComboBoxUI;
/*     */     }
/*  61 */   };
/*     */   private boolean isRollover;
/* 117 */   private static final PropertyChangeListener componentOrientationListener = new PropertyChangeListener()
/*     */   {
/*     */     public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/* 120 */       String str = paramAnonymousPropertyChangeEvent.getPropertyName();
/* 121 */       Object localObject = null;
/* 122 */       if (("componentOrientation" == str) && (((localObject = paramAnonymousPropertyChangeEvent.getSource()) instanceof JComboBox)) && ((((JComboBox)localObject).getUI() instanceof WindowsComboBoxUI)))
/*     */       {
/* 126 */         JComboBox localJComboBox = (JComboBox)localObject;
/* 127 */         WindowsComboBoxUI localWindowsComboBoxUI = (WindowsComboBoxUI)localJComboBox.getUI();
/* 128 */         if ((localWindowsComboBoxUI.arrowButton instanceof WindowsComboBoxUI.XPComboBoxButton))
/* 129 */           ((WindowsComboBoxUI.XPComboBoxButton)localWindowsComboBoxUI.arrowButton).setPart(localJComboBox.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT ? TMSchema.Part.CP_DROPDOWNBUTTONLEFT : TMSchema.Part.CP_DROPDOWNBUTTONRIGHT);
/*     */       }
/*     */     }
/* 117 */   };
/*     */ 
/*     */   public WindowsComboBoxUI()
/*     */   {
/* 115 */     this.isRollover = false;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 140 */     return new WindowsComboBoxUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/* 144 */     super.installUI(paramJComponent);
/* 145 */     this.isRollover = false;
/* 146 */     this.comboBox.setRequestFocusEnabled(true);
/* 147 */     if ((XPStyle.getXP() != null) && (this.arrowButton != null))
/*     */     {
/* 150 */       this.comboBox.addMouseListener(rolloverListener);
/* 151 */       this.arrowButton.addMouseListener(rolloverListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent) {
/* 156 */     this.comboBox.removeMouseListener(rolloverListener);
/* 157 */     if (this.arrowButton != null) {
/* 158 */       this.arrowButton.removeMouseListener(rolloverListener);
/*     */     }
/* 160 */     super.uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 169 */     super.installListeners();
/* 170 */     XPStyle localXPStyle = XPStyle.getXP();
/*     */ 
/* 172 */     if ((localXPStyle != null) && (localXPStyle.isSkinDefined(this.comboBox, TMSchema.Part.CP_DROPDOWNBUTTONRIGHT)))
/*     */     {
/* 174 */       this.comboBox.addPropertyChangeListener("componentOrientation", componentOrientationListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 185 */     super.uninstallListeners();
/* 186 */     this.comboBox.removePropertyChangeListener("componentOrientation", componentOrientationListener);
/*     */   }
/*     */ 
/*     */   protected void configureEditor()
/*     */   {
/* 195 */     super.configureEditor();
/* 196 */     if (XPStyle.getXP() != null)
/* 197 */       this.editor.addMouseListener(rolloverListener);
/*     */   }
/*     */ 
/*     */   protected void unconfigureEditor()
/*     */   {
/* 206 */     super.unconfigureEditor();
/* 207 */     this.editor.removeMouseListener(rolloverListener);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 215 */     if (XPStyle.getXP() != null) {
/* 216 */       paintXPComboBoxBackground(paramGraphics, paramJComponent);
/*     */     }
/* 218 */     super.paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   TMSchema.State getXPComboBoxState(JComponent paramJComponent) {
/* 222 */     TMSchema.State localState = TMSchema.State.NORMAL;
/* 223 */     if (!paramJComponent.isEnabled())
/* 224 */       localState = TMSchema.State.DISABLED;
/* 225 */     else if (isPopupVisible(this.comboBox))
/* 226 */       localState = TMSchema.State.PRESSED;
/* 227 */     else if (this.isRollover) {
/* 228 */       localState = TMSchema.State.HOT;
/*     */     }
/* 230 */     return localState;
/*     */   }
/*     */ 
/*     */   private void paintXPComboBoxBackground(Graphics paramGraphics, JComponent paramJComponent) {
/* 234 */     XPStyle localXPStyle = XPStyle.getXP();
/* 235 */     if (localXPStyle == null) {
/* 236 */       return;
/*     */     }
/* 238 */     TMSchema.State localState = getXPComboBoxState(paramJComponent);
/* 239 */     XPStyle.Skin localSkin = null;
/* 240 */     if ((!this.comboBox.isEditable()) && (localXPStyle.isSkinDefined(paramJComponent, TMSchema.Part.CP_READONLY)))
/*     */     {
/* 242 */       localSkin = localXPStyle.getSkin(paramJComponent, TMSchema.Part.CP_READONLY);
/*     */     }
/* 244 */     if (localSkin == null) {
/* 245 */       localSkin = localXPStyle.getSkin(paramJComponent, TMSchema.Part.CP_COMBOBOX);
/*     */     }
/* 247 */     localSkin.paintSkin(paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), localState);
/*     */   }
/*     */ 
/*     */   public void paintCurrentValue(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean)
/*     */   {
/* 261 */     XPStyle localXPStyle = XPStyle.getXP();
/* 262 */     if (localXPStyle != null) {
/* 263 */       paramRectangle.x += 2;
/* 264 */       paramRectangle.y += 2;
/* 265 */       paramRectangle.width -= 4;
/* 266 */       paramRectangle.height -= 4;
/*     */     } else {
/* 268 */       paramRectangle.x += 1;
/* 269 */       paramRectangle.y += 1;
/* 270 */       paramRectangle.width -= 2;
/* 271 */       paramRectangle.height -= 2;
/*     */     }
/* 273 */     if ((!this.comboBox.isEditable()) && (localXPStyle != null) && (localXPStyle.isSkinDefined(this.comboBox, TMSchema.Part.CP_READONLY)))
/*     */     {
/* 280 */       ListCellRenderer localListCellRenderer = this.comboBox.getRenderer();
/*     */       Component localComponent;
/* 282 */       if ((paramBoolean) && (!isPopupVisible(this.comboBox))) {
/* 283 */         localComponent = localListCellRenderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, true, false);
/*     */       }
/*     */       else
/*     */       {
/* 290 */         localComponent = localListCellRenderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
/*     */       }
/*     */ 
/* 297 */       localComponent.setFont(this.comboBox.getFont());
/* 298 */       if (this.comboBox.isEnabled()) {
/* 299 */         localComponent.setForeground(this.comboBox.getForeground());
/* 300 */         localComponent.setBackground(this.comboBox.getBackground());
/*     */       } else {
/* 302 */         localComponent.setForeground(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledForeground", null));
/*     */ 
/* 304 */         localComponent.setBackground(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledBackground", null));
/*     */       }
/*     */ 
/* 307 */       boolean bool = false;
/* 308 */       if ((localComponent instanceof JPanel)) {
/* 309 */         bool = true;
/*     */       }
/* 311 */       this.currentValuePane.paintComponent(paramGraphics, localComponent, this.comboBox, paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height, bool);
/*     */     }
/*     */     else
/*     */     {
/* 315 */       super.paintCurrentValue(paramGraphics, paramRectangle, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintCurrentValueBackground(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean)
/*     */   {
/* 325 */     if (XPStyle.getXP() == null)
/* 326 */       super.paintCurrentValueBackground(paramGraphics, paramRectangle, paramBoolean);
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 331 */     Dimension localDimension = super.getMinimumSize(paramJComponent);
/* 332 */     if (XPStyle.getXP() != null)
/* 333 */       localDimension.width += 5;
/*     */     else {
/* 335 */       localDimension.width += 4;
/*     */     }
/* 337 */     localDimension.height += 2;
/* 338 */     return localDimension;
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayoutManager()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: new 131	com/sun/java/swing/plaf/windows/WindowsComboBoxUI$3
/*     */     //   3: dup
/*     */     //   4: aload_0
/*     */     //   5: invokespecial 285	com/sun/java/swing/plaf/windows/WindowsComboBoxUI$3:<init>	(Lcom/sun/java/swing/plaf/windows/WindowsComboBoxUI;)V
/*     */     //   8: areturn
/*     */   }
/*     */ 
/*     */   protected void installKeyboardActions()
/*     */   {
/* 367 */     super.installKeyboardActions();
/*     */   }
/*     */ 
/*     */   protected ComboPopup createPopup() {
/* 371 */     return super.createPopup();
/*     */   }
/*     */ 
/*     */   protected ComboBoxEditor createEditor()
/*     */   {
/* 383 */     return new WindowsComboBoxEditor();
/*     */   }
/*     */ 
/*     */   protected ListCellRenderer createRenderer()
/*     */   {
/* 392 */     XPStyle localXPStyle = XPStyle.getXP();
/* 393 */     if ((localXPStyle != null) && (localXPStyle.isSkinDefined(this.comboBox, TMSchema.Part.CP_READONLY))) {
/* 394 */       return new WindowsComboBoxRenderer(null);
/*     */     }
/* 396 */     return super.createRenderer();
/*     */   }
/*     */ 
/*     */   protected JButton createArrowButton()
/*     */   {
/* 407 */     XPStyle localXPStyle = XPStyle.getXP();
/* 408 */     if (localXPStyle != null) {
/* 409 */       return new XPComboBoxButton(localXPStyle);
/*     */     }
/* 411 */     return super.createArrowButton();
/*     */   }
/*     */ 
/*     */   public static class WindowsComboBoxEditor extends BasicComboBoxEditor.UIResource
/*     */   {
/*     */     protected JTextField createEditorComponent()
/*     */     {
/* 497 */       JTextField localJTextField = super.createEditorComponent();
/* 498 */       Border localBorder = (Border)UIManager.get("ComboBox.editorBorder");
/* 499 */       if (localBorder != null) {
/* 500 */         localJTextField.setBorder(localBorder);
/*     */       }
/* 502 */       localJTextField.setOpaque(false);
/* 503 */       return localJTextField;
/*     */     }
/*     */ 
/*     */     public void setItem(Object paramObject) {
/* 507 */       super.setItem(paramObject);
/* 508 */       if (this.editor.hasFocus())
/* 509 */         this.editor.selectAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class WindowsComboBoxRenderer extends BasicComboBoxRenderer.UIResource
/*     */   {
/* 520 */     private static final Object BORDER_KEY = new StringUIClientPropertyKey("BORDER_KEY");
/*     */ 
/* 522 */     private static final Border NULL_BORDER = new EmptyBorder(0, 0, 0, 0);
/*     */ 
/*     */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 533 */       Component localComponent = super.getListCellRendererComponent(paramJList, paramObject, paramInt, paramBoolean1, paramBoolean2);
/*     */ 
/* 536 */       if ((localComponent instanceof JComponent)) {
/* 537 */         JComponent localJComponent = (JComponent)localComponent;
/*     */         Object localObject;
/* 538 */         if ((paramInt == -1) && (paramBoolean1)) {
/* 539 */           localObject = localJComponent.getBorder();
/* 540 */           WindowsBorders.DashedBorder localDashedBorder = new WindowsBorders.DashedBorder(paramJList.getForeground());
/*     */ 
/* 542 */           localJComponent.setBorder(localDashedBorder);
/*     */ 
/* 544 */           if (localJComponent.getClientProperty(BORDER_KEY) == null) {
/* 545 */             localJComponent.putClientProperty(BORDER_KEY, localObject == null ? NULL_BORDER : localObject);
/*     */           }
/*     */ 
/*     */         }
/* 549 */         else if ((localJComponent.getBorder() instanceof WindowsBorders.DashedBorder))
/*     */         {
/* 551 */           localObject = localJComponent.getClientProperty(BORDER_KEY);
/* 552 */           if ((localObject instanceof Border)) {
/* 553 */             localJComponent.setBorder(localObject == NULL_BORDER ? null : (Border)localObject);
/*     */           }
/*     */ 
/* 557 */           localJComponent.putClientProperty(BORDER_KEY, null);
/*     */         }
/*     */ 
/* 560 */         if (paramInt == -1) {
/* 561 */           localJComponent.setOpaque(false);
/* 562 */           localJComponent.setForeground(paramJList.getForeground());
/*     */         } else {
/* 564 */           localJComponent.setOpaque(true);
/*     */         }
/*     */       }
/* 567 */       return localComponent;
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected class WindowsComboPopup extends BasicComboPopup
/*     */   {
/*     */     public WindowsComboPopup(JComboBox arg2)
/*     */     {
/* 471 */       super();
/*     */     }
/*     */ 
/*     */     protected KeyListener createKeyListener() {
/* 475 */       return new InvocationKeyHandler();
/*     */     }
/*     */ 
/*     */     protected class InvocationKeyHandler extends BasicComboPopup.InvocationKeyHandler {
/*     */       protected InvocationKeyHandler() {
/* 480 */         super();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class XPComboBoxButton extends XPStyle.GlyphButton
/*     */   {
/*     */     public XPComboBoxButton(XPStyle arg2)
/*     */     {
/* 417 */       super(WindowsComboBoxUI.this.comboBox.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT ? TMSchema.Part.CP_DROPDOWNBUTTONLEFT : !localObject.isSkinDefined(WindowsComboBoxUI.this.comboBox, TMSchema.Part.CP_DROPDOWNBUTTONRIGHT) ? TMSchema.Part.CP_DROPDOWNBUTTON : TMSchema.Part.CP_DROPDOWNBUTTONRIGHT);
/*     */ 
/* 424 */       setRequestFocusEnabled(false);
/*     */     }
/*     */ 
/*     */     protected TMSchema.State getState()
/*     */     {
/* 430 */       TMSchema.State localState = super.getState();
/* 431 */       XPStyle localXPStyle = XPStyle.getXP();
/* 432 */       if ((localState != TMSchema.State.DISABLED) && (WindowsComboBoxUI.this.comboBox != null) && (!WindowsComboBoxUI.this.comboBox.isEditable()) && (localXPStyle != null) && (localXPStyle.isSkinDefined(WindowsComboBoxUI.this.comboBox, TMSchema.Part.CP_DROPDOWNBUTTONRIGHT)))
/*     */       {
/* 440 */         localState = TMSchema.State.NORMAL;
/*     */       }
/* 442 */       return localState;
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize() {
/* 446 */       return new Dimension(17, 21);
/*     */     }
/*     */ 
/*     */     void setPart(TMSchema.Part paramPart) {
/* 450 */       setPart(WindowsComboBoxUI.this.comboBox, paramPart);
/*     */     }
/*     */ 
/*     */     WindowsComboBoxUI getWindowsComboBoxUI() {
/* 454 */       return WindowsComboBoxUI.this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsComboBoxUI
 * JD-Core Version:    0.6.2
 */