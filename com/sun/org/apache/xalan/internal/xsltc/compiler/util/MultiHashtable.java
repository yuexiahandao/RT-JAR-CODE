/*    */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public final class MultiHashtable extends Hashtable
/*    */ {
/*    */   static final long serialVersionUID = -6151608290510033572L;
/*    */ 
/*    */   public Object put(Object key, Object value)
/*    */   {
/* 36 */     Vector vector = (Vector)get(key);
/* 37 */     if (vector == null)
/* 38 */       super.put(key, vector = new Vector());
/* 39 */     vector.add(value);
/* 40 */     return vector;
/*    */   }
/*    */ 
/*    */   public Object maps(Object from, Object to) {
/* 44 */     if (from == null) return null;
/* 45 */     Vector vector = (Vector)get(from);
/* 46 */     if (vector != null) {
/* 47 */       int n = vector.size();
/* 48 */       for (int i = 0; i < n; i++) {
/* 49 */         Object item = vector.elementAt(i);
/* 50 */         if (item.equals(to)) {
/* 51 */           return item;
/*    */         }
/*    */       }
/*    */     }
/* 55 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.MultiHashtable
 * JD-Core Version:    0.6.2
 */