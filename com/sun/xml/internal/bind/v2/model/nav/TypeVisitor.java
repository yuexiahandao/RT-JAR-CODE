/*    */ package com.sun.xml.internal.bind.v2.model.nav;
/*    */ 
/*    */ import java.lang.reflect.GenericArrayType;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
/*    */ import java.lang.reflect.TypeVariable;
/*    */ import java.lang.reflect.WildcardType;
/*    */ 
/*    */ abstract class TypeVisitor<T, P>
/*    */ {
/*    */   public final T visit(Type t, P param)
/*    */   {
/* 39 */     assert (t != null);
/*    */ 
/* 41 */     if ((t instanceof Class))
/* 42 */       return onClass((Class)t, param);
/* 43 */     if ((t instanceof ParameterizedType))
/* 44 */       return onParameterizdType((ParameterizedType)t, param);
/* 45 */     if ((t instanceof GenericArrayType))
/* 46 */       return onGenericArray((GenericArrayType)t, param);
/* 47 */     if ((t instanceof WildcardType))
/* 48 */       return onWildcard((WildcardType)t, param);
/* 49 */     if ((t instanceof TypeVariable)) {
/* 50 */       return onVariable((TypeVariable)t, param);
/*    */     }
/*    */ 
/* 53 */     if (!$assertionsDisabled) throw new AssertionError();
/* 54 */     throw new IllegalArgumentException();
/*    */   }
/*    */ 
/*    */   protected abstract T onClass(Class paramClass, P paramP);
/*    */ 
/*    */   protected abstract T onParameterizdType(ParameterizedType paramParameterizedType, P paramP);
/*    */ 
/*    */   protected abstract T onGenericArray(GenericArrayType paramGenericArrayType, P paramP);
/*    */ 
/*    */   protected abstract T onVariable(TypeVariable paramTypeVariable, P paramP);
/*    */ 
/*    */   protected abstract T onWildcard(WildcardType paramWildcardType, P paramP);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.nav.TypeVisitor
 * JD-Core Version:    0.6.2
 */