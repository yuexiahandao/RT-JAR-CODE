/*     */ package javax.swing.table;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.EventListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ import javax.swing.event.TableModelListener;
/*     */ 
/*     */ public abstract class AbstractTableModel
/*     */   implements TableModel, Serializable
/*     */ {
/*  67 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*     */   public String getColumnName(int paramInt)
/*     */   {
/*  82 */     String str = "";
/*  83 */     for (; paramInt >= 0; paramInt = paramInt / 26 - 1) {
/*  84 */       str = (char)((char)(paramInt % 26) + 'A') + str;
/*     */     }
/*  86 */     return str;
/*     */   }
/*     */ 
/*     */   public int findColumn(String paramString)
/*     */   {
/* 100 */     for (int i = 0; i < getColumnCount(); i++) {
/* 101 */       if (paramString.equals(getColumnName(i))) {
/* 102 */         return i;
/*     */       }
/*     */     }
/* 105 */     return -1;
/*     */   }
/*     */ 
/*     */   public Class<?> getColumnClass(int paramInt)
/*     */   {
/* 115 */     return Object.class;
/*     */   }
/*     */ 
/*     */   public boolean isCellEditable(int paramInt1, int paramInt2)
/*     */   {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   public void setValueAt(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void addTableModelListener(TableModelListener paramTableModelListener)
/*     */   {
/* 152 */     this.listenerList.add(TableModelListener.class, paramTableModelListener);
/*     */   }
/*     */ 
/*     */   public void removeTableModelListener(TableModelListener paramTableModelListener)
/*     */   {
/* 162 */     this.listenerList.remove(TableModelListener.class, paramTableModelListener);
/*     */   }
/*     */ 
/*     */   public TableModelListener[] getTableModelListeners()
/*     */   {
/* 179 */     return (TableModelListener[])this.listenerList.getListeners(TableModelListener.class);
/*     */   }
/*     */ 
/*     */   public void fireTableDataChanged()
/*     */   {
/* 198 */     fireTableChanged(new TableModelEvent(this));
/*     */   }
/*     */ 
/*     */   public void fireTableStructureChanged()
/*     */   {
/* 216 */     fireTableChanged(new TableModelEvent(this, -1));
/*     */   }
/*     */ 
/*     */   public void fireTableRowsInserted(int paramInt1, int paramInt2)
/*     */   {
/* 231 */     fireTableChanged(new TableModelEvent(this, paramInt1, paramInt2, -1, 1));
/*     */   }
/*     */ 
/*     */   public void fireTableRowsUpdated(int paramInt1, int paramInt2)
/*     */   {
/* 246 */     fireTableChanged(new TableModelEvent(this, paramInt1, paramInt2, -1, 0));
/*     */   }
/*     */ 
/*     */   public void fireTableRowsDeleted(int paramInt1, int paramInt2)
/*     */   {
/* 261 */     fireTableChanged(new TableModelEvent(this, paramInt1, paramInt2, -1, -1));
/*     */   }
/*     */ 
/*     */   public void fireTableCellUpdated(int paramInt1, int paramInt2)
/*     */   {
/* 275 */     fireTableChanged(new TableModelEvent(this, paramInt1, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public void fireTableChanged(TableModelEvent paramTableModelEvent)
/*     */   {
/* 291 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 294 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 295 */       if (arrayOfObject[i] == TableModelListener.class)
/* 296 */         ((TableModelListener)arrayOfObject[(i + 1)]).tableChanged(paramTableModelEvent);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 338 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.table.AbstractTableModel
 * JD-Core Version:    0.6.2
 */