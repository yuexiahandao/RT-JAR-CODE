/*     */ package com.sun.java.swing;
/*     */ 
/*     */ import java.applet.Applet;
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Window;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.RepaintManager;
/*     */ import sun.awt.AppContext;
/*     */ import sun.awt.EventQueueDelegate;
/*     */ import sun.awt.EventQueueDelegate.Delegate;
/*     */ import sun.awt.SunToolkit;
/*     */ 
/*     */ public class SwingUtilities3
/*     */ {
/*  61 */   private static final Object DELEGATE_REPAINT_MANAGER_KEY = new StringBuilder("DelegateRepaintManagerKey");
/*     */ 
/*  79 */   private static final Map<Container, Boolean> vsyncedMap = Collections.synchronizedMap(new WeakHashMap());
/*     */ 
/*     */   public static void setDelegateRepaintManager(JComponent paramJComponent, RepaintManager paramRepaintManager)
/*     */   {
/*  72 */     AppContext.getAppContext().put(DELEGATE_REPAINT_MANAGER_KEY, Boolean.TRUE);
/*     */ 
/*  75 */     paramJComponent.putClientProperty(DELEGATE_REPAINT_MANAGER_KEY, paramRepaintManager);
/*     */   }
/*     */ 
/*     */   public static void setVsyncRequested(Container paramContainer, boolean paramBoolean)
/*     */   {
/*  97 */     assert (((paramContainer instanceof Applet)) || ((paramContainer instanceof Window)));
/*  98 */     if (paramBoolean)
/*  99 */       vsyncedMap.put(paramContainer, Boolean.TRUE);
/*     */     else
/* 101 */       vsyncedMap.remove(paramContainer);
/*     */   }
/*     */ 
/*     */   public static boolean isVsyncRequested(Container paramContainer)
/*     */   {
/* 112 */     assert (((paramContainer instanceof Applet)) || ((paramContainer instanceof Window)));
/* 113 */     return Boolean.TRUE == vsyncedMap.get(paramContainer);
/*     */   }
/*     */ 
/*     */   public static RepaintManager getDelegateRepaintManager(Component paramComponent)
/*     */   {
/* 121 */     RepaintManager localRepaintManager = null;
/* 122 */     if (Boolean.TRUE == SunToolkit.targetToAppContext(paramComponent).get(DELEGATE_REPAINT_MANAGER_KEY))
/*     */     {
/* 124 */       while ((localRepaintManager == null) && (paramComponent != null))
/*     */       {
/* 126 */         while ((paramComponent != null) && (!(paramComponent instanceof JComponent))) {
/* 127 */           paramComponent = paramComponent.getParent();
/*     */         }
/* 129 */         if (paramComponent != null) {
/* 130 */           localRepaintManager = (RepaintManager)((JComponent)paramComponent).getClientProperty(DELEGATE_REPAINT_MANAGER_KEY);
/*     */ 
/* 133 */           paramComponent = paramComponent.getParent();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 138 */     return localRepaintManager;
/*     */   }
/*     */ 
/*     */   public static void setEventQueueDelegate(Map<String, Map<String, Object>> paramMap)
/*     */   {
/* 147 */     EventQueueDelegate.setDelegate(new EventQueueDelegateFromMap(paramMap));
/*     */   }
/*     */   private static class EventQueueDelegateFromMap implements EventQueueDelegate.Delegate {
/*     */     private final AWTEvent[] afterDispatchEventArgument;
/*     */     private final Object[] afterDispatchHandleArgument;
/*     */     private final Callable<Void> afterDispatchCallable;
/*     */     private final AWTEvent[] beforeDispatchEventArgument;
/*     */     private final Callable<Object> beforeDispatchCallable;
/*     */     private final EventQueue[] getNextEventEventQueueArgument;
/*     */     private final Callable<AWTEvent> getNextEventCallable;
/*     */ 
/* 164 */     public EventQueueDelegateFromMap(Map<String, Map<String, Object>> paramMap) { Map localMap = (Map)paramMap.get("afterDispatch");
/* 165 */       this.afterDispatchEventArgument = ((AWTEvent[])localMap.get("event"));
/* 166 */       this.afterDispatchHandleArgument = ((Object[])localMap.get("handle"));
/* 167 */       this.afterDispatchCallable = ((Callable)localMap.get("method"));
/*     */ 
/* 169 */       localMap = (Map)paramMap.get("beforeDispatch");
/* 170 */       this.beforeDispatchEventArgument = ((AWTEvent[])localMap.get("event"));
/* 171 */       this.beforeDispatchCallable = ((Callable)localMap.get("method"));
/*     */ 
/* 173 */       localMap = (Map)paramMap.get("getNextEvent");
/* 174 */       this.getNextEventEventQueueArgument = ((EventQueue[])localMap.get("eventQueue"));
/*     */ 
/* 176 */       this.getNextEventCallable = ((Callable)localMap.get("method")); }
/*     */ 
/*     */     public void afterDispatch(AWTEvent paramAWTEvent, Object paramObject)
/*     */       throws InterruptedException
/*     */     {
/* 181 */       this.afterDispatchEventArgument[0] = paramAWTEvent;
/* 182 */       this.afterDispatchHandleArgument[0] = paramObject;
/*     */       try {
/* 184 */         this.afterDispatchCallable.call();
/*     */       } catch (InterruptedException localInterruptedException) {
/* 186 */         throw localInterruptedException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 188 */         throw localRuntimeException;
/*     */       } catch (Exception localException) {
/* 190 */         throw new RuntimeException(localException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Object beforeDispatch(AWTEvent paramAWTEvent) throws InterruptedException
/*     */     {
/* 196 */       this.beforeDispatchEventArgument[0] = paramAWTEvent;
/*     */       try {
/* 198 */         return this.beforeDispatchCallable.call();
/*     */       } catch (InterruptedException localInterruptedException) {
/* 200 */         throw localInterruptedException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 202 */         throw localRuntimeException;
/*     */       } catch (Exception localException) {
/* 204 */         throw new RuntimeException(localException);
/*     */       }
/*     */     }
/*     */ 
/*     */     public AWTEvent getNextEvent(EventQueue paramEventQueue) throws InterruptedException
/*     */     {
/* 210 */       this.getNextEventEventQueueArgument[0] = paramEventQueue;
/*     */       try {
/* 212 */         return (AWTEvent)this.getNextEventCallable.call();
/*     */       } catch (InterruptedException localInterruptedException) {
/* 214 */         throw localInterruptedException;
/*     */       } catch (RuntimeException localRuntimeException) {
/* 216 */         throw localRuntimeException;
/*     */       } catch (Exception localException) {
/* 218 */         throw new RuntimeException(localException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.SwingUtilities3
 * JD-Core Version:    0.6.2
 */