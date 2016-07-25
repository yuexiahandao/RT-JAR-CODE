/*    */ package com.sun.xml.internal.bind.v2.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ public final class FlattenIterator<T>
/*    */   implements Iterator<T>
/*    */ {
/*    */   private final Iterator<? extends Map<?, ? extends T>> parent;
/* 41 */   private Iterator<? extends T> child = null;
/*    */   private T next;
/*    */ 
/*    */   public FlattenIterator(Iterable<? extends Map<?, ? extends T>> core)
/*    */   {
/* 45 */     this.parent = core.iterator();
/*    */   }
/*    */ 
/*    */   public void remove()
/*    */   {
/* 50 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ 
/*    */   public boolean hasNext() {
/* 54 */     getNext();
/* 55 */     return this.next != null;
/*    */   }
/*    */ 
/*    */   public T next() {
/* 59 */     Object r = this.next;
/* 60 */     this.next = null;
/* 61 */     if (r == null)
/* 62 */       throw new NoSuchElementException();
/* 63 */     return r;
/*    */   }
/*    */ 
/*    */   private void getNext() {
/* 67 */     if (this.next != null) return;
/*    */ 
/* 69 */     if ((this.child != null) && (this.child.hasNext())) {
/* 70 */       this.next = this.child.next();
/* 71 */       return;
/*    */     }
/*    */ 
/* 74 */     if (this.parent.hasNext()) {
/* 75 */       this.child = ((Map)this.parent.next()).values().iterator();
/* 76 */       getNext();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.util.FlattenIterator
 * JD-Core Version:    0.6.2
 */