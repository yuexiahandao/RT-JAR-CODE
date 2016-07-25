/*    */ package com.sun.xml.internal.bind.v2.model.core;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*    */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*    */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*    */ 
/*    */ public class Adapter<TypeT, ClassDeclT>
/*    */ {
/*    */   public final ClassDeclT adapterType;
/*    */   public final TypeT defaultType;
/*    */   public final TypeT customType;
/*    */ 
/*    */   public Adapter(XmlJavaTypeAdapter spec, AnnotationReader<TypeT, ClassDeclT, ?, ?> reader, Navigator<TypeT, ClassDeclT, ?, ?> nav)
/*    */   {
/* 68 */     this(nav.asDecl(reader.getClassValue(spec, "value")), nav);
/*    */   }
/*    */ 
/*    */   public Adapter(ClassDeclT adapterType, Navigator<TypeT, ClassDeclT, ?, ?> nav) {
/* 72 */     this.adapterType = adapterType;
/* 73 */     Object baseClass = nav.getBaseClass(nav.use(adapterType), nav.asDecl(XmlAdapter.class));
/*    */ 
/* 76 */     assert (baseClass != null);
/*    */ 
/* 78 */     if (nav.isParameterizedType(baseClass))
/* 79 */       this.defaultType = nav.getTypeArgument(baseClass, 0);
/*    */     else {
/* 81 */       this.defaultType = nav.ref(Object.class);
/*    */     }
/* 83 */     if (nav.isParameterizedType(baseClass))
/* 84 */       this.customType = nav.getTypeArgument(baseClass, 1);
/*    */     else
/* 86 */       this.customType = nav.ref(Object.class);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.Adapter
 * JD-Core Version:    0.6.2
 */