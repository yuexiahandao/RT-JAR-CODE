/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.EventObject;
/*     */ import javax.swing.event.CellEditorListener;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ public abstract class AbstractCellEditor
/*     */   implements CellEditor, Serializable
/*     */ {
/*  56 */   protected EventListenerList listenerList = new EventListenerList();
/*  57 */   protected transient ChangeEvent changeEvent = null;
/*     */ 
/*     */   public boolean isCellEditable(EventObject paramEventObject)
/*     */   {
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean shouldSelectCell(EventObject paramEventObject)
/*     */   {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean stopCellEditing()
/*     */   {
/*  85 */     fireEditingStopped();
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   public void cancelCellEditing()
/*     */   {
/*  93 */     fireEditingCanceled();
/*     */   }
/*     */ 
/*     */   public void addCellEditorListener(CellEditorListener paramCellEditorListener)
/*     */   {
/* 101 */     this.listenerList.add(CellEditorListener.class, paramCellEditorListener);
/*     */   }
/*     */ 
/*     */   public void removeCellEditorListener(CellEditorListener paramCellEditorListener)
/*     */   {
/* 109 */     this.listenerList.remove(CellEditorListener.class, paramCellEditorListener);
/*     */   }
/*     */ 
/*     */   public CellEditorListener[] getCellEditorListeners()
/*     */   {
/* 121 */     return (CellEditorListener[])this.listenerList.getListeners(CellEditorListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireEditingStopped()
/*     */   {
/* 133 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 136 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 137 */       if (arrayOfObject[i] == CellEditorListener.class)
/*     */       {
/* 139 */         if (this.changeEvent == null)
/* 140 */           this.changeEvent = new ChangeEvent(this);
/* 141 */         ((CellEditorListener)arrayOfObject[(i + 1)]).editingStopped(this.changeEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireEditingCanceled()
/*     */   {
/* 155 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 158 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 159 */       if (arrayOfObject[i] == CellEditorListener.class)
/*     */       {
/* 161 */         if (this.changeEvent == null)
/* 162 */           this.changeEvent = new ChangeEvent(this);
/* 163 */         ((CellEditorListener)arrayOfObject[(i + 1)]).editingCanceled(this.changeEvent);
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.AbstractCellEditor
 * JD-Core Version:    0.6.2
 */