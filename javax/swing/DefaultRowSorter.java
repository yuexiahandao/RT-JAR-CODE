/*      */ package javax.swing;
/*      */ 
/*      */ import java.text.Collator;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ 
/*      */ public abstract class DefaultRowSorter<M, I> extends RowSorter<M>
/*      */ {
/*      */   private boolean sortsOnUpdates;
/*      */   private Row[] viewToModel;
/*      */   private int[] modelToView;
/*      */   private Comparator[] comparators;
/*      */   private boolean[] isSortable;
/*      */   private RowSorter.SortKey[] cachedSortKeys;
/*      */   private Comparator[] sortComparators;
/*      */   private RowFilter<? super M, ? super I> filter;
/*      */   private DefaultRowSorter<M, I>.FilterEntry filterEntry;
/*      */   private List<RowSorter.SortKey> sortKeys;
/*      */   private boolean[] useToString;
/*      */   private boolean sorted;
/*      */   private int maxSortKeys;
/*      */   private ModelWrapper<M, I> modelWrapper;
/*      */   private int modelRowCount;
/*      */ 
/*      */   public DefaultRowSorter()
/*      */   {
/*  196 */     this.sortKeys = Collections.emptyList();
/*  197 */     this.maxSortKeys = 3;
/*      */   }
/*      */ 
/*      */   protected final void setModelWrapper(ModelWrapper<M, I> paramModelWrapper)
/*      */   {
/*  210 */     if (paramModelWrapper == null) {
/*  211 */       throw new IllegalArgumentException("modelWrapper most be non-null");
/*      */     }
/*      */ 
/*  214 */     ModelWrapper localModelWrapper = this.modelWrapper;
/*  215 */     this.modelWrapper = paramModelWrapper;
/*  216 */     if (localModelWrapper != null) {
/*  217 */       modelStructureChanged();
/*      */     }
/*      */     else
/*      */     {
/*  221 */       this.modelRowCount = getModelWrapper().getRowCount();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final ModelWrapper<M, I> getModelWrapper()
/*      */   {
/*  233 */     return this.modelWrapper;
/*      */   }
/*      */ 
/*      */   public final M getModel()
/*      */   {
/*  242 */     return getModelWrapper().getModel();
/*      */   }
/*      */ 
/*      */   public void setSortable(int paramInt, boolean paramBoolean)
/*      */   {
/*  261 */     checkColumn(paramInt);
/*  262 */     if (this.isSortable == null) {
/*  263 */       this.isSortable = new boolean[getModelWrapper().getColumnCount()];
/*  264 */       for (int i = this.isSortable.length - 1; i >= 0; i--) {
/*  265 */         this.isSortable[i] = true;
/*      */       }
/*      */     }
/*  268 */     this.isSortable[paramInt] = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean isSortable(int paramInt)
/*      */   {
/*  281 */     checkColumn(paramInt);
/*  282 */     return this.isSortable == null ? 1 : this.isSortable[paramInt];
/*      */   }
/*      */ 
/*      */   public void setSortKeys(List<? extends RowSorter.SortKey> paramList)
/*      */   {
/*  299 */     List localList = this.sortKeys;
/*  300 */     if ((paramList != null) && (paramList.size() > 0)) {
/*  301 */       int i = getModelWrapper().getColumnCount();
/*  302 */       for (RowSorter.SortKey localSortKey : paramList) {
/*  303 */         if ((localSortKey == null) || (localSortKey.getColumn() < 0) || (localSortKey.getColumn() >= i))
/*      */         {
/*  305 */           throw new IllegalArgumentException("Invalid SortKey");
/*      */         }
/*      */       }
/*  308 */       this.sortKeys = Collections.unmodifiableList(new ArrayList(paramList));
/*      */     }
/*      */     else
/*      */     {
/*  312 */       this.sortKeys = Collections.emptyList();
/*      */     }
/*  314 */     if (!this.sortKeys.equals(localList)) {
/*  315 */       fireSortOrderChanged();
/*  316 */       if (this.viewToModel == null)
/*      */       {
/*  319 */         sort();
/*      */       }
/*  321 */       else sortExistingData();
/*      */     }
/*      */   }
/*      */ 
/*      */   public List<? extends RowSorter.SortKey> getSortKeys()
/*      */   {
/*  335 */     return this.sortKeys;
/*      */   }
/*      */ 
/*      */   public void setMaxSortKeys(int paramInt)
/*      */   {
/*  367 */     if (paramInt < 1) {
/*  368 */       throw new IllegalArgumentException("Invalid max");
/*      */     }
/*  370 */     this.maxSortKeys = paramInt;
/*      */   }
/*      */ 
/*      */   public int getMaxSortKeys()
/*      */   {
/*  379 */     return this.maxSortKeys;
/*      */   }
/*      */ 
/*      */   public void setSortsOnUpdates(boolean paramBoolean)
/*      */   {
/*  392 */     this.sortsOnUpdates = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getSortsOnUpdates()
/*      */   {
/*  402 */     return this.sortsOnUpdates;
/*      */   }
/*      */ 
/*      */   public void setRowFilter(RowFilter<? super M, ? super I> paramRowFilter)
/*      */   {
/*  423 */     this.filter = paramRowFilter;
/*  424 */     sort();
/*      */   }
/*      */ 
/*      */   public RowFilter<? super M, ? super I> getRowFilter()
/*      */   {
/*  434 */     return this.filter;
/*      */   }
/*      */ 
/*      */   public void toggleSortOrder(int paramInt)
/*      */   {
/*  452 */     checkColumn(paramInt);
/*  453 */     if (isSortable(paramInt)) {
/*  454 */       Object localObject = new ArrayList(getSortKeys());
/*      */ 
/*  457 */       for (int i = ((List)localObject).size() - 1; (i >= 0) && 
/*  458 */         (((RowSorter.SortKey)((List)localObject).get(i)).getColumn() != paramInt); i--);
/*  462 */       if (i == -1)
/*      */       {
/*  464 */         RowSorter.SortKey localSortKey = new RowSorter.SortKey(paramInt, SortOrder.ASCENDING);
/*  465 */         ((List)localObject).add(0, localSortKey);
/*      */       }
/*  467 */       else if (i == 0)
/*      */       {
/*  469 */         ((List)localObject).set(0, toggle((RowSorter.SortKey)((List)localObject).get(0)));
/*      */       }
/*      */       else
/*      */       {
/*  474 */         ((List)localObject).remove(i);
/*  475 */         ((List)localObject).add(0, new RowSorter.SortKey(paramInt, SortOrder.ASCENDING));
/*      */       }
/*  477 */       if (((List)localObject).size() > getMaxSortKeys()) {
/*  478 */         localObject = ((List)localObject).subList(0, getMaxSortKeys());
/*      */       }
/*  480 */       setSortKeys((List)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private RowSorter.SortKey toggle(RowSorter.SortKey paramSortKey) {
/*  485 */     if (paramSortKey.getSortOrder() == SortOrder.ASCENDING) {
/*  486 */       return new RowSorter.SortKey(paramSortKey.getColumn(), SortOrder.DESCENDING);
/*      */     }
/*  488 */     return new RowSorter.SortKey(paramSortKey.getColumn(), SortOrder.ASCENDING);
/*      */   }
/*      */ 
/*      */   public int convertRowIndexToView(int paramInt)
/*      */   {
/*  497 */     if (this.modelToView == null) {
/*  498 */       if ((paramInt < 0) || (paramInt >= getModelWrapper().getRowCount())) {
/*  499 */         throw new IndexOutOfBoundsException("Invalid index");
/*      */       }
/*  501 */       return paramInt;
/*      */     }
/*  503 */     return this.modelToView[paramInt];
/*      */   }
/*      */ 
/*      */   public int convertRowIndexToModel(int paramInt)
/*      */   {
/*  512 */     if (this.viewToModel == null) {
/*  513 */       if ((paramInt < 0) || (paramInt >= getModelWrapper().getRowCount())) {
/*  514 */         throw new IndexOutOfBoundsException("Invalid index");
/*      */       }
/*  516 */       return paramInt;
/*      */     }
/*  518 */     return this.viewToModel[paramInt].modelIndex;
/*      */   }
/*      */ 
/*      */   private boolean isUnsorted() {
/*  522 */     List localList = getSortKeys();
/*  523 */     int i = localList.size();
/*  524 */     return (i == 0) || (((RowSorter.SortKey)localList.get(0)).getSortOrder() == SortOrder.UNSORTED);
/*      */   }
/*      */ 
/*      */   private void sortExistingData()
/*      */   {
/*  533 */     int[] arrayOfInt = getViewToModelAsInts(this.viewToModel);
/*      */ 
/*  535 */     updateUseToString();
/*  536 */     cacheSortKeys(getSortKeys());
/*      */ 
/*  538 */     if (isUnsorted()) {
/*  539 */       if (getRowFilter() == null) {
/*  540 */         this.viewToModel = null;
/*  541 */         this.modelToView = null;
/*      */       } else {
/*  543 */         int i = 0;
/*  544 */         for (int j = 0; j < this.modelToView.length; j++)
/*  545 */           if (this.modelToView[j] != -1) {
/*  546 */             this.viewToModel[i].modelIndex = j;
/*  547 */             this.modelToView[j] = (i++);
/*      */           }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  553 */       Arrays.sort(this.viewToModel);
/*      */ 
/*  556 */       setModelToViewFromViewToModel(false);
/*      */     }
/*  558 */     fireRowSorterChanged(arrayOfInt);
/*      */   }
/*      */ 
/*      */   public void sort()
/*      */   {
/*  571 */     this.sorted = true;
/*  572 */     int[] arrayOfInt = getViewToModelAsInts(this.viewToModel);
/*  573 */     updateUseToString();
/*  574 */     if (isUnsorted())
/*      */     {
/*  576 */       this.cachedSortKeys = new RowSorter.SortKey[0];
/*  577 */       if (getRowFilter() == null)
/*      */       {
/*  579 */         if (this.viewToModel != null)
/*      */         {
/*  581 */           this.viewToModel = null;
/*  582 */           this.modelToView = null;
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  592 */         initializeFilteredMapping();
/*      */       }
/*      */     }
/*      */     else {
/*  596 */       cacheSortKeys(getSortKeys());
/*      */ 
/*  598 */       if (getRowFilter() != null) {
/*  599 */         initializeFilteredMapping();
/*      */       }
/*      */       else {
/*  602 */         createModelToView(getModelWrapper().getRowCount());
/*  603 */         createViewToModel(getModelWrapper().getRowCount());
/*      */       }
/*      */ 
/*  607 */       Arrays.sort(this.viewToModel);
/*      */ 
/*  610 */       setModelToViewFromViewToModel(false);
/*      */     }
/*  612 */     fireRowSorterChanged(arrayOfInt);
/*      */   }
/*      */ 
/*      */   private void updateUseToString()
/*      */   {
/*  619 */     int i = getModelWrapper().getColumnCount();
/*  620 */     if ((this.useToString == null) || (this.useToString.length != i)) {
/*  621 */       this.useToString = new boolean[i];
/*      */     }
/*  623 */     for (i--; i >= 0; i--)
/*  624 */       this.useToString[i] = useToString(i);
/*      */   }
/*      */ 
/*      */   private void initializeFilteredMapping()
/*      */   {
/*  633 */     int i = getModelWrapper().getRowCount();
/*      */ 
/*  635 */     int m = 0;
/*      */ 
/*  638 */     createModelToView(i);
/*  639 */     for (int j = 0; j < i; j++) {
/*  640 */       if (include(j)) {
/*  641 */         this.modelToView[j] = (j - m);
/*      */       }
/*      */       else {
/*  644 */         this.modelToView[j] = -1;
/*  645 */         m++;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  650 */     createViewToModel(i - m);
/*  651 */     j = 0; for (int k = 0; j < i; j++)
/*  652 */       if (this.modelToView[j] != -1)
/*  653 */         this.viewToModel[(k++)].modelIndex = j;
/*      */   }
/*      */ 
/*      */   private void createModelToView(int paramInt)
/*      */   {
/*  662 */     if ((this.modelToView == null) || (this.modelToView.length != paramInt))
/*  663 */       this.modelToView = new int[paramInt];
/*      */   }
/*      */ 
/*      */   private void createViewToModel(int paramInt)
/*      */   {
/*  671 */     int i = 0;
/*  672 */     if (this.viewToModel != null) {
/*  673 */       i = Math.min(paramInt, this.viewToModel.length);
/*  674 */       if (this.viewToModel.length != paramInt) {
/*  675 */         Row[] arrayOfRow = this.viewToModel;
/*  676 */         this.viewToModel = new Row[paramInt];
/*  677 */         System.arraycopy(arrayOfRow, 0, this.viewToModel, 0, i);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  682 */       this.viewToModel = new Row[paramInt];
/*      */     }
/*      */ 
/*  685 */     for (int j = 0; j < i; j++) {
/*  686 */       this.viewToModel[j].modelIndex = j;
/*      */     }
/*  688 */     for (j = i; j < paramInt; j++)
/*  689 */       this.viewToModel[j] = new Row(this, j);
/*      */   }
/*      */ 
/*      */   private void cacheSortKeys(List<? extends RowSorter.SortKey> paramList)
/*      */   {
/*  697 */     int i = paramList.size();
/*  698 */     this.sortComparators = new Comparator[i];
/*  699 */     for (int j = 0; j < i; j++) {
/*  700 */       this.sortComparators[j] = getComparator0(((RowSorter.SortKey)paramList.get(j)).getColumn());
/*      */     }
/*  702 */     this.cachedSortKeys = ((RowSorter.SortKey[])paramList.toArray(new RowSorter.SortKey[i]));
/*      */   }
/*      */ 
/*      */   protected boolean useToString(int paramInt)
/*      */   {
/*  718 */     return getComparator(paramInt) == null;
/*      */   }
/*      */ 
/*      */   private void setModelToViewFromViewToModel(boolean paramBoolean)
/*      */   {
/*  728 */     if (paramBoolean) {
/*  729 */       for (i = this.modelToView.length - 1; i >= 0; i--) {
/*  730 */         this.modelToView[i] = -1;
/*      */       }
/*      */     }
/*  733 */     for (int i = this.viewToModel.length - 1; i >= 0; i--)
/*  734 */       this.modelToView[this.viewToModel[i].modelIndex] = i;
/*      */   }
/*      */ 
/*      */   private int[] getViewToModelAsInts(Row[] paramArrayOfRow)
/*      */   {
/*  739 */     if (paramArrayOfRow != null) {
/*  740 */       int[] arrayOfInt = new int[paramArrayOfRow.length];
/*  741 */       for (int i = paramArrayOfRow.length - 1; i >= 0; i--) {
/*  742 */         arrayOfInt[i] = paramArrayOfRow[i].modelIndex;
/*      */       }
/*  744 */       return arrayOfInt;
/*      */     }
/*  746 */     return new int[0];
/*      */   }
/*      */ 
/*      */   public void setComparator(int paramInt, Comparator<?> paramComparator)
/*      */   {
/*  761 */     checkColumn(paramInt);
/*  762 */     if (this.comparators == null) {
/*  763 */       this.comparators = new Comparator[getModelWrapper().getColumnCount()];
/*      */     }
/*  765 */     this.comparators[paramInt] = paramComparator;
/*      */   }
/*      */ 
/*      */   public Comparator<?> getComparator(int paramInt)
/*      */   {
/*  780 */     checkColumn(paramInt);
/*  781 */     if (this.comparators != null) {
/*  782 */       return this.comparators[paramInt];
/*      */     }
/*  784 */     return null;
/*      */   }
/*      */ 
/*      */   private Comparator getComparator0(int paramInt)
/*      */   {
/*  790 */     Comparator localComparator = getComparator(paramInt);
/*  791 */     if (localComparator != null) {
/*  792 */       return localComparator;
/*      */     }
/*      */ 
/*  796 */     return Collator.getInstance();
/*      */   }
/*      */ 
/*      */   private RowFilter.Entry<M, I> getFilterEntry(int paramInt) {
/*  800 */     if (this.filterEntry == null) {
/*  801 */       this.filterEntry = new FilterEntry(null);
/*      */     }
/*  803 */     this.filterEntry.modelIndex = paramInt;
/*  804 */     return this.filterEntry;
/*      */   }
/*      */ 
/*      */   public int getViewRowCount()
/*      */   {
/*  811 */     if (this.viewToModel != null)
/*      */     {
/*  813 */       return this.viewToModel.length;
/*      */     }
/*  815 */     return getModelWrapper().getRowCount();
/*      */   }
/*      */ 
/*      */   public int getModelRowCount()
/*      */   {
/*  822 */     return getModelWrapper().getRowCount();
/*      */   }
/*      */ 
/*      */   private void allChanged() {
/*  826 */     this.modelToView = null;
/*  827 */     this.viewToModel = null;
/*  828 */     this.comparators = null;
/*  829 */     this.isSortable = null;
/*  830 */     if (isUnsorted())
/*      */     {
/*  833 */       sort();
/*      */     }
/*  835 */     else setSortKeys(null);
/*      */   }
/*      */ 
/*      */   public void modelStructureChanged()
/*      */   {
/*  843 */     allChanged();
/*  844 */     this.modelRowCount = getModelWrapper().getRowCount();
/*      */   }
/*      */ 
/*      */   public void allRowsChanged()
/*      */   {
/*  851 */     this.modelRowCount = getModelWrapper().getRowCount();
/*  852 */     sort();
/*      */   }
/*      */ 
/*      */   public void rowsInserted(int paramInt1, int paramInt2)
/*      */   {
/*  861 */     checkAgainstModel(paramInt1, paramInt2);
/*  862 */     int i = getModelWrapper().getRowCount();
/*  863 */     if (paramInt2 >= i) {
/*  864 */       throw new IndexOutOfBoundsException("Invalid range");
/*      */     }
/*  866 */     this.modelRowCount = i;
/*  867 */     if (shouldOptimizeChange(paramInt1, paramInt2))
/*  868 */       rowsInserted0(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void rowsDeleted(int paramInt1, int paramInt2)
/*      */   {
/*  878 */     checkAgainstModel(paramInt1, paramInt2);
/*  879 */     if ((paramInt1 >= this.modelRowCount) || (paramInt2 >= this.modelRowCount)) {
/*  880 */       throw new IndexOutOfBoundsException("Invalid range");
/*      */     }
/*  882 */     this.modelRowCount = getModelWrapper().getRowCount();
/*  883 */     if (shouldOptimizeChange(paramInt1, paramInt2))
/*  884 */       rowsDeleted0(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void rowsUpdated(int paramInt1, int paramInt2)
/*      */   {
/*  894 */     checkAgainstModel(paramInt1, paramInt2);
/*  895 */     if ((paramInt1 >= this.modelRowCount) || (paramInt2 >= this.modelRowCount)) {
/*  896 */       throw new IndexOutOfBoundsException("Invalid range");
/*      */     }
/*  898 */     if (getSortsOnUpdates()) {
/*  899 */       if (shouldOptimizeChange(paramInt1, paramInt2)) {
/*  900 */         rowsUpdated0(paramInt1, paramInt2);
/*      */       }
/*      */     }
/*      */     else
/*  904 */       this.sorted = false;
/*      */   }
/*      */ 
/*      */   public void rowsUpdated(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  914 */     checkColumn(paramInt3);
/*  915 */     rowsUpdated(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private void checkAgainstModel(int paramInt1, int paramInt2) {
/*  919 */     if ((paramInt1 > paramInt2) || (paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > this.modelRowCount))
/*      */     {
/*  921 */       throw new IndexOutOfBoundsException("Invalid range");
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean include(int paramInt)
/*      */   {
/*  929 */     RowFilter localRowFilter = getRowFilter();
/*  930 */     if (localRowFilter != null) {
/*  931 */       return localRowFilter.include(getFilterEntry(paramInt));
/*      */     }
/*      */ 
/*  934 */     return true;
/*      */   }
/*      */ 
/*      */   private int compare(int paramInt1, int paramInt2)
/*      */   {
/*  944 */     for (int k = 0; k < this.cachedSortKeys.length; k++) {
/*  945 */       int i = this.cachedSortKeys[k].getColumn();
/*  946 */       SortOrder localSortOrder = this.cachedSortKeys[k].getSortOrder();
/*      */       int j;
/*  947 */       if (localSortOrder == SortOrder.UNSORTED) {
/*  948 */         j = paramInt1 - paramInt2;
/*      */       }
/*      */       else
/*      */       {
/*      */         Object localObject1;
/*      */         Object localObject2;
/*  951 */         if (this.useToString[i] != 0) {
/*  952 */           localObject1 = getModelWrapper().getStringValueAt(paramInt1, i);
/*  953 */           localObject2 = getModelWrapper().getStringValueAt(paramInt2, i);
/*      */         } else {
/*  955 */           localObject1 = getModelWrapper().getValueAt(paramInt1, i);
/*  956 */           localObject2 = getModelWrapper().getValueAt(paramInt2, i);
/*      */         }
/*      */ 
/*  959 */         if (localObject1 == null) {
/*  960 */           if (localObject2 == null)
/*  961 */             j = 0;
/*      */           else
/*  963 */             j = -1;
/*      */         }
/*  965 */         else if (localObject2 == null)
/*  966 */           j = 1;
/*      */         else {
/*  968 */           j = this.sortComparators[k].compare(localObject1, localObject2);
/*      */         }
/*  970 */         if (localSortOrder == SortOrder.DESCENDING) {
/*  971 */           j *= -1;
/*      */         }
/*      */       }
/*  974 */       if (j != 0) {
/*  975 */         return j;
/*      */       }
/*      */     }
/*      */ 
/*  979 */     return paramInt1 - paramInt2;
/*      */   }
/*      */ 
/*      */   private boolean isTransformed()
/*      */   {
/*  986 */     return this.viewToModel != null;
/*      */   }
/*      */ 
/*      */   private void insertInOrder(List<Row> paramList, Row[] paramArrayOfRow)
/*      */   {
/*  996 */     int i = 0;
/*      */ 
/*  998 */     int k = paramList.size();
/*  999 */     for (int m = 0; m < k; m++) {
/* 1000 */       int j = Arrays.binarySearch(paramArrayOfRow, paramList.get(m));
/* 1001 */       if (j < 0) {
/* 1002 */         j = -1 - j;
/*      */       }
/* 1004 */       System.arraycopy(paramArrayOfRow, i, this.viewToModel, i + m, j - i);
/*      */ 
/* 1006 */       this.viewToModel[(j + m)] = ((Row)paramList.get(m));
/* 1007 */       i = j;
/*      */     }
/* 1009 */     System.arraycopy(paramArrayOfRow, i, this.viewToModel, i + k, paramArrayOfRow.length - i);
/*      */   }
/*      */ 
/*      */   private boolean shouldOptimizeChange(int paramInt1, int paramInt2)
/*      */   {
/* 1019 */     if (!isTransformed())
/*      */     {
/* 1021 */       return false;
/*      */     }
/* 1023 */     if ((!this.sorted) || (paramInt2 - paramInt1 > this.viewToModel.length / 10))
/*      */     {
/* 1025 */       sort();
/* 1026 */       return false;
/*      */     }
/* 1028 */     return true;
/*      */   }
/*      */ 
/*      */   private void rowsInserted0(int paramInt1, int paramInt2) {
/* 1032 */     int[] arrayOfInt = getViewToModelAsInts(this.viewToModel);
/*      */ 
/* 1034 */     int j = paramInt2 - paramInt1 + 1;
/* 1035 */     ArrayList localArrayList = new ArrayList(j);
/*      */ 
/* 1038 */     for (int i = paramInt1; i <= paramInt2; i++) {
/* 1039 */       if (include(i)) {
/* 1040 */         localArrayList.add(new Row(this, i));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1046 */     for (i = this.modelToView.length - 1; i >= paramInt1; i--) {
/* 1047 */       int k = this.modelToView[i];
/* 1048 */       if (k != -1) {
/* 1049 */         this.viewToModel[k].modelIndex += j;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1054 */     if (localArrayList.size() > 0) {
/* 1055 */       Collections.sort(localArrayList);
/* 1056 */       Row[] arrayOfRow = this.viewToModel;
/* 1057 */       this.viewToModel = new Row[this.viewToModel.length + localArrayList.size()];
/* 1058 */       insertInOrder(localArrayList, arrayOfRow);
/*      */     }
/*      */ 
/* 1062 */     createModelToView(getModelWrapper().getRowCount());
/* 1063 */     setModelToViewFromViewToModel(true);
/*      */ 
/* 1066 */     fireRowSorterChanged(arrayOfInt);
/*      */   }
/*      */ 
/*      */   private void rowsDeleted0(int paramInt1, int paramInt2) {
/* 1070 */     int[] arrayOfInt = getViewToModelAsInts(this.viewToModel);
/* 1071 */     int i = 0;
/*      */     int k;
/* 1076 */     for (int j = paramInt1; j <= paramInt2; j++) {
/* 1077 */       k = this.modelToView[j];
/* 1078 */       if (k != -1) {
/* 1079 */         i++;
/* 1080 */         this.viewToModel[k] = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1085 */     int m = paramInt2 - paramInt1 + 1;
/* 1086 */     for (j = this.modelToView.length - 1; j > paramInt2; j--) {
/* 1087 */       k = this.modelToView[j];
/* 1088 */       if (k != -1) {
/* 1089 */         this.viewToModel[k].modelIndex -= m;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1094 */     if (i > 0) {
/* 1095 */       Row[] arrayOfRow = new Row[this.viewToModel.length - i];
/*      */ 
/* 1097 */       int n = 0;
/* 1098 */       int i1 = 0;
/* 1099 */       for (j = 0; j < this.viewToModel.length; j++) {
/* 1100 */         if (this.viewToModel[j] == null) {
/* 1101 */           System.arraycopy(this.viewToModel, i1, arrayOfRow, n, j - i1);
/*      */ 
/* 1103 */           n += j - i1;
/* 1104 */           i1 = j + 1;
/*      */         }
/*      */       }
/* 1107 */       System.arraycopy(this.viewToModel, i1, arrayOfRow, n, this.viewToModel.length - i1);
/*      */ 
/* 1109 */       this.viewToModel = arrayOfRow;
/*      */     }
/*      */ 
/* 1113 */     createModelToView(getModelWrapper().getRowCount());
/* 1114 */     setModelToViewFromViewToModel(true);
/*      */ 
/* 1117 */     fireRowSorterChanged(arrayOfInt);
/*      */   }
/*      */ 
/*      */   private void rowsUpdated0(int paramInt1, int paramInt2) {
/* 1121 */     int[] arrayOfInt = getViewToModelAsInts(this.viewToModel);
/*      */ 
/* 1123 */     int k = paramInt2 - paramInt1 + 1;
/*      */     Object localObject;
/*      */     int j;
/*      */     int i;
/*      */     int m;
/* 1128 */     if (getRowFilter() == null)
/*      */     {
/* 1132 */       localObject = new Row[k];
/* 1133 */       j = 0; for (i = paramInt1; i <= paramInt2; j++) {
/* 1134 */         localObject[j] = this.viewToModel[this.modelToView[i]];
/*      */ 
/* 1133 */         i++;
/*      */       }
/*      */ 
/* 1138 */       Arrays.sort((Object[])localObject);
/*      */ 
/* 1142 */       Row[] arrayOfRow1 = new Row[this.viewToModel.length - k];
/* 1143 */       i = 0; for (j = 0; i < this.viewToModel.length; i++) {
/* 1144 */         m = this.viewToModel[i].modelIndex;
/* 1145 */         if ((m < paramInt1) || (m > paramInt2)) {
/* 1146 */           arrayOfRow1[(j++)] = this.viewToModel[i];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1151 */       insertInOrder(Arrays.asList((Object[])localObject), arrayOfRow1);
/*      */ 
/* 1154 */       setModelToViewFromViewToModel(false);
/*      */     }
/*      */     else
/*      */     {
/* 1161 */       localObject = new ArrayList(k);
/* 1162 */       int n = 0;
/* 1163 */       int i1 = 0;
/* 1164 */       int i2 = 0;
/* 1165 */       for (i = paramInt1; i <= paramInt2; i++) {
/* 1166 */         if (this.modelToView[i] == -1)
/*      */         {
/* 1168 */           if (include(i))
/*      */           {
/* 1170 */             ((List)localObject).add(new Row(this, i));
/* 1171 */             n++;
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1177 */           if (!include(i)) {
/* 1178 */             i1++;
/*      */           }
/*      */           else {
/* 1181 */             ((List)localObject).add(this.viewToModel[this.modelToView[i]]);
/*      */           }
/* 1183 */           this.modelToView[i] = -2;
/* 1184 */           i2++;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1189 */       Collections.sort((List)localObject);
/*      */ 
/* 1193 */       Row[] arrayOfRow2 = new Row[this.viewToModel.length - i2];
/* 1194 */       i = 0; for (j = 0; i < this.viewToModel.length; i++) {
/* 1195 */         m = this.viewToModel[i].modelIndex;
/* 1196 */         if (this.modelToView[m] != -2) {
/* 1197 */           arrayOfRow2[(j++)] = this.viewToModel[i];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1202 */       if (n != i1) {
/* 1203 */         this.viewToModel = new Row[this.viewToModel.length + n - i1];
/*      */       }
/*      */ 
/* 1208 */       insertInOrder((List)localObject, arrayOfRow2);
/*      */ 
/* 1211 */       setModelToViewFromViewToModel(true);
/*      */     }
/*      */ 
/* 1214 */     fireRowSorterChanged(arrayOfInt);
/*      */   }
/*      */ 
/*      */   private void checkColumn(int paramInt) {
/* 1218 */     if ((paramInt < 0) || (paramInt >= getModelWrapper().getColumnCount()))
/* 1219 */       throw new IndexOutOfBoundsException("column beyond range of TableModel");
/*      */   }
/*      */ 
/*      */   private class FilterEntry extends RowFilter.Entry<M, I>
/*      */   {
/*      */     int modelIndex;
/*      */ 
/*      */     private FilterEntry()
/*      */     {
/*      */     }
/*      */ 
/*      */     public M getModel()
/*      */     {
/* 1340 */       return DefaultRowSorter.this.getModelWrapper().getModel();
/*      */     }
/*      */ 
/*      */     public int getValueCount() {
/* 1344 */       return DefaultRowSorter.this.getModelWrapper().getColumnCount();
/*      */     }
/*      */ 
/*      */     public Object getValue(int paramInt) {
/* 1348 */       return DefaultRowSorter.this.getModelWrapper().getValueAt(this.modelIndex, paramInt);
/*      */     }
/*      */ 
/*      */     public String getStringValue(int paramInt) {
/* 1352 */       return DefaultRowSorter.this.getModelWrapper().getStringValueAt(this.modelIndex, paramInt);
/*      */     }
/*      */ 
/*      */     public I getIdentifier() {
/* 1356 */       return DefaultRowSorter.this.getModelWrapper().getIdentifier(this.modelIndex);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static abstract class ModelWrapper<M, I>
/*      */   {
/*      */     public abstract M getModel();
/*      */ 
/*      */     public abstract int getColumnCount();
/*      */ 
/*      */     public abstract int getRowCount();
/*      */ 
/*      */     public abstract Object getValueAt(int paramInt1, int paramInt2);
/*      */ 
/*      */     public String getStringValueAt(int paramInt1, int paramInt2)
/*      */     {
/* 1301 */       Object localObject = getValueAt(paramInt1, paramInt2);
/* 1302 */       if (localObject == null) {
/* 1303 */         return "";
/*      */       }
/* 1305 */       String str = localObject.toString();
/* 1306 */       if (str == null) {
/* 1307 */         return "";
/*      */       }
/* 1309 */       return str;
/*      */     }
/*      */ 
/*      */     public abstract I getIdentifier(int paramInt);
/*      */   }
/*      */ 
/*      */   private static class Row
/*      */     implements Comparable<Row>
/*      */   {
/*      */     private DefaultRowSorter sorter;
/*      */     int modelIndex;
/*      */ 
/*      */     public Row(DefaultRowSorter paramDefaultRowSorter, int paramInt)
/*      */     {
/* 1371 */       this.sorter = paramDefaultRowSorter;
/* 1372 */       this.modelIndex = paramInt;
/*      */     }
/*      */ 
/*      */     public int compareTo(Row paramRow) {
/* 1376 */       return this.sorter.compare(this.modelIndex, paramRow.modelIndex);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultRowSorter
 * JD-Core Version:    0.6.2
 */