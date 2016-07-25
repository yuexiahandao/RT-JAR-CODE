/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElement.DEFAULT;
/*     */ import javax.xml.bind.annotation.XmlElements;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ class ElementPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> extends ERPropertyInfoImpl<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements ElementPropertyInfo<TypeT, ClassDeclT>
/*     */ {
/*     */   private List<TypeRefImpl<TypeT, ClassDeclT>> types;
/*  61 */   private final List<TypeInfo<TypeT, ClassDeclT>> ref = new AbstractList() {
/*     */     public TypeInfo<TypeT, ClassDeclT> get(int index) {
/*  63 */       return ((TypeRefImpl)ElementPropertyInfoImpl.this.getTypes().get(index)).getTarget();
/*     */     }
/*     */ 
/*     */     public int size() {
/*  67 */       return ElementPropertyInfoImpl.this.getTypes().size();
/*     */     }
/*  61 */   };
/*     */   private Boolean isRequired;
/*     */   private final boolean isValueList;
/*     */ 
/*     */   ElementPropertyInfoImpl(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, PropertySeed<TypeT, ClassDeclT, FieldT, MethodT> propertySeed)
/*     */   {
/*  85 */     super(parent, propertySeed);
/*     */ 
/*  87 */     this.isValueList = this.seed.hasAnnotation(XmlList.class);
/*     */   }
/*     */ 
/*     */   public List<? extends TypeRefImpl<TypeT, ClassDeclT>> getTypes()
/*     */   {
/*  92 */     if (this.types == null) {
/*  93 */       this.types = new FinalArrayList();
/*  94 */       XmlElement[] ann = null;
/*     */ 
/*  96 */       XmlElement xe = (XmlElement)this.seed.readAnnotation(XmlElement.class);
/*  97 */       XmlElements xes = (XmlElements)this.seed.readAnnotation(XmlElements.class);
/*     */ 
/*  99 */       if ((xe != null) && (xes != null)) {
/* 100 */         this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(new Object[] { nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), xe.annotationType().getName(), xes.annotationType().getName() }), xe, xes));
/*     */       }
/*     */ 
/* 107 */       this.isRequired = Boolean.valueOf(true);
/*     */ 
/* 109 */       if (xe != null) {
/* 110 */         ann = new XmlElement[] { xe };
/*     */       }
/* 112 */       else if (xes != null) {
/* 113 */         ann = xes.value();
/*     */       }
/* 115 */       if (ann == null)
/*     */       {
/* 117 */         Object t = getIndividualType();
/* 118 */         if ((!nav().isPrimitive(t)) || (isCollection())) {
/* 119 */           this.isRequired = Boolean.valueOf(false);
/*     */         }
/* 121 */         this.types.add(createTypeRef(calcXmlName((XmlElement)null), t, isCollection(), null));
/*     */       } else {
/* 123 */         for (XmlElement item : ann)
/*     */         {
/* 125 */           QName name = calcXmlName(item);
/* 126 */           Object type = reader().getClassValue(item, "type");
/* 127 */           if (type.equals(nav().ref(XmlElement.DEFAULT.class))) type = getIndividualType();
/* 128 */           if (((!nav().isPrimitive(type)) || (isCollection())) && (!item.required()))
/* 129 */             this.isRequired = Boolean.valueOf(false);
/* 130 */           this.types.add(createTypeRef(name, type, item.nillable(), getDefaultValue(item.defaultValue())));
/*     */         }
/*     */       }
/* 133 */       this.types = Collections.unmodifiableList(this.types);
/* 134 */       assert (!this.types.contains(null));
/*     */     }
/* 136 */     return this.types;
/*     */   }
/*     */ 
/*     */   private String getDefaultValue(String value) {
/* 140 */     if (value.equals("")) {
/* 141 */       return null;
/*     */     }
/* 143 */     return value;
/*     */   }
/*     */ 
/*     */   protected TypeRefImpl<TypeT, ClassDeclT> createTypeRef(QName name, TypeT type, boolean isNillable, String defaultValue)
/*     */   {
/* 150 */     return new TypeRefImpl(this, name, type, isNillable, defaultValue);
/*     */   }
/*     */ 
/*     */   public boolean isValueList() {
/* 154 */     return this.isValueList;
/*     */   }
/*     */ 
/*     */   public boolean isRequired() {
/* 158 */     if (this.isRequired == null)
/* 159 */       getTypes();
/* 160 */     return this.isRequired.booleanValue();
/*     */   }
/*     */ 
/*     */   public List<? extends TypeInfo<TypeT, ClassDeclT>> ref() {
/* 164 */     return this.ref;
/*     */   }
/*     */ 
/*     */   public final PropertyKind kind() {
/* 168 */     return PropertyKind.ELEMENT;
/*     */   }
/*     */ 
/*     */   protected void link() {
/* 172 */     super.link();
/* 173 */     for (TypeRefImpl ref : getTypes()) {
/* 174 */       ref.link();
/*     */     }
/*     */ 
/* 177 */     if (isValueList())
/*     */     {
/* 180 */       if (id() != ID.IDREF)
/*     */       {
/* 184 */         for (TypeRefImpl ref : this.types) {
/* 185 */           if (!ref.getTarget().isSimpleType()) {
/* 186 */             this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_NEEDS_SIMPLETYPE.format(new Object[] { nav().getTypeName(ref.getTarget().getType()) }), this));
/*     */ 
/* 189 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 194 */       if (!isCollection())
/* 195 */         this.parent.builder.reportError(new IllegalAnnotationException(Messages.XMLLIST_ON_SINGLE_PROPERTY.format(new Object[0]), this));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.ElementPropertyInfoImpl
 * JD-Core Version:    0.6.2
 */