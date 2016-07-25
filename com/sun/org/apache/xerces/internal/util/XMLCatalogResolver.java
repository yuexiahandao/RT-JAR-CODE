/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMInputImpl;
/*     */ import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*     */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
/*     */ import java.io.IOException;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ import org.w3c.dom.ls.LSResourceResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.ext.EntityResolver2;
/*     */ 
/*     */ public class XMLCatalogResolver
/*     */   implements XMLEntityResolver, EntityResolver2, LSResourceResolver
/*     */ {
/*  71 */   private CatalogManager fResolverCatalogManager = null;
/*     */ 
/*  74 */   private Catalog fCatalog = null;
/*     */ 
/*  77 */   private String[] fCatalogsList = null;
/*     */ 
/*  83 */   private boolean fCatalogsChanged = true;
/*     */ 
/*  86 */   private boolean fPreferPublic = true;
/*     */ 
/*  94 */   private boolean fUseLiteralSystemId = true;
/*     */ 
/*     */   public XMLCatalogResolver()
/*     */   {
/* 100 */     this(null, true);
/*     */   }
/*     */ 
/*     */   public XMLCatalogResolver(String[] catalogs)
/*     */   {
/* 110 */     this(catalogs, true);
/*     */   }
/*     */ 
/*     */   public XMLCatalogResolver(String[] catalogs, boolean preferPublic)
/*     */   {
/* 122 */     init(catalogs, preferPublic);
/*     */   }
/*     */ 
/*     */   public final synchronized String[] getCatalogList()
/*     */   {
/* 131 */     return this.fCatalogsList != null ? (String[])this.fCatalogsList.clone() : null;
/*     */   }
/*     */ 
/*     */   public final synchronized void setCatalogList(String[] catalogs)
/*     */   {
/* 145 */     this.fCatalogsChanged = true;
/* 146 */     this.fCatalogsList = (catalogs != null ? (String[])catalogs.clone() : null);
/*     */   }
/*     */ 
/*     */   public final synchronized void clear()
/*     */   {
/* 154 */     this.fCatalog = null;
/*     */   }
/*     */ 
/*     */   public final boolean getPreferPublic()
/*     */   {
/* 168 */     return this.fPreferPublic;
/*     */   }
/*     */ 
/*     */   public final void setPreferPublic(boolean preferPublic)
/*     */   {
/* 180 */     this.fPreferPublic = preferPublic;
/* 181 */     this.fResolverCatalogManager.setPreferPublic(preferPublic);
/*     */   }
/*     */ 
/*     */   public final boolean getUseLiteralSystemId()
/*     */   {
/* 197 */     return this.fUseLiteralSystemId;
/*     */   }
/*     */ 
/*     */   public final void setUseLiteralSystemId(boolean useLiteralSystemId)
/*     */   {
/* 221 */     this.fUseLiteralSystemId = useLiteralSystemId;
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 240 */     String resolvedId = null;
/* 241 */     if ((publicId != null) && (systemId != null)) {
/* 242 */       resolvedId = resolvePublic(publicId, systemId);
/*     */     }
/* 244 */     else if (systemId != null) {
/* 245 */       resolvedId = resolveSystem(systemId);
/*     */     }
/*     */ 
/* 248 */     if (resolvedId != null) {
/* 249 */       InputSource source = new InputSource(resolvedId);
/* 250 */       source.setPublicId(publicId);
/* 251 */       return source;
/*     */     }
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 274 */     String resolvedId = null;
/*     */ 
/* 276 */     if ((!getUseLiteralSystemId()) && (baseURI != null)) {
/*     */       try
/*     */       {
/* 279 */         URI uri = new URI(new URI(baseURI), systemId);
/* 280 */         systemId = uri.toString();
/*     */       }
/*     */       catch (URI.MalformedURIException ex)
/*     */       {
/*     */       }
/*     */     }
/* 286 */     if ((publicId != null) && (systemId != null)) {
/* 287 */       resolvedId = resolvePublic(publicId, systemId);
/*     */     }
/* 289 */     else if (systemId != null) {
/* 290 */       resolvedId = resolveSystem(systemId);
/*     */     }
/*     */ 
/* 293 */     if (resolvedId != null) {
/* 294 */       InputSource source = new InputSource(resolvedId);
/* 295 */       source.setPublicId(publicId);
/* 296 */       return source;
/*     */     }
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */   public InputSource getExternalSubset(String name, String baseURI)
/*     */     throws SAXException, IOException
/*     */   {
/* 314 */     return null;
/*     */   }
/*     */ 
/*     */   public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI)
/*     */   {
/* 336 */     String resolvedId = null;
/*     */     try
/*     */     {
/* 342 */       if (namespaceURI != null) {
/* 343 */         resolvedId = resolveURI(namespaceURI);
/*     */       }
/*     */ 
/* 346 */       if ((!getUseLiteralSystemId()) && (baseURI != null)) {
/*     */         try
/*     */         {
/* 349 */           URI uri = new URI(new URI(baseURI), systemId);
/* 350 */           systemId = uri.toString();
/*     */         }
/*     */         catch (URI.MalformedURIException ex)
/*     */         {
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 361 */       if (resolvedId == null) {
/* 362 */         if ((publicId != null) && (systemId != null)) {
/* 363 */           resolvedId = resolvePublic(publicId, systemId);
/*     */         }
/* 365 */         else if (systemId != null) {
/* 366 */           resolvedId = resolveSystem(systemId);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/*     */     }
/* 373 */     if (resolvedId != null) {
/* 374 */       return new DOMInputImpl(publicId, resolvedId, baseURI);
/*     */     }
/* 376 */     return null;
/*     */   }
/*     */ 
/*     */   public XMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier)
/*     */     throws XNIException, IOException
/*     */   {
/* 395 */     String resolvedId = resolveIdentifier(resourceIdentifier);
/* 396 */     if (resolvedId != null) {
/* 397 */       return new XMLInputSource(resourceIdentifier.getPublicId(), resolvedId, resourceIdentifier.getBaseSystemId());
/*     */     }
/*     */ 
/* 401 */     return null;
/*     */   }
/*     */ 
/*     */   public String resolveIdentifier(XMLResourceIdentifier resourceIdentifier)
/*     */     throws IOException, XNIException
/*     */   {
/* 418 */     String resolvedId = null;
/*     */ 
/* 423 */     String namespace = resourceIdentifier.getNamespace();
/* 424 */     if (namespace != null) {
/* 425 */       resolvedId = resolveURI(namespace);
/*     */     }
/*     */ 
/* 433 */     if (resolvedId == null) {
/* 434 */       String publicId = resourceIdentifier.getPublicId();
/* 435 */       String systemId = getUseLiteralSystemId() ? resourceIdentifier.getLiteralSystemId() : resourceIdentifier.getExpandedSystemId();
/*     */ 
/* 438 */       if ((publicId != null) && (systemId != null)) {
/* 439 */         resolvedId = resolvePublic(publicId, systemId);
/*     */       }
/* 441 */       else if (systemId != null) {
/* 442 */         resolvedId = resolveSystem(systemId);
/*     */       }
/*     */     }
/* 445 */     return resolvedId;
/*     */   }
/*     */ 
/*     */   public final synchronized String resolveSystem(String systemId)
/*     */     throws IOException
/*     */   {
/* 467 */     if (this.fCatalogsChanged) {
/* 468 */       parseCatalogs();
/* 469 */       this.fCatalogsChanged = false;
/*     */     }
/* 471 */     return this.fCatalog != null ? this.fCatalog.resolveSystem(systemId) : null;
/*     */   }
/*     */ 
/*     */   public final synchronized String resolvePublic(String publicId, String systemId)
/*     */     throws IOException
/*     */   {
/* 493 */     if (this.fCatalogsChanged) {
/* 494 */       parseCatalogs();
/* 495 */       this.fCatalogsChanged = false;
/*     */     }
/* 497 */     return this.fCatalog != null ? this.fCatalog.resolvePublic(publicId, systemId) : null;
/*     */   }
/*     */ 
/*     */   public final synchronized String resolveURI(String uri)
/*     */     throws IOException
/*     */   {
/* 522 */     if (this.fCatalogsChanged) {
/* 523 */       parseCatalogs();
/* 524 */       this.fCatalogsChanged = false;
/*     */     }
/* 526 */     return this.fCatalog != null ? this.fCatalog.resolveURI(uri) : null;
/*     */   }
/*     */ 
/*     */   private void init(String[] catalogs, boolean preferPublic)
/*     */   {
/* 537 */     this.fCatalogsList = (catalogs != null ? (String[])catalogs.clone() : null);
/* 538 */     this.fPreferPublic = preferPublic;
/* 539 */     this.fResolverCatalogManager = new CatalogManager();
/* 540 */     this.fResolverCatalogManager.setAllowOasisXMLCatalogPI(false);
/* 541 */     this.fResolverCatalogManager.setCatalogClassName("com.sun.org.apache.xml.internal.resolver.Catalog");
/* 542 */     this.fResolverCatalogManager.setCatalogFiles("");
/* 543 */     this.fResolverCatalogManager.setIgnoreMissingProperties(true);
/* 544 */     this.fResolverCatalogManager.setPreferPublic(this.fPreferPublic);
/* 545 */     this.fResolverCatalogManager.setRelativeCatalogs(false);
/* 546 */     this.fResolverCatalogManager.setUseStaticCatalog(false);
/* 547 */     this.fResolverCatalogManager.setVerbosity(0);
/*     */   }
/*     */ 
/*     */   private void parseCatalogs()
/*     */     throws IOException
/*     */   {
/* 557 */     if (this.fCatalogsList != null) {
/* 558 */       this.fCatalog = new Catalog(this.fResolverCatalogManager);
/* 559 */       attachReaderToCatalog(this.fCatalog);
/* 560 */       for (int i = 0; i < this.fCatalogsList.length; i++) {
/* 561 */         String catalog = this.fCatalogsList[i];
/* 562 */         if ((catalog != null) && (catalog.length() > 0))
/* 563 */           this.fCatalog.parseCatalog(catalog);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 568 */       this.fCatalog = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void attachReaderToCatalog(Catalog catalog)
/*     */   {
/* 577 */     SAXParserFactory spf = new SAXParserFactoryImpl();
/* 578 */     spf.setNamespaceAware(true);
/* 579 */     spf.setValidating(false);
/*     */ 
/* 581 */     SAXCatalogReader saxReader = new SAXCatalogReader(spf);
/* 582 */     saxReader.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader");
/*     */ 
/* 584 */     catalog.addReader("application/xml", saxReader);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLCatalogResolver
 * JD-Core Version:    0.6.2
 */