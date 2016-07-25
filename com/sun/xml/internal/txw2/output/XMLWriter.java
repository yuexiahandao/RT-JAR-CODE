/*      */ package com.sun.xml.internal.txw2.output;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Writer;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.ext.LexicalHandler;
/*      */ import org.xml.sax.helpers.AttributesImpl;
/*      */ import org.xml.sax.helpers.XMLFilterImpl;
/*      */ 
/*      */ public class XMLWriter extends XMLFilterImpl
/*      */   implements LexicalHandler
/*      */ {
/*  414 */   private final HashMap locallyDeclaredPrefix = new HashMap();
/*      */ 
/* 1050 */   private final Attributes EMPTY_ATTS = new AttributesImpl();
/*      */ 
/* 1058 */   private boolean inCDATA = false;
/* 1059 */   private int elementLevel = 0;
/*      */   private Writer output;
/*      */   private String encoding;
/* 1062 */   private boolean writeXmlDecl = true;
/*      */ 
/* 1068 */   private String header = null;
/*      */   private final CharacterEscapeHandler escapeHandler;
/* 1072 */   private boolean startTagIsClosed = true;
/*      */ 
/*      */   public XMLWriter(Writer writer, String encoding, CharacterEscapeHandler _escapeHandler)
/*      */   {
/*  291 */     init(writer, encoding);
/*  292 */     this.escapeHandler = _escapeHandler;
/*      */   }
/*      */ 
/*      */   public XMLWriter(Writer writer, String encoding) {
/*  296 */     this(writer, encoding, DumbEscapeHandler.theInstance);
/*      */   }
/*      */ 
/*      */   private void init(Writer writer, String encoding)
/*      */   {
/*  311 */     setOutput(writer, encoding);
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  342 */     this.elementLevel = 0;
/*  343 */     this.startTagIsClosed = true;
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */     throws IOException
/*      */   {
/*  364 */     this.output.flush();
/*      */   }
/*      */ 
/*      */   public void setOutput(Writer writer, String _encoding)
/*      */   {
/*  377 */     if (writer == null)
/*  378 */       this.output = new OutputStreamWriter(System.out);
/*      */     else {
/*  380 */       this.output = writer;
/*      */     }
/*  382 */     this.encoding = _encoding;
/*      */   }
/*      */ 
/*      */   public void setEncoding(String encoding) {
/*  386 */     this.encoding = encoding;
/*      */   }
/*      */ 
/*      */   public void setXmlDecl(boolean _writeXmlDecl)
/*      */   {
/*  396 */     this.writeXmlDecl = _writeXmlDecl;
/*      */   }
/*      */ 
/*      */   public void setHeader(String _header)
/*      */   {
/*  410 */     this.header = _header;
/*      */   }
/*      */ 
/*      */   public void startPrefixMapping(String prefix, String uri)
/*      */     throws SAXException
/*      */   {
/*  416 */     this.locallyDeclaredPrefix.put(prefix, uri);
/*      */   }
/*      */ 
/*      */   public void startDocument()
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  438 */       reset();
/*      */ 
/*  440 */       if (this.writeXmlDecl) {
/*  441 */         String e = "";
/*  442 */         if (this.encoding != null) {
/*  443 */           e = " encoding=\"" + this.encoding + "\"";
/*      */         }
/*  445 */         write("<?xml version=\"1.0\"" + e + " standalone=\"yes\"?>\n");
/*      */       }
/*      */ 
/*  448 */       if (this.header != null) {
/*  449 */         write(this.header);
/*      */       }
/*  451 */       super.startDocument();
/*      */     } catch (IOException e) {
/*  453 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endDocument()
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  472 */       if (!this.startTagIsClosed) {
/*  473 */         write("/>");
/*  474 */         this.startTagIsClosed = true;
/*      */       }
/*  476 */       write('\n');
/*  477 */       super.endDocument();
/*      */       try {
/*  479 */         flush();
/*      */       } catch (IOException e) {
/*  481 */         throw new SAXException(e);
/*      */       }
/*      */     } catch (IOException e) {
/*  484 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  513 */       if (!this.startTagIsClosed) {
/*  514 */         write(">");
/*      */       }
/*  516 */       this.elementLevel += 1;
/*      */ 
/*  519 */       write('<');
/*  520 */       writeName(uri, localName, qName, true);
/*  521 */       writeAttributes(atts);
/*      */ 
/*  524 */       if (!this.locallyDeclaredPrefix.isEmpty()) {
/*  525 */         Iterator itr = this.locallyDeclaredPrefix.entrySet().iterator();
/*  526 */         while (itr.hasNext()) {
/*  527 */           Map.Entry e = (Map.Entry)itr.next();
/*  528 */           String p = (String)e.getKey();
/*  529 */           String u = (String)e.getValue();
/*  530 */           if (u == null) {
/*  531 */             u = "";
/*      */           }
/*  533 */           write(' ');
/*  534 */           if ("".equals(p)) {
/*  535 */             write("xmlns=\"");
/*      */           } else {
/*  537 */             write("xmlns:");
/*  538 */             write(p);
/*  539 */             write("=\"");
/*      */           }
/*  541 */           char[] ch = u.toCharArray();
/*  542 */           writeEsc(ch, 0, ch.length, true);
/*  543 */           write('"');
/*      */         }
/*  545 */         this.locallyDeclaredPrefix.clear();
/*      */       }
/*      */ 
/*  552 */       super.startElement(uri, localName, qName, atts);
/*  553 */       this.startTagIsClosed = false;
/*      */     } catch (IOException e) {
/*  555 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String uri, String localName, String qName)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  582 */       if (this.startTagIsClosed) {
/*  583 */         write("</");
/*  584 */         writeName(uri, localName, qName, true);
/*  585 */         write('>');
/*      */       } else {
/*  587 */         write("/>");
/*  588 */         this.startTagIsClosed = true;
/*      */       }
/*  590 */       if (this.elementLevel == 1) {
/*  591 */         write('\n');
/*      */       }
/*  593 */       super.endElement(uri, localName, qName);
/*      */ 
/*  595 */       this.elementLevel -= 1;
/*      */     } catch (IOException e) {
/*  597 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(char[] ch, int start, int len)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  619 */       if (!this.startTagIsClosed) {
/*  620 */         write('>');
/*  621 */         this.startTagIsClosed = true;
/*      */       }
/*  623 */       if (this.inCDATA)
/*  624 */         this.output.write(ch, start, len);
/*      */       else
/*  626 */         writeEsc(ch, start, len, false);
/*  627 */       super.characters(ch, start, len);
/*      */     } catch (IOException e) {
/*  629 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] ch, int start, int length)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  651 */       writeEsc(ch, start, length, false);
/*  652 */       super.ignorableWhitespace(ch, start, length);
/*      */     } catch (IOException e) {
/*  654 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String target, String data)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  676 */       if (!this.startTagIsClosed) {
/*  677 */         write('>');
/*  678 */         this.startTagIsClosed = true;
/*      */       }
/*  680 */       write("<?");
/*  681 */       write(target);
/*  682 */       write(' ');
/*  683 */       write(data);
/*  684 */       write("?>");
/*  685 */       if (this.elementLevel < 1) {
/*  686 */         write('\n');
/*      */       }
/*  688 */       super.processingInstruction(target, data);
/*      */     } catch (IOException e) {
/*  690 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startElement(String uri, String localName)
/*      */     throws SAXException
/*      */   {
/*  721 */     startElement(uri, localName, "", this.EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void startElement(String localName)
/*      */     throws SAXException
/*      */   {
/*  743 */     startElement("", localName, "", this.EMPTY_ATTS);
/*      */   }
/*      */ 
/*      */   public void endElement(String uri, String localName)
/*      */     throws SAXException
/*      */   {
/*  764 */     endElement(uri, localName, "");
/*      */   }
/*      */ 
/*      */   public void endElement(String localName)
/*      */     throws SAXException
/*      */   {
/*  785 */     endElement("", localName, "");
/*      */   }
/*      */ 
/*      */   public void dataElement(String uri, String localName, String qName, Attributes atts, String content)
/*      */     throws SAXException
/*      */   {
/*  819 */     startElement(uri, localName, qName, atts);
/*  820 */     characters(content);
/*  821 */     endElement(uri, localName, qName);
/*      */   }
/*      */ 
/*      */   public void dataElement(String uri, String localName, String content)
/*      */     throws SAXException
/*      */   {
/*  852 */     dataElement(uri, localName, "", this.EMPTY_ATTS, content);
/*      */   }
/*      */ 
/*      */   public void dataElement(String localName, String content)
/*      */     throws SAXException
/*      */   {
/*  883 */     dataElement("", localName, "", this.EMPTY_ATTS, content);
/*      */   }
/*      */ 
/*      */   public void characters(String data)
/*      */     throws SAXException
/*      */   {
/*      */     try
/*      */     {
/*  902 */       if (!this.startTagIsClosed) {
/*  903 */         write('>');
/*  904 */         this.startTagIsClosed = true;
/*      */       }
/*  906 */       char[] ch = data.toCharArray();
/*  907 */       characters(ch, 0, ch.length);
/*      */     } catch (IOException e) {
/*  909 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void startDTD(String name, String publicId, String systemId) throws SAXException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void endDTD() throws SAXException {
/*      */   }
/*      */ 
/*      */   public void startEntity(String name) throws SAXException {
/*      */   }
/*      */ 
/*      */   public void endEntity(String name) throws SAXException {
/*      */   }
/*      */ 
/*      */   public void startCDATA() throws SAXException {
/*      */     try {
/*  928 */       if (!this.startTagIsClosed) {
/*  929 */         write('>');
/*  930 */         this.startTagIsClosed = true;
/*      */       }
/*  932 */       write("<![CDATA[");
/*  933 */       this.inCDATA = true;
/*      */     } catch (IOException e) {
/*  935 */       new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endCDATA() throws SAXException {
/*      */     try {
/*  941 */       this.inCDATA = false;
/*  942 */       write("]]>");
/*      */     } catch (IOException e) {
/*  944 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void comment(char[] ch, int start, int length) throws SAXException {
/*      */     try {
/*  950 */       this.output.write("<!--");
/*  951 */       this.output.write(ch, start, length);
/*  952 */       this.output.write("-->");
/*      */     } catch (IOException e) {
/*  954 */       throw new SAXException(e);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write(char c)
/*      */     throws IOException
/*      */   {
/*  973 */     this.output.write(c);
/*      */   }
/*      */ 
/*      */   private void write(String s)
/*      */     throws IOException
/*      */   {
/*  981 */     this.output.write(s);
/*      */   }
/*      */ 
/*      */   private void writeAttributes(Attributes atts)
/*      */     throws IOException, SAXException
/*      */   {
/*  996 */     int len = atts.getLength();
/*  997 */     for (int i = 0; i < len; i++) {
/*  998 */       char[] ch = atts.getValue(i).toCharArray();
/*  999 */       write(' ');
/* 1000 */       writeName(atts.getURI(i), atts.getLocalName(i), atts.getQName(i), false);
/*      */ 
/* 1002 */       write("=\"");
/* 1003 */       writeEsc(ch, 0, ch.length, true);
/* 1004 */       write('"');
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeEsc(char[] ch, int start, int length, boolean isAttVal)
/*      */     throws SAXException, IOException
/*      */   {
/* 1024 */     this.escapeHandler.escape(ch, start, length, isAttVal, this.output);
/*      */   }
/*      */ 
/*      */   private void writeName(String uri, String localName, String qName, boolean isElement)
/*      */     throws IOException
/*      */   {
/* 1041 */     write(qName);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.output.XMLWriter
 * JD-Core Version:    0.6.2
 */