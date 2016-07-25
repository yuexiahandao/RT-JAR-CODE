/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.EventListener;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import javax.swing.event.EventListenerList;
/*     */ 
/*     */ public class Timer
/*     */   implements Serializable
/*     */ {
/* 155 */   protected EventListenerList listenerList = new EventListenerList();
/*     */ 
/* 170 */   private final transient AtomicBoolean notify = new AtomicBoolean(false);
/*     */   private volatile int initialDelay;
/*     */   private volatile int delay;
/* 173 */   private volatile boolean repeats = true; private volatile boolean coalesce = true;
/*     */   private final transient Runnable doPostEvent;
/*     */   private static volatile boolean logTimers;
/* 179 */   private final transient Lock lock = new ReentrantLock();
/*     */ 
/* 185 */   transient TimerQueue.DelayedTimer delayedTimer = null;
/*     */   private volatile String actionCommand;
/* 218 */   private volatile transient AccessControlContext acc = AccessController.getContext();
/*     */ 
/*     */   public Timer(int paramInt, ActionListener paramActionListener)
/*     */   {
/* 205 */     this.delay = paramInt;
/* 206 */     this.initialDelay = paramInt;
/*     */ 
/* 208 */     this.doPostEvent = new DoPostEvent();
/*     */ 
/* 210 */     if (paramActionListener != null)
/* 211 */       addActionListener(paramActionListener);
/*     */   }
/*     */ 
/*     */   final AccessControlContext getAccessControlContext()
/*     */   {
/* 225 */     if (this.acc == null) {
/* 226 */       throw new SecurityException("Timer is missing AccessControlContext");
/*     */     }
/*     */ 
/* 229 */     return this.acc;
/*     */   }
/*     */ 
/*     */   public void addActionListener(ActionListener paramActionListener)
/*     */   {
/* 266 */     this.listenerList.add(ActionListener.class, paramActionListener);
/*     */   }
/*     */ 
/*     */   public void removeActionListener(ActionListener paramActionListener)
/*     */   {
/* 276 */     this.listenerList.remove(ActionListener.class, paramActionListener);
/*     */   }
/*     */ 
/*     */   public ActionListener[] getActionListeners()
/*     */   {
/* 293 */     return (ActionListener[])this.listenerList.getListeners(ActionListener.class);
/*     */   }
/*     */ 
/*     */   protected void fireActionPerformed(ActionEvent paramActionEvent)
/*     */   {
/* 306 */     Object[] arrayOfObject = this.listenerList.getListenerList();
/*     */ 
/* 310 */     for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 311 */       if (arrayOfObject[i] == ActionListener.class)
/* 312 */         ((ActionListener)arrayOfObject[(i + 1)]).actionPerformed(paramActionEvent);
/*     */   }
/*     */ 
/*     */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*     */   {
/* 355 */     return this.listenerList.getListeners(paramClass);
/*     */   }
/*     */ 
/*     */   private TimerQueue timerQueue()
/*     */   {
/* 362 */     return TimerQueue.sharedInstance();
/*     */   }
/*     */ 
/*     */   public static void setLogTimers(boolean paramBoolean)
/*     */   {
/* 374 */     logTimers = paramBoolean;
/*     */   }
/*     */ 
/*     */   public static boolean getLogTimers()
/*     */   {
/* 385 */     return logTimers;
/*     */   }
/*     */ 
/*     */   public void setDelay(int paramInt)
/*     */   {
/* 398 */     if (paramInt < 0) {
/* 399 */       throw new IllegalArgumentException("Invalid delay: " + paramInt);
/*     */     }
/*     */ 
/* 402 */     this.delay = paramInt;
/*     */   }
/*     */ 
/*     */   public int getDelay()
/*     */   {
/* 415 */     return this.delay;
/*     */   }
/*     */ 
/*     */   public void setInitialDelay(int paramInt)
/*     */   {
/* 431 */     if (paramInt < 0) {
/* 432 */       throw new IllegalArgumentException("Invalid initial delay: " + paramInt);
/*     */     }
/*     */ 
/* 436 */     this.initialDelay = paramInt;
/*     */   }
/*     */ 
/*     */   public int getInitialDelay()
/*     */   {
/* 448 */     return this.initialDelay;
/*     */   }
/*     */ 
/*     */   public void setRepeats(boolean paramBoolean)
/*     */   {
/* 461 */     this.repeats = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isRepeats()
/*     */   {
/* 474 */     return this.repeats;
/*     */   }
/*     */ 
/*     */   public void setCoalesce(boolean paramBoolean)
/*     */   {
/* 495 */     boolean bool = this.coalesce;
/* 496 */     this.coalesce = paramBoolean;
/* 497 */     if ((!bool) && (this.coalesce))
/*     */     {
/* 501 */       cancelEvent();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isCoalesce()
/*     */   {
/* 513 */     return this.coalesce;
/*     */   }
/*     */ 
/*     */   public void setActionCommand(String paramString)
/*     */   {
/* 526 */     this.actionCommand = paramString;
/*     */   }
/*     */ 
/*     */   public String getActionCommand()
/*     */   {
/* 539 */     return this.actionCommand;
/*     */   }
/*     */ 
/*     */   public void start()
/*     */   {
/* 551 */     timerQueue().addTimer(this, getInitialDelay());
/*     */   }
/*     */ 
/*     */   public boolean isRunning()
/*     */   {
/* 561 */     return timerQueue().containsTimer(this);
/*     */   }
/*     */ 
/*     */   public void stop()
/*     */   {
/* 573 */     getLock().lock();
/*     */     try {
/* 575 */       cancelEvent();
/* 576 */       timerQueue().removeTimer(this);
/*     */     } finally {
/* 578 */       getLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void restart()
/*     */   {
/* 589 */     getLock().lock();
/*     */     try {
/* 591 */       stop();
/* 592 */       start();
/*     */     } finally {
/* 594 */       getLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   void cancelEvent()
/*     */   {
/* 605 */     this.notify.set(false);
/*     */   }
/*     */ 
/*     */   void post()
/*     */   {
/* 610 */     if ((this.notify.compareAndSet(false, true)) || (!this.coalesce))
/* 611 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Void run() {
/* 613 */           SwingUtilities.invokeLater(Timer.this.doPostEvent);
/* 614 */           return null;
/*     */         }
/*     */       }
/*     */       , getAccessControlContext());
/*     */   }
/*     */ 
/*     */   Lock getLock()
/*     */   {
/* 621 */     return this.lock;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 627 */     this.acc = AccessController.getContext();
/* 628 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ 
/*     */   private Object readResolve()
/*     */   {
/* 636 */     Timer localTimer = new Timer(getDelay(), null);
/* 637 */     localTimer.listenerList = this.listenerList;
/* 638 */     localTimer.initialDelay = this.initialDelay;
/* 639 */     localTimer.delay = this.delay;
/* 640 */     localTimer.repeats = this.repeats;
/* 641 */     localTimer.coalesce = this.coalesce;
/* 642 */     localTimer.actionCommand = this.actionCommand;
/* 643 */     return localTimer;
/*     */   }
/*     */ 
/*     */   class DoPostEvent
/*     */     implements Runnable
/*     */   {
/*     */     DoPostEvent()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/* 240 */       if (Timer.logTimers) {
/* 241 */         System.out.println("Timer ringing: " + Timer.this);
/*     */       }
/* 243 */       if (Timer.this.notify.get()) {
/* 244 */         Timer.this.fireActionPerformed(new ActionEvent(Timer.this, 0, Timer.this.getActionCommand(), System.currentTimeMillis(), 0));
/*     */ 
/* 247 */         if (Timer.this.coalesce)
/* 248 */           Timer.this.cancelEvent();
/*     */       }
/*     */     }
/*     */ 
/*     */     Timer getTimer()
/*     */     {
/* 254 */       return Timer.this;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.Timer
 * JD-Core Version:    0.6.2
 */