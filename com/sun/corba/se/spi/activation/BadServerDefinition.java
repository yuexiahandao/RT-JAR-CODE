/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.UserException;
/*    */ 
/*    */ public final class BadServerDefinition extends UserException
/*    */ {
/* 13 */   public String reason = null;
/*    */ 
/*    */   public BadServerDefinition()
/*    */   {
/* 17 */     super(BadServerDefinitionHelper.id());
/*    */   }
/*    */ 
/*    */   public BadServerDefinition(String paramString)
/*    */   {
/* 22 */     super(BadServerDefinitionHelper.id());
/* 23 */     this.reason = paramString;
/*    */   }
/*    */ 
/*    */   public BadServerDefinition(String paramString1, String paramString2)
/*    */   {
/* 29 */     super(BadServerDefinitionHelper.id() + "  " + paramString1);
/* 30 */     this.reason = paramString2;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.BadServerDefinition
 * JD-Core Version:    0.6.2
 */