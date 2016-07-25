/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.AccessorFactory;
/*     */ import com.sun.xml.internal.bind.AccessorFactoryImpl;
/*     */ import com.sun.xml.internal.bind.InternalAccessorFactory;
/*     */ import com.sun.xml.internal.bind.XmlAccessorFactory;
/*     */ import com.sun.xml.internal.bind.annotation.XmlLocation;
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElement;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeValuePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.FieldReflection;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ class RuntimeClassInfoImpl extends ClassInfoImpl<Type, Class, Field, Method>
/*     */   implements RuntimeClassInfo, RuntimeElement
/*     */ {
/*     */   private Accessor<?, Locator> xmlLocationAccessor;
/*     */   private AccessorFactory accessorFactory;
/*  84 */   private boolean supressAccessorWarnings = false;
/*     */   private Accessor<?, Map<QName, String>> attributeWildcardAccessor;
/* 198 */   private boolean computedTransducer = false;
/* 199 */   private Transducer xducer = null;
/*     */ 
/*     */   public RuntimeClassInfoImpl(RuntimeModelBuilder modelBuilder, Locatable upstream, Class clazz)
/*     */   {
/*  87 */     super(modelBuilder, upstream, clazz);
/*  88 */     this.accessorFactory = createAccessorFactory(clazz);
/*     */   }
/*     */ 
/*     */   protected AccessorFactory createAccessorFactory(Class clazz)
/*     */   {
/*  93 */     AccessorFactory accFactory = null;
/*     */ 
/*  96 */     JAXBContextImpl context = ((RuntimeModelBuilder)this.builder).context;
/*  97 */     if (context != null) {
/*  98 */       this.supressAccessorWarnings = context.supressAccessorWarnings;
/*  99 */       if (context.xmlAccessorFactorySupport) {
/* 100 */         XmlAccessorFactory factoryAnn = findXmlAccessorFactoryAnnotation(clazz);
/* 101 */         if (factoryAnn != null) {
/*     */           try {
/* 103 */             accFactory = (AccessorFactory)factoryAnn.value().newInstance();
/*     */           } catch (InstantiationException e) {
/* 105 */             this.builder.reportError(new IllegalAnnotationException(Messages.ACCESSORFACTORY_INSTANTIATION_EXCEPTION.format(new Object[] { factoryAnn.getClass().getName(), nav().getClassName(clazz) }), this));
/*     */           }
/*     */           catch (IllegalAccessException e)
/*     */           {
/* 109 */             this.builder.reportError(new IllegalAnnotationException(Messages.ACCESSORFACTORY_ACCESS_EXCEPTION.format(new Object[] { factoryAnn.getClass().getName(), nav().getClassName(clazz) }), this));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 120 */     if (accFactory == null) {
/* 121 */       accFactory = AccessorFactoryImpl.getInstance();
/*     */     }
/* 123 */     return accFactory;
/*     */   }
/*     */ 
/*     */   protected XmlAccessorFactory findXmlAccessorFactoryAnnotation(Class clazz) {
/* 127 */     XmlAccessorFactory factoryAnn = (XmlAccessorFactory)reader().getClassAnnotation(XmlAccessorFactory.class, clazz, this);
/* 128 */     if (factoryAnn == null) {
/* 129 */       factoryAnn = (XmlAccessorFactory)reader().getPackageAnnotation(XmlAccessorFactory.class, clazz, this);
/*     */     }
/* 131 */     return factoryAnn;
/*     */   }
/*     */ 
/*     */   public Method getFactoryMethod()
/*     */   {
/* 136 */     return super.getFactoryMethod();
/*     */   }
/*     */ 
/*     */   public final RuntimeClassInfoImpl getBaseClass() {
/* 140 */     return (RuntimeClassInfoImpl)super.getBaseClass();
/*     */   }
/*     */ 
/*     */   protected ReferencePropertyInfoImpl createReferenceProperty(PropertySeed<Type, Class, Field, Method> seed)
/*     */   {
/* 145 */     return new RuntimeReferencePropertyInfoImpl(this, seed);
/*     */   }
/*     */ 
/*     */   protected AttributePropertyInfoImpl createAttributeProperty(PropertySeed<Type, Class, Field, Method> seed)
/*     */   {
/* 150 */     return new RuntimeAttributePropertyInfoImpl(this, seed);
/*     */   }
/*     */ 
/*     */   protected ValuePropertyInfoImpl createValueProperty(PropertySeed<Type, Class, Field, Method> seed)
/*     */   {
/* 155 */     return new RuntimeValuePropertyInfoImpl(this, seed);
/*     */   }
/*     */ 
/*     */   protected ElementPropertyInfoImpl createElementProperty(PropertySeed<Type, Class, Field, Method> seed)
/*     */   {
/* 160 */     return new RuntimeElementPropertyInfoImpl(this, seed);
/*     */   }
/*     */ 
/*     */   protected MapPropertyInfoImpl createMapProperty(PropertySeed<Type, Class, Field, Method> seed)
/*     */   {
/* 165 */     return new RuntimeMapPropertyInfoImpl(this, seed);
/*     */   }
/*     */ 
/*     */   public List<? extends RuntimePropertyInfo> getProperties()
/*     */   {
/* 171 */     return super.getProperties();
/*     */   }
/*     */ 
/*     */   public RuntimePropertyInfo getProperty(String name)
/*     */   {
/* 176 */     return (RuntimePropertyInfo)super.getProperty(name);
/*     */   }
/*     */ 
/*     */   public void link()
/*     */   {
/* 181 */     getTransducer();
/* 182 */     super.link();
/*     */   }
/*     */ 
/*     */   public <B> Accessor<B, Map<QName, String>> getAttributeWildcard()
/*     */   {
/* 188 */     for (RuntimeClassInfoImpl c = this; c != null; c = c.getBaseClass()) {
/* 189 */       if (c.attributeWildcard != null) {
/* 190 */         if (c.attributeWildcardAccessor == null)
/* 191 */           c.attributeWildcardAccessor = c.createAttributeWildcardAccessor();
/* 192 */         return c.attributeWildcardAccessor;
/*     */       }
/*     */     }
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */   public Transducer getTransducer()
/*     */   {
/* 202 */     if (!this.computedTransducer) {
/* 203 */       this.computedTransducer = true;
/* 204 */       this.xducer = calcTransducer();
/*     */     }
/* 206 */     return this.xducer;
/*     */   }
/*     */ 
/*     */   private Transducer calcTransducer()
/*     */   {
/* 213 */     RuntimeValuePropertyInfo valuep = null;
/* 214 */     if (hasAttributeWildcard())
/* 215 */       return null;
/* 216 */     for (RuntimeClassInfoImpl ci = this; ci != null; ci = ci.getBaseClass()) {
/* 217 */       for (RuntimePropertyInfo pi : ci.getProperties()) {
/* 218 */         if (pi.kind() == PropertyKind.VALUE) {
/* 219 */           valuep = (RuntimeValuePropertyInfo)pi;
/*     */         }
/*     */         else
/* 222 */           return null;
/*     */       }
/*     */     }
/* 225 */     if (valuep == null)
/* 226 */       return null;
/* 227 */     if (!valuep.getTarget().isSimpleType()) {
/* 228 */       return null;
/*     */     }
/* 230 */     return new TransducerImpl((Class)getClazz(), TransducedAccessor.get(((RuntimeModelBuilder)this.builder).context, valuep));
/*     */   }
/*     */ 
/*     */   private Accessor<?, Map<QName, String>> createAttributeWildcardAccessor()
/*     */   {
/* 238 */     assert (this.attributeWildcard != null);
/* 239 */     return ((RuntimePropertySeed)this.attributeWildcard).getAccessor();
/*     */   }
/*     */ 
/*     */   protected RuntimePropertySeed createFieldSeed(Field field)
/*     */   {
/* 244 */     boolean readOnly = Modifier.isStatic(field.getModifiers());
/*     */     Accessor acc;
/*     */     try
/*     */     {
/*     */       Accessor acc;
/* 247 */       if (this.supressAccessorWarnings)
/* 248 */         acc = ((InternalAccessorFactory)this.accessorFactory).createFieldAccessor((Class)this.clazz, field, readOnly, this.supressAccessorWarnings);
/*     */       else
/* 250 */         acc = this.accessorFactory.createFieldAccessor((Class)this.clazz, field, readOnly);
/*     */     }
/*     */     catch (JAXBException e) {
/* 253 */       this.builder.reportError(new IllegalAnnotationException(Messages.CUSTOM_ACCESSORFACTORY_FIELD_ERROR.format(new Object[] { nav().getClassName(this.clazz), e.toString() }), this));
/*     */ 
/* 256 */       acc = Accessor.getErrorInstance();
/*     */     }
/* 258 */     return new RuntimePropertySeed(super.createFieldSeed(field), acc);
/*     */   }
/*     */ 
/*     */   public RuntimePropertySeed createAccessorSeed(Method getter, Method setter)
/*     */   {
/*     */     Accessor acc;
/*     */     try {
/* 265 */       acc = this.accessorFactory.createPropertyAccessor((Class)this.clazz, getter, setter);
/*     */     } catch (JAXBException e) {
/* 267 */       this.builder.reportError(new IllegalAnnotationException(Messages.CUSTOM_ACCESSORFACTORY_PROPERTY_ERROR.format(new Object[] { nav().getClassName(this.clazz), e.toString() }), this));
/*     */ 
/* 270 */       acc = Accessor.getErrorInstance();
/*     */     }
/* 272 */     return new RuntimePropertySeed(super.createAccessorSeed(getter, setter), acc);
/*     */   }
/*     */ 
/*     */   protected void checkFieldXmlLocation(Field f)
/*     */   {
/* 278 */     if (reader().hasFieldAnnotation(XmlLocation.class, f))
/*     */     {
/* 281 */       this.xmlLocationAccessor = new Accessor.FieldReflection(f);
/*     */     }
/*     */   }
/*     */ 
/* 285 */   public Accessor<?, Locator> getLocatorField() { return this.xmlLocationAccessor; }
/*     */ 
/*     */ 
/*     */   static final class RuntimePropertySeed
/*     */     implements PropertySeed<Type, Class, Field, Method>
/*     */   {
/*     */     private final Accessor acc;
/*     */     private final PropertySeed<Type, Class, Field, Method> core;
/*     */ 
/*     */     public RuntimePropertySeed(PropertySeed<Type, Class, Field, Method> core, Accessor acc)
/*     */     {
/* 297 */       this.core = core;
/* 298 */       this.acc = acc;
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 302 */       return this.core.getName();
/*     */     }
/*     */ 
/*     */     public <A extends Annotation> A readAnnotation(Class<A> annotationType) {
/* 306 */       return this.core.readAnnotation(annotationType);
/*     */     }
/*     */ 
/*     */     public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/* 310 */       return this.core.hasAnnotation(annotationType);
/*     */     }
/*     */ 
/*     */     public Type getRawType() {
/* 314 */       return (Type)this.core.getRawType();
/*     */     }
/*     */ 
/*     */     public Location getLocation() {
/* 318 */       return this.core.getLocation();
/*     */     }
/*     */ 
/*     */     public Locatable getUpstream() {
/* 322 */       return this.core.getUpstream();
/*     */     }
/*     */ 
/*     */     public Accessor getAccessor() {
/* 326 */       return this.acc;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class TransducerImpl<BeanT>
/*     */     implements Transducer<BeanT>
/*     */   {
/*     */     private final TransducedAccessor<BeanT> xacc;
/*     */     private final Class<BeanT> ownerClass;
/*     */ 
/*     */     public TransducerImpl(Class<BeanT> ownerClass, TransducedAccessor<BeanT> xacc)
/*     */     {
/* 342 */       this.xacc = xacc;
/* 343 */       this.ownerClass = ownerClass;
/*     */     }
/*     */ 
/*     */     public boolean useNamespace() {
/* 347 */       return this.xacc.useNamespace();
/*     */     }
/*     */ 
/*     */     public boolean isDefault() {
/* 351 */       return false;
/*     */     }
/*     */ 
/*     */     public void declareNamespace(BeanT bean, XMLSerializer w) throws AccessorException {
/*     */       try {
/* 356 */         this.xacc.declareNamespace(bean, w);
/*     */       } catch (SAXException e) {
/* 358 */         throw new AccessorException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     @NotNull
/*     */     public CharSequence print(BeanT o) throws AccessorException {
/*     */       try { CharSequence value = this.xacc.print(o);
/* 365 */         if (value == null)
/* 366 */           throw new AccessorException(Messages.THERE_MUST_BE_VALUE_IN_XMLVALUE.format(new Object[] { o }));
/* 367 */         return value;
/*     */       } catch (SAXException e) {
/* 369 */         throw new AccessorException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     public BeanT parse(CharSequence lexical) throws AccessorException, SAXException {
/* 374 */       UnmarshallingContext ctxt = UnmarshallingContext.getInstance();
/*     */       Object inst;
/*     */       Object inst;
/* 376 */       if (ctxt != null) {
/* 377 */         inst = ctxt.createInstance(this.ownerClass);
/*     */       }
/*     */       else
/*     */       {
/* 381 */         inst = ClassFactory.create(this.ownerClass);
/*     */       }
/* 383 */       this.xacc.parse(inst, lexical);
/* 384 */       return inst;
/*     */     }
/*     */ 
/*     */     public void writeText(XMLSerializer w, BeanT o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 388 */       if (!this.xacc.hasValue(o))
/* 389 */         throw new AccessorException(Messages.THERE_MUST_BE_VALUE_IN_XMLVALUE.format(new Object[] { o }));
/* 390 */       this.xacc.writeText(w, o, fieldName);
/*     */     }
/*     */ 
/*     */     public void writeLeafElement(XMLSerializer w, Name tagName, BeanT o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 394 */       if (!this.xacc.hasValue(o))
/* 395 */         throw new AccessorException(Messages.THERE_MUST_BE_VALUE_IN_XMLVALUE.format(new Object[] { o }));
/* 396 */       this.xacc.writeLeafElement(w, tagName, o, fieldName);
/*     */     }
/*     */ 
/*     */     public QName getTypeName(BeanT instance) {
/* 400 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeClassInfoImpl
 * JD-Core Version:    0.6.2
 */