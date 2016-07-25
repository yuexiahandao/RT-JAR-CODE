/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Hashtable;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DocumentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class SAXCatalogReader
/*     */   implements CatalogReader, ContentHandler, DocumentHandler
/*     */ {
/*  81 */   protected SAXParserFactory parserFactory = null;
/*     */ 
/*  84 */   protected String parserClass = null;
/*     */ 
/*  93 */   protected Hashtable namespaceMap = new Hashtable();
/*     */ 
/*  96 */   private SAXCatalogParser saxParser = null;
/*     */ 
/* 101 */   private boolean abandonHope = false;
/*     */   private Catalog catalog;
/* 137 */   protected Debug debug = CatalogManager.getStaticManager().debug;
/*     */ 
/*     */   public void setParserFactory(SAXParserFactory parserFactory)
/*     */   {
/* 109 */     this.parserFactory = parserFactory;
/*     */   }
/*     */ 
/*     */   public void setParserClass(String parserClass)
/*     */   {
/* 115 */     this.parserClass = parserClass;
/*     */   }
/*     */ 
/*     */   public SAXParserFactory getParserFactory()
/*     */   {
/* 120 */     return this.parserFactory;
/*     */   }
/*     */ 
/*     */   public String getParserClass()
/*     */   {
/* 125 */     return this.parserClass;
/*     */   }
/*     */ 
/*     */   public SAXCatalogReader()
/*     */   {
/* 141 */     this.parserFactory = null;
/* 142 */     this.parserClass = null;
/*     */   }
/*     */ 
/*     */   public SAXCatalogReader(SAXParserFactory parserFactory)
/*     */   {
/* 147 */     this.parserFactory = parserFactory;
/*     */   }
/*     */ 
/*     */   public SAXCatalogReader(String parserClass)
/*     */   {
/* 152 */     this.parserClass = parserClass;
/*     */   }
/*     */ 
/*     */   public void setCatalogParser(String namespaceURI, String rootElement, String parserClass)
/*     */   {
/* 161 */     if (namespaceURI == null)
/* 162 */       this.namespaceMap.put(rootElement, parserClass);
/*     */     else
/* 164 */       this.namespaceMap.put("{" + namespaceURI + "}" + rootElement, parserClass);
/*     */   }
/*     */ 
/*     */   public String getCatalogParser(String namespaceURI, String rootElement)
/*     */   {
/* 173 */     if (namespaceURI == null) {
/* 174 */       return (String)this.namespaceMap.get(rootElement);
/*     */     }
/* 176 */     return (String)this.namespaceMap.get("{" + namespaceURI + "}" + rootElement);
/*     */   }
/*     */ 
/*     */   public void readCatalog(Catalog catalog, String fileUrl)
/*     */     throws MalformedURLException, IOException, CatalogException
/*     */   {
/* 193 */     URL url = null;
/*     */     try
/*     */     {
/* 196 */       url = new URL(fileUrl);
/*     */     } catch (MalformedURLException e) {
/* 198 */       url = new URL("file:///" + fileUrl);
/*     */     }
/*     */ 
/* 201 */     this.debug = catalog.getCatalogManager().debug;
/*     */     try
/*     */     {
/* 204 */       URLConnection urlCon = url.openConnection();
/* 205 */       readCatalog(catalog, urlCon.getInputStream());
/*     */     } catch (FileNotFoundException e) {
/* 207 */       catalog.getCatalogManager().debug.message(1, "Failed to load catalog, file not found", url.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readCatalog(Catalog catalog, InputStream is)
/*     */     throws IOException, CatalogException
/*     */   {
/* 226 */     if ((this.parserFactory == null) && (this.parserClass == null)) {
/* 227 */       this.debug.message(1, "Cannot read SAX catalog without a parser");
/* 228 */       throw new CatalogException(6);
/*     */     }
/*     */ 
/* 231 */     this.debug = catalog.getCatalogManager().debug;
/* 232 */     EntityResolver bResolver = catalog.getCatalogManager().getBootstrapResolver();
/*     */ 
/* 234 */     this.catalog = catalog;
/*     */     try
/*     */     {
/* 237 */       if (this.parserFactory != null) {
/* 238 */         SAXParser parser = this.parserFactory.newSAXParser();
/* 239 */         SAXParserHandler spHandler = new SAXParserHandler();
/* 240 */         spHandler.setContentHandler(this);
/* 241 */         if (bResolver != null) {
/* 242 */           spHandler.setEntityResolver(bResolver);
/*     */         }
/* 244 */         parser.parse(new InputSource(is), spHandler);
/*     */       } else {
/* 246 */         Parser parser = (Parser)ReflectUtil.forName(this.parserClass).newInstance();
/* 247 */         parser.setDocumentHandler(this);
/* 248 */         if (bResolver != null) {
/* 249 */           parser.setEntityResolver(bResolver);
/*     */         }
/* 251 */         parser.parse(new InputSource(is));
/*     */       }
/*     */     } catch (ClassNotFoundException cnfe) {
/* 254 */       throw new CatalogException(6);
/*     */     } catch (IllegalAccessException iae) {
/* 256 */       throw new CatalogException(6);
/*     */     } catch (InstantiationException ie) {
/* 258 */       throw new CatalogException(6);
/*     */     } catch (ParserConfigurationException pce) {
/* 260 */       throw new CatalogException(5);
/*     */     } catch (SAXException se) {
/* 262 */       Exception e = se.getException();
/*     */ 
/* 264 */       UnknownHostException uhe = new UnknownHostException();
/* 265 */       FileNotFoundException fnfe = new FileNotFoundException();
/* 266 */       if (e != null) {
/* 267 */         if (e.getClass() == uhe.getClass()) {
/* 268 */           throw new CatalogException(7, e.toString());
/*     */         }
/* 270 */         if (e.getClass() == fnfe.getClass()) {
/* 271 */           throw new CatalogException(7, e.toString());
/*     */         }
/*     */       }
/*     */ 
/* 275 */       throw new CatalogException(se);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/* 284 */     if (this.saxParser != null)
/* 285 */       this.saxParser.setDocumentLocator(locator);
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 291 */     this.saxParser = null;
/* 292 */     this.abandonHope = false;
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 298 */     if (this.saxParser != null)
/* 299 */       this.saxParser.endDocument();
/*     */   }
/*     */ 
/*     */   public void startElement(String name, AttributeList atts)
/*     */     throws SAXException
/*     */   {
/* 313 */     if (this.abandonHope) {
/* 314 */       return;
/*     */     }
/*     */ 
/* 317 */     if (this.saxParser == null) {
/* 318 */       String prefix = "";
/* 319 */       if (name.indexOf(':') > 0) {
/* 320 */         prefix = name.substring(0, name.indexOf(':'));
/*     */       }
/*     */ 
/* 323 */       String localName = name;
/* 324 */       if (localName.indexOf(':') > 0) {
/* 325 */         localName = localName.substring(localName.indexOf(':') + 1);
/*     */       }
/*     */ 
/* 328 */       String namespaceURI = null;
/* 329 */       if (prefix.equals(""))
/* 330 */         namespaceURI = atts.getValue("xmlns");
/*     */       else {
/* 332 */         namespaceURI = atts.getValue("xmlns:" + prefix);
/*     */       }
/*     */ 
/* 335 */       String saxParserClass = getCatalogParser(namespaceURI, localName);
/*     */ 
/* 338 */       if (saxParserClass == null) {
/* 339 */         this.abandonHope = true;
/* 340 */         if (namespaceURI == null)
/* 341 */           this.debug.message(2, "No Catalog parser for " + name);
/*     */         else {
/* 343 */           this.debug.message(2, "No Catalog parser for {" + namespaceURI + "}" + name);
/*     */         }
/*     */ 
/* 347 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 351 */         this.saxParser = ((SAXCatalogParser)ReflectUtil.forName(saxParserClass).newInstance());
/*     */ 
/* 354 */         this.saxParser.setCatalog(this.catalog);
/* 355 */         this.saxParser.startDocument();
/* 356 */         this.saxParser.startElement(name, atts);
/*     */       } catch (ClassNotFoundException cnfe) {
/* 358 */         this.saxParser = null;
/* 359 */         this.abandonHope = true;
/* 360 */         this.debug.message(2, cnfe.toString());
/*     */       } catch (InstantiationException ie) {
/* 362 */         this.saxParser = null;
/* 363 */         this.abandonHope = true;
/* 364 */         this.debug.message(2, ie.toString());
/*     */       } catch (IllegalAccessException iae) {
/* 366 */         this.saxParser = null;
/* 367 */         this.abandonHope = true;
/* 368 */         this.debug.message(2, iae.toString());
/*     */       } catch (ClassCastException cce) {
/* 370 */         this.saxParser = null;
/* 371 */         this.abandonHope = true;
/* 372 */         this.debug.message(2, cce.toString());
/*     */       }
/*     */     } else {
/* 375 */       this.saxParser.startElement(name, atts);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 391 */     if (this.abandonHope) {
/* 392 */       return;
/*     */     }
/*     */ 
/* 395 */     if (this.saxParser == null) {
/* 396 */       String saxParserClass = getCatalogParser(namespaceURI, localName);
/*     */ 
/* 399 */       if (saxParserClass == null) {
/* 400 */         this.abandonHope = true;
/* 401 */         if (namespaceURI == null)
/* 402 */           this.debug.message(2, "No Catalog parser for " + localName);
/*     */         else {
/* 404 */           this.debug.message(2, "No Catalog parser for {" + namespaceURI + "}" + localName);
/*     */         }
/*     */ 
/* 408 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 412 */         this.saxParser = ((SAXCatalogParser)ReflectUtil.forName(saxParserClass).newInstance());
/*     */ 
/* 415 */         this.saxParser.setCatalog(this.catalog);
/* 416 */         this.saxParser.startDocument();
/* 417 */         this.saxParser.startElement(namespaceURI, localName, qName, atts);
/*     */       } catch (ClassNotFoundException cnfe) {
/* 419 */         this.saxParser = null;
/* 420 */         this.abandonHope = true;
/* 421 */         this.debug.message(2, cnfe.toString());
/*     */       } catch (InstantiationException ie) {
/* 423 */         this.saxParser = null;
/* 424 */         this.abandonHope = true;
/* 425 */         this.debug.message(2, ie.toString());
/*     */       } catch (IllegalAccessException iae) {
/* 427 */         this.saxParser = null;
/* 428 */         this.abandonHope = true;
/* 429 */         this.debug.message(2, iae.toString());
/*     */       } catch (ClassCastException cce) {
/* 431 */         this.saxParser = null;
/* 432 */         this.abandonHope = true;
/* 433 */         this.debug.message(2, cce.toString());
/*     */       }
/*     */     } else {
/* 436 */       this.saxParser.startElement(namespaceURI, localName, qName, atts);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String name) throws SAXException
/*     */   {
/* 442 */     if (this.saxParser != null)
/* 443 */       this.saxParser.endElement(name);
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 451 */     if (this.saxParser != null)
/* 452 */       this.saxParser.endElement(namespaceURI, localName, qName);
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 459 */     if (this.saxParser != null)
/* 460 */       this.saxParser.characters(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/* 467 */     if (this.saxParser != null)
/* 468 */       this.saxParser.ignorableWhitespace(ch, start, length);
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/* 475 */     if (this.saxParser != null)
/* 476 */       this.saxParser.processingInstruction(target, data);
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/* 483 */     if (this.saxParser != null)
/* 484 */       this.saxParser.startPrefixMapping(prefix, uri);
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/* 491 */     if (this.saxParser != null)
/* 492 */       this.saxParser.endPrefixMapping(prefix);
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {
/* 499 */     if (this.saxParser != null)
/* 500 */       this.saxParser.skippedEntity(name);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader
 * JD-Core Version:    0.6.2
 */