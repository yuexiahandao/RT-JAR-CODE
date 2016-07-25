/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.InputEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.JTextComponent.AccessibleJTextComponent;
/*     */ import javax.swing.text.PlainDocument;
/*     */ import javax.swing.text.TextAction;
/*     */ 
/*     */ public class JTextField extends JTextComponent
/*     */   implements SwingConstants
/*     */ {
/*     */   private Action action;
/*     */   private PropertyChangeListener actionPropertyChangeListener;
/*     */   public static final String notifyAction = "notify-field-accept";
/*     */   private BoundedRangeModel visibility;
/* 808 */   private int horizontalAlignment = 10;
/*     */   private int columns;
/*     */   private int columnWidth;
/*     */   private String command;
/* 813 */   private static final Action[] defaultActions = { new NotifyAction() };
/*     */   private static final String uiClassID = "TextFieldUI";
/*     */ 
/*     */   public JTextField()
/*     */   {
/* 172 */     this(null, null, 0);
/*     */   }
/*     */ 
/*     */   public JTextField(String paramString)
/*     */   {
/* 183 */     this(null, paramString, 0);
/*     */   }
/*     */ 
/*     */   public JTextField(int paramInt)
/*     */   {
/* 198 */     this(null, null, paramInt);
/*     */   }
/*     */ 
/*     */   public JTextField(String paramString, int paramInt)
/*     */   {
/* 212 */     this(null, paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public JTextField(Document paramDocument, String paramString, int paramInt)
/*     */   {
/* 232 */     if (paramInt < 0) {
/* 233 */       throw new IllegalArgumentException("columns less than zero.");
/*     */     }
/* 235 */     this.visibility = new DefaultBoundedRangeModel();
/* 236 */     this.visibility.addChangeListener(new ScrollRepainter());
/* 237 */     this.columns = paramInt;
/* 238 */     if (paramDocument == null) {
/* 239 */       paramDocument = createDefaultModel();
/*     */     }
/* 241 */     setDocument(paramDocument);
/* 242 */     if (paramString != null)
/* 243 */       setText(paramString);
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 255 */     return "TextFieldUI";
/*     */   }
/*     */ 
/*     */   public void setDocument(Document paramDocument)
/*     */   {
/* 273 */     if (paramDocument != null) {
/* 274 */       paramDocument.putProperty("filterNewlines", Boolean.TRUE);
/*     */     }
/* 276 */     super.setDocument(paramDocument);
/*     */   }
/*     */ 
/*     */   public boolean isValidateRoot()
/*     */   {
/* 295 */     return !(SwingUtilities.getUnwrappedParent(this) instanceof JViewport);
/*     */   }
/*     */ 
/*     */   public int getHorizontalAlignment()
/*     */   {
/* 313 */     return this.horizontalAlignment;
/*     */   }
/*     */ 
/*     */   public void setHorizontalAlignment(int paramInt)
/*     */   {
/* 342 */     if (paramInt == this.horizontalAlignment) return;
/* 343 */     int i = this.horizontalAlignment;
/* 344 */     if ((paramInt == 2) || (paramInt == 0) || (paramInt == 4) || (paramInt == 10) || (paramInt == 11))
/*     */     {
/* 347 */       this.horizontalAlignment = paramInt;
/*     */     }
/* 349 */     else throw new IllegalArgumentException("horizontalAlignment");
/*     */ 
/* 351 */     firePropertyChange("horizontalAlignment", i, this.horizontalAlignment);
/* 352 */     invalidate();
/* 353 */     repaint();
/*     */   }
/*     */ 
/*     */   protected Document createDefaultModel()
/*     */   {
/* 364 */     return new PlainDocument();
/*     */   }
/*     */ 
/*     */   public int getColumns()
/*     */   {
/* 373 */     return this.columns;
/*     */   }
/*     */ 
/*     */   public void setColumns(int paramInt)
/*     */   {
/* 387 */     int i = this.columns;
/* 388 */     if (paramInt < 0) {
/* 389 */       throw new IllegalArgumentException("columns less than zero.");
/*     */     }
/* 391 */     if (paramInt != i) {
/* 392 */       this.columns = paramInt;
/* 393 */       invalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected int getColumnWidth()
/*     */   {
/* 408 */     if (this.columnWidth == 0) {
/* 409 */       FontMetrics localFontMetrics = getFontMetrics(getFont());
/* 410 */       this.columnWidth = localFontMetrics.charWidth('m');
/*     */     }
/* 412 */     return this.columnWidth;
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize()
/*     */   {
/* 424 */     Dimension localDimension = super.getPreferredSize();
/* 425 */     if (this.columns != 0) {
/* 426 */       Insets localInsets = getInsets();
/* 427 */       localDimension.width = (this.columns * getColumnWidth() + localInsets.left + localInsets.right);
/*     */     }
/*     */ 
/* 430 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void setFont(Font paramFont)
/*     */   {
/* 441 */     super.setFont(paramFont);
/* 442 */     this.columnWidth = 0;
/*     */   }
/*     */ 
/*     */   public synchronized void addActionListener(ActionListener paramActionListener)
/*     */   {
/* 452 */     this.listenerList.add(ActionListener.class, paramActionListener);
/*     */   }
/*     */ 
/*     */   public synchronized void removeActionListener(ActionListener paramActionListener)
/*     */   {
/* 462 */     if ((paramActionListener != null) && (getAction() == paramActionListener))
/* 463 */       setAction(null);
/*     */     else
/* 465 */       this.listenerList.remove(ActionListener.class, paramActionListener);
/*     */   }
/*     */ 
/*     */   public synchronized ActionListener[] getActionListeners()
/*     */   {
/* 478 */     return (ActionListener[])this.listenerList.getListeners(ActionListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireActionPerformed()
/*     */   {
/* 491 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 492 */     int i = 0;
/* 493 */     AWTEvent localAWTEvent = EventQueue.getCurrentEvent();
/* 494 */     if ((localAWTEvent instanceof InputEvent))
/* 495 */       i = ((InputEvent)localAWTEvent).getModifiers();
/* 496 */     else if ((localAWTEvent instanceof ActionEvent)) {
/* 497 */       i = ((ActionEvent)localAWTEvent).getModifiers();
/*     */     }
/* 499 */     ActionEvent localActionEvent = new ActionEvent(this, 1001, this.command != null ? this.command : getText(), EventQueue.getMostRecentEventTime(), i);
/*     */ 
/* 506 */     for (int j = arrayOfObject.length - 2; j >= 0; j -= 2)
/* 507 */       if (arrayOfObject[j] == ActionListener.class)
/* 508 */         ((ActionListener)arrayOfObject[(j + 1)]).actionPerformed(localActionEvent);
/*     */   }
/*     */ 
/*     */   public void setActionCommand(String paramString)
/*     */   {
/* 519 */     this.command = paramString;
/*     */   }
/*     */ 
/*     */   public void setAction(Action paramAction)
/*     */   {
/* 566 */     Action localAction = getAction();
/* 567 */     if ((this.action == null) || (!this.action.equals(paramAction))) {
/* 568 */       this.action = paramAction;
/* 569 */       if (localAction != null) {
/* 570 */         removeActionListener(localAction);
/* 571 */         localAction.removePropertyChangeListener(this.actionPropertyChangeListener);
/* 572 */         this.actionPropertyChangeListener = null;
/*     */       }
/* 574 */       configurePropertiesFromAction(this.action);
/* 575 */       if (this.action != null)
/*     */       {
/* 577 */         if (!isListener(ActionListener.class, this.action)) {
/* 578 */           addActionListener(this.action);
/*     */         }
/*     */ 
/* 581 */         this.actionPropertyChangeListener = createActionPropertyChangeListener(this.action);
/* 582 */         this.action.addPropertyChangeListener(this.actionPropertyChangeListener);
/*     */       }
/* 584 */       firePropertyChange("action", localAction, this.action);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isListener(Class paramClass, ActionListener paramActionListener) {
/* 589 */     boolean bool = false;
/* 590 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 591 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2) {
/* 592 */       if ((arrayOfObject[i] == paramClass) && (arrayOfObject[(i + 1)] == paramActionListener)) {
/* 593 */         bool = true;
/*     */       }
/*     */     }
/* 596 */     return bool;
/*     */   }
/*     */ 
/*     */   public Action getAction()
/*     */   {
/* 611 */     return this.action;
/*     */   }
/*     */ 
/*     */   protected void configurePropertiesFromAction(Action paramAction)
/*     */   {
/* 627 */     AbstractAction.setEnabledFromAction(this, paramAction);
/* 628 */     AbstractAction.setToolTipTextFromAction(this, paramAction);
/* 629 */     setActionCommandFromAction(paramAction);
/*     */   }
/*     */ 
/*     */   protected void actionPropertyChanged(Action paramAction, String paramString)
/*     */   {
/* 652 */     if (paramString == "ActionCommandKey")
/* 653 */       setActionCommandFromAction(paramAction);
/* 654 */     else if (paramString == "enabled")
/* 655 */       AbstractAction.setEnabledFromAction(this, paramAction);
/* 656 */     else if (paramString == "ShortDescription")
/* 657 */       AbstractAction.setToolTipTextFromAction(this, paramAction);
/*     */   }
/*     */ 
/*     */   private void setActionCommandFromAction(Action paramAction)
/*     */   {
/* 662 */     setActionCommand(paramAction == null ? null : (String)paramAction.getValue("ActionCommandKey"));
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createActionPropertyChangeListener(Action paramAction)
/*     */   {
/* 681 */     return new TextFieldActionPropertyChangeListener(this, paramAction);
/*     */   }
/*     */ 
/*     */   public Action[] getActions()
/*     */   {
/* 711 */     return TextAction.augmentList(super.getActions(), defaultActions);
/*     */   }
/*     */ 
/*     */   public void postActionEvent()
/*     */   {
/* 721 */     fireActionPerformed();
/*     */   }
/*     */ 
/*     */   public BoundedRangeModel getHorizontalVisibility()
/*     */   {
/* 741 */     return this.visibility;
/*     */   }
/*     */ 
/*     */   public int getScrollOffset()
/*     */   {
/* 750 */     return this.visibility.getValue();
/*     */   }
/*     */ 
/*     */   public void setScrollOffset(int paramInt)
/*     */   {
/* 759 */     this.visibility.setValue(paramInt);
/*     */   }
/*     */ 
/*     */   public void scrollRectToVisible(Rectangle paramRectangle)
/*     */   {
/* 769 */     Insets localInsets = getInsets();
/* 770 */     int i = paramRectangle.x + this.visibility.getValue() - localInsets.left;
/* 771 */     int j = i + paramRectangle.width;
/* 772 */     if (i < this.visibility.getValue())
/*     */     {
/* 774 */       this.visibility.setValue(i);
/* 775 */     } else if (j > this.visibility.getValue() + this.visibility.getExtent())
/*     */     {
/* 777 */       this.visibility.setValue(j - this.visibility.getExtent());
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean hasActionListener()
/*     */   {
/* 787 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 790 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2) {
/* 791 */       if (arrayOfObject[i] == ActionListener.class) {
/* 792 */         return true;
/*     */       }
/*     */     }
/* 795 */     return false;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 864 */     paramObjectOutputStream.defaultWriteObject();
/* 865 */     if (getUIClassID().equals("TextFieldUI")) {
/* 866 */       byte b = JComponent.getWriteObjCounter(this);
/* 867 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 868 */       if ((b == 0) && (this.ui != null))
/* 869 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/*     */     String str1;
/* 886 */     if (this.horizontalAlignment == 2)
/* 887 */       str1 = "LEFT";
/* 888 */     else if (this.horizontalAlignment == 0)
/* 889 */       str1 = "CENTER";
/* 890 */     else if (this.horizontalAlignment == 4)
/* 891 */       str1 = "RIGHT";
/* 892 */     else if (this.horizontalAlignment == 10)
/* 893 */       str1 = "LEADING";
/* 894 */     else if (this.horizontalAlignment == 11)
/* 895 */       str1 = "TRAILING";
/* 896 */     else str1 = "";
/* 897 */     String str2 = this.command != null ? this.command : "";
/*     */ 
/* 900 */     return super.paramString() + ",columns=" + this.columns + ",columnWidth=" + this.columnWidth + ",command=" + str2 + ",horizontalAlignment=" + str1;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 925 */     if (this.accessibleContext == null) {
/* 926 */       this.accessibleContext = new AccessibleJTextField();
/*     */     }
/* 928 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJTextField extends JTextComponent.AccessibleJTextComponent
/*     */   {
/*     */     protected AccessibleJTextField()
/*     */     {
/* 946 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 956 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 957 */       localAccessibleStateSet.add(AccessibleState.SINGLE_LINE);
/* 958 */       return localAccessibleStateSet;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class NotifyAction extends TextAction
/*     */   {
/*     */     NotifyAction()
/*     */     {
/* 829 */       super();
/*     */     }
/*     */ 
/*     */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 833 */       JTextComponent localJTextComponent = getFocusedComponent();
/* 834 */       if ((localJTextComponent instanceof JTextField)) {
/* 835 */         JTextField localJTextField = (JTextField)localJTextComponent;
/* 836 */         localJTextField.postActionEvent();
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isEnabled() {
/* 841 */       JTextComponent localJTextComponent = getFocusedComponent();
/* 842 */       if ((localJTextComponent instanceof JTextField)) {
/* 843 */         return ((JTextField)localJTextComponent).hasActionListener();
/*     */       }
/* 845 */       return false;
/*     */     }
/*     */   }
/*     */   class ScrollRepainter implements ChangeListener, Serializable {
/*     */     ScrollRepainter() {
/*     */     }
/*     */ 
/* 852 */     public void stateChanged(ChangeEvent paramChangeEvent) { JTextField.this.repaint(); }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class TextFieldActionPropertyChangeListener extends ActionPropertyChangeListener<JTextField>
/*     */   {
/*     */     TextFieldActionPropertyChangeListener(JTextField paramJTextField, Action paramAction)
/*     */     {
/* 687 */       super(paramAction);
/*     */     }
/*     */ 
/*     */     protected void actionPropertyChanged(JTextField paramJTextField, Action paramAction, PropertyChangeEvent paramPropertyChangeEvent)
/*     */     {
/* 693 */       if (AbstractAction.shouldReconfigure(paramPropertyChangeEvent))
/* 694 */         paramJTextField.configurePropertiesFromAction(paramAction);
/*     */       else
/* 696 */         paramJTextField.actionPropertyChanged(paramAction, paramPropertyChangeEvent.getPropertyName());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JTextField
 * JD-Core Version:    0.6.2
 */