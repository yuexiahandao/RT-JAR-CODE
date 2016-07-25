/*     */ package javax.swing;
/*     */ 
/*     */ import java.beans.Transient;
/*     */ import java.io.Serializable;
/*     */ import java.util.BitSet;
/*     */ import java.util.EventListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ 
/*     */ public class DefaultListSelectionModel
/*     */   implements ListSelectionModel, Cloneable, Serializable
/*     */ {
/*     */   private static final int MIN = -1;
/*     */   private static final int MAX = 2147483647;
/*  57 */   private int selectionMode = 2;
/*  58 */   private int minIndex = 2147483647;
/*  59 */   private int maxIndex = -1;
/*  60 */   private int anchorIndex = -1;
/*  61 */   private int leadIndex = -1;
/*  62 */   private int firstAdjustedIndex = 2147483647;
/*  63 */   private int lastAdjustedIndex = -1;
/*  64 */   private boolean isAdjusting = false;
/*     */ 
/*  66 */   private int firstChangedIndex = 2147483647;
/*  67 */   private int lastChangedIndex = -1;
/*     */ 
/*  69 */   private BitSet value = new BitSet(32);
/*  70 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*  72 */   protected boolean leadAnchorNotificationEnabled = true;
/*     */ 
/*     */   public int getMinSelectionIndex() {
/*  75 */     return isSelectionEmpty() ? -1 : this.minIndex;
/*     */   }
/*     */   public int getMaxSelectionIndex() {
/*  78 */     return this.maxIndex;
/*     */   }
/*     */   public boolean getValueIsAdjusting() {
/*  81 */     return this.isAdjusting;
/*     */   }
/*     */   public int getSelectionMode() {
/*  84 */     return this.selectionMode;
/*     */   }
/*     */ 
/*     */   public void setSelectionMode(int paramInt)
/*     */   {
/*  91 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*  95 */       this.selectionMode = paramInt;
/*  96 */       break;
/*     */     default:
/*  98 */       throw new IllegalArgumentException("invalid selectionMode");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isSelectedIndex(int paramInt)
/*     */   {
/* 104 */     return (paramInt < this.minIndex) || (paramInt > this.maxIndex) ? false : this.value.get(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isSelectionEmpty()
/*     */   {
/* 109 */     return this.minIndex > this.maxIndex;
/*     */   }
/*     */ 
/*     */   public void addListSelectionListener(ListSelectionListener paramListSelectionListener)
/*     */   {
/* 114 */     this.listenerList.add(ListSelectionListener.class, paramListSelectionListener);
/*     */   }
/*     */ 
/*     */   public void removeListSelectionListener(ListSelectionListener paramListSelectionListener)
/*     */   {
/* 119 */     this.listenerList.remove(ListSelectionListener.class, paramListSelectionListener);
/*     */   }
/*     */ 
/*     */   public ListSelectionListener[] getListSelectionListeners()
/*     */   {
/* 136 */     return (ListSelectionListener[])this.listenerList.getListeners(ListSelectionListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireValueChanged(boolean paramBoolean)
/*     */   {
/* 143 */     if (this.lastChangedIndex == -1) {
/* 144 */       return;
/*     */     }
/*     */ 
/* 150 */     int i = this.firstChangedIndex;
/* 151 */     int j = this.lastChangedIndex;
/* 152 */     this.firstChangedIndex = 2147483647;
/* 153 */     this.lastChangedIndex = -1;
/* 154 */     fireValueChanged(i, j, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void fireValueChanged(int paramInt1, int paramInt2)
/*     */   {
/* 164 */     fireValueChanged(paramInt1, paramInt2, getValueIsAdjusting());
/*     */   }
/*     */ 
/*     */   protected void fireValueChanged(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 176 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 177 */     ListSelectionEvent localListSelectionEvent = null;
/*     */ 
/* 179 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 180 */       if (arrayOfObject[i] == ListSelectionListener.class) {
/* 181 */         if (localListSelectionEvent == null) {
/* 182 */           localListSelectionEvent = new ListSelectionEvent(this, paramInt1, paramInt2, paramBoolean);
/*     */         }
/* 184 */         ((ListSelectionListener)arrayOfObject[(i + 1)]).valueChanged(localListSelectionEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void fireValueChanged()
/*     */   {
/* 190 */     if (this.lastAdjustedIndex == -1) {
/* 191 */       return;
/*     */     }
/*     */ 
/* 198 */     if (getValueIsAdjusting()) {
/* 199 */       this.firstChangedIndex = Math.min(this.firstChangedIndex, this.firstAdjustedIndex);
/* 200 */       this.lastChangedIndex = Math.max(this.lastChangedIndex, this.lastAdjustedIndex);
/*     */     }
/*     */ 
/* 206 */     int i = this.firstAdjustedIndex;
/* 207 */     int j = this.lastAdjustedIndex;
/* 208 */     this.firstAdjustedIndex = 2147483647;
/* 209 */     this.lastAdjustedIndex = -1;
/*     */ 
/* 211 */     fireValueChanged(i, j);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 250 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ 
/*     */   private void markAsDirty(int paramInt)
/*     */   {
/* 255 */     if (paramInt == -1) {
/* 256 */       return;
/*     */     }
/*     */ 
/* 259 */     this.firstAdjustedIndex = Math.min(this.firstAdjustedIndex, paramInt);
/* 260 */     this.lastAdjustedIndex = Math.max(this.lastAdjustedIndex, paramInt);
/*     */   }
/*     */ 
/*     */   private void set(int paramInt)
/*     */   {
/* 265 */     if (this.value.get(paramInt)) {
/* 266 */       return;
/*     */     }
/* 268 */     this.value.set(paramInt);
/* 269 */     markAsDirty(paramInt);
/*     */ 
/* 272 */     this.minIndex = Math.min(this.minIndex, paramInt);
/* 273 */     this.maxIndex = Math.max(this.maxIndex, paramInt);
/*     */   }
/*     */ 
/*     */   private void clear(int paramInt)
/*     */   {
/* 278 */     if (!this.value.get(paramInt)) {
/* 279 */       return;
/*     */     }
/* 281 */     this.value.clear(paramInt);
/* 282 */     markAsDirty(paramInt);
/*     */ 
/* 291 */     if (paramInt == this.minIndex)
/*     */     {
/* 292 */       for (this.minIndex += 1; (this.minIndex <= this.maxIndex) && 
/* 293 */         (!this.value.get(this.minIndex)); this.minIndex += 1);
/*     */     }
/*     */ 
/* 304 */     if (paramInt == this.maxIndex)
/*     */     {
/* 305 */       for (this.maxIndex -= 1; (this.minIndex <= this.maxIndex) && 
/* 306 */         (!this.value.get(this.maxIndex)); this.maxIndex -= 1);
/*     */     }
/*     */ 
/* 326 */     if (isSelectionEmpty()) {
/* 327 */       this.minIndex = 2147483647;
/* 328 */       this.maxIndex = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setLeadAnchorNotificationEnabled(boolean paramBoolean)
/*     */   {
/* 337 */     this.leadAnchorNotificationEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isLeadAnchorNotificationEnabled()
/*     */   {
/* 359 */     return this.leadAnchorNotificationEnabled;
/*     */   }
/*     */ 
/*     */   private void updateLeadAnchorIndices(int paramInt1, int paramInt2) {
/* 363 */     if (this.leadAnchorNotificationEnabled) {
/* 364 */       if (this.anchorIndex != paramInt1) {
/* 365 */         markAsDirty(this.anchorIndex);
/* 366 */         markAsDirty(paramInt1);
/*     */       }
/*     */ 
/* 369 */       if (this.leadIndex != paramInt2) {
/* 370 */         markAsDirty(this.leadIndex);
/* 371 */         markAsDirty(paramInt2);
/*     */       }
/*     */     }
/* 374 */     this.anchorIndex = paramInt1;
/* 375 */     this.leadIndex = paramInt2;
/*     */   }
/*     */ 
/*     */   private boolean contains(int paramInt1, int paramInt2, int paramInt3) {
/* 379 */     return (paramInt3 >= paramInt1) && (paramInt3 <= paramInt2);
/*     */   }
/*     */ 
/*     */   private void changeSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 384 */     for (int i = Math.min(paramInt3, paramInt1); i <= Math.max(paramInt4, paramInt2); i++)
/*     */     {
/* 386 */       boolean bool1 = contains(paramInt1, paramInt2, i);
/* 387 */       boolean bool2 = contains(paramInt3, paramInt4, i);
/*     */ 
/* 389 */       if ((bool2) && (bool1)) {
/* 390 */         if (paramBoolean) {
/* 391 */           bool1 = false;
/*     */         }
/*     */         else {
/* 394 */           bool2 = false;
/*     */         }
/*     */       }
/*     */ 
/* 398 */       if (bool2) {
/* 399 */         set(i);
/*     */       }
/* 401 */       if (bool1) {
/* 402 */         clear(i);
/*     */       }
/*     */     }
/* 405 */     fireValueChanged();
/*     */   }
/*     */ 
/*     */   private void changeSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 415 */     changeSelection(paramInt1, paramInt2, paramInt3, paramInt4, true);
/*     */   }
/*     */ 
/*     */   public void clearSelection()
/*     */   {
/* 420 */     removeSelectionIntervalImpl(this.minIndex, this.maxIndex, false);
/*     */   }
/*     */ 
/*     */   public void setSelectionInterval(int paramInt1, int paramInt2)
/*     */   {
/* 445 */     if ((paramInt1 == -1) || (paramInt2 == -1)) {
/* 446 */       return;
/*     */     }
/*     */ 
/* 449 */     if (getSelectionMode() == 0) {
/* 450 */       paramInt1 = paramInt2;
/*     */     }
/*     */ 
/* 453 */     updateLeadAnchorIndices(paramInt1, paramInt2);
/*     */ 
/* 455 */     int i = this.minIndex;
/* 456 */     int j = this.maxIndex;
/* 457 */     int k = Math.min(paramInt1, paramInt2);
/* 458 */     int m = Math.max(paramInt1, paramInt2);
/* 459 */     changeSelection(i, j, k, m);
/*     */   }
/*     */ 
/*     */   public void addSelectionInterval(int paramInt1, int paramInt2)
/*     */   {
/* 490 */     if ((paramInt1 == -1) || (paramInt2 == -1)) {
/* 491 */       return;
/*     */     }
/*     */ 
/* 496 */     if (getSelectionMode() == 0) {
/* 497 */       setSelectionInterval(paramInt1, paramInt2);
/* 498 */       return;
/*     */     }
/*     */ 
/* 501 */     updateLeadAnchorIndices(paramInt1, paramInt2);
/*     */ 
/* 503 */     int i = 2147483647;
/* 504 */     int j = -1;
/* 505 */     int k = Math.min(paramInt1, paramInt2);
/* 506 */     int m = Math.max(paramInt1, paramInt2);
/*     */ 
/* 511 */     if ((getSelectionMode() == 1) && ((m < this.minIndex - 1) || (k > this.maxIndex + 1)))
/*     */     {
/* 514 */       setSelectionInterval(paramInt1, paramInt2);
/* 515 */       return;
/*     */     }
/*     */ 
/* 518 */     changeSelection(i, j, k, m);
/*     */   }
/*     */ 
/*     */   public void removeSelectionInterval(int paramInt1, int paramInt2)
/*     */   {
/* 548 */     removeSelectionIntervalImpl(paramInt1, paramInt2, true);
/*     */   }
/*     */ 
/*     */   private void removeSelectionIntervalImpl(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 556 */     if ((paramInt1 == -1) || (paramInt2 == -1)) {
/* 557 */       return;
/*     */     }
/*     */ 
/* 560 */     if (paramBoolean) {
/* 561 */       updateLeadAnchorIndices(paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 564 */     int i = Math.min(paramInt1, paramInt2);
/* 565 */     int j = Math.max(paramInt1, paramInt2);
/* 566 */     int k = 2147483647;
/* 567 */     int m = -1;
/*     */ 
/* 571 */     if ((getSelectionMode() != 2) && (i > this.minIndex) && (j < this.maxIndex))
/*     */     {
/* 573 */       j = this.maxIndex;
/*     */     }
/*     */ 
/* 576 */     changeSelection(i, j, k, m);
/*     */   }
/*     */ 
/*     */   private void setState(int paramInt, boolean paramBoolean) {
/* 580 */     if (paramBoolean) {
/* 581 */       set(paramInt);
/*     */     }
/*     */     else
/* 584 */       clear(paramInt);
/*     */   }
/*     */ 
/*     */   public void insertIndexInterval(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 601 */     int i = paramBoolean ? paramInt1 : paramInt1 + 1;
/* 602 */     int j = i + paramInt2 - 1;
/*     */ 
/* 608 */     for (int k = this.maxIndex; k >= i; k--) {
/* 609 */       setState(k + paramInt2, this.value.get(k));
/*     */     }
/*     */ 
/* 614 */     boolean bool = getSelectionMode() == 0 ? false : this.value.get(paramInt1);
/*     */ 
/* 616 */     for (int m = i; m <= j; m++) {
/* 617 */       setState(m, bool);
/*     */     }
/*     */ 
/* 620 */     m = this.leadIndex;
/* 621 */     if ((m > paramInt1) || ((paramBoolean) && (m == paramInt1))) {
/* 622 */       m = this.leadIndex + paramInt2;
/*     */     }
/* 624 */     int n = this.anchorIndex;
/* 625 */     if ((n > paramInt1) || ((paramBoolean) && (n == paramInt1))) {
/* 626 */       n = this.anchorIndex + paramInt2;
/*     */     }
/* 628 */     if ((m != this.leadIndex) || (n != this.anchorIndex)) {
/* 629 */       updateLeadAnchorIndices(n, m);
/*     */     }
/*     */ 
/* 632 */     fireValueChanged();
/*     */   }
/*     */ 
/*     */   public void removeIndexInterval(int paramInt1, int paramInt2)
/*     */   {
/* 644 */     int i = Math.min(paramInt1, paramInt2);
/* 645 */     int j = Math.max(paramInt1, paramInt2);
/* 646 */     int k = j - i + 1;
/*     */ 
/* 651 */     for (int m = i; m <= this.maxIndex; m++) {
/* 652 */       setState(m, this.value.get(m + k));
/*     */     }
/*     */ 
/* 655 */     m = this.leadIndex;
/* 656 */     if ((m != 0) || (i != 0))
/*     */     {
/* 658 */       if (m > j)
/* 659 */         m = this.leadIndex - k;
/* 660 */       else if (m >= i) {
/* 661 */         m = i - 1;
/*     */       }
/*     */     }
/* 664 */     int n = this.anchorIndex;
/* 665 */     if ((n != 0) || (i != 0))
/*     */     {
/* 667 */       if (n > j)
/* 668 */         n = this.anchorIndex - k;
/* 669 */       else if (n >= i) {
/* 670 */         n = i - 1;
/*     */       }
/*     */     }
/* 673 */     if ((m != this.leadIndex) || (n != this.anchorIndex)) {
/* 674 */       updateLeadAnchorIndices(n, m);
/*     */     }
/*     */ 
/* 677 */     fireValueChanged();
/*     */   }
/*     */ 
/*     */   public void setValueIsAdjusting(boolean paramBoolean)
/*     */   {
/* 683 */     if (paramBoolean != this.isAdjusting) {
/* 684 */       this.isAdjusting = paramBoolean;
/* 685 */       fireValueChanged(paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 697 */     String str = (getValueIsAdjusting() ? "~" : "=") + this.value.toString();
/* 698 */     return getClass().getName() + " " + Integer.toString(hashCode()) + " " + str;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 710 */     DefaultListSelectionModel localDefaultListSelectionModel = (DefaultListSelectionModel)super.clone();
/* 711 */     localDefaultListSelectionModel.value = ((BitSet)this.value.clone());
/* 712 */     localDefaultListSelectionModel.listenerList = new EventListenerList();
/* 713 */     return localDefaultListSelectionModel;
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public int getAnchorSelectionIndex()
/*     */   {
/* 719 */     return this.anchorIndex;
/*     */   }
/*     */ 
/*     */   @Transient
/*     */   public int getLeadSelectionIndex()
/*     */   {
/* 725 */     return this.leadIndex;
/*     */   }
/*     */ 
/*     */   public void setAnchorSelectionIndex(int paramInt)
/*     */   {
/* 737 */     updateLeadAnchorIndices(paramInt, this.leadIndex);
/* 738 */     fireValueChanged();
/*     */   }
/*     */ 
/*     */   public void moveLeadSelectionIndex(int paramInt)
/*     */   {
/* 756 */     if ((paramInt == -1) && 
/* 757 */       (this.anchorIndex != -1)) {
/* 758 */       return;
/*     */     }
/*     */ 
/* 776 */     updateLeadAnchorIndices(this.anchorIndex, paramInt);
/* 777 */     fireValueChanged();
/*     */   }
/*     */ 
/*     */   public void setLeadSelectionIndex(int paramInt)
/*     */   {
/* 809 */     int i = this.anchorIndex;
/*     */ 
/* 812 */     if (paramInt == -1) {
/* 813 */       if (i == -1) {
/* 814 */         updateLeadAnchorIndices(i, paramInt);
/* 815 */         fireValueChanged();
/*     */       }
/*     */ 
/* 818 */       return;
/*     */     }
/* 820 */     if (i == -1) {
/* 821 */       return;
/*     */     }
/*     */ 
/* 824 */     if (this.leadIndex == -1) {
/* 825 */       this.leadIndex = paramInt;
/*     */     }
/*     */ 
/* 828 */     boolean bool = this.value.get(this.anchorIndex);
/*     */ 
/* 830 */     if (getSelectionMode() == 0) {
/* 831 */       i = paramInt;
/* 832 */       bool = true;
/*     */     }
/*     */ 
/* 835 */     int j = Math.min(this.anchorIndex, this.leadIndex);
/* 836 */     int k = Math.max(this.anchorIndex, this.leadIndex);
/* 837 */     int m = Math.min(i, paramInt);
/* 838 */     int n = Math.max(i, paramInt);
/*     */ 
/* 840 */     updateLeadAnchorIndices(i, paramInt);
/*     */ 
/* 842 */     if (bool) {
/* 843 */       changeSelection(j, k, m, n);
/*     */     }
/*     */     else
/* 846 */       changeSelection(m, n, j, k, false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultListSelectionModel
 * JD-Core Version:    0.6.2
 */