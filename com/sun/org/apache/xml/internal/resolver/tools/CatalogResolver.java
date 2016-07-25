/*     */ package com.sun.org.apache.xml.internal.resolver.tools;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class CatalogResolver
/*     */   implements EntityResolver, URIResolver
/*     */ {
/*  69 */   public boolean namespaceAware = true;
/*     */ 
/*  72 */   public boolean validating = false;
/*     */ 
/*  75 */   private Catalog catalog = null;
/*     */ 
/*  78 */   private CatalogManager catalogManager = CatalogManager.getStaticManager();
/*     */ 
/*     */   public CatalogResolver()
/*     */   {
/*  82 */     initializeCatalogs(false);
/*     */   }
/*     */ 
/*     */   public CatalogResolver(boolean privateCatalog)
/*     */   {
/*  87 */     initializeCatalogs(privateCatalog);
/*     */   }
/*     */ 
/*     */   public CatalogResolver(CatalogManager manager)
/*     */   {
/*  92 */     this.catalogManager = manager;
/*  93 */     initializeCatalogs(!this.catalogManager.getUseStaticCatalog());
/*     */   }
/*     */ 
/*     */   private void initializeCatalogs(boolean privateCatalog)
/*     */   {
/*  98 */     this.catalog = this.catalogManager.getCatalog();
/*     */   }
/*     */ 
/*     */   public Catalog getCatalog()
/*     */   {
/* 103 */     return this.catalog;
/*     */   }
/*     */ 
/*     */   public String getResolvedEntity(String publicId, String systemId)
/*     */   {
/* 127 */     String resolved = null;
/*     */ 
/* 129 */     if (this.catalog == null) {
/* 130 */       this.catalogManager.debug.message(1, "Catalog resolution attempted with null catalog; ignored");
/* 131 */       return null;
/*     */     }
/*     */ 
/* 134 */     if (systemId != null) {
/*     */       try {
/* 136 */         resolved = this.catalog.resolveSystem(systemId);
/*     */       } catch (MalformedURLException me) {
/* 138 */         this.catalogManager.debug.message(1, "Malformed URL exception trying to resolve", publicId);
/*     */ 
/* 140 */         resolved = null;
/*     */       } catch (IOException ie) {
/* 142 */         this.catalogManager.debug.message(1, "I/O exception trying to resolve", publicId);
/* 143 */         resolved = null;
/*     */       }
/*     */     }
/*     */ 
/* 147 */     if (resolved == null) {
/* 148 */       if (publicId != null) {
/*     */         try {
/* 150 */           resolved = this.catalog.resolvePublic(publicId, systemId);
/*     */         } catch (MalformedURLException me) {
/* 152 */           this.catalogManager.debug.message(1, "Malformed URL exception trying to resolve", publicId);
/*     */         }
/*     */         catch (IOException ie) {
/* 155 */           this.catalogManager.debug.message(1, "I/O exception trying to resolve", publicId);
/*     */         }
/*     */       }
/*     */ 
/* 159 */       if (resolved != null)
/* 160 */         this.catalogManager.debug.message(2, "Resolved public", publicId, resolved);
/*     */     }
/*     */     else {
/* 163 */       this.catalogManager.debug.message(2, "Resolved system", systemId, resolved);
/*     */     }
/*     */ 
/* 166 */     return resolved;
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */   {
/* 197 */     String resolved = getResolvedEntity(publicId, systemId);
/*     */ 
/* 199 */     if (resolved != null) {
/*     */       try {
/* 201 */         InputSource iSource = new InputSource(resolved);
/* 202 */         iSource.setPublicId(publicId);
/*     */ 
/* 215 */         URL url = new URL(resolved);
/* 216 */         InputStream iStream = url.openStream();
/* 217 */         iSource.setByteStream(iStream);
/*     */ 
/* 219 */         return iSource;
/*     */       } catch (Exception e) {
/* 221 */         this.catalogManager.debug.message(1, "Failed to create InputSource", resolved);
/* 222 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   public Source resolve(String href, String base)
/*     */     throws TransformerException
/*     */   {
/* 233 */     String uri = href;
/* 234 */     String fragment = null;
/* 235 */     int hashPos = href.indexOf("#");
/* 236 */     if (hashPos >= 0) {
/* 237 */       uri = href.substring(0, hashPos);
/* 238 */       fragment = href.substring(hashPos + 1);
/*     */     }
/*     */ 
/* 241 */     String result = null;
/*     */     try
/*     */     {
/* 244 */       result = this.catalog.resolveURI(href);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/* 249 */     if (result == null) {
/*     */       try {
/* 251 */         URL url = null;
/*     */ 
/* 253 */         if (base == null) {
/* 254 */           url = new URL(uri);
/* 255 */           result = url.toString();
/*     */         } else {
/* 257 */           URL baseURL = new URL(base);
/* 258 */           url = href.length() == 0 ? baseURL : new URL(baseURL, uri);
/* 259 */           result = url.toString();
/*     */         }
/*     */       }
/*     */       catch (MalformedURLException mue) {
/* 263 */         String absBase = makeAbsolute(base);
/* 264 */         if (!absBase.equals(base))
/*     */         {
/* 266 */           return resolve(href, absBase);
/*     */         }
/* 268 */         throw new TransformerException("Malformed URL " + href + "(base " + base + ")", mue);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 275 */     this.catalogManager.debug.message(2, "Resolved URI", href, result);
/*     */ 
/* 277 */     SAXSource source = new SAXSource();
/* 278 */     source.setInputSource(new InputSource(result));
/* 279 */     setEntityResolver(source);
/* 280 */     return source;
/*     */   }
/*     */ 
/*     */   private void setEntityResolver(SAXSource source)
/*     */     throws TransformerException
/*     */   {
/* 305 */     XMLReader reader = source.getXMLReader();
/* 306 */     if (reader == null) {
/* 307 */       SAXParserFactory spFactory = this.catalogManager.useServicesMechanism() ? SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
/*     */ 
/* 309 */       spFactory.setNamespaceAware(true);
/*     */       try {
/* 311 */         reader = spFactory.newSAXParser().getXMLReader();
/*     */       }
/*     */       catch (ParserConfigurationException ex) {
/* 314 */         throw new TransformerException(ex);
/*     */       }
/*     */       catch (SAXException ex) {
/* 317 */         throw new TransformerException(ex);
/*     */       }
/*     */     }
/* 320 */     reader.setEntityResolver(this);
/* 321 */     source.setXMLReader(reader);
/*     */   }
/*     */ 
/*     */   private String makeAbsolute(String uri)
/*     */   {
/* 326 */     if (uri == null) {
/* 327 */       uri = "";
/*     */     }
/*     */     try
/*     */     {
/* 331 */       URL url = new URL(uri);
/* 332 */       return url.toString();
/*     */     } catch (MalformedURLException mue) {
/*     */       try {
/* 335 */         URL fileURL = FileURL.makeURL(uri);
/* 336 */         return fileURL.toString(); } catch (MalformedURLException mue2) {
/*     */       }
/*     */     }
/* 339 */     return uri;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver
 * JD-Core Version:    0.6.2
 */