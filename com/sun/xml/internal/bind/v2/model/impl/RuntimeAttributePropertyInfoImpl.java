/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeAttributePropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ 
/*    */ class RuntimeAttributePropertyInfoImpl extends AttributePropertyInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeAttributePropertyInfo
/*    */ {
/*    */   RuntimeAttributePropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed)
/*    */   {
/* 43 */     super(classInfo, seed);
/*    */   }
/*    */ 
/*    */   public boolean elementOnlyContent() {
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   public RuntimeNonElement getTarget() {
/* 51 */     return (RuntimeNonElement)super.getTarget();
/*    */   }
/*    */ 
/*    */   public List<? extends RuntimeNonElement> ref() {
/* 55 */     return super.ref();
/*    */   }
/*    */ 
/*    */   public RuntimePropertyInfo getSource() {
/* 59 */     return this;
/*    */   }
/*    */ 
/*    */   public void link() {
/* 63 */     getTransducer();
/* 64 */     super.link();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeAttributePropertyInfoImpl
 * JD-Core Version:    0.6.2
 */