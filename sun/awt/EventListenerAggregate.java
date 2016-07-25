/*     */ package sun.awt;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.EventListener;
/*     */ 
/*     */ public class EventListenerAggregate
/*     */ {
/*     */   private EventListener[] listenerList;
/*     */ 
/*     */   public EventListenerAggregate(Class paramClass)
/*     */   {
/*  57 */     if (paramClass == null) {
/*  58 */       throw new NullPointerException("listener class is null");
/*     */     }
/*     */ 
/*  61 */     if (!EventListener.class.isAssignableFrom(paramClass)) {
/*  62 */       throw new ClassCastException("listener class " + paramClass + " is not assignable to EventListener");
/*     */     }
/*     */ 
/*  66 */     this.listenerList = ((EventListener[])Array.newInstance(paramClass, 0));
/*     */   }
/*     */ 
/*     */   private Class getListenerClass() {
/*  70 */     return this.listenerList.getClass().getComponentType();
/*     */   }
/*     */ 
/*     */   public synchronized void add(EventListener paramEventListener)
/*     */   {
/*  83 */     Class localClass = getListenerClass();
/*     */ 
/*  85 */     if (!localClass.isInstance(paramEventListener)) {
/*  86 */       throw new ClassCastException("listener " + paramEventListener + " is not " + "an instance of listener class " + localClass);
/*     */     }
/*     */ 
/*  90 */     EventListener[] arrayOfEventListener = (EventListener[])Array.newInstance(localClass, this.listenerList.length + 1);
/*  91 */     System.arraycopy(this.listenerList, 0, arrayOfEventListener, 0, this.listenerList.length);
/*  92 */     arrayOfEventListener[this.listenerList.length] = paramEventListener;
/*  93 */     this.listenerList = arrayOfEventListener;
/*     */   }
/*     */ 
/*     */   public synchronized boolean remove(EventListener paramEventListener)
/*     */   {
/* 110 */     Class localClass = getListenerClass();
/*     */ 
/* 112 */     if (!localClass.isInstance(paramEventListener)) {
/* 113 */       throw new ClassCastException("listener " + paramEventListener + " is not " + "an instance of listener class " + localClass);
/*     */     }
/*     */ 
/* 117 */     for (int i = 0; i < this.listenerList.length; i++) {
/* 118 */       if (this.listenerList[i].equals(paramEventListener)) {
/* 119 */         EventListener[] arrayOfEventListener = (EventListener[])Array.newInstance(localClass, this.listenerList.length - 1);
/*     */ 
/* 121 */         System.arraycopy(this.listenerList, 0, arrayOfEventListener, 0, i);
/* 122 */         System.arraycopy(this.listenerList, i + 1, arrayOfEventListener, i, this.listenerList.length - i - 1);
/* 123 */         this.listenerList = arrayOfEventListener;
/*     */ 
/* 125 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized EventListener[] getListenersInternal()
/*     */   {
/* 143 */     return this.listenerList;
/*     */   }
/*     */ 
/*     */   public synchronized EventListener[] getListenersCopy()
/*     */   {
/* 158 */     return this.listenerList.length == 0 ? this.listenerList : (EventListener[])this.listenerList.clone();
/*     */   }
/*     */ 
/*     */   public synchronized int size()
/*     */   {
/* 167 */     return this.listenerList.length;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isEmpty()
/*     */   {
/* 178 */     return this.listenerList.length == 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.EventListenerAggregate
 * JD-Core Version:    0.6.2
 */