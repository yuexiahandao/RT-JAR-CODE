/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public final class DOMAdapter
/*     */   implements DOM
/*     */ {
/*     */   private DOMEnhancedForDTM _enhancedDOM;
/*     */   private DOM _dom;
/*     */   private String[] _namesArray;
/*     */   private String[] _urisArray;
/*     */   private int[] _typesArray;
/*     */   private String[] _namespaceArray;
/*  55 */   private short[] _mapping = null;
/*  56 */   private int[] _reverse = null;
/*  57 */   private short[] _NSmapping = null;
/*  58 */   private short[] _NSreverse = null;
/*     */ 
/*  60 */   private StripFilter _filter = null;
/*     */   private int _multiDOMMask;
/*     */ 
/*     */   public DOMAdapter(DOM dom, String[] namesArray, String[] urisArray, int[] typesArray, String[] namespaceArray)
/*     */   {
/*  69 */     if ((dom instanceof DOMEnhancedForDTM)) {
/*  70 */       this._enhancedDOM = ((DOMEnhancedForDTM)dom);
/*     */     }
/*     */ 
/*  73 */     this._dom = dom;
/*  74 */     this._namesArray = namesArray;
/*  75 */     this._urisArray = urisArray;
/*  76 */     this._typesArray = typesArray;
/*  77 */     this._namespaceArray = namespaceArray;
/*     */   }
/*     */ 
/*     */   public void setupMapping(String[] names, String[] urisArray, int[] typesArray, String[] namespaces)
/*     */   {
/*  82 */     this._namesArray = names;
/*  83 */     this._urisArray = urisArray;
/*  84 */     this._typesArray = typesArray;
/*  85 */     this._namespaceArray = namespaces;
/*     */   }
/*     */ 
/*     */   public String[] getNamesArray() {
/*  89 */     return this._namesArray;
/*     */   }
/*     */ 
/*     */   public String[] getUrisArray() {
/*  93 */     return this._urisArray;
/*     */   }
/*     */ 
/*     */   public int[] getTypesArray() {
/*  97 */     return this._typesArray;
/*     */   }
/*     */ 
/*     */   public String[] getNamespaceArray() {
/* 101 */     return this._namespaceArray;
/*     */   }
/*     */ 
/*     */   public DOM getDOMImpl() {
/* 105 */     return this._dom;
/*     */   }
/*     */ 
/*     */   private short[] getMapping() {
/* 109 */     if ((this._mapping == null) && 
/* 110 */       (this._enhancedDOM != null)) {
/* 111 */       this._mapping = this._enhancedDOM.getMapping(this._namesArray, this._urisArray, this._typesArray);
/*     */     }
/*     */ 
/* 115 */     return this._mapping;
/*     */   }
/*     */ 
/*     */   private int[] getReverse() {
/* 119 */     if ((this._reverse == null) && 
/* 120 */       (this._enhancedDOM != null)) {
/* 121 */       this._reverse = this._enhancedDOM.getReverseMapping(this._namesArray, this._urisArray, this._typesArray);
/*     */     }
/*     */ 
/* 126 */     return this._reverse;
/*     */   }
/*     */ 
/*     */   private short[] getNSMapping() {
/* 130 */     if ((this._NSmapping == null) && 
/* 131 */       (this._enhancedDOM != null)) {
/* 132 */       this._NSmapping = this._enhancedDOM.getNamespaceMapping(this._namespaceArray);
/*     */     }
/*     */ 
/* 135 */     return this._NSmapping;
/*     */   }
/*     */ 
/*     */   private short[] getNSReverse() {
/* 139 */     if ((this._NSreverse == null) && 
/* 140 */       (this._enhancedDOM != null)) {
/* 141 */       this._NSreverse = this._enhancedDOM.getReverseNamespaceMapping(this._namespaceArray);
/*     */     }
/*     */ 
/* 145 */     return this._NSreverse;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getIterator()
/*     */   {
/* 152 */     return this._dom.getIterator();
/*     */   }
/*     */ 
/*     */   public String getStringValue() {
/* 156 */     return this._dom.getStringValue();
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getChildren(int node) {
/* 160 */     if (this._enhancedDOM != null) {
/* 161 */       return this._enhancedDOM.getChildren(node);
/*     */     }
/*     */ 
/* 164 */     DTMAxisIterator iterator = this._dom.getChildren(node);
/* 165 */     return iterator.setStartNode(node);
/*     */   }
/*     */ 
/*     */   public void setFilter(StripFilter filter)
/*     */   {
/* 170 */     this._filter = filter;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getTypedChildren(int type) {
/* 174 */     int[] reverse = getReverse();
/*     */ 
/* 176 */     if (this._enhancedDOM != null) {
/* 177 */       return this._enhancedDOM.getTypedChildren(reverse[type]);
/*     */     }
/*     */ 
/* 180 */     return this._dom.getTypedChildren(type);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
/*     */   {
/* 186 */     return this._dom.getNamespaceAxisIterator(axis, getNSReverse()[ns]);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getAxisIterator(int axis) {
/* 190 */     if (this._enhancedDOM != null) {
/* 191 */       return this._enhancedDOM.getAxisIterator(axis);
/*     */     }
/*     */ 
/* 194 */     return this._dom.getAxisIterator(axis);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getTypedAxisIterator(int axis, int type)
/*     */   {
/* 200 */     int[] reverse = getReverse();
/* 201 */     if (this._enhancedDOM != null) {
/* 202 */       return this._enhancedDOM.getTypedAxisIterator(axis, reverse[type]);
/*     */     }
/* 204 */     return this._dom.getTypedAxisIterator(axis, type);
/*     */   }
/*     */ 
/*     */   public int getMultiDOMMask()
/*     */   {
/* 209 */     return this._multiDOMMask;
/*     */   }
/*     */ 
/*     */   public void setMultiDOMMask(int mask) {
/* 213 */     this._multiDOMMask = mask;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getNthDescendant(int type, int n, boolean includeself)
/*     */   {
/* 218 */     return this._dom.getNthDescendant(getReverse()[type], n, includeself);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op)
/*     */   {
/* 224 */     return this._dom.getNodeValueIterator(iterator, type, value, op);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
/* 228 */     return this._dom.orderNodes(source, node);
/*     */   }
/*     */ 
/*     */   public int getExpandedTypeID(int node) {
/* 232 */     short[] mapping = getMapping();
/*     */     int type;
/*     */     int type;
/* 234 */     if (this._enhancedDOM != null) {
/* 235 */       type = mapping[this._enhancedDOM.getExpandedTypeID2(node)];
/*     */     }
/*     */     else
/*     */     {
/*     */       int type;
/* 238 */       if (null != mapping)
/*     */       {
/* 240 */         type = mapping[this._dom.getExpandedTypeID(node)];
/*     */       }
/*     */       else
/*     */       {
/* 244 */         type = this._dom.getExpandedTypeID(node);
/*     */       }
/*     */     }
/* 247 */     return type;
/*     */   }
/*     */ 
/*     */   public int getNamespaceType(int node) {
/* 251 */     return getNSMapping()[this._dom.getNSType(node)];
/*     */   }
/*     */ 
/*     */   public int getNSType(int node) {
/* 255 */     return this._dom.getNSType(node);
/*     */   }
/*     */ 
/*     */   public int getParent(int node) {
/* 259 */     return this._dom.getParent(node);
/*     */   }
/*     */ 
/*     */   public int getAttributeNode(int type, int element) {
/* 263 */     return this._dom.getAttributeNode(getReverse()[type], element);
/*     */   }
/*     */ 
/*     */   public String getNodeName(int node) {
/* 267 */     if (node == -1) {
/* 268 */       return "";
/*     */     }
/* 270 */     return this._dom.getNodeName(node);
/*     */   }
/*     */ 
/*     */   public String getNodeNameX(int node)
/*     */   {
/* 275 */     if (node == -1) {
/* 276 */       return "";
/*     */     }
/* 278 */     return this._dom.getNodeNameX(node);
/*     */   }
/*     */ 
/*     */   public String getNamespaceName(int node)
/*     */   {
/* 283 */     if (node == -1) {
/* 284 */       return "";
/*     */     }
/* 286 */     return this._dom.getNamespaceName(node);
/*     */   }
/*     */ 
/*     */   public String getStringValueX(int node)
/*     */   {
/* 291 */     if (this._enhancedDOM != null) {
/* 292 */       return this._enhancedDOM.getStringValueX(node);
/*     */     }
/*     */ 
/* 295 */     if (node == -1) {
/* 296 */       return "";
/*     */     }
/* 298 */     return this._dom.getStringValueX(node);
/*     */   }
/*     */ 
/*     */   public void copy(int node, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/* 305 */     this._dom.copy(node, handler);
/*     */   }
/*     */ 
/*     */   public void copy(DTMAxisIterator nodes, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/* 311 */     this._dom.copy(nodes, handler);
/*     */   }
/*     */ 
/*     */   public String shallowCopy(int node, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/* 317 */     if (this._enhancedDOM != null) {
/* 318 */       return this._enhancedDOM.shallowCopy(node, handler);
/*     */     }
/*     */ 
/* 321 */     return this._dom.shallowCopy(node, handler);
/*     */   }
/*     */ 
/*     */   public boolean lessThan(int node1, int node2)
/*     */   {
/* 327 */     return this._dom.lessThan(node1, node2);
/*     */   }
/*     */ 
/*     */   public void characters(int textNode, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/* 333 */     if (this._enhancedDOM != null) {
/* 334 */       this._enhancedDOM.characters(textNode, handler);
/*     */     }
/*     */     else
/* 337 */       this._dom.characters(textNode, handler);
/*     */   }
/*     */ 
/*     */   public Node makeNode(int index)
/*     */   {
/* 343 */     return this._dom.makeNode(index);
/*     */   }
/*     */ 
/*     */   public Node makeNode(DTMAxisIterator iter)
/*     */   {
/* 348 */     return this._dom.makeNode(iter);
/*     */   }
/*     */ 
/*     */   public NodeList makeNodeList(int index)
/*     */   {
/* 353 */     return this._dom.makeNodeList(index);
/*     */   }
/*     */ 
/*     */   public NodeList makeNodeList(DTMAxisIterator iter)
/*     */   {
/* 358 */     return this._dom.makeNodeList(iter);
/*     */   }
/*     */ 
/*     */   public String getLanguage(int node)
/*     */   {
/* 363 */     return this._dom.getLanguage(node);
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 368 */     return this._dom.getSize();
/*     */   }
/*     */ 
/*     */   public void setDocumentURI(String uri)
/*     */   {
/* 373 */     if (this._enhancedDOM != null)
/* 374 */       this._enhancedDOM.setDocumentURI(uri);
/*     */   }
/*     */ 
/*     */   public String getDocumentURI()
/*     */   {
/* 380 */     if (this._enhancedDOM != null) {
/* 381 */       return this._enhancedDOM.getDocumentURI();
/*     */     }
/*     */ 
/* 384 */     return "";
/*     */   }
/*     */ 
/*     */   public String getDocumentURI(int node)
/*     */   {
/* 390 */     return this._dom.getDocumentURI(node);
/*     */   }
/*     */ 
/*     */   public int getDocument()
/*     */   {
/* 395 */     return this._dom.getDocument();
/*     */   }
/*     */ 
/*     */   public boolean isElement(int node)
/*     */   {
/* 400 */     return this._dom.isElement(node);
/*     */   }
/*     */ 
/*     */   public boolean isAttribute(int node)
/*     */   {
/* 405 */     return this._dom.isAttribute(node);
/*     */   }
/*     */ 
/*     */   public int getNodeIdent(int nodeHandle)
/*     */   {
/* 410 */     return this._dom.getNodeIdent(nodeHandle);
/*     */   }
/*     */ 
/*     */   public int getNodeHandle(int nodeId)
/*     */   {
/* 415 */     return this._dom.getNodeHandle(nodeId);
/*     */   }
/*     */ 
/*     */   public DOM getResultTreeFrag(int initSize, int rtfType)
/*     */   {
/* 423 */     if (this._enhancedDOM != null) {
/* 424 */       return this._enhancedDOM.getResultTreeFrag(initSize, rtfType);
/*     */     }
/*     */ 
/* 427 */     return this._dom.getResultTreeFrag(initSize, rtfType);
/*     */   }
/*     */ 
/*     */   public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager)
/*     */   {
/* 437 */     if (this._enhancedDOM != null) {
/* 438 */       return this._enhancedDOM.getResultTreeFrag(initSize, rtfType, addToManager);
/*     */     }
/*     */ 
/* 442 */     return this._dom.getResultTreeFrag(initSize, rtfType, addToManager);
/*     */   }
/*     */ 
/*     */   public SerializationHandler getOutputDomBuilder()
/*     */   {
/* 452 */     return this._dom.getOutputDomBuilder();
/*     */   }
/*     */ 
/*     */   public String lookupNamespace(int node, String prefix)
/*     */     throws TransletException
/*     */   {
/* 458 */     return this._dom.lookupNamespace(node, prefix);
/*     */   }
/*     */ 
/*     */   public String getUnparsedEntityURI(String entity) {
/* 462 */     return this._dom.getUnparsedEntityURI(entity);
/*     */   }
/*     */ 
/*     */   public Hashtable getElementsWithIDs() {
/* 466 */     return this._dom.getElementsWithIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.DOMAdapter
 * JD-Core Version:    0.6.2
 */