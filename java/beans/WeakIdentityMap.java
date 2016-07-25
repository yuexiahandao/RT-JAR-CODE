/*     */ package java.beans;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ 
/*     */ abstract class WeakIdentityMap<T>
/*     */ {
/*     */   private static final int MAXIMUM_CAPACITY = 1073741824;
/*  47 */   private static final Object NULL = new Object();
/*     */   private final ReferenceQueue<Object> queue;
/*     */   private volatile Entry<T>[] table;
/*     */   private int threshold;
/*     */   private int size;
/*     */ 
/*     */   WeakIdentityMap()
/*     */   {
/*  49 */     this.queue = new ReferenceQueue();
/*     */ 
/*  51 */     this.table = newTable(8);
/*  52 */     this.threshold = 6;
/*  53 */     this.size = 0;
/*     */   }
/*     */   public T get(Object paramObject) {
/*  56 */     removeStaleEntries();
/*  57 */     if (paramObject == null) {
/*  58 */       paramObject = NULL;
/*     */     }
/*  60 */     int i = paramObject.hashCode();
/*  61 */     Entry[] arrayOfEntry = this.table;
/*     */ 
/*  64 */     int j = getIndex(arrayOfEntry, i);
/*  65 */     for (Entry localEntry = arrayOfEntry[j]; localEntry != null; localEntry = localEntry.next) {
/*  66 */       if (localEntry.isMatched(paramObject, i)) {
/*  67 */         return localEntry.value;
/*     */       }
/*     */     }
/*  70 */     synchronized (NULL)
/*     */     {
/*  73 */       j = getIndex(this.table, i);
/*  74 */       for (Object localObject1 = this.table[j]; localObject1 != null; localObject1 = ((Entry)localObject1).next) {
/*  75 */         if (((Entry)localObject1).isMatched(paramObject, i)) {
/*  76 */           return ((Entry)localObject1).value;
/*     */         }
/*     */       }
/*  79 */       localObject1 = create(paramObject);
/*  80 */       this.table[j] = new Entry(paramObject, i, localObject1, this.queue, this.table[j]);
/*  81 */       if (++this.size >= this.threshold) {
/*  82 */         if (this.table.length == 1073741824) {
/*  83 */           this.threshold = 2147483647;
/*     */         }
/*     */         else {
/*  86 */           removeStaleEntries();
/*  87 */           arrayOfEntry = newTable(this.table.length * 2);
/*  88 */           transfer(this.table, arrayOfEntry);
/*     */ 
/*  92 */           if (this.size >= this.threshold / 2) {
/*  93 */             this.table = arrayOfEntry;
/*  94 */             this.threshold *= 2;
/*     */           }
/*     */           else {
/*  97 */             transfer(arrayOfEntry, this.table);
/*     */           }
/*     */         }
/*     */       }
/* 101 */       return localObject1;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract T create(Object paramObject);
/*     */ 
/*     */   private void removeStaleEntries() {
/* 108 */     Reference localReference = this.queue.poll();
/* 109 */     if (localReference != null)
/* 110 */       synchronized (NULL)
/*     */       {
/*     */         do {
/* 113 */           Entry localEntry1 = (Entry)localReference;
/* 114 */           int i = getIndex(this.table, localEntry1.hash);
/*     */ 
/* 116 */           Object localObject1 = this.table[i];
/* 117 */           Object localObject2 = localObject1;
/* 118 */           while (localObject2 != null) {
/* 119 */             Entry localEntry2 = localObject2.next;
/* 120 */             if (localObject2 == localEntry1) {
/* 121 */               if (localObject1 == localEntry1) {
/* 122 */                 this.table[i] = localEntry2;
/*     */               }
/*     */               else {
/* 125 */                 ((Entry)localObject1).next = localEntry2;
/*     */               }
/* 127 */               localEntry1.value = null;
/* 128 */               localEntry1.next = null;
/* 129 */               this.size -= 1;
/* 130 */               break;
/*     */             }
/* 132 */             localObject1 = localObject2;
/* 133 */             localObject2 = localEntry2;
/*     */           }
/* 135 */           localReference = this.queue.poll();
/*     */         }
/* 137 */         while (localReference != null);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void transfer(Entry<T>[] paramArrayOfEntry1, Entry<T>[] paramArrayOfEntry2)
/*     */   {
/* 143 */     for (int i = 0; i < paramArrayOfEntry1.length; i++) {
/* 144 */       Object localObject1 = paramArrayOfEntry1[i];
/* 145 */       paramArrayOfEntry1[i] = null;
/* 146 */       while (localObject1 != null) {
/* 147 */         Entry localEntry = ((Entry)localObject1).next;
/* 148 */         Object localObject2 = ((Entry)localObject1).get();
/* 149 */         if (localObject2 == null) {
/* 150 */           ((Entry)localObject1).value = null;
/* 151 */           ((Entry)localObject1).next = null;
/* 152 */           this.size -= 1;
/*     */         }
/*     */         else {
/* 155 */           int j = getIndex(paramArrayOfEntry2, ((Entry)localObject1).hash);
/* 156 */           ((Entry)localObject1).next = paramArrayOfEntry2[j];
/* 157 */           paramArrayOfEntry2[j] = localObject1;
/*     */         }
/* 159 */         localObject1 = localEntry;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private Entry<T>[] newTable(int paramInt)
/*     */   {
/* 167 */     return (Entry[])new Entry[paramInt];
/*     */   }
/*     */ 
/*     */   private static int getIndex(Entry<?>[] paramArrayOfEntry, int paramInt) {
/* 171 */     return paramInt & paramArrayOfEntry.length - 1;
/*     */   }
/*     */   private static class Entry<T> extends WeakReference<Object> {
/*     */     private final int hash;
/*     */     private volatile T value;
/*     */     private volatile Entry<T> next;
/*     */ 
/* 180 */     Entry(Object paramObject, int paramInt, T paramT, ReferenceQueue<Object> paramReferenceQueue, Entry<T> paramEntry) { super(paramReferenceQueue);
/* 181 */       this.hash = paramInt;
/* 182 */       this.value = paramT;
/* 183 */       this.next = paramEntry; }
/*     */ 
/*     */     boolean isMatched(Object paramObject, int paramInt)
/*     */     {
/* 187 */       return (this.hash == paramInt) && (paramObject == get());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.WeakIdentityMap
 * JD-Core Version:    0.6.2
 */