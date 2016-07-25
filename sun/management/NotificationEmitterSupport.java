/*     */ package sun.management;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationEmitter;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ 
/*     */ abstract class NotificationEmitterSupport
/*     */   implements NotificationEmitter
/*     */ {
/*  48 */   private Object listenerLock = new Object();
/*     */ 
/* 195 */   private List<ListenerInfo> listenerList = Collections.emptyList();
/*     */ 
/*     */   public void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */   {
/*  56 */     if (paramNotificationListener == null) {
/*  57 */       throw new IllegalArgumentException("Listener can't be null");
/*     */     }
/*     */ 
/*  73 */     synchronized (this.listenerLock) {
/*  74 */       ArrayList localArrayList = new ArrayList(this.listenerList.size() + 1);
/*  75 */       localArrayList.addAll(this.listenerList);
/*  76 */       localArrayList.add(new ListenerInfo(paramNotificationListener, paramNotificationFilter, paramObject));
/*  77 */       this.listenerList = localArrayList;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(NotificationListener paramNotificationListener)
/*     */     throws ListenerNotFoundException
/*     */   {
/*  84 */     synchronized (this.listenerLock) {
/*  85 */       ArrayList localArrayList = new ArrayList(this.listenerList);
/*     */ 
/*  89 */       for (int i = localArrayList.size() - 1; i >= 0; i--) {
/*  90 */         ListenerInfo localListenerInfo = (ListenerInfo)localArrayList.get(i);
/*     */ 
/*  92 */         if (localListenerInfo.listener == paramNotificationListener)
/*  93 */           localArrayList.remove(i);
/*     */       }
/*  95 */       if (localArrayList.size() == this.listenerList.size())
/*  96 */         throw new ListenerNotFoundException("Listener not registered");
/*  97 */       this.listenerList = localArrayList;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 106 */     int i = 0;
/*     */ 
/* 108 */     synchronized (this.listenerLock) {
/* 109 */       ArrayList localArrayList = new ArrayList(this.listenerList);
/* 110 */       int j = localArrayList.size();
/* 111 */       for (int k = 0; k < j; k++) {
/* 112 */         ListenerInfo localListenerInfo = (ListenerInfo)localArrayList.get(k);
/*     */ 
/* 114 */         if (localListenerInfo.listener == paramNotificationListener) {
/* 115 */           i = 1;
/* 116 */           if ((localListenerInfo.filter == paramNotificationFilter) && (localListenerInfo.handback == paramObject))
/*     */           {
/* 118 */             localArrayList.remove(k);
/* 119 */             this.listenerList = localArrayList;
/* 120 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 126 */     if (i != 0)
/*     */     {
/* 130 */       throw new ListenerNotFoundException("Listener not registered with this filter and handback");
/*     */     }
/*     */ 
/* 134 */     throw new ListenerNotFoundException("Listener not registered");
/*     */   }
/*     */ 
/*     */   void sendNotification(Notification paramNotification)
/*     */   {
/* 140 */     if (paramNotification == null)
/*     */       return;
/*     */     List localList;
/* 145 */     synchronized (this.listenerLock) {
/* 146 */       localList = this.listenerList;
/*     */     }
/*     */ 
/* 149 */     ??? = localList.size();
/* 150 */     for (Object localObject2 = 0; localObject2 < ???; localObject2++) {
/* 151 */       ListenerInfo localListenerInfo = (ListenerInfo)localList.get(localObject2);
/*     */ 
/* 153 */       if ((localListenerInfo.filter == null) || (localListenerInfo.filter.isNotificationEnabled(paramNotification)))
/*     */         try
/*     */         {
/* 156 */           localListenerInfo.listener.handleNotification(paramNotification, localListenerInfo.handback);
/*     */         } catch (Exception localException) {
/* 158 */           localException.printStackTrace();
/* 159 */           throw new AssertionError("Error in invoking listener");
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean hasListeners()
/*     */   {
/* 166 */     synchronized (this.listenerLock) {
/* 167 */       return !this.listenerList.isEmpty();
/*     */     }
/*     */   }
/*     */   public abstract MBeanNotificationInfo[] getNotificationInfo();
/*     */ 
/*     */   private class ListenerInfo {
/*     */     public NotificationListener listener;
/*     */     NotificationFilter filter;
/*     */     Object handback;
/*     */ 
/* 179 */     public ListenerInfo(NotificationListener paramNotificationFilter, NotificationFilter paramObject, Object arg4) { this.listener = paramNotificationFilter;
/* 180 */       this.filter = paramObject;
/*     */       Object localObject;
/* 181 */       this.handback = localObject;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.NotificationEmitterSupport
 * JD-Core Version:    0.6.2
 */