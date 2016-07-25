/*    */ package com.sun.xml.internal.stream.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class ReadOnlyIterator
/*    */   implements Iterator
/*    */ {
/* 36 */   Iterator iterator = null;
/*    */ 
/*    */   public ReadOnlyIterator() {
/*    */   }
/*    */ 
/*    */   public ReadOnlyIterator(Iterator itr) {
/* 42 */     this.iterator = itr;
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 49 */     if (this.iterator != null)
/* 50 */       return this.iterator.hasNext();
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   public Object next()
/*    */   {
/* 58 */     if (this.iterator != null)
/* 59 */       return this.iterator.next();
/* 60 */     return null;
/*    */   }
/*    */ 
/*    */   public void remove() {
/* 64 */     throw new UnsupportedOperationException("Remove operation is not supported");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.util.ReadOnlyIterator
 * JD-Core Version:    0.6.2
 */