/*     */ package com.sun.org.apache.xml.internal.resolver.tools;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Locale;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.DocumentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class ResolvingParser
/*     */   implements Parser, DTDHandler, DocumentHandler, EntityResolver
/*     */ {
/*  71 */   public static boolean namespaceAware = true;
/*     */ 
/*  74 */   public static boolean validating = false;
/*     */ 
/*  80 */   public static boolean suppressExplanation = false;
/*     */ 
/*  83 */   private SAXParser saxParser = null;
/*     */ 
/*  86 */   private Parser parser = null;
/*     */ 
/*  89 */   private DocumentHandler documentHandler = null;
/*     */ 
/*  92 */   private DTDHandler dtdHandler = null;
/*     */ 
/*  95 */   private CatalogManager catalogManager = CatalogManager.getStaticManager();
/*     */ 
/*  98 */   private CatalogResolver catalogResolver = null;
/*     */ 
/* 101 */   private CatalogResolver piCatalogResolver = null;
/*     */ 
/* 104 */   private boolean allowXMLCatalogPI = false;
/*     */ 
/* 107 */   private boolean oasisXMLCatalogPI = false;
/*     */ 
/* 110 */   private URL baseURL = null;
/*     */ 
/*     */   public ResolvingParser()
/*     */   {
/* 114 */     initParser();
/*     */   }
/*     */ 
/*     */   public ResolvingParser(CatalogManager manager)
/*     */   {
/* 119 */     this.catalogManager = manager;
/* 120 */     initParser();
/*     */   }
/*     */ 
/*     */   private void initParser()
/*     */   {
/* 125 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/* 126 */     SAXParserFactory spf = this.catalogManager.useServicesMechanism() ? SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
/*     */ 
/* 128 */     spf.setNamespaceAware(namespaceAware);
/* 129 */     spf.setValidating(validating);
/*     */     try
/*     */     {
/* 132 */       this.saxParser = spf.newSAXParser();
/* 133 */       this.parser = this.saxParser.getParser();
/* 134 */       this.documentHandler = null;
/* 135 */       this.dtdHandler = null;
/*     */     } catch (Exception ex) {
/* 137 */       ex.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Catalog getCatalog()
/*     */   {
/* 143 */     return this.catalogResolver.getCatalog();
/*     */   }
/*     */ 
/*     */   public void parse(InputSource input)
/*     */     throws IOException, SAXException
/*     */   {
/* 170 */     setupParse(input.getSystemId());
/*     */     try {
/* 172 */       this.parser.parse(input);
/*     */     } catch (InternalError ie) {
/* 174 */       explain(input.getSystemId());
/* 175 */       throw ie;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void parse(String systemId)
/*     */     throws IOException, SAXException
/*     */   {
/* 186 */     setupParse(systemId);
/*     */     try {
/* 188 */       this.parser.parse(systemId);
/*     */     } catch (InternalError ie) {
/* 190 */       explain(systemId);
/* 191 */       throw ie;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDocumentHandler(DocumentHandler handler)
/*     */   {
/* 197 */     this.documentHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setDTDHandler(DTDHandler handler)
/*     */   {
/* 202 */     this.dtdHandler = handler;
/*     */   }
/*     */ 
/*     */   public void setEntityResolver(EntityResolver resolver)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler handler)
/*     */   {
/* 217 */     this.parser.setErrorHandler(handler);
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale) throws SAXException
/*     */   {
/* 222 */     this.parser.setLocale(locale);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 228 */     if (this.documentHandler != null)
/* 229 */       this.documentHandler.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 235 */     if (this.documentHandler != null)
/* 236 */       this.documentHandler.endDocument();
/*     */   }
/*     */ 
/*     */   public void endElement(String name)
/*     */     throws SAXException
/*     */   {
/* 242 */     if (this.documentHandler != null)
/* 243 */       this.documentHandler.endElement(name);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 250 */     if (this.documentHandler != null)
/* 251 */       this.documentHandler.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String pidata)
/*     */     throws SAXException
/*     */   {
/* 259 */     if (target.equals("oasis-xml-catalog")) {
/* 260 */       URL catalog = null;
/* 261 */       String data = pidata;
/*     */ 
/* 263 */       int pos = data.indexOf("catalog=");
/* 264 */       if (pos >= 0) {
/* 265 */         data = data.substring(pos + 8);
/* 266 */         if (data.length() > 1) {
/* 267 */           String quote = data.substring(0, 1);
/* 268 */           data = data.substring(1);
/* 269 */           pos = data.indexOf(quote);
/* 270 */           if (pos >= 0) {
/* 271 */             data = data.substring(0, pos);
/*     */             try {
/* 273 */               if (this.baseURL != null)
/* 274 */                 catalog = new URL(this.baseURL, data);
/*     */               else {
/* 276 */                 catalog = new URL(data);
/*     */               }
/*     */             }
/*     */             catch (MalformedURLException mue)
/*     */             {
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 285 */       if (this.allowXMLCatalogPI) {
/* 286 */         if (this.catalogManager.getAllowOasisXMLCatalogPI()) {
/* 287 */           this.catalogManager.debug.message(4, "oasis-xml-catalog PI", pidata);
/*     */ 
/* 289 */           if (catalog != null) {
/*     */             try {
/* 291 */               this.catalogManager.debug.message(4, "oasis-xml-catalog", catalog.toString());
/* 292 */               this.oasisXMLCatalogPI = true;
/*     */ 
/* 294 */               if (this.piCatalogResolver == null) {
/* 295 */                 this.piCatalogResolver = new CatalogResolver(true);
/*     */               }
/*     */ 
/* 298 */               this.piCatalogResolver.getCatalog().parseCatalog(catalog.toString());
/*     */             } catch (Exception e) {
/* 300 */               this.catalogManager.debug.message(3, "Exception parsing oasis-xml-catalog: " + catalog.toString());
/*     */             }
/*     */           }
/*     */           else
/* 304 */             this.catalogManager.debug.message(3, "PI oasis-xml-catalog unparseable: " + pidata);
/*     */         }
/*     */         else {
/* 307 */           this.catalogManager.debug.message(4, "PI oasis-xml-catalog ignored: " + pidata);
/*     */         }
/*     */       }
/* 310 */       else this.catalogManager.debug.message(3, "PI oasis-xml-catalog occurred in an invalid place: " + pidata);
/*     */ 
/*     */     }
/* 314 */     else if (this.documentHandler != null) {
/* 315 */       this.documentHandler.processingInstruction(target, pidata);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 322 */     if (this.documentHandler != null)
/* 323 */       this.documentHandler.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 329 */     if (this.documentHandler != null)
/* 330 */       this.documentHandler.startDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String name, AttributeList atts)
/*     */     throws SAXException
/*     */   {
/* 337 */     this.allowXMLCatalogPI = false;
/* 338 */     if (this.documentHandler != null)
/* 339 */       this.documentHandler.startElement(name, atts);
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 346 */     this.allowXMLCatalogPI = false;
/* 347 */     if (this.dtdHandler != null)
/* 348 */       this.dtdHandler.notationDecl(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*     */     throws SAXException
/*     */   {
/* 358 */     this.allowXMLCatalogPI = false;
/* 359 */     if (this.dtdHandler != null)
/* 360 */       this.dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */   {
/* 370 */     this.allowXMLCatalogPI = false;
/* 371 */     String resolved = this.catalogResolver.getResolvedEntity(publicId, systemId);
/*     */ 
/* 373 */     if ((resolved == null) && (this.piCatalogResolver != null)) {
/* 374 */       resolved = this.piCatalogResolver.getResolvedEntity(publicId, systemId);
/*     */     }
/*     */ 
/* 377 */     if (resolved != null) {
/*     */       try {
/* 379 */         InputSource iSource = new InputSource(resolved);
/* 380 */         iSource.setPublicId(publicId);
/*     */ 
/* 393 */         URL url = new URL(resolved);
/* 394 */         InputStream iStream = url.openStream();
/* 395 */         iSource.setByteStream(iStream);
/*     */ 
/* 397 */         return iSource;
/*     */       } catch (Exception e) {
/* 399 */         this.catalogManager.debug.message(1, "Failed to create InputSource", resolved);
/* 400 */         return null;
/*     */       }
/*     */     }
/* 403 */     return null;
/*     */   }
/*     */ 
/*     */   private void setupParse(String systemId)
/*     */   {
/* 409 */     this.allowXMLCatalogPI = true;
/* 410 */     this.parser.setEntityResolver(this);
/* 411 */     this.parser.setDocumentHandler(this);
/* 412 */     this.parser.setDTDHandler(this);
/*     */ 
/* 414 */     URL cwd = null;
/*     */     try
/*     */     {
/* 417 */       cwd = FileURL.makeURL("basename");
/*     */     } catch (MalformedURLException mue) {
/* 419 */       cwd = null;
/*     */     }
/*     */     try
/*     */     {
/* 423 */       this.baseURL = new URL(systemId);
/*     */     } catch (MalformedURLException mue) {
/* 425 */       if (cwd != null) {
/*     */         try {
/* 427 */           this.baseURL = new URL(cwd, systemId);
/*     */         }
/*     */         catch (MalformedURLException mue2) {
/* 430 */           this.baseURL = null;
/*     */         }
/*     */       }
/*     */       else
/* 434 */         this.baseURL = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void explain(String systemId)
/*     */   {
/* 441 */     if (!suppressExplanation) {
/* 442 */       System.out.println("Parser probably encountered bad URI in " + systemId);
/* 443 */       System.out.println("For example, replace '/some/uri' with 'file:/some/uri'.");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.tools.ResolvingParser
 * JD-Core Version:    0.6.2
 */