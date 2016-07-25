/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.AttList;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.DOM2Helper;
/*     */ import java.io.File;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.EntityReference;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ public final class TreeWalker
/*     */ {
/*     */   private final ContentHandler m_contentHandler;
/*     */   private final SerializationHandler m_Serializer;
/*     */   protected final DOM2Helper m_dh;
/*  71 */   private final LocatorImpl m_locator = new LocatorImpl();
/*     */ 
/* 234 */   boolean nextIsRaw = false;
/*     */ 
/*     */   public ContentHandler getContentHandler()
/*     */   {
/*  80 */     return this.m_contentHandler;
/*     */   }
/*     */ 
/*     */   public TreeWalker(ContentHandler ch) {
/*  84 */     this(ch, null);
/*     */   }
/*     */ 
/*     */   public TreeWalker(ContentHandler contentHandler, String systemId)
/*     */   {
/*  94 */     this.m_contentHandler = contentHandler;
/*  95 */     if ((this.m_contentHandler instanceof SerializationHandler)) {
/*  96 */       this.m_Serializer = ((SerializationHandler)this.m_contentHandler);
/*     */     }
/*     */     else {
/*  99 */       this.m_Serializer = null;
/*     */     }
/*     */ 
/* 102 */     this.m_contentHandler.setDocumentLocator(this.m_locator);
/* 103 */     if (systemId != null)
/* 104 */       this.m_locator.setSystemId(systemId);
/*     */     else {
/*     */       try
/*     */       {
/* 108 */         this.m_locator.setSystemId(SecuritySupport.getSystemProperty("user.dir") + File.separator + "dummy.xsl");
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/*     */       }
/*     */     }
/*     */ 
/* 115 */     if (this.m_contentHandler != null)
/* 116 */       this.m_contentHandler.setDocumentLocator(this.m_locator);
/*     */     try
/*     */     {
/* 119 */       this.m_locator.setSystemId(SecuritySupport.getSystemProperty("user.dir") + File.separator + "dummy.xsl");
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/*     */     }
/* 124 */     this.m_dh = new DOM2Helper();
/*     */   }
/*     */ 
/*     */   public void traverse(Node pos)
/*     */     throws SAXException
/*     */   {
/* 142 */     this.m_contentHandler.startDocument();
/*     */ 
/* 144 */     Node top = pos;
/*     */ 
/* 146 */     while (null != pos)
/*     */     {
/* 148 */       startNode(pos);
/*     */ 
/* 150 */       Node nextNode = pos.getFirstChild();
/*     */ 
/* 152 */       while (null == nextNode)
/*     */       {
/* 154 */         endNode(pos);
/*     */ 
/* 156 */         if (!top.equals(pos))
/*     */         {
/* 159 */           nextNode = pos.getNextSibling();
/*     */ 
/* 161 */           if (null == nextNode)
/*     */           {
/* 163 */             pos = pos.getParentNode();
/*     */ 
/* 165 */             if ((null == pos) || (top.equals(pos)))
/*     */             {
/* 167 */               if (null != pos) {
/* 168 */                 endNode(pos);
/*     */               }
/* 170 */               nextNode = null;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 177 */       pos = nextNode;
/*     */     }
/* 179 */     this.m_contentHandler.endDocument();
/*     */   }
/*     */ 
/*     */   public void traverse(Node pos, Node top)
/*     */     throws SAXException
/*     */   {
/* 198 */     this.m_contentHandler.startDocument();
/*     */ 
/* 200 */     while (null != pos)
/*     */     {
/* 202 */       startNode(pos);
/*     */ 
/* 204 */       Node nextNode = pos.getFirstChild();
/*     */ 
/* 206 */       while (null == nextNode)
/*     */       {
/* 208 */         endNode(pos);
/*     */ 
/* 210 */         if ((null == top) || (!top.equals(pos)))
/*     */         {
/* 213 */           nextNode = pos.getNextSibling();
/*     */ 
/* 215 */           if (null == nextNode)
/*     */           {
/* 217 */             pos = pos.getParentNode();
/*     */ 
/* 219 */             if ((null == pos) || ((null != top) && (top.equals(pos))))
/*     */             {
/* 221 */               nextNode = null;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 228 */       pos = nextNode;
/*     */     }
/* 230 */     this.m_contentHandler.endDocument();
/*     */   }
/*     */ 
/*     */   private final void dispatachChars(Node node)
/*     */     throws SAXException
/*     */   {
/* 242 */     if (this.m_Serializer != null)
/*     */     {
/* 244 */       this.m_Serializer.characters(node);
/*     */     }
/*     */     else
/*     */     {
/* 248 */       String data = ((Text)node).getData();
/* 249 */       this.m_contentHandler.characters(data.toCharArray(), 0, data.length());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void startNode(Node node)
/*     */     throws SAXException
/*     */   {
/* 274 */     if ((node instanceof Locator))
/*     */     {
/* 276 */       Locator loc = (Locator)node;
/* 277 */       this.m_locator.setColumnNumber(loc.getColumnNumber());
/* 278 */       this.m_locator.setLineNumber(loc.getLineNumber());
/* 279 */       this.m_locator.setPublicId(loc.getPublicId());
/* 280 */       this.m_locator.setSystemId(loc.getSystemId());
/*     */     }
/*     */     else
/*     */     {
/* 284 */       this.m_locator.setColumnNumber(0);
/* 285 */       this.m_locator.setLineNumber(0);
/*     */     }
/*     */ 
/* 288 */     switch (node.getNodeType())
/*     */     {
/*     */     case 8:
/* 292 */       String data = ((Comment)node).getData();
/*     */ 
/* 294 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 296 */         LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
/*     */ 
/* 298 */         lh.comment(data.toCharArray(), 0, data.length());
/*     */       }
/*     */ 
/* 301 */       break;
/*     */     case 11:
/* 305 */       break;
/*     */     case 9:
/* 308 */       break;
/*     */     case 1:
/* 310 */       Element elem_node = (Element)node;
/*     */ 
/* 315 */       String uri = elem_node.getNamespaceURI();
/* 316 */       if (uri != null) {
/* 317 */         String prefix = elem_node.getPrefix();
/* 318 */         if (prefix == null)
/* 319 */           prefix = "";
/* 320 */         this.m_contentHandler.startPrefixMapping(prefix, uri);
/*     */       }
/*     */ 
/* 323 */       NamedNodeMap atts = elem_node.getAttributes();
/* 324 */       int nAttrs = atts.getLength();
/*     */ 
/* 330 */       for (int i = 0; i < nAttrs; i++)
/*     */       {
/* 332 */         Node attr = atts.item(i);
/* 333 */         String attrName = attr.getNodeName();
/* 334 */         int colon = attrName.indexOf(':');
/*     */ 
/* 338 */         if ((attrName.equals("xmlns")) || (attrName.startsWith("xmlns:")))
/*     */         {
/*     */           String prefix;
/*     */           String prefix;
/* 343 */           if (colon < 0)
/* 344 */             prefix = "";
/*     */           else {
/* 346 */             prefix = attrName.substring(colon + 1);
/*     */           }
/* 348 */           this.m_contentHandler.startPrefixMapping(prefix, attr.getNodeValue());
/*     */         }
/* 351 */         else if (colon > 0) {
/* 352 */           String prefix = attrName.substring(0, colon);
/* 353 */           String uri = attr.getNamespaceURI();
/* 354 */           if (uri != null) {
/* 355 */             this.m_contentHandler.startPrefixMapping(prefix, uri);
/*     */           }
/*     */         }
/*     */       }
/* 359 */       String ns = this.m_dh.getNamespaceOfNode(node);
/* 360 */       if (null == ns)
/* 361 */         ns = "";
/* 362 */       this.m_contentHandler.startElement(ns, this.m_dh.getLocalNameOfNode(node), node.getNodeName(), new AttList(atts, this.m_dh));
/*     */ 
/* 366 */       break;
/*     */     case 7:
/* 369 */       ProcessingInstruction pi = (ProcessingInstruction)node;
/* 370 */       String name = pi.getNodeName();
/*     */ 
/* 373 */       if (name.equals("xslt-next-is-raw"))
/*     */       {
/* 375 */         this.nextIsRaw = true;
/*     */       }
/*     */       else
/*     */       {
/* 379 */         this.m_contentHandler.processingInstruction(pi.getNodeName(), pi.getData());
/*     */       }
/*     */ 
/* 383 */       break;
/*     */     case 4:
/* 386 */       boolean isLexH = this.m_contentHandler instanceof LexicalHandler;
/* 387 */       LexicalHandler lh = isLexH ? (LexicalHandler)this.m_contentHandler : null;
/*     */ 
/* 390 */       if (isLexH)
/*     */       {
/* 392 */         lh.startCDATA();
/*     */       }
/*     */ 
/* 395 */       dispatachChars(node);
/*     */ 
/* 398 */       if (isLexH)
/*     */       {
/* 400 */         lh.endCDATA();
/*     */       }
/*     */ 
/* 404 */       break;
/*     */     case 3:
/* 409 */       if (this.nextIsRaw)
/*     */       {
/* 411 */         this.nextIsRaw = false;
/*     */ 
/* 413 */         this.m_contentHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
/* 414 */         dispatachChars(node);
/* 415 */         this.m_contentHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
/*     */       }
/*     */       else
/*     */       {
/* 419 */         dispatachChars(node);
/*     */       }
/*     */ 
/* 422 */       break;
/*     */     case 5:
/* 425 */       EntityReference eref = (EntityReference)node;
/*     */ 
/* 427 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 429 */         ((LexicalHandler)this.m_contentHandler).startEntity(eref.getNodeName());
/*     */       }
/*     */ 
/* 438 */       break;
/*     */     case 2:
/*     */     case 6:
/*     */     case 10:
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void endNode(Node node)
/*     */     throws SAXException
/*     */   {
/* 454 */     switch (node.getNodeType())
/*     */     {
/*     */     case 9:
/* 457 */       break;
/*     */     case 1:
/* 460 */       String ns = this.m_dh.getNamespaceOfNode(node);
/* 461 */       if (null == ns)
/* 462 */         ns = "";
/* 463 */       this.m_contentHandler.endElement(ns, this.m_dh.getLocalNameOfNode(node), node.getNodeName());
/*     */ 
/* 467 */       if (this.m_Serializer == null)
/*     */       {
/* 471 */         Element elem_node = (Element)node;
/* 472 */         NamedNodeMap atts = elem_node.getAttributes();
/* 473 */         int nAttrs = atts.getLength();
/*     */ 
/* 477 */         for (int i = nAttrs - 1; 0 <= i; i--)
/*     */         {
/* 479 */           Node attr = atts.item(i);
/* 480 */           String attrName = attr.getNodeName();
/* 481 */           int colon = attrName.indexOf(':');
/*     */ 
/* 484 */           if ((attrName.equals("xmlns")) || (attrName.startsWith("xmlns:")))
/*     */           {
/*     */             String prefix;
/*     */             String prefix;
/* 489 */             if (colon < 0)
/* 490 */               prefix = "";
/*     */             else {
/* 492 */               prefix = attrName.substring(colon + 1);
/*     */             }
/* 494 */             this.m_contentHandler.endPrefixMapping(prefix);
/*     */           }
/* 496 */           else if (colon > 0) {
/* 497 */             String prefix = attrName.substring(0, colon);
/* 498 */             this.m_contentHandler.endPrefixMapping(prefix);
/*     */           }
/*     */         }
/*     */ 
/* 502 */         String uri = elem_node.getNamespaceURI();
/* 503 */         if (uri != null) {
/* 504 */           String prefix = elem_node.getPrefix();
/* 505 */           if (prefix == null)
/* 506 */             prefix = "";
/* 507 */           this.m_contentHandler.endPrefixMapping(prefix);
/*     */         }
/*     */       }
/* 510 */       break;
/*     */     case 4:
/* 513 */       break;
/*     */     case 5:
/* 516 */       EntityReference eref = (EntityReference)node;
/*     */ 
/* 518 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 520 */         LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
/*     */ 
/* 522 */         lh.endEntity(eref.getNodeName());
/*     */       }
/*     */ 
/* 525 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.TreeWalker
 * JD-Core Version:    0.6.2
 */