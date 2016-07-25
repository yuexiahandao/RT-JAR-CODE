/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeRef;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ 
/*     */ final class RuntimeElementInfoImpl extends ElementInfoImpl<Type, Class, Field, Method>
/*     */   implements RuntimeElementInfo
/*     */ {
/*     */   private final Class<? extends XmlAdapter> adapterType;
/*     */ 
/*     */   public RuntimeElementInfoImpl(RuntimeModelBuilder modelBuilder, RegistryInfoImpl registry, Method method)
/*     */     throws IllegalAnnotationException
/*     */   {
/*  56 */     super(modelBuilder, registry, method);
/*     */ 
/*  58 */     Adapter a = getProperty().getAdapter();
/*     */ 
/*  60 */     if (a != null)
/*  61 */       this.adapterType = ((Class)a.adapterType);
/*     */     else
/*  63 */       this.adapterType = null;
/*     */   }
/*     */ 
/*     */   protected ElementInfoImpl<Type, Class, Field, Method>.PropertyImpl createPropertyImpl()
/*     */   {
/*  68 */     return new RuntimePropertyImpl();
/*     */   }
/*     */ 
/*     */   public RuntimeElementPropertyInfo getProperty()
/*     */   {
/* 120 */     return (RuntimeElementPropertyInfo)super.getProperty();
/*     */   }
/*     */ 
/*     */   public Class<? extends JAXBElement> getType() {
/* 124 */     return (Class)Utils.REFLECTION_NAVIGATOR.erasure(super.getType());
/*     */   }
/*     */ 
/*     */   public RuntimeClassInfo getScope() {
/* 128 */     return (RuntimeClassInfo)super.getScope();
/*     */   }
/*     */ 
/*     */   public RuntimeNonElement getContentType() {
/* 132 */     return (RuntimeNonElement)super.getContentType();
/*     */   }
/*     */ 
/*     */   class RuntimePropertyImpl extends ElementInfoImpl<Type, Class, Field, Method>.PropertyImpl
/*     */     implements RuntimeElementPropertyInfo, RuntimeTypeRef
/*     */   {
/*     */     RuntimePropertyImpl()
/*     */     {
/*  71 */       super();
/*     */     }
/*  73 */     public Accessor getAccessor() { if (RuntimeElementInfoImpl.this.adapterType == null) {
/*  74 */         return Accessor.JAXB_ELEMENT_VALUE;
/*     */       }
/*  76 */       return Accessor.JAXB_ELEMENT_VALUE.adapt((Class)getAdapter().defaultType, RuntimeElementInfoImpl.this.adapterType);
/*     */     }
/*     */ 
/*     */     public Type getRawType()
/*     */     {
/*  81 */       return Collection.class;
/*     */     }
/*     */ 
/*     */     public Type getIndividualType() {
/*  85 */       return (Type)RuntimeElementInfoImpl.this.getContentType().getType();
/*     */     }
/*     */ 
/*     */     public boolean elementOnlyContent()
/*     */     {
/*  90 */       return false;
/*     */     }
/*     */ 
/*     */     public List<? extends RuntimeTypeRef> getTypes() {
/*  94 */       return Collections.singletonList(this);
/*     */     }
/*     */ 
/*     */     public List<? extends RuntimeNonElement> ref() {
/*  98 */       return super.ref();
/*     */     }
/*     */ 
/*     */     public RuntimeNonElement getTarget() {
/* 102 */       return (RuntimeNonElement)super.getTarget();
/*     */     }
/*     */ 
/*     */     public RuntimePropertyInfo getSource() {
/* 106 */       return this;
/*     */     }
/*     */ 
/*     */     public Transducer getTransducer() {
/* 110 */       return RuntimeModelBuilder.createTransducer(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeElementInfoImpl
 * JD-Core Version:    0.6.2
 */