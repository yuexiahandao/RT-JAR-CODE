/*    */ package com.sun.org.apache.xml.internal.dtm.ref;
/*    */ 
/*    */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*    */ 
/*    */ public final class EmptyIterator
/*    */   implements DTMAxisIterator
/*    */ {
/* 35 */   private static final EmptyIterator INSTANCE = new EmptyIterator();
/*    */ 
/* 37 */   public static DTMAxisIterator getInstance() { return INSTANCE; }
/*    */ 
/*    */   public final int next()
/*    */   {
/* 41 */     return -1;
/*    */   }
/* 43 */   public final DTMAxisIterator reset() { return this; } 
/*    */   public final int getLast() {
/* 45 */     return 0;
/*    */   }
/* 47 */   public final int getPosition() { return 1; } 
/*    */   public final void setMark() {
/*    */   }
/*    */   public final void gotoMark() {
/*    */   }
/*    */   public final DTMAxisIterator setStartNode(int node) {
/* 53 */     return this;
/*    */   }
/* 55 */   public final int getStartNode() { return -1; } 
/*    */   public final boolean isReverse() {
/* 57 */     return false;
/*    */   }
/* 59 */   public final DTMAxisIterator cloneIterator() { return this; } 
/*    */   public final void setRestartable(boolean isRestartable) {
/*    */   }
/*    */   public final int getNodeByPosition(int position) {
/* 63 */     return -1;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.EmptyIterator
 * JD-Core Version:    0.6.2
 */