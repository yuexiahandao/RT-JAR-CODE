/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.beans.Transient;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleComponent;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleIcon;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.TabbedPaneUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class JTabbedPane extends JComponent
/*      */   implements Serializable, Accessible, SwingConstants
/*      */ {
/*      */   public static final int WRAP_TAB_LAYOUT = 0;
/*      */   public static final int SCROLL_TAB_LAYOUT = 1;
/*      */   private static final String uiClassID = "TabbedPaneUI";
/*  137 */   protected int tabPlacement = 1;
/*      */   private int tabLayoutPolicy;
/*      */   protected SingleSelectionModel model;
/*      */   private boolean haveRegistered;
/*  150 */   protected ChangeListener changeListener = null;
/*      */   private final List<Page> pages;
/*  155 */   private Component visComp = null;
/*      */ 
/*  163 */   protected transient ChangeEvent changeEvent = null;
/*      */ 
/*      */   public JTabbedPane()
/*      */   {
/*  171 */     this(1, 0);
/*      */   }
/*      */ 
/*      */   public JTabbedPane(int paramInt)
/*      */   {
/*  183 */     this(paramInt, 0);
/*      */   }
/*      */ 
/*      */   public JTabbedPane(int paramInt1, int paramInt2)
/*      */   {
/*  202 */     setTabPlacement(paramInt1);
/*  203 */     setTabLayoutPolicy(paramInt2);
/*  204 */     this.pages = new ArrayList(1);
/*  205 */     setModel(new DefaultSingleSelectionModel());
/*  206 */     updateUI();
/*      */   }
/*      */ 
/*      */   public TabbedPaneUI getUI()
/*      */   {
/*  216 */     return (TabbedPaneUI)this.ui;
/*      */   }
/*      */ 
/*      */   public void setUI(TabbedPaneUI paramTabbedPaneUI)
/*      */   {
/*  231 */     super.setUI(paramTabbedPaneUI);
/*      */ 
/*  233 */     for (int i = 0; i < getTabCount(); i++) {
/*  234 */       Icon localIcon = ((Page)this.pages.get(i)).disabledIcon;
/*  235 */       if ((localIcon instanceof UIResource))
/*  236 */         setDisabledIconAt(i, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void updateUI()
/*      */   {
/*  247 */     setUI((TabbedPaneUI)UIManager.getUI(this));
/*      */   }
/*      */ 
/*      */   public String getUIClassID()
/*      */   {
/*  260 */     return "TabbedPaneUI";
/*      */   }
/*      */ 
/*      */   protected ChangeListener createChangeListener()
/*      */   {
/*  282 */     return new ModelListener();
/*      */   }
/*      */ 
/*      */   public void addChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  293 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public void removeChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  304 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*      */   }
/*      */ 
/*      */   public ChangeListener[] getChangeListeners()
/*      */   {
/*  316 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireStateChanged()
/*      */   {
/*  353 */     int i = getSelectedIndex();
/*      */ 
/*  356 */     if (i < 0)
/*      */     {
/*  358 */       if ((this.visComp != null) && (this.visComp.isVisible()))
/*      */       {
/*  360 */         this.visComp.setVisible(false);
/*      */       }
/*      */ 
/*  364 */       this.visComp = null;
/*      */     }
/*      */     else
/*      */     {
/*  369 */       localObject = getComponentAt(i);
/*      */ 
/*  372 */       if ((localObject != null) && (localObject != this.visComp)) {
/*  373 */         j = 0;
/*      */ 
/*  382 */         if (this.visComp != null) {
/*  383 */           j = SwingUtilities.findFocusOwner(this.visComp) != null ? 1 : 0;
/*      */ 
/*  387 */           if (this.visComp.isVisible())
/*      */           {
/*  389 */             this.visComp.setVisible(false);
/*      */           }
/*      */         }
/*      */ 
/*  393 */         if (!((Component)localObject).isVisible()) {
/*  394 */           ((Component)localObject).setVisible(true);
/*      */         }
/*      */ 
/*  397 */         if (j != 0) {
/*  398 */           SwingUtilities2.tabbedPaneChangeFocusTo((Component)localObject);
/*      */         }
/*      */ 
/*  401 */         this.visComp = ((Component)localObject);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  408 */     Object localObject = this.listenerList.getListenerList();
/*      */ 
/*  411 */     for (int j = localObject.length - 2; j >= 0; j -= 2)
/*  412 */       if (localObject[j] == ChangeListener.class)
/*      */       {
/*  414 */         if (this.changeEvent == null)
/*  415 */           this.changeEvent = new ChangeEvent(this);
/*  416 */         ((ChangeListener)localObject[(j + 1)]).stateChanged(this.changeEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public SingleSelectionModel getModel()
/*      */   {
/*  427 */     return this.model;
/*      */   }
/*      */ 
/*      */   public void setModel(SingleSelectionModel paramSingleSelectionModel)
/*      */   {
/*  440 */     SingleSelectionModel localSingleSelectionModel = getModel();
/*      */ 
/*  442 */     if (localSingleSelectionModel != null) {
/*  443 */       localSingleSelectionModel.removeChangeListener(this.changeListener);
/*  444 */       this.changeListener = null;
/*      */     }
/*      */ 
/*  447 */     this.model = paramSingleSelectionModel;
/*      */ 
/*  449 */     if (paramSingleSelectionModel != null) {
/*  450 */       this.changeListener = createChangeListener();
/*  451 */       paramSingleSelectionModel.addChangeListener(this.changeListener);
/*      */     }
/*      */ 
/*  454 */     firePropertyChange("model", localSingleSelectionModel, paramSingleSelectionModel);
/*  455 */     repaint();
/*      */   }
/*      */ 
/*      */   public int getTabPlacement()
/*      */   {
/*  463 */     return this.tabPlacement;
/*      */   }
/*      */ 
/*      */   public void setTabPlacement(int paramInt)
/*      */   {
/*  492 */     if ((paramInt != 1) && (paramInt != 2) && (paramInt != 3) && (paramInt != 4))
/*      */     {
/*  494 */       throw new IllegalArgumentException("illegal tab placement: must be TOP, BOTTOM, LEFT, or RIGHT");
/*      */     }
/*  496 */     if (this.tabPlacement != paramInt) {
/*  497 */       int i = this.tabPlacement;
/*  498 */       this.tabPlacement = paramInt;
/*  499 */       firePropertyChange("tabPlacement", i, paramInt);
/*  500 */       revalidate();
/*  501 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getTabLayoutPolicy()
/*      */   {
/*  512 */     return this.tabLayoutPolicy;
/*      */   }
/*      */ 
/*      */   public void setTabLayoutPolicy(int paramInt)
/*      */   {
/*  546 */     if ((paramInt != 0) && (paramInt != 1)) {
/*  547 */       throw new IllegalArgumentException("illegal tab layout policy: must be WRAP_TAB_LAYOUT or SCROLL_TAB_LAYOUT");
/*      */     }
/*  549 */     if (this.tabLayoutPolicy != paramInt) {
/*  550 */       int i = this.tabLayoutPolicy;
/*  551 */       this.tabLayoutPolicy = paramInt;
/*  552 */       firePropertyChange("tabLayoutPolicy", i, paramInt);
/*  553 */       revalidate();
/*  554 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public int getSelectedIndex()
/*      */   {
/*  567 */     return this.model.getSelectedIndex();
/*      */   }
/*      */ 
/*      */   public void setSelectedIndex(int paramInt)
/*      */   {
/*  588 */     if (paramInt != -1) {
/*  589 */       checkIndex(paramInt);
/*      */     }
/*  591 */     setSelectedIndexImpl(paramInt, true);
/*      */   }
/*      */ 
/*      */   private void setSelectedIndexImpl(int paramInt, boolean paramBoolean)
/*      */   {
/*  596 */     int i = this.model.getSelectedIndex();
/*  597 */     Page localPage1 = null; Page localPage2 = null;
/*  598 */     String str = null;
/*      */ 
/*  600 */     paramBoolean = (paramBoolean) && (i != paramInt);
/*      */ 
/*  602 */     if (paramBoolean) {
/*  603 */       if (this.accessibleContext != null) {
/*  604 */         str = this.accessibleContext.getAccessibleName();
/*      */       }
/*      */ 
/*  607 */       if (i >= 0) {
/*  608 */         localPage1 = (Page)this.pages.get(i);
/*      */       }
/*      */ 
/*  611 */       if (paramInt >= 0) {
/*  612 */         localPage2 = (Page)this.pages.get(paramInt);
/*      */       }
/*      */     }
/*      */ 
/*  616 */     this.model.setSelectedIndex(paramInt);
/*      */ 
/*  618 */     if (paramBoolean)
/*  619 */       changeAccessibleSelection(localPage1, str, localPage2);
/*      */   }
/*      */ 
/*      */   private void changeAccessibleSelection(Page paramPage1, String paramString, Page paramPage2)
/*      */   {
/*  624 */     if (this.accessibleContext == null) {
/*  625 */       return;
/*      */     }
/*      */ 
/*  628 */     if (paramPage1 != null) {
/*  629 */       paramPage1.firePropertyChange("AccessibleState", AccessibleState.SELECTED, null);
/*      */     }
/*      */ 
/*  633 */     if (paramPage2 != null) {
/*  634 */       paramPage2.firePropertyChange("AccessibleState", null, AccessibleState.SELECTED);
/*      */     }
/*      */ 
/*  638 */     this.accessibleContext.firePropertyChange("AccessibleName", paramString, this.accessibleContext.getAccessibleName());
/*      */   }
/*      */ 
/*      */   @Transient
/*      */   public Component getSelectedComponent()
/*      */   {
/*  653 */     int i = getSelectedIndex();
/*  654 */     if (i == -1) {
/*  655 */       return null;
/*      */     }
/*  657 */     return getComponentAt(i);
/*      */   }
/*      */ 
/*      */   public void setSelectedComponent(Component paramComponent)
/*      */   {
/*  673 */     int i = indexOfComponent(paramComponent);
/*  674 */     if (i != -1)
/*  675 */       setSelectedIndex(i);
/*      */     else
/*  677 */       throw new IllegalArgumentException("component not found in tabbed pane");
/*      */   }
/*      */ 
/*      */   public void insertTab(String paramString1, Icon paramIcon, Component paramComponent, String paramString2, int paramInt)
/*      */   {
/*  700 */     int i = paramInt;
/*      */ 
/*  708 */     int j = indexOfComponent(paramComponent);
/*  709 */     if ((paramComponent != null) && (j != -1)) {
/*  710 */       removeTabAt(j);
/*  711 */       if (i > j) {
/*  712 */         i--;
/*      */       }
/*      */     }
/*      */ 
/*  716 */     int k = getSelectedIndex();
/*      */ 
/*  718 */     this.pages.add(i, new Page(this, paramString1 != null ? paramString1 : "", paramIcon, null, paramComponent, paramString2));
/*      */ 
/*  723 */     if (paramComponent != null) {
/*  724 */       addImpl(paramComponent, null, -1);
/*  725 */       paramComponent.setVisible(false);
/*      */     } else {
/*  727 */       firePropertyChange("indexForNullComponent", -1, paramInt);
/*      */     }
/*      */ 
/*  730 */     if (this.pages.size() == 1) {
/*  731 */       setSelectedIndex(0);
/*      */     }
/*      */ 
/*  734 */     if (k >= i) {
/*  735 */       setSelectedIndexImpl(k + 1, false);
/*      */     }
/*      */ 
/*  738 */     if ((!this.haveRegistered) && (paramString2 != null)) {
/*  739 */       ToolTipManager.sharedInstance().registerComponent(this);
/*  740 */       this.haveRegistered = true;
/*      */     }
/*      */ 
/*  743 */     if (this.accessibleContext != null) {
/*  744 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", null, paramComponent);
/*      */     }
/*      */ 
/*  748 */     revalidate();
/*  749 */     repaint();
/*      */   }
/*      */ 
/*      */   public void addTab(String paramString1, Icon paramIcon, Component paramComponent, String paramString2)
/*      */   {
/*  767 */     insertTab(paramString1, paramIcon, paramComponent, paramString2, this.pages.size());
/*      */   }
/*      */ 
/*      */   public void addTab(String paramString, Icon paramIcon, Component paramComponent)
/*      */   {
/*  783 */     insertTab(paramString, paramIcon, paramComponent, null, this.pages.size());
/*      */   }
/*      */ 
/*      */   public void addTab(String paramString, Component paramComponent)
/*      */   {
/*  798 */     insertTab(paramString, null, paramComponent, null, this.pages.size());
/*      */   }
/*      */ 
/*      */   public Component add(Component paramComponent)
/*      */   {
/*  814 */     if (!(paramComponent instanceof UIResource))
/*  815 */       addTab(paramComponent.getName(), paramComponent);
/*      */     else {
/*  817 */       super.add(paramComponent);
/*      */     }
/*  819 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public Component add(String paramString, Component paramComponent)
/*      */   {
/*  834 */     if (!(paramComponent instanceof UIResource))
/*  835 */       addTab(paramString, paramComponent);
/*      */     else {
/*  837 */       super.add(paramString, paramComponent);
/*      */     }
/*  839 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public Component add(Component paramComponent, int paramInt)
/*      */   {
/*  855 */     if (!(paramComponent instanceof UIResource))
/*      */     {
/*  858 */       insertTab(paramComponent.getName(), null, paramComponent, null, paramInt == -1 ? getTabCount() : paramInt);
/*      */     }
/*      */     else {
/*  861 */       super.add(paramComponent, paramInt);
/*      */     }
/*  863 */     return paramComponent;
/*      */   }
/*      */ 
/*      */   public void add(Component paramComponent, Object paramObject)
/*      */   {
/*  880 */     if (!(paramComponent instanceof UIResource)) {
/*  881 */       if ((paramObject instanceof String))
/*  882 */         addTab((String)paramObject, paramComponent);
/*  883 */       else if ((paramObject instanceof Icon))
/*  884 */         addTab(null, (Icon)paramObject, paramComponent);
/*      */       else
/*  886 */         add(paramComponent);
/*      */     }
/*      */     else
/*  889 */       super.add(paramComponent, paramObject);
/*      */   }
/*      */ 
/*      */   public void add(Component paramComponent, Object paramObject, int paramInt)
/*      */   {
/*  908 */     if (!(paramComponent instanceof UIResource))
/*      */     {
/*  910 */       Icon localIcon = (paramObject instanceof Icon) ? (Icon)paramObject : null;
/*  911 */       String str = (paramObject instanceof String) ? (String)paramObject : null;
/*      */ 
/*  914 */       insertTab(str, localIcon, paramComponent, null, paramInt == -1 ? getTabCount() : paramInt);
/*      */     } else {
/*  916 */       super.add(paramComponent, paramObject, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeTabAt(int paramInt)
/*      */   {
/*  933 */     checkIndex(paramInt);
/*      */ 
/*  935 */     Component localComponent = getComponentAt(paramInt);
/*  936 */     int i = 0;
/*  937 */     int j = getSelectedIndex();
/*  938 */     String str = null;
/*      */ 
/*  941 */     if (localComponent == this.visComp) {
/*  942 */       i = SwingUtilities.findFocusOwner(this.visComp) != null ? 1 : 0;
/*  943 */       this.visComp = null;
/*      */     }
/*      */ 
/*  946 */     if (this.accessibleContext != null)
/*      */     {
/*  948 */       if (paramInt == j)
/*      */       {
/*  950 */         ((Page)this.pages.get(paramInt)).firePropertyChange("AccessibleState", AccessibleState.SELECTED, null);
/*      */ 
/*  954 */         str = this.accessibleContext.getAccessibleName();
/*      */       }
/*      */ 
/*  957 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", localComponent, null);
/*      */     }
/*      */ 
/*  963 */     setTabComponentAt(paramInt, null);
/*  964 */     this.pages.remove(paramInt);
/*      */ 
/*  971 */     putClientProperty("__index_to_remove__", Integer.valueOf(paramInt));
/*      */     Object localObject;
/*  974 */     if (j > paramInt) {
/*  975 */       setSelectedIndexImpl(j - 1, false);
/*      */     }
/*  978 */     else if (j >= getTabCount()) {
/*  979 */       setSelectedIndexImpl(j - 1, false);
/*  980 */       localObject = j != 0 ? (Page)this.pages.get(j - 1) : null;
/*      */ 
/*  984 */       changeAccessibleSelection(null, str, (Page)localObject);
/*      */     }
/*  987 */     else if (paramInt == j) {
/*  988 */       fireStateChanged();
/*  989 */       changeAccessibleSelection(null, str, (Page)this.pages.get(paramInt));
/*      */     }
/*      */ 
/*  995 */     if (localComponent != null) {
/*  996 */       localObject = getComponents();
/*  997 */       int k = localObject.length;
/*      */       do { k--; if (k < 0) break; }
/*  998 */       while (localObject[k] != localComponent);
/*  999 */       super.remove(k);
/* 1000 */       localComponent.setVisible(true);
/*      */     }
/*      */ 
/* 1006 */     if (i != 0) {
/* 1007 */       SwingUtilities2.tabbedPaneChangeFocusTo(getSelectedComponent());
/*      */     }
/*      */ 
/* 1010 */     revalidate();
/* 1011 */     repaint();
/*      */   }
/*      */ 
/*      */   public void remove(Component paramComponent)
/*      */   {
/* 1024 */     int i = indexOfComponent(paramComponent);
/* 1025 */     if (i != -1) {
/* 1026 */       removeTabAt(i);
/*      */     }
/*      */     else
/*      */     {
/* 1030 */       Component[] arrayOfComponent = getComponents();
/* 1031 */       for (int j = 0; j < arrayOfComponent.length; j++)
/* 1032 */         if (paramComponent == arrayOfComponent[j]) {
/* 1033 */           super.remove(j);
/* 1034 */           break;
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt)
/*      */   {
/* 1051 */     removeTabAt(paramInt);
/*      */   }
/*      */ 
/*      */   public void removeAll()
/*      */   {
/* 1062 */     setSelectedIndexImpl(-1, true);
/*      */ 
/* 1064 */     int i = getTabCount();
/*      */ 
/* 1067 */     while (i-- > 0)
/* 1068 */       removeTabAt(i);
/*      */   }
/*      */ 
/*      */   public int getTabCount()
/*      */   {
/* 1078 */     return this.pages.size();
/*      */   }
/*      */ 
/*      */   public int getTabRunCount()
/*      */   {
/* 1093 */     if (this.ui != null) {
/* 1094 */       return ((TabbedPaneUI)this.ui).getTabRunCount(this);
/*      */     }
/* 1096 */     return 0;
/*      */   }
/*      */ 
/*      */   public String getTitleAt(int paramInt)
/*      */   {
/* 1112 */     return ((Page)this.pages.get(paramInt)).title;
/*      */   }
/*      */ 
/*      */   public Icon getIconAt(int paramInt)
/*      */   {
/* 1126 */     return ((Page)this.pages.get(paramInt)).icon;
/*      */   }
/*      */ 
/*      */   public Icon getDisabledIconAt(int paramInt)
/*      */   {
/* 1145 */     Page localPage = (Page)this.pages.get(paramInt);
/* 1146 */     if (localPage.disabledIcon == null) {
/* 1147 */       localPage.disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, localPage.icon);
/*      */     }
/* 1149 */     return localPage.disabledIcon;
/*      */   }
/*      */ 
/*      */   public String getToolTipTextAt(int paramInt)
/*      */   {
/* 1164 */     return ((Page)this.pages.get(paramInt)).tip;
/*      */   }
/*      */ 
/*      */   public Color getBackgroundAt(int paramInt)
/*      */   {
/* 1179 */     return ((Page)this.pages.get(paramInt)).getBackground();
/*      */   }
/*      */ 
/*      */   public Color getForegroundAt(int paramInt)
/*      */   {
/* 1194 */     return ((Page)this.pages.get(paramInt)).getForeground();
/*      */   }
/*      */ 
/*      */   public boolean isEnabledAt(int paramInt)
/*      */   {
/* 1210 */     return ((Page)this.pages.get(paramInt)).isEnabled();
/*      */   }
/*      */ 
/*      */   public Component getComponentAt(int paramInt)
/*      */   {
/* 1224 */     return ((Page)this.pages.get(paramInt)).component;
/*      */   }
/*      */ 
/*      */   public int getMnemonicAt(int paramInt)
/*      */   {
/* 1244 */     checkIndex(paramInt);
/*      */ 
/* 1246 */     Page localPage = (Page)this.pages.get(paramInt);
/* 1247 */     return localPage.getMnemonic();
/*      */   }
/*      */ 
/*      */   public int getDisplayedMnemonicIndexAt(int paramInt)
/*      */   {
/* 1265 */     checkIndex(paramInt);
/*      */ 
/* 1267 */     Page localPage = (Page)this.pages.get(paramInt);
/* 1268 */     return localPage.getDisplayedMnemonicIndex();
/*      */   }
/*      */ 
/*      */   public Rectangle getBoundsAt(int paramInt)
/*      */   {
/* 1287 */     checkIndex(paramInt);
/* 1288 */     if (this.ui != null) {
/* 1289 */       return ((TabbedPaneUI)this.ui).getTabBounds(this, paramInt);
/*      */     }
/* 1291 */     return null;
/*      */   }
/*      */ 
/*      */   public void setTitleAt(int paramInt, String paramString)
/*      */   {
/* 1316 */     Page localPage = (Page)this.pages.get(paramInt);
/* 1317 */     String str = localPage.title;
/* 1318 */     localPage.title = paramString;
/*      */ 
/* 1320 */     if (str != paramString) {
/* 1321 */       firePropertyChange("indexForTitle", -1, paramInt);
/*      */     }
/* 1323 */     localPage.updateDisplayedMnemonicIndex();
/* 1324 */     if ((str != paramString) && (this.accessibleContext != null)) {
/* 1325 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", str, paramString);
/*      */     }
/*      */ 
/* 1329 */     if ((paramString == null) || (str == null) || (!paramString.equals(str)))
/*      */     {
/* 1331 */       revalidate();
/* 1332 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setIconAt(int paramInt, Icon paramIcon)
/*      */   {
/* 1360 */     Page localPage = (Page)this.pages.get(paramInt);
/* 1361 */     Icon localIcon = localPage.icon;
/* 1362 */     if (paramIcon != localIcon) {
/* 1363 */       localPage.icon = paramIcon;
/*      */ 
/* 1369 */       if ((localPage.disabledIcon instanceof UIResource)) {
/* 1370 */         localPage.disabledIcon = null;
/*      */       }
/*      */ 
/* 1374 */       if (this.accessibleContext != null) {
/* 1375 */         this.accessibleContext.firePropertyChange("AccessibleVisibleData", localIcon, paramIcon);
/*      */       }
/*      */ 
/* 1379 */       revalidate();
/* 1380 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDisabledIconAt(int paramInt, Icon paramIcon)
/*      */   {
/* 1401 */     Icon localIcon = ((Page)this.pages.get(paramInt)).disabledIcon;
/* 1402 */     ((Page)this.pages.get(paramInt)).disabledIcon = paramIcon;
/* 1403 */     if ((paramIcon != localIcon) && (!isEnabledAt(paramInt))) {
/* 1404 */       revalidate();
/* 1405 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setToolTipTextAt(int paramInt, String paramString)
/*      */   {
/* 1426 */     String str = ((Page)this.pages.get(paramInt)).tip;
/* 1427 */     ((Page)this.pages.get(paramInt)).tip = paramString;
/*      */ 
/* 1429 */     if ((str != paramString) && (this.accessibleContext != null)) {
/* 1430 */       this.accessibleContext.firePropertyChange("AccessibleVisibleData", str, paramString);
/*      */     }
/*      */ 
/* 1434 */     if ((!this.haveRegistered) && (paramString != null)) {
/* 1435 */       ToolTipManager.sharedInstance().registerComponent(this);
/* 1436 */       this.haveRegistered = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setBackgroundAt(int paramInt, Color paramColor)
/*      */   {
/* 1462 */     Color localColor = ((Page)this.pages.get(paramInt)).background;
/* 1463 */     ((Page)this.pages.get(paramInt)).setBackground(paramColor);
/* 1464 */     if ((paramColor == null) || (localColor == null) || (!paramColor.equals(localColor)))
/*      */     {
/* 1466 */       Rectangle localRectangle = getBoundsAt(paramInt);
/* 1467 */       if (localRectangle != null)
/* 1468 */         repaint(localRectangle);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setForegroundAt(int paramInt, Color paramColor)
/*      */   {
/* 1495 */     Color localColor = ((Page)this.pages.get(paramInt)).foreground;
/* 1496 */     ((Page)this.pages.get(paramInt)).setForeground(paramColor);
/* 1497 */     if ((paramColor == null) || (localColor == null) || (!paramColor.equals(localColor)))
/*      */     {
/* 1499 */       Rectangle localRectangle = getBoundsAt(paramInt);
/* 1500 */       if (localRectangle != null)
/* 1501 */         repaint(localRectangle);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setEnabledAt(int paramInt, boolean paramBoolean)
/*      */   {
/* 1518 */     boolean bool = ((Page)this.pages.get(paramInt)).isEnabled();
/* 1519 */     ((Page)this.pages.get(paramInt)).setEnabled(paramBoolean);
/* 1520 */     if (paramBoolean != bool) {
/* 1521 */       revalidate();
/* 1522 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setComponentAt(int paramInt, Component paramComponent)
/*      */   {
/* 1541 */     Page localPage = (Page)this.pages.get(paramInt);
/* 1542 */     if (paramComponent != localPage.component) {
/* 1543 */       int i = 0;
/*      */ 
/* 1545 */       if (localPage.component != null) {
/* 1546 */         i = SwingUtilities.findFocusOwner(localPage.component) != null ? 1 : 0;
/*      */ 
/* 1551 */         synchronized (getTreeLock()) {
/* 1552 */           int j = getComponentCount();
/* 1553 */           Component[] arrayOfComponent = getComponents();
/* 1554 */           for (int k = 0; k < j; k++) {
/* 1555 */             if (arrayOfComponent[k] == localPage.component) {
/* 1556 */               super.remove(k);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 1562 */       localPage.component = paramComponent;
/* 1563 */       boolean bool = getSelectedIndex() == paramInt;
/*      */ 
/* 1565 */       if (bool) {
/* 1566 */         this.visComp = paramComponent;
/*      */       }
/*      */ 
/* 1569 */       if (paramComponent != null) {
/* 1570 */         paramComponent.setVisible(bool);
/* 1571 */         addImpl(paramComponent, null, -1);
/*      */ 
/* 1573 */         if (i != 0)
/* 1574 */           SwingUtilities2.tabbedPaneChangeFocusTo(paramComponent);
/*      */       }
/*      */       else {
/* 1577 */         repaint();
/*      */       }
/*      */ 
/* 1580 */       revalidate();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setDisplayedMnemonicIndexAt(int paramInt1, int paramInt2)
/*      */   {
/* 1623 */     checkIndex(paramInt1);
/*      */ 
/* 1625 */     Page localPage = (Page)this.pages.get(paramInt1);
/*      */ 
/* 1627 */     localPage.setDisplayedMnemonicIndex(paramInt2);
/*      */   }
/*      */ 
/*      */   public void setMnemonicAt(int paramInt1, int paramInt2)
/*      */   {
/* 1663 */     checkIndex(paramInt1);
/*      */ 
/* 1665 */     Page localPage = (Page)this.pages.get(paramInt1);
/* 1666 */     localPage.setMnemonic(paramInt2);
/*      */ 
/* 1668 */     firePropertyChange("mnemonicAt", null, null);
/*      */   }
/*      */ 
/*      */   public int indexOfTab(String paramString)
/*      */   {
/* 1682 */     for (int i = 0; i < getTabCount(); i++) {
/* 1683 */       if (getTitleAt(i).equals(paramString == null ? "" : paramString)) {
/* 1684 */         return i;
/*      */       }
/*      */     }
/* 1687 */     return -1;
/*      */   }
/*      */ 
/*      */   public int indexOfTab(Icon paramIcon)
/*      */   {
/* 1699 */     for (int i = 0; i < getTabCount(); i++) {
/* 1700 */       Icon localIcon = getIconAt(i);
/* 1701 */       if (((localIcon != null) && (localIcon.equals(paramIcon))) || ((localIcon == null) && (localIcon == paramIcon)))
/*      */       {
/* 1703 */         return i;
/*      */       }
/*      */     }
/* 1706 */     return -1;
/*      */   }
/*      */ 
/*      */   public int indexOfComponent(Component paramComponent)
/*      */   {
/* 1718 */     for (int i = 0; i < getTabCount(); i++) {
/* 1719 */       Component localComponent = getComponentAt(i);
/* 1720 */       if (((localComponent != null) && (localComponent.equals(paramComponent))) || ((localComponent == null) && (localComponent == paramComponent)))
/*      */       {
/* 1722 */         return i;
/*      */       }
/*      */     }
/* 1725 */     return -1;
/*      */   }
/*      */ 
/*      */   public int indexAtLocation(int paramInt1, int paramInt2)
/*      */   {
/* 1740 */     if (this.ui != null) {
/* 1741 */       return ((TabbedPaneUI)this.ui).tabForCoordinate(this, paramInt1, paramInt2);
/*      */     }
/* 1743 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getToolTipText(MouseEvent paramMouseEvent)
/*      */   {
/* 1756 */     if (this.ui != null) {
/* 1757 */       int i = ((TabbedPaneUI)this.ui).tabForCoordinate(this, paramMouseEvent.getX(), paramMouseEvent.getY());
/*      */ 
/* 1759 */       if (i != -1) {
/* 1760 */         return ((Page)this.pages.get(i)).tip;
/*      */       }
/*      */     }
/* 1763 */     return super.getToolTipText(paramMouseEvent);
/*      */   }
/*      */ 
/*      */   private void checkIndex(int paramInt) {
/* 1767 */     if ((paramInt < 0) || (paramInt >= this.pages.size()))
/* 1768 */       throw new IndexOutOfBoundsException("Index: " + paramInt + ", Tab count: " + this.pages.size());
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1779 */     paramObjectOutputStream.defaultWriteObject();
/* 1780 */     if (getUIClassID().equals("TabbedPaneUI")) {
/* 1781 */       byte b = JComponent.getWriteObjCounter(this);
/* 1782 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 1783 */       if ((b == 0) && (this.ui != null))
/* 1784 */         this.ui.installUI(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   void compWriteObjectNotify()
/*      */   {
/* 1794 */     super.compWriteObjectNotify();
/*      */ 
/* 1797 */     if ((getToolTipText() == null) && (this.haveRegistered))
/* 1798 */       ToolTipManager.sharedInstance().unregisterComponent(this);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1810 */     paramObjectInputStream.defaultReadObject();
/* 1811 */     if ((this.ui != null) && (getUIClassID().equals("TabbedPaneUI"))) {
/* 1812 */       this.ui.installUI(this);
/*      */     }
/*      */ 
/* 1816 */     if ((getToolTipText() == null) && (this.haveRegistered))
/* 1817 */       ToolTipManager.sharedInstance().registerComponent(this);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/*      */     String str1;
/* 1834 */     if (this.tabPlacement == 1)
/* 1835 */       str1 = "TOP";
/* 1836 */     else if (this.tabPlacement == 3)
/* 1837 */       str1 = "BOTTOM";
/* 1838 */     else if (this.tabPlacement == 2)
/* 1839 */       str1 = "LEFT";
/* 1840 */     else if (this.tabPlacement == 4)
/* 1841 */       str1 = "RIGHT";
/* 1842 */     else str1 = "";
/* 1843 */     String str2 = this.haveRegistered ? "true" : "false";
/*      */ 
/* 1846 */     return super.paramString() + ",haveRegistered=" + str2 + ",tabPlacement=" + str1;
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1865 */     if (this.accessibleContext == null) {
/* 1866 */       this.accessibleContext = new AccessibleJTabbedPane();
/*      */ 
/* 1869 */       int i = getTabCount();
/* 1870 */       for (int j = 0; j < i; j++) {
/* 1871 */         ((Page)this.pages.get(j)).initAccessibleContext();
/*      */       }
/*      */     }
/* 1874 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   public void setTabComponentAt(int paramInt, Component paramComponent)
/*      */   {
/* 2369 */     if ((paramComponent != null) && (indexOfComponent(paramComponent) != -1)) {
/* 2370 */       throw new IllegalArgumentException("Component is already added to this JTabbedPane");
/*      */     }
/* 2372 */     Component localComponent = getTabComponentAt(paramInt);
/* 2373 */     if (paramComponent != localComponent) {
/* 2374 */       int i = indexOfTabComponent(paramComponent);
/* 2375 */       if (i != -1) {
/* 2376 */         setTabComponentAt(i, null);
/*      */       }
/* 2378 */       ((Page)this.pages.get(paramInt)).tabComponent = paramComponent;
/* 2379 */       firePropertyChange("indexForTabComponent", -1, paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Component getTabComponentAt(int paramInt)
/*      */   {
/* 2395 */     return ((Page)this.pages.get(paramInt)).tabComponent;
/*      */   }
/*      */ 
/*      */   public int indexOfTabComponent(Component paramComponent)
/*      */   {
/* 2410 */     for (int i = 0; i < getTabCount(); i++) {
/* 2411 */       Component localComponent = getTabComponentAt(i);
/* 2412 */       if (localComponent == paramComponent) {
/* 2413 */         return i;
/*      */       }
/*      */     }
/* 2416 */     return -1;
/*      */   }
/*      */ 
/*      */   protected class AccessibleJTabbedPane extends JComponent.AccessibleJComponent
/*      */     implements AccessibleSelection, ChangeListener
/*      */   {
/*      */     public String getAccessibleName()
/*      */     {
/* 1903 */       if (this.accessibleName != null) {
/* 1904 */         return this.accessibleName;
/*      */       }
/*      */ 
/* 1907 */       String str = (String)JTabbedPane.this.getClientProperty("AccessibleName");
/*      */ 
/* 1909 */       if (str != null) {
/* 1910 */         return str;
/*      */       }
/*      */ 
/* 1913 */       int i = JTabbedPane.this.getSelectedIndex();
/*      */ 
/* 1915 */       if (i >= 0) {
/* 1916 */         return ((JTabbedPane.Page)JTabbedPane.this.pages.get(i)).getAccessibleName();
/*      */       }
/*      */ 
/* 1919 */       return super.getAccessibleName();
/*      */     }
/*      */ 
/*      */     public AccessibleJTabbedPane()
/*      */     {
/* 1926 */       super();
/* 1927 */       JTabbedPane.this.model.addChangeListener(this);
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent) {
/* 1931 */       Object localObject = paramChangeEvent.getSource();
/* 1932 */       firePropertyChange("AccessibleSelection", null, localObject);
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1943 */       return AccessibleRole.PAGE_TAB_LIST;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 1952 */       return JTabbedPane.this.getTabCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 1963 */       if ((paramInt < 0) || (paramInt >= JTabbedPane.this.getTabCount())) {
/* 1964 */         return null;
/*      */       }
/* 1966 */       return (Accessible)JTabbedPane.this.pages.get(paramInt);
/*      */     }
/*      */ 
/*      */     public AccessibleSelection getAccessibleSelection()
/*      */     {
/* 1979 */       return this;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/* 1991 */       int i = ((TabbedPaneUI)JTabbedPane.this.ui).tabForCoordinate(JTabbedPane.this, paramPoint.x, paramPoint.y);
/*      */ 
/* 1993 */       if (i == -1) {
/* 1994 */         i = JTabbedPane.this.getSelectedIndex();
/*      */       }
/* 1996 */       return getAccessibleChild(i);
/*      */     }
/*      */ 
/*      */     public int getAccessibleSelectionCount() {
/* 2000 */       return 1;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleSelection(int paramInt) {
/* 2004 */       int i = JTabbedPane.this.getSelectedIndex();
/* 2005 */       if (i == -1) {
/* 2006 */         return null;
/*      */       }
/* 2008 */       return (Accessible)JTabbedPane.this.pages.get(i);
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleChildSelected(int paramInt) {
/* 2012 */       return paramInt == JTabbedPane.this.getSelectedIndex();
/*      */     }
/*      */ 
/*      */     public void addAccessibleSelection(int paramInt) {
/* 2016 */       JTabbedPane.this.setSelectedIndex(paramInt);
/*      */     }
/*      */ 
/*      */     public void removeAccessibleSelection(int paramInt)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void clearAccessibleSelection()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void selectAllAccessibleSelection()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class ModelListener
/*      */     implements ChangeListener, Serializable
/*      */   {
/*      */     protected ModelListener()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void stateChanged(ChangeEvent paramChangeEvent)
/*      */     {
/*  270 */       JTabbedPane.this.fireStateChanged();
/*      */     }
/*      */   }
/*      */ 
/*      */   private class Page extends AccessibleContext
/*      */     implements Serializable, Accessible, AccessibleComponent
/*      */   {
/*      */     String title;
/*      */     Color background;
/*      */     Color foreground;
/*      */     Icon icon;
/*      */     Icon disabledIcon;
/*      */     JTabbedPane parent;
/*      */     Component component;
/*      */     String tip;
/* 2042 */     boolean enabled = true;
/*      */     boolean needsUIUpdate;
/* 2044 */     int mnemonic = -1;
/* 2045 */     int mnemonicIndex = -1;
/*      */     Component tabComponent;
/*      */ 
/*      */     Page(JTabbedPane paramString1, String paramIcon1, Icon paramIcon2, Icon paramComponent, Component paramString2, String arg7)
/*      */     {
/* 2050 */       this.title = paramIcon1;
/* 2051 */       this.icon = paramIcon2;
/* 2052 */       this.disabledIcon = paramComponent;
/* 2053 */       this.parent = paramString1;
/* 2054 */       setAccessibleParent(paramString1);
/* 2055 */       this.component = paramString2;
/*      */       Object localObject;
/* 2056 */       this.tip = localObject;
/*      */ 
/* 2058 */       initAccessibleContext();
/*      */     }
/*      */ 
/*      */     void initAccessibleContext()
/*      */     {
/* 2065 */       if ((JTabbedPane.this.accessibleContext != null) && ((this.component instanceof Accessible)))
/*      */       {
/* 2073 */         AccessibleContext localAccessibleContext = this.component.getAccessibleContext();
/* 2074 */         if (localAccessibleContext != null)
/* 2075 */           localAccessibleContext.setAccessibleParent(this);
/*      */       }
/*      */     }
/*      */ 
/*      */     void setMnemonic(int paramInt)
/*      */     {
/* 2081 */       this.mnemonic = paramInt;
/* 2082 */       updateDisplayedMnemonicIndex();
/*      */     }
/*      */ 
/*      */     int getMnemonic() {
/* 2086 */       return this.mnemonic;
/*      */     }
/*      */ 
/*      */     void setDisplayedMnemonicIndex(int paramInt)
/*      */     {
/* 2093 */       if (this.mnemonicIndex != paramInt) {
/* 2094 */         if ((paramInt != -1) && ((this.title == null) || (paramInt < 0) || (paramInt >= this.title.length())))
/*      */         {
/* 2097 */           throw new IllegalArgumentException("Invalid mnemonic index: " + paramInt);
/*      */         }
/*      */ 
/* 2100 */         this.mnemonicIndex = paramInt;
/* 2101 */         JTabbedPane.this.firePropertyChange("displayedMnemonicIndexAt", null, null);
/*      */       }
/*      */     }
/*      */ 
/*      */     int getDisplayedMnemonicIndex()
/*      */     {
/* 2110 */       return this.mnemonicIndex;
/*      */     }
/*      */ 
/*      */     void updateDisplayedMnemonicIndex() {
/* 2114 */       setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(this.title, this.mnemonic));
/*      */     }
/*      */ 
/*      */     public AccessibleContext getAccessibleContext()
/*      */     {
/* 2123 */       return this;
/*      */     }
/*      */ 
/*      */     public String getAccessibleName()
/*      */     {
/* 2130 */       if (this.accessibleName != null)
/* 2131 */         return this.accessibleName;
/* 2132 */       if (this.title != null) {
/* 2133 */         return this.title;
/*      */       }
/* 2135 */       return null;
/*      */     }
/*      */ 
/*      */     public String getAccessibleDescription() {
/* 2139 */       if (this.accessibleDescription != null)
/* 2140 */         return this.accessibleDescription;
/* 2141 */       if (this.tip != null) {
/* 2142 */         return this.tip;
/*      */       }
/* 2144 */       return null;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole() {
/* 2148 */       return AccessibleRole.PAGE_TAB;
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 2153 */       AccessibleStateSet localAccessibleStateSet = this.parent.getAccessibleContext().getAccessibleStateSet();
/* 2154 */       localAccessibleStateSet.add(AccessibleState.SELECTABLE);
/* 2155 */       int i = this.parent.indexOfTab(this.title);
/* 2156 */       if (i == this.parent.getSelectedIndex()) {
/* 2157 */         localAccessibleStateSet.add(AccessibleState.SELECTED);
/*      */       }
/* 2159 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public int getAccessibleIndexInParent() {
/* 2163 */       return this.parent.indexOfTab(this.title);
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount() {
/* 2167 */       if ((this.component instanceof Accessible)) {
/* 2168 */         return 1;
/*      */       }
/* 2170 */       return 0;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 2175 */       if ((this.component instanceof Accessible)) {
/* 2176 */         return (Accessible)this.component;
/*      */       }
/* 2178 */       return null;
/*      */     }
/*      */ 
/*      */     public Locale getLocale()
/*      */     {
/* 2183 */       return this.parent.getLocale();
/*      */     }
/*      */ 
/*      */     public AccessibleComponent getAccessibleComponent() {
/* 2187 */       return this;
/*      */     }
/*      */ 
/*      */     public Color getBackground()
/*      */     {
/* 2194 */       return this.background != null ? this.background : this.parent.getBackground();
/*      */     }
/*      */ 
/*      */     public void setBackground(Color paramColor) {
/* 2198 */       this.background = paramColor;
/*      */     }
/*      */ 
/*      */     public Color getForeground() {
/* 2202 */       return this.foreground != null ? this.foreground : this.parent.getForeground();
/*      */     }
/*      */ 
/*      */     public void setForeground(Color paramColor) {
/* 2206 */       this.foreground = paramColor;
/*      */     }
/*      */ 
/*      */     public Cursor getCursor() {
/* 2210 */       return this.parent.getCursor();
/*      */     }
/*      */ 
/*      */     public void setCursor(Cursor paramCursor) {
/* 2214 */       this.parent.setCursor(paramCursor);
/*      */     }
/*      */ 
/*      */     public Font getFont() {
/* 2218 */       return this.parent.getFont();
/*      */     }
/*      */ 
/*      */     public void setFont(Font paramFont) {
/* 2222 */       this.parent.setFont(paramFont);
/*      */     }
/*      */ 
/*      */     public FontMetrics getFontMetrics(Font paramFont) {
/* 2226 */       return this.parent.getFontMetrics(paramFont);
/*      */     }
/*      */ 
/*      */     public boolean isEnabled() {
/* 2230 */       return this.enabled;
/*      */     }
/*      */ 
/*      */     public void setEnabled(boolean paramBoolean) {
/* 2234 */       this.enabled = paramBoolean;
/*      */     }
/*      */ 
/*      */     public boolean isVisible() {
/* 2238 */       return this.parent.isVisible();
/*      */     }
/*      */ 
/*      */     public void setVisible(boolean paramBoolean) {
/* 2242 */       this.parent.setVisible(paramBoolean);
/*      */     }
/*      */ 
/*      */     public boolean isShowing() {
/* 2246 */       return this.parent.isShowing();
/*      */     }
/*      */ 
/*      */     public boolean contains(Point paramPoint) {
/* 2250 */       Rectangle localRectangle = getBounds();
/* 2251 */       return localRectangle.contains(paramPoint);
/*      */     }
/*      */ 
/*      */     public Point getLocationOnScreen() {
/* 2255 */       Point localPoint1 = this.parent.getLocationOnScreen();
/* 2256 */       Point localPoint2 = getLocation();
/* 2257 */       localPoint2.translate(localPoint1.x, localPoint1.y);
/* 2258 */       return localPoint2;
/*      */     }
/*      */ 
/*      */     public Point getLocation() {
/* 2262 */       Rectangle localRectangle = getBounds();
/* 2263 */       return new Point(localRectangle.x, localRectangle.y);
/*      */     }
/*      */ 
/*      */     public void setLocation(Point paramPoint)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Rectangle getBounds() {
/* 2271 */       return this.parent.getUI().getTabBounds(this.parent, this.parent.indexOfTab(this.title));
/*      */     }
/*      */ 
/*      */     public void setBounds(Rectangle paramRectangle)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Dimension getSize()
/*      */     {
/* 2280 */       Rectangle localRectangle = getBounds();
/* 2281 */       return new Dimension(localRectangle.width, localRectangle.height);
/*      */     }
/*      */ 
/*      */     public void setSize(Dimension paramDimension)
/*      */     {
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint) {
/* 2289 */       if ((this.component instanceof Accessible)) {
/* 2290 */         return (Accessible)this.component;
/*      */       }
/* 2292 */       return null;
/*      */     }
/*      */ 
/*      */     public boolean isFocusTraversable()
/*      */     {
/* 2297 */       return false;
/*      */     }
/*      */ 
/*      */     public void requestFocus()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addFocusListener(FocusListener paramFocusListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void removeFocusListener(FocusListener paramFocusListener)
/*      */     {
/*      */     }
/*      */ 
/*      */     public AccessibleIcon[] getAccessibleIcon()
/*      */     {
/* 2322 */       AccessibleIcon localAccessibleIcon = null;
/*      */       Object localObject;
/* 2323 */       if ((this.enabled) && ((this.icon instanceof ImageIcon))) {
/* 2324 */         localObject = ((ImageIcon)this.icon).getAccessibleContext();
/*      */ 
/* 2326 */         localAccessibleIcon = (AccessibleIcon)localObject;
/* 2327 */       } else if ((!this.enabled) && ((this.disabledIcon instanceof ImageIcon))) {
/* 2328 */         localObject = ((ImageIcon)this.disabledIcon).getAccessibleContext();
/*      */ 
/* 2330 */         localAccessibleIcon = (AccessibleIcon)localObject;
/*      */       }
/* 2332 */       if (localAccessibleIcon != null) {
/* 2333 */         localObject = new AccessibleIcon[1];
/* 2334 */         localObject[0] = localAccessibleIcon;
/* 2335 */         return localObject;
/*      */       }
/* 2337 */       return null;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JTabbedPane
 * JD-Core Version:    0.6.2
 */