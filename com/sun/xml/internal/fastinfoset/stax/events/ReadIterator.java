/*    */ package com.sun.xml.internal.fastinfoset.stax.events;
/*    */ 
/*    */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class ReadIterator
/*    */   implements Iterator
/*    */ {
/* 35 */   Iterator iterator = EmptyIterator.getInstance();
/*    */ 
/*    */   public ReadIterator() {
/*    */   }
/*    */ 
/*    */   public ReadIterator(Iterator iterator) {
/* 41 */     if (iterator != null)
/* 42 */       this.iterator = iterator;
/*    */   }
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 47 */     return this.iterator.hasNext();
/*    */   }
/*    */ 
/*    */   public Object next() {
/* 51 */     return this.iterator.next();
/*    */   }
/*    */ 
/*    */   public void remove() {
/* 55 */     throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.readonlyList"));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.events.ReadIterator
 * JD-Core Version:    0.6.2
 */