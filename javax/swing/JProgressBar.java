/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Graphics;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.Format;
/*      */ import java.text.NumberFormat;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.ProgressBarUI;
/*      */ 
/*      */ public class JProgressBar extends JComponent
/*      */   implements SwingConstants, Accessible
/*      */ {
/*      */   private static final String uiClassID = "ProgressBarUI";
/*      */   protected int orientation;
/*      */   protected boolean paintBorder;
/*      */   protected BoundedRangeModel model;
/*      */   protected String progressString;
/*      */   protected boolean paintString;
/*      */   private static final int defaultMinimum = 0;
/*      */   private static final int defaultMaximum = 100;
/*      */   private static final int defaultOrientation = 0;
/*  210 */   protected transient ChangeEvent changeEvent = null;
/*      */ 
/*  220 */   protected ChangeListener changeListener = null;
/*      */   private transient Format format;
/*      */   private boolean indeterminate;
/*      */ 
/*      */   public JProgressBar()
/*      */   {
/*  251 */     this(0);
/*      */   }
/*      */ 
/*      */   public JProgressBar(int paramInt)
/*      */   {
/*  274 */     this(paramInt, 0, 100);
/*      */   }
/*      */ 
/*      */   public JProgressBar(int paramInt1, int paramInt2)
/*      */   {
/*  301 */     this(0, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public JProgressBar(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  333 */     setModel(new DefaultBoundedRangeModel(paramInt2, 0, paramInt2, paramInt3));
/*  334 */     updateUI();
/*      */ 
/*  336 */     setOrientation(paramInt1);
/*  337 */     setBorderPainted(true);
/*  338 */     setStringPainted(false);
/*  339 */     setString(null);
/*  340 */     setIndeterminate(false);
/*      */   }
/*      */ 
/*      */   public JProgressBar(BoundedRangeModel paramBoundedRangeModel)
/*      */   {
/*  360 */     setModel(paramBoundedRangeModel);
/*  361 */     updateUI();
/*      */ 
/*  363 */     setOrientation(0);
/*  364 */     setBorderPainted(true);
/*  365 */     setStringPainted(false);
/*  366 */     setString(null);
/*  367 */     setIndeterminate(false);
/*      */   }
/*      */ 
/*      */   public int getOrientation()
/*      */   {
/*  381 */     return this.orientation;
/*      */   }
/*      */ 
/*      */   public void setOrientation(int paramInt)
/*      */   {
/*  403 */     if (this.orientation != paramInt) {
/*  404 */       switch (paramInt) {
/*      */       case 0:
/*      */       case 1:
/*  407 */         int i = this.orientation;
/*  408 */         this.orientation = paramInt;
/*  409 */         firePropertyChange("orientation", i, paramInt);
/*  410 */         if (this.accessibleContext != null)
/*  411 */           this.accessibleContext.firePropertyChange("AccessibleState", i == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL, this.orientation == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL); break;
/*      */       default:
/*  422 */         throw new IllegalArgumentException(paramInt + " is not a legal orientation");
/*      */       }
/*      */ 
/*  425 */       revalidate();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isStringPainted()
/*      */   {
/*  438 */     return this.paintString;
/*      */   }
/*      */ 
/*      */   public void setStringPainted(boolean paramBoolean)
/*      */   {
/*  462 */     boolean bool = this.paintString;
/*  463 */     this.paintString = paramBoolean;
/*  464 */     firePropertyChange("stringPainted", bool, this.paintString);
/*  465 */     if (this.paintString != bool) {
/*  466 */       revalidate();
/*  467 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getString()
/*      */   {
/*  483 */     if (this.progressString != null) {
/*  484 */       return this.progressString;
/*      */     }
/*  486 */     if (this.format == null) {
/*  487 */       this.format = NumberFormat.getPercentInstance();
/*      */     }
/*  489 */     return this.format.format(new Double(getPercentComplete()));
/*      */   }
/*      */ 
/*      */   public void setString(String paramString)
/*      */   {
/*  513 */     String str = this.progressString;
/*  514 */     this.progressString = paramString;
/*  515 */     firePropertyChange("string", str, this.progressString);
/*  516 */     if ((this.progressString == null) || (str == null) || (!this.progressString.equals(str)))
/*  517 */       repaint();
/*      */   }
/*      */ 
/*      */   public double getPercentComplete()
/*      */   {
/*  528 */     long l = this.model.getMaximum() - this.model.getMinimum();
/*  529 */     double d1 = this.model.getValue();
/*  530 */     double d2 = (d1 - this.model.getMinimum()) / l;
/*  531 */     return d2;
/*      */   }
/*      */ 
/*      */   public boolean isBorderPainted()
/*      */   {
/*  543 */     return this.paintBorder;
/*      */   }
/*      */ 
/*      */   public void setBorderPainted(boolean paramBoolean)
/*      */   {
/*  563 */     boolean bool = this.paintBorder;
/*  564 */     this.paintBorder = paramBoolean;
/*  565 */     firePropertyChange("borderPainted", bool, this.paintBorder);
/*  566 */     if (this.paintBorder != bool)
/*  567 */       repaint();
/*      */   }
/*      */ 
/*      */   protected void paintBorder(Graphics paramGraphics)
/*      */   {
/*  582 */     if (isBorderPainted())
/*  583 */       super.paintBorder(paramGraphics);
/*      */   }
/*      */ 
/*      */   public ProgressBarUI getUI()
/*      */   {
/*  594 */     return (ProgressBarUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(ProgressBarUI paramProgressBarUI)
/*      */   {
/*  609 */     super.setUI(paramProgressBarUI);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  619 */     setUI((ProgressBarUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  634 */     return "ProgressBarUI";
/*      */   }
/*      */ 
/*      */   protected ChangeListener createChangeListener()
/*      */   {
/*  672 */     return new ModelListener(null);
/*      */   }
/*      */ 
/*      */   public void addChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  681 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public void removeChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  690 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public ChangeListener[] getChangeListeners()
/*      */   {
/*  702 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireStateChanged()
/*      */   {
/*  721 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  724 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  725 */       if (arrayOfObject[i] == ChangeListener.class)
/*      */       {
/*  727 */         if (this.changeEvent == null)
/*  728 */           this.changeEvent = new ChangeEvent(this);
/*  729 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public BoundedRangeModel getModel()
/*      */   {
/*  742 */     return this.model;
/*      */   }
/*      */ 
/*      */   public void setModel(BoundedRangeModel paramBoundedRangeModel)
/*      */   {
/*  758 */     BoundedRangeModel localBoundedRangeModel = getModel();
/*      */ 
/*  760 */     if (paramBoundedRangeModel != localBoundedRangeModel) {
/*  761 */       if (localBoundedRangeModel != null) {
/*  762 */         localBoundedRangeModel.removeChangeListener(this.changeListener);
/*  763 */         this.changeListener = null;
/*      */       }
/*      */ 
/*  766 */       this.model = paramBoundedRangeModel;
/*      */ 
/*  768 */       if (paramBoundedRangeModel != null) {
/*  769 */         this.changeListener = createChangeListener();
/*  770 */         paramBoundedRangeModel.addChangeListener(this.changeListener);
/*      */       }
/*      */ 
/*  773 */       if (this.accessibleContext != null) {
/*  774 */         this.accessibleContext.firePropertyChange("AccessibleValue", localBoundedRangeModel == null ? null : Integer.valueOf(localBoundedRangeModel.getValue()), paramBoundedRangeModel == null ? null : Integer.valueOf(paramBoundedRangeModel.getValue()));
/*      */       }
/*      */ 
/*  782 */       if (this.model != null) {
/*  783 */         this.model.setExtent(0);
/*      */       }
/*  785 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getValue()
/*      */   {
/*  802 */     return getModel().getValue();
/*      */   }
/*      */ 
/*      */   public int getMinimum()
/*      */   {
/*  812 */     return getModel().getMinimum();
/*      */   }
/*      */ 
/*      */   public int getMaximum()
/*      */   {
/*  822 */     return getModel().getMaximum();
/*      */   }
/*      */ 
/*      */   public void setValue(int paramInt)
/*      */   {
/*  845 */     BoundedRangeModel localBoundedRangeModel = getModel();
/*  846 */     int i = localBoundedRangeModel.getValue();
/*  847 */     localBoundedRangeModel.setValue(paramInt);
/*      */ 
/*  849 */     if (this.accessibleContext != null)
/*  850 */       this.accessibleContext.firePropertyChange("AccessibleValue", Integer.valueOf(i), Integer.valueOf(localBoundedRangeModel.getValue()));
/*      */   }
/*      */ 
/*      */   public void setMinimum(int paramInt)
/*      */   {
/*  877 */     getModel().setMinimum(paramInt);
/*      */   }
/*      */ 
/*      */   public void setMaximum(int paramInt)
/*      */   {
/*  898 */     getModel().setMaximum(paramInt);
/*      */   }
/*      */ 
/*      */   public void setIndeterminate(boolean paramBoolean)
/*      */   {
/*  932 */     boolean bool = this.indeterminate;
/*  933 */     this.indeterminate = paramBoolean;
/*  934 */     firePropertyChange("indeterminate", bool, this.indeterminate);
/*      */   }
/*      */ 
/*      */   public boolean isIndeterminate()
/*      */   {
/*  950 */     return this.indeterminate;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  959 */     paramObjectOutputStream.defaultWriteObject();
/*  960 */     if (getUIClassID().equals("ProgressBarUI")) {
/*  961 */       byte b = JComponent.getWriteObjCounter(this);
/*  962 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/*  963 */       if ((b == 0) && (this.ui != null))
/*  964 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/*  980 */     String str1 = this.orientation == 0 ? "HORIZONTAL" : "VERTICAL";
/*      */ 
/*  982 */     String str2 = this.paintBorder ? "true" : "false";
/*      */ 
/*  984 */     String str3 = this.progressString != null ? this.progressString : "";
/*      */ 
/*  986 */     String str4 = this.paintString ? "true" : "false";
/*      */ 
/*  988 */     String str5 = this.indeterminate ? "true" : "false";
/*      */ 
/*  991 */     return super.paramString() + ",orientation=" + str1 + ",paintBorder=" + str2 + ",paintString=" + str4 + ",progressString=" + str3 + ",indeterminateString=" + str5;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1017 */     if (this.accessibleContext == null) {
/* 1018 */       this.accessibleContext = new AccessibleJProgressBar();
/*      */     }
/* 1020 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJProgressBar extends JComponent.AccessibleJComponent
/*      */     implements AccessibleValue
/*      */   {
/*      */     protected AccessibleJProgressBar()
/*      */     {
/* 1038 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 1049 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 1050 */       if (JProgressBar.this.getModel().getValueIsAdjusting()) {
/* 1051 */         localAccessibleStateSet.add(AccessibleState.BUSY);
/*      */       }
/* 1053 */       if (JProgressBar.this.getOrientation() == 1)
/* 1054 */         localAccessibleStateSet.add(AccessibleState.VERTICAL);
/*      */       else {
/* 1056 */         localAccessibleStateSet.add(AccessibleState.HORIZONTAL);
/*      */       }
/* 1058 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1068 */       return AccessibleRole.PROGRESS_BAR;
/*      */     }
/*      */ 
/*      */     public AccessibleValue getAccessibleValue()
/*      */     {
/* 1080 */       return this;
/*      */     }
/*      */ 
/*      */     public Number getCurrentAccessibleValue()
/*      */     {
/* 1089 */       return Integer.valueOf(JProgressBar.this.getValue());
/*      */     }
/*      */ 
/*      */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*      */     {
/* 1099 */       if (paramNumber == null) {
/* 1100 */         return false;
/*      */       }
/* 1102 */       JProgressBar.this.setValue(paramNumber.intValue());
/* 1103 */       return true;
/*      */     }
/*      */ 
/*      */     public Number getMinimumAccessibleValue()
/*      */     {
/* 1112 */       return Integer.valueOf(JProgressBar.this.getMinimum());
/*      */     }
/*      */ 
/*      */     public Number getMaximumAccessibleValue()
/*      */     {
/* 1122 */       return Integer.valueOf(JProgressBar.this.model.getMaximum() - JProgressBar.this.model.getExtent());
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
/*  652 */       JProgressBar.this.fireStateChanged();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JProgressBar
 * JD-Core Version:    0.6.2
 */