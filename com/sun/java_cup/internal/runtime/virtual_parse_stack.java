/*     */ package com.sun.java_cup.internal.runtime;
/*     */ 
/*     */ import java.util.Stack;
/*     */ 
/*     */ public class virtual_parse_stack
/*     */ {
/*     */   protected Stack real_stack;
/*     */   protected int real_next;
/*     */   protected Stack vstack;
/*     */ 
/*     */   public virtual_parse_stack(Stack shadowing_stack)
/*     */     throws Exception
/*     */   {
/*  55 */     if (shadowing_stack == null) {
/*  56 */       throw new Exception("Internal parser error: attempt to create null virtual stack");
/*     */     }
/*     */ 
/*  60 */     this.real_stack = shadowing_stack;
/*  61 */     this.vstack = new Stack();
/*  62 */     this.real_next = 0;
/*     */ 
/*  65 */     get_from_real();
/*     */   }
/*     */ 
/*     */   protected void get_from_real()
/*     */   {
/* 108 */     if (this.real_next >= this.real_stack.size()) return;
/*     */ 
/* 111 */     Symbol stack_sym = (Symbol)this.real_stack.elementAt(this.real_stack.size() - 1 - this.real_next);
/*     */ 
/* 114 */     this.real_next += 1;
/*     */ 
/* 117 */     this.vstack.push(new Integer(stack_sym.parse_state));
/*     */   }
/*     */ 
/*     */   public boolean empty()
/*     */   {
/* 127 */     return this.vstack.empty();
/*     */   }
/*     */ 
/*     */   public int top()
/*     */     throws Exception
/*     */   {
/* 135 */     if (this.vstack.empty()) {
/* 136 */       throw new Exception("Internal parser error: top() called on empty virtual stack");
/*     */     }
/*     */ 
/* 139 */     return ((Integer)this.vstack.peek()).intValue();
/*     */   }
/*     */ 
/*     */   public void pop()
/*     */     throws Exception
/*     */   {
/* 147 */     if (this.vstack.empty()) {
/* 148 */       throw new Exception("Internal parser error: pop from empty virtual stack");
/*     */     }
/*     */ 
/* 152 */     this.vstack.pop();
/*     */ 
/* 155 */     if (this.vstack.empty())
/* 156 */       get_from_real();
/*     */   }
/*     */ 
/*     */   public void push(int state_num)
/*     */   {
/* 164 */     this.vstack.push(new Integer(state_num));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java_cup.internal.runtime.virtual_parse_stack
 * JD-Core Version:    0.6.2
 */