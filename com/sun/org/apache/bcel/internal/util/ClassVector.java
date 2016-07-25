/*    */ package com.sun.org.apache.bcel.internal.util;
/*    */ 
/*    */ import com.sun.org.apache.bcel.internal.classfile.JavaClass;
/*    */ import java.io.Serializable;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public class ClassVector
/*    */   implements Serializable
/*    */ {
/* 71 */   protected ArrayList vec = new ArrayList();
/*    */ 
/* 73 */   public void addElement(JavaClass clazz) { this.vec.add(clazz); } 
/* 74 */   public JavaClass elementAt(int index) { return (JavaClass)this.vec.get(index); } 
/* 75 */   public void removeElementAt(int index) { this.vec.remove(index); }
/*    */ 
/*    */   public JavaClass[] toArray() {
/* 78 */     JavaClass[] classes = new JavaClass[this.vec.size()];
/* 79 */     this.vec.toArray(classes);
/* 80 */     return classes;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.util.ClassVector
 * JD-Core Version:    0.6.2
 */