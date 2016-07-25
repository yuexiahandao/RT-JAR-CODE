/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*    */ import com.sun.xml.internal.bind.v2.model.core.ValuePropertyInfo;
/*    */ 
/*    */ class ValuePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends SingleTypePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*    */   implements ValuePropertyInfo<TypeT, ClassDeclT>
/*    */ {
/*    */   ValuePropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> seed)
/*    */   {
/* 42 */     super(parent, seed);
/*    */   }
/*    */ 
/*    */   public PropertyKind kind() {
/* 46 */     return PropertyKind.VALUE;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.ValuePropertyInfoImpl
 * JD-Core Version:    0.6.2
 */