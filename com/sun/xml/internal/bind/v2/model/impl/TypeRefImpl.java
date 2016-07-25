/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*    */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.core.TypeRef;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ class TypeRefImpl<TypeT, ClassDeclT>
/*    */   implements TypeRef<TypeT, ClassDeclT>
/*    */ {
/*    */   private final QName elementName;
/*    */   private final TypeT type;
/*    */   protected final ElementPropertyInfoImpl<TypeT, ClassDeclT, ?, ?> owner;
/*    */   private NonElement<TypeT, ClassDeclT> ref;
/*    */   private final boolean isNillable;
/*    */   private String defaultValue;
/*    */ 
/*    */   public TypeRefImpl(ElementPropertyInfoImpl<TypeT, ClassDeclT, ?, ?> owner, QName elementName, TypeT type, boolean isNillable, String defaultValue)
/*    */   {
/* 46 */     this.owner = owner;
/* 47 */     this.elementName = elementName;
/* 48 */     this.type = type;
/* 49 */     this.isNillable = isNillable;
/* 50 */     this.defaultValue = defaultValue;
/* 51 */     assert (owner != null);
/* 52 */     assert (elementName != null);
/* 53 */     assert (type != null);
/*    */   }
/*    */ 
/*    */   public NonElement<TypeT, ClassDeclT> getTarget() {
/* 57 */     if (this.ref == null)
/* 58 */       calcRef();
/* 59 */     return this.ref;
/*    */   }
/*    */ 
/*    */   public QName getTagName() {
/* 63 */     return this.elementName;
/*    */   }
/*    */ 
/*    */   public boolean isNillable() {
/* 67 */     return this.isNillable;
/*    */   }
/*    */ 
/*    */   public String getDefaultValue() {
/* 71 */     return this.defaultValue;
/*    */   }
/*    */ 
/*    */   protected void link()
/*    */   {
/* 76 */     calcRef();
/*    */   }
/*    */ 
/*    */   private void calcRef()
/*    */   {
/* 81 */     this.ref = this.owner.parent.builder.getTypeInfo(this.type, this.owner);
/* 82 */     assert (this.ref != null);
/*    */   }
/*    */ 
/*    */   public PropertyInfo<TypeT, ClassDeclT> getSource() {
/* 86 */     return this.owner;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.TypeRefImpl
 * JD-Core Version:    0.6.2
 */