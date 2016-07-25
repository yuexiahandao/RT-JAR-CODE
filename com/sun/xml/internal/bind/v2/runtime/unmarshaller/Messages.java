/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 35 */   UNRESOLVED_IDREF, 
/* 36 */   UNEXPECTED_ELEMENT, 
/* 37 */   UNEXPECTED_TEXT, 
/* 38 */   NOT_A_QNAME, 
/* 39 */   UNRECOGNIZED_TYPE_NAME, 
/* 40 */   UNRECOGNIZED_TYPE_NAME_MAYBE, 
/* 41 */   UNABLE_TO_CREATE_MAP, 
/* 42 */   UNINTERNED_STRINGS;
/*    */ 
/* 45 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */ 
/*    */   public String toString() {
/* 48 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 52 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.Messages
 * JD-Core Version:    0.6.2
 */