/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.AttrImpl;
/*     */ import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.opti.ElementImpl;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Hashtable;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ls.LSException;
/*     */ 
/*     */ public class DOMUtil
/*     */ {
/*     */   public static void copyInto(Node src, Node dest)
/*     */     throws DOMException
/*     */   {
/*  70 */     Document factory = dest.getOwnerDocument();
/*  71 */     boolean domimpl = factory instanceof DocumentImpl;
/*     */ 
/*  74 */     Node start = src;
/*  75 */     Node parent = src;
/*  76 */     Node place = src;
/*     */ 
/*  79 */     while (place != null)
/*     */     {
/*  82 */       Node node = null;
/*  83 */       int type = place.getNodeType();
/*  84 */       switch (type) {
/*     */       case 4:
/*  86 */         node = factory.createCDATASection(place.getNodeValue());
/*  87 */         break;
/*     */       case 8:
/*  90 */         node = factory.createComment(place.getNodeValue());
/*  91 */         break;
/*     */       case 1:
/*  94 */         Element element = factory.createElement(place.getNodeName());
/*  95 */         node = element;
/*  96 */         NamedNodeMap attrs = place.getAttributes();
/*  97 */         int attrCount = attrs.getLength();
/*  98 */         for (int i = 0; i < attrCount; i++) {
/*  99 */           Attr attr = (Attr)attrs.item(i);
/* 100 */           String attrName = attr.getNodeName();
/* 101 */           String attrValue = attr.getNodeValue();
/* 102 */           element.setAttribute(attrName, attrValue);
/* 103 */           if ((domimpl) && (!attr.getSpecified())) {
/* 104 */             ((AttrImpl)element.getAttributeNode(attrName)).setSpecified(false);
/*     */           }
/*     */         }
/* 107 */         break;
/*     */       case 5:
/* 110 */         node = factory.createEntityReference(place.getNodeName());
/* 111 */         break;
/*     */       case 7:
/* 114 */         node = factory.createProcessingInstruction(place.getNodeName(), place.getNodeValue());
/*     */ 
/* 116 */         break;
/*     */       case 3:
/* 119 */         node = factory.createTextNode(place.getNodeValue());
/* 120 */         break;
/*     */       case 2:
/*     */       case 6:
/*     */       default:
/* 123 */         throw new IllegalArgumentException("can't copy node type, " + type + " (" + place.getNodeName() + ')');
/*     */       }
/*     */ 
/* 128 */       dest.appendChild(node);
/*     */ 
/* 131 */       if (place.hasChildNodes()) {
/* 132 */         parent = place;
/* 133 */         place = place.getFirstChild();
/* 134 */         dest = node;
/*     */       }
/*     */       else
/*     */       {
/* 139 */         place = place.getNextSibling();
/* 140 */         while ((place == null) && (parent != start)) {
/* 141 */           place = parent.getNextSibling();
/* 142 */           parent = parent.getParentNode();
/* 143 */           dest = dest.getParentNode();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Element getFirstChildElement(Node parent)
/*     */   {
/* 155 */     Node child = parent.getFirstChild();
/* 156 */     while (child != null) {
/* 157 */       if (child.getNodeType() == 1) {
/* 158 */         return (Element)child;
/*     */       }
/* 160 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getFirstVisibleChildElement(Node parent)
/*     */   {
/* 172 */     Node child = parent.getFirstChild();
/* 173 */     while (child != null) {
/* 174 */       if ((child.getNodeType() == 1) && (!isHidden(child)))
/*     */       {
/* 176 */         return (Element)child;
/*     */       }
/* 178 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 182 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getFirstVisibleChildElement(Node parent, Hashtable hiddenNodes)
/*     */   {
/* 190 */     Node child = parent.getFirstChild();
/* 191 */     while (child != null) {
/* 192 */       if ((child.getNodeType() == 1) && (!isHidden(child, hiddenNodes)))
/*     */       {
/* 194 */         return (Element)child;
/*     */       }
/* 196 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 200 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getLastChildElement(Node parent)
/*     */   {
/* 210 */     Node child = parent.getLastChild();
/* 211 */     while (child != null) {
/* 212 */       if (child.getNodeType() == 1) {
/* 213 */         return (Element)child;
/*     */       }
/* 215 */       child = child.getPreviousSibling();
/*     */     }
/*     */ 
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getLastVisibleChildElement(Node parent)
/*     */   {
/* 227 */     Node child = parent.getLastChild();
/* 228 */     while (child != null) {
/* 229 */       if ((child.getNodeType() == 1) && (!isHidden(child)))
/*     */       {
/* 231 */         return (Element)child;
/*     */       }
/* 233 */       child = child.getPreviousSibling();
/*     */     }
/*     */ 
/* 237 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getLastVisibleChildElement(Node parent, Hashtable hiddenNodes)
/*     */   {
/* 247 */     Node child = parent.getLastChild();
/* 248 */     while (child != null) {
/* 249 */       if ((child.getNodeType() == 1) && (!isHidden(child, hiddenNodes)))
/*     */       {
/* 251 */         return (Element)child;
/*     */       }
/* 253 */       child = child.getPreviousSibling();
/*     */     }
/*     */ 
/* 257 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getNextSiblingElement(Node node)
/*     */   {
/* 264 */     Node sibling = node.getNextSibling();
/* 265 */     while (sibling != null) {
/* 266 */       if (sibling.getNodeType() == 1) {
/* 267 */         return (Element)sibling;
/*     */       }
/* 269 */       sibling = sibling.getNextSibling();
/*     */     }
/*     */ 
/* 273 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getNextVisibleSiblingElement(Node node)
/*     */   {
/* 281 */     Node sibling = node.getNextSibling();
/* 282 */     while (sibling != null) {
/* 283 */       if ((sibling.getNodeType() == 1) && (!isHidden(sibling)))
/*     */       {
/* 285 */         return (Element)sibling;
/*     */       }
/* 287 */       sibling = sibling.getNextSibling();
/*     */     }
/*     */ 
/* 291 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getNextVisibleSiblingElement(Node node, Hashtable hiddenNodes)
/*     */   {
/* 299 */     Node sibling = node.getNextSibling();
/* 300 */     while (sibling != null) {
/* 301 */       if ((sibling.getNodeType() == 1) && (!isHidden(sibling, hiddenNodes)))
/*     */       {
/* 303 */         return (Element)sibling;
/*     */       }
/* 305 */       sibling = sibling.getNextSibling();
/*     */     }
/*     */ 
/* 309 */     return null;
/*     */   }
/*     */ 
/*     */   public static void setHidden(Node node)
/*     */   {
/* 315 */     if ((node instanceof com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl))
/* 316 */       ((com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)node).setReadOnly(true, false);
/* 317 */     else if ((node instanceof com.sun.org.apache.xerces.internal.dom.NodeImpl))
/* 318 */       ((com.sun.org.apache.xerces.internal.dom.NodeImpl)node).setReadOnly(true, false);
/*     */   }
/*     */ 
/*     */   public static void setHidden(Node node, Hashtable hiddenNodes)
/*     */   {
/* 323 */     if ((node instanceof com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)) {
/* 324 */       ((com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)node).setReadOnly(true, false);
/*     */     }
/*     */     else
/* 327 */       hiddenNodes.put(node, "");
/*     */   }
/*     */ 
/*     */   public static void setVisible(Node node)
/*     */   {
/* 333 */     if ((node instanceof com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl))
/* 334 */       ((com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)node).setReadOnly(false, false);
/* 335 */     else if ((node instanceof com.sun.org.apache.xerces.internal.dom.NodeImpl))
/* 336 */       ((com.sun.org.apache.xerces.internal.dom.NodeImpl)node).setReadOnly(false, false);
/*     */   }
/*     */ 
/*     */   public static void setVisible(Node node, Hashtable hiddenNodes)
/*     */   {
/* 341 */     if ((node instanceof com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)) {
/* 342 */       ((com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)node).setReadOnly(false, false);
/*     */     }
/*     */     else
/* 345 */       hiddenNodes.remove(node);
/*     */   }
/*     */ 
/*     */   public static boolean isHidden(Node node)
/*     */   {
/* 351 */     if ((node instanceof com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl))
/* 352 */       return ((com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)node).getReadOnly();
/* 353 */     if ((node instanceof com.sun.org.apache.xerces.internal.dom.NodeImpl))
/* 354 */       return ((com.sun.org.apache.xerces.internal.dom.NodeImpl)node).getReadOnly();
/* 355 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isHidden(Node node, Hashtable hiddenNodes)
/*     */   {
/* 360 */     if ((node instanceof com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)) {
/* 361 */       return ((com.sun.org.apache.xerces.internal.impl.xs.opti.NodeImpl)node).getReadOnly();
/*     */     }
/*     */ 
/* 364 */     return hiddenNodes.containsKey(node);
/*     */   }
/*     */ 
/*     */   public static Element getFirstChildElement(Node parent, String elemName)
/*     */   {
/* 372 */     Node child = parent.getFirstChild();
/* 373 */     while (child != null) {
/* 374 */       if ((child.getNodeType() == 1) && 
/* 375 */         (child.getNodeName().equals(elemName))) {
/* 376 */         return (Element)child;
/*     */       }
/*     */ 
/* 379 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 383 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getLastChildElement(Node parent, String elemName)
/*     */   {
/* 391 */     Node child = parent.getLastChild();
/* 392 */     while (child != null) {
/* 393 */       if ((child.getNodeType() == 1) && 
/* 394 */         (child.getNodeName().equals(elemName))) {
/* 395 */         return (Element)child;
/*     */       }
/*     */ 
/* 398 */       child = child.getPreviousSibling();
/*     */     }
/*     */ 
/* 402 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getNextSiblingElement(Node node, String elemName)
/*     */   {
/* 410 */     Node sibling = node.getNextSibling();
/* 411 */     while (sibling != null) {
/* 412 */       if ((sibling.getNodeType() == 1) && 
/* 413 */         (sibling.getNodeName().equals(elemName))) {
/* 414 */         return (Element)sibling;
/*     */       }
/*     */ 
/* 417 */       sibling = sibling.getNextSibling();
/*     */     }
/*     */ 
/* 421 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getFirstChildElementNS(Node parent, String uri, String localpart)
/*     */   {
/* 430 */     Node child = parent.getFirstChild();
/* 431 */     while (child != null) {
/* 432 */       if (child.getNodeType() == 1) {
/* 433 */         String childURI = child.getNamespaceURI();
/* 434 */         if ((childURI != null) && (childURI.equals(uri)) && (child.getLocalName().equals(localpart)))
/*     */         {
/* 436 */           return (Element)child;
/*     */         }
/*     */       }
/* 439 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 443 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getLastChildElementNS(Node parent, String uri, String localpart)
/*     */   {
/* 452 */     Node child = parent.getLastChild();
/* 453 */     while (child != null) {
/* 454 */       if (child.getNodeType() == 1) {
/* 455 */         String childURI = child.getNamespaceURI();
/* 456 */         if ((childURI != null) && (childURI.equals(uri)) && (child.getLocalName().equals(localpart)))
/*     */         {
/* 458 */           return (Element)child;
/*     */         }
/*     */       }
/* 461 */       child = child.getPreviousSibling();
/*     */     }
/*     */ 
/* 465 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getNextSiblingElementNS(Node node, String uri, String localpart)
/*     */   {
/* 474 */     Node sibling = node.getNextSibling();
/* 475 */     while (sibling != null) {
/* 476 */       if (sibling.getNodeType() == 1) {
/* 477 */         String siblingURI = sibling.getNamespaceURI();
/* 478 */         if ((siblingURI != null) && (siblingURI.equals(uri)) && (sibling.getLocalName().equals(localpart)))
/*     */         {
/* 480 */           return (Element)sibling;
/*     */         }
/*     */       }
/* 483 */       sibling = sibling.getNextSibling();
/*     */     }
/*     */ 
/* 487 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getFirstChildElement(Node parent, String[] elemNames)
/*     */   {
/* 495 */     Node child = parent.getFirstChild();
/* 496 */     while (child != null) {
/* 497 */       if (child.getNodeType() == 1) {
/* 498 */         for (int i = 0; i < elemNames.length; i++) {
/* 499 */           if (child.getNodeName().equals(elemNames[i])) {
/* 500 */             return (Element)child;
/*     */           }
/*     */         }
/*     */       }
/* 504 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 508 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getLastChildElement(Node parent, String[] elemNames)
/*     */   {
/* 516 */     Node child = parent.getLastChild();
/* 517 */     while (child != null) {
/* 518 */       if (child.getNodeType() == 1) {
/* 519 */         for (int i = 0; i < elemNames.length; i++) {
/* 520 */           if (child.getNodeName().equals(elemNames[i])) {
/* 521 */             return (Element)child;
/*     */           }
/*     */         }
/*     */       }
/* 525 */       child = child.getPreviousSibling();
/*     */     }
/*     */ 
/* 529 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getNextSiblingElement(Node node, String[] elemNames)
/*     */   {
/* 537 */     Node sibling = node.getNextSibling();
/* 538 */     while (sibling != null) {
/* 539 */       if (sibling.getNodeType() == 1) {
/* 540 */         for (int i = 0; i < elemNames.length; i++) {
/* 541 */           if (sibling.getNodeName().equals(elemNames[i])) {
/* 542 */             return (Element)sibling;
/*     */           }
/*     */         }
/*     */       }
/* 546 */       sibling = sibling.getNextSibling();
/*     */     }
/*     */ 
/* 550 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getFirstChildElementNS(Node parent, String[][] elemNames)
/*     */   {
/* 559 */     Node child = parent.getFirstChild();
/* 560 */     while (child != null) {
/* 561 */       if (child.getNodeType() == 1) {
/* 562 */         for (int i = 0; i < elemNames.length; i++) {
/* 563 */           String uri = child.getNamespaceURI();
/* 564 */           if ((uri != null) && (uri.equals(elemNames[i][0])) && (child.getLocalName().equals(elemNames[i][1])))
/*     */           {
/* 566 */             return (Element)child;
/*     */           }
/*     */         }
/*     */       }
/* 570 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 574 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getLastChildElementNS(Node parent, String[][] elemNames)
/*     */   {
/* 583 */     Node child = parent.getLastChild();
/* 584 */     while (child != null) {
/* 585 */       if (child.getNodeType() == 1) {
/* 586 */         for (int i = 0; i < elemNames.length; i++) {
/* 587 */           String uri = child.getNamespaceURI();
/* 588 */           if ((uri != null) && (uri.equals(elemNames[i][0])) && (child.getLocalName().equals(elemNames[i][1])))
/*     */           {
/* 590 */             return (Element)child;
/*     */           }
/*     */         }
/*     */       }
/* 594 */       child = child.getPreviousSibling();
/*     */     }
/*     */ 
/* 598 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getNextSiblingElementNS(Node node, String[][] elemNames)
/*     */   {
/* 607 */     Node sibling = node.getNextSibling();
/* 608 */     while (sibling != null) {
/* 609 */       if (sibling.getNodeType() == 1) {
/* 610 */         for (int i = 0; i < elemNames.length; i++) {
/* 611 */           String uri = sibling.getNamespaceURI();
/* 612 */           if ((uri != null) && (uri.equals(elemNames[i][0])) && (sibling.getLocalName().equals(elemNames[i][1])))
/*     */           {
/* 614 */             return (Element)sibling;
/*     */           }
/*     */         }
/*     */       }
/* 618 */       sibling = sibling.getNextSibling();
/*     */     }
/*     */ 
/* 622 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getFirstChildElement(Node parent, String elemName, String attrName, String attrValue)
/*     */   {
/* 636 */     Node child = parent.getFirstChild();
/* 637 */     while (child != null) {
/* 638 */       if (child.getNodeType() == 1) {
/* 639 */         Element element = (Element)child;
/* 640 */         if ((element.getNodeName().equals(elemName)) && (element.getAttribute(attrName).equals(attrValue)))
/*     */         {
/* 642 */           return element;
/*     */         }
/*     */       }
/* 645 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 649 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getLastChildElement(Node parent, String elemName, String attrName, String attrValue)
/*     */   {
/* 663 */     Node child = parent.getLastChild();
/* 664 */     while (child != null) {
/* 665 */       if (child.getNodeType() == 1) {
/* 666 */         Element element = (Element)child;
/* 667 */         if ((element.getNodeName().equals(elemName)) && (element.getAttribute(attrName).equals(attrValue)))
/*     */         {
/* 669 */           return element;
/*     */         }
/*     */       }
/* 672 */       child = child.getPreviousSibling();
/*     */     }
/*     */ 
/* 676 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element getNextSiblingElement(Node node, String elemName, String attrName, String attrValue)
/*     */   {
/* 691 */     Node sibling = node.getNextSibling();
/* 692 */     while (sibling != null) {
/* 693 */       if (sibling.getNodeType() == 1) {
/* 694 */         Element element = (Element)sibling;
/* 695 */         if ((element.getNodeName().equals(elemName)) && (element.getAttribute(attrName).equals(attrValue)))
/*     */         {
/* 697 */           return element;
/*     */         }
/*     */       }
/* 700 */       sibling = sibling.getNextSibling();
/*     */     }
/*     */ 
/* 704 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getChildText(Node node)
/*     */   {
/* 720 */     if (node == null) {
/* 721 */       return null;
/*     */     }
/*     */ 
/* 725 */     StringBuffer str = new StringBuffer();
/* 726 */     Node child = node.getFirstChild();
/* 727 */     while (child != null) {
/* 728 */       short type = child.getNodeType();
/* 729 */       if (type == 3) {
/* 730 */         str.append(child.getNodeValue());
/*     */       }
/* 732 */       else if (type == 4) {
/* 733 */         str.append(getChildText(child));
/*     */       }
/* 735 */       child = child.getNextSibling();
/*     */     }
/*     */ 
/* 739 */     return str.toString();
/*     */   }
/*     */ 
/*     */   public static String getName(Node node)
/*     */   {
/* 745 */     return node.getNodeName();
/*     */   }
/*     */ 
/*     */   public static String getLocalName(Node node)
/*     */   {
/* 752 */     String name = node.getLocalName();
/* 753 */     return name != null ? name : node.getNodeName();
/*     */   }
/*     */ 
/*     */   public static Element getParent(Element elem) {
/* 757 */     Node parent = elem.getParentNode();
/* 758 */     if ((parent instanceof Element))
/* 759 */       return (Element)parent;
/* 760 */     return null;
/*     */   }
/*     */ 
/*     */   public static Document getDocument(Node node)
/*     */   {
/* 765 */     return node.getOwnerDocument();
/*     */   }
/*     */ 
/*     */   public static Element getRoot(Document doc)
/*     */   {
/* 770 */     return doc.getDocumentElement();
/*     */   }
/*     */ 
/*     */   public static Attr getAttr(Element elem, String name)
/*     */   {
/* 777 */     return elem.getAttributeNode(name);
/*     */   }
/*     */ 
/*     */   public static Attr getAttrNS(Element elem, String nsUri, String localName)
/*     */   {
/* 783 */     return elem.getAttributeNodeNS(nsUri, localName);
/*     */   }
/*     */ 
/*     */   public static Attr[] getAttrs(Element elem)
/*     */   {
/* 788 */     NamedNodeMap attrMap = elem.getAttributes();
/* 789 */     Attr[] attrArray = new Attr[attrMap.getLength()];
/* 790 */     for (int i = 0; i < attrMap.getLength(); i++)
/* 791 */       attrArray[i] = ((Attr)attrMap.item(i));
/* 792 */     return attrArray;
/*     */   }
/*     */ 
/*     */   public static String getValue(Attr attribute)
/*     */   {
/* 797 */     return attribute.getValue();
/*     */   }
/*     */ 
/*     */   public static String getAttrValue(Element elem, String name)
/*     */   {
/* 808 */     return elem.getAttribute(name);
/*     */   }
/*     */ 
/*     */   public static String getAttrValueNS(Element elem, String nsUri, String localName)
/*     */   {
/* 815 */     return elem.getAttributeNS(nsUri, localName);
/*     */   }
/*     */ 
/*     */   public static String getPrefix(Node node)
/*     */   {
/* 820 */     return node.getPrefix();
/*     */   }
/*     */ 
/*     */   public static String getNamespaceURI(Node node)
/*     */   {
/* 825 */     return node.getNamespaceURI();
/*     */   }
/*     */ 
/*     */   public static String getAnnotation(Node node)
/*     */   {
/* 830 */     if ((node instanceof ElementImpl)) {
/* 831 */       return ((ElementImpl)node).getAnnotation();
/*     */     }
/* 833 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getSyntheticAnnotation(Node node)
/*     */   {
/* 838 */     if ((node instanceof ElementImpl)) {
/* 839 */       return ((ElementImpl)node).getSyntheticAnnotation();
/*     */     }
/* 841 */     return null;
/*     */   }
/*     */ 
/*     */   public static DOMException createDOMException(short code, Throwable cause)
/*     */   {
/* 848 */     DOMException de = new DOMException(code, cause != null ? cause.getMessage() : null);
/* 849 */     if ((cause != null) && (ThrowableMethods.fgThrowableMethodsAvailable))
/*     */       try {
/* 851 */         ThrowableMethods.fgThrowableInitCauseMethod.invoke(de, new Object[] { cause });
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/* 856 */     return de;
/*     */   }
/*     */ 
/*     */   public static LSException createLSException(short code, Throwable cause)
/*     */   {
/* 863 */     LSException lse = new LSException(code, cause != null ? cause.getMessage() : null);
/* 864 */     if ((cause != null) && (ThrowableMethods.fgThrowableMethodsAvailable))
/*     */       try {
/* 866 */         ThrowableMethods.fgThrowableInitCauseMethod.invoke(lse, new Object[] { cause });
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/* 871 */     return lse;
/*     */   }
/*     */ 
/*     */   static class ThrowableMethods
/*     */   {
/* 880 */     private static Method fgThrowableInitCauseMethod = null;
/*     */ 
/* 883 */     private static boolean fgThrowableMethodsAvailable = false;
/*     */ 
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/* 890 */         fgThrowableInitCauseMethod = Throwable.class.getMethod("initCause", new Class[] { Throwable.class });
/* 891 */         fgThrowableMethodsAvailable = true;
/*     */       }
/*     */       catch (Exception exc)
/*     */       {
/* 896 */         fgThrowableInitCauseMethod = null;
/* 897 */         fgThrowableMethodsAvailable = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.DOMUtil
 * JD-Core Version:    0.6.2
 */