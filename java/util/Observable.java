/*     */ package java.util;
/*     */ 
/*     */ public class Observable
/*     */ {
/*  63 */   private boolean changed = false;
/*     */   private Vector obs;
/*     */ 
/*     */   public Observable()
/*     */   {
/*  69 */     this.obs = new Vector();
/*     */   }
/*     */ 
/*     */   public synchronized void addObserver(Observer paramObserver)
/*     */   {
/*  82 */     if (paramObserver == null)
/*  83 */       throw new NullPointerException();
/*  84 */     if (!this.obs.contains(paramObserver))
/*  85 */       this.obs.addElement(paramObserver);
/*     */   }
/*     */ 
/*     */   public synchronized void deleteObserver(Observer paramObserver)
/*     */   {
/*  95 */     this.obs.removeElement(paramObserver);
/*     */   }
/*     */ 
/*     */   public void notifyObservers()
/*     */   {
/* 115 */     notifyObservers(null);
/*     */   }
/*     */ 
/*     */   public void notifyObservers(Object paramObject)
/*     */   {
/*     */     Object[] arrayOfObject;
/* 139 */     synchronized (this)
/*     */     {
/* 152 */       if (!this.changed)
/* 153 */         return;
/* 154 */       arrayOfObject = this.obs.toArray();
/* 155 */       clearChanged();
/*     */     }
/*     */ 
/* 158 */     for (int i = arrayOfObject.length - 1; i >= 0; i--)
/* 159 */       ((Observer)arrayOfObject[i]).update(this, paramObject);
/*     */   }
/*     */ 
/*     */   public synchronized void deleteObservers()
/*     */   {
/* 166 */     this.obs.removeAllElements();
/*     */   }
/*     */ 
/*     */   protected synchronized void setChanged()
/*     */   {
/* 174 */     this.changed = true;
/*     */   }
/*     */ 
/*     */   protected synchronized void clearChanged()
/*     */   {
/* 188 */     this.changed = false;
/*     */   }
/*     */ 
/*     */   public synchronized boolean hasChanged()
/*     */   {
/* 202 */     return this.changed;
/*     */   }
/*     */ 
/*     */   public synchronized int countObservers()
/*     */   {
/* 211 */     return this.obs.size();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.Observable
 * JD-Core Version:    0.6.2
 */