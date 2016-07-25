/*     */ package javax.swing;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ 
/*     */ abstract class ActionPropertyChangeListener<T extends JComponent>
/*     */   implements PropertyChangeListener, Serializable
/*     */ {
/*     */   private static ReferenceQueue<JComponent> queue;
/*     */   private transient OwnedWeakReference<T> target;
/*     */   private Action action;
/*     */ 
/*     */   private static ReferenceQueue<JComponent> getQueue()
/*     */   {
/*  64 */     synchronized (ActionPropertyChangeListener.class) {
/*  65 */       if (queue == null) {
/*  66 */         queue = new ReferenceQueue();
/*     */       }
/*     */     }
/*  69 */     return queue;
/*     */   }
/*     */ 
/*     */   public ActionPropertyChangeListener(T paramT, Action paramAction)
/*     */   {
/*  74 */     setTarget(paramT);
/*  75 */     this.action = paramAction;
/*     */   }
/*     */ 
/*     */   public final void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/*  84 */     JComponent localJComponent = getTarget();
/*  85 */     if (localJComponent == null)
/*  86 */       getAction().removePropertyChangeListener(this);
/*     */     else
/*  88 */       actionPropertyChanged(localJComponent, getAction(), paramPropertyChangeEvent);
/*     */   }
/*     */ 
/*     */   protected abstract void actionPropertyChanged(T paramT, Action paramAction, PropertyChangeEvent paramPropertyChangeEvent);
/*     */ 
/*     */   private void setTarget(T paramT)
/*     */   {
/* 100 */     ReferenceQueue localReferenceQueue = getQueue();
/*     */     OwnedWeakReference localOwnedWeakReference;
/* 105 */     while ((localOwnedWeakReference = (OwnedWeakReference)localReferenceQueue.poll()) != null) {
/* 106 */       ActionPropertyChangeListener localActionPropertyChangeListener = localOwnedWeakReference.getOwner();
/* 107 */       Action localAction = localActionPropertyChangeListener.getAction();
/* 108 */       if (localAction != null) {
/* 109 */         localAction.removePropertyChangeListener(localActionPropertyChangeListener);
/*     */       }
/*     */     }
/* 112 */     this.target = new OwnedWeakReference(paramT, localReferenceQueue, this);
/*     */   }
/*     */ 
/*     */   public T getTarget() {
/* 116 */     if (this.target == null)
/*     */     {
/* 118 */       return null;
/*     */     }
/* 120 */     return (JComponent)this.target.get();
/*     */   }
/*     */ 
/*     */   public Action getAction() {
/* 124 */     return this.action;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 128 */     paramObjectOutputStream.defaultWriteObject();
/* 129 */     paramObjectOutputStream.writeObject(getTarget());
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 135 */     paramObjectInputStream.defaultReadObject();
/* 136 */     JComponent localJComponent = (JComponent)paramObjectInputStream.readObject();
/* 137 */     if (localJComponent != null)
/* 138 */       setTarget(localJComponent);
/*     */   }
/*     */ 
/*     */   private static class OwnedWeakReference<U extends JComponent> extends WeakReference<U>
/*     */   {
/*     */     private ActionPropertyChangeListener owner;
/*     */ 
/*     */     OwnedWeakReference(U paramU, ReferenceQueue<? super U> paramReferenceQueue, ActionPropertyChangeListener paramActionPropertyChangeListener)
/*     */     {
/* 149 */       super(paramReferenceQueue);
/* 150 */       this.owner = paramActionPropertyChangeListener;
/*     */     }
/*     */ 
/*     */     public ActionPropertyChangeListener getOwner() {
/* 154 */       return this.owner;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ActionPropertyChangeListener
 * JD-Core Version:    0.6.2
 */