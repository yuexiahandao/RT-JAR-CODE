/*    */ package com.sun.xml.internal.bind.v2.model.impl;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.core.BuiltinLeafInfo;
/*    */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*    */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public class BuiltinLeafInfoImpl<TypeT, ClassDeclT> extends LeafInfoImpl<TypeT, ClassDeclT>
/*    */   implements BuiltinLeafInfo<TypeT, ClassDeclT>
/*    */ {
/*    */   private final QName[] typeNames;
/*    */ 
/*    */   protected BuiltinLeafInfoImpl(TypeT type, QName[] typeNames)
/*    */   {
/* 49 */     super(type, typeNames.length > 0 ? typeNames[0] : null);
/* 50 */     this.typeNames = typeNames;
/*    */   }
/*    */ 
/*    */   public final QName[] getTypeNames()
/*    */   {
/* 60 */     return this.typeNames;
/*    */   }
/*    */ 
/*    */   /** @deprecated */
/*    */   public final boolean isElement()
/*    */   {
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */   /** @deprecated */
/*    */   public final QName getElementName()
/*    */   {
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */   /** @deprecated */
/*    */   public final Element<TypeT, ClassDeclT> asElement()
/*    */   {
/* 81 */     return null;
/*    */   }
/*    */ 
/*    */   public static <TypeT, ClassDeclT> Map<TypeT, BuiltinLeafInfoImpl<TypeT, ClassDeclT>> createLeaves(Navigator<TypeT, ClassDeclT, ?, ?> nav)
/*    */   {
/* 91 */     Map leaves = new HashMap();
/*    */ 
/* 93 */     for (RuntimeBuiltinLeafInfoImpl leaf : RuntimeBuiltinLeafInfoImpl.builtinBeanInfos) {
/* 94 */       Object t = nav.ref(leaf.getClazz());
/* 95 */       leaves.put(t, new BuiltinLeafInfoImpl(t, leaf.getTypeNames()));
/*    */     }
/*    */ 
/* 98 */     return leaves;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.BuiltinLeafInfoImpl
 * JD-Core Version:    0.6.2
 */