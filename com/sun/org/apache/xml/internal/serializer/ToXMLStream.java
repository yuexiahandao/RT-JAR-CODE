/*     */ package com.sun.org.apache.xml.internal.serializer;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Messages;
/*     */ import com.sun.org.apache.xml.internal.serializer.utils.Utils;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Writer;
/*     */ import javax.xml.transform.ErrorListener;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class ToXMLStream extends ToStream
/*     */ {
/*  52 */   boolean m_cdataTagOpen = false;
/*     */ 
/*  59 */   private static CharInfo m_xmlcharInfo = CharInfo.getCharInfoInternal("com.sun.org.apache.xml.internal.serializer.XMLEntities", "xml");
/*     */ 
/*     */   public ToXMLStream()
/*     */   {
/*  68 */     this.m_charInfo = m_xmlcharInfo;
/*     */ 
/*  70 */     initCDATA();
/*     */ 
/*  72 */     this.m_prefixMap = new NamespaceMappings();
/*     */   }
/*     */ 
/*     */   public void CopyFrom(ToXMLStream xmlListener)
/*     */   {
/*  84 */     this.m_writer = xmlListener.m_writer;
/*     */ 
/*  88 */     String encoding = xmlListener.getEncoding();
/*  89 */     setEncoding(encoding);
/*     */ 
/*  91 */     setOmitXMLDeclaration(xmlListener.getOmitXMLDeclaration());
/*     */ 
/*  93 */     this.m_ispreserve = xmlListener.m_ispreserve;
/*  94 */     this.m_preserves = xmlListener.m_preserves;
/*  95 */     this.m_isprevtext = xmlListener.m_isprevtext;
/*  96 */     this.m_doIndent = xmlListener.m_doIndent;
/*  97 */     setIndentAmount(xmlListener.getIndentAmount());
/*  98 */     this.m_startNewLine = xmlListener.m_startNewLine;
/*  99 */     this.m_needToOutputDocTypeDecl = xmlListener.m_needToOutputDocTypeDecl;
/* 100 */     setDoctypeSystem(xmlListener.getDoctypeSystem());
/* 101 */     setDoctypePublic(xmlListener.getDoctypePublic());
/* 102 */     setStandalone(xmlListener.getStandalone());
/* 103 */     setMediaType(xmlListener.getMediaType());
/* 104 */     this.m_maxCharacter = xmlListener.m_maxCharacter;
/* 105 */     this.m_encodingInfo = xmlListener.m_encodingInfo;
/* 106 */     this.m_spaceBeforeClose = xmlListener.m_spaceBeforeClose;
/* 107 */     this.m_cdataStartCalled = xmlListener.m_cdataStartCalled;
/*     */   }
/*     */ 
/*     */   public void startDocumentInternal()
/*     */     throws SAXException
/*     */   {
/* 122 */     if (this.m_needToCallStartDocument)
/*     */     {
/* 124 */       super.startDocumentInternal();
/* 125 */       this.m_needToCallStartDocument = false;
/*     */ 
/* 127 */       if (this.m_inEntityRef) {
/* 128 */         return;
/*     */       }
/* 130 */       this.m_needToOutputDocTypeDecl = true;
/* 131 */       this.m_startNewLine = false;
/*     */ 
/* 136 */       if (!getOmitXMLDeclaration())
/*     */       {
/* 138 */         String encoding = Encodings.getMimeEncoding(getEncoding());
/* 139 */         String version = getVersion();
/* 140 */         if (version == null)
/* 141 */           version = "1.0";
/*     */         String standalone;
/*     */         String standalone;
/* 144 */         if (this.m_standaloneWasSpecified)
/*     */         {
/* 146 */           standalone = " standalone=\"" + getStandalone() + "\"";
/*     */         }
/*     */         else
/*     */         {
/* 150 */           standalone = "";
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/* 155 */           Writer writer = this.m_writer;
/* 156 */           writer.write("<?xml version=\"");
/* 157 */           writer.write(version);
/* 158 */           writer.write("\" encoding=\"");
/* 159 */           writer.write(encoding);
/* 160 */           writer.write(34);
/* 161 */           writer.write(standalone);
/* 162 */           writer.write("?>");
/* 163 */           if ((this.m_doIndent) && (
/* 164 */             (this.m_standaloneWasSpecified) || (getDoctypePublic() != null) || (getDoctypeSystem() != null) || (this.m_isStandalone)))
/*     */           {
/* 177 */             writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (IOException e)
/*     */         {
/* 183 */           throw new SAXException(e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 200 */     flushPending();
/* 201 */     if ((this.m_doIndent) && (!this.m_isprevtext))
/*     */     {
/*     */       try
/*     */       {
/* 205 */         outputLineSep();
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 209 */         throw new SAXException(e);
/*     */       }
/*     */     }
/*     */ 
/* 213 */     flushWriter();
/*     */ 
/* 215 */     if (this.m_tracer != null)
/* 216 */       super.fireEndDoc();
/*     */   }
/*     */ 
/*     */   public void startPreserving()
/*     */     throws SAXException
/*     */   {
/* 235 */     this.m_preserves.push(true);
/*     */ 
/* 237 */     this.m_ispreserve = true;
/*     */   }
/*     */ 
/*     */   public void endPreserving()
/*     */     throws SAXException
/*     */   {
/* 251 */     this.m_ispreserve = (this.m_preserves.isEmpty() ? false : this.m_preserves.pop());
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 268 */     if (this.m_inEntityRef) {
/* 269 */       return;
/*     */     }
/* 271 */     flushPending();
/*     */ 
/* 273 */     if (target.equals("javax.xml.transform.disable-output-escaping"))
/*     */     {
/* 275 */       startNonEscaping();
/*     */     }
/* 277 */     else if (target.equals("javax.xml.transform.enable-output-escaping"))
/*     */     {
/* 279 */       endNonEscaping();
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 285 */         if (this.m_elemContext.m_startTagOpen)
/*     */         {
/* 287 */           closeStartTag();
/* 288 */           this.m_elemContext.m_startTagOpen = false;
/*     */         }
/* 290 */         else if (this.m_needToCallStartDocument) {
/* 291 */           startDocumentInternal();
/*     */         }
/* 293 */         if (shouldIndent()) {
/* 294 */           indent();
/*     */         }
/* 296 */         Writer writer = this.m_writer;
/* 297 */         writer.write("<?");
/* 298 */         writer.write(target);
/*     */ 
/* 300 */         if ((data.length() > 0) && (!Character.isSpaceChar(data.charAt(0))))
/*     */         {
/* 302 */           writer.write(32);
/*     */         }
/* 304 */         int indexOfQLT = data.indexOf("?>");
/*     */ 
/* 306 */         if (indexOfQLT >= 0)
/*     */         {
/* 310 */           if (indexOfQLT > 0)
/*     */           {
/* 312 */             writer.write(data.substring(0, indexOfQLT));
/*     */           }
/*     */ 
/* 315 */           writer.write("? >");
/*     */ 
/* 317 */           if (indexOfQLT + 2 < data.length())
/*     */           {
/* 319 */             writer.write(data.substring(indexOfQLT + 2));
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 324 */           writer.write(data);
/*     */         }
/*     */ 
/* 327 */         writer.write(63);
/* 328 */         writer.write(62);
/*     */ 
/* 334 */         if ((this.m_elemContext.m_currentElemDepth <= 0) && (this.m_isStandalone)) {
/* 335 */           writer.write(this.m_lineSep, 0, this.m_lineSepLen);
/*     */         }
/*     */ 
/* 347 */         this.m_startNewLine = true;
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 351 */         throw new SAXException(e);
/*     */       }
/*     */     }
/*     */ 
/* 355 */     if (this.m_tracer != null)
/* 356 */       super.fireEscapingEvent(target, data);
/*     */   }
/*     */ 
/*     */   public void entityReference(String name)
/*     */     throws SAXException
/*     */   {
/* 368 */     if (this.m_elemContext.m_startTagOpen)
/*     */     {
/* 370 */       closeStartTag();
/* 371 */       this.m_elemContext.m_startTagOpen = false;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 376 */       if (shouldIndent()) {
/* 377 */         indent();
/*     */       }
/* 379 */       Writer writer = this.m_writer;
/* 380 */       writer.write(38);
/* 381 */       writer.write(name);
/* 382 */       writer.write(59);
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 386 */       throw new SAXException(e);
/*     */     }
/*     */ 
/* 389 */     if (this.m_tracer != null)
/* 390 */       super.fireEntityReference(name);
/*     */   }
/*     */ 
/*     */   public void addUniqueAttribute(String name, String value, int flags)
/*     */     throws SAXException
/*     */   {
/* 406 */     if (this.m_elemContext.m_startTagOpen)
/*     */     {
/*     */       try
/*     */       {
/* 411 */         String patchedName = patchName(name);
/* 412 */         Writer writer = this.m_writer;
/* 413 */         if (((flags & 0x1) > 0) && (m_xmlcharInfo.onlyQuotAmpLtGt))
/*     */         {
/* 421 */           writer.write(32);
/* 422 */           writer.write(patchedName);
/* 423 */           writer.write("=\"");
/* 424 */           writer.write(value);
/* 425 */           writer.write(34);
/*     */         }
/*     */         else
/*     */         {
/* 429 */           writer.write(32);
/* 430 */           writer.write(patchedName);
/* 431 */           writer.write("=\"");
/* 432 */           writeAttrString(writer, value, getEncoding());
/* 433 */           writer.write(34);
/*     */         }
/*     */       } catch (IOException e) {
/* 436 */         throw new SAXException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addAttribute(String uri, String localName, String rawName, String type, String value, boolean xslAttribute)
/*     */     throws SAXException
/*     */   {
/* 461 */     if (this.m_elemContext.m_startTagOpen)
/*     */     {
/* 463 */       boolean was_added = addAttributeAlways(uri, localName, rawName, type, value, xslAttribute);
/*     */ 
/* 473 */       if ((was_added) && (!xslAttribute) && (!rawName.startsWith("xmlns")))
/*     */       {
/* 475 */         String prefixUsed = ensureAttributesNamespaceIsDeclared(uri, localName, rawName);
/*     */ 
/* 480 */         if ((prefixUsed != null) && (rawName != null) && (!rawName.startsWith(prefixUsed)))
/*     */         {
/* 486 */           rawName = prefixUsed + ":" + localName;
/*     */         }
/*     */       }
/*     */ 
/* 490 */       addAttributeAlways(uri, localName, rawName, type, value, xslAttribute);
/*     */     }
/*     */     else
/*     */     {
/* 508 */       String msg = Utils.messages.createMessage("ER_ILLEGAL_ATTRIBUTE_POSITION", new Object[] { localName });
/*     */       try
/*     */       {
/* 513 */         Transformer tran = super.getTransformer();
/* 514 */         ErrorListener errHandler = tran.getErrorListener();
/*     */ 
/* 518 */         if ((null != errHandler) && (this.m_sourceLocator != null))
/* 519 */           errHandler.warning(new TransformerException(msg, this.m_sourceLocator));
/*     */         else
/* 521 */           System.out.println(msg);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String elemName)
/*     */     throws SAXException
/*     */   {
/* 532 */     endElement(null, null, elemName);
/*     */   }
/*     */ 
/*     */   public void namespaceAfterStartElement(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 549 */     if (this.m_elemContext.m_elementURI == null)
/*     */     {
/* 551 */       String prefix1 = getPrefixPart(this.m_elemContext.m_elementName);
/* 552 */       if ((prefix1 == null) && ("".equals(prefix)))
/*     */       {
/* 558 */         this.m_elemContext.m_elementURI = uri;
/*     */       }
/*     */     }
/* 561 */     startPrefixMapping(prefix, uri, false);
/*     */   }
/*     */ 
/*     */   protected boolean pushNamespace(String prefix, String uri)
/*     */   {
/*     */     try
/*     */     {
/* 575 */       if (this.m_prefixMap.pushNamespace(prefix, uri, this.m_elemContext.m_currentElemDepth))
/*     */       {
/* 578 */         startPrefixMapping(prefix, uri);
/* 579 */         return true;
/*     */       }
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */     }
/*     */ 
/* 586 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean reset()
/*     */   {
/* 597 */     boolean wasReset = false;
/* 598 */     if (super.reset())
/*     */     {
/* 600 */       resetToXMLStream();
/* 601 */       wasReset = true;
/*     */     }
/* 603 */     return wasReset;
/*     */   }
/*     */ 
/*     */   private void resetToXMLStream()
/*     */   {
/* 612 */     this.m_cdataTagOpen = false;
/*     */   }
/*     */ 
/*     */   private String getXMLVersion()
/*     */   {
/* 627 */     String xmlVersion = getVersion();
/* 628 */     if ((xmlVersion == null) || (xmlVersion.equals("1.0")))
/*     */     {
/* 630 */       xmlVersion = "1.0";
/*     */     }
/* 632 */     else if (xmlVersion.equals("1.1"))
/*     */     {
/* 634 */       xmlVersion = "1.1";
/*     */     }
/*     */     else
/*     */     {
/* 638 */       String msg = Utils.messages.createMessage("ER_XML_VERSION_NOT_SUPPORTED", new Object[] { xmlVersion });
/*     */       try
/*     */       {
/* 643 */         Transformer tran = super.getTransformer();
/* 644 */         ErrorListener errHandler = tran.getErrorListener();
/*     */ 
/* 646 */         if ((null != errHandler) && (this.m_sourceLocator != null))
/* 647 */           errHandler.warning(new TransformerException(msg, this.m_sourceLocator));
/*     */         else
/* 649 */           System.out.println(msg);
/*     */       } catch (Exception e) {
/*     */       }
/* 652 */       xmlVersion = "1.0";
/*     */     }
/* 654 */     return xmlVersion;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serializer.ToXMLStream
 * JD-Core Version:    0.6.2
 */