/*     */ package sun.misc;
/*     */ 
/*     */ import java.lang.ref.PhantomReference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public class Cleaner extends PhantomReference
/*     */ {
/*  67 */   private static final ReferenceQueue dummyQueue = new ReferenceQueue();
/*     */ 
/*  72 */   private static Cleaner first = null;
/*     */ 
/*  74 */   private Cleaner next = null; private Cleaner prev = null;
/*     */   private final Runnable thunk;
/*     */ 
/*     */   private static synchronized Cleaner add(Cleaner paramCleaner)
/*     */   {
/*  79 */     if (first != null) {
/*  80 */       paramCleaner.next = first;
/*  81 */       first.prev = paramCleaner;
/*     */     }
/*  83 */     first = paramCleaner;
/*  84 */     return paramCleaner;
/*     */   }
/*     */ 
/*     */   private static synchronized boolean remove(Cleaner paramCleaner)
/*     */   {
/*  90 */     if (paramCleaner.next == paramCleaner) {
/*  91 */       return false;
/*     */     }
/*     */ 
/*  94 */     if (first == paramCleaner) {
/*  95 */       if (paramCleaner.next != null)
/*  96 */         first = paramCleaner.next;
/*     */       else
/*  98 */         first = paramCleaner.prev;
/*     */     }
/* 100 */     if (paramCleaner.next != null)
/* 101 */       paramCleaner.next.prev = paramCleaner.prev;
/* 102 */     if (paramCleaner.prev != null) {
/* 103 */       paramCleaner.prev.next = paramCleaner.next;
/*     */     }
/*     */ 
/* 106 */     paramCleaner.next = paramCleaner;
/* 107 */     paramCleaner.prev = paramCleaner;
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   private Cleaner(Object paramObject, Runnable paramRunnable)
/*     */   {
/* 115 */     super(paramObject, dummyQueue);
/* 116 */     this.thunk = paramRunnable;
/*     */   }
/*     */ 
/*     */   public static Cleaner create(Object paramObject, Runnable paramRunnable)
/*     */   {
/* 130 */     if (paramRunnable == null)
/* 131 */       return null;
/* 132 */     return add(new Cleaner(paramObject, paramRunnable));
/*     */   }
/*     */ 
/*     */   public void clean()
/*     */   {
/* 139 */     if (!remove(this))
/* 140 */       return;
/*     */     try {
/* 142 */       this.thunk.run();
/*     */     } catch (Throwable localThrowable) {
/* 144 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Void run() {
/* 146 */           if (System.err != null) {
/* 147 */             new Error("Cleaner terminated abnormally", localThrowable).printStackTrace();
/*     */           }
/* 149 */           System.exit(1);
/* 150 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.Cleaner
 * JD-Core Version:    0.6.2
 */