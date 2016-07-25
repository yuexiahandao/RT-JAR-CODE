/*    */ package com.sun.xml.internal.bind;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.FieldReflection;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterOnlyReflection;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.ReadOnlyFieldReflection;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.SetterOnlyReflection;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public class AccessorFactoryImpl
/*    */   implements InternalAccessorFactory
/*    */ {
/* 37 */   private static AccessorFactoryImpl instance = new AccessorFactoryImpl();
/*    */ 
/*    */   public static AccessorFactoryImpl getInstance()
/*    */   {
/* 41 */     return instance;
/*    */   }
/*    */ 
/*    */   public Accessor createFieldAccessor(Class bean, Field field, boolean readOnly)
/*    */   {
/* 55 */     return readOnly ? new Accessor.ReadOnlyFieldReflection(field) : new Accessor.FieldReflection(field);
/*    */   }
/*    */ 
/*    */   public Accessor createFieldAccessor(Class bean, Field field, boolean readOnly, boolean supressWarning)
/*    */   {
/* 72 */     return readOnly ? new Accessor.ReadOnlyFieldReflection(field, supressWarning) : new Accessor.FieldReflection(field, supressWarning);
/*    */   }
/*    */ 
/*    */   public Accessor createPropertyAccessor(Class bean, Method getter, Method setter)
/*    */   {
/* 88 */     if (getter == null) {
/* 89 */       return new Accessor.SetterOnlyReflection(setter);
/*    */     }
/* 91 */     if (setter == null) {
/* 92 */       return new Accessor.GetterOnlyReflection(getter);
/*    */     }
/* 94 */     return new Accessor.GetterSetterReflection(getter, setter);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.AccessorFactoryImpl
 * JD-Core Version:    0.6.2
 */