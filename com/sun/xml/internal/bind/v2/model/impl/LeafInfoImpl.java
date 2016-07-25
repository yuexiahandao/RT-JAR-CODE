/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*    */ import com.sun.xml.internal.bind.v2.model.core.LeafInfo;
/*    */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ abstract class LeafInfoImpl<TypeT, ClassDeclT>
/*    */   implements LeafInfo<TypeT, ClassDeclT>, Location
/*    */ {
/*    */   private final TypeT type;
/*    */   private final QName typeName;
/*    */ 
/*    */   protected LeafInfoImpl(TypeT type, QName typeName)
/*    */   {
/* 45 */     assert (type != null);
/*    */ 
/* 47 */     this.type = type;
/* 48 */     this.typeName = typeName;
/*    */   }
/*    */ 
/*    */   public TypeT getType()
/*    */   {
/* 55 */     return this.type;
/*    */   }
/*    */ 
/*    */   /** @deprecated */
/*    */   public final boolean canBeReferencedByIDREF()
/*    */   {
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   public QName getTypeName() {
/* 69 */     return this.typeName;
/*    */   }
/*    */ 
/*    */   public Locatable getUpstream() {
/* 73 */     return null;
/*    */   }
/*    */ 
/*    */   public Location getLocation()
/*    */   {
/* 80 */     return this;
/*    */   }
/*    */ 
/*    */   public boolean isSimpleType() {
/* 84 */     return true;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 88 */     return this.type.toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.LeafInfoImpl
 * JD-Core Version:    0.6.2
 */