/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*     */ import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.bind.annotation.XmlEnum;
/*     */ import javax.xml.bind.annotation.XmlEnumValue;
/*     */ import javax.xml.bind.annotation.XmlSchemaType;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ class EnumLeafInfoImpl<T, C, F, M> extends TypeInfoImpl<T, C, F, M>
/*     */   implements EnumLeafInfo<T, C>, Element<T, C>, Iterable<EnumConstantImpl<T, C, F, M>>
/*     */ {
/*     */   final C clazz;
/*     */   NonElement<T, C> baseType;
/*     */   private final T type;
/*     */   private final QName typeName;
/*     */   private EnumConstantImpl<T, C, F, M> firstConstant;
/*     */   private QName elementName;
/*     */   protected boolean tokenStringType;
/*     */ 
/*     */   public EnumLeafInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C clazz, T type)
/*     */   {
/*  92 */     super(builder, upstream);
/*  93 */     this.clazz = clazz;
/*  94 */     this.type = type;
/*     */ 
/*  96 */     this.elementName = parseElementName(clazz);
/*     */ 
/* 100 */     this.typeName = parseTypeName(clazz);
/*     */ 
/* 104 */     XmlEnum xe = (XmlEnum)builder.reader.getClassAnnotation(XmlEnum.class, clazz, this);
/* 105 */     if (xe != null) {
/* 106 */       Object base = builder.reader.getClassValue(xe, "value");
/* 107 */       this.baseType = builder.getTypeInfo(base, this);
/*     */     } else {
/* 109 */       this.baseType = builder.getTypeInfo(builder.nav.ref(String.class), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void calcConstants()
/*     */   {
/* 117 */     EnumConstantImpl last = null;
/*     */ 
/* 120 */     Collection fields = nav().getDeclaredFields(this.clazz);
/* 121 */     for (Iterator i$ = fields.iterator(); i$.hasNext(); ) { Object f = i$.next();
/* 122 */       if (nav().getFieldType(f).equals(String.class)) {
/* 123 */         XmlSchemaType schemaTypeAnnotation = (XmlSchemaType)this.builder.reader.getFieldAnnotation(XmlSchemaType.class, f, this);
/* 124 */         if ((schemaTypeAnnotation != null) && 
/* 125 */           ("token".equals(schemaTypeAnnotation.name()))) {
/* 126 */           this.tokenStringType = true;
/* 127 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 132 */     Object[] constants = nav().getEnumConstants(this.clazz);
/* 133 */     for (int i = constants.length - 1; i >= 0; i--) {
/* 134 */       Object constant = constants[i];
/* 135 */       String name = nav().getFieldName(constant);
/* 136 */       XmlEnumValue xev = (XmlEnumValue)this.builder.reader.getFieldAnnotation(XmlEnumValue.class, constant, this);
/*     */       String literal;
/*     */       String literal;
/* 139 */       if (xev == null) literal = name; else {
/* 140 */         literal = xev.value();
/*     */       }
/* 142 */       last = createEnumConstant(name, literal, constant, last);
/*     */     }
/* 144 */     this.firstConstant = last;
/*     */   }
/*     */ 
/*     */   protected EnumConstantImpl<T, C, F, M> createEnumConstant(String name, String literal, F constant, EnumConstantImpl<T, C, F, M> last) {
/* 148 */     return new EnumConstantImpl(this, name, literal, last);
/*     */   }
/*     */ 
/*     */   public T getType()
/*     */   {
/* 153 */     return this.type;
/*     */   }
/*     */ 
/*     */   public boolean isToken()
/*     */   {
/* 161 */     return this.tokenStringType;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final boolean canBeReferencedByIDREF()
/*     */   {
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */   public QName getTypeName() {
/* 175 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public C getClazz() {
/* 179 */     return this.clazz;
/*     */   }
/*     */ 
/*     */   public NonElement<T, C> getBaseType() {
/* 183 */     return this.baseType;
/*     */   }
/*     */ 
/*     */   public boolean isSimpleType() {
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 191 */     return nav().getClassLocation(this.clazz);
/*     */   }
/*     */ 
/*     */   public Iterable<? extends EnumConstantImpl<T, C, F, M>> getConstants() {
/* 195 */     if (this.firstConstant == null)
/* 196 */       calcConstants();
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */   public void link()
/*     */   {
/* 203 */     getConstants();
/* 204 */     super.link();
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Element<T, C> getSubstitutionHead()
/*     */   {
/* 213 */     return null;
/*     */   }
/*     */ 
/*     */   public QName getElementName() {
/* 217 */     return this.elementName;
/*     */   }
/*     */ 
/*     */   public boolean isElement() {
/* 221 */     return this.elementName != null;
/*     */   }
/*     */ 
/*     */   public Element<T, C> asElement() {
/* 225 */     if (isElement()) {
/* 226 */       return this;
/*     */     }
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public ClassInfo<T, C> getScope()
/*     */   {
/* 239 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator<EnumConstantImpl<T, C, F, M>> iterator() {
/* 243 */     return new Iterator() {
/* 244 */       private EnumConstantImpl<T, C, F, M> next = EnumLeafInfoImpl.this.firstConstant;
/*     */ 
/* 246 */       public boolean hasNext() { return this.next != null; }
/*     */ 
/*     */       public EnumConstantImpl<T, C, F, M> next()
/*     */       {
/* 250 */         EnumConstantImpl r = this.next;
/* 251 */         this.next = this.next.next;
/* 252 */         return r;
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 256 */         throw new UnsupportedOperationException();
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.EnumLeafInfoImpl
 * JD-Core Version:    0.6.2
 */