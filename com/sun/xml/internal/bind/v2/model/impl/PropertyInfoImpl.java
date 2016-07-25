/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.TODO;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import com.sun.xml.internal.bind.v2.runtime.SwaRefAdapter;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlElementWrapper;
/*     */ import javax.xml.bind.annotation.XmlID;
/*     */ import javax.xml.bind.annotation.XmlIDREF;
/*     */ import javax.xml.bind.annotation.XmlInlineBinaryData;
/*     */ import javax.xml.bind.annotation.XmlMimeType;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ abstract class PropertyInfoImpl<T, C, F, M>
/*     */   implements PropertyInfo<T, C>, Locatable, Comparable<PropertyInfoImpl>
/*     */ {
/*     */   protected final PropertySeed<T, C, F, M> seed;
/*     */   private final boolean isCollection;
/*     */   private final ID id;
/*     */   private final MimeType expectedMimeType;
/*     */   private final boolean inlineBinary;
/*     */   private final QName schemaType;
/*     */   protected final ClassInfoImpl<T, C, F, M> parent;
/*     */   private final Adapter<T, C> adapter;
/*     */ 
/*     */   protected PropertyInfoImpl(ClassInfoImpl<T, C, F, M> parent, PropertySeed<T, C, F, M> spi)
/*     */   {
/*  84 */     this.seed = spi;
/*  85 */     this.parent = parent;
/*     */ 
/*  87 */     if (parent == null)
/*     */     {
/*  95 */       throw new AssertionError();
/*     */     }
/*  97 */     MimeType mt = Util.calcExpectedMediaType(this.seed, parent.builder);
/*  98 */     if ((mt != null) && (!kind().canHaveXmlMimeType)) {
/*  99 */       parent.builder.reportError(new IllegalAnnotationException(Messages.ILLEGAL_ANNOTATION.format(new Object[] { XmlMimeType.class.getName() }), this.seed.readAnnotation(XmlMimeType.class)));
/*     */ 
/* 103 */       mt = null;
/*     */     }
/* 105 */     this.expectedMimeType = mt;
/* 106 */     this.inlineBinary = this.seed.hasAnnotation(XmlInlineBinaryData.class);
/*     */ 
/* 108 */     Object t = this.seed.getRawType();
/*     */ 
/* 111 */     XmlJavaTypeAdapter xjta = getApplicableAdapter(t);
/* 112 */     if (xjta != null) {
/* 113 */       this.isCollection = false;
/* 114 */       this.adapter = new Adapter(xjta, reader(), nav());
/*     */     }
/*     */     else
/*     */     {
/* 118 */       this.isCollection = ((nav().isSubClassOf(t, nav().ref(Collection.class))) || (nav().isArrayButNotByteArray(t)));
/*     */ 
/* 121 */       xjta = getApplicableAdapter(getIndividualType());
/* 122 */       if (xjta == null)
/*     */       {
/* 124 */         XmlAttachmentRef xsa = (XmlAttachmentRef)this.seed.readAnnotation(XmlAttachmentRef.class);
/* 125 */         if (xsa != null) {
/* 126 */           parent.builder.hasSwaRef = true;
/* 127 */           this.adapter = new Adapter(nav().asDecl(SwaRefAdapter.class), nav());
/*     */         } else {
/* 129 */           this.adapter = null;
/*     */ 
/* 133 */           xjta = (XmlJavaTypeAdapter)this.seed.readAnnotation(XmlJavaTypeAdapter.class);
/* 134 */           if (xjta != null) {
/* 135 */             Object ad = reader().getClassValue(xjta, "value");
/* 136 */             parent.builder.reportError(new IllegalAnnotationException(Messages.UNMATCHABLE_ADAPTER.format(new Object[] { nav().getTypeName(ad), nav().getTypeName(t) }), xjta));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 144 */         this.adapter = new Adapter(xjta, reader(), nav());
/*     */       }
/*     */     }
/*     */ 
/* 148 */     this.id = calcId();
/* 149 */     this.schemaType = Util.calcSchemaType(reader(), this.seed, parent.clazz, getIndividualType(), this);
/*     */   }
/*     */ 
/*     */   public ClassInfoImpl<T, C, F, M> parent()
/*     */   {
/* 155 */     return this.parent;
/*     */   }
/*     */ 
/*     */   protected final Navigator<T, C, F, M> nav() {
/* 159 */     return this.parent.nav();
/*     */   }
/*     */   protected final AnnotationReader<T, C, F, M> reader() {
/* 162 */     return this.parent.reader();
/*     */   }
/*     */ 
/*     */   public T getRawType() {
/* 166 */     return this.seed.getRawType();
/*     */   }
/*     */ 
/*     */   public T getIndividualType() {
/* 170 */     if (this.adapter != null)
/* 171 */       return this.adapter.defaultType;
/* 172 */     Object raw = getRawType();
/* 173 */     if (!isCollection()) {
/* 174 */       return raw;
/*     */     }
/* 176 */     if (nav().isArrayButNotByteArray(raw)) {
/* 177 */       return nav().getComponentType(raw);
/*     */     }
/* 179 */     Object bt = nav().getBaseClass(raw, nav().asDecl(Collection.class));
/* 180 */     if (nav().isParameterizedType(bt)) {
/* 181 */       return nav().getTypeArgument(bt, 0);
/*     */     }
/* 183 */     return nav().ref(Object.class);
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 188 */     return this.seed.getName();
/*     */   }
/*     */ 
/*     */   private boolean isApplicable(XmlJavaTypeAdapter jta, T declaredType)
/*     */   {
/* 195 */     if (jta == null) return false;
/*     */ 
/* 197 */     Object type = reader().getClassValue(jta, "type");
/* 198 */     if (declaredType.equals(type)) {
/* 199 */       return true;
/*     */     }
/* 201 */     Object ad = reader().getClassValue(jta, "value");
/* 202 */     Object ba = nav().getBaseClass(ad, nav().asDecl(XmlAdapter.class));
/* 203 */     if (!nav().isParameterizedType(ba))
/* 204 */       return true;
/* 205 */     Object inMemType = nav().getTypeArgument(ba, 1);
/*     */ 
/* 207 */     return nav().isSubClassOf(declaredType, inMemType);
/*     */   }
/*     */ 
/*     */   private XmlJavaTypeAdapter getApplicableAdapter(T type) {
/* 211 */     XmlJavaTypeAdapter jta = (XmlJavaTypeAdapter)this.seed.readAnnotation(XmlJavaTypeAdapter.class);
/* 212 */     if ((jta != null) && (isApplicable(jta, type))) {
/* 213 */       return jta;
/*     */     }
/*     */ 
/* 216 */     XmlJavaTypeAdapters jtas = (XmlJavaTypeAdapters)reader().getPackageAnnotation(XmlJavaTypeAdapters.class, this.parent.clazz, this.seed);
/* 217 */     if (jtas != null) {
/* 218 */       for (XmlJavaTypeAdapter xjta : jtas.value()) {
/* 219 */         if (isApplicable(xjta, type))
/* 220 */           return xjta;
/*     */       }
/*     */     }
/* 223 */     jta = (XmlJavaTypeAdapter)reader().getPackageAnnotation(XmlJavaTypeAdapter.class, this.parent.clazz, this.seed);
/* 224 */     if (isApplicable(jta, type)) {
/* 225 */       return jta;
/*     */     }
/*     */ 
/* 228 */     Object refType = nav().asDecl(type);
/* 229 */     if (refType != null) {
/* 230 */       jta = (XmlJavaTypeAdapter)reader().getClassAnnotation(XmlJavaTypeAdapter.class, refType, this.seed);
/* 231 */       if ((jta != null) && (isApplicable(jta, type))) {
/* 232 */         return jta;
/*     */       }
/*     */     }
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   public Adapter<T, C> getAdapter()
/*     */   {
/* 243 */     return this.adapter;
/*     */   }
/*     */ 
/*     */   public final String displayName()
/*     */   {
/* 248 */     return nav().getClassName(this.parent.getClazz()) + '#' + getName();
/*     */   }
/*     */ 
/*     */   public final ID id() {
/* 252 */     return this.id;
/*     */   }
/*     */ 
/*     */   private ID calcId() {
/* 256 */     if (this.seed.hasAnnotation(XmlID.class))
/*     */     {
/* 258 */       if (!getIndividualType().equals(nav().ref(String.class))) {
/* 259 */         this.parent.builder.reportError(new IllegalAnnotationException(Messages.ID_MUST_BE_STRING.format(new Object[] { getName() }), this.seed));
/*     */       }
/*     */ 
/* 262 */       return ID.ID;
/*     */     }
/* 264 */     if (this.seed.hasAnnotation(XmlIDREF.class)) {
/* 265 */       return ID.IDREF;
/*     */     }
/* 267 */     return ID.NONE;
/*     */   }
/*     */ 
/*     */   public final MimeType getExpectedMimeType()
/*     */   {
/* 272 */     return this.expectedMimeType;
/*     */   }
/*     */ 
/*     */   public final boolean inlineBinaryData() {
/* 276 */     return this.inlineBinary;
/*     */   }
/*     */ 
/*     */   public final QName getSchemaType() {
/* 280 */     return this.schemaType;
/*     */   }
/*     */ 
/*     */   public final boolean isCollection() {
/* 284 */     return this.isCollection;
/*     */   }
/*     */ 
/*     */   protected void link()
/*     */   {
/* 293 */     if (this.id == ID.IDREF)
/*     */     {
/* 295 */       for (TypeInfo ti : ref())
/* 296 */         if (!ti.canBeReferencedByIDREF())
/* 297 */           this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_IDREF.format(new Object[] { this.parent.builder.nav.getTypeName(ti.getType()) }), this));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Locatable getUpstream()
/*     */   {
/* 309 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 313 */     return this.seed.getLocation();
/*     */   }
/*     */ 
/*     */   protected final QName calcXmlName(XmlElement e)
/*     */   {
/* 328 */     if (e != null) {
/* 329 */       return calcXmlName(e.namespace(), e.name());
/*     */     }
/* 331 */     return calcXmlName("##default", "##default");
/*     */   }
/*     */ 
/*     */   protected final QName calcXmlName(XmlElementWrapper e)
/*     */   {
/* 338 */     if (e != null) {
/* 339 */       return calcXmlName(e.namespace(), e.name());
/*     */     }
/* 341 */     return calcXmlName("##default", "##default");
/*     */   }
/*     */ 
/*     */   private QName calcXmlName(String uri, String local)
/*     */   {
/* 346 */     TODO.checkSpec();
/* 347 */     if ((local.length() == 0) || (local.equals("##default")))
/* 348 */       local = this.seed.getName();
/* 349 */     if (uri.equals("##default")) {
/* 350 */       XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
/*     */ 
/* 353 */       if (xs != null)
/* 354 */         switch (1.$SwitchMap$javax$xml$bind$annotation$XmlNsForm[xs.elementFormDefault().ordinal()]) {
/*     */         case 1:
/* 356 */           QName typeName = this.parent.getTypeName();
/* 357 */           if (typeName != null)
/* 358 */             uri = typeName.getNamespaceURI();
/*     */           else
/* 360 */             uri = xs.namespace();
/* 361 */           if (uri.length() == 0)
/* 362 */             uri = this.parent.builder.defaultNsUri; break;
/*     */         case 2:
/*     */         case 3:
/* 366 */           uri = "";
/*     */         }
/*     */       else {
/* 369 */         uri = "";
/*     */       }
/*     */     }
/* 372 */     return new QName(uri.intern(), local.intern());
/*     */   }
/*     */ 
/*     */   public int compareTo(PropertyInfoImpl that) {
/* 376 */     return getName().compareTo(that.getName());
/*     */   }
/*     */ 
/*     */   public final <A extends Annotation> A readAnnotation(Class<A> annotationType) {
/* 380 */     return this.seed.readAnnotation(annotationType);
/*     */   }
/*     */ 
/*     */   public final boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 384 */     return this.seed.hasAnnotation(annotationType);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.PropertyInfoImpl
 * JD-Core Version:    0.6.2
 */