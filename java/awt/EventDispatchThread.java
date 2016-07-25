/*     */ package java.awt;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import sun.awt.EventQueueDelegate;
/*     */ import sun.awt.EventQueueDelegate.Delegate;
/*     */ import sun.awt.ModalExclude;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.awt.dnd.SunDragSourceContextPeer;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ class EventDispatchThread extends Thread
/*     */ {
/*  67 */   private static final PlatformLogger eventLog = PlatformLogger.getLogger("java.awt.event.EventDispatchThread");
/*     */   private EventQueue theQueue;
/*  70 */   private volatile boolean doDispatch = true;
/*     */   private static final int ANY_EVENT = -1;
/*  74 */   private ArrayList<EventFilter> eventFilters = new ArrayList();
/*     */ 
/*     */   EventDispatchThread(ThreadGroup paramThreadGroup, String paramString, EventQueue paramEventQueue) {
/*  77 */     super(paramThreadGroup, paramString);
/*  78 */     setEventQueue(paramEventQueue);
/*     */   }
/*     */ 
/*     */   public void stopDispatching()
/*     */   {
/*  85 */     this.doDispatch = false;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try {
/*  91 */       pumpEvents(new Conditional() {
/*     */         public boolean evaluate() {
/*  93 */           return true;
/*     */         }
/*     */       });
/* 100 */       if (!getEventQueue().detachDispatchThread(this, (!this.doDispatch) || (isInterrupted())));
/*     */     }
/*     */     finally {
/* 100 */       while (!getEventQueue().detachDispatchThread(this, (!this.doDispatch) || (isInterrupted())))
/*     */       {
/* 103 */         throw localObject;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private Conditional _macosxGetConditional(final Object paramObject)
/*     */   {
/*     */     try
/*     */     {
/* 121 */       return new Conditional() {
/* 122 */         final Method evaluateMethod = Class.forName("sun.lwawt.macosx.EventDispatchAccess").getMethod("evaluate", null);
/*     */ 
/*     */         public boolean evaluate() {
/*     */           try { return ((Boolean)this.evaluateMethod.invoke(paramObject, null)).booleanValue(); } catch (Exception localException) {
/*     */           }
/* 127 */           return false;
/*     */         } } ;
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 132 */     return new Conditional() { public boolean evaluate() { return false; }
/*     */     };
/*     */   }
/*     */ 
/*     */   void pumpEvents(Conditional paramConditional)
/*     */   {
/* 138 */     pumpEvents(-1, paramConditional);
/*     */   }
/*     */ 
/*     */   void pumpEventsForHierarchy(Conditional paramConditional, Component paramComponent) {
/* 142 */     pumpEventsForHierarchy(-1, paramConditional, paramComponent);
/*     */   }
/*     */ 
/*     */   void pumpEvents(int paramInt, Conditional paramConditional) {
/* 146 */     pumpEventsForHierarchy(paramInt, paramConditional, null);
/*     */   }
/*     */ 
/*     */   void pumpEventsForHierarchy(int paramInt, Conditional paramConditional, Component paramComponent) {
/* 150 */     pumpEventsForFilter(paramInt, paramConditional, new HierarchyEventFilter(paramComponent));
/*     */   }
/*     */ 
/*     */   void pumpEventsForFilter(Conditional paramConditional, EventFilter paramEventFilter) {
/* 154 */     pumpEventsForFilter(-1, paramConditional, paramEventFilter);
/*     */   }
/*     */ 
/*     */   void pumpEventsForFilter(int paramInt, Conditional paramConditional, EventFilter paramEventFilter) {
/* 158 */     addEventFilter(paramEventFilter);
/* 159 */     this.doDispatch = true;
/* 160 */     while ((this.doDispatch) && (!isInterrupted()) && (paramConditional.evaluate())) {
/* 161 */       pumpOneEventForFilters(paramInt);
/*     */     }
/* 163 */     removeEventFilter(paramEventFilter);
/*     */   }
/*     */ 
/*     */   void addEventFilter(EventFilter paramEventFilter) {
/* 167 */     eventLog.finest("adding the event filter: " + paramEventFilter);
/* 168 */     synchronized (this.eventFilters) {
/* 169 */       if (!this.eventFilters.contains(paramEventFilter))
/* 170 */         if ((paramEventFilter instanceof ModalEventFilter)) {
/* 171 */           ModalEventFilter localModalEventFilter1 = (ModalEventFilter)paramEventFilter;
/* 172 */           int i = 0;
/* 173 */           for (i = 0; i < this.eventFilters.size(); i++) {
/* 174 */             EventFilter localEventFilter = (EventFilter)this.eventFilters.get(i);
/* 175 */             if ((localEventFilter instanceof ModalEventFilter)) {
/* 176 */               ModalEventFilter localModalEventFilter2 = (ModalEventFilter)localEventFilter;
/* 177 */               if (localModalEventFilter2.compareTo(localModalEventFilter1) > 0) {
/*     */                 break;
/*     */               }
/*     */             }
/*     */           }
/* 182 */           this.eventFilters.add(i, paramEventFilter);
/*     */         } else {
/* 184 */           this.eventFilters.add(paramEventFilter);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeEventFilter(EventFilter paramEventFilter)
/*     */   {
/* 191 */     eventLog.finest("removing the event filter: " + paramEventFilter);
/* 192 */     synchronized (this.eventFilters) {
/* 193 */       this.eventFilters.remove(paramEventFilter);
/*     */     }
/*     */   }
/*     */ 
/*     */   void pumpOneEventForFilters(int paramInt) {
/* 198 */     AWTEvent localAWTEvent = null;
/* 199 */     int i = 0;
/*     */     try {
/* 201 */       EventQueue localEventQueue = null;
/* 202 */       EventQueueDelegate.Delegate localDelegate = null;
/*     */       do
/*     */       {
/* 205 */         localEventQueue = getEventQueue();
/* 206 */         localDelegate = EventQueueDelegate.getDelegate();
/*     */ 
/* 208 */         if ((localDelegate != null) && (paramInt == -1))
/* 209 */           localAWTEvent = localDelegate.getNextEvent(localEventQueue);
/*     */         else {
/* 211 */           localAWTEvent = paramInt == -1 ? localEventQueue.getNextEvent() : localEventQueue.getNextEvent(paramInt);
/*     */         }
/*     */ 
/* 214 */         i = 1;
/* 215 */         synchronized (this.eventFilters) {
/* 216 */           for (int j = this.eventFilters.size() - 1; j >= 0; j--) {
/* 217 */             EventFilter localEventFilter = (EventFilter)this.eventFilters.get(j);
/* 218 */             EventFilter.FilterAction localFilterAction = localEventFilter.acceptEvent(localAWTEvent);
/* 219 */             if (localFilterAction == EventFilter.FilterAction.REJECT)
/* 220 */               i = 0;
/*     */             else {
/* 222 */               if (localFilterAction == EventFilter.FilterAction.ACCEPT_IMMEDIATELY)
/*     */                 break;
/*     */             }
/*     */           }
/*     */         }
/* 227 */         i = (i != 0) && (SunDragSourceContextPeer.checkEvent(localAWTEvent)) ? 1 : 0;
/* 228 */         if (i == 0) {
/* 229 */           localAWTEvent.consume();
/*     */         }
/*     */       }
/* 232 */       while (i == 0);
/*     */ 
/* 234 */       if (eventLog.isLoggable(300)) {
/* 235 */         eventLog.finest("Dispatching: " + localAWTEvent);
/*     */       }
/*     */ 
/* 238 */       ??? = null;
/* 239 */       if (localDelegate != null) {
/* 240 */         ??? = localDelegate.beforeDispatch(localAWTEvent);
/*     */       }
/* 242 */       localEventQueue.dispatchEvent(localAWTEvent);
/* 243 */       if (localDelegate != null)
/* 244 */         localDelegate.afterDispatch(localAWTEvent, ???);
/*     */     }
/*     */     catch (ThreadDeath localThreadDeath)
/*     */     {
/* 248 */       this.doDispatch = false;
/* 249 */       throw localThreadDeath;
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {
/* 252 */       this.doDispatch = false;
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 256 */       processException(localThrowable);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void processException(Throwable paramThrowable) {
/* 261 */     if (eventLog.isLoggable(500)) {
/* 262 */       eventLog.fine("Processing exception: " + paramThrowable);
/*     */     }
/* 264 */     getUncaughtExceptionHandler().uncaughtException(this, paramThrowable);
/*     */   }
/*     */ 
/*     */   public synchronized EventQueue getEventQueue() {
/* 268 */     return this.theQueue;
/*     */   }
/*     */   public synchronized void setEventQueue(EventQueue paramEventQueue) {
/* 271 */     this.theQueue = paramEventQueue;
/*     */   }
/*     */   private static class HierarchyEventFilter implements EventFilter {
/*     */     private Component modalComponent;
/*     */ 
/*     */     public HierarchyEventFilter(Component paramComponent) {
/* 277 */       this.modalComponent = paramComponent;
/*     */     }
/*     */     public EventFilter.FilterAction acceptEvent(AWTEvent paramAWTEvent) {
/* 280 */       if (this.modalComponent != null) {
/* 281 */         int i = paramAWTEvent.getID();
/* 282 */         int j = (i >= 500) && (i <= 507) ? 1 : 0;
/*     */ 
/* 284 */         int k = (i >= 1001) && (i <= 1001) ? 1 : 0;
/*     */ 
/* 286 */         int m = i == 201 ? 1 : 0;
/*     */ 
/* 293 */         if (Component.isInstanceOf(this.modalComponent, "javax.swing.JInternalFrame"))
/*     */         {
/* 300 */           return m != 0 ? EventFilter.FilterAction.REJECT : EventFilter.FilterAction.ACCEPT;
/*     */         }
/* 302 */         if ((j != 0) || (k != 0) || (m != 0)) {
/* 303 */           Object localObject1 = paramAWTEvent.getSource();
/* 304 */           if ((localObject1 instanceof ModalExclude))
/*     */           {
/* 307 */             return EventFilter.FilterAction.ACCEPT;
/* 308 */           }if ((localObject1 instanceof Component)) {
/* 309 */             Object localObject2 = (Component)localObject1;
/*     */ 
/* 311 */             int n = 0;
/* 312 */             if ((this.modalComponent instanceof Container)) {
/* 313 */               while ((localObject2 != this.modalComponent) && (localObject2 != null)) {
/* 314 */                 if (((localObject2 instanceof Window)) && (SunToolkit.isModalExcluded((Window)localObject2)))
/*     */                 {
/* 318 */                   n = 1;
/* 319 */                   break;
/*     */                 }
/* 321 */                 localObject2 = ((Component)localObject2).getParent();
/*     */               }
/*     */             }
/* 324 */             if ((n == 0) && (localObject2 != this.modalComponent)) {
/* 325 */               return EventFilter.FilterAction.REJECT;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 330 */       return EventFilter.FilterAction.ACCEPT;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.EventDispatchThread
 * JD-Core Version:    0.6.2
 */