/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.annotation.XmlAnyElement;
/*     */ import javax.xml.bind.annotation.XmlElementRef;
/*     */ import javax.xml.bind.annotation.XmlElementRef.DEFAULT;
/*     */ import javax.xml.bind.annotation.XmlElementRefs;
/*     */ import javax.xml.bind.annotation.XmlMixed;
/*     */ import javax.xml.bind.annotation.XmlNsForm;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ class ReferencePropertyInfoImpl<T, C, F, M> extends ERPropertyInfoImpl<T, C, F, M>
/*     */   implements ReferencePropertyInfo<T, C>, DummyPropertyInfo<T, C, F, M>
/*     */ {
/*     */   private Set<Element<T, C>> types;
/*  67 */   private Set<PropertyInfoImpl<T, C, F, M>> subTypes = new LinkedHashSet();
/*     */   private final boolean isMixed;
/*     */   private final WildcardMode wildcard;
/*     */   private final C domHandler;
/*     */   private Boolean isRequired;
/* 272 */   private static boolean is2_2 = true;
/*     */ 
/*     */   public ReferencePropertyInfoImpl(ClassInfoImpl<T, C, F, M> classInfo, PropertySeed<T, C, F, M> seed)
/*     */   {
/*  83 */     super(classInfo, seed);
/*     */ 
/*  85 */     this.isMixed = (seed.readAnnotation(XmlMixed.class) != null);
/*     */ 
/*  87 */     XmlAnyElement xae = (XmlAnyElement)seed.readAnnotation(XmlAnyElement.class);
/*  88 */     if (xae == null) {
/*  89 */       this.wildcard = null;
/*  90 */       this.domHandler = null;
/*     */     } else {
/*  92 */       this.wildcard = (xae.lax() ? WildcardMode.LAX : WildcardMode.SKIP);
/*  93 */       this.domHandler = nav().asDecl(reader().getClassValue(xae, "value"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<? extends Element<T, C>> ref() {
/*  98 */     return getElements();
/*     */   }
/*     */ 
/*     */   public PropertyKind kind() {
/* 102 */     return PropertyKind.REFERENCE;
/*     */   }
/*     */ 
/*     */   public Set<? extends Element<T, C>> getElements() {
/* 106 */     if (this.types == null)
/* 107 */       calcTypes(false);
/* 108 */     assert (this.types != null);
/* 109 */     return this.types;
/*     */   }
/*     */ 
/*     */   private void calcTypes(boolean last)
/*     */   {
/* 120 */     this.types = new LinkedHashSet();
/* 121 */     XmlElementRefs refs = (XmlElementRefs)this.seed.readAnnotation(XmlElementRefs.class);
/* 122 */     XmlElementRef ref = (XmlElementRef)this.seed.readAnnotation(XmlElementRef.class);
/*     */ 
/* 124 */     if ((refs != null) && (ref != null))
/* 125 */       this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(new Object[] { nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), ref.annotationType().getName(), refs.annotationType().getName() }), ref, refs));
/*     */     XmlElementRef[] ann;
/*     */     XmlElementRef[] ann;
/* 132 */     if (refs != null) {
/* 133 */       ann = refs.value();
/*     */     }
/*     */     else
/*     */     {
/*     */       XmlElementRef[] ann;
/* 135 */       if (ref != null)
/* 136 */         ann = new XmlElementRef[] { ref };
/*     */       else {
/* 138 */         ann = null;
/*     */       }
/*     */     }
/* 141 */     this.isRequired = Boolean.valueOf(!isCollection());
/*     */ 
/* 143 */     if (ann != null) {
/* 144 */       Navigator nav = nav();
/* 145 */       AnnotationReader reader = reader();
/*     */ 
/* 147 */       Object defaultType = nav.ref(XmlElementRef.DEFAULT.class);
/* 148 */       Object je = nav.asDecl(JAXBElement.class);
/*     */ 
/* 150 */       for (XmlElementRef r : ann)
/*     */       {
/* 152 */         Object type = reader.getClassValue(r, "type");
/* 153 */         if (type.equals(defaultType)) type = nav.erasure(getIndividualType());
/*     */         boolean yield;
/*     */         boolean yield;
/* 154 */         if (nav.getBaseClass(type, je) != null)
/* 155 */           yield = addGenericElement(r);
/*     */         else {
/* 157 */           yield = addAllSubtypes(type);
/*     */         }
/*     */ 
/* 161 */         if ((this.isRequired.booleanValue()) && (!isRequired(r))) {
/* 162 */           this.isRequired = Boolean.valueOf(false);
/*     */         }
/* 164 */         if ((last) && (!yield))
/*     */         {
/* 167 */           if (type.equals(nav.ref(JAXBElement.class)))
/*     */           {
/* 169 */             this.parent.builder.reportError(new IllegalAnnotationException(Messages.NO_XML_ELEMENT_DECL.format(new Object[] { getEffectiveNamespaceFor(r), r.name() }), this));
/*     */           }
/*     */           else
/*     */           {
/* 175 */             this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ELEMENT_REF.format(new Object[] { type }), this));
/*     */           }
/*     */ 
/* 183 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 188 */     Iterator i = this.subTypes.iterator();
/* 189 */     while (i.hasNext())
/*     */     {
/* 191 */       ReferencePropertyInfoImpl info = (ReferencePropertyInfoImpl)i.next();
/* 192 */       PropertySeed sd = info.seed;
/* 193 */       refs = (XmlElementRefs)sd.readAnnotation(XmlElementRefs.class);
/* 194 */       ref = (XmlElementRef)sd.readAnnotation(XmlElementRef.class);
/*     */ 
/* 196 */       if ((refs != null) && (ref != null)) {
/* 197 */         this.parent.builder.reportError(new IllegalAnnotationException(Messages.MUTUALLY_EXCLUSIVE_ANNOTATIONS.format(new Object[] { nav().getClassName(this.parent.getClazz()) + '#' + this.seed.getName(), ref.annotationType().getName(), refs.annotationType().getName() }), ref, refs));
/*     */       }
/*     */ 
/* 204 */       if (refs != null) {
/* 205 */         ann = refs.value();
/*     */       }
/* 207 */       else if (ref != null)
/* 208 */         ann = new XmlElementRef[] { ref };
/*     */       else {
/* 210 */         ann = null;
/*     */       }
/*     */ 
/* 214 */       if (ann != null) {
/* 215 */         Navigator nav = nav();
/* 216 */         AnnotationReader reader = reader();
/*     */ 
/* 218 */         Object defaultType = nav.ref(XmlElementRef.DEFAULT.class);
/* 219 */         Object je = nav.asDecl(JAXBElement.class);
/*     */ 
/* 221 */         for (XmlElementRef r : ann)
/*     */         {
/* 223 */           Object type = reader.getClassValue(r, "type");
/* 224 */           if (type.equals(defaultType))
/* 225 */             type = nav.erasure(getIndividualType());
/*     */           boolean yield;
/*     */           boolean yield;
/* 227 */           if (nav.getBaseClass(type, je) != null) {
/* 228 */             yield = addGenericElement(r, info);
/*     */           }
/*     */           else {
/* 231 */             yield = addAllSubtypes(type);
/*     */           }
/*     */ 
/* 234 */           if ((last) && (!yield))
/*     */           {
/* 237 */             if (type.equals(nav.ref(JAXBElement.class)))
/*     */             {
/* 239 */               this.parent.builder.reportError(new IllegalAnnotationException(Messages.NO_XML_ELEMENT_DECL.format(new Object[] { getEffectiveNamespaceFor(r), r.name() }), this));
/*     */             }
/*     */             else
/*     */             {
/* 245 */               this.parent.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ELEMENT_REF.format(new Object[0]), this));
/*     */             }
/*     */ 
/* 253 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 259 */     this.types = Collections.unmodifiableSet(this.types);
/*     */   }
/*     */ 
/*     */   public boolean isRequired() {
/* 263 */     if (this.isRequired == null)
/* 264 */       calcTypes(false);
/* 265 */     return this.isRequired.booleanValue();
/*     */   }
/*     */ 
/*     */   private boolean isRequired(XmlElementRef ref)
/*     */   {
/* 280 */     if (!is2_2) return true;
/*     */     try
/*     */     {
/* 283 */       return ref.required();
/*     */     } catch (LinkageError e) {
/* 285 */       is2_2 = false;
/* 286 */     }return true;
/*     */   }
/*     */ 
/*     */   private boolean addGenericElement(XmlElementRef r)
/*     */   {
/* 295 */     String nsUri = getEffectiveNamespaceFor(r);
/*     */ 
/* 297 */     return addGenericElement(this.parent.owner.getElementInfo(this.parent.getClazz(), new QName(nsUri, r.name())));
/*     */   }
/*     */ 
/*     */   private boolean addGenericElement(XmlElementRef r, ReferencePropertyInfoImpl<T, C, F, M> info) {
/* 301 */     String nsUri = info.getEffectiveNamespaceFor(r);
/* 302 */     ElementInfo ei = this.parent.owner.getElementInfo(info.parent.getClazz(), new QName(nsUri, r.name()));
/* 303 */     this.types.add(ei);
/* 304 */     return true;
/*     */   }
/*     */ 
/*     */   private String getEffectiveNamespaceFor(XmlElementRef r) {
/* 308 */     String nsUri = r.namespace();
/*     */ 
/* 310 */     XmlSchema xs = (XmlSchema)reader().getPackageAnnotation(XmlSchema.class, this.parent.getClazz(), this);
/* 311 */     if ((xs != null) && (xs.attributeFormDefault() == XmlNsForm.QUALIFIED))
/*     */     {
/* 314 */       if (nsUri.length() == 0) {
/* 315 */         nsUri = this.parent.builder.defaultNsUri;
/*     */       }
/*     */     }
/* 318 */     return nsUri;
/*     */   }
/*     */ 
/*     */   private boolean addGenericElement(ElementInfo<T, C> ei) {
/* 322 */     if (ei == null)
/* 323 */       return false;
/* 324 */     this.types.add(ei);
/* 325 */     for (ElementInfo subst : ei.getSubstitutionMembers())
/* 326 */       addGenericElement(subst);
/* 327 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean addAllSubtypes(T type) {
/* 331 */     Navigator nav = nav();
/*     */ 
/* 334 */     NonElement t = this.parent.builder.getClassInfo(nav.asDecl(type), this);
/* 335 */     if (!(t instanceof ClassInfo))
/*     */     {
/* 337 */       return false;
/*     */     }
/* 339 */     boolean result = false;
/*     */ 
/* 341 */     ClassInfo c = (ClassInfo)t;
/* 342 */     if (c.isElement()) {
/* 343 */       this.types.add(c.asElement());
/* 344 */       result = true;
/*     */     }
/*     */ 
/* 348 */     for (ClassInfo ci : this.parent.owner.beans().values()) {
/* 349 */       if ((ci.isElement()) && (nav.isSubClassOf(ci.getType(), type))) {
/* 350 */         this.types.add(ci.asElement());
/* 351 */         result = true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 356 */     for (ElementInfo ei : this.parent.owner.getElementMappings(null).values()) {
/* 357 */       if (nav.isSubClassOf(ei.getType(), type)) {
/* 358 */         this.types.add(ei);
/* 359 */         result = true;
/*     */       }
/*     */     }
/*     */ 
/* 363 */     return result;
/*     */   }
/*     */ 
/*     */   protected void link()
/*     */   {
/* 369 */     super.link();
/*     */ 
/* 374 */     calcTypes(true);
/*     */   }
/*     */ 
/*     */   public final void addType(PropertyInfoImpl<T, C, F, M> info)
/*     */   {
/* 379 */     this.subTypes.add(info);
/*     */   }
/*     */ 
/*     */   public final boolean isMixed() {
/* 383 */     return this.isMixed;
/*     */   }
/*     */ 
/*     */   public final WildcardMode getWildcard() {
/* 387 */     return this.wildcard;
/*     */   }
/*     */ 
/*     */   public final C getDOMHandler() {
/* 391 */     return this.domHandler;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.ReferencePropertyInfoImpl
 * JD-Core Version:    0.6.2
 */