/*     */ package com.sun.xml.internal.bind.marshaller;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map.Entry;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public class XMLWriter extends XMLFilterImpl
/*     */ {
/* 398 */   private final HashMap<String, String> locallyDeclaredPrefix = new HashMap();
/*     */ 
/* 952 */   private final Attributes EMPTY_ATTS = new AttributesImpl();
/*     */ 
/* 960 */   private int elementLevel = 0;
/*     */   private Writer output;
/*     */   private String encoding;
/* 963 */   private boolean writeXmlDecl = true;
/*     */ 
/* 969 */   private String header = null;
/*     */   private final CharacterEscapeHandler escapeHandler;
/* 973 */   private boolean startTagIsClosed = true;
/*     */ 
/*     */   public XMLWriter(Writer writer, String encoding, CharacterEscapeHandler _escapeHandler)
/*     */   {
/* 279 */     init(writer, encoding);
/* 280 */     this.escapeHandler = _escapeHandler;
/*     */   }
/*     */ 
/*     */   public XMLWriter(Writer writer, String encoding) {
/* 284 */     this(writer, encoding, DumbEscapeHandler.theInstance);
/*     */   }
/*     */ 
/*     */   private void init(Writer writer, String encoding)
/*     */   {
/* 299 */     setOutput(writer, encoding);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 330 */     this.elementLevel = 0;
/* 331 */     this.startTagIsClosed = true;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 352 */     this.output.flush();
/*     */   }
/*     */ 
/*     */   public void setOutput(Writer writer, String _encoding)
/*     */   {
/* 365 */     if (writer == null)
/* 366 */       this.output = new OutputStreamWriter(System.out);
/*     */     else {
/* 368 */       this.output = writer;
/*     */     }
/* 370 */     this.encoding = _encoding;
/*     */   }
/*     */ 
/*     */   public void setXmlDecl(boolean _writeXmlDecl)
/*     */   {
/* 380 */     this.writeXmlDecl = _writeXmlDecl;
/*     */   }
/*     */ 
/*     */   public void setHeader(String _header)
/*     */   {
/* 394 */     this.header = _header;
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 400 */     this.locallyDeclaredPrefix.put(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 422 */       reset();
/*     */ 
/* 424 */       if (this.writeXmlDecl) {
/* 425 */         String e = "";
/* 426 */         if (this.encoding != null) {
/* 427 */           e = " encoding=\"" + this.encoding + '"';
/*     */         }
/* 429 */         writeXmlDecl("<?xml version=\"1.0\"" + e + " standalone=\"yes\"?>");
/*     */       }
/*     */ 
/* 432 */       if (this.header != null) {
/* 433 */         write(this.header);
/*     */       }
/* 435 */       super.startDocument();
/*     */     } catch (IOException e) {
/* 437 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void writeXmlDecl(String decl) throws IOException {
/* 442 */     write(decl);
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 460 */       super.endDocument();
/* 461 */       flush();
/*     */     } catch (IOException e) {
/* 463 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 492 */       if (!this.startTagIsClosed) {
/* 493 */         write(">");
/*     */       }
/* 495 */       this.elementLevel += 1;
/*     */ 
/* 498 */       write('<');
/* 499 */       write(qName);
/* 500 */       writeAttributes(atts);
/*     */ 
/* 503 */       if (!this.locallyDeclaredPrefix.isEmpty()) {
/* 504 */         for (Map.Entry e : this.locallyDeclaredPrefix.entrySet()) {
/* 505 */           String p = (String)e.getKey();
/* 506 */           String u = (String)e.getValue();
/* 507 */           if (u == null) {
/* 508 */             u = "";
/*     */           }
/* 510 */           write(' ');
/* 511 */           if ("".equals(p)) {
/* 512 */             write("xmlns=\"");
/*     */           } else {
/* 514 */             write("xmlns:");
/* 515 */             write(p);
/* 516 */             write("=\"");
/*     */           }
/* 518 */           char[] ch = u.toCharArray();
/* 519 */           writeEsc(ch, 0, ch.length, true);
/* 520 */           write('"');
/*     */         }
/* 522 */         this.locallyDeclaredPrefix.clear();
/*     */       }
/*     */ 
/* 529 */       super.startElement(uri, localName, qName, atts);
/* 530 */       this.startTagIsClosed = false;
/*     */     } catch (IOException e) {
/* 532 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 559 */       if (this.startTagIsClosed) {
/* 560 */         write("</");
/* 561 */         write(qName);
/* 562 */         write('>');
/*     */       } else {
/* 564 */         write("/>");
/* 565 */         this.startTagIsClosed = true;
/*     */       }
/* 567 */       super.endElement(uri, localName, qName);
/*     */ 
/* 569 */       this.elementLevel -= 1;
/*     */     } catch (IOException e) {
/* 571 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int len)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 593 */       if (!this.startTagIsClosed) {
/* 594 */         write('>');
/* 595 */         this.startTagIsClosed = true;
/*     */       }
/* 597 */       writeEsc(ch, start, len, false);
/* 598 */       super.characters(ch, start, len);
/*     */     } catch (IOException e) {
/* 600 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 622 */       writeEsc(ch, start, length, false);
/* 623 */       super.ignorableWhitespace(ch, start, length);
/*     */     } catch (IOException e) {
/* 625 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 647 */       if (!this.startTagIsClosed) {
/* 648 */         write('>');
/* 649 */         this.startTagIsClosed = true;
/*     */       }
/* 651 */       write("<?");
/* 652 */       write(target);
/* 653 */       write(' ');
/* 654 */       write(data);
/* 655 */       write("?>");
/* 656 */       if (this.elementLevel < 1) {
/* 657 */         write('\n');
/*     */       }
/* 659 */       super.processingInstruction(target, data);
/*     */     } catch (IOException e) {
/* 661 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName)
/*     */     throws SAXException
/*     */   {
/* 692 */     startElement(uri, localName, "", this.EMPTY_ATTS);
/*     */   }
/*     */ 
/*     */   public void startElement(String localName)
/*     */     throws SAXException
/*     */   {
/* 714 */     startElement("", localName, "", this.EMPTY_ATTS);
/*     */   }
/*     */ 
/*     */   public void endElement(String uri, String localName)
/*     */     throws SAXException
/*     */   {
/* 735 */     endElement(uri, localName, "");
/*     */   }
/*     */ 
/*     */   public void endElement(String localName)
/*     */     throws SAXException
/*     */   {
/* 756 */     endElement("", localName, "");
/*     */   }
/*     */ 
/*     */   public void dataElement(String uri, String localName, String qName, Attributes atts, String content)
/*     */     throws SAXException
/*     */   {
/* 790 */     startElement(uri, localName, qName, atts);
/* 791 */     characters(content);
/* 792 */     endElement(uri, localName, qName);
/*     */   }
/*     */ 
/*     */   public void dataElement(String uri, String localName, String content)
/*     */     throws SAXException
/*     */   {
/* 823 */     dataElement(uri, localName, "", this.EMPTY_ATTS, content);
/*     */   }
/*     */ 
/*     */   public void dataElement(String localName, String content)
/*     */     throws SAXException
/*     */   {
/* 854 */     dataElement("", localName, "", this.EMPTY_ATTS, content);
/*     */   }
/*     */ 
/*     */   public void characters(String data)
/*     */     throws SAXException
/*     */   {
/*     */     try
/*     */     {
/* 873 */       if (!this.startTagIsClosed) {
/* 874 */         write('>');
/* 875 */         this.startTagIsClosed = true;
/*     */       }
/* 877 */       char[] ch = data.toCharArray();
/* 878 */       characters(ch, 0, ch.length);
/*     */     } catch (IOException e) {
/* 880 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void write(char c)
/*     */     throws IOException
/*     */   {
/* 899 */     this.output.write(c);
/*     */   }
/*     */ 
/*     */   protected final void write(String s)
/*     */     throws IOException
/*     */   {
/* 907 */     this.output.write(s);
/*     */   }
/*     */ 
/*     */   private void writeAttributes(Attributes atts)
/*     */     throws IOException
/*     */   {
/* 919 */     int len = atts.getLength();
/* 920 */     for (int i = 0; i < len; i++) {
/* 921 */       char[] ch = atts.getValue(i).toCharArray();
/* 922 */       write(' ');
/* 923 */       write(atts.getQName(i));
/* 924 */       write("=\"");
/* 925 */       writeEsc(ch, 0, ch.length, true);
/* 926 */       write('"');
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeEsc(char[] ch, int start, int length, boolean isAttVal)
/*     */     throws IOException
/*     */   {
/* 943 */     this.escapeHandler.escape(ch, start, length, isAttVal, this.output);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.marshaller.XMLWriter
 * JD-Core Version:    0.6.2
 */