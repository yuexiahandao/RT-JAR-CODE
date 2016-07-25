/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 36 */   UNABLE_TO_ACCESS_NON_PUBLIC_FIELD, 
/* 37 */   UNASSIGNABLE_TYPE, 
/* 38 */   NO_SETTER, 
/* 39 */   NO_GETTER;
/*    */ 
/* 42 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */ 
/*    */   public String toString() {
/* 45 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 49 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.Messages
 * JD-Core Version:    0.6.2
 */