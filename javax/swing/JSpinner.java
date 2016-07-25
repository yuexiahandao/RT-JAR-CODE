/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Component.BaselineResizeBehavior;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormat;
/*      */ import java.text.DecimalFormat;
/*      */ import java.text.MessageFormat;
/*      */ import java.text.NumberFormat;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.ResourceBundle;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleAction;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleEditableText;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.SpinnerUI;
/*      */ import javax.swing.text.AttributeSet;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.DateFormatter;
/*      */ import javax.swing.text.DefaultFormatterFactory;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.DocumentFilter;
/*      */ import javax.swing.text.DocumentFilter.FilterBypass;
/*      */ import javax.swing.text.NumberFormatter;
/*      */ import sun.util.resources.LocaleData;
/*      */ 
/*      */ public class JSpinner extends JComponent
/*      */   implements Accessible
/*      */ {
/*      */   private static final String uiClassID = "SpinnerUI";
/*  134 */   private static final Action DISABLED_ACTION = new DisabledAction(null);
/*      */   private SpinnerModel model;
/*      */   private JComponent editor;
/*      */   private ChangeListener modelListener;
/*      */   private transient ChangeEvent changeEvent;
/*  140 */   private boolean editorExplicitlySet = false;
/*      */ 
/*      */   public JSpinner(SpinnerModel paramSpinnerModel)
/*      */   {
/*  151 */     if (paramSpinnerModel == null) {
/*  152 */       throw new NullPointerException("model cannot be null");
/*      */     }
/*  154 */     this.model = paramSpinnerModel;
/*  155 */     this.editor = createEditor(paramSpinnerModel);
/*  156 */     setUIProperty("opaque", Boolean.valueOf(true));
/*  157 */     updateUI();
/*      */   }
/*      */ 
/*      */   public JSpinner()
/*      */   {
/*  166 */     this(new SpinnerNumberModel());
/*      */   }
/*      */ 
/*      */   public SpinnerUI getUI()
/*      */   {
/*  176 */     return (SpinnerUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(SpinnerUI paramSpinnerUI)
/*      */   {
/*  187 */     super.setUI(paramSpinnerUI);
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  200 */     return "SpinnerUI";
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  211 */     setUI((SpinnerUI)UIManager.getUI(this));
/*  212 */     invalidate();
/*      */   }
/*      */ 
/*      */   protected JComponent createEditor(SpinnerModel paramSpinnerModel)
/*      */   {
/*  242 */     if ((paramSpinnerModel instanceof SpinnerDateModel)) {
/*  243 */       return new DateEditor(this);
/*      */     }
/*  245 */     if ((paramSpinnerModel instanceof SpinnerListModel)) {
/*  246 */       return new ListEditor(this);
/*      */     }
/*  248 */     if ((paramSpinnerModel instanceof SpinnerNumberModel)) {
/*  249 */       return new NumberEditor(this);
/*      */     }
/*      */ 
/*  252 */     return new DefaultEditor(this);
/*      */   }
/*      */ 
/*      */   public void setModel(SpinnerModel paramSpinnerModel)
/*      */   {
/*  280 */     if (paramSpinnerModel == null) {
/*  281 */       throw new IllegalArgumentException("null model");
/*      */     }
/*  283 */     if (!paramSpinnerModel.equals(this.model)) {
/*  284 */       SpinnerModel localSpinnerModel = this.model;
/*  285 */       this.model = paramSpinnerModel;
/*  286 */       if (this.modelListener != null) {
/*  287 */         localSpinnerModel.removeChangeListener(this.modelListener);
/*  288 */         this.model.addChangeListener(this.modelListener);
/*      */       }
/*  290 */       firePropertyChange("model", localSpinnerModel, paramSpinnerModel);
/*  291 */       if (!this.editorExplicitlySet) {
/*  292 */         setEditor(createEditor(paramSpinnerModel));
/*  293 */         this.editorExplicitlySet = false;
/*      */       }
/*  295 */       repaint();
/*  296 */       revalidate();
/*      */     }
/*      */   }
/*      */ 
/*      */   public SpinnerModel getModel()
/*      */   {
/*  309 */     return this.model;
/*      */   }
/*      */ 
/*      */   public Object getValue()
/*      */   {
/*  331 */     return getModel().getValue();
/*      */   }
/*      */ 
/*      */   public void setValue(Object paramObject)
/*      */   {
/*  353 */     getModel().setValue(paramObject);
/*      */   }
/*      */ 
/*      */   public Object getNextValue()
/*      */   {
/*  375 */     return getModel().getNextValue();
/*      */   }
/*      */ 
/*      */   public void addChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  405 */     if (this.modelListener == null) {
/*  406 */       this.modelListener = new ModelListener(null);
/*  407 */       getModel().addChangeListener(this.modelListener);
/*      */     }
/*  409 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public void removeChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  422 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public ChangeListener[] getChangeListeners()
/*      */   {
/*  435 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireStateChanged()
/*      */   {
/*  451 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*  452 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  453 */       if (arrayOfObject[i] == ChangeListener.class) {
/*  454 */         if (this.changeEvent == null) {
/*  455 */           this.changeEvent = new ChangeEvent(this);
/*      */         }
/*  457 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public Object getPreviousValue()
/*      */   {
/*  483 */     return getModel().getPreviousValue();
/*      */   }
/*      */ 
/*      */   public void setEditor(JComponent paramJComponent)
/*      */   {
/*  507 */     if (paramJComponent == null) {
/*  508 */       throw new IllegalArgumentException("null editor");
/*      */     }
/*  510 */     if (!paramJComponent.equals(this.editor)) {
/*  511 */       JComponent localJComponent = this.editor;
/*  512 */       this.editor = paramJComponent;
/*  513 */       if ((localJComponent instanceof DefaultEditor)) {
/*  514 */         ((DefaultEditor)localJComponent).dismiss(this);
/*      */       }
/*  516 */       this.editorExplicitlySet = true;
/*  517 */       firePropertyChange("editor", localJComponent, paramJComponent);
/*  518 */       revalidate();
/*  519 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public JComponent getEditor()
/*      */   {
/*  534 */     return this.editor;
/*      */   }
/*      */ 
/*      */   public void commitEdit()
/*      */     throws ParseException
/*      */   {
/*  548 */     JComponent localJComponent = getEditor();
/*  549 */     if ((localJComponent instanceof DefaultEditor))
/*  550 */       ((DefaultEditor)localJComponent).commitEdit();
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  562 */     paramObjectOutputStream.defaultWriteObject();
/*  563 */     if (getUIClassID().equals("SpinnerUI")) {
/*  564 */       byte b = JComponent.getWriteObjCounter(this);
/*  565 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/*  566 */       if ((b == 0) && (this.ui != null))
/*  567 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1398 */     if (this.accessibleContext == null) {
/* 1399 */       this.accessibleContext = new AccessibleJSpinner();
/*      */     }
/* 1401 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJSpinner extends JComponent.AccessibleJComponent
/*      */     implements AccessibleValue, AccessibleAction, AccessibleText, AccessibleEditableText, ChangeListener
/*      */   {
/* 1413 */     private Object oldModelValue = null;
/*      */ 
/*      */     protected AccessibleJSpinner()
/*      */     {
/* 1418 */       super();
/*      */ 
/* 1420 */       this.oldModelValue = JSpinner.this.model.getValue();
/* 1421 */       JSpinner.this.addChangeListener(this);
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/* 1431 */       if (paramChangeEvent == null) {
/* 1432 */         throw new NullPointerException();
/*      */       }
/* 1434 */       Object localObject = JSpinner.this.model.getValue();
/* 1435 */       firePropertyChange("AccessibleValue", this.oldModelValue, localObject);
/*      */ 
/* 1438 */       firePropertyChange("AccessibleText", null, Integer.valueOf(0));
/*      */ 
/* 1442 */       this.oldModelValue = localObject;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1466 */       return AccessibleRole.SPIN_BOX;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 1476 */       if (JSpinner.this.editor.getAccessibleContext() != null) {
/* 1477 */         return 1;
/*      */       }
/* 1479 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 1494 */       if (paramInt != 0) {
/* 1495 */         return null;
/*      */       }
/* 1497 */       if (JSpinner.this.editor.getAccessibleContext() != null) {
/* 1498 */         return (Accessible)JSpinner.this.editor;
/*      */       }
/* 1500 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleAction getAccessibleAction()
/*      */     {
/* 1513 */       return this;
/*      */     }
/*      */ 
/*      */     public AccessibleText getAccessibleText()
/*      */     {
/* 1524 */       return this;
/*      */     }
/*      */ 
/*      */     private AccessibleContext getEditorAccessibleContext()
/*      */     {
/* 1531 */       if ((JSpinner.this.editor instanceof JSpinner.DefaultEditor)) {
/* 1532 */         JFormattedTextField localJFormattedTextField = ((JSpinner.DefaultEditor)JSpinner.this.editor).getTextField();
/* 1533 */         if (localJFormattedTextField != null)
/* 1534 */           return localJFormattedTextField.getAccessibleContext();
/*      */       }
/* 1536 */       else if ((JSpinner.this.editor instanceof Accessible)) {
/* 1537 */         return JSpinner.this.editor.getAccessibleContext();
/*      */       }
/* 1539 */       return null;
/*      */     }
/*      */ 
/*      */     private AccessibleText getEditorAccessibleText()
/*      */     {
/* 1546 */       AccessibleContext localAccessibleContext = getEditorAccessibleContext();
/* 1547 */       if (localAccessibleContext != null) {
/* 1548 */         return localAccessibleContext.getAccessibleText();
/*      */       }
/* 1550 */       return null;
/*      */     }
/*      */ 
/*      */     private AccessibleEditableText getEditorAccessibleEditableText()
/*      */     {
/* 1557 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1558 */       if ((localAccessibleText instanceof AccessibleEditableText)) {
/* 1559 */         return (AccessibleEditableText)localAccessibleText;
/*      */       }
/* 1561 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleValue getAccessibleValue()
/*      */     {
/* 1572 */       return this;
/*      */     }
/*      */ 
/*      */     public Number getCurrentAccessibleValue()
/*      */     {
/* 1585 */       Object localObject = JSpinner.this.model.getValue();
/* 1586 */       if ((localObject instanceof Number)) {
/* 1587 */         return (Number)localObject;
/*      */       }
/* 1589 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*      */     {
/*      */       try
/*      */       {
/* 1602 */         JSpinner.this.model.setValue(paramNumber);
/* 1603 */         return true;
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {
/*      */       }
/* 1607 */       return false;
/*      */     }
/*      */ 
/*      */     public Number getMinimumAccessibleValue()
/*      */     {
/* 1618 */       if ((JSpinner.this.model instanceof SpinnerNumberModel)) {
/* 1619 */         SpinnerNumberModel localSpinnerNumberModel = (SpinnerNumberModel)JSpinner.this.model;
/* 1620 */         Comparable localComparable = localSpinnerNumberModel.getMinimum();
/* 1621 */         if ((localComparable instanceof Number)) {
/* 1622 */           return (Number)localComparable;
/*      */         }
/*      */       }
/* 1625 */       return null;
/*      */     }
/*      */ 
/*      */     public Number getMaximumAccessibleValue()
/*      */     {
/* 1636 */       if ((JSpinner.this.model instanceof SpinnerNumberModel)) {
/* 1637 */         SpinnerNumberModel localSpinnerNumberModel = (SpinnerNumberModel)JSpinner.this.model;
/* 1638 */         Comparable localComparable = localSpinnerNumberModel.getMaximum();
/* 1639 */         if ((localComparable instanceof Number)) {
/* 1640 */           return (Number)localComparable;
/*      */         }
/*      */       }
/* 1643 */       return null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleActionCount()
/*      */     {
/* 1662 */       return 2;
/*      */     }
/*      */ 
/*      */     public String getAccessibleActionDescription(int paramInt)
/*      */     {
/* 1673 */       if (paramInt == 0)
/* 1674 */         return AccessibleAction.INCREMENT;
/* 1675 */       if (paramInt == 1) {
/* 1676 */         return AccessibleAction.DECREMENT;
/*      */       }
/* 1678 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean doAccessibleAction(int paramInt)
/*      */     {
/* 1691 */       if ((paramInt < 0) || (paramInt > 1))
/* 1692 */         return false;
/*      */       Object localObject;
/* 1695 */       if (paramInt == 0)
/* 1696 */         localObject = JSpinner.this.getNextValue();
/*      */       else {
/* 1698 */         localObject = JSpinner.this.getPreviousValue();
/*      */       }
/*      */       try
/*      */       {
/* 1702 */         JSpinner.this.model.setValue(localObject);
/* 1703 */         return true;
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException) {
/*      */       }
/* 1707 */       return false;
/*      */     }
/*      */ 
/*      */     private boolean sameWindowAncestor(Component paramComponent1, Component paramComponent2)
/*      */     {
/* 1719 */       if ((paramComponent1 == null) || (paramComponent2 == null)) {
/* 1720 */         return false;
/*      */       }
/* 1722 */       return SwingUtilities.getWindowAncestor(paramComponent1) == SwingUtilities.getWindowAncestor(paramComponent2);
/*      */     }
/*      */ 
/*      */     public int getIndexAtPoint(Point paramPoint)
/*      */     {
/* 1736 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1737 */       if ((localAccessibleText != null) && (sameWindowAncestor(JSpinner.this, JSpinner.this.editor)))
/*      */       {
/* 1740 */         Point localPoint = SwingUtilities.convertPoint(JSpinner.this, paramPoint, JSpinner.this.editor);
/*      */ 
/* 1743 */         if (localPoint != null) {
/* 1744 */           return localAccessibleText.getIndexAtPoint(localPoint);
/*      */         }
/*      */       }
/* 1747 */       return -1;
/*      */     }
/*      */ 
/*      */     public Rectangle getCharacterBounds(int paramInt)
/*      */     {
/* 1761 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1762 */       if (localAccessibleText != null) {
/* 1763 */         Rectangle localRectangle = localAccessibleText.getCharacterBounds(paramInt);
/* 1764 */         if ((localRectangle != null) && (sameWindowAncestor(JSpinner.this, JSpinner.this.editor)))
/*      */         {
/* 1767 */           return SwingUtilities.convertRectangle(JSpinner.this.editor, localRectangle, JSpinner.this);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1772 */       return null;
/*      */     }
/*      */ 
/*      */     public int getCharCount()
/*      */     {
/* 1781 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1782 */       if (localAccessibleText != null) {
/* 1783 */         return localAccessibleText.getCharCount();
/*      */       }
/* 1785 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getCaretPosition()
/*      */     {
/* 1796 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1797 */       if (localAccessibleText != null) {
/* 1798 */         return localAccessibleText.getCaretPosition();
/*      */       }
/* 1800 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getAtIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1811 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1812 */       if (localAccessibleText != null) {
/* 1813 */         return localAccessibleText.getAtIndex(paramInt1, paramInt2);
/*      */       }
/* 1815 */       return null;
/*      */     }
/*      */ 
/*      */     public String getAfterIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1826 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1827 */       if (localAccessibleText != null) {
/* 1828 */         return localAccessibleText.getAfterIndex(paramInt1, paramInt2);
/*      */       }
/* 1830 */       return null;
/*      */     }
/*      */ 
/*      */     public String getBeforeIndex(int paramInt1, int paramInt2)
/*      */     {
/* 1841 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1842 */       if (localAccessibleText != null) {
/* 1843 */         return localAccessibleText.getBeforeIndex(paramInt1, paramInt2);
/*      */       }
/* 1845 */       return null;
/*      */     }
/*      */ 
/*      */     public AttributeSet getCharacterAttribute(int paramInt)
/*      */     {
/* 1855 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1856 */       if (localAccessibleText != null) {
/* 1857 */         return localAccessibleText.getCharacterAttribute(paramInt);
/*      */       }
/* 1859 */       return null;
/*      */     }
/*      */ 
/*      */     public int getSelectionStart()
/*      */     {
/* 1870 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1871 */       if (localAccessibleText != null) {
/* 1872 */         return localAccessibleText.getSelectionStart();
/*      */       }
/* 1874 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getSelectionEnd()
/*      */     {
/* 1885 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1886 */       if (localAccessibleText != null) {
/* 1887 */         return localAccessibleText.getSelectionEnd();
/*      */       }
/* 1889 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getSelectedText()
/*      */     {
/* 1898 */       AccessibleText localAccessibleText = getEditorAccessibleText();
/* 1899 */       if (localAccessibleText != null) {
/* 1900 */         return localAccessibleText.getSelectedText();
/*      */       }
/* 1902 */       return null;
/*      */     }
/*      */ 
/*      */     public void setTextContents(String paramString)
/*      */     {
/* 1916 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 1917 */       if (localAccessibleEditableText != null)
/* 1918 */         localAccessibleEditableText.setTextContents(paramString);
/*      */     }
/*      */ 
/*      */     public void insertTextAtIndex(int paramInt, String paramString)
/*      */     {
/* 1930 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 1931 */       if (localAccessibleEditableText != null)
/* 1932 */         localAccessibleEditableText.insertTextAtIndex(paramInt, paramString);
/*      */     }
/*      */ 
/*      */     public String getTextRange(int paramInt1, int paramInt2)
/*      */     {
/* 1944 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 1945 */       if (localAccessibleEditableText != null) {
/* 1946 */         return localAccessibleEditableText.getTextRange(paramInt1, paramInt2);
/*      */       }
/* 1948 */       return null;
/*      */     }
/*      */ 
/*      */     public void delete(int paramInt1, int paramInt2)
/*      */     {
/* 1958 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 1959 */       if (localAccessibleEditableText != null)
/* 1960 */         localAccessibleEditableText.delete(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void cut(int paramInt1, int paramInt2)
/*      */     {
/* 1971 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 1972 */       if (localAccessibleEditableText != null)
/* 1973 */         localAccessibleEditableText.cut(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void paste(int paramInt)
/*      */     {
/* 1984 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 1985 */       if (localAccessibleEditableText != null)
/* 1986 */         localAccessibleEditableText.paste(paramInt);
/*      */     }
/*      */ 
/*      */     public void replaceText(int paramInt1, int paramInt2, String paramString)
/*      */     {
/* 1999 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 2000 */       if (localAccessibleEditableText != null)
/* 2001 */         localAccessibleEditableText.replaceText(paramInt1, paramInt2, paramString);
/*      */     }
/*      */ 
/*      */     public void selectText(int paramInt1, int paramInt2)
/*      */     {
/* 2012 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 2013 */       if (localAccessibleEditableText != null)
/* 2014 */         localAccessibleEditableText.selectText(paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void setAttributes(int paramInt1, int paramInt2, AttributeSet paramAttributeSet)
/*      */     {
/* 2027 */       AccessibleEditableText localAccessibleEditableText = getEditorAccessibleEditableText();
/* 2028 */       if (localAccessibleEditableText != null)
/* 2029 */         localAccessibleEditableText.setAttributes(paramInt1, paramInt2, paramAttributeSet);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DateEditor extends JSpinner.DefaultEditor
/*      */   {
/*      */     private static String getDefaultPattern(Locale paramLocale)
/*      */     {
/*  949 */       ResourceBundle localResourceBundle = LocaleData.getDateFormatData(paramLocale);
/*  950 */       String[] arrayOfString = localResourceBundle.getStringArray("DateTimePatterns");
/*  951 */       Object[] arrayOfObject = { arrayOfString[3], arrayOfString[7] };
/*      */ 
/*  953 */       return MessageFormat.format(arrayOfString[8], arrayOfObject);
/*      */     }
/*      */ 
/*      */     public DateEditor(JSpinner paramJSpinner)
/*      */     {
/*  973 */       this(paramJSpinner, getDefaultPattern(paramJSpinner.getLocale()));
/*      */     }
/*      */ 
/*      */     public DateEditor(JSpinner paramJSpinner, String paramString)
/*      */     {
/*  998 */       this(paramJSpinner, new SimpleDateFormat(paramString, paramJSpinner.getLocale()));
/*      */     }
/*      */ 
/*      */     private DateEditor(JSpinner paramJSpinner, DateFormat paramDateFormat)
/*      */     {
/* 1023 */       super();
/* 1024 */       if (!(paramJSpinner.getModel() instanceof SpinnerDateModel)) {
/* 1025 */         throw new IllegalArgumentException("model not a SpinnerDateModel");
/*      */       }
/*      */ 
/* 1029 */       SpinnerDateModel localSpinnerDateModel = (SpinnerDateModel)paramJSpinner.getModel();
/* 1030 */       JSpinner.DateEditorFormatter localDateEditorFormatter = new JSpinner.DateEditorFormatter(localSpinnerDateModel, paramDateFormat);
/* 1031 */       DefaultFormatterFactory localDefaultFormatterFactory = new DefaultFormatterFactory(localDateEditorFormatter);
/*      */ 
/* 1033 */       JFormattedTextField localJFormattedTextField = getTextField();
/* 1034 */       localJFormattedTextField.setEditable(true);
/* 1035 */       localJFormattedTextField.setFormatterFactory(localDefaultFormatterFactory);
/*      */       try
/*      */       {
/* 1042 */         String str1 = localDateEditorFormatter.valueToString(localSpinnerDateModel.getStart());
/* 1043 */         String str2 = localDateEditorFormatter.valueToString(localSpinnerDateModel.getEnd());
/* 1044 */         localJFormattedTextField.setColumns(Math.max(str1.length(), str2.length()));
/*      */       }
/*      */       catch (ParseException localParseException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     public SimpleDateFormat getFormat()
/*      */     {
/* 1062 */       return (SimpleDateFormat)((DateFormatter)getTextField().getFormatter()).getFormat();
/*      */     }
/*      */ 
/*      */     public SpinnerDateModel getModel()
/*      */     {
/* 1074 */       return (SpinnerDateModel)getSpinner().getModel();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class DateEditorFormatter extends DateFormatter
/*      */   {
/*      */     private final SpinnerDateModel model;
/*      */ 
/*      */     DateEditorFormatter(SpinnerDateModel paramSpinnerDateModel, DateFormat paramDateFormat)
/*      */     {
/*  912 */       super();
/*  913 */       this.model = paramSpinnerDateModel;
/*      */     }
/*      */ 
/*      */     public void setMinimum(Comparable paramComparable) {
/*  917 */       this.model.setStart(paramComparable);
/*      */     }
/*      */ 
/*      */     public Comparable getMinimum() {
/*  921 */       return this.model.getStart();
/*      */     }
/*      */ 
/*      */     public void setMaximum(Comparable paramComparable) {
/*  925 */       this.model.setEnd(paramComparable);
/*      */     }
/*      */ 
/*      */     public Comparable getMaximum() {
/*  929 */       return this.model.getEnd();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class DefaultEditor extends JPanel
/*      */     implements ChangeListener, PropertyChangeListener, LayoutManager
/*      */   {
/*      */     public DefaultEditor(JSpinner paramJSpinner)
/*      */     {
/*  619 */       super();
/*      */ 
/*  621 */       JFormattedTextField localJFormattedTextField = new JFormattedTextField();
/*  622 */       localJFormattedTextField.setName("Spinner.formattedTextField");
/*  623 */       localJFormattedTextField.setValue(paramJSpinner.getValue());
/*  624 */       localJFormattedTextField.addPropertyChangeListener(this);
/*  625 */       localJFormattedTextField.setEditable(false);
/*  626 */       localJFormattedTextField.setInheritsPopupMenu(true);
/*      */ 
/*  628 */       String str = paramJSpinner.getToolTipText();
/*  629 */       if (str != null) {
/*  630 */         localJFormattedTextField.setToolTipText(str);
/*      */       }
/*      */ 
/*  633 */       add(localJFormattedTextField);
/*      */ 
/*  635 */       setLayout(this);
/*  636 */       paramJSpinner.addChangeListener(this);
/*      */ 
/*  643 */       ActionMap localActionMap = localJFormattedTextField.getActionMap();
/*      */ 
/*  645 */       if (localActionMap != null) {
/*  646 */         localActionMap.put("increment", JSpinner.DISABLED_ACTION);
/*  647 */         localActionMap.put("decrement", JSpinner.DISABLED_ACTION);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void dismiss(JSpinner paramJSpinner)
/*      */     {
/*  661 */       paramJSpinner.removeChangeListener(this);
/*      */     }
/*      */ 
/*      */     public JSpinner getSpinner()
/*      */     {
/*  680 */       for (Object localObject = this; localObject != null; localObject = ((Component)localObject).getParent()) {
/*  681 */         if ((localObject instanceof JSpinner)) {
/*  682 */           return (JSpinner)localObject;
/*      */         }
/*      */       }
/*  685 */       return null;
/*      */     }
/*      */ 
/*      */     public JFormattedTextField getTextField()
/*      */     {
/*  700 */       return (JFormattedTextField)getComponent(0);
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/*  715 */       JSpinner localJSpinner = (JSpinner)paramChangeEvent.getSource();
/*  716 */       getTextField().setValue(localJSpinner.getValue());
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  737 */       JSpinner localJSpinner = getSpinner();
/*      */ 
/*  739 */       if (localJSpinner == null)
/*      */       {
/*  741 */         return;
/*      */       }
/*      */ 
/*  744 */       Object localObject1 = paramPropertyChangeEvent.getSource();
/*  745 */       String str = paramPropertyChangeEvent.getPropertyName();
/*  746 */       if (((localObject1 instanceof JFormattedTextField)) && ("value".equals(str))) {
/*  747 */         Object localObject2 = localJSpinner.getValue();
/*      */         try
/*      */         {
/*  751 */           localJSpinner.setValue(getTextField().getValue());
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException1) {
/*      */           try {
/*  755 */             ((JFormattedTextField)localObject1).setValue(localObject2);
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException2)
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void addLayoutComponent(String paramString, Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeLayoutComponent(Component paramComponent)
/*      */     {
/*      */     }
/*      */ 
/*      */     private Dimension insetSize(Container paramContainer)
/*      */     {
/*  792 */       Insets localInsets = paramContainer.getInsets();
/*  793 */       int i = localInsets.left + localInsets.right;
/*  794 */       int j = localInsets.top + localInsets.bottom;
/*  795 */       return new Dimension(i, j);
/*      */     }
/*      */ 
/*      */     public Dimension preferredLayoutSize(Container paramContainer)
/*      */     {
/*  808 */       Dimension localDimension1 = insetSize(paramContainer);
/*  809 */       if (paramContainer.getComponentCount() > 0) {
/*  810 */         Dimension localDimension2 = getComponent(0).getPreferredSize();
/*  811 */         localDimension1.width += localDimension2.width;
/*  812 */         localDimension1.height += localDimension2.height;
/*      */       }
/*  814 */       return localDimension1;
/*      */     }
/*      */ 
/*      */     public Dimension minimumLayoutSize(Container paramContainer)
/*      */     {
/*  827 */       Dimension localDimension1 = insetSize(paramContainer);
/*  828 */       if (paramContainer.getComponentCount() > 0) {
/*  829 */         Dimension localDimension2 = getComponent(0).getMinimumSize();
/*  830 */         localDimension1.width += localDimension2.width;
/*  831 */         localDimension1.height += localDimension2.height;
/*      */       }
/*  833 */       return localDimension1;
/*      */     }
/*      */ 
/*      */     public void layoutContainer(Container paramContainer)
/*      */     {
/*  842 */       if (paramContainer.getComponentCount() > 0) {
/*  843 */         Insets localInsets = paramContainer.getInsets();
/*  844 */         int i = paramContainer.getWidth() - (localInsets.left + localInsets.right);
/*  845 */         int j = paramContainer.getHeight() - (localInsets.top + localInsets.bottom);
/*  846 */         getComponent(0).setBounds(localInsets.left, localInsets.top, i, j);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void commitEdit()
/*      */       throws ParseException
/*      */     {
/*  862 */       JFormattedTextField localJFormattedTextField = getTextField();
/*      */ 
/*  864 */       localJFormattedTextField.commitEdit();
/*      */     }
/*      */ 
/*      */     public int getBaseline(int paramInt1, int paramInt2)
/*      */     {
/*  877 */       super.getBaseline(paramInt1, paramInt2);
/*  878 */       Insets localInsets = getInsets();
/*  879 */       paramInt1 = paramInt1 - localInsets.left - localInsets.right;
/*  880 */       paramInt2 = paramInt2 - localInsets.top - localInsets.bottom;
/*  881 */       int i = getComponent(0).getBaseline(paramInt1, paramInt2);
/*  882 */       if (i >= 0) {
/*  883 */         return i + localInsets.top;
/*      */       }
/*  885 */       return -1;
/*      */     }
/*      */ 
/*      */     public Component.BaselineResizeBehavior getBaselineResizeBehavior()
/*      */     {
/*  897 */       return getComponent(0).getBaselineResizeBehavior();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class DisabledAction
/*      */     implements Action
/*      */   {
/*      */     public Object getValue(String paramString)
/*      */     {
/* 1370 */       return null;
/*      */     }
/*      */     public void putValue(String paramString, Object paramObject) {
/*      */     }
/*      */     public void setEnabled(boolean paramBoolean) {
/*      */     }
/*      */     public boolean isEnabled() {
/* 1377 */       return false;
/*      */     }
/*      */ 
/*      */     public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class ListEditor extends JSpinner.DefaultEditor
/*      */   {
/*      */     public ListEditor(JSpinner paramJSpinner)
/*      */     {
/* 1282 */       super();
/* 1283 */       if (!(paramJSpinner.getModel() instanceof SpinnerListModel)) {
/* 1284 */         throw new IllegalArgumentException("model not a SpinnerListModel");
/*      */       }
/* 1286 */       getTextField().setEditable(true);
/* 1287 */       getTextField().setFormatterFactory(new DefaultFormatterFactory(new ListFormatter(null)));
/*      */     }
/*      */ 
/*      */     public SpinnerListModel getModel()
/*      */     {
/* 1299 */       return (SpinnerListModel)getSpinner().getModel();
/*      */     }
/*      */ 
/*      */     private class ListFormatter extends JFormattedTextField.AbstractFormatter
/*      */     {
/*      */       private DocumentFilter filter;
/*      */ 
/*      */       private ListFormatter()
/*      */       {
/*      */       }
/*      */ 
/*      */       public String valueToString(Object paramObject)
/*      */         throws ParseException
/*      */       {
/* 1314 */         if (paramObject == null) {
/* 1315 */           return "";
/*      */         }
/* 1317 */         return paramObject.toString();
/*      */       }
/*      */ 
/*      */       public Object stringToValue(String paramString) throws ParseException {
/* 1321 */         return paramString;
/*      */       }
/*      */ 
/*      */       protected DocumentFilter getDocumentFilter() {
/* 1325 */         if (this.filter == null) {
/* 1326 */           this.filter = new Filter(null);
/*      */         }
/* 1328 */         return this.filter;
/*      */       }
/*      */ 
/*      */       private class Filter extends DocumentFilter {
/*      */         private Filter() {
/*      */         }
/*      */ 
/*      */         public void replace(DocumentFilter.FilterBypass paramFilterBypass, int paramInt1, int paramInt2, String paramString, AttributeSet paramAttributeSet) throws BadLocationException {
/* 1336 */           if ((paramString != null) && (paramInt1 + paramInt2 == paramFilterBypass.getDocument().getLength()))
/*      */           {
/* 1338 */             Object localObject = JSpinner.ListEditor.this.getModel().findNextMatch(paramFilterBypass.getDocument().getText(0, paramInt1) + paramString);
/*      */ 
/* 1341 */             String str = localObject != null ? localObject.toString() : null;
/*      */ 
/* 1343 */             if (str != null) {
/* 1344 */               paramFilterBypass.remove(0, paramInt1 + paramInt2);
/* 1345 */               paramFilterBypass.insertString(0, str, null);
/* 1346 */               JSpinner.ListEditor.ListFormatter.this.getFormattedTextField().select(paramInt1 + paramString.length(), str.length());
/*      */ 
/* 1349 */               return;
/*      */             }
/*      */           }
/* 1352 */           super.replace(paramFilterBypass, paramInt1, paramInt2, paramString, paramAttributeSet);
/*      */         }
/*      */ 
/*      */         public void insertString(DocumentFilter.FilterBypass paramFilterBypass, int paramInt, String paramString, AttributeSet paramAttributeSet)
/*      */           throws BadLocationException
/*      */         {
/* 1358 */           replace(paramFilterBypass, paramInt, 0, paramString, paramAttributeSet);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ModelListener
/*      */     implements ChangeListener, Serializable
/*      */   {
/*      */     private ModelListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/*  385 */       JSpinner.this.fireStateChanged();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class NumberEditor extends JSpinner.DefaultEditor
/*      */   {
/*      */     private static String getDefaultPattern(Locale paramLocale)
/*      */     {
/* 1128 */       ResourceBundle localResourceBundle = LocaleData.getNumberFormatData(paramLocale);
/* 1129 */       String[] arrayOfString = localResourceBundle.getStringArray("NumberPatterns");
/* 1130 */       return arrayOfString[0];
/*      */     }
/*      */ 
/*      */     public NumberEditor(JSpinner paramJSpinner)
/*      */     {
/* 1150 */       this(paramJSpinner, getDefaultPattern(paramJSpinner.getLocale()));
/*      */     }
/*      */ 
/*      */     public NumberEditor(JSpinner paramJSpinner, String paramString)
/*      */     {
/* 1175 */       this(paramJSpinner, new DecimalFormat(paramString));
/*      */     }
/*      */ 
/*      */     private NumberEditor(JSpinner paramJSpinner, DecimalFormat paramDecimalFormat)
/*      */     {
/* 1199 */       super();
/* 1200 */       if (!(paramJSpinner.getModel() instanceof SpinnerNumberModel)) {
/* 1201 */         throw new IllegalArgumentException("model not a SpinnerNumberModel");
/*      */       }
/*      */ 
/* 1205 */       SpinnerNumberModel localSpinnerNumberModel = (SpinnerNumberModel)paramJSpinner.getModel();
/* 1206 */       JSpinner.NumberEditorFormatter localNumberEditorFormatter = new JSpinner.NumberEditorFormatter(localSpinnerNumberModel, paramDecimalFormat);
/*      */ 
/* 1208 */       DefaultFormatterFactory localDefaultFormatterFactory = new DefaultFormatterFactory(localNumberEditorFormatter);
/*      */ 
/* 1210 */       JFormattedTextField localJFormattedTextField = getTextField();
/* 1211 */       localJFormattedTextField.setEditable(true);
/* 1212 */       localJFormattedTextField.setFormatterFactory(localDefaultFormatterFactory);
/* 1213 */       localJFormattedTextField.setHorizontalAlignment(4);
/*      */       try
/*      */       {
/* 1220 */         String str1 = localNumberEditorFormatter.valueToString(localSpinnerNumberModel.getMinimum());
/* 1221 */         String str2 = localNumberEditorFormatter.valueToString(localSpinnerNumberModel.getMaximum());
/* 1222 */         localJFormattedTextField.setColumns(Math.max(str1.length(), str2.length()));
/*      */       }
/*      */       catch (ParseException localParseException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*      */     public DecimalFormat getFormat()
/*      */     {
/* 1242 */       return (DecimalFormat)((NumberFormatter)getTextField().getFormatter()).getFormat();
/*      */     }
/*      */ 
/*      */     public SpinnerNumberModel getModel()
/*      */     {
/* 1254 */       return (SpinnerNumberModel)getSpinner().getModel();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class NumberEditorFormatter extends NumberFormatter
/*      */   {
/*      */     private final SpinnerNumberModel model;
/*      */ 
/*      */     NumberEditorFormatter(SpinnerNumberModel paramSpinnerNumberModel, NumberFormat paramNumberFormat)
/*      */     {
/* 1088 */       super();
/* 1089 */       this.model = paramSpinnerNumberModel;
/* 1090 */       setValueClass(paramSpinnerNumberModel.getValue().getClass());
/*      */     }
/*      */ 
/*      */     public void setMinimum(Comparable paramComparable) {
/* 1094 */       this.model.setMinimum(paramComparable);
/*      */     }
/*      */ 
/*      */     public Comparable getMinimum() {
/* 1098 */       return this.model.getMinimum();
/*      */     }
/*      */ 
/*      */     public void setMaximum(Comparable paramComparable) {
/* 1102 */       this.model.setMaximum(paramComparable);
/*      */     }
/*      */ 
/*      */     public Comparable getMaximum() {
/* 1106 */       return this.model.getMaximum();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JSpinner
 * JD-Core Version:    0.6.2
 */