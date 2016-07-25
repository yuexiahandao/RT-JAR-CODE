/*     */ package java.lang.ref;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ import sun.misc.VM;
/*     */ 
/*     */ final class Finalizer extends FinalReference
/*     */ {
/*  38 */   private static ReferenceQueue queue = new ReferenceQueue();
/*  39 */   private static Finalizer unfinalized = null;
/*  40 */   private static final Object lock = new Object();
/*     */ 
/*  42 */   private Finalizer next = null; private Finalizer prev = null;
/*     */ 
/*     */   private boolean hasBeenFinalized()
/*     */   {
/*  47 */     return this.next == this;
/*     */   }
/*     */ 
/*     */   private void add() {
/*  51 */     synchronized (lock) {
/*  52 */       if (unfinalized != null) {
/*  53 */         this.next = unfinalized;
/*  54 */         unfinalized.prev = this;
/*     */       }
/*  56 */       unfinalized = this;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void remove() {
/*  61 */     synchronized (lock) {
/*  62 */       if (unfinalized == this) {
/*  63 */         if (this.next != null)
/*  64 */           unfinalized = this.next;
/*     */         else {
/*  66 */           unfinalized = this.prev;
/*     */         }
/*     */       }
/*  69 */       if (this.next != null) {
/*  70 */         this.next.prev = this.prev;
/*     */       }
/*  72 */       if (this.prev != null) {
/*  73 */         this.prev.next = this.next;
/*     */       }
/*  75 */       this.next = this;
/*  76 */       this.prev = this;
/*     */     }
/*     */   }
/*     */ 
/*     */   private Finalizer(Object paramObject) {
/*  81 */     super(paramObject, queue);
/*  82 */     add();
/*     */   }
/*     */ 
/*     */   static void register(Object paramObject)
/*     */   {
/*  87 */     new Finalizer(paramObject);
/*     */   }
/*     */ 
/*     */   private void runFinalizer(JavaLangAccess paramJavaLangAccess) {
/*  91 */     synchronized (this) {
/*  92 */       if (hasBeenFinalized()) return;
/*  93 */       remove();
/*     */     }
/*     */     try {
/*  96 */       ??? = get();
/*  97 */       if ((??? != null) && (!(??? instanceof Enum))) {
/*  98 */         paramJavaLangAccess.invokeFinalize(???);
/*     */ 
/* 102 */         ??? = null;
/*     */       }
/*     */     } catch (Throwable localThrowable) {  }
/*     */ 
/* 105 */     super.clear();
/*     */   }
/*     */ 
/*     */   private static void forkSecondaryFinalizer(Runnable paramRunnable)
/*     */   {
/* 122 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run() {
/* 125 */         Object localObject1 = Thread.currentThread().getThreadGroup();
/* 126 */         for (Object localObject2 = localObject1; 
/* 127 */           localObject2 != null; 
/* 128 */           localObject2 = ((ThreadGroup)localObject1).getParent()) localObject1 = localObject2;
/* 129 */         localObject2 = new Thread((ThreadGroup)localObject1, this.val$proc, "Secondary finalizer");
/* 130 */         ((Thread)localObject2).start();
/*     */         try {
/* 132 */           ((Thread)localObject2).join();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {
/*     */         }
/* 136 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static void runFinalization() {
/* 142 */     if (!VM.isBooted()) {
/* 143 */       return;
/*     */     }
/*     */ 
/* 146 */     forkSecondaryFinalizer(new Runnable() {
/*     */       private volatile boolean running;
/*     */ 
/* 149 */       public void run() { if (this.running)
/* 150 */           return;
/* 151 */         JavaLangAccess localJavaLangAccess = SharedSecrets.getJavaLangAccess();
/* 152 */         this.running = true;
/*     */         while (true) {
/* 154 */           Finalizer localFinalizer = (Finalizer)Finalizer.queue.poll();
/* 155 */           if (localFinalizer == null) break;
/* 156 */           localFinalizer.runFinalizer(localJavaLangAccess);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static void runAllFinalizers()
/*     */   {
/* 164 */     if (!VM.isBooted()) {
/* 165 */       return;
/*     */     }
/*     */ 
/* 168 */     forkSecondaryFinalizer(new Runnable() {
/*     */       private volatile boolean running;
/*     */ 
/* 171 */       public void run() { if (this.running)
/* 172 */           return;
/* 173 */         JavaLangAccess localJavaLangAccess = SharedSecrets.getJavaLangAccess();
/* 174 */         this.running = true;
/*     */         while (true)
/*     */         {
/*     */           Finalizer localFinalizer;
/* 177 */           synchronized (Finalizer.lock) {
/* 178 */             localFinalizer = Finalizer.unfinalized;
/* 179 */             if (localFinalizer == null) break;
/* 180 */             Finalizer.access$302(localFinalizer.next);
/*     */           }
/* 182 */           localFinalizer.runFinalizer(localJavaLangAccess);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 219 */     Object localObject1 = Thread.currentThread().getThreadGroup();
/* 220 */     for (Object localObject2 = localObject1; 
/* 221 */       localObject2 != null; 
/* 222 */       localObject2 = ((ThreadGroup)localObject1).getParent()) localObject1 = localObject2;
/* 223 */     localObject2 = new FinalizerThread((ThreadGroup)localObject1);
/* 224 */     ((Thread)localObject2).setPriority(8);
/* 225 */     ((Thread)localObject2).setDaemon(true);
/* 226 */     ((Thread)localObject2).start();
/*     */   }
/*     */ 
/*     */   private static class FinalizerThread extends Thread
/*     */   {
/*     */     private volatile boolean running;
/*     */ 
/*     */     FinalizerThread(ThreadGroup paramThreadGroup)
/*     */     {
/* 189 */       super("Finalizer");
/*     */     }
/*     */     public void run() {
/* 192 */       if (this.running) {
/* 193 */         return;
/*     */       }
/*     */ 
/* 197 */       while (!VM.isBooted())
/*     */         try
/*     */         {
/* 200 */           VM.awaitBooted();
/*     */         }
/*     */         catch (InterruptedException localInterruptedException1)
/*     */         {
/*     */         }
/* 205 */       JavaLangAccess localJavaLangAccess = SharedSecrets.getJavaLangAccess();
/* 206 */       this.running = true;
/*     */       while (true)
/*     */         try {
/* 209 */           Finalizer localFinalizer = (Finalizer)Finalizer.queue.remove();
/* 210 */           localFinalizer.runFinalizer(localJavaLangAccess);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException2)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ref.Finalizer
 * JD-Core Version:    0.6.2
 */