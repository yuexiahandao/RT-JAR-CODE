/*    */ package com.sun.script.util;
/*    */ 
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map.Entry;
/*    */ 
/*    */ public class BindingsEntrySet extends AbstractSet<Map.Entry<String, Object>>
/*    */ {
/*    */   private BindingsBase base;
/*    */   private String[] keys;
/*    */ 
/*    */   public BindingsEntrySet(BindingsBase paramBindingsBase)
/*    */   {
/* 41 */     this.base = paramBindingsBase;
/* 42 */     this.keys = paramBindingsBase.getNames();
/*    */   }
/*    */ 
/*    */   public int size() {
/* 46 */     return this.keys.length;
/*    */   }
/*    */ 
/*    */   public Iterator<Map.Entry<String, Object>> iterator() {
/* 50 */     return new BindingsIterator();
/*    */   }
/*    */ 
/*    */   public class BindingsEntry
/*    */     implements Map.Entry<String, Object>
/*    */   {
/*    */     private String key;
/*    */ 
/*    */     public BindingsEntry(String arg2)
/*    */     {
/*    */       Object localObject;
/* 56 */       this.key = localObject;
/*    */     }
/*    */ 
/*    */     public Object setValue(Object paramObject) {
/* 60 */       throw new UnsupportedOperationException();
/*    */     }
/*    */ 
/*    */     public String getKey() {
/* 64 */       return this.key;
/*    */     }
/*    */ 
/*    */     public Object getValue() {
/* 68 */       return BindingsEntrySet.this.base.get(this.key);
/*    */     }
/*    */   }
/*    */ 
/*    */   public class BindingsIterator
/*    */     implements Iterator<Map.Entry<String, Object>>
/*    */   {
/* 75 */     private int current = 0;
/* 76 */     private boolean stale = false;
/*    */ 
/*    */     public BindingsIterator() {  } 
/* 79 */     public boolean hasNext() { return this.current < BindingsEntrySet.this.keys.length; }
/*    */ 
/*    */     public BindingsEntrySet.BindingsEntry next()
/*    */     {
/* 83 */       this.stale = false;
/* 84 */       return new BindingsEntrySet.BindingsEntry(BindingsEntrySet.this, BindingsEntrySet.this.keys[(this.current++)]);
/*    */     }
/*    */ 
/*    */     public void remove() {
/* 88 */       if ((this.stale) || (this.current == 0)) {
/* 89 */         throw new IllegalStateException();
/*    */       }
/*    */ 
/* 92 */       this.stale = true;
/* 93 */       BindingsEntrySet.this.base.remove(BindingsEntrySet.this.keys[(this.current - 1)]);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.script.util.BindingsEntrySet
 * JD-Core Version:    0.6.2
 */