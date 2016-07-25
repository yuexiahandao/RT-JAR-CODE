/*      */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*      */ 
/*      */ import com.sun.istack.internal.NotNull;
/*      */ import com.sun.istack.internal.Nullable;
/*      */ import com.sun.istack.internal.SAXParseException2;
/*      */ import com.sun.xml.internal.bind.IDResolver;
/*      */ import com.sun.xml.internal.bind.api.AccessorException;
/*      */ import com.sun.xml.internal.bind.api.ClassResolver;
/*      */ import com.sun.xml.internal.bind.unmarshaller.InfosetScanner;
/*      */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*      */ import com.sun.xml.internal.bind.v2.runtime.AssociationMap;
/*      */ import com.sun.xml.internal.bind.v2.runtime.Coordinator;
/*      */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*      */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.Callable;
/*      */ import javax.xml.bind.JAXBElement;
/*      */ import javax.xml.bind.UnmarshalException;
/*      */ import javax.xml.bind.ValidationEvent;
/*      */ import javax.xml.bind.ValidationEventHandler;
/*      */ import javax.xml.bind.ValidationEventLocator;
/*      */ import javax.xml.bind.helpers.ValidationEventImpl;
/*      */ import javax.xml.namespace.NamespaceContext;
/*      */ import javax.xml.namespace.QName;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.helpers.LocatorImpl;
/*      */ 
/*      */ public final class UnmarshallingContext extends Coordinator
/*      */   implements NamespaceContext, ValidationEventHandler, ErrorHandler, XmlVisitor, XmlVisitor.TextPredictor
/*      */ {
/*      */   private final State root;
/*      */   private State current;
/*   97 */   private static final LocatorEx DUMMY_INSTANCE = new LocatorExWrapper(loc);
/*      */ 
/*      */   @NotNull
/*  100 */   private LocatorEx locator = DUMMY_INSTANCE;
/*      */   private Object result;
/*      */   private JaxBeanInfo expectedType;
/*      */   private IDResolver idResolver;
/*  129 */   private boolean isUnmarshalInProgress = true;
/*  130 */   private boolean aborted = false;
/*      */   public final UnmarshallerImpl parent;
/*      */   private final AssociationMap assoc;
/*      */   private boolean isInplaceMode;
/*      */   private InfosetScanner scanner;
/*      */   private Object currentElement;
/*      */   private NamespaceContext environmentNamespaceContext;
/*      */ 
/*      */   @Nullable
/*      */   public ClassResolver classResolver;
/*      */ 
/*      */   @Nullable
/*      */   public ClassLoader classLoader;
/*      */   private final Map<Class, Factory> factories;
/*      */   private Patcher[] patchers;
/*      */   private int patchersLen;
/*      */   private String[] nsBind;
/*      */   private int nsLen;
/*      */   private Scope[] scopes;
/*      */   private int scopeTop;
/* 1072 */   private static final Loader DEFAULT_ROOT_LOADER = new DefaultRootLoader(null);
/* 1073 */   private static final Loader EXPECTED_TYPE_ROOT_LOADER = new ExpectedTypeRootLoader(null);
/*      */ 
/*      */   public UnmarshallingContext(UnmarshallerImpl _parent, AssociationMap assoc)
/*      */   {
/*  459 */     this.factories = new HashMap();
/*      */ 
/*  766 */     this.patchers = null;
/*  767 */     this.patchersLen = 0;
/*      */ 
/*  851 */     this.nsBind = new String[16];
/*  852 */     this.nsLen = 0;
/*      */ 
/*  988 */     this.scopes = new Scope[16];
/*      */ 
/*  992 */     this.scopeTop = 0;
/*      */ 
/*  995 */     for (int i = 0; i < this.scopes.length; i++)
/*  996 */       this.scopes[i] = new Scope(this);
/*  390 */     this.parent = _parent;
/*  391 */     this.assoc = assoc;
/*  392 */     this.root = (this.current = new State(null, null));
/*      */   }
/*      */ 
/*      */   public void reset(InfosetScanner scanner, boolean isInplaceMode, JaxBeanInfo expectedType, IDResolver idResolver) {
/*  396 */     this.scanner = scanner;
/*  397 */     this.isInplaceMode = isInplaceMode;
/*  398 */     this.expectedType = expectedType;
/*  399 */     this.idResolver = idResolver;
/*      */   }
/*      */ 
/*      */   public JAXBContextImpl getJAXBContext() {
/*  403 */     return this.parent.context;
/*      */   }
/*      */ 
/*      */   public State getCurrentState() {
/*  407 */     return this.current;
/*      */   }
/*      */ 
/*      */   public Loader selectRootLoader(State state, TagName tag)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  419 */       Loader l = getJAXBContext().selectRootLoader(state, tag);
/*  420 */       if (l != null) return l;
/*      */ 
/*  422 */       if (this.classResolver != null) {
/*  423 */         Class clazz = this.classResolver.resolveElementName(tag.uri, tag.local);
/*  424 */         if (clazz != null) {
/*  425 */           JAXBContextImpl enhanced = getJAXBContext().createAugmented(clazz);
/*  426 */           JaxBeanInfo bi = enhanced.getBeanInfo(clazz);
/*  427 */           return bi.getLoader(enhanced, true);
/*      */         }
/*      */       }
/*      */     } catch (RuntimeException e) {
/*  431 */       throw e;
/*      */     } catch (Exception e) {
/*  433 */       handleError(e);
/*      */     }
/*      */ 
/*  436 */     return null;
/*      */   }
/*      */ 
/*      */   public void clearStates() {
/*  440 */     State last = this.current;
/*  441 */     while (last.next != null) last = last.next;
/*  442 */     while (last.prev != null) {
/*  443 */       last.loader = null;
/*  444 */       last.nil = false;
/*  445 */       last.receiver = null;
/*  446 */       last.intercepter = null;
/*  447 */       last.elementDefaultValue = null;
/*  448 */       last.target = null;
/*  449 */       last = last.prev;
/*  450 */       last.next.prev = null;
/*  451 */       last.next = null;
/*      */     }
/*  453 */     this.current = last;
/*      */   }
/*      */ 
/*      */   public void setFactories(Object factoryInstances)
/*      */   {
/*  462 */     this.factories.clear();
/*  463 */     if (factoryInstances == null) {
/*  464 */       return;
/*      */     }
/*  466 */     if ((factoryInstances instanceof Object[]))
/*  467 */       for (Object factory : (Object[])factoryInstances)
/*      */       {
/*  469 */         addFactory(factory);
/*      */       }
/*      */     else
/*  472 */       addFactory(factoryInstances);
/*      */   }
/*      */ 
/*      */   private void addFactory(Object factory)
/*      */   {
/*  477 */     for (Method m : factory.getClass().getMethods())
/*      */     {
/*  479 */       if (m.getName().startsWith("create"))
/*      */       {
/*  481 */         if (m.getParameterTypes().length <= 0)
/*      */         {
/*  484 */           Class type = m.getReturnType();
/*      */ 
/*  486 */           this.factories.put(type, new Factory(factory, m)); } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*  491 */   public void startDocument(LocatorEx locator, NamespaceContext nsContext) throws SAXException { if (locator != null)
/*  492 */       this.locator = locator;
/*  493 */     this.environmentNamespaceContext = nsContext;
/*      */ 
/*  495 */     this.result = null;
/*  496 */     this.current = this.root;
/*      */ 
/*  498 */     this.patchersLen = 0;
/*  499 */     this.aborted = false;
/*  500 */     this.isUnmarshalInProgress = true;
/*  501 */     this.nsLen = 0;
/*      */ 
/*  503 */     setThreadAffinity();
/*      */ 
/*  505 */     if (this.expectedType != null)
/*  506 */       this.root.loader = EXPECTED_TYPE_ROOT_LOADER;
/*      */     else {
/*  508 */       this.root.loader = DEFAULT_ROOT_LOADER;
/*      */     }
/*  510 */     this.idResolver.startDocument(this); }
/*      */ 
/*      */   public void startElement(TagName tagName) throws SAXException
/*      */   {
/*  514 */     pushCoordinator();
/*      */     try {
/*  516 */       _startElement(tagName);
/*      */     } finally {
/*  518 */       popCoordinator();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void _startElement(TagName tagName)
/*      */     throws SAXException
/*      */   {
/*  527 */     if (this.assoc != null) {
/*  528 */       this.currentElement = this.scanner.getCurrentElement();
/*      */     }
/*  530 */     Loader h = this.current.loader;
/*  531 */     this.current.push();
/*      */ 
/*  534 */     h.childElement(this.current, tagName);
/*  535 */     assert (this.current.loader != null);
/*      */ 
/*  537 */     this.current.loader.startElement(this.current, tagName);
/*      */   }
/*      */ 
/*      */   public void text(CharSequence pcdata) throws SAXException {
/*  541 */     pushCoordinator();
/*      */     try {
/*  543 */       if ((this.current.elementDefaultValue != null) && 
/*  544 */         (pcdata.length() == 0))
/*      */       {
/*  546 */         pcdata = this.current.elementDefaultValue;
/*      */       }
/*      */ 
/*  549 */       this.current.loader.text(this.current, pcdata);
/*      */     } finally {
/*  551 */       popCoordinator();
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void endElement(TagName tagName) throws SAXException {
/*  556 */     pushCoordinator();
/*      */     try {
/*  558 */       State child = this.current;
/*      */ 
/*  561 */       child.loader.leaveElement(child, tagName);
/*      */ 
/*  564 */       Object target = child.target;
/*  565 */       Receiver recv = child.receiver;
/*  566 */       Intercepter intercepter = child.intercepter;
/*  567 */       child.pop();
/*      */ 
/*  570 */       if (intercepter != null)
/*  571 */         target = intercepter.intercept(this.current, target);
/*  572 */       if (recv != null)
/*  573 */         recv.receive(this.current, target);
/*      */     } finally {
/*  575 */       popCoordinator();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDocument() throws SAXException {
/*  580 */     runPatchers();
/*  581 */     this.idResolver.endDocument();
/*      */ 
/*  583 */     this.isUnmarshalInProgress = false;
/*  584 */     this.currentElement = null;
/*  585 */     this.locator = DUMMY_INSTANCE;
/*  586 */     this.environmentNamespaceContext = null;
/*      */ 
/*  589 */     assert (this.root == this.current);
/*      */ 
/*  591 */     resetThreadAffinity();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean expectText()
/*      */   {
/*  599 */     return this.current.loader.expectText;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public XmlVisitor.TextPredictor getPredictor()
/*      */   {
/*  607 */     return this;
/*      */   }
/*      */ 
/*      */   public UnmarshallingContext getContext() {
/*  611 */     return this;
/*      */   }
/*      */ 
/*      */   public Object getResult()
/*      */     throws UnmarshalException
/*      */   {
/*  618 */     if (this.isUnmarshalInProgress) {
/*  619 */       throw new IllegalStateException();
/*      */     }
/*  621 */     if (!this.aborted) return this.result;
/*      */ 
/*  624 */     throw new UnmarshalException((String)null);
/*      */   }
/*      */ 
/*      */   void clearResult() {
/*  628 */     if (this.isUnmarshalInProgress) {
/*  629 */       throw new IllegalStateException();
/*      */     }
/*  631 */     this.result = null;
/*      */   }
/*      */ 
/*      */   public Object createInstance(Class<?> clazz)
/*      */     throws SAXException
/*      */   {
/*  639 */     if (!this.factories.isEmpty()) {
/*  640 */       Factory factory = (Factory)this.factories.get(clazz);
/*  641 */       if (factory != null)
/*  642 */         return factory.createInstance();
/*      */     }
/*  644 */     return ClassFactory.create(clazz);
/*      */   }
/*      */ 
/*      */   public Object createInstance(JaxBeanInfo beanInfo)
/*      */     throws SAXException
/*      */   {
/*  652 */     if (!this.factories.isEmpty()) {
/*  653 */       Factory factory = (Factory)this.factories.get(beanInfo.jaxbType);
/*  654 */       if (factory != null)
/*  655 */         return factory.createInstance();
/*      */     }
/*      */     try {
/*  658 */       return beanInfo.createInstance(this);
/*      */     } catch (IllegalAccessException e) {
/*  660 */       Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e, false);
/*      */     } catch (InvocationTargetException e) {
/*  662 */       Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e, false);
/*      */     } catch (InstantiationException e) {
/*  664 */       Loader.reportError("Unable to create an instance of " + beanInfo.jaxbType.getName(), e, false);
/*      */     }
/*  666 */     return null;
/*      */   }
/*      */ 
/*      */   public void handleEvent(ValidationEvent event, boolean canRecover)
/*      */     throws SAXException
/*      */   {
/*  688 */     ValidationEventHandler eventHandler = this.parent.getEventHandler();
/*      */ 
/*  690 */     boolean recover = eventHandler.handleEvent(event);
/*      */ 
/*  694 */     if (!recover) this.aborted = true;
/*      */ 
/*  696 */     if ((!canRecover) || (!recover))
/*  697 */       throw new SAXParseException2(event.getMessage(), this.locator, new UnmarshalException(event.getMessage(), event.getLinkedException()));
/*      */   }
/*      */ 
/*      */   public boolean handleEvent(ValidationEvent event)
/*      */   {
/*      */     try
/*      */     {
/*  706 */       boolean recover = this.parent.getEventHandler().handleEvent(event);
/*  707 */       if (!recover) this.aborted = true;
/*  708 */       return recover;
/*      */     }
/*      */     catch (RuntimeException re) {
/*      */     }
/*  712 */     return false;
/*      */   }
/*      */ 
/*      */   public void handleError(Exception e)
/*      */     throws SAXException
/*      */   {
/*  722 */     handleError(e, true);
/*      */   }
/*      */ 
/*      */   public void handleError(Exception e, boolean canRecover) throws SAXException {
/*  726 */     handleEvent(new ValidationEventImpl(1, e.getMessage(), this.locator.getLocation(), e), canRecover);
/*      */   }
/*      */ 
/*      */   public void handleError(String msg) {
/*  730 */     handleEvent(new ValidationEventImpl(1, msg, this.locator.getLocation()));
/*      */   }
/*      */ 
/*      */   protected ValidationEventLocator getLocation() {
/*  734 */     return this.locator.getLocation();
/*      */   }
/*      */ 
/*      */   public LocatorEx getLocator()
/*      */   {
/*  743 */     return this.locator;
/*      */   }
/*      */ 
/*      */   public void errorUnresolvedIDREF(Object bean, String idref, LocatorEx loc)
/*      */     throws SAXException
/*      */   {
/*  749 */     handleEvent(new ValidationEventImpl(1, Messages.UNRESOLVED_IDREF.format(new Object[] { idref }), loc.getLocation()), true);
/*      */   }
/*      */ 
/*      */   public void addPatcher(Patcher job)
/*      */   {
/*  779 */     if (this.patchers == null)
/*  780 */       this.patchers = new Patcher[32];
/*  781 */     if (this.patchers.length == this.patchersLen) {
/*  782 */       Patcher[] buf = new Patcher[this.patchersLen * 2];
/*  783 */       System.arraycopy(this.patchers, 0, buf, 0, this.patchersLen);
/*  784 */       this.patchers = buf;
/*      */     }
/*  786 */     this.patchers[(this.patchersLen++)] = job;
/*      */   }
/*      */ 
/*      */   private void runPatchers() throws SAXException
/*      */   {
/*  791 */     if (this.patchers != null)
/*  792 */       for (int i = 0; i < this.patchersLen; i++) {
/*  793 */         this.patchers[i].run();
/*  794 */         this.patchers[i] = null;
/*      */       }
/*      */   }
/*      */ 
/*      */   public String addToIdTable(String id)
/*      */     throws SAXException
/*      */   {
/*  826 */     Object o = this.current.target;
/*  827 */     if (o == null)
/*  828 */       o = State.access$400(this.current).target;
/*  829 */     this.idResolver.bind(id, o);
/*  830 */     return id;
/*      */   }
/*      */ 
/*      */   public Callable getObjectFromId(String id, Class targetType)
/*      */     throws SAXException
/*      */   {
/*  843 */     return this.idResolver.resolve(id, targetType);
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */   {
/*  855 */     if (this.nsBind.length == this.nsLen)
/*      */     {
/*  857 */       String[] n = new String[this.nsLen * 2];
/*  858 */       System.arraycopy(this.nsBind, 0, n, 0, this.nsLen);
/*  859 */       this.nsBind = n;
/*      */     }
/*  861 */     this.nsBind[(this.nsLen++)] = prefix;
/*  862 */     this.nsBind[(this.nsLen++)] = uri;
/*      */   }
/*      */   public void endPrefixMapping(String prefix) {
/*  865 */     this.nsLen -= 2;
/*      */   }
/*      */   private String resolveNamespacePrefix(String prefix) {
/*  868 */     if (prefix.equals("xml")) {
/*  869 */       return "http://www.w3.org/XML/1998/namespace";
/*      */     }
/*  871 */     for (int i = this.nsLen - 2; i >= 0; i -= 2) {
/*  872 */       if (prefix.equals(this.nsBind[i])) {
/*  873 */         return this.nsBind[(i + 1)];
/*      */       }
/*      */     }
/*  876 */     if (this.environmentNamespaceContext != null)
/*      */     {
/*  878 */       return this.environmentNamespaceContext.getNamespaceURI(prefix.intern());
/*      */     }
/*      */ 
/*  882 */     if (prefix.equals("")) {
/*  883 */       return "";
/*      */     }
/*      */ 
/*  886 */     return null;
/*      */   }
/*      */ 
/*      */   public String[] getNewlyDeclaredPrefixes()
/*      */   {
/*  897 */     return getPrefixList(State.access$400(this.current).numNsDecl);
/*      */   }
/*      */ 
/*      */   public String[] getAllDeclaredPrefixes()
/*      */   {
/*  908 */     return getPrefixList(0);
/*      */   }
/*      */ 
/*      */   private String[] getPrefixList(int startIndex) {
/*  912 */     int size = (this.current.numNsDecl - startIndex) / 2;
/*  913 */     String[] r = new String[size];
/*  914 */     for (int i = 0; i < r.length; i++)
/*  915 */       r[i] = this.nsBind[(startIndex + i * 2)];
/*  916 */     return r;
/*      */   }
/*      */ 
/*      */   public Iterator<String> getPrefixes(String uri)
/*      */   {
/*  925 */     return Collections.unmodifiableList(getAllPrefixesInList(uri)).iterator();
/*      */   }
/*      */ 
/*      */   private List<String> getAllPrefixesInList(String uri)
/*      */   {
/*  930 */     List a = new ArrayList();
/*      */ 
/*  932 */     if (uri == null)
/*  933 */       throw new IllegalArgumentException();
/*  934 */     if (uri.equals("http://www.w3.org/XML/1998/namespace")) {
/*  935 */       a.add("xml");
/*  936 */       return a;
/*      */     }
/*  938 */     if (uri.equals("http://www.w3.org/2000/xmlns/")) {
/*  939 */       a.add("xmlns");
/*  940 */       return a;
/*      */     }
/*      */ 
/*  943 */     for (int i = this.nsLen - 2; i >= 0; i -= 2)
/*  944 */       if ((uri.equals(this.nsBind[(i + 1)])) && 
/*  945 */         (getNamespaceURI(this.nsBind[i]).equals(this.nsBind[(i + 1)])))
/*      */       {
/*  947 */         a.add(this.nsBind[i]);
/*      */       }
/*  949 */     return a;
/*      */   }
/*      */ 
/*      */   public String getPrefix(String uri) {
/*  953 */     if (uri == null)
/*  954 */       throw new IllegalArgumentException();
/*  955 */     if (uri.equals("http://www.w3.org/XML/1998/namespace"))
/*  956 */       return "xml";
/*  957 */     if (uri.equals("http://www.w3.org/2000/xmlns/")) {
/*  958 */       return "xmlns";
/*      */     }
/*  960 */     for (int i = this.nsLen - 2; i >= 0; i -= 2)
/*  961 */       if ((uri.equals(this.nsBind[(i + 1)])) && 
/*  962 */         (getNamespaceURI(this.nsBind[i]).equals(this.nsBind[(i + 1)])))
/*      */       {
/*  964 */         return this.nsBind[i];
/*      */       }
/*  966 */     if (this.environmentNamespaceContext != null) {
/*  967 */       return this.environmentNamespaceContext.getPrefix(uri);
/*      */     }
/*  969 */     return null;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(String prefix) {
/*  973 */     if (prefix == null)
/*  974 */       throw new IllegalArgumentException();
/*  975 */     if (prefix.equals("xmlns")) {
/*  976 */       return "http://www.w3.org/2000/xmlns/";
/*      */     }
/*  978 */     return resolveNamespacePrefix(prefix);
/*      */   }
/*      */ 
/*      */   public void startScope(int frameSize)
/*      */   {
/* 1015 */     this.scopeTop += frameSize;
/*      */ 
/* 1018 */     if (this.scopeTop >= this.scopes.length) {
/* 1019 */       Scope[] s = new Scope[Math.max(this.scopeTop + 1, this.scopes.length * 2)];
/* 1020 */       System.arraycopy(this.scopes, 0, s, 0, this.scopes.length);
/* 1021 */       for (int i = this.scopes.length; i < s.length; i++)
/* 1022 */         s[i] = new Scope(this);
/* 1023 */       this.scopes = s;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endScope(int frameSize)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/* 1039 */       for (; frameSize > 0; this.scopeTop -= 1) {
/* 1040 */         this.scopes[this.scopeTop].finish();
/*      */ 
/* 1039 */         frameSize--;
/*      */       }
/*      */     } catch (AccessorException e) {
/* 1042 */       handleError(e);
/*      */ 
/* 1046 */       for (; frameSize > 0; frameSize--)
/* 1047 */         this.scopes[(this.scopeTop--)] = new Scope(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Scope getScope(int offset)
/*      */   {
/* 1061 */     return this.scopes[(this.scopeTop - offset)];
/*      */   }
/*      */ 
/*      */   public void recordInnerPeer(Object innerPeer)
/*      */   {
/* 1167 */     if (this.assoc != null)
/* 1168 */       this.assoc.addInner(this.currentElement, innerPeer);
/*      */   }
/*      */ 
/*      */   public Object getInnerPeer()
/*      */   {
/* 1179 */     if ((this.assoc != null) && (this.isInplaceMode)) {
/* 1180 */       return this.assoc.getInnerPeer(this.currentElement);
/*      */     }
/* 1182 */     return null;
/*      */   }
/*      */ 
/*      */   public void recordOuterPeer(Object outerPeer)
/*      */   {
/* 1193 */     if (this.assoc != null)
/* 1194 */       this.assoc.addOuter(this.currentElement, outerPeer);
/*      */   }
/*      */ 
/*      */   public Object getOuterPeer()
/*      */   {
/* 1205 */     if ((this.assoc != null) && (this.isInplaceMode)) {
/* 1206 */       return this.assoc.getOuterPeer(this.currentElement);
/*      */     }
/* 1208 */     return null;
/*      */   }
/*      */ 
/*      */   public String getXMIMEContentType()
/*      */   {
/* 1227 */     Object t = this.current.target;
/* 1228 */     if (t == null) return null;
/* 1229 */     return getJAXBContext().getXMIMEContentType(t);
/*      */   }
/*      */ 
/*      */   public static UnmarshallingContext getInstance()
/*      */   {
/* 1237 */     return (UnmarshallingContext)Coordinator._getInstance();
/*      */   }
/*      */ 
/*      */   public Collection<QName> getCurrentExpectedElements()
/*      */   {
/* 1247 */     pushCoordinator();
/*      */     try {
/* 1249 */       State s = getCurrentState();
/* 1250 */       Loader l = s.loader;
/* 1251 */       return l != null ? l.getExpectedChildElements() : null;
/*      */     } finally {
/* 1253 */       popCoordinator();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Collection<QName> getCurrentExpectedAttributes()
/*      */   {
/* 1264 */     pushCoordinator();
/*      */     try {
/* 1266 */       State s = getCurrentState();
/* 1267 */       Loader l = s.loader;
/* 1268 */       return l != null ? l.getExpectedAttributes() : null;
/*      */     } finally {
/* 1270 */       popCoordinator();
/*      */     }
/*      */   }
/*      */ 
/*      */   public StructureLoader getStructureLoader()
/*      */   {
/* 1280 */     if ((this.current.loader instanceof StructureLoader)) {
/* 1281 */       return (StructureLoader)this.current.loader;
/*      */     }
/* 1283 */     return null;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   92 */     LocatorImpl loc = new LocatorImpl();
/*   93 */     loc.setPublicId(null);
/*   94 */     loc.setSystemId(null);
/*   95 */     loc.setLineNumber(-1);
/*   96 */     loc.setColumnNumber(-1);
/*      */   }
/*      */ 
/*      */   private static final class DefaultRootLoader extends Loader
/*      */     implements Receiver
/*      */   {
/*      */     public void childElement(UnmarshallingContext.State state, TagName ea)
/*      */       throws SAXException
/*      */     {
/* 1086 */       Loader loader = state.getContext().selectRootLoader(state, ea);
/* 1087 */       if (loader != null) {
/* 1088 */         UnmarshallingContext.State.access$502(state, loader);
/* 1089 */         UnmarshallingContext.State.access$702(state, this);
/* 1090 */         return;
/*      */       }
/*      */ 
/* 1095 */       JaxBeanInfo beanInfo = XsiTypeLoader.parseXsiType(state, ea, null);
/* 1096 */       if (beanInfo == null)
/*      */       {
/* 1098 */         reportUnexpectedChildElement(ea, false);
/* 1099 */         return;
/*      */       }
/*      */ 
/* 1102 */       UnmarshallingContext.State.access$502(state, beanInfo.getLoader(null, false));
/* 1103 */       UnmarshallingContext.State.access$1602(UnmarshallingContext.State.access$400(state), new JAXBElement(ea.createQName(), Object.class, null));
/* 1104 */       UnmarshallingContext.State.access$702(state, this);
/*      */     }
/*      */ 
/*      */     public Collection<QName> getExpectedChildElements()
/*      */     {
/* 1109 */       return UnmarshallingContext.getInstance().getJAXBContext().getValidRootNames();
/*      */     }
/*      */ 
/*      */     public void receive(UnmarshallingContext.State state, Object o) {
/* 1113 */       if (UnmarshallingContext.State.access$1600(state) != null) {
/* 1114 */         ((JAXBElement)UnmarshallingContext.State.access$1600(state)).setValue(o);
/* 1115 */         o = UnmarshallingContext.State.access$1600(state);
/*      */       }
/* 1117 */       if (UnmarshallingContext.State.access$600(state)) {
/* 1118 */         ((JAXBElement)o).setNil(true);
/*      */       }
/* 1120 */       state.getContext().result = o;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class ExpectedTypeRootLoader extends Loader
/*      */     implements Receiver
/*      */   {
/*      */     public void childElement(UnmarshallingContext.State state, TagName ea)
/*      */     {
/* 1135 */       UnmarshallingContext context = state.getContext();
/*      */ 
/* 1138 */       QName qn = new QName(ea.uri, ea.local);
/* 1139 */       UnmarshallingContext.State.access$1002(UnmarshallingContext.State.access$400(state), new JAXBElement(qn, context.expectedType.jaxbType, null, null));
/* 1140 */       UnmarshallingContext.State.access$702(state, this);
/*      */ 
/* 1145 */       UnmarshallingContext.State.access$502(state, new XsiNilLoader(context.expectedType.getLoader(null, true)));
/*      */     }
/*      */ 
/*      */     public void receive(UnmarshallingContext.State state, Object o) {
/* 1149 */       JAXBElement e = (JAXBElement)UnmarshallingContext.State.access$1000(state);
/* 1150 */       e.setValue(o);
/* 1151 */       state.getContext().recordOuterPeer(e);
/* 1152 */       state.getContext().result = e;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Factory
/*      */   {
/*      */     private final Object factorInstance;
/*      */     private final Method method;
/*      */ 
/*      */     public Factory(Object factorInstance, Method method)
/*      */     {
/*  365 */       this.factorInstance = factorInstance;
/*  366 */       this.method = method;
/*      */     }
/*      */ 
/*      */     public Object createInstance() throws SAXException {
/*      */       try {
/*  371 */         return this.method.invoke(this.factorInstance, new Object[0]);
/*      */       } catch (IllegalAccessException e) {
/*  373 */         UnmarshallingContext.getInstance().handleError(e, false);
/*      */       } catch (InvocationTargetException e) {
/*  375 */         UnmarshallingContext.getInstance().handleError(e, false);
/*      */       }
/*  377 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class State
/*      */   {
/*      */     private Loader loader;
/*      */     private Receiver receiver;
/*      */     private Intercepter intercepter;
/*      */     private Object target;
/*      */     private Object backup;
/*      */     private int numNsDecl;
/*      */     private String elementDefaultValue;
/*      */     private State prev;
/*      */     private State next;
/*  253 */     private boolean nil = false;
/*      */ 
/*  258 */     private boolean mixed = false;
/*      */ 
/*      */     public UnmarshallingContext getContext()
/*      */     {
/*  264 */       return UnmarshallingContext.this;
/*      */     }
/*      */ 
/*      */     private State(State prev) {
/*  268 */       this.prev = prev;
/*  269 */       if (prev != null) {
/*  270 */         prev.next = this;
/*  271 */         if (prev.mixed)
/*  272 */           this.mixed = true;
/*      */       }
/*      */     }
/*      */ 
/*      */     private void push() {
/*  277 */       if (this.next == null) {
/*  278 */         this.next = new State(UnmarshallingContext.this, this);
/*      */       }
/*  280 */       State n = this.next;
/*  281 */       n.numNsDecl = UnmarshallingContext.this.nsLen;
/*  282 */       UnmarshallingContext.this.current = n;
/*      */     }
/*      */ 
/*      */     private void pop() {
/*  286 */       assert (this.prev != null);
/*  287 */       this.loader = null;
/*  288 */       this.nil = false;
/*  289 */       this.mixed = false;
/*  290 */       this.receiver = null;
/*  291 */       this.intercepter = null;
/*  292 */       this.elementDefaultValue = null;
/*  293 */       this.target = null;
/*  294 */       UnmarshallingContext.this.current = this.prev;
/*  295 */       this.next = null;
/*      */     }
/*      */ 
/*      */     public boolean isMixed() {
/*  299 */       return this.mixed;
/*      */     }
/*      */ 
/*      */     public Object getTarget() {
/*  303 */       return this.target;
/*      */     }
/*      */ 
/*      */     public void setLoader(Loader loader) {
/*  307 */       if ((loader instanceof StructureLoader))
/*  308 */         this.mixed = (!((StructureLoader)loader).getBeanInfo().hasElementOnlyContentModel());
/*  309 */       this.loader = loader;
/*      */     }
/*      */ 
/*      */     public void setReceiver(Receiver receiver) {
/*  313 */       this.receiver = receiver;
/*      */     }
/*      */ 
/*      */     public State getPrev() {
/*  317 */       return this.prev;
/*      */     }
/*      */ 
/*      */     public void setIntercepter(Intercepter intercepter) {
/*  321 */       this.intercepter = intercepter;
/*      */     }
/*      */ 
/*      */     public void setBackup(Object backup) {
/*  325 */       this.backup = backup;
/*      */     }
/*      */ 
/*      */     public void setTarget(Object target) {
/*  329 */       this.target = target;
/*      */     }
/*      */ 
/*      */     public Object getBackup() {
/*  333 */       return this.backup;
/*      */     }
/*      */ 
/*      */     public boolean isNil() {
/*  337 */       return this.nil;
/*      */     }
/*      */ 
/*      */     public void setNil(boolean nil) {
/*  341 */       this.nil = nil;
/*      */     }
/*      */ 
/*      */     public Loader getLoader() {
/*  345 */       return this.loader;
/*      */     }
/*      */ 
/*      */     public String getElementDefaultValue() {
/*  349 */       return this.elementDefaultValue;
/*      */     }
/*      */ 
/*      */     public void setElementDefaultValue(String elementDefaultValue) {
/*  353 */       this.elementDefaultValue = elementDefaultValue;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext
 * JD-Core Version:    0.6.2
 */