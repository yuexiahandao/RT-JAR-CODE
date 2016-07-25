/*    */ package com.sun.org.apache.xerces.internal.impl.xs;
/*    */ 
/*    */ public class XMLSchemaException extends Exception
/*    */ {
/*    */   static final long serialVersionUID = -9096984648537046218L;
/*    */   String key;
/*    */   Object[] args;
/*    */ 
/*    */   public XMLSchemaException(String key, Object[] args)
/*    */   {
/* 41 */     this.key = key;
/* 42 */     this.args = args;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 46 */     return this.key;
/*    */   }
/*    */ 
/*    */   public Object[] getArgs() {
/* 50 */     return this.args;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaException
 * JD-Core Version:    0.6.2
 */