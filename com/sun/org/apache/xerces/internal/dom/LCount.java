/*    */ package com.sun.org.apache.xerces.internal.dom;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ class LCount
/*    */ {
/* 47 */   static Hashtable lCounts = new Hashtable();
/* 48 */   public int captures = 0; public int bubbles = 0;
/*    */   public int defaults;
/* 48 */   public int total = 0;
/*    */ 
/*    */   static LCount lookup(String evtName)
/*    */   {
/* 52 */     LCount lc = (LCount)lCounts.get(evtName);
/* 53 */     if (lc == null)
/* 54 */       lCounts.put(evtName, lc = new LCount());
/* 55 */     return lc;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.LCount
 * JD-Core Version:    0.6.2
 */