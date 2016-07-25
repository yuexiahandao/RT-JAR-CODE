/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
/*    */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*    */ import java.lang.reflect.Type;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ final class RuntimeTypeRefImpl extends TypeRefImpl<Type, Class>
/*    */   implements RuntimeTypeRef
/*    */ {
/*    */   public RuntimeTypeRefImpl(RuntimeElementPropertyInfoImpl elementPropertyInfo, QName elementName, Type type, boolean isNillable, String defaultValue)
/*    */   {
/* 43 */     super(elementPropertyInfo, elementName, type, isNillable, defaultValue);
/*    */   }
/*    */ 
/*    */   public RuntimeNonElement getTarget() {
/* 47 */     return (RuntimeNonElement)super.getTarget();
/*    */   }
/*    */ 
/*    */   public Transducer getTransducer() {
/* 51 */     return RuntimeModelBuilder.createTransducer(this);
/*    */   }
/*    */ 
/*    */   public RuntimePropertyInfo getSource() {
/* 55 */     return (RuntimePropertyInfo)this.owner;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeTypeRefImpl
 * JD-Core Version:    0.6.2
 */