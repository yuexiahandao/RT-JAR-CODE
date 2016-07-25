/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.BitSet;
/*     */ import javax.swing.DefaultListModel;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ 
/*     */ class OptionListModel extends DefaultListModel
/*     */   implements ListSelectionModel, Serializable
/*     */ {
/*     */   private static final int MIN = -1;
/*     */   private static final int MAX = 2147483647;
/*  52 */   private int selectionMode = 0;
/*  53 */   private int minIndex = 2147483647;
/*  54 */   private int maxIndex = -1;
/*  55 */   private int anchorIndex = -1;
/*  56 */   private int leadIndex = -1;
/*  57 */   private int firstChangedIndex = 2147483647;
/*  58 */   private int lastChangedIndex = -1;
/*  59 */   private boolean isAdjusting = false;
/*  60 */   private BitSet value = new BitSet(32);
/*  61 */   private BitSet initialValue = new BitSet(32);
/*  62 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*  64 */   protected boolean leadAnchorNotificationEnabled = true;
/*     */ 
/*  66 */   public int getMinSelectionIndex() { return isSelectionEmpty() ? -1 : this.minIndex; } 
/*     */   public int getMaxSelectionIndex() {
/*  68 */     return this.maxIndex;
/*     */   }
/*  70 */   public boolean getValueIsAdjusting() { return this.isAdjusting; } 
/*     */   public int getSelectionMode() {
/*  72 */     return this.selectionMode;
/*     */   }
/*     */   public void setSelectionMode(int paramInt) {
/*  75 */     switch (paramInt) {
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*  79 */       this.selectionMode = paramInt;
/*  80 */       break;
/*     */     default:
/*  82 */       throw new IllegalArgumentException("invalid selectionMode");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isSelectedIndex(int paramInt) {
/*  87 */     return (paramInt < this.minIndex) || (paramInt > this.maxIndex) ? false : this.value.get(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isSelectionEmpty() {
/*  91 */     return this.minIndex > this.maxIndex;
/*     */   }
/*     */ 
/*     */   public void addListSelectionListener(ListSelectionListener paramListSelectionListener) {
/*  95 */     this.listenerList.add(ListSelectionListener.class, paramListSelectionListener);
/*     */   }
/*     */ 
/*     */   public void removeListSelectionListener(ListSelectionListener paramListSelectionListener) {
/*  99 */     this.listenerList.remove(ListSelectionListener.class, paramListSelectionListener);
/*     */   }
/*     */ 
/*     */   public ListSelectionListener[] getListSelectionListeners()
/*     */   {
/* 111 */     return (ListSelectionListener[])this.listenerList.getListeners(ListSelectionListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireValueChanged(boolean paramBoolean)
/*     */   {
/* 119 */     fireValueChanged(getMinSelectionIndex(), getMaxSelectionIndex(), paramBoolean);
/*     */   }
/*     */ 
/*     */   protected void fireValueChanged(int paramInt1, int paramInt2)
/*     */   {
/* 128 */     fireValueChanged(paramInt1, paramInt2, getValueIsAdjusting());
/*     */   }
/*     */ 
/*     */   protected void fireValueChanged(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 139 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 140 */     ListSelectionEvent localListSelectionEvent = null;
/*     */ 
/* 142 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 143 */       if (arrayOfObject[i] == ListSelectionListener.class) {
/* 144 */         if (localListSelectionEvent == null) {
/* 145 */           localListSelectionEvent = new ListSelectionEvent(this, paramInt1, paramInt2, paramBoolean);
/*     */         }
/* 147 */         ((ListSelectionListener)arrayOfObject[(i + 1)]).valueChanged(localListSelectionEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void fireValueChanged()
/*     */   {
/* 153 */     if (this.lastChangedIndex == -1) {
/* 154 */       return;
/*     */     }
/*     */ 
/* 160 */     int i = this.firstChangedIndex;
/* 161 */     int j = this.lastChangedIndex;
/* 162 */     this.firstChangedIndex = 2147483647;
/* 163 */     this.lastChangedIndex = -1;
/* 164 */     fireValueChanged(i, j);
/*     */   }
/*     */ 
/*     */   private void markAsDirty(int paramInt)
/*     */   {
/* 170 */     this.firstChangedIndex = Math.min(this.firstChangedIndex, paramInt);
/* 171 */     this.lastChangedIndex = Math.max(this.lastChangedIndex, paramInt);
/*     */   }
/*     */ 
/*     */   private void set(int paramInt)
/*     */   {
/* 176 */     if (this.value.get(paramInt)) {
/* 177 */       return;
/*     */     }
/* 179 */     this.value.set(paramInt);
/* 180 */     Option localOption = (Option)get(paramInt);
/* 181 */     localOption.setSelection(true);
/* 182 */     markAsDirty(paramInt);
/*     */ 
/* 185 */     this.minIndex = Math.min(this.minIndex, paramInt);
/* 186 */     this.maxIndex = Math.max(this.maxIndex, paramInt);
/*     */   }
/*     */ 
/*     */   private void clear(int paramInt)
/*     */   {
/* 191 */     if (!this.value.get(paramInt)) {
/* 192 */       return;
/*     */     }
/* 194 */     this.value.clear(paramInt);
/* 195 */     Option localOption = (Option)get(paramInt);
/* 196 */     localOption.setSelection(false);
/* 197 */     markAsDirty(paramInt);
/*     */ 
/* 206 */     if (paramInt == this.minIndex)
/*     */     {
/* 207 */       for (this.minIndex += 1; (this.minIndex <= this.maxIndex) && 
/* 208 */         (!this.value.get(this.minIndex)); this.minIndex += 1);
/*     */     }
/*     */ 
/* 219 */     if (paramInt == this.maxIndex)
/*     */     {
/* 220 */       for (this.maxIndex -= 1; (this.minIndex <= this.maxIndex) && 
/* 221 */         (!this.value.get(this.maxIndex)); this.maxIndex -= 1);
/*     */     }
/*     */ 
/* 241 */     if (isSelectionEmpty()) {
/* 242 */       this.minIndex = 2147483647;
/* 243 */       this.maxIndex = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setLeadAnchorNotificationEnabled(boolean paramBoolean)
/*     */   {
/* 252 */     this.leadAnchorNotificationEnabled = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isLeadAnchorNotificationEnabled()
/*     */   {
/* 268 */     return this.leadAnchorNotificationEnabled;
/*     */   }
/*     */ 
/*     */   private void updateLeadAnchorIndices(int paramInt1, int paramInt2) {
/* 272 */     if (this.leadAnchorNotificationEnabled) {
/* 273 */       if (this.anchorIndex != paramInt1) {
/* 274 */         if (this.anchorIndex != -1) {
/* 275 */           markAsDirty(this.anchorIndex);
/*     */         }
/* 277 */         markAsDirty(paramInt1);
/*     */       }
/*     */ 
/* 280 */       if (this.leadIndex != paramInt2) {
/* 281 */         if (this.leadIndex != -1) {
/* 282 */           markAsDirty(this.leadIndex);
/*     */         }
/* 284 */         markAsDirty(paramInt2);
/*     */       }
/*     */     }
/* 287 */     this.anchorIndex = paramInt1;
/* 288 */     this.leadIndex = paramInt2;
/*     */   }
/*     */ 
/*     */   private boolean contains(int paramInt1, int paramInt2, int paramInt3) {
/* 292 */     return (paramInt3 >= paramInt1) && (paramInt3 <= paramInt2);
/*     */   }
/*     */ 
/*     */   private void changeSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 297 */     for (int i = Math.min(paramInt3, paramInt1); i <= Math.max(paramInt4, paramInt2); i++)
/*     */     {
/* 299 */       boolean bool1 = contains(paramInt1, paramInt2, i);
/* 300 */       boolean bool2 = contains(paramInt3, paramInt4, i);
/*     */ 
/* 302 */       if ((bool2) && (bool1)) {
/* 303 */         if (paramBoolean) {
/* 304 */           bool1 = false;
/*     */         }
/*     */         else {
/* 307 */           bool2 = false;
/*     */         }
/*     */       }
/*     */ 
/* 311 */       if (bool2) {
/* 312 */         set(i);
/*     */       }
/* 314 */       if (bool1) {
/* 315 */         clear(i);
/*     */       }
/*     */     }
/* 318 */     fireValueChanged();
/*     */   }
/*     */ 
/*     */   private void changeSelection(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 327 */     changeSelection(paramInt1, paramInt2, paramInt3, paramInt4, true);
/*     */   }
/*     */ 
/*     */   public void clearSelection() {
/* 331 */     removeSelectionInterval(this.minIndex, this.maxIndex);
/*     */   }
/*     */ 
/*     */   public void setSelectionInterval(int paramInt1, int paramInt2) {
/* 335 */     if ((paramInt1 == -1) || (paramInt2 == -1)) {
/* 336 */       return;
/*     */     }
/*     */ 
/* 339 */     if (getSelectionMode() == 0) {
/* 340 */       paramInt1 = paramInt2;
/*     */     }
/*     */ 
/* 343 */     updateLeadAnchorIndices(paramInt1, paramInt2);
/*     */ 
/* 345 */     int i = this.minIndex;
/* 346 */     int j = this.maxIndex;
/* 347 */     int k = Math.min(paramInt1, paramInt2);
/* 348 */     int m = Math.max(paramInt1, paramInt2);
/* 349 */     changeSelection(i, j, k, m);
/*     */   }
/*     */ 
/*     */   public void addSelectionInterval(int paramInt1, int paramInt2)
/*     */   {
/* 354 */     if ((paramInt1 == -1) || (paramInt2 == -1)) {
/* 355 */       return;
/*     */     }
/*     */ 
/* 358 */     if (getSelectionMode() != 2) {
/* 359 */       setSelectionInterval(paramInt1, paramInt2);
/* 360 */       return;
/*     */     }
/*     */ 
/* 363 */     updateLeadAnchorIndices(paramInt1, paramInt2);
/*     */ 
/* 365 */     int i = 2147483647;
/* 366 */     int j = -1;
/* 367 */     int k = Math.min(paramInt1, paramInt2);
/* 368 */     int m = Math.max(paramInt1, paramInt2);
/* 369 */     changeSelection(i, j, k, m);
/*     */   }
/*     */ 
/*     */   public void removeSelectionInterval(int paramInt1, int paramInt2)
/*     */   {
/* 375 */     if ((paramInt1 == -1) || (paramInt2 == -1)) {
/* 376 */       return;
/*     */     }
/*     */ 
/* 379 */     updateLeadAnchorIndices(paramInt1, paramInt2);
/*     */ 
/* 381 */     int i = Math.min(paramInt1, paramInt2);
/* 382 */     int j = Math.max(paramInt1, paramInt2);
/* 383 */     int k = 2147483647;
/* 384 */     int m = -1;
/* 385 */     changeSelection(i, j, k, m);
/*     */   }
/*     */ 
/*     */   private void setState(int paramInt, boolean paramBoolean) {
/* 389 */     if (paramBoolean) {
/* 390 */       set(paramInt);
/*     */     }
/*     */     else
/* 393 */       clear(paramInt);
/*     */   }
/*     */ 
/*     */   public void insertIndexInterval(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 409 */     int i = paramBoolean ? paramInt1 : paramInt1 + 1;
/* 410 */     int j = i + paramInt2 - 1;
/*     */ 
/* 416 */     for (int k = this.maxIndex; k >= i; k--) {
/* 417 */       setState(k + paramInt2, this.value.get(k));
/*     */     }
/*     */ 
/* 422 */     boolean bool = this.value.get(paramInt1);
/* 423 */     for (int m = i; m <= j; m++)
/* 424 */       setState(m, bool);
/*     */   }
/*     */ 
/*     */   public void removeIndexInterval(int paramInt1, int paramInt2)
/*     */   {
/* 437 */     int i = Math.min(paramInt1, paramInt2);
/* 438 */     int j = Math.max(paramInt1, paramInt2);
/* 439 */     int k = j - i + 1;
/*     */ 
/* 444 */     for (int m = i; m <= this.maxIndex; m++)
/* 445 */       setState(m, this.value.get(m + k));
/*     */   }
/*     */ 
/*     */   public void setValueIsAdjusting(boolean paramBoolean)
/*     */   {
/* 451 */     if (paramBoolean != this.isAdjusting) {
/* 452 */       this.isAdjusting = paramBoolean;
/* 453 */       fireValueChanged(paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 459 */     String str = (getValueIsAdjusting() ? "~" : "=") + this.value.toString();
/* 460 */     return getClass().getName() + " " + Integer.toString(hashCode()) + " " + str;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 473 */     OptionListModel localOptionListModel = (OptionListModel)super.clone();
/* 474 */     localOptionListModel.value = ((BitSet)this.value.clone());
/* 475 */     localOptionListModel.listenerList = new EventListenerList();
/* 476 */     return localOptionListModel;
/*     */   }
/*     */ 
/*     */   public int getAnchorSelectionIndex() {
/* 480 */     return this.anchorIndex;
/*     */   }
/*     */ 
/*     */   public int getLeadSelectionIndex() {
/* 484 */     return this.leadIndex;
/*     */   }
/*     */ 
/*     */   public void setAnchorSelectionIndex(int paramInt)
/*     */   {
/* 494 */     this.anchorIndex = paramInt;
/*     */   }
/*     */ 
/*     */   public void setLeadSelectionIndex(int paramInt)
/*     */   {
/* 525 */     int i = this.anchorIndex;
/* 526 */     if (getSelectionMode() == 0) {
/* 527 */       i = paramInt;
/*     */     }
/*     */ 
/* 530 */     int j = Math.min(this.anchorIndex, this.leadIndex);
/* 531 */     int k = Math.max(this.anchorIndex, this.leadIndex);
/* 532 */     int m = Math.min(i, paramInt);
/* 533 */     int n = Math.max(i, paramInt);
/* 534 */     if (this.value.get(this.anchorIndex)) {
/* 535 */       changeSelection(j, k, m, n);
/*     */     }
/*     */     else {
/* 538 */       changeSelection(m, n, j, k, false);
/*     */     }
/* 540 */     this.anchorIndex = i;
/* 541 */     this.leadIndex = paramInt;
/*     */   }
/*     */ 
/*     */   public void setInitialSelection(int paramInt)
/*     */   {
/* 553 */     if (this.initialValue.get(paramInt)) {
/* 554 */       return;
/*     */     }
/* 556 */     if (this.selectionMode == 0)
/*     */     {
/* 558 */       this.initialValue.and(new BitSet());
/*     */     }
/* 560 */     this.initialValue.set(paramInt);
/*     */   }
/*     */ 
/*     */   public BitSet getInitialSelection()
/*     */   {
/* 568 */     return this.initialValue;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.OptionListModel
 * JD-Core Version:    0.6.2
 */