/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ import java.util.Vector;
/*     */ import javax.naming.event.NamingEvent;
/*     */ import javax.naming.event.NamingExceptionEvent;
/*     */ import javax.naming.event.NamingListener;
/*     */ import javax.naming.ldap.UnsolicitedNotificationEvent;
/*     */ import javax.naming.ldap.UnsolicitedNotificationListener;
/*     */ 
/*     */ final class EventQueue
/*     */   implements Runnable
/*     */ {
/*     */   private static final boolean debug = false;
/*  63 */   private QueueElement head = null;
/*  64 */   private QueueElement tail = null;
/*     */   private Thread qThread;
/*     */ 
/*     */   EventQueue()
/*     */   {
/*  69 */     this.qThread = Obj.helper.createThread(this);
/*  70 */     this.qThread.setDaemon(true);
/*  71 */     this.qThread.start();
/*     */   }
/*     */ 
/*     */   synchronized void enqueue(EventObject paramEventObject, Vector paramVector)
/*     */   {
/*  91 */     QueueElement localQueueElement = new QueueElement(paramEventObject, paramVector);
/*     */ 
/*  93 */     if (this.head == null) {
/*  94 */       this.head = localQueueElement;
/*  95 */       this.tail = localQueueElement;
/*     */     } else {
/*  97 */       localQueueElement.next = this.head;
/*  98 */       this.head.prev = localQueueElement;
/*  99 */       this.head = localQueueElement;
/*     */     }
/* 101 */     notify();
/*     */   }
/*     */ 
/*     */   private synchronized QueueElement dequeue()
/*     */     throws InterruptedException
/*     */   {
/* 114 */     while (this.tail == null)
/* 115 */       wait();
/* 116 */     QueueElement localQueueElement = this.tail;
/* 117 */     this.tail = localQueueElement.prev;
/* 118 */     if (this.tail == null)
/* 119 */       this.head = null;
/*     */     else {
/* 121 */       this.tail.next = null;
/*     */     }
/* 123 */     localQueueElement.prev = (localQueueElement.next = null);
/* 124 */     return localQueueElement;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*     */       QueueElement localQueueElement;
/* 134 */       while ((localQueueElement = dequeue()) != null) {
/* 135 */         EventObject localEventObject = localQueueElement.event;
/* 136 */         Vector localVector = localQueueElement.vector;
/*     */ 
/* 138 */         for (int i = 0; i < localVector.size(); i++)
/*     */         {
/* 147 */           if ((localEventObject instanceof NamingEvent)) {
/* 148 */             ((NamingEvent)localEventObject).dispatch((NamingListener)localVector.elementAt(i));
/*     */           }
/* 151 */           else if ((localEventObject instanceof NamingExceptionEvent)) {
/* 152 */             ((NamingExceptionEvent)localEventObject).dispatch((NamingListener)localVector.elementAt(i));
/*     */           }
/* 154 */           else if ((localEventObject instanceof UnsolicitedNotificationEvent)) {
/* 155 */             ((UnsolicitedNotificationEvent)localEventObject).dispatch((UnsolicitedNotificationListener)localVector.elementAt(i));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 160 */         localQueueElement = null; localEventObject = null; localVector = null;
/*     */       }
/*     */     }
/*     */     catch (InterruptedException localInterruptedException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   void stop()
/*     */   {
/* 173 */     if (this.qThread != null) {
/* 174 */       this.qThread.interrupt();
/* 175 */       this.qThread = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class QueueElement
/*     */   {
/*  52 */     QueueElement next = null;
/*  53 */     QueueElement prev = null;
/*  54 */     EventObject event = null;
/*  55 */     Vector vector = null;
/*     */ 
/*     */     QueueElement(EventObject paramEventObject, Vector paramVector) {
/*  58 */       this.event = paramEventObject;
/*  59 */       this.vector = paramVector;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.EventQueue
 * JD-Core Version:    0.6.2
 */