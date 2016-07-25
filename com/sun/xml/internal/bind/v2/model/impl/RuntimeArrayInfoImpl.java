/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeArrayInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ final class RuntimeArrayInfoImpl extends ArrayInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeArrayInfo
/*    */ {
/*    */   RuntimeArrayInfoImpl(RuntimeModelBuilder builder, Locatable upstream, Class arrayType)
/*    */   {
/* 42 */     super(builder, upstream, arrayType);
/*    */   }
/*    */ 
/*    */   public Class getType() {
/* 46 */     return (Class)super.getType();
/*    */   }
/*    */ 
/*    */   public RuntimeNonElement getItemType() {
/* 50 */     return (RuntimeNonElement)super.getItemType();
/*    */   }
/*    */ 
/*    */   public <V> Transducer<V> getTransducer() {
/* 54 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeArrayInfoImpl
 * JD-Core Version:    0.6.2
 */