/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.ComboBoxEditor;
/*     */ import javax.swing.DefaultButtonModel;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.ListCellRenderer;
/*     */ import javax.swing.event.PopupMenuEvent;
/*     */ import javax.swing.event.PopupMenuListener;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicComboBoxEditor.UIResource;
/*     */ import javax.swing.plaf.basic.BasicComboBoxUI;
/*     */ import javax.swing.plaf.basic.ComboPopup;
/*     */ 
/*     */ public class SynthComboBoxUI extends BasicComboBoxUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private boolean useListColors;
/*     */   Insets popupInsets;
/*     */   private boolean buttonWhenNotEditable;
/*     */   private boolean pressedWhenPopupVisible;
/*     */   private ButtonHandler buttonHandler;
/*     */   private EditorFocusHandler editorFocusHandler;
/*     */   private boolean forceOpaque;
/*     */ 
/*     */   public SynthComboBoxUI()
/*     */   {
/*  93 */     this.forceOpaque = false;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 102 */     return new SynthComboBoxUI();
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 114 */     this.buttonHandler = new ButtonHandler(null);
/* 115 */     super.installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 120 */     updateStyle(this.comboBox);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComboBox paramJComboBox) {
/* 124 */     SynthStyle localSynthStyle = this.style;
/* 125 */     SynthContext localSynthContext = getContext(paramJComboBox, 1);
/*     */ 
/* 127 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 128 */     if (this.style != localSynthStyle) {
/* 129 */       this.padding = ((Insets)this.style.get(localSynthContext, "ComboBox.padding"));
/* 130 */       this.popupInsets = ((Insets)this.style.get(localSynthContext, "ComboBox.popupInsets"));
/* 131 */       this.useListColors = this.style.getBoolean(localSynthContext, "ComboBox.rendererUseListColors", true);
/*     */ 
/* 133 */       this.buttonWhenNotEditable = this.style.getBoolean(localSynthContext, "ComboBox.buttonWhenNotEditable", false);
/*     */ 
/* 135 */       this.pressedWhenPopupVisible = this.style.getBoolean(localSynthContext, "ComboBox.pressedWhenPopupVisible", false);
/*     */ 
/* 137 */       this.squareButton = this.style.getBoolean(localSynthContext, "ComboBox.squareButton", true);
/*     */ 
/* 140 */       if (localSynthStyle != null) {
/* 141 */         uninstallKeyboardActions();
/* 142 */         installKeyboardActions();
/*     */       }
/* 144 */       this.forceOpaque = this.style.getBoolean(localSynthContext, "ComboBox.forceOpaque", false);
/*     */     }
/*     */ 
/* 147 */     localSynthContext.dispose();
/*     */ 
/* 149 */     if (this.listBox != null)
/* 150 */       SynthLookAndFeel.updateStyles(this.listBox);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 159 */     this.comboBox.addPropertyChangeListener(this);
/* 160 */     this.comboBox.addMouseListener(this.buttonHandler);
/* 161 */     this.editorFocusHandler = new EditorFocusHandler(this.comboBox, null);
/* 162 */     super.installListeners();
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 170 */     if ((this.popup instanceof SynthComboPopup)) {
/* 171 */       ((SynthComboPopup)this.popup).removePopupMenuListener(this.buttonHandler);
/*     */     }
/* 173 */     super.uninstallUI(paramJComponent);
/* 174 */     this.buttonHandler = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 182 */     SynthContext localSynthContext = getContext(this.comboBox, 1);
/*     */ 
/* 184 */     this.style.uninstallDefaults(localSynthContext);
/* 185 */     localSynthContext.dispose();
/* 186 */     this.style = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 194 */     this.editorFocusHandler.unregister();
/* 195 */     this.comboBox.removePropertyChangeListener(this);
/* 196 */     this.comboBox.removeMouseListener(this.buttonHandler);
/* 197 */     this.buttonHandler.pressed = false;
/* 198 */     this.buttonHandler.over = false;
/* 199 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 207 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 211 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 222 */     if (!(paramJComponent instanceof JComboBox)) return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */ 
/* 224 */     JComboBox localJComboBox = (JComboBox)paramJComponent;
/* 225 */     if (shouldActLikeButton()) {
/* 226 */       i = 1;
/* 227 */       if (!paramJComponent.isEnabled()) {
/* 228 */         i = 8;
/*     */       }
/* 230 */       if (this.buttonHandler.isPressed()) {
/* 231 */         i |= 4;
/*     */       }
/* 233 */       if (this.buttonHandler.isRollover()) {
/* 234 */         i |= 2;
/*     */       }
/* 236 */       if (localJComboBox.isFocusOwner()) {
/* 237 */         i |= 256;
/*     */       }
/* 239 */       return i;
/*     */     }
/*     */ 
/* 244 */     int i = SynthLookAndFeel.getComponentState(paramJComponent);
/* 245 */     if ((localJComboBox.isEditable()) && (localJComboBox.getEditor().getEditorComponent().isFocusOwner()))
/*     */     {
/* 247 */       i |= 256;
/*     */     }
/* 249 */     return i;
/*     */   }
/*     */ 
/*     */   protected ComboPopup createPopup()
/*     */   {
/* 258 */     SynthComboPopup localSynthComboPopup = new SynthComboPopup(this.comboBox);
/* 259 */     localSynthComboPopup.addPopupMenuListener(this.buttonHandler);
/* 260 */     return localSynthComboPopup;
/*     */   }
/*     */ 
/*     */   protected ListCellRenderer createRenderer()
/*     */   {
/* 268 */     return new SynthComboBoxRenderer();
/*     */   }
/*     */ 
/*     */   protected ComboBoxEditor createEditor()
/*     */   {
/* 276 */     return new SynthComboBoxEditor(null);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 288 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 289 */       updateStyle(this.comboBox);
/*     */   }
/*     */ 
/*     */   protected JButton createArrowButton()
/*     */   {
/* 298 */     SynthArrowButton localSynthArrowButton = new SynthArrowButton(5);
/* 299 */     localSynthArrowButton.setName("ComboBox.arrowButton");
/* 300 */     localSynthArrowButton.setModel(this.buttonHandler);
/* 301 */     return localSynthArrowButton;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 321 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 323 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 324 */     localSynthContext.getPainter().paintComboBoxBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 326 */     paint(localSynthContext, paramGraphics);
/* 327 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 341 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 343 */     paint(localSynthContext, paramGraphics);
/* 344 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 355 */     this.hasFocus = this.comboBox.hasFocus();
/* 356 */     if (!this.comboBox.isEditable()) {
/* 357 */       Rectangle localRectangle = rectangleForCurrentValue();
/* 358 */       paintCurrentValue(paramGraphics, localRectangle, this.hasFocus);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 368 */     paramSynthContext.getPainter().paintComboBoxBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void paintCurrentValue(Graphics paramGraphics, Rectangle paramRectangle, boolean paramBoolean)
/*     */   {
/* 376 */     ListCellRenderer localListCellRenderer = this.comboBox.getRenderer();
/*     */ 
/* 379 */     Component localComponent = localListCellRenderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
/*     */ 
/* 383 */     boolean bool = false;
/* 384 */     if ((localComponent instanceof JPanel)) {
/* 385 */       bool = true;
/*     */     }
/*     */ 
/* 388 */     if ((localComponent instanceof UIResource)) {
/* 389 */       localComponent.setName("ComboBox.renderer");
/*     */     }
/*     */ 
/* 392 */     int i = (this.forceOpaque) && ((localComponent instanceof JComponent)) ? 1 : 0;
/* 393 */     if (i != 0) {
/* 394 */       ((JComponent)localComponent).setOpaque(false);
/*     */     }
/*     */ 
/* 397 */     int j = paramRectangle.x; int k = paramRectangle.y; int m = paramRectangle.width; int n = paramRectangle.height;
/* 398 */     if (this.padding != null) {
/* 399 */       j = paramRectangle.x + this.padding.left;
/* 400 */       k = paramRectangle.y + this.padding.top;
/* 401 */       m = paramRectangle.width - (this.padding.left + this.padding.right);
/* 402 */       n = paramRectangle.height - (this.padding.top + this.padding.bottom);
/*     */     }
/*     */ 
/* 405 */     this.currentValuePane.paintComponent(paramGraphics, localComponent, this.comboBox, j, k, m, n, bool);
/*     */ 
/* 407 */     if (i != 0)
/* 408 */       ((JComponent)localComponent).setOpaque(true);
/*     */   }
/*     */ 
/*     */   private boolean shouldActLikeButton()
/*     */   {
/* 418 */     return (this.buttonWhenNotEditable) && (!this.comboBox.isEditable());
/*     */   }
/*     */ 
/*     */   protected Dimension getDefaultSize()
/*     */   {
/* 435 */     SynthComboBoxRenderer localSynthComboBoxRenderer = new SynthComboBoxRenderer();
/* 436 */     Dimension localDimension = getSizeForComponent(localSynthComboBoxRenderer.getListCellRendererComponent(this.listBox, " ", -1, false, false));
/* 437 */     return new Dimension(localDimension.width, localDimension.height);
/*     */   }
/*     */ 
/*     */   private final class ButtonHandler extends DefaultButtonModel
/*     */     implements MouseListener, PopupMenuListener
/*     */   {
/*     */     private boolean over;
/*     */     private boolean pressed;
/*     */ 
/*     */     private ButtonHandler()
/*     */     {
/*     */     }
/*     */ 
/*     */     private void updatePressed(boolean paramBoolean)
/*     */     {
/* 564 */       this.pressed = ((paramBoolean) && (isEnabled()));
/* 565 */       if (SynthComboBoxUI.this.shouldActLikeButton())
/* 566 */         SynthComboBoxUI.this.comboBox.repaint();
/*     */     }
/*     */ 
/*     */     private void updateOver(boolean paramBoolean)
/*     */     {
/* 579 */       boolean bool1 = isRollover();
/* 580 */       this.over = ((paramBoolean) && (isEnabled()));
/* 581 */       boolean bool2 = isRollover();
/* 582 */       if ((SynthComboBoxUI.this.shouldActLikeButton()) && (bool1 != bool2))
/* 583 */         SynthComboBoxUI.this.comboBox.repaint();
/*     */     }
/*     */ 
/*     */     public boolean isPressed()
/*     */     {
/* 601 */       boolean bool = SynthComboBoxUI.this.shouldActLikeButton() ? this.pressed : super.isPressed();
/* 602 */       return (bool) || ((SynthComboBoxUI.this.pressedWhenPopupVisible) && (SynthComboBoxUI.this.comboBox.isPopupVisible()));
/*     */     }
/*     */ 
/*     */     public boolean isArmed()
/*     */     {
/* 615 */       int i = (SynthComboBoxUI.this.shouldActLikeButton()) || ((SynthComboBoxUI.this.pressedWhenPopupVisible) && (SynthComboBoxUI.this.comboBox.isPopupVisible())) ? 1 : 0;
/*     */ 
/* 617 */       return i != 0 ? isPressed() : super.isArmed();
/*     */     }
/*     */ 
/*     */     public boolean isRollover()
/*     */     {
/* 628 */       return SynthComboBoxUI.this.shouldActLikeButton() ? this.over : super.isRollover();
/*     */     }
/*     */ 
/*     */     public void setPressed(boolean paramBoolean)
/*     */     {
/* 638 */       super.setPressed(paramBoolean);
/* 639 */       updatePressed(paramBoolean);
/*     */     }
/*     */ 
/*     */     public void setRollover(boolean paramBoolean)
/*     */     {
/* 649 */       super.setRollover(paramBoolean);
/* 650 */       updateOver(paramBoolean);
/*     */     }
/*     */ 
/*     */     public void mouseEntered(MouseEvent paramMouseEvent)
/*     */     {
/* 659 */       updateOver(true);
/*     */     }
/*     */ 
/*     */     public void mouseExited(MouseEvent paramMouseEvent)
/*     */     {
/* 664 */       updateOver(false);
/*     */     }
/*     */ 
/*     */     public void mousePressed(MouseEvent paramMouseEvent)
/*     */     {
/* 669 */       updatePressed(true);
/*     */     }
/*     */ 
/*     */     public void mouseReleased(MouseEvent paramMouseEvent)
/*     */     {
/* 674 */       updatePressed(false);
/*     */     }
/*     */ 
/*     */     public void mouseClicked(MouseEvent paramMouseEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void popupMenuCanceled(PopupMenuEvent paramPopupMenuEvent)
/*     */     {
/* 696 */       if ((SynthComboBoxUI.this.shouldActLikeButton()) || (SynthComboBoxUI.this.pressedWhenPopupVisible))
/* 697 */         SynthComboBoxUI.this.comboBox.repaint();
/*     */     }
/*     */ 
/*     */     public void popupMenuWillBecomeVisible(PopupMenuEvent paramPopupMenuEvent)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void popupMenuWillBecomeInvisible(PopupMenuEvent paramPopupMenuEvent)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class EditorFocusHandler
/*     */     implements FocusListener, PropertyChangeListener
/*     */   {
/*     */     private JComboBox comboBox;
/* 713 */     private ComboBoxEditor editor = null;
/* 714 */     private Component editorComponent = null;
/*     */ 
/*     */     private EditorFocusHandler(JComboBox paramJComboBox) {
/* 717 */       this.comboBox = paramJComboBox;
/* 718 */       this.editor = paramJComboBox.getEditor();
/* 719 */       if (this.editor != null) {
/* 720 */         this.editorComponent = this.editor.getEditorComponent();
/* 721 */         if (this.editorComponent != null) {
/* 722 */           this.editorComponent.addFocusListener(this);
/*     */         }
/*     */       }
/* 725 */       paramJComboBox.addPropertyChangeListener("editor", this);
/*     */     }
/*     */ 
/*     */     public void unregister() {
/* 729 */       this.comboBox.removePropertyChangeListener(this);
/* 730 */       if (this.editorComponent != null)
/* 731 */         this.editorComponent.removeFocusListener(this);
/*     */     }
/*     */ 
/*     */     public void focusGained(FocusEvent paramFocusEvent)
/*     */     {
/* 738 */       this.comboBox.repaint();
/*     */     }
/*     */ 
/*     */     public void focusLost(FocusEvent paramFocusEvent)
/*     */     {
/* 744 */       this.comboBox.repaint();
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 754 */       ComboBoxEditor localComboBoxEditor = this.comboBox.getEditor();
/* 755 */       if (this.editor != localComboBoxEditor) {
/* 756 */         if (this.editorComponent != null) {
/* 757 */           this.editorComponent.removeFocusListener(this);
/*     */         }
/* 759 */         this.editor = localComboBoxEditor;
/* 760 */         if (this.editor != null) {
/* 761 */           this.editorComponent = this.editor.getEditorComponent();
/* 762 */           if (this.editorComponent != null)
/* 763 */             this.editorComponent.addFocusListener(this);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SynthComboBoxEditor extends BasicComboBoxEditor.UIResource
/*     */   {
/*     */     public JTextField createEditorComponent()
/*     */     {
/* 522 */       JTextField localJTextField = new JTextField("", 9);
/* 523 */       localJTextField.setName("ComboBox.textField");
/* 524 */       return localJTextField;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SynthComboBoxRenderer extends JLabel
/*     */     implements ListCellRenderer, UIResource
/*     */   {
/*     */     public SynthComboBoxRenderer()
/*     */     {
/* 450 */       setText(" ");
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 459 */       String str = super.getName();
/*     */ 
/* 461 */       return str == null ? "ComboBox.renderer" : str;
/*     */     }
/*     */ 
/*     */     public Component getListCellRendererComponent(JList paramJList, Object paramObject, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 467 */       setName("ComboBox.listRenderer");
/* 468 */       SynthLookAndFeel.resetSelectedUI();
/* 469 */       if (paramBoolean1) {
/* 470 */         setBackground(paramJList.getSelectionBackground());
/* 471 */         setForeground(paramJList.getSelectionForeground());
/* 472 */         if (!SynthComboBoxUI.this.useListColors) {
/* 473 */           SynthLookAndFeel.setSelectedUI((SynthLabelUI)SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), paramBoolean1, paramBoolean2, paramJList.isEnabled(), false);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 479 */         setBackground(paramJList.getBackground());
/* 480 */         setForeground(paramJList.getForeground());
/*     */       }
/*     */ 
/* 483 */       setFont(paramJList.getFont());
/*     */ 
/* 485 */       if ((paramObject instanceof Icon)) {
/* 486 */         setIcon((Icon)paramObject);
/* 487 */         setText("");
/*     */       } else {
/* 489 */         String str = paramObject == null ? " " : paramObject.toString();
/*     */ 
/* 491 */         if ("".equals(str)) {
/* 492 */           str = " ";
/*     */         }
/* 494 */         setText(str);
/*     */       }
/*     */ 
/* 502 */       if (SynthComboBoxUI.this.comboBox != null) {
/* 503 */         setEnabled(SynthComboBoxUI.this.comboBox.isEnabled());
/* 504 */         setComponentOrientation(SynthComboBoxUI.this.comboBox.getComponentOrientation());
/*     */       }
/*     */ 
/* 507 */       return this;
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics)
/*     */     {
/* 512 */       super.paint(paramGraphics);
/* 513 */       SynthLookAndFeel.resetSelectedUI();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthComboBoxUI
 * JD-Core Version:    0.6.2
 */