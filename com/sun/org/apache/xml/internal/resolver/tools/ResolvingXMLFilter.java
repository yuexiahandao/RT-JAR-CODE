/*     */ package com.sun.org.apache.xml.internal.resolver.tools;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLFilterImpl;
/*     */ 
/*     */ public class ResolvingXMLFilter extends XMLFilterImpl
/*     */ {
/*  64 */   public static boolean suppressExplanation = false;
/*     */ 
/*  67 */   CatalogManager catalogManager = CatalogManager.getStaticManager();
/*     */ 
/*  70 */   private CatalogResolver catalogResolver = null;
/*     */ 
/*  73 */   private CatalogResolver piCatalogResolver = null;
/*     */ 
/*  76 */   private boolean allowXMLCatalogPI = false;
/*     */ 
/*  79 */   private boolean oasisXMLCatalogPI = false;
/*     */ 
/*  82 */   private URL baseURL = null;
/*     */ 
/*     */   public ResolvingXMLFilter()
/*     */   {
/*  87 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */   }
/*     */ 
/*     */   public ResolvingXMLFilter(XMLReader parent)
/*     */   {
/*  92 */     super(parent);
/*  93 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */   }
/*     */ 
/*     */   public ResolvingXMLFilter(CatalogManager manager)
/*     */   {
/*  99 */     this.catalogManager = manager;
/* 100 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */   }
/*     */ 
/*     */   public ResolvingXMLFilter(XMLReader parent, CatalogManager manager)
/*     */   {
/* 105 */     super(parent);
/* 106 */     this.catalogManager = manager;
/* 107 */     this.catalogResolver = new CatalogResolver(this.catalogManager);
/*     */   }
/*     */ 
/*     */   public Catalog getCatalog()
/*     */   {
/* 114 */     return this.catalogResolver.getCatalog();
/*     */   }
/*     */ 
/*     */   public void parse(InputSource input)
/*     */     throws IOException, SAXException
/*     */   {
/* 140 */     this.allowXMLCatalogPI = true;
/*     */ 
/* 142 */     setupBaseURI(input.getSystemId());
/*     */     try
/*     */     {
/* 145 */       super.parse(input);
/*     */     } catch (InternalError ie) {
/* 147 */       explain(input.getSystemId());
/* 148 */       throw ie;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void parse(String systemId)
/*     */     throws IOException, SAXException
/*     */   {
/* 158 */     this.allowXMLCatalogPI = true;
/*     */ 
/* 160 */     setupBaseURI(systemId);
/*     */     try
/*     */     {
/* 163 */       super.parse(systemId);
/*     */     } catch (InternalError ie) {
/* 165 */       explain(systemId);
/* 166 */       throw ie;
/*     */     }
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */   {
/* 176 */     this.allowXMLCatalogPI = false;
/* 177 */     String resolved = this.catalogResolver.getResolvedEntity(publicId, systemId);
/*     */ 
/* 179 */     if ((resolved == null) && (this.piCatalogResolver != null)) {
/* 180 */       resolved = this.piCatalogResolver.getResolvedEntity(publicId, systemId);
/*     */     }
/*     */ 
/* 183 */     if (resolved != null) {
/*     */       try {
/* 185 */         InputSource iSource = new InputSource(resolved);
/* 186 */         iSource.setPublicId(publicId);
/*     */ 
/* 199 */         URL url = new URL(resolved);
/* 200 */         InputStream iStream = url.openStream();
/* 201 */         iSource.setByteStream(iStream);
/*     */ 
/* 203 */         return iSource;
/*     */       } catch (Exception e) {
/* 205 */         this.catalogManager.debug.message(1, "Failed to create InputSource", resolved);
/* 206 */         return null;
/*     */       }
/*     */     }
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 221 */     this.allowXMLCatalogPI = false;
/* 222 */     super.notationDecl(name, publicId, systemId);
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*     */     throws SAXException
/*     */   {
/* 236 */     this.allowXMLCatalogPI = false;
/* 237 */     super.unparsedEntityDecl(name, publicId, systemId, notationName);
/*     */   }
/*     */ 
/*     */   public void startElement(String uri, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 249 */     this.allowXMLCatalogPI = false;
/* 250 */     super.startElement(uri, localName, qName, atts);
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
/*     */     }
/*     */     else
/*     */     {
/* 314 */       super.processingInstruction(target, pidata);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setupBaseURI(String systemId)
/*     */   {
/* 320 */     URL cwd = null;
/*     */     try
/*     */     {
/* 323 */       cwd = FileURL.makeURL("basename");
/*     */     } catch (MalformedURLException mue) {
/* 325 */       cwd = null;
/*     */     }
/*     */     try
/*     */     {
/* 329 */       this.baseURL = new URL(systemId);
/*     */     } catch (MalformedURLException mue) {
/* 331 */       if (cwd != null) {
/*     */         try {
/* 333 */           this.baseURL = new URL(cwd, systemId);
/*     */         }
/*     */         catch (MalformedURLException mue2) {
/* 336 */           this.baseURL = null;
/*     */         }
/*     */       }
/*     */       else
/* 340 */         this.baseURL = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void explain(String systemId)
/*     */   {
/* 347 */     if (!suppressExplanation) {
/* 348 */       System.out.println("XMLReader probably encountered bad URI in " + systemId);
/* 349 */       System.out.println("For example, replace '/some/uri' with 'file:/some/uri'.");
/*     */     }
/* 351 */     suppressExplanation = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.tools.ResolvingXMLFilter
 * JD-Core Version:    0.6.2
 */