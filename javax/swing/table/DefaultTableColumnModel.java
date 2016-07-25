/*     */ package javax.swing.table;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.EventListener;
/*     */ import java.util.Vector;
/*     */ import javax.swing.DefaultListSelectionModel;
/*     */ import javax.swing.ListSelectionModel;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.event.TableColumnModelEvent;
/*     */ import javax.swing.event.TableColumnModelListener;
/*     */ 
/*     */ public class DefaultTableColumnModel
/*     */   implements TableColumnModel, PropertyChangeListener, ListSelectionListener, Serializable
/*     */ {
/*     */   protected Vector<TableColumn> tableColumns;
/*     */   protected ListSelectionModel selectionModel;
/*     */   protected int columnMargin;
/*  72 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*  75 */   protected transient ChangeEvent changeEvent = null;
/*     */   protected boolean columnSelectionAllowed;
/*     */   protected int totalColumnWidth;
/*     */ 
/*     */   public DefaultTableColumnModel()
/*     */   {
/*  93 */     this.tableColumns = new Vector();
/*  94 */     setSelectionModel(createSelectionModel());
/*  95 */     setColumnMargin(1);
/*  96 */     invalidateWidthCache();
/*  97 */     setColumnSelectionAllowed(false);
/*     */   }
/*     */ 
/*     */   public void addColumn(TableColumn paramTableColumn)
/*     */   {
/* 116 */     if (paramTableColumn == null) {
/* 117 */       throw new IllegalArgumentException("Object is null");
/*     */     }
/*     */ 
/* 120 */     this.tableColumns.addElement(paramTableColumn);
/* 121 */     paramTableColumn.addPropertyChangeListener(this);
/* 122 */     invalidateWidthCache();
/*     */ 
/* 125 */     fireColumnAdded(new TableColumnModelEvent(this, 0, getColumnCount() - 1));
/*     */   }
/*     */ 
/*     */   public void removeColumn(TableColumn paramTableColumn)
/*     */   {
/* 142 */     int i = this.tableColumns.indexOf(paramTableColumn);
/*     */ 
/* 144 */     if (i != -1)
/*     */     {
/* 146 */       if (this.selectionModel != null) {
/* 147 */         this.selectionModel.removeIndexInterval(i, i);
/*     */       }
/*     */ 
/* 150 */       paramTableColumn.removePropertyChangeListener(this);
/* 151 */       this.tableColumns.removeElementAt(i);
/* 152 */       invalidateWidthCache();
/*     */ 
/* 156 */       fireColumnRemoved(new TableColumnModelEvent(this, i, 0));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void moveColumn(int paramInt1, int paramInt2)
/*     */   {
/* 177 */     if ((paramInt1 < 0) || (paramInt1 >= getColumnCount()) || (paramInt2 < 0) || (paramInt2 >= getColumnCount()))
/*     */     {
/* 179 */       throw new IllegalArgumentException("moveColumn() - Index out of range");
/*     */     }
/*     */ 
/* 189 */     if (paramInt1 == paramInt2) {
/* 190 */       fireColumnMoved(new TableColumnModelEvent(this, paramInt1, paramInt2));
/* 191 */       return;
/*     */     }
/* 193 */     TableColumn localTableColumn = (TableColumn)this.tableColumns.elementAt(paramInt1);
/*     */ 
/* 195 */     this.tableColumns.removeElementAt(paramInt1);
/* 196 */     boolean bool = this.selectionModel.isSelectedIndex(paramInt1);
/* 197 */     this.selectionModel.removeIndexInterval(paramInt1, paramInt1);
/*     */ 
/* 199 */     this.tableColumns.insertElementAt(localTableColumn, paramInt2);
/* 200 */     this.selectionModel.insertIndexInterval(paramInt2, 1, true);
/* 201 */     if (bool) {
/* 202 */       this.selectionModel.addSelectionInterval(paramInt2, paramInt2);
/*     */     }
/*     */     else {
/* 205 */       this.selectionModel.removeSelectionInterval(paramInt2, paramInt2);
/*     */     }
/*     */ 
/* 208 */     fireColumnMoved(new TableColumnModelEvent(this, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public void setColumnMargin(int paramInt)
/*     */   {
/* 222 */     if (paramInt != this.columnMargin) {
/* 223 */       this.columnMargin = paramInt;
/*     */ 
/* 225 */       fireColumnMarginChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getColumnCount()
/*     */   {
/* 240 */     return this.tableColumns.size();
/*     */   }
/*     */ 
/*     */   public Enumeration<TableColumn> getColumns()
/*     */   {
/* 248 */     return this.tableColumns.elements();
/*     */   }
/*     */ 
/*     */   public int getColumnIndex(Object paramObject)
/*     */   {
/* 267 */     if (paramObject == null) {
/* 268 */       throw new IllegalArgumentException("Identifier is null");
/*     */     }
/*     */ 
/* 271 */     Enumeration localEnumeration = getColumns();
/*     */ 
/* 273 */     int i = 0;
/*     */ 
/* 275 */     while (localEnumeration.hasMoreElements()) {
/* 276 */       TableColumn localTableColumn = (TableColumn)localEnumeration.nextElement();
/*     */ 
/* 278 */       if (paramObject.equals(localTableColumn.getIdentifier()))
/* 279 */         return i;
/* 280 */       i++;
/*     */     }
/* 282 */     throw new IllegalArgumentException("Identifier not found");
/*     */   }
/*     */ 
/*     */   public TableColumn getColumn(int paramInt)
/*     */   {
/* 294 */     return (TableColumn)this.tableColumns.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public int getColumnMargin()
/*     */   {
/* 305 */     return this.columnMargin;
/*     */   }
/*     */ 
/*     */   public int getColumnIndexAtX(int paramInt)
/*     */   {
/* 331 */     if (paramInt < 0) {
/* 332 */       return -1;
/*     */     }
/* 334 */     int i = getColumnCount();
/* 335 */     for (int j = 0; j < i; j++) {
/* 336 */       paramInt -= getColumn(j).getWidth();
/* 337 */       if (paramInt < 0) {
/* 338 */         return j;
/*     */       }
/*     */     }
/* 341 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getTotalColumnWidth()
/*     */   {
/* 349 */     if (this.totalColumnWidth == -1) {
/* 350 */       recalcWidthCache();
/*     */     }
/* 352 */     return this.totalColumnWidth;
/*     */   }
/*     */ 
/*     */   public void setSelectionModel(ListSelectionModel paramListSelectionModel)
/*     */   {
/* 372 */     if (paramListSelectionModel == null) {
/* 373 */       throw new IllegalArgumentException("Cannot set a null SelectionModel");
/*     */     }
/*     */ 
/* 376 */     ListSelectionModel localListSelectionModel = this.selectionModel;
/*     */ 
/* 378 */     if (paramListSelectionModel != localListSelectionModel) {
/* 379 */       if (localListSelectionModel != null) {
/* 380 */         localListSelectionModel.removeListSelectionListener(this);
/*     */       }
/*     */ 
/* 383 */       this.selectionModel = paramListSelectionModel;
/* 384 */       paramListSelectionModel.addListSelectionListener(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ListSelectionModel getSelectionModel()
/*     */   {
/* 397 */     return this.selectionModel;
/*     */   }
/*     */ 
/*     */   public void setColumnSelectionAllowed(boolean paramBoolean)
/*     */   {
/* 406 */     this.columnSelectionAllowed = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getColumnSelectionAllowed()
/*     */   {
/* 416 */     return this.columnSelectionAllowed;
/*     */   }
/*     */ 
/*     */   public int[] getSelectedColumns()
/*     */   {
/* 428 */     if (this.selectionModel != null) {
/* 429 */       int i = this.selectionModel.getMinSelectionIndex();
/* 430 */       int j = this.selectionModel.getMaxSelectionIndex();
/*     */ 
/* 432 */       if ((i == -1) || (j == -1)) {
/* 433 */         return new int[0];
/*     */       }
/*     */ 
/* 436 */       int[] arrayOfInt1 = new int[1 + (j - i)];
/* 437 */       int k = 0;
/* 438 */       for (int m = i; m <= j; m++) {
/* 439 */         if (this.selectionModel.isSelectedIndex(m)) {
/* 440 */           arrayOfInt1[(k++)] = m;
/*     */         }
/*     */       }
/* 443 */       int[] arrayOfInt2 = new int[k];
/* 444 */       System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, k);
/* 445 */       return arrayOfInt2;
/*     */     }
/* 447 */     return new int[0];
/*     */   }
/*     */ 
/*     */   public int getSelectedColumnCount()
/*     */   {
/* 456 */     if (this.selectionModel != null) {
/* 457 */       int i = this.selectionModel.getMinSelectionIndex();
/* 458 */       int j = this.selectionModel.getMaxSelectionIndex();
/* 459 */       int k = 0;
/*     */ 
/* 461 */       for (int m = i; m <= j; m++) {
/* 462 */         if (this.selectionModel.isSelectedIndex(m)) {
/* 463 */           k++;
/*     */         }
/*     */       }
/* 466 */       return k;
/*     */     }
/* 468 */     return 0;
/*     */   }
/*     */ 
/*     */   public void addColumnModelListener(TableColumnModelListener paramTableColumnModelListener)
/*     */   {
/* 481 */     this.listenerList.add(TableColumnModelListener.class, paramTableColumnModelListener);
/*     */   }
/*     */ 
/*     */   public void removeColumnModelListener(TableColumnModelListener paramTableColumnModelListener)
/*     */   {
/* 490 */     this.listenerList.remove(TableColumnModelListener.class, paramTableColumnModelListener);
/*     */   }
/*     */ 
/*     */   public TableColumnModelListener[] getColumnModelListeners()
/*     */   {
/* 507 */     return (TableColumnModelListener[])this.listenerList.getListeners(TableColumnModelListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireColumnAdded(TableColumnModelEvent paramTableColumnModelEvent)
/*     */   {
/* 524 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 527 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 528 */       if (arrayOfObject[i] == TableColumnModelListener.class)
/*     */       {
/* 532 */         ((TableColumnModelListener)arrayOfObject[(i + 1)]).columnAdded(paramTableColumnModelEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireColumnRemoved(TableColumnModelEvent paramTableColumnModelEvent)
/*     */   {
/* 548 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 551 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 552 */       if (arrayOfObject[i] == TableColumnModelListener.class)
/*     */       {
/* 556 */         ((TableColumnModelListener)arrayOfObject[(i + 1)]).columnRemoved(paramTableColumnModelEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireColumnMoved(TableColumnModelEvent paramTableColumnModelEvent)
/*     */   {
/* 572 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 575 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 576 */       if (arrayOfObject[i] == TableColumnModelListener.class)
/*     */       {
/* 580 */         ((TableColumnModelListener)arrayOfObject[(i + 1)]).columnMoved(paramTableColumnModelEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireColumnSelectionChanged(ListSelectionEvent paramListSelectionEvent)
/*     */   {
/* 596 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 599 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 600 */       if (arrayOfObject[i] == TableColumnModelListener.class)
/*     */       {
/* 604 */         ((TableColumnModelListener)arrayOfObject[(i + 1)]).columnSelectionChanged(paramListSelectionEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireColumnMarginChanged()
/*     */   {
/* 619 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 622 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 623 */       if (arrayOfObject[i] == TableColumnModelListener.class)
/*     */       {
/* 625 */         if (this.changeEvent == null)
/* 626 */           this.changeEvent = new ChangeEvent(this);
/* 627 */         ((TableColumnModelListener)arrayOfObject[(i + 1)]).columnMarginChanged(this.changeEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 669 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 685 */     String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 687 */     if ((str == "width") || (str == "preferredWidth")) {
/* 688 */       invalidateWidthCache();
/*     */ 
/* 691 */       fireColumnMarginChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void valueChanged(ListSelectionEvent paramListSelectionEvent)
/*     */   {
/* 709 */     fireColumnSelectionChanged(paramListSelectionEvent);
/*     */   }
/*     */ 
/*     */   protected ListSelectionModel createSelectionModel()
/*     */   {
/* 720 */     return new DefaultListSelectionModel();
/*     */   }
/*     */ 
/*     */   protected void recalcWidthCache()
/*     */   {
/* 728 */     Enumeration localEnumeration = getColumns();
/* 729 */     this.totalColumnWidth = 0;
/* 730 */     while (localEnumeration.hasMoreElements())
/* 731 */       this.totalColumnWidth += ((TableColumn)localEnumeration.nextElement()).getWidth();
/*     */   }
/*     */ 
/*     */   private void invalidateWidthCache()
/*     */   {
/* 736 */     this.totalColumnWidth = -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.table.DefaultTableColumnModel
 * JD-Core Version:    0.6.2
 */