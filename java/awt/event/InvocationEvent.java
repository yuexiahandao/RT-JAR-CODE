/*     */ package java.awt.event;
/*     */ 
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.ActiveEvent;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.InvocationEventAccessor;
/*     */ 
/*     */ public class InvocationEvent extends AWTEvent
/*     */   implements ActiveEvent
/*     */ {
/*     */   public static final int INVOCATION_FIRST = 1200;
/*     */   public static final int INVOCATION_DEFAULT = 1200;
/*     */   public static final int INVOCATION_LAST = 1200;
/*     */   protected Runnable runnable;
/*     */   protected Object notifier;
/*     */   private final Runnable listener;
/* 118 */   private volatile boolean dispatched = false;
/*     */   protected boolean catchExceptions;
/* 132 */   private Exception exception = null;
/*     */ 
/* 139 */   private Throwable throwable = null;
/*     */   private long when;
/*     */   private static final long serialVersionUID = 436056344909459450L;
/*     */ 
/*     */   public InvocationEvent(Object paramObject, Runnable paramRunnable)
/*     */   {
/* 174 */     this(paramObject, 1200, paramRunnable, null, null, false);
/*     */   }
/*     */ 
/*     */   public InvocationEvent(Object paramObject1, Runnable paramRunnable, Object paramObject2, boolean paramBoolean)
/*     */   {
/* 212 */     this(paramObject1, 1200, paramRunnable, paramObject2, null, paramBoolean);
/*     */   }
/*     */ 
/*     */   private InvocationEvent(Object paramObject, Runnable paramRunnable1, Runnable paramRunnable2, boolean paramBoolean)
/*     */   {
/* 244 */     this(paramObject, 1200, paramRunnable1, null, paramRunnable2, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected InvocationEvent(Object paramObject1, int paramInt, Runnable paramRunnable, Object paramObject2, boolean paramBoolean)
/*     */   {
/* 280 */     this(paramObject1, paramInt, paramRunnable, paramObject2, null, paramBoolean);
/*     */   }
/*     */ 
/*     */   private InvocationEvent(Object paramObject1, int paramInt, Runnable paramRunnable1, Object paramObject2, Runnable paramRunnable2, boolean paramBoolean)
/*     */   {
/* 285 */     super(paramObject1, paramInt);
/* 286 */     this.runnable = paramRunnable1;
/* 287 */     this.notifier = paramObject2;
/* 288 */     this.listener = paramRunnable2;
/* 289 */     this.catchExceptions = paramBoolean;
/* 290 */     this.when = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   public void dispatch()
/*     */   {
/*     */     try
/*     */     {
/* 300 */       if (this.catchExceptions) {
/*     */         try {
/* 302 */           this.runnable.run();
/*     */         }
/*     */         catch (Throwable localThrowable) {
/* 305 */           if ((localThrowable instanceof Exception)) {
/* 306 */             this.exception = ((Exception)localThrowable);
/*     */           }
/* 308 */           this.throwable = localThrowable;
/*     */         }
/*     */       }
/*     */       else
/* 312 */         this.runnable.run();
/*     */     }
/*     */     finally {
/* 315 */       finishedDispatching(true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Exception getException()
/*     */   {
/* 328 */     return this.catchExceptions ? this.exception : null;
/*     */   }
/*     */ 
/*     */   public Throwable getThrowable()
/*     */   {
/* 341 */     return this.catchExceptions ? this.throwable : null;
/*     */   }
/*     */ 
/*     */   public long getWhen()
/*     */   {
/* 351 */     return this.when;
/*     */   }
/*     */ 
/*     */   public boolean isDispatched()
/*     */   {
/* 385 */     return this.dispatched;
/*     */   }
/*     */ 
/*     */   private void finishedDispatching(boolean paramBoolean)
/*     */   {
/* 394 */     this.dispatched = paramBoolean;
/*     */ 
/* 396 */     if (this.notifier != null) {
/* 397 */       synchronized (this.notifier) {
/* 398 */         this.notifier.notifyAll();
/*     */       }
/*     */     }
/*     */ 
/* 402 */     if (this.listener != null)
/* 403 */       this.listener.run();
/*     */   }
/*     */ 
/*     */   public String paramString()
/*     */   {
/*     */     String str;
/* 415 */     switch (this.id) {
/*     */     case 1200:
/* 417 */       str = "INVOCATION_DEFAULT";
/* 418 */       break;
/*     */     default:
/* 420 */       str = "unknown type";
/*     */     }
/* 422 */     return str + ",runnable=" + this.runnable + ",notifier=" + this.notifier + ",catchExceptions=" + this.catchExceptions + ",when=" + this.when;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  62 */     AWTAccessor.setInvocationEventAccessor(new AWTAccessor.InvocationEventAccessor()
/*     */     {
/*     */       public void dispose(InvocationEvent paramAnonymousInvocationEvent) {
/*  65 */         paramAnonymousInvocationEvent.finishedDispatching(false);
/*     */       }
/*     */ 
/*     */       public InvocationEvent createEvent(Object paramAnonymousObject, Runnable paramAnonymousRunnable1, Runnable paramAnonymousRunnable2, boolean paramAnonymousBoolean)
/*     */       {
/*  70 */         return new InvocationEvent(paramAnonymousObject, paramAnonymousRunnable1, paramAnonymousRunnable2, paramAnonymousBoolean, null);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.InvocationEvent
 * JD-Core Version:    0.6.2
 */