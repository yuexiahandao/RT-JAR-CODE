/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.EventListener;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ public abstract class AbstractSpinnerModel
/*     */   implements SpinnerModel, Serializable
/*     */ {
/*  57 */   private transient ChangeEvent changeEvent = null;
/*     */ 
/*  64 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*     */   public void addChangeListener(ChangeListener paramChangeListener)
/*     */   {
/*  76 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public void removeChangeListener(ChangeListener paramChangeListener)
/*     */   {
/*  88 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public ChangeListener[] getChangeListeners()
/*     */   {
/* 101 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireStateChanged()
/*     */   {
/* 113 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 114 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 115 */       if (arrayOfObject[i] == ChangeListener.class) {
/* 116 */         if (this.changeEvent == null) {
/* 117 */           this.changeEvent = new ChangeEvent(this);
/*     */         }
/* 119 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 138 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.AbstractSpinnerModel
 * JD-Core Version:    0.6.2
 */