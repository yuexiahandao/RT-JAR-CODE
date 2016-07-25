/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeValuePropertyInfo;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ 
/*    */ final class RuntimeValuePropertyInfoImpl extends ValuePropertyInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeValuePropertyInfo
/*    */ {
/*    */   RuntimeValuePropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed)
/*    */   {
/* 43 */     super(classInfo, seed);
/*    */   }
/*    */ 
/*    */   public boolean elementOnlyContent() {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   public RuntimePropertyInfo getSource() {
/* 51 */     return (RuntimePropertyInfo)super.getSource();
/*    */   }
/*    */ 
/*    */   public RuntimeNonElement getTarget() {
/* 55 */     return (RuntimeNonElement)super.getTarget();
/*    */   }
/*    */ 
/*    */   public List<? extends RuntimeNonElement> ref() {
/* 59 */     return super.ref();
/*    */   }
/*    */ 
/*    */   public void link() {
/* 63 */     getTransducer();
/* 64 */     super.link();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeValuePropertyInfoImpl
 * JD-Core Version:    0.6.2
 */