/*    */ package com.sun.org.apache.bcel.internal.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*    */ import java.io.Serializable;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class ClassSet
/*    */   implements Serializable
/*    */ {
/* 73 */   private HashMap _map = new HashMap();
/*    */ 
/*    */   public boolean add(JavaClass clazz) {
/* 76 */     boolean result = false;
/*    */ 
/* 78 */     if (!this._map.containsKey(clazz.getClassName())) {
/* 79 */       result = true;
/* 80 */       this._map.put(clazz.getClassName(), clazz);
/*    */     }
/*    */ 
/* 83 */     return result;
/*    */   }
/*    */   public void remove(JavaClass clazz) {
/* 86 */     this._map.remove(clazz.getClassName()); } 
/* 87 */   public boolean empty() { return this._map.isEmpty(); }
/*    */ 
/*    */   public JavaClass[] toArray() {
/* 90 */     Collection values = this._map.values();
/* 91 */     JavaClass[] classes = new JavaClass[values.size()];
/* 92 */     values.toArray(classes);
/* 93 */     return classes;
/*    */   }
/*    */ 
/*    */   public String[] getClassNames() {
/* 97 */     return (String[])this._map.keySet().toArray(new String[this._map.keySet().size()]);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.ClassSet
 * JD-Core Version:    0.6.2
 */