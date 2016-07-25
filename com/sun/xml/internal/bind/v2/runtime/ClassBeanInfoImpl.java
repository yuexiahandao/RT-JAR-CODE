/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.xml.internal.bind.Util;
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.AttributeProperty;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.Property;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.StructureLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiTypeLoader;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public final class ClassBeanInfoImpl<BeanT> extends JaxBeanInfo<BeanT>
/*     */   implements AttributeAccessor<BeanT>
/*     */ {
/*     */   public final Property<BeanT>[] properties;
/*     */   private Property<? super BeanT> idProperty;
/*     */   private Loader loader;
/*     */   private Loader loaderWithTypeSubst;
/*     */   private RuntimeClassInfo ci;
/*     */   private final Accessor<? super BeanT, Map<QName, String>> inheritedAttWildcard;
/*     */   private final Transducer<BeanT> xducer;
/*     */   public final ClassBeanInfoImpl<? super BeanT> superClazz;
/*     */   private final Accessor<? super BeanT, Locator> xmlLocatorField;
/*     */   private final Name tagName;
/* 109 */   private boolean retainPropertyInfo = false;
/*     */   private AttributeProperty<BeanT>[] attributeProperties;
/*     */   private Property<BeanT>[] uriProperties;
/*     */   private final Method factoryMethod;
/* 427 */   private static final AttributeProperty[] EMPTY_PROPERTIES = new AttributeProperty[0];
/*     */ 
/* 429 */   private static final Logger logger = Util.getClassLogger();
/*     */ 
/*     */   ClassBeanInfoImpl(JAXBContextImpl owner, RuntimeClassInfo ci)
/*     */   {
/* 125 */     super(owner, ci, (Class)ci.getClazz(), ci.getTypeName(), ci.isElement(), false, true);
/*     */ 
/* 127 */     this.ci = ci;
/* 128 */     this.inheritedAttWildcard = ci.getAttributeWildcard();
/* 129 */     this.xducer = ci.getTransducer();
/* 130 */     this.factoryMethod = ci.getFactoryMethod();
/* 131 */     this.retainPropertyInfo = owner.retainPropertyInfo;
/*     */ 
/* 134 */     if (this.factoryMethod != null) {
/* 135 */       int classMod = this.factoryMethod.getDeclaringClass().getModifiers();
/*     */ 
/* 137 */       if ((!Modifier.isPublic(classMod)) || (!Modifier.isPublic(this.factoryMethod.getModifiers()))) {
/*     */         try
/*     */         {
/* 140 */           this.factoryMethod.setAccessible(true);
/*     */         }
/*     */         catch (SecurityException e) {
/* 143 */           logger.log(Level.FINE, "Unable to make the method of " + this.factoryMethod + " accessible", e);
/* 144 */           throw e;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 150 */     if (ci.getBaseClass() == null)
/* 151 */       this.superClazz = null;
/*     */     else {
/* 153 */       this.superClazz = owner.getOrCreate(ci.getBaseClass());
/*     */     }
/* 155 */     if ((this.superClazz != null) && (this.superClazz.xmlLocatorField != null))
/* 156 */       this.xmlLocatorField = this.superClazz.xmlLocatorField;
/*     */     else {
/* 158 */       this.xmlLocatorField = ci.getLocatorField();
/*     */     }
/*     */ 
/* 161 */     Collection ps = ci.getProperties();
/* 162 */     this.properties = new Property[ps.size()];
/* 163 */     int idx = 0;
/* 164 */     boolean elementOnly = true;
/* 165 */     for (RuntimePropertyInfo info : ps) {
/* 166 */       Property p = PropertyFactory.create(owner, info);
/* 167 */       if (info.id() == ID.ID)
/* 168 */         this.idProperty = p;
/* 169 */       this.properties[(idx++)] = p;
/* 170 */       elementOnly &= info.elementOnlyContent();
/* 171 */       checkOverrideProperties(p);
/*     */     }
/*     */ 
/* 176 */     hasElementOnlyContentModel(elementOnly);
/*     */ 
/* 179 */     if (ci.isElement())
/* 180 */       this.tagName = owner.nameBuilder.createElementName(ci.getElementName());
/*     */     else {
/* 182 */       this.tagName = null;
/*     */     }
/* 184 */     setLifecycleFlags();
/*     */   }
/*     */ 
/*     */   private void checkOverrideProperties(Property p) {
/* 188 */     ClassBeanInfoImpl bi = this;
/* 189 */     while ((bi = bi.superClazz) != null) {
/* 190 */       Property[] props = bi.properties;
/* 191 */       if (props == null) break;
/* 192 */       for (Property superProperty : props) {
/* 193 */         if (superProperty == null) break;
/* 194 */         String spName = superProperty.getFieldName();
/* 195 */         if ((spName != null) && (spName.equals(p.getFieldName())))
/* 196 */           superProperty.setHiddenByOverride(true);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void link(JAXBContextImpl grammar)
/*     */   {
/* 204 */     if (this.uriProperties != null) {
/* 205 */       return;
/*     */     }
/* 207 */     super.link(grammar);
/*     */ 
/* 209 */     if (this.superClazz != null) {
/* 210 */       this.superClazz.link(grammar);
/*     */     }
/* 212 */     getLoader(grammar, true);
/*     */ 
/* 215 */     if (this.superClazz != null) {
/* 216 */       if (this.idProperty == null) {
/* 217 */         this.idProperty = this.superClazz.idProperty;
/*     */       }
/* 219 */       if (!this.superClazz.hasElementOnlyContentModel()) {
/* 220 */         hasElementOnlyContentModel(false);
/*     */       }
/*     */     }
/*     */ 
/* 224 */     List attProps = new FinalArrayList();
/* 225 */     List uriProps = new FinalArrayList();
/* 226 */     for (ClassBeanInfoImpl bi = this; bi != null; bi = bi.superClazz) {
/* 227 */       for (int i = 0; i < bi.properties.length; i++) {
/* 228 */         Property p = bi.properties[i];
/* 229 */         if ((p instanceof AttributeProperty))
/* 230 */           attProps.add((AttributeProperty)p);
/* 231 */         if (p.hasSerializeURIAction())
/* 232 */           uriProps.add(p);
/*     */       }
/*     */     }
/* 235 */     if (grammar.c14nSupport) {
/* 236 */       Collections.sort(attProps);
/*     */     }
/* 238 */     if (attProps.isEmpty())
/* 239 */       this.attributeProperties = EMPTY_PROPERTIES;
/*     */     else {
/* 241 */       this.attributeProperties = ((AttributeProperty[])attProps.toArray(new AttributeProperty[attProps.size()]));
/*     */     }
/* 243 */     if (uriProps.isEmpty())
/* 244 */       this.uriProperties = EMPTY_PROPERTIES;
/*     */     else
/* 246 */       this.uriProperties = ((Property[])uriProps.toArray(new Property[uriProps.size()]));
/*     */   }
/*     */ 
/*     */   public void wrapUp()
/*     */   {
/* 251 */     for (Property p : this.properties)
/* 252 */       p.wrapUp();
/* 253 */     this.ci = null;
/* 254 */     super.wrapUp();
/*     */   }
/*     */ 
/*     */   public String getElementNamespaceURI(BeanT bean) {
/* 258 */     return this.tagName.nsUri;
/*     */   }
/*     */ 
/*     */   public String getElementLocalName(BeanT bean) {
/* 262 */     return this.tagName.localName;
/*     */   }
/*     */ 
/*     */   public BeanT createInstance(UnmarshallingContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException, SAXException
/*     */   {
/* 267 */     Object bean = null;
/* 268 */     if (this.factoryMethod == null) {
/* 269 */       bean = ClassFactory.create0(this.jaxbType);
/*     */     } else {
/* 271 */       Object o = ClassFactory.create(this.factoryMethod);
/* 272 */       if (this.jaxbType.isInstance(o))
/* 273 */         bean = o;
/*     */       else {
/* 275 */         throw new InstantiationException("The factory method didn't return a correct object");
/*     */       }
/*     */     }
/*     */ 
/* 279 */     if (this.xmlLocatorField != null)
/*     */       try
/*     */       {
/* 282 */         this.xmlLocatorField.set(bean, new LocatorImpl(context.getLocator()));
/*     */       } catch (AccessorException e) {
/* 284 */         context.handleError(e);
/*     */       }
/* 286 */     return bean;
/*     */   }
/*     */ 
/*     */   public boolean reset(BeanT bean, UnmarshallingContext context) throws SAXException {
/*     */     try {
/* 291 */       if (this.superClazz != null)
/* 292 */         this.superClazz.reset(bean, context);
/* 293 */       for (Property p : this.properties)
/* 294 */         p.reset(bean);
/* 295 */       return true;
/*     */     } catch (AccessorException e) {
/* 297 */       context.handleError(e);
/* 298 */     }return false;
/*     */   }
/*     */ 
/*     */   public String getId(BeanT bean, XMLSerializer target) throws SAXException
/*     */   {
/* 303 */     if (this.idProperty != null) {
/*     */       try {
/* 305 */         return this.idProperty.getIdValue(bean);
/*     */       } catch (AccessorException e) {
/* 307 */         target.reportError(null, e);
/*     */       }
/*     */     }
/* 310 */     return null;
/*     */   }
/*     */ 
/*     */   public void serializeRoot(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 314 */     if (this.tagName == null) {
/* 315 */       Class beanClass = bean.getClass();
/*     */       String message;
/*     */       String message;
/* 317 */       if (beanClass.isAnnotationPresent(XmlRootElement.class))
/* 318 */         message = Messages.UNABLE_TO_MARSHAL_UNBOUND_CLASS.format(new Object[] { beanClass.getName() });
/*     */       else {
/* 320 */         message = Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { beanClass.getName() });
/*     */       }
/* 322 */       target.reportError(new ValidationEventImpl(1, message, null, null));
/*     */     } else {
/* 324 */       target.startElement(this.tagName, bean);
/* 325 */       target.childAsSoleContent(bean, null);
/* 326 */       target.endElement();
/* 327 */       if (this.retainPropertyInfo)
/* 328 */         target.currentProperty.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serializeBody(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException
/*     */   {
/* 334 */     if (this.superClazz != null)
/* 335 */       this.superClazz.serializeBody(bean, target);
/*     */     try
/*     */     {
/* 338 */       for (Property p : this.properties) {
/* 339 */         if (this.retainPropertyInfo) {
/* 340 */           target.currentProperty.set(p);
/*     */         }
/* 342 */         if ((!p.isHiddenByOverride()) || (bean.getClass().equals(this.jaxbType)))
/* 343 */           p.serializeBody(bean, target, null);
/*     */       }
/*     */     }
/*     */     catch (AccessorException e) {
/* 347 */       target.reportError(null, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serializeAttributes(BeanT bean, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 352 */     for (AttributeProperty p : this.attributeProperties)
/*     */       try {
/* 354 */         if (this.retainPropertyInfo) {
/* 355 */           Property parentProperty = target.getCurrentProperty();
/* 356 */           target.currentProperty.set(p);
/* 357 */           p.serializeAttributes(bean, target);
/* 358 */           target.currentProperty.set(parentProperty);
/*     */         } else {
/* 360 */           p.serializeAttributes(bean, target);
/*     */         }
/* 362 */         if (p.attName.equals("http://www.w3.org/2001/XMLSchema-instance", "nil"))
/* 363 */           this.isNilIncluded = true;
/*     */       }
/*     */       catch (AccessorException e) {
/* 366 */         target.reportError(null, e);
/*     */       }
/*     */     try
/*     */     {
/* 370 */       if (this.inheritedAttWildcard != null) {
/* 371 */         Map map = (Map)this.inheritedAttWildcard.get(bean);
/* 372 */         target.attWildcardAsAttributes(map, null);
/*     */       }
/*     */     } catch (AccessorException e) {
/* 375 */       target.reportError(null, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serializeURIs(BeanT bean, XMLSerializer target) throws SAXException {
/*     */     try {
/* 381 */       if (this.retainPropertyInfo) {
/* 382 */         Property parentProperty = target.getCurrentProperty();
/* 383 */         for (Property p : this.uriProperties) {
/* 384 */           target.currentProperty.set(p);
/* 385 */           p.serializeURIs(bean, target);
/*     */         }
/* 387 */         target.currentProperty.set(parentProperty);
/*     */       } else {
/* 389 */         for (Property p : this.uriProperties) {
/* 390 */           p.serializeURIs(bean, target);
/*     */         }
/*     */       }
/* 393 */       if (this.inheritedAttWildcard != null) {
/* 394 */         Map map = (Map)this.inheritedAttWildcard.get(bean);
/* 395 */         target.attWildcardAsURIs(map, null);
/*     */       }
/*     */     } catch (AccessorException e) {
/* 398 */       target.reportError(null, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
/* 403 */     if (this.loader == null)
/*     */     {
/* 406 */       StructureLoader sl = new StructureLoader(this);
/* 407 */       this.loader = sl;
/* 408 */       if (this.ci.hasSubClasses()) {
/* 409 */         this.loaderWithTypeSubst = new XsiTypeLoader(this);
/*     */       }
/*     */       else {
/* 412 */         this.loaderWithTypeSubst = this.loader;
/*     */       }
/*     */ 
/* 415 */       sl.init(context, this, this.ci.getAttributeWildcard());
/*     */     }
/* 417 */     if (typeSubstitutionCapable) {
/* 418 */       return this.loaderWithTypeSubst;
/*     */     }
/* 420 */     return this.loader;
/*     */   }
/*     */ 
/*     */   public Transducer<BeanT> getTransducer() {
/* 424 */     return this.xducer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.ClassBeanInfoImpl
 * JD-Core Version:    0.6.2
 */