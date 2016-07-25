/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*    */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ final class RuntimeAnyTypeImpl extends AnyTypeImpl<Type, Class>
/*    */   implements RuntimeNonElement
/*    */ {
/* 45 */   static final RuntimeNonElement theInstance = new RuntimeAnyTypeImpl();
/*    */ 
/*    */   private RuntimeAnyTypeImpl()
/*    */   {
/* 38 */     super(Utils.REFLECTION_NAVIGATOR);
/*    */   }
/*    */ 
/*    */   public <V> Transducer<V> getTransducer() {
/* 42 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeAnyTypeImpl
 * JD-Core Version:    0.6.2
 */