/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Adjustable;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.AdjustmentEvent;
/*     */ import java.awt.event.AdjustmentListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleState;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import javax.accessibility.AccessibleValue;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.ScrollBarUI;
/*     */ 
/*     */ public class JScrollBar extends JComponent
/*     */   implements Adjustable, Accessible
/*     */ {
/*     */   private static final String uiClassID = "ScrollBarUI";
/*  93 */   private ChangeListener fwdAdjustmentEvents = new ModelListener(null);
/*     */   protected BoundedRangeModel model;
/*     */   protected int orientation;
/*     */   protected int unitIncrement;
/*     */   protected int blockIncrement;
/*     */ 
/*     */   private void checkOrientation(int paramInt)
/*     */   {
/* 123 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/* 126 */       break;
/*     */     default:
/* 128 */       throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
/*     */     }
/*     */   }
/*     */ 
/*     */   public JScrollBar(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 154 */     checkOrientation(paramInt1);
/* 155 */     this.unitIncrement = 1;
/* 156 */     this.blockIncrement = (paramInt3 == 0 ? 1 : paramInt3);
/* 157 */     this.orientation = paramInt1;
/* 158 */     this.model = new DefaultBoundedRangeModel(paramInt2, paramInt3, paramInt4, paramInt5);
/* 159 */     this.model.addChangeListener(this.fwdAdjustmentEvents);
/* 160 */     setRequestFocusEnabled(false);
/* 161 */     updateUI();
/*     */   }
/*     */ 
/*     */   public JScrollBar(int paramInt)
/*     */   {
/* 176 */     this(paramInt, 0, 10, 0, 100);
/*     */   }
/*     */ 
/*     */   public JScrollBar()
/*     */   {
/* 190 */     this(1);
/*     */   }
/*     */ 
/*     */   public void setUI(ScrollBarUI paramScrollBarUI)
/*     */   {
/* 207 */     super.setUI(paramScrollBarUI);
/*     */   }
/*     */ 
/*     */   public ScrollBarUI getUI()
/*     */   {
/* 218 */     return (ScrollBarUI)this.ui;
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 227 */     setUI((ScrollBarUI)UIManager.getUI(this));
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 239 */     return "ScrollBarUI";
/*     */   }
/*     */ 
/*     */   public int getOrientation()
/*     */   {
/* 252 */     return this.orientation;
/*     */   }
/*     */ 
/*     */   public void setOrientation(int paramInt)
/*     */   {
/* 272 */     checkOrientation(paramInt);
/* 273 */     int i = this.orientation;
/* 274 */     this.orientation = paramInt;
/* 275 */     firePropertyChange("orientation", i, paramInt);
/*     */ 
/* 277 */     if ((i != paramInt) && (this.accessibleContext != null)) {
/* 278 */       this.accessibleContext.firePropertyChange("AccessibleState", i == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL, paramInt == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL);
/*     */     }
/*     */ 
/* 285 */     if (paramInt != i)
/* 286 */       revalidate();
/*     */   }
/*     */ 
/*     */   public BoundedRangeModel getModel()
/*     */   {
/* 298 */     return this.model;
/*     */   }
/*     */ 
/*     */   public void setModel(BoundedRangeModel paramBoundedRangeModel)
/*     */   {
/* 313 */     Integer localInteger = null;
/* 314 */     BoundedRangeModel localBoundedRangeModel = this.model;
/* 315 */     if (this.model != null) {
/* 316 */       this.model.removeChangeListener(this.fwdAdjustmentEvents);
/* 317 */       localInteger = Integer.valueOf(this.model.getValue());
/*     */     }
/* 319 */     this.model = paramBoundedRangeModel;
/* 320 */     if (this.model != null) {
/* 321 */       this.model.addChangeListener(this.fwdAdjustmentEvents);
/*     */     }
/*     */ 
/* 324 */     firePropertyChange("model", localBoundedRangeModel, this.model);
/*     */ 
/* 326 */     if (this.accessibleContext != null)
/* 327 */       this.accessibleContext.firePropertyChange("AccessibleValue", localInteger, new Integer(this.model.getValue()));
/*     */   }
/*     */ 
/*     */   public int getUnitIncrement(int paramInt)
/*     */   {
/* 355 */     return this.unitIncrement;
/*     */   }
/*     */ 
/*     */   public void setUnitIncrement(int paramInt)
/*     */   {
/* 371 */     int i = this.unitIncrement;
/* 372 */     this.unitIncrement = paramInt;
/* 373 */     firePropertyChange("unitIncrement", i, paramInt);
/*     */   }
/*     */ 
/*     */   public int getBlockIncrement(int paramInt)
/*     */   {
/* 398 */     return this.blockIncrement;
/*     */   }
/*     */ 
/*     */   public void setBlockIncrement(int paramInt)
/*     */   {
/* 414 */     int i = this.blockIncrement;
/* 415 */     this.blockIncrement = paramInt;
/* 416 */     firePropertyChange("blockIncrement", i, paramInt);
/*     */   }
/*     */ 
/*     */   public int getUnitIncrement()
/*     */   {
/* 426 */     return this.unitIncrement;
/*     */   }
/*     */ 
/*     */   public int getBlockIncrement()
/*     */   {
/* 436 */     return this.blockIncrement;
/*     */   }
/*     */ 
/*     */   public int getValue()
/*     */   {
/* 446 */     return getModel().getValue();
/*     */   }
/*     */ 
/*     */   public void setValue(int paramInt)
/*     */   {
/* 461 */     BoundedRangeModel localBoundedRangeModel = getModel();
/* 462 */     int i = localBoundedRangeModel.getValue();
/* 463 */     localBoundedRangeModel.setValue(paramInt);
/*     */ 
/* 465 */     if (this.accessibleContext != null)
/* 466 */       this.accessibleContext.firePropertyChange("AccessibleValue", Integer.valueOf(i), Integer.valueOf(localBoundedRangeModel.getValue()));
/*     */   }
/*     */ 
/*     */   public int getVisibleAmount()
/*     */   {
/* 483 */     return getModel().getExtent();
/*     */   }
/*     */ 
/*     */   public void setVisibleAmount(int paramInt)
/*     */   {
/* 497 */     getModel().setExtent(paramInt);
/*     */   }
/*     */ 
/*     */   public int getMinimum()
/*     */   {
/* 509 */     return getModel().getMinimum();
/*     */   }
/*     */ 
/*     */   public void setMinimum(int paramInt)
/*     */   {
/* 523 */     getModel().setMinimum(paramInt);
/*     */   }
/*     */ 
/*     */   public int getMaximum()
/*     */   {
/* 534 */     return getModel().getMaximum();
/*     */   }
/*     */ 
/*     */   public void setMaximum(int paramInt)
/*     */   {
/* 549 */     getModel().setMaximum(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean getValueIsAdjusting()
/*     */   {
/* 560 */     return getModel().getValueIsAdjusting();
/*     */   }
/*     */ 
/*     */   public void setValueIsAdjusting(boolean paramBoolean)
/*     */   {
/* 578 */     BoundedRangeModel localBoundedRangeModel = getModel();
/* 579 */     boolean bool = localBoundedRangeModel.getValueIsAdjusting();
/* 580 */     localBoundedRangeModel.setValueIsAdjusting(paramBoolean);
/*     */ 
/* 582 */     if ((bool != paramBoolean) && (this.accessibleContext != null))
/* 583 */       this.accessibleContext.firePropertyChange("AccessibleState", bool ? AccessibleState.BUSY : null, paramBoolean ? AccessibleState.BUSY : null);
/*     */   }
/*     */ 
/*     */   public void setValues(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 607 */     BoundedRangeModel localBoundedRangeModel = getModel();
/* 608 */     int i = localBoundedRangeModel.getValue();
/* 609 */     localBoundedRangeModel.setRangeProperties(paramInt1, paramInt2, paramInt3, paramInt4, localBoundedRangeModel.getValueIsAdjusting());
/*     */ 
/* 611 */     if (this.accessibleContext != null)
/* 612 */       this.accessibleContext.firePropertyChange("AccessibleValue", Integer.valueOf(i), Integer.valueOf(localBoundedRangeModel.getValue()));
/*     */   }
/*     */ 
/*     */   public void addAdjustmentListener(AdjustmentListener paramAdjustmentListener)
/*     */   {
/* 638 */     this.listenerList.add(AdjustmentListener.class, paramAdjustmentListener);
/*     */   }
/*     */ 
/*     */   public void removeAdjustmentListener(AdjustmentListener paramAdjustmentListener)
/*     */   {
/* 649 */     this.listenerList.remove(AdjustmentListener.class, paramAdjustmentListener);
/*     */   }
/*     */ 
/*     */   public AdjustmentListener[] getAdjustmentListeners()
/*     */   {
/* 662 */     return (AdjustmentListener[])this.listenerList.getListeners(AdjustmentListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireAdjustmentValueChanged(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 673 */     fireAdjustmentValueChanged(paramInt1, paramInt2, paramInt3, getValueIsAdjusting());
/*     */   }
/*     */ 
/*     */   private void fireAdjustmentValueChanged(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */   {
/* 684 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 685 */     AdjustmentEvent localAdjustmentEvent = null;
/* 686 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 687 */       if (arrayOfObject[i] == AdjustmentListener.class) {
/* 688 */         if (localAdjustmentEvent == null) {
/* 689 */           localAdjustmentEvent = new AdjustmentEvent(this, paramInt1, paramInt2, paramInt3, paramBoolean);
/*     */         }
/* 691 */         ((AdjustmentListener)arrayOfObject[(i + 1)]).adjustmentValueChanged(localAdjustmentEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize()
/*     */   {
/* 725 */     Dimension localDimension = getPreferredSize();
/* 726 */     if (this.orientation == 1) {
/* 727 */       return new Dimension(localDimension.width, 5);
/*     */     }
/* 729 */     return new Dimension(5, localDimension.height);
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize()
/*     */   {
/* 738 */     Dimension localDimension = getPreferredSize();
/* 739 */     if (getOrientation() == 1) {
/* 740 */       return new Dimension(localDimension.width, 32767);
/*     */     }
/* 742 */     return new Dimension(32767, localDimension.height);
/*     */   }
/*     */ 
/*     */   public void setEnabled(boolean paramBoolean)
/*     */   {
/* 754 */     super.setEnabled(paramBoolean);
/* 755 */     Component[] arrayOfComponent1 = getComponents();
/* 756 */     for (Component localComponent : arrayOfComponent1)
/* 757 */       localComponent.setEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 766 */     paramObjectOutputStream.defaultWriteObject();
/* 767 */     if (getUIClassID().equals("ScrollBarUI")) {
/* 768 */       byte b = JComponent.getWriteObjCounter(this);
/* 769 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 770 */       if ((b == 0) && (this.ui != null))
/* 771 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 787 */     String str = this.orientation == 0 ? "HORIZONTAL" : "VERTICAL";
/*     */ 
/* 790 */     return super.paramString() + ",blockIncrement=" + this.blockIncrement + ",orientation=" + str + ",unitIncrement=" + this.unitIncrement;
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 810 */     if (this.accessibleContext == null) {
/* 811 */       this.accessibleContext = new AccessibleJScrollBar();
/*     */     }
/* 813 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJScrollBar extends JComponent.AccessibleJComponent
/*     */     implements AccessibleValue
/*     */   {
/*     */     protected AccessibleJScrollBar()
/*     */     {
/* 831 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 842 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 843 */       if (JScrollBar.this.getValueIsAdjusting()) {
/* 844 */         localAccessibleStateSet.add(AccessibleState.BUSY);
/*     */       }
/* 846 */       if (JScrollBar.this.getOrientation() == 1)
/* 847 */         localAccessibleStateSet.add(AccessibleState.VERTICAL);
/*     */       else {
/* 849 */         localAccessibleStateSet.add(AccessibleState.HORIZONTAL);
/*     */       }
/* 851 */       return localAccessibleStateSet;
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 861 */       return AccessibleRole.SCROLL_BAR;
/*     */     }
/*     */ 
/*     */     public AccessibleValue getAccessibleValue()
/*     */     {
/* 873 */       return this;
/*     */     }
/*     */ 
/*     */     public Number getCurrentAccessibleValue()
/*     */     {
/* 882 */       return Integer.valueOf(JScrollBar.this.getValue());
/*     */     }
/*     */ 
/*     */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*     */     {
/* 892 */       if (paramNumber == null) {
/* 893 */         return false;
/*     */       }
/* 895 */       JScrollBar.this.setValue(paramNumber.intValue());
/* 896 */       return true;
/*     */     }
/*     */ 
/*     */     public Number getMinimumAccessibleValue()
/*     */     {
/* 905 */       return Integer.valueOf(JScrollBar.this.getMinimum());
/*     */     }
/*     */ 
/*     */     public Number getMaximumAccessibleValue()
/*     */     {
/* 915 */       return new Integer(JScrollBar.this.model.getMaximum() - JScrollBar.this.model.getExtent());
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ModelListener
/*     */     implements ChangeListener, Serializable
/*     */   {
/*     */     private ModelListener()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void stateChanged(ChangeEvent paramChangeEvent)
/*     */     {
/* 706 */       Object localObject = paramChangeEvent.getSource();
/* 707 */       if ((localObject instanceof BoundedRangeModel)) {
/* 708 */         int i = 601;
/* 709 */         int j = 5;
/* 710 */         BoundedRangeModel localBoundedRangeModel = (BoundedRangeModel)localObject;
/* 711 */         int k = localBoundedRangeModel.getValue();
/* 712 */         boolean bool = localBoundedRangeModel.getValueIsAdjusting();
/* 713 */         JScrollBar.this.fireAdjustmentValueChanged(i, j, k, bool);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JScrollBar
 * JD-Core Version:    0.6.2
 */