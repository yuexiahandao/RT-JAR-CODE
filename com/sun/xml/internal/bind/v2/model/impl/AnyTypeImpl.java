/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*    */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*    */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*    */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ class AnyTypeImpl<T, C>
/*    */   implements NonElement<T, C>
/*    */ {
/*    */   private final T type;
/*    */   private final Navigator<T, C, ?, ?> nav;
/*    */ 
/*    */   public AnyTypeImpl(Navigator<T, C, ?, ?> nav)
/*    */   {
/* 48 */     this.type = nav.ref(Object.class);
/* 49 */     this.nav = nav;
/*    */   }
/*    */ 
/*    */   public QName getTypeName() {
/* 53 */     return ANYTYPE_NAME;
/*    */   }
/*    */ 
/*    */   public T getType() {
/* 57 */     return this.type;
/*    */   }
/*    */ 
/*    */   public Locatable getUpstream() {
/* 61 */     return null;
/*    */   }
/*    */ 
/*    */   public boolean isSimpleType() {
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   public Location getLocation() {
/* 69 */     return this.nav.getClassLocation(this.nav.asDecl(Object.class));
/*    */   }
/*    */ 
/*    */   /** @deprecated */
/*    */   public final boolean canBeReferencedByIDREF()
/*    */   {
/* 79 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.AnyTypeImpl
 * JD-Core Version:    0.6.2
 */