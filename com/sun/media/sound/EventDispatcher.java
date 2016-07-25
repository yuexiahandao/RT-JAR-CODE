/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.sound.midi.ControllerEventListener;
/*     */ import javax.sound.midi.MetaEventListener;
/*     */ import javax.sound.midi.MetaMessage;
/*     */ import javax.sound.midi.ShortMessage;
/*     */ import javax.sound.sampled.LineEvent;
/*     */ import javax.sound.sampled.LineListener;
/*     */ 
/*     */ final class EventDispatcher
/*     */   implements Runnable
/*     */ {
/*     */   private static final int AUTO_CLOSE_TIME = 5000;
/*     */   private final ArrayList eventQueue;
/*     */   private Thread thread;
/*     */   private final ArrayList<ClipInfo> autoClosingClips;
/*     */   private final ArrayList<LineMonitor> lineMonitors;
/*     */   static final int LINE_MONITOR_TIME = 400;
/*     */ 
/*     */   EventDispatcher()
/*     */   {
/*  60 */     this.eventQueue = new ArrayList();
/*     */ 
/*  66 */     this.thread = null;
/*     */ 
/*  72 */     this.autoClosingClips = new ArrayList();
/*     */ 
/*  77 */     this.lineMonitors = new ArrayList();
/*     */   }
/*     */ 
/*     */   synchronized void start()
/*     */   {
/*  90 */     if (this.thread == null)
/*  91 */       this.thread = JSSecurityManager.createThread(this, "Java Sound Event Dispatcher", true, -1, true);
/*     */   }
/*     */ 
/*     */   void processEvent(EventInfo paramEventInfo)
/*     */   {
/* 105 */     int i = paramEventInfo.getListenerCount();
/*     */     Object localObject;
/*     */     int j;
/* 108 */     if ((paramEventInfo.getEvent() instanceof LineEvent)) {
/* 109 */       localObject = (LineEvent)paramEventInfo.getEvent();
/*     */ 
/* 111 */       for (j = 0; j < i; j++)
/*     */         try {
/* 113 */           ((LineListener)paramEventInfo.getListener(j)).update((LineEvent)localObject);
/*     */         }
/*     */         catch (Throwable localThrowable1)
/*     */         {
/*     */         }
/* 118 */       return;
/*     */     }
/*     */ 
/* 122 */     if ((paramEventInfo.getEvent() instanceof MetaMessage)) {
/* 123 */       localObject = (MetaMessage)paramEventInfo.getEvent();
/* 124 */       for (j = 0; j < i; j++)
/*     */         try {
/* 126 */           ((MetaEventListener)paramEventInfo.getListener(j)).meta((MetaMessage)localObject);
/*     */         }
/*     */         catch (Throwable localThrowable2)
/*     */         {
/*     */         }
/* 131 */       return;
/*     */     }
/*     */ 
/* 135 */     if ((paramEventInfo.getEvent() instanceof ShortMessage)) {
/* 136 */       localObject = (ShortMessage)paramEventInfo.getEvent();
/* 137 */       j = ((ShortMessage)localObject).getStatus();
/*     */ 
/* 141 */       if ((j & 0xF0) == 176) {
/* 142 */         for (int k = 0; k < i; k++)
/*     */           try {
/* 144 */             ((ControllerEventListener)paramEventInfo.getListener(k)).controlChange((ShortMessage)localObject);
/*     */           }
/*     */           catch (Throwable localThrowable3)
/*     */           {
/*     */           }
/*     */       }
/* 150 */       return;
/*     */     }
/*     */ 
/* 153 */     Printer.err("Unknown event type: " + paramEventInfo.getEvent());
/*     */   }
/*     */ 
/*     */   void dispatchEvents()
/*     */   {
/* 167 */     EventInfo localEventInfo = null;
/*     */ 
/* 169 */     synchronized (this)
/*     */     {
/*     */       try
/*     */       {
/* 174 */         if (this.eventQueue.size() == 0)
/* 175 */           if ((this.autoClosingClips.size() > 0) || (this.lineMonitors.size() > 0)) {
/* 176 */             int i = 5000;
/* 177 */             if (this.lineMonitors.size() > 0) {
/* 178 */               i = 400;
/*     */             }
/* 180 */             wait(i);
/*     */           } else {
/* 182 */             wait();
/*     */           }
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/* 187 */       if (this.eventQueue.size() > 0)
/*     */       {
/* 189 */         localEventInfo = (EventInfo)this.eventQueue.remove(0);
/*     */       }
/*     */     }
/*     */ 
/* 193 */     if (localEventInfo != null) {
/* 194 */       processEvent(localEventInfo);
/*     */     } else {
/* 196 */       if (this.autoClosingClips.size() > 0) {
/* 197 */         closeAutoClosingClips();
/*     */       }
/* 199 */       if (this.lineMonitors.size() > 0)
/* 200 */         monitorLines();
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized void postEvent(EventInfo paramEventInfo)
/*     */   {
/* 210 */     this.eventQueue.add(paramEventInfo);
/* 211 */     notifyAll();
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     while (true)
/*     */       try
/*     */       {
/* 222 */         dispatchEvents();
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   void sendAudioEvents(Object paramObject, List paramList)
/*     */   {
/* 234 */     if ((paramList == null) || (paramList.size() == 0))
/*     */     {
/* 237 */       return;
/*     */     }
/*     */ 
/* 240 */     start();
/*     */ 
/* 242 */     EventInfo localEventInfo = new EventInfo(paramObject, paramList);
/* 243 */     postEvent(localEventInfo);
/*     */   }
/*     */ 
/*     */   private void closeAutoClosingClips()
/*     */   {
/* 254 */     synchronized (this.autoClosingClips)
/*     */     {
/* 256 */       long l = System.currentTimeMillis();
/* 257 */       for (int i = this.autoClosingClips.size() - 1; i >= 0; i--) {
/* 258 */         ClipInfo localClipInfo = (ClipInfo)this.autoClosingClips.get(i);
/* 259 */         if (localClipInfo.isExpired(l)) {
/* 260 */           AutoClosingClip localAutoClosingClip = localClipInfo.getClip();
/*     */ 
/* 262 */           if ((!localAutoClosingClip.isOpen()) || (!localAutoClosingClip.isAutoClosing()))
/*     */           {
/* 264 */             this.autoClosingClips.remove(i);
/*     */           }
/* 266 */           else if ((!localAutoClosingClip.isRunning()) && (!localAutoClosingClip.isActive()) && (localAutoClosingClip.isAutoClosing()))
/*     */           {
/* 268 */             localAutoClosingClip.close();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getAutoClosingClipIndex(AutoClosingClip paramAutoClosingClip)
/*     */   {
/* 283 */     synchronized (this.autoClosingClips) {
/* 284 */       for (int i = this.autoClosingClips.size() - 1; i >= 0; i--) {
/* 285 */         if (paramAutoClosingClip.equals(((ClipInfo)this.autoClosingClips.get(i)).getClip())) {
/* 286 */           return i;
/*     */         }
/*     */       }
/*     */     }
/* 290 */     return -1;
/*     */   }
/*     */ 
/*     */   void autoClosingClipOpened(AutoClosingClip paramAutoClosingClip)
/*     */   {
/* 298 */     int i = 0;
/* 299 */     synchronized (this.autoClosingClips) {
/* 300 */       i = getAutoClosingClipIndex(paramAutoClosingClip);
/* 301 */       if (i == -1)
/*     */       {
/* 303 */         this.autoClosingClips.add(new ClipInfo(paramAutoClosingClip));
/*     */       }
/*     */     }
/* 306 */     if (i == -1)
/* 307 */       synchronized (this)
/*     */       {
/* 312 */         notifyAll();
/*     */       }
/*     */   }
/*     */ 
/*     */   void autoClosingClipClosed(AutoClosingClip paramAutoClosingClip)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void monitorLines()
/*     */   {
/* 334 */     synchronized (this.lineMonitors)
/*     */     {
/* 336 */       for (int i = 0; i < this.lineMonitors.size(); i++)
/* 337 */         ((LineMonitor)this.lineMonitors.get(i)).checkLine();
/*     */     }
/*     */   }
/*     */ 
/*     */   void addLineMonitor(LineMonitor paramLineMonitor)
/*     */   {
/* 349 */     synchronized (this.lineMonitors) {
/* 350 */       if (this.lineMonitors.indexOf(paramLineMonitor) >= 0)
/*     */       {
/* 352 */         return;
/*     */       }
/*     */ 
/* 355 */       this.lineMonitors.add(paramLineMonitor);
/*     */     }
/* 357 */     synchronized (this)
/*     */     {
/* 359 */       notifyAll();
/*     */     }
/*     */   }
/*     */ 
/*     */   void removeLineMonitor(LineMonitor paramLineMonitor)
/*     */   {
/* 369 */     synchronized (this.lineMonitors) {
/* 370 */       if (this.lineMonitors.indexOf(paramLineMonitor) < 0)
/*     */       {
/* 372 */         return;
/*     */       }
/*     */ 
/* 375 */       this.lineMonitors.remove(paramLineMonitor);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ClipInfo
/*     */   {
/*     */     private final AutoClosingClip clip;
/*     */     private final long expiration;
/*     */ 
/*     */     ClipInfo(AutoClosingClip arg2)
/*     */     {
/*     */       Object localObject;
/* 427 */       this.clip = localObject;
/* 428 */       this.expiration = (System.currentTimeMillis() + 5000L);
/*     */     }
/*     */ 
/*     */     AutoClosingClip getClip() {
/* 432 */       return this.clip;
/*     */     }
/*     */ 
/*     */     boolean isExpired(long paramLong) {
/* 436 */       return paramLong > this.expiration;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EventInfo
/*     */   {
/*     */     private final Object event;
/*     */     private final Object[] listeners;
/*     */ 
/*     */     EventInfo(Object paramList, List arg3)
/*     */     {
/* 396 */       this.event = paramList;
/*     */       Object localObject;
/* 397 */       this.listeners = localObject.toArray();
/*     */     }
/*     */ 
/*     */     Object getEvent() {
/* 401 */       return this.event;
/*     */     }
/*     */ 
/*     */     int getListenerCount() {
/* 405 */       return this.listeners.length;
/*     */     }
/*     */ 
/*     */     Object getListener(int paramInt) {
/* 409 */       return this.listeners[paramInt];
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract interface LineMonitor
/*     */   {
/*     */     public abstract void checkLine();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.EventDispatcher
 * JD-Core Version:    0.6.2
 */