/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Font;
/*      */ import java.awt.Image;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Dictionary;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
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
/*      */ import javax.swing.plaf.SliderUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ 
/*      */ public class JSlider extends JComponent
/*      */   implements SwingConstants, Accessible
/*      */ {
/*      */   private static final String uiClassID = "SliderUI";
/*   88 */   private boolean paintTicks = false;
/*   89 */   private boolean paintTrack = true;
/*   90 */   private boolean paintLabels = false;
/*   91 */   private boolean isInverted = false;
/*      */   protected BoundedRangeModel sliderModel;
/*      */   protected int majorTickSpacing;
/*      */   protected int minorTickSpacing;
/*  118 */   protected boolean snapToTicks = false;
/*      */ 
/*  125 */   boolean snapToValue = true;
/*      */   protected int orientation;
/*      */   private Dictionary labelTable;
/*  152 */   protected ChangeListener changeListener = createChangeListener();
/*      */ 
/*  163 */   protected transient ChangeEvent changeEvent = null;
/*      */ 
/*      */   private void checkOrientation(int paramInt)
/*      */   {
/*  167 */     switch (paramInt) {
/*      */     case 0:
/*      */     case 1:
/*  170 */       break;
/*      */     default:
/*  172 */       throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
/*      */     }
/*      */   }
/*      */ 
/*      */   public JSlider()
/*      */   {
/*  182 */     this(0, 0, 100, 50);
/*      */   }
/*      */ 
/*      */   public JSlider(int paramInt)
/*      */   {
/*  198 */     this(paramInt, 0, 100, 50);
/*      */   }
/*      */ 
/*      */   public JSlider(int paramInt1, int paramInt2)
/*      */   {
/*  219 */     this(0, paramInt1, paramInt2, (paramInt1 + paramInt2) / 2);
/*      */   }
/*      */ 
/*      */   public JSlider(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  241 */     this(0, paramInt1, paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   public JSlider(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  272 */     checkOrientation(paramInt1);
/*  273 */     this.orientation = paramInt1;
/*  274 */     setModel(new DefaultBoundedRangeModel(paramInt4, 0, paramInt2, paramInt3));
/*  275 */     updateUI();
/*      */   }
/*      */ 
/*      */   public JSlider(BoundedRangeModel paramBoundedRangeModel)
/*      */   {
/*  285 */     this.orientation = 0;
/*  286 */     setModel(paramBoundedRangeModel);
/*  287 */     updateUI();
/*      */   }
/*      */ 
/*      */   public SliderUI getUI()
/*      */   {
/*  297 */     return (SliderUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(SliderUI paramSliderUI)
/*      */   {
/*  313 */     super.setUI(paramSliderUI);
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  323 */     setUI((SliderUI)UIManager.getUI(this));
/*      */ 
/*  328 */     updateLabelUIs();
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  340 */     return "SliderUI";
/*      */   }
/*      */ 
/*      */   protected ChangeListener createChangeListener()
/*      */   {
/*  370 */     return new ModelListener(null);
/*      */   }
/*      */ 
/*      */   public void addChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  382 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public void removeChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  395 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public ChangeListener[] getChangeListeners()
/*      */   {
/*  408 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireStateChanged()
/*      */   {
/*  426 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*  427 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  428 */       if (arrayOfObject[i] == ChangeListener.class) {
/*  429 */         if (this.changeEvent == null) {
/*  430 */           this.changeEvent = new ChangeEvent(this);
/*      */         }
/*  432 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public BoundedRangeModel getModel()
/*      */   {
/*  447 */     return this.sliderModel;
/*      */   }
/*      */ 
/*      */   public void setModel(BoundedRangeModel paramBoundedRangeModel)
/*      */   {
/*  468 */     BoundedRangeModel localBoundedRangeModel = getModel();
/*      */ 
/*  470 */     if (localBoundedRangeModel != null) {
/*  471 */       localBoundedRangeModel.removeChangeListener(this.changeListener);
/*      */     }
/*      */ 
/*  474 */     this.sliderModel = paramBoundedRangeModel;
/*      */ 
/*  476 */     if (paramBoundedRangeModel != null) {
/*  477 */       paramBoundedRangeModel.addChangeListener(this.changeListener);
/*      */     }
/*      */ 
/*  480 */     if (this.accessibleContext != null) {
/*  481 */       this.accessibleContext.firePropertyChange("AccessibleValue", localBoundedRangeModel == null ? null : Integer.valueOf(localBoundedRangeModel.getValue()), paramBoundedRangeModel == null ? null : Integer.valueOf(paramBoundedRangeModel.getValue()));
/*      */     }
/*      */ 
/*  489 */     firePropertyChange("model", localBoundedRangeModel, this.sliderModel);
/*      */   }
/*      */ 
/*      */   public int getValue()
/*      */   {
/*  502 */     return getModel().getValue();
/*      */   }
/*      */ 
/*      */   public void setValue(int paramInt)
/*      */   {
/*  526 */     BoundedRangeModel localBoundedRangeModel = getModel();
/*  527 */     int i = localBoundedRangeModel.getValue();
/*  528 */     if (i == paramInt) {
/*  529 */       return;
/*      */     }
/*  531 */     localBoundedRangeModel.setValue(paramInt);
/*      */ 
/*  533 */     if (this.accessibleContext != null)
/*  534 */       this.accessibleContext.firePropertyChange("AccessibleValue", Integer.valueOf(i), Integer.valueOf(localBoundedRangeModel.getValue()));
/*      */   }
/*      */ 
/*      */   public int getMinimum()
/*      */   {
/*  551 */     return getModel().getMinimum();
/*      */   }
/*      */ 
/*      */   public void setMinimum(int paramInt)
/*      */   {
/*  577 */     int i = getModel().getMinimum();
/*  578 */     getModel().setMinimum(paramInt);
/*  579 */     firePropertyChange("minimum", Integer.valueOf(i), Integer.valueOf(paramInt));
/*      */   }
/*      */ 
/*      */   public int getMaximum()
/*      */   {
/*  592 */     return getModel().getMaximum();
/*      */   }
/*      */ 
/*      */   public void setMaximum(int paramInt)
/*      */   {
/*  618 */     int i = getModel().getMaximum();
/*  619 */     getModel().setMaximum(paramInt);
/*  620 */     firePropertyChange("maximum", Integer.valueOf(i), Integer.valueOf(paramInt));
/*      */   }
/*      */ 
/*      */   public boolean getValueIsAdjusting()
/*      */   {
/*  633 */     return getModel().getValueIsAdjusting();
/*      */   }
/*      */ 
/*      */   public void setValueIsAdjusting(boolean paramBoolean)
/*      */   {
/*  650 */     BoundedRangeModel localBoundedRangeModel = getModel();
/*  651 */     boolean bool = localBoundedRangeModel.getValueIsAdjusting();
/*  652 */     localBoundedRangeModel.setValueIsAdjusting(paramBoolean);
/*      */ 
/*  654 */     if ((bool != paramBoolean) && (this.accessibleContext != null))
/*  655 */       this.accessibleContext.firePropertyChange("AccessibleState", bool ? AccessibleState.BUSY : null, paramBoolean ? AccessibleState.BUSY : null);
/*      */   }
/*      */ 
/*      */   public int getExtent()
/*      */   {
/*  672 */     return getModel().getExtent();
/*      */   }
/*      */ 
/*      */   public void setExtent(int paramInt)
/*      */   {
/*  698 */     getModel().setExtent(paramInt);
/*      */   }
/*      */ 
/*      */   public int getOrientation()
/*      */   {
/*  709 */     return this.orientation;
/*      */   }
/*      */ 
/*      */   public void setOrientation(int paramInt)
/*      */   {
/*  731 */     checkOrientation(paramInt);
/*  732 */     int i = this.orientation;
/*  733 */     this.orientation = paramInt;
/*  734 */     firePropertyChange("orientation", i, paramInt);
/*      */ 
/*  736 */     if ((i != paramInt) && (this.accessibleContext != null)) {
/*  737 */       this.accessibleContext.firePropertyChange("AccessibleState", i == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL, paramInt == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL);
/*      */     }
/*      */ 
/*  744 */     if (paramInt != i)
/*  745 */       revalidate();
/*      */   }
/*      */ 
/*      */   public void setFont(Font paramFont)
/*      */   {
/*  756 */     super.setFont(paramFont);
/*  757 */     updateLabelSizes();
/*      */   }
/*      */ 
/*      */   public boolean imageUpdate(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */   {
/*  765 */     if (!isShowing()) {
/*  766 */       return false;
/*      */     }
/*      */ 
/*  770 */     Enumeration localEnumeration = this.labelTable.elements();
/*      */ 
/*  772 */     while (localEnumeration.hasMoreElements()) {
/*  773 */       Component localComponent = (Component)localEnumeration.nextElement();
/*      */ 
/*  775 */       if ((localComponent instanceof JLabel)) {
/*  776 */         JLabel localJLabel = (JLabel)localComponent;
/*      */ 
/*  778 */         if ((SwingUtilities.doesIconReferenceImage(localJLabel.getIcon(), paramImage)) || (SwingUtilities.doesIconReferenceImage(localJLabel.getDisabledIcon(), paramImage)))
/*      */         {
/*  780 */           return super.imageUpdate(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  785 */     return false;
/*      */   }
/*      */ 
/*      */   public Dictionary getLabelTable()
/*      */   {
/*  800 */     return this.labelTable;
/*      */   }
/*      */ 
/*      */   public void setLabelTable(Dictionary paramDictionary)
/*      */   {
/*  828 */     Dictionary localDictionary = this.labelTable;
/*  829 */     this.labelTable = paramDictionary;
/*  830 */     updateLabelUIs();
/*  831 */     firePropertyChange("labelTable", localDictionary, this.labelTable);
/*  832 */     if (paramDictionary != localDictionary) {
/*  833 */       revalidate();
/*  834 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void updateLabelUIs()
/*      */   {
/*  849 */     Dictionary localDictionary = getLabelTable();
/*      */ 
/*  851 */     if (localDictionary == null) {
/*  852 */       return;
/*      */     }
/*  854 */     Enumeration localEnumeration = localDictionary.keys();
/*  855 */     while (localEnumeration.hasMoreElements()) {
/*  856 */       JComponent localJComponent = (JComponent)localDictionary.get(localEnumeration.nextElement());
/*  857 */       localJComponent.updateUI();
/*  858 */       localJComponent.setSize(localJComponent.getPreferredSize());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void updateLabelSizes() {
/*  863 */     Dictionary localDictionary = getLabelTable();
/*  864 */     if (localDictionary != null) {
/*  865 */       Enumeration localEnumeration = localDictionary.elements();
/*  866 */       while (localEnumeration.hasMoreElements()) {
/*  867 */         JComponent localJComponent = (JComponent)localEnumeration.nextElement();
/*  868 */         localJComponent.setSize(localJComponent.getPreferredSize());
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public Hashtable createStandardLabels(int paramInt)
/*      */   {
/*  896 */     return createStandardLabels(paramInt, getMinimum());
/*      */   }
/*      */ 
/*      */   public Hashtable createStandardLabels(int paramInt1, int paramInt2)
/*      */   {
/*  924 */     if ((paramInt2 > getMaximum()) || (paramInt2 < getMinimum())) {
/*  925 */       throw new IllegalArgumentException("Slider label start point out of range.");
/*      */     }
/*      */ 
/*  928 */     if (paramInt1 <= 0) {
/*  929 */       throw new IllegalArgumentException("Label incremement must be > 0");
/*      */     }
/*      */ 
/* 1012 */     Hashtable local1SmartHashtable = new Hashtable()
/*      */     {
/*  933 */       int increment = 0;
/*  934 */       int start = 0;
/*  935 */       boolean startAtMin = false;
/*      */ 
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
/*      */       {
/*  972 */         if ((paramAnonymousPropertyChangeEvent.getPropertyName().equals("minimum")) && (this.startAtMin)) {
/*  973 */           this.start = JSlider.this.getMinimum();
/*      */         }
/*      */ 
/*  976 */         if ((paramAnonymousPropertyChangeEvent.getPropertyName().equals("minimum")) || (paramAnonymousPropertyChangeEvent.getPropertyName().equals("maximum")))
/*      */         {
/*  979 */           Enumeration localEnumeration = JSlider.this.getLabelTable().keys();
/*  980 */           Hashtable localHashtable = new Hashtable();
/*      */           Object localObject1;
/*  983 */           while (localEnumeration.hasMoreElements()) {
/*  984 */             localObject1 = localEnumeration.nextElement();
/*  985 */             Object localObject2 = JSlider.this.labelTable.get(localObject1);
/*  986 */             if (!(localObject2 instanceof LabelUIResource)) {
/*  987 */               localHashtable.put(localObject1, localObject2);
/*      */             }
/*      */           }
/*      */ 
/*  991 */           clear();
/*  992 */           createLabels();
/*      */ 
/*  995 */           localEnumeration = localHashtable.keys();
/*  996 */           while (localEnumeration.hasMoreElements()) {
/*  997 */             localObject1 = localEnumeration.nextElement();
/*  998 */             put(localObject1, localHashtable.get(localObject1));
/*      */           }
/*      */ 
/* 1001 */           ((JSlider)paramAnonymousPropertyChangeEvent.getSource()).setLabelTable(this);
/*      */         }
/*      */       }
/*      */ 
/*      */       void createLabels() {
/* 1006 */         for (int i = this.start; i <= JSlider.this.getMaximum(); i += this.increment)
/* 1007 */           put(Integer.valueOf(i), new LabelUIResource("" + i, 0));
/*      */       }
/*      */ 
/*      */       class LabelUIResource extends JLabel
/*      */         implements UIResource
/*      */       {
/*      */         public LabelUIResource(String paramInt, int arg3)
/*      */         {
/*  939 */           super(i);
/*  940 */           setName("Slider.label");
/*      */         }
/*      */ 
/*      */         public Font getFont() {
/*  944 */           Font localFont = super.getFont();
/*  945 */           if ((localFont != null) && (!(localFont instanceof UIResource))) {
/*  946 */             return localFont;
/*      */           }
/*  948 */           return JSlider.this.getFont();
/*      */         }
/*      */ 
/*      */         public Color getForeground() {
/*  952 */           Color localColor = super.getForeground();
/*  953 */           if ((localColor != null) && (!(localColor instanceof UIResource))) {
/*  954 */             return localColor;
/*      */           }
/*  956 */           if (!(JSlider.this.getForeground() instanceof UIResource)) {
/*  957 */             return JSlider.this.getForeground();
/*      */           }
/*  959 */           return localColor;
/*      */         }
/*      */       }
/*      */     };
/* 1014 */     Dictionary localDictionary = getLabelTable();
/*      */ 
/* 1016 */     if ((localDictionary != null) && ((localDictionary instanceof PropertyChangeListener))) {
/* 1017 */       removePropertyChangeListener((PropertyChangeListener)localDictionary);
/*      */     }
/*      */ 
/* 1020 */     addPropertyChangeListener(local1SmartHashtable);
/*      */ 
/* 1022 */     return local1SmartHashtable;
/*      */   }
/*      */ 
/*      */   public boolean getInverted()
/*      */   {
/* 1033 */     return this.isInverted;
/*      */   }
/*      */ 
/*      */   public void setInverted(boolean paramBoolean)
/*      */   {
/* 1058 */     boolean bool = this.isInverted;
/* 1059 */     this.isInverted = paramBoolean;
/* 1060 */     firePropertyChange("inverted", bool, this.isInverted);
/* 1061 */     if (paramBoolean != bool)
/* 1062 */       repaint();
/*      */   }
/*      */ 
/*      */   public int getMajorTickSpacing()
/*      */   {
/* 1078 */     return this.majorTickSpacing;
/*      */   }
/*      */ 
/*      */   public void setMajorTickSpacing(int paramInt)
/*      */   {
/* 1114 */     int i = this.majorTickSpacing;
/* 1115 */     this.majorTickSpacing = paramInt;
/* 1116 */     if ((this.labelTable == null) && (getMajorTickSpacing() > 0) && (getPaintLabels())) {
/* 1117 */       setLabelTable(createStandardLabels(getMajorTickSpacing()));
/*      */     }
/* 1119 */     firePropertyChange("majorTickSpacing", i, this.majorTickSpacing);
/* 1120 */     if ((this.majorTickSpacing != i) && (getPaintTicks()))
/* 1121 */       repaint();
/*      */   }
/*      */ 
/*      */   public int getMinorTickSpacing()
/*      */   {
/* 1138 */     return this.minorTickSpacing;
/*      */   }
/*      */ 
/*      */   public void setMinorTickSpacing(int paramInt)
/*      */   {
/* 1161 */     int i = this.minorTickSpacing;
/* 1162 */     this.minorTickSpacing = paramInt;
/* 1163 */     firePropertyChange("minorTickSpacing", i, this.minorTickSpacing);
/* 1164 */     if ((this.minorTickSpacing != i) && (getPaintTicks()))
/* 1165 */       repaint();
/*      */   }
/*      */ 
/*      */   public boolean getSnapToTicks()
/*      */   {
/* 1179 */     return this.snapToTicks;
/*      */   }
/*      */ 
/*      */   boolean getSnapToValue()
/*      */   {
/* 1192 */     return this.snapToValue;
/*      */   }
/*      */ 
/*      */   public void setSnapToTicks(boolean paramBoolean)
/*      */   {
/* 1209 */     boolean bool = this.snapToTicks;
/* 1210 */     this.snapToTicks = paramBoolean;
/* 1211 */     firePropertyChange("snapToTicks", bool, this.snapToTicks);
/*      */   }
/*      */ 
/*      */   void setSnapToValue(boolean paramBoolean)
/*      */   {
/* 1230 */     boolean bool = this.snapToValue;
/* 1231 */     this.snapToValue = paramBoolean;
/* 1232 */     firePropertyChange("snapToValue", bool, this.snapToValue);
/*      */   }
/*      */ 
/*      */   public boolean getPaintTicks()
/*      */   {
/* 1242 */     return this.paintTicks;
/*      */   }
/*      */ 
/*      */   public void setPaintTicks(boolean paramBoolean)
/*      */   {
/* 1258 */     boolean bool = this.paintTicks;
/* 1259 */     this.paintTicks = paramBoolean;
/* 1260 */     firePropertyChange("paintTicks", bool, this.paintTicks);
/* 1261 */     if (this.paintTicks != bool) {
/* 1262 */       revalidate();
/* 1263 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean getPaintTrack()
/*      */   {
/* 1273 */     return this.paintTrack;
/*      */   }
/*      */ 
/*      */   public void setPaintTrack(boolean paramBoolean)
/*      */   {
/* 1289 */     boolean bool = this.paintTrack;
/* 1290 */     this.paintTrack = paramBoolean;
/* 1291 */     firePropertyChange("paintTrack", bool, this.paintTrack);
/* 1292 */     if (this.paintTrack != bool)
/* 1293 */       repaint();
/*      */   }
/*      */ 
/*      */   public boolean getPaintLabels()
/*      */   {
/* 1304 */     return this.paintLabels;
/*      */   }
/*      */ 
/*      */   public void setPaintLabels(boolean paramBoolean)
/*      */   {
/* 1331 */     boolean bool = this.paintLabels;
/* 1332 */     this.paintLabels = paramBoolean;
/* 1333 */     if ((this.labelTable == null) && (getMajorTickSpacing() > 0)) {
/* 1334 */       setLabelTable(createStandardLabels(getMajorTickSpacing()));
/*      */     }
/* 1336 */     firePropertyChange("paintLabels", bool, this.paintLabels);
/* 1337 */     if (this.paintLabels != bool) {
/* 1338 */       revalidate();
/* 1339 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1349 */     paramObjectOutputStream.defaultWriteObject();
/* 1350 */     if (getUIClassID().equals("SliderUI")) {
/* 1351 */       byte b = JComponent.getWriteObjCounter(this);
/* 1352 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 1353 */       if ((b == 0) && (this.ui != null))
/* 1354 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1370 */     String str1 = this.paintTicks ? "true" : "false";
/*      */ 
/* 1372 */     String str2 = this.paintTrack ? "true" : "false";
/*      */ 
/* 1374 */     String str3 = this.paintLabels ? "true" : "false";
/*      */ 
/* 1376 */     String str4 = this.isInverted ? "true" : "false";
/*      */ 
/* 1378 */     String str5 = this.snapToTicks ? "true" : "false";
/*      */ 
/* 1380 */     String str6 = this.snapToValue ? "true" : "false";
/*      */ 
/* 1382 */     String str7 = this.orientation == 0 ? "HORIZONTAL" : "VERTICAL";
/*      */ 
/* 1385 */     return super.paramString() + ",isInverted=" + str4 + ",majorTickSpacing=" + this.majorTickSpacing + ",minorTickSpacing=" + this.minorTickSpacing + ",orientation=" + str7 + ",paintLabels=" + str3 + ",paintTicks=" + str1 + ",paintTrack=" + str2 + ",snapToTicks=" + str5 + ",snapToValue=" + str6;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1412 */     if (this.accessibleContext == null) {
/* 1413 */       this.accessibleContext = new AccessibleJSlider();
/*      */     }
/* 1415 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJSlider extends JComponent.AccessibleJComponent
/*      */     implements AccessibleValue
/*      */   {
/*      */     protected AccessibleJSlider()
/*      */     {
/* 1432 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 1443 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 1444 */       if (JSlider.this.getValueIsAdjusting()) {
/* 1445 */         localAccessibleStateSet.add(AccessibleState.BUSY);
/*      */       }
/* 1447 */       if (JSlider.this.getOrientation() == 1) {
/* 1448 */         localAccessibleStateSet.add(AccessibleState.VERTICAL);
/*      */       }
/*      */       else {
/* 1451 */         localAccessibleStateSet.add(AccessibleState.HORIZONTAL);
/*      */       }
/* 1453 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1462 */       return AccessibleRole.SLIDER;
/*      */     }
/*      */ 
/*      */     public AccessibleValue getAccessibleValue()
/*      */     {
/* 1474 */       return this;
/*      */     }
/*      */ 
/*      */     public Number getCurrentAccessibleValue()
/*      */     {
/* 1483 */       return Integer.valueOf(JSlider.this.getValue());
/*      */     }
/*      */ 
/*      */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*      */     {
/* 1493 */       if (paramNumber == null) {
/* 1494 */         return false;
/*      */       }
/* 1496 */       JSlider.this.setValue(paramNumber.intValue());
/* 1497 */       return true;
/*      */     }
/*      */ 
/*      */     public Number getMinimumAccessibleValue()
/*      */     {
/* 1506 */       return Integer.valueOf(JSlider.this.getMinimum());
/*      */     }
/*      */ 
/*      */     public Number getMaximumAccessibleValue()
/*      */     {
/* 1516 */       BoundedRangeModel localBoundedRangeModel = JSlider.this.getModel();
/* 1517 */       return Integer.valueOf(localBoundedRangeModel.getMaximum() - localBoundedRangeModel.getExtent());
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
/*  350 */       JSlider.this.fireStateChanged();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JSlider
 * JD-Core Version:    0.6.2
 */