/*    */ package com.sun.beans.decoder;
/*    */ 
/*    */ final class ValueObjectImpl
/*    */   implements ValueObject
/*    */ {
/* 36 */   static final ValueObject NULL = new ValueObjectImpl(null);
/* 37 */   static final ValueObject VOID = new ValueObjectImpl();
/*    */   private Object value;
/*    */   private boolean isVoid;
/*    */ 
/*    */   static ValueObject create(Object paramObject)
/*    */   {
/* 46 */     return paramObject != null ? new ValueObjectImpl(paramObject) : NULL;
/*    */   }
/*    */ 
/*    */   private ValueObjectImpl()
/*    */   {
/* 58 */     this.isVoid = true;
/*    */   }
/*    */ 
/*    */   private ValueObjectImpl(Object paramObject)
/*    */   {
/* 67 */     this.value = paramObject;
/*    */   }
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 76 */     return this.value;
/*    */   }
/*    */ 
/*    */   public boolean isVoid()
/*    */   {
/* 86 */     return this.isVoid;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.ValueObjectImpl
 * JD-Core Version:    0.6.2
 */