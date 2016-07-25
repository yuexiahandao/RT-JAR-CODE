/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElement;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeReferencePropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Set;
/*    */ 
/*    */ class RuntimeReferencePropertyInfoImpl extends ReferencePropertyInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeReferencePropertyInfo
/*    */ {
/*    */   private final Accessor acc;
/*    */ 
/*    */   public RuntimeReferencePropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed)
/*    */   {
/* 46 */     super(classInfo, seed);
/* 47 */     Accessor rawAcc = ((RuntimeClassInfoImpl.RuntimePropertySeed)seed).getAccessor();
/* 48 */     if ((getAdapter() != null) && (!isCollection()))
/*    */     {
/* 51 */       rawAcc = rawAcc.adapt(getAdapter());
/* 52 */     }this.acc = rawAcc;
/*    */   }
/*    */ 
/*    */   public Set<? extends RuntimeElement> getElements() {
/* 56 */     return super.getElements();
/*    */   }
/*    */ 
/*    */   public Set<? extends RuntimeElement> ref() {
/* 60 */     return super.ref();
/*    */   }
/*    */ 
/*    */   public Accessor getAccessor() {
/* 64 */     return this.acc;
/*    */   }
/*    */ 
/*    */   public boolean elementOnlyContent() {
/* 68 */     return !isMixed();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeReferencePropertyInfoImpl
 * JD-Core Version:    0.6.2
 */