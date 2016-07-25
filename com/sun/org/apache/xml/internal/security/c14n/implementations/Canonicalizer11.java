/*     */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.helper.C14nHelper;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class Canonicalizer11 extends CanonicalizerBase
/*     */ {
/*  61 */   boolean firstCall = true;
/*  62 */   final SortedSet result = new TreeSet(COMPARE);
/*     */   static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/";
/*     */   static final String XML_LANG_URI = "http://www.w3.org/XML/1998/namespace";
/*  66 */   static Logger log = Logger.getLogger(Canonicalizer11.class.getName());
/*     */ 
/* 179 */   XmlAttrStack xmlattrStack = new XmlAttrStack();
/*     */ 
/*     */   public Canonicalizer11(boolean paramBoolean)
/*     */   {
/* 187 */     super(paramBoolean);
/*     */   }
/*     */ 
/*     */   Iterator handleAttributesSubtree(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */     throws CanonicalizationException
/*     */   {
/* 207 */     if ((!paramElement.hasAttributes()) && (!this.firstCall)) {
/* 208 */       return null;
/*     */     }
/*     */ 
/* 211 */     SortedSet localSortedSet = this.result;
/* 212 */     localSortedSet.clear();
/* 213 */     NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/* 214 */     int i = localNamedNodeMap.getLength();
/*     */ 
/* 216 */     for (int j = 0; j < i; j++) {
/* 217 */       Attr localAttr = (Attr)localNamedNodeMap.item(j);
/* 218 */       String str1 = localAttr.getNamespaceURI();
/*     */ 
/* 220 */       if ("http://www.w3.org/2000/xmlns/" != str1)
/*     */       {
/* 223 */         localSortedSet.add(localAttr);
/*     */       }
/*     */       else
/*     */       {
/* 227 */         String str2 = localAttr.getLocalName();
/* 228 */         String str3 = localAttr.getValue();
/* 229 */         if ((!"xml".equals(str2)) || (!"http://www.w3.org/XML/1998/namespace".equals(str3)))
/*     */         {
/* 235 */           Node localNode = paramNameSpaceSymbTable.addMappingAndRender(str2, str3, localAttr);
/*     */ 
/* 237 */           if (localNode != null)
/*     */           {
/* 239 */             localSortedSet.add(localNode);
/* 240 */             if (C14nHelper.namespaceIsRelative(localAttr)) {
/* 241 */               Object[] arrayOfObject = { paramElement.getTagName(), str2, localAttr.getNodeValue() };
/* 242 */               throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 248 */     if (this.firstCall)
/*     */     {
/* 252 */       paramNameSpaceSymbTable.getUnrenderedNodes(localSortedSet);
/*     */ 
/* 254 */       this.xmlattrStack.getXmlnsAttr(localSortedSet);
/* 255 */       this.firstCall = false;
/*     */     }
/*     */ 
/* 258 */     return localSortedSet.iterator();
/*     */   }
/*     */ 
/*     */   Iterator handleAttributes(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */     throws CanonicalizationException
/*     */   {
/* 277 */     this.xmlattrStack.push(paramNameSpaceSymbTable.getLevel());
/* 278 */     int i = isVisibleDO(paramElement, paramNameSpaceSymbTable.getLevel()) == 1 ? 1 : 0;
/* 279 */     NamedNodeMap localNamedNodeMap = null;
/* 280 */     int j = 0;
/* 281 */     if (paramElement.hasAttributes()) {
/* 282 */       localNamedNodeMap = paramElement.getAttributes();
/* 283 */       j = localNamedNodeMap.getLength();
/*     */     }
/*     */ 
/* 286 */     SortedSet localSortedSet = this.result;
/* 287 */     localSortedSet.clear();
/*     */     Object localObject;
/* 289 */     for (int k = 0; k < j; k++) {
/* 290 */       localObject = (Attr)localNamedNodeMap.item(k);
/* 291 */       String str1 = ((Attr)localObject).getNamespaceURI();
/*     */ 
/* 293 */       if ("http://www.w3.org/2000/xmlns/" != str1)
/*     */       {
/* 295 */         if ("http://www.w3.org/XML/1998/namespace" == str1) {
/* 296 */           if (((Attr)localObject).getLocalName().equals("id")) {
/* 297 */             if (i != 0)
/*     */             {
/* 300 */               localSortedSet.add(localObject);
/*     */             }
/*     */           }
/* 303 */           else this.xmlattrStack.addXmlnsAttr((Attr)localObject);
/*     */         }
/* 305 */         else if (i != 0)
/*     */         {
/* 308 */           localSortedSet.add(localObject);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 314 */         String str2 = ((Attr)localObject).getLocalName();
/* 315 */         String str3 = ((Attr)localObject).getValue();
/* 316 */         if ((!"xml".equals(str2)) || (!"http://www.w3.org/XML/1998/namespace".equals(str3)))
/*     */         {
/* 326 */           if (isVisible((Node)localObject)) {
/* 327 */             if ((i != 0) || (!paramNameSpaceSymbTable.removeMappingIfRender(str2)))
/*     */             {
/* 333 */               Node localNode = paramNameSpaceSymbTable.addMappingAndRender(str2, str3, (Attr)localObject);
/* 334 */               if (localNode != null) {
/* 335 */                 localSortedSet.add(localNode);
/* 336 */                 if (C14nHelper.namespaceIsRelative((Attr)localObject)) {
/* 337 */                   Object[] arrayOfObject = { paramElement.getTagName(), str2, ((Attr)localObject).getNodeValue() };
/*     */ 
/* 339 */                   throw new CanonicalizationException("c14n.Canonicalizer.RelativeNamespace", arrayOfObject);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 344 */           else if ((i != 0) && (str2 != "xmlns"))
/* 345 */             paramNameSpaceSymbTable.removeMapping(str2);
/*     */           else
/* 347 */             paramNameSpaceSymbTable.addMapping(str2, str3, (Attr)localObject);
/*     */         }
/*     */       }
/*     */     }
/* 351 */     if (i != 0)
/*     */     {
/* 353 */       Attr localAttr = paramElement.getAttributeNodeNS("http://www.w3.org/2000/xmlns/", "xmlns");
/* 354 */       localObject = null;
/* 355 */       if (localAttr == null)
/*     */       {
/* 357 */         localObject = paramNameSpaceSymbTable.getMapping("xmlns");
/* 358 */       } else if (!isVisible(localAttr))
/*     */       {
/* 361 */         localObject = paramNameSpaceSymbTable.addMappingAndRender("xmlns", "", getNullNode(localAttr.getOwnerDocument()));
/*     */       }
/*     */ 
/* 365 */       if (localObject != null) {
/* 366 */         localSortedSet.add(localObject);
/*     */       }
/*     */ 
/* 370 */       this.xmlattrStack.getXmlnsAttr(localSortedSet);
/* 371 */       paramNameSpaceSymbTable.getUnrenderedNodes(localSortedSet);
/*     */     }
/*     */ 
/* 374 */     return localSortedSet.iterator();
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeXPathNodeSet(Set paramSet, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 387 */     throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeSubTree(Node paramNode, String paramString)
/*     */     throws CanonicalizationException
/*     */   {
/* 401 */     throw new CanonicalizationException("c14n.Canonicalizer.UnsupportedOperation");
/*     */   }
/*     */ 
/*     */   void circumventBugIfNeeded(XMLSignatureInput paramXMLSignatureInput)
/*     */     throws CanonicalizationException, ParserConfigurationException, IOException, SAXException
/*     */   {
/* 408 */     if (!paramXMLSignatureInput.isNeedsToBeExpanded())
/* 409 */       return;
/* 410 */     Document localDocument = null;
/* 411 */     if (paramXMLSignatureInput.getSubNode() != null)
/* 412 */       localDocument = XMLUtils.getOwnerDocument(paramXMLSignatureInput.getSubNode());
/*     */     else {
/* 414 */       localDocument = XMLUtils.getOwnerDocument(paramXMLSignatureInput.getNodeSet());
/*     */     }
/* 416 */     XMLUtils.circumventBug2650(localDocument);
/*     */   }
/*     */ 
/*     */   void handleParent(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable) {
/* 420 */     if (!paramElement.hasAttributes()) {
/* 421 */       return;
/*     */     }
/* 423 */     this.xmlattrStack.push(-1);
/* 424 */     NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/* 425 */     int i = localNamedNodeMap.getLength();
/* 426 */     for (int j = 0; j < i; j++) {
/* 427 */       Attr localAttr = (Attr)localNamedNodeMap.item(j);
/* 428 */       if ("http://www.w3.org/2000/xmlns/" != localAttr.getNamespaceURI())
/*     */       {
/* 430 */         if ("http://www.w3.org/XML/1998/namespace" == localAttr.getNamespaceURI()) {
/* 431 */           this.xmlattrStack.addXmlnsAttr(localAttr);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 436 */         String str1 = localAttr.getLocalName();
/* 437 */         String str2 = localAttr.getNodeValue();
/* 438 */         if ((!"xml".equals(str1)) || (!"http://www.w3.org/XML/1998/namespace".equals(str2)))
/*     */         {
/* 442 */           paramNameSpaceSymbTable.addMapping(str1, str2, localAttr);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 448 */   private static String joinURI(String paramString1, String paramString2) throws URISyntaxException { String str1 = null;
/* 449 */     String str2 = null;
/* 450 */     String str3 = "";
/* 451 */     String str4 = null;
/* 452 */     String str5 = null;
/*     */ 
/* 455 */     if (paramString1 != null) {
/* 456 */       if (paramString1.endsWith("..")) {
/* 457 */         paramString1 = paramString1 + "/";
/*     */       }
/* 459 */       localURI = new URI(paramString1);
/* 460 */       str1 = localURI.getScheme();
/* 461 */       str2 = localURI.getAuthority();
/* 462 */       str3 = localURI.getPath();
/* 463 */       str4 = localURI.getQuery();
/* 464 */       str5 = localURI.getFragment();
/*     */     }
/*     */ 
/* 467 */     URI localURI = new URI(paramString2);
/* 468 */     String str6 = localURI.getScheme();
/* 469 */     String str7 = localURI.getAuthority();
/* 470 */     String str8 = localURI.getPath();
/* 471 */     String str9 = localURI.getQuery();
/* 472 */     Object localObject1 = null;
/*     */ 
/* 475 */     if ((str6 != null) && (str6.equals(str1)))
/* 476 */       str6 = null;
/*     */     String str10;
/*     */     String str11;
/*     */     String str12;
/*     */     String str13;
/* 478 */     if (str6 != null) {
/* 479 */       str10 = str6;
/* 480 */       str11 = str7;
/* 481 */       str12 = removeDotSegments(str8);
/* 482 */       str13 = str9;
/*     */     } else {
/* 484 */       if (str7 != null) {
/* 485 */         str11 = str7;
/* 486 */         str12 = removeDotSegments(str8);
/* 487 */         str13 = str9;
/*     */       } else {
/* 489 */         if (str8.length() == 0) {
/* 490 */           str12 = str3;
/* 491 */           if (str9 != null)
/* 492 */             str13 = str9;
/*     */           else
/* 494 */             str13 = str4;
/*     */         }
/*     */         else {
/* 497 */           if (str8.startsWith("/")) {
/* 498 */             str12 = removeDotSegments(str8);
/*     */           } else {
/* 500 */             if ((str2 != null) && (str3.length() == 0)) {
/* 501 */               str12 = "/" + str8;
/*     */             } else {
/* 503 */               int i = str3.lastIndexOf('/');
/* 504 */               if (i == -1)
/* 505 */                 str12 = str8;
/*     */               else {
/* 507 */                 str12 = str3.substring(0, i + 1) + str8;
/*     */               }
/*     */             }
/* 510 */             str12 = removeDotSegments(str12);
/*     */           }
/* 512 */           str13 = str9;
/*     */         }
/* 514 */         str11 = str2;
/*     */       }
/* 516 */       str10 = str1;
/*     */     }
/* 518 */     Object localObject2 = localObject1;
/* 519 */     return new URI(str10, str11, str12, str13, localObject2).toString();
/*     */   }
/*     */ 
/*     */   private static String removeDotSegments(String paramString)
/*     */   {
/* 524 */     log.log(Level.FINE, "STEP   OUTPUT BUFFER\t\tINPUT BUFFER");
/*     */ 
/* 529 */     String str1 = paramString;
/* 530 */     while (str1.indexOf("//") > -1) {
/* 531 */       str1 = str1.replaceAll("//", "/");
/*     */     }
/*     */ 
/* 535 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 539 */     if (str1.charAt(0) == '/') {
/* 540 */       localStringBuffer.append("/");
/* 541 */       str1 = str1.substring(1);
/*     */     }
/*     */ 
/* 544 */     printStep("1 ", localStringBuffer.toString(), str1);
/*     */ 
/* 547 */     while (str1.length() != 0)
/*     */     {
/* 554 */       if (str1.startsWith("./")) {
/* 555 */         str1 = str1.substring(2);
/* 556 */         printStep("2A", localStringBuffer.toString(), str1);
/* 557 */       } else if (str1.startsWith("../")) {
/* 558 */         str1 = str1.substring(3);
/* 559 */         if (!localStringBuffer.toString().equals("/")) {
/* 560 */           localStringBuffer.append("../");
/*     */         }
/* 562 */         printStep("2A", localStringBuffer.toString(), str1);
/*     */       }
/* 566 */       else if (str1.startsWith("/./")) {
/* 567 */         str1 = str1.substring(2);
/* 568 */         printStep("2B", localStringBuffer.toString(), str1);
/* 569 */       } else if (str1.equals("/."))
/*     */       {
/* 571 */         str1 = str1.replaceFirst("/.", "/");
/* 572 */         printStep("2B", localStringBuffer.toString(), str1);
/*     */       }
/*     */       else
/*     */       {
/*     */         int i;
/* 583 */         if (str1.startsWith("/../")) {
/* 584 */           str1 = str1.substring(3);
/* 585 */           if (localStringBuffer.length() == 0) {
/* 586 */             localStringBuffer.append("/");
/* 587 */           } else if (localStringBuffer.toString().endsWith("../")) {
/* 588 */             localStringBuffer.append("..");
/* 589 */           } else if (localStringBuffer.toString().endsWith("..")) {
/* 590 */             localStringBuffer.append("/..");
/*     */           } else {
/* 592 */             i = localStringBuffer.lastIndexOf("/");
/* 593 */             if (i == -1) {
/* 594 */               localStringBuffer = new StringBuffer();
/* 595 */               if (str1.charAt(0) == '/')
/* 596 */                 str1 = str1.substring(1);
/*     */             }
/*     */             else {
/* 599 */               localStringBuffer = localStringBuffer.delete(i, localStringBuffer.length());
/*     */             }
/*     */           }
/* 602 */           printStep("2C", localStringBuffer.toString(), str1);
/* 603 */         } else if (str1.equals("/.."))
/*     */         {
/* 605 */           str1 = str1.replaceFirst("/..", "/");
/* 606 */           if (localStringBuffer.length() == 0) {
/* 607 */             localStringBuffer.append("/");
/* 608 */           } else if (localStringBuffer.toString().endsWith("../")) {
/* 609 */             localStringBuffer.append("..");
/* 610 */           } else if (localStringBuffer.toString().endsWith("..")) {
/* 611 */             localStringBuffer.append("/..");
/*     */           } else {
/* 613 */             i = localStringBuffer.lastIndexOf("/");
/* 614 */             if (i == -1) {
/* 615 */               localStringBuffer = new StringBuffer();
/* 616 */               if (str1.charAt(0) == '/')
/* 617 */                 str1 = str1.substring(1);
/*     */             }
/*     */             else {
/* 620 */               localStringBuffer = localStringBuffer.delete(i, localStringBuffer.length());
/*     */             }
/*     */           }
/* 623 */           printStep("2C", localStringBuffer.toString(), str1);
/*     */         }
/* 629 */         else if (str1.equals(".")) {
/* 630 */           str1 = "";
/* 631 */           printStep("2D", localStringBuffer.toString(), str1);
/* 632 */         } else if (str1.equals("..")) {
/* 633 */           if (!localStringBuffer.toString().equals("/"))
/* 634 */             localStringBuffer.append("..");
/* 635 */           str1 = "";
/* 636 */           printStep("2D", localStringBuffer.toString(), str1);
/*     */         }
/*     */         else
/*     */         {
/* 642 */           i = -1;
/* 643 */           int j = str1.indexOf('/');
/* 644 */           if (j == 0) {
/* 645 */             i = str1.indexOf('/', 1);
/*     */           } else {
/* 647 */             i = j;
/* 648 */             j = 0;
/*     */           }
/*     */           String str2;
/* 651 */           if (i == -1) {
/* 652 */             str2 = str1.substring(j);
/* 653 */             str1 = "";
/*     */           } else {
/* 655 */             str2 = str1.substring(j, i);
/* 656 */             str1 = str1.substring(i);
/*     */           }
/* 658 */           localStringBuffer.append(str2);
/* 659 */           printStep("2E", localStringBuffer.toString(), str1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 667 */     if (localStringBuffer.toString().endsWith("..")) {
/* 668 */       localStringBuffer.append("/");
/* 669 */       printStep("3 ", localStringBuffer.toString(), str1);
/*     */     }
/*     */ 
/* 672 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static void printStep(String paramString1, String paramString2, String paramString3) {
/* 676 */     if (log.isLoggable(Level.FINE)) {
/* 677 */       log.log(Level.FINE, " " + paramString1 + ":   " + paramString2);
/* 678 */       if (paramString2.length() == 0)
/* 679 */         log.log(Level.FINE, "\t\t\t\t" + paramString3);
/*     */       else
/* 681 */         log.log(Level.FINE, "\t\t\t" + paramString3);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class XmlAttrStack
/*     */   {
/*  69 */     int currentLevel = 0;
/*  70 */     int lastlevel = 0;
/*     */     XmlsStackElement cur;
/*  77 */     List levels = new ArrayList();
/*     */ 
/*  79 */     void push(int paramInt) { this.currentLevel = paramInt;
/*  80 */       if (this.currentLevel == -1)
/*  81 */         return;
/*  82 */       this.cur = null;
/*  83 */       while (this.lastlevel >= this.currentLevel) {
/*  84 */         this.levels.remove(this.levels.size() - 1);
/*  85 */         if (this.levels.size() == 0) {
/*  86 */           this.lastlevel = 0;
/*  87 */           return;
/*     */         }
/*  89 */         this.lastlevel = ((XmlsStackElement)this.levels.get(this.levels.size() - 1)).level;
/*     */       } }
/*     */ 
/*     */     void addXmlnsAttr(Attr paramAttr) {
/*  93 */       if (this.cur == null) {
/*  94 */         this.cur = new XmlsStackElement();
/*  95 */         this.cur.level = this.currentLevel;
/*  96 */         this.levels.add(this.cur);
/*  97 */         this.lastlevel = this.currentLevel;
/*     */       }
/*  99 */       this.cur.nodes.add(paramAttr);
/*     */     }
/*     */     void getXmlnsAttr(Collection paramCollection) {
/* 102 */       if (this.cur == null) {
/* 103 */         this.cur = new XmlsStackElement();
/* 104 */         this.cur.level = this.currentLevel;
/* 105 */         this.lastlevel = this.currentLevel;
/* 106 */         this.levels.add(this.cur);
/*     */       }
/* 108 */       int i = this.levels.size() - 2;
/* 109 */       int j = 0;
/* 110 */       XmlsStackElement localXmlsStackElement = null;
/* 111 */       if (i == -1) {
/* 112 */         j = 1;
/*     */       } else {
/* 114 */         localXmlsStackElement = (XmlsStackElement)this.levels.get(i);
/* 115 */         if ((localXmlsStackElement.rendered) && (localXmlsStackElement.level + 1 == this.currentLevel))
/* 116 */           j = 1;
/*     */       }
/* 118 */       if (j != 0) {
/* 119 */         paramCollection.addAll(this.cur.nodes);
/* 120 */         this.cur.rendered = true;
/* 121 */         return;
/*     */       }
/*     */ 
/* 124 */       HashMap localHashMap = new HashMap();
/* 125 */       ArrayList localArrayList = new ArrayList();
/* 126 */       int k = 1;
/*     */       Iterator localIterator;
/*     */       Object localObject1;
/* 127 */       for (; i >= 0; i--) {
/* 128 */         localXmlsStackElement = (XmlsStackElement)this.levels.get(i);
/* 129 */         if (localXmlsStackElement.rendered) {
/* 130 */           k = 0;
/*     */         }
/* 132 */         localIterator = localXmlsStackElement.nodes.iterator();
/* 133 */         while ((localIterator.hasNext()) && (k != 0)) {
/* 134 */           localObject1 = (Attr)localIterator.next();
/* 135 */           if (((Attr)localObject1).getLocalName().equals("base")) {
/* 136 */             if (!localXmlsStackElement.rendered)
/* 137 */               localArrayList.add(localObject1);
/*     */           }
/* 139 */           else if (!localHashMap.containsKey(((Attr)localObject1).getName()))
/* 140 */             localHashMap.put(((Attr)localObject1).getName(), localObject1);
/*     */         }
/*     */       }
/* 143 */       if (!localArrayList.isEmpty()) {
/* 144 */         localIterator = this.cur.nodes.iterator();
/* 145 */         localObject1 = null;
/* 146 */         Object localObject2 = null;
/*     */         Attr localAttr;
/* 147 */         while (localIterator.hasNext()) {
/* 148 */           localAttr = (Attr)localIterator.next();
/* 149 */           if (localAttr.getLocalName().equals("base")) {
/* 150 */             localObject1 = localAttr.getValue();
/* 151 */             localObject2 = localAttr;
/* 152 */             break;
/*     */           }
/*     */         }
/* 155 */         localIterator = localArrayList.iterator();
/* 156 */         while (localIterator.hasNext()) {
/* 157 */           localAttr = (Attr)localIterator.next();
/* 158 */           if (localObject1 == null) {
/* 159 */             localObject1 = localAttr.getValue();
/* 160 */             localObject2 = localAttr;
/*     */           } else {
/*     */             try {
/* 163 */               localObject1 = Canonicalizer11.joinURI(localAttr.getValue(), (String)localObject1);
/*     */             } catch (URISyntaxException localURISyntaxException) {
/* 165 */               localURISyntaxException.printStackTrace();
/*     */             }
/*     */           }
/*     */         }
/* 169 */         if ((localObject1 != null) && (((String)localObject1).length() != 0)) {
/* 170 */           localObject2.setValue((String)localObject1);
/* 171 */           paramCollection.add(localObject2);
/*     */         }
/*     */       }
/*     */ 
/* 175 */       this.cur.rendered = true;
/* 176 */       paramCollection.addAll(localHashMap.values());
/*     */     }
/*     */ 
/*     */     static class XmlsStackElement
/*     */     {
/*     */       int level;
/*  74 */       boolean rendered = false;
/*  75 */       List nodes = new ArrayList();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11
 * JD-Core Version:    0.6.2
 */