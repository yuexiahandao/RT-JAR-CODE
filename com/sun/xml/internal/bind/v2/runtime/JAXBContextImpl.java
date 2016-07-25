/*      */ package com.sun.xml.internal.bind.v2.runtime;
/*      */ 
/*      */ import com.sun.istack.internal.NotNull;
/*      */ import com.sun.istack.internal.Pool;
/*      */ import com.sun.istack.internal.Pool.Impl;
/*      */ import com.sun.xml.internal.bind.api.AccessorException;
/*      */ import com.sun.xml.internal.bind.api.Bridge;
/*      */ import com.sun.xml.internal.bind.api.BridgeContext;
/*      */ import com.sun.xml.internal.bind.api.CompositeStructure;
/*      */ import com.sun.xml.internal.bind.api.ErrorListener;
/*      */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*      */ import com.sun.xml.internal.bind.api.RawAccessor;
/*      */ import com.sun.xml.internal.bind.api.TypeReference;
/*      */ import com.sun.xml.internal.bind.unmarshaller.DOMScanner;
/*      */ import com.sun.xml.internal.bind.util.Which;
/*      */ import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
/*      */ import com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
/*      */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*      */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*      */ import com.sun.xml.internal.bind.v2.model.core.Ref;
/*      */ import com.sun.xml.internal.bind.v2.model.impl.RuntimeBuiltinLeafInfoImpl;
/*      */ import com.sun.xml.internal.bind.v2.model.impl.RuntimeModelBuilder;
/*      */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeArrayInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeBuiltinLeafInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeElementInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeEnumLeafInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeLeafInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet;
/*      */ import com.sun.xml.internal.bind.v2.runtime.output.Encoded;
/*      */ import com.sun.xml.internal.bind.v2.runtime.property.AttributeProperty;
/*      */ import com.sun.xml.internal.bind.v2.runtime.property.Property;
/*      */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*      */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*      */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*      */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
/*      */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*      */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext.State;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator;
/*      */ import com.sun.xml.internal.bind.v2.util.EditDistance;
/*      */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*      */ import com.sun.xml.internal.bind.v2.util.QNameMap.Entry;
/*      */ import com.sun.xml.internal.txw2.output.ResultFactory;
/*      */ import java.io.IOException;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import javax.xml.bind.Binder;
/*      */ import javax.xml.bind.JAXBElement;
/*      */ import javax.xml.bind.JAXBException;
/*      */ import javax.xml.bind.JAXBIntrospector;
/*      */ import javax.xml.bind.Marshaller;
/*      */ import javax.xml.bind.SchemaOutputResolver;
/*      */ import javax.xml.bind.Unmarshaller;
/*      */ import javax.xml.bind.Validator;
/*      */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*      */ import javax.xml.bind.annotation.XmlList;
/*      */ import javax.xml.bind.annotation.XmlNs;
/*      */ import javax.xml.bind.annotation.XmlSchema;
/*      */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.FactoryConfigurationError;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.transform.Result;
/*      */ import javax.xml.transform.Transformer;
/*      */ import javax.xml.transform.TransformerConfigurationException;
/*      */ import javax.xml.transform.TransformerFactory;
/*      */ import javax.xml.transform.sax.SAXResult;
/*      */ import javax.xml.transform.sax.SAXTransformerFactory;
/*      */ import javax.xml.transform.sax.TransformerHandler;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */ 
/*      */ public final class JAXBContextImpl extends JAXBRIContext
/*      */ {
/*  130 */   private final Map<TypeReference, Bridge> bridges = new LinkedHashMap();
/*      */   private static DocumentBuilder db;
/*  138 */   private final QNameMap<JaxBeanInfo> rootMap = new QNameMap();
/*  139 */   private final HashMap<QName, JaxBeanInfo> typeMap = new HashMap();
/*      */ 
/*  144 */   private final Map<Class, JaxBeanInfo> beanInfoMap = new LinkedHashMap();
/*      */ 
/*  155 */   protected Map<RuntimeTypeInfo, JaxBeanInfo> beanInfos = new LinkedHashMap();
/*      */ 
/*  157 */   private final Map<Class, Map<QName, ElementBeanInfoImpl>> elements = new LinkedHashMap();
/*      */ 
/*  162 */   public final Pool<Marshaller> marshallerPool = new Pool.Impl() {
/*  164 */     @NotNull
/*      */     protected Marshaller create() { return JAXBContextImpl.this.createMarshaller(); }
/*      */ 
/*  162 */   };
/*      */ 
/*  168 */   public final Pool<Unmarshaller> unmarshallerPool = new Pool.Impl() {
/*  170 */     @NotNull
/*      */     protected Unmarshaller create() { return JAXBContextImpl.this.createUnmarshaller(); }
/*      */ 
/*  168 */   };
/*      */ 
/*  178 */   public NameBuilder nameBuilder = new NameBuilder();
/*      */   public final NameList nameList;
/*      */   private final String defaultNsUri;
/*      */   private final Class[] classes;
/*      */   protected final boolean c14nSupport;
/*      */   public final boolean xmlAccessorFactorySupport;
/*      */   public final boolean allNillable;
/*      */   public final boolean retainPropertyInfo;
/*      */   public final boolean supressAccessorWarnings;
/*      */   public final boolean improvedXsiTypeHandling;
/*      */   private WeakReference<RuntimeTypeInfoSet> typeInfoSetCache;
/*      */ 
/*      */   @NotNull
/*      */   private RuntimeAnnotationReader annotationReader;
/*      */   private boolean hasSwaRef;
/*      */ 
/*      */   @NotNull
/*      */   private final Map<Class, Class> subclassReplacements;
/*      */   public final boolean fastBoot;
/*  238 */   private Set<XmlNs> xmlNsSet = null;
/*      */   private Encoded[] utf8nameTable;
/* 1015 */   private static final Comparator<QName> QNAME_COMPARATOR = new Comparator() {
/*      */     public int compare(QName lhs, QName rhs) {
/* 1017 */       int r = lhs.getLocalPart().compareTo(rhs.getLocalPart());
/* 1018 */       if (r != 0) return r;
/*      */ 
/* 1020 */       return lhs.getNamespaceURI().compareTo(rhs.getNamespaceURI());
/*      */     }
/* 1015 */   };
/*      */ 
/*      */   public Set<XmlNs> getXmlNsSet()
/*      */   {
/*  246 */     return this.xmlNsSet; } 
/*  251 */   private JAXBContextImpl(JAXBContextBuilder builder) throws JAXBException { this.defaultNsUri = builder.defaultNsUri;
/*  252 */     this.retainPropertyInfo = builder.retainPropertyInfo;
/*  253 */     this.annotationReader = builder.annotationReader;
/*  254 */     this.subclassReplacements = builder.subclassReplacements;
/*  255 */     this.c14nSupport = builder.c14nSupport;
/*  256 */     this.classes = builder.classes;
/*  257 */     this.xmlAccessorFactorySupport = builder.xmlAccessorFactorySupport;
/*  258 */     this.allNillable = builder.allNillable;
/*  259 */     this.supressAccessorWarnings = builder.supressAccessorWarnings;
/*  260 */     this.improvedXsiTypeHandling = builder.improvedXsiTypeHandling;
/*      */ 
/*  262 */     Collection typeRefs = builder.typeRefs;
/*      */     boolean fastB;
/*      */     try { fastB = Boolean.getBoolean(JAXBContextImpl.class.getName() + ".fastBoot");
/*      */     } catch (SecurityException e) {
/*  268 */       fastB = false;
/*      */     }
/*  270 */     this.fastBoot = fastB;
/*      */ 
/*  272 */     System.arraycopy(this.classes, 0, this.classes, 0, this.classes.length);
/*      */ 
/*  274 */     RuntimeTypeInfoSet typeSet = getTypeInfoSet();
/*      */ 
/*  277 */     this.elements.put(null, new LinkedHashMap());
/*      */ 
/*  280 */     for (RuntimeBuiltinLeafInfo leaf : RuntimeBuiltinLeafInfoImpl.builtinBeanInfos) {
/*  281 */       bi = new LeafBeanInfoImpl(this, leaf);
/*  282 */       this.beanInfoMap.put(leaf.getClazz(), bi);
/*  283 */       for (QName t : bi.getTypeNames())
/*  284 */         this.typeMap.put(t, bi);
/*      */     }
/*      */     LeafBeanInfoImpl bi;
/*  287 */     for (RuntimeEnumLeafInfo e : typeSet.enums().values()) {
/*  288 */       JaxBeanInfo bi = getOrCreate(e);
/*  289 */       for (QName qn : bi.getTypeNames())
/*  290 */         this.typeMap.put(qn, bi);
/*  291 */       if (e.isElement()) {
/*  292 */         this.rootMap.put(e.getElementName(), bi);
/*      */       }
/*      */     }
/*  295 */     for (RuntimeArrayInfo a : typeSet.arrays().values()) {
/*  296 */       ai = getOrCreate(a);
/*  297 */       for (QName qn : ai.getTypeNames())
/*  298 */         this.typeMap.put(qn, ai);
/*      */     }
/*      */     JaxBeanInfo ai;
/*  301 */     for (Map.Entry e : typeSet.beans().entrySet()) {
/*  302 */       bi = getOrCreate((RuntimeClassInfo)e.getValue());
/*      */ 
/*  304 */       XmlSchema xs = (XmlSchema)this.annotationReader.getPackageAnnotation(XmlSchema.class, e.getKey(), null);
/*  305 */       if ((xs != null) && 
/*  306 */         (xs.xmlns() != null) && (xs.xmlns().length > 0)) {
/*  307 */         if (this.xmlNsSet == null)
/*  308 */           this.xmlNsSet = new HashSet();
/*  309 */         this.xmlNsSet.addAll(Arrays.asList(xs.xmlns()));
/*      */       }
/*      */ 
/*  313 */       if (bi.isElement()) {
/*  314 */         this.rootMap.put(((RuntimeClassInfo)e.getValue()).getElementName(), bi);
/*      */       }
/*  316 */       for (QName qn : bi.getTypeNames())
/*  317 */         this.typeMap.put(qn, bi);
/*      */     }
/*      */     ClassBeanInfoImpl bi;
/*  321 */     for (RuntimeElementInfo n : typeSet.getAllElements()) {
/*  322 */       ElementBeanInfoImpl bi = getOrCreate(n);
/*  323 */       if (n.getScope() == null) {
/*  324 */         this.rootMap.put(n.getElementName(), bi);
/*      */       }
/*  326 */       RuntimeClassInfo scope = n.getScope();
/*  327 */       Class scopeClazz = scope == null ? null : (Class)scope.getClazz();
/*  328 */       Map m = (Map)this.elements.get(scopeClazz);
/*  329 */       if (m == null) {
/*  330 */         m = new LinkedHashMap();
/*  331 */         this.elements.put(scopeClazz, m);
/*      */       }
/*  333 */       m.put(n.getElementName(), bi);
/*      */     }
/*      */ 
/*  337 */     this.beanInfoMap.put(JAXBElement.class, new ElementBeanInfoImpl(this));
/*      */ 
/*  339 */     this.beanInfoMap.put(CompositeStructure.class, new CompositeStructureBeanInfo(this));
/*      */ 
/*  341 */     getOrCreate(typeSet.getAnyTypeInfo());
/*      */ 
/*  344 */     for (JaxBeanInfo bi : this.beanInfos.values()) {
/*  345 */       bi.link(this);
/*      */     }
/*      */ 
/*  348 */     for (Map.Entry e : RuntimeUtil.primitiveToBox.entrySet()) {
/*  349 */       this.beanInfoMap.put(e.getKey(), this.beanInfoMap.get(e.getValue()));
/*      */     }
/*      */ 
/*  352 */     Navigator nav = typeSet.getNavigator();
/*      */ 
/*  354 */     for (TypeReference tr : typeRefs) {
/*  355 */       XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter)tr.get(XmlJavaTypeAdapter.class);
/*  356 */       Adapter a = null;
/*  357 */       XmlList xl = (XmlList)tr.get(XmlList.class);
/*      */ 
/*  360 */       Class erasedType = (Class)nav.erasure(tr.type);
/*      */ 
/*  362 */       if (xjta != null) {
/*  363 */         a = new Adapter(xjta.value(), nav);
/*      */       }
/*  365 */       if (tr.get(XmlAttachmentRef.class) != null) {
/*  366 */         a = new Adapter(SwaRefAdapter.class, nav);
/*  367 */         this.hasSwaRef = true;
/*      */       }
/*      */ 
/*  370 */       if (a != null) {
/*  371 */         erasedType = (Class)nav.erasure(a.defaultType);
/*      */       }
/*      */ 
/*  374 */       Name name = this.nameBuilder.createElementName(tr.tagName);
/*      */       InternalBridge bridge;
/*      */       InternalBridge bridge;
/*  377 */       if (xl == null)
/*  378 */         bridge = new BridgeImpl(this, name, getBeanInfo(erasedType, true), tr);
/*      */       else {
/*  380 */         bridge = new BridgeImpl(this, name, new ValueListBeanInfoImpl(this, erasedType), tr);
/*      */       }
/*  382 */       if (a != null) {
/*  383 */         bridge = new BridgeAdapter(bridge, (Class)a.adapterType);
/*      */       }
/*  385 */       this.bridges.put(tr, bridge);
/*      */     }
/*      */ 
/*  388 */     this.nameList = this.nameBuilder.conclude();
/*      */ 
/*  390 */     for (JaxBeanInfo bi : this.beanInfos.values()) {
/*  391 */       bi.wrapUp();
/*      */     }
/*      */ 
/*  394 */     this.nameBuilder = null;
/*  395 */     this.beanInfos = null;
/*      */   }
/*      */ 
/*      */   public boolean hasSwaRef()
/*      */   {
/*  402 */     return this.hasSwaRef;
/*      */   }
/*      */ 
/*      */   public RuntimeTypeInfoSet getRuntimeTypeInfoSet() {
/*      */     try {
/*  407 */       return getTypeInfoSet();
/*      */     }
/*      */     catch (IllegalAnnotationsException e) {
/*  410 */       throw new AssertionError(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public RuntimeTypeInfoSet getTypeInfoSet()
/*      */     throws IllegalAnnotationsException
/*      */   {
/*  420 */     if (this.typeInfoSetCache != null) {
/*  421 */       RuntimeTypeInfoSet r = (RuntimeTypeInfoSet)this.typeInfoSetCache.get();
/*  422 */       if (r != null) {
/*  423 */         return r;
/*      */       }
/*      */     }
/*  426 */     RuntimeModelBuilder builder = new RuntimeModelBuilder(this, this.annotationReader, this.subclassReplacements, this.defaultNsUri);
/*      */ 
/*  428 */     IllegalAnnotationsException.Builder errorHandler = new IllegalAnnotationsException.Builder();
/*  429 */     builder.setErrorHandler(errorHandler);
/*      */ 
/*  431 */     for (Class c : this.classes) {
/*  432 */       if (c != CompositeStructure.class)
/*      */       {
/*  436 */         builder.getTypeInfo(new Ref(c));
/*      */       }
/*      */     }
/*  439 */     this.hasSwaRef |= builder.hasSwaRef;
/*  440 */     RuntimeTypeInfoSet r = builder.link();
/*      */ 
/*  442 */     errorHandler.check();
/*  443 */     assert (r != null) : "if no error was reported, the link must be a success";
/*      */ 
/*  445 */     this.typeInfoSetCache = new WeakReference(r);
/*      */ 
/*  447 */     return r;
/*      */   }
/*      */ 
/*      */   public ElementBeanInfoImpl getElement(Class scope, QName name)
/*      */   {
/*  452 */     Map m = (Map)this.elements.get(scope);
/*  453 */     if (m != null) {
/*  454 */       ElementBeanInfoImpl bi = (ElementBeanInfoImpl)m.get(name);
/*  455 */       if (bi != null)
/*  456 */         return bi;
/*      */     }
/*  458 */     m = (Map)this.elements.get(null);
/*  459 */     return (ElementBeanInfoImpl)m.get(name);
/*      */   }
/*      */ 
/*      */   private ElementBeanInfoImpl getOrCreate(RuntimeElementInfo rei)
/*      */   {
/*  467 */     JaxBeanInfo bi = (JaxBeanInfo)this.beanInfos.get(rei);
/*  468 */     if (bi != null) return (ElementBeanInfoImpl)bi;
/*      */ 
/*  471 */     return new ElementBeanInfoImpl(this, rei);
/*      */   }
/*      */ 
/*      */   protected JaxBeanInfo getOrCreate(RuntimeEnumLeafInfo eli) {
/*  475 */     JaxBeanInfo bi = (JaxBeanInfo)this.beanInfos.get(eli);
/*  476 */     if (bi != null) return bi;
/*  477 */     bi = new LeafBeanInfoImpl(this, eli);
/*  478 */     this.beanInfoMap.put(bi.jaxbType, bi);
/*  479 */     return bi;
/*      */   }
/*      */ 
/*      */   protected ClassBeanInfoImpl getOrCreate(RuntimeClassInfo ci) {
/*  483 */     ClassBeanInfoImpl bi = (ClassBeanInfoImpl)this.beanInfos.get(ci);
/*  484 */     if (bi != null) return bi;
/*  485 */     bi = new ClassBeanInfoImpl(this, ci);
/*  486 */     this.beanInfoMap.put(bi.jaxbType, bi);
/*  487 */     return bi;
/*      */   }
/*      */ 
/*      */   protected JaxBeanInfo getOrCreate(RuntimeArrayInfo ai) {
/*  491 */     JaxBeanInfo abi = (JaxBeanInfo)this.beanInfos.get(ai);
/*  492 */     if (abi != null) return abi;
/*      */ 
/*  494 */     abi = new ArrayBeanInfoImpl(this, ai);
/*      */ 
/*  496 */     this.beanInfoMap.put(ai.getType(), abi);
/*  497 */     return abi;
/*      */   }
/*      */ 
/*      */   public JaxBeanInfo getOrCreate(RuntimeTypeInfo e) {
/*  501 */     if ((e instanceof RuntimeElementInfo))
/*  502 */       return getOrCreate((RuntimeElementInfo)e);
/*  503 */     if ((e instanceof RuntimeClassInfo))
/*  504 */       return getOrCreate((RuntimeClassInfo)e);
/*  505 */     if ((e instanceof RuntimeLeafInfo)) {
/*  506 */       JaxBeanInfo bi = (JaxBeanInfo)this.beanInfos.get(e);
/*  507 */       assert (bi != null);
/*  508 */       return bi;
/*      */     }
/*  510 */     if ((e instanceof RuntimeArrayInfo))
/*  511 */       return getOrCreate((RuntimeArrayInfo)e);
/*  512 */     if (e.getType() == Object.class)
/*      */     {
/*  514 */       JaxBeanInfo bi = (JaxBeanInfo)this.beanInfoMap.get(Object.class);
/*  515 */       if (bi == null) {
/*  516 */         bi = new AnyTypeBeanInfo(this, e);
/*  517 */         this.beanInfoMap.put(Object.class, bi);
/*      */       }
/*  519 */       return bi;
/*      */     }
/*      */ 
/*  522 */     throw new IllegalArgumentException();
/*      */   }
/*      */ 
/*      */   public final JaxBeanInfo getBeanInfo(Object o)
/*      */   {
/*  537 */     for (Class c = o.getClass(); c != Object.class; c = c.getSuperclass()) {
/*  538 */       JaxBeanInfo bi = (JaxBeanInfo)this.beanInfoMap.get(c);
/*  539 */       if (bi != null) return bi;
/*      */     }
/*  541 */     if ((o instanceof org.w3c.dom.Element))
/*  542 */       return (JaxBeanInfo)this.beanInfoMap.get(Object.class);
/*  543 */     for (Class c : o.getClass().getInterfaces()) {
/*  544 */       JaxBeanInfo bi = (JaxBeanInfo)this.beanInfoMap.get(c);
/*  545 */       if (bi != null) return bi;
/*      */     }
/*  547 */     return null;
/*      */   }
/*      */ 
/*      */   public final JaxBeanInfo getBeanInfo(Object o, boolean fatal)
/*      */     throws JAXBException
/*      */   {
/*  559 */     JaxBeanInfo bi = getBeanInfo(o);
/*  560 */     if (bi != null) return bi;
/*  561 */     if (fatal) {
/*  562 */       if ((o instanceof Document))
/*  563 */         throw new JAXBException(Messages.ELEMENT_NEEDED_BUT_FOUND_DOCUMENT.format(new Object[] { o.getClass() }));
/*  564 */       throw new JAXBException(Messages.UNKNOWN_CLASS.format(new Object[] { o.getClass() }));
/*      */     }
/*  566 */     return null;
/*      */   }
/*      */ 
/*      */   public final <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz)
/*      */   {
/*  580 */     return (JaxBeanInfo)this.beanInfoMap.get(clazz);
/*      */   }
/*      */ 
/*      */   public final <T> JaxBeanInfo<T> getBeanInfo(Class<T> clazz, boolean fatal)
/*      */     throws JAXBException
/*      */   {
/*  592 */     JaxBeanInfo bi = getBeanInfo(clazz);
/*  593 */     if (bi != null) return bi;
/*  594 */     if (fatal)
/*  595 */       throw new JAXBException(clazz.getName() + " is not known to this context");
/*  596 */     return null;
/*      */   }
/*      */ 
/*      */   public final Loader selectRootLoader(UnmarshallingContext.State state, TagName tag)
/*      */   {
/*  607 */     JaxBeanInfo beanInfo = (JaxBeanInfo)this.rootMap.get(tag.uri, tag.local);
/*  608 */     if (beanInfo == null) {
/*  609 */       return null;
/*      */     }
/*  611 */     return beanInfo.getLoader(this, true);
/*      */   }
/*      */ 
/*      */   public JaxBeanInfo getGlobalType(QName name)
/*      */   {
/*  623 */     return (JaxBeanInfo)this.typeMap.get(name);
/*      */   }
/*      */ 
/*      */   public String getNearestTypeName(QName name)
/*      */   {
/*  634 */     String[] all = new String[this.typeMap.size()];
/*  635 */     int i = 0;
/*  636 */     for (QName qn : this.typeMap.keySet()) {
/*  637 */       if (qn.getLocalPart().equals(name.getLocalPart()))
/*  638 */         return qn.toString();
/*  639 */       all[(i++)] = qn.toString();
/*      */     }
/*      */ 
/*  642 */     String nearest = EditDistance.findNearest(name.toString(), all);
/*      */ 
/*  644 */     if (EditDistance.editDistance(nearest, name.toString()) > 10) {
/*  645 */       return null;
/*      */     }
/*  647 */     return nearest;
/*      */   }
/*      */ 
/*      */   public Set<QName> getValidRootNames()
/*      */   {
/*  655 */     Set r = new TreeSet(QNAME_COMPARATOR);
/*  656 */     for (QNameMap.Entry e : this.rootMap.entrySet()) {
/*  657 */       r.add(e.createQName());
/*      */     }
/*  659 */     return r;
/*      */   }
/*      */ 
/*      */   public synchronized Encoded[] getUTF8NameTable()
/*      */   {
/*  668 */     if (this.utf8nameTable == null) {
/*  669 */       Encoded[] x = new Encoded[this.nameList.localNames.length];
/*  670 */       for (int i = 0; i < x.length; i++) {
/*  671 */         Encoded e = new Encoded(this.nameList.localNames[i]);
/*  672 */         e.compact();
/*  673 */         x[i] = e;
/*      */       }
/*  675 */       this.utf8nameTable = x;
/*      */     }
/*  677 */     return this.utf8nameTable;
/*      */   }
/*      */ 
/*      */   public int getNumberOfLocalNames() {
/*  681 */     return this.nameList.localNames.length;
/*      */   }
/*      */ 
/*      */   public int getNumberOfElementNames() {
/*  685 */     return this.nameList.numberOfElementNames;
/*      */   }
/*      */ 
/*      */   public int getNumberOfAttributeNames() {
/*  689 */     return this.nameList.numberOfAttributeNames;
/*      */   }
/*      */ 
/*      */   static Transformer createTransformer()
/*      */   {
/*      */     try
/*      */     {
/*  697 */       SAXTransformerFactory tf = (SAXTransformerFactory)TransformerFactory.newInstance();
/*  698 */       return tf.newTransformer();
/*      */     } catch (TransformerConfigurationException e) {
/*  700 */       throw new Error(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static TransformerHandler createTransformerHandler()
/*      */   {
/*      */     try
/*      */     {
/*  709 */       SAXTransformerFactory tf = (SAXTransformerFactory)TransformerFactory.newInstance();
/*  710 */       return tf.newTransformerHandler();
/*      */     } catch (TransformerConfigurationException e) {
/*  712 */       throw new Error(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   static Document createDom()
/*      */   {
/*  720 */     synchronized (JAXBContextImpl.class) {
/*  721 */       if (db == null) {
/*      */         try {
/*  723 */           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  724 */           dbf.setNamespaceAware(true);
/*  725 */           db = dbf.newDocumentBuilder();
/*      */         }
/*      */         catch (ParserConfigurationException e) {
/*  728 */           throw new FactoryConfigurationError(e);
/*      */         }
/*      */       }
/*  731 */       return db.newDocument();
/*      */     }
/*      */   }
/*      */ 
/*      */   public MarshallerImpl createMarshaller() {
/*  736 */     return new MarshallerImpl(this, null);
/*      */   }
/*      */ 
/*      */   public UnmarshallerImpl createUnmarshaller() {
/*  740 */     return new UnmarshallerImpl(this, null);
/*      */   }
/*      */ 
/*      */   public Validator createValidator() {
/*  744 */     throw new UnsupportedOperationException(Messages.NOT_IMPLEMENTED_IN_2_0.format(new Object[0]));
/*      */   }
/*      */ 
/*      */   public JAXBIntrospector createJAXBIntrospector()
/*      */   {
/*  749 */     return new JAXBIntrospector() {
/*      */       public boolean isElement(Object object) {
/*  751 */         return getElementName(object) != null;
/*      */       }
/*      */ 
/*      */       public QName getElementName(Object jaxbElement) {
/*      */         try {
/*  756 */           return JAXBContextImpl.this.getElementName(jaxbElement); } catch (JAXBException e) {
/*      */         }
/*  758 */         return null;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private NonElement<Type, Class> getXmlType(RuntimeTypeInfoSet tis, TypeReference tr)
/*      */   {
/*  765 */     if (tr == null) {
/*  766 */       throw new IllegalArgumentException();
/*      */     }
/*  768 */     XmlJavaTypeAdapter xjta = (XmlJavaTypeAdapter)tr.get(XmlJavaTypeAdapter.class);
/*  769 */     XmlList xl = (XmlList)tr.get(XmlList.class);
/*      */ 
/*  771 */     Ref ref = new Ref(this.annotationReader, tis.getNavigator(), tr.type, xjta, xl);
/*      */ 
/*  773 */     return tis.getTypeInfo(ref);
/*      */   }
/*      */ 
/*      */   public void generateEpisode(Result output)
/*      */   {
/*  778 */     if (output == null)
/*  779 */       throw new IllegalArgumentException();
/*  780 */     createSchemaGenerator().writeEpisodeFile(ResultFactory.createSerializer(output));
/*      */   }
/*      */ 
/*      */   public void generateSchema(SchemaOutputResolver outputResolver)
/*      */     throws IOException
/*      */   {
/*  786 */     if (outputResolver == null) {
/*  787 */       throw new IOException(Messages.NULL_OUTPUT_RESOLVER.format(new Object[0]));
/*      */     }
/*  789 */     final SAXParseException[] e = new SAXParseException[1];
/*  790 */     final SAXParseException[] w = new SAXParseException[1];
/*      */ 
/*  792 */     createSchemaGenerator().write(outputResolver, new ErrorListener() {
/*      */       public void error(SAXParseException exception) {
/*  794 */         e[0] = exception;
/*      */       }
/*      */ 
/*      */       public void fatalError(SAXParseException exception) {
/*  798 */         e[0] = exception;
/*      */       }
/*      */ 
/*      */       public void warning(SAXParseException exception) {
/*  802 */         w[0] = exception;
/*      */       }
/*      */ 
/*      */       public void info(SAXParseException exception)
/*      */       {
/*      */       }
/*      */     });
/*  808 */     if (e[0] != null) {
/*  809 */       IOException x = new IOException(Messages.FAILED_TO_GENERATE_SCHEMA.format(new Object[0]));
/*  810 */       x.initCause(e[0]);
/*  811 */       throw x;
/*      */     }
/*  813 */     if (w[0] != null) {
/*  814 */       IOException x = new IOException(Messages.ERROR_PROCESSING_SCHEMA.format(new Object[0]));
/*  815 */       x.initCause(w[0]);
/*  816 */       throw x;
/*      */     }
/*      */   }
/*      */ 
/*      */   private XmlSchemaGenerator<Type, Class, Field, Method> createSchemaGenerator() {
/*      */     RuntimeTypeInfoSet tis;
/*      */     try {
/*  823 */       tis = getTypeInfoSet();
/*      */     }
/*      */     catch (IllegalAnnotationsException e) {
/*  826 */       throw new AssertionError(e);
/*      */     }
/*      */ 
/*  829 */     XmlSchemaGenerator xsdgen = new XmlSchemaGenerator(tis.getNavigator(), tis);
/*      */ 
/*  835 */     Set rootTagNames = new HashSet();
/*  836 */     for (RuntimeElementInfo ei : tis.getAllElements()) {
/*  837 */       rootTagNames.add(ei.getElementName());
/*      */     }
/*  839 */     for (RuntimeClassInfo ci : tis.beans().values()) {
/*  840 */       if (ci.isElement()) {
/*  841 */         rootTagNames.add(ci.asElement().getElementName());
/*      */       }
/*      */     }
/*  844 */     for (TypeReference tr : this.bridges.keySet())
/*  845 */       if (!rootTagNames.contains(tr.tagName))
/*      */       {
/*  848 */         if ((tr.type == Void.TYPE) || (tr.type == Void.class)) {
/*  849 */           xsdgen.add(tr.tagName, false, null);
/*      */         }
/*  851 */         else if (tr.type != CompositeStructure.class)
/*      */         {
/*  854 */           NonElement typeInfo = getXmlType(tis, tr);
/*  855 */           xsdgen.add(tr.tagName, !tis.getNavigator().isPrimitive(tr.type), typeInfo);
/*      */         }
/*      */       }
/*  858 */     return xsdgen;
/*      */   }
/*      */ 
/*      */   public QName getTypeName(TypeReference tr) {
/*      */     try {
/*  863 */       NonElement xt = getXmlType(getTypeInfoSet(), tr);
/*  864 */       if (xt == null) throw new IllegalArgumentException();
/*  865 */       return xt.getTypeName();
/*      */     }
/*      */     catch (IllegalAnnotationsException e) {
/*  868 */       throw new AssertionError(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public SchemaOutputResolver createTestResolver()
/*      */   {
/*  876 */     return new SchemaOutputResolver() {
/*      */       public Result createOutput(String namespaceUri, String suggestedFileName) {
/*  878 */         SAXResult r = new SAXResult(new DefaultHandler());
/*  879 */         r.setSystemId(suggestedFileName);
/*  880 */         return r;
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public <T> Binder<T> createBinder(Class<T> domType)
/*      */   {
/*  887 */     if (domType == Node.class) {
/*  888 */       return createBinder();
/*      */     }
/*  890 */     return super.createBinder(domType);
/*      */   }
/*      */ 
/*      */   public Binder<Node> createBinder()
/*      */   {
/*  895 */     return new BinderImpl(this, new DOMScanner());
/*      */   }
/*      */ 
/*      */   public QName getElementName(Object o) throws JAXBException {
/*  899 */     JaxBeanInfo bi = getBeanInfo(o, true);
/*  900 */     if (!bi.isElement())
/*  901 */       return null;
/*  902 */     return new QName(bi.getElementNamespaceURI(o), bi.getElementLocalName(o));
/*      */   }
/*      */ 
/*      */   public QName getElementName(Class o) throws JAXBException {
/*  906 */     JaxBeanInfo bi = getBeanInfo(o, true);
/*  907 */     if (!bi.isElement())
/*  908 */       return null;
/*  909 */     return new QName(bi.getElementNamespaceURI(o), bi.getElementLocalName(o));
/*      */   }
/*      */ 
/*      */   public Bridge createBridge(TypeReference ref) {
/*  913 */     return (Bridge)this.bridges.get(ref);
/*      */   }
/*      */   @NotNull
/*      */   public BridgeContext createBridgeContext() {
/*  917 */     return new BridgeContextImpl(this);
/*      */   }
/*      */ 
/*      */   public RawAccessor getElementPropertyAccessor(Class wrapperBean, String nsUri, String localName) throws JAXBException {
/*  921 */     JaxBeanInfo bi = getBeanInfo(wrapperBean, true);
/*  922 */     if (!(bi instanceof ClassBeanInfoImpl)) {
/*  923 */       throw new JAXBException(wrapperBean + " is not a bean");
/*      */     }
/*  925 */     for (ClassBeanInfoImpl cb = (ClassBeanInfoImpl)bi; cb != null; cb = cb.superClazz) {
/*  926 */       for (Property p : cb.properties) {
/*  927 */         final Accessor acc = p.getElementPropertyAccessor(nsUri, localName);
/*  928 */         if (acc != null)
/*  929 */           return new RawAccessor()
/*      */           {
/*      */             public Object get(Object bean)
/*      */               throws AccessorException
/*      */             {
/*  936 */               return acc.getUnadapted(bean);
/*      */             }
/*      */ 
/*      */             public void set(Object bean, Object value) throws AccessorException {
/*  940 */               acc.setUnadapted(bean, value);
/*      */             }
/*      */           };
/*      */       }
/*      */     }
/*  945 */     throw new JAXBException(new QName(nsUri, localName) + " is not a valid property on " + wrapperBean);
/*      */   }
/*      */ 
/*      */   public List<String> getKnownNamespaceURIs() {
/*  949 */     return Arrays.asList(this.nameList.namespaceURIs);
/*      */   }
/*      */ 
/*      */   public String getBuildId() {
/*  953 */     Package pkg = getClass().getPackage();
/*  954 */     if (pkg == null) return null;
/*  955 */     return pkg.getImplementationVersion();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  960 */     StringBuilder buf = new StringBuilder(Which.which(getClass()) + " Build-Id: " + getBuildId());
/*  961 */     buf.append("\nClasses known to this context:\n");
/*      */ 
/*  963 */     Set names = new TreeSet();
/*      */ 
/*  965 */     for (Class key : this.beanInfoMap.keySet()) {
/*  966 */       names.add(key.getName());
/*      */     }
/*  968 */     for (String name : names) {
/*  969 */       buf.append("  ").append(name).append('\n');
/*      */     }
/*  971 */     return buf.toString();
/*      */   }
/*      */ 
/*      */   public String getXMIMEContentType(Object o)
/*      */   {
/*  979 */     JaxBeanInfo bi = getBeanInfo(o);
/*  980 */     if (!(bi instanceof ClassBeanInfoImpl)) {
/*  981 */       return null;
/*      */     }
/*  983 */     ClassBeanInfoImpl cb = (ClassBeanInfoImpl)bi;
/*  984 */     for (Property p : cb.properties) {
/*  985 */       if ((p instanceof AttributeProperty)) {
/*  986 */         AttributeProperty ap = (AttributeProperty)p;
/*  987 */         if (ap.attName.equals("http://www.w3.org/2005/05/xmlmime", "contentType"))
/*      */           try {
/*  989 */             return (String)ap.xacc.print(o);
/*      */           } catch (AccessorException e) {
/*  991 */             return null;
/*      */           } catch (SAXException e) {
/*  993 */             return null;
/*      */           } catch (ClassCastException e) {
/*  995 */             return null;
/*      */           }
/*      */       }
/*      */     }
/*  999 */     return null;
/*      */   }
/*      */ 
/*      */   public JAXBContextImpl createAugmented(Class<?> clazz)
/*      */     throws JAXBException
/*      */   {
/* 1006 */     Class[] newList = new Class[this.classes.length + 1];
/* 1007 */     System.arraycopy(this.classes, 0, newList, 0, this.classes.length);
/* 1008 */     newList[this.classes.length] = clazz;
/*      */ 
/* 1010 */     JAXBContextBuilder builder = new JAXBContextBuilder(this);
/* 1011 */     builder.setClasses(newList);
/* 1012 */     return builder.build();
/*      */   }
/*      */ 
/*      */   public static class JAXBContextBuilder
/*      */   {
/* 1026 */     private boolean retainPropertyInfo = false;
/* 1027 */     private boolean supressAccessorWarnings = false;
/* 1028 */     private String defaultNsUri = "";
/*      */ 
/*      */     @NotNull
/* 1029 */     private RuntimeAnnotationReader annotationReader = new RuntimeInlineAnnotationReader();
/*      */ 
/*      */     @NotNull
/* 1030 */     private Map<Class, Class> subclassReplacements = Collections.emptyMap();
/* 1031 */     private boolean c14nSupport = false;
/*      */     private Class[] classes;
/*      */     private Collection<TypeReference> typeRefs;
/* 1034 */     private boolean xmlAccessorFactorySupport = false;
/*      */     private boolean allNillable;
/* 1036 */     private boolean improvedXsiTypeHandling = true;
/*      */ 
/*      */     public JAXBContextBuilder() {
/*      */     }
/*      */     public JAXBContextBuilder(JAXBContextImpl baseImpl) {
/* 1041 */       this.supressAccessorWarnings = baseImpl.supressAccessorWarnings;
/* 1042 */       this.retainPropertyInfo = baseImpl.retainPropertyInfo;
/* 1043 */       this.defaultNsUri = baseImpl.defaultNsUri;
/* 1044 */       this.annotationReader = baseImpl.annotationReader;
/* 1045 */       this.subclassReplacements = baseImpl.subclassReplacements;
/* 1046 */       this.c14nSupport = baseImpl.c14nSupport;
/* 1047 */       this.classes = baseImpl.classes;
/* 1048 */       this.typeRefs = baseImpl.bridges.keySet();
/* 1049 */       this.xmlAccessorFactorySupport = baseImpl.xmlAccessorFactorySupport;
/* 1050 */       this.allNillable = baseImpl.allNillable;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setRetainPropertyInfo(boolean val) {
/* 1054 */       this.retainPropertyInfo = val;
/* 1055 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setSupressAccessorWarnings(boolean val) {
/* 1059 */       this.supressAccessorWarnings = val;
/* 1060 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setC14NSupport(boolean val) {
/* 1064 */       this.c14nSupport = val;
/* 1065 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setXmlAccessorFactorySupport(boolean val) {
/* 1069 */       this.xmlAccessorFactorySupport = val;
/* 1070 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setDefaultNsUri(String val) {
/* 1074 */       this.defaultNsUri = val;
/* 1075 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setAllNillable(boolean val) {
/* 1079 */       this.allNillable = val;
/* 1080 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setClasses(Class[] val) {
/* 1084 */       this.classes = val;
/* 1085 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setAnnotationReader(RuntimeAnnotationReader val) {
/* 1089 */       this.annotationReader = val;
/* 1090 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setSubclassReplacements(Map<Class, Class> val) {
/* 1094 */       this.subclassReplacements = val;
/* 1095 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setTypeRefs(Collection<TypeReference> val) {
/* 1099 */       this.typeRefs = val;
/* 1100 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextBuilder setImprovedXsiTypeHandling(boolean val) {
/* 1104 */       this.improvedXsiTypeHandling = val;
/* 1105 */       return this;
/*      */     }
/*      */ 
/*      */     public JAXBContextImpl build()
/*      */       throws JAXBException
/*      */     {
/* 1111 */       if (this.defaultNsUri == null) {
/* 1112 */         this.defaultNsUri = "";
/*      */       }
/*      */ 
/* 1115 */       if (this.subclassReplacements == null) {
/* 1116 */         this.subclassReplacements = Collections.emptyMap();
/*      */       }
/*      */ 
/* 1119 */       if (this.annotationReader == null) {
/* 1120 */         this.annotationReader = new RuntimeInlineAnnotationReader();
/*      */       }
/*      */ 
/* 1123 */       if (this.typeRefs == null) {
/* 1124 */         this.typeRefs = Collections.emptyList();
/*      */       }
/*      */ 
/* 1127 */       return new JAXBContextImpl(this, null);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl
 * JD-Core Version:    0.6.2
 */