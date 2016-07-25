/*     */ package javax.swing;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.RowSorterEvent;
/*     */ import javax.swing.event.RowSorterEvent.Type;
/*     */ import javax.swing.event.RowSorterListener;
/*     */ 
/*     */ public abstract class RowSorter<M>
/*     */ {
/*  96 */   private EventListenerList listenerList = new EventListenerList();
/*     */ 
/*     */   public abstract M getModel();
/*     */ 
/*     */   public abstract void toggleSortOrder(int paramInt);
/*     */ 
/*     */   public abstract int convertRowIndexToModel(int paramInt);
/*     */ 
/*     */   public abstract int convertRowIndexToView(int paramInt);
/*     */ 
/*     */   public abstract void setSortKeys(List<? extends SortKey> paramList);
/*     */ 
/*     */   public abstract List<? extends SortKey> getSortKeys();
/*     */ 
/*     */   public abstract int getViewRowCount();
/*     */ 
/*     */   public abstract int getModelRowCount();
/*     */ 
/*     */   public abstract void modelStructureChanged();
/*     */ 
/*     */   public abstract void allRowsChanged();
/*     */ 
/*     */   public abstract void rowsInserted(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract void rowsDeleted(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract void rowsUpdated(int paramInt1, int paramInt2);
/*     */ 
/*     */   public abstract void rowsUpdated(int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   public void addRowSorterListener(RowSorterListener paramRowSorterListener)
/*     */   {
/* 305 */     this.listenerList.add(RowSorterListener.class, paramRowSorterListener);
/*     */   }
/*     */ 
/*     */   public void removeRowSorterListener(RowSorterListener paramRowSorterListener)
/*     */   {
/* 315 */     this.listenerList.remove(RowSorterListener.class, paramRowSorterListener);
/*     */   }
/*     */ 
/*     */   protected void fireSortOrderChanged()
/*     */   {
/* 322 */     fireRowSorterChanged(new RowSorterEvent(this));
/*     */   }
/*     */ 
/*     */   protected void fireRowSorterChanged(int[] paramArrayOfInt)
/*     */   {
/* 332 */     fireRowSorterChanged(new RowSorterEvent(this, RowSorterEvent.Type.SORTED, paramArrayOfInt));
/*     */   }
/*     */ 
/*     */   void fireRowSorterChanged(RowSorterEvent paramRowSorterEvent)
/*     */   {
/* 337 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 338 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 339 */       if (arrayOfObject[i] == RowSorterListener.class)
/* 340 */         ((RowSorterListener)arrayOfObject[(i + 1)]).sorterChanged(paramRowSorterEvent);
/*     */   }
/*     */ 
/*     */   public static class SortKey
/*     */   {
/*     */     private int column;
/*     */     private SortOrder sortOrder;
/*     */ 
/*     */     public SortKey(int paramInt, SortOrder paramSortOrder)
/*     */     {
/* 367 */       if (paramSortOrder == null) {
/* 368 */         throw new IllegalArgumentException("sort order must be non-null");
/*     */       }
/*     */ 
/* 371 */       this.column = paramInt;
/* 372 */       this.sortOrder = paramSortOrder;
/*     */     }
/*     */ 
/*     */     public final int getColumn()
/*     */     {
/* 381 */       return this.column;
/*     */     }
/*     */ 
/*     */     public final SortOrder getSortOrder()
/*     */     {
/* 390 */       return this.sortOrder;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 399 */       int i = 17;
/* 400 */       i = 37 * i + this.column;
/* 401 */       i = 37 * i + this.sortOrder.hashCode();
/* 402 */       return i;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 415 */       if (paramObject == this) {
/* 416 */         return true;
/*     */       }
/* 418 */       if ((paramObject instanceof SortKey)) {
/* 419 */         return (((SortKey)paramObject).column == this.column) && (((SortKey)paramObject).sortOrder == this.sortOrder);
/*     */       }
/*     */ 
/* 422 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.RowSorter
 * JD-Core Version:    0.6.2
 */