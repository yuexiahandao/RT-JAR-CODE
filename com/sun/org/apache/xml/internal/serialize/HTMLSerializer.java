/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class HTMLSerializer extends BaseMarkupSerializer
/*     */ {
/*     */   private boolean _xhtml;
/*     */   public static final String XHTMLNamespace = "http://www.w3.org/1999/xhtml";
/* 110 */   private String fUserXHTMLNamespace = null;
/*     */ 
/*     */   protected HTMLSerializer(boolean xhtml, OutputFormat format)
/*     */   {
/* 122 */     super(format);
/* 123 */     this._xhtml = xhtml;
/*     */   }
/*     */ 
/*     */   public HTMLSerializer()
/*     */   {
/* 134 */     this(false, new OutputFormat("html", "ISO-8859-1", false));
/*     */   }
/*     */ 
/*     */   public HTMLSerializer(OutputFormat format)
/*     */   {
/* 145 */     this(false, format != null ? format : new OutputFormat("html", "ISO-8859-1", false));
/*     */   }
/*     */ 
/*     */   public HTMLSerializer(Writer writer, OutputFormat format)
/*     */   {
/* 160 */     this(false, format != null ? format : new OutputFormat("html", "ISO-8859-1", false));
/* 161 */     setOutputCharStream(writer);
/*     */   }
/*     */ 
/*     */   public HTMLSerializer(OutputStream output, OutputFormat format)
/*     */   {
/* 175 */     this(false, format != null ? format : new OutputFormat("html", "ISO-8859-1", false));
/* 176 */     setOutputByteStream(output);
/*     */   }
/*     */ 
/*     */   public void setOutputFormat(OutputFormat format)
/*     */   {
/* 182 */     super.setOutputFormat(format != null ? format : new OutputFormat("html", "ISO-8859-1", false));
/*     */   }
/*     */ 
/*     */   public void setXHTMLNamespace(String newNamespace)
/*     */   {
/* 187 */     this.fUserXHTMLNamespace = newNamespace;
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs)
/*     */     throws SAXException
/*     */   {
/* 205 */     boolean addNSAttr = false;
/*     */     try
/*     */     {
/* 208 */       if (this._printer == null) {
/* 209 */         throw new IllegalStateException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null));
/*     */       }
/*     */ 
/* 214 */       ElementState state = getElementState();
/* 215 */       if (isDocumentState())
/*     */       {
/* 220 */         if (!this._started) {
/* 221 */           startDocument((localName == null) || (localName.length() == 0) ? rawName : localName);
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 227 */         if (state.empty) {
/* 228 */           this._printer.printText('>');
/*     */         }
/*     */ 
/* 232 */         if ((this._indenting) && (!state.preserveSpace) && ((state.empty) || (state.afterElement)))
/*     */         {
/* 234 */           this._printer.breakLine();
/*     */         }
/*     */       }
/* 236 */       boolean preserveSpace = state.preserveSpace;
/*     */ 
/* 244 */       boolean hasNamespaceURI = (namespaceURI != null) && (namespaceURI.length() != 0);
/*     */ 
/* 248 */       if ((rawName == null) || (rawName.length() == 0)) {
/* 249 */         rawName = localName;
/* 250 */         if (hasNamespaceURI)
/*     */         {
/* 252 */           String prefix = getPrefix(namespaceURI);
/* 253 */           if ((prefix != null) && (prefix.length() != 0))
/* 254 */             rawName = prefix + ":" + localName;
/*     */         }
/* 256 */         addNSAttr = true;
/*     */       }
/*     */       String htmlName;
/*     */       String htmlName;
/* 258 */       if (!hasNamespaceURI) {
/* 259 */         htmlName = rawName;
/*     */       }
/*     */       else
/*     */       {
/*     */         String htmlName;
/* 261 */         if ((namespaceURI.equals("http://www.w3.org/1999/xhtml")) || ((this.fUserXHTMLNamespace != null) && (this.fUserXHTMLNamespace.equals(namespaceURI))))
/*     */         {
/* 263 */           htmlName = localName;
/*     */         }
/* 265 */         else htmlName = null;
/*     */ 
/*     */       }
/*     */ 
/* 269 */       this._printer.printText('<');
/* 270 */       if (this._xhtml)
/* 271 */         this._printer.printText(rawName.toLowerCase(Locale.ENGLISH));
/*     */       else
/* 273 */         this._printer.printText(rawName);
/* 274 */       this._printer.indent();
/*     */ 
/* 279 */       if (attrs != null) {
/* 280 */         for (int i = 0; i < attrs.getLength(); i++) {
/* 281 */           this._printer.printSpace();
/* 282 */           String name = attrs.getQName(i).toLowerCase(Locale.ENGLISH);
/* 283 */           String value = attrs.getValue(i);
/* 284 */           if ((this._xhtml) || (hasNamespaceURI))
/*     */           {
/* 286 */             if (value == null) {
/* 287 */               this._printer.printText(name);
/* 288 */               this._printer.printText("=\"\"");
/*     */             } else {
/* 290 */               this._printer.printText(name);
/* 291 */               this._printer.printText("=\"");
/* 292 */               printEscaped(value);
/* 293 */               this._printer.printText('"');
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 298 */             if (value == null) {
/* 299 */               value = "";
/*     */             }
/* 301 */             if ((!this._format.getPreserveEmptyAttributes()) && (value.length() == 0)) {
/* 302 */               this._printer.printText(name);
/* 303 */             } else if (HTMLdtd.isURI(rawName, name)) {
/* 304 */               this._printer.printText(name);
/* 305 */               this._printer.printText("=\"");
/* 306 */               this._printer.printText(escapeURI(value));
/* 307 */               this._printer.printText('"');
/* 308 */             } else if (HTMLdtd.isBoolean(rawName, name)) {
/* 309 */               this._printer.printText(name);
/*     */             } else {
/* 311 */               this._printer.printText(name);
/* 312 */               this._printer.printText("=\"");
/* 313 */               printEscaped(value);
/* 314 */               this._printer.printText('"');
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 319 */       if ((htmlName != null) && (HTMLdtd.isPreserveSpace(htmlName))) {
/* 320 */         preserveSpace = true;
/*     */       }
/* 322 */       if (addNSAttr)
/*     */       {
/* 325 */         Enumeration keys = this._prefixes.keys();
/* 326 */         while (keys.hasMoreElements()) {
/* 327 */           this._printer.printSpace();
/* 328 */           String value = (String)keys.nextElement();
/* 329 */           String name = (String)this._prefixes.get(value);
/* 330 */           if (name.length() == 0) {
/* 331 */             this._printer.printText("xmlns=\"");
/* 332 */             printEscaped(value);
/* 333 */             this._printer.printText('"');
/*     */           } else {
/* 335 */             this._printer.printText("xmlns:");
/* 336 */             this._printer.printText(name);
/* 337 */             this._printer.printText("=\"");
/* 338 */             printEscaped(value);
/* 339 */             this._printer.printText('"');
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 347 */       state = enterElementState(namespaceURI, localName, rawName, preserveSpace);
/*     */ 
/* 351 */       if ((htmlName != null) && ((htmlName.equalsIgnoreCase("A")) || (htmlName.equalsIgnoreCase("TD"))))
/*     */       {
/* 353 */         state.empty = false;
/* 354 */         this._printer.printText('>');
/*     */       }
/*     */ 
/* 360 */       if ((htmlName != null) && ((rawName.equalsIgnoreCase("SCRIPT")) || (rawName.equalsIgnoreCase("STYLE"))))
/*     */       {
/* 362 */         if (this._xhtml)
/*     */         {
/* 364 */           state.doCData = true;
/*     */         }
/*     */         else
/* 367 */           state.unescaped = true;
/*     */       }
/*     */     }
/*     */     catch (IOException except) {
/* 371 */       throw new SAXException(except);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String rawName)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 381 */       endElementIO(namespaceURI, localName, rawName);
/*     */     } catch (IOException except) {
/* 383 */       throw new SAXException(except);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElementIO(String namespaceURI, String localName, String rawName)
/*     */     throws IOException
/*     */   {
/* 398 */     this._printer.unindent();
/* 399 */     ElementState state = getElementState();
/*     */     String htmlName;
/*     */     String htmlName;
/* 401 */     if ((state.namespaceURI == null) || (state.namespaceURI.length() == 0)) {
/* 402 */       htmlName = state.rawName;
/*     */     }
/*     */     else
/*     */     {
/*     */       String htmlName;
/* 404 */       if ((state.namespaceURI.equals("http://www.w3.org/1999/xhtml")) || ((this.fUserXHTMLNamespace != null) && (this.fUserXHTMLNamespace.equals(state.namespaceURI))))
/*     */       {
/* 406 */         htmlName = state.localName;
/*     */       }
/* 408 */       else htmlName = null;
/*     */     }
/*     */ 
/* 411 */     if (this._xhtml) {
/* 412 */       if (state.empty) {
/* 413 */         this._printer.printText(" />");
/*     */       }
/*     */       else {
/* 416 */         if (state.inCData) {
/* 417 */           this._printer.printText("]]>");
/*     */         }
/* 419 */         this._printer.printText("</");
/* 420 */         this._printer.printText(state.rawName.toLowerCase(Locale.ENGLISH));
/* 421 */         this._printer.printText('>');
/*     */       }
/*     */     } else {
/* 424 */       if (state.empty) {
/* 425 */         this._printer.printText('>');
/*     */       }
/*     */ 
/* 431 */       if ((htmlName == null) || (!HTMLdtd.isOnlyOpening(htmlName))) {
/* 432 */         if ((this._indenting) && (!state.preserveSpace) && (state.afterElement)) {
/* 433 */           this._printer.breakLine();
/*     */         }
/* 435 */         if (state.inCData)
/* 436 */           this._printer.printText("]]>");
/* 437 */         this._printer.printText("</");
/* 438 */         this._printer.printText(state.rawName);
/* 439 */         this._printer.printText('>');
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 444 */     state = leaveElementState();
/*     */ 
/* 446 */     if ((htmlName == null) || ((!htmlName.equalsIgnoreCase("A")) && (!htmlName.equalsIgnoreCase("TD"))))
/*     */     {
/* 449 */       state.afterElement = true;
/* 450 */     }state.empty = false;
/* 451 */     if (isDocumentState())
/* 452 */       this._printer.flush();
/*     */   }
/*     */ 
/*     */   public void characters(char[] chars, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 468 */       ElementState state = content();
/* 469 */       state.doCData = false;
/* 470 */       super.characters(chars, start, length);
/*     */     } catch (IOException except) {
/* 472 */       throw new SAXException(except);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String tagName, AttributeList attrs)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 487 */       if (this._printer == null) {
/* 488 */         throw new IllegalStateException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "NoWriterSupplied", null));
/*     */       }
/*     */ 
/* 494 */       ElementState state = getElementState();
/* 495 */       if (isDocumentState())
/*     */       {
/* 500 */         if (!this._started) {
/* 501 */           startDocument(tagName);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 506 */         if (state.empty) {
/* 507 */           this._printer.printText('>');
/*     */         }
/*     */ 
/* 511 */         if ((this._indenting) && (!state.preserveSpace) && ((state.empty) || (state.afterElement)))
/*     */         {
/* 513 */           this._printer.breakLine();
/*     */         }
/*     */       }
/* 515 */       boolean preserveSpace = state.preserveSpace;
/*     */ 
/* 521 */       this._printer.printText('<');
/* 522 */       if (this._xhtml)
/* 523 */         this._printer.printText(tagName.toLowerCase(Locale.ENGLISH));
/*     */       else
/* 525 */         this._printer.printText(tagName);
/* 526 */       this._printer.indent();
/*     */ 
/* 531 */       if (attrs != null) {
/* 532 */         for (int i = 0; i < attrs.getLength(); i++) {
/* 533 */           this._printer.printSpace();
/* 534 */           String name = attrs.getName(i).toLowerCase(Locale.ENGLISH);
/* 535 */           String value = attrs.getValue(i);
/* 536 */           if (this._xhtml)
/*     */           {
/* 538 */             if (value == null) {
/* 539 */               this._printer.printText(name);
/* 540 */               this._printer.printText("=\"\"");
/*     */             } else {
/* 542 */               this._printer.printText(name);
/* 543 */               this._printer.printText("=\"");
/* 544 */               printEscaped(value);
/* 545 */               this._printer.printText('"');
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 550 */             if (value == null) {
/* 551 */               value = "";
/*     */             }
/* 553 */             if ((!this._format.getPreserveEmptyAttributes()) && (value.length() == 0)) {
/* 554 */               this._printer.printText(name);
/* 555 */             } else if (HTMLdtd.isURI(tagName, name)) {
/* 556 */               this._printer.printText(name);
/* 557 */               this._printer.printText("=\"");
/* 558 */               this._printer.printText(escapeURI(value));
/* 559 */               this._printer.printText('"');
/* 560 */             } else if (HTMLdtd.isBoolean(tagName, name)) {
/* 561 */               this._printer.printText(name);
/*     */             } else {
/* 563 */               this._printer.printText(name);
/* 564 */               this._printer.printText("=\"");
/* 565 */               printEscaped(value);
/* 566 */               this._printer.printText('"');
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 571 */       if (HTMLdtd.isPreserveSpace(tagName)) {
/* 572 */         preserveSpace = true;
/*     */       }
/*     */ 
/* 577 */       state = enterElementState(null, null, tagName, preserveSpace);
/*     */ 
/* 580 */       if ((tagName.equalsIgnoreCase("A")) || (tagName.equalsIgnoreCase("TD"))) {
/* 581 */         state.empty = false;
/* 582 */         this._printer.printText('>');
/*     */       }
/*     */ 
/* 588 */       if ((tagName.equalsIgnoreCase("SCRIPT")) || (tagName.equalsIgnoreCase("STYLE")))
/*     */       {
/* 590 */         if (this._xhtml)
/*     */         {
/* 592 */           state.doCData = true;
/*     */         }
/*     */         else
/* 595 */           state.unescaped = true;
/*     */       }
/*     */     }
/*     */     catch (IOException except) {
/* 599 */       throw new SAXException(except);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String tagName)
/*     */     throws SAXException
/*     */   {
/* 607 */     endElement(null, null, tagName);
/*     */   }
/*     */ 
/*     */   protected void startDocument(String rootTagName)
/*     */     throws IOException
/*     */   {
/* 635 */     this._printer.leaveDTD();
/* 636 */     if (!this._started)
/*     */     {
/* 640 */       if ((this._docTypePublicId == null) && (this._docTypeSystemId == null)) {
/* 641 */         if (this._xhtml) {
/* 642 */           this._docTypePublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
/* 643 */           this._docTypeSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
/*     */         } else {
/* 645 */           this._docTypePublicId = "-//W3C//DTD HTML 4.01//EN";
/* 646 */           this._docTypeSystemId = "http://www.w3.org/TR/html4/strict.dtd";
/*     */         }
/*     */       }
/*     */ 
/* 650 */       if (!this._format.getOmitDocumentType())
/*     */       {
/* 657 */         if ((this._docTypePublicId != null) && ((!this._xhtml) || (this._docTypeSystemId != null))) {
/* 658 */           if (this._xhtml) {
/* 659 */             this._printer.printText("<!DOCTYPE html PUBLIC ");
/*     */           }
/*     */           else {
/* 662 */             this._printer.printText("<!DOCTYPE HTML PUBLIC ");
/*     */           }
/* 664 */           printDoctypeURL(this._docTypePublicId);
/* 665 */           if (this._docTypeSystemId != null) {
/* 666 */             if (this._indenting) {
/* 667 */               this._printer.breakLine();
/* 668 */               this._printer.printText("                      ");
/*     */             } else {
/* 670 */               this._printer.printText(' ');
/* 671 */             }printDoctypeURL(this._docTypeSystemId);
/*     */           }
/* 673 */           this._printer.printText('>');
/* 674 */           this._printer.breakLine();
/* 675 */         } else if (this._docTypeSystemId != null) {
/* 676 */           if (this._xhtml) {
/* 677 */             this._printer.printText("<!DOCTYPE html SYSTEM ");
/*     */           }
/*     */           else {
/* 680 */             this._printer.printText("<!DOCTYPE HTML SYSTEM ");
/*     */           }
/* 682 */           printDoctypeURL(this._docTypeSystemId);
/* 683 */           this._printer.printText('>');
/* 684 */           this._printer.breakLine();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 689 */     this._started = true;
/*     */ 
/* 691 */     serializePreRoot();
/*     */   }
/*     */ 
/*     */   protected void serializeElement(Element elem)
/*     */     throws IOException
/*     */   {
/* 713 */     String tagName = elem.getTagName();
/* 714 */     ElementState state = getElementState();
/* 715 */     if (isDocumentState())
/*     */     {
/* 720 */       if (!this._started) {
/* 721 */         startDocument(tagName);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 726 */       if (state.empty) {
/* 727 */         this._printer.printText('>');
/*     */       }
/*     */ 
/* 731 */       if ((this._indenting) && (!state.preserveSpace) && ((state.empty) || (state.afterElement)))
/*     */       {
/* 733 */         this._printer.breakLine();
/*     */       }
/*     */     }
/* 735 */     boolean preserveSpace = state.preserveSpace;
/*     */ 
/* 741 */     this._printer.printText('<');
/* 742 */     if (this._xhtml)
/* 743 */       this._printer.printText(tagName.toLowerCase(Locale.ENGLISH));
/*     */     else
/* 745 */       this._printer.printText(tagName);
/* 746 */     this._printer.indent();
/*     */ 
/* 753 */     NamedNodeMap attrMap = elem.getAttributes();
/* 754 */     if (attrMap != null) {
/* 755 */       for (int i = 0; i < attrMap.getLength(); i++) {
/* 756 */         Attr attr = (Attr)attrMap.item(i);
/* 757 */         String name = attr.getName().toLowerCase(Locale.ENGLISH);
/* 758 */         String value = attr.getValue();
/* 759 */         if (attr.getSpecified()) {
/* 760 */           this._printer.printSpace();
/* 761 */           if (this._xhtml)
/*     */           {
/* 763 */             if (value == null) {
/* 764 */               this._printer.printText(name);
/* 765 */               this._printer.printText("=\"\"");
/*     */             } else {
/* 767 */               this._printer.printText(name);
/* 768 */               this._printer.printText("=\"");
/* 769 */               printEscaped(value);
/* 770 */               this._printer.printText('"');
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 775 */             if (value == null) {
/* 776 */               value = "";
/*     */             }
/* 778 */             if ((!this._format.getPreserveEmptyAttributes()) && (value.length() == 0)) {
/* 779 */               this._printer.printText(name);
/* 780 */             } else if (HTMLdtd.isURI(tagName, name)) {
/* 781 */               this._printer.printText(name);
/* 782 */               this._printer.printText("=\"");
/* 783 */               this._printer.printText(escapeURI(value));
/* 784 */               this._printer.printText('"');
/* 785 */             } else if (HTMLdtd.isBoolean(tagName, name)) {
/* 786 */               this._printer.printText(name);
/*     */             } else {
/* 788 */               this._printer.printText(name);
/* 789 */               this._printer.printText("=\"");
/* 790 */               printEscaped(value);
/* 791 */               this._printer.printText('"');
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 797 */     if (HTMLdtd.isPreserveSpace(tagName)) {
/* 798 */       preserveSpace = true;
/*     */     }
/*     */ 
/* 802 */     if ((elem.hasChildNodes()) || (!HTMLdtd.isEmptyTag(tagName)))
/*     */     {
/* 805 */       state = enterElementState(null, null, tagName, preserveSpace);
/*     */ 
/* 808 */       if ((tagName.equalsIgnoreCase("A")) || (tagName.equalsIgnoreCase("TD"))) {
/* 809 */         state.empty = false;
/* 810 */         this._printer.printText('>');
/*     */       }
/*     */ 
/* 816 */       if ((tagName.equalsIgnoreCase("SCRIPT")) || (tagName.equalsIgnoreCase("STYLE")))
/*     */       {
/* 818 */         if (this._xhtml)
/*     */         {
/* 820 */           state.doCData = true;
/*     */         }
/*     */         else {
/* 823 */           state.unescaped = true;
/*     */         }
/*     */       }
/* 826 */       Node child = elem.getFirstChild();
/* 827 */       while (child != null) {
/* 828 */         serializeNode(child);
/* 829 */         child = child.getNextSibling();
/*     */       }
/* 831 */       endElementIO(null, null, tagName);
/*     */     } else {
/* 833 */       this._printer.unindent();
/*     */ 
/* 836 */       if (this._xhtml)
/* 837 */         this._printer.printText(" />");
/*     */       else {
/* 839 */         this._printer.printText('>');
/*     */       }
/* 841 */       state.afterElement = true;
/* 842 */       state.empty = false;
/* 843 */       if (isDocumentState())
/* 844 */         this._printer.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void characters(String text)
/*     */     throws IOException
/*     */   {
/* 856 */     ElementState state = content();
/* 857 */     super.characters(text);
/*     */   }
/*     */ 
/*     */   protected String getEntityRef(int ch)
/*     */   {
/* 863 */     return HTMLdtd.fromChar(ch);
/*     */   }
/*     */ 
/*     */   protected String escapeURI(String uri)
/*     */   {
/* 873 */     int index = uri.indexOf("\"");
/* 874 */     if (index >= 0) {
/* 875 */       return uri.substring(0, index);
/*     */     }
/* 877 */     return uri;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.HTMLSerializer
 * JD-Core Version:    0.6.2
 */