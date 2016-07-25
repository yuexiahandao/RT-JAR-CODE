/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.EventListener;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ public class DefaultSingleSelectionModel
/*     */   implements SingleSelectionModel, Serializable
/*     */ {
/*  52 */   protected transient ChangeEvent changeEvent = null;
/*     */ 
/*  54 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*  56 */   private int index = -1;
/*     */ 
/*     */   public int getSelectedIndex()
/*     */   {
/*  60 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setSelectedIndex(int paramInt)
/*     */   {
/*  65 */     if (this.index != paramInt) {
/*  66 */       this.index = paramInt;
/*  67 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clearSelection()
/*     */   {
/*  73 */     setSelectedIndex(-1);
/*     */   }
/*     */ 
/*     */   public boolean isSelected()
/*     */   {
/*  78 */     boolean bool = false;
/*  79 */     if (getSelectedIndex() != -1) {
/*  80 */       bool = true;
/*     */     }
/*  82 */     return bool;
/*     */   }
/*     */ 
/*     */   public void addChangeListener(ChangeListener paramChangeListener)
/*     */   {
/*  89 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public void removeChangeListener(ChangeListener paramChangeListener)
/*     */   {
/*  96 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public ChangeListener[] getChangeListeners()
/*     */   {
/* 113 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireStateChanged()
/*     */   {
/* 124 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 127 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 128 */       if (arrayOfObject[i] == ChangeListener.class)
/*     */       {
/* 130 */         if (this.changeEvent == null)
/* 131 */           this.changeEvent = new ChangeEvent(this);
/* 132 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 173 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultSingleSelectionModel
 * JD-Core Version:    0.6.2
 */