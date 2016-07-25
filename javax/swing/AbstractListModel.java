/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.EventListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.event.ListDataEvent;
/*     */ import javax.swing.event.ListDataListener;
/*     */ 
/*     */ public abstract class AbstractListModel<E>
/*     */   implements ListModel<E>, Serializable
/*     */ {
/*  51 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*     */   public void addListDataListener(ListDataListener paramListDataListener)
/*     */   {
/*  61 */     this.listenerList.add(ListDataListener.class, paramListDataListener);
/*     */   }
/*     */ 
/*     */   public void removeListDataListener(ListDataListener paramListDataListener)
/*     */   {
/*  72 */     this.listenerList.remove(ListDataListener.class, paramListDataListener);
/*     */   }
/*     */ 
/*     */   public ListDataListener[] getListDataListeners()
/*     */   {
/*  90 */     return (ListDataListener[])this.listenerList.getListeners(ListDataListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireContentsChanged(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/* 110 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 111 */     ListDataEvent localListDataEvent = null;
/*     */ 
/* 113 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 114 */       if (arrayOfObject[i] == ListDataListener.class) {
/* 115 */         if (localListDataEvent == null) {
/* 116 */           localListDataEvent = new ListDataEvent(paramObject, 0, paramInt1, paramInt2);
/*     */         }
/* 118 */         ((ListDataListener)arrayOfObject[(i + 1)]).contentsChanged(localListDataEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireIntervalAdded(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/* 140 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 141 */     ListDataEvent localListDataEvent = null;
/*     */ 
/* 143 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 144 */       if (arrayOfObject[i] == ListDataListener.class) {
/* 145 */         if (localListDataEvent == null) {
/* 146 */           localListDataEvent = new ListDataEvent(paramObject, 1, paramInt1, paramInt2);
/*     */         }
/* 148 */         ((ListDataListener)arrayOfObject[(i + 1)]).intervalAdded(localListDataEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected void fireIntervalRemoved(Object paramObject, int paramInt1, int paramInt2)
/*     */   {
/* 171 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 172 */     ListDataEvent localListDataEvent = null;
/*     */ 
/* 174 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 175 */       if (arrayOfObject[i] == ListDataListener.class) {
/* 176 */         if (localListDataEvent == null) {
/* 177 */           localListDataEvent = new ListDataEvent(paramObject, 2, paramInt1, paramInt2);
/*     */         }
/* 179 */         ((ListDataListener)arrayOfObject[(i + 1)]).intervalRemoved(localListDataEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 220 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.AbstractListModel
 * JD-Core Version:    0.6.2
 */