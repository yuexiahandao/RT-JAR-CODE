/*     */ package java.awt;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.SequencedEventAccessor;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ class SequencedEvent extends AWTEvent
/*     */   implements ActiveEvent
/*     */ {
/*     */   private static final long serialVersionUID = 547742659238625067L;
/*     */   private static final int ID = 1006;
/*  52 */   private static final LinkedList list = new LinkedList();
/*     */   private final AWTEvent nested;
/*     */   private AppContext appContext;
/*     */   private boolean disposed;
/*     */ 
/*     */   public SequencedEvent(AWTEvent paramAWTEvent)
/*     */   {
/*  77 */     super(paramAWTEvent.getSource(), 1006);
/*  78 */     this.nested = paramAWTEvent;
/*     */ 
/*  81 */     SunToolkit.setSystemGenerated(paramAWTEvent);
/*  82 */     synchronized (SequencedEvent.class) {
/*  83 */       list.add(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void dispatch()
/*     */   {
/*     */     try
/*     */     {
/* 101 */       this.appContext = AppContext.getAppContext();
/*     */ 
/* 103 */       if (getFirst() != this) {
/* 104 */         if (EventQueue.isDispatchThread()) {
/* 105 */           EventDispatchThread localEventDispatchThread = (EventDispatchThread)Thread.currentThread();
/*     */ 
/* 107 */           localEventDispatchThread.pumpEvents(1007, new Conditional() {
/*     */             public boolean evaluate() {
/* 109 */               return !SequencedEvent.this.isFirstOrDisposed();
/*     */             } } );
/*     */         }
/*     */         else {
/* 113 */           while (!isFirstOrDisposed()) {
/* 114 */             synchronized (SequencedEvent.class) {
/*     */               try {
/* 116 */                 SequencedEvent.class.wait(1000L);
/*     */               } catch (InterruptedException localInterruptedException) {
/* 118 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 125 */       if (!this.disposed) {
/* 126 */         KeyboardFocusManager.getCurrentKeyboardFocusManager().setCurrentSequencedEvent(this);
/*     */ 
/* 128 */         Toolkit.getEventQueue().dispatchEvent(this.nested);
/*     */       }
/*     */     } finally {
/* 131 */       dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final boolean isOwnerAppContextDisposed(SequencedEvent paramSequencedEvent)
/*     */   {
/* 139 */     if (paramSequencedEvent != null) {
/* 140 */       Object localObject = paramSequencedEvent.nested.getSource();
/* 141 */       if ((localObject instanceof Component)) {
/* 142 */         return ((Component)localObject).appContext.isDisposed();
/*     */       }
/*     */     }
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean isFirstOrDisposed()
/*     */   {
/* 155 */     if (this.disposed) {
/* 156 */       return true;
/*     */     }
/*     */ 
/* 159 */     return (this == getFirstWithContext()) || (this.disposed);
/*     */   }
/*     */ 
/*     */   private static final synchronized SequencedEvent getFirst() {
/* 163 */     return (SequencedEvent)list.getFirst();
/*     */   }
/*     */ 
/*     */   private static final SequencedEvent getFirstWithContext()
/*     */   {
/* 170 */     SequencedEvent localSequencedEvent = getFirst();
/* 171 */     while (isOwnerAppContextDisposed(localSequencedEvent)) {
/* 172 */       localSequencedEvent.dispose();
/* 173 */       localSequencedEvent = getFirst();
/*     */     }
/* 175 */     return localSequencedEvent;
/*     */   }
/*     */ 
/*     */   final void dispose()
/*     */   {
/* 189 */     synchronized (SequencedEvent.class) {
/* 190 */       if (this.disposed) {
/* 191 */         return;
/*     */       }
/* 193 */       if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentSequencedEvent() == this)
/*     */       {
/* 195 */         KeyboardFocusManager.getCurrentKeyboardFocusManager().setCurrentSequencedEvent(null);
/*     */       }
/*     */ 
/* 198 */       this.disposed = true;
/*     */     }
/*     */ 
/* 201 */     if (this.appContext != null) {
/* 202 */       SunToolkit.postEvent(this.appContext, new SentEvent());
/*     */     }
/*     */ 
/* 205 */     ??? = null;
/*     */ 
/* 207 */     synchronized (SequencedEvent.class) {
/* 208 */       SequencedEvent.class.notifyAll();
/*     */ 
/* 210 */       if (list.getFirst() == this) {
/* 211 */         list.removeFirst();
/*     */ 
/* 213 */         if (!list.isEmpty())
/* 214 */           ??? = (SequencedEvent)list.getFirst();
/*     */       }
/*     */       else {
/* 217 */         list.remove(this);
/*     */       }
/*     */     }
/*     */ 
/* 221 */     if ((??? != null) && (((SequencedEvent)???).appContext != null))
/* 222 */       SunToolkit.postEvent(((SequencedEvent)???).appContext, new SentEvent());
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  59 */     AWTAccessor.setSequencedEventAccessor(new AWTAccessor.SequencedEventAccessor() {
/*     */       public AWTEvent getNested(AWTEvent paramAnonymousAWTEvent) {
/*  61 */         return ((SequencedEvent)paramAnonymousAWTEvent).nested;
/*     */       }
/*     */       public boolean isSequencedEvent(AWTEvent paramAnonymousAWTEvent) {
/*  64 */         return paramAnonymousAWTEvent instanceof SequencedEvent;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.SequencedEvent
 * JD-Core Version:    0.6.2
 */