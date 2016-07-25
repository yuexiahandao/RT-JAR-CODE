/*      */ package com.sun.org.apache.xml.internal.utils;
/*      */ 
/*      */ import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeProxy;
/*      */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.xml.parsers.DocumentBuilder;
/*      */ import javax.xml.parsers.DocumentBuilderFactory;
/*      */ import javax.xml.parsers.ParserConfigurationException;
/*      */ import javax.xml.transform.TransformerException;
/*      */ import org.w3c.dom.Attr;
/*      */ import org.w3c.dom.DOMImplementation;
/*      */ import org.w3c.dom.Document;
/*      */ import org.w3c.dom.DocumentType;
/*      */ import org.w3c.dom.Element;
/*      */ import org.w3c.dom.Entity;
/*      */ import org.w3c.dom.NamedNodeMap;
/*      */ import org.w3c.dom.Node;
/*      */ import org.w3c.dom.Text;
/*      */ 
/*      */ /** @deprecated */
/*      */ public class DOMHelper
/*      */ {
/*  568 */   Hashtable m_NSInfos = new Hashtable();
/*      */ 
/*  572 */   protected static final NSInfo m_NSInfoUnProcWithXMLNS = new NSInfo(false, true);
/*      */ 
/*  577 */   protected static final NSInfo m_NSInfoUnProcWithoutXMLNS = new NSInfo(false, false);
/*      */ 
/*  582 */   protected static final NSInfo m_NSInfoUnProcNoAncestorXMLNS = new NSInfo(false, false, 2);
/*      */ 
/*  587 */   protected static final NSInfo m_NSInfoNullWithXMLNS = new NSInfo(true, true);
/*      */ 
/*  592 */   protected static final NSInfo m_NSInfoNullWithoutXMLNS = new NSInfo(true, false);
/*      */ 
/*  597 */   protected static final NSInfo m_NSInfoNullNoAncestorXMLNS = new NSInfo(true, false, 2);
/*      */ 
/*  602 */   protected Vector m_candidateNoAncestorXMLNS = new Vector();
/*      */ 
/* 1225 */   protected Document m_DOMFactory = null;
/*      */ 
/*      */   public static Document createDocument(boolean isSecureProcessing)
/*      */   {
/*      */     try
/*      */     {
/*   87 */       DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
/*      */ 
/*   89 */       dfactory.setNamespaceAware(true);
/*   90 */       dfactory.setValidating(true);
/*      */ 
/*   92 */       if (isSecureProcessing)
/*      */       {
/*      */         try
/*      */         {
/*   96 */           dfactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/*      */         }
/*      */         catch (ParserConfigurationException pce) {
/*      */         }
/*      */       }
/*  101 */       DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
/*  102 */       return docBuilder.newDocument();
/*      */     }
/*      */     catch (ParserConfigurationException pce)
/*      */     {
/*      */     }
/*      */ 
/*  108 */     throw new RuntimeException(XMLMessages.createXMLMessage("ER_CREATEDOCUMENT_NOT_SUPPORTED", null));
/*      */   }
/*      */ 
/*      */   public static Document createDocument()
/*      */   {
/*  128 */     return createDocument(false);
/*      */   }
/*      */ 
/*      */   public boolean shouldStripSourceNode(Node textNode)
/*      */     throws TransformerException
/*      */   {
/*  148 */     return false;
/*      */   }
/*      */ 
/*      */   public String getUniqueID(Node node)
/*      */   {
/*  178 */     return "N" + Integer.toHexString(node.hashCode()).toUpperCase();
/*      */   }
/*      */ 
/*      */   public static boolean isNodeAfter(Node node1, Node node2)
/*      */   {
/*  201 */     if ((node1 == node2) || (isNodeTheSame(node1, node2))) {
/*  202 */       return true;
/*      */     }
/*      */ 
/*  205 */     boolean isNodeAfter = true;
/*      */ 
/*  207 */     Node parent1 = getParentOfNode(node1);
/*  208 */     Node parent2 = getParentOfNode(node2);
/*      */ 
/*  211 */     if ((parent1 == parent2) || (isNodeTheSame(parent1, parent2)))
/*      */     {
/*  213 */       if (null != parent1) {
/*  214 */         isNodeAfter = isNodeAfterSibling(parent1, node1, node2);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  240 */       int nParents1 = 2; int nParents2 = 2;
/*      */ 
/*  242 */       while (parent1 != null)
/*      */       {
/*  244 */         nParents1++;
/*      */ 
/*  246 */         parent1 = getParentOfNode(parent1);
/*      */       }
/*      */ 
/*  249 */       while (parent2 != null)
/*      */       {
/*  251 */         nParents2++;
/*      */ 
/*  253 */         parent2 = getParentOfNode(parent2);
/*      */       }
/*      */ 
/*  258 */       Node startNode1 = node1; Node startNode2 = node2;
/*      */ 
/*  262 */       if (nParents1 < nParents2)
/*      */       {
/*  265 */         int adjust = nParents2 - nParents1;
/*      */ 
/*  267 */         for (int i = 0; i < adjust; i++)
/*      */         {
/*  269 */           startNode2 = getParentOfNode(startNode2);
/*      */         }
/*      */       }
/*  272 */       else if (nParents1 > nParents2)
/*      */       {
/*  275 */         int adjust = nParents1 - nParents2;
/*      */ 
/*  277 */         for (int i = 0; i < adjust; i++)
/*      */         {
/*  279 */           startNode1 = getParentOfNode(startNode1);
/*      */         }
/*      */       }
/*      */ 
/*  283 */       Node prevChild1 = null; Node prevChild2 = null;
/*      */ 
/*  286 */       while (null != startNode1)
/*      */       {
/*  288 */         if ((startNode1 == startNode2) || (isNodeTheSame(startNode1, startNode2)))
/*      */         {
/*  290 */           if (null == prevChild1)
/*      */           {
/*  294 */             isNodeAfter = nParents1 < nParents2;
/*      */ 
/*  296 */             break;
/*      */           }
/*      */ 
/*  301 */           isNodeAfter = isNodeAfterSibling(startNode1, prevChild1, prevChild2);
/*      */ 
/*  304 */           break;
/*      */         }
/*      */ 
/*  309 */         prevChild1 = startNode1;
/*  310 */         startNode1 = getParentOfNode(startNode1);
/*  311 */         prevChild2 = startNode2;
/*  312 */         startNode2 = getParentOfNode(startNode2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  324 */     return isNodeAfter;
/*      */   }
/*      */ 
/*      */   public static boolean isNodeTheSame(Node node1, Node node2)
/*      */   {
/*  336 */     if (((node1 instanceof DTMNodeProxy)) && ((node2 instanceof DTMNodeProxy))) {
/*  337 */       return ((DTMNodeProxy)node1).equals((DTMNodeProxy)node2);
/*      */     }
/*  339 */     return node1 == node2;
/*      */   }
/*      */ 
/*      */   private static boolean isNodeAfterSibling(Node parent, Node child1, Node child2)
/*      */   {
/*  360 */     boolean isNodeAfterSibling = false;
/*  361 */     short child1type = child1.getNodeType();
/*  362 */     short child2type = child2.getNodeType();
/*      */ 
/*  364 */     if ((2 != child1type) && (2 == child2type))
/*      */     {
/*  369 */       isNodeAfterSibling = false;
/*      */     }
/*  371 */     else if ((2 == child1type) && (2 != child2type))
/*      */     {
/*  376 */       isNodeAfterSibling = true;
/*      */     }
/*  378 */     else if (2 == child1type)
/*      */     {
/*  380 */       NamedNodeMap children = parent.getAttributes();
/*  381 */       int nNodes = children.getLength();
/*  382 */       boolean found1 = false; boolean found2 = false;
/*      */ 
/*  385 */       for (int i = 0; i < nNodes; i++)
/*      */       {
/*  387 */         Node child = children.item(i);
/*      */ 
/*  389 */         if ((child1 == child) || (isNodeTheSame(child1, child)))
/*      */         {
/*  391 */           if (found2)
/*      */           {
/*  393 */             isNodeAfterSibling = false;
/*      */ 
/*  395 */             break;
/*      */           }
/*      */ 
/*  398 */           found1 = true;
/*      */         }
/*  400 */         else if ((child2 == child) || (isNodeTheSame(child2, child)))
/*      */         {
/*  402 */           if (found1)
/*      */           {
/*  404 */             isNodeAfterSibling = true;
/*      */ 
/*  406 */             break;
/*      */           }
/*      */ 
/*  409 */           found2 = true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  426 */       Node child = parent.getFirstChild();
/*  427 */       boolean found1 = false; boolean found2 = false;
/*      */ 
/*  429 */       while (null != child)
/*      */       {
/*  433 */         if ((child1 == child) || (isNodeTheSame(child1, child)))
/*      */         {
/*  435 */           if (found2)
/*      */           {
/*  437 */             isNodeAfterSibling = false;
/*      */ 
/*  439 */             break;
/*      */           }
/*      */ 
/*  442 */           found1 = true;
/*      */         }
/*  444 */         else if ((child2 == child) || (isNodeTheSame(child2, child)))
/*      */         {
/*  446 */           if (found1)
/*      */           {
/*  448 */             isNodeAfterSibling = true;
/*      */ 
/*  450 */             break;
/*      */           }
/*      */ 
/*  453 */           found2 = true;
/*      */         }
/*      */ 
/*  456 */         child = child.getNextSibling();
/*      */       }
/*      */     }
/*      */ 
/*  460 */     return isNodeAfterSibling;
/*      */   }
/*      */ 
/*      */   public short getLevel(Node n)
/*      */   {
/*  478 */     short level = 1;
/*      */ 
/*  480 */     while (null != (n = getParentOfNode(n)))
/*      */     {
/*  482 */       level = (short)(level + 1);
/*      */     }
/*      */ 
/*  485 */     return level;
/*      */   }
/*      */ 
/*      */   public String getNamespaceForPrefix(String prefix, Element namespaceContext)
/*      */   {
/*  511 */     Node parent = namespaceContext;
/*  512 */     String namespace = null;
/*      */ 
/*  514 */     if (prefix.equals("xml"))
/*      */     {
/*  516 */       namespace = "http://www.w3.org/XML/1998/namespace";
/*      */     }
/*  518 */     else if (prefix.equals("xmlns"))
/*      */     {
/*  525 */       namespace = "http://www.w3.org/2000/xmlns/";
/*      */     }
/*      */     else
/*      */     {
/*  530 */       String declname = "xmlns:" + prefix;
/*      */       int type;
/*  536 */       while ((null != parent) && (null == namespace) && (((type = parent.getNodeType()) == 1) || (type == 5)))
/*      */       {
/*  539 */         if (type == 1)
/*      */         {
/*  550 */           Attr attr = ((Element)parent).getAttributeNode(declname);
/*  551 */           if (attr != null)
/*      */           {
/*  553 */             namespace = attr.getNodeValue();
/*  554 */             break;
/*      */           }
/*      */         }
/*      */ 
/*  558 */         parent = getParentOfNode(parent);
/*      */       }
/*      */     }
/*      */ 
/*  562 */     return namespace;
/*      */   }
/*      */ 
/*      */   public String getNamespaceOfNode(Node n)
/*      */   {
/*  622 */     short ntype = n.getNodeType();
/*      */     boolean hasProcessedNS;
/*      */     boolean hasProcessedNS;
/*      */     NSInfo nsInfo;
/*  624 */     if (2 != ntype)
/*      */     {
/*  626 */       Object nsObj = this.m_NSInfos.get(n);
/*      */ 
/*  628 */       NSInfo nsInfo = nsObj == null ? null : (NSInfo)nsObj;
/*  629 */       hasProcessedNS = nsInfo == null ? false : nsInfo.m_hasProcessedNS;
/*      */     }
/*      */     else
/*      */     {
/*  633 */       hasProcessedNS = false;
/*  634 */       nsInfo = null;
/*      */     }
/*      */     String namespaceOfPrefix;
/*      */     String namespaceOfPrefix;
/*  637 */     if (hasProcessedNS)
/*      */     {
/*  639 */       namespaceOfPrefix = nsInfo.m_namespace;
/*      */     }
/*      */     else
/*      */     {
/*  643 */       namespaceOfPrefix = null;
/*      */ 
/*  645 */       String nodeName = n.getNodeName();
/*  646 */       int indexOfNSSep = nodeName.indexOf(':');
/*      */       String prefix;
/*  649 */       if (2 == ntype)
/*      */       {
/*      */         String prefix;
/*  651 */         if (indexOfNSSep > 0)
/*      */         {
/*  653 */           prefix = nodeName.substring(0, indexOfNSSep);
/*      */         }
/*      */         else
/*      */         {
/*  660 */           return namespaceOfPrefix;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  665 */         prefix = indexOfNSSep >= 0 ? nodeName.substring(0, indexOfNSSep) : "";
/*      */       }
/*      */ 
/*  669 */       boolean ancestorsHaveXMLNS = false;
/*  670 */       boolean nHasXMLNS = false;
/*      */ 
/*  672 */       if (prefix.equals("xml"))
/*      */       {
/*  674 */         namespaceOfPrefix = "http://www.w3.org/XML/1998/namespace";
/*      */       }
/*      */       else
/*      */       {
/*  679 */         Node parent = n;
/*      */ 
/*  681 */         while ((null != parent) && (null == namespaceOfPrefix))
/*      */         {
/*  683 */           if ((null != nsInfo) && (nsInfo.m_ancestorHasXMLNSAttrs == 2))
/*      */           {
/*      */             break;
/*      */           }
/*      */ 
/*  690 */           int parentType = parent.getNodeType();
/*      */ 
/*  692 */           if ((null == nsInfo) || (nsInfo.m_hasXMLNSAttrs))
/*      */           {
/*  694 */             boolean elementHasXMLNS = false;
/*      */ 
/*  696 */             if (parentType == 1)
/*      */             {
/*  698 */               NamedNodeMap nnm = parent.getAttributes();
/*      */ 
/*  700 */               for (int i = 0; i < nnm.getLength(); i++)
/*      */               {
/*  702 */                 Node attr = nnm.item(i);
/*  703 */                 String aname = attr.getNodeName();
/*      */ 
/*  705 */                 if (aname.charAt(0) == 'x')
/*      */                 {
/*  707 */                   boolean isPrefix = aname.startsWith("xmlns:");
/*      */ 
/*  709 */                   if ((aname.equals("xmlns")) || (isPrefix))
/*      */                   {
/*  711 */                     if (n == parent) {
/*  712 */                       nHasXMLNS = true;
/*      */                     }
/*  714 */                     elementHasXMLNS = true;
/*  715 */                     ancestorsHaveXMLNS = true;
/*      */ 
/*  717 */                     String p = isPrefix ? aname.substring(6) : "";
/*      */ 
/*  719 */                     if (p.equals(prefix))
/*      */                     {
/*  721 */                       namespaceOfPrefix = attr.getNodeValue();
/*      */ 
/*  723 */                       break;
/*      */                     }
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */ 
/*  730 */             if ((2 != parentType) && (null == nsInfo) && (n != parent))
/*      */             {
/*  733 */               nsInfo = elementHasXMLNS ? m_NSInfoUnProcWithXMLNS : m_NSInfoUnProcWithoutXMLNS;
/*      */ 
/*  736 */               this.m_NSInfos.put(parent, nsInfo);
/*      */             }
/*      */           }
/*      */ 
/*  740 */           if (2 == parentType)
/*      */           {
/*  742 */             parent = getParentOfNode(parent);
/*      */           }
/*      */           else
/*      */           {
/*  746 */             this.m_candidateNoAncestorXMLNS.addElement(parent);
/*  747 */             this.m_candidateNoAncestorXMLNS.addElement(nsInfo);
/*      */ 
/*  749 */             parent = parent.getParentNode();
/*      */           }
/*      */ 
/*  752 */           if (null != parent)
/*      */           {
/*  754 */             Object nsObj = this.m_NSInfos.get(parent);
/*      */ 
/*  756 */             nsInfo = nsObj == null ? null : (NSInfo)nsObj;
/*      */           }
/*      */         }
/*      */ 
/*  760 */         int nCandidates = this.m_candidateNoAncestorXMLNS.size();
/*      */ 
/*  762 */         if (nCandidates > 0)
/*      */         {
/*  764 */           if ((false == ancestorsHaveXMLNS) && (null == parent))
/*      */           {
/*  766 */             for (int i = 0; i < nCandidates; i += 2)
/*      */             {
/*  768 */               Object candidateInfo = this.m_candidateNoAncestorXMLNS.elementAt(i + 1);
/*      */ 
/*  771 */               if (candidateInfo == m_NSInfoUnProcWithoutXMLNS)
/*      */               {
/*  773 */                 this.m_NSInfos.put(this.m_candidateNoAncestorXMLNS.elementAt(i), m_NSInfoUnProcNoAncestorXMLNS);
/*      */               }
/*  776 */               else if (candidateInfo == m_NSInfoNullWithoutXMLNS)
/*      */               {
/*  778 */                 this.m_NSInfos.put(this.m_candidateNoAncestorXMLNS.elementAt(i), m_NSInfoNullNoAncestorXMLNS);
/*      */               }
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*  784 */           this.m_candidateNoAncestorXMLNS.removeAllElements();
/*      */         }
/*      */       }
/*      */ 
/*  788 */       if (2 != ntype)
/*      */       {
/*  790 */         if (null == namespaceOfPrefix)
/*      */         {
/*  792 */           if (ancestorsHaveXMLNS)
/*      */           {
/*  794 */             if (nHasXMLNS)
/*  795 */               this.m_NSInfos.put(n, m_NSInfoNullWithXMLNS);
/*      */             else {
/*  797 */               this.m_NSInfos.put(n, m_NSInfoNullWithoutXMLNS);
/*      */             }
/*      */           }
/*      */           else {
/*  801 */             this.m_NSInfos.put(n, m_NSInfoNullNoAncestorXMLNS);
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  806 */           this.m_NSInfos.put(n, new NSInfo(namespaceOfPrefix, nHasXMLNS));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  811 */     return namespaceOfPrefix;
/*      */   }
/*      */ 
/*      */   public String getLocalNameOfNode(Node n)
/*      */   {
/*  826 */     String qname = n.getNodeName();
/*  827 */     int index = qname.indexOf(':');
/*      */ 
/*  829 */     return index < 0 ? qname : qname.substring(index + 1);
/*      */   }
/*      */ 
/*      */   public String getExpandedElementName(Element elem)
/*      */   {
/*  847 */     String namespace = getNamespaceOfNode(elem);
/*      */ 
/*  849 */     return null != namespace ? namespace + ":" + getLocalNameOfNode(elem) : getLocalNameOfNode(elem);
/*      */   }
/*      */ 
/*      */   public String getExpandedAttributeName(Attr attr)
/*      */   {
/*  869 */     String namespace = getNamespaceOfNode(attr);
/*      */ 
/*  871 */     return null != namespace ? namespace + ":" + getLocalNameOfNode(attr) : getLocalNameOfNode(attr);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public boolean isIgnorableWhitespace(Text node)
/*      */   {
/*  898 */     boolean isIgnorable = false;
/*      */ 
/*  906 */     return isIgnorable;
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public Node getRoot(Node node)
/*      */   {
/*  920 */     Node root = null;
/*      */ 
/*  922 */     while (node != null)
/*      */     {
/*  924 */       root = node;
/*  925 */       node = getParentOfNode(node);
/*      */     }
/*      */ 
/*  928 */     return root;
/*      */   }
/*      */ 
/*      */   public Node getRootNode(Node n)
/*      */   {
/*  951 */     int nt = n.getNodeType();
/*  952 */     return (9 == nt) || (11 == nt) ? n : n.getOwnerDocument();
/*      */   }
/*      */ 
/*      */   public boolean isNamespaceNode(Node n)
/*      */   {
/*  969 */     if (2 == n.getNodeType())
/*      */     {
/*  971 */       String attrName = n.getNodeName();
/*      */ 
/*  973 */       return (attrName.startsWith("xmlns:")) || (attrName.equals("xmlns"));
/*      */     }
/*      */ 
/*  976 */     return false;
/*      */   }
/*      */ 
/*      */   public static Node getParentOfNode(Node node)
/*      */     throws RuntimeException
/*      */   {
/* 1008 */     short nodeType = node.getNodeType();
/*      */     Node parent;
/*      */     Node parent;
/* 1010 */     if (2 == nodeType)
/*      */     {
/* 1012 */       Document doc = node.getOwnerDocument();
/*      */ 
/* 1030 */       DOMImplementation impl = doc.getImplementation();
/* 1031 */       if ((impl != null) && (impl.hasFeature("Core", "2.0")))
/*      */       {
/* 1033 */         Node parent = ((Attr)node).getOwnerElement();
/* 1034 */         return parent;
/*      */       }
/*      */ 
/* 1039 */       Element rootElem = doc.getDocumentElement();
/*      */ 
/* 1041 */       if (null == rootElem)
/*      */       {
/* 1043 */         throw new RuntimeException(XMLMessages.createXMLMessage("ER_CHILD_HAS_NO_OWNER_DOCUMENT_ELEMENT", null));
/*      */       }
/*      */ 
/* 1049 */       parent = locateAttrParent(rootElem, node);
/*      */     }
/*      */     else
/*      */     {
/* 1054 */       parent = node.getParentNode();
/*      */     }
/*      */ 
/* 1062 */     return parent;
/*      */   }
/*      */ 
/*      */   public Element getElementByID(String id, Document doc)
/*      */   {
/* 1085 */     return null;
/*      */   }
/*      */ 
/*      */   public String getUnparsedEntityURI(String name, Document doc)
/*      */   {
/* 1126 */     String url = "";
/* 1127 */     DocumentType doctype = doc.getDoctype();
/*      */ 
/* 1129 */     if (null != doctype)
/*      */     {
/* 1131 */       NamedNodeMap entities = doctype.getEntities();
/* 1132 */       if (null == entities)
/* 1133 */         return url;
/* 1134 */       Entity entity = (Entity)entities.getNamedItem(name);
/* 1135 */       if (null == entity) {
/* 1136 */         return url;
/*      */       }
/* 1138 */       String notationName = entity.getNotationName();
/*      */ 
/* 1140 */       if (null != notationName)
/*      */       {
/* 1151 */         url = entity.getSystemId();
/*      */ 
/* 1153 */         if (null == url)
/*      */         {
/* 1155 */           url = entity.getPublicId();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1165 */     return url;
/*      */   }
/*      */ 
/*      */   private static Node locateAttrParent(Element elem, Node attr)
/*      */   {
/* 1192 */     Node parent = null;
/*      */ 
/* 1199 */     Attr check = elem.getAttributeNode(attr.getNodeName());
/* 1200 */     if (check == attr) {
/* 1201 */       parent = elem;
/*      */     }
/* 1203 */     if (null == parent)
/*      */     {
/* 1205 */       for (Node node = elem.getFirstChild(); null != node; 
/* 1206 */         node = node.getNextSibling())
/*      */       {
/* 1208 */         if (1 == node.getNodeType())
/*      */         {
/* 1210 */           parent = locateAttrParent((Element)node, attr);
/*      */ 
/* 1212 */           if (null != parent) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1218 */     return parent;
/*      */   }
/*      */ 
/*      */   public void setDOMFactory(Document domFactory)
/*      */   {
/* 1237 */     this.m_DOMFactory = domFactory;
/*      */   }
/*      */ 
/*      */   public Document getDOMFactory()
/*      */   {
/* 1249 */     if (null == this.m_DOMFactory)
/*      */     {
/* 1251 */       this.m_DOMFactory = createDocument();
/*      */     }
/*      */ 
/* 1254 */     return this.m_DOMFactory;
/*      */   }
/*      */ 
/*      */   public static String getNodeData(Node node)
/*      */   {
/* 1271 */     FastStringBuffer buf = StringBufferPool.get();
/*      */     String s;
/*      */     try
/*      */     {
/* 1276 */       getNodeData(node, buf);
/*      */ 
/* 1278 */       s = buf.length() > 0 ? buf.toString() : "";
/*      */     }
/*      */     finally
/*      */     {
/* 1282 */       StringBufferPool.free(buf);
/*      */     }
/*      */ 
/* 1285 */     return s;
/*      */   }
/*      */ 
/*      */   public static void getNodeData(Node node, FastStringBuffer buf)
/*      */   {
/* 1308 */     switch (node.getNodeType())
/*      */     {
/*      */     case 1:
/*      */     case 9:
/*      */     case 11:
/* 1314 */       for (Node child = node.getFirstChild(); null != child; 
/* 1315 */         child = child.getNextSibling())
/*      */       {
/* 1317 */         getNodeData(child, buf);
/*      */       }
/*      */ 
/* 1320 */       break;
/*      */     case 3:
/*      */     case 4:
/* 1323 */       buf.append(node.getNodeValue());
/* 1324 */       break;
/*      */     case 2:
/* 1326 */       buf.append(node.getNodeValue());
/* 1327 */       break;
/*      */     case 7:
/* 1330 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 8:
/*      */     case 10:
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.DOMHelper
 * JD-Core Version:    0.6.2
 */