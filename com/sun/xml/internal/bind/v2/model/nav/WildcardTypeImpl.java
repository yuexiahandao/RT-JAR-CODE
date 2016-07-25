/*    */ package com.sun.xml.internal.bind.v2.model.nav;
/*    */ 
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.WildcardType;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ final class WildcardTypeImpl
/*    */   implements WildcardType
/*    */ {
/*    */   private final Type[] ub;
/*    */   private final Type[] lb;
/*    */ 
/*    */   public WildcardTypeImpl(Type[] ub, Type[] lb)
/*    */   {
/* 41 */     this.ub = ub;
/* 42 */     this.lb = lb;
/*    */   }
/*    */ 
/*    */   public Type[] getUpperBounds() {
/* 46 */     return this.ub;
/*    */   }
/*    */ 
/*    */   public Type[] getLowerBounds() {
/* 50 */     return this.lb;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 54 */     return Arrays.hashCode(this.lb) ^ Arrays.hashCode(this.ub);
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 58 */     if ((obj instanceof WildcardType)) {
/* 59 */       WildcardType that = (WildcardType)obj;
/* 60 */       return (Arrays.equals(that.getLowerBounds(), this.lb)) && (Arrays.equals(that.getUpperBounds(), this.ub));
/*    */     }
/*    */ 
/* 63 */     return false;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.nav.WildcardTypeImpl
 * JD-Core Version:    0.6.2
 */