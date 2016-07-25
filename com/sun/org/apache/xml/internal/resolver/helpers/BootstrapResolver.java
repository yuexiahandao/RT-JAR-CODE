/*     */ package com.sun.org.apache.xml.internal.resolver.helpers;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Hashtable;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ public class BootstrapResolver
/*     */   implements EntityResolver, URIResolver
/*     */ {
/*     */   public static final String xmlCatalogXSD = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.xsd";
/*     */   public static final String xmlCatalogRNG = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.rng";
/*     */   public static final String xmlCatalogPubId = "-//OASIS//DTD XML Catalogs V1.0//EN";
/*     */   public static final String xmlCatalogSysId = "http://www.oasis-open.org/committees/entity/release/1.0/catalog.dtd";
/*  69 */   private Hashtable publicMap = new Hashtable();
/*     */ 
/*  72 */   private Hashtable systemMap = new Hashtable();
/*     */ 
/*  75 */   private Hashtable uriMap = new Hashtable();
/*     */ 
/*     */   public BootstrapResolver()
/*     */   {
/*  79 */     URL url = getClass().getResource("/com/sun/org/apache/xml/internal/resolver/etc/catalog.dtd");
/*  80 */     if (url != null) {
/*  81 */       this.publicMap.put("-//OASIS//DTD XML Catalogs V1.0//EN", url.toString());
/*  82 */       this.systemMap.put("http://www.oasis-open.org/committees/entity/release/1.0/catalog.dtd", url.toString());
/*     */     }
/*     */ 
/*  85 */     url = getClass().getResource("/com/sun/org/apache/xml/internal/resolver/etc/catalog.rng");
/*  86 */     if (url != null) {
/*  87 */       this.uriMap.put("http://www.oasis-open.org/committees/entity/release/1.0/catalog.rng", url.toString());
/*     */     }
/*     */ 
/*  90 */     url = getClass().getResource("/com/sun/org/apache/xml/internal/resolver/etc/catalog.xsd");
/*  91 */     if (url != null)
/*  92 */       this.uriMap.put("http://www.oasis-open.org/committees/entity/release/1.0/catalog.xsd", url.toString());
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */   {
/*  98 */     String resolved = null;
/*     */ 
/* 100 */     if ((systemId != null) && (this.systemMap.containsKey(systemId)))
/* 101 */       resolved = (String)this.systemMap.get(systemId);
/* 102 */     else if ((publicId != null) && (this.publicMap.containsKey(publicId))) {
/* 103 */       resolved = (String)this.publicMap.get(publicId);
/*     */     }
/*     */ 
/* 106 */     if (resolved != null) {
/*     */       try {
/* 108 */         InputSource iSource = new InputSource(resolved);
/* 109 */         iSource.setPublicId(publicId);
/*     */ 
/* 122 */         URL url = new URL(resolved);
/* 123 */         InputStream iStream = url.openStream();
/* 124 */         iSource.setByteStream(iStream);
/*     */ 
/* 126 */         return iSource;
/*     */       }
/*     */       catch (Exception e) {
/* 129 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */   public Source resolve(String href, String base)
/*     */     throws TransformerException
/*     */   {
/* 140 */     String uri = href;
/* 141 */     String fragment = null;
/* 142 */     int hashPos = href.indexOf("#");
/* 143 */     if (hashPos >= 0) {
/* 144 */       uri = href.substring(0, hashPos);
/* 145 */       fragment = href.substring(hashPos + 1);
/*     */     }
/*     */ 
/* 148 */     String result = null;
/* 149 */     if ((href != null) && (this.uriMap.containsKey(href))) {
/* 150 */       result = (String)this.uriMap.get(href);
/*     */     }
/*     */ 
/* 153 */     if (result == null) {
/*     */       try {
/* 155 */         URL url = null;
/*     */ 
/* 157 */         if (base == null) {
/* 158 */           url = new URL(uri);
/* 159 */           result = url.toString();
/*     */         } else {
/* 161 */           URL baseURL = new URL(base);
/* 162 */           url = href.length() == 0 ? baseURL : new URL(baseURL, uri);
/* 163 */           result = url.toString();
/*     */         }
/*     */       }
/*     */       catch (MalformedURLException mue) {
/* 167 */         String absBase = makeAbsolute(base);
/* 168 */         if (!absBase.equals(base))
/*     */         {
/* 170 */           return resolve(href, absBase);
/*     */         }
/* 172 */         throw new TransformerException("Malformed URL " + href + "(base " + base + ")", mue);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 179 */     SAXSource source = new SAXSource();
/* 180 */     source.setInputSource(new InputSource(result));
/* 181 */     return source;
/*     */   }
/*     */ 
/*     */   private String makeAbsolute(String uri)
/*     */   {
/* 186 */     if (uri == null) {
/* 187 */       uri = "";
/*     */     }
/*     */     try
/*     */     {
/* 191 */       URL url = new URL(uri);
/* 192 */       return url.toString();
/*     */     } catch (MalformedURLException mue) {
/*     */       try {
/* 195 */         URL fileURL = FileURL.makeURL(uri);
/* 196 */         return fileURL.toString(); } catch (MalformedURLException mue2) {
/*     */       }
/*     */     }
/* 199 */     return uri;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.helpers.BootstrapResolver
 * JD-Core Version:    0.6.2
 */