/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.dtm.ref.dom2dtm.DOM2DTM.CharacterNodeHandler;
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
/*     */ public class TreeWalker
/*     */ {
/*  51 */   private ContentHandler m_contentHandler = null;
/*     */   protected DOMHelper m_dh;
/*  60 */   private LocatorImpl m_locator = new LocatorImpl();
/*     */ 
/* 265 */   boolean nextIsRaw = false;
/*     */ 
/*     */   public ContentHandler getContentHandler()
/*     */   {
/*  69 */     return this.m_contentHandler;
/*     */   }
/*     */ 
/*     */   public void setContentHandler(ContentHandler ch)
/*     */   {
/*  79 */     this.m_contentHandler = ch;
/*     */   }
/*     */ 
/*     */   public TreeWalker(ContentHandler contentHandler, DOMHelper dh, String systemId)
/*     */   {
/*  90 */     this.m_contentHandler = contentHandler;
/*  91 */     this.m_contentHandler.setDocumentLocator(this.m_locator);
/*  92 */     if (systemId != null)
/*  93 */       this.m_locator.setSystemId(systemId);
/*     */     else
/*     */       try
/*     */       {
/*  97 */         this.m_locator.setSystemId(SecuritySupport.getSystemProperty("user.dir") + File.separator + "dummy.xsl");
/*     */       }
/*     */       catch (SecurityException se)
/*     */       {
/*     */       }
/* 102 */     this.m_dh = dh;
/*     */   }
/*     */ 
/*     */   public TreeWalker(ContentHandler contentHandler, DOMHelper dh)
/*     */   {
/* 112 */     this.m_contentHandler = contentHandler;
/* 113 */     this.m_contentHandler.setDocumentLocator(this.m_locator);
/*     */     try
/*     */     {
/* 116 */       this.m_locator.setSystemId(SecuritySupport.getSystemProperty("user.dir") + File.separator + "dummy.xsl");
/*     */     }
/*     */     catch (SecurityException se) {
/*     */     }
/* 120 */     this.m_dh = dh;
/*     */   }
/*     */ 
/*     */   public TreeWalker(ContentHandler contentHandler)
/*     */   {
/* 130 */     this.m_contentHandler = contentHandler;
/* 131 */     if (this.m_contentHandler != null)
/* 132 */       this.m_contentHandler.setDocumentLocator(this.m_locator);
/*     */     try
/*     */     {
/* 135 */       this.m_locator.setSystemId(SecuritySupport.getSystemProperty("user.dir") + File.separator + "dummy.xsl");
/*     */     }
/*     */     catch (SecurityException se)
/*     */     {
/*     */     }
/* 140 */     this.m_dh = new DOM2Helper();
/*     */   }
/*     */ 
/*     */   public void traverse(Node pos)
/*     */     throws SAXException
/*     */   {
/* 157 */     this.m_contentHandler.startDocument();
/*     */ 
/* 159 */     traverseFragment(pos);
/*     */ 
/* 161 */     this.m_contentHandler.endDocument();
/*     */   }
/*     */ 
/*     */   public void traverseFragment(Node pos)
/*     */     throws SAXException
/*     */   {
/* 176 */     Node top = pos;
/*     */ 
/* 178 */     while (null != pos)
/*     */     {
/* 180 */       startNode(pos);
/*     */ 
/* 182 */       Node nextNode = pos.getFirstChild();
/*     */ 
/* 184 */       while (null == nextNode)
/*     */       {
/* 186 */         endNode(pos);
/*     */ 
/* 188 */         if (!top.equals(pos))
/*     */         {
/* 191 */           nextNode = pos.getNextSibling();
/*     */ 
/* 193 */           if (null == nextNode)
/*     */           {
/* 195 */             pos = pos.getParentNode();
/*     */ 
/* 197 */             if ((null == pos) || (top.equals(pos)))
/*     */             {
/* 199 */               if (null != pos) {
/* 200 */                 endNode(pos);
/*     */               }
/* 202 */               nextNode = null;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 209 */       pos = nextNode;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void traverse(Node pos, Node top)
/*     */     throws SAXException
/*     */   {
/* 229 */     this.m_contentHandler.startDocument();
/*     */ 
/* 231 */     while (null != pos)
/*     */     {
/* 233 */       startNode(pos);
/*     */ 
/* 235 */       Node nextNode = pos.getFirstChild();
/*     */ 
/* 237 */       while (null == nextNode)
/*     */       {
/* 239 */         endNode(pos);
/*     */ 
/* 241 */         if ((null == top) || (!top.equals(pos)))
/*     */         {
/* 244 */           nextNode = pos.getNextSibling();
/*     */ 
/* 246 */           if (null == nextNode)
/*     */           {
/* 248 */             pos = pos.getParentNode();
/*     */ 
/* 250 */             if ((null == pos) || ((null != top) && (top.equals(pos))))
/*     */             {
/* 252 */               nextNode = null;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 259 */       pos = nextNode;
/*     */     }
/* 261 */     this.m_contentHandler.endDocument();
/*     */   }
/*     */ 
/*     */   private final void dispatachChars(Node node)
/*     */     throws SAXException
/*     */   {
/* 273 */     if ((this.m_contentHandler instanceof DOM2DTM.CharacterNodeHandler))
/*     */     {
/* 275 */       ((DOM2DTM.CharacterNodeHandler)this.m_contentHandler).characters(node);
/*     */     }
/*     */     else
/*     */     {
/* 279 */       String data = ((Text)node).getData();
/* 280 */       this.m_contentHandler.characters(data.toCharArray(), 0, data.length());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void startNode(Node node)
/*     */     throws SAXException
/*     */   {
/* 295 */     if ((this.m_contentHandler instanceof NodeConsumer))
/*     */     {
/* 297 */       ((NodeConsumer)this.m_contentHandler).setOriginatingNode(node);
/*     */     }
/*     */ 
/* 300 */     if ((node instanceof Locator))
/*     */     {
/* 302 */       Locator loc = (Locator)node;
/* 303 */       this.m_locator.setColumnNumber(loc.getColumnNumber());
/* 304 */       this.m_locator.setLineNumber(loc.getLineNumber());
/* 305 */       this.m_locator.setPublicId(loc.getPublicId());
/* 306 */       this.m_locator.setSystemId(loc.getSystemId());
/*     */     }
/*     */     else
/*     */     {
/* 310 */       this.m_locator.setColumnNumber(0);
/* 311 */       this.m_locator.setLineNumber(0);
/*     */     }
/*     */ 
/* 314 */     switch (node.getNodeType())
/*     */     {
/*     */     case 8:
/* 318 */       String data = ((Comment)node).getData();
/*     */ 
/* 320 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 322 */         LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
/*     */ 
/* 324 */         lh.comment(data.toCharArray(), 0, data.length());
/*     */       }
/*     */ 
/* 327 */       break;
/*     */     case 11:
/* 331 */       break;
/*     */     case 9:
/* 334 */       break;
/*     */     case 1:
/* 336 */       NamedNodeMap atts = ((Element)node).getAttributes();
/* 337 */       int nAttrs = atts.getLength();
/*     */ 
/* 340 */       for (int i = 0; i < nAttrs; i++)
/*     */       {
/* 342 */         Node attr = atts.item(i);
/* 343 */         String attrName = attr.getNodeName();
/*     */ 
/* 346 */         if ((attrName.equals("xmlns")) || (attrName.startsWith("xmlns:")))
/*     */         {
/*     */           int index;
/* 353 */           String prefix = (index = attrName.indexOf(":")) < 0 ? "" : attrName.substring(index + 1);
/*     */ 
/* 356 */           this.m_contentHandler.startPrefixMapping(prefix, attr.getNodeValue());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 364 */       String ns = this.m_dh.getNamespaceOfNode(node);
/* 365 */       if (null == ns)
/* 366 */         ns = "";
/* 367 */       this.m_contentHandler.startElement(ns, this.m_dh.getLocalNameOfNode(node), node.getNodeName(), new AttList(atts, this.m_dh));
/*     */ 
/* 371 */       break;
/*     */     case 7:
/* 374 */       ProcessingInstruction pi = (ProcessingInstruction)node;
/* 375 */       String name = pi.getNodeName();
/*     */ 
/* 378 */       if (name.equals("xslt-next-is-raw"))
/*     */       {
/* 380 */         this.nextIsRaw = true;
/*     */       }
/*     */       else
/*     */       {
/* 384 */         this.m_contentHandler.processingInstruction(pi.getNodeName(), pi.getData());
/*     */       }
/*     */ 
/* 388 */       break;
/*     */     case 4:
/* 391 */       boolean isLexH = this.m_contentHandler instanceof LexicalHandler;
/* 392 */       LexicalHandler lh = isLexH ? (LexicalHandler)this.m_contentHandler : null;
/*     */ 
/* 395 */       if (isLexH)
/*     */       {
/* 397 */         lh.startCDATA();
/*     */       }
/*     */ 
/* 400 */       dispatachChars(node);
/*     */ 
/* 403 */       if (isLexH)
/*     */       {
/* 405 */         lh.endCDATA();
/*     */       }
/*     */ 
/* 409 */       break;
/*     */     case 3:
/* 414 */       if (this.nextIsRaw)
/*     */       {
/* 416 */         this.nextIsRaw = false;
/*     */ 
/* 418 */         this.m_contentHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
/* 419 */         dispatachChars(node);
/* 420 */         this.m_contentHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
/*     */       }
/*     */       else
/*     */       {
/* 424 */         dispatachChars(node);
/*     */       }
/*     */ 
/* 427 */       break;
/*     */     case 5:
/* 430 */       EntityReference eref = (EntityReference)node;
/*     */ 
/* 432 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 434 */         ((LexicalHandler)this.m_contentHandler).startEntity(eref.getNodeName());
/*     */       }
/*     */ 
/* 443 */       break;
/*     */     case 2:
/*     */     case 6:
/*     */     case 10:
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void endNode(Node node)
/*     */     throws SAXException
/*     */   {
/* 459 */     switch (node.getNodeType())
/*     */     {
/*     */     case 9:
/* 462 */       break;
/*     */     case 1:
/* 465 */       String ns = this.m_dh.getNamespaceOfNode(node);
/* 466 */       if (null == ns)
/* 467 */         ns = "";
/* 468 */       this.m_contentHandler.endElement(ns, this.m_dh.getLocalNameOfNode(node), node.getNodeName());
/*     */ 
/* 472 */       NamedNodeMap atts = ((Element)node).getAttributes();
/* 473 */       int nAttrs = atts.getLength();
/*     */ 
/* 475 */       for (int i = 0; i < nAttrs; i++)
/*     */       {
/* 477 */         Node attr = atts.item(i);
/* 478 */         String attrName = attr.getNodeName();
/*     */ 
/* 480 */         if ((attrName.equals("xmlns")) || (attrName.startsWith("xmlns:")))
/*     */         {
/*     */           int index;
/* 486 */           String prefix = (index = attrName.indexOf(":")) < 0 ? "" : attrName.substring(index + 1);
/*     */ 
/* 489 */           this.m_contentHandler.endPrefixMapping(prefix);
/*     */         }
/*     */       }
/* 492 */       break;
/*     */     case 4:
/* 494 */       break;
/*     */     case 5:
/* 497 */       EntityReference eref = (EntityReference)node;
/*     */ 
/* 499 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 501 */         LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
/*     */ 
/* 503 */         lh.endEntity(eref.getNodeName());
/*     */       }
/*     */ 
/* 506 */       break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.TreeWalker
 * JD-Core Version:    0.6.2
 */