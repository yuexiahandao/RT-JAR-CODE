/*      */ package com.sun.xml.internal.bind.v2.schemagen;
/*      */ 
/*      */ import com.sun.istack.internal.NotNull;
/*      */ import com.sun.istack.internal.Nullable;
/*      */ import com.sun.xml.internal.bind.api.CompositeStructure;
/*      */ import com.sun.xml.internal.bind.api.ErrorListener;
/*      */ import com.sun.xml.internal.bind.v2.TODO;
/*      */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*      */ import com.sun.xml.internal.bind.v2.model.core.ArrayInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.AttributePropertyInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.ClassInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.Element;
/*      */ import com.sun.xml.internal.bind.v2.model.core.ElementInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.ElementPropertyInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.EnumConstant;
/*      */ import com.sun.xml.internal.bind.v2.model.core.EnumLeafInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*      */ import com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.MaybeElement;
/*      */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*      */ import com.sun.xml.internal.bind.v2.model.core.NonElementRef;
/*      */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*      */ import com.sun.xml.internal.bind.v2.model.core.ReferencePropertyInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
/*      */ import com.sun.xml.internal.bind.v2.model.core.TypeRef;
/*      */ import com.sun.xml.internal.bind.v2.model.core.ValuePropertyInfo;
/*      */ import com.sun.xml.internal.bind.v2.model.core.WildcardMode;
/*      */ import com.sun.xml.internal.bind.v2.model.impl.ClassInfoImpl;
/*      */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*      */ import com.sun.xml.internal.bind.v2.runtime.SwaRefAdapter;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.episode.Bindings;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.episode.Klass;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.episode.SchemaBindings;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Any;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.AttrDecls;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.AttributeType;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexContent;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexExtension;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexType;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexTypeHost;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ContentModelContainer;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ExplicitGroup;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Import;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalAttribute;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.LocalElement;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.NoFixedFacet;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Occurs;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Schema;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleContent;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleExtension;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleRestrictionModel;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleType;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.SimpleTypeHost;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TopLevelAttribute;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TopLevelElement;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeDefParticle;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.TypeHost;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Wildcard;
/*      */ import com.sun.xml.internal.bind.v2.util.CollisionCheckStack;
/*      */ import com.sun.xml.internal.bind.v2.util.StackRecorder;
/*      */ import com.sun.xml.internal.txw2.TXW;
/*      */ import com.sun.xml.internal.txw2.TxwException;
/*      */ import com.sun.xml.internal.txw2.TypedXmlWriter;
/*      */ import com.sun.xml.internal.txw2.output.ResultFactory;
/*      */ import com.sun.xml.internal.txw2.output.XmlSerializer;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.activation.MimeType;
/*      */ import javax.xml.bind.SchemaOutputResolver;
/*      */ import javax.xml.bind.annotation.XmlElement;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.transform.Result;
/*      */ import javax.xml.transform.stream.StreamResult;
/*      */ import org.xml.sax.SAXParseException;
/*      */ 
/*      */ public final class XmlSchemaGenerator<T, C, F, M>
/*      */ {
/*  132 */   private static final Logger logger = com.sun.xml.internal.bind.Util.getClassLogger();
/*      */ 
/*  143 */   private final Map<String, XmlSchemaGenerator<T, C, F, M>.Namespace> namespaces = new TreeMap(NAMESPACE_COMPARATOR);
/*      */   private ErrorListener errorListener;
/*      */   private Navigator<T, C, F, M> navigator;
/*      */   private final TypeInfoSet<T, C, F, M> types;
/*      */   private final NonElement<T, C> stringType;
/*      */   private final NonElement<T, C> anyType;
/*  168 */   private final CollisionCheckStack<ClassInfo<T, C>> collisionChecker = new CollisionCheckStack();
/*      */ 
/* 1587 */   private static final Comparator<String> NAMESPACE_COMPARATOR = new Comparator() {
/*      */     public int compare(String lhs, String rhs) {
/* 1589 */       return -lhs.compareTo(rhs);
/*      */     }
/* 1587 */   };
/*      */   private static final String newline = "\n";
/*      */ 
/*      */   public XmlSchemaGenerator(Navigator<T, C, F, M> navigator, TypeInfoSet<T, C, F, M> types)
/*      */   {
/*  171 */     this.navigator = navigator;
/*  172 */     this.types = types;
/*      */ 
/*  174 */     this.stringType = types.getTypeInfo(navigator.ref(String.class));
/*  175 */     this.anyType = types.getAnyTypeInfo();
/*      */ 
/*  178 */     for (ClassInfo ci : types.beans().values())
/*  179 */       add(ci);
/*  180 */     for (ElementInfo ei1 : types.getElementMappings(null).values())
/*  181 */       add(ei1);
/*  182 */     for (EnumLeafInfo ei : types.enums().values())
/*  183 */       add(ei);
/*  184 */     for (ArrayInfo a : types.arrays().values())
/*  185 */       add(a);
/*      */   }
/*      */ 
/*      */   private XmlSchemaGenerator<T, C, F, M>.Namespace getNamespace(String uri) {
/*  189 */     Namespace n = (Namespace)this.namespaces.get(uri);
/*  190 */     if (n == null)
/*  191 */       this.namespaces.put(uri, n = new Namespace(uri));
/*  192 */     return n;
/*      */   }
/*      */ 
/*      */   public void add(ClassInfo<T, C> clazz)
/*      */   {
/*  204 */     assert (clazz != null);
/*      */ 
/*  206 */     String nsUri = null;
/*      */ 
/*  208 */     if (clazz.getClazz() == this.navigator.asDecl(CompositeStructure.class)) {
/*  209 */       return;
/*      */     }
/*  211 */     if (clazz.isElement())
/*      */     {
/*  213 */       nsUri = clazz.getElementName().getNamespaceURI();
/*  214 */       Namespace ns = getNamespace(nsUri);
/*  215 */       ns.classes.add(clazz);
/*  216 */       ns.addDependencyTo(clazz.getTypeName());
/*      */ 
/*  219 */       add(clazz.getElementName(), false, clazz);
/*      */     }
/*      */ 
/*  222 */     QName tn = clazz.getTypeName();
/*  223 */     if (tn != null) {
/*  224 */       nsUri = tn.getNamespaceURI();
/*      */     }
/*  227 */     else if (nsUri == null) {
/*  228 */       return;
/*      */     }
/*      */ 
/*  231 */     Namespace n = getNamespace(nsUri);
/*  232 */     n.classes.add(clazz);
/*      */ 
/*  235 */     for (PropertyInfo p : clazz.getProperties()) {
/*  236 */       n.processForeignNamespaces(p, 1);
/*  237 */       if ((p instanceof AttributePropertyInfo)) {
/*  238 */         AttributePropertyInfo ap = (AttributePropertyInfo)p;
/*  239 */         String aUri = ap.getXmlName().getNamespaceURI();
/*  240 */         if (aUri.length() > 0)
/*      */         {
/*  242 */           getNamespace(aUri).addGlobalAttribute(ap);
/*  243 */           n.addDependencyTo(ap.getXmlName());
/*      */         }
/*      */       }
/*  246 */       if ((p instanceof ElementPropertyInfo)) {
/*  247 */         ElementPropertyInfo ep = (ElementPropertyInfo)p;
/*  248 */         for (TypeRef tref : ep.getTypes()) {
/*  249 */           String eUri = tref.getTagName().getNamespaceURI();
/*  250 */           if ((eUri.length() > 0) && (!eUri.equals(n.uri))) {
/*  251 */             getNamespace(eUri).addGlobalElement(tref);
/*  252 */             n.addDependencyTo(tref.getTagName());
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  257 */       if (generateSwaRefAdapter(p)) {
/*  258 */         n.useSwaRef = true;
/*      */       }
/*  260 */       MimeType mimeType = p.getExpectedMimeType();
/*  261 */       if (mimeType != null) {
/*  262 */         n.useMimeNs = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  268 */     ClassInfo bc = clazz.getBaseClass();
/*  269 */     if (bc != null) {
/*  270 */       add(bc);
/*  271 */       n.addDependencyTo(bc.getTypeName());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void add(ElementInfo<T, C> elem)
/*      */   {
/*  279 */     assert (elem != null);
/*      */ 
/*  281 */     boolean nillable = false;
/*      */ 
/*  283 */     QName name = elem.getElementName();
/*  284 */     Namespace n = getNamespace(name.getNamespaceURI());
/*      */     ElementInfo ei;
/*      */     ElementInfo ei;
/*  287 */     if (elem.getScope() != null)
/*  288 */       ei = this.types.getElementInfo(elem.getScope().getClazz(), name);
/*      */     else {
/*  290 */       ei = this.types.getElementInfo(null, name);
/*      */     }
/*      */ 
/*  293 */     XmlElement xmlElem = (XmlElement)ei.getProperty().readAnnotation(XmlElement.class);
/*      */ 
/*  295 */     if (xmlElem == null)
/*  296 */       nillable = false;
/*      */     else
/*  298 */       nillable = xmlElem.nillable();
/*      */     Namespace tmp138_136 = n; tmp138_136.getClass(); n.elementDecls.put(name.getLocalPart(), new XmlSchemaGenerator.Namespace.ElementWithType(tmp138_136, nillable, elem.getContentType()));
/*      */ 
/*  304 */     n.processForeignNamespaces(elem.getProperty(), 1);
/*      */   }
/*      */ 
/*      */   public void add(EnumLeafInfo<T, C> envm) {
/*  308 */     assert (envm != null);
/*      */ 
/*  310 */     String nsUri = null;
/*      */ 
/*  312 */     if (envm.isElement())
/*      */     {
/*  314 */       nsUri = envm.getElementName().getNamespaceURI();
/*  315 */       Namespace ns = getNamespace(nsUri);
/*  316 */       ns.enums.add(envm);
/*  317 */       ns.addDependencyTo(envm.getTypeName());
/*      */ 
/*  320 */       add(envm.getElementName(), false, envm);
/*      */     }
/*      */ 
/*  323 */     QName typeName = envm.getTypeName();
/*  324 */     if (typeName != null) {
/*  325 */       nsUri = typeName.getNamespaceURI();
/*      */     }
/*  327 */     else if (nsUri == null) {
/*  328 */       return;
/*      */     }
/*      */ 
/*  331 */     Namespace n = getNamespace(nsUri);
/*  332 */     n.enums.add(envm);
/*      */ 
/*  335 */     n.addDependencyTo(envm.getBaseType().getTypeName());
/*      */   }
/*      */ 
/*      */   public void add(ArrayInfo<T, C> a) {
/*  339 */     assert (a != null);
/*      */ 
/*  341 */     String namespaceURI = a.getTypeName().getNamespaceURI();
/*  342 */     Namespace n = getNamespace(namespaceURI);
/*  343 */     n.arrays.add(a);
/*      */ 
/*  346 */     n.addDependencyTo(a.getItemType().getTypeName());
/*      */   }
/*      */ 
/*      */   public void add(QName tagName, boolean isNillable, NonElement<T, C> type)
/*      */   {
/*  360 */     if ((type != null) && (type.getType() == this.navigator.ref(CompositeStructure.class))) {
/*  361 */       return;
/*      */     }
/*      */ 
/*  364 */     Namespace n = getNamespace(tagName.getNamespaceURI());
/*      */     Namespace tmp51_49 = n; tmp51_49.getClass(); n.elementDecls.put(tagName.getLocalPart(), new XmlSchemaGenerator.Namespace.ElementWithType(tmp51_49, isNillable, type));
/*      */ 
/*  368 */     if (type != null)
/*  369 */       n.addDependencyTo(type.getTypeName());
/*      */   }
/*      */ 
/*      */   public void writeEpisodeFile(XmlSerializer out)
/*      */   {
/*  376 */     Bindings root = (Bindings)TXW.create(Bindings.class, out);
/*      */ 
/*  378 */     if (this.namespaces.containsKey(""))
/*  379 */       root._namespace("http://java.sun.com/xml/ns/jaxb", "jaxb");
/*  380 */     root.version("2.1");
/*      */ 
/*  384 */     for (Map.Entry e : this.namespaces.entrySet()) {
/*  385 */       Bindings group = root.bindings();
/*      */ 
/*  388 */       String tns = (String)e.getKey();
/*      */       String prefix;
/*      */       String prefix;
/*  389 */       if (!tns.equals("")) {
/*  390 */         group._namespace(tns, "tns");
/*  391 */         prefix = "tns:";
/*      */       } else {
/*  393 */         prefix = "";
/*      */       }
/*      */ 
/*  396 */       group.scd("x-schema::" + (tns.equals("") ? "" : "tns"));
/*  397 */       group.schemaBindings().map(false);
/*      */ 
/*  399 */       for (ClassInfo ci : ((Namespace)e.getValue()).classes) {
/*  400 */         if (ci.getTypeName() != null)
/*      */         {
/*  402 */           if (ci.getTypeName().getNamespaceURI().equals(tns)) {
/*  403 */             Bindings child = group.bindings();
/*  404 */             child.scd('~' + prefix + ci.getTypeName().getLocalPart());
/*  405 */             child.klass().ref(ci.getName());
/*      */           }
/*      */ 
/*  408 */           if ((ci.isElement()) && (ci.getElementName().getNamespaceURI().equals(tns))) {
/*  409 */             Bindings child = group.bindings();
/*  410 */             child.scd(prefix + ci.getElementName().getLocalPart());
/*  411 */             child.klass().ref(ci.getName());
/*      */           }
/*      */         }
/*      */       }
/*  415 */       for (EnumLeafInfo en : ((Namespace)e.getValue()).enums) {
/*  416 */         if (en.getTypeName() != null)
/*      */         {
/*  418 */           Bindings child = group.bindings();
/*  419 */           child.scd('~' + prefix + en.getTypeName().getLocalPart());
/*  420 */           child.klass().ref(this.navigator.getClassName(en.getClazz()));
/*      */         }
/*      */       }
/*  423 */       group.commit(true);
/*      */     }
/*      */ 
/*  426 */     root.commit();
/*      */   }
/*      */ 
/*      */   public void write(SchemaOutputResolver resolver, ErrorListener errorListener)
/*      */     throws IOException
/*      */   {
/*  433 */     if (resolver == null) {
/*  434 */       throw new IllegalArgumentException();
/*      */     }
/*  436 */     if (logger.isLoggable(Level.FINE))
/*      */     {
/*  438 */       logger.log(Level.FINE, "Wrigin XML Schema for " + toString(), new StackRecorder());
/*      */     }
/*      */ 
/*  442 */     resolver = new FoolProofResolver(resolver);
/*  443 */     this.errorListener = errorListener;
/*      */ 
/*  445 */     Map schemaLocations = this.types.getSchemaLocations();
/*      */ 
/*  447 */     Map out = new HashMap();
/*  448 */     Map systemIds = new HashMap();
/*      */ 
/*  452 */     this.namespaces.remove("http://www.w3.org/2001/XMLSchema");
/*      */ 
/*  456 */     for (Namespace n : this.namespaces.values()) {
/*  457 */       String schemaLocation = (String)schemaLocations.get(n.uri);
/*  458 */       if (schemaLocation != null) {
/*  459 */         systemIds.put(n, schemaLocation);
/*      */       } else {
/*  461 */         Result output = resolver.createOutput(n.uri, "schema" + (out.size() + 1) + ".xsd");
/*  462 */         if (output != null) {
/*  463 */           out.put(n, output);
/*  464 */           systemIds.put(n, output.getSystemId());
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  470 */     for (Map.Entry e : out.entrySet()) {
/*  471 */       Result result = (Result)e.getValue();
/*  472 */       ((Namespace)e.getKey()).writeTo(result, systemIds);
/*  473 */       if ((result instanceof StreamResult)) {
/*  474 */         OutputStream outputStream = ((StreamResult)result).getOutputStream();
/*  475 */         if (outputStream != null) {
/*  476 */           outputStream.close();
/*      */         } else {
/*  478 */           Writer writer = ((StreamResult)result).getWriter();
/*  479 */           if (writer != null) writer.close();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean generateSwaRefAdapter(NonElementRef<T, C> typeRef)
/*      */   {
/* 1454 */     return generateSwaRefAdapter(typeRef.getSource());
/*      */   }
/*      */ 
/*      */   private boolean generateSwaRefAdapter(PropertyInfo<T, C> prop)
/*      */   {
/* 1461 */     Adapter adapter = prop.getAdapter();
/* 1462 */     if (adapter == null) return false;
/* 1463 */     Object o = this.navigator.asDecl(SwaRefAdapter.class);
/* 1464 */     if (o == null) return false;
/* 1465 */     return o.equals(adapter.adapterType);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1473 */     StringBuilder buf = new StringBuilder();
/* 1474 */     for (Namespace ns : this.namespaces.values()) {
/* 1475 */       if (buf.length() > 0) buf.append(',');
/* 1476 */       buf.append(ns.uri).append('=').append(ns);
/*      */     }
/* 1478 */     return super.toString() + '[' + buf + ']';
/*      */   }
/*      */ 
/*      */   private static String getProcessContentsModeName(WildcardMode wc)
/*      */   {
/* 1487 */     switch (2.$SwitchMap$com$sun$xml$internal$bind$v2$model$core$WildcardMode[wc.ordinal()]) {
/*      */     case 1:
/*      */     case 2:
/* 1490 */       return wc.name().toLowerCase();
/*      */     case 3:
/* 1492 */       return null;
/*      */     }
/* 1494 */     throw new IllegalStateException();
/*      */   }
/*      */ 
/*      */   protected static String relativize(String uri, String baseUri)
/*      */   {
/*      */     try
/*      */     {
/* 1517 */       assert (uri != null);
/*      */ 
/* 1519 */       if (baseUri == null) return uri;
/*      */ 
/* 1521 */       URI theUri = new URI(Util.escapeURI(uri));
/* 1522 */       URI theBaseUri = new URI(Util.escapeURI(baseUri));
/*      */ 
/* 1524 */       if ((theUri.isOpaque()) || (theBaseUri.isOpaque())) {
/* 1525 */         return uri;
/*      */       }
/* 1527 */       if ((!Util.equalsIgnoreCase(theUri.getScheme(), theBaseUri.getScheme())) || (!Util.equal(theUri.getAuthority(), theBaseUri.getAuthority())))
/*      */       {
/* 1529 */         return uri;
/*      */       }
/* 1531 */       String uriPath = theUri.getPath();
/* 1532 */       String basePath = theBaseUri.getPath();
/*      */ 
/* 1535 */       if (!basePath.endsWith("/")) {
/* 1536 */         basePath = Util.normalizeUriPath(basePath);
/*      */       }
/*      */ 
/* 1539 */       if (uriPath.equals(basePath)) {
/* 1540 */         return ".";
/*      */       }
/* 1542 */       String relPath = calculateRelativePath(uriPath, basePath, fixNull(theUri.getScheme()).equals("file"));
/*      */ 
/* 1544 */       if (relPath == null)
/* 1545 */         return uri;
/* 1546 */       StringBuilder relUri = new StringBuilder();
/* 1547 */       relUri.append(relPath);
/* 1548 */       if (theUri.getQuery() != null)
/* 1549 */         relUri.append('?').append(theUri.getQuery());
/* 1550 */       if (theUri.getFragment() != null) {
/* 1551 */         relUri.append('#').append(theUri.getFragment());
/*      */       }
/* 1553 */       return relUri.toString(); } catch (URISyntaxException e) {
/*      */     }
/* 1555 */     throw new InternalError("Error escaping one of these uris:\n\t" + uri + "\n\t" + baseUri);
/*      */   }
/*      */ 
/*      */   private static String fixNull(String s)
/*      */   {
/* 1560 */     if (s == null) return "";
/* 1561 */     return s;
/*      */   }
/*      */ 
/*      */   private static String calculateRelativePath(String uri, String base, boolean fileUrl)
/*      */   {
/* 1567 */     boolean onWindows = File.pathSeparatorChar == ';';
/*      */ 
/* 1569 */     if (base == null) {
/* 1570 */       return null;
/*      */     }
/* 1572 */     if (((fileUrl) && (onWindows) && (startsWithIgnoreCase(uri, base))) || (uri.startsWith(base))) {
/* 1573 */       return uri.substring(base.length());
/*      */     }
/* 1575 */     return "../" + calculateRelativePath(uri, Util.getParentUriPath(base), fileUrl);
/*      */   }
/*      */ 
/*      */   private static boolean startsWithIgnoreCase(String s, String t)
/*      */   {
/* 1580 */     return s.toUpperCase().startsWith(t.toUpperCase());
/*      */   }
/*      */ 
/*      */   private class Namespace
/*      */   {
/*      */ 
/*      */     @NotNull
/*      */     final String uri;
/*  496 */     private final Set<XmlSchemaGenerator<T, C, F, M>.Namespace> depends = new LinkedHashSet();
/*      */     private boolean selfReference;
/*  506 */     private final Set<ClassInfo<T, C>> classes = new LinkedHashSet();
/*      */ 
/*  511 */     private final Set<EnumLeafInfo<T, C>> enums = new LinkedHashSet();
/*      */ 
/*  516 */     private final Set<ArrayInfo<T, C>> arrays = new LinkedHashSet();
/*      */ 
/*  521 */     private final MultiMap<String, AttributePropertyInfo<T, C>> attributeDecls = new MultiMap(null);
/*      */ 
/*  526 */     private final MultiMap<String, XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration> elementDecls = new MultiMap(new ElementWithType(true, XmlSchemaGenerator.this.anyType));
/*      */     private Form attributeFormDefault;
/*      */     private Form elementFormDefault;
/*      */     private boolean useSwaRef;
/*      */     private boolean useMimeNs;
/*      */ 
/*      */     public Namespace(String uri)
/*      */     {
/*  545 */       this.uri = uri;
/*  546 */       assert (!XmlSchemaGenerator.this.namespaces.containsKey(uri));
/*  547 */       XmlSchemaGenerator.this.namespaces.put(uri, this);
/*      */     }
/*      */ 
/*      */     private void processForeignNamespaces(PropertyInfo<T, C> p, int processingDepth)
/*      */     {
/*  559 */       for (TypeInfo t : p.ref()) {
/*  560 */         if (((t instanceof ClassInfo)) && (processingDepth > 0)) {
/*  561 */           java.util.List l = ((ClassInfo)t).getProperties();
/*  562 */           for (PropertyInfo subp : l) {
/*  563 */             processForeignNamespaces(subp, --processingDepth);
/*      */           }
/*      */         }
/*  566 */         if ((t instanceof Element)) {
/*  567 */           addDependencyTo(((Element)t).getElementName());
/*      */         }
/*  569 */         if ((t instanceof NonElement))
/*  570 */           addDependencyTo(((NonElement)t).getTypeName());
/*      */       }
/*      */     }
/*      */ 
/*      */     private void addDependencyTo(@Nullable QName qname)
/*      */     {
/*  579 */       if (qname == null) {
/*  580 */         return;
/*      */       }
/*      */ 
/*  583 */       String nsUri = qname.getNamespaceURI();
/*      */ 
/*  585 */       if (nsUri.equals("http://www.w3.org/2001/XMLSchema"))
/*      */       {
/*  587 */         return;
/*      */       }
/*      */ 
/*  590 */       if (nsUri.equals(this.uri)) {
/*  591 */         this.selfReference = true;
/*  592 */         return;
/*      */       }
/*      */ 
/*  596 */       this.depends.add(XmlSchemaGenerator.this.getNamespace(nsUri));
/*      */     }
/*      */ 
/*      */     private void writeTo(Result result, Map<XmlSchemaGenerator<T, C, F, M>.Namespace, String> systemIds)
/*      */       throws IOException
/*      */     {
/*      */       try
/*      */       {
/*  607 */         Schema schema = (Schema)TXW.create(Schema.class, ResultFactory.createSerializer(result));
/*      */ 
/*  610 */         Map xmlNs = XmlSchemaGenerator.this.types.getXmlNs(this.uri);
/*      */ 
/*  612 */         for (Map.Entry e : xmlNs.entrySet()) {
/*  613 */           schema._namespace((String)e.getValue(), (String)e.getKey());
/*      */         }
/*      */ 
/*  616 */         if (this.useSwaRef) {
/*  617 */           schema._namespace("http://ws-i.org/profiles/basic/1.1/xsd", "swaRef");
/*      */         }
/*  619 */         if (this.useMimeNs) {
/*  620 */           schema._namespace("http://www.w3.org/2005/05/xmlmime", "xmime");
/*      */         }
/*  622 */         this.attributeFormDefault = Form.get(XmlSchemaGenerator.this.types.getAttributeFormDefault(this.uri));
/*  623 */         this.attributeFormDefault.declare("attributeFormDefault", schema);
/*      */ 
/*  625 */         this.elementFormDefault = Form.get(XmlSchemaGenerator.this.types.getElementFormDefault(this.uri));
/*      */ 
/*  627 */         this.elementFormDefault.declare("elementFormDefault", schema);
/*      */ 
/*  632 */         if ((!xmlNs.containsValue("http://www.w3.org/2001/XMLSchema")) && (!xmlNs.containsKey("xs")))
/*      */         {
/*  634 */           schema._namespace("http://www.w3.org/2001/XMLSchema", "xs");
/*  635 */         }schema.version("1.0");
/*      */ 
/*  637 */         if (this.uri.length() != 0) {
/*  638 */           schema.targetNamespace(this.uri);
/*      */         }
/*      */ 
/*  642 */         for (Namespace ns : this.depends) {
/*  643 */           schema._namespace(ns.uri);
/*      */         }
/*      */ 
/*  646 */         if ((this.selfReference) && (this.uri.length() != 0))
/*      */         {
/*  649 */           schema._namespace(this.uri, "tns");
/*      */         }
/*      */ 
/*  652 */         schema._pcdata("\n");
/*      */ 
/*  655 */         for (Namespace n : this.depends) {
/*  656 */           Import imp = schema._import();
/*  657 */           if (n.uri.length() != 0)
/*  658 */             imp.namespace(n.uri);
/*  659 */           String refSystemId = (String)systemIds.get(n);
/*  660 */           if ((refSystemId != null) && (!refSystemId.equals("")))
/*      */           {
/*  662 */             imp.schemaLocation(XmlSchemaGenerator.relativize(refSystemId, result.getSystemId()));
/*      */           }
/*  664 */           schema._pcdata("\n");
/*      */         }
/*  666 */         if (this.useSwaRef) {
/*  667 */           schema._import().namespace("http://ws-i.org/profiles/basic/1.1/xsd").schemaLocation("http://ws-i.org/profiles/basic/1.1/swaref.xsd");
/*      */         }
/*  669 */         if (this.useMimeNs) {
/*  670 */           schema._import().namespace("http://www.w3.org/2005/05/xmlmime").schemaLocation("http://www.w3.org/2005/05/xmlmime");
/*      */         }
/*      */ 
/*  674 */         for (Map.Entry e : this.elementDecls.entrySet()) {
/*  675 */           ((ElementDeclaration)e.getValue()).writeTo((String)e.getKey(), schema);
/*  676 */           schema._pcdata("\n");
/*      */         }
/*  678 */         for (ClassInfo c : this.classes)
/*  679 */           if (c.getTypeName() != null)
/*      */           {
/*  683 */             if (this.uri.equals(c.getTypeName().getNamespaceURI()))
/*  684 */               writeClass(c, schema);
/*  685 */             schema._pcdata("\n");
/*      */           }
/*  687 */         for (EnumLeafInfo e : this.enums)
/*  688 */           if (e.getTypeName() != null)
/*      */           {
/*  692 */             if (this.uri.equals(e.getTypeName().getNamespaceURI()))
/*  693 */               writeEnum(e, schema);
/*  694 */             schema._pcdata("\n");
/*      */           }
/*  696 */         for (ArrayInfo a : this.arrays) {
/*  697 */           writeArray(a, schema);
/*  698 */           schema._pcdata("\n");
/*      */         }
/*  700 */         for (Map.Entry e : this.attributeDecls.entrySet()) {
/*  701 */           TopLevelAttribute a = schema.attribute();
/*  702 */           a.name((String)e.getKey());
/*  703 */           if (e.getValue() == null)
/*  704 */             writeTypeRef(a, XmlSchemaGenerator.this.stringType, "type");
/*      */           else
/*  706 */             writeAttributeTypeRef((AttributePropertyInfo)e.getValue(), a);
/*  707 */           schema._pcdata("\n");
/*      */         }
/*      */ 
/*  711 */         schema.commit();
/*      */       } catch (TxwException e) {
/*  713 */         XmlSchemaGenerator.logger.log(Level.INFO, e.getMessage(), e);
/*  714 */         throw new IOException(e.getMessage());
/*      */       }
/*      */     }
/*      */ 
/*      */     private void writeTypeRef(TypeHost th, NonElementRef<T, C> typeRef, String refAttName)
/*      */     {
/*  731 */       switch (XmlSchemaGenerator.2.$SwitchMap$com$sun$xml$internal$bind$v2$model$core$ID[typeRef.getSource().id().ordinal()]) {
/*      */       case 1:
/*  733 */         th._attribute(refAttName, new QName("http://www.w3.org/2001/XMLSchema", "ID"));
/*  734 */         return;
/*      */       case 2:
/*  736 */         th._attribute(refAttName, new QName("http://www.w3.org/2001/XMLSchema", "IDREF"));
/*  737 */         return;
/*      */       case 3:
/*  740 */         break;
/*      */       default:
/*  742 */         throw new IllegalStateException();
/*      */       }
/*      */ 
/*  746 */       MimeType mimeType = typeRef.getSource().getExpectedMimeType();
/*  747 */       if (mimeType != null) {
/*  748 */         th._attribute(new QName("http://www.w3.org/2005/05/xmlmime", "expectedContentTypes", "xmime"), mimeType.toString());
/*      */       }
/*      */ 
/*  752 */       if (XmlSchemaGenerator.this.generateSwaRefAdapter(typeRef)) {
/*  753 */         th._attribute(refAttName, new QName("http://ws-i.org/profiles/basic/1.1/xsd", "swaRef", "ref"));
/*  754 */         return;
/*      */       }
/*      */ 
/*  758 */       if (typeRef.getSource().getSchemaType() != null) {
/*  759 */         th._attribute(refAttName, typeRef.getSource().getSchemaType());
/*  760 */         return;
/*      */       }
/*      */ 
/*  764 */       writeTypeRef(th, typeRef.getTarget(), refAttName);
/*      */     }
/*      */ 
/*      */     private void writeTypeRef(TypeHost th, NonElement<T, C> type, String refAttName)
/*      */     {
/*  780 */       Element e = null;
/*  781 */       if ((type instanceof MaybeElement)) {
/*  782 */         MaybeElement me = (MaybeElement)type;
/*  783 */         boolean isElement = me.isElement();
/*  784 */         if (isElement) e = me.asElement();
/*      */       }
/*  786 */       if ((type instanceof Element)) {
/*  787 */         e = (Element)type;
/*      */       }
/*  789 */       if (type.getTypeName() == null) {
/*  790 */         if ((e != null) && (e.getElementName() != null)) {
/*  791 */           th.block();
/*  792 */           if ((type instanceof ClassInfo))
/*  793 */             writeClass((ClassInfo)type, th);
/*      */           else
/*  795 */             writeEnum((EnumLeafInfo)type, (SimpleTypeHost)th);
/*      */         }
/*      */         else
/*      */         {
/*  799 */           th.block();
/*  800 */           if ((type instanceof ClassInfo)) {
/*  801 */             if (XmlSchemaGenerator.this.collisionChecker.push((ClassInfo)type)) {
/*  802 */               XmlSchemaGenerator.this.errorListener.warning(new SAXParseException(Messages.ANONYMOUS_TYPE_CYCLE.format(new Object[] { XmlSchemaGenerator.this.collisionChecker.getCycleString() }), null));
/*      */             }
/*      */             else
/*      */             {
/*  807 */               writeClass((ClassInfo)type, th);
/*      */             }
/*  809 */             XmlSchemaGenerator.this.collisionChecker.pop();
/*      */           } else {
/*  811 */             writeEnum((EnumLeafInfo)type, (SimpleTypeHost)th);
/*      */           }
/*      */         }
/*      */       }
/*  815 */       else th._attribute(refAttName, type.getTypeName());
/*      */     }
/*      */ 
/*      */     private void writeArray(ArrayInfo<T, C> a, Schema schema)
/*      */     {
/*  823 */       ComplexType ct = schema.complexType().name(a.getTypeName().getLocalPart());
/*  824 */       ct._final("#all");
/*  825 */       LocalElement le = ct.sequence().element().name("item");
/*  826 */       le.type(a.getItemType().getTypeName());
/*  827 */       le.minOccurs(0).maxOccurs("unbounded");
/*  828 */       le.nillable(true);
/*  829 */       ct.commit();
/*      */     }
/*      */ 
/*      */     private void writeEnum(EnumLeafInfo<T, C> e, SimpleTypeHost th)
/*      */     {
/*  836 */       SimpleType st = th.simpleType();
/*  837 */       writeName(e, st);
/*      */ 
/*  839 */       SimpleRestrictionModel base = st.restriction();
/*  840 */       writeTypeRef(base, e.getBaseType(), "base");
/*      */ 
/*  842 */       for (EnumConstant c : e.getConstants()) {
/*  843 */         base.enumeration().value(c.getLexicalValue());
/*      */       }
/*  845 */       st.commit();
/*      */     }
/*      */ 
/*      */     private void writeClass(ClassInfo<T, C> c, TypeHost parent)
/*      */     {
/*  856 */       if (containsValueProp(c)) {
/*  857 */         if (c.getProperties().size() == 1)
/*      */         {
/*  863 */           ValuePropertyInfo vp = (ValuePropertyInfo)c.getProperties().get(0);
/*  864 */           SimpleType st = ((SimpleTypeHost)parent).simpleType();
/*  865 */           writeName(c, st);
/*  866 */           if (vp.isCollection())
/*  867 */             writeTypeRef(st.list(), vp.getTarget(), "itemType");
/*      */           else {
/*  869 */             writeTypeRef(st.restriction(), vp.getTarget(), "base");
/*      */           }
/*  871 */           return;
/*      */         }
/*      */ 
/*  885 */         ComplexType ct = ((ComplexTypeHost)parent).complexType();
/*  886 */         writeName(c, ct);
/*  887 */         if (c.isFinal()) {
/*  888 */           ct._final("extension restriction");
/*      */         }
/*  890 */         SimpleExtension se = ct.simpleContent().extension();
/*  891 */         se.block();
/*  892 */         for (PropertyInfo p : c.getProperties()) {
/*  893 */           switch (XmlSchemaGenerator.2.$SwitchMap$com$sun$xml$internal$bind$v2$model$core$PropertyKind[p.kind().ordinal()]) {
/*      */           case 1:
/*  895 */             handleAttributeProp((AttributePropertyInfo)p, se);
/*  896 */             break;
/*      */           case 2:
/*  898 */             TODO.checkSpec("what if vp.isCollection() == true?");
/*  899 */             ValuePropertyInfo vp = (ValuePropertyInfo)p;
/*  900 */             se.base(vp.getTarget().getTypeName());
/*  901 */             break;
/*      */           case 3:
/*      */           case 4:
/*      */           default:
/*  905 */             if (!$assertionsDisabled) throw new AssertionError();
/*  906 */             throw new IllegalStateException();
/*      */           }
/*      */         }
/*  909 */         se.commit();
/*      */ 
/*  911 */         TODO.schemaGenerator("figure out what to do if bc != null");
/*  912 */         TODO.checkSpec("handle sec 8.9.5.2, bullet #4");
/*      */ 
/*  916 */         return;
/*      */       }
/*      */ 
/*  922 */       ComplexType ct = ((ComplexTypeHost)parent).complexType();
/*  923 */       writeName(c, ct);
/*  924 */       if (c.isFinal())
/*  925 */         ct._final("extension restriction");
/*  926 */       if (c.isAbstract()) {
/*  927 */         ct._abstract(true);
/*      */       }
/*      */ 
/*  930 */       AttrDecls contentModel = ct;
/*  931 */       TypeDefParticle contentModelOwner = ct;
/*      */ 
/*  934 */       ClassInfo bc = c.getBaseClass();
/*  935 */       if (bc != null) {
/*  936 */         if (bc.hasValueProperty())
/*      */         {
/*  938 */           SimpleExtension se = ct.simpleContent().extension();
/*  939 */           contentModel = se;
/*  940 */           contentModelOwner = null;
/*  941 */           se.base(bc.getTypeName());
/*      */         } else {
/*  943 */           ComplexExtension ce = ct.complexContent().extension();
/*  944 */           contentModel = ce;
/*  945 */           contentModelOwner = ce;
/*      */ 
/*  947 */           ce.base(bc.getTypeName());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  952 */       if (contentModelOwner != null)
/*      */       {
/*  954 */         ArrayList children = new ArrayList();
/*  955 */         for (PropertyInfo p : c.getProperties())
/*      */         {
/*  957 */           if (((p instanceof ReferencePropertyInfo)) && (((ReferencePropertyInfo)p).isMixed())) {
/*  958 */             ct.mixed(true);
/*      */           }
/*  960 */           Tree t = buildPropertyContentModel(p);
/*  961 */           if (t != null) {
/*  962 */             children.add(t);
/*      */           }
/*      */         }
/*  965 */         Tree top = Tree.makeGroup(c.isOrdered() ? GroupKind.SEQUENCE : GroupKind.ALL, children);
/*      */ 
/*  968 */         top.write(contentModelOwner);
/*      */       }
/*      */ 
/*  972 */       for (PropertyInfo p : c.getProperties()) {
/*  973 */         if ((p instanceof AttributePropertyInfo)) {
/*  974 */           handleAttributeProp((AttributePropertyInfo)p, contentModel);
/*      */         }
/*      */       }
/*  977 */       if (c.hasAttributeWildcard()) {
/*  978 */         contentModel.anyAttribute().namespace("##other").processContents("skip");
/*      */       }
/*  980 */       ct.commit();
/*      */     }
/*      */ 
/*      */     private void writeName(NonElement<T, C> c, TypedXmlWriter xw)
/*      */     {
/*  987 */       QName tn = c.getTypeName();
/*  988 */       if (tn != null)
/*  989 */         xw._attribute("name", tn.getLocalPart());
/*      */     }
/*      */ 
/*      */     private boolean containsValueProp(ClassInfo<T, C> c) {
/*  993 */       for (PropertyInfo p : c.getProperties()) {
/*  994 */         if ((p instanceof ValuePropertyInfo)) return true;
/*      */       }
/*  996 */       return false;
/*      */     }
/*      */ 
/*      */     private Tree buildPropertyContentModel(PropertyInfo<T, C> p)
/*      */     {
/* 1003 */       switch (XmlSchemaGenerator.2.$SwitchMap$com$sun$xml$internal$bind$v2$model$core$PropertyKind[p.kind().ordinal()]) {
/*      */       case 3:
/* 1005 */         return handleElementProp((ElementPropertyInfo)p);
/*      */       case 1:
/* 1008 */         return null;
/*      */       case 4:
/* 1010 */         return handleReferenceProp((ReferencePropertyInfo)p);
/*      */       case 5:
/* 1012 */         return handleMapProp((MapPropertyInfo)p);
/*      */       case 2:
/* 1015 */         if (!$assertionsDisabled) throw new AssertionError();
/* 1016 */         throw new IllegalStateException();
/*      */       }
/* 1018 */       if (!$assertionsDisabled) throw new AssertionError();
/* 1019 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     private Tree handleElementProp(final ElementPropertyInfo<T, C> ep)
/*      */     {
/* 1033 */       if (ep.isValueList()) {
/* 1034 */         return new Tree.Term() {
/*      */           protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1036 */             TypeRef t = (TypeRef)ep.getTypes().get(0);
/* 1037 */             LocalElement e = parent.element();
/* 1038 */             e.block();
/* 1039 */             QName tn = t.getTagName();
/* 1040 */             e.name(tn.getLocalPart());
/* 1041 */             com.sun.xml.internal.bind.v2.schemagen.xmlschema.List lst = e.simpleType().list();
/* 1042 */             XmlSchemaGenerator.Namespace.this.writeTypeRef(lst, t, "itemType");
/* 1043 */             XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, tn);
/* 1044 */             writeOccurs(e, (isOptional) || (!ep.isRequired()), repeated);
/*      */           }
/*      */         };
/*      */       }
/*      */ 
/* 1049 */       ArrayList children = new ArrayList();
/* 1050 */       for (final TypeRef t : ep.getTypes()) {
/* 1051 */         children.add(new Tree.Term() {
/*      */           protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1053 */             LocalElement e = parent.element();
/*      */ 
/* 1055 */             QName tn = t.getTagName();
/*      */ 
/* 1057 */             PropertyInfo propInfo = t.getSource();
/* 1058 */             TypeInfo parentInfo = propInfo == null ? null : propInfo.parent();
/*      */ 
/* 1060 */             if (XmlSchemaGenerator.Namespace.this.canBeDirectElementRef(t, tn, parentInfo)) {
/* 1061 */               if ((!t.getTarget().isSimpleType()) && ((t.getTarget() instanceof ClassInfo)) && (XmlSchemaGenerator.this.collisionChecker.findDuplicate((ClassInfo)t.getTarget()))) {
/* 1062 */                 e.ref(new QName(XmlSchemaGenerator.Namespace.this.uri, tn.getLocalPart()));
/*      */               }
/*      */               else {
/* 1065 */                 QName elemName = null;
/* 1066 */                 if ((t.getTarget() instanceof Element)) {
/* 1067 */                   Element te = (Element)t.getTarget();
/* 1068 */                   elemName = te.getElementName();
/*      */                 }
/*      */ 
/* 1071 */                 Collection refs = propInfo.ref();
/* 1072 */                 if ((refs != null) && (!refs.isEmpty()) && (elemName != null)) {
/* 1073 */                   ClassInfoImpl cImpl = (ClassInfoImpl)refs.iterator().next();
/* 1074 */                   if ((cImpl != null) && (cImpl.getElementName() != null))
/* 1075 */                     e.ref(new QName(cImpl.getElementName().getNamespaceURI(), tn.getLocalPart()));
/*      */                   else
/* 1077 */                     e.ref(new QName("", tn.getLocalPart()));
/*      */                 }
/*      */                 else {
/* 1080 */                   e.ref(tn);
/*      */                 }
/*      */               }
/*      */             } else {
/* 1084 */               e.name(tn.getLocalPart());
/* 1085 */               XmlSchemaGenerator.Namespace.this.writeTypeRef(e, t, "type");
/* 1086 */               XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, tn);
/*      */             }
/*      */ 
/* 1089 */             if (t.isNillable()) {
/* 1090 */               e.nillable(true);
/*      */             }
/* 1092 */             if (t.getDefaultValue() != null)
/* 1093 */               e._default(t.getDefaultValue());
/* 1094 */             writeOccurs(e, isOptional, repeated);
/*      */           }
/*      */         });
/*      */       }
/*      */ 
/* 1099 */       final Tree choice = Tree.makeGroup(GroupKind.CHOICE, children).makeOptional(!ep.isRequired()).makeRepeated(ep.isCollection());
/*      */ 
/* 1104 */       final QName ename = ep.getXmlName();
/* 1105 */       if (ename != null) {
/* 1106 */         return new Tree.Term() {
/*      */           protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1108 */             LocalElement e = parent.element();
/* 1109 */             if ((ename.getNamespaceURI().length() > 0) && 
/* 1110 */               (!ename.getNamespaceURI().equals(XmlSchemaGenerator.Namespace.this.uri)))
/*      */             {
/* 1113 */               e.ref(new QName(ename.getNamespaceURI(), ename.getLocalPart()));
/* 1114 */               return;
/*      */             }
/*      */ 
/* 1117 */             e.name(ename.getLocalPart());
/* 1118 */             XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, ename);
/*      */ 
/* 1120 */             if (ep.isCollectionNillable()) {
/* 1121 */               e.nillable(true);
/*      */             }
/* 1123 */             writeOccurs(e, !ep.isCollectionRequired(), repeated);
/*      */ 
/* 1125 */             ComplexType p = e.complexType();
/* 1126 */             choice.write(p);
/*      */           }
/*      */         };
/*      */       }
/* 1130 */       return choice;
/*      */     }
/*      */ 
/*      */     private boolean canBeDirectElementRef(TypeRef<T, C> t, QName tn, TypeInfo parentInfo)
/*      */     {
/* 1141 */       Element te = null;
/* 1142 */       ClassInfo ci = null;
/* 1143 */       QName targetTagName = null;
/*      */ 
/* 1145 */       if ((t.isNillable()) || (t.getDefaultValue() != null))
/*      */       {
/* 1147 */         return false;
/*      */       }
/*      */ 
/* 1150 */       if ((t.getTarget() instanceof Element)) {
/* 1151 */         te = (Element)t.getTarget();
/* 1152 */         targetTagName = te.getElementName();
/* 1153 */         if ((te instanceof ClassInfo)) {
/* 1154 */           ci = (ClassInfo)te;
/*      */         }
/*      */       }
/*      */ 
/* 1158 */       String nsUri = tn.getNamespaceURI();
/* 1159 */       if ((!nsUri.equals(this.uri)) && (nsUri.length() > 0) && ((!(parentInfo instanceof ClassInfo)) || (((ClassInfo)parentInfo).getTypeName() != null))) {
/* 1160 */         return true;
/*      */       }
/*      */ 
/* 1164 */       if ((ci != null) && (targetTagName != null) && (te.getScope() == null) && 
/* 1165 */         (targetTagName.getLocalPart().equals(tn.getLocalPart()))) {
/* 1166 */         return true;
/*      */       }
/*      */ 
/* 1171 */       if (te != null) {
/* 1172 */         return (targetTagName != null) && (targetTagName.equals(tn));
/*      */       }
/*      */ 
/* 1175 */       return false;
/*      */     }
/*      */ 
/*      */     private void handleAttributeProp(AttributePropertyInfo<T, C> ap, AttrDecls attr)
/*      */     {
/* 1208 */       LocalAttribute localAttribute = attr.attribute();
/*      */ 
/* 1210 */       String attrURI = ap.getXmlName().getNamespaceURI();
/* 1211 */       if (attrURI.equals("")) {
/* 1212 */         localAttribute.name(ap.getXmlName().getLocalPart());
/*      */ 
/* 1214 */         writeAttributeTypeRef(ap, localAttribute);
/*      */ 
/* 1216 */         this.attributeFormDefault.writeForm(localAttribute, ap.getXmlName());
/*      */       } else {
/* 1218 */         localAttribute.ref(ap.getXmlName());
/*      */       }
/*      */ 
/* 1221 */       if (ap.isRequired())
/*      */       {
/* 1223 */         localAttribute.use("required");
/*      */       }
/*      */     }
/*      */ 
/*      */     private void writeAttributeTypeRef(AttributePropertyInfo<T, C> ap, AttributeType a) {
/* 1228 */       if (ap.isCollection())
/* 1229 */         writeTypeRef(a.simpleType().list(), ap, "itemType");
/*      */       else
/* 1231 */         writeTypeRef(a, ap, "type");
/*      */     }
/*      */ 
/*      */     private Tree handleReferenceProp(final ReferencePropertyInfo<T, C> rp)
/*      */     {
/* 1243 */       ArrayList children = new ArrayList();
/*      */ 
/* 1245 */       for (final Element e : rp.getElements()) {
/* 1246 */         children.add(new Tree.Term() {
/*      */           protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1248 */             LocalElement eref = parent.element();
/*      */ 
/* 1250 */             boolean local = false;
/*      */ 
/* 1252 */             QName en = e.getElementName();
/* 1253 */             if (e.getScope() != null)
/*      */             {
/* 1255 */               boolean qualified = en.getNamespaceURI().equals(XmlSchemaGenerator.Namespace.this.uri);
/* 1256 */               boolean unqualified = en.getNamespaceURI().equals("");
/* 1257 */               if ((qualified) || (unqualified))
/*      */               {
/* 1261 */                 if (unqualified) {
/* 1262 */                   if (XmlSchemaGenerator.Namespace.this.elementFormDefault.isEffectivelyQualified)
/* 1263 */                     eref.form("unqualified");
/*      */                 }
/* 1265 */                 else if (!XmlSchemaGenerator.Namespace.this.elementFormDefault.isEffectivelyQualified) {
/* 1266 */                   eref.form("qualified");
/*      */                 }
/*      */ 
/* 1269 */                 local = true;
/* 1270 */                 eref.name(en.getLocalPart());
/*      */ 
/* 1273 */                 if ((e instanceof ClassInfo))
/* 1274 */                   XmlSchemaGenerator.Namespace.this.writeTypeRef(eref, (ClassInfo)e, "type");
/*      */                 else {
/* 1276 */                   XmlSchemaGenerator.Namespace.this.writeTypeRef(eref, ((ElementInfo)e).getContentType(), "type");
/*      */                 }
/*      */               }
/*      */             }
/* 1280 */             if (!local)
/* 1281 */               eref.ref(en);
/* 1282 */             writeOccurs(eref, isOptional, repeated);
/*      */           }
/*      */         });
/*      */       }
/*      */ 
/* 1287 */       final WildcardMode wc = rp.getWildcard();
/* 1288 */       if (wc != null) {
/* 1289 */         children.add(new Tree.Term() {
/*      */           protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1291 */             Any any = parent.any();
/* 1292 */             String pcmode = XmlSchemaGenerator.getProcessContentsModeName(wc);
/* 1293 */             if (pcmode != null) any.processContents(pcmode);
/* 1294 */             any.namespace("##other");
/* 1295 */             writeOccurs(any, isOptional, repeated);
/*      */           }
/*      */ 
/*      */         });
/*      */       }
/*      */ 
/* 1301 */       final Tree choice = Tree.makeGroup(GroupKind.CHOICE, children).makeRepeated(rp.isCollection()).makeOptional(!rp.isRequired());
/*      */ 
/* 1303 */       final QName ename = rp.getXmlName();
/*      */ 
/* 1305 */       if (ename != null) {
/* 1306 */         return new Tree.Term() {
/*      */           protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1308 */             LocalElement e = parent.element().name(ename.getLocalPart());
/* 1309 */             XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, ename);
/* 1310 */             if (rp.isCollectionNillable())
/* 1311 */               e.nillable(true);
/* 1312 */             writeOccurs(e, true, repeated);
/*      */ 
/* 1314 */             ComplexType p = e.complexType();
/* 1315 */             choice.write(p);
/*      */           }
/*      */         };
/*      */       }
/* 1319 */       return choice;
/*      */     }
/*      */ 
/*      */     private Tree handleMapProp(final MapPropertyInfo<T, C> mp)
/*      */     {
/* 1330 */       return new Tree.Term() {
/*      */         protected void write(ContentModelContainer parent, boolean isOptional, boolean repeated) {
/* 1332 */           QName ename = mp.getXmlName();
/*      */ 
/* 1334 */           LocalElement e = parent.element();
/* 1335 */           XmlSchemaGenerator.Namespace.this.elementFormDefault.writeForm(e, ename);
/* 1336 */           if (mp.isCollectionNillable()) {
/* 1337 */             e.nillable(true);
/*      */           }
/* 1339 */           e = e.name(ename.getLocalPart());
/* 1340 */           writeOccurs(e, isOptional, repeated);
/* 1341 */           ComplexType p = e.complexType();
/*      */ 
/* 1345 */           e = p.sequence().element();
/* 1346 */           e.name("entry").minOccurs(0).maxOccurs("unbounded");
/*      */ 
/* 1348 */           ExplicitGroup seq = e.complexType().sequence();
/* 1349 */           XmlSchemaGenerator.Namespace.this.writeKeyOrValue(seq, "key", mp.getKeyType());
/* 1350 */           XmlSchemaGenerator.Namespace.this.writeKeyOrValue(seq, "value", mp.getValueType());
/*      */         }
/*      */       };
/*      */     }
/*      */ 
/*      */     private void writeKeyOrValue(ExplicitGroup seq, String tagName, NonElement<T, C> typeRef) {
/* 1356 */       LocalElement key = seq.element().name(tagName);
/* 1357 */       key.minOccurs(0);
/* 1358 */       writeTypeRef(key, typeRef, "type");
/*      */     }
/*      */ 
/*      */     public void addGlobalAttribute(AttributePropertyInfo<T, C> ap) {
/* 1362 */       this.attributeDecls.put(ap.getXmlName().getLocalPart(), ap);
/* 1363 */       addDependencyTo(ap.getTarget().getTypeName());
/*      */     }
/*      */ 
/*      */     public void addGlobalElement(TypeRef<T, C> tref) {
/* 1367 */       this.elementDecls.put(tref.getTagName().getLocalPart(), new ElementWithType(false, tref.getTarget()));
/* 1368 */       addDependencyTo(tref.getTarget().getTypeName());
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1373 */       StringBuilder buf = new StringBuilder();
/* 1374 */       buf.append("[classes=").append(this.classes);
/* 1375 */       buf.append(",elementDecls=").append(this.elementDecls);
/* 1376 */       buf.append(",enums=").append(this.enums);
/* 1377 */       buf.append("]");
/* 1378 */       return super.toString();
/*      */     }
/*      */ 
/*      */     abstract class ElementDeclaration
/*      */     {
/*      */       ElementDeclaration()
/*      */       {
/*      */       }
/*      */ 
/*      */       public abstract boolean equals(Object paramObject);
/*      */ 
/*      */       public abstract int hashCode();
/*      */ 
/*      */       public abstract void writeTo(String paramString, Schema paramSchema);
/*      */     }
/*      */ 
/*      */     class ElementWithType extends XmlSchemaGenerator<T, C, F, M>.Namespace.ElementDeclaration
/*      */     {
/*      */       private final boolean nillable;
/*      */       private final NonElement<T, C> type;
/*      */ 
/*      */       public ElementWithType(NonElement<T, C> nillable)
/*      */       {
/* 1418 */         super();
/* 1419 */         this.type = type;
/* 1420 */         this.nillable = nillable;
/*      */       }
/*      */ 
/*      */       public void writeTo(String localName, Schema schema) {
/* 1424 */         TopLevelElement e = schema.element().name(localName);
/* 1425 */         if (this.nillable)
/* 1426 */           e.nillable(true);
/* 1427 */         if (this.type != null)
/* 1428 */           XmlSchemaGenerator.Namespace.this.writeTypeRef(e, this.type, "type");
/*      */         else {
/* 1430 */           e.complexType();
/*      */         }
/* 1432 */         e.commit();
/*      */       }
/*      */ 
/*      */       public boolean equals(Object o) {
/* 1436 */         if (this == o) return true;
/* 1437 */         if ((o == null) || (getClass() != o.getClass())) return false;
/*      */ 
/* 1439 */         ElementWithType that = (ElementWithType)o;
/* 1440 */         return this.type.equals(that.type);
/*      */       }
/*      */ 
/*      */       public int hashCode() {
/* 1444 */         return this.type.hashCode();
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.schemagen.XmlSchemaGenerator
 * JD-Core Version:    0.6.2
 */