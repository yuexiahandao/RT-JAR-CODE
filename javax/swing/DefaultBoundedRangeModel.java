/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.EventListener;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ public class DefaultBoundedRangeModel
/*     */   implements BoundedRangeModel, Serializable
/*     */ {
/*  55 */   protected transient ChangeEvent changeEvent = null;
/*     */ 
/*  58 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/*  60 */   private int value = 0;
/*  61 */   private int extent = 0;
/*  62 */   private int min = 0;
/*  63 */   private int max = 100;
/*  64 */   private boolean isAdjusting = false;
/*     */ 
/*     */   public DefaultBoundedRangeModel()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DefaultBoundedRangeModel(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  92 */     if ((paramInt4 >= paramInt3) && (paramInt1 >= paramInt3) && (paramInt1 + paramInt2 >= paramInt1) && (paramInt1 + paramInt2 <= paramInt4))
/*     */     {
/*  96 */       this.value = paramInt1;
/*  97 */       this.extent = paramInt2;
/*  98 */       this.min = paramInt3;
/*  99 */       this.max = paramInt4;
/*     */     }
/*     */     else {
/* 102 */       throw new IllegalArgumentException("invalid range properties");
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getValue()
/*     */   {
/* 114 */     return this.value;
/*     */   }
/*     */ 
/*     */   public int getExtent()
/*     */   {
/* 125 */     return this.extent;
/*     */   }
/*     */ 
/*     */   public int getMinimum()
/*     */   {
/* 136 */     return this.min;
/*     */   }
/*     */ 
/*     */   public int getMaximum()
/*     */   {
/* 147 */     return this.max;
/*     */   }
/*     */ 
/*     */   public void setValue(int paramInt)
/*     */   {
/* 162 */     paramInt = Math.min(paramInt, 2147483647 - this.extent);
/*     */ 
/* 164 */     int i = Math.max(paramInt, this.min);
/* 165 */     if (i + this.extent > this.max) {
/* 166 */       i = this.max - this.extent;
/*     */     }
/* 168 */     setRangeProperties(i, this.extent, this.min, this.max, this.isAdjusting);
/*     */   }
/*     */ 
/*     */   public void setExtent(int paramInt)
/*     */   {
/* 182 */     int i = Math.max(0, paramInt);
/* 183 */     if (this.value + i > this.max) {
/* 184 */       i = this.max - this.value;
/*     */     }
/* 186 */     setRangeProperties(this.value, i, this.min, this.max, this.isAdjusting);
/*     */   }
/*     */ 
/*     */   public void setMinimum(int paramInt)
/*     */   {
/* 200 */     int i = Math.max(paramInt, this.max);
/* 201 */     int j = Math.max(paramInt, this.value);
/* 202 */     int k = Math.min(i - j, this.extent);
/* 203 */     setRangeProperties(j, k, paramInt, i, this.isAdjusting);
/*     */   }
/*     */ 
/*     */   public void setMaximum(int paramInt)
/*     */   {
/* 216 */     int i = Math.min(paramInt, this.min);
/* 217 */     int j = Math.min(paramInt - i, this.extent);
/* 218 */     int k = Math.min(paramInt - j, this.value);
/* 219 */     setRangeProperties(k, j, i, paramInt, this.isAdjusting);
/*     */   }
/*     */ 
/*     */   public void setValueIsAdjusting(boolean paramBoolean)
/*     */   {
/* 231 */     setRangeProperties(this.value, this.extent, this.min, this.max, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean getValueIsAdjusting()
/*     */   {
/* 244 */     return this.isAdjusting;
/*     */   }
/*     */ 
/*     */   public void setRangeProperties(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*     */   {
/* 266 */     if (paramInt3 > paramInt4) {
/* 267 */       paramInt3 = paramInt4;
/*     */     }
/* 269 */     if (paramInt1 > paramInt4) {
/* 270 */       paramInt4 = paramInt1;
/*     */     }
/* 272 */     if (paramInt1 < paramInt3) {
/* 273 */       paramInt3 = paramInt1;
/*     */     }
/*     */ 
/* 280 */     if (paramInt2 + paramInt1 > paramInt4) {
/* 281 */       paramInt2 = paramInt4 - paramInt1;
/*     */     }
/*     */ 
/* 284 */     if (paramInt2 < 0) {
/* 285 */       paramInt2 = 0;
/*     */     }
/*     */ 
/* 288 */     int i = (paramInt1 != this.value) || (paramInt2 != this.extent) || (paramInt3 != this.min) || (paramInt4 != this.max) || (paramBoolean != this.isAdjusting) ? 1 : 0;
/*     */ 
/* 295 */     if (i != 0) {
/* 296 */       this.value = paramInt1;
/* 297 */       this.extent = paramInt2;
/* 298 */       this.min = paramInt3;
/* 299 */       this.max = paramInt4;
/* 300 */       this.isAdjusting = paramBoolean;
/*     */ 
/* 302 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addChangeListener(ChangeListener paramChangeListener)
/*     */   {
/* 316 */     this.listenerList.add(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public void removeChangeListener(ChangeListener paramChangeListener)
/*     */   {
/* 328 */     this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*     */   }
/*     */ 
/*     */   public ChangeListener[] getChangeListeners()
/*     */   {
/* 346 */     return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireStateChanged()
/*     */   {
/* 358 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/* 359 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 360 */       if (arrayOfObject[i] == ChangeListener.class) {
/* 361 */         if (this.changeEvent == null) {
/* 362 */           this.changeEvent = new ChangeEvent(this);
/*     */         }
/* 364 */         ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*     */       }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 375 */     String str = "value=" + getValue() + ", " + "extent=" + getExtent() + ", " + "min=" + getMinimum() + ", " + "max=" + getMaximum() + ", " + "adj=" + getValueIsAdjusting();
/*     */ 
/* 382 */     return getClass().getName() + "[" + str + "]";
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 421 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DefaultBoundedRangeModel
 * JD-Core Version:    0.6.2
 */