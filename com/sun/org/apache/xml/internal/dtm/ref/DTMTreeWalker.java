/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.utils.NodeConsumer;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLString;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public class DTMTreeWalker
/*     */ {
/*  45 */   private ContentHandler m_contentHandler = null;
/*     */   protected DTM m_dtm;
/* 198 */   boolean nextIsRaw = false;
/*     */ 
/*     */   public void setDTM(DTM dtm)
/*     */   {
/*  57 */     this.m_dtm = dtm;
/*     */   }
/*     */ 
/*     */   public ContentHandler getcontentHandler()
/*     */   {
/*  67 */     return this.m_contentHandler;
/*     */   }
/*     */ 
/*     */   public void setcontentHandler(ContentHandler ch)
/*     */   {
/*  77 */     this.m_contentHandler = ch;
/*     */   }
/*     */ 
/*     */   public DTMTreeWalker()
/*     */   {
/*     */   }
/*     */ 
/*     */   public DTMTreeWalker(ContentHandler contentHandler, DTM dtm)
/*     */   {
/*  95 */     this.m_contentHandler = contentHandler;
/*  96 */     this.m_dtm = dtm;
/*     */   }
/*     */ 
/*     */   public void traverse(int pos)
/*     */     throws SAXException
/*     */   {
/* 112 */     int top = pos;
/*     */ 
/* 114 */     while (-1 != pos)
/*     */     {
/* 116 */       startNode(pos);
/* 117 */       int nextNode = this.m_dtm.getFirstChild(pos);
/* 118 */       while (-1 == nextNode)
/*     */       {
/* 120 */         endNode(pos);
/*     */ 
/* 122 */         if (top != pos)
/*     */         {
/* 125 */           nextNode = this.m_dtm.getNextSibling(pos);
/*     */ 
/* 127 */           if (-1 == nextNode)
/*     */           {
/* 129 */             pos = this.m_dtm.getParent(pos);
/*     */ 
/* 131 */             if ((-1 == pos) || (top == pos))
/*     */             {
/* 135 */               if (-1 != pos) {
/* 136 */                 endNode(pos);
/*     */               }
/* 138 */               nextNode = -1;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 145 */       pos = nextNode;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void traverse(int pos, int top)
/*     */     throws SAXException
/*     */   {
/* 167 */     while (-1 != pos)
/*     */     {
/* 169 */       startNode(pos);
/* 170 */       int nextNode = this.m_dtm.getFirstChild(pos);
/* 171 */       while (-1 == nextNode)
/*     */       {
/* 173 */         endNode(pos);
/*     */ 
/* 175 */         if ((-1 == top) || (top != pos))
/*     */         {
/* 178 */           nextNode = this.m_dtm.getNextSibling(pos);
/*     */ 
/* 180 */           if (-1 == nextNode)
/*     */           {
/* 182 */             pos = this.m_dtm.getParent(pos);
/*     */ 
/* 184 */             if ((-1 == pos) || ((-1 != top) && (top == pos)))
/*     */             {
/* 186 */               nextNode = -1;
/*     */             }
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 193 */       pos = nextNode;
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void dispatachChars(int node)
/*     */     throws SAXException
/*     */   {
/* 206 */     this.m_dtm.dispatchCharactersEvents(node, this.m_contentHandler, false);
/*     */   }
/*     */ 
/*     */   protected void startNode(int node)
/*     */     throws SAXException
/*     */   {
/* 220 */     if ((this.m_contentHandler instanceof NodeConsumer));
/* 226 */     switch (this.m_dtm.getNodeType(node))
/*     */     {
/*     */     case 8:
/* 230 */       XMLString data = this.m_dtm.getStringValue(node);
/*     */ 
/* 232 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 234 */         LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
/* 235 */         data.dispatchAsComment(lh);
/*     */       }
/*     */ 
/* 238 */       break;
/*     */     case 11:
/* 242 */       break;
/*     */     case 9:
/* 244 */       this.m_contentHandler.startDocument();
/* 245 */       break;
/*     */     case 1:
/* 247 */       DTM dtm = this.m_dtm;
/*     */ 
/* 249 */       for (int nsn = dtm.getFirstNamespaceNode(node, true); -1 != nsn; 
/* 250 */         nsn = dtm.getNextNamespaceNode(node, nsn, true))
/*     */       {
/* 253 */         String prefix = dtm.getNodeNameX(nsn);
/*     */ 
/* 255 */         this.m_contentHandler.startPrefixMapping(prefix, dtm.getNodeValue(nsn));
/*     */       }
/*     */ 
/* 261 */       String ns = dtm.getNamespaceURI(node);
/* 262 */       if (null == ns) {
/* 263 */         ns = "";
/*     */       }
/*     */ 
/* 266 */       AttributesImpl attrs = new AttributesImpl();
/*     */ 
/* 269 */       for (int i = dtm.getFirstAttribute(node); 
/* 270 */         i != -1; 
/* 271 */         i = dtm.getNextAttribute(i))
/*     */       {
/* 273 */         attrs.addAttribute(dtm.getNamespaceURI(i), dtm.getLocalName(i), dtm.getNodeName(i), "CDATA", dtm.getNodeValue(i));
/*     */       }
/*     */ 
/* 281 */       this.m_contentHandler.startElement(ns, this.m_dtm.getLocalName(node), this.m_dtm.getNodeName(node), attrs);
/*     */ 
/* 285 */       break;
/*     */     case 7:
/* 288 */       String name = this.m_dtm.getNodeName(node);
/*     */ 
/* 291 */       if (name.equals("xslt-next-is-raw"))
/*     */       {
/* 293 */         this.nextIsRaw = true;
/*     */       }
/*     */       else
/*     */       {
/* 297 */         this.m_contentHandler.processingInstruction(name, this.m_dtm.getNodeValue(node));
/*     */       }
/*     */ 
/* 301 */       break;
/*     */     case 4:
/* 304 */       boolean isLexH = this.m_contentHandler instanceof LexicalHandler;
/* 305 */       LexicalHandler lh = isLexH ? (LexicalHandler)this.m_contentHandler : null;
/*     */ 
/* 308 */       if (isLexH)
/*     */       {
/* 310 */         lh.startCDATA();
/*     */       }
/*     */ 
/* 313 */       dispatachChars(node);
/*     */ 
/* 316 */       if (isLexH)
/*     */       {
/* 318 */         lh.endCDATA();
/*     */       }
/*     */ 
/* 322 */       break;
/*     */     case 3:
/* 325 */       if (this.nextIsRaw)
/*     */       {
/* 327 */         this.nextIsRaw = false;
/*     */ 
/* 329 */         this.m_contentHandler.processingInstruction("javax.xml.transform.disable-output-escaping", "");
/* 330 */         dispatachChars(node);
/* 331 */         this.m_contentHandler.processingInstruction("javax.xml.transform.enable-output-escaping", "");
/*     */       }
/*     */       else
/*     */       {
/* 335 */         dispatachChars(node);
/*     */       }
/*     */ 
/* 338 */       break;
/*     */     case 5:
/* 341 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 343 */         ((LexicalHandler)this.m_contentHandler).startEntity(this.m_dtm.getNodeName(node)); } break;
/*     */     case 2:
/*     */     case 6:
/*     */     case 10:
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void endNode(int node)
/*     */     throws SAXException
/*     */   {
/* 368 */     switch (this.m_dtm.getNodeType(node))
/*     */     {
/*     */     case 9:
/* 371 */       this.m_contentHandler.endDocument();
/* 372 */       break;
/*     */     case 1:
/* 374 */       String ns = this.m_dtm.getNamespaceURI(node);
/* 375 */       if (null == ns)
/* 376 */         ns = "";
/* 377 */       this.m_contentHandler.endElement(ns, this.m_dtm.getLocalName(node), this.m_dtm.getNodeName(node));
/*     */ 
/* 381 */       for (int nsn = this.m_dtm.getFirstNamespaceNode(node, true); -1 != nsn; 
/* 382 */         nsn = this.m_dtm.getNextNamespaceNode(node, nsn, true))
/*     */       {
/* 385 */         String prefix = this.m_dtm.getNodeNameX(nsn);
/*     */ 
/* 387 */         this.m_contentHandler.endPrefixMapping(prefix);
/*     */       }
/* 389 */       break;
/*     */     case 4:
/* 391 */       break;
/*     */     case 5:
/* 394 */       if ((this.m_contentHandler instanceof LexicalHandler))
/*     */       {
/* 396 */         LexicalHandler lh = (LexicalHandler)this.m_contentHandler;
/*     */ 
/* 398 */         lh.endEntity(this.m_dtm.getNodeName(node));
/* 399 */       }break;
/*     */     case 2:
/*     */     case 3:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMTreeWalker
 * JD-Core Version:    0.6.2
 */