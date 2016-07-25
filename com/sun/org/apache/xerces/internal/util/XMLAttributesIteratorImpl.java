/*    */ package com.sun.org.apache.xerces.internal.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ 
/*    */ public class XMLAttributesIteratorImpl extends XMLAttributesImpl
/*    */   implements Iterator
/*    */ {
/* 53 */   protected int fCurrent = 0;
/*    */   protected XMLAttributesImpl.Attribute fLastReturnedItem;
/*    */ 
/*    */   public boolean hasNext()
/*    */   {
/* 62 */     return this.fCurrent < getLength();
/*    */   }
/*    */ 
/*    */   public Object next() {
/* 66 */     if (hasNext())
/*    */     {
/* 68 */       return this.fLastReturnedItem = this.fAttributes[(this.fCurrent++)];
/*    */     }
/*    */ 
/* 71 */     throw new NoSuchElementException();
/*    */   }
/*    */ 
/*    */   public void remove()
/*    */   {
/* 77 */     if (this.fLastReturnedItem == this.fAttributes[(this.fCurrent - 1)])
/*    */     {
/* 79 */       removeAttributeAt(this.fCurrent--);
/*    */     }
/*    */     else
/*    */     {
/* 84 */       throw new IllegalStateException();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void removeAllAttributes() {
/* 89 */     super.removeAllAttributes();
/* 90 */     this.fCurrent = 0;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLAttributesIteratorImpl
 * JD-Core Version:    0.6.2
 */