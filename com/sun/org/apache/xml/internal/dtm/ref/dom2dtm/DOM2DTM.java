/*      */ package com.sun.org.apache.xml.internal.dtm.ref.dom2dtm;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*      */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.ExpandedNameTable;
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource;
/*      */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*      */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*      */ import com.sun.org.apache.xml.internal.utils.QName;
/*      */ import com.sun.org.apache.xml.internal.utils.StringBufferPool;
/*      */ import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
/*      */ import com.sun.org.apache.xml.internal.utils.TreeWalker;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLCharacterRecognizer;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*      */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*      */ import java.util.Vector;
/*      */ import javax.xml.transform.SourceLocator;
/*      */ import javax.xml.transform.dom.DOMSource;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Entity;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.xml.sax.ContentHandler;
/*      */ import org.xml.sax.DTDHandler;
/*      */ import org.xml.sax.EntityResolver;
/*      */ import org.xml.sax.ErrorHandler;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.DeclHandler;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ 
/*      */ public class DOM2DTM extends DTMDefaultBaseIterators
/*      */ {
/*      */   static final boolean JJK_DEBUG = false;
/*      */   static final boolean JJK_NEWCODE = true;
/*      */   static final String NAMESPACE_DECL_NS = "http://www.w3.org/XML/1998/namespace";
/*      */   private transient Node m_pos;
/*   81 */   private int m_last_parent = 0;
/*      */ 
/*   84 */   private int m_last_kid = -1;
/*      */   private transient Node m_root;
/*   93 */   boolean m_processedFirstElement = false;
/*      */   private transient boolean m_nodesAreProcessed;
/*  106 */   protected Vector m_nodes = new Vector();
/*      */ 
/* 1703 */   TreeWalker m_walker = new TreeWalker(null);
/*      */ 
/*      */   public DOM2DTM(DTMManager mgr, DOMSource domSource, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
/*      */   {
/*  125 */     super(mgr, domSource, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
/*      */ 
/*  129 */     this.m_pos = (this.m_root = domSource.getNode());
/*      */ 
/*  131 */     this.m_last_parent = (this.m_last_kid = -1);
/*  132 */     this.m_last_kid = addNode(this.m_root, this.m_last_parent, this.m_last_kid, -1);
/*      */ 
/*  144 */     if (1 == this.m_root.getNodeType())
/*      */     {
/*  146 */       NamedNodeMap attrs = this.m_root.getAttributes();
/*  147 */       int attrsize = attrs == null ? 0 : attrs.getLength();
/*  148 */       if (attrsize > 0)
/*      */       {
/*  150 */         int attrIndex = -1;
/*  151 */         for (int i = 0; i < attrsize; i++)
/*      */         {
/*  156 */           attrIndex = addNode(attrs.item(i), 0, attrIndex, -1);
/*  157 */           this.m_firstch.setElementAt(-1, attrIndex);
/*      */         }
/*      */ 
/*  161 */         this.m_nextsib.setElementAt(-1, attrIndex);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  168 */     this.m_nodesAreProcessed = false;
/*      */   }
/*      */ 
/*      */   protected int addNode(Node node, int parentIndex, int previousSibling, int forceNodeType)
/*      */   {
/*  186 */     int nodeIndex = this.m_nodes.size();
/*      */ 
/*  189 */     if (this.m_dtmIdent.size() == nodeIndex >>> 16)
/*      */     {
/*      */       try
/*      */       {
/*  193 */         if (this.m_mgr == null) {
/*  194 */           throw new ClassCastException();
/*      */         }
/*      */ 
/*  197 */         DTMManagerDefault mgrD = (DTMManagerDefault)this.m_mgr;
/*  198 */         int id = mgrD.getFirstFreeDTMID();
/*  199 */         mgrD.addDTM(this, id, nodeIndex);
/*  200 */         this.m_dtmIdent.addElement(id << 16);
/*      */       }
/*      */       catch (ClassCastException e)
/*      */       {
/*  207 */         error(XMLMessages.createXMLMessage("ER_NO_DTMIDS_AVAIL", null));
/*      */       }
/*      */     }
/*      */ 
/*  211 */     this.m_size += 1;
/*      */     int type;
/*      */     int type;
/*  215 */     if (-1 == forceNodeType)
/*  216 */       type = node.getNodeType();
/*      */     else {
/*  218 */       type = forceNodeType;
/*      */     }
/*      */ 
/*  237 */     if (2 == type)
/*      */     {
/*  239 */       String name = node.getNodeName();
/*      */ 
/*  241 */       if ((name.startsWith("xmlns:")) || (name.equals("xmlns")))
/*      */       {
/*  243 */         type = 13;
/*      */       }
/*      */     }
/*      */ 
/*  247 */     this.m_nodes.addElement(node);
/*      */ 
/*  249 */     this.m_firstch.setElementAt(-2, nodeIndex);
/*  250 */     this.m_nextsib.setElementAt(-2, nodeIndex);
/*  251 */     this.m_prevsib.setElementAt(previousSibling, nodeIndex);
/*  252 */     this.m_parent.setElementAt(parentIndex, nodeIndex);
/*      */ 
/*  254 */     if ((-1 != parentIndex) && (type != 2) && (type != 13))
/*      */     {
/*  259 */       if (-2 == this.m_firstch.elementAt(parentIndex)) {
/*  260 */         this.m_firstch.setElementAt(nodeIndex, parentIndex);
/*      */       }
/*      */     }
/*  263 */     String nsURI = node.getNamespaceURI();
/*      */ 
/*  268 */     String localName = type == 7 ? node.getNodeName() : node.getLocalName();
/*      */ 
/*  273 */     if (((type == 1) || (type == 2)) && (null == localName))
/*      */     {
/*  275 */       localName = node.getNodeName();
/*      */     }
/*  277 */     ExpandedNameTable exnt = this.m_expandedNameTable;
/*      */ 
/*  293 */     int expandedNameID = ((node.getLocalName() != null) || (type == 1) || (type != 2)) || 
/*  293 */       (null != localName) ? exnt.getExpandedTypeID(nsURI, localName, type) : exnt.getExpandedTypeID(type);
/*      */ 
/*  297 */     this.m_exptype.setElementAt(expandedNameID, nodeIndex);
/*      */ 
/*  299 */     indexNode(expandedNameID, nodeIndex);
/*      */ 
/*  301 */     if (-1 != previousSibling) {
/*  302 */       this.m_nextsib.setElementAt(nodeIndex, previousSibling);
/*      */     }
/*      */ 
/*  306 */     if (type == 13) {
/*  307 */       declareNamespaceInContext(parentIndex, nodeIndex);
/*      */     }
/*  309 */     return nodeIndex;
/*      */   }
/*      */ 
/*      */   public int getNumberOfNodes()
/*      */   {
/*  317 */     return this.m_nodes.size();
/*      */   }
/*      */ 
/*      */   protected boolean nextNode()
/*      */   {
/*  335 */     if (this.m_nodesAreProcessed) {
/*  336 */       return false;
/*      */     }
/*      */ 
/*  340 */     Node pos = this.m_pos;
/*  341 */     Node next = null;
/*  342 */     int nexttype = -1;
/*      */     do
/*      */     {
/*  348 */       if (pos.hasChildNodes())
/*      */       {
/*  350 */         next = pos.getFirstChild();
/*      */ 
/*  354 */         if ((next != null) && (10 == next.getNodeType())) {
/*  355 */           next = next.getNextSibling();
/*      */         }
/*      */ 
/*  359 */         if (5 != pos.getNodeType())
/*      */         {
/*  361 */           this.m_last_parent = this.m_last_kid;
/*  362 */           this.m_last_kid = -1;
/*      */ 
/*  364 */           if (null != this.m_wsfilter)
/*      */           {
/*  366 */             short wsv = this.m_wsfilter.getShouldStripSpace(makeNodeHandle(this.m_last_parent), this);
/*      */ 
/*  368 */             boolean shouldStrip = 2 == wsv ? true : 3 == wsv ? getShouldStripWhitespace() : false;
/*      */ 
/*  371 */             pushShouldStripWhitespace(shouldStrip);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  379 */         if (this.m_last_kid != -1)
/*      */         {
/*  383 */           if (this.m_firstch.elementAt(this.m_last_kid) == -2) {
/*  384 */             this.m_firstch.setElementAt(-1, this.m_last_kid);
/*      */           }
/*      */         }
/*  387 */         while (this.m_last_parent != -1)
/*      */         {
/*  391 */           next = pos.getNextSibling();
/*  392 */           if ((next != null) && (10 == next.getNodeType())) {
/*  393 */             next = next.getNextSibling();
/*      */           }
/*  395 */           if (next != null)
/*      */           {
/*      */             break;
/*      */           }
/*  399 */           pos = pos.getParentNode();
/*  400 */           if ((pos != null) || (
/*  413 */             (pos == null) || (5 != pos.getNodeType())))
/*      */           {
/*  421 */             popShouldStripWhitespace();
/*      */ 
/*  423 */             if (this.m_last_kid == -1)
/*  424 */               this.m_firstch.setElementAt(-1, this.m_last_parent);
/*      */             else
/*  426 */               this.m_nextsib.setElementAt(-1, this.m_last_kid);
/*  427 */             this.m_last_parent = this.m_parent.elementAt(this.m_last_kid = this.m_last_parent);
/*      */           }
/*      */         }
/*  430 */         if (this.m_last_parent == -1) {
/*  431 */           next = null;
/*      */         }
/*      */       }
/*  434 */       if (next != null) {
/*  435 */         nexttype = next.getNodeType();
/*      */       }
/*      */ 
/*  442 */       if (5 == nexttype)
/*  443 */         pos = next;
/*      */     }
/*  445 */     while (5 == nexttype);
/*      */ 
/*  448 */     if (next == null)
/*      */     {
/*  450 */       this.m_nextsib.setElementAt(-1, 0);
/*  451 */       this.m_nodesAreProcessed = true;
/*  452 */       this.m_pos = null;
/*      */ 
/*  461 */       return false;
/*      */     }
/*      */ 
/*  479 */     boolean suppressNode = false;
/*  480 */     Node lastTextNode = null;
/*      */ 
/*  482 */     nexttype = next.getNodeType();
/*      */ 
/*  485 */     if ((3 == nexttype) || (4 == nexttype))
/*      */     {
/*  488 */       suppressNode = (null != this.m_wsfilter) && (getShouldStripWhitespace());
/*      */ 
/*  492 */       Node n = next;
/*  493 */       while (n != null)
/*      */       {
/*  495 */         lastTextNode = n;
/*      */ 
/*  497 */         if (3 == n.getNodeType()) {
/*  498 */           nexttype = 3;
/*      */         }
/*      */ 
/*  501 */         suppressNode &= XMLCharacterRecognizer.isWhiteSpace(n.getNodeValue());
/*      */ 
/*  504 */         n = logicalNextDOMTextNode(n);
/*      */       }
/*      */ 
/*      */     }
/*  513 */     else if (7 == nexttype)
/*      */     {
/*  515 */       suppressNode = pos.getNodeName().toLowerCase().equals("xml");
/*      */     }
/*      */ 
/*  519 */     if (!suppressNode)
/*      */     {
/*  524 */       int nextindex = addNode(next, this.m_last_parent, this.m_last_kid, nexttype);
/*      */ 
/*  527 */       this.m_last_kid = nextindex;
/*      */ 
/*  529 */       if (1 == nexttype)
/*      */       {
/*  531 */         int attrIndex = -1;
/*      */ 
/*  534 */         NamedNodeMap attrs = next.getAttributes();
/*  535 */         int attrsize = attrs == null ? 0 : attrs.getLength();
/*  536 */         if (attrsize > 0)
/*      */         {
/*  538 */           for (int i = 0; i < attrsize; i++)
/*      */           {
/*  543 */             attrIndex = addNode(attrs.item(i), nextindex, attrIndex, -1);
/*      */ 
/*  545 */             this.m_firstch.setElementAt(-1, attrIndex);
/*      */ 
/*  556 */             if ((!this.m_processedFirstElement) && ("xmlns:xml".equals(attrs.item(i).getNodeName())))
/*      */             {
/*  558 */               this.m_processedFirstElement = true;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  563 */         if (!this.m_processedFirstElement)
/*      */         {
/*  571 */           attrIndex = addNode(new DOM2DTMdefaultNamespaceDeclarationNode((Element)next, "xml", "http://www.w3.org/XML/1998/namespace", makeNodeHandle((attrIndex == -1 ? nextindex : attrIndex) + 1)), nextindex, attrIndex, -1);
/*      */ 
/*  576 */           this.m_firstch.setElementAt(-1, attrIndex);
/*  577 */           this.m_processedFirstElement = true;
/*      */         }
/*  579 */         if (attrIndex != -1) {
/*  580 */           this.m_nextsib.setElementAt(-1, attrIndex);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  585 */     if ((3 == nexttype) || (4 == nexttype))
/*      */     {
/*  589 */       next = lastTextNode;
/*      */     }
/*      */ 
/*  593 */     this.m_pos = next;
/*  594 */     return true;
/*      */   }
/*      */ 
/*      */   public Node getNode(int nodeHandle)
/*      */   {
/*  608 */     int identity = makeNodeIdentity(nodeHandle);
/*      */ 
/*  610 */     return (Node)this.m_nodes.elementAt(identity);
/*      */   }
/*      */ 
/*      */   protected Node lookupNode(int nodeIdentity)
/*      */   {
/*  622 */     return (Node)this.m_nodes.elementAt(nodeIdentity);
/*      */   }
/*      */ 
/*      */   protected int getNextNodeIdentity(int identity)
/*      */   {
/*  635 */     identity++;
/*      */ 
/*  637 */     if (identity >= this.m_nodes.size())
/*      */     {
/*  639 */       if (!nextNode()) {
/*  640 */         identity = -1;
/*      */       }
/*      */     }
/*  643 */     return identity;
/*      */   }
/*      */ 
/*      */   private int getHandleFromNode(Node node)
/*      */   {
/*  667 */     if (null != node) {
/*  669 */       int len = this.m_nodes.size();
/*      */ 
/*  671 */       int i = 0;
/*      */       boolean isMore;
/*      */       do { for (; i < len; i++)
/*      */         {
/*  676 */           if (this.m_nodes.elementAt(i) == node) {
/*  677 */             return makeNodeHandle(i);
/*      */           }
/*      */         }
/*  680 */         isMore = nextNode();
/*      */ 
/*  682 */         len = this.m_nodes.size();
/*      */       }
/*      */ 
/*  685 */       while ((isMore) || (i < len));
/*      */     }
/*      */ 
/*  688 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getHandleOfNode(Node node)
/*      */   {
/*  706 */     if (null != node)
/*      */     {
/*  711 */       if ((this.m_root == node) || ((this.m_root.getNodeType() == 9) && (this.m_root == node.getOwnerDocument())) || ((this.m_root.getNodeType() != 9) && (this.m_root.getOwnerDocument() == node.getOwnerDocument())))
/*      */       {
/*  723 */         for (Node cursor = node; 
/*  724 */           cursor != null; 
/*  725 */           cursor = cursor.getNodeType() != 2 ? cursor.getParentNode() : ((Attr)cursor).getOwnerElement())
/*      */         {
/*  730 */           if (cursor == this.m_root)
/*      */           {
/*  732 */             return getHandleFromNode(node);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  737 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getAttributeNode(int nodeHandle, String namespaceURI, String name)
/*      */   {
/*  757 */     if (null == namespaceURI) {
/*  758 */       namespaceURI = "";
/*      */     }
/*  760 */     int type = getNodeType(nodeHandle);
/*      */ 
/*  762 */     if (1 == type)
/*      */     {
/*  766 */       int identity = makeNodeIdentity(nodeHandle);
/*      */ 
/*  768 */       while (-1 != (identity = getNextNodeIdentity(identity)))
/*      */       {
/*  771 */         type = _type(identity);
/*      */ 
/*  780 */         if ((type != 2) && (type != 13))
/*      */           break;
/*  782 */         Node node = lookupNode(identity);
/*  783 */         String nodeuri = node.getNamespaceURI();
/*      */ 
/*  785 */         if (null == nodeuri) {
/*  786 */           nodeuri = "";
/*      */         }
/*  788 */         String nodelocalname = node.getLocalName();
/*      */ 
/*  790 */         if ((nodeuri.equals(namespaceURI)) && (name.equals(nodelocalname))) {
/*  791 */           return makeNodeHandle(identity);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  801 */     return -1;
/*      */   }
/*      */ 
/*      */   public XMLString getStringValue(int nodeHandle)
/*      */   {
/*  816 */     int type = getNodeType(nodeHandle);
/*  817 */     Node node = getNode(nodeHandle);
/*      */ 
/*  820 */     if ((1 == type) || (9 == type) || (11 == type))
/*      */     {
/*  823 */       FastStringBuffer buf = StringBufferPool.get();
/*      */       String s;
/*      */       try
/*      */       {
/*  828 */         getNodeData(node, buf);
/*      */ 
/*  830 */         s = buf.length() > 0 ? buf.toString() : "";
/*      */       }
/*      */       finally
/*      */       {
/*  834 */         StringBufferPool.free(buf);
/*      */       }
/*      */ 
/*  837 */       return this.m_xstrf.newstr(s);
/*      */     }
/*  839 */     if ((3 == type) || (4 == type))
/*      */     {
/*  848 */       FastStringBuffer buf = StringBufferPool.get();
/*  849 */       while (node != null)
/*      */       {
/*  851 */         buf.append(node.getNodeValue());
/*  852 */         node = logicalNextDOMTextNode(node);
/*      */       }
/*  854 */       String s = buf.length() > 0 ? buf.toString() : "";
/*  855 */       StringBufferPool.free(buf);
/*  856 */       return this.m_xstrf.newstr(s);
/*      */     }
/*      */ 
/*  859 */     return this.m_xstrf.newstr(node.getNodeValue());
/*      */   }
/*      */ 
/*      */   public boolean isWhitespace(int nodeHandle)
/*      */   {
/*  871 */     int type = getNodeType(nodeHandle);
/*  872 */     Node node = getNode(nodeHandle);
/*  873 */     if ((3 == type) || (4 == type))
/*      */     {
/*  882 */       FastStringBuffer buf = StringBufferPool.get();
/*  883 */       while (node != null)
/*      */       {
/*  885 */         buf.append(node.getNodeValue());
/*  886 */         node = logicalNextDOMTextNode(node);
/*      */       }
/*  888 */       boolean b = buf.isWhitespace(0, buf.length());
/*  889 */       StringBufferPool.free(buf);
/*  890 */       return b;
/*      */     }
/*  892 */     return false;
/*      */   }
/*      */ 
/*      */   protected static void getNodeData(Node node, FastStringBuffer buf)
/*      */   {
/*  920 */     switch (node.getNodeType())
/*      */     {
/*      */     case 1:
/*      */     case 9:
/*      */     case 11:
/*  926 */       for (Node child = node.getFirstChild(); null != child; 
/*  927 */         child = child.getNextSibling())
/*      */       {
/*  929 */         getNodeData(child, buf);
/*      */       }
/*      */ 
/*  932 */       break;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*  936 */       buf.append(node.getNodeValue());
/*  937 */       break;
/*      */     case 7:
/*  940 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 8:
/*      */     case 10:
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getNodeName(int nodeHandle)
/*      */   {
/*  959 */     Node node = getNode(nodeHandle);
/*      */ 
/*  962 */     return node.getNodeName();
/*      */   }
/*      */ 
/*      */   public String getNodeNameX(int nodeHandle)
/*      */   {
/*  977 */     short type = getNodeType(nodeHandle);
/*      */     String name;
/*  979 */     switch (type)
/*      */     {
/*      */     case 13:
/*  983 */       Node node = getNode(nodeHandle);
/*      */ 
/*  986 */       name = node.getNodeName();
/*  987 */       if (name.startsWith("xmlns:"))
/*      */       {
/*  989 */         name = QName.getLocalPart(name);
/*      */       }
/*  991 */       else if (name.equals("xmlns"))
/*      */       {
/*  993 */         name = "";
/*      */       }
/*      */ 
/*  996 */       break;
/*      */     case 1:
/*      */     case 2:
/*      */     case 5:
/*      */     case 7:
/* 1002 */       Node node = getNode(nodeHandle);
/*      */ 
/* 1005 */       name = node.getNodeName();
/*      */ 
/* 1007 */       break;
/*      */     case 3:
/*      */     case 4:
/*      */     case 6:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     default:
/* 1009 */       name = "";
/*      */     }
/*      */ 
/* 1012 */     return name;
/*      */   }
/*      */ 
/*      */   public String getLocalName(int nodeHandle)
/*      */   {
/* 1027 */     int id = makeNodeIdentity(nodeHandle);
/* 1028 */     if (-1 == id) return null;
/* 1029 */     Node newnode = (Node)this.m_nodes.elementAt(id);
/* 1030 */     String newname = newnode.getLocalName();
/* 1031 */     if (null == newname)
/*      */     {
/* 1034 */       String qname = newnode.getNodeName();
/* 1035 */       if ('#' == qname.charAt(0))
/*      */       {
/* 1039 */         newname = "";
/*      */       }
/*      */       else
/*      */       {
/* 1043 */         int index = qname.indexOf(':');
/* 1044 */         newname = index < 0 ? qname : qname.substring(index + 1);
/*      */       }
/*      */     }
/* 1047 */     return newname;
/*      */   }
/*      */ 
/*      */   public String getPrefix(int nodeHandle)
/*      */   {
/* 1098 */     short type = getNodeType(nodeHandle);
/*      */     String prefix;
/* 1100 */     switch (type)
/*      */     {
/*      */     case 13:
/* 1104 */       Node node = getNode(nodeHandle);
/*      */ 
/* 1107 */       String qname = node.getNodeName();
/* 1108 */       int index = qname.indexOf(':');
/*      */ 
/* 1110 */       prefix = index < 0 ? "" : qname.substring(index + 1);
/*      */ 
/* 1112 */       break;
/*      */     case 1:
/*      */     case 2:
/* 1116 */       Node node = getNode(nodeHandle);
/*      */ 
/* 1119 */       String qname = node.getNodeName();
/* 1120 */       int index = qname.indexOf(':');
/*      */ 
/* 1122 */       prefix = index < 0 ? "" : qname.substring(0, index);
/*      */ 
/* 1124 */       break;
/*      */     default:
/* 1126 */       prefix = "";
/*      */     }
/*      */ 
/* 1129 */     return prefix;
/*      */   }
/*      */ 
/*      */   public String getNamespaceURI(int nodeHandle)
/*      */   {
/* 1147 */     int id = makeNodeIdentity(nodeHandle);
/* 1148 */     if (id == -1) return null;
/* 1149 */     Node node = (Node)this.m_nodes.elementAt(id);
/* 1150 */     return node.getNamespaceURI();
/*      */   }
/*      */ 
/*      */   private Node logicalNextDOMTextNode(Node n)
/*      */   {
/* 1191 */     Node p = n.getNextSibling();
/* 1192 */     if (p == null)
/*      */     {
/* 1195 */       for (n = n.getParentNode(); 
/* 1196 */         (n != null) && (5 == n.getNodeType()); 
/* 1197 */         n = n.getParentNode())
/*      */       {
/* 1199 */         p = n.getNextSibling();
/* 1200 */         if (p != null)
/*      */           break;
/*      */       }
/*      */     }
/* 1204 */     n = p;
/* 1205 */     while ((n != null) && (5 == n.getNodeType()))
/*      */     {
/* 1208 */       if (n.hasChildNodes())
/* 1209 */         n = n.getFirstChild();
/*      */       else
/* 1211 */         n = n.getNextSibling();
/*      */     }
/* 1213 */     if (n != null)
/*      */     {
/* 1216 */       int ntype = n.getNodeType();
/* 1217 */       if ((3 != ntype) && (4 != ntype))
/* 1218 */         n = null;
/*      */     }
/* 1220 */     return n;
/*      */   }
/*      */ 
/*      */   public String getNodeValue(int nodeHandle)
/*      */   {
/* 1237 */     int type = _exptype(makeNodeIdentity(nodeHandle));
/* 1238 */     type = -1 != type ? getNodeType(nodeHandle) : -1;
/*      */ 
/* 1240 */     if ((3 != type) && (4 != type)) {
/* 1241 */       return getNode(nodeHandle).getNodeValue();
/*      */     }
/*      */ 
/* 1250 */     Node node = getNode(nodeHandle);
/* 1251 */     Node n = logicalNextDOMTextNode(node);
/* 1252 */     if (n == null) {
/* 1253 */       return node.getNodeValue();
/*      */     }
/* 1255 */     FastStringBuffer buf = StringBufferPool.get();
/* 1256 */     buf.append(node.getNodeValue());
/* 1257 */     while (n != null)
/*      */     {
/* 1259 */       buf.append(n.getNodeValue());
/* 1260 */       n = logicalNextDOMTextNode(n);
/*      */     }
/* 1262 */     String s = buf.length() > 0 ? buf.toString() : "";
/* 1263 */     StringBufferPool.free(buf);
/* 1264 */     return s;
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationSystemIdentifier()
/*      */   {
/*      */     Document doc;
/*      */     Document doc;
/* 1280 */     if (this.m_root.getNodeType() == 9)
/* 1281 */       doc = (Document)this.m_root;
/*      */     else {
/* 1283 */       doc = this.m_root.getOwnerDocument();
/*      */     }
/* 1285 */     if (null != doc)
/*      */     {
/* 1287 */       DocumentType dtd = doc.getDoctype();
/*      */ 
/* 1289 */       if (null != dtd)
/*      */       {
/* 1291 */         return dtd.getSystemId();
/*      */       }
/*      */     }
/*      */ 
/* 1295 */     return null;
/*      */   }
/*      */ 
/*      */   public String getDocumentTypeDeclarationPublicIdentifier()
/*      */   {
/*      */     Document doc;
/*      */     Document doc;
/* 1311 */     if (this.m_root.getNodeType() == 9)
/* 1312 */       doc = (Document)this.m_root;
/*      */     else {
/* 1314 */       doc = this.m_root.getOwnerDocument();
/*      */     }
/* 1316 */     if (null != doc)
/*      */     {
/* 1318 */       DocumentType dtd = doc.getDoctype();
/*      */ 
/* 1320 */       if (null != dtd)
/*      */       {
/* 1322 */         return dtd.getPublicId();
/*      */       }
/*      */     }
/*      */ 
/* 1326 */     return null;
/*      */   }
/*      */ 
/*      */   public int getElementById(String elementId)
/*      */   {
/* 1349 */     Document doc = this.m_root.getNodeType() == 9 ? (Document)this.m_root : this.m_root.getOwnerDocument();
/*      */ 
/* 1352 */     if (null != doc)
/*      */     {
/* 1354 */       Node elem = doc.getElementById(elementId);
/* 1355 */       if (null != elem)
/*      */       {
/* 1357 */         int elemHandle = getHandleFromNode(elem);
/*      */ 
/* 1359 */         if (-1 == elemHandle)
/*      */         {
/* 1361 */           int identity = this.m_nodes.size() - 1;
/* 1362 */           while (-1 != (identity = getNextNodeIdentity(identity)))
/*      */           {
/* 1364 */             Node node = getNode(identity);
/* 1365 */             if (node == elem)
/*      */             {
/* 1367 */               elemHandle = getHandleFromNode(elem);
/* 1368 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1373 */         return elemHandle;
/*      */       }
/*      */     }
/*      */ 
/* 1377 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getUnparsedEntityURI(String name)
/*      */   {
/* 1417 */     String url = "";
/* 1418 */     Document doc = this.m_root.getNodeType() == 9 ? (Document)this.m_root : this.m_root.getOwnerDocument();
/*      */ 
/* 1421 */     if (null != doc)
/*      */     {
/* 1423 */       DocumentType doctype = doc.getDoctype();
/*      */ 
/* 1425 */       if (null != doctype)
/*      */       {
/* 1427 */         NamedNodeMap entities = doctype.getEntities();
/* 1428 */         if (null == entities)
/* 1429 */           return url;
/* 1430 */         Entity entity = (Entity)entities.getNamedItem(name);
/* 1431 */         if (null == entity) {
/* 1432 */           return url;
/*      */         }
/* 1434 */         String notationName = entity.getNotationName();
/*      */ 
/* 1436 */         if (null != notationName)
/*      */         {
/* 1447 */           url = entity.getSystemId();
/*      */ 
/* 1449 */           if (null == url)
/*      */           {
/* 1451 */             url = entity.getPublicId();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1462 */     return url;
/*      */   }
/*      */ 
/*      */   public boolean isAttributeSpecified(int attributeHandle)
/*      */   {
/* 1476 */     int type = getNodeType(attributeHandle);
/*      */ 
/* 1478 */     if (2 == type)
/*      */     {
/* 1480 */       Attr attr = (Attr)getNode(attributeHandle);
/* 1481 */       return attr.getSpecified();
/*      */     }
/* 1483 */     return false;
/*      */   }
/*      */ 
/*      */   public void setIncrementalSAXSource(IncrementalSAXSource source)
/*      */   {
/*      */   }
/*      */ 
/*      */   public ContentHandler getContentHandler()
/*      */   {
/* 1507 */     return null;
/*      */   }
/*      */ 
/*      */   public LexicalHandler getLexicalHandler()
/*      */   {
/* 1523 */     return null;
/*      */   }
/*      */ 
/*      */   public EntityResolver getEntityResolver()
/*      */   {
/* 1535 */     return null;
/*      */   }
/*      */ 
/*      */   public DTDHandler getDTDHandler()
/*      */   {
/* 1546 */     return null;
/*      */   }
/*      */ 
/*      */   public ErrorHandler getErrorHandler()
/*      */   {
/* 1557 */     return null;
/*      */   }
/*      */ 
/*      */   public DeclHandler getDeclHandler()
/*      */   {
/* 1568 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean needsTwoThreads()
/*      */   {
/* 1578 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean isSpace(char ch)
/*      */   {
/* 1592 */     return XMLCharacterRecognizer.isWhiteSpace(ch);
/*      */   }
/*      */ 
/*      */   public void dispatchCharactersEvents(int nodeHandle, ContentHandler ch, boolean normalize)
/*      */     throws SAXException
/*      */   {
/* 1613 */     if (normalize)
/*      */     {
/* 1615 */       XMLString str = getStringValue(nodeHandle);
/* 1616 */       str = str.fixWhiteSpace(true, true, false);
/* 1617 */       str.dispatchCharactersEvents(ch);
/*      */     }
/*      */     else
/*      */     {
/* 1621 */       int type = getNodeType(nodeHandle);
/* 1622 */       Node node = getNode(nodeHandle);
/* 1623 */       dispatchNodeData(node, ch, 0);
/*      */ 
/* 1626 */       if ((3 == type) || (4 == type))
/*      */       {
/* 1628 */         while (null != (node = logicalNextDOMTextNode(node)))
/*      */         {
/* 1630 */           dispatchNodeData(node, ch, 0);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static void dispatchNodeData(Node node, ContentHandler ch, int depth)
/*      */     throws SAXException
/*      */   {
/* 1662 */     switch (node.getNodeType())
/*      */     {
/*      */     case 1:
/*      */     case 9:
/*      */     case 11:
/* 1668 */       for (Node child = node.getFirstChild(); null != child; 
/* 1669 */         child = child.getNextSibling())
/*      */       {
/* 1671 */         dispatchNodeData(child, ch, depth + 1);
/*      */       }
/*      */ 
/* 1674 */       break;
/*      */     case 7:
/*      */     case 8:
/* 1677 */       if (0 != depth)
/*      */       {
/*      */         break;
/*      */       }
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/* 1684 */       String str = node.getNodeValue();
/* 1685 */       if ((ch instanceof CharacterNodeHandler))
/*      */       {
/* 1687 */         ((CharacterNodeHandler)ch).characters(node);
/*      */       }
/*      */       else
/*      */       {
/* 1691 */         ch.characters(str.toCharArray(), 0, str.length());
/*      */       }
/* 1693 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 10:
/*      */     }
/*      */   }
/*      */ 
/*      */   public void dispatchToEvents(int nodeHandle, ContentHandler ch)
/*      */     throws SAXException
/*      */   {
/* 1716 */     TreeWalker treeWalker = this.m_walker;
/* 1717 */     ContentHandler prevCH = treeWalker.getContentHandler();
/*      */ 
/* 1719 */     if (null != prevCH)
/*      */     {
/* 1721 */       treeWalker = new TreeWalker(null);
/*      */     }
/* 1723 */     treeWalker.setContentHandler(ch);
/*      */     try
/*      */     {
/* 1727 */       Node node = getNode(nodeHandle);
/* 1728 */       treeWalker.traverseFragment(node);
/*      */     }
/*      */     finally
/*      */     {
/* 1732 */       treeWalker.setContentHandler(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setProperty(String property, Object value)
/*      */   {
/*      */   }
/*      */ 
/*      */   public SourceLocator getSourceLocatorFor(int node)
/*      */   {
/* 1762 */     return null;
/*      */   }
/*      */ 
/*      */   public static abstract interface CharacterNodeHandler
/*      */   {
/*      */     public abstract void characters(Node paramNode)
/*      */       throws SAXException;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTM
 * JD-Core Version:    0.6.2
 */