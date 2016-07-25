/*    */ package com.sun.corba.se.impl.orb;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.Operation;
/*    */ import com.sun.corba.se.spi.orb.ParserData;
/*    */ 
/*    */ public abstract class ParserDataBase
/*    */   implements ParserData
/*    */ {
/*    */   private String propertyName;
/*    */   private Operation operation;
/*    */   private String fieldName;
/*    */   private Object defaultValue;
/*    */   private Object testValue;
/*    */ 
/*    */   protected ParserDataBase(String paramString1, Operation paramOperation, String paramString2, Object paramObject1, Object paramObject2)
/*    */   {
/* 42 */     this.propertyName = paramString1;
/* 43 */     this.operation = paramOperation;
/* 44 */     this.fieldName = paramString2;
/* 45 */     this.defaultValue = paramObject1;
/* 46 */     this.testValue = paramObject2;
/*    */   }
/*    */   public String getPropertyName() {
/* 49 */     return this.propertyName; } 
/* 50 */   public Operation getOperation() { return this.operation; } 
/* 51 */   public String getFieldName() { return this.fieldName; } 
/* 52 */   public Object getDefaultValue() { return this.defaultValue; } 
/* 53 */   public Object getTestValue() { return this.testValue; }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.ParserDataBase
 * JD-Core Version:    0.6.2
 */