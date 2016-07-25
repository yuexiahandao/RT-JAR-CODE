/*     */ package sun.management;
/*     */ 
/*     */ import java.lang.management.MemoryUsage;
/*     */ 
/*     */ public abstract class Sensor
/*     */ {
/*     */   private Object lock;
/*     */   private String name;
/*     */   private long count;
/*     */   private boolean on;
/*     */ 
/*     */   public Sensor(String paramString)
/*     */   {
/*  62 */     this.name = paramString;
/*  63 */     this.count = 0L;
/*  64 */     this.on = false;
/*  65 */     this.lock = new Object();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  74 */     return this.name;
/*     */   }
/*     */ 
/*     */   public long getCount()
/*     */   {
/*  83 */     synchronized (this.lock) {
/*  84 */       return this.count;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isOn()
/*     */   {
/*  96 */     synchronized (this.lock) {
/*  97 */       return this.on;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void trigger()
/*     */   {
/* 106 */     synchronized (this.lock) {
/* 107 */       this.on = true;
/* 108 */       this.count += 1L;
/*     */     }
/* 110 */     triggerAction();
/*     */   }
/*     */ 
/*     */   public void trigger(int paramInt)
/*     */   {
/* 118 */     synchronized (this.lock) {
/* 119 */       this.on = true;
/* 120 */       this.count += paramInt;
/*     */     }
/*     */ 
/* 123 */     triggerAction();
/*     */   }
/*     */ 
/*     */   public void trigger(int paramInt, MemoryUsage paramMemoryUsage)
/*     */   {
/* 132 */     synchronized (this.lock) {
/* 133 */       this.on = true;
/* 134 */       this.count += paramInt;
/*     */     }
/*     */ 
/* 137 */     triggerAction(paramMemoryUsage);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 144 */     synchronized (this.lock) {
/* 145 */       this.on = false;
/*     */     }
/* 147 */     clearAction();
/*     */   }
/*     */ 
/*     */   public void clear(int paramInt)
/*     */   {
/* 156 */     synchronized (this.lock) {
/* 157 */       this.on = false;
/* 158 */       this.count += paramInt;
/*     */     }
/* 160 */     clearAction();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 164 */     return "Sensor - " + getName() + (isOn() ? " on " : " off ") + " count = " + getCount();
/*     */   }
/*     */ 
/*     */   abstract void triggerAction();
/*     */ 
/*     */   abstract void triggerAction(MemoryUsage paramMemoryUsage);
/*     */ 
/*     */   abstract void clearAction();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.Sensor
 * JD-Core Version:    0.6.2
 */