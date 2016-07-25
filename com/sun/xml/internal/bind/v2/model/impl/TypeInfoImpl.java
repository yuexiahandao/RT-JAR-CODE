/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ abstract class TypeInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements TypeInfo<TypeT, ClassDeclT>, Locatable
/*     */ {
/*     */   private final Locatable upstream;
/*     */   protected final TypeInfoSetImpl<TypeT, ClassDeclT, FieldT, MethodT> owner;
/*     */   protected ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder;
/*     */ 
/*     */   protected TypeInfoImpl(ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder, Locatable upstream)
/*     */   {
/*  70 */     this.builder = builder;
/*  71 */     this.owner = builder.typeInfoSet;
/*  72 */     this.upstream = upstream;
/*     */   }
/*     */ 
/*     */   public Locatable getUpstream() {
/*  76 */     return this.upstream;
/*     */   }
/*     */ 
/*     */   void link() {
/*  80 */     this.builder = null;
/*     */   }
/*     */ 
/*     */   protected final Navigator<TypeT, ClassDeclT, FieldT, MethodT> nav() {
/*  84 */     return this.owner.nav;
/*     */   }
/*     */ 
/*     */   protected final AnnotationReader<TypeT, ClassDeclT, FieldT, MethodT> reader() {
/*  88 */     return this.owner.reader;
/*     */   }
/*     */ 
/*     */   protected final QName parseElementName(ClassDeclT clazz)
/*     */   {
/*  99 */     XmlRootElement e = (XmlRootElement)reader().getClassAnnotation(XmlRootElement.class, clazz, this);
/* 100 */     if (e == null) {
/* 101 */       return null;
/*     */     }
/* 103 */     String local = e.name();
/* 104 */     if (local.equals("##default"))
/*     */     {
/* 106 */       local = NameConverter.standard.toVariableName(nav().getClassShortName(clazz));
/*     */     }
/* 108 */     String nsUri = e.namespace();
/* 109 */     if (nsUri.equals("##default"))
/*     */     {
/* 111 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, clazz, this);
/* 112 */       if (xs != null)
/* 113 */         nsUri = xs.namespace();
/*     */       else {
/* 115 */         nsUri = this.builder.defaultNsUri;
/*     */       }
/*     */     }
/*     */ 
/* 119 */     return new QName(nsUri.intern(), local.intern());
/*     */   }
/*     */ 
/*     */   protected final QName parseTypeName(ClassDeclT clazz) {
/* 123 */     return parseTypeName(clazz, (XmlType)reader().getClassAnnotation(XmlType.class, clazz, this));
/*     */   }
/*     */ 
/*     */   protected final QName parseTypeName(ClassDeclT clazz, XmlType t)
/*     */   {
/* 138 */     String nsUri = "##default";
/* 139 */     String local = "##default";
/* 140 */     if (t != null) {
/* 141 */       nsUri = t.namespace();
/* 142 */       local = t.name();
/*     */     }
/*     */ 
/* 145 */     if (local.length() == 0) {
/* 146 */       return null;
/*     */     }
/*     */ 
/* 149 */     if (local.equals("##default"))
/*     */     {
/* 151 */       local = NameConverter.standard.toVariableName(nav().getClassShortName(clazz));
/*     */     }
/* 153 */     if (nsUri.equals("##default"))
/*     */     {
/* 155 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, clazz, this);
/* 156 */       if (xs != null)
/* 157 */         nsUri = xs.namespace();
/*     */       else {
/* 159 */         nsUri = this.builder.defaultNsUri;
/*     */       }
/*     */     }
/*     */ 
/* 163 */     return new QName(nsUri.intern(), local.intern());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.TypeInfoImpl
 * JD-Core Version:    0.6.2
 */