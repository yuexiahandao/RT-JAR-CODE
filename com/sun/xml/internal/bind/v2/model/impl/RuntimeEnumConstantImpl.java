/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ final class RuntimeEnumConstantImpl extends EnumConstantImpl<Type, Class, Field, Method>
/*    */ {
/*    */   public RuntimeEnumConstantImpl(RuntimeEnumLeafInfoImpl owner, String name, String lexical, EnumConstantImpl<Type, Class, Field, Method> next)
/*    */   {
/* 39 */     super(owner, name, lexical, next);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeEnumConstantImpl
 * JD-Core Version:    0.6.2
 */