/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ final class RuntimeTypeInfoSetImpl extends TypeInfoSetImpl<Type, Class, Field, Method>
/*    */   implements RuntimeTypeInfoSet
/*    */ {
/*    */   public RuntimeTypeInfoSetImpl(AnnotationReader<Type, Class, Field, Method> reader)
/*    */   {
/* 47 */     super(Utils.REFLECTION_NAVIGATOR, reader, RuntimeBuiltinLeafInfoImpl.LEAVES);
/*    */   }
/*    */ 
/*    */   protected RuntimeNonElement createAnyType()
/*    */   {
/* 52 */     return RuntimeAnyTypeImpl.theInstance;
/*    */   }
/*    */ 
/*    */   public RuntimeNonElement getTypeInfo(Type type) {
/* 56 */     return (RuntimeNonElement)super.getTypeInfo(type);
/*    */   }
/*    */ 
/*    */   public RuntimeNonElement getAnyTypeInfo() {
/* 60 */     return (RuntimeNonElement)super.getAnyTypeInfo();
/*    */   }
/*    */ 
/*    */   public RuntimeNonElement getClassInfo(Class clazz) {
/* 64 */     return (RuntimeNonElement)super.getClassInfo(clazz);
/*    */   }
/*    */ 
/*    */   public Map<Class, RuntimeClassInfoImpl> beans() {
/* 68 */     return super.beans();
/*    */   }
/*    */ 
/*    */   public Map<Type, RuntimeBuiltinLeafInfoImpl<?>> builtins() {
/* 72 */     return super.builtins();
/*    */   }
/*    */ 
/*    */   public Map<Class, RuntimeEnumLeafInfoImpl<?, ?>> enums() {
/* 76 */     return super.enums();
/*    */   }
/*    */ 
/*    */   public Map<Class, RuntimeArrayInfoImpl> arrays() {
/* 80 */     return super.arrays();
/*    */   }
/*    */ 
/*    */   public RuntimeElementInfoImpl getElementInfo(Class scope, QName name) {
/* 84 */     return (RuntimeElementInfoImpl)super.getElementInfo(scope, name);
/*    */   }
/*    */ 
/*    */   public Map<QName, RuntimeElementInfoImpl> getElementMappings(Class scope) {
/* 88 */     return super.getElementMappings(scope);
/*    */   }
/*    */ 
/*    */   public Iterable<RuntimeElementInfoImpl> getAllElements() {
/* 92 */     return super.getAllElements();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeTypeInfoSetImpl
 * JD-Core Version:    0.6.2
 */