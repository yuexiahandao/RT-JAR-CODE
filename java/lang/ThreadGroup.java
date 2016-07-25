/*      */ package java.lang;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import java.util.Arrays;
/*      */ import sun.misc.VM;
/*      */ 
/*      */ public class ThreadGroup
/*      */   implements Thread.UncaughtExceptionHandler
/*      */ {
/*      */   private final ThreadGroup parent;
/*      */   String name;
/*      */   int maxPriority;
/*      */   boolean destroyed;
/*      */   boolean daemon;
/*      */   boolean vmAllowSuspension;
/*   65 */   int nUnstartedThreads = 0;
/*      */   int nthreads;
/*      */   Thread[] threads;
/*      */   int ngroups;
/*      */   ThreadGroup[] groups;
/*      */ 
/*      */   private ThreadGroup()
/*      */   {
/*   77 */     this.name = "system";
/*   78 */     this.maxPriority = 10;
/*   79 */     this.parent = null;
/*      */   }
/*      */ 
/*      */   public ThreadGroup(String paramString)
/*      */   {
/*   96 */     this(Thread.currentThread().getThreadGroup(), paramString);
/*      */   }
/*      */ 
/*      */   public ThreadGroup(ThreadGroup paramThreadGroup, String paramString)
/*      */   {
/*  117 */     this(checkParentAccess(paramThreadGroup), paramThreadGroup, paramString);
/*      */   }
/*      */ 
/*      */   private ThreadGroup(Void paramVoid, ThreadGroup paramThreadGroup, String paramString) {
/*  121 */     this.name = paramString;
/*  122 */     this.maxPriority = paramThreadGroup.maxPriority;
/*  123 */     this.daemon = paramThreadGroup.daemon;
/*  124 */     this.vmAllowSuspension = paramThreadGroup.vmAllowSuspension;
/*  125 */     this.parent = paramThreadGroup;
/*  126 */     paramThreadGroup.add(this);
/*      */   }
/*      */ 
/*      */   private static Void checkParentAccess(ThreadGroup paramThreadGroup)
/*      */   {
/*  135 */     paramThreadGroup.checkAccess();
/*  136 */     return null;
/*      */   }
/*      */ 
/*      */   public final String getName()
/*      */   {
/*  146 */     return this.name;
/*      */   }
/*      */ 
/*      */   public final ThreadGroup getParent()
/*      */   {
/*  166 */     if (this.parent != null)
/*  167 */       this.parent.checkAccess();
/*  168 */     return this.parent;
/*      */   }
/*      */ 
/*      */   public final int getMaxPriority()
/*      */   {
/*  182 */     return this.maxPriority;
/*      */   }
/*      */ 
/*      */   public final boolean isDaemon()
/*      */   {
/*  195 */     return this.daemon;
/*      */   }
/*      */ 
/*      */   public synchronized boolean isDestroyed()
/*      */   {
/*  205 */     return this.destroyed;
/*      */   }
/*      */ 
/*      */   public final void setDaemon(boolean paramBoolean)
/*      */   {
/*  227 */     checkAccess();
/*  228 */     this.daemon = paramBoolean;
/*      */   }
/*      */ 
/*      */   public final void setMaxPriority(int paramInt)
/*      */   {
/*      */     Object localObject1;
/*      */     ThreadGroup[] arrayOfThreadGroup;
/*  262 */     synchronized (this) {
/*  263 */       checkAccess();
/*  264 */       if ((paramInt < 1) || (paramInt > 10)) {
/*  265 */         return;
/*      */       }
/*  267 */       this.maxPriority = (this.parent != null ? Math.min(paramInt, this.parent.maxPriority) : paramInt);
/*  268 */       localObject1 = this.ngroups;
/*  269 */       if (this.groups != null)
/*  270 */         arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */       else {
/*  272 */         arrayOfThreadGroup = null;
/*      */       }
/*      */     }
/*  275 */     for (??? = 0; ??? < localObject1; ???++)
/*  276 */       arrayOfThreadGroup[???].setMaxPriority(paramInt);
/*      */   }
/*      */ 
/*      */   public final boolean parentOf(ThreadGroup paramThreadGroup)
/*      */   {
/*  291 */     for (; paramThreadGroup != null; paramThreadGroup = paramThreadGroup.parent) {
/*  292 */       if (paramThreadGroup == this) {
/*  293 */         return true;
/*      */       }
/*      */     }
/*  296 */     return false;
/*      */   }
/*      */ 
/*      */   public final void checkAccess()
/*      */   {
/*  313 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  314 */     if (localSecurityManager != null)
/*  315 */       localSecurityManager.checkAccess(this);
/*      */   }
/*      */ 
/*      */   public int activeCount()
/*      */   {
/*      */     int i;
/*      */     Object localObject1;
/*      */     ThreadGroup[] arrayOfThreadGroup;
/*  342 */     synchronized (this) {
/*  343 */       if (this.destroyed) {
/*  344 */         return 0;
/*      */       }
/*  346 */       i = this.nthreads;
/*  347 */       localObject1 = this.ngroups;
/*  348 */       if (this.groups != null)
/*  349 */         arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */       else {
/*  351 */         arrayOfThreadGroup = null;
/*      */       }
/*      */     }
/*  354 */     for (??? = 0; ??? < localObject1; ???++) {
/*  355 */       i += arrayOfThreadGroup[???].activeCount();
/*      */     }
/*  357 */     return i;
/*      */   }
/*      */ 
/*      */   public int enumerate(Thread[] paramArrayOfThread)
/*      */   {
/*  383 */     checkAccess();
/*  384 */     return enumerate(paramArrayOfThread, 0, true);
/*      */   }
/*      */ 
/*      */   public int enumerate(Thread[] paramArrayOfThread, boolean paramBoolean)
/*      */   {
/*  421 */     checkAccess();
/*  422 */     return enumerate(paramArrayOfThread, 0, paramBoolean);
/*      */   }
/*      */ 
/*      */   private int enumerate(Thread[] paramArrayOfThread, int paramInt, boolean paramBoolean) {
/*  426 */     Object localObject1 = 0;
/*  427 */     ThreadGroup[] arrayOfThreadGroup = null;
/*  428 */     synchronized (this) {
/*  429 */       if (this.destroyed) {
/*  430 */         return 0;
/*      */       }
/*  432 */       int i = this.nthreads;
/*  433 */       if (i > paramArrayOfThread.length - paramInt) {
/*  434 */         i = paramArrayOfThread.length - paramInt;
/*      */       }
/*  436 */       for (int j = 0; j < i; j++) {
/*  437 */         if (this.threads[j].isAlive()) {
/*  438 */           paramArrayOfThread[(paramInt++)] = this.threads[j];
/*      */         }
/*      */       }
/*  441 */       if (paramBoolean) {
/*  442 */         localObject1 = this.ngroups;
/*  443 */         if (this.groups != null)
/*  444 */           arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */         else {
/*  446 */           arrayOfThreadGroup = null;
/*      */         }
/*      */       }
/*      */     }
/*  450 */     if (paramBoolean) {
/*  451 */       for (??? = 0; ??? < localObject1; ???++) {
/*  452 */         paramInt = arrayOfThreadGroup[???].enumerate(paramArrayOfThread, paramInt, true);
/*      */       }
/*      */     }
/*  455 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public int activeGroupCount()
/*      */   {
/*      */     Object localObject1;
/*      */     ThreadGroup[] arrayOfThreadGroup;
/*  476 */     synchronized (this) {
/*  477 */       if (this.destroyed) {
/*  478 */         return 0;
/*      */       }
/*  480 */       localObject1 = this.ngroups;
/*  481 */       if (this.groups != null)
/*  482 */         arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */       else {
/*  484 */         arrayOfThreadGroup = null;
/*      */       }
/*      */     }
/*  487 */     ??? = localObject1;
/*      */     int i;
/*  488 */     for (int j = 0; j < localObject1; j++) {
/*  489 */       ??? += arrayOfThreadGroup[j].activeGroupCount();
/*      */     }
/*  491 */     return i;
/*      */   }
/*      */ 
/*      */   public int enumerate(ThreadGroup[] paramArrayOfThreadGroup)
/*      */   {
/*  517 */     checkAccess();
/*  518 */     return enumerate(paramArrayOfThreadGroup, 0, true);
/*      */   }
/*      */ 
/*      */   public int enumerate(ThreadGroup[] paramArrayOfThreadGroup, boolean paramBoolean)
/*      */   {
/*  555 */     checkAccess();
/*  556 */     return enumerate(paramArrayOfThreadGroup, 0, paramBoolean);
/*      */   }
/*      */ 
/*      */   private int enumerate(ThreadGroup[] paramArrayOfThreadGroup, int paramInt, boolean paramBoolean) {
/*  560 */     Object localObject1 = 0;
/*  561 */     ThreadGroup[] arrayOfThreadGroup = null;
/*  562 */     synchronized (this) {
/*  563 */       if (this.destroyed) {
/*  564 */         return 0;
/*      */       }
/*  566 */       int i = this.ngroups;
/*  567 */       if (i > paramArrayOfThreadGroup.length - paramInt) {
/*  568 */         i = paramArrayOfThreadGroup.length - paramInt;
/*      */       }
/*  570 */       if (i > 0) {
/*  571 */         System.arraycopy(this.groups, 0, paramArrayOfThreadGroup, paramInt, i);
/*  572 */         paramInt += i;
/*      */       }
/*  574 */       if (paramBoolean) {
/*  575 */         localObject1 = this.ngroups;
/*  576 */         if (this.groups != null)
/*  577 */           arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */         else {
/*  579 */           arrayOfThreadGroup = null;
/*      */         }
/*      */       }
/*      */     }
/*  583 */     if (paramBoolean) {
/*  584 */       for (??? = 0; ??? < localObject1; ???++) {
/*  585 */         paramInt = arrayOfThreadGroup[???].enumerate(paramArrayOfThreadGroup, paramInt, true);
/*      */       }
/*      */     }
/*  588 */     return paramInt;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public final void stop()
/*      */   {
/*  612 */     if (stopOrSuspend(false))
/*  613 */       Thread.currentThread().stop();
/*      */   }
/*      */ 
/*      */   public final void interrupt()
/*      */   {
/*      */     Object localObject1;
/*      */     ThreadGroup[] arrayOfThreadGroup;
/*  636 */     synchronized (this) {
/*  637 */       checkAccess();
/*  638 */       for (int i = 0; i < this.nthreads; i++) {
/*  639 */         this.threads[i].interrupt();
/*      */       }
/*  641 */       localObject1 = this.ngroups;
/*  642 */       if (this.groups != null)
/*  643 */         arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */       else {
/*  645 */         arrayOfThreadGroup = null;
/*      */       }
/*      */     }
/*  648 */     for (??? = 0; ??? < localObject1; ???++)
/*  649 */       arrayOfThreadGroup[???].interrupt();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public final void suspend()
/*      */   {
/*  674 */     if (stopOrSuspend(true))
/*  675 */       Thread.currentThread().suspend();
/*      */   }
/*      */ 
/*      */   private boolean stopOrSuspend(boolean paramBoolean)
/*      */   {
/*  686 */     boolean bool = false;
/*  687 */     Thread localThread = Thread.currentThread();
/*      */ 
/*  689 */     ThreadGroup[] arrayOfThreadGroup = null;
/*      */     Object localObject1;
/*  690 */     synchronized (this) {
/*  691 */       checkAccess();
/*  692 */       for (int i = 0; i < this.nthreads; i++) {
/*  693 */         if (this.threads[i] == localThread)
/*  694 */           bool = true;
/*  695 */         else if (paramBoolean)
/*  696 */           this.threads[i].suspend();
/*      */         else {
/*  698 */           this.threads[i].stop();
/*      */         }
/*      */       }
/*  701 */       localObject1 = this.ngroups;
/*  702 */       if (this.groups != null) {
/*  703 */         arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */       }
/*      */     }
/*  706 */     for (??? = 0; ??? < localObject1; ???++) {
/*  707 */       bool = (arrayOfThreadGroup[???].stopOrSuspend(paramBoolean)) || (bool);
/*      */     }
/*  709 */     return bool;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public final void resume()
/*      */   {
/*      */     Object localObject1;
/*      */     ThreadGroup[] arrayOfThreadGroup;
/*  737 */     synchronized (this) {
/*  738 */       checkAccess();
/*  739 */       for (int i = 0; i < this.nthreads; i++) {
/*  740 */         this.threads[i].resume();
/*      */       }
/*  742 */       localObject1 = this.ngroups;
/*  743 */       if (this.groups != null)
/*  744 */         arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */       else {
/*  746 */         arrayOfThreadGroup = null;
/*      */       }
/*      */     }
/*  749 */     for (??? = 0; ??? < localObject1; ???++)
/*  750 */       arrayOfThreadGroup[???].resume();
/*      */   }
/*      */ 
/*      */   public final void destroy()
/*      */   {
/*      */     Object localObject1;
/*      */     ThreadGroup[] arrayOfThreadGroup;
/*  772 */     synchronized (this) {
/*  773 */       checkAccess();
/*  774 */       if ((this.destroyed) || (this.nthreads > 0)) {
/*  775 */         throw new IllegalThreadStateException();
/*      */       }
/*  777 */       localObject1 = this.ngroups;
/*  778 */       if (this.groups != null)
/*  779 */         arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */       else {
/*  781 */         arrayOfThreadGroup = null;
/*      */       }
/*  783 */       if (this.parent != null) {
/*  784 */         this.destroyed = true;
/*  785 */         this.ngroups = 0;
/*  786 */         this.groups = null;
/*  787 */         this.nthreads = 0;
/*  788 */         this.threads = null;
/*      */       }
/*      */     }
/*  791 */     for (??? = 0; ??? < localObject1; ???++) {
/*  792 */       arrayOfThreadGroup[???].destroy();
/*      */     }
/*  794 */     if (this.parent != null)
/*  795 */       this.parent.remove(this);
/*      */   }
/*      */ 
/*      */   private final void add(ThreadGroup paramThreadGroup)
/*      */   {
/*  805 */     synchronized (this) {
/*  806 */       if (this.destroyed) {
/*  807 */         throw new IllegalThreadStateException();
/*      */       }
/*  809 */       if (this.groups == null)
/*  810 */         this.groups = new ThreadGroup[4];
/*  811 */       else if (this.ngroups == this.groups.length) {
/*  812 */         this.groups = ((ThreadGroup[])Arrays.copyOf(this.groups, this.ngroups * 2));
/*      */       }
/*  814 */       this.groups[this.ngroups] = paramThreadGroup;
/*      */ 
/*  818 */       this.ngroups += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void remove(ThreadGroup paramThreadGroup)
/*      */   {
/*  828 */     synchronized (this) {
/*  829 */       if (this.destroyed) {
/*  830 */         return;
/*      */       }
/*  832 */       for (int i = 0; i < this.ngroups; i++) {
/*  833 */         if (this.groups[i] == paramThreadGroup) {
/*  834 */           this.ngroups -= 1;
/*  835 */           System.arraycopy(this.groups, i + 1, this.groups, i, this.ngroups - i);
/*      */ 
/*  838 */           this.groups[this.ngroups] = null;
/*  839 */           break;
/*      */         }
/*      */       }
/*  842 */       if (this.nthreads == 0) {
/*  843 */         notifyAll();
/*      */       }
/*  845 */       if ((this.daemon) && (this.nthreads == 0) && (this.nUnstartedThreads == 0) && (this.ngroups == 0))
/*      */       {
/*  848 */         destroy();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void addUnstarted()
/*      */   {
/*  862 */     synchronized (this) {
/*  863 */       if (this.destroyed) {
/*  864 */         throw new IllegalThreadStateException();
/*      */       }
/*  866 */       this.nUnstartedThreads += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   void add(Thread paramThread)
/*      */   {
/*  884 */     synchronized (this) {
/*  885 */       if (this.destroyed) {
/*  886 */         throw new IllegalThreadStateException();
/*      */       }
/*  888 */       if (this.threads == null)
/*  889 */         this.threads = new Thread[4];
/*  890 */       else if (this.nthreads == this.threads.length) {
/*  891 */         this.threads = ((Thread[])Arrays.copyOf(this.threads, this.nthreads * 2));
/*      */       }
/*  893 */       this.threads[this.nthreads] = paramThread;
/*      */ 
/*  897 */       this.nthreads += 1;
/*      */ 
/*  903 */       this.nUnstartedThreads -= 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   void threadStartFailed(Thread paramThread)
/*      */   {
/*  923 */     synchronized (this) {
/*  924 */       remove(paramThread);
/*  925 */       this.nUnstartedThreads += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   void threadTerminated(Thread paramThread)
/*      */   {
/*  941 */     synchronized (this) {
/*  942 */       remove(paramThread);
/*      */ 
/*  944 */       if (this.nthreads == 0) {
/*  945 */         notifyAll();
/*      */       }
/*  947 */       if ((this.daemon) && (this.nthreads == 0) && (this.nUnstartedThreads == 0) && (this.ngroups == 0))
/*      */       {
/*  950 */         destroy();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void remove(Thread paramThread)
/*      */   {
/*  963 */     synchronized (this) {
/*  964 */       if (this.destroyed) {
/*  965 */         return;
/*      */       }
/*  967 */       for (int i = 0; i < this.nthreads; i++)
/*  968 */         if (this.threads[i] == paramThread) {
/*  969 */           System.arraycopy(this.threads, i + 1, this.threads, i, --this.nthreads - i);
/*      */ 
/*  972 */           this.threads[this.nthreads] = null;
/*  973 */           break;
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void list()
/*      */   {
/*  986 */     list(System.out, 0);
/*      */   }
/*      */ 
/*      */   void list(PrintStream paramPrintStream, int paramInt)
/*      */   {
/*      */     Object localObject1;
/*      */     ThreadGroup[] arrayOfThreadGroup;
/*  991 */     synchronized (this) {
/*  992 */       for (int i = 0; i < paramInt; i++) {
/*  993 */         paramPrintStream.print(" ");
/*      */       }
/*  995 */       paramPrintStream.println(this);
/*  996 */       paramInt += 4;
/*  997 */       for (i = 0; i < this.nthreads; i++) {
/*  998 */         for (int j = 0; j < paramInt; j++) {
/*  999 */           paramPrintStream.print(" ");
/*      */         }
/* 1001 */         paramPrintStream.println(this.threads[i]);
/*      */       }
/* 1003 */       localObject1 = this.ngroups;
/* 1004 */       if (this.groups != null)
/* 1005 */         arrayOfThreadGroup = (ThreadGroup[])Arrays.copyOf(this.groups, localObject1);
/*      */       else {
/* 1007 */         arrayOfThreadGroup = null;
/*      */       }
/*      */     }
/* 1010 */     for (??? = 0; ??? < localObject1; ???++)
/* 1011 */       arrayOfThreadGroup[???].list(paramPrintStream, paramInt);
/*      */   }
/*      */ 
/*      */   public void uncaughtException(Thread paramThread, Throwable paramThrowable)
/*      */   {
/* 1051 */     if (this.parent != null) {
/* 1052 */       this.parent.uncaughtException(paramThread, paramThrowable);
/*      */     } else {
/* 1054 */       Thread.UncaughtExceptionHandler localUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
/*      */ 
/* 1056 */       if (localUncaughtExceptionHandler != null) {
/* 1057 */         localUncaughtExceptionHandler.uncaughtException(paramThread, paramThrowable);
/* 1058 */       } else if (!(paramThrowable instanceof ThreadDeath)) {
/* 1059 */         System.err.print("Exception in thread \"" + paramThread.getName() + "\" ");
/*      */ 
/* 1061 */         paramThrowable.printStackTrace(System.err);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean allowThreadSuspension(boolean paramBoolean)
/*      */   {
/* 1078 */     this.vmAllowSuspension = paramBoolean;
/* 1079 */     if (!paramBoolean) {
/* 1080 */       VM.unsuspendSomeThreads();
/*      */     }
/* 1082 */     return true;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1092 */     return getClass().getName() + "[name=" + getName() + ",maxpri=" + this.maxPriority + "]";
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ThreadGroup
 * JD-Core Version:    0.6.2
 */