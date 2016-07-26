/*     */
package java.lang.ref;
/*     */ 
/*     */

import sun.misc.Cleaner;

/*     */
/*     */ public abstract class Reference<T>
/*     */ {
    /*     */   private T referent;
    /*     */ ReferenceQueue<? super T> queue;
    /*     */ Reference next;
    /*     */   private transient Reference<T> discovered;
    /* 104 */   private static Lock lock = new Lock(null);
    /*     */
/* 111 */   private static Reference pending = null;

    /*     */
/*     */
    public T get()
/*     */ {
/* 177 */
        return this.referent;
/*     */
    }

    /*     */
/*     */
    public void clear()
/*     */ {
/* 188 */
        this.referent = null;
/*     */
    }

    /*     */
/*     */
    public boolean isEnqueued()
/*     */ {
/* 206 */
        synchronized (this) {
/* 207 */
            return (this.queue != ReferenceQueue.NULL) && (this.next != null);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public boolean enqueue()
/*     */ {
/* 223 */
        return this.queue.enqueue(this);
/*     */
    }

    /*     */
/*     */   Reference(T paramT)
/*     */ {
/* 230 */
        this(paramT, null);
/*     */
    }

    /*     */
/*     */   Reference(T paramT, ReferenceQueue<? super T> paramReferenceQueue) {
/* 234 */
        this.referent = paramT;
/* 235 */
        this.queue = (paramReferenceQueue == null ? ReferenceQueue.NULL : paramReferenceQueue);
/*     */
    }

    /*     */
/*     */   static
/*     */ {
/* 152 */
        Object localObject1 = Thread.currentThread().getThreadGroup();
/* 153 */
        for (Object localObject2 = localObject1;
/* 154 */       localObject2 != null; 
/* 155 */       localObject2 = ((ThreadGroup) localObject1).getParent())
            localObject1 = localObject2;
/* 156 */
        localObject2 = new ReferenceHandler((ThreadGroup) localObject1, "Reference Handler");
/*     */ 
/* 160 */
        ((Thread) localObject2).setPriority(10);
/* 161 */
        ((Thread) localObject2).setDaemon(true);
/* 162 */
        ((Thread) localObject2).start();
/*     */
    }

    /*     */
/*     */   private static class Lock
/*     */ {
/*     */
    }

    /*     */
/*     */   private static class ReferenceHandler extends Thread
/*     */ {
        /*     */     ReferenceHandler(ThreadGroup paramThreadGroup, String paramString)
/*     */ {
/* 118 */
            super(paramString);
/*     */
        }

        /*     */
/*     */
        public void run()
/*     */ {
/*     */
            while (true)
/*     */ {
/*     */
                Reference localReference1;
/* 125 */
                synchronized (Reference.lock) {
/* 126 */
                    if (Reference.pending != null) {
/* 127 */
                        localReference1 = Reference.pending;
/* 128 */
                        Reference localReference2 = localReference1.next;
/* 129 */
                        Reference.access$202(localReference2 == localReference1 ? null : localReference2);
/* 130 */
                        localReference1.next = localReference1;
/*     */
                    } else {
/*     */
                        try {
/* 133 */
                            Reference.lock.wait();
                        } catch (InterruptedException localInterruptedException) {
/*     */
                        }
/* 135 */
                        continue;
/*     */
                    }
/*     */ 
/*     */
                }
/*     */ 
/* 140 */
                if ((localReference1 instanceof Cleaner)) {
/* 141 */
                    ((Cleaner) localReference1).clean();
/*     */
                }
/*     */
                else
/*     */ {
/* 145 */
                    ???=localReference1.queue;
/* 146 */
                    if (???!=ReferenceQueue.NULL)((ReferenceQueue) ? ??).enqueue(localReference1);
/*     */
                }
/*     */
            }
/*     */
        }
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ref.Reference
 * JD-Core Version:    0.6.2
 */