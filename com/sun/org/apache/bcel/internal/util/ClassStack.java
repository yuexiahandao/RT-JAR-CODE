/*    */ package com.sun.org.apache.bcel.internal.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*    */ import java.io.Serializable;
/*    */ import java.util.Stack;
/*    */ 
/*    */ public class ClassStack
/*    */   implements Serializable
/*    */ {
/* 70 */   private Stack stack = new Stack();
/*    */ 
/* 72 */   public void push(JavaClass clazz) { this.stack.push(clazz); } 
/* 73 */   public JavaClass pop() { return (JavaClass)this.stack.pop(); } 
/* 74 */   public JavaClass top() { return (JavaClass)this.stack.peek(); } 
/* 75 */   public boolean empty() { return this.stack.empty(); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.ClassStack
 * JD-Core Version:    0.6.2
 */