/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;
/*     */ import com.sun.org.apache.xml.internal.dtm.Axis;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIteratorBase;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBase;
/*     */ import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
/*     */ import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public final class MultiDOM
/*     */   implements DOM
/*     */ {
/*     */   private static final int NO_TYPE = -2;
/*     */   private static final int INITIAL_SIZE = 4;
/*     */   private DOM[] _adapters;
/*     */   private DOMAdapter _main;
/*     */   private DTMManager _dtmManager;
/*     */   private int _free;
/*     */   private int _size;
/*  59 */   private Hashtable _documents = new Hashtable();
/*     */ 
/*     */   public MultiDOM(DOM main)
/*     */   {
/* 246 */     this._size = 4;
/* 247 */     this._free = 1;
/* 248 */     this._adapters = new DOM[4];
/* 249 */     DOMAdapter adapter = (DOMAdapter)main;
/* 250 */     this._adapters[0] = adapter;
/* 251 */     this._main = adapter;
/* 252 */     DOM dom = adapter.getDOMImpl();
/* 253 */     if ((dom instanceof DTMDefaultBase)) {
/* 254 */       this._dtmManager = ((DTMDefaultBase)dom).getManager();
/*     */     }
/*     */ 
/* 271 */     addDOMAdapter(adapter, false);
/*     */   }
/*     */ 
/*     */   public int nextMask() {
/* 275 */     return this._free;
/*     */   }
/*     */ 
/*     */   public void setupMapping(String[] names, String[] uris, int[] types, String[] namespaces)
/*     */   {
/*     */   }
/*     */ 
/*     */   public int addDOMAdapter(DOMAdapter adapter) {
/* 283 */     return addDOMAdapter(adapter, true);
/*     */   }
/*     */ 
/*     */   private int addDOMAdapter(DOMAdapter adapter, boolean indexByURI)
/*     */   {
/* 288 */     DOM dom = adapter.getDOMImpl();
/*     */ 
/* 290 */     int domNo = 1;
/* 291 */     int dtmSize = 1;
/* 292 */     SuballocatedIntVector dtmIds = null;
/* 293 */     if ((dom instanceof DTMDefaultBase)) {
/* 294 */       DTMDefaultBase dtmdb = (DTMDefaultBase)dom;
/* 295 */       dtmIds = dtmdb.getDTMIDs();
/* 296 */       dtmSize = dtmIds.size();
/* 297 */       domNo = dtmIds.elementAt(dtmSize - 1) >>> 16;
/*     */     }
/* 299 */     else if ((dom instanceof SimpleResultTreeImpl)) {
/* 300 */       SimpleResultTreeImpl simpleRTF = (SimpleResultTreeImpl)dom;
/* 301 */       domNo = simpleRTF.getDocument() >>> 16;
/*     */     }
/*     */ 
/* 304 */     if (domNo >= this._size) {
/* 305 */       int oldSize = this._size;
/*     */       do
/* 307 */         this._size *= 2;
/* 308 */       while (this._size <= domNo);
/*     */ 
/* 310 */       DOMAdapter[] newArray = new DOMAdapter[this._size];
/* 311 */       System.arraycopy(this._adapters, 0, newArray, 0, oldSize);
/* 312 */       this._adapters = newArray;
/*     */     }
/*     */ 
/* 315 */     this._free = (domNo + 1);
/*     */ 
/* 317 */     if (dtmSize == 1) {
/* 318 */       this._adapters[domNo] = adapter;
/*     */     }
/* 320 */     else if (dtmIds != null) {
/* 321 */       int domPos = 0;
/* 322 */       for (int i = dtmSize - 1; i >= 0; i--) {
/* 323 */         domPos = dtmIds.elementAt(i) >>> 16;
/* 324 */         this._adapters[domPos] = adapter;
/*     */       }
/* 326 */       domNo = domPos;
/*     */     }
/*     */ 
/* 330 */     if (indexByURI) {
/* 331 */       String uri = adapter.getDocumentURI(0);
/* 332 */       this._documents.put(uri, new Integer(domNo));
/*     */     }
/*     */ 
/* 338 */     if ((dom instanceof AdaptiveResultTreeImpl)) {
/* 339 */       AdaptiveResultTreeImpl adaptiveRTF = (AdaptiveResultTreeImpl)dom;
/* 340 */       DOM nestedDom = adaptiveRTF.getNestedDOM();
/* 341 */       if (nestedDom != null) {
/* 342 */         DOMAdapter newAdapter = new DOMAdapter(nestedDom, adapter.getNamesArray(), adapter.getUrisArray(), adapter.getTypesArray(), adapter.getNamespaceArray());
/*     */ 
/* 347 */         addDOMAdapter(newAdapter);
/*     */       }
/*     */     }
/*     */ 
/* 351 */     return domNo;
/*     */   }
/*     */ 
/*     */   public int getDocumentMask(String uri) {
/* 355 */     Integer domIdx = (Integer)this._documents.get(uri);
/* 356 */     if (domIdx == null) {
/* 357 */       return -1;
/*     */     }
/* 359 */     return domIdx.intValue();
/*     */   }
/*     */ 
/*     */   public DOM getDOMAdapter(String uri)
/*     */   {
/* 364 */     Integer domIdx = (Integer)this._documents.get(uri);
/* 365 */     if (domIdx == null) {
/* 366 */       return null;
/*     */     }
/* 368 */     return this._adapters[domIdx.intValue()];
/*     */   }
/*     */ 
/*     */   public int getDocument()
/*     */   {
/* 374 */     return this._main.getDocument();
/*     */   }
/*     */ 
/*     */   public DTMManager getDTMManager() {
/* 378 */     return this._dtmManager;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getIterator()
/*     */   {
/* 386 */     return this._main.getIterator();
/*     */   }
/*     */ 
/*     */   public String getStringValue() {
/* 390 */     return this._main.getStringValue();
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getChildren(int node) {
/* 394 */     return this._adapters[getDTMId(node)].getChildren(node);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getTypedChildren(int type) {
/* 398 */     return new AxisIterator(3, type);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getAxisIterator(int axis) {
/* 402 */     return new AxisIterator(axis, -2);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getTypedAxisIterator(int axis, int type)
/*     */   {
/* 407 */     return new AxisIterator(axis, type);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getNthDescendant(int node, int n, boolean includeself)
/*     */   {
/* 413 */     return this._adapters[getDTMId(node)].getNthDescendant(node, n, includeself);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getNodeValueIterator(DTMAxisIterator iterator, int type, String value, boolean op)
/*     */   {
/* 420 */     return new NodeValueIterator(iterator, type, value, op);
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator getNamespaceAxisIterator(int axis, int ns)
/*     */   {
/* 426 */     DTMAxisIterator iterator = this._main.getNamespaceAxisIterator(axis, ns);
/* 427 */     return iterator;
/*     */   }
/*     */ 
/*     */   public DTMAxisIterator orderNodes(DTMAxisIterator source, int node) {
/* 431 */     return this._adapters[getDTMId(node)].orderNodes(source, node);
/*     */   }
/*     */ 
/*     */   public int getExpandedTypeID(int node) {
/* 435 */     if (node != -1) {
/* 436 */       return this._adapters[(node >>> 16)].getExpandedTypeID(node);
/*     */     }
/*     */ 
/* 439 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getNamespaceType(int node)
/*     */   {
/* 444 */     return this._adapters[getDTMId(node)].getNamespaceType(node);
/*     */   }
/*     */ 
/*     */   public int getNSType(int node)
/*     */   {
/* 449 */     return this._adapters[getDTMId(node)].getNSType(node);
/*     */   }
/*     */ 
/*     */   public int getParent(int node) {
/* 453 */     if (node == -1) {
/* 454 */       return -1;
/*     */     }
/* 456 */     return this._adapters[(node >>> 16)].getParent(node);
/*     */   }
/*     */ 
/*     */   public int getAttributeNode(int type, int el) {
/* 460 */     if (el == -1) {
/* 461 */       return -1;
/*     */     }
/* 463 */     return this._adapters[(el >>> 16)].getAttributeNode(type, el);
/*     */   }
/*     */ 
/*     */   public String getNodeName(int node) {
/* 467 */     if (node == -1) {
/* 468 */       return "";
/*     */     }
/* 470 */     return this._adapters[(node >>> 16)].getNodeName(node);
/*     */   }
/*     */ 
/*     */   public String getNodeNameX(int node) {
/* 474 */     if (node == -1) {
/* 475 */       return "";
/*     */     }
/* 477 */     return this._adapters[(node >>> 16)].getNodeNameX(node);
/*     */   }
/*     */ 
/*     */   public String getNamespaceName(int node) {
/* 481 */     if (node == -1) {
/* 482 */       return "";
/*     */     }
/* 484 */     return this._adapters[(node >>> 16)].getNamespaceName(node);
/*     */   }
/*     */ 
/*     */   public String getStringValueX(int node) {
/* 488 */     if (node == -1) {
/* 489 */       return "";
/*     */     }
/* 491 */     return this._adapters[(node >>> 16)].getStringValueX(node);
/*     */   }
/*     */ 
/*     */   public void copy(int node, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/* 497 */     if (node != -1)
/* 498 */       this._adapters[(node >>> 16)].copy(node, handler);
/*     */   }
/*     */ 
/*     */   public void copy(DTMAxisIterator nodes, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/*     */     int node;
/* 506 */     while ((node = nodes.next()) != -1)
/* 507 */       this._adapters[(node >>> 16)].copy(node, handler);
/*     */   }
/*     */ 
/*     */   public String shallowCopy(int node, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/* 515 */     if (node == -1) {
/* 516 */       return "";
/*     */     }
/* 518 */     return this._adapters[(node >>> 16)].shallowCopy(node, handler);
/*     */   }
/*     */ 
/*     */   public boolean lessThan(int node1, int node2) {
/* 522 */     if (node1 == -1) {
/* 523 */       return true;
/*     */     }
/* 525 */     if (node2 == -1) {
/* 526 */       return false;
/*     */     }
/* 528 */     int dom1 = getDTMId(node1);
/* 529 */     int dom2 = getDTMId(node2);
/* 530 */     return dom1 < dom2 ? true : dom1 == dom2 ? this._adapters[dom1].lessThan(node1, node2) : false;
/*     */   }
/*     */ 
/*     */   public void characters(int textNode, SerializationHandler handler)
/*     */     throws TransletException
/*     */   {
/* 537 */     if (textNode != -1)
/* 538 */       this._adapters[(textNode >>> 16)].characters(textNode, handler);
/*     */   }
/*     */ 
/*     */   public void setFilter(StripFilter filter)
/*     */   {
/* 543 */     for (int dom = 0; dom < this._free; dom++)
/* 544 */       if (this._adapters[dom] != null)
/* 545 */         this._adapters[dom].setFilter(filter);
/*     */   }
/*     */ 
/*     */   public Node makeNode(int index)
/*     */   {
/* 551 */     if (index == -1) {
/* 552 */       return null;
/*     */     }
/* 554 */     return this._adapters[getDTMId(index)].makeNode(index);
/*     */   }
/*     */ 
/*     */   public Node makeNode(DTMAxisIterator iter)
/*     */   {
/* 559 */     return this._main.makeNode(iter);
/*     */   }
/*     */ 
/*     */   public NodeList makeNodeList(int index) {
/* 563 */     if (index == -1) {
/* 564 */       return null;
/*     */     }
/* 566 */     return this._adapters[getDTMId(index)].makeNodeList(index);
/*     */   }
/*     */ 
/*     */   public NodeList makeNodeList(DTMAxisIterator iter)
/*     */   {
/* 571 */     return this._main.makeNodeList(iter);
/*     */   }
/*     */ 
/*     */   public String getLanguage(int node) {
/* 575 */     return this._adapters[getDTMId(node)].getLanguage(node);
/*     */   }
/*     */ 
/*     */   public int getSize() {
/* 579 */     int size = 0;
/* 580 */     for (int i = 0; i < this._size; i++) {
/* 581 */       size += this._adapters[i].getSize();
/*     */     }
/* 583 */     return size;
/*     */   }
/*     */ 
/*     */   public String getDocumentURI(int node) {
/* 587 */     if (node == -1) {
/* 588 */       node = 0;
/*     */     }
/* 590 */     return this._adapters[(node >>> 16)].getDocumentURI(0);
/*     */   }
/*     */ 
/*     */   public boolean isElement(int node) {
/* 594 */     if (node == -1) {
/* 595 */       return false;
/*     */     }
/* 597 */     return this._adapters[(node >>> 16)].isElement(node);
/*     */   }
/*     */ 
/*     */   public boolean isAttribute(int node) {
/* 601 */     if (node == -1) {
/* 602 */       return false;
/*     */     }
/* 604 */     return this._adapters[(node >>> 16)].isAttribute(node);
/*     */   }
/*     */ 
/*     */   public int getDTMId(int nodeHandle)
/*     */   {
/* 609 */     if (nodeHandle == -1) {
/* 610 */       return 0;
/*     */     }
/* 612 */     int id = nodeHandle >>> 16;
/* 613 */     while ((id >= 2) && (this._adapters[id] == this._adapters[(id - 1)])) {
/* 614 */       id--;
/*     */     }
/* 616 */     return id;
/*     */   }
/*     */ 
/*     */   public DOM getDTM(int nodeHandle) {
/* 620 */     return this._adapters[getDTMId(nodeHandle)];
/*     */   }
/*     */ 
/*     */   public int getNodeIdent(int nodeHandle)
/*     */   {
/* 625 */     return this._adapters[(nodeHandle >>> 16)].getNodeIdent(nodeHandle);
/*     */   }
/*     */ 
/*     */   public int getNodeHandle(int nodeId)
/*     */   {
/* 630 */     return this._main.getNodeHandle(nodeId);
/*     */   }
/*     */ 
/*     */   public DOM getResultTreeFrag(int initSize, int rtfType)
/*     */   {
/* 635 */     return this._main.getResultTreeFrag(initSize, rtfType);
/*     */   }
/*     */ 
/*     */   public DOM getResultTreeFrag(int initSize, int rtfType, boolean addToManager)
/*     */   {
/* 640 */     return this._main.getResultTreeFrag(initSize, rtfType, addToManager);
/*     */   }
/*     */ 
/*     */   public DOM getMain()
/*     */   {
/* 645 */     return this._main;
/*     */   }
/*     */ 
/*     */   public SerializationHandler getOutputDomBuilder()
/*     */   {
/* 653 */     return this._main.getOutputDomBuilder();
/*     */   }
/*     */ 
/*     */   public String lookupNamespace(int node, String prefix)
/*     */     throws TransletException
/*     */   {
/* 659 */     return this._main.lookupNamespace(node, prefix);
/*     */   }
/*     */ 
/*     */   public String getUnparsedEntityURI(String entity)
/*     */   {
/* 664 */     return this._main.getUnparsedEntityURI(entity);
/*     */   }
/*     */ 
/*     */   public Hashtable getElementsWithIDs()
/*     */   {
/* 669 */     return this._main.getElementsWithIDs();
/*     */   }
/*     */ 
/*     */   private final class AxisIterator extends DTMAxisIteratorBase
/*     */   {
/*     */     private final int _axis;
/*     */     private final int _type;
/*     */     private DTMAxisIterator _source;
/*  67 */     private int _dtmId = -1;
/*     */ 
/*     */     public AxisIterator(int axis, int type) {
/*  70 */       this._axis = axis;
/*  71 */       this._type = type;
/*     */     }
/*     */ 
/*     */     public int next() {
/*  75 */       if (this._source == null) {
/*  76 */         return -1;
/*     */       }
/*  78 */       return this._source.next();
/*     */     }
/*     */ 
/*     */     public void setRestartable(boolean flag)
/*     */     {
/*  83 */       if (this._source != null)
/*  84 */         this._source.setRestartable(flag);
/*     */     }
/*     */ 
/*     */     public DTMAxisIterator setStartNode(int node)
/*     */     {
/*  89 */       if (node == -1) {
/*  90 */         return this;
/*     */       }
/*     */ 
/*  93 */       int dom = node >>> 16;
/*     */ 
/*  96 */       if ((this._source == null) || (this._dtmId != dom)) {
/*  97 */         if (this._type == -2)
/*  98 */           this._source = MultiDOM.this._adapters[dom].getAxisIterator(this._axis);
/*  99 */         else if (this._axis == 3)
/* 100 */           this._source = MultiDOM.this._adapters[dom].getTypedChildren(this._type);
/*     */         else {
/* 102 */           this._source = MultiDOM.this._adapters[dom].getTypedAxisIterator(this._axis, this._type);
/*     */         }
/*     */       }
/*     */ 
/* 106 */       this._dtmId = dom;
/* 107 */       this._source.setStartNode(node);
/* 108 */       return this;
/*     */     }
/*     */ 
/*     */     public DTMAxisIterator reset() {
/* 112 */       if (this._source != null) {
/* 113 */         this._source.reset();
/*     */       }
/* 115 */       return this;
/*     */     }
/*     */ 
/*     */     public int getLast() {
/* 119 */       if (this._source != null) {
/* 120 */         return this._source.getLast();
/*     */       }
/*     */ 
/* 123 */       return -1;
/*     */     }
/*     */ 
/*     */     public int getPosition()
/*     */     {
/* 128 */       if (this._source != null) {
/* 129 */         return this._source.getPosition();
/*     */       }
/*     */ 
/* 132 */       return -1;
/*     */     }
/*     */ 
/*     */     public boolean isReverse()
/*     */     {
/* 137 */       return Axis.isReverse(this._axis);
/*     */     }
/*     */ 
/*     */     public void setMark() {
/* 141 */       if (this._source != null)
/* 142 */         this._source.setMark();
/*     */     }
/*     */ 
/*     */     public void gotoMark()
/*     */     {
/* 147 */       if (this._source != null)
/* 148 */         this._source.gotoMark();
/*     */     }
/*     */ 
/*     */     public DTMAxisIterator cloneIterator()
/*     */     {
/* 153 */       AxisIterator clone = new AxisIterator(MultiDOM.this, this._axis, this._type);
/* 154 */       if (this._source != null) {
/* 155 */         clone._source = this._source.cloneIterator();
/*     */       }
/* 157 */       clone._dtmId = this._dtmId;
/* 158 */       return clone;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final class NodeValueIterator extends DTMAxisIteratorBase
/*     */   {
/*     */     private DTMAxisIterator _source;
/*     */     private String _value;
/*     */     private boolean _op;
/*     */     private final boolean _isReverse;
/* 173 */     private int _returnType = 1;
/*     */ 
/*     */     public NodeValueIterator(DTMAxisIterator source, int returnType, String value, boolean op)
/*     */     {
/* 177 */       this._source = source;
/* 178 */       this._returnType = returnType;
/* 179 */       this._value = value;
/* 180 */       this._op = op;
/* 181 */       this._isReverse = source.isReverse();
/*     */     }
/*     */ 
/*     */     public boolean isReverse() {
/* 185 */       return this._isReverse;
/*     */     }
/*     */ 
/*     */     public DTMAxisIterator cloneIterator() {
/*     */       try {
/* 190 */         NodeValueIterator clone = (NodeValueIterator)super.clone();
/* 191 */         clone._source = this._source.cloneIterator();
/* 192 */         clone.setRestartable(false);
/* 193 */         return clone.reset();
/*     */       }
/*     */       catch (CloneNotSupportedException e) {
/* 196 */         BasisLibrary.runTimeError("ITERATOR_CLONE_ERR", e.toString());
/*     */       }
/* 198 */       return null;
/*     */     }
/*     */ 
/*     */     public void setRestartable(boolean isRestartable)
/*     */     {
/* 204 */       this._isRestartable = isRestartable;
/* 205 */       this._source.setRestartable(isRestartable);
/*     */     }
/*     */ 
/*     */     public DTMAxisIterator reset() {
/* 209 */       this._source.reset();
/* 210 */       return resetPosition();
/*     */     }
/*     */ 
/*     */     public int next()
/*     */     {
/*     */       int node;
/* 216 */       while ((node = this._source.next()) != -1) {
/* 217 */         String val = MultiDOM.this.getStringValueX(node);
/* 218 */         if (this._value.equals(val) == this._op) {
/* 219 */           if (this._returnType == 0) {
/* 220 */             return returnNode(node);
/*     */           }
/* 222 */           return returnNode(MultiDOM.this.getParent(node));
/*     */         }
/*     */       }
/* 225 */       return -1;
/*     */     }
/*     */ 
/*     */     public DTMAxisIterator setStartNode(int node) {
/* 229 */       if (this._isRestartable) {
/* 230 */         this._source.setStartNode(this._startNode = node);
/* 231 */         return resetPosition();
/*     */       }
/* 233 */       return this;
/*     */     }
/*     */ 
/*     */     public void setMark() {
/* 237 */       this._source.setMark();
/*     */     }
/*     */ 
/*     */     public void gotoMark() {
/* 241 */       this._source.gotoMark();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.MultiDOM
 * JD-Core Version:    0.6.2
 */