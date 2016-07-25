/*    */ package com.sun.xml.internal.bind.v2.model.core;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*    */ import com.sun.xml.internal.bind.v2.model.impl.ModelBuilder;
/*    */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*    */ import javax.xml.bind.annotation.XmlList;
/*    */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*    */ 
/*    */ public final class Ref<T, C>
/*    */ {
/*    */   public final T type;
/*    */   public final Adapter<T, C> adapter;
/*    */   public final boolean valueList;
/*    */ 
/*    */   public Ref(T type)
/*    */   {
/* 60 */     this(type, null, false);
/*    */   }
/*    */ 
/*    */   public Ref(T type, Adapter<T, C> adapter, boolean valueList) {
/* 64 */     this.adapter = adapter;
/* 65 */     if (adapter != null)
/* 66 */       type = adapter.defaultType;
/* 67 */     this.type = type;
/* 68 */     this.valueList = valueList;
/*    */   }
/*    */ 
/*    */   public Ref(ModelBuilder<T, C, ?, ?> builder, T type, XmlJavaTypeAdapter xjta, XmlList xl) {
/* 72 */     this(builder.reader, builder.nav, type, xjta, xl);
/*    */   }
/*    */ 
/*    */   public Ref(AnnotationReader<T, C, ?, ?> reader, Navigator<T, C, ?, ?> nav, T type, XmlJavaTypeAdapter xjta, XmlList xl)
/*    */   {
/* 78 */     Adapter adapter = null;
/* 79 */     if (xjta != null) {
/* 80 */       adapter = new Adapter(xjta, reader, nav);
/* 81 */       type = adapter.defaultType;
/*    */     }
/*    */ 
/* 84 */     this.type = type;
/* 85 */     this.adapter = adapter;
/* 86 */     this.valueList = (xl != null);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.Ref
 * JD-Core Version:    0.6.2
 */