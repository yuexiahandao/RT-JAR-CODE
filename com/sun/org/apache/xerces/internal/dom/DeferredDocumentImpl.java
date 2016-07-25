/*      */ package com.sun.org.apache.xerces.internal.dom;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Hashtable;
/*      */ import org.w3c.dom.DOMImplementation;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Node;
/*      */ 
/*      */ public class DeferredDocumentImpl extends DocumentImpl
/*      */   implements DeferredNode
/*      */ {
/*      */   static final long serialVersionUID = 5186323580749626857L;
/*      */   private static final boolean DEBUG_PRINT_REF_COUNTS = false;
/*      */   private static final boolean DEBUG_PRINT_TABLES = false;
/*      */   private static final boolean DEBUG_IDS = false;
/*      */   protected static final int CHUNK_SHIFT = 8;
/*      */   protected static final int CHUNK_SIZE = 256;
/*      */   protected static final int CHUNK_MASK = 255;
/*      */   protected static final int INITIAL_CHUNK_COUNT = 32;
/*   91 */   protected transient int fNodeCount = 0;
/*      */   protected transient int[][] fNodeType;
/*      */   protected transient Object[][] fNodeName;
/*      */   protected transient Object[][] fNodeValue;
/*      */   protected transient int[][] fNodeParent;
/*      */   protected transient int[][] fNodeLastChild;
/*      */   protected transient int[][] fNodePrevSib;
/*      */   protected transient Object[][] fNodeURI;
/*      */   protected transient int[][] fNodeExtra;
/*      */   protected transient int fIdCount;
/*      */   protected transient String[] fIdName;
/*      */   protected transient int[] fIdElement;
/*  130 */   protected boolean fNamespacesEnabled = false;
/*      */ 
/*  135 */   private final transient StringBuilder fBufferStr = new StringBuilder();
/*  136 */   private final transient ArrayList fStrChunks = new ArrayList();
/*      */ 
/* 1927 */   private static final int[] INIT_ARRAY = new int[257];
/*      */ 
/*      */   public DeferredDocumentImpl()
/*      */   {
/*  147 */     this(false);
/*      */   }
/*      */ 
/*      */   public DeferredDocumentImpl(boolean namespacesEnabled)
/*      */   {
/*  155 */     this(namespacesEnabled, false);
/*      */   }
/*      */ 
/*      */   public DeferredDocumentImpl(boolean namespaces, boolean grammarAccess)
/*      */   {
/*  160 */     super(grammarAccess);
/*      */ 
/*  162 */     needsSyncData(true);
/*  163 */     needsSyncChildren(true);
/*      */ 
/*  165 */     this.fNamespacesEnabled = namespaces;
/*      */   }
/*      */ 
/*      */   public DOMImplementation getImplementation()
/*      */   {
/*  182 */     return DeferredDOMImplementationImpl.getDOMImplementation();
/*      */   }
/*      */ 
/*      */   boolean getNamespacesEnabled()
/*      */   {
/*  187 */     return this.fNamespacesEnabled;
/*      */   }
/*      */ 
/*      */   void setNamespacesEnabled(boolean enable) {
/*  191 */     this.fNamespacesEnabled = enable;
/*      */   }
/*      */ 
/*      */   public int createDeferredDocument()
/*      */   {
/*  198 */     int nodeIndex = createNode((short)9);
/*  199 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int createDeferredDocumentType(String rootElementName, String publicId, String systemId)
/*      */   {
/*  207 */     int nodeIndex = createNode((short)10);
/*  208 */     int chunk = nodeIndex >> 8;
/*  209 */     int index = nodeIndex & 0xFF;
/*      */ 
/*  212 */     setChunkValue(this.fNodeName, rootElementName, chunk, index);
/*  213 */     setChunkValue(this.fNodeValue, publicId, chunk, index);
/*  214 */     setChunkValue(this.fNodeURI, systemId, chunk, index);
/*      */ 
/*  217 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public void setInternalSubset(int doctypeIndex, String subset)
/*      */   {
/*  222 */     int chunk = doctypeIndex >> 8;
/*  223 */     int index = doctypeIndex & 0xFF;
/*      */ 
/*  226 */     int extraDataIndex = createNode((short)10);
/*  227 */     int echunk = extraDataIndex >> 8;
/*  228 */     int eindex = extraDataIndex & 0xFF;
/*  229 */     setChunkIndex(this.fNodeExtra, extraDataIndex, chunk, index);
/*  230 */     setChunkValue(this.fNodeValue, subset, echunk, eindex);
/*      */   }
/*      */ 
/*      */   public int createDeferredNotation(String notationName, String publicId, String systemId, String baseURI)
/*      */   {
/*  238 */     int nodeIndex = createNode((short)12);
/*  239 */     int chunk = nodeIndex >> 8;
/*  240 */     int index = nodeIndex & 0xFF;
/*      */ 
/*  244 */     int extraDataIndex = createNode((short)12);
/*  245 */     int echunk = extraDataIndex >> 8;
/*  246 */     int eindex = extraDataIndex & 0xFF;
/*      */ 
/*  249 */     setChunkValue(this.fNodeName, notationName, chunk, index);
/*  250 */     setChunkValue(this.fNodeValue, publicId, chunk, index);
/*  251 */     setChunkValue(this.fNodeURI, systemId, chunk, index);
/*      */ 
/*  254 */     setChunkIndex(this.fNodeExtra, extraDataIndex, chunk, index);
/*  255 */     setChunkValue(this.fNodeName, baseURI, echunk, eindex);
/*      */ 
/*  258 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int createDeferredEntity(String entityName, String publicId, String systemId, String notationName, String baseURI)
/*      */   {
/*  267 */     int nodeIndex = createNode((short)6);
/*  268 */     int chunk = nodeIndex >> 8;
/*  269 */     int index = nodeIndex & 0xFF;
/*      */ 
/*  272 */     int extraDataIndex = createNode((short)6);
/*  273 */     int echunk = extraDataIndex >> 8;
/*  274 */     int eindex = extraDataIndex & 0xFF;
/*      */ 
/*  277 */     setChunkValue(this.fNodeName, entityName, chunk, index);
/*  278 */     setChunkValue(this.fNodeValue, publicId, chunk, index);
/*  279 */     setChunkValue(this.fNodeURI, systemId, chunk, index);
/*  280 */     setChunkIndex(this.fNodeExtra, extraDataIndex, chunk, index);
/*      */ 
/*  283 */     setChunkValue(this.fNodeName, notationName, echunk, eindex);
/*      */ 
/*  285 */     setChunkValue(this.fNodeValue, null, echunk, eindex);
/*      */ 
/*  287 */     setChunkValue(this.fNodeURI, null, echunk, eindex);
/*      */ 
/*  290 */     int extraDataIndex2 = createNode((short)6);
/*  291 */     int echunk2 = extraDataIndex2 >> 8;
/*  292 */     int eindex2 = extraDataIndex2 & 0xFF;
/*      */ 
/*  294 */     setChunkIndex(this.fNodeExtra, extraDataIndex2, echunk, eindex);
/*      */ 
/*  297 */     setChunkValue(this.fNodeName, baseURI, echunk2, eindex2);
/*      */ 
/*  300 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public String getDeferredEntityBaseURI(int entityIndex)
/*      */   {
/*  305 */     if (entityIndex != -1) {
/*  306 */       int extraDataIndex = getNodeExtra(entityIndex, false);
/*  307 */       extraDataIndex = getNodeExtra(extraDataIndex, false);
/*  308 */       return getNodeName(extraDataIndex, false);
/*      */     }
/*  310 */     return null;
/*      */   }
/*      */ 
/*      */   public void setEntityInfo(int currentEntityDecl, String version, String encoding)
/*      */   {
/*  316 */     int eNodeIndex = getNodeExtra(currentEntityDecl, false);
/*  317 */     if (eNodeIndex != -1) {
/*  318 */       int echunk = eNodeIndex >> 8;
/*  319 */       int eindex = eNodeIndex & 0xFF;
/*  320 */       setChunkValue(this.fNodeValue, version, echunk, eindex);
/*  321 */       setChunkValue(this.fNodeURI, encoding, echunk, eindex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setTypeInfo(int elementNodeIndex, Object type)
/*      */   {
/*  327 */     int elementChunk = elementNodeIndex >> 8;
/*  328 */     int elementIndex = elementNodeIndex & 0xFF;
/*  329 */     setChunkValue(this.fNodeValue, type, elementChunk, elementIndex);
/*      */   }
/*      */ 
/*      */   public void setInputEncoding(int currentEntityDecl, String value)
/*      */   {
/*  342 */     int nodeIndex = getNodeExtra(currentEntityDecl, false);
/*      */ 
/*  344 */     int extraDataIndex = getNodeExtra(nodeIndex, false);
/*      */ 
/*  346 */     int echunk = extraDataIndex >> 8;
/*  347 */     int eindex = extraDataIndex & 0xFF;
/*      */ 
/*  349 */     setChunkValue(this.fNodeValue, value, echunk, eindex);
/*      */   }
/*      */ 
/*      */   public int createDeferredEntityReference(String name, String baseURI)
/*      */   {
/*  357 */     int nodeIndex = createNode((short)5);
/*  358 */     int chunk = nodeIndex >> 8;
/*  359 */     int index = nodeIndex & 0xFF;
/*  360 */     setChunkValue(this.fNodeName, name, chunk, index);
/*  361 */     setChunkValue(this.fNodeValue, baseURI, chunk, index);
/*      */ 
/*  364 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public int createDeferredElement(String elementURI, String elementName, Object type)
/*      */   {
/*  377 */     int elementNodeIndex = createNode((short)1);
/*  378 */     int elementChunk = elementNodeIndex >> 8;
/*  379 */     int elementIndex = elementNodeIndex & 0xFF;
/*  380 */     setChunkValue(this.fNodeName, elementName, elementChunk, elementIndex);
/*  381 */     setChunkValue(this.fNodeURI, elementURI, elementChunk, elementIndex);
/*  382 */     setChunkValue(this.fNodeValue, type, elementChunk, elementIndex);
/*      */ 
/*  385 */     return elementNodeIndex;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public int createDeferredElement(String elementName)
/*      */   {
/*  394 */     return createDeferredElement(null, elementName);
/*      */   }
/*      */ 
/*      */   public int createDeferredElement(String elementURI, String elementName)
/*      */   {
/*  403 */     int elementNodeIndex = createNode((short)1);
/*  404 */     int elementChunk = elementNodeIndex >> 8;
/*  405 */     int elementIndex = elementNodeIndex & 0xFF;
/*  406 */     setChunkValue(this.fNodeName, elementName, elementChunk, elementIndex);
/*  407 */     setChunkValue(this.fNodeURI, elementURI, elementChunk, elementIndex);
/*      */ 
/*  410 */     return elementNodeIndex;
/*      */   }
/*      */ 
/*      */   public int setDeferredAttribute(int elementNodeIndex, String attrName, String attrURI, String attrValue, boolean specified, boolean id, Object type)
/*      */   {
/*  435 */     int attrNodeIndex = createDeferredAttribute(attrName, attrURI, attrValue, specified);
/*  436 */     int attrChunk = attrNodeIndex >> 8;
/*  437 */     int attrIndex = attrNodeIndex & 0xFF;
/*      */ 
/*  439 */     setChunkIndex(this.fNodeParent, elementNodeIndex, attrChunk, attrIndex);
/*      */ 
/*  441 */     int elementChunk = elementNodeIndex >> 8;
/*  442 */     int elementIndex = elementNodeIndex & 0xFF;
/*      */ 
/*  445 */     int lastAttrNodeIndex = getChunkIndex(this.fNodeExtra, elementChunk, elementIndex);
/*  446 */     if (lastAttrNodeIndex != 0)
/*      */     {
/*  448 */       setChunkIndex(this.fNodePrevSib, lastAttrNodeIndex, attrChunk, attrIndex);
/*      */     }
/*      */ 
/*  451 */     setChunkIndex(this.fNodeExtra, attrNodeIndex, elementChunk, elementIndex);
/*      */ 
/*  453 */     int extra = getChunkIndex(this.fNodeExtra, attrChunk, attrIndex);
/*  454 */     if (id) {
/*  455 */       extra |= 512;
/*  456 */       setChunkIndex(this.fNodeExtra, extra, attrChunk, attrIndex);
/*  457 */       String value = getChunkValue(this.fNodeValue, attrChunk, attrIndex);
/*  458 */       putIdentifier(value, elementNodeIndex);
/*      */     }
/*      */ 
/*  461 */     if (type != null) {
/*  462 */       int extraDataIndex = createNode((short)20);
/*  463 */       int echunk = extraDataIndex >> 8;
/*  464 */       int eindex = extraDataIndex & 0xFF;
/*      */ 
/*  466 */       setChunkIndex(this.fNodeLastChild, extraDataIndex, attrChunk, attrIndex);
/*  467 */       setChunkValue(this.fNodeValue, type, echunk, eindex);
/*      */     }
/*      */ 
/*  471 */     return attrNodeIndex;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public int setDeferredAttribute(int elementNodeIndex, String attrName, String attrURI, String attrValue, boolean specified)
/*      */   {
/*  482 */     int attrNodeIndex = createDeferredAttribute(attrName, attrURI, attrValue, specified);
/*      */ 
/*  484 */     int attrChunk = attrNodeIndex >> 8;
/*  485 */     int attrIndex = attrNodeIndex & 0xFF;
/*      */ 
/*  487 */     setChunkIndex(this.fNodeParent, elementNodeIndex, attrChunk, attrIndex);
/*      */ 
/*  489 */     int elementChunk = elementNodeIndex >> 8;
/*  490 */     int elementIndex = elementNodeIndex & 0xFF;
/*      */ 
/*  493 */     int lastAttrNodeIndex = getChunkIndex(this.fNodeExtra, elementChunk, elementIndex);
/*      */ 
/*  495 */     if (lastAttrNodeIndex != 0)
/*      */     {
/*  497 */       setChunkIndex(this.fNodePrevSib, lastAttrNodeIndex, attrChunk, attrIndex);
/*      */     }
/*      */ 
/*  501 */     setChunkIndex(this.fNodeExtra, attrNodeIndex, elementChunk, elementIndex);
/*      */ 
/*  505 */     return attrNodeIndex;
/*      */   }
/*      */ 
/*      */   public int createDeferredAttribute(String attrName, String attrValue, boolean specified)
/*      */   {
/*  512 */     return createDeferredAttribute(attrName, null, attrValue, specified);
/*      */   }
/*      */ 
/*      */   public int createDeferredAttribute(String attrName, String attrURI, String attrValue, boolean specified)
/*      */   {
/*  520 */     int nodeIndex = createNode((short)2);
/*  521 */     int chunk = nodeIndex >> 8;
/*  522 */     int index = nodeIndex & 0xFF;
/*  523 */     setChunkValue(this.fNodeName, attrName, chunk, index);
/*  524 */     setChunkValue(this.fNodeURI, attrURI, chunk, index);
/*  525 */     setChunkValue(this.fNodeValue, attrValue, chunk, index);
/*  526 */     int extra = specified ? 32 : 0;
/*  527 */     setChunkIndex(this.fNodeExtra, extra, chunk, index);
/*      */ 
/*  530 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int createDeferredElementDefinition(String elementName)
/*      */   {
/*  538 */     int nodeIndex = createNode((short)21);
/*  539 */     int chunk = nodeIndex >> 8;
/*  540 */     int index = nodeIndex & 0xFF;
/*  541 */     setChunkValue(this.fNodeName, elementName, chunk, index);
/*      */ 
/*  544 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int createDeferredTextNode(String data, boolean ignorableWhitespace)
/*      */   {
/*  553 */     int nodeIndex = createNode((short)3);
/*  554 */     int chunk = nodeIndex >> 8;
/*  555 */     int index = nodeIndex & 0xFF;
/*  556 */     setChunkValue(this.fNodeValue, data, chunk, index);
/*      */ 
/*  558 */     setChunkIndex(this.fNodeExtra, ignorableWhitespace ? 1 : 0, chunk, index);
/*      */ 
/*  561 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int createDeferredCDATASection(String data)
/*      */   {
/*  569 */     int nodeIndex = createNode((short)4);
/*  570 */     int chunk = nodeIndex >> 8;
/*  571 */     int index = nodeIndex & 0xFF;
/*  572 */     setChunkValue(this.fNodeValue, data, chunk, index);
/*      */ 
/*  575 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int createDeferredProcessingInstruction(String target, String data)
/*      */   {
/*  583 */     int nodeIndex = createNode((short)7);
/*  584 */     int chunk = nodeIndex >> 8;
/*  585 */     int index = nodeIndex & 0xFF;
/*  586 */     setChunkValue(this.fNodeName, target, chunk, index);
/*  587 */     setChunkValue(this.fNodeValue, data, chunk, index);
/*      */ 
/*  589 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int createDeferredComment(String data)
/*      */   {
/*  597 */     int nodeIndex = createNode((short)8);
/*  598 */     int chunk = nodeIndex >> 8;
/*  599 */     int index = nodeIndex & 0xFF;
/*  600 */     setChunkValue(this.fNodeValue, data, chunk, index);
/*      */ 
/*  603 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int cloneNode(int nodeIndex, boolean deep)
/*      */   {
/*  612 */     int nchunk = nodeIndex >> 8;
/*  613 */     int nindex = nodeIndex & 0xFF;
/*  614 */     int nodeType = this.fNodeType[nchunk][nindex];
/*  615 */     int cloneIndex = createNode((short)nodeType);
/*  616 */     int cchunk = cloneIndex >> 8;
/*  617 */     int cindex = cloneIndex & 0xFF;
/*  618 */     setChunkValue(this.fNodeName, this.fNodeName[nchunk][nindex], cchunk, cindex);
/*  619 */     setChunkValue(this.fNodeValue, this.fNodeValue[nchunk][nindex], cchunk, cindex);
/*  620 */     setChunkValue(this.fNodeURI, this.fNodeURI[nchunk][nindex], cchunk, cindex);
/*  621 */     int extraIndex = this.fNodeExtra[nchunk][nindex];
/*  622 */     if (extraIndex != -1) {
/*  623 */       if ((nodeType != 2) && (nodeType != 3)) {
/*  624 */         extraIndex = cloneNode(extraIndex, false);
/*      */       }
/*  626 */       setChunkIndex(this.fNodeExtra, extraIndex, cchunk, cindex);
/*      */     }
/*      */ 
/*  630 */     if (deep) {
/*  631 */       int prevIndex = -1;
/*  632 */       int childIndex = getLastChild(nodeIndex, false);
/*  633 */       while (childIndex != -1) {
/*  634 */         int clonedChildIndex = cloneNode(childIndex, deep);
/*  635 */         insertBefore(cloneIndex, clonedChildIndex, prevIndex);
/*  636 */         prevIndex = clonedChildIndex;
/*  637 */         childIndex = getRealPrevSibling(childIndex, false);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  644 */     return cloneIndex;
/*      */   }
/*      */ 
/*      */   public void appendChild(int parentIndex, int childIndex)
/*      */   {
/*  652 */     int pchunk = parentIndex >> 8;
/*  653 */     int pindex = parentIndex & 0xFF;
/*  654 */     int cchunk = childIndex >> 8;
/*  655 */     int cindex = childIndex & 0xFF;
/*  656 */     setChunkIndex(this.fNodeParent, parentIndex, cchunk, cindex);
/*      */ 
/*  659 */     int olast = getChunkIndex(this.fNodeLastChild, pchunk, pindex);
/*  660 */     setChunkIndex(this.fNodePrevSib, olast, cchunk, cindex);
/*      */ 
/*  663 */     setChunkIndex(this.fNodeLastChild, childIndex, pchunk, pindex);
/*      */   }
/*      */ 
/*      */   public int setAttributeNode(int elemIndex, int attrIndex)
/*      */   {
/*  670 */     int echunk = elemIndex >> 8;
/*  671 */     int eindex = elemIndex & 0xFF;
/*  672 */     int achunk = attrIndex >> 8;
/*  673 */     int aindex = attrIndex & 0xFF;
/*      */ 
/*  676 */     String attrName = getChunkValue(this.fNodeName, achunk, aindex);
/*  677 */     int oldAttrIndex = getChunkIndex(this.fNodeExtra, echunk, eindex);
/*  678 */     int nextIndex = -1;
/*  679 */     int oachunk = -1;
/*  680 */     int oaindex = -1;
/*  681 */     while (oldAttrIndex != -1) {
/*  682 */       oachunk = oldAttrIndex >> 8;
/*  683 */       oaindex = oldAttrIndex & 0xFF;
/*  684 */       String oldAttrName = getChunkValue(this.fNodeName, oachunk, oaindex);
/*  685 */       if (oldAttrName.equals(attrName)) {
/*      */         break;
/*      */       }
/*  688 */       nextIndex = oldAttrIndex;
/*  689 */       oldAttrIndex = getChunkIndex(this.fNodePrevSib, oachunk, oaindex);
/*      */     }
/*      */ 
/*  693 */     if (oldAttrIndex != -1)
/*      */     {
/*  696 */       int prevIndex = getChunkIndex(this.fNodePrevSib, oachunk, oaindex);
/*  697 */       if (nextIndex == -1) {
/*  698 */         setChunkIndex(this.fNodeExtra, prevIndex, echunk, eindex);
/*      */       }
/*      */       else {
/*  701 */         int pchunk = nextIndex >> 8;
/*  702 */         int pindex = nextIndex & 0xFF;
/*  703 */         setChunkIndex(this.fNodePrevSib, prevIndex, pchunk, pindex);
/*      */       }
/*      */ 
/*  707 */       clearChunkIndex(this.fNodeType, oachunk, oaindex);
/*  708 */       clearChunkValue(this.fNodeName, oachunk, oaindex);
/*  709 */       clearChunkValue(this.fNodeValue, oachunk, oaindex);
/*  710 */       clearChunkIndex(this.fNodeParent, oachunk, oaindex);
/*  711 */       clearChunkIndex(this.fNodePrevSib, oachunk, oaindex);
/*  712 */       int attrTextIndex = clearChunkIndex(this.fNodeLastChild, oachunk, oaindex);
/*      */ 
/*  714 */       int atchunk = attrTextIndex >> 8;
/*  715 */       int atindex = attrTextIndex & 0xFF;
/*  716 */       clearChunkIndex(this.fNodeType, atchunk, atindex);
/*  717 */       clearChunkValue(this.fNodeValue, atchunk, atindex);
/*  718 */       clearChunkIndex(this.fNodeParent, atchunk, atindex);
/*  719 */       clearChunkIndex(this.fNodeLastChild, atchunk, atindex);
/*      */     }
/*      */ 
/*  723 */     int prevIndex = getChunkIndex(this.fNodeExtra, echunk, eindex);
/*  724 */     setChunkIndex(this.fNodeExtra, attrIndex, echunk, eindex);
/*  725 */     setChunkIndex(this.fNodePrevSib, prevIndex, achunk, aindex);
/*      */ 
/*  728 */     return oldAttrIndex;
/*      */   }
/*      */ 
/*      */   public void setIdAttributeNode(int elemIndex, int attrIndex)
/*      */   {
/*  736 */     int chunk = attrIndex >> 8;
/*  737 */     int index = attrIndex & 0xFF;
/*  738 */     int extra = getChunkIndex(this.fNodeExtra, chunk, index);
/*  739 */     extra |= 512;
/*  740 */     setChunkIndex(this.fNodeExtra, extra, chunk, index);
/*      */ 
/*  742 */     String value = getChunkValue(this.fNodeValue, chunk, index);
/*  743 */     putIdentifier(value, elemIndex);
/*      */   }
/*      */ 
/*      */   public void setIdAttribute(int attrIndex)
/*      */   {
/*  750 */     int chunk = attrIndex >> 8;
/*  751 */     int index = attrIndex & 0xFF;
/*  752 */     int extra = getChunkIndex(this.fNodeExtra, chunk, index);
/*  753 */     extra |= 512;
/*  754 */     setChunkIndex(this.fNodeExtra, extra, chunk, index);
/*      */   }
/*      */ 
/*      */   public int insertBefore(int parentIndex, int newChildIndex, int refChildIndex)
/*      */   {
/*  760 */     if (refChildIndex == -1) {
/*  761 */       appendChild(parentIndex, newChildIndex);
/*  762 */       return newChildIndex;
/*      */     }
/*      */ 
/*  765 */     int nchunk = newChildIndex >> 8;
/*  766 */     int nindex = newChildIndex & 0xFF;
/*  767 */     int rchunk = refChildIndex >> 8;
/*  768 */     int rindex = refChildIndex & 0xFF;
/*  769 */     int previousIndex = getChunkIndex(this.fNodePrevSib, rchunk, rindex);
/*  770 */     setChunkIndex(this.fNodePrevSib, newChildIndex, rchunk, rindex);
/*  771 */     setChunkIndex(this.fNodePrevSib, previousIndex, nchunk, nindex);
/*      */ 
/*  773 */     return newChildIndex;
/*      */   }
/*      */ 
/*      */   public void setAsLastChild(int parentIndex, int childIndex)
/*      */   {
/*  779 */     int pchunk = parentIndex >> 8;
/*  780 */     int pindex = parentIndex & 0xFF;
/*  781 */     setChunkIndex(this.fNodeLastChild, childIndex, pchunk, pindex);
/*      */   }
/*      */ 
/*      */   public int getParentNode(int nodeIndex)
/*      */   {
/*  789 */     return getParentNode(nodeIndex, false);
/*      */   }
/*      */ 
/*      */   public int getParentNode(int nodeIndex, boolean free)
/*      */   {
/*  798 */     if (nodeIndex == -1) {
/*  799 */       return -1;
/*      */     }
/*      */ 
/*  802 */     int chunk = nodeIndex >> 8;
/*  803 */     int index = nodeIndex & 0xFF;
/*  804 */     return free ? clearChunkIndex(this.fNodeParent, chunk, index) : getChunkIndex(this.fNodeParent, chunk, index);
/*      */   }
/*      */ 
/*      */   public int getLastChild(int nodeIndex)
/*      */   {
/*  811 */     return getLastChild(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public int getLastChild(int nodeIndex, boolean free)
/*      */   {
/*  820 */     if (nodeIndex == -1) {
/*  821 */       return -1;
/*      */     }
/*      */ 
/*  824 */     int chunk = nodeIndex >> 8;
/*  825 */     int index = nodeIndex & 0xFF;
/*  826 */     return free ? clearChunkIndex(this.fNodeLastChild, chunk, index) : getChunkIndex(this.fNodeLastChild, chunk, index);
/*      */   }
/*      */ 
/*      */   public int getPrevSibling(int nodeIndex)
/*      */   {
/*  836 */     return getPrevSibling(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public int getPrevSibling(int nodeIndex, boolean free)
/*      */   {
/*  845 */     if (nodeIndex == -1) {
/*  846 */       return -1;
/*      */     }
/*      */ 
/*  849 */     int chunk = nodeIndex >> 8;
/*  850 */     int index = nodeIndex & 0xFF;
/*  851 */     int type = getChunkIndex(this.fNodeType, chunk, index);
/*  852 */     if (type == 3) {
/*      */       do {
/*  854 */         nodeIndex = getChunkIndex(this.fNodePrevSib, chunk, index);
/*  855 */         if (nodeIndex == -1) {
/*      */           break;
/*      */         }
/*  858 */         chunk = nodeIndex >> 8;
/*  859 */         index = nodeIndex & 0xFF;
/*  860 */         type = getChunkIndex(this.fNodeType, chunk, index);
/*  861 */       }while (type == 3);
/*      */     }
/*      */     else {
/*  864 */       nodeIndex = getChunkIndex(this.fNodePrevSib, chunk, index);
/*      */     }
/*      */ 
/*  867 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int getRealPrevSibling(int nodeIndex)
/*      */   {
/*  877 */     return getRealPrevSibling(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public int getRealPrevSibling(int nodeIndex, boolean free)
/*      */   {
/*  886 */     if (nodeIndex == -1) {
/*  887 */       return -1;
/*      */     }
/*      */ 
/*  890 */     int chunk = nodeIndex >> 8;
/*  891 */     int index = nodeIndex & 0xFF;
/*  892 */     return free ? clearChunkIndex(this.fNodePrevSib, chunk, index) : getChunkIndex(this.fNodePrevSib, chunk, index);
/*      */   }
/*      */ 
/*      */   public int lookupElementDefinition(String elementName)
/*      */   {
/*  904 */     if (this.fNodeCount > 1)
/*      */     {
/*  907 */       int docTypeIndex = -1;
/*  908 */       int nchunk = 0;
/*  909 */       int nindex = 0;
/*  910 */       for (int index = getChunkIndex(this.fNodeLastChild, nchunk, nindex); 
/*  911 */         index != -1; 
/*  912 */         index = getChunkIndex(this.fNodePrevSib, nchunk, nindex))
/*      */       {
/*  914 */         nchunk = index >> 8;
/*  915 */         nindex = index & 0xFF;
/*  916 */         if (getChunkIndex(this.fNodeType, nchunk, nindex) == 10) {
/*  917 */           docTypeIndex = index;
/*  918 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  923 */       if (docTypeIndex == -1) {
/*  924 */         return -1;
/*      */       }
/*  926 */       nchunk = docTypeIndex >> 8;
/*  927 */       nindex = docTypeIndex & 0xFF;
/*  928 */       for (int index = getChunkIndex(this.fNodeLastChild, nchunk, nindex); 
/*  929 */         index != -1; 
/*  930 */         index = getChunkIndex(this.fNodePrevSib, nchunk, nindex))
/*      */       {
/*  932 */         nchunk = index >> 8;
/*  933 */         nindex = index & 0xFF;
/*  934 */         if ((getChunkIndex(this.fNodeType, nchunk, nindex) == 21) && (getChunkValue(this.fNodeName, nchunk, nindex) == elementName))
/*      */         {
/*  937 */           return index;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  942 */     return -1;
/*      */   }
/*      */ 
/*      */   public DeferredNode getNodeObject(int nodeIndex)
/*      */   {
/*  950 */     if (nodeIndex == -1) {
/*  951 */       return null;
/*      */     }
/*      */ 
/*  955 */     int chunk = nodeIndex >> 8;
/*  956 */     int index = nodeIndex & 0xFF;
/*  957 */     int type = getChunkIndex(this.fNodeType, chunk, index);
/*  958 */     if ((type != 3) && (type != 4)) {
/*  959 */       clearChunkIndex(this.fNodeType, chunk, index);
/*      */     }
/*      */ 
/*  963 */     DeferredNode node = null;
/*  964 */     switch (type)
/*      */     {
/*      */     case 2:
/*  971 */       if (this.fNamespacesEnabled)
/*  972 */         node = new DeferredAttrNSImpl(this, nodeIndex);
/*      */       else {
/*  974 */         node = new DeferredAttrImpl(this, nodeIndex);
/*      */       }
/*  976 */       break;
/*      */     case 4:
/*  980 */       node = new DeferredCDATASectionImpl(this, nodeIndex);
/*  981 */       break;
/*      */     case 8:
/*  985 */       node = new DeferredCommentImpl(this, nodeIndex);
/*  986 */       break;
/*      */     case 9:
/*  998 */       node = this;
/*  999 */       break;
/*      */     case 10:
/* 1003 */       node = new DeferredDocumentTypeImpl(this, nodeIndex);
/*      */ 
/* 1005 */       this.docType = ((DocumentTypeImpl)node);
/* 1006 */       break;
/*      */     case 1:
/* 1016 */       if (this.fNamespacesEnabled)
/* 1017 */         node = new DeferredElementNSImpl(this, nodeIndex);
/*      */       else {
/* 1019 */         node = new DeferredElementImpl(this, nodeIndex);
/*      */       }
/*      */ 
/* 1024 */       if (this.fIdElement != null) {
/* 1025 */         int idIndex = binarySearch(this.fIdElement, 0, this.fIdCount - 1, nodeIndex);
/*      */ 
/* 1027 */         while (idIndex != -1)
/*      */         {
/* 1036 */           String name = this.fIdName[idIndex];
/* 1037 */           if (name != null)
/*      */           {
/* 1042 */             putIdentifier0(name, (Element)node);
/* 1043 */             this.fIdName[idIndex] = null;
/*      */           }
/*      */ 
/* 1048 */           if ((idIndex + 1 < this.fIdCount) && (this.fIdElement[(idIndex + 1)] == nodeIndex))
/*      */           {
/* 1050 */             idIndex++;
/*      */           }
/*      */           else
/* 1053 */             idIndex = -1;
/*      */         }
/*      */       }
/* 1056 */       break;
/*      */     case 6:
/* 1061 */       node = new DeferredEntityImpl(this, nodeIndex);
/* 1062 */       break;
/*      */     case 5:
/* 1066 */       node = new DeferredEntityReferenceImpl(this, nodeIndex);
/* 1067 */       break;
/*      */     case 12:
/* 1071 */       node = new DeferredNotationImpl(this, nodeIndex);
/* 1072 */       break;
/*      */     case 7:
/* 1076 */       node = new DeferredProcessingInstructionImpl(this, nodeIndex);
/* 1077 */       break;
/*      */     case 3:
/* 1081 */       node = new DeferredTextImpl(this, nodeIndex);
/* 1082 */       break;
/*      */     case 21:
/* 1090 */       node = new DeferredElementDefinitionImpl(this, nodeIndex);
/* 1091 */       break;
/*      */     case 11:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     default:
/* 1095 */       throw new IllegalArgumentException("type: " + type);
/*      */     }
/*      */ 
/* 1101 */     if (node != null) {
/* 1102 */       return node;
/*      */     }
/*      */ 
/* 1106 */     throw new IllegalArgumentException();
/*      */   }
/*      */ 
/*      */   public String getNodeName(int nodeIndex)
/*      */   {
/* 1112 */     return getNodeName(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public String getNodeName(int nodeIndex, boolean free)
/*      */   {
/* 1121 */     if (nodeIndex == -1) {
/* 1122 */       return null;
/*      */     }
/*      */ 
/* 1125 */     int chunk = nodeIndex >> 8;
/* 1126 */     int index = nodeIndex & 0xFF;
/* 1127 */     return free ? clearChunkValue(this.fNodeName, chunk, index) : getChunkValue(this.fNodeName, chunk, index);
/*      */   }
/*      */ 
/*      */   public String getNodeValueString(int nodeIndex)
/*      */   {
/* 1134 */     return getNodeValueString(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public String getNodeValueString(int nodeIndex, boolean free)
/*      */   {
/* 1143 */     if (nodeIndex == -1) {
/* 1144 */       return null;
/*      */     }
/*      */ 
/* 1147 */     int chunk = nodeIndex >> 8;
/* 1148 */     int index = nodeIndex & 0xFF;
/* 1149 */     String value = free ? clearChunkValue(this.fNodeValue, chunk, index) : getChunkValue(this.fNodeValue, chunk, index);
/*      */ 
/* 1151 */     if (value == null) {
/* 1152 */       return null;
/*      */     }
/*      */ 
/* 1155 */     int type = getChunkIndex(this.fNodeType, chunk, index);
/* 1156 */     if (type == 3) {
/* 1157 */       int prevSib = getRealPrevSibling(nodeIndex);
/* 1158 */       if ((prevSib != -1) && (getNodeType(prevSib, false) == 3))
/*      */       {
/* 1163 */         this.fStrChunks.add(value);
/*      */         do
/*      */         {
/* 1167 */           chunk = prevSib >> 8;
/* 1168 */           index = prevSib & 0xFF;
/* 1169 */           value = getChunkValue(this.fNodeValue, chunk, index);
/* 1170 */           this.fStrChunks.add(value);
/* 1171 */           prevSib = getChunkIndex(this.fNodePrevSib, chunk, index);
/* 1172 */         }while ((prevSib != -1) && 
/* 1175 */           (getNodeType(prevSib, false) == 3));
/*      */ 
/* 1177 */         int chunkCount = this.fStrChunks.size();
/*      */ 
/* 1180 */         for (int i = chunkCount - 1; i >= 0; i--) {
/* 1181 */           this.fBufferStr.append((String)this.fStrChunks.get(i));
/*      */         }
/*      */ 
/* 1184 */         value = this.fBufferStr.toString();
/* 1185 */         this.fStrChunks.clear();
/* 1186 */         this.fBufferStr.setLength(0);
/* 1187 */         return value;
/*      */       }
/*      */     }
/* 1190 */     else if (type == 4)
/*      */     {
/* 1192 */       int child = getLastChild(nodeIndex, false);
/* 1193 */       if (child != -1)
/*      */       {
/* 1195 */         this.fBufferStr.append(value);
/* 1196 */         while (child != -1)
/*      */         {
/* 1199 */           chunk = child >> 8;
/* 1200 */           index = child & 0xFF;
/* 1201 */           value = getChunkValue(this.fNodeValue, chunk, index);
/* 1202 */           this.fStrChunks.add(value);
/* 1203 */           child = getChunkIndex(this.fNodePrevSib, chunk, index);
/*      */         }
/*      */ 
/* 1206 */         for (int i = this.fStrChunks.size() - 1; i >= 0; i--) {
/* 1207 */           this.fBufferStr.append((String)this.fStrChunks.get(i));
/*      */         }
/*      */ 
/* 1210 */         value = this.fBufferStr.toString();
/* 1211 */         this.fStrChunks.clear();
/* 1212 */         this.fBufferStr.setLength(0);
/* 1213 */         return value;
/*      */       }
/*      */     }
/*      */ 
/* 1217 */     return value;
/*      */   }
/*      */ 
/*      */   public String getNodeValue(int nodeIndex)
/*      */   {
/* 1225 */     return getNodeValue(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public Object getTypeInfo(int nodeIndex)
/*      */   {
/* 1234 */     if (nodeIndex == -1) {
/* 1235 */       return null;
/*      */     }
/*      */ 
/* 1238 */     int chunk = nodeIndex >> 8;
/* 1239 */     int index = nodeIndex & 0xFF;
/*      */ 
/* 1242 */     Object value = this.fNodeValue[chunk] != null ? this.fNodeValue[chunk][index] : null;
/* 1243 */     if (value != null) {
/* 1244 */       this.fNodeValue[chunk][index] = null;
/* 1245 */       RefCount c = (RefCount)this.fNodeValue[chunk][256];
/* 1246 */       c.fCount -= 1;
/* 1247 */       if (c.fCount == 0) {
/* 1248 */         this.fNodeValue[chunk] = null;
/*      */       }
/*      */     }
/* 1251 */     return value;
/*      */   }
/*      */ 
/*      */   public String getNodeValue(int nodeIndex, boolean free)
/*      */   {
/* 1260 */     if (nodeIndex == -1) {
/* 1261 */       return null;
/*      */     }
/*      */ 
/* 1264 */     int chunk = nodeIndex >> 8;
/* 1265 */     int index = nodeIndex & 0xFF;
/* 1266 */     return free ? clearChunkValue(this.fNodeValue, chunk, index) : getChunkValue(this.fNodeValue, chunk, index);
/*      */   }
/*      */ 
/*      */   public int getNodeExtra(int nodeIndex)
/*      */   {
/* 1276 */     return getNodeExtra(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public int getNodeExtra(int nodeIndex, boolean free)
/*      */   {
/* 1285 */     if (nodeIndex == -1) {
/* 1286 */       return -1;
/*      */     }
/*      */ 
/* 1289 */     int chunk = nodeIndex >> 8;
/* 1290 */     int index = nodeIndex & 0xFF;
/* 1291 */     return free ? clearChunkIndex(this.fNodeExtra, chunk, index) : getChunkIndex(this.fNodeExtra, chunk, index);
/*      */   }
/*      */ 
/*      */   public short getNodeType(int nodeIndex)
/*      */   {
/* 1298 */     return getNodeType(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public short getNodeType(int nodeIndex, boolean free)
/*      */   {
/* 1307 */     if (nodeIndex == -1) {
/* 1308 */       return -1;
/*      */     }
/*      */ 
/* 1311 */     int chunk = nodeIndex >> 8;
/* 1312 */     int index = nodeIndex & 0xFF;
/* 1313 */     return free ? (short)clearChunkIndex(this.fNodeType, chunk, index) : (short)getChunkIndex(this.fNodeType, chunk, index);
/*      */   }
/*      */ 
/*      */   public String getAttribute(int elemIndex, String name)
/*      */   {
/* 1320 */     if ((elemIndex == -1) || (name == null)) {
/* 1321 */       return null;
/*      */     }
/* 1323 */     int echunk = elemIndex >> 8;
/* 1324 */     int eindex = elemIndex & 0xFF;
/* 1325 */     int attrIndex = getChunkIndex(this.fNodeExtra, echunk, eindex);
/* 1326 */     while (attrIndex != -1) {
/* 1327 */       int achunk = attrIndex >> 8;
/* 1328 */       int aindex = attrIndex & 0xFF;
/* 1329 */       if (getChunkValue(this.fNodeName, achunk, aindex) == name) {
/* 1330 */         return getChunkValue(this.fNodeValue, achunk, aindex);
/*      */       }
/* 1332 */       attrIndex = getChunkIndex(this.fNodePrevSib, achunk, aindex);
/*      */     }
/* 1334 */     return null;
/*      */   }
/*      */ 
/*      */   public String getNodeURI(int nodeIndex)
/*      */   {
/* 1339 */     return getNodeURI(nodeIndex, true);
/*      */   }
/*      */ 
/*      */   public String getNodeURI(int nodeIndex, boolean free)
/*      */   {
/* 1348 */     if (nodeIndex == -1) {
/* 1349 */       return null;
/*      */     }
/*      */ 
/* 1352 */     int chunk = nodeIndex >> 8;
/* 1353 */     int index = nodeIndex & 0xFF;
/* 1354 */     return free ? clearChunkValue(this.fNodeURI, chunk, index) : getChunkValue(this.fNodeURI, chunk, index);
/*      */   }
/*      */ 
/*      */   public void putIdentifier(String name, int elementNodeIndex)
/*      */   {
/* 1373 */     if (this.fIdName == null) {
/* 1374 */       this.fIdName = new String[64];
/* 1375 */       this.fIdElement = new int[64];
/*      */     }
/*      */ 
/* 1379 */     if (this.fIdCount == this.fIdName.length) {
/* 1380 */       String[] idName = new String[this.fIdCount * 2];
/* 1381 */       System.arraycopy(this.fIdName, 0, idName, 0, this.fIdCount);
/* 1382 */       this.fIdName = idName;
/*      */ 
/* 1384 */       int[] idElement = new int[idName.length];
/* 1385 */       System.arraycopy(this.fIdElement, 0, idElement, 0, this.fIdCount);
/* 1386 */       this.fIdElement = idElement;
/*      */     }
/*      */ 
/* 1390 */     this.fIdName[this.fIdCount] = name;
/* 1391 */     this.fIdElement[this.fIdCount] = elementNodeIndex;
/* 1392 */     this.fIdCount += 1;
/*      */   }
/*      */ 
/*      */   public void print()
/*      */   {
/*      */   }
/*      */ 
/*      */   public int getNodeIndex()
/*      */   {
/* 1521 */     return 0;
/*      */   }
/*      */ 
/*      */   protected void synchronizeData()
/*      */   {
/* 1532 */     needsSyncData(false);
/*      */ 
/* 1535 */     if (this.fIdElement != null)
/*      */     {
/* 1544 */       IntVector path = new IntVector();
/* 1545 */       for (int i = 0; i < this.fIdCount; i++)
/*      */       {
/* 1548 */         int elementNodeIndex = this.fIdElement[i];
/* 1549 */         String idName = this.fIdName[i];
/* 1550 */         if (idName != null)
/*      */         {
/* 1555 */           path.removeAllElements();
/* 1556 */           int index = elementNodeIndex;
/*      */           do {
/* 1558 */             path.addElement(index);
/* 1559 */             int pchunk = index >> 8;
/* 1560 */             int pindex = index & 0xFF;
/* 1561 */             index = getChunkIndex(this.fNodeParent, pchunk, pindex);
/* 1562 */           }while (index != -1);
/*      */ 
/* 1568 */           Node place = this;
/* 1569 */           for (int j = path.size() - 2; j >= 0; j--) {
/* 1570 */             index = path.elementAt(j);
/* 1571 */             Node child = place.getLastChild();
/* 1572 */             while (child != null) {
/* 1573 */               if ((child instanceof DeferredNode)) {
/* 1574 */                 int nodeIndex = ((DeferredNode)child).getNodeIndex();
/*      */ 
/* 1576 */                 if (nodeIndex == index) {
/* 1577 */                   place = child;
/* 1578 */                   break;
/*      */                 }
/*      */               }
/* 1581 */               child = child.getPreviousSibling();
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/* 1586 */           Element element = (Element)place;
/* 1587 */           putIdentifier0(idName, element);
/* 1588 */           this.fIdName[i] = null;
/*      */ 
/* 1591 */           while ((i + 1 < this.fIdCount) && (this.fIdElement[(i + 1)] == elementNodeIndex))
/*      */           {
/* 1593 */             idName = this.fIdName[(++i)];
/* 1594 */             if (idName != null)
/*      */             {
/* 1597 */               putIdentifier0(idName, element);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void synchronizeChildren()
/*      */   {
/* 1613 */     if (needsSyncData()) {
/* 1614 */       synchronizeData();
/*      */ 
/* 1620 */       if (!needsSyncChildren()) {
/* 1621 */         return;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1626 */     boolean orig = this.mutationEvents;
/* 1627 */     this.mutationEvents = false;
/*      */ 
/* 1630 */     needsSyncChildren(false);
/*      */ 
/* 1632 */     getNodeType(0);
/*      */ 
/* 1635 */     ChildNode first = null;
/* 1636 */     ChildNode last = null;
/* 1637 */     for (int index = getLastChild(0); 
/* 1638 */       index != -1; 
/* 1639 */       index = getPrevSibling(index))
/*      */     {
/* 1641 */       ChildNode node = (ChildNode)getNodeObject(index);
/* 1642 */       if (last == null) {
/* 1643 */         last = node;
/*      */       }
/*      */       else {
/* 1646 */         first.previousSibling = node;
/*      */       }
/* 1648 */       node.ownerNode = this;
/* 1649 */       node.isOwned(true);
/* 1650 */       node.nextSibling = first;
/* 1651 */       first = node;
/*      */ 
/* 1654 */       int type = node.getNodeType();
/* 1655 */       if (type == 1) {
/* 1656 */         this.docElement = ((ElementImpl)node);
/*      */       }
/* 1658 */       else if (type == 10) {
/* 1659 */         this.docType = ((DocumentTypeImpl)node);
/*      */       }
/*      */     }
/*      */ 
/* 1663 */     if (first != null) {
/* 1664 */       this.firstChild = first;
/* 1665 */       first.isFirstChild(true);
/* 1666 */       lastChild(last);
/*      */     }
/*      */ 
/* 1670 */     this.mutationEvents = orig;
/*      */   }
/*      */ 
/*      */   protected final void synchronizeChildren(AttrImpl a, int nodeIndex)
/*      */   {
/* 1685 */     boolean orig = getMutationEvents();
/* 1686 */     setMutationEvents(false);
/*      */ 
/* 1689 */     a.needsSyncChildren(false);
/*      */ 
/* 1693 */     int last = getLastChild(nodeIndex);
/* 1694 */     int prev = getPrevSibling(last);
/* 1695 */     if (prev == -1) {
/* 1696 */       a.value = getNodeValueString(nodeIndex);
/* 1697 */       a.hasStringValue(true);
/*      */     }
/*      */     else {
/* 1700 */       ChildNode firstNode = null;
/* 1701 */       ChildNode lastNode = null;
/* 1702 */       for (int index = last; index != -1; 
/* 1703 */         index = getPrevSibling(index))
/*      */       {
/* 1705 */         ChildNode node = (ChildNode)getNodeObject(index);
/* 1706 */         if (lastNode == null) {
/* 1707 */           lastNode = node;
/*      */         }
/*      */         else {
/* 1710 */           firstNode.previousSibling = node;
/*      */         }
/* 1712 */         node.ownerNode = a;
/* 1713 */         node.isOwned(true);
/* 1714 */         node.nextSibling = firstNode;
/* 1715 */         firstNode = node;
/*      */       }
/* 1717 */       if (lastNode != null) {
/* 1718 */         a.value = firstNode;
/* 1719 */         firstNode.isFirstChild(true);
/* 1720 */         a.lastChild(lastNode);
/*      */       }
/* 1722 */       a.hasStringValue(false);
/*      */     }
/*      */ 
/* 1726 */     setMutationEvents(orig);
/*      */   }
/*      */ 
/*      */   protected final void synchronizeChildren(ParentNode p, int nodeIndex)
/*      */   {
/* 1742 */     boolean orig = getMutationEvents();
/* 1743 */     setMutationEvents(false);
/*      */ 
/* 1746 */     p.needsSyncChildren(false);
/*      */ 
/* 1749 */     ChildNode firstNode = null;
/* 1750 */     ChildNode lastNode = null;
/* 1751 */     for (int index = getLastChild(nodeIndex); 
/* 1752 */       index != -1; 
/* 1753 */       index = getPrevSibling(index))
/*      */     {
/* 1755 */       ChildNode node = (ChildNode)getNodeObject(index);
/* 1756 */       if (lastNode == null) {
/* 1757 */         lastNode = node;
/*      */       }
/*      */       else {
/* 1760 */         firstNode.previousSibling = node;
/*      */       }
/* 1762 */       node.ownerNode = p;
/* 1763 */       node.isOwned(true);
/* 1764 */       node.nextSibling = firstNode;
/* 1765 */       firstNode = node;
/*      */     }
/* 1767 */     if (lastNode != null) {
/* 1768 */       p.firstChild = firstNode;
/* 1769 */       firstNode.isFirstChild(true);
/* 1770 */       p.lastChild(lastNode);
/*      */     }
/*      */ 
/* 1774 */     setMutationEvents(orig);
/*      */   }
/*      */ 
/*      */   protected void ensureCapacity(int chunk)
/*      */   {
/* 1782 */     if (this.fNodeType == null)
/*      */     {
/* 1784 */       this.fNodeType = new int[32][];
/* 1785 */       this.fNodeName = new Object[32][];
/* 1786 */       this.fNodeValue = new Object[32][];
/* 1787 */       this.fNodeParent = new int[32][];
/* 1788 */       this.fNodeLastChild = new int[32][];
/* 1789 */       this.fNodePrevSib = new int[32][];
/* 1790 */       this.fNodeURI = new Object[32][];
/* 1791 */       this.fNodeExtra = new int[32][];
/*      */     }
/* 1793 */     else if (this.fNodeType.length <= chunk)
/*      */     {
/* 1795 */       int newsize = chunk * 2;
/*      */ 
/* 1797 */       int[][] newArray = new int[newsize][];
/* 1798 */       System.arraycopy(this.fNodeType, 0, newArray, 0, chunk);
/* 1799 */       this.fNodeType = newArray;
/*      */ 
/* 1801 */       Object[][] newStrArray = new Object[newsize][];
/* 1802 */       System.arraycopy(this.fNodeName, 0, newStrArray, 0, chunk);
/* 1803 */       this.fNodeName = newStrArray;
/*      */ 
/* 1805 */       newStrArray = new Object[newsize][];
/* 1806 */       System.arraycopy(this.fNodeValue, 0, newStrArray, 0, chunk);
/* 1807 */       this.fNodeValue = newStrArray;
/*      */ 
/* 1809 */       newArray = new int[newsize][];
/* 1810 */       System.arraycopy(this.fNodeParent, 0, newArray, 0, chunk);
/* 1811 */       this.fNodeParent = newArray;
/*      */ 
/* 1813 */       newArray = new int[newsize][];
/* 1814 */       System.arraycopy(this.fNodeLastChild, 0, newArray, 0, chunk);
/* 1815 */       this.fNodeLastChild = newArray;
/*      */ 
/* 1817 */       newArray = new int[newsize][];
/* 1818 */       System.arraycopy(this.fNodePrevSib, 0, newArray, 0, chunk);
/* 1819 */       this.fNodePrevSib = newArray;
/*      */ 
/* 1821 */       newStrArray = new Object[newsize][];
/* 1822 */       System.arraycopy(this.fNodeURI, 0, newStrArray, 0, chunk);
/* 1823 */       this.fNodeURI = newStrArray;
/*      */ 
/* 1825 */       newArray = new int[newsize][];
/* 1826 */       System.arraycopy(this.fNodeExtra, 0, newArray, 0, chunk);
/* 1827 */       this.fNodeExtra = newArray;
/*      */     }
/* 1829 */     else if (this.fNodeType[chunk] != null)
/*      */     {
/* 1831 */       return;
/*      */     }
/*      */ 
/* 1835 */     createChunk(this.fNodeType, chunk);
/* 1836 */     createChunk(this.fNodeName, chunk);
/* 1837 */     createChunk(this.fNodeValue, chunk);
/* 1838 */     createChunk(this.fNodeParent, chunk);
/* 1839 */     createChunk(this.fNodeLastChild, chunk);
/* 1840 */     createChunk(this.fNodePrevSib, chunk);
/* 1841 */     createChunk(this.fNodeURI, chunk);
/* 1842 */     createChunk(this.fNodeExtra, chunk);
/*      */   }
/*      */ 
/*      */   protected int createNode(short nodeType)
/*      */   {
/* 1852 */     int chunk = this.fNodeCount >> 8;
/* 1853 */     int index = this.fNodeCount & 0xFF;
/* 1854 */     ensureCapacity(chunk);
/*      */ 
/* 1857 */     setChunkIndex(this.fNodeType, nodeType, chunk, index);
/*      */ 
/* 1860 */     return this.fNodeCount++;
/*      */   }
/*      */ 
/*      */   protected static int binarySearch(int[] values, int start, int end, int target)
/*      */   {
/* 1887 */     while (start <= end)
/*      */     {
/* 1890 */       int middle = start + end >>> 1;
/* 1891 */       int value = values[middle];
/*      */ 
/* 1896 */       if (value == target) {
/* 1897 */         while ((middle > 0) && (values[(middle - 1)] == target)) {
/* 1898 */           middle--;
/*      */         }
/*      */ 
/* 1903 */         return middle;
/*      */       }
/*      */ 
/* 1907 */       if (value > target) {
/* 1908 */         end = middle - 1;
/*      */       }
/*      */       else {
/* 1911 */         start = middle + 1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1920 */     return -1;
/*      */   }
/*      */ 
/*      */   private final void createChunk(int[][] data, int chunk)
/*      */   {
/* 1935 */     data[chunk] = new int[257];
/* 1936 */     System.arraycopy(INIT_ARRAY, 0, data[chunk], 0, 256);
/*      */   }
/*      */ 
/*      */   private final void createChunk(Object[][] data, int chunk)
/*      */   {
/* 1944 */     data[chunk] = new Object[257];
/* 1945 */     data[chunk][256] = new RefCount();
/*      */   }
/*      */ 
/*      */   private final int setChunkIndex(int[][] data, int value, int chunk, int index)
/*      */   {
/* 1955 */     if (value == -1) {
/* 1956 */       return clearChunkIndex(data, chunk, index);
/*      */     }
/* 1958 */     int[] dataChunk = data[chunk];
/*      */ 
/* 1960 */     if (dataChunk == null) {
/* 1961 */       createChunk(data, chunk);
/* 1962 */       dataChunk = data[chunk];
/*      */     }
/* 1964 */     int ovalue = dataChunk[index];
/* 1965 */     if (ovalue == -1) {
/* 1966 */       dataChunk[256] += 1;
/*      */     }
/* 1968 */     dataChunk[index] = value;
/* 1969 */     return ovalue;
/*      */   }
/*      */ 
/*      */   private final String setChunkValue(Object[][] data, Object value, int chunk, int index) {
/* 1973 */     if (value == null) {
/* 1974 */       return clearChunkValue(data, chunk, index);
/*      */     }
/* 1976 */     Object[] dataChunk = data[chunk];
/*      */ 
/* 1978 */     if (dataChunk == null) {
/* 1979 */       createChunk(data, chunk);
/* 1980 */       dataChunk = data[chunk];
/*      */     }
/* 1982 */     String ovalue = (String)dataChunk[index];
/* 1983 */     if (ovalue == null) {
/* 1984 */       RefCount c = (RefCount)dataChunk[256];
/* 1985 */       c.fCount += 1;
/*      */     }
/* 1987 */     dataChunk[index] = value;
/* 1988 */     return ovalue;
/*      */   }
/*      */ 
/*      */   private final int getChunkIndex(int[][] data, int chunk, int index)
/*      */   {
/* 1995 */     return data[chunk] != null ? data[chunk][index] : -1;
/*      */   }
/*      */   private final String getChunkValue(Object[][] data, int chunk, int index) {
/* 1998 */     return data[chunk] != null ? (String)data[chunk][index] : null;
/*      */   }
/*      */   private final String getNodeValue(int chunk, int index) {
/* 2001 */     Object data = this.fNodeValue[chunk][index];
/* 2002 */     if (data == null) {
/* 2003 */       return null;
/*      */     }
/* 2005 */     if ((data instanceof String)) {
/* 2006 */       return (String)data;
/*      */     }
/*      */ 
/* 2010 */     return data.toString();
/*      */   }
/*      */ 
/*      */   private final int clearChunkIndex(int[][] data, int chunk, int index)
/*      */   {
/* 2023 */     int value = data[chunk] != null ? data[chunk][index] : -1;
/* 2024 */     if (value != -1) {
/* 2025 */       data[chunk][256] -= 1;
/* 2026 */       data[chunk][index] = -1;
/* 2027 */       if (data[chunk][256] == 0) {
/* 2028 */         data[chunk] = null;
/*      */       }
/*      */     }
/* 2031 */     return value;
/*      */   }
/*      */ 
/*      */   private final String clearChunkValue(Object[][] data, int chunk, int index) {
/* 2035 */     String value = data[chunk] != null ? (String)data[chunk][index] : null;
/* 2036 */     if (value != null) {
/* 2037 */       data[chunk][index] = null;
/* 2038 */       RefCount c = (RefCount)data[chunk][256];
/* 2039 */       c.fCount -= 1;
/* 2040 */       if (c.fCount == 0) {
/* 2041 */         data[chunk] = null;
/*      */       }
/*      */     }
/* 2044 */     return value;
/*      */   }
/*      */ 
/*      */   private final void putIdentifier0(String idName, Element element)
/*      */   {
/* 2061 */     if (this.identifiers == null) {
/* 2062 */       this.identifiers = new Hashtable();
/*      */     }
/*      */ 
/* 2066 */     this.identifiers.put(idName, element);
/*      */   }
/*      */ 
/*      */   private static void print(int[] values, int start, int end, int middle, int target)
/*      */   {
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1929 */     for (int i = 0; i < 256; i++)
/* 1930 */       INIT_ARRAY[i] = -1;
/*      */   }
/*      */ 
/*      */   static final class IntVector
/*      */   {
/*      */     private int[] data;
/*      */     private int size;
/*      */ 
/*      */     public int size()
/*      */     {
/* 2119 */       return this.size;
/*      */     }
/*      */ 
/*      */     public int elementAt(int index)
/*      */     {
/* 2124 */       return this.data[index];
/*      */     }
/*      */ 
/*      */     public void addElement(int element)
/*      */     {
/* 2129 */       ensureCapacity(this.size + 1);
/* 2130 */       this.data[(this.size++)] = element;
/*      */     }
/*      */ 
/*      */     public void removeAllElements()
/*      */     {
/* 2135 */       this.size = 0;
/*      */     }
/*      */ 
/*      */     private void ensureCapacity(int newsize)
/*      */     {
/* 2145 */       if (this.data == null) {
/* 2146 */         this.data = new int[newsize + 15];
/*      */       }
/* 2148 */       else if (newsize > this.data.length) {
/* 2149 */         int[] newdata = new int[newsize + 15];
/* 2150 */         System.arraycopy(this.data, 0, newdata, 0, this.data.length);
/* 2151 */         this.data = newdata;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class RefCount
/*      */   {
/*      */     int fCount;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DeferredDocumentImpl
 * JD-Core Version:    0.6.2
 */