/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ import java.lang.reflect.Field;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ class RuntimeElementPropertyInfoImpl extends ElementPropertyInfoImpl<Type, Class, Field, Method>
/*    */   implements RuntimeElementPropertyInfo
/*    */ {
/*    */   private final Accessor acc;
/*    */ 
/*    */   RuntimeElementPropertyInfoImpl(RuntimeClassInfoImpl classInfo, PropertySeed<Type, Class, Field, Method> seed)
/*    */   {
/* 48 */     super(classInfo, seed);
/* 49 */     Accessor rawAcc = ((RuntimeClassInfoImpl.RuntimePropertySeed)seed).getAccessor();
/* 50 */     if ((getAdapter() != null) && (!isCollection()))
/*    */     {
/* 53 */       rawAcc = rawAcc.adapt(getAdapter());
/* 54 */     }this.acc = rawAcc;
/*    */   }
/*    */ 
/*    */   public Accessor getAccessor() {
/* 58 */     return this.acc;
/*    */   }
/*    */ 
/*    */   public boolean elementOnlyContent() {
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   public List<? extends RuntimeTypeInfo> ref() {
/* 66 */     return super.ref();
/*    */   }
/*    */ 
/*    */   protected RuntimeTypeRefImpl createTypeRef(QName name, Type type, boolean isNillable, String defaultValue)
/*    */   {
/* 71 */     return new RuntimeTypeRefImpl(this, name, type, isNillable, defaultValue);
/*    */   }
/*    */ 
/*    */   public List<RuntimeTypeRefImpl> getTypes() {
/* 75 */     return super.getTypes();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeElementPropertyInfoImpl
 * JD-Core Version:    0.6.2
 */