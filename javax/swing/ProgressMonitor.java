/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Frame;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.Locale;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleText;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.text.AttributeSet;
/*      */ 
/*      */ public class ProgressMonitor
/*      */   implements Accessible
/*      */ {
/*      */   private ProgressMonitor root;
/*      */   private JDialog dialog;
/*      */   private JOptionPane pane;
/*      */   private JProgressBar myBar;
/*      */   private JLabel noteLabel;
/*      */   private Component parentComponent;
/*      */   private String note;
/*   90 */   private Object[] cancelOption = null;
/*      */   private Object message;
/*      */   private long T0;
/*   93 */   private int millisToDecideToPopup = 500;
/*   94 */   private int millisToPopup = 2000;
/*      */   private int min;
/*      */   private int max;
/*  463 */   protected AccessibleContext accessibleContext = null;
/*      */ 
/*  465 */   private AccessibleContext accessibleJOptionPane = null;
/*      */ 
/*      */   public ProgressMonitor(Component paramComponent, Object paramObject, String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  127 */     this(paramComponent, paramObject, paramString, paramInt1, paramInt2, null);
/*      */   }
/*      */ 
/*      */   private ProgressMonitor(Component paramComponent, Object paramObject, String paramString, int paramInt1, int paramInt2, ProgressMonitor paramProgressMonitor)
/*      */   {
/*  137 */     this.min = paramInt1;
/*  138 */     this.max = paramInt2;
/*  139 */     this.parentComponent = paramComponent;
/*      */ 
/*  141 */     this.cancelOption = new Object[1];
/*  142 */     this.cancelOption[0] = UIManager.getString("OptionPane.cancelButtonText");
/*      */ 
/*  144 */     this.message = paramObject;
/*  145 */     this.note = paramString;
/*  146 */     if (paramProgressMonitor != null) {
/*  147 */       this.root = (paramProgressMonitor.root != null ? paramProgressMonitor.root : paramProgressMonitor);
/*  148 */       this.T0 = this.root.T0;
/*  149 */       this.dialog = this.root.dialog;
/*      */     }
/*      */     else {
/*  152 */       this.T0 = System.currentTimeMillis();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setProgress(int paramInt)
/*      */   {
/*  263 */     if (paramInt >= this.max) {
/*  264 */       close();
/*      */     }
/*  267 */     else if (this.myBar != null) {
/*  268 */       this.myBar.setValue(paramInt);
/*      */     }
/*      */     else {
/*  271 */       long l1 = System.currentTimeMillis();
/*  272 */       long l2 = (int)(l1 - this.T0);
/*  273 */       if (l2 >= this.millisToDecideToPopup)
/*      */       {
/*      */         int i;
/*  275 */         if (paramInt > this.min) {
/*  276 */           i = (int)(l2 * (this.max - this.min) / (paramInt - this.min));
/*      */         }
/*      */         else
/*      */         {
/*  281 */           i = this.millisToPopup;
/*      */         }
/*  283 */         if (i >= this.millisToPopup) {
/*  284 */           this.myBar = new JProgressBar();
/*  285 */           this.myBar.setMinimum(this.min);
/*  286 */           this.myBar.setMaximum(this.max);
/*  287 */           this.myBar.setValue(paramInt);
/*  288 */           if (this.note != null) this.noteLabel = new JLabel(this.note);
/*  289 */           this.pane = new ProgressOptionPane(new Object[] { this.message, this.noteLabel, this.myBar });
/*      */ 
/*  292 */           this.dialog = this.pane.createDialog(this.parentComponent, UIManager.getString("ProgressMonitor.progressText"));
/*      */ 
/*  295 */           this.dialog.show();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/*  309 */     if (this.dialog != null) {
/*  310 */       this.dialog.setVisible(false);
/*  311 */       this.dialog.dispose();
/*  312 */       this.dialog = null;
/*  313 */       this.pane = null;
/*  314 */       this.myBar = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getMinimum()
/*      */   {
/*  326 */     return this.min;
/*      */   }
/*      */ 
/*      */   public void setMinimum(int paramInt)
/*      */   {
/*  337 */     if (this.myBar != null) {
/*  338 */       this.myBar.setMinimum(paramInt);
/*      */     }
/*  340 */     this.min = paramInt;
/*      */   }
/*      */ 
/*      */   public int getMaximum()
/*      */   {
/*  351 */     return this.max;
/*      */   }
/*      */ 
/*      */   public void setMaximum(int paramInt)
/*      */   {
/*  362 */     if (this.myBar != null) {
/*  363 */       this.myBar.setMaximum(paramInt);
/*      */     }
/*  365 */     this.max = paramInt;
/*      */   }
/*      */ 
/*      */   public boolean isCanceled()
/*      */   {
/*  373 */     if (this.pane == null) return false;
/*  374 */     Object localObject = this.pane.getValue();
/*  375 */     return (localObject != null) && (this.cancelOption.length == 1) && (localObject.equals(this.cancelOption[0]));
/*      */   }
/*      */ 
/*      */   public void setMillisToDecideToPopup(int paramInt)
/*      */   {
/*  390 */     this.millisToDecideToPopup = paramInt;
/*      */   }
/*      */ 
/*      */   public int getMillisToDecideToPopup()
/*      */   {
/*  401 */     return this.millisToDecideToPopup;
/*      */   }
/*      */ 
/*      */   public void setMillisToPopup(int paramInt)
/*      */   {
/*  414 */     this.millisToPopup = paramInt;
/*      */   }
/*      */ 
/*      */   public int getMillisToPopup()
/*      */   {
/*  424 */     return this.millisToPopup;
/*      */   }
/*      */ 
/*      */   public void setNote(String paramString)
/*      */   {
/*  437 */     this.note = paramString;
/*  438 */     if (this.noteLabel != null)
/*  439 */       this.noteLabel.setText(paramString);
/*      */   }
/*      */ 
/*      */   public String getNote()
/*      */   {
/*  452 */     return this.note;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/*  476 */     if (this.accessibleContext == null) {
/*  477 */       this.accessibleContext = new AccessibleProgressMonitor();
/*      */     }
/*  479 */     if ((this.pane != null) && (this.accessibleJOptionPane == null))
/*      */     {
/*  486 */       if ((this.accessibleContext instanceof AccessibleProgressMonitor)) {
/*  487 */         ((AccessibleProgressMonitor)this.accessibleContext).optionPaneCreated();
/*      */       }
/*      */     }
/*  490 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleProgressMonitor extends AccessibleContext
/*      */     implements AccessibleText, ChangeListener, PropertyChangeListener
/*      */   {
/*      */     private Object oldModelValue;
/*      */ 
/*      */     protected AccessibleProgressMonitor()
/*      */     {
/*      */     }
/*      */ 
/*      */     private void optionPaneCreated()
/*      */     {
/*  548 */       ProgressMonitor.this.accessibleJOptionPane = ProgressMonitor.ProgressOptionPane.access$400((ProgressMonitor.ProgressOptionPane)ProgressMonitor.this.pane);
/*      */ 
/*  552 */       if (ProgressMonitor.this.myBar != null) {
/*  553 */         ProgressMonitor.this.myBar.addChangeListener(this);
/*      */       }
/*      */ 
/*  557 */       if (ProgressMonitor.this.noteLabel != null)
/*  558 */         ProgressMonitor.this.noteLabel.addPropertyChangeListener(this);
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/*  569 */       if (paramChangeEvent == null) {
/*  570 */         return;
/*      */       }
/*  572 */       if (ProgressMonitor.this.myBar != null)
/*      */       {
/*  574 */         Integer localInteger = Integer.valueOf(ProgressMonitor.this.myBar.getValue());
/*  575 */         firePropertyChange("AccessibleValue", this.oldModelValue, localInteger);
/*      */ 
/*  578 */         this.oldModelValue = localInteger;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     {
/*  590 */       if ((paramPropertyChangeEvent.getSource() == ProgressMonitor.this.noteLabel) && (paramPropertyChangeEvent.getPropertyName() == "text"))
/*      */       {
/*  592 */         firePropertyChange("AccessibleText", null, Integer.valueOf(0));
/*      */       }
/*      */     }
/*      */ 
/*      */     public String getAccessibleName()
/*      */     {
/*  614 */       if (this.accessibleName != null)
/*  615 */         return this.accessibleName;
/*  616 */       if (ProgressMonitor.this.accessibleJOptionPane != null)
/*      */       {
/*  618 */         return ProgressMonitor.this.accessibleJOptionPane.getAccessibleName();
/*      */       }
/*  620 */       return null;
/*      */     }
/*      */ 
/*      */     public String getAccessibleDescription()
/*      */     {
/*  636 */       if (this.accessibleDescription != null)
/*  637 */         return this.accessibleDescription;
/*  638 */       if (ProgressMonitor.this.accessibleJOptionPane != null)
/*      */       {
/*  640 */         return ProgressMonitor.this.accessibleJOptionPane.getAccessibleDescription();
/*      */       }
/*  642 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/*  664 */       return AccessibleRole.PROGRESS_MONITOR;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/*  680 */       if (ProgressMonitor.this.accessibleJOptionPane != null)
/*      */       {
/*  682 */         return ProgressMonitor.this.accessibleJOptionPane.getAccessibleStateSet();
/*      */       }
/*  684 */       return null;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleParent()
/*      */     {
/*  694 */       return ProgressMonitor.this.dialog;
/*      */     }
/*      */ 
/*      */     private AccessibleContext getParentAccessibleContext()
/*      */     {
/*  701 */       if (ProgressMonitor.this.dialog != null) {
/*  702 */         return ProgressMonitor.this.dialog.getAccessibleContext();
/*      */       }
/*  704 */       return null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleIndexInParent()
/*      */     {
/*  718 */       if (ProgressMonitor.this.accessibleJOptionPane != null)
/*      */       {
/*  720 */         return ProgressMonitor.this.accessibleJOptionPane.getAccessibleIndexInParent();
/*      */       }
/*  722 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/*  733 */       AccessibleContext localAccessibleContext = getPanelAccessibleContext();
/*  734 */       if (localAccessibleContext != null) {
/*  735 */         return localAccessibleContext.getAccessibleChildrenCount();
/*      */       }
/*  737 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/*  753 */       AccessibleContext localAccessibleContext = getPanelAccessibleContext();
/*  754 */       if (localAccessibleContext != null) {
/*  755 */         return localAccessibleContext.getAccessibleChild(paramInt);
/*      */       }
/*  757 */       return null;
/*      */     }
/*      */ 
/*      */     private AccessibleContext getPanelAccessibleContext()
/*      */     {
/*  765 */       if (ProgressMonitor.this.myBar != null) {
/*  766 */         Container localContainer = ProgressMonitor.this.myBar.getParent();
/*  767 */         if ((localContainer instanceof Accessible)) {
/*  768 */           return localContainer.getAccessibleContext();
/*      */         }
/*      */       }
/*  771 */       return null;
/*      */     }
/*      */ 
/*      */     public Locale getLocale()
/*      */       throws IllegalComponentStateException
/*      */     {
/*  787 */       if (ProgressMonitor.this.accessibleJOptionPane != null)
/*      */       {
/*  789 */         return ProgressMonitor.this.accessibleJOptionPane.getLocale();
/*      */       }
/*  791 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleComponent getAccessibleComponent()
/*      */     {
/*  804 */       if (ProgressMonitor.this.accessibleJOptionPane != null)
/*      */       {
/*  806 */         return ProgressMonitor.this.accessibleJOptionPane.getAccessibleComponent();
/*      */       }
/*  808 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleValue getAccessibleValue()
/*      */     {
/*  819 */       if (ProgressMonitor.this.myBar != null)
/*      */       {
/*  821 */         return ProgressMonitor.this.myBar.getAccessibleContext().getAccessibleValue();
/*      */       }
/*  823 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleText getAccessibleText()
/*      */     {
/*  834 */       if (getNoteLabelAccessibleText() != null) {
/*  835 */         return this;
/*      */       }
/*  837 */       return null;
/*      */     }
/*      */ 
/*      */     private AccessibleText getNoteLabelAccessibleText()
/*      */     {
/*  844 */       if (ProgressMonitor.this.noteLabel != null)
/*      */       {
/*  847 */         return ProgressMonitor.this.noteLabel.getAccessibleContext().getAccessibleText();
/*      */       }
/*  849 */       return null;
/*      */     }
/*      */ 
/*      */     public int getIndexAtPoint(Point paramPoint)
/*      */     {
/*  864 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/*  865 */       if ((localAccessibleText != null) && (sameWindowAncestor(ProgressMonitor.this.pane, ProgressMonitor.this.noteLabel)))
/*      */       {
/*  868 */         Point localPoint = SwingUtilities.convertPoint(ProgressMonitor.this.pane, paramPoint, ProgressMonitor.this.noteLabel);
/*      */ 
/*  871 */         if (localPoint != null) {
/*  872 */           return localAccessibleText.getIndexAtPoint(localPoint);
/*      */         }
/*      */       }
/*  875 */       return -1;
/*      */     }
/*      */ 
/*      */     public Rectangle getCharacterBounds(int paramInt)
/*      */     {
/*  888 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/*  889 */       if ((localAccessibleText != null) && (sameWindowAncestor(ProgressMonitor.this.pane, ProgressMonitor.this.noteLabel)))
/*      */       {
/*  891 */         Rectangle localRectangle = localAccessibleText.getCharacterBounds(paramInt);
/*  892 */         if (localRectangle != null) {
/*  893 */           return SwingUtilities.convertRectangle(ProgressMonitor.this.noteLabel, localRectangle, ProgressMonitor.this.pane);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  898 */       return null;
/*      */     }
/*      */ 
/*      */     private boolean sameWindowAncestor(Component paramComponent1, Component paramComponent2)
/*      */     {
/*  906 */       if ((paramComponent1 == null) || (paramComponent2 == null)) {
/*  907 */         return false;
/*      */       }
/*  909 */       return SwingUtilities.getWindowAncestor(paramComponent1) == SwingUtilities.getWindowAncestor(paramComponent2);
/*      */     }
/*      */ 
/*      */     public int getCharCount()
/*      */     {
/*  919 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/*  920 */       if (localAccessibleText != null) {
/*  921 */         return localAccessibleText.getCharCount();
/*      */       }
/*  923 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getCaretPosition()
/*      */     {
/*  934 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/*  935 */       if (localAccessibleText != null) {
/*  936 */         return localAccessibleText.getCaretPosition();
/*      */       }
/*  938 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getAtIndex(int paramInt1, int paramInt2)
/*      */     {
/*  949 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/*  950 */       if (localAccessibleText != null) {
/*  951 */         return localAccessibleText.getAtIndex(paramInt1, paramInt2);
/*      */       }
/*  953 */       return null;
/*      */     }
/*      */ 
/*      */     public String getAfterIndex(int paramInt1, int paramInt2)
/*      */     {
/*  964 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/*  965 */       if (localAccessibleText != null) {
/*  966 */         return localAccessibleText.getAfterIndex(paramInt1, paramInt2);
/*      */       }
/*  968 */       return null;
/*      */     }
/*      */ 
/*      */     public String getBeforeIndex(int paramInt1, int paramInt2)
/*      */     {
/*  979 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/*  980 */       if (localAccessibleText != null) {
/*  981 */         return localAccessibleText.getBeforeIndex(paramInt1, paramInt2);
/*      */       }
/*  983 */       return null;
/*      */     }
/*      */ 
/*      */     public AttributeSet getCharacterAttribute(int paramInt)
/*      */     {
/*  993 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/*  994 */       if (localAccessibleText != null) {
/*  995 */         return localAccessibleText.getCharacterAttribute(paramInt);
/*      */       }
/*  997 */       return null;
/*      */     }
/*      */ 
/*      */     public int getSelectionStart()
/*      */     {
/* 1008 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/* 1009 */       if (localAccessibleText != null) {
/* 1010 */         return localAccessibleText.getSelectionStart();
/*      */       }
/* 1012 */       return -1;
/*      */     }
/*      */ 
/*      */     public int getSelectionEnd()
/*      */     {
/* 1023 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/* 1024 */       if (localAccessibleText != null) {
/* 1025 */         return localAccessibleText.getSelectionEnd();
/*      */       }
/* 1027 */       return -1;
/*      */     }
/*      */ 
/*      */     public String getSelectedText()
/*      */     {
/* 1036 */       AccessibleText localAccessibleText = getNoteLabelAccessibleText();
/* 1037 */       if (localAccessibleText != null) {
/* 1038 */         return localAccessibleText.getSelectedText();
/*      */       }
/* 1040 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ProgressOptionPane extends JOptionPane
/*      */   {
/*      */     ProgressOptionPane(Object arg2)
/*      */     {
/*  160 */       super(1, -1, null, ProgressMonitor.this.cancelOption, null);
/*      */     }
/*      */ 
/*      */     public int getMaxCharactersPerLineCount()
/*      */     {
/*  170 */       return 60;
/*      */     }
/*      */ 
/*      */     public JDialog createDialog(Component paramComponent, String paramString)
/*      */     {
/*  181 */       Window localWindow = JOptionPane.getWindowForComponent(paramComponent);
/*      */       final JDialog localJDialog;
/*  182 */       if ((localWindow instanceof Frame))
/*  183 */         localJDialog = new JDialog((Frame)localWindow, paramString, false);
/*      */       else {
/*  185 */         localJDialog = new JDialog((Dialog)localWindow, paramString, false);
/*      */       }
/*  187 */       if ((localWindow instanceof SwingUtilities.SharedOwnerFrame)) {
/*  188 */         localObject = SwingUtilities.getSharedOwnerFrameShutdownListener();
/*      */ 
/*  190 */         localJDialog.addWindowListener((WindowListener)localObject);
/*      */       }
/*  192 */       Object localObject = localJDialog.getContentPane();
/*      */ 
/*  194 */       ((Container)localObject).setLayout(new BorderLayout());
/*  195 */       ((Container)localObject).add(this, "Center");
/*  196 */       localJDialog.pack();
/*  197 */       localJDialog.setLocationRelativeTo(paramComponent);
/*  198 */       localJDialog.addWindowListener(new WindowAdapter() {
/*  199 */         boolean gotFocus = false;
/*      */ 
/*      */         public void windowClosing(WindowEvent paramAnonymousWindowEvent) {
/*  202 */           ProgressMonitor.ProgressOptionPane.this.setValue(ProgressMonitor.this.cancelOption[0]);
/*      */         }
/*      */ 
/*      */         public void windowActivated(WindowEvent paramAnonymousWindowEvent)
/*      */         {
/*  207 */           if (!this.gotFocus) {
/*  208 */             ProgressMonitor.ProgressOptionPane.this.selectInitialValue();
/*  209 */             this.gotFocus = true;
/*      */           }
/*      */         }
/*      */       });
/*  214 */       addPropertyChangeListener(new PropertyChangeListener() {
/*      */         public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent) {
/*  216 */           if ((localJDialog.isVisible()) && (paramAnonymousPropertyChangeEvent.getSource() == ProgressMonitor.ProgressOptionPane.this) && ((paramAnonymousPropertyChangeEvent.getPropertyName().equals("value")) || (paramAnonymousPropertyChangeEvent.getPropertyName().equals("inputValue"))))
/*      */           {
/*  220 */             localJDialog.setVisible(false);
/*  221 */             localJDialog.dispose();
/*      */           }
/*      */         }
/*      */       });
/*  226 */       return localJDialog;
/*      */     }
/*      */ 
/*      */     public AccessibleContext getAccessibleContext()
/*      */     {
/*  240 */       return ProgressMonitor.this.getAccessibleContext();
/*      */     }
/*      */ 
/*      */     private AccessibleContext getAccessibleJOptionPane()
/*      */     {
/*  247 */       return super.getAccessibleContext();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ProgressMonitor
 * JD-Core Version:    0.6.2
 */