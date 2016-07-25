/*    */ package com.sun.org.apache.xerces.internal.impl.dv;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*    */ 
/*    */ public class ValidatedInfo
/*    */ {
/*    */   public String normalizedValue;
/*    */   public Object actualValue;
/*    */   public short actualValueType;
/*    */   public XSSimpleType memberType;
/*    */   public XSSimpleType[] memberTypes;
/*    */   public ShortList itemValueTypes;
/*    */ 
/*    */   public void reset()
/*    */   {
/* 82 */     this.normalizedValue = null;
/* 83 */     this.actualValue = null;
/* 84 */     this.memberType = null;
/* 85 */     this.memberTypes = null;
/*    */   }
/*    */ 
/*    */   public String stringValue()
/*    */   {
/* 93 */     if (this.actualValue == null) {
/* 94 */       return this.normalizedValue;
/*    */     }
/* 96 */     return this.actualValue.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo
 * JD-Core Version:    0.6.2
 */