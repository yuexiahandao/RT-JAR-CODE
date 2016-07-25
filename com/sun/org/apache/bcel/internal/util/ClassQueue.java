/*    */ package com.sun.org.apache.bcel.internal.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*    */ import java.io.Serializable;
/*    */ import java.util.LinkedList;
/*    */ 
/*    */ public class ClassQueue
/*    */   implements Serializable
/*    */ {
/* 71 */   protected LinkedList vec = new LinkedList();
/*    */ 
/* 73 */   public void enqueue(JavaClass clazz) { this.vec.addLast(clazz); }
/*    */ 
/*    */   public JavaClass dequeue() {
/* 76 */     return (JavaClass)this.vec.removeFirst();
/*    */   }
/*    */   public boolean empty() {
/* 79 */     return this.vec.isEmpty();
/*    */   }
/*    */   public String toString() {
/* 82 */     return this.vec.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.ClassQueue
 * JD-Core Version:    0.6.2
 */