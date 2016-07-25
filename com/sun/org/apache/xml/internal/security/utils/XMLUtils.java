/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ 
/*     */ public class XMLUtils
/*     */ {
/*  56 */   private static boolean ignoreLineBreaks = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Boolean run() {
/*  59 */       return Boolean.valueOf(Boolean.getBoolean("com.sun.org.apache.xml.internal.security.ignoreLineBreaks"));
/*     */     }
/*     */   })).booleanValue();
/*     */ 
/*  64 */   private static volatile String dsPrefix = "ds";
/*  65 */   private static volatile String xencPrefix = "xenc";
/*     */ 
/*  67 */   private static final Logger log = Logger.getLogger(XMLUtils.class.getName());
/*     */ 
/* 263 */   static Map<String, String> namePrefixes = new HashMap();
/*     */ 
/*     */   public static void setDsPrefix(String paramString)
/*     */   {
/*  86 */     JavaUtils.checkRegisterPermission();
/*  87 */     dsPrefix = paramString;
/*     */   }
/*     */ 
/*     */   public static void setXencPrefix(String paramString)
/*     */   {
/*  97 */     JavaUtils.checkRegisterPermission();
/*  98 */     xencPrefix = paramString;
/*     */   }
/*     */ 
/*     */   public static Element getNextElement(Node paramNode) {
/* 102 */     while ((paramNode != null) && (paramNode.getNodeType() != 1)) {
/* 103 */       paramNode = paramNode.getNextSibling();
/*     */     }
/* 105 */     return (Element)paramNode;
/*     */   }
/*     */ 
/*     */   public static void getSet(Node paramNode1, Set<Node> paramSet, Node paramNode2, boolean paramBoolean)
/*     */   {
/* 116 */     if ((paramNode2 != null) && (isDescendantOrSelf(paramNode2, paramNode1))) {
/* 117 */       return;
/*     */     }
/* 119 */     getSetRec(paramNode1, paramSet, paramNode2, paramBoolean);
/*     */   }
/*     */ 
/*     */   static final void getSetRec(Node paramNode1, Set<Node> paramSet, Node paramNode2, boolean paramBoolean)
/*     */   {
/* 126 */     if (paramNode1 == paramNode2)
/*     */       return;
/*     */     Object localObject;
/* 129 */     switch (paramNode1.getNodeType()) {
/*     */     case 1:
/* 131 */       paramSet.add(paramNode1);
/* 132 */       Element localElement = (Element)paramNode1;
/* 133 */       if (localElement.hasAttributes()) {
/* 134 */         localObject = ((Element)paramNode1).getAttributes();
/* 135 */         for (int i = 0; i < ((NamedNodeMap)localObject).getLength(); i++) {
/* 136 */           paramSet.add(((NamedNodeMap)localObject).item(i));
/*     */         }
/*     */       }
/*     */ 
/*     */     case 9:
/* 141 */       for (localObject = paramNode1.getFirstChild(); localObject != null; localObject = ((Node)localObject).getNextSibling()) {
/* 142 */         if (((Node)localObject).getNodeType() == 3) {
/* 143 */           paramSet.add(localObject);
/* 144 */           while ((localObject != null) && (((Node)localObject).getNodeType() == 3)) {
/* 145 */             localObject = ((Node)localObject).getNextSibling();
/*     */           }
/* 147 */           if (localObject == null)
/* 148 */             return;
/*     */         }
/* 150 */         getSetRec((Node)localObject, paramSet, paramNode2, paramBoolean);
/*     */       }
/* 152 */       return;
/*     */     case 8:
/* 154 */       if (paramBoolean) {
/* 155 */         paramSet.add(paramNode1);
/*     */       }
/* 157 */       return;
/*     */     case 10:
/* 159 */       return;
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/* 161 */     case 7: } paramSet.add(paramNode1);
/*     */   }
/*     */ 
/*     */   public static void outputDOM(Node paramNode, OutputStream paramOutputStream)
/*     */   {
/* 174 */     outputDOM(paramNode, paramOutputStream, false);
/*     */   }
/*     */ 
/*     */   public static void outputDOM(Node paramNode, OutputStream paramOutputStream, boolean paramBoolean)
/*     */   {
/*     */     try
/*     */     {
/* 190 */       if (paramBoolean) {
/* 191 */         paramOutputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
/*     */       }
/*     */ 
/* 194 */       paramOutputStream.write(Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments").canonicalizeSubtree(paramNode));
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */     catch (InvalidCanonicalizerException localInvalidCanonicalizerException) {
/* 200 */       localInvalidCanonicalizerException.printStackTrace();
/*     */     } catch (CanonicalizationException localCanonicalizationException) {
/* 202 */       localCanonicalizationException.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void outputDOMc14nWithComments(Node paramNode, OutputStream paramOutputStream)
/*     */   {
/*     */     try
/*     */     {
/* 223 */       paramOutputStream.write(Canonicalizer.getInstance("http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments").canonicalizeSubtree(paramNode));
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */     catch (InvalidCanonicalizerException localInvalidCanonicalizerException)
/*     */     {
/*     */     }
/*     */     catch (CanonicalizationException localCanonicalizationException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getFullTextChildrenFromElement(Element paramElement)
/*     */   {
/* 248 */     StringBuffer localStringBuffer = new StringBuffer();
/* 249 */     NodeList localNodeList = paramElement.getChildNodes();
/* 250 */     int i = localNodeList.getLength();
/*     */ 
/* 252 */     for (int j = 0; j < i; j++) {
/* 253 */       Node localNode = localNodeList.item(j);
/*     */ 
/* 255 */       if (localNode.getNodeType() == 3) {
/* 256 */         localStringBuffer.append(((Text)localNode).getData());
/*     */       }
/*     */     }
/*     */ 
/* 260 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static Element createElementInSignatureSpace(Document paramDocument, String paramString)
/*     */   {
/* 275 */     if (paramDocument == null) {
/* 276 */       throw new RuntimeException("Document is null");
/*     */     }
/*     */ 
/* 279 */     if ((dsPrefix == null) || (dsPrefix.length() == 0)) {
/* 280 */       return paramDocument.createElementNS("http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */     }
/* 282 */     String str = (String)namePrefixes.get(paramString);
/* 283 */     if (str == null) {
/* 284 */       StringBuffer localStringBuffer = new StringBuffer(dsPrefix);
/* 285 */       localStringBuffer.append(':');
/* 286 */       localStringBuffer.append(paramString);
/* 287 */       str = localStringBuffer.toString();
/* 288 */       namePrefixes.put(paramString, str);
/*     */     }
/* 290 */     return paramDocument.createElementNS("http://www.w3.org/2000/09/xmldsig#", str);
/*     */   }
/*     */ 
/*     */   public static boolean elementIsInSignatureSpace(Element paramElement, String paramString)
/*     */   {
/* 302 */     if (paramElement == null) {
/* 303 */       return false;
/*     */     }
/*     */ 
/* 306 */     return ("http://www.w3.org/2000/09/xmldsig#".equals(paramElement.getNamespaceURI())) && (paramElement.getLocalName().equals(paramString));
/*     */   }
/*     */ 
/*     */   public static boolean elementIsInEncryptionSpace(Element paramElement, String paramString)
/*     */   {
/* 319 */     if (paramElement == null) {
/* 320 */       return false;
/*     */     }
/* 322 */     return ("http://www.w3.org/2001/04/xmlenc#".equals(paramElement.getNamespaceURI())) && (paramElement.getLocalName().equals(paramString));
/*     */   }
/*     */ 
/*     */   public static Document getOwnerDocument(Node paramNode)
/*     */   {
/* 337 */     if (paramNode.getNodeType() == 9)
/* 338 */       return (Document)paramNode;
/*     */     try
/*     */     {
/* 341 */       return paramNode.getOwnerDocument();
/*     */     } catch (NullPointerException localNullPointerException) {
/* 343 */       throw new NullPointerException(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + localNullPointerException.getMessage() + "\"");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Document getOwnerDocument(Set<Node> paramSet)
/*     */   {
/* 360 */     Object localObject = null;
/* 361 */     for (Node localNode : paramSet) {
/* 362 */       int i = localNode.getNodeType();
/* 363 */       if (i == 9)
/* 364 */         return (Document)localNode;
/*     */       try
/*     */       {
/* 367 */         if (i == 2) {
/* 368 */           return ((Attr)localNode).getOwnerElement().getOwnerDocument();
/*     */         }
/* 370 */         return localNode.getOwnerDocument();
/*     */       } catch (NullPointerException localNullPointerException) {
/* 372 */         localObject = localNullPointerException;
/*     */       }
/*     */     }
/*     */ 
/* 376 */     throw new NullPointerException(I18n.translate("endorsed.jdk1.4.0") + " Original message was \"" + (localObject == null ? "" : localObject.getMessage()) + "\"");
/*     */   }
/*     */ 
/*     */   public static Element createDSctx(Document paramDocument, String paramString1, String paramString2)
/*     */   {
/* 392 */     if ((paramString1 == null) || (paramString1.trim().length() == 0)) {
/* 393 */       throw new IllegalArgumentException("You must supply a prefix");
/*     */     }
/*     */ 
/* 396 */     Element localElement = paramDocument.createElementNS(null, "namespaceContext");
/*     */ 
/* 398 */     localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + paramString1.trim(), paramString2);
/*     */ 
/* 401 */     return localElement;
/*     */   }
/*     */ 
/*     */   public static void addReturnToElement(Element paramElement)
/*     */   {
/* 411 */     if (!ignoreLineBreaks) {
/* 412 */       Document localDocument = paramElement.getOwnerDocument();
/* 413 */       paramElement.appendChild(localDocument.createTextNode("\n"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void addReturnToElement(Document paramDocument, HelperNodeList paramHelperNodeList) {
/* 418 */     if (!ignoreLineBreaks)
/* 419 */       paramHelperNodeList.appendChild(paramDocument.createTextNode("\n"));
/*     */   }
/*     */ 
/*     */   public static void addReturnBeforeChild(Element paramElement, Node paramNode)
/*     */   {
/* 424 */     if (!ignoreLineBreaks) {
/* 425 */       Document localDocument = paramElement.getOwnerDocument();
/* 426 */       paramElement.insertBefore(localDocument.createTextNode("\n"), paramNode);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Set<Node> convertNodelistToSet(NodeList paramNodeList)
/*     */   {
/* 438 */     if (paramNodeList == null) {
/* 439 */       return new HashSet();
/*     */     }
/*     */ 
/* 442 */     int i = paramNodeList.getLength();
/* 443 */     HashSet localHashSet = new HashSet(i);
/*     */ 
/* 445 */     for (int j = 0; j < i; j++) {
/* 446 */       localHashSet.add(paramNodeList.item(j));
/*     */     }
/*     */ 
/* 449 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   public static void circumventBug2650(Document paramDocument)
/*     */   {
/* 466 */     Element localElement = paramDocument.getDocumentElement();
/*     */ 
/* 469 */     Attr localAttr = localElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
/*     */ 
/* 472 */     if (localAttr == null) {
/* 473 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "");
/*     */     }
/*     */ 
/* 476 */     circumventBug2650internal(paramDocument);
/*     */   }
/*     */ 
/*     */   private static void circumventBug2650internal(Node paramNode)
/*     */   {
/* 487 */     Node localNode1 = null;
/* 488 */     Node localNode2 = null;
/*     */     while (true)
/*     */     {
/* 491 */       switch (paramNode.getNodeType()) {
/*     */       case 1:
/* 493 */         Element localElement1 = (Element)paramNode;
/* 494 */         if (localElement1.hasChildNodes())
/*     */         {
/* 496 */           if (localElement1.hasAttributes()) {
/* 497 */             NamedNodeMap localNamedNodeMap = localElement1.getAttributes();
/* 498 */             int i = localNamedNodeMap.getLength();
/*     */ 
/* 500 */             for (Node localNode3 = localElement1.getFirstChild(); localNode3 != null; 
/* 501 */               localNode3 = localNode3.getNextSibling())
/*     */             {
/* 503 */               if (localNode3.getNodeType() == 1)
/*     */               {
/* 506 */                 Element localElement2 = (Element)localNode3;
/*     */ 
/* 508 */                 for (int j = 0; j < i; j++) {
/* 509 */                   Attr localAttr = (Attr)localNamedNodeMap.item(j);
/* 510 */                   if ("http://www.w3.org/2000/xmlns/" == localAttr.getNamespaceURI())
/*     */                   {
/* 512 */                     if (!localElement2.hasAttributeNS("http://www.w3.org/2000/xmlns/", localAttr.getLocalName()))
/*     */                     {
/* 516 */                       localElement2.setAttributeNS("http://www.w3.org/2000/xmlns/", localAttr.getName(), localAttr.getNodeValue());
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         break;
/*     */       case 5:
/*     */       case 9:
/* 526 */         localNode1 = paramNode;
/* 527 */         localNode2 = paramNode.getFirstChild();
/*     */       }
/*     */ 
/* 530 */       while ((localNode2 == null) && (localNode1 != null)) {
/* 531 */         localNode2 = localNode1.getNextSibling();
/* 532 */         localNode1 = localNode1.getParentNode();
/*     */       }
/* 534 */       if (localNode2 == null) {
/* 535 */         return;
/*     */       }
/*     */ 
/* 538 */       paramNode = localNode2;
/* 539 */       localNode2 = paramNode.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Element selectDsNode(Node paramNode, String paramString, int paramInt)
/*     */   {
/* 550 */     while (paramNode != null) {
/* 551 */       if (("http://www.w3.org/2000/09/xmldsig#".equals(paramNode.getNamespaceURI())) && (paramNode.getLocalName().equals(paramString)))
/*     */       {
/* 553 */         if (paramInt == 0) {
/* 554 */           return (Element)paramNode;
/*     */         }
/* 556 */         paramInt--;
/*     */       }
/* 558 */       paramNode = paramNode.getNextSibling();
/*     */     }
/* 560 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element selectXencNode(Node paramNode, String paramString, int paramInt)
/*     */   {
/* 570 */     while (paramNode != null) {
/* 571 */       if (("http://www.w3.org/2001/04/xmlenc#".equals(paramNode.getNamespaceURI())) && (paramNode.getLocalName().equals(paramString)))
/*     */       {
/* 573 */         if (paramInt == 0) {
/* 574 */           return (Element)paramNode;
/*     */         }
/* 576 */         paramInt--;
/*     */       }
/* 578 */       paramNode = paramNode.getNextSibling();
/*     */     }
/* 580 */     return null;
/*     */   }
/*     */ 
/*     */   public static Text selectDsNodeText(Node paramNode, String paramString, int paramInt)
/*     */   {
/* 590 */     Object localObject = selectDsNode(paramNode, paramString, paramInt);
/* 591 */     if (localObject == null) {
/* 592 */       return null;
/*     */     }
/* 594 */     localObject = ((Node)localObject).getFirstChild();
/* 595 */     while ((localObject != null) && (((Node)localObject).getNodeType() != 3)) {
/* 596 */       localObject = ((Node)localObject).getNextSibling();
/*     */     }
/* 598 */     return (Text)localObject;
/*     */   }
/*     */ 
/*     */   public static Text selectNodeText(Node paramNode, String paramString1, String paramString2, int paramInt)
/*     */   {
/* 609 */     Object localObject = selectNode(paramNode, paramString1, paramString2, paramInt);
/* 610 */     if (localObject == null) {
/* 611 */       return null;
/*     */     }
/* 613 */     localObject = ((Node)localObject).getFirstChild();
/* 614 */     while ((localObject != null) && (((Node)localObject).getNodeType() != 3)) {
/* 615 */       localObject = ((Node)localObject).getNextSibling();
/*     */     }
/* 617 */     return (Text)localObject;
/*     */   }
/*     */ 
/*     */   public static Element selectNode(Node paramNode, String paramString1, String paramString2, int paramInt)
/*     */   {
/* 628 */     while (paramNode != null) {
/* 629 */       if ((paramNode.getNamespaceURI() != null) && (paramNode.getNamespaceURI().equals(paramString1)) && (paramNode.getLocalName().equals(paramString2)))
/*     */       {
/* 631 */         if (paramInt == 0) {
/* 632 */           return (Element)paramNode;
/*     */         }
/* 634 */         paramInt--;
/*     */       }
/* 636 */       paramNode = paramNode.getNextSibling();
/*     */     }
/* 638 */     return null;
/*     */   }
/*     */ 
/*     */   public static Element[] selectDsNodes(Node paramNode, String paramString)
/*     */   {
/* 647 */     return selectNodes(paramNode, "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */   }
/*     */ 
/*     */   public static Element[] selectNodes(Node paramNode, String paramString1, String paramString2)
/*     */   {
/* 657 */     ArrayList localArrayList = new ArrayList();
/* 658 */     while (paramNode != null) {
/* 659 */       if ((paramNode.getNamespaceURI() != null) && (paramNode.getNamespaceURI().equals(paramString1)) && (paramNode.getLocalName().equals(paramString2)))
/*     */       {
/* 661 */         localArrayList.add((Element)paramNode);
/*     */       }
/* 663 */       paramNode = paramNode.getNextSibling();
/*     */     }
/* 665 */     return (Element[])localArrayList.toArray(new Element[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public static Set<Node> excludeNodeFromSet(Node paramNode, Set<Node> paramSet)
/*     */   {
/* 674 */     HashSet localHashSet = new HashSet();
/* 675 */     Iterator localIterator = paramSet.iterator();
/*     */ 
/* 677 */     while (localIterator.hasNext()) {
/* 678 */       Node localNode = (Node)localIterator.next();
/*     */ 
/* 680 */       if (!isDescendantOrSelf(paramNode, localNode))
/*     */       {
/* 682 */         localHashSet.add(localNode);
/*     */       }
/*     */     }
/* 685 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   public static boolean isDescendantOrSelf(Node paramNode1, Node paramNode2)
/*     */   {
/* 698 */     if (paramNode1 == paramNode2) {
/* 699 */       return true;
/*     */     }
/*     */ 
/* 702 */     Object localObject = paramNode2;
/*     */     while (true)
/*     */     {
/* 705 */       if (localObject == null) {
/* 706 */         return false;
/*     */       }
/*     */ 
/* 709 */       if (localObject == paramNode1) {
/* 710 */         return true;
/*     */       }
/*     */ 
/* 713 */       if (((Node)localObject).getNodeType() == 2)
/* 714 */         localObject = ((Attr)localObject).getOwnerElement();
/*     */       else
/* 716 */         localObject = ((Node)localObject).getParentNode();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static boolean ignoreLineBreaks()
/*     */   {
/* 722 */     return ignoreLineBreaks;
/*     */   }
/*     */ 
/*     */   public static boolean protectAgainstWrappingAttack(Node paramNode, String paramString)
/*     */   {
/* 735 */     Node localNode1 = paramNode.getParentNode();
/* 736 */     Node localNode2 = null;
/* 737 */     Element localElement1 = null;
/*     */ 
/* 739 */     String str = paramString.trim();
/* 740 */     if (str.charAt(0) == '#') {
/* 741 */       str = str.substring(1);
/*     */     }
/*     */ 
/* 744 */     while (paramNode != null) {
/* 745 */       if (paramNode.getNodeType() == 1) {
/* 746 */         Element localElement2 = (Element)paramNode;
/*     */ 
/* 748 */         NamedNodeMap localNamedNodeMap = localElement2.getAttributes();
/* 749 */         if (localNamedNodeMap != null) {
/* 750 */           for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
/* 751 */             Attr localAttr = (Attr)localNamedNodeMap.item(i);
/* 752 */             if ((localAttr.isId()) && (str.equals(localAttr.getValue()))) {
/* 753 */               if (localElement1 == null)
/*     */               {
/* 755 */                 localElement1 = localAttr.getOwnerElement();
/*     */               } else {
/* 757 */                 log.log(Level.FINE, "Multiple elements with the same 'Id' attribute value!");
/* 758 */                 return false;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 765 */       localNode2 = paramNode;
/* 766 */       paramNode = paramNode.getFirstChild();
/*     */ 
/* 769 */       if (paramNode == null)
/*     */       {
/* 771 */         paramNode = localNode2.getNextSibling();
/*     */       }
/*     */ 
/* 776 */       while (paramNode == null) {
/* 777 */         localNode2 = localNode2.getParentNode();
/* 778 */         if (localNode2 == localNode1) {
/* 779 */           return true;
/*     */         }
/*     */ 
/* 782 */         paramNode = localNode2.getNextSibling();
/*     */       }
/*     */     }
/* 785 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean protectAgainstWrappingAttack(Node paramNode, Element paramElement, String paramString)
/*     */   {
/* 798 */     Node localNode1 = paramNode.getParentNode();
/* 799 */     Node localNode2 = null;
/*     */ 
/* 801 */     String str = paramString.trim();
/* 802 */     if (str.charAt(0) == '#') {
/* 803 */       str = str.substring(1);
/*     */     }
/*     */ 
/* 806 */     while (paramNode != null) {
/* 807 */       if (paramNode.getNodeType() == 1) {
/* 808 */         Element localElement = (Element)paramNode;
/*     */ 
/* 810 */         NamedNodeMap localNamedNodeMap = localElement.getAttributes();
/* 811 */         if (localNamedNodeMap != null) {
/* 812 */           for (int i = 0; i < localNamedNodeMap.getLength(); i++) {
/* 813 */             Attr localAttr = (Attr)localNamedNodeMap.item(i);
/* 814 */             if ((localAttr.isId()) && (str.equals(localAttr.getValue())) && (localElement != paramElement))
/*     */             {
/* 817 */               log.log(Level.FINE, "Multiple elements with the same 'Id' attribute value!");
/* 818 */               return false;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 824 */       localNode2 = paramNode;
/* 825 */       paramNode = paramNode.getFirstChild();
/*     */ 
/* 828 */       if (paramNode == null)
/*     */       {
/* 830 */         paramNode = localNode2.getNextSibling();
/*     */       }
/*     */ 
/* 835 */       while (paramNode == null) {
/* 836 */         localNode2 = localNode2.getParentNode();
/* 837 */         if (localNode2 == localNode1) {
/* 838 */           return true;
/*     */         }
/*     */ 
/* 841 */         paramNode = localNode2.getNextSibling();
/*     */       }
/*     */     }
/* 844 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.XMLUtils
 * JD-Core Version:    0.6.2
 */