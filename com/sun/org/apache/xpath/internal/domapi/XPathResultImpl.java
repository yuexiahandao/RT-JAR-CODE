/*     */ package com.sun.org.apache.xpath.internal.domapi;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.XPath;
/*     */ import com.sun.org.apache.xpath.internal.objects.XObject;
/*     */ import com.sun.org.apache.xpath.internal.res.XPATHMessages;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.events.Event;
/*     */ import org.w3c.dom.events.EventListener;
/*     */ import org.w3c.dom.events.EventTarget;
/*     */ import org.w3c.dom.traversal.NodeIterator;
/*     */ import org.w3c.dom.xpath.XPathException;
/*     */ import org.w3c.dom.xpath.XPathResult;
/*     */ 
/*     */ class XPathResultImpl
/*     */   implements XPathResult, EventListener
/*     */ {
/*     */   private final XObject m_resultObj;
/*     */   private final XPath m_xpath;
/*     */   private final short m_resultType;
/*  81 */   private boolean m_isInvalidIteratorState = false;
/*     */   private final Node m_contextNode;
/*  92 */   private NodeIterator m_iterator = null;
/*     */ 
/*  97 */   private NodeList m_list = null;
/*     */ 
/*     */   XPathResultImpl(short type, XObject result, Node contextNode, XPath xpath)
/*     */   {
/* 107 */     if (!isValidType(type)) {
/* 108 */       String fmsg = XPATHMessages.createXPATHMessage("ER_INVALID_XPATH_TYPE", new Object[] { new Integer(type) });
/* 109 */       throw new XPathException((short)2, fmsg);
/*     */     }
/*     */ 
/* 113 */     if (null == result) {
/* 114 */       String fmsg = XPATHMessages.createXPATHMessage("ER_EMPTY_XPATH_RESULT", null);
/* 115 */       throw new XPathException((short)1, fmsg);
/*     */     }
/*     */ 
/* 118 */     this.m_resultObj = result;
/* 119 */     this.m_contextNode = contextNode;
/* 120 */     this.m_xpath = xpath;
/*     */ 
/* 123 */     if (type == 0)
/* 124 */       this.m_resultType = getTypeFromXObject(result);
/*     */     else {
/* 126 */       this.m_resultType = type;
/*     */     }
/*     */ 
/* 131 */     if ((this.m_resultType == 5) || (this.m_resultType == 4))
/*     */     {
/* 133 */       addEventListener();
/*     */     }
/*     */ 
/* 138 */     if ((this.m_resultType == 5) || (this.m_resultType == 4) || (this.m_resultType == 8) || (this.m_resultType == 9))
/*     */     {
/*     */       try
/*     */       {
/* 144 */         this.m_iterator = this.m_resultObj.nodeset();
/*     */       }
/*     */       catch (TransformerException te) {
/* 147 */         String fmsg = XPATHMessages.createXPATHMessage("ER_INCOMPATIBLE_TYPES", new Object[] { this.m_xpath.getPatternString(), getTypeString(getTypeFromXObject(this.m_resultObj)), getTypeString(this.m_resultType) });
/* 148 */         throw new XPathException((short)2, fmsg);
/*     */       }
/*     */ 
/*     */     }
/* 159 */     else if ((this.m_resultType == 6) || (this.m_resultType == 7))
/*     */       try
/*     */       {
/* 162 */         this.m_list = this.m_resultObj.nodelist();
/*     */       }
/*     */       catch (TransformerException te) {
/* 165 */         String fmsg = XPATHMessages.createXPATHMessage("ER_INCOMPATIBLE_TYPES", new Object[] { this.m_xpath.getPatternString(), getTypeString(getTypeFromXObject(this.m_resultObj)), getTypeString(this.m_resultType) });
/* 166 */         throw new XPathException((short)2, fmsg);
/*     */       }
/*     */   }
/*     */ 
/*     */   public short getResultType()
/*     */   {
/* 175 */     return this.m_resultType;
/*     */   }
/*     */ 
/*     */   public double getNumberValue()
/*     */     throws XPathException
/*     */   {
/* 186 */     if (getResultType() != 1) {
/* 187 */       String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_XPATHRESULTTYPE_TO_NUMBER", new Object[] { this.m_xpath.getPatternString(), getTypeString(this.m_resultType) });
/* 188 */       throw new XPathException((short)2, fmsg);
/*     */     }
/*     */     try
/*     */     {
/* 192 */       return this.m_resultObj.num();
/*     */     }
/*     */     catch (Exception e) {
/* 195 */       throw new XPathException((short)2, e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getStringValue()
/*     */     throws XPathException
/*     */   {
/* 209 */     if (getResultType() != 2) {
/* 210 */       String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_STRING", new Object[] { this.m_xpath.getPatternString(), this.m_resultObj.getTypeString() });
/* 211 */       throw new XPathException((short)2, fmsg);
/*     */     }
/*     */     try
/*     */     {
/* 215 */       return this.m_resultObj.str();
/*     */     }
/*     */     catch (Exception e) {
/* 218 */       throw new XPathException((short)2, e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getBooleanValue()
/*     */     throws XPathException
/*     */   {
/* 227 */     if (getResultType() != 3) {
/* 228 */       String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_BOOLEAN", new Object[] { this.m_xpath.getPatternString(), getTypeString(this.m_resultType) });
/* 229 */       throw new XPathException((short)2, fmsg);
/*     */     }
/*     */     try
/*     */     {
/* 233 */       return this.m_resultObj.bool();
/*     */     }
/*     */     catch (TransformerException e) {
/* 236 */       throw new XPathException((short)2, e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Node getSingleNodeValue()
/*     */     throws XPathException
/*     */   {
/* 252 */     if ((this.m_resultType != 8) && (this.m_resultType != 9))
/*     */     {
/* 254 */       String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_SINGLENODE", new Object[] { this.m_xpath.getPatternString(), getTypeString(this.m_resultType) });
/* 255 */       throw new XPathException((short)2, fmsg);
/*     */     }
/*     */ 
/* 260 */     NodeIterator result = null;
/*     */     try {
/* 262 */       result = this.m_resultObj.nodeset();
/*     */     } catch (TransformerException te) {
/* 264 */       throw new XPathException((short)2, te.getMessage());
/*     */     }
/*     */ 
/* 267 */     if (null == result) return null;
/*     */ 
/* 269 */     Node node = result.nextNode();
/*     */ 
/* 272 */     if (isNamespaceNode(node)) {
/* 273 */       return new XPathNamespaceImpl(node);
/*     */     }
/* 275 */     return node;
/*     */   }
/*     */ 
/*     */   public boolean getInvalidIteratorState()
/*     */   {
/* 283 */     return this.m_isInvalidIteratorState;
/*     */   }
/*     */ 
/*     */   public int getSnapshotLength()
/*     */     throws XPathException
/*     */   {
/* 299 */     if ((this.m_resultType != 6) && (this.m_resultType != 7))
/*     */     {
/* 301 */       String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_GET_SNAPSHOT_LENGTH", new Object[] { this.m_xpath.getPatternString(), getTypeString(this.m_resultType) });
/* 302 */       throw new XPathException((short)2, fmsg);
/*     */     }
/*     */ 
/* 306 */     return this.m_list.getLength();
/*     */   }
/*     */ 
/*     */   public Node iterateNext()
/*     */     throws XPathException, DOMException
/*     */   {
/* 323 */     if ((this.m_resultType != 4) && (this.m_resultType != 5))
/*     */     {
/* 325 */       String fmsg = XPATHMessages.createXPATHMessage("ER_NON_ITERATOR_TYPE", new Object[] { this.m_xpath.getPatternString(), getTypeString(this.m_resultType) });
/* 326 */       throw new XPathException((short)2, fmsg);
/*     */     }
/*     */ 
/* 331 */     if (getInvalidIteratorState()) {
/* 332 */       String fmsg = XPATHMessages.createXPATHMessage("ER_DOC_MUTATED", null);
/* 333 */       throw new DOMException((short)11, fmsg);
/*     */     }
/*     */ 
/* 336 */     Node node = this.m_iterator.nextNode();
/* 337 */     if (null == node) {
/* 338 */       removeEventListener();
/*     */     }
/* 340 */     if (isNamespaceNode(node)) {
/* 341 */       return new XPathNamespaceImpl(node);
/*     */     }
/* 343 */     return node;
/*     */   }
/*     */ 
/*     */   public Node snapshotItem(int index)
/*     */     throws XPathException
/*     */   {
/* 366 */     if ((this.m_resultType != 6) && (this.m_resultType != 7))
/*     */     {
/* 368 */       String fmsg = XPATHMessages.createXPATHMessage("ER_NON_SNAPSHOT_TYPE", new Object[] { this.m_xpath.getPatternString(), getTypeString(this.m_resultType) });
/* 369 */       throw new XPathException((short)2, fmsg);
/*     */     }
/*     */ 
/* 374 */     Node node = this.m_list.item(index);
/*     */ 
/* 377 */     if (isNamespaceNode(node)) {
/* 378 */       return new XPathNamespaceImpl(node);
/*     */     }
/* 380 */     return node;
/*     */   }
/*     */ 
/*     */   static boolean isValidType(short type)
/*     */   {
/* 392 */     switch (type) { case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/* 402 */       return true; }
/* 403 */     return false;
/*     */   }
/*     */ 
/*     */   public void handleEvent(Event event)
/*     */   {
/* 412 */     if (event.getType().equals("DOMSubtreeModified"))
/*     */     {
/* 414 */       this.m_isInvalidIteratorState = true;
/*     */ 
/* 417 */       removeEventListener();
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getTypeString(int type)
/*     */   {
/* 429 */     switch (type) { case 0:
/* 430 */       return "ANY_TYPE";
/*     */     case 8:
/* 431 */       return "ANY_UNORDERED_NODE_TYPE";
/*     */     case 3:
/* 432 */       return "BOOLEAN";
/*     */     case 9:
/* 433 */       return "FIRST_ORDERED_NODE_TYPE";
/*     */     case 1:
/* 434 */       return "NUMBER_TYPE";
/*     */     case 5:
/* 435 */       return "ORDERED_NODE_ITERATOR_TYPE";
/*     */     case 7:
/* 436 */       return "ORDERED_NODE_SNAPSHOT_TYPE";
/*     */     case 2:
/* 437 */       return "STRING_TYPE";
/*     */     case 4:
/* 438 */       return "UNORDERED_NODE_ITERATOR_TYPE";
/*     */     case 6:
/* 439 */       return "UNORDERED_NODE_SNAPSHOT_TYPE"; }
/* 440 */     return "#UNKNOWN";
/*     */   }
/*     */ 
/*     */   private short getTypeFromXObject(XObject object)
/*     */   {
/* 450 */     switch (object.getType()) { case 1:
/* 451 */       return 3;
/*     */     case 4:
/* 452 */       return 4;
/*     */     case 2:
/* 453 */       return 1;
/*     */     case 3:
/* 454 */       return 2;
/*     */     case 5:
/* 467 */       return 4;
/*     */     case -1:
/* 468 */       return 0;
/* 469 */     case 0: } return 0;
/*     */   }
/*     */ 
/*     */   private boolean isNamespaceNode(Node node)
/*     */   {
/* 483 */     if ((null != node) && (node.getNodeType() == 2) && ((node.getNodeName().startsWith("xmlns:")) || (node.getNodeName().equals("xmlns"))))
/*     */     {
/* 486 */       return true;
/*     */     }
/* 488 */     return false;
/*     */   }
/*     */ 
/*     */   private void addEventListener()
/*     */   {
/* 497 */     if ((this.m_contextNode instanceof EventTarget))
/* 498 */       ((EventTarget)this.m_contextNode).addEventListener("DOMSubtreeModified", this, true);
/*     */   }
/*     */ 
/*     */   private void removeEventListener()
/*     */   {
/* 508 */     if ((this.m_contextNode instanceof EventTarget))
/* 509 */       ((EventTarget)this.m_contextNode).removeEventListener("DOMSubtreeModified", this, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.domapi.XPathResultImpl
 * JD-Core Version:    0.6.2
 */