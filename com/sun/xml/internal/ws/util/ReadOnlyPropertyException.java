/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ public class ReadOnlyPropertyException extends IllegalArgumentException
/*    */ {
/*    */   private final String propertyName;
/*    */ 
/*    */   public ReadOnlyPropertyException(String propertyName)
/*    */   {
/* 40 */     super(propertyName + " is a read-only property.");
/* 41 */     this.propertyName = propertyName;
/*    */   }
/*    */ 
/*    */   public String getPropertyName()
/*    */   {
/* 48 */     return this.propertyName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.ReadOnlyPropertyException
 * JD-Core Version:    0.6.2
 */