/*    */ package com.sun.corba.se.impl.orbutil;
/*    */ 
/*    */ import java.util.EmptyStackException;
/*    */ 
/*    */ public class StackImpl
/*    */ {
/* 36 */   private Object[] data = new Object[3];
/* 37 */   private int top = -1;
/*    */ 
/*    */   public final boolean empty()
/*    */   {
/* 41 */     return this.top == -1;
/*    */   }
/*    */ 
/*    */   public final Object peek()
/*    */   {
/* 47 */     if (empty()) {
/* 48 */       throw new EmptyStackException();
/*    */     }
/* 50 */     return this.data[this.top];
/*    */   }
/*    */ 
/*    */   public final Object pop()
/*    */   {
/* 56 */     Object localObject = peek();
/* 57 */     this.data[this.top] = null;
/* 58 */     this.top -= 1;
/* 59 */     return localObject;
/*    */   }
/*    */ 
/*    */   private void ensure()
/*    */   {
/* 64 */     if (this.top == this.data.length - 1) {
/* 65 */       int i = 2 * this.data.length;
/* 66 */       Object[] arrayOfObject = new Object[i];
/* 67 */       System.arraycopy(this.data, 0, arrayOfObject, 0, this.data.length);
/* 68 */       this.data = arrayOfObject;
/*    */     }
/*    */   }
/*    */ 
/*    */   public final Object push(Object paramObject)
/*    */   {
/* 74 */     ensure();
/* 75 */     this.top += 1;
/* 76 */     this.data[this.top] = paramObject;
/* 77 */     return paramObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.StackImpl
 * JD-Core Version:    0.6.2
 */