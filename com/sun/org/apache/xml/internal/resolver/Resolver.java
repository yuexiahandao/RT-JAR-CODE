/*     */ package com.sun.org.apache.xml.internal.resolver;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
/*     */ import com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ 
/*     */ public class Resolver extends Catalog
/*     */ {
/*  58 */   public static final int URISUFFIX = CatalogEntry.addEntryType("URISUFFIX", 2);
/*     */ 
/*  66 */   public static final int SYSTEMSUFFIX = CatalogEntry.addEntryType("SYSTEMSUFFIX", 2);
/*     */ 
/*  73 */   public static final int RESOLVER = CatalogEntry.addEntryType("RESOLVER", 1);
/*     */ 
/*  83 */   public static final int SYSTEMREVERSE = CatalogEntry.addEntryType("SYSTEMREVERSE", 1);
/*     */ 
/*     */   public void setupReaders()
/*     */   {
/*  90 */     SAXParserFactory spf = this.catalogManager.useServicesMechanism() ? SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
/*     */ 
/*  92 */     spf.setNamespaceAware(true);
/*  93 */     spf.setValidating(false);
/*     */ 
/*  95 */     SAXCatalogReader saxReader = new SAXCatalogReader(spf);
/*     */ 
/*  97 */     saxReader.setCatalogParser(null, "XMLCatalog", "com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader");
/*     */ 
/* 100 */     saxReader.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader");
/*     */ 
/* 104 */     addReader("application/xml", saxReader);
/*     */ 
/* 106 */     TR9401CatalogReader textReader = new TR9401CatalogReader();
/* 107 */     addReader("text/plain", textReader);
/*     */   }
/*     */ 
/*     */   public void addEntry(CatalogEntry entry)
/*     */   {
/* 121 */     int type = entry.getEntryType();
/*     */ 
/* 123 */     if (type == URISUFFIX) {
/* 124 */       String suffix = normalizeURI(entry.getEntryArg(0));
/* 125 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*     */ 
/* 127 */       entry.setEntryArg(1, fsi);
/*     */ 
/* 129 */       this.catalogManager.debug.message(4, "URISUFFIX", suffix, fsi);
/* 130 */     } else if (type == SYSTEMSUFFIX) {
/* 131 */       String suffix = normalizeURI(entry.getEntryArg(0));
/* 132 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*     */ 
/* 134 */       entry.setEntryArg(1, fsi);
/*     */ 
/* 136 */       this.catalogManager.debug.message(4, "SYSTEMSUFFIX", suffix, fsi);
/*     */     }
/*     */ 
/* 139 */     super.addEntry(entry);
/*     */   }
/*     */ 
/*     */   public String resolveURI(String uri)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 165 */     String resolved = super.resolveURI(uri);
/* 166 */     if (resolved != null) {
/* 167 */       return resolved;
/*     */     }
/*     */ 
/* 170 */     Enumeration en = this.catalogEntries.elements();
/* 171 */     while (en.hasMoreElements()) {
/* 172 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 173 */       if (e.getEntryType() == RESOLVER) {
/* 174 */         resolved = resolveExternalSystem(uri, e.getEntryArg(0));
/* 175 */         if (resolved != null)
/* 176 */           return resolved;
/*     */       }
/* 178 */       else if (e.getEntryType() == URISUFFIX) {
/* 179 */         String suffix = e.getEntryArg(0);
/* 180 */         String result = e.getEntryArg(1);
/*     */ 
/* 182 */         if ((suffix.length() <= uri.length()) && (uri.substring(uri.length() - suffix.length()).equals(suffix)))
/*     */         {
/* 184 */           return result;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 190 */     return resolveSubordinateCatalogs(Catalog.URI, null, null, uri);
/*     */   }
/*     */ 
/*     */   public String resolveSystem(String systemId)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 222 */     String resolved = super.resolveSystem(systemId);
/* 223 */     if (resolved != null) {
/* 224 */       return resolved;
/*     */     }
/*     */ 
/* 227 */     Enumeration en = this.catalogEntries.elements();
/* 228 */     while (en.hasMoreElements()) {
/* 229 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 230 */       if (e.getEntryType() == RESOLVER) {
/* 231 */         resolved = resolveExternalSystem(systemId, e.getEntryArg(0));
/* 232 */         if (resolved != null)
/* 233 */           return resolved;
/*     */       }
/* 235 */       else if (e.getEntryType() == SYSTEMSUFFIX) {
/* 236 */         String suffix = e.getEntryArg(0);
/* 237 */         String result = e.getEntryArg(1);
/*     */ 
/* 239 */         if ((suffix.length() <= systemId.length()) && (systemId.substring(systemId.length() - suffix.length()).equals(suffix)))
/*     */         {
/* 241 */           return result;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 246 */     return resolveSubordinateCatalogs(Catalog.SYSTEM, null, null, systemId);
/*     */   }
/*     */ 
/*     */   public String resolvePublic(String publicId, String systemId)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 284 */     String resolved = super.resolvePublic(publicId, systemId);
/* 285 */     if (resolved != null) {
/* 286 */       return resolved;
/*     */     }
/*     */ 
/* 289 */     Enumeration en = this.catalogEntries.elements();
/* 290 */     while (en.hasMoreElements()) {
/* 291 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 292 */       if (e.getEntryType() == RESOLVER) {
/* 293 */         if (systemId != null) {
/* 294 */           resolved = resolveExternalSystem(systemId, e.getEntryArg(0));
/*     */ 
/* 296 */           if (resolved != null) {
/* 297 */             return resolved;
/*     */           }
/*     */         }
/* 300 */         resolved = resolveExternalPublic(publicId, e.getEntryArg(0));
/* 301 */         if (resolved != null) {
/* 302 */           return resolved;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 307 */     return resolveSubordinateCatalogs(Catalog.PUBLIC, null, publicId, systemId);
/*     */   }
/*     */ 
/*     */   protected String resolveExternalSystem(String systemId, String resolver)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 323 */     Resolver r = queryResolver(resolver, "i2l", systemId, null);
/* 324 */     if (r != null) {
/* 325 */       return r.resolveSystem(systemId);
/*     */     }
/* 327 */     return null;
/*     */   }
/*     */ 
/*     */   protected String resolveExternalPublic(String publicId, String resolver)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 341 */     Resolver r = queryResolver(resolver, "fpi2l", publicId, null);
/* 342 */     if (r != null) {
/* 343 */       return r.resolvePublic(publicId, null);
/*     */     }
/* 345 */     return null;
/*     */   }
/*     */ 
/*     */   protected Resolver queryResolver(String resolver, String command, String arg1, String arg2)
/*     */   {
/* 363 */     InputStream iStream = null;
/* 364 */     String RFC2483 = resolver + "?command=" + command + "&format=tr9401&uri=" + arg1 + "&uri2=" + arg2;
/*     */ 
/* 367 */     String line = null;
/*     */     try
/*     */     {
/* 370 */       URL url = new URL(RFC2483);
/*     */ 
/* 372 */       URLConnection urlCon = url.openConnection();
/*     */ 
/* 374 */       urlCon.setUseCaches(false);
/*     */ 
/* 376 */       Resolver r = (Resolver)newCatalog();
/*     */ 
/* 378 */       String cType = urlCon.getContentType();
/*     */ 
/* 381 */       if (cType.indexOf(";") > 0) {
/* 382 */         cType = cType.substring(0, cType.indexOf(";"));
/*     */       }
/*     */ 
/* 385 */       r.parseCatalog(cType, urlCon.getInputStream());
/*     */ 
/* 387 */       return r;
/*     */     } catch (CatalogException cex) {
/* 389 */       if (cex.getExceptionType() == 6)
/* 390 */         this.catalogManager.debug.message(1, "Unparseable catalog: " + RFC2483);
/* 391 */       else if (cex.getExceptionType() == 5)
/*     */       {
/* 393 */         this.catalogManager.debug.message(1, "Unknown catalog format: " + RFC2483);
/*     */       }
/* 395 */       return null;
/*     */     } catch (MalformedURLException mue) {
/* 397 */       this.catalogManager.debug.message(1, "Malformed resolver URL: " + RFC2483);
/* 398 */       return null;
/*     */     } catch (IOException ie) {
/* 400 */       this.catalogManager.debug.message(1, "I/O Exception opening resolver: " + RFC2483);
/* 401 */     }return null;
/*     */   }
/*     */ 
/*     */   private Vector appendVector(Vector vec, Vector appvec)
/*     */   {
/* 413 */     if (appvec != null) {
/* 414 */       for (int count = 0; count < appvec.size(); count++) {
/* 415 */         vec.addElement(appvec.elementAt(count));
/*     */       }
/*     */     }
/* 418 */     return vec;
/*     */   }
/*     */ 
/*     */   public Vector resolveAllSystemReverse(String systemId)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 430 */     Vector resolved = new Vector();
/*     */ 
/* 433 */     if (systemId != null) {
/* 434 */       Vector localResolved = resolveLocalSystemReverse(systemId);
/* 435 */       resolved = appendVector(resolved, localResolved);
/*     */     }
/*     */ 
/* 439 */     Vector subResolved = resolveAllSubordinateCatalogs(SYSTEMREVERSE, null, null, systemId);
/*     */ 
/* 444 */     return appendVector(resolved, subResolved);
/*     */   }
/*     */ 
/*     */   public String resolveSystemReverse(String systemId)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 456 */     Vector resolved = resolveAllSystemReverse(systemId);
/* 457 */     if ((resolved != null) && (resolved.size() > 0)) {
/* 458 */       return (String)resolved.elementAt(0);
/*     */     }
/* 460 */     return null;
/*     */   }
/*     */ 
/*     */   public Vector resolveAllSystem(String systemId)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 493 */     Vector resolutions = new Vector();
/*     */ 
/* 496 */     if (systemId != null) {
/* 497 */       Vector localResolutions = resolveAllLocalSystem(systemId);
/* 498 */       resolutions = appendVector(resolutions, localResolutions);
/*     */     }
/*     */ 
/* 502 */     Vector subResolutions = resolveAllSubordinateCatalogs(SYSTEM, null, null, systemId);
/*     */ 
/* 506 */     resolutions = appendVector(resolutions, subResolutions);
/*     */ 
/* 508 */     if (resolutions.size() > 0) {
/* 509 */       return resolutions;
/*     */     }
/* 511 */     return null;
/*     */   }
/*     */ 
/*     */   private Vector resolveAllLocalSystem(String systemId)
/*     */   {
/* 527 */     Vector map = new Vector();
/* 528 */     String osname = SecuritySupport.getSystemProperty("os.name");
/* 529 */     boolean windows = osname.indexOf("Windows") >= 0;
/* 530 */     Enumeration en = this.catalogEntries.elements();
/* 531 */     while (en.hasMoreElements()) {
/* 532 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 533 */       if ((e.getEntryType() == SYSTEM) && ((e.getEntryArg(0).equals(systemId)) || ((windows) && (e.getEntryArg(0).equalsIgnoreCase(systemId)))))
/*     */       {
/* 537 */         map.addElement(e.getEntryArg(1));
/*     */       }
/*     */     }
/* 540 */     if (map.size() == 0) {
/* 541 */       return null;
/*     */     }
/* 543 */     return map;
/*     */   }
/*     */ 
/*     */   private Vector resolveLocalSystemReverse(String systemId)
/*     */   {
/* 555 */     Vector map = new Vector();
/* 556 */     String osname = SecuritySupport.getSystemProperty("os.name");
/* 557 */     boolean windows = osname.indexOf("Windows") >= 0;
/* 558 */     Enumeration en = this.catalogEntries.elements();
/* 559 */     while (en.hasMoreElements()) {
/* 560 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 561 */       if ((e.getEntryType() == SYSTEM) && ((e.getEntryArg(1).equals(systemId)) || ((windows) && (e.getEntryArg(1).equalsIgnoreCase(systemId)))))
/*     */       {
/* 565 */         map.addElement(e.getEntryArg(0));
/*     */       }
/*     */     }
/* 568 */     if (map.size() == 0) {
/* 569 */       return null;
/*     */     }
/* 571 */     return map;
/*     */   }
/*     */ 
/*     */   private synchronized Vector resolveAllSubordinateCatalogs(int entityType, String entityName, String publicId, String systemId)
/*     */     throws MalformedURLException, IOException
/*     */   {
/* 609 */     Vector resolutions = new Vector();
/*     */ 
/* 611 */     for (int catPos = 0; catPos < this.catalogs.size(); catPos++) {
/* 612 */       Resolver c = null;
/*     */       try
/*     */       {
/* 615 */         c = (Resolver)this.catalogs.elementAt(catPos);
/*     */       } catch (ClassCastException e) {
/* 617 */         String catfile = (String)this.catalogs.elementAt(catPos);
/* 618 */         c = (Resolver)newCatalog();
/*     */         try
/*     */         {
/* 621 */           c.parseCatalog(catfile);
/*     */         } catch (MalformedURLException mue) {
/* 623 */           this.catalogManager.debug.message(1, "Malformed Catalog URL", catfile);
/*     */         } catch (FileNotFoundException fnfe) {
/* 625 */           this.catalogManager.debug.message(1, "Failed to load catalog, file not found", catfile);
/*     */         }
/*     */         catch (IOException ioe) {
/* 628 */           this.catalogManager.debug.message(1, "Failed to load catalog, I/O error", catfile);
/*     */         }
/*     */ 
/* 631 */         this.catalogs.setElementAt(c, catPos);
/*     */       }
/*     */ 
/* 634 */       String resolved = null;
/*     */ 
/* 637 */       if (entityType == DOCTYPE) {
/* 638 */         resolved = c.resolveDoctype(entityName, publicId, systemId);
/*     */ 
/* 641 */         if (resolved != null)
/*     */         {
/* 643 */           resolutions.addElement(resolved);
/* 644 */           return resolutions;
/*     */         }
/* 646 */       } else if (entityType == DOCUMENT) {
/* 647 */         resolved = c.resolveDocument();
/* 648 */         if (resolved != null)
/*     */         {
/* 650 */           resolutions.addElement(resolved);
/* 651 */           return resolutions;
/*     */         }
/* 653 */       } else if (entityType == ENTITY) {
/* 654 */         resolved = c.resolveEntity(entityName, publicId, systemId);
/*     */ 
/* 657 */         if (resolved != null)
/*     */         {
/* 659 */           resolutions.addElement(resolved);
/* 660 */           return resolutions;
/*     */         }
/* 662 */       } else if (entityType == NOTATION) {
/* 663 */         resolved = c.resolveNotation(entityName, publicId, systemId);
/*     */ 
/* 666 */         if (resolved != null)
/*     */         {
/* 668 */           resolutions.addElement(resolved);
/* 669 */           return resolutions;
/*     */         }
/* 671 */       } else if (entityType == PUBLIC) {
/* 672 */         resolved = c.resolvePublic(publicId, systemId);
/* 673 */         if (resolved != null)
/*     */         {
/* 675 */           resolutions.addElement(resolved);
/* 676 */           return resolutions;
/*     */         }
/*     */       } else { if (entityType == SYSTEM) {
/* 679 */           Vector localResolutions = c.resolveAllSystem(systemId);
/* 680 */           resolutions = appendVector(resolutions, localResolutions);
/* 681 */           break;
/* 682 */         }if (entityType == SYSTEMREVERSE) {
/* 683 */           Vector localResolutions = c.resolveAllSystemReverse(systemId);
/* 684 */           resolutions = appendVector(resolutions, localResolutions);
/*     */         }
/*     */       }
/*     */     }
/* 688 */     if (resolutions != null) {
/* 689 */       return resolutions;
/*     */     }
/* 691 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.Resolver
 * JD-Core Version:    0.6.2
 */