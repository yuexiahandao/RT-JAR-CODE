/*     */ package com.sun.corba.se.impl.activation;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ 
/*     */ public class ProcessMonitorThread extends Thread
/*     */ {
/*     */   private HashMap serverTable;
/*     */   private int sleepTime;
/*  42 */   private static ProcessMonitorThread instance = null;
/*     */ 
/*     */   private ProcessMonitorThread(HashMap paramHashMap, int paramInt) {
/*  45 */     this.serverTable = paramHashMap;
/*  46 */     this.sleepTime = paramInt;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     while (true)
/*     */     {
/*     */       try
/*     */       {
/*  55 */         Thread.sleep(this.sleepTime);
/*     */       } catch (InterruptedException localInterruptedException) {
/*  57 */         break;
/*     */       }
/*     */       Iterator localIterator;
/*  60 */       synchronized (this.serverTable)
/*     */       {
/*  63 */         localIterator = this.serverTable.values().iterator();
/*     */       }
/*     */       try {
/*  66 */         checkServerHealth(localIterator);
/*     */       } catch (ConcurrentModificationException localConcurrentModificationException) {
/*  68 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkServerHealth(Iterator paramIterator) {
/*  74 */     if (paramIterator == null) return;
/*  75 */     while (paramIterator.hasNext()) {
/*  76 */       ServerTableEntry localServerTableEntry = (ServerTableEntry)paramIterator.next();
/*  77 */       localServerTableEntry.checkProcessHealth();
/*     */     }
/*     */   }
/*     */ 
/*     */   static void start(HashMap paramHashMap) {
/*  82 */     int i = 1000;
/*     */ 
/*  84 */     String str = System.getProperties().getProperty("com.sun.CORBA.activation.ServerPollingTime");
/*     */ 
/*  87 */     if (str != null) {
/*     */       try {
/*  89 */         i = Integer.parseInt(str);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/*  96 */     instance = new ProcessMonitorThread(paramHashMap, i);
/*     */ 
/*  98 */     instance.setDaemon(true);
/*  99 */     instance.start();
/*     */   }
/*     */ 
/*     */   static void interruptThread() {
/* 103 */     instance.interrupt();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.activation.ProcessMonitorThread
 * JD-Core Version:    0.6.2
 */