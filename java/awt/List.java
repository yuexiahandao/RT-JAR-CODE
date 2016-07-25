/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.ItemEvent;
/*      */ import java.awt.event.ItemListener;
/*      */ import java.awt.peer.ListPeer;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.util.EventListener;
/*      */ import java.util.Locale;
/*      */ import java.util.Vector;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleSelection;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ 
/*      */ public class List extends Component
/*      */   implements ItemSelectable, Accessible
/*      */ {
/*  118 */   Vector items = new Vector();
/*      */ 
/*  129 */   int rows = 0;
/*      */ 
/*  145 */   boolean multipleMode = false;
/*      */ 
/*  155 */   int[] selected = new int[0];
/*      */ 
/*  164 */   int visibleIndex = -1;
/*      */   transient ActionListener actionListener;
/*      */   transient ItemListener itemListener;
/*      */   private static final String base = "list";
/*  170 */   private static int nameCounter = 0;
/*      */   private static final long serialVersionUID = -3304312411574666869L;
/*      */   static final int DEFAULT_VISIBLE_ROWS = 4;
/* 1177 */   private int listSerializedDataVersion = 1;
/*      */ 
/*      */   public List()
/*      */     throws HeadlessException
/*      */   {
/*  188 */     this(0, false);
/*      */   }
/*      */ 
/*      */   public List(int paramInt)
/*      */     throws HeadlessException
/*      */   {
/*  205 */     this(paramInt, false);
/*      */   }
/*      */ 
/*      */   public List(int paramInt, boolean paramBoolean)
/*      */     throws HeadlessException
/*      */   {
/*  233 */     GraphicsEnvironment.checkHeadless();
/*  234 */     this.rows = (paramInt != 0 ? paramInt : 4);
/*  235 */     this.multipleMode = paramBoolean;
/*      */   }
/*      */ 
/*      */   String constructComponentName()
/*      */   {
/*  243 */     synchronized (List.class) {
/*  244 */       return "list" + nameCounter++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/*  253 */     synchronized (getTreeLock()) {
/*  254 */       if (this.peer == null)
/*  255 */         this.peer = getToolkit().createList(this);
/*  256 */       super.addNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNotify()
/*      */   {
/*  265 */     synchronized (getTreeLock()) {
/*  266 */       ListPeer localListPeer = (ListPeer)this.peer;
/*  267 */       if (localListPeer != null) {
/*  268 */         this.selected = localListPeer.getSelectedIndexes();
/*      */       }
/*  270 */       super.removeNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getItemCount()
/*      */   {
/*  281 */     return countItems();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int countItems()
/*      */   {
/*  290 */     return this.items.size();
/*      */   }
/*      */ 
/*      */   public String getItem(int paramInt)
/*      */   {
/*  301 */     return getItemImpl(paramInt);
/*      */   }
/*      */ 
/*      */   final String getItemImpl(int paramInt)
/*      */   {
/*  309 */     return (String)this.items.elementAt(paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized String[] getItems()
/*      */   {
/*  321 */     String[] arrayOfString = new String[this.items.size()];
/*  322 */     this.items.copyInto(arrayOfString);
/*  323 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public void add(String paramString)
/*      */   {
/*  332 */     addItem(paramString);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void addItem(String paramString)
/*      */   {
/*  340 */     addItem(paramString, -1);
/*      */   }
/*      */ 
/*      */   public void add(String paramString, int paramInt)
/*      */   {
/*  357 */     addItem(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public synchronized void addItem(String paramString, int paramInt)
/*      */   {
/*  365 */     if ((paramInt < -1) || (paramInt >= this.items.size())) {
/*  366 */       paramInt = -1;
/*      */     }
/*      */ 
/*  369 */     if (paramString == null) {
/*  370 */       paramString = "";
/*      */     }
/*      */ 
/*  373 */     if (paramInt == -1)
/*  374 */       this.items.addElement(paramString);
/*      */     else {
/*  376 */       this.items.insertElementAt(paramString, paramInt);
/*      */     }
/*      */ 
/*  379 */     ListPeer localListPeer = (ListPeer)this.peer;
/*  380 */     if (localListPeer != null)
/*  381 */       localListPeer.add(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized void replaceItem(String paramString, int paramInt)
/*      */   {
/*  394 */     remove(paramInt);
/*  395 */     add(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   public void removeAll()
/*      */   {
/*  405 */     clear();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public synchronized void clear()
/*      */   {
/*  414 */     ListPeer localListPeer = (ListPeer)this.peer;
/*  415 */     if (localListPeer != null) {
/*  416 */       localListPeer.removeAll();
/*      */     }
/*  418 */     this.items = new Vector();
/*  419 */     this.selected = new int[0];
/*      */   }
/*      */ 
/*      */   public synchronized void remove(String paramString)
/*      */   {
/*  432 */     int i = this.items.indexOf(paramString);
/*  433 */     if (i < 0) {
/*  434 */       throw new IllegalArgumentException("item " + paramString + " not found in list");
/*      */     }
/*      */ 
/*  437 */     remove(i);
/*      */   }
/*      */ 
/*      */   public void remove(int paramInt)
/*      */   {
/*  454 */     delItem(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void delItem(int paramInt)
/*      */   {
/*  463 */     delItems(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   public synchronized int getSelectedIndex()
/*      */   {
/*  477 */     int[] arrayOfInt = getSelectedIndexes();
/*  478 */     return arrayOfInt.length == 1 ? arrayOfInt[0] : -1;
/*      */   }
/*      */ 
/*      */   public synchronized int[] getSelectedIndexes()
/*      */   {
/*  491 */     ListPeer localListPeer = (ListPeer)this.peer;
/*  492 */     if (localListPeer != null) {
/*  493 */       this.selected = localListPeer.getSelectedIndexes();
/*      */     }
/*  495 */     return (int[])this.selected.clone();
/*      */   }
/*      */ 
/*      */   public synchronized String getSelectedItem()
/*      */   {
/*  509 */     int i = getSelectedIndex();
/*  510 */     return i < 0 ? null : getItem(i);
/*      */   }
/*      */ 
/*      */   public synchronized String[] getSelectedItems()
/*      */   {
/*  523 */     int[] arrayOfInt = getSelectedIndexes();
/*  524 */     String[] arrayOfString = new String[arrayOfInt.length];
/*  525 */     for (int i = 0; i < arrayOfInt.length; i++) {
/*  526 */       arrayOfString[i] = getItem(arrayOfInt[i]);
/*      */     }
/*  528 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public Object[] getSelectedObjects()
/*      */   {
/*  540 */     return getSelectedItems();
/*      */   }
/*      */ 
/*      */   public void select(int paramInt)
/*      */   {
/*      */     ListPeer localListPeer;
/*      */     do
/*      */     {
/*  569 */       localListPeer = (ListPeer)this.peer;
/*  570 */       if (localListPeer != null) {
/*  571 */         localListPeer.select(paramInt);
/*  572 */         return;
/*      */       }
/*      */ 
/*  575 */       synchronized (this)
/*      */       {
/*  577 */         int i = 0;
/*      */ 
/*  579 */         for (int j = 0; j < this.selected.length; j++) {
/*  580 */           if (this.selected[j] == paramInt) {
/*  581 */             i = 1;
/*  582 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  586 */         if (i == 0)
/*  587 */           if (!this.multipleMode) {
/*  588 */             this.selected = new int[1];
/*  589 */             this.selected[0] = paramInt;
/*      */           } else {
/*  591 */             int[] arrayOfInt = new int[this.selected.length + 1];
/*  592 */             System.arraycopy(this.selected, 0, arrayOfInt, 0, this.selected.length);
/*      */ 
/*  594 */             arrayOfInt[this.selected.length] = paramInt;
/*  595 */             this.selected = arrayOfInt;
/*      */           }
/*      */       }
/*      */     }
/*  599 */     while (localListPeer != this.peer);
/*      */   }
/*      */ 
/*      */   public synchronized void deselect(int paramInt)
/*      */   {
/*  616 */     ListPeer localListPeer = (ListPeer)this.peer;
/*  617 */     if ((localListPeer != null) && (
/*  618 */       (isMultipleMode()) || (getSelectedIndex() == paramInt))) {
/*  619 */       localListPeer.deselect(paramInt);
/*      */     }
/*      */ 
/*  623 */     for (int i = 0; i < this.selected.length; i++)
/*  624 */       if (this.selected[i] == paramInt) {
/*  625 */         int[] arrayOfInt = new int[this.selected.length - 1];
/*  626 */         System.arraycopy(this.selected, 0, arrayOfInt, 0, i);
/*  627 */         System.arraycopy(this.selected, i + 1, arrayOfInt, i, this.selected.length - (i + 1));
/*  628 */         this.selected = arrayOfInt;
/*  629 */         return;
/*      */       }
/*      */   }
/*      */ 
/*      */   public boolean isIndexSelected(int paramInt)
/*      */   {
/*  645 */     return isSelected(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean isSelected(int paramInt)
/*      */   {
/*  654 */     int[] arrayOfInt = getSelectedIndexes();
/*  655 */     for (int i = 0; i < arrayOfInt.length; i++) {
/*  656 */       if (arrayOfInt[i] == paramInt) {
/*  657 */         return true;
/*      */       }
/*      */     }
/*  660 */     return false;
/*      */   }
/*      */ 
/*      */   public int getRows()
/*      */   {
/*  670 */     return this.rows;
/*      */   }
/*      */ 
/*      */   public boolean isMultipleMode()
/*      */   {
/*  681 */     return allowsMultipleSelections();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean allowsMultipleSelections()
/*      */   {
/*  690 */     return this.multipleMode;
/*      */   }
/*      */ 
/*      */   public void setMultipleMode(boolean paramBoolean)
/*      */   {
/*  708 */     setMultipleSelections(paramBoolean);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public synchronized void setMultipleSelections(boolean paramBoolean)
/*      */   {
/*  717 */     if (paramBoolean != this.multipleMode) {
/*  718 */       this.multipleMode = paramBoolean;
/*  719 */       ListPeer localListPeer = (ListPeer)this.peer;
/*  720 */       if (localListPeer != null)
/*  721 */         localListPeer.setMultipleMode(paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getVisibleIndex()
/*      */   {
/*  733 */     return this.visibleIndex;
/*      */   }
/*      */ 
/*      */   public synchronized void makeVisible(int paramInt)
/*      */   {
/*  742 */     this.visibleIndex = paramInt;
/*  743 */     ListPeer localListPeer = (ListPeer)this.peer;
/*  744 */     if (localListPeer != null)
/*  745 */       localListPeer.makeVisible(paramInt);
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize(int paramInt)
/*      */   {
/*  759 */     return preferredSize(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Dimension preferredSize(int paramInt)
/*      */   {
/*  768 */     synchronized (getTreeLock()) {
/*  769 */       ListPeer localListPeer = (ListPeer)this.peer;
/*  770 */       return localListPeer != null ? localListPeer.getPreferredSize(paramInt) : super.preferredSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getPreferredSize()
/*      */   {
/*  783 */     return preferredSize();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Dimension preferredSize()
/*      */   {
/*  792 */     synchronized (getTreeLock()) {
/*  793 */       return this.rows > 0 ? preferredSize(this.rows) : super.preferredSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize(int paramInt)
/*      */   {
/*  809 */     return minimumSize(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Dimension minimumSize(int paramInt)
/*      */   {
/*  818 */     synchronized (getTreeLock()) {
/*  819 */       ListPeer localListPeer = (ListPeer)this.peer;
/*  820 */       return localListPeer != null ? localListPeer.getMinimumSize(paramInt) : super.minimumSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Dimension getMinimumSize()
/*      */   {
/*  834 */     return minimumSize();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Dimension minimumSize()
/*      */   {
/*  843 */     synchronized (getTreeLock()) {
/*  844 */       return this.rows > 0 ? minimumSize(this.rows) : super.minimumSize();
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void addItemListener(ItemListener paramItemListener)
/*      */   {
/*  867 */     if (paramItemListener == null) {
/*  868 */       return;
/*      */     }
/*  870 */     this.itemListener = AWTEventMulticaster.add(this.itemListener, paramItemListener);
/*  871 */     this.newEventsOnly = true;
/*      */   }
/*      */ 
/*      */   public synchronized void removeItemListener(ItemListener paramItemListener)
/*      */   {
/*  890 */     if (paramItemListener == null) {
/*  891 */       return;
/*      */     }
/*  893 */     this.itemListener = AWTEventMulticaster.remove(this.itemListener, paramItemListener);
/*      */   }
/*      */ 
/*      */   public synchronized ItemListener[] getItemListeners()
/*      */   {
/*  911 */     return (ItemListener[])getListeners(ItemListener.class);
/*      */   }
/*      */ 
/*      */   public synchronized void addActionListener(ActionListener paramActionListener)
/*      */   {
/*  933 */     if (paramActionListener == null) {
/*  934 */       return;
/*      */     }
/*  936 */     this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
/*  937 */     this.newEventsOnly = true;
/*      */   }
/*      */ 
/*      */   public synchronized void removeActionListener(ActionListener paramActionListener)
/*      */   {
/*  957 */     if (paramActionListener == null) {
/*  958 */       return;
/*      */     }
/*  960 */     this.actionListener = AWTEventMulticaster.remove(this.actionListener, paramActionListener);
/*      */   }
/*      */ 
/*      */   public synchronized ActionListener[] getActionListeners()
/*      */   {
/*  978 */     return (ActionListener[])getListeners(ActionListener.class);
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/* 1015 */     Object localObject = null;
/* 1016 */     if (paramClass == ActionListener.class)
/* 1017 */       localObject = this.actionListener;
/* 1018 */     else if (paramClass == ItemListener.class)
/* 1019 */       localObject = this.itemListener;
/*      */     else {
/* 1021 */       return super.getListeners(paramClass);
/*      */     }
/* 1023 */     return AWTEventMulticaster.getListeners((EventListener)localObject, paramClass);
/*      */   }
/*      */ 
/*      */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*      */   {
/* 1028 */     switch (paramAWTEvent.id) {
/*      */     case 1001:
/* 1030 */       if (((this.eventMask & 0x80) != 0L) || (this.actionListener != null))
/*      */       {
/* 1032 */         return true;
/*      */       }
/* 1034 */       return false;
/*      */     case 701:
/* 1036 */       if (((this.eventMask & 0x200) != 0L) || (this.itemListener != null))
/*      */       {
/* 1038 */         return true;
/*      */       }
/* 1040 */       return false;
/*      */     }
/*      */ 
/* 1044 */     return super.eventEnabled(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processEvent(AWTEvent paramAWTEvent)
/*      */   {
/* 1067 */     if ((paramAWTEvent instanceof ItemEvent)) {
/* 1068 */       processItemEvent((ItemEvent)paramAWTEvent);
/* 1069 */       return;
/* 1070 */     }if ((paramAWTEvent instanceof ActionEvent)) {
/* 1071 */       processActionEvent((ActionEvent)paramAWTEvent);
/* 1072 */       return;
/*      */     }
/* 1074 */     super.processEvent(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processItemEvent(ItemEvent paramItemEvent)
/*      */   {
/* 1102 */     ItemListener localItemListener = this.itemListener;
/* 1103 */     if (localItemListener != null)
/* 1104 */       localItemListener.itemStateChanged(paramItemEvent);
/*      */   }
/*      */ 
/*      */   protected void processActionEvent(ActionEvent paramActionEvent)
/*      */   {
/* 1133 */     ActionListener localActionListener = this.actionListener;
/* 1134 */     if (localActionListener != null)
/* 1135 */       localActionListener.actionPerformed(paramActionEvent);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1145 */     return super.paramString() + ",selected=" + getSelectedItem();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public synchronized void delItems(int paramInt1, int paramInt2)
/*      */   {
/* 1156 */     for (int i = paramInt2; i >= paramInt1; i--) {
/* 1157 */       this.items.removeElementAt(i);
/*      */     }
/* 1159 */     ListPeer localListPeer = (ListPeer)this.peer;
/* 1160 */     if (localListPeer != null)
/* 1161 */       localListPeer.delItems(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1205 */     synchronized (this) {
/* 1206 */       ListPeer localListPeer = (ListPeer)this.peer;
/* 1207 */       if (localListPeer != null) {
/* 1208 */         this.selected = localListPeer.getSelectedIndexes();
/*      */       }
/*      */     }
/* 1211 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1213 */     AWTEventMulticaster.save(paramObjectOutputStream, "itemL", this.itemListener);
/* 1214 */     AWTEventMulticaster.save(paramObjectOutputStream, "actionL", this.actionListener);
/* 1215 */     paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException, HeadlessException
/*      */   {
/* 1238 */     GraphicsEnvironment.checkHeadless();
/* 1239 */     paramObjectInputStream.defaultReadObject();
/*      */     Object localObject;
/* 1242 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 1243 */       String str = ((String)localObject).intern();
/*      */ 
/* 1245 */       if ("itemL" == str) {
/* 1246 */         addItemListener((ItemListener)paramObjectInputStream.readObject());
/*      */       }
/* 1248 */       else if ("actionL" == str) {
/* 1249 */         addActionListener((ActionListener)paramObjectInputStream.readObject());
/*      */       }
/*      */       else
/* 1252 */         paramObjectInputStream.readObject();
/*      */     }
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1273 */     if (this.accessibleContext == null) {
/* 1274 */       this.accessibleContext = new AccessibleAWTList();
/*      */     }
/* 1276 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   protected class AccessibleAWTList extends Component.AccessibleAWTComponent
/*      */     implements AccessibleSelection, ItemListener, ActionListener
/*      */   {
/*      */     private static final long serialVersionUID = 7924617370136012829L;
/*      */ 
/*      */     public AccessibleAWTList()
/*      */     {
/* 1294 */       super();
/* 1295 */       List.this.addActionListener(this);
/* 1296 */       List.this.addItemListener(this);
/*      */     }
/*      */ 
/*      */     public void actionPerformed(ActionEvent paramActionEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void itemStateChanged(ItemEvent paramItemEvent)
/*      */     {
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 1313 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 1314 */       if (List.this.isMultipleMode()) {
/* 1315 */         localAccessibleStateSet.add(AccessibleState.MULTISELECTABLE);
/*      */       }
/* 1317 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1328 */       return AccessibleRole.LIST;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleAt(Point paramPoint)
/*      */     {
/* 1338 */       return null;
/*      */     }
/*      */ 
/*      */     public int getAccessibleChildrenCount()
/*      */     {
/* 1349 */       return List.this.getItemCount();
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleChild(int paramInt)
/*      */     {
/* 1359 */       synchronized (List.this) {
/* 1360 */         if (paramInt >= List.this.getItemCount()) {
/* 1361 */           return null;
/*      */         }
/* 1363 */         return new AccessibleAWTListChild(List.this, paramInt);
/*      */       }
/*      */     }
/*      */ 
/*      */     public AccessibleSelection getAccessibleSelection()
/*      */     {
/* 1377 */       return this;
/*      */     }
/*      */ 
/*      */     public int getAccessibleSelectionCount()
/*      */     {
/* 1389 */       return List.this.getSelectedIndexes().length;
/*      */     }
/*      */ 
/*      */     public Accessible getAccessibleSelection(int paramInt)
/*      */     {
/* 1402 */       synchronized (List.this) {
/* 1403 */         int i = getAccessibleSelectionCount();
/* 1404 */         if ((paramInt < 0) || (paramInt >= i)) {
/* 1405 */           return null;
/*      */         }
/* 1407 */         return getAccessibleChild(List.this.getSelectedIndexes()[paramInt]);
/*      */       }
/*      */     }
/*      */ 
/*      */     public boolean isAccessibleChildSelected(int paramInt)
/*      */     {
/* 1420 */       return List.this.isIndexSelected(paramInt);
/*      */     }
/*      */ 
/*      */     public void addAccessibleSelection(int paramInt)
/*      */     {
/* 1433 */       List.this.select(paramInt);
/*      */     }
/*      */ 
/*      */     public void removeAccessibleSelection(int paramInt)
/*      */     {
/* 1444 */       List.this.deselect(paramInt);
/*      */     }
/*      */ 
/*      */     public void clearAccessibleSelection()
/*      */     {
/* 1452 */       synchronized (List.this) {
/* 1453 */         int[] arrayOfInt = List.this.getSelectedIndexes();
/* 1454 */         if (arrayOfInt == null)
/* 1455 */           return;
/* 1456 */         for (int i = arrayOfInt.length - 1; i >= 0; i--)
/* 1457 */           List.this.deselect(arrayOfInt[i]);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void selectAllAccessibleSelection()
/*      */     {
/* 1467 */       synchronized (List.this) {
/* 1468 */         for (int i = List.this.getItemCount() - 1; i >= 0; i--)
/* 1469 */           List.this.select(i);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected class AccessibleAWTListChild extends Component.AccessibleAWTComponent
/*      */       implements Accessible
/*      */     {
/*      */       private static final long serialVersionUID = 4412022926028300317L;
/*      */       private List parent;
/*      */       private int indexInParent;
/*      */ 
/*      */       public AccessibleAWTListChild(List paramInt, int arg3)
/*      */       {
/* 1494 */         super();
/* 1495 */         this.parent = paramInt;
/* 1496 */         setAccessibleParent(paramInt);
/*      */         int i;
/* 1497 */         this.indexInParent = i;
/*      */       }
/*      */ 
/*      */       public AccessibleContext getAccessibleContext()
/*      */       {
/* 1511 */         return this;
/*      */       }
/*      */ 
/*      */       public AccessibleRole getAccessibleRole()
/*      */       {
/* 1526 */         return AccessibleRole.LIST_ITEM;
/*      */       }
/*      */ 
/*      */       public AccessibleStateSet getAccessibleStateSet()
/*      */       {
/* 1543 */         AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 1544 */         if (this.parent.isIndexSelected(this.indexInParent)) {
/* 1545 */           localAccessibleStateSet.add(AccessibleState.SELECTED);
/*      */         }
/* 1547 */         return localAccessibleStateSet;
/*      */       }
/*      */ 
/*      */       public Locale getLocale()
/*      */       {
/* 1563 */         return this.parent.getLocale();
/*      */       }
/*      */ 
/*      */       public int getAccessibleIndexInParent()
/*      */       {
/* 1577 */         return this.indexInParent;
/*      */       }
/*      */ 
/*      */       public int getAccessibleChildrenCount()
/*      */       {
/* 1586 */         return 0;
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleChild(int paramInt)
/*      */       {
/* 1600 */         return null;
/*      */       }
/*      */ 
/*      */       public Color getBackground()
/*      */       {
/* 1616 */         return this.parent.getBackground();
/*      */       }
/*      */ 
/*      */       public void setBackground(Color paramColor)
/*      */       {
/* 1626 */         this.parent.setBackground(paramColor);
/*      */       }
/*      */ 
/*      */       public Color getForeground()
/*      */       {
/* 1637 */         return this.parent.getForeground();
/*      */       }
/*      */ 
/*      */       public void setForeground(Color paramColor)
/*      */       {
/* 1647 */         this.parent.setForeground(paramColor);
/*      */       }
/*      */ 
/*      */       public Cursor getCursor()
/*      */       {
/* 1657 */         return this.parent.getCursor();
/*      */       }
/*      */ 
/*      */       public void setCursor(Cursor paramCursor)
/*      */       {
/* 1670 */         this.parent.setCursor(paramCursor);
/*      */       }
/*      */ 
/*      */       public Font getFont()
/*      */       {
/* 1680 */         return this.parent.getFont();
/*      */       }
/*      */ 
/*      */       public void setFont(Font paramFont)
/*      */       {
/* 1690 */         this.parent.setFont(paramFont);
/*      */       }
/*      */ 
/*      */       public FontMetrics getFontMetrics(Font paramFont)
/*      */       {
/* 1701 */         return this.parent.getFontMetrics(paramFont);
/*      */       }
/*      */ 
/*      */       public boolean isEnabled()
/*      */       {
/* 1716 */         return this.parent.isEnabled();
/*      */       }
/*      */ 
/*      */       public void setEnabled(boolean paramBoolean)
/*      */       {
/* 1726 */         this.parent.setEnabled(paramBoolean);
/*      */       }
/*      */ 
/*      */       public boolean isVisible()
/*      */       {
/* 1746 */         return false;
/*      */       }
/*      */ 
/*      */       public void setVisible(boolean paramBoolean)
/*      */       {
/* 1758 */         this.parent.setVisible(paramBoolean);
/*      */       }
/*      */ 
/*      */       public boolean isShowing()
/*      */       {
/* 1773 */         return false;
/*      */       }
/*      */ 
/*      */       public boolean contains(Point paramPoint)
/*      */       {
/* 1789 */         return false;
/*      */       }
/*      */ 
/*      */       public Point getLocationOnScreen()
/*      */       {
/* 1803 */         return null;
/*      */       }
/*      */ 
/*      */       public Point getLocation()
/*      */       {
/* 1819 */         return null;
/*      */       }
/*      */ 
/*      */       public void setLocation(Point paramPoint)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Rectangle getBounds()
/*      */       {
/* 1842 */         return null;
/*      */       }
/*      */ 
/*      */       public void setBounds(Rectangle paramRectangle)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Dimension getSize()
/*      */       {
/* 1869 */         return null;
/*      */       }
/*      */ 
/*      */       public void setSize(Dimension paramDimension)
/*      */       {
/*      */       }
/*      */ 
/*      */       public Accessible getAccessibleAt(Point paramPoint)
/*      */       {
/* 1892 */         return null;
/*      */       }
/*      */ 
/*      */       public boolean isFocusTraversable()
/*      */       {
/* 1908 */         return false;
/*      */       }
/*      */ 
/*      */       public void requestFocus()
/*      */       {
/*      */       }
/*      */ 
/*      */       public void addFocusListener(FocusListener paramFocusListener)
/*      */       {
/*      */       }
/*      */ 
/*      */       public void removeFocusListener(FocusListener paramFocusListener)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.List
 * JD-Core Version:    0.6.2
 */