/*    */ package com.sun.xml.internal.bind.v2;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 35 */   ILLEGAL_ENTRY, 
/* 36 */   ERROR_LOADING_CLASS, 
/* 37 */   INVALID_PROPERTY_VALUE, 
/* 38 */   UNSUPPORTED_PROPERTY, 
/* 39 */   BROKEN_CONTEXTPATH, 
/* 40 */   NO_DEFAULT_CONSTRUCTOR_IN_INNER_CLASS, 
/* 41 */   INVALID_TYPE_IN_MAP;
/*    */ 
/* 44 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */ 
/*    */   public String toString() {
/* 47 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 51 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.Messages
 * JD-Core Version:    0.6.2
 */