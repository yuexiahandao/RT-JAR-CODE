/*      */ package javax.swing.tree;
/*      */ 
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.BitSet;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventListener;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.Vector;
/*      */ import javax.swing.DefaultListSelectionModel;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.event.SwingPropertyChangeSupport;
/*      */ import javax.swing.event.TreeSelectionEvent;
/*      */ import javax.swing.event.TreeSelectionListener;
/*      */ 
/*      */ public class DefaultTreeSelectionModel
/*      */   implements Cloneable, Serializable, TreeSelectionModel
/*      */ {
/*      */   public static final String SELECTION_MODE_PROPERTY = "selectionMode";
/*      */   protected SwingPropertyChangeSupport changeSupport;
/*      */   protected TreePath[] selection;
/*   77 */   protected EventListenerList listenerList = new EventListenerList();
/*      */   protected transient RowMapper rowMapper;
/*      */   protected DefaultListSelectionModel listSelectionModel;
/*      */   protected int selectionMode;
/*      */   protected TreePath leadPath;
/*      */   protected int leadIndex;
/*      */   protected int leadRow;
/*      */   private Hashtable<TreePath, Boolean> uniquePaths;
/*      */   private Hashtable<TreePath, Boolean> lastPaths;
/*      */   private TreePath[] tempPaths;
/*      */ 
/*      */   public DefaultTreeSelectionModel()
/*      */   {
/*  111 */     this.listSelectionModel = new DefaultListSelectionModel();
/*  112 */     this.selectionMode = 4;
/*  113 */     this.leadIndex = (this.leadRow = -1);
/*  114 */     this.uniquePaths = new Hashtable();
/*  115 */     this.lastPaths = new Hashtable();
/*  116 */     this.tempPaths = new TreePath[1];
/*      */   }
/*      */ 
/*      */   public void setRowMapper(RowMapper paramRowMapper)
/*      */   {
/*  124 */     this.rowMapper = paramRowMapper;
/*  125 */     resetRowSelection();
/*      */   }
/*      */ 
/*      */   public RowMapper getRowMapper()
/*      */   {
/*  133 */     return this.rowMapper;
/*      */   }
/*      */ 
/*      */   public void setSelectionMode(int paramInt)
/*      */   {
/*  151 */     int i = this.selectionMode;
/*      */ 
/*  153 */     this.selectionMode = paramInt;
/*  154 */     if ((this.selectionMode != 1) && (this.selectionMode != 2) && (this.selectionMode != 4))
/*      */     {
/*  157 */       this.selectionMode = 4;
/*  158 */     }if ((i != this.selectionMode) && (this.changeSupport != null))
/*  159 */       this.changeSupport.firePropertyChange("selectionMode", Integer.valueOf(i), Integer.valueOf(this.selectionMode));
/*      */   }
/*      */ 
/*      */   public int getSelectionMode()
/*      */   {
/*  170 */     return this.selectionMode;
/*      */   }
/*      */ 
/*      */   public void setSelectionPath(TreePath paramTreePath)
/*      */   {
/*  181 */     if (paramTreePath == null) {
/*  182 */       setSelectionPaths(null);
/*      */     } else {
/*  184 */       TreePath[] arrayOfTreePath = new TreePath[1];
/*      */ 
/*  186 */       arrayOfTreePath[0] = paramTreePath;
/*  187 */       setSelectionPaths(arrayOfTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */   {
/*  216 */     TreePath[] arrayOfTreePath = paramArrayOfTreePath;
/*      */     int i;
/*  218 */     if (arrayOfTreePath == null)
/*  219 */       i = 0;
/*      */     else
/*  221 */       i = arrayOfTreePath.length;
/*      */     int k;
/*  222 */     if (this.selection == null)
/*  223 */       k = 0;
/*      */     else
/*  225 */       k = this.selection.length;
/*  226 */     if (i + k != 0) {
/*  227 */       if (this.selectionMode == 1)
/*      */       {
/*  230 */         if (i > 1) {
/*  231 */           arrayOfTreePath = new TreePath[1];
/*  232 */           arrayOfTreePath[0] = paramArrayOfTreePath[0];
/*  233 */           i = 1;
/*      */         }
/*      */       }
/*  236 */       else if (this.selectionMode == 2)
/*      */       {
/*  240 */         if ((i > 0) && (!arePathsContiguous(arrayOfTreePath))) {
/*  241 */           arrayOfTreePath = new TreePath[1];
/*  242 */           arrayOfTreePath[0] = paramArrayOfTreePath[0];
/*  243 */           i = 1;
/*      */         }
/*      */       }
/*      */ 
/*  247 */       TreePath localTreePath = this.leadPath;
/*  248 */       Vector localVector = new Vector(i + k);
/*  249 */       ArrayList localArrayList = new ArrayList(i);
/*      */ 
/*  252 */       this.lastPaths.clear();
/*  253 */       this.leadPath = null;
/*      */ 
/*  255 */       for (int j = 0; j < i; j++) {
/*  256 */         localObject = arrayOfTreePath[j];
/*  257 */         if ((localObject != null) && (this.lastPaths.get(localObject) == null)) {
/*  258 */           this.lastPaths.put(localObject, Boolean.TRUE);
/*  259 */           if (this.uniquePaths.get(localObject) == null) {
/*  260 */             localVector.addElement(new PathPlaceHolder((TreePath)localObject, true));
/*      */           }
/*  262 */           this.leadPath = ((TreePath)localObject);
/*  263 */           localArrayList.add(localObject);
/*      */         }
/*      */       }
/*      */ 
/*  267 */       Object localObject = (TreePath[])localArrayList.toArray(new TreePath[localArrayList.size()]);
/*      */ 
/*  271 */       for (int m = 0; m < k; m++) {
/*  272 */         if ((this.selection[m] != null) && (this.lastPaths.get(this.selection[m]) == null))
/*      */         {
/*  274 */           localVector.addElement(new PathPlaceHolder(this.selection[m], false));
/*      */         }
/*      */       }
/*  277 */       this.selection = ((TreePath[])localObject);
/*      */ 
/*  279 */       Hashtable localHashtable = this.uniquePaths;
/*      */ 
/*  281 */       this.uniquePaths = this.lastPaths;
/*  282 */       this.lastPaths = localHashtable;
/*  283 */       this.lastPaths.clear();
/*      */ 
/*  286 */       insureUniqueness();
/*      */ 
/*  288 */       updateLeadIndex();
/*      */ 
/*  290 */       resetRowSelection();
/*      */ 
/*  292 */       if (localVector.size() > 0)
/*  293 */         notifyPathChange(localVector, localTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addSelectionPath(TreePath paramTreePath)
/*      */   {
/*  305 */     if (paramTreePath != null) {
/*  306 */       TreePath[] arrayOfTreePath = new TreePath[1];
/*      */ 
/*  308 */       arrayOfTreePath[0] = paramTreePath;
/*  309 */       addSelectionPaths(arrayOfTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */   {
/*  329 */     int i = paramArrayOfTreePath == null ? 0 : paramArrayOfTreePath.length;
/*      */ 
/*  331 */     if (i > 0)
/*  332 */       if (this.selectionMode == 1) {
/*  333 */         setSelectionPaths(paramArrayOfTreePath);
/*      */       }
/*  335 */       else if ((this.selectionMode == 2) && (!canPathsBeAdded(paramArrayOfTreePath)))
/*      */       {
/*  337 */         if (arePathsContiguous(paramArrayOfTreePath)) {
/*  338 */           setSelectionPaths(paramArrayOfTreePath);
/*      */         }
/*      */         else {
/*  341 */           TreePath[] arrayOfTreePath1 = new TreePath[1];
/*      */ 
/*  343 */           arrayOfTreePath1[0] = paramArrayOfTreePath[0];
/*  344 */           setSelectionPaths(arrayOfTreePath1);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  350 */         TreePath localTreePath = this.leadPath;
/*  351 */         Vector localVector = null;
/*      */         int m;
/*  353 */         if (this.selection == null)
/*  354 */           m = 0;
/*      */         else {
/*  356 */           m = this.selection.length;
/*      */         }
/*      */ 
/*  359 */         this.lastPaths.clear();
/*  360 */         int j = 0; for (int k = 0; j < i; 
/*  361 */           j++) {
/*  362 */           if (paramArrayOfTreePath[j] != null) {
/*  363 */             if (this.uniquePaths.get(paramArrayOfTreePath[j]) == null) {
/*  364 */               k++;
/*  365 */               if (localVector == null)
/*  366 */                 localVector = new Vector();
/*  367 */               localVector.addElement(new PathPlaceHolder(paramArrayOfTreePath[j], true));
/*      */ 
/*  369 */               this.uniquePaths.put(paramArrayOfTreePath[j], Boolean.TRUE);
/*  370 */               this.lastPaths.put(paramArrayOfTreePath[j], Boolean.TRUE);
/*      */             }
/*  372 */             this.leadPath = paramArrayOfTreePath[j];
/*      */           }
/*      */         }
/*      */ 
/*  376 */         if (this.leadPath == null) {
/*  377 */           this.leadPath = localTreePath;
/*      */         }
/*      */ 
/*  380 */         if (k > 0) {
/*  381 */           TreePath[] arrayOfTreePath2 = new TreePath[m + k];
/*      */ 
/*  385 */           if (m > 0) {
/*  386 */             System.arraycopy(this.selection, 0, arrayOfTreePath2, 0, m);
/*      */           }
/*  388 */           if (k != paramArrayOfTreePath.length)
/*      */           {
/*  391 */             Enumeration localEnumeration = this.lastPaths.keys();
/*      */ 
/*  393 */             j = m;
/*  394 */             while (localEnumeration.hasMoreElements())
/*  395 */               arrayOfTreePath2[(j++)] = ((TreePath)localEnumeration.nextElement());
/*      */           }
/*      */           else
/*      */           {
/*  399 */             System.arraycopy(paramArrayOfTreePath, 0, arrayOfTreePath2, m, k);
/*      */           }
/*      */ 
/*  403 */           this.selection = arrayOfTreePath2;
/*      */ 
/*  405 */           insureUniqueness();
/*      */ 
/*  407 */           updateLeadIndex();
/*      */ 
/*  409 */           resetRowSelection();
/*      */ 
/*  411 */           notifyPathChange(localVector, localTreePath);
/*      */         }
/*      */         else {
/*  414 */           this.leadPath = localTreePath;
/*  415 */         }this.lastPaths.clear();
/*      */       }
/*      */   }
/*      */ 
/*      */   public void removeSelectionPath(TreePath paramTreePath)
/*      */   {
/*  428 */     if (paramTreePath != null) {
/*  429 */       TreePath[] arrayOfTreePath = new TreePath[1];
/*      */ 
/*  431 */       arrayOfTreePath[0] = paramTreePath;
/*  432 */       removeSelectionPaths(arrayOfTreePath);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeSelectionPaths(TreePath[] paramArrayOfTreePath)
/*      */   {
/*  444 */     if ((paramArrayOfTreePath != null) && (this.selection != null) && (paramArrayOfTreePath.length > 0))
/*  445 */       if (!canPathsBeRemoved(paramArrayOfTreePath))
/*      */       {
/*  447 */         clearSelection();
/*      */       }
/*      */       else {
/*  450 */         Vector localVector = null;
/*      */ 
/*  453 */         for (int i = paramArrayOfTreePath.length - 1; i >= 0; 
/*  454 */           i--) {
/*  455 */           if ((paramArrayOfTreePath[i] != null) && 
/*  456 */             (this.uniquePaths.get(paramArrayOfTreePath[i]) != null)) {
/*  457 */             if (localVector == null)
/*  458 */               localVector = new Vector(paramArrayOfTreePath.length);
/*  459 */             this.uniquePaths.remove(paramArrayOfTreePath[i]);
/*  460 */             localVector.addElement(new PathPlaceHolder(paramArrayOfTreePath[i], false));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  465 */         if (localVector != null) {
/*  466 */           i = localVector.size();
/*  467 */           TreePath localTreePath = this.leadPath;
/*      */ 
/*  469 */           if (i == this.selection.length) {
/*  470 */             this.selection = null;
/*      */           }
/*      */           else {
/*  473 */             Enumeration localEnumeration = this.uniquePaths.keys();
/*  474 */             int j = 0;
/*      */ 
/*  476 */             this.selection = new TreePath[this.selection.length - i];
/*      */ 
/*  478 */             while (localEnumeration.hasMoreElements()) {
/*  479 */               this.selection[(j++)] = ((TreePath)localEnumeration.nextElement());
/*      */             }
/*      */           }
/*  482 */           if ((this.leadPath != null) && (this.uniquePaths.get(this.leadPath) == null))
/*      */           {
/*  484 */             if (this.selection != null) {
/*  485 */               this.leadPath = this.selection[(this.selection.length - 1)];
/*      */             }
/*      */             else {
/*  488 */               this.leadPath = null;
/*      */             }
/*      */           }
/*  491 */           else if (this.selection != null) {
/*  492 */             this.leadPath = this.selection[(this.selection.length - 1)];
/*      */           }
/*      */           else {
/*  495 */             this.leadPath = null;
/*      */           }
/*  497 */           updateLeadIndex();
/*      */ 
/*  499 */           resetRowSelection();
/*      */ 
/*  501 */           notifyPathChange(localVector, localTreePath);
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   public TreePath getSelectionPath()
/*      */   {
/*  512 */     if ((this.selection != null) && (this.selection.length > 0)) {
/*  513 */       return this.selection[0];
/*      */     }
/*  515 */     return null;
/*      */   }
/*      */ 
/*      */   public TreePath[] getSelectionPaths()
/*      */   {
/*  524 */     if (this.selection != null) {
/*  525 */       int i = this.selection.length;
/*  526 */       TreePath[] arrayOfTreePath = new TreePath[i];
/*      */ 
/*  528 */       System.arraycopy(this.selection, 0, arrayOfTreePath, 0, i);
/*  529 */       return arrayOfTreePath;
/*      */     }
/*  531 */     return new TreePath[0];
/*      */   }
/*      */ 
/*      */   public int getSelectionCount()
/*      */   {
/*  538 */     return this.selection == null ? 0 : this.selection.length;
/*      */   }
/*      */ 
/*      */   public boolean isPathSelected(TreePath paramTreePath)
/*      */   {
/*  546 */     return this.uniquePaths.get(paramTreePath) != null;
/*      */   }
/*      */ 
/*      */   public boolean isSelectionEmpty()
/*      */   {
/*  553 */     return (this.selection == null) || (this.selection.length == 0);
/*      */   }
/*      */ 
/*      */   public void clearSelection()
/*      */   {
/*  561 */     if ((this.selection != null) && (this.selection.length > 0)) {
/*  562 */       int i = this.selection.length;
/*  563 */       boolean[] arrayOfBoolean = new boolean[i];
/*      */ 
/*  565 */       for (int j = 0; j < i; j++) {
/*  566 */         arrayOfBoolean[j] = false;
/*      */       }
/*  568 */       TreeSelectionEvent localTreeSelectionEvent = new TreeSelectionEvent(this, this.selection, arrayOfBoolean, this.leadPath, null);
/*      */ 
/*  571 */       this.leadPath = null;
/*  572 */       this.leadIndex = (this.leadRow = -1);
/*  573 */       this.uniquePaths.clear();
/*  574 */       this.selection = null;
/*  575 */       resetRowSelection();
/*  576 */       fireValueChanged(localTreeSelectionEvent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addTreeSelectionListener(TreeSelectionListener paramTreeSelectionListener)
/*      */   {
/*  587 */     this.listenerList.add(TreeSelectionListener.class, paramTreeSelectionListener);
/*      */   }
/*      */ 
/*      */   public void removeTreeSelectionListener(TreeSelectionListener paramTreeSelectionListener)
/*      */   {
/*  597 */     this.listenerList.remove(TreeSelectionListener.class, paramTreeSelectionListener);
/*      */   }
/*      */ 
/*      */   public TreeSelectionListener[] getTreeSelectionListeners()
/*      */   {
/*  614 */     return (TreeSelectionListener[])this.listenerList.getListeners(TreeSelectionListener.class);
/*      */   }
/*      */ 
/*      */   protected void fireValueChanged(TreeSelectionEvent paramTreeSelectionEvent)
/*      */   {
/*  625 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/*  629 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/*  630 */       if (arrayOfObject[i] == TreeSelectionListener.class)
/*      */       {
/*  634 */         ((TreeSelectionListener)arrayOfObject[(i + 1)]).valueChanged(paramTreeSelectionEvent);
/*      */       }
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/*  677 */     return this.listenerList.getListeners(paramClass);
/*      */   }
/*      */ 
/*      */   public int[] getSelectionRows()
/*      */   {
/*  697 */     if ((this.rowMapper != null) && (this.selection != null) && (this.selection.length > 0)) {
/*  698 */       Object localObject = this.rowMapper.getRowsForPaths(this.selection);
/*      */ 
/*  700 */       if (localObject != null) {
/*  701 */         int i = 0;
/*      */ 
/*  703 */         for (int j = localObject.length - 1; j >= 0; j--) {
/*  704 */           if (localObject[j] == -1) {
/*  705 */             i++;
/*      */           }
/*      */         }
/*  708 */         if (i > 0) {
/*  709 */           if (i == localObject.length) {
/*  710 */             localObject = null;
/*      */           }
/*      */           else {
/*  713 */             int[] arrayOfInt = new int[localObject.length - i];
/*      */ 
/*  715 */             int k = localObject.length - 1; int m = 0;
/*  716 */             for (; k >= 0; k--) {
/*  717 */               if (localObject[k] != -1) {
/*  718 */                 arrayOfInt[(m++)] = localObject[k];
/*      */               }
/*      */             }
/*  721 */             localObject = arrayOfInt;
/*      */           }
/*      */         }
/*      */       }
/*  725 */       return localObject;
/*      */     }
/*  727 */     return new int[0];
/*      */   }
/*      */ 
/*      */   public int getMinSelectionRow()
/*      */   {
/*  736 */     return this.listSelectionModel.getMinSelectionIndex();
/*      */   }
/*      */ 
/*      */   public int getMaxSelectionRow()
/*      */   {
/*  745 */     return this.listSelectionModel.getMaxSelectionIndex();
/*      */   }
/*      */ 
/*      */   public boolean isRowSelected(int paramInt)
/*      */   {
/*  752 */     return this.listSelectionModel.isSelectedIndex(paramInt);
/*      */   }
/*      */ 
/*      */   public void resetRowSelection()
/*      */   {
/*  767 */     this.listSelectionModel.clearSelection();
/*  768 */     if ((this.selection != null) && (this.rowMapper != null))
/*      */     {
/*  770 */       int j = 0;
/*  771 */       int[] arrayOfInt = this.rowMapper.getRowsForPaths(this.selection);
/*      */ 
/*  773 */       int k = 0; int m = this.selection.length;
/*  774 */       for (; k < m; k++) {
/*  775 */         int i = arrayOfInt[k];
/*  776 */         if (i != -1) {
/*  777 */           this.listSelectionModel.addSelectionInterval(i, i);
/*      */         }
/*      */       }
/*  780 */       if ((this.leadIndex != -1) && (arrayOfInt != null)) {
/*  781 */         this.leadRow = arrayOfInt[this.leadIndex];
/*      */       }
/*  783 */       else if (this.leadPath != null)
/*      */       {
/*  785 */         this.tempPaths[0] = this.leadPath;
/*  786 */         arrayOfInt = this.rowMapper.getRowsForPaths(this.tempPaths);
/*  787 */         this.leadRow = (arrayOfInt != null ? arrayOfInt[0] : -1);
/*      */       }
/*      */       else {
/*  790 */         this.leadRow = -1;
/*      */       }
/*  792 */       insureRowContinuity();
/*      */     }
/*      */     else
/*      */     {
/*  796 */       this.leadRow = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getLeadSelectionRow()
/*      */   {
/*  804 */     return this.leadRow;
/*      */   }
/*      */ 
/*      */   public TreePath getLeadSelectionPath()
/*      */   {
/*  812 */     return this.leadPath;
/*      */   }
/*      */ 
/*      */   public synchronized void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/*  826 */     if (this.changeSupport == null) {
/*  827 */       this.changeSupport = new SwingPropertyChangeSupport(this);
/*      */     }
/*  829 */     this.changeSupport.addPropertyChangeListener(paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public synchronized void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/*  842 */     if (this.changeSupport == null) {
/*  843 */       return;
/*      */     }
/*  845 */     this.changeSupport.removePropertyChangeListener(paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public PropertyChangeListener[] getPropertyChangeListeners()
/*      */   {
/*  862 */     if (this.changeSupport == null) {
/*  863 */       return new PropertyChangeListener[0];
/*      */     }
/*  865 */     return this.changeSupport.getPropertyChangeListeners();
/*      */   }
/*      */ 
/*      */   protected void insureRowContinuity()
/*      */   {
/*  883 */     if ((this.selectionMode == 2) && (this.selection != null) && (this.rowMapper != null))
/*      */     {
/*  885 */       DefaultListSelectionModel localDefaultListSelectionModel = this.listSelectionModel;
/*  886 */       int i = localDefaultListSelectionModel.getMinSelectionIndex();
/*      */ 
/*  888 */       if (i != -1) {
/*  889 */         int j = i;
/*  890 */         int k = localDefaultListSelectionModel.getMaxSelectionIndex();
/*  891 */         for (; j <= k; j++) {
/*  892 */           if (!localDefaultListSelectionModel.isSelectedIndex(j)) {
/*  893 */             if (j == i) {
/*  894 */               clearSelection();
/*      */             }
/*      */             else {
/*  897 */               TreePath[] arrayOfTreePath = new TreePath[j - i];
/*  898 */               int[] arrayOfInt = this.rowMapper.getRowsForPaths(this.selection);
/*      */ 
/*  901 */               for (int m = 0; m < arrayOfInt.length; m++) {
/*  902 */                 if (arrayOfInt[m] < j) {
/*  903 */                   arrayOfTreePath[(arrayOfInt[m] - i)] = this.selection[m];
/*      */                 }
/*      */               }
/*  906 */               setSelectionPaths(arrayOfTreePath);
/*  907 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  913 */     else if ((this.selectionMode == 1) && (this.selection != null) && (this.selection.length > 1))
/*      */     {
/*  915 */       setSelectionPath(this.selection[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean arePathsContiguous(TreePath[] paramArrayOfTreePath)
/*      */   {
/*  924 */     if ((this.rowMapper == null) || (paramArrayOfTreePath.length < 2)) {
/*  925 */       return true;
/*      */     }
/*  927 */     BitSet localBitSet = new BitSet(32);
/*      */ 
/*  929 */     int m = paramArrayOfTreePath.length;
/*  930 */     int n = 0;
/*  931 */     TreePath[] arrayOfTreePath = new TreePath[1];
/*      */ 
/*  933 */     arrayOfTreePath[0] = paramArrayOfTreePath[0];
/*  934 */     int k = this.rowMapper.getRowsForPaths(arrayOfTreePath)[0];
/*  935 */     for (int j = 0; j < m; j++) {
/*  936 */       if (paramArrayOfTreePath[j] != null) {
/*  937 */         arrayOfTreePath[0] = paramArrayOfTreePath[j];
/*  938 */         int[] arrayOfInt = this.rowMapper.getRowsForPaths(arrayOfTreePath);
/*  939 */         if (arrayOfInt == null) {
/*  940 */           return false;
/*      */         }
/*  942 */         int i = arrayOfInt[0];
/*  943 */         if ((i == -1) || (i < k - m) || (i > k + m))
/*      */         {
/*  945 */           return false;
/*  946 */         }if (i < k)
/*  947 */           k = i;
/*  948 */         if (!localBitSet.get(i)) {
/*  949 */           localBitSet.set(i);
/*  950 */           n++;
/*      */         }
/*      */       }
/*      */     }
/*  954 */     int i1 = n + k;
/*      */ 
/*  956 */     for (j = k; j < i1; j++) {
/*  957 */       if (!localBitSet.get(j))
/*  958 */         return false;
/*      */     }
/*  960 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean canPathsBeAdded(TreePath[] paramArrayOfTreePath)
/*      */   {
/*  972 */     if ((paramArrayOfTreePath == null) || (paramArrayOfTreePath.length == 0) || (this.rowMapper == null) || (this.selection == null) || (this.selectionMode == 4))
/*      */     {
/*  975 */       return true;
/*      */     }
/*  977 */     BitSet localBitSet = new BitSet();
/*  978 */     DefaultListSelectionModel localDefaultListSelectionModel = this.listSelectionModel;
/*      */ 
/*  981 */     int k = localDefaultListSelectionModel.getMinSelectionIndex();
/*  982 */     int m = localDefaultListSelectionModel.getMaxSelectionIndex();
/*  983 */     TreePath[] arrayOfTreePath = new TreePath[1];
/*      */ 
/*  985 */     if (k != -1) {
/*  986 */       for (j = k; j <= m; j++) {
/*  987 */         if (localDefaultListSelectionModel.isSelectedIndex(j)) {
/*  988 */           localBitSet.set(j);
/*      */         }
/*      */       }
/*      */     }
/*  992 */     arrayOfTreePath[0] = paramArrayOfTreePath[0];
/*  993 */     k = m = this.rowMapper.getRowsForPaths(arrayOfTreePath)[0];
/*      */ 
/*  995 */     for (int j = paramArrayOfTreePath.length - 1; j >= 0; j--) {
/*  996 */       if (paramArrayOfTreePath[j] != null) {
/*  997 */         arrayOfTreePath[0] = paramArrayOfTreePath[j];
/*  998 */         int[] arrayOfInt = this.rowMapper.getRowsForPaths(arrayOfTreePath);
/*  999 */         if (arrayOfInt == null) {
/* 1000 */           return false;
/*      */         }
/* 1002 */         int i = arrayOfInt[0];
/* 1003 */         k = Math.min(i, k);
/* 1004 */         m = Math.max(i, m);
/* 1005 */         if (i == -1)
/* 1006 */           return false;
/* 1007 */         localBitSet.set(i);
/*      */       }
/*      */     }
/* 1010 */     for (j = k; j <= m; j++) {
/* 1011 */       if (!localBitSet.get(j))
/* 1012 */         return false;
/*      */     }
/* 1014 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean canPathsBeRemoved(TreePath[] paramArrayOfTreePath)
/*      */   {
/* 1023 */     if ((this.rowMapper == null) || (this.selection == null) || (this.selectionMode == 4))
/*      */     {
/* 1025 */       return true;
/*      */     }
/* 1027 */     BitSet localBitSet = new BitSet();
/*      */ 
/* 1029 */     int j = paramArrayOfTreePath.length;
/*      */ 
/* 1031 */     int k = -1;
/* 1032 */     int m = 0;
/* 1033 */     TreePath[] arrayOfTreePath = new TreePath[1];
/*      */ 
/* 1037 */     this.lastPaths.clear();
/* 1038 */     for (int i = 0; i < j; i++) {
/* 1039 */       if (paramArrayOfTreePath[i] != null) {
/* 1040 */         this.lastPaths.put(paramArrayOfTreePath[i], Boolean.TRUE);
/*      */       }
/*      */     }
/* 1043 */     for (i = this.selection.length - 1; i >= 0; i--) {
/* 1044 */       if (this.lastPaths.get(this.selection[i]) == null) {
/* 1045 */         arrayOfTreePath[0] = this.selection[i];
/* 1046 */         int[] arrayOfInt = this.rowMapper.getRowsForPaths(arrayOfTreePath);
/* 1047 */         if ((arrayOfInt != null) && (arrayOfInt[0] != -1) && (!localBitSet.get(arrayOfInt[0]))) {
/* 1048 */           m++;
/* 1049 */           if (k == -1)
/* 1050 */             k = arrayOfInt[0];
/*      */           else
/* 1052 */             k = Math.min(k, arrayOfInt[0]);
/* 1053 */           localBitSet.set(arrayOfInt[0]);
/*      */         }
/*      */       }
/*      */     }
/* 1057 */     this.lastPaths.clear();
/*      */ 
/* 1059 */     if (m > 1) {
/* 1060 */       for (i = k + m - 1; i >= k; 
/* 1061 */         i--) {
/* 1062 */         if (!localBitSet.get(i))
/* 1063 */           return false;
/*      */       }
/*      */     }
/* 1066 */     return true;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected void notifyPathChange(Vector paramVector, TreePath paramTreePath)
/*      */   {
/* 1078 */     int i = paramVector.size();
/* 1079 */     boolean[] arrayOfBoolean = new boolean[i];
/* 1080 */     TreePath[] arrayOfTreePath = new TreePath[i];
/*      */ 
/* 1083 */     for (int j = 0; j < i; j++) {
/* 1084 */       PathPlaceHolder localPathPlaceHolder = (PathPlaceHolder)paramVector.elementAt(j);
/* 1085 */       arrayOfBoolean[j] = localPathPlaceHolder.isNew;
/* 1086 */       arrayOfTreePath[j] = localPathPlaceHolder.path;
/*      */     }
/*      */ 
/* 1089 */     TreeSelectionEvent localTreeSelectionEvent = new TreeSelectionEvent(this, arrayOfTreePath, arrayOfBoolean, paramTreePath, this.leadPath);
/*      */ 
/* 1092 */     fireValueChanged(localTreeSelectionEvent);
/*      */   }
/*      */ 
/*      */   protected void updateLeadIndex()
/*      */   {
/* 1099 */     if (this.leadPath != null) {
/* 1100 */       if (this.selection == null) {
/* 1101 */         this.leadPath = null;
/* 1102 */         this.leadIndex = (this.leadRow = -1);
/*      */       }
/*      */       else {
/* 1105 */         this.leadRow = (this.leadIndex = -1);
/* 1106 */         for (int i = this.selection.length - 1; i >= 0; 
/* 1107 */           i--)
/*      */         {
/* 1110 */           if (this.selection[i] == this.leadPath) {
/* 1111 */             this.leadIndex = i;
/* 1112 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/* 1118 */       this.leadIndex = -1;
/*      */   }
/*      */ 
/*      */   protected void insureUniqueness()
/*      */   {
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1138 */     int i = getSelectionCount();
/* 1139 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */     int[] arrayOfInt;
/* 1142 */     if (this.rowMapper != null)
/* 1143 */       arrayOfInt = this.rowMapper.getRowsForPaths(this.selection);
/*      */     else
/* 1145 */       arrayOfInt = null;
/* 1146 */     localStringBuffer.append(getClass().getName() + " " + hashCode() + " [ ");
/* 1147 */     for (int j = 0; j < i; j++) {
/* 1148 */       if (arrayOfInt != null) {
/* 1149 */         localStringBuffer.append(this.selection[j].toString() + "@" + Integer.toString(arrayOfInt[j]) + " ");
/*      */       }
/*      */       else
/* 1152 */         localStringBuffer.append(this.selection[j].toString() + " ");
/*      */     }
/* 1154 */     localStringBuffer.append("]");
/* 1155 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/* 1167 */     DefaultTreeSelectionModel localDefaultTreeSelectionModel = (DefaultTreeSelectionModel)super.clone();
/*      */ 
/* 1170 */     localDefaultTreeSelectionModel.changeSupport = null;
/* 1171 */     if (this.selection != null) {
/* 1172 */       int i = this.selection.length;
/*      */ 
/* 1174 */       localDefaultTreeSelectionModel.selection = new TreePath[i];
/* 1175 */       System.arraycopy(this.selection, 0, localDefaultTreeSelectionModel.selection, 0, i);
/*      */     }
/* 1177 */     localDefaultTreeSelectionModel.listenerList = new EventListenerList();
/* 1178 */     localDefaultTreeSelectionModel.listSelectionModel = ((DefaultListSelectionModel)this.listSelectionModel.clone());
/*      */ 
/* 1180 */     localDefaultTreeSelectionModel.uniquePaths = new Hashtable();
/* 1181 */     localDefaultTreeSelectionModel.lastPaths = new Hashtable();
/* 1182 */     localDefaultTreeSelectionModel.tempPaths = new TreePath[1];
/* 1183 */     return localDefaultTreeSelectionModel;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1190 */     paramObjectOutputStream.defaultWriteObject();
/*      */     Object[] arrayOfObject;
/* 1192 */     if ((this.rowMapper != null) && ((this.rowMapper instanceof Serializable))) {
/* 1193 */       arrayOfObject = new Object[2];
/* 1194 */       arrayOfObject[0] = "rowMapper";
/* 1195 */       arrayOfObject[1] = this.rowMapper;
/*      */     }
/*      */     else {
/* 1198 */       arrayOfObject = new Object[0];
/* 1199 */     }paramObjectOutputStream.writeObject(arrayOfObject);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1207 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1209 */     Object[] arrayOfObject = (Object[])paramObjectInputStream.readObject();
/*      */ 
/* 1211 */     if ((arrayOfObject.length > 0) && (arrayOfObject[0].equals("rowMapper")))
/* 1212 */       this.rowMapper = ((RowMapper)arrayOfObject[1]);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.DefaultTreeSelectionModel
 * JD-Core Version:    0.6.2
 */