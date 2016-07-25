/*      */ package javax.swing.plaf.basic;
/*      */ 
/*      */ import java.awt.AWTEvent;
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.FocusTraversalPolicy;
/*      */ import java.awt.Insets;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.DateFormat.Field;
/*      */ import java.text.Format;
/*      */ import java.text.Format.Field;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Map;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.ButtonModel;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFormattedTextField;
/*      */ import javax.swing.JFormattedTextField.AbstractFormatter;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JSpinner;
/*      */ import javax.swing.JSpinner.DateEditor;
/*      */ import javax.swing.JSpinner.DefaultEditor;
/*      */ import javax.swing.JTextField;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.SpinnerDateModel;
/*      */ import javax.swing.SpinnerModel;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.CompoundBorder;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.SpinnerUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.InternationalFormatter;
/*      */ import sun.swing.DefaultLookup;
/*      */ 
/*      */ public class BasicSpinnerUI extends SpinnerUI
/*      */ {
/*      */   protected JSpinner spinner;
/*      */   private Handler handler;
/*   72 */   private static final ArrowButtonHandler nextButtonHandler = new ArrowButtonHandler("increment", true);
/*   73 */   private static final ArrowButtonHandler previousButtonHandler = new ArrowButtonHandler("decrement", false);
/*      */   private PropertyChangeListener propertyChangeListener;
/*   81 */   private static final Dimension zeroSize = new Dimension(0, 0);
/*      */ 
/*      */   public static ComponentUI createUI(JComponent paramJComponent)
/*      */   {
/*   93 */     return new BasicSpinnerUI();
/*      */   }
/*      */ 
/*      */   private void maybeAdd(Component paramComponent, String paramString)
/*      */   {
/*   98 */     if (paramComponent != null)
/*   99 */       this.spinner.add(paramComponent, paramString);
/*      */   }
/*      */ 
/*      */   public void installUI(JComponent paramJComponent)
/*      */   {
/*  117 */     this.spinner = ((JSpinner)paramJComponent);
/*  118 */     installDefaults();
/*  119 */     installListeners();
/*  120 */     maybeAdd(createNextButton(), "Next");
/*  121 */     maybeAdd(createPreviousButton(), "Previous");
/*  122 */     maybeAdd(createEditor(), "Editor");
/*  123 */     updateEnabledState();
/*  124 */     installKeyboardActions();
/*      */   }
/*      */ 
/*      */   public void uninstallUI(JComponent paramJComponent)
/*      */   {
/*  135 */     uninstallDefaults();
/*  136 */     uninstallListeners();
/*  137 */     this.spinner = null;
/*  138 */     paramJComponent.removeAll();
/*      */   }
/*      */ 
/*      */   protected void installListeners()
/*      */   {
/*  153 */     this.propertyChangeListener = createPropertyChangeListener();
/*  154 */     this.spinner.addPropertyChangeListener(this.propertyChangeListener);
/*  155 */     if (DefaultLookup.getBoolean(this.spinner, this, "Spinner.disableOnBoundaryValues", false))
/*      */     {
/*  157 */       this.spinner.addChangeListener(getHandler());
/*      */     }
/*  159 */     JComponent localJComponent = this.spinner.getEditor();
/*  160 */     if ((localJComponent != null) && ((localJComponent instanceof JSpinner.DefaultEditor))) {
/*  161 */       JFormattedTextField localJFormattedTextField = ((JSpinner.DefaultEditor)localJComponent).getTextField();
/*  162 */       if (localJFormattedTextField != null) {
/*  163 */         localJFormattedTextField.addFocusListener(nextButtonHandler);
/*  164 */         localJFormattedTextField.addFocusListener(previousButtonHandler);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void uninstallListeners()
/*      */   {
/*  179 */     this.spinner.removePropertyChangeListener(this.propertyChangeListener);
/*  180 */     this.spinner.removeChangeListener(this.handler);
/*  181 */     JComponent localJComponent = this.spinner.getEditor();
/*  182 */     removeEditorBorderListener(localJComponent);
/*  183 */     if ((localJComponent instanceof JSpinner.DefaultEditor)) {
/*  184 */       JFormattedTextField localJFormattedTextField = ((JSpinner.DefaultEditor)localJComponent).getTextField();
/*  185 */       if (localJFormattedTextField != null) {
/*  186 */         localJFormattedTextField.removeFocusListener(nextButtonHandler);
/*  187 */         localJFormattedTextField.removeFocusListener(previousButtonHandler);
/*      */       }
/*      */     }
/*  190 */     this.propertyChangeListener = null;
/*  191 */     this.handler = null;
/*      */   }
/*      */ 
/*      */   protected void installDefaults()
/*      */   {
/*  209 */     this.spinner.setLayout(createLayout());
/*  210 */     LookAndFeel.installBorder(this.spinner, "Spinner.border");
/*  211 */     LookAndFeel.installColorsAndFont(this.spinner, "Spinner.background", "Spinner.foreground", "Spinner.font");
/*  212 */     LookAndFeel.installProperty(this.spinner, "opaque", Boolean.TRUE);
/*      */   }
/*      */ 
/*      */   protected void uninstallDefaults()
/*      */   {
/*  224 */     this.spinner.setLayout(null);
/*      */   }
/*      */ 
/*      */   private Handler getHandler()
/*      */   {
/*  229 */     if (this.handler == null) {
/*  230 */       this.handler = new Handler(null);
/*      */     }
/*  232 */     return this.handler;
/*      */   }
/*      */ 
/*      */   protected void installNextButtonListeners(Component paramComponent)
/*      */   {
/*  246 */     installButtonListeners(paramComponent, nextButtonHandler);
/*      */   }
/*      */ 
/*      */   protected void installPreviousButtonListeners(Component paramComponent)
/*      */   {
/*  259 */     installButtonListeners(paramComponent, previousButtonHandler);
/*      */   }
/*      */ 
/*      */   private void installButtonListeners(Component paramComponent, ArrowButtonHandler paramArrowButtonHandler)
/*      */   {
/*  264 */     if ((paramComponent instanceof JButton)) {
/*  265 */       ((JButton)paramComponent).addActionListener(paramArrowButtonHandler);
/*      */     }
/*  267 */     paramComponent.addMouseListener(paramArrowButtonHandler);
/*      */   }
/*      */ 
/*      */   protected LayoutManager createLayout()
/*      */   {
/*  284 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected PropertyChangeListener createPropertyChangeListener()
/*      */   {
/*  300 */     return getHandler();
/*      */   }
/*      */ 
/*      */   protected Component createPreviousButton()
/*      */   {
/*  317 */     Component localComponent = createArrowButton(5);
/*  318 */     localComponent.setName("Spinner.previousButton");
/*  319 */     installPreviousButtonListeners(localComponent);
/*  320 */     return localComponent;
/*      */   }
/*      */ 
/*      */   protected Component createNextButton()
/*      */   {
/*  337 */     Component localComponent = createArrowButton(1);
/*  338 */     localComponent.setName("Spinner.nextButton");
/*  339 */     installNextButtonListeners(localComponent);
/*  340 */     return localComponent;
/*      */   }
/*      */ 
/*      */   private Component createArrowButton(int paramInt) {
/*  344 */     BasicArrowButton localBasicArrowButton = new BasicArrowButton(paramInt);
/*  345 */     Border localBorder = UIManager.getBorder("Spinner.arrowButtonBorder");
/*  346 */     if ((localBorder instanceof UIResource))
/*      */     {
/*  349 */       localBasicArrowButton.setBorder(new CompoundBorder(localBorder, null));
/*      */     }
/*  351 */     else localBasicArrowButton.setBorder(localBorder);
/*      */ 
/*  353 */     localBasicArrowButton.setInheritsPopupMenu(true);
/*  354 */     return localBasicArrowButton;
/*      */   }
/*      */ 
/*      */   protected JComponent createEditor()
/*      */   {
/*  382 */     JComponent localJComponent = this.spinner.getEditor();
/*  383 */     maybeRemoveEditorBorder(localJComponent);
/*  384 */     installEditorBorderListener(localJComponent);
/*  385 */     localJComponent.setInheritsPopupMenu(true);
/*  386 */     updateEditorAlignment(localJComponent);
/*  387 */     return localJComponent;
/*      */   }
/*      */ 
/*      */   protected void replaceEditor(JComponent paramJComponent1, JComponent paramJComponent2)
/*      */   {
/*  407 */     this.spinner.remove(paramJComponent1);
/*  408 */     maybeRemoveEditorBorder(paramJComponent2);
/*  409 */     installEditorBorderListener(paramJComponent2);
/*  410 */     paramJComponent2.setInheritsPopupMenu(true);
/*  411 */     this.spinner.add(paramJComponent2, "Editor");
/*      */   }
/*      */ 
/*      */   private void updateEditorAlignment(JComponent paramJComponent) {
/*  415 */     if ((paramJComponent instanceof JSpinner.DefaultEditor))
/*      */     {
/*  417 */       int i = UIManager.getInt("Spinner.editorAlignment");
/*  418 */       JFormattedTextField localJFormattedTextField = ((JSpinner.DefaultEditor)paramJComponent).getTextField();
/*  419 */       localJFormattedTextField.setHorizontalAlignment(i);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void maybeRemoveEditorBorder(JComponent paramJComponent)
/*      */   {
/*  428 */     if (!UIManager.getBoolean("Spinner.editorBorderPainted")) {
/*  429 */       if (((paramJComponent instanceof JPanel)) && (paramJComponent.getBorder() == null) && (paramJComponent.getComponentCount() > 0))
/*      */       {
/*  433 */         paramJComponent = (JComponent)paramJComponent.getComponent(0);
/*      */       }
/*      */ 
/*  436 */       if ((paramJComponent != null) && ((paramJComponent.getBorder() instanceof UIResource)))
/*  437 */         paramJComponent.setBorder(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void installEditorBorderListener(JComponent paramJComponent)
/*      */   {
/*  447 */     if (!UIManager.getBoolean("Spinner.editorBorderPainted")) {
/*  448 */       if (((paramJComponent instanceof JPanel)) && (paramJComponent.getBorder() == null) && (paramJComponent.getComponentCount() > 0))
/*      */       {
/*  452 */         paramJComponent = (JComponent)paramJComponent.getComponent(0);
/*      */       }
/*  454 */       if ((paramJComponent != null) && ((paramJComponent.getBorder() == null) || ((paramJComponent.getBorder() instanceof UIResource))))
/*      */       {
/*  457 */         paramJComponent.addPropertyChangeListener(getHandler());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeEditorBorderListener(JComponent paramJComponent) {
/*  463 */     if (!UIManager.getBoolean("Spinner.editorBorderPainted")) {
/*  464 */       if (((paramJComponent instanceof JPanel)) && (paramJComponent.getComponentCount() > 0))
/*      */       {
/*  467 */         paramJComponent = (JComponent)paramJComponent.getComponent(0);
/*      */       }
/*  469 */       if (paramJComponent != null)
/*  470 */         paramJComponent.removePropertyChangeListener(getHandler());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateEnabledState()
/*      */   {
/*  481 */     updateEnabledState(this.spinner, this.spinner.isEnabled());
/*      */   }
/*      */ 
/*      */   private void updateEnabledState(Container paramContainer, boolean paramBoolean)
/*      */   {
/*  490 */     for (int i = paramContainer.getComponentCount() - 1; i >= 0; i--) {
/*  491 */       Component localComponent = paramContainer.getComponent(i);
/*      */ 
/*  493 */       if (DefaultLookup.getBoolean(this.spinner, this, "Spinner.disableOnBoundaryValues", false))
/*      */       {
/*  495 */         SpinnerModel localSpinnerModel = this.spinner.getModel();
/*  496 */         if ((localComponent.getName() == "Spinner.nextButton") && (localSpinnerModel.getNextValue() == null))
/*      */         {
/*  498 */           localComponent.setEnabled(false);
/*      */         }
/*  500 */         else if ((localComponent.getName() == "Spinner.previousButton") && (localSpinnerModel.getPreviousValue() == null))
/*      */         {
/*  502 */           localComponent.setEnabled(false);
/*      */         }
/*      */         else
/*  505 */           localComponent.setEnabled(paramBoolean);
/*      */       }
/*      */       else
/*      */       {
/*  509 */         localComponent.setEnabled(paramBoolean);
/*      */       }
/*  511 */       if ((localComponent instanceof Container))
/*  512 */         updateEnabledState((Container)localComponent, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void installKeyboardActions()
/*      */   {
/*  524 */     InputMap localInputMap = getInputMap(1);
/*      */ 
/*  527 */     SwingUtilities.replaceUIInputMap(this.spinner, 1, localInputMap);
/*      */ 
/*  531 */     LazyActionMap.installLazyActionMap(this.spinner, BasicSpinnerUI.class, "Spinner.actionMap");
/*      */   }
/*      */ 
/*      */   private InputMap getInputMap(int paramInt)
/*      */   {
/*  539 */     if (paramInt == 1) {
/*  540 */       return (InputMap)DefaultLookup.get(this.spinner, this, "Spinner.ancestorInputMap");
/*      */     }
/*      */ 
/*  543 */     return null;
/*      */   }
/*      */ 
/*      */   static void loadActionMap(LazyActionMap paramLazyActionMap) {
/*  547 */     paramLazyActionMap.put("increment", nextButtonHandler);
/*  548 */     paramLazyActionMap.put("decrement", previousButtonHandler);
/*      */   }
/*      */ 
/*      */   public int getBaseline(JComponent paramJComponent, int paramInt1, int paramInt2)
/*      */   {
/*  560 */     super.getBaseline(paramJComponent, paramInt1, paramInt2);
/*  561 */     JComponent localJComponent = this.spinner.getEditor();
/*  562 */     Insets localInsets = this.spinner.getInsets();
/*  563 */     paramInt1 = paramInt1 - localInsets.left - localInsets.right;
/*  564 */     paramInt2 = paramInt2 - localInsets.top - localInsets.bottom;
/*  565 */     if ((paramInt1 >= 0) && (paramInt2 >= 0)) {
/*  566 */       int i = localJComponent.getBaseline(paramInt1, paramInt2);
/*  567 */       if (i >= 0) {
/*  568 */         return localInsets.top + i;
/*      */       }
/*      */     }
/*  571 */     return -1;
/*      */   }
/*      */ 
/*      */   public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent paramJComponent)
/*      */   {
/*  584 */     super.getBaselineResizeBehavior(paramJComponent);
/*  585 */     return this.spinner.getEditor().getBaselineResizeBehavior();
/*      */   }
/*      */ 
/*      */   private static class ArrowButtonHandler extends AbstractAction
/*      */     implements FocusListener, MouseListener, UIResource
/*      */   {
/*      */     final Timer autoRepeatTimer;
/*      */     final boolean isNext;
/*  608 */     JSpinner spinner = null;
/*  609 */     JButton arrowButton = null;
/*      */ 
/*      */     ArrowButtonHandler(String paramString, boolean paramBoolean) {
/*  612 */       super();
/*  613 */       this.isNext = paramBoolean;
/*  614 */       this.autoRepeatTimer = new Timer(60, this);
/*  615 */       this.autoRepeatTimer.setInitialDelay(300);
/*      */     }
/*      */ 
/*      */     private JSpinner eventToSpinner(AWTEvent paramAWTEvent) {
/*  619 */       Object localObject = paramAWTEvent.getSource();
/*  620 */       while (((localObject instanceof Component)) && (!(localObject instanceof JSpinner))) {
/*  621 */         localObject = ((Component)localObject).getParent();
/*      */       }
/*  623 */       return (localObject instanceof JSpinner) ? (JSpinner)localObject : null;
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/*  627 */       JSpinner localJSpinner = this.spinner;
/*      */ 
/*  629 */       if (!(paramActionEvent.getSource() instanceof Timer))
/*      */       {
/*  631 */         localJSpinner = eventToSpinner(paramActionEvent);
/*  632 */         if ((paramActionEvent.getSource() instanceof JButton)) {
/*  633 */           this.arrowButton = ((JButton)paramActionEvent.getSource());
/*      */         }
/*      */       }
/*  636 */       else if ((this.arrowButton != null) && (!this.arrowButton.getModel().isPressed()) && (this.autoRepeatTimer.isRunning()))
/*      */       {
/*  638 */         this.autoRepeatTimer.stop();
/*  639 */         localJSpinner = null;
/*  640 */         this.arrowButton = null;
/*      */       }
/*      */ 
/*  643 */       if (localJSpinner != null)
/*      */         try {
/*  645 */           int i = getCalendarField(localJSpinner);
/*  646 */           localJSpinner.commitEdit();
/*  647 */           if (i != -1) {
/*  648 */             ((SpinnerDateModel)localJSpinner.getModel()).setCalendarField(i);
/*      */           }
/*      */ 
/*  651 */           Object localObject = this.isNext ? localJSpinner.getNextValue() : localJSpinner.getPreviousValue();
/*      */ 
/*  653 */           if (localObject != null) {
/*  654 */             localJSpinner.setValue(localObject);
/*  655 */             select(localJSpinner);
/*      */           }
/*      */         } catch (IllegalArgumentException localIllegalArgumentException) {
/*  658 */           UIManager.getLookAndFeel().provideErrorFeedback(localJSpinner);
/*      */         } catch (ParseException localParseException) {
/*  660 */           UIManager.getLookAndFeel().provideErrorFeedback(localJSpinner);
/*      */         }
/*      */     }
/*      */ 
/*      */     private void select(JSpinner paramJSpinner)
/*      */     {
/*  670 */       JComponent localJComponent = paramJSpinner.getEditor();
/*      */ 
/*  672 */       if ((localJComponent instanceof JSpinner.DateEditor)) {
/*  673 */         JSpinner.DateEditor localDateEditor = (JSpinner.DateEditor)localJComponent;
/*  674 */         JFormattedTextField localJFormattedTextField = localDateEditor.getTextField();
/*  675 */         SimpleDateFormat localSimpleDateFormat = localDateEditor.getFormat();
/*      */         Object localObject;
/*  678 */         if ((localSimpleDateFormat != null) && ((localObject = paramJSpinner.getValue()) != null)) {
/*  679 */           SpinnerDateModel localSpinnerDateModel = localDateEditor.getModel();
/*  680 */           DateFormat.Field localField = DateFormat.Field.ofCalendarField(localSpinnerDateModel.getCalendarField());
/*      */ 
/*  683 */           if (localField != null)
/*      */             try {
/*  685 */               AttributedCharacterIterator localAttributedCharacterIterator = localSimpleDateFormat.formatToCharacterIterator(localObject);
/*      */ 
/*  687 */               if ((!select(localJFormattedTextField, localAttributedCharacterIterator, localField)) && (localField == DateFormat.Field.HOUR0))
/*      */               {
/*  689 */                 select(localJFormattedTextField, localAttributedCharacterIterator, DateFormat.Field.HOUR1);
/*      */               }
/*      */             }
/*      */             catch (IllegalArgumentException localIllegalArgumentException)
/*      */             {
/*      */             }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private boolean select(JFormattedTextField paramJFormattedTextField, AttributedCharacterIterator paramAttributedCharacterIterator, DateFormat.Field paramField)
/*      */     {
/*  705 */       int i = paramJFormattedTextField.getDocument().getLength();
/*      */ 
/*  707 */       paramAttributedCharacterIterator.first();
/*      */       do {
/*  709 */         Map localMap = paramAttributedCharacterIterator.getAttributes();
/*      */ 
/*  711 */         if ((localMap != null) && (localMap.containsKey(paramField))) {
/*  712 */           int j = paramAttributedCharacterIterator.getRunStart(paramField);
/*  713 */           int k = paramAttributedCharacterIterator.getRunLimit(paramField);
/*      */ 
/*  715 */           if ((j != -1) && (k != -1) && (j <= i) && (k <= i))
/*      */           {
/*  717 */             paramJFormattedTextField.select(j, k);
/*      */           }
/*  719 */           return true;
/*      */         }
/*      */       }
/*  721 */       while (paramAttributedCharacterIterator.next() != 65535);
/*  722 */       return false;
/*      */     }
/*      */ 
/*      */     private int getCalendarField(JSpinner paramJSpinner)
/*      */     {
/*  731 */       JComponent localJComponent = paramJSpinner.getEditor();
/*      */ 
/*  733 */       if ((localJComponent instanceof JSpinner.DateEditor)) {
/*  734 */         JSpinner.DateEditor localDateEditor = (JSpinner.DateEditor)localJComponent;
/*  735 */         JFormattedTextField localJFormattedTextField = localDateEditor.getTextField();
/*  736 */         int i = localJFormattedTextField.getSelectionStart();
/*  737 */         JFormattedTextField.AbstractFormatter localAbstractFormatter = localJFormattedTextField.getFormatter();
/*      */ 
/*  740 */         if ((localAbstractFormatter instanceof InternationalFormatter)) {
/*  741 */           Format.Field[] arrayOfField = ((InternationalFormatter)localAbstractFormatter).getFields(i);
/*      */ 
/*  744 */           for (int j = 0; j < arrayOfField.length; j++) {
/*  745 */             if ((arrayOfField[j] instanceof DateFormat.Field))
/*      */             {
/*      */               int k;
/*  748 */               if (arrayOfField[j] == DateFormat.Field.HOUR1) {
/*  749 */                 k = 10;
/*      */               }
/*      */               else {
/*  752 */                 k = ((DateFormat.Field)arrayOfField[j]).getCalendarField();
/*      */               }
/*      */ 
/*  755 */               if (k != -1) {
/*  756 */                 return k;
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*  762 */       return -1;
/*      */     }
/*      */ 
/*      */     public void mousePressed(MouseEvent paramMouseEvent) {
/*  766 */       if ((SwingUtilities.isLeftMouseButton(paramMouseEvent)) && (paramMouseEvent.getComponent().isEnabled())) {
/*  767 */         this.spinner = eventToSpinner(paramMouseEvent);
/*  768 */         this.autoRepeatTimer.start();
/*      */ 
/*  770 */         focusSpinnerIfNecessary();
/*      */       }
/*      */     }
/*      */ 
/*      */     public void mouseReleased(MouseEvent paramMouseEvent) {
/*  775 */       this.autoRepeatTimer.stop();
/*  776 */       this.arrowButton = null;
/*  777 */       this.spinner = null;
/*      */     }
/*      */ 
/*      */     public void mouseClicked(MouseEvent paramMouseEvent) {
/*      */     }
/*      */ 
/*      */     public void mouseEntered(MouseEvent paramMouseEvent) {
/*  784 */       if ((this.spinner != null) && (!this.autoRepeatTimer.isRunning()) && (this.spinner == eventToSpinner(paramMouseEvent)))
/*  785 */         this.autoRepeatTimer.start();
/*      */     }
/*      */ 
/*      */     public void mouseExited(MouseEvent paramMouseEvent)
/*      */     {
/*  790 */       if (this.autoRepeatTimer.isRunning())
/*  791 */         this.autoRepeatTimer.stop();
/*      */     }
/*      */ 
/*      */     private void focusSpinnerIfNecessary()
/*      */     {
/*  800 */       Component localComponent1 = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
/*      */ 
/*  802 */       if ((this.spinner.isRequestFocusEnabled()) && ((localComponent1 == null) || (!SwingUtilities.isDescendingFrom(localComponent1, this.spinner))))
/*      */       {
/*  805 */         Object localObject = this.spinner;
/*      */ 
/*  807 */         if (!((Container)localObject).isFocusCycleRoot()) {
/*  808 */           localObject = ((Container)localObject).getFocusCycleRootAncestor();
/*      */         }
/*  810 */         if (localObject != null) {
/*  811 */           FocusTraversalPolicy localFocusTraversalPolicy = ((Container)localObject).getFocusTraversalPolicy();
/*  812 */           Component localComponent2 = localFocusTraversalPolicy.getComponentAfter((Container)localObject, this.spinner);
/*      */ 
/*  814 */           if ((localComponent2 != null) && (SwingUtilities.isDescendingFrom(localComponent2, this.spinner)))
/*      */           {
/*  816 */             localComponent2.requestFocus();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void focusGained(FocusEvent paramFocusEvent) {
/*      */     }
/*      */ 
/*      */     public void focusLost(FocusEvent paramFocusEvent) {
/*  826 */       if (this.spinner == eventToSpinner(paramFocusEvent)) {
/*  827 */         if (this.autoRepeatTimer.isRunning()) {
/*  828 */           this.autoRepeatTimer.stop();
/*      */         }
/*  830 */         this.spinner = null;
/*  831 */         if (this.arrowButton != null) {
/*  832 */           ButtonModel localButtonModel = this.arrowButton.getModel();
/*  833 */           localButtonModel.setPressed(false);
/*  834 */           localButtonModel.setArmed(false);
/*  835 */           this.arrowButton = null;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Handler
/*      */     implements LayoutManager, PropertyChangeListener, ChangeListener
/*      */   {
/*  847 */     private Component nextButton = null;
/*  848 */     private Component previousButton = null;
/*  849 */     private Component editor = null;
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent) {
/*  852 */       if ("Next".equals(paramString)) {
/*  853 */         this.nextButton = paramComponent;
/*      */       }
/*  855 */       else if ("Previous".equals(paramString)) {
/*  856 */         this.previousButton = paramComponent;
/*      */       }
/*  858 */       else if ("Editor".equals(paramString))
/*  859 */         this.editor = paramComponent;
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/*  864 */       if (paramComponent == this.nextButton) {
/*  865 */         this.nextButton = null;
/*      */       }
/*  867 */       else if (paramComponent == this.previousButton) {
/*  868 */         this.previousButton = null;
/*      */       }
/*  870 */       else if (paramComponent == this.editor)
/*  871 */         this.editor = null;
/*      */     }
/*      */ 
/*      */     private Dimension preferredSize(Component paramComponent)
/*      */     {
/*  876 */       return paramComponent == null ? BasicSpinnerUI.zeroSize : paramComponent.getPreferredSize();
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer) {
/*  880 */       Dimension localDimension1 = preferredSize(this.nextButton);
/*  881 */       Dimension localDimension2 = preferredSize(this.previousButton);
/*  882 */       Dimension localDimension3 = preferredSize(this.editor);
/*      */ 
/*  886 */       localDimension3.height = ((localDimension3.height + 1) / 2 * 2);
/*      */ 
/*  888 */       Dimension localDimension4 = new Dimension(localDimension3.width, localDimension3.height);
/*  889 */       localDimension4.width += Math.max(localDimension1.width, localDimension2.width);
/*  890 */       Insets localInsets = paramContainer.getInsets();
/*  891 */       localDimension4.width += localInsets.left + localInsets.right;
/*  892 */       localDimension4.height += localInsets.top + localInsets.bottom;
/*  893 */       return localDimension4;
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer) {
/*  897 */       return preferredLayoutSize(paramContainer);
/*      */     }
/*      */ 
/*      */     private void setBounds(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  901 */       if (paramComponent != null)
/*  902 */         paramComponent.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer)
/*      */     {
/*  907 */       int i = paramContainer.getWidth();
/*  908 */       int j = paramContainer.getHeight();
/*      */ 
/*  910 */       Insets localInsets1 = paramContainer.getInsets();
/*      */ 
/*  912 */       if ((this.nextButton == null) && (this.previousButton == null)) {
/*  913 */         setBounds(this.editor, localInsets1.left, localInsets1.top, i - localInsets1.left - localInsets1.right, j - localInsets1.top - localInsets1.bottom);
/*      */ 
/*  916 */         return;
/*      */       }
/*      */ 
/*  919 */       Dimension localDimension1 = preferredSize(this.nextButton);
/*  920 */       Dimension localDimension2 = preferredSize(this.previousButton);
/*  921 */       int k = Math.max(localDimension1.width, localDimension2.width);
/*  922 */       int m = j - (localInsets1.top + localInsets1.bottom);
/*      */ 
/*  929 */       Insets localInsets2 = UIManager.getInsets("Spinner.arrowButtonInsets");
/*  930 */       if (localInsets2 == null)
/*  931 */         localInsets2 = localInsets1;
/*      */       int n;
/*      */       int i1;
/*      */       int i2;
/*  937 */       if (paramContainer.getComponentOrientation().isLeftToRight()) {
/*  938 */         n = localInsets1.left;
/*  939 */         i1 = i - localInsets1.left - k - localInsets2.right;
/*  940 */         i2 = i - k - localInsets2.right;
/*      */       } else {
/*  942 */         i2 = localInsets2.left;
/*  943 */         n = i2 + k;
/*  944 */         i1 = i - localInsets2.left - k - localInsets1.right;
/*      */       }
/*      */ 
/*  947 */       int i3 = localInsets2.top;
/*  948 */       int i4 = j / 2 + j % 2 - i3;
/*  949 */       int i5 = localInsets2.top + i4;
/*  950 */       int i6 = j - i5 - localInsets2.bottom;
/*      */ 
/*  952 */       setBounds(this.editor, n, localInsets1.top, i1, m);
/*  953 */       setBounds(this.nextButton, i2, i3, k, i4);
/*  954 */       setBounds(this.previousButton, i2, i5, k, i6);
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  963 */       String str = paramPropertyChangeEvent.getPropertyName();
/*      */       Object localObject1;
/*      */       Object localObject2;
/*      */       Object localObject3;
/*      */       Object localObject4;
/*  964 */       if ((paramPropertyChangeEvent.getSource() instanceof JSpinner)) {
/*  965 */         localObject1 = (JSpinner)paramPropertyChangeEvent.getSource();
/*  966 */         localObject2 = ((JSpinner)localObject1).getUI();
/*      */ 
/*  968 */         if ((localObject2 instanceof BasicSpinnerUI)) {
/*  969 */           localObject3 = (BasicSpinnerUI)localObject2;
/*      */           Object localObject5;
/*  971 */           if ("editor".equals(str)) {
/*  972 */             localObject4 = (JComponent)paramPropertyChangeEvent.getOldValue();
/*  973 */             localObject5 = (JComponent)paramPropertyChangeEvent.getNewValue();
/*  974 */             ((BasicSpinnerUI)localObject3).replaceEditor((JComponent)localObject4, (JComponent)localObject5);
/*  975 */             ((BasicSpinnerUI)localObject3).updateEnabledState();
/*      */             JFormattedTextField localJFormattedTextField;
/*  976 */             if ((localObject4 instanceof JSpinner.DefaultEditor)) {
/*  977 */               localJFormattedTextField = ((JSpinner.DefaultEditor)localObject4).getTextField();
/*      */ 
/*  979 */               if (localJFormattedTextField != null) {
/*  980 */                 localJFormattedTextField.removeFocusListener(BasicSpinnerUI.nextButtonHandler);
/*  981 */                 localJFormattedTextField.removeFocusListener(BasicSpinnerUI.previousButtonHandler);
/*      */               }
/*      */             }
/*  984 */             if ((localObject5 instanceof JSpinner.DefaultEditor)) {
/*  985 */               localJFormattedTextField = ((JSpinner.DefaultEditor)localObject5).getTextField();
/*      */ 
/*  987 */               if (localJFormattedTextField != null) {
/*  988 */                 if ((localJFormattedTextField.getFont() instanceof UIResource)) {
/*  989 */                   localJFormattedTextField.setFont(((JSpinner)localObject1).getFont());
/*      */                 }
/*  991 */                 localJFormattedTextField.addFocusListener(BasicSpinnerUI.nextButtonHandler);
/*  992 */                 localJFormattedTextField.addFocusListener(BasicSpinnerUI.previousButtonHandler);
/*      */               }
/*      */             }
/*      */           }
/*  996 */           else if (("enabled".equals(str)) || ("model".equals(str)))
/*      */           {
/*  998 */             ((BasicSpinnerUI)localObject3).updateEnabledState();
/*      */           }
/* 1000 */           else if ("font".equals(str)) {
/* 1001 */             localObject4 = ((JSpinner)localObject1).getEditor();
/* 1002 */             if ((localObject4 != null) && ((localObject4 instanceof JSpinner.DefaultEditor))) {
/* 1003 */               localObject5 = ((JSpinner.DefaultEditor)localObject4).getTextField();
/*      */ 
/* 1005 */               if ((localObject5 != null) && 
/* 1006 */                 ((((JTextField)localObject5).getFont() instanceof UIResource))) {
/* 1007 */                 ((JTextField)localObject5).setFont(((JSpinner)localObject1).getFont());
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/* 1012 */           else if ("ToolTipText".equals(str)) {
/* 1013 */             updateToolTipTextForChildren((JComponent)localObject1);
/*      */           }
/*      */         }
/* 1016 */       } else if ((paramPropertyChangeEvent.getSource() instanceof JComponent)) {
/* 1017 */         localObject1 = (JComponent)paramPropertyChangeEvent.getSource();
/* 1018 */         if (((((JComponent)localObject1).getParent() instanceof JPanel)) && ((((JComponent)localObject1).getParent().getParent() instanceof JSpinner)) && ("border".equals(str)))
/*      */         {
/* 1022 */           localObject2 = (JSpinner)((JComponent)localObject1).getParent().getParent();
/* 1023 */           localObject3 = ((JSpinner)localObject2).getUI();
/* 1024 */           if ((localObject3 instanceof BasicSpinnerUI)) {
/* 1025 */             localObject4 = (BasicSpinnerUI)localObject3;
/* 1026 */             ((BasicSpinnerUI)localObject4).maybeRemoveEditorBorder((JComponent)localObject1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void updateToolTipTextForChildren(JComponent paramJComponent)
/*      */     {
/* 1035 */       String str = paramJComponent.getToolTipText();
/* 1036 */       Component[] arrayOfComponent = paramJComponent.getComponents();
/* 1037 */       for (int i = 0; i < arrayOfComponent.length; i++)
/* 1038 */         if ((arrayOfComponent[i] instanceof JSpinner.DefaultEditor)) {
/* 1039 */           JFormattedTextField localJFormattedTextField = ((JSpinner.DefaultEditor)arrayOfComponent[i]).getTextField();
/* 1040 */           if (localJFormattedTextField != null)
/* 1041 */             localJFormattedTextField.setToolTipText(str);
/*      */         }
/* 1043 */         else if ((arrayOfComponent[i] instanceof JComponent)) {
/* 1044 */           ((JComponent)arrayOfComponent[i]).setToolTipText(paramJComponent.getToolTipText());
/*      */         }
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1050 */       if ((paramChangeEvent.getSource() instanceof JSpinner)) {
/* 1051 */         JSpinner localJSpinner = (JSpinner)paramChangeEvent.getSource();
/* 1052 */         SpinnerUI localSpinnerUI = localJSpinner.getUI();
/* 1053 */         if ((DefaultLookup.getBoolean(localJSpinner, localSpinnerUI, "Spinner.disableOnBoundaryValues", false)) && ((localSpinnerUI instanceof BasicSpinnerUI)))
/*      */         {
/* 1056 */           BasicSpinnerUI localBasicSpinnerUI = (BasicSpinnerUI)localSpinnerUI;
/* 1057 */           localBasicSpinnerUI.updateEnabledState();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.basic.BasicSpinnerUI
 * JD-Core Version:    0.6.2
 */