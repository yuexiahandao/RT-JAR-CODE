/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.impl.NameConverter;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ class AttributePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends SingleTypePropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements AttributePropertyInfo<TypeT, ClassDeclT>
/*     */ {
/*     */   private final QName xmlName;
/*     */   private final boolean isRequired;
/*     */ 
/*     */   AttributePropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> seed)
/*     */   {
/*  48 */     super(parent, seed);
/*  49 */     XmlAttribute att = (XmlAttribute)seed.readAnnotation(XmlAttribute.class);
/*  50 */     assert (att != null);
/*     */ 
/*  52 */     if (att.required())
/*  53 */       this.isRequired = true;
/*  54 */     else this.isRequired = nav().isPrimitive(getIndividualType());
/*     */ 
/*  56 */     this.xmlName = calcXmlName(att);
/*     */   }
/*     */ 
/*     */   private QName calcXmlName(XmlAttribute att)
/*     */   {
/*  63 */     String uri = att.namespace();
/*  64 */     String local = att.name();
/*     */ 
/*  67 */     if (local.equals("##default"))
/*  68 */       local = NameConverter.standard.toVariableName(getName());
/*  69 */     if (uri.equals("##default")) {
/*  70 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
/*     */ 
/*  73 */       if (xs != null)
/*  74 */         switch (1.$SwitchMap$javax$xml$bind$annotation$XmlNsForm[xs.attributeFormDefault().ordinal()]) {
/*     */         case 1:
/*  76 */           uri = this.parent.getTypeName().getNamespaceURI();
/*  77 */           if (uri.length() == 0)
/*  78 */             uri = this.parent.builder.defaultNsUri; break;
/*     */         case 2:
/*     */         case 3:
/*  82 */           uri = "";
/*     */         }
/*     */       else {
/*  85 */         uri = "";
/*     */       }
/*     */     }
/*  88 */     return new QName(uri.intern(), local.intern());
/*     */   }
/*     */ 
/*     */   public boolean isRequired() {
/*  92 */     return this.isRequired;
/*     */   }
/*     */ 
/*     */   public final QName getXmlName() {
/*  96 */     return this.xmlName;
/*     */   }
/*     */ 
/*     */   public final PropertyKind kind() {
/* 100 */     return PropertyKind.ATTRIBUTE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.AttributePropertyInfoImpl
 * JD-Core Version:    0.6.2
 */