/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*     */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager.Limit;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.XSLTC;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stax.StAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ public final class Util
/*     */ {
/*     */   public static String baseName(String name)
/*     */   {
/*  66 */     return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.baseName(name);
/*     */   }
/*     */ 
/*     */   public static String noExtName(String name) {
/*  70 */     return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.noExtName(name);
/*     */   }
/*     */ 
/*     */   public static String toJavaName(String name) {
/*  74 */     return com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util.toJavaName(name);
/*     */   }
/*     */ 
/*     */   public static InputSource getInputSource(XSLTC xsltc, Source source)
/*     */     throws TransformerConfigurationException
/*     */   {
/*  86 */     InputSource input = null;
/*     */ 
/*  88 */     String systemId = source.getSystemId();
/*     */     try
/*     */     {
/*  92 */       if ((source instanceof SAXSource)) {
/*  93 */         SAXSource sax = (SAXSource)source;
/*  94 */         input = sax.getInputSource();
/*     */         try
/*     */         {
/*  97 */           XMLReader reader = sax.getXMLReader();
/*     */ 
/* 107 */           if (reader == null) {
/*     */             try {
/* 109 */               reader = XMLReaderFactory.createXMLReader();
/*     */               try {
/* 111 */                 reader.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", xsltc.isSecureProcessing());
/*     */               }
/*     */               catch (SAXNotRecognizedException e) {
/* 114 */                 System.err.println("Warning:  " + reader.getClass().getName() + ": " + e.getMessage());
/*     */               }
/*     */ 
/*     */             }
/*     */             catch (Exception e)
/*     */             {
/*     */               try
/*     */               {
/* 122 */                 SAXParserFactory parserFactory = FactoryImpl.getSAXFactory(xsltc.useServicesMechnism());
/* 123 */                 parserFactory.setNamespaceAware(true);
/*     */ 
/* 125 */                 if (xsltc.isSecureProcessing()) {
/*     */                   try {
/* 127 */                     parserFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
/*     */                   }
/*     */                   catch (SAXException se)
/*     */                   {
/*     */                   }
/*     */                 }
/* 133 */                 reader = parserFactory.newSAXParser().getXMLReader();
/*     */               }
/*     */               catch (ParserConfigurationException pce)
/*     */               {
/* 138 */                 throw new TransformerConfigurationException("ParserConfigurationException", pce);
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 143 */           reader.setFeature("http://xml.org/sax/features/namespaces", true);
/*     */ 
/* 145 */           reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
/*     */           try
/*     */           {
/* 149 */             reader.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", xsltc.getProperty("http://javax.xml.XMLConstants/property/accessExternalDTD"));
/*     */           }
/*     */           catch (SAXNotRecognizedException e) {
/* 152 */             System.err.println("Warning:  " + reader.getClass().getName() + ": " + e.getMessage());
/*     */           }
/*     */ 
/*     */           try
/*     */           {
/* 157 */             XMLSecurityManager securityManager = (XMLSecurityManager)xsltc.getProperty("http://apache.org/xml/properties/security-manager");
/*     */ 
/* 159 */             if (securityManager != null) {
/* 160 */               for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
/* 161 */                 reader.setProperty(limit.apiProperty(), securityManager.getLimitValueAsString(limit));
/*     */               }
/*     */ 
/* 164 */               if (securityManager.printEntityCountInfo())
/* 165 */                 reader.setProperty("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo", "yes");
/*     */             }
/*     */           }
/*     */           catch (SAXException se) {
/* 169 */             System.err.println("Warning:  " + reader.getClass().getName() + ": " + se.getMessage());
/*     */           }
/*     */ 
/* 172 */           xsltc.setXMLReader(reader);
/*     */         } catch (SAXNotRecognizedException snre) {
/* 174 */           throw new TransformerConfigurationException("SAXNotRecognizedException ", snre);
/*     */         }
/*     */         catch (SAXNotSupportedException snse) {
/* 177 */           throw new TransformerConfigurationException("SAXNotSupportedException ", snse);
/*     */         }
/*     */         catch (SAXException se) {
/* 180 */           throw new TransformerConfigurationException("SAXException ", se);
/*     */         }
/*     */ 
/*     */       }
/* 186 */       else if ((source instanceof DOMSource)) {
/* 187 */         DOMSource domsrc = (DOMSource)source;
/* 188 */         Document dom = (Document)domsrc.getNode();
/* 189 */         DOM2SAX dom2sax = new DOM2SAX(dom);
/* 190 */         xsltc.setXMLReader(dom2sax);
/*     */ 
/* 193 */         input = SAXSource.sourceToInputSource(source);
/* 194 */         if (input == null) {
/* 195 */           input = new InputSource(domsrc.getSystemId());
/*     */         }
/*     */ 
/*     */       }
/* 200 */       else if ((source instanceof StAXSource)) {
/* 201 */         StAXSource staxSource = (StAXSource)source;
/* 202 */         StAXEvent2SAX staxevent2sax = null;
/* 203 */         StAXStream2SAX staxStream2SAX = null;
/* 204 */         if (staxSource.getXMLEventReader() != null) {
/* 205 */           XMLEventReader xmlEventReader = staxSource.getXMLEventReader();
/* 206 */           staxevent2sax = new StAXEvent2SAX(xmlEventReader);
/* 207 */           xsltc.setXMLReader(staxevent2sax);
/* 208 */         } else if (staxSource.getXMLStreamReader() != null) {
/* 209 */           XMLStreamReader xmlStreamReader = staxSource.getXMLStreamReader();
/* 210 */           staxStream2SAX = new StAXStream2SAX(xmlStreamReader);
/* 211 */           xsltc.setXMLReader(staxStream2SAX);
/*     */         }
/*     */ 
/* 215 */         input = SAXSource.sourceToInputSource(source);
/* 216 */         if (input == null) {
/* 217 */           input = new InputSource(staxSource.getSystemId());
/*     */         }
/*     */ 
/*     */       }
/* 222 */       else if ((source instanceof StreamSource)) {
/* 223 */         StreamSource stream = (StreamSource)source;
/* 224 */         InputStream istream = stream.getInputStream();
/* 225 */         Reader reader = stream.getReader();
/* 226 */         xsltc.setXMLReader(null);
/*     */ 
/* 229 */         if (istream != null) {
/* 230 */           input = new InputSource(istream);
/*     */         }
/* 232 */         else if (reader != null) {
/* 233 */           input = new InputSource(reader);
/*     */         }
/*     */         else
/* 236 */           input = new InputSource(systemId);
/*     */       }
/*     */       else
/*     */       {
/* 240 */         ErrorMsg err = new ErrorMsg("JAXP_UNKNOWN_SOURCE_ERR");
/* 241 */         throw new TransformerConfigurationException(err.toString());
/*     */       }
/* 243 */       input.setSystemId(systemId);
/*     */     }
/*     */     catch (NullPointerException e) {
/* 246 */       ErrorMsg err = new ErrorMsg("JAXP_NO_SOURCE_ERR", "TransformerFactory.newTemplates()");
/*     */ 
/* 248 */       throw new TransformerConfigurationException(err.toString());
/*     */     }
/*     */     catch (SecurityException e) {
/* 251 */       ErrorMsg err = new ErrorMsg("FILE_ACCESS_ERR", systemId);
/* 252 */       throw new TransformerConfigurationException(err.toString());
/*     */     }
/* 254 */     return input;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.Util
 * JD-Core Version:    0.6.2
 */