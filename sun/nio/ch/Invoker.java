/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.nio.channels.AsynchronousChannel;
/*     */ import java.nio.channels.CompletionHandler;
/*     */ import java.nio.channels.ShutdownChannelGroupException;
/*     */ import java.security.AccessController;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import sun.misc.InnocuousThread;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ 
/*     */ class Invoker
/*     */ {
/*  43 */   private static final int maxHandlerInvokeCount = ((Integer)AccessController.doPrivileged(new GetIntegerAction("sun.nio.ch.maxCompletionHandlersOnStack", 16))).intValue();
/*     */ 
/*  71 */   private static final ThreadLocal<GroupAndInvokeCount> myGroupAndInvokeCount = new ThreadLocal()
/*     */   {
/*     */     protected Invoker.GroupAndInvokeCount initialValue() {
/*  74 */       return null;
/*     */     }
/*  71 */   };
/*     */ 
/*     */   static void bindToGroup(AsynchronousChannelGroupImpl paramAsynchronousChannelGroupImpl)
/*     */   {
/*  82 */     myGroupAndInvokeCount.set(new GroupAndInvokeCount(paramAsynchronousChannelGroupImpl));
/*     */   }
/*     */ 
/*     */   static GroupAndInvokeCount getGroupAndInvokeCount()
/*     */   {
/*  89 */     return (GroupAndInvokeCount)myGroupAndInvokeCount.get();
/*     */   }
/*     */ 
/*     */   static boolean isBoundToAnyGroup()
/*     */   {
/*  96 */     return myGroupAndInvokeCount.get() != null;
/*     */   }
/*     */ 
/*     */   static boolean mayInvokeDirect(GroupAndInvokeCount paramGroupAndInvokeCount, AsynchronousChannelGroupImpl paramAsynchronousChannelGroupImpl)
/*     */   {
/* 107 */     if ((paramGroupAndInvokeCount != null) && (paramGroupAndInvokeCount.group() == paramAsynchronousChannelGroupImpl) && (paramGroupAndInvokeCount.invokeCount() < maxHandlerInvokeCount))
/*     */     {
/* 111 */       return true;
/*     */     }
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   static <V, A> void invokeUnchecked(CompletionHandler<V, ? super A> paramCompletionHandler, A paramA, V paramV, Throwable paramThrowable)
/*     */   {
/* 125 */     if (paramThrowable == null)
/* 126 */       paramCompletionHandler.completed(paramV, paramA);
/*     */     else {
/* 128 */       paramCompletionHandler.failed(paramThrowable, paramA);
/*     */     }
/*     */ 
/* 132 */     Thread.interrupted();
/*     */ 
/* 135 */     if (System.getSecurityManager() != null) {
/* 136 */       Thread localThread = Thread.currentThread();
/* 137 */       if ((localThread instanceof InnocuousThread)) {
/* 138 */         GroupAndInvokeCount localGroupAndInvokeCount = (GroupAndInvokeCount)myGroupAndInvokeCount.get();
/* 139 */         ((InnocuousThread)localThread).eraseThreadLocals();
/* 140 */         if (localGroupAndInvokeCount != null)
/* 141 */           myGroupAndInvokeCount.set(localGroupAndInvokeCount);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static <V, A> void invokeDirect(GroupAndInvokeCount paramGroupAndInvokeCount, CompletionHandler<V, ? super A> paramCompletionHandler, A paramA, V paramV, Throwable paramThrowable)
/*     */   {
/* 156 */     paramGroupAndInvokeCount.incrementInvokeCount();
/* 157 */     invokeUnchecked(paramCompletionHandler, paramA, paramV, paramThrowable);
/*     */   }
/*     */ 
/*     */   static <V, A> void invoke(AsynchronousChannel paramAsynchronousChannel, CompletionHandler<V, ? super A> paramCompletionHandler, A paramA, V paramV, Throwable paramThrowable)
/*     */   {
/* 171 */     int i = 0;
/* 172 */     int j = 0;
/* 173 */     GroupAndInvokeCount localGroupAndInvokeCount = (GroupAndInvokeCount)myGroupAndInvokeCount.get();
/* 174 */     if (localGroupAndInvokeCount != null) {
/* 175 */       if (localGroupAndInvokeCount.group() == ((Groupable)paramAsynchronousChannel).group())
/* 176 */         j = 1;
/* 177 */       if ((j != 0) && (localGroupAndInvokeCount.invokeCount() < maxHandlerInvokeCount))
/*     */       {
/* 181 */         i = 1;
/*     */       }
/*     */     }
/* 184 */     if (i != 0)
/* 185 */       invokeDirect(localGroupAndInvokeCount, paramCompletionHandler, paramA, paramV, paramThrowable);
/*     */     else
/*     */       try {
/* 188 */         invokeIndirectly(paramAsynchronousChannel, paramCompletionHandler, paramA, paramV, paramThrowable);
/*     */       }
/*     */       catch (RejectedExecutionException localRejectedExecutionException)
/*     */       {
/* 192 */         if (j != 0) {
/* 193 */           invokeDirect(localGroupAndInvokeCount, paramCompletionHandler, paramA, paramV, paramThrowable);
/*     */         }
/*     */         else
/* 196 */           throw new ShutdownChannelGroupException();
/*     */       }
/*     */   }
/*     */ 
/*     */   static <V, A> void invokeIndirectly(AsynchronousChannel paramAsynchronousChannel, CompletionHandler<V, ? super A> paramCompletionHandler, final A paramA, final V paramV, final Throwable paramThrowable)
/*     */   {
/*     */     try
/*     */     {
/* 212 */       ((Groupable)paramAsynchronousChannel).group().executeOnPooledThread(new Runnable() {
/*     */         public void run() {
/* 214 */           Invoker.GroupAndInvokeCount localGroupAndInvokeCount = (Invoker.GroupAndInvokeCount)Invoker.myGroupAndInvokeCount.get();
/*     */ 
/* 216 */           if (localGroupAndInvokeCount != null)
/* 217 */             localGroupAndInvokeCount.setInvokeCount(1);
/* 218 */           Invoker.invokeUnchecked(this.val$handler, paramA, paramV, paramThrowable);
/*     */         } } );
/*     */     }
/*     */     catch (RejectedExecutionException localRejectedExecutionException) {
/* 222 */       throw new ShutdownChannelGroupException();
/*     */     }
/*     */   }
/*     */ 
/*     */   static <V, A> void invokeIndirectly(CompletionHandler<V, ? super A> paramCompletionHandler, final A paramA, final V paramV, final Throwable paramThrowable, Executor paramExecutor)
/*     */   {
/*     */     try
/*     */     {
/* 236 */       paramExecutor.execute(new Runnable() {
/*     */         public void run() {
/* 238 */           Invoker.invokeUnchecked(this.val$handler, paramA, paramV, paramThrowable);
/*     */         } } );
/*     */     }
/*     */     catch (RejectedExecutionException localRejectedExecutionException) {
/* 242 */       throw new ShutdownChannelGroupException();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void invokeOnThreadInThreadPool(Groupable paramGroupable, Runnable paramRunnable)
/*     */   {
/* 255 */     GroupAndInvokeCount localGroupAndInvokeCount = (GroupAndInvokeCount)myGroupAndInvokeCount.get();
/* 256 */     AsynchronousChannelGroupImpl localAsynchronousChannelGroupImpl = paramGroupable.group();
/*     */     int i;
/* 257 */     if (localGroupAndInvokeCount == null)
/* 258 */       i = 0;
/*     */     else
/* 260 */       i = localGroupAndInvokeCount.group == localAsynchronousChannelGroupImpl ? 1 : 0;
/*     */     try
/*     */     {
/* 263 */       if (i != 0)
/* 264 */         paramRunnable.run();
/*     */       else
/* 266 */         localAsynchronousChannelGroupImpl.executeOnPooledThread(paramRunnable);
/*     */     }
/*     */     catch (RejectedExecutionException localRejectedExecutionException) {
/* 269 */       throw new ShutdownChannelGroupException();
/*     */     }
/*     */   }
/*     */ 
/*     */   static <V, A> void invokeUnchecked(PendingFuture<V, A> paramPendingFuture)
/*     */   {
/* 278 */     assert (paramPendingFuture.isDone());
/* 279 */     CompletionHandler localCompletionHandler = paramPendingFuture.handler();
/* 280 */     if (localCompletionHandler != null)
/* 281 */       invokeUnchecked(localCompletionHandler, paramPendingFuture.attachment(), paramPendingFuture.value(), paramPendingFuture.exception());
/*     */   }
/*     */ 
/*     */   static <V, A> void invoke(PendingFuture<V, A> paramPendingFuture)
/*     */   {
/* 294 */     assert (paramPendingFuture.isDone());
/* 295 */     CompletionHandler localCompletionHandler = paramPendingFuture.handler();
/* 296 */     if (localCompletionHandler != null)
/* 297 */       invoke(paramPendingFuture.channel(), localCompletionHandler, paramPendingFuture.attachment(), paramPendingFuture.value(), paramPendingFuture.exception());
/*     */   }
/*     */ 
/*     */   static <V, A> void invokeIndirectly(PendingFuture<V, A> paramPendingFuture)
/*     */   {
/* 310 */     assert (paramPendingFuture.isDone());
/* 311 */     CompletionHandler localCompletionHandler = paramPendingFuture.handler();
/* 312 */     if (localCompletionHandler != null)
/* 313 */       invokeIndirectly(paramPendingFuture.channel(), localCompletionHandler, paramPendingFuture.attachment(), paramPendingFuture.value(), paramPendingFuture.exception());
/*     */   }
/*     */ 
/*     */   static class GroupAndInvokeCount
/*     */   {
/*     */     private final AsynchronousChannelGroupImpl group;
/*     */     private int handlerInvokeCount;
/*     */ 
/*     */     GroupAndInvokeCount(AsynchronousChannelGroupImpl paramAsynchronousChannelGroupImpl)
/*     */     {
/*  53 */       this.group = paramAsynchronousChannelGroupImpl;
/*     */     }
/*     */     AsynchronousChannelGroupImpl group() {
/*  56 */       return this.group;
/*     */     }
/*     */     int invokeCount() {
/*  59 */       return this.handlerInvokeCount;
/*     */     }
/*     */     void setInvokeCount(int paramInt) {
/*  62 */       this.handlerInvokeCount = paramInt;
/*     */     }
/*     */     void resetInvokeCount() {
/*  65 */       this.handlerInvokeCount = 0;
/*     */     }
/*     */     void incrementInvokeCount() {
/*  68 */       this.handlerInvokeCount += 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.Invoker
 * JD-Core Version:    0.6.2
 */