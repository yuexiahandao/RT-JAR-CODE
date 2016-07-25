/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.InputMethodEvent;
/*      */ import java.awt.im.InputContext;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.text.DateFormat;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.Format;
/*      */ import java.text.NumberFormat;
/*      */ import java.text.ParseException;
/*      */ import java.util.Date;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.DateFormatter;
/*      */ import javax.swing.text.DefaultFormatter;
/*      */ import javax.swing.text.DefaultFormatterFactory;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.DocumentFilter;
/*      */ import javax.swing.text.InternationalFormatter;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.NavigationFilter;
/*      */ import javax.swing.text.NumberFormatter;
/*      */ import javax.swing.text.TextAction;
/*      */ 
/*      */ public class JFormattedTextField extends JTextField
/*      */ {
/*      */   private static final String uiClassID = "FormattedTextFieldUI";
/*  182 */   private static final Action[] defaultActions = { new CommitAction(), new CancelAction() };
/*      */   public static final int COMMIT = 0;
/*      */   public static final int COMMIT_OR_REVERT = 1;
/*      */   public static final int REVERT = 2;
/*      */   public static final int PERSIST = 3;
/*      */   private AbstractFormatterFactory factory;
/*      */   private AbstractFormatter format;
/*      */   private Object value;
/*      */   private boolean editValid;
/*      */   private int focusLostBehavior;
/*      */   private boolean edited;
/*      */   private DocumentListener documentListener;
/*      */   private Object mask;
/*      */   private ActionMap textFormatterActionMap;
/*  262 */   private boolean composedTextExists = false;
/*      */   private FocusLostHandler focusLostHandler;
/*      */ 
/*      */   public JFormattedTextField()
/*      */   {
/*  278 */     enableEvents(4L);
/*  279 */     setFocusLostBehavior(1);
/*      */   }
/*      */ 
/*      */   public JFormattedTextField(Object paramObject)
/*      */   {
/*  290 */     this();
/*  291 */     setValue(paramObject);
/*      */   }
/*      */ 
/*      */   public JFormattedTextField(Format paramFormat)
/*      */   {
/*  302 */     this();
/*  303 */     setFormatterFactory(getDefaultFormatterFactory(paramFormat));
/*      */   }
/*      */ 
/*      */   public JFormattedTextField(AbstractFormatter paramAbstractFormatter)
/*      */   {
/*  314 */     this(new DefaultFormatterFactory(paramAbstractFormatter));
/*      */   }
/*      */ 
/*      */   public JFormattedTextField(AbstractFormatterFactory paramAbstractFormatterFactory)
/*      */   {
/*  324 */     this();
/*  325 */     setFormatterFactory(paramAbstractFormatterFactory);
/*      */   }
/*      */ 
/*      */   public JFormattedTextField(AbstractFormatterFactory paramAbstractFormatterFactory, Object paramObject)
/*      */   {
/*  338 */     this(paramObject);
/*  339 */     setFormatterFactory(paramAbstractFormatterFactory);
/*      */   }
/*      */ 
/*      */   public void setFocusLostBehavior(int paramInt)
/*      */   {
/*  368 */     if ((paramInt != 0) && (paramInt != 1) && (paramInt != 3) && (paramInt != 2))
/*      */     {
/*  370 */       throw new IllegalArgumentException("setFocusLostBehavior must be one of: JFormattedTextField.COMMIT, JFormattedTextField.COMMIT_OR_REVERT, JFormattedTextField.PERSIST or JFormattedTextField.REVERT");
/*      */     }
/*  372 */     this.focusLostBehavior = paramInt;
/*      */   }
/*      */ 
/*      */   public int getFocusLostBehavior()
/*      */   {
/*  387 */     return this.focusLostBehavior;
/*      */   }
/*      */ 
/*      */   public void setFormatterFactory(AbstractFormatterFactory paramAbstractFormatterFactory)
/*      */   {
/*  417 */     AbstractFormatterFactory localAbstractFormatterFactory = this.factory;
/*      */ 
/*  419 */     this.factory = paramAbstractFormatterFactory;
/*  420 */     firePropertyChange("formatterFactory", localAbstractFormatterFactory, paramAbstractFormatterFactory);
/*  421 */     setValue(getValue(), true, false);
/*      */   }
/*      */ 
/*      */   public AbstractFormatterFactory getFormatterFactory()
/*      */   {
/*  432 */     return this.factory;
/*      */   }
/*      */ 
/*      */   protected void setFormatter(AbstractFormatter paramAbstractFormatter)
/*      */   {
/*  457 */     AbstractFormatter localAbstractFormatter = this.format;
/*      */ 
/*  459 */     if (localAbstractFormatter != null) {
/*  460 */       localAbstractFormatter.uninstall();
/*      */     }
/*  462 */     setEditValid(true);
/*  463 */     this.format = paramAbstractFormatter;
/*  464 */     if (paramAbstractFormatter != null) {
/*  465 */       paramAbstractFormatter.install(this);
/*      */     }
/*  467 */     setEdited(false);
/*  468 */     firePropertyChange("textFormatter", localAbstractFormatter, paramAbstractFormatter);
/*      */   }
/*      */ 
/*      */   public AbstractFormatter getFormatter()
/*      */   {
/*  478 */     return this.format;
/*      */   }
/*      */ 
/*      */   public void setValue(Object paramObject)
/*      */   {
/*  499 */     if ((paramObject != null) && (getFormatterFactory() == null)) {
/*  500 */       setFormatterFactory(getDefaultFormatterFactory(paramObject));
/*      */     }
/*  502 */     setValue(paramObject, true, true);
/*      */   }
/*      */ 
/*      */   public Object getValue()
/*      */   {
/*  514 */     return this.value;
/*      */   }
/*      */ 
/*      */   public void commitEdit()
/*      */     throws ParseException
/*      */   {
/*  527 */     AbstractFormatter localAbstractFormatter = getFormatter();
/*      */ 
/*  529 */     if (localAbstractFormatter != null)
/*  530 */       setValue(localAbstractFormatter.stringToValue(getText()), false, true);
/*      */   }
/*      */ 
/*      */   private void setEditValid(boolean paramBoolean)
/*      */   {
/*  553 */     if (paramBoolean != this.editValid) {
/*  554 */       this.editValid = paramBoolean;
/*  555 */       firePropertyChange("editValid", Boolean.valueOf(!paramBoolean), Boolean.valueOf(paramBoolean));
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isEditValid()
/*      */   {
/*  568 */     return this.editValid;
/*      */   }
/*      */ 
/*      */   protected void invalidEdit()
/*      */   {
/*  577 */     UIManager.getLookAndFeel().provideErrorFeedback(this);
/*      */   }
/*      */ 
/*      */   protected void processInputMethodEvent(InputMethodEvent paramInputMethodEvent)
/*      */   {
/*  589 */     AttributedCharacterIterator localAttributedCharacterIterator = paramInputMethodEvent.getText();
/*  590 */     int i = paramInputMethodEvent.getCommittedCharacterCount();
/*      */ 
/*  593 */     if (localAttributedCharacterIterator != null) {
/*  594 */       int j = localAttributedCharacterIterator.getBeginIndex();
/*  595 */       int k = localAttributedCharacterIterator.getEndIndex();
/*  596 */       this.composedTextExists = (k - j > i);
/*      */     } else {
/*  598 */       this.composedTextExists = false;
/*      */     }
/*      */ 
/*  601 */     super.processInputMethodEvent(paramInputMethodEvent);
/*      */   }
/*      */ 
/*      */   protected void processFocusEvent(FocusEvent paramFocusEvent)
/*      */   {
/*  613 */     super.processFocusEvent(paramFocusEvent);
/*      */ 
/*  616 */     if (paramFocusEvent.isTemporary()) {
/*  617 */       return;
/*      */     }
/*      */ 
/*  620 */     if ((isEdited()) && (paramFocusEvent.getID() == 1005)) {
/*  621 */       InputContext localInputContext = getInputContext();
/*  622 */       if (this.focusLostHandler == null) {
/*  623 */         this.focusLostHandler = new FocusLostHandler(null);
/*      */       }
/*      */ 
/*  627 */       if ((localInputContext != null) && (this.composedTextExists)) {
/*  628 */         localInputContext.endComposition();
/*  629 */         EventQueue.invokeLater(this.focusLostHandler);
/*      */       } else {
/*  631 */         this.focusLostHandler.run();
/*      */       }
/*      */     }
/*  634 */     else if (!isEdited())
/*      */     {
/*  636 */       setValue(getValue(), true, true);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Action[] getActions()
/*      */   {
/*  677 */     return TextAction.augmentList(super.getActions(), defaultActions);
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  687 */     return "FormattedTextFieldUI";
/*      */   }
/*      */ 
/*      */   public void setDocument(Document paramDocument)
/*      */   {
/*  704 */     if ((this.documentListener != null) && (getDocument() != null)) {
/*  705 */       getDocument().removeDocumentListener(this.documentListener);
/*      */     }
/*  707 */     super.setDocument(paramDocument);
/*  708 */     if (this.documentListener == null) {
/*  709 */       this.documentListener = new DocumentHandler(null);
/*      */     }
/*  711 */     paramDocument.addDocumentListener(this.documentListener);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  721 */     paramObjectOutputStream.defaultWriteObject();
/*  722 */     if (getUIClassID().equals("FormattedTextFieldUI")) {
/*  723 */       byte b = JComponent.getWriteObjCounter(this);
/*  724 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/*  725 */       if ((b == 0) && (this.ui != null))
/*  726 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setFormatterActions(Action[] paramArrayOfAction)
/*      */   {
/*  736 */     if (paramArrayOfAction == null) {
/*  737 */       if (this.textFormatterActionMap != null)
/*  738 */         this.textFormatterActionMap.clear();
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject2;
/*  742 */       if (this.textFormatterActionMap == null) {
/*  743 */         Object localObject1 = getActionMap();
/*      */ 
/*  745 */         this.textFormatterActionMap = new ActionMap();
/*  746 */         while (localObject1 != null) {
/*  747 */           localObject2 = ((ActionMap)localObject1).getParent();
/*      */ 
/*  749 */           if (((localObject2 instanceof UIResource)) || (localObject2 == null)) {
/*  750 */             ((ActionMap)localObject1).setParent(this.textFormatterActionMap);
/*  751 */             this.textFormatterActionMap.setParent((ActionMap)localObject2);
/*  752 */             break;
/*      */           }
/*  754 */           localObject1 = localObject2;
/*      */         }
/*      */       }
/*  757 */       for (int i = paramArrayOfAction.length - 1; i >= 0; 
/*  758 */         i--) {
/*  759 */         localObject2 = paramArrayOfAction[i].getValue("Name");
/*      */ 
/*  761 */         if (localObject2 != null)
/*  762 */           this.textFormatterActionMap.put(localObject2, paramArrayOfAction[i]);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setValue(Object paramObject, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  775 */     Object localObject = this.value;
/*      */ 
/*  777 */     this.value = paramObject;
/*      */ 
/*  779 */     if (paramBoolean1) {
/*  780 */       AbstractFormatterFactory localAbstractFormatterFactory = getFormatterFactory();
/*      */       AbstractFormatter localAbstractFormatter;
/*  783 */       if (localAbstractFormatterFactory != null) {
/*  784 */         localAbstractFormatter = localAbstractFormatterFactory.getFormatter(this);
/*      */       }
/*      */       else {
/*  787 */         localAbstractFormatter = null;
/*      */       }
/*  789 */       setFormatter(localAbstractFormatter);
/*      */     }
/*      */     else
/*      */     {
/*  793 */       setEditValid(true);
/*      */     }
/*      */ 
/*  796 */     setEdited(false);
/*      */ 
/*  798 */     if (paramBoolean2)
/*  799 */       firePropertyChange("value", localObject, paramObject);
/*      */   }
/*      */ 
/*      */   private void setEdited(boolean paramBoolean)
/*      */   {
/*  807 */     this.edited = paramBoolean;
/*      */   }
/*      */ 
/*      */   private boolean isEdited()
/*      */   {
/*  814 */     return this.edited;
/*      */   }
/*      */ 
/*      */   private AbstractFormatterFactory getDefaultFormatterFactory(Object paramObject)
/*      */   {
/*  822 */     if ((paramObject instanceof DateFormat)) {
/*  823 */       return new DefaultFormatterFactory(new DateFormatter((DateFormat)paramObject));
/*      */     }
/*      */ 
/*  826 */     if ((paramObject instanceof NumberFormat)) {
/*  827 */       return new DefaultFormatterFactory(new NumberFormatter((NumberFormat)paramObject));
/*      */     }
/*      */ 
/*  830 */     if ((paramObject instanceof Format)) {
/*  831 */       return new DefaultFormatterFactory(new InternationalFormatter((Format)paramObject));
/*      */     }
/*      */ 
/*  834 */     if ((paramObject instanceof Date)) {
/*  835 */       return new DefaultFormatterFactory(new DateFormatter());
/*      */     }
/*  837 */     if ((paramObject instanceof Number)) {
/*  838 */       NumberFormatter localNumberFormatter1 = new NumberFormatter();
/*  839 */       ((NumberFormatter)localNumberFormatter1).setValueClass(paramObject.getClass());
/*  840 */       NumberFormatter localNumberFormatter2 = new NumberFormatter(new DecimalFormat("#.#"));
/*      */ 
/*  842 */       ((NumberFormatter)localNumberFormatter2).setValueClass(paramObject.getClass());
/*      */ 
/*  844 */       return new DefaultFormatterFactory(localNumberFormatter1, localNumberFormatter1, localNumberFormatter2);
/*      */     }
/*      */ 
/*  847 */     return new DefaultFormatterFactory(new DefaultFormatter());
/*      */   }
/*      */ 
/*      */   public static abstract class AbstractFormatter
/*      */     implements Serializable
/*      */   {
/*      */     private JFormattedTextField ftf;
/*      */ 
/*      */     public void install(JFormattedTextField paramJFormattedTextField)
/*      */     {
/*  943 */       if (this.ftf != null) {
/*  944 */         uninstall();
/*      */       }
/*  946 */       this.ftf = paramJFormattedTextField;
/*  947 */       if (paramJFormattedTextField != null) {
/*      */         try {
/*  949 */           paramJFormattedTextField.setText(valueToString(paramJFormattedTextField.getValue()));
/*      */         } catch (ParseException localParseException) {
/*  951 */           paramJFormattedTextField.setText("");
/*  952 */           setEditValid(false);
/*      */         }
/*  954 */         installDocumentFilter(getDocumentFilter());
/*  955 */         paramJFormattedTextField.setNavigationFilter(getNavigationFilter());
/*  956 */         paramJFormattedTextField.setFormatterActions(getActions());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void uninstall()
/*      */     {
/*  968 */       if (this.ftf != null) {
/*  969 */         installDocumentFilter(null);
/*  970 */         this.ftf.setNavigationFilter(null);
/*  971 */         this.ftf.setFormatterActions(null);
/*      */       }
/*      */     }
/*      */ 
/*      */     public abstract Object stringToValue(String paramString)
/*      */       throws ParseException;
/*      */ 
/*      */     public abstract String valueToString(Object paramObject)
/*      */       throws ParseException;
/*      */ 
/*      */     protected JFormattedTextField getFormattedTextField()
/*      */     {
/* 1003 */       return this.ftf;
/*      */     }
/*      */ 
/*      */     protected void invalidEdit()
/*      */     {
/* 1011 */       JFormattedTextField localJFormattedTextField = getFormattedTextField();
/*      */ 
/* 1013 */       if (localJFormattedTextField != null)
/* 1014 */         localJFormattedTextField.invalidEdit();
/*      */     }
/*      */ 
/*      */     protected void setEditValid(boolean paramBoolean)
/*      */     {
/* 1027 */       JFormattedTextField localJFormattedTextField = getFormattedTextField();
/*      */ 
/* 1029 */       if (localJFormattedTextField != null)
/* 1030 */         localJFormattedTextField.setEditValid(paramBoolean);
/*      */     }
/*      */ 
/*      */     protected Action[] getActions()
/*      */     {
/* 1042 */       return null;
/*      */     }
/*      */ 
/*      */     protected DocumentFilter getDocumentFilter()
/*      */     {
/* 1054 */       return null;
/*      */     }
/*      */ 
/*      */     protected NavigationFilter getNavigationFilter()
/*      */     {
/* 1066 */       return null;
/*      */     }
/*      */ 
/*      */     protected Object clone()
/*      */       throws CloneNotSupportedException
/*      */     {
/* 1076 */       AbstractFormatter localAbstractFormatter = (AbstractFormatter)super.clone();
/*      */ 
/* 1078 */       localAbstractFormatter.ftf = null;
/* 1079 */       return localAbstractFormatter;
/*      */     }
/*      */ 
/*      */     private void installDocumentFilter(DocumentFilter paramDocumentFilter)
/*      */     {
/* 1089 */       JFormattedTextField localJFormattedTextField = getFormattedTextField();
/*      */ 
/* 1091 */       if (localJFormattedTextField != null) {
/* 1092 */         Document localDocument = localJFormattedTextField.getDocument();
/*      */ 
/* 1094 */         if ((localDocument instanceof AbstractDocument)) {
/* 1095 */           ((AbstractDocument)localDocument).setDocumentFilter(paramDocumentFilter);
/*      */         }
/* 1097 */         localDocument.putProperty(DocumentFilter.class, null);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class AbstractFormatterFactory
/*      */   {
/*      */     public abstract JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField paramJFormattedTextField);
/*      */   }
/*      */ 
/*      */   private static class CancelAction extends TextAction
/*      */   {
/*      */     public CancelAction()
/*      */     {
/* 1149 */       super();
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent) {
/* 1153 */       JTextComponent localJTextComponent = getFocusedComponent();
/*      */ 
/* 1155 */       if ((localJTextComponent instanceof JFormattedTextField)) {
/* 1156 */         JFormattedTextField localJFormattedTextField = (JFormattedTextField)localJTextComponent;
/* 1157 */         localJFormattedTextField.setValue(localJFormattedTextField.getValue());
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isEnabled() {
/* 1162 */       JTextComponent localJTextComponent = getFocusedComponent();
/* 1163 */       if ((localJTextComponent instanceof JFormattedTextField)) {
/* 1164 */         JFormattedTextField localJFormattedTextField = (JFormattedTextField)localJTextComponent;
/* 1165 */         if (!localJFormattedTextField.isEdited()) {
/* 1166 */           return false;
/*      */         }
/* 1168 */         return true;
/*      */       }
/* 1170 */       return super.isEnabled();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class CommitAction extends JTextField.NotifyAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/* 1111 */       JTextComponent localJTextComponent = getFocusedComponent();
/*      */ 
/* 1113 */       if ((localJTextComponent instanceof JFormattedTextField)) {
/*      */         try
/*      */         {
/* 1116 */           ((JFormattedTextField)localJTextComponent).commitEdit();
/*      */         } catch (ParseException localParseException) {
/* 1118 */           ((JFormattedTextField)localJTextComponent).invalidEdit();
/*      */ 
/* 1120 */           return;
/*      */         }
/*      */       }
/*      */ 
/* 1124 */       super.actionPerformed(paramActionEvent);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled() {
/* 1128 */       JTextComponent localJTextComponent = getFocusedComponent();
/* 1129 */       if ((localJTextComponent instanceof JFormattedTextField)) {
/* 1130 */         JFormattedTextField localJFormattedTextField = (JFormattedTextField)localJTextComponent;
/* 1131 */         if (!localJFormattedTextField.isEdited()) {
/* 1132 */           return false;
/*      */         }
/* 1134 */         return true;
/*      */       }
/* 1136 */       return super.isEnabled();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class DocumentHandler
/*      */     implements DocumentListener, Serializable
/*      */   {
/*      */     private DocumentHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void insertUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/* 1180 */       JFormattedTextField.this.setEdited(true);
/*      */     }
/*      */     public void removeUpdate(DocumentEvent paramDocumentEvent) {
/* 1183 */       JFormattedTextField.this.setEdited(true);
/*      */     }
/*      */ 
/*      */     public void changedUpdate(DocumentEvent paramDocumentEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FocusLostHandler
/*      */     implements Runnable, Serializable
/*      */   {
/*      */     private FocusLostHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void run()
/*      */     {
/*  645 */       int i = JFormattedTextField.this.getFocusLostBehavior();
/*  646 */       if ((i == 0) || (i == 1)) {
/*      */         try
/*      */         {
/*  649 */           JFormattedTextField.this.commitEdit();
/*      */ 
/*  651 */           JFormattedTextField.this.setValue(JFormattedTextField.this.getValue(), true, true);
/*      */         }
/*      */         catch (ParseException localParseException) {
/*  654 */           if (i == 1) {
/*  655 */             JFormattedTextField.this.setValue(JFormattedTextField.this.getValue(), true, true);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*  660 */       else if (i == 2)
/*  661 */         JFormattedTextField.this.setValue(JFormattedTextField.this.getValue(), true, true);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JFormattedTextField
 * JD-Core Version:    0.6.2
 */