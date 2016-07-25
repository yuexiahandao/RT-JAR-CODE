/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Graphics;
/*      */ import java.beans.ConstructorProperties;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.SplitPaneUI;
/*      */ 
/*      */ public class JSplitPane extends JComponent
/*      */   implements Accessible
/*      */ {
/*      */   private static final String uiClassID = "SplitPaneUI";
/*      */   public static final int VERTICAL_SPLIT = 0;
/*      */   public static final int HORIZONTAL_SPLIT = 1;
/*      */   public static final String LEFT = "left";
/*      */   public static final String RIGHT = "right";
/*      */   public static final String TOP = "top";
/*      */   public static final String BOTTOM = "bottom";
/*      */   public static final String DIVIDER = "divider";
/*      */   public static final String ORIENTATION_PROPERTY = "orientation";
/*      */   public static final String CONTINUOUS_LAYOUT_PROPERTY = "continuousLayout";
/*      */   public static final String DIVIDER_SIZE_PROPERTY = "dividerSize";
/*      */   public static final String ONE_TOUCH_EXPANDABLE_PROPERTY = "oneTouchExpandable";
/*      */   public static final String LAST_DIVIDER_LOCATION_PROPERTY = "lastDividerLocation";
/*      */   public static final String DIVIDER_LOCATION_PROPERTY = "dividerLocation";
/*      */   public static final String RESIZE_WEIGHT_PROPERTY = "resizeWeight";
/*      */   protected int orientation;
/*      */   protected boolean continuousLayout;
/*      */   protected Component leftComponent;
/*      */   protected Component rightComponent;
/*      */   protected int dividerSize;
/*  217 */   private boolean dividerSizeSet = false;
/*      */   protected boolean oneTouchExpandable;
/*      */   private boolean oneTouchExpandableSet;
/*      */   protected int lastDividerLocation;
/*      */   private double resizeWeight;
/*      */   private int dividerLocation;
/*      */ 
/*      */   public JSplitPane()
/*      */   {
/*  248 */     this(1, UIManager.getBoolean("SplitPane.continuousLayout"), new JButton(UIManager.getString("SplitPane.leftButtonText")), new JButton(UIManager.getString("SplitPane.rightButtonText")));
/*      */   }
/*      */ 
/*      */   @ConstructorProperties({"orientation"})
/*      */   public JSplitPane(int paramInt)
/*      */   {
/*  266 */     this(paramInt, UIManager.getBoolean("SplitPane.continuousLayout"));
/*      */   }
/*      */ 
/*      */   public JSplitPane(int paramInt, boolean paramBoolean)
/*      */   {
/*  285 */     this(paramInt, paramBoolean, null, null);
/*      */   }
/*      */ 
/*      */   public JSplitPane(int paramInt, Component paramComponent1, Component paramComponent2)
/*      */   {
/*  309 */     this(paramInt, UIManager.getBoolean("SplitPane.continuousLayout"), paramComponent1, paramComponent2);
/*      */   }
/*      */ 
/*      */   public JSplitPane(int paramInt, boolean paramBoolean, Component paramComponent1, Component paramComponent2)
/*      */   {
/*  342 */     this.dividerLocation = -1;
/*  343 */     setLayout(null);
/*  344 */     setUIProperty("opaque", Boolean.TRUE);
/*  345 */     this.orientation = paramInt;
/*  346 */     if ((this.orientation != 1) && (this.orientation != 0)) {
/*  347 */       throw new IllegalArgumentException("cannot create JSplitPane, orientation must be one of JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT");
/*      */     }
/*      */ 
/*  351 */     this.continuousLayout = paramBoolean;
/*  352 */     if (paramComponent1 != null)
/*  353 */       setLeftComponent(paramComponent1);
/*  354 */     if (paramComponent2 != null)
/*  355 */       setRightComponent(paramComponent2);
/*  356 */     updateUI();
/*      */   }
/*      */ 
/*      */   public void setUI(SplitPaneUI paramSplitPaneUI)
/*      */   {
/*  373 */     if ((SplitPaneUI)this.ui != paramSplitPaneUI) {
/*  374 */       super.setUI(paramSplitPaneUI);
/*  375 */       revalidate();
/*      */     }
/*      */   }
/*      */ 
/*      */   public SplitPaneUI getUI()
/*      */   {
/*  390 */     return (SplitPaneUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  402 */     setUI((SplitPaneUI)UIManager.getUI(this));
/*  403 */     revalidate();
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  418 */     return "SplitPaneUI";
/*      */   }
/*      */ 
/*      */   public void setDividerSize(int paramInt)
/*      */   {
/*  431 */     int i = this.dividerSize;
/*      */ 
/*  433 */     this.dividerSizeSet = true;
/*  434 */     if (i != paramInt) {
/*  435 */       this.dividerSize = paramInt;
/*  436 */       firePropertyChange("dividerSize", i, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getDividerSize()
/*      */   {
/*  447 */     return this.dividerSize;
/*      */   }
/*      */ 
/*      */   public void setLeftComponent(Component paramComponent)
/*      */   {
/*  457 */     if (paramComponent == null) {
/*  458 */       if (this.leftComponent != null) {
/*  459 */         remove(this.leftComponent);
/*  460 */         this.leftComponent = null;
/*      */       }
/*      */     }
/*  463 */     else add(paramComponent, "left");
/*      */   }
/*      */ 
/*      */   public Component getLeftComponent()
/*      */   {
/*  477 */     return this.leftComponent;
/*      */   }
/*      */ 
/*      */   public void setTopComponent(Component paramComponent)
/*      */   {
/*  489 */     setLeftComponent(paramComponent);
/*      */   }
/*      */ 
/*      */   public Component getTopComponent()
/*      */   {
/*  499 */     return this.leftComponent;
/*      */   }
/*      */ 
/*      */   public void setRightComponent(Component paramComponent)
/*      */   {
/*  512 */     if (paramComponent == null) {
/*  513 */       if (this.rightComponent != null) {
/*  514 */         remove(this.rightComponent);
/*  515 */         this.rightComponent = null;
/*      */       }
/*      */     }
/*  518 */     else add(paramComponent, "right");
/*      */   }
/*      */ 
/*      */   public Component getRightComponent()
/*      */   {
/*  529 */     return this.rightComponent;
/*      */   }
/*      */ 
/*      */   public void setBottomComponent(Component paramComponent)
/*      */   {
/*  541 */     setRightComponent(paramComponent);
/*      */   }
/*      */ 
/*      */   public Component getBottomComponent()
/*      */   {
/*  551 */     return this.rightComponent;
/*      */   }
/*      */ 
/*      */   public void setOneTouchExpandable(boolean paramBoolean)
/*      */   {
/*  574 */     boolean bool = this.oneTouchExpandable;
/*      */ 
/*  576 */     this.oneTouchExpandable = paramBoolean;
/*  577 */     this.oneTouchExpandableSet = true;
/*  578 */     firePropertyChange("oneTouchExpandable", bool, paramBoolean);
/*  579 */     repaint();
/*      */   }
/*      */ 
/*      */   public boolean isOneTouchExpandable()
/*      */   {
/*  590 */     return this.oneTouchExpandable;
/*      */   }
/*      */ 
/*      */   public void setLastDividerLocation(int paramInt)
/*      */   {
/*  606 */     int i = this.lastDividerLocation;
/*      */ 
/*  608 */     this.lastDividerLocation = paramInt;
/*  609 */     firePropertyChange("lastDividerLocation", i, paramInt);
/*      */   }
/*      */ 
/*      */   public int getLastDividerLocation()
/*      */   {
/*  622 */     return this.lastDividerLocation;
/*      */   }
/*      */ 
/*      */   public void setOrientation(int paramInt)
/*      */   {
/*  643 */     if ((paramInt != 0) && (paramInt != 1))
/*      */     {
/*  645 */       throw new IllegalArgumentException("JSplitPane: orientation must be one of JSplitPane.VERTICAL_SPLIT or JSplitPane.HORIZONTAL_SPLIT");
/*      */     }
/*      */ 
/*  651 */     int i = this.orientation;
/*      */ 
/*  653 */     this.orientation = paramInt;
/*  654 */     firePropertyChange("orientation", i, paramInt);
/*      */   }
/*      */ 
/*      */   public int getOrientation()
/*      */   {
/*  665 */     return this.orientation;
/*      */   }
/*      */ 
/*      */   public void setContinuousLayout(boolean paramBoolean)
/*      */   {
/*  688 */     boolean bool = this.continuousLayout;
/*      */ 
/*  690 */     this.continuousLayout = paramBoolean;
/*  691 */     firePropertyChange("continuousLayout", bool, paramBoolean);
/*      */   }
/*      */ 
/*      */   public boolean isContinuousLayout()
/*      */   {
/*  703 */     return this.continuousLayout;
/*      */   }
/*      */ 
/*      */   public void setResizeWeight(double paramDouble)
/*      */   {
/*  725 */     if ((paramDouble < 0.0D) || (paramDouble > 1.0D)) {
/*  726 */       throw new IllegalArgumentException("JSplitPane weight must be between 0 and 1");
/*      */     }
/*  728 */     double d = this.resizeWeight;
/*      */ 
/*  730 */     this.resizeWeight = paramDouble;
/*  731 */     firePropertyChange("resizeWeight", d, paramDouble);
/*      */   }
/*      */ 
/*      */   public double getResizeWeight()
/*      */   {
/*  741 */     return this.resizeWeight;
/*      */   }
/*      */ 
/*      */   public void resetToPreferredSizes()
/*      */   {
/*  750 */     SplitPaneUI localSplitPaneUI = getUI();
/*      */ 
/*  752 */     if (localSplitPaneUI != null)
/*  753 */       localSplitPaneUI.resetToPreferredSizes(this);
/*      */   }
/*      */ 
/*      */   public void setDividerLocation(double paramDouble)
/*      */   {
/*  778 */     if ((paramDouble < 0.0D) || (paramDouble > 1.0D))
/*      */     {
/*  780 */       throw new IllegalArgumentException("proportional location must be between 0.0 and 1.0.");
/*      */     }
/*      */ 
/*  783 */     if (getOrientation() == 0) {
/*  784 */       setDividerLocation((int)((getHeight() - getDividerSize()) * paramDouble));
/*      */     }
/*      */     else
/*  787 */       setDividerLocation((int)((getWidth() - getDividerSize()) * paramDouble));
/*      */   }
/*      */ 
/*      */   public void setDividerLocation(int paramInt)
/*      */   {
/*  808 */     int i = this.dividerLocation;
/*      */ 
/*  810 */     this.dividerLocation = paramInt;
/*      */ 
/*  813 */     SplitPaneUI localSplitPaneUI = getUI();
/*      */ 
/*  815 */     if (localSplitPaneUI != null) {
/*  816 */       localSplitPaneUI.setDividerLocation(this, paramInt);
/*      */     }
/*      */ 
/*  820 */     firePropertyChange("dividerLocation", i, paramInt);
/*      */ 
/*  823 */     setLastDividerLocation(i);
/*      */   }
/*      */ 
/*      */   public int getDividerLocation()
/*      */   {
/*  836 */     return this.dividerLocation;
/*      */   }
/*      */ 
/*      */   public int getMinimumDividerLocation()
/*      */   {
/*  851 */     SplitPaneUI localSplitPaneUI = getUI();
/*      */ 
/*  853 */     if (localSplitPaneUI != null) {
/*  854 */       return localSplitPaneUI.getMinimumDividerLocation(this);
/*      */     }
/*  856 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getMaximumDividerLocation()
/*      */   {
/*  869 */     SplitPaneUI localSplitPaneUI = getUI();
/*      */ 
/*  871 */     if (localSplitPaneUI != null) {
/*  872 */       return localSplitPaneUI.getMaximumDividerLocation(this);
/*      */     }
/*  874 */     return -1;
/*      */   }
/*      */ 
/*      */   public void remove(Component paramComponent)
/*      */   {
/*  886 */     if (paramComponent == this.leftComponent)
/*  887 */       this.leftComponent = null;
/*  888 */     else if (paramComponent == this.rightComponent) {
/*  889 */       this.rightComponent = null;
/*      */     }
/*  891 */     super.remove(paramComponent);
/*      */ 
/*  894 */     revalidate();
/*  895 */     repaint();
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt)
/*      */   {
/*  909 */     Component localComponent = getComponent(paramInt);
/*      */ 
/*  911 */     if (localComponent == this.leftComponent)
/*  912 */       this.leftComponent = null;
/*  913 */     else if (localComponent == this.rightComponent) {
/*  914 */       this.rightComponent = null;
/*      */     }
/*  916 */     super.remove(paramInt);
/*      */ 
/*  919 */     revalidate();
/*  920 */     repaint();
/*      */   }
/*      */ 
/*      */   public void removeAll()
/*      */   {
/*  930 */     this.leftComponent = (this.rightComponent = null);
/*  931 */     super.removeAll();
/*      */ 
/*  934 */     revalidate();
/*  935 */     repaint();
/*      */   }
/*      */ 
/*      */   public boolean isValidateRoot()
/*      */   {
/*  954 */     return true;
/*      */   }
/*      */ 
/*      */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*      */   {
/*  992 */     if ((paramObject != null) && (!(paramObject instanceof String))) {
/*  993 */       throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
/*      */     }
/*      */ 
/* 1000 */     if (paramObject == null)
/* 1001 */       if (getLeftComponent() == null)
/* 1002 */         paramObject = "left";
/* 1003 */       else if (getRightComponent() == null)
/* 1004 */         paramObject = "right";
/*      */     Component localComponent;
/* 1009 */     if ((paramObject != null) && ((paramObject.equals("left")) || (paramObject.equals("top"))))
/*      */     {
/* 1011 */       localComponent = getLeftComponent();
/* 1012 */       if (localComponent != null) {
/* 1013 */         remove(localComponent);
/*      */       }
/* 1015 */       this.leftComponent = paramComponent;
/* 1016 */       paramInt = -1;
/* 1017 */     } else if ((paramObject != null) && ((paramObject.equals("right")) || (paramObject.equals("bottom"))))
/*      */     {
/* 1020 */       localComponent = getRightComponent();
/* 1021 */       if (localComponent != null) {
/* 1022 */         remove(localComponent);
/*      */       }
/* 1024 */       this.rightComponent = paramComponent;
/* 1025 */       paramInt = -1;
/* 1026 */     } else if ((paramObject != null) && (paramObject.equals("divider")))
/*      */     {
/* 1028 */       paramInt = -1;
/*      */     }
/*      */ 
/* 1032 */     super.addImpl(paramComponent, paramObject, paramInt);
/*      */ 
/* 1035 */     revalidate();
/* 1036 */     repaint();
/*      */   }
/*      */ 
/*      */   protected void paintChildren(Graphics paramGraphics)
/*      */   {
/* 1047 */     super.paintChildren(paramGraphics);
/*      */ 
/* 1049 */     SplitPaneUI localSplitPaneUI = getUI();
/*      */ 
/* 1051 */     if (localSplitPaneUI != null) {
/* 1052 */       Graphics localGraphics = paramGraphics.create();
/* 1053 */       localSplitPaneUI.finishedPaintingChildren(this, localGraphics);
/* 1054 */       localGraphics.dispose();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1065 */     paramObjectOutputStream.defaultWriteObject();
/* 1066 */     if (getUIClassID().equals("SplitPaneUI")) {
/* 1067 */       byte b = JComponent.getWriteObjCounter(this);
/* 1068 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 1069 */       if ((b == 0) && (this.ui != null))
/* 1070 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   void setUIProperty(String paramString, Object paramObject)
/*      */   {
/* 1076 */     if (paramString == "dividerSize") {
/* 1077 */       if (!this.dividerSizeSet) {
/* 1078 */         setDividerSize(((Number)paramObject).intValue());
/* 1079 */         this.dividerSizeSet = false;
/*      */       }
/* 1081 */     } else if (paramString == "oneTouchExpandable") {
/* 1082 */       if (!this.oneTouchExpandableSet) {
/* 1083 */         setOneTouchExpandable(((Boolean)paramObject).booleanValue());
/* 1084 */         this.oneTouchExpandableSet = false;
/*      */       }
/*      */     }
/* 1087 */     else super.setUIProperty(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1103 */     String str1 = this.orientation == 1 ? "HORIZONTAL_SPLIT" : "VERTICAL_SPLIT";
/*      */ 
/* 1105 */     String str2 = this.continuousLayout ? "true" : "false";
/*      */ 
/* 1107 */     String str3 = this.oneTouchExpandable ? "true" : "false";
/*      */ 
/* 1110 */     return super.paramString() + ",continuousLayout=" + str2 + ",dividerSize=" + this.dividerSize + ",lastDividerLocation=" + this.lastDividerLocation + ",oneTouchExpandable=" + str3 + ",orientation=" + str1;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1138 */     if (this.accessibleContext == null) {
/* 1139 */       this.accessibleContext = new AccessibleJSplitPane();
/*      */     }
/* 1141 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJSplitPane extends JComponent.AccessibleJComponent
/*      */     implements AccessibleValue
/*      */   {
/*      */     protected AccessibleJSplitPane()
/*      */     {
/* 1159 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 1169 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/*      */ 
/* 1173 */       if (JSplitPane.this.getOrientation() == 0)
/* 1174 */         localAccessibleStateSet.add(AccessibleState.VERTICAL);
/*      */       else {
/* 1176 */         localAccessibleStateSet.add(AccessibleState.HORIZONTAL);
/*      */       }
/* 1178 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleValue getAccessibleValue()
/*      */     {
/* 1191 */       return this;
/*      */     }
/*      */ 
/*      */     public Number getCurrentAccessibleValue()
/*      */     {
/* 1201 */       return Integer.valueOf(JSplitPane.this.getDividerLocation());
/*      */     }
/*      */ 
/*      */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*      */     {
/* 1212 */       if (paramNumber == null) {
/* 1213 */         return false;
/*      */       }
/* 1215 */       JSplitPane.this.setDividerLocation(paramNumber.intValue());
/* 1216 */       return true;
/*      */     }
/*      */ 
/*      */     public Number getMinimumAccessibleValue()
/*      */     {
/* 1226 */       return Integer.valueOf(JSplitPane.this.getUI().getMinimumDividerLocation(JSplitPane.this));
/*      */     }
/*      */ 
/*      */     public Number getMaximumAccessibleValue()
/*      */     {
/* 1237 */       return Integer.valueOf(JSplitPane.this.getUI().getMaximumDividerLocation(JSplitPane.this));
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1250 */       return AccessibleRole.SPLIT_PANE;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JSplitPane
 * JD-Core Version:    0.6.2
 */