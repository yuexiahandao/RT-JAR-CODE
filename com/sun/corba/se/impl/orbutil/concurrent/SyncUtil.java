/*    */ package com.sun.corba.se.impl.orbutil.concurrent;
/*    */ 
/*    */ public class SyncUtil
/*    */ {
/*    */   public static void acquire(Sync paramSync)
/*    */   {
/* 39 */     int i = 0;
/* 40 */     while (i == 0)
/*    */       try {
/* 42 */         paramSync.acquire();
/* 43 */         i = 1;
/*    */       } catch (InterruptedException localInterruptedException) {
/* 45 */         i = 0;
/*    */       }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.concurrent.SyncUtil
 * JD-Core Version:    0.6.2
 */