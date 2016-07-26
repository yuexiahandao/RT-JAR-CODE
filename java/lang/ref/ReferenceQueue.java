/*     */
package java.lang.ref;
/*     */ 
/*     */

import sun.misc.VM;

/*     */
/*     */ public class ReferenceQueue<T>
/*     */ {
    /*  49 */   static ReferenceQueue NULL = new Null(null);
    /*  50 */   static ReferenceQueue ENQUEUED = new Null(null);
    /*     */
/*  53 */   private Lock lock = new Lock(null);
    /*  54 */   private volatile Reference<? extends T> head = null;
    /*  55 */   private long queueLength = 0L;

    /*     */
/*     */   boolean enqueue(Reference<? extends T> paramReference) {
/*  58 */
        synchronized (paramReference) {
/*  59 */
            if (paramReference.queue == ENQUEUED) return false;
/*  60 */
            synchronized (this.lock) {
/*  61 */
                paramReference.queue = ENQUEUED;
/*  62 */
                paramReference.next = (this.head == null ? paramReference : this.head);
/*  63 */
                this.head = paramReference;
/*  64 */
                this.queueLength += 1L;
/*  65 */
                if ((paramReference instanceof FinalReference)) {
/*  66 */
                    VM.addFinalRefCount(1);
/*     */
                }
/*  68 */
                this.lock.notifyAll();
/*  69 */
                return true;
/*     */
            }
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private Reference<? extends T> reallyPoll() {
/*  75 */
        if (this.head != null) {
/*  76 */
            Reference localReference = this.head;
/*  77 */
            this.head = (localReference.next == localReference ? null : localReference.next);
/*  78 */
            localReference.queue = NULL;
/*  79 */
            localReference.next = localReference;
/*  80 */
            this.queueLength -= 1L;
/*  81 */
            if ((localReference instanceof FinalReference)) {
/*  82 */
                VM.addFinalRefCount(-1);
/*     */
            }
/*  84 */
            return localReference;
/*     */
        }
/*  86 */
        return null;
/*     */
    }

    /*     */
/*     */
    public Reference<? extends T> poll()
/*     */ {
/*  98 */
        if (this.head == null)
/*  99 */ return null;
/* 100 */
        synchronized (this.lock) {
/* 101 */
            return reallyPoll();
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public Reference<? extends T> remove(long paramLong)
/*     */     throws IllegalArgumentException, InterruptedException
/*     */ {
/* 128 */
        if (paramLong < 0L) {
/* 129 */
            throw new IllegalArgumentException("Negative timeout value");
/*     */
        }
/* 131 */
        synchronized (this.lock) {
/* 132 */
            Reference localReference = reallyPoll();
/* 133 */
            if (localReference != null) return localReference;
            do
/*     */ {
/* 135 */
                this.lock.wait(paramLong);
/* 136 */
                localReference = reallyPoll();
/* 137 */
                if (localReference != null) return localReference;
/*     */
            }
/* 138 */       while (paramLong == 0L);
            return null;
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public Reference<? extends T> remove()
/*     */     throws InterruptedException
/*     */ {
/* 151 */
        return remove(0L);
/*     */
    }

    /*     */
/*     */   private static class Lock
/*     */ {
/*     */
    }

    /*     */
/*     */   private static class Null extends ReferenceQueue
/*     */ {
        /*     */     boolean enqueue(Reference paramReference)
/*     */ {
/*  45 */
            return false;
/*     */
        }
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ref.ReferenceQueue
 * JD-Core Version:    0.6.2
 */