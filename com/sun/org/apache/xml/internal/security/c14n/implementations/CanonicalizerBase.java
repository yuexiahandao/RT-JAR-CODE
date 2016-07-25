/*     */ package com.sun.org.apache.xml.internal.security.c14n.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi;
/*     */ import com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare;
/*     */ import com.sun.org.apache.xml.internal.security.signature.NodeFilter;
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.UnsyncByteArrayOutputStream;
/*     */ import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class CanonicalizerBase extends CanonicalizerSpi
/*     */ {
/*  69 */   private static final byte[] _END_PI = { 63, 62 };
/*  70 */   private static final byte[] _BEGIN_PI = { 60, 63 };
/*  71 */   private static final byte[] _END_COMM = { 45, 45, 62 };
/*  72 */   private static final byte[] _BEGIN_COMM = { 60, 33, 45, 45 };
/*  73 */   private static final byte[] __XA_ = { 38, 35, 120, 65, 59 };
/*  74 */   private static final byte[] __X9_ = { 38, 35, 120, 57, 59 };
/*  75 */   private static final byte[] _QUOT_ = { 38, 113, 117, 111, 116, 59 };
/*  76 */   private static final byte[] __XD_ = { 38, 35, 120, 68, 59 };
/*  77 */   private static final byte[] _GT_ = { 38, 103, 116, 59 };
/*  78 */   private static final byte[] _LT_ = { 38, 108, 116, 59 };
/*  79 */   private static final byte[] _END_TAG = { 60, 47 };
/*  80 */   private static final byte[] _AMP_ = { 38, 97, 109, 112, 59 };
/*  81 */   private static final byte[] _EQUALS_STR = { 61, 34 };
/*     */ 
/*  83 */   static final AttrCompare COMPARE = new AttrCompare();
/*     */   static final String XML = "xml";
/*     */   static final String XMLNS = "xmlns";
/*     */   static final int NODE_BEFORE_DOCUMENT_ELEMENT = -1;
/*     */   static final int NODE_NOT_BEFORE_OR_AFTER_DOCUMENT_ELEMENT = 0;
/*     */   static final int NODE_AFTER_DOCUMENT_ELEMENT = 1;
/*     */   List nodeFilter;
/*     */   boolean _includeComments;
/*  91 */   Set _xpathNodeSet = null;
/*     */ 
/*  97 */   Node _excludeNode = null;
/*  98 */   OutputStream _writer = new UnsyncByteArrayOutputStream();
/*     */   private Attr nullNode;
/*     */ 
/*     */   public CanonicalizerBase(boolean paramBoolean)
/*     */   {
/* 111 */     this._includeComments = paramBoolean;
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeSubTree(Node paramNode)
/*     */     throws CanonicalizationException
/*     */   {
/* 122 */     return engineCanonicalizeSubTree(paramNode, (Node)null);
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalizeXPathNodeSet(Set paramSet)
/*     */     throws CanonicalizationException
/*     */   {
/* 132 */     this._xpathNodeSet = paramSet;
/* 133 */     return engineCanonicalizeXPathNodeSetInternal(XMLUtils.getOwnerDocument(this._xpathNodeSet));
/*     */   }
/*     */ 
/*     */   public byte[] engineCanonicalize(XMLSignatureInput paramXMLSignatureInput)
/*     */     throws CanonicalizationException
/*     */   {
/*     */     try
/*     */     {
/* 145 */       if (paramXMLSignatureInput.isExcludeComments()) {
/* 146 */         this._includeComments = false;
/*     */       }
/* 148 */       if (paramXMLSignatureInput.isOctetStream())
/* 149 */         return engineCanonicalize(paramXMLSignatureInput.getBytes());
/*     */       byte[] arrayOfByte;
/* 151 */       if (paramXMLSignatureInput.isElement()) {
/* 152 */         return engineCanonicalizeSubTree(paramXMLSignatureInput.getSubNode(), paramXMLSignatureInput.getExcludeNode());
/*     */       }
/*     */ 
/* 155 */       if (paramXMLSignatureInput.isNodeSet()) {
/* 156 */         this.nodeFilter = paramXMLSignatureInput.getNodeFilters();
/*     */ 
/* 158 */         circumventBugIfNeeded(paramXMLSignatureInput);
/*     */ 
/* 160 */         if (paramXMLSignatureInput.getSubNode() != null) {
/* 161 */           arrayOfByte = engineCanonicalizeXPathNodeSetInternal(paramXMLSignatureInput.getSubNode());
/*     */         }
/* 163 */         return engineCanonicalizeXPathNodeSet(paramXMLSignatureInput.getNodeSet());
/*     */       }
/*     */ 
/* 168 */       return null;
/*     */     } catch (CanonicalizationException localCanonicalizationException) {
/* 170 */       throw new CanonicalizationException("empty", localCanonicalizationException);
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 172 */       throw new CanonicalizationException("empty", localParserConfigurationException);
/*     */     } catch (IOException localIOException) {
/* 174 */       throw new CanonicalizationException("empty", localIOException);
/*     */     } catch (SAXException localSAXException) {
/* 176 */       throw new CanonicalizationException("empty", localSAXException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setWriter(OutputStream paramOutputStream)
/*     */   {
/* 183 */     this._writer = paramOutputStream;
/*     */   }
/*     */ 
/*     */   byte[] engineCanonicalizeSubTree(Node paramNode1, Node paramNode2)
/*     */     throws CanonicalizationException
/*     */   {
/* 198 */     this._excludeNode = paramNode2;
/*     */     try {
/* 200 */       NameSpaceSymbTable localNameSpaceSymbTable = new NameSpaceSymbTable();
/* 201 */       int i = -1;
/* 202 */       if ((paramNode1 != null) && (paramNode1.getNodeType() == 1))
/*     */       {
/* 204 */         getParentNameSpaces((Element)paramNode1, localNameSpaceSymbTable);
/* 205 */         i = 0;
/*     */       }
/* 207 */       canonicalizeSubTree(paramNode1, localNameSpaceSymbTable, paramNode1, i);
/* 208 */       this._writer.close();
/*     */       byte[] arrayOfByte;
/* 209 */       if ((this._writer instanceof ByteArrayOutputStream)) {
/* 210 */         arrayOfByte = ((ByteArrayOutputStream)this._writer).toByteArray();
/* 211 */         if (this.reset) {
/* 212 */           ((ByteArrayOutputStream)this._writer).reset();
/*     */         }
/* 214 */         return arrayOfByte;
/* 215 */       }if ((this._writer instanceof UnsyncByteArrayOutputStream)) {
/* 216 */         arrayOfByte = ((UnsyncByteArrayOutputStream)this._writer).toByteArray();
/* 217 */         if (this.reset) {
/* 218 */           ((UnsyncByteArrayOutputStream)this._writer).reset();
/*     */         }
/* 220 */         return arrayOfByte;
/*     */       }
/* 222 */       return null;
/*     */     }
/*     */     catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 225 */       throw new CanonicalizationException("empty", localUnsupportedEncodingException);
/*     */     } catch (IOException localIOException) {
/* 227 */       throw new CanonicalizationException("empty", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   final void canonicalizeSubTree(Node paramNode1, NameSpaceSymbTable paramNameSpaceSymbTable, Node paramNode2, int paramInt)
/*     */     throws CanonicalizationException, IOException
/*     */   {
/* 244 */     if (isVisibleInt(paramNode1) == -1)
/* 245 */       return;
/* 246 */     Node localNode1 = null;
/* 247 */     Object localObject = null;
/* 248 */     OutputStream localOutputStream = this._writer;
/* 249 */     Node localNode2 = this._excludeNode;
/* 250 */     boolean bool = this._includeComments;
/* 251 */     HashMap localHashMap = new HashMap();
/*     */     while (true) {
/* 253 */       switch (paramNode1.getNodeType()) {
/*     */       case 5:
/*     */       case 10:
/*     */       default:
/* 257 */         break;
/*     */       case 2:
/*     */       case 6:
/*     */       case 12:
/* 263 */         throw new CanonicalizationException("empty");
/*     */       case 9:
/*     */       case 11:
/* 267 */         paramNameSpaceSymbTable.outputNodePush();
/* 268 */         localNode1 = paramNode1.getFirstChild();
/* 269 */         break;
/*     */       case 8:
/* 272 */         if (bool)
/* 273 */           outputCommentToWriter((Comment)paramNode1, localOutputStream, paramInt); break;
/*     */       case 7:
/* 278 */         outputPItoWriter((ProcessingInstruction)paramNode1, localOutputStream, paramInt);
/* 279 */         break;
/*     */       case 3:
/*     */       case 4:
/* 283 */         outputTextToWriter(paramNode1.getNodeValue(), localOutputStream);
/* 284 */         break;
/*     */       case 1:
/* 287 */         paramInt = 0;
/* 288 */         if (paramNode1 != localNode2)
/*     */         {
/* 291 */           Element localElement = (Element)paramNode1;
/*     */ 
/* 293 */           paramNameSpaceSymbTable.outputNodePush();
/* 294 */           localOutputStream.write(60);
/* 295 */           String str = localElement.getTagName();
/* 296 */           UtfHelpper.writeByte(str, localOutputStream, localHashMap);
/*     */ 
/* 298 */           Iterator localIterator = handleAttributesSubtree(localElement, paramNameSpaceSymbTable);
/* 299 */           if (localIterator != null)
/*     */           {
/* 301 */             while (localIterator.hasNext()) {
/* 302 */               Attr localAttr = (Attr)localIterator.next();
/* 303 */               outputAttrToWriter(localAttr.getNodeName(), localAttr.getNodeValue(), localOutputStream, localHashMap);
/*     */             }
/*     */           }
/* 306 */           localOutputStream.write(62);
/* 307 */           localNode1 = paramNode1.getFirstChild();
/* 308 */           if (localNode1 == null) {
/* 309 */             localOutputStream.write((byte[])_END_TAG.clone());
/* 310 */             UtfHelpper.writeStringToUtf8(str, localOutputStream);
/* 311 */             localOutputStream.write(62);
/*     */ 
/* 313 */             paramNameSpaceSymbTable.outputNodePop();
/* 314 */             if (localObject != null)
/* 315 */               localNode1 = paramNode1.getNextSibling();
/*     */           }
/*     */           else {
/* 318 */             localObject = localElement;
/*     */           }
/*     */         }
/*     */         break;
/*     */       }
/* 322 */       while ((localNode1 == null) && (localObject != null)) {
/* 323 */         localOutputStream.write((byte[])_END_TAG.clone());
/* 324 */         UtfHelpper.writeByte(((Element)localObject).getTagName(), localOutputStream, localHashMap);
/* 325 */         localOutputStream.write(62);
/*     */ 
/* 327 */         paramNameSpaceSymbTable.outputNodePop();
/* 328 */         if (localObject == paramNode2)
/* 329 */           return;
/* 330 */         localNode1 = ((Node)localObject).getNextSibling();
/* 331 */         localObject = ((Node)localObject).getParentNode();
/* 332 */         if ((localObject != null) && (((Node)localObject).getNodeType() != 1)) {
/* 333 */           paramInt = 1;
/* 334 */           localObject = null;
/*     */         }
/*     */       }
/* 337 */       if (localNode1 == null)
/* 338 */         return;
/* 339 */       paramNode1 = localNode1;
/* 340 */       localNode1 = paramNode1.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] engineCanonicalizeXPathNodeSetInternal(Node paramNode)
/*     */     throws CanonicalizationException
/*     */   {
/*     */     try
/*     */     {
/* 350 */       canonicalizeXPathNodeSet(paramNode, paramNode);
/* 351 */       this._writer.close();
/*     */       byte[] arrayOfByte;
/* 352 */       if ((this._writer instanceof ByteArrayOutputStream)) {
/* 353 */         arrayOfByte = ((ByteArrayOutputStream)this._writer).toByteArray();
/* 354 */         if (this.reset) {
/* 355 */           ((ByteArrayOutputStream)this._writer).reset();
/*     */         }
/* 357 */         return arrayOfByte;
/* 358 */       }if ((this._writer instanceof UnsyncByteArrayOutputStream)) {
/* 359 */         arrayOfByte = ((UnsyncByteArrayOutputStream)this._writer).toByteArray();
/* 360 */         if (this.reset) {
/* 361 */           ((UnsyncByteArrayOutputStream)this._writer).reset();
/*     */         }
/* 363 */         return arrayOfByte;
/*     */       }
/* 365 */       return null;
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/* 367 */       throw new CanonicalizationException("empty", localUnsupportedEncodingException);
/*     */     } catch (IOException localIOException) {
/* 369 */       throw new CanonicalizationException("empty", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   final void canonicalizeXPathNodeSet(Node paramNode1, Node paramNode2)
/*     */     throws CanonicalizationException, IOException
/*     */   {
/* 384 */     if (isVisibleInt(paramNode1) == -1)
/* 385 */       return;
/* 386 */     int i = 0;
/* 387 */     NameSpaceSymbTable localNameSpaceSymbTable = new NameSpaceSymbTable();
/* 388 */     if ((paramNode1 != null) && (paramNode1.getNodeType() == 1))
/* 389 */       getParentNameSpaces((Element)paramNode1, localNameSpaceSymbTable);
/* 390 */     Node localNode = null;
/* 391 */     Object localObject1 = null;
/* 392 */     OutputStream localOutputStream = this._writer;
/* 393 */     int j = -1;
/* 394 */     HashMap localHashMap = new HashMap();
/*     */     while (true)
/*     */     {
/*     */       Object localObject2;
/* 396 */       switch (paramNode1.getNodeType()) {
/*     */       case 5:
/*     */       case 10:
/*     */       default:
/* 400 */         break;
/*     */       case 2:
/*     */       case 6:
/*     */       case 12:
/* 406 */         throw new CanonicalizationException("empty");
/*     */       case 9:
/*     */       case 11:
/* 410 */         localNameSpaceSymbTable.outputNodePush();
/*     */ 
/* 412 */         localNode = paramNode1.getFirstChild();
/* 413 */         break;
/*     */       case 8:
/* 416 */         if ((this._includeComments) && (isVisibleDO(paramNode1, localNameSpaceSymbTable.getLevel()) == 1))
/* 417 */           outputCommentToWriter((Comment)paramNode1, localOutputStream, j); break;
/*     */       case 7:
/* 422 */         if (isVisible(paramNode1))
/* 423 */           outputPItoWriter((ProcessingInstruction)paramNode1, localOutputStream, j); break;
/*     */       case 3:
/*     */       case 4:
/* 428 */         if (isVisible(paramNode1)) {
/* 429 */           outputTextToWriter(paramNode1.getNodeValue(), localOutputStream);
/* 430 */           for (localObject2 = paramNode1.getNextSibling(); 
/* 432 */             (localObject2 != null) && ((((Node)localObject2).getNodeType() == 3) || (((Node)localObject2).getNodeType() == 4)); 
/* 435 */             localObject2 = ((Node)localObject2).getNextSibling()) {
/* 436 */             outputTextToWriter(((Node)localObject2).getNodeValue(), localOutputStream);
/* 437 */             paramNode1 = (Node)localObject2;
/* 438 */             localNode = paramNode1.getNextSibling();
/*     */           }
/*     */         }
/* 435 */         break;
/*     */       case 1:
/* 445 */         j = 0;
/* 446 */         localObject2 = (Element)paramNode1;
/*     */ 
/* 448 */         String str = null;
/* 449 */         int k = isVisibleDO(paramNode1, localNameSpaceSymbTable.getLevel());
/* 450 */         if (k == -1) {
/* 451 */           localNode = paramNode1.getNextSibling();
/*     */         }
/*     */         else {
/* 454 */           i = k == 1 ? 1 : 0;
/* 455 */           if (i != 0) {
/* 456 */             localNameSpaceSymbTable.outputNodePush();
/* 457 */             localOutputStream.write(60);
/* 458 */             str = ((Element)localObject2).getTagName();
/* 459 */             UtfHelpper.writeByte(str, localOutputStream, localHashMap);
/*     */           } else {
/* 461 */             localNameSpaceSymbTable.push();
/*     */           }
/*     */ 
/* 464 */           Iterator localIterator = handleAttributes((Element)localObject2, localNameSpaceSymbTable);
/* 465 */           if (localIterator != null)
/*     */           {
/* 467 */             while (localIterator.hasNext()) {
/* 468 */               Attr localAttr = (Attr)localIterator.next();
/* 469 */               outputAttrToWriter(localAttr.getNodeName(), localAttr.getNodeValue(), localOutputStream, localHashMap);
/*     */             }
/*     */           }
/* 472 */           if (i != 0) {
/* 473 */             localOutputStream.write(62);
/*     */           }
/* 475 */           localNode = paramNode1.getFirstChild();
/*     */ 
/* 477 */           if (localNode == null) {
/* 478 */             if (i != 0) {
/* 479 */               localOutputStream.write((byte[])_END_TAG.clone());
/* 480 */               UtfHelpper.writeByte(str, localOutputStream, localHashMap);
/* 481 */               localOutputStream.write(62);
/*     */ 
/* 483 */               localNameSpaceSymbTable.outputNodePop();
/*     */             } else {
/* 485 */               localNameSpaceSymbTable.pop();
/*     */             }
/* 487 */             if (localObject1 != null)
/* 488 */               localNode = paramNode1.getNextSibling();
/*     */           }
/*     */           else {
/* 491 */             localObject1 = localObject2;
/*     */           }
/*     */         }
/*     */         break;
/*     */       }
/* 495 */       while ((localNode == null) && (localObject1 != null)) {
/* 496 */         if (isVisible((Node)localObject1)) {
/* 497 */           localOutputStream.write((byte[])_END_TAG.clone());
/* 498 */           UtfHelpper.writeByte(((Element)localObject1).getTagName(), localOutputStream, localHashMap);
/* 499 */           localOutputStream.write(62);
/*     */ 
/* 501 */           localNameSpaceSymbTable.outputNodePop();
/*     */         } else {
/* 503 */           localNameSpaceSymbTable.pop();
/*     */         }
/* 505 */         if (localObject1 == paramNode2)
/* 506 */           return;
/* 507 */         localNode = ((Node)localObject1).getNextSibling();
/* 508 */         localObject1 = ((Node)localObject1).getParentNode();
/* 509 */         if ((localObject1 != null) && (((Node)localObject1).getNodeType() != 1)) {
/* 510 */           localObject1 = null;
/* 511 */           j = 1;
/*     */         }
/*     */       }
/* 514 */       if (localNode == null)
/* 515 */         return;
/* 516 */       paramNode1 = localNode;
/* 517 */       localNode = paramNode1.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/* 521 */   int isVisibleDO(Node paramNode, int paramInt) { if (this.nodeFilter != null) {
/* 522 */       Iterator localIterator = this.nodeFilter.iterator();
/* 523 */       while (localIterator.hasNext()) {
/* 524 */         int i = ((NodeFilter)localIterator.next()).isNodeIncludeDO(paramNode, paramInt);
/* 525 */         if (i != 1)
/* 526 */           return i;
/*     */       }
/*     */     }
/* 529 */     if ((this._xpathNodeSet != null) && (!this._xpathNodeSet.contains(paramNode)))
/* 530 */       return 0;
/* 531 */     return 1; }
/*     */ 
/*     */   int isVisibleInt(Node paramNode) {
/* 534 */     if (this.nodeFilter != null) {
/* 535 */       Iterator localIterator = this.nodeFilter.iterator();
/* 536 */       while (localIterator.hasNext()) {
/* 537 */         int i = ((NodeFilter)localIterator.next()).isNodeInclude(paramNode);
/* 538 */         if (i != 1)
/* 539 */           return i;
/*     */       }
/*     */     }
/* 542 */     if ((this._xpathNodeSet != null) && (!this._xpathNodeSet.contains(paramNode)))
/* 543 */       return 0;
/* 544 */     return 1;
/*     */   }
/*     */ 
/*     */   boolean isVisible(Node paramNode) {
/* 548 */     if (this.nodeFilter != null) {
/* 549 */       Iterator localIterator = this.nodeFilter.iterator();
/* 550 */       while (localIterator.hasNext()) {
/* 551 */         if (((NodeFilter)localIterator.next()).isNodeInclude(paramNode) != 1)
/* 552 */           return false;
/*     */       }
/*     */     }
/* 555 */     if ((this._xpathNodeSet != null) && (!this._xpathNodeSet.contains(paramNode)))
/* 556 */       return false;
/* 557 */     return true;
/*     */   }
/*     */ 
/*     */   void handleParent(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable) {
/* 561 */     if (!paramElement.hasAttributes()) {
/* 562 */       return;
/*     */     }
/* 564 */     NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
/* 565 */     int i = localNamedNodeMap.getLength();
/* 566 */     for (int j = 0; j < i; j++) {
/* 567 */       Attr localAttr = (Attr)localNamedNodeMap.item(j);
/* 568 */       if ("http://www.w3.org/2000/xmlns/" == localAttr.getNamespaceURI())
/*     */       {
/* 573 */         String str1 = localAttr.getLocalName();
/* 574 */         String str2 = localAttr.getNodeValue();
/* 575 */         if ((!"xml".equals(str1)) || (!"http://www.w3.org/XML/1998/namespace".equals(str2)))
/*     */         {
/* 579 */           paramNameSpaceSymbTable.addMapping(str1, str2, localAttr);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final void getParentNameSpaces(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */   {
/* 589 */     ArrayList localArrayList = new ArrayList(10);
/* 590 */     Node localNode1 = paramElement.getParentNode();
/* 591 */     if ((localNode1 == null) || (localNode1.getNodeType() != 1)) {
/* 592 */       return;
/*     */     }
/*     */ 
/* 595 */     Node localNode2 = localNode1;
/* 596 */     while ((localNode2 != null) && (localNode2.getNodeType() == 1)) {
/* 597 */       localArrayList.add((Element)localNode2);
/* 598 */       localNode2 = localNode2.getParentNode();
/*     */     }
/*     */ 
/* 601 */     ListIterator localListIterator = localArrayList.listIterator(localArrayList.size());
/*     */     Object localObject;
/* 602 */     while (localListIterator.hasPrevious()) {
/* 603 */       localObject = (Element)localListIterator.previous();
/* 604 */       handleParent((Element)localObject, paramNameSpaceSymbTable);
/*     */     }
/*     */ 
/* 607 */     if (((localObject = paramNameSpaceSymbTable.getMappingWithoutRendered("xmlns")) != null) && ("".equals(((Attr)localObject).getValue())))
/*     */     {
/* 609 */       paramNameSpaceSymbTable.addMappingAndRender("xmlns", "", getNullNode(((Attr)localObject).getOwnerDocument()));
/*     */     }
/*     */   }
/*     */ 
/*     */   abstract Iterator handleAttributes(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */     throws CanonicalizationException;
/*     */ 
/*     */   abstract Iterator handleAttributesSubtree(Element paramElement, NameSpaceSymbTable paramNameSpaceSymbTable)
/*     */     throws CanonicalizationException;
/*     */ 
/*     */   abstract void circumventBugIfNeeded(XMLSignatureInput paramXMLSignatureInput)
/*     */     throws CanonicalizationException, ParserConfigurationException, IOException, SAXException;
/*     */ 
/*     */   static final void outputAttrToWriter(String paramString1, String paramString2, OutputStream paramOutputStream, Map paramMap)
/*     */     throws IOException
/*     */   {
/* 658 */     paramOutputStream.write(32);
/* 659 */     UtfHelpper.writeByte(paramString1, paramOutputStream, paramMap);
/* 660 */     paramOutputStream.write((byte[])_EQUALS_STR.clone());
/*     */ 
/* 662 */     int i = paramString2.length();
/* 663 */     int j = 0;
/* 664 */     while (j < i) {
/* 665 */       char c = paramString2.charAt(j++);
/*     */       byte[] arrayOfByte;
/* 667 */       switch (c)
/*     */       {
/*     */       case '&':
/* 670 */         arrayOfByte = (byte[])_AMP_.clone();
/* 671 */         break;
/*     */       case '<':
/* 674 */         arrayOfByte = (byte[])_LT_.clone();
/* 675 */         break;
/*     */       case '"':
/* 678 */         arrayOfByte = (byte[])_QUOT_.clone();
/* 679 */         break;
/*     */       case '\t':
/* 682 */         arrayOfByte = (byte[])__X9_.clone();
/* 683 */         break;
/*     */       case '\n':
/* 686 */         arrayOfByte = (byte[])__XA_.clone();
/* 687 */         break;
/*     */       case '\r':
/* 690 */         arrayOfByte = (byte[])__XD_.clone();
/* 691 */         break;
/*     */       default:
/* 694 */         if (c < '') {
/* 695 */           paramOutputStream.write(c); continue;
/*     */         }
/* 697 */         UtfHelpper.writeCharToUtf8(c, paramOutputStream);
/*     */ 
/* 699 */         break;
/*     */       }
/* 701 */       paramOutputStream.write(arrayOfByte);
/*     */     }
/*     */ 
/* 704 */     paramOutputStream.write(34);
/*     */   }
/*     */ 
/*     */   static final void outputPItoWriter(ProcessingInstruction paramProcessingInstruction, OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 716 */     if (paramInt == 1) {
/* 717 */       paramOutputStream.write(10);
/*     */     }
/* 719 */     paramOutputStream.write((byte[])_BEGIN_PI.clone());
/*     */ 
/* 721 */     String str1 = paramProcessingInstruction.getTarget();
/* 722 */     int i = str1.length();
/*     */     int k;
/* 724 */     for (int j = 0; j < i; j++) {
/* 725 */       k = str1.charAt(j);
/* 726 */       if (k == 13) {
/* 727 */         paramOutputStream.write((byte[])__XD_.clone());
/*     */       }
/* 729 */       else if (k < 128)
/* 730 */         paramOutputStream.write(k);
/*     */       else {
/* 732 */         UtfHelpper.writeCharToUtf8(k, paramOutputStream);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 737 */     String str2 = paramProcessingInstruction.getData();
/*     */ 
/* 739 */     i = str2.length();
/*     */ 
/* 741 */     if (i > 0) {
/* 742 */       paramOutputStream.write(32);
/*     */ 
/* 744 */       for (k = 0; k < i; k++) {
/* 745 */         char c = str2.charAt(k);
/* 746 */         if (c == '\r')
/* 747 */           paramOutputStream.write((byte[])__XD_.clone());
/*     */         else {
/* 749 */           UtfHelpper.writeCharToUtf8(c, paramOutputStream);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 754 */     paramOutputStream.write((byte[])_END_PI.clone());
/* 755 */     if (paramInt == -1)
/* 756 */       paramOutputStream.write(10);
/*     */   }
/*     */ 
/*     */   static final void outputCommentToWriter(Comment paramComment, OutputStream paramOutputStream, int paramInt)
/*     */     throws IOException
/*     */   {
/* 768 */     if (paramInt == 1) {
/* 769 */       paramOutputStream.write(10);
/*     */     }
/* 771 */     paramOutputStream.write((byte[])_BEGIN_COMM.clone());
/*     */ 
/* 773 */     String str = paramComment.getData();
/* 774 */     int i = str.length();
/*     */ 
/* 776 */     for (int j = 0; j < i; j++) {
/* 777 */       char c = str.charAt(j);
/* 778 */       if (c == '\r') {
/* 779 */         paramOutputStream.write((byte[])__XD_.clone());
/*     */       }
/* 781 */       else if (c < '')
/* 782 */         paramOutputStream.write(c);
/*     */       else {
/* 784 */         UtfHelpper.writeCharToUtf8(c, paramOutputStream);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 789 */     paramOutputStream.write((byte[])_END_COMM.clone());
/* 790 */     if (paramInt == -1)
/* 791 */       paramOutputStream.write(10);
/*     */   }
/*     */ 
/*     */   static final void outputTextToWriter(String paramString, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 803 */     int i = paramString.length();
/*     */ 
/* 805 */     for (int j = 0; j < i; j++) {
/* 806 */       char c = paramString.charAt(j);
/*     */       byte[] arrayOfByte;
/* 808 */       switch (c)
/*     */       {
/*     */       case '&':
/* 811 */         arrayOfByte = (byte[])_AMP_.clone();
/* 812 */         break;
/*     */       case '<':
/* 815 */         arrayOfByte = (byte[])_LT_.clone();
/* 816 */         break;
/*     */       case '>':
/* 819 */         arrayOfByte = (byte[])_GT_.clone();
/* 820 */         break;
/*     */       case '\r':
/* 823 */         arrayOfByte = (byte[])__XD_.clone();
/* 824 */         break;
/*     */       default:
/* 827 */         if (c < '')
/* 828 */           paramOutputStream.write(c);
/*     */         else {
/* 830 */           UtfHelpper.writeCharToUtf8(c, paramOutputStream);
/*     */         }
/* 832 */         break;
/*     */       }
/* 834 */       paramOutputStream.write(arrayOfByte);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Attr getNullNode(Document paramDocument)
/*     */   {
/* 840 */     if (this.nullNode == null) {
/*     */       try {
/* 842 */         this.nullNode = paramDocument.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns");
/*     */ 
/* 844 */         this.nullNode.setValue("");
/*     */       } catch (Exception localException) {
/* 846 */         throw new RuntimeException("Unable to create nullNode: " + localException);
/*     */       }
/*     */     }
/* 849 */     return this.nullNode;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.implementations.CanonicalizerBase
 * JD-Core Version:    0.6.2
 */