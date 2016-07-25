/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.TODO;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class ArrayInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends TypeInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements ArrayInfo<TypeT, ClassDeclT>, Location
/*     */ {
/*     */   private final NonElement<TypeT, ClassDeclT> itemType;
/*     */   private final QName typeName;
/*     */   private final TypeT arrayType;
/*     */ 
/*     */   public ArrayInfoImpl(ModelBuilder<TypeT, ClassDeclT, FieldT, MethodT> builder, Locatable upstream, TypeT arrayType)
/*     */   {
/*  60 */     super(builder, upstream);
/*  61 */     this.arrayType = arrayType;
/*  62 */     Object componentType = nav().getComponentType(arrayType);
/*  63 */     this.itemType = builder.getTypeInfo(componentType, this);
/*     */ 
/*  65 */     QName n = this.itemType.getTypeName();
/*  66 */     if (n == null) {
/*  67 */       builder.reportError(new IllegalAnnotationException(Messages.ANONYMOUS_ARRAY_ITEM.format(new Object[] { nav().getTypeName(componentType) }), this));
/*     */ 
/*  69 */       n = new QName("#dummy");
/*     */     }
/*  71 */     this.typeName = calcArrayTypeName(n);
/*     */   }
/*     */ 
/*     */   public static QName calcArrayTypeName(QName n)
/*     */   {
/*     */     String uri;
/*     */     String uri;
/*  79 */     if (n.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema")) {
/*  80 */       TODO.checkSpec("this URI");
/*  81 */       uri = "http://jaxb.dev.java.net/array";
/*     */     } else {
/*  83 */       uri = n.getNamespaceURI();
/*  84 */     }return new QName(uri, n.getLocalPart() + "Array");
/*     */   }
/*     */ 
/*     */   public NonElement<TypeT, ClassDeclT> getItemType() {
/*  88 */     return this.itemType;
/*     */   }
/*     */ 
/*     */   public QName getTypeName() {
/*  92 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public boolean isSimpleType() {
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   public TypeT getType() {
/* 100 */     return this.arrayType;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final boolean canBeReferencedByIDREF()
/*     */   {
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 114 */     return this;
/*     */   }
/*     */   public String toString() {
/* 117 */     return nav().getTypeName(this.arrayType);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.ArrayInfoImpl
 * JD-Core Version:    0.6.2
 */