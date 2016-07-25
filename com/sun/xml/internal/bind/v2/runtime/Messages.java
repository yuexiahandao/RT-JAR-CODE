/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import java.text.MessageFormat;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */  enum Messages
/*    */ {
/* 35 */   ILLEGAL_PARAMETER, 
/* 36 */   UNABLE_TO_FIND_CONVERSION_METHOD, 
/* 37 */   MISSING_ID, 
/* 38 */   NOT_IMPLEMENTED_IN_2_0, 
/* 39 */   UNRECOGNIZED_ELEMENT_NAME, 
/* 40 */   TYPE_MISMATCH, 
/* 41 */   MISSING_OBJECT, 
/* 42 */   NOT_IDENTIFIABLE, 
/* 43 */   DANGLING_IDREF, 
/* 44 */   NULL_OUTPUT_RESOLVER, 
/* 45 */   UNABLE_TO_MARSHAL_NON_ELEMENT, 
/* 46 */   UNABLE_TO_MARSHAL_UNBOUND_CLASS, 
/* 47 */   UNSUPPORTED_PROPERTY, 
/* 48 */   NULL_PROPERTY_NAME, 
/* 49 */   MUST_BE_X, 
/* 50 */   NOT_MARSHALLABLE, 
/* 51 */   UNSUPPORTED_RESULT, 
/* 52 */   UNSUPPORTED_ENCODING, 
/* 53 */   SUBSTITUTED_BY_ANONYMOUS_TYPE, 
/* 54 */   CYCLE_IN_MARSHALLER, 
/* 55 */   UNABLE_TO_DISCOVER_EVENTHANDLER, 
/* 56 */   ELEMENT_NEEDED_BUT_FOUND_DOCUMENT, 
/* 57 */   UNKNOWN_CLASS, 
/* 58 */   FAILED_TO_GENERATE_SCHEMA, 
/* 59 */   ERROR_PROCESSING_SCHEMA, 
/* 60 */   ILLEGAL_CONTENT;
/*    */ 
/* 63 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());
/*    */ 
/*    */   public String toString()
/*    */   {
/* 67 */     return format(new Object[0]);
/*    */   }
/*    */ 
/*    */   public String format(Object[] args) {
/* 71 */     return MessageFormat.format(rb.getString(name()), args);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.Messages
 * JD-Core Version:    0.6.2
 */