/*    */ package com.sun.xml.internal.bind.v2.runtime.property;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 35 */   UNSUBSTITUTABLE_TYPE, 
/* 36 */   UNEXPECTED_JAVA_TYPE;
/*    */ 
/* 39 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */ 
/*    */   public String toString() {
/* 42 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 46 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.Messages
 * JD-Core Version:    0.6.2
 */