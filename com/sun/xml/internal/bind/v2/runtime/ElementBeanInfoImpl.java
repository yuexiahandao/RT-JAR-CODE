/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.Property;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.PropertyFactory;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.UnmarshallerChain;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Discarder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Intercepter;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext.State;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap.Entry;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBElement.GlobalScope;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class ElementBeanInfoImpl extends JaxBeanInfo<JAXBElement>
/*     */ {
/*     */   private Loader loader;
/*     */   private final Property property;
/*     */   private final QName tagName;
/*     */   public final Class expectedType;
/*     */   private final Class scope;
/*     */   private final Constructor<? extends JAXBElement> constructor;
/*     */ 
/*     */   ElementBeanInfoImpl(JAXBContextImpl grammar, RuntimeElementInfo rei)
/*     */   {
/*  78 */     super(grammar, rei, rei.getType(), true, false, true);
/*     */ 
/*  80 */     this.property = PropertyFactory.create(grammar, rei.getProperty());
/*     */ 
/*  82 */     this.tagName = rei.getElementName();
/*  83 */     this.expectedType = ((Class)Utils.REFLECTION_NAVIGATOR.erasure(rei.getContentInMemoryType()));
/*  84 */     this.scope = (rei.getScope() == null ? JAXBElement.GlobalScope.class : (Class)rei.getScope().getClazz());
/*     */ 
/*  86 */     Class type = (Class)Utils.REFLECTION_NAVIGATOR.erasure(rei.getType());
/*  87 */     if (type == JAXBElement.class)
/*  88 */       this.constructor = null;
/*     */     else
/*     */       try {
/*  91 */         this.constructor = type.getConstructor(new Class[] { this.expectedType });
/*     */       } catch (NoSuchMethodException e) {
/*  93 */         NoSuchMethodError x = new NoSuchMethodError("Failed to find the constructor for " + type + " with " + this.expectedType);
/*  94 */         x.initCause(e);
/*  95 */         throw x;
/*     */       }
/*     */   }
/*     */ 
/*     */   protected ElementBeanInfoImpl(final JAXBContextImpl grammar)
/*     */   {
/* 109 */     super(grammar, null, JAXBElement.class, true, false, true);
/* 110 */     this.tagName = null;
/* 111 */     this.expectedType = null;
/* 112 */     this.scope = null;
/* 113 */     this.constructor = null;
/*     */ 
/* 115 */     this.property = new Property() {
/*     */       public void reset(JAXBElement o) {
/* 117 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */       public void serializeBody(JAXBElement e, XMLSerializer target, Object outerPeer) throws SAXException, IOException, XMLStreamException {
/* 121 */         Class scope = e.getScope();
/* 122 */         if (e.isGlobalScope()) scope = null;
/* 123 */         QName n = e.getName();
/* 124 */         ElementBeanInfoImpl bi = grammar.getElement(scope, n);
/* 125 */         if (bi == null)
/*     */         {
/*     */           JaxBeanInfo tbi;
/*     */           try {
/* 129 */             tbi = grammar.getBeanInfo(e.getDeclaredType(), true);
/*     */           }
/*     */           catch (JAXBException x) {
/* 132 */             target.reportError(null, x);
/* 133 */             return;
/*     */           }
/* 135 */           Object value = e.getValue();
/* 136 */           target.startElement(n.getNamespaceURI(), n.getLocalPart(), n.getPrefix(), null);
/* 137 */           if (value == null)
/* 138 */             target.writeXsiNilTrue();
/*     */           else {
/* 140 */             target.childAsXsiType(value, "value", tbi, false);
/*     */           }
/* 142 */           target.endElement();
/*     */         } else {
/*     */           try {
/* 145 */             bi.property.serializeBody(e, target, e);
/*     */           } catch (AccessorException x) {
/* 147 */             target.reportError(null, x);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */       public void serializeURIs(JAXBElement o, XMLSerializer target) {
/*     */       }
/*     */ 
/*     */       public boolean hasSerializeURIAction() {
/* 156 */         return false;
/*     */       }
/*     */ 
/*     */       public String getIdValue(JAXBElement o) {
/* 160 */         return null;
/*     */       }
/*     */ 
/*     */       public PropertyKind getKind() {
/* 164 */         return PropertyKind.ELEMENT;
/*     */       }
/*     */ 
/*     */       public void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> handlers) {
/*     */       }
/*     */ 
/*     */       public Accessor getElementPropertyAccessor(String nsUri, String localName) {
/* 171 */         throw new UnsupportedOperationException();
/*     */       }
/*     */ 
/*     */       public void wrapUp() {
/*     */       }
/*     */ 
/*     */       public RuntimePropertyInfo getInfo() {
/* 178 */         return ElementBeanInfoImpl.this.property.getInfo();
/*     */       }
/*     */ 
/*     */       public boolean isHiddenByOverride() {
/* 182 */         return false;
/*     */       }
/*     */ 
/*     */       public void setHiddenByOverride(boolean hidden) {
/* 186 */         throw new UnsupportedOperationException("Not supported on jaxbelements.");
/*     */       }
/*     */ 
/*     */       public String getFieldName() {
/* 190 */         return null;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public String getElementNamespaceURI(JAXBElement e)
/*     */   {
/* 263 */     return e.getName().getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public String getElementLocalName(JAXBElement e) {
/* 267 */     return e.getName().getLocalPart();
/*     */   }
/*     */ 
/*     */   public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable) {
/* 271 */     if (this.loader == null)
/*     */     {
/* 273 */       UnmarshallerChain c = new UnmarshallerChain(context);
/* 274 */       QNameMap result = new QNameMap();
/* 275 */       this.property.buildChildElementUnmarshallers(c, result);
/* 276 */       if (result.size() == 1)
/*     */       {
/* 278 */         this.loader = new IntercepterLoader(((ChildLoader)result.getOne().getValue()).loader);
/*     */       }
/*     */       else
/* 281 */         this.loader = Discarder.INSTANCE;
/*     */     }
/* 283 */     return this.loader;
/*     */   }
/*     */ 
/*     */   public final JAXBElement createInstance(UnmarshallingContext context) throws IllegalAccessException, InvocationTargetException, InstantiationException {
/* 287 */     return createInstanceFromValue(null);
/*     */   }
/*     */ 
/*     */   public final JAXBElement createInstanceFromValue(Object o) throws IllegalAccessException, InvocationTargetException, InstantiationException {
/* 291 */     if (this.constructor == null) {
/* 292 */       return new JAXBElement(this.tagName, this.expectedType, this.scope, o);
/*     */     }
/* 294 */     return (JAXBElement)this.constructor.newInstance(new Object[] { o });
/*     */   }
/*     */ 
/*     */   public boolean reset(JAXBElement e, UnmarshallingContext context) {
/* 298 */     e.setValue(null);
/* 299 */     return true;
/*     */   }
/*     */ 
/*     */   public String getId(JAXBElement e, XMLSerializer target)
/*     */   {
/* 308 */     Object o = e.getValue();
/* 309 */     if ((o instanceof String)) {
/* 310 */       return (String)o;
/*     */     }
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   public void serializeBody(JAXBElement element, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/*     */     try {
/* 317 */       this.property.serializeBody(element, target, null);
/*     */     } catch (AccessorException x) {
/* 319 */       target.reportError(null, x);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serializeRoot(JAXBElement e, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/* 324 */     serializeBody(e, target);
/*     */   }
/*     */ 
/*     */   public void serializeAttributes(JAXBElement e, XMLSerializer target)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void serializeURIs(JAXBElement e, XMLSerializer target)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final Transducer<JAXBElement> getTransducer() {
/* 336 */     return null;
/*     */   }
/*     */ 
/*     */   public void wrapUp()
/*     */   {
/* 341 */     super.wrapUp();
/* 342 */     this.property.wrapUp();
/*     */   }
/*     */ 
/*     */   public void link(JAXBContextImpl grammar)
/*     */   {
/* 347 */     super.link(grammar);
/* 348 */     getLoader(grammar, true);
/*     */   }
/*     */ 
/*     */   private final class IntercepterLoader extends Loader
/*     */     implements Intercepter
/*     */   {
/*     */     private final Loader core;
/*     */ 
/*     */     public IntercepterLoader(Loader core)
/*     */     {
/* 206 */       this.core = core;
/*     */     }
/*     */ 
/*     */     public final void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */     {
/* 211 */       state.setLoader(this.core);
/* 212 */       state.setIntercepter(this);
/*     */ 
/* 217 */       UnmarshallingContext context = state.getContext();
/*     */ 
/* 220 */       Object child = context.getOuterPeer();
/*     */ 
/* 222 */       if ((child != null) && (ElementBeanInfoImpl.this.jaxbType != child.getClass())) {
/* 223 */         child = null;
/*     */       }
/* 225 */       if (child != null) {
/* 226 */         ElementBeanInfoImpl.this.reset((JAXBElement)child, context);
/*     */       }
/* 228 */       if (child == null) {
/* 229 */         child = context.createInstance(ElementBeanInfoImpl.this);
/*     */       }
/* 231 */       fireBeforeUnmarshal(ElementBeanInfoImpl.this, child, state);
/*     */ 
/* 233 */       context.recordOuterPeer(child);
/* 234 */       UnmarshallingContext.State p = state.getPrev();
/* 235 */       p.setBackup(p.getTarget());
/* 236 */       p.setTarget(child);
/*     */ 
/* 238 */       this.core.startElement(state, ea);
/*     */     }
/*     */ 
/*     */     public Object intercept(UnmarshallingContext.State state, Object o) throws SAXException {
/* 242 */       JAXBElement e = (JAXBElement)state.getTarget();
/* 243 */       state.setTarget(state.getBackup());
/* 244 */       state.setBackup(null);
/*     */ 
/* 246 */       if (state.isNil()) {
/* 247 */         e.setNil(true);
/* 248 */         state.setNil(false);
/*     */       }
/*     */ 
/* 251 */       if (o != null)
/*     */       {
/* 254 */         e.setValue(o);
/*     */       }
/* 256 */       fireAfterUnmarshal(ElementBeanInfoImpl.this, e, state);
/*     */ 
/* 258 */       return e;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.ElementBeanInfoImpl
 * JD-Core Version:    0.6.2
 */