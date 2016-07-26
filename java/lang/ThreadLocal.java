/*     */
package java.lang;
/*     */ 
/*     */

import java.lang.ref.WeakReference;
/*     */ import java.util.concurrent.atomic.AtomicInteger;

/*     */
/*     */ public class ThreadLocal<T>
/*     */ {
    /*  83 */   private final int threadLocalHashCode = nextHashCode();
    /*     */
/*  89 */   private static AtomicInteger nextHashCode = new AtomicInteger();
    /*     */   private static final int HASH_INCREMENT = 1640531527;

    /*     */
/*     */
    private static int nextHashCode()
/*     */ {
/* 103 */
        return nextHashCode.getAndAdd(1640531527);
/*     */
    }

    /*     */
/*     */
    protected T initialValue()
/*     */ {
/* 125 */
        return null;
/*     */
    }

    /*     */
/*     */
    public T get()
/*     */ {
/* 143 */
        Thread localThread = Thread.currentThread();
/* 144 */
        ThreadLocalMap localThreadLocalMap = getMap(localThread);
/* 145 */
        if (localThreadLocalMap != null) {
/* 146 */
            ThreadLocal.ThreadLocalMap.Entry localEntry = localThreadLocalMap.getEntry(this);
/* 147 */
            if (localEntry != null)
/* 148 */ return localEntry.value;
/*     */
        }
/* 150 */
        return setInitialValue();
/*     */
    }

    /*     */
/*     */
    private T setInitialValue()
/*     */ {
/* 160 */
        Object localObject = initialValue();
/* 161 */
        Thread localThread = Thread.currentThread();
/* 162 */
        ThreadLocalMap localThreadLocalMap = getMap(localThread);
/* 163 */
        if (localThreadLocalMap != null)
/* 164 */ localThreadLocalMap.set(this, localObject);
/*     */
        else
/* 166 */       createMap(localThread, localObject);
/* 167 */
        return localObject;
/*     */
    }

    /*     */
/*     */
    public void set(T paramT)
/*     */ {
/* 180 */
        Thread localThread = Thread.currentThread();
/* 181 */
        ThreadLocalMap localThreadLocalMap = getMap(localThread);
/* 182 */
        if (localThreadLocalMap != null)
/* 183 */ localThreadLocalMap.set(this, paramT);
/*     */
        else
/* 185 */       createMap(localThread, paramT);
/*     */
    }

    /*     */
/*     */
    public void remove()
/*     */ {
/* 200 */
        ThreadLocalMap localThreadLocalMap = getMap(Thread.currentThread());
/* 201 */
        if (localThreadLocalMap != null)
/* 202 */ localThreadLocalMap.remove(this);
/*     */
    }

    /*     */
/*     */   ThreadLocalMap getMap(Thread paramThread)
/*     */ {
/* 213 */
        return paramThread.threadLocals;
/*     */
    }

    /*     */
/*     */   void createMap(Thread paramThread, T paramT)
/*     */ {
/* 225 */
        paramThread.threadLocals = new ThreadLocalMap(this, paramT);
/*     */
    }

    /*     */
/*     */
    static ThreadLocalMap createInheritedMap(ThreadLocalMap paramThreadLocalMap)
/*     */ {
/* 236 */
        return new ThreadLocalMap(paramThreadLocalMap, null);
/*     */
    }

    /*     */
/*     */   T childValue(T paramT)
/*     */ {
/* 248 */
        throw new UnsupportedOperationException();
/*     */
    }

    /*     */
/*     */   static class ThreadLocalMap
/*     */ {
        /*     */     private static final int INITIAL_CAPACITY = 16;
        /*     */     private Entry[] table;
        /* 295 */     private int size = 0;
        /*     */     private int threshold;

        /*     */
/*     */
        private void setThreshold(int paramInt)
/*     */ {
/* 306 */
            this.threshold = (paramInt * 2 / 3);
/*     */
        }

        /*     */
/*     */
        private static int nextIndex(int paramInt1, int paramInt2)
/*     */ {
/* 313 */
            return paramInt1 + 1 < paramInt2 ? paramInt1 + 1 : 0;
/*     */
        }

        /*     */
/*     */
        private static int prevIndex(int paramInt1, int paramInt2)
/*     */ {
/* 320 */
            return paramInt1 - 1 >= 0 ? paramInt1 - 1 : paramInt2 - 1;
/*     */
        }

        /*     */
/*     */     ThreadLocalMap(ThreadLocal paramThreadLocal, Object paramObject)
/*     */ {
/* 329 */
            this.table = new Entry[16];
/* 330 */
            int i = paramThreadLocal.threadLocalHashCode & 0xF;
/* 331 */
            this.table[i] = new Entry(paramThreadLocal, paramObject);
/* 332 */
            this.size = 1;
/* 333 */
            setThreshold(16);
/*     */
        }

        /*     */
/*     */
        private ThreadLocalMap(ThreadLocalMap paramThreadLocalMap)
/*     */ {
/* 343 */
            Entry[] arrayOfEntry = paramThreadLocalMap.table;
/* 344 */
            int i = arrayOfEntry.length;
/* 345 */
            setThreshold(i);
/* 346 */
            this.table = new Entry[i];
/*     */ 
/* 348 */
            for (int j = 0; j < i; j++) {
/* 349 */
                Entry localEntry1 = arrayOfEntry[j];
/* 350 */
                if (localEntry1 != null) {
/* 351 */
                    ThreadLocal localThreadLocal = (ThreadLocal) localEntry1.get();
/* 352 */
                    if (localThreadLocal != null) {
/* 353 */
                        Object localObject = localThreadLocal.childValue(localEntry1.value);
/* 354 */
                        Entry localEntry2 = new Entry(localThreadLocal, localObject);
/* 355 */
                        int k = localThreadLocal.threadLocalHashCode & i - 1;
/* 356 */
                        while (this.table[k] != null)
/* 357 */ k = nextIndex(k, i);
/* 358 */
                        this.table[k] = localEntry2;
/* 359 */
                        this.size += 1;
/*     */
                    }
/*     */
                }
/*     */
            }
/*     */
        }

        /*     */
/*     */
        private Entry getEntry(ThreadLocal paramThreadLocal)
/*     */ {
/* 376 */
            int i = paramThreadLocal.threadLocalHashCode & this.table.length - 1;
/* 377 */
            Entry localEntry = this.table[i];
/* 378 */
            if ((localEntry != null) && (localEntry.get() == paramThreadLocal)) {
/* 379 */
                return localEntry;
/*     */
            }
/* 381 */
            return getEntryAfterMiss(paramThreadLocal, i, localEntry);
/*     */
        }

        /*     */
/*     */
        private Entry getEntryAfterMiss(ThreadLocal paramThreadLocal, int paramInt, Entry paramEntry)
/*     */ {
/* 394 */
            Entry[] arrayOfEntry = this.table;
/* 395 */
            int i = arrayOfEntry.length;
/*     */ 
/* 397 */
            while (paramEntry != null) {
/* 398 */
                ThreadLocal localThreadLocal = (ThreadLocal) paramEntry.get();
/* 399 */
                if (localThreadLocal == paramThreadLocal)
/* 400 */ return paramEntry;
/* 401 */
                if (localThreadLocal == null)
/* 402 */ expungeStaleEntry(paramInt);
/*     */
                else
/* 404 */           paramInt = nextIndex(paramInt, i);
/* 405 */
                paramEntry = arrayOfEntry[paramInt];
/*     */
            }
/* 407 */
            return null;
/*     */
        }

        /*     */
/*     */
        private void set(ThreadLocal paramThreadLocal, Object paramObject)
/*     */ {
/* 423 */
            Entry[] arrayOfEntry = this.table;
/* 424 */
            int i = arrayOfEntry.length;
/* 425 */
            int j = paramThreadLocal.threadLocalHashCode & i - 1;
/*     */ 
/* 427 */
            for (Entry localEntry = arrayOfEntry[j];
/* 428 */         localEntry != null; 
/* 429 */         localEntry = arrayOfEntry[(j = nextIndex(j, i))]) {
/* 430 */
                ThreadLocal localThreadLocal = (ThreadLocal) localEntry.get();
/*     */ 
/* 432 */
                if (localThreadLocal == paramThreadLocal) {
/* 433 */
                    localEntry.value = paramObject;
/* 434 */
                    return;
/*     */
                }
/*     */ 
/* 437 */
                if (localThreadLocal == null) {
/* 438 */
                    replaceStaleEntry(paramThreadLocal, paramObject, j);
/* 439 */
                    return;
/*     */
                }
/*     */
            }
/*     */ 
/* 443 */
            arrayOfEntry[j] = new Entry(paramThreadLocal, paramObject);
/* 444 */
            int k = ++this.size;
/* 445 */
            if ((!cleanSomeSlots(j, k)) && (k >= this.threshold))
/* 446 */ rehash();
/*     */
        }

        /*     */
/*     */
        private void remove(ThreadLocal paramThreadLocal)
/*     */ {
/* 453 */
            Entry[] arrayOfEntry = this.table;
/* 454 */
            int i = arrayOfEntry.length;
/* 455 */
            int j = paramThreadLocal.threadLocalHashCode & i - 1;
/* 456 */
            for (Entry localEntry = arrayOfEntry[j];
/* 457 */         localEntry != null; 
/* 458 */         localEntry = arrayOfEntry[(j = nextIndex(j, i))])
/* 459 */
                if (localEntry.get() == paramThreadLocal) {
/* 460 */
                    localEntry.clear();
/* 461 */
                    expungeStaleEntry(j);
/* 462 */
                    return;
/*     */
                }
/*     */
        }

        /*     */
/*     */
        private void replaceStaleEntry(ThreadLocal paramThreadLocal, Object paramObject, int paramInt)
/*     */ {
/* 484 */
            Entry[] arrayOfEntry = this.table;
/* 485 */
            int i = arrayOfEntry.length;
/*     */ 
/* 492 */
            int j = paramInt;
/*     */
            Entry localEntry;
/* 493 */
            for (int k = prevIndex(paramInt, i);
/* 494 */         (localEntry = arrayOfEntry[k]) != null; 
/* 495 */         k = prevIndex(k, i)) {
/* 496 */
                if (localEntry.get() == null) {
/* 497 */
                    j = k;
/*     */
                }
/*     */
            }
/*     */ 
/* 501 */
            for (k = nextIndex(paramInt, i);
/* 502 */         (localEntry = arrayOfEntry[k]) != null; 
/* 503 */         k = nextIndex(k, i)) {
/* 504 */
                ThreadLocal localThreadLocal = (ThreadLocal) localEntry.get();
/*     */ 
/* 511 */
                if (localThreadLocal == paramThreadLocal) {
/* 512 */
                    localEntry.value = paramObject;
/*     */ 
/* 514 */
                    arrayOfEntry[k] = arrayOfEntry[paramInt];
/* 515 */
                    arrayOfEntry[paramInt] = localEntry;
/*     */ 
/* 518 */
                    if (j == paramInt)
/* 519 */ j = k;
/* 520 */
                    cleanSomeSlots(expungeStaleEntry(j), i);
/* 521 */
                    return;
/*     */
                }
/*     */ 
/* 527 */
                if ((localThreadLocal == null) && (j == paramInt)) {
/* 528 */
                    j = k;
/*     */
                }
/*     */
            }
/*     */ 
/* 532 */
            arrayOfEntry[paramInt].value = null;
/* 533 */
            arrayOfEntry[paramInt] = new Entry(paramThreadLocal, paramObject);
/*     */ 
/* 536 */
            if (j != paramInt)
/* 537 */ cleanSomeSlots(expungeStaleEntry(j), i);
/*     */
        }

        /*     */
/*     */
        private int expungeStaleEntry(int paramInt)
/*     */ {
/* 552 */
            Entry[] arrayOfEntry = this.table;
/* 553 */
            int i = arrayOfEntry.length;
/*     */ 
/* 556 */
            arrayOfEntry[paramInt].value = null;
/* 557 */
            arrayOfEntry[paramInt] = null;
/* 558 */
            this.size -= 1;
/*     */
            Entry localEntry;
/* 563 */
            for (int j = nextIndex(paramInt, i);
/* 564 */         (localEntry = arrayOfEntry[j]) != null; 
/* 565 */         j = nextIndex(j, i)) {
/* 566 */
                ThreadLocal localThreadLocal = (ThreadLocal) localEntry.get();
/* 567 */
                if (localThreadLocal == null) {
/* 568 */
                    localEntry.value = null;
/* 569 */
                    arrayOfEntry[j] = null;
/* 570 */
                    this.size -= 1;
/*     */
                } else {
/* 572 */
                    int k = localThreadLocal.threadLocalHashCode & i - 1;
/* 573 */
                    if (k != j) {
/* 574 */
                        arrayOfEntry[j] = null;
/*     */ 
/* 578 */
                        while (arrayOfEntry[k] != null)
/* 579 */ k = nextIndex(k, i);
/* 580 */
                        arrayOfEntry[k] = localEntry;
/*     */
                    }
/*     */
                }
/*     */
            }
/* 584 */
            return j;
/*     */
        }

        /*     */
/*     */
        private boolean cleanSomeSlots(int paramInt1, int paramInt2)
/*     */ {
/* 612 */
            boolean bool = false;
/* 613 */
            Entry[] arrayOfEntry = this.table;
/* 614 */
            int i = arrayOfEntry.length;
/*     */
            do {
/* 616 */
                paramInt1 = nextIndex(paramInt1, i);
/* 617 */
                Entry localEntry = arrayOfEntry[paramInt1];
/* 618 */
                if ((localEntry != null) && (localEntry.get() == null)) {
/* 619 */
                    paramInt2 = i;
/* 620 */
                    bool = true;
/* 621 */
                    paramInt1 = expungeStaleEntry(paramInt1);
/*     */
                }
/*     */
            }
/* 623 */       while (paramInt2 >>>= 1 != 0);
/* 624 */
            return bool;
/*     */
        }

        /*     */
/*     */
        private void rehash()
/*     */ {
/* 633 */
            expungeStaleEntries();
/*     */ 
/* 636 */
            if (this.size >= this.threshold - this.threshold / 4)
/* 637 */ resize();
/*     */
        }

        /*     */
/*     */
        private void resize()
/*     */ {
/* 644 */
            Entry[] arrayOfEntry1 = this.table;
/* 645 */
            int i = arrayOfEntry1.length;
/* 646 */
            int j = i * 2;
/* 647 */
            Entry[] arrayOfEntry2 = new Entry[j];
/* 648 */
            int k = 0;
/*     */ 
/* 650 */
            for (int m = 0; m < i; m++) {
/* 651 */
                Entry localEntry = arrayOfEntry1[m];
/* 652 */
                if (localEntry != null) {
/* 653 */
                    ThreadLocal localThreadLocal = (ThreadLocal) localEntry.get();
/* 654 */
                    if (localThreadLocal == null) {
/* 655 */
                        localEntry.value = null;
/*     */
                    } else {
/* 657 */
                        int n = localThreadLocal.threadLocalHashCode & j - 1;
/* 658 */
                        while (arrayOfEntry2[n] != null)
/* 659 */ n = nextIndex(n, j);
/* 660 */
                        arrayOfEntry2[n] = localEntry;
/* 661 */
                        k++;
/*     */
                    }
/*     */
                }
/*     */
            }
/*     */ 
/* 666 */
            setThreshold(j);
/* 667 */
            this.size = k;
/* 668 */
            this.table = arrayOfEntry2;
/*     */
        }

        /*     */
/*     */
        private void expungeStaleEntries()
/*     */ {
/* 675 */
            Entry[] arrayOfEntry = this.table;
/* 676 */
            int i = arrayOfEntry.length;
/* 677 */
            for (int j = 0; j < i; j++) {
/* 678 */
                Entry localEntry = arrayOfEntry[j];
/* 679 */
                if ((localEntry != null) && (localEntry.get() == null))
/* 680 */ expungeStaleEntry(j);
/*     */
            }
/*     */
        }

        /*     */
/*     */     static class Entry extends WeakReference<ThreadLocal>
/*     */ {
            /*     */ Object value;

            /*     */
/*     */       Entry(ThreadLocal paramThreadLocal, Object paramObject)
/*     */ {
/* 276 */
                super();
/* 277 */
                this.value = paramObject;
/*     */
            }
/*     */
        }
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ThreadLocal
 * JD-Core Version:    0.6.2
 */