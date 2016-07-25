/*     */ package com.sun.xml.internal.messaging.saaj.util;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.ext.LexicalHandler;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public class RejectDoctypeSaxFilter extends XMLFilterImpl
/*     */   implements XMLReader, LexicalHandler
/*     */ {
/*  52 */   protected static final Logger log = Logger.getLogger("com.sun.xml.internal.messaging.saaj.util", "com.sun.xml.internal.messaging.saaj.util.LocalStrings");
/*     */   static final String LEXICAL_HANDLER_PROP = "http://xml.org/sax/properties/lexical-handler";
/*  60 */   static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd".intern();
/*  61 */   static final String SIGNATURE_LNAME = "Signature".intern();
/*  62 */   static final String ENCRYPTED_DATA_LNAME = "EncryptedData".intern();
/*  63 */   static final String DSIG_NS = "http://www.w3.org/2000/09/xmldsig#".intern();
/*  64 */   static final String XENC_NS = "http://www.w3.org/2001/04/xmlenc#".intern();
/*  65 */   static final String ID_NAME = "ID".intern();
/*     */   private LexicalHandler lexicalHandler;
/*     */ 
/*     */   public RejectDoctypeSaxFilter(SAXParser saxParser)
/*     */     throws SOAPException
/*     */   {
/*     */     XMLReader xmlReader;
/*     */     try
/*     */     {
/*  73 */       xmlReader = saxParser.getXMLReader();
/*     */     } catch (Exception e) {
/*  75 */       log.severe("SAAJ0602.util.getXMLReader.exception");
/*  76 */       throw new SOAPExceptionImpl("Couldn't get an XMLReader while constructing a RejectDoctypeSaxFilter", e);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  83 */       xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
/*     */     } catch (Exception e) {
/*  85 */       log.severe("SAAJ0603.util.setProperty.exception");
/*  86 */       throw new SOAPExceptionImpl("Couldn't set the lexical handler property while constructing a RejectDoctypeSaxFilter", e);
/*     */     }
/*     */ 
/*  92 */     setParent(xmlReader);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 101 */     if ("http://xml.org/sax/properties/lexical-handler".equals(name))
/* 102 */       this.lexicalHandler = ((LexicalHandler)value);
/*     */     else
/* 104 */       super.setProperty(name, value);
/*     */   }
/*     */ 
/*     */   public void startDTD(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 114 */     throw new SAXException("Document Type Declaration is not allowed");
/*     */   }
/*     */ 
/*     */   public void endDTD() throws SAXException {
/*     */   }
/*     */ 
/*     */   public void startEntity(String name) throws SAXException {
/* 121 */     if (this.lexicalHandler != null)
/* 122 */       this.lexicalHandler.startEntity(name);
/*     */   }
/*     */ 
/*     */   public void endEntity(String name) throws SAXException
/*     */   {
/* 127 */     if (this.lexicalHandler != null)
/* 128 */       this.lexicalHandler.endEntity(name);
/*     */   }
/*     */ 
/*     */   public void startCDATA() throws SAXException
/*     */   {
/* 133 */     if (this.lexicalHandler != null)
/* 134 */       this.lexicalHandler.startCDATA();
/*     */   }
/*     */ 
/*     */   public void endCDATA() throws SAXException
/*     */   {
/* 139 */     if (this.lexicalHandler != null)
/* 140 */       this.lexicalHandler.endCDATA();
/*     */   }
/*     */ 
/*     */   public void comment(char[] ch, int start, int length) throws SAXException
/*     */   {
/* 145 */     if (this.lexicalHandler != null)
/* 146 */       this.lexicalHandler.comment(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 156 */     if (atts != null) {
/* 157 */       boolean eos = false;
/* 158 */       if ((namespaceURI == DSIG_NS) || (XENC_NS == namespaceURI)) {
/* 159 */         eos = true;
/*     */       }
/* 161 */       int length = atts.getLength();
/* 162 */       AttributesImpl attrImpl = new AttributesImpl();
/* 163 */       for (int i = 0; i < length; i++) {
/* 164 */         String name = atts.getLocalName(i);
/* 165 */         if ((name != null) && (name.equals("Id"))) {
/* 166 */           if ((eos) || (atts.getURI(i) == WSU_NS)) {
/* 167 */             attrImpl.addAttribute(atts.getURI(i), atts.getLocalName(i), atts.getQName(i), ID_NAME, atts.getValue(i));
/*     */           }
/*     */           else
/* 170 */             attrImpl.addAttribute(atts.getURI(i), atts.getLocalName(i), atts.getQName(i), atts.getType(i), atts.getValue(i));
/*     */         }
/*     */         else {
/* 173 */           attrImpl.addAttribute(atts.getURI(i), atts.getLocalName(i), atts.getQName(i), atts.getType(i), atts.getValue(i));
/*     */         }
/*     */       }
/*     */ 
/* 177 */       super.startElement(namespaceURI, localName, qName, attrImpl);
/*     */     } else {
/* 179 */       super.startElement(namespaceURI, localName, qName, atts);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.util.RejectDoctypeSaxFilter
 * JD-Core Version:    0.6.2
 */