/*    */ package com.sun.org.apache.xalan.internal.xsltc.runtime;
/*    */ 
/*    */ class HashtableEntry
/*    */ {
/*    */   int hash;
/*    */   Object key;
/*    */   Object value;
/*    */   HashtableEntry next;
/*    */ 
/*    */   protected Object clone()
/*    */   {
/* 46 */     HashtableEntry entry = new HashtableEntry();
/* 47 */     entry.hash = this.hash;
/* 48 */     entry.key = this.key;
/* 49 */     entry.value = this.value;
/* 50 */     entry.next = (this.next != null ? (HashtableEntry)this.next.clone() : null);
/* 51 */     return entry;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.runtime.HashtableEntry
 * JD-Core Version:    0.6.2
 */