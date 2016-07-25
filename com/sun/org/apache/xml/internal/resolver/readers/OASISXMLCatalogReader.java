/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class OASISXMLCatalogReader extends SAXCatalogReader
/*     */   implements SAXCatalogParser
/*     */ {
/*  51 */   protected Catalog catalog = null;
/*     */   public static final String namespaceName = "urn:oasis:names:tc:entity:xmlns:xml:catalog";
/*     */   public static final String tr9401NamespaceName = "urn:oasis:names:tc:entity:xmlns:tr9401:catalog";
/*  59 */   protected Stack baseURIStack = new Stack();
/*  60 */   protected Stack overrideStack = new Stack();
/*  61 */   protected Stack namespaceStack = new Stack();
/*     */ 
/*     */   public void setCatalog(Catalog catalog)
/*     */   {
/*  65 */     this.catalog = catalog;
/*  66 */     this.debug = catalog.getCatalogManager().debug;
/*     */   }
/*     */ 
/*     */   public Catalog getCatalog()
/*     */   {
/*  71 */     return this.catalog;
/*     */   }
/*     */ 
/*     */   protected boolean inExtensionNamespace()
/*     */   {
/*  81 */     boolean inExtension = false;
/*     */ 
/*  83 */     Enumeration elements = this.namespaceStack.elements();
/*  84 */     while ((!inExtension) && (elements.hasMoreElements())) {
/*  85 */       String ns = (String)elements.nextElement();
/*  86 */       if (ns == null)
/*  87 */         inExtension = true;
/*     */       else {
/*  89 */         inExtension = (!ns.equals("urn:oasis:names:tc:entity:xmlns:tr9401:catalog")) && (!ns.equals("urn:oasis:names:tc:entity:xmlns:xml:catalog"));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  94 */     return inExtension;
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 108 */     this.baseURIStack.push(this.catalog.getCurrentBase());
/* 109 */     this.overrideStack.push(this.catalog.getDefaultOverride());
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/* 137 */     int entryType = -1;
/* 138 */     Vector entryArgs = new Vector();
/*     */ 
/* 140 */     this.namespaceStack.push(namespaceURI);
/*     */ 
/* 142 */     boolean inExtension = inExtensionNamespace();
/*     */ 
/* 144 */     if ((namespaceURI != null) && ("urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(namespaceURI)) && (!inExtension))
/*     */     {
/* 148 */       if (atts.getValue("xml:base") != null) {
/* 149 */         String baseURI = atts.getValue("xml:base");
/* 150 */         entryType = Catalog.BASE;
/* 151 */         entryArgs.add(baseURI);
/* 152 */         this.baseURIStack.push(baseURI);
/*     */ 
/* 154 */         this.debug.message(4, "xml:base", baseURI);
/*     */         try
/*     */         {
/* 157 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 158 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/* 160 */           if (cex.getExceptionType() == 3)
/* 161 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 162 */           else if (cex.getExceptionType() == 2) {
/* 163 */             this.debug.message(1, "Invalid catalog entry (base)", localName);
/*     */           }
/*     */         }
/*     */ 
/* 167 */         entryType = -1;
/* 168 */         entryArgs = new Vector();
/*     */       }
/*     */       else {
/* 171 */         this.baseURIStack.push(this.baseURIStack.peek());
/*     */       }
/*     */ 
/* 174 */       if (((localName.equals("catalog")) || (localName.equals("group"))) && (atts.getValue("prefer") != null))
/*     */       {
/* 176 */         String override = atts.getValue("prefer");
/*     */ 
/* 178 */         if (override.equals("public")) {
/* 179 */           override = "yes";
/* 180 */         } else if (override.equals("system")) {
/* 181 */           override = "no";
/*     */         } else {
/* 183 */           this.debug.message(1, "Invalid prefer: must be 'system' or 'public'", localName);
/*     */ 
/* 186 */           override = this.catalog.getDefaultOverride();
/*     */         }
/*     */ 
/* 189 */         entryType = Catalog.OVERRIDE;
/* 190 */         entryArgs.add(override);
/* 191 */         this.overrideStack.push(override);
/*     */ 
/* 193 */         this.debug.message(4, "override", override);
/*     */         try
/*     */         {
/* 196 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 197 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/* 199 */           if (cex.getExceptionType() == 3)
/* 200 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 201 */           else if (cex.getExceptionType() == 2) {
/* 202 */             this.debug.message(1, "Invalid catalog entry (override)", localName);
/*     */           }
/*     */         }
/*     */ 
/* 206 */         entryType = -1;
/* 207 */         entryArgs = new Vector();
/*     */       }
/*     */       else {
/* 210 */         this.overrideStack.push(this.overrideStack.peek());
/*     */       }
/*     */ 
/* 213 */       if (localName.equals("delegatePublic")) {
/* 214 */         if (checkAttributes(atts, "publicIdStartString", "catalog")) {
/* 215 */           entryType = Catalog.DELEGATE_PUBLIC;
/* 216 */           entryArgs.add(atts.getValue("publicIdStartString"));
/* 217 */           entryArgs.add(atts.getValue("catalog"));
/*     */ 
/* 219 */           this.debug.message(4, "delegatePublic", PublicId.normalize(atts.getValue("publicIdStartString")), atts.getValue("catalog"));
/*     */         }
/*     */ 
/*     */       }
/* 223 */       else if (localName.equals("delegateSystem")) {
/* 224 */         if (checkAttributes(atts, "systemIdStartString", "catalog")) {
/* 225 */           entryType = Catalog.DELEGATE_SYSTEM;
/* 226 */           entryArgs.add(atts.getValue("systemIdStartString"));
/* 227 */           entryArgs.add(atts.getValue("catalog"));
/*     */ 
/* 229 */           this.debug.message(4, "delegateSystem", atts.getValue("systemIdStartString"), atts.getValue("catalog"));
/*     */         }
/*     */ 
/*     */       }
/* 233 */       else if (localName.equals("delegateURI")) {
/* 234 */         if (checkAttributes(atts, "uriStartString", "catalog")) {
/* 235 */           entryType = Catalog.DELEGATE_URI;
/* 236 */           entryArgs.add(atts.getValue("uriStartString"));
/* 237 */           entryArgs.add(atts.getValue("catalog"));
/*     */ 
/* 239 */           this.debug.message(4, "delegateURI", atts.getValue("uriStartString"), atts.getValue("catalog"));
/*     */         }
/*     */ 
/*     */       }
/* 243 */       else if (localName.equals("rewriteSystem")) {
/* 244 */         if (checkAttributes(atts, "systemIdStartString", "rewritePrefix")) {
/* 245 */           entryType = Catalog.REWRITE_SYSTEM;
/* 246 */           entryArgs.add(atts.getValue("systemIdStartString"));
/* 247 */           entryArgs.add(atts.getValue("rewritePrefix"));
/*     */ 
/* 249 */           this.debug.message(4, "rewriteSystem", atts.getValue("systemIdStartString"), atts.getValue("rewritePrefix"));
/*     */         }
/*     */ 
/*     */       }
/* 253 */       else if (localName.equals("systemSuffix")) {
/* 254 */         if (checkAttributes(atts, "systemIdSuffix", "uri")) {
/* 255 */           entryType = Catalog.SYSTEM_SUFFIX;
/* 256 */           entryArgs.add(atts.getValue("systemIdSuffix"));
/* 257 */           entryArgs.add(atts.getValue("uri"));
/*     */ 
/* 259 */           this.debug.message(4, "systemSuffix", atts.getValue("systemIdSuffix"), atts.getValue("uri"));
/*     */         }
/*     */ 
/*     */       }
/* 263 */       else if (localName.equals("rewriteURI")) {
/* 264 */         if (checkAttributes(atts, "uriStartString", "rewritePrefix")) {
/* 265 */           entryType = Catalog.REWRITE_URI;
/* 266 */           entryArgs.add(atts.getValue("uriStartString"));
/* 267 */           entryArgs.add(atts.getValue("rewritePrefix"));
/*     */ 
/* 269 */           this.debug.message(4, "rewriteURI", atts.getValue("uriStartString"), atts.getValue("rewritePrefix"));
/*     */         }
/*     */ 
/*     */       }
/* 273 */       else if (localName.equals("uriSuffix")) {
/* 274 */         if (checkAttributes(atts, "uriSuffix", "uri")) {
/* 275 */           entryType = Catalog.URI_SUFFIX;
/* 276 */           entryArgs.add(atts.getValue("uriSuffix"));
/* 277 */           entryArgs.add(atts.getValue("uri"));
/*     */ 
/* 279 */           this.debug.message(4, "uriSuffix", atts.getValue("uriSuffix"), atts.getValue("uri"));
/*     */         }
/*     */ 
/*     */       }
/* 283 */       else if (localName.equals("nextCatalog")) {
/* 284 */         if (checkAttributes(atts, "catalog")) {
/* 285 */           entryType = Catalog.CATALOG;
/* 286 */           entryArgs.add(atts.getValue("catalog"));
/*     */ 
/* 288 */           this.debug.message(4, "nextCatalog", atts.getValue("catalog"));
/*     */         }
/* 290 */       } else if (localName.equals("public")) {
/* 291 */         if (checkAttributes(atts, "publicId", "uri")) {
/* 292 */           entryType = Catalog.PUBLIC;
/* 293 */           entryArgs.add(atts.getValue("publicId"));
/* 294 */           entryArgs.add(atts.getValue("uri"));
/*     */ 
/* 296 */           this.debug.message(4, "public", PublicId.normalize(atts.getValue("publicId")), atts.getValue("uri"));
/*     */         }
/*     */ 
/*     */       }
/* 300 */       else if (localName.equals("system")) {
/* 301 */         if (checkAttributes(atts, "systemId", "uri")) {
/* 302 */           entryType = Catalog.SYSTEM;
/* 303 */           entryArgs.add(atts.getValue("systemId"));
/* 304 */           entryArgs.add(atts.getValue("uri"));
/*     */ 
/* 306 */           this.debug.message(4, "system", atts.getValue("systemId"), atts.getValue("uri"));
/*     */         }
/*     */ 
/*     */       }
/* 310 */       else if (localName.equals("uri")) {
/* 311 */         if (checkAttributes(atts, "name", "uri")) {
/* 312 */           entryType = Catalog.URI;
/* 313 */           entryArgs.add(atts.getValue("name"));
/* 314 */           entryArgs.add(atts.getValue("uri"));
/*     */ 
/* 316 */           this.debug.message(4, "uri", atts.getValue("name"), atts.getValue("uri"));
/*     */         }
/*     */ 
/*     */       }
/* 320 */       else if (!localName.equals("catalog"))
/*     */       {
/* 322 */         if (!localName.equals("group"))
/*     */         {
/* 326 */           this.debug.message(1, "Invalid catalog entry type", localName);
/*     */         }
/*     */       }
/* 329 */       if (entryType >= 0) {
/*     */         try {
/* 331 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 332 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/* 334 */           if (cex.getExceptionType() == 3)
/* 335 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 336 */           else if (cex.getExceptionType() == 2) {
/* 337 */             this.debug.message(1, "Invalid catalog entry", localName);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 343 */     if ((namespaceURI != null) && ("urn:oasis:names:tc:entity:xmlns:tr9401:catalog".equals(namespaceURI)) && (!inExtension))
/*     */     {
/* 347 */       if (atts.getValue("xml:base") != null) {
/* 348 */         String baseURI = atts.getValue("xml:base");
/* 349 */         entryType = Catalog.BASE;
/* 350 */         entryArgs.add(baseURI);
/* 351 */         this.baseURIStack.push(baseURI);
/*     */ 
/* 353 */         this.debug.message(4, "xml:base", baseURI);
/*     */         try
/*     */         {
/* 356 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 357 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/* 359 */           if (cex.getExceptionType() == 3)
/* 360 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 361 */           else if (cex.getExceptionType() == 2) {
/* 362 */             this.debug.message(1, "Invalid catalog entry (base)", localName);
/*     */           }
/*     */         }
/*     */ 
/* 366 */         entryType = -1;
/* 367 */         entryArgs = new Vector();
/*     */       }
/*     */       else {
/* 370 */         this.baseURIStack.push(this.baseURIStack.peek());
/*     */       }
/*     */ 
/* 373 */       if (localName.equals("doctype")) {
/* 374 */         entryType = Catalog.DOCTYPE;
/* 375 */         entryArgs.add(atts.getValue("name"));
/* 376 */         entryArgs.add(atts.getValue("uri"));
/* 377 */       } else if (localName.equals("document")) {
/* 378 */         entryType = Catalog.DOCUMENT;
/* 379 */         entryArgs.add(atts.getValue("uri"));
/* 380 */       } else if (localName.equals("dtddecl")) {
/* 381 */         entryType = Catalog.DTDDECL;
/* 382 */         entryArgs.add(atts.getValue("publicId"));
/* 383 */         entryArgs.add(atts.getValue("uri"));
/* 384 */       } else if (localName.equals("entity")) {
/* 385 */         entryType = Catalog.ENTITY;
/* 386 */         entryArgs.add(atts.getValue("name"));
/* 387 */         entryArgs.add(atts.getValue("uri"));
/* 388 */       } else if (localName.equals("linktype")) {
/* 389 */         entryType = Catalog.LINKTYPE;
/* 390 */         entryArgs.add(atts.getValue("name"));
/* 391 */         entryArgs.add(atts.getValue("uri"));
/* 392 */       } else if (localName.equals("notation")) {
/* 393 */         entryType = Catalog.NOTATION;
/* 394 */         entryArgs.add(atts.getValue("name"));
/* 395 */         entryArgs.add(atts.getValue("uri"));
/* 396 */       } else if (localName.equals("sgmldecl")) {
/* 397 */         entryType = Catalog.SGMLDECL;
/* 398 */         entryArgs.add(atts.getValue("uri"));
/*     */       }
/*     */       else {
/* 401 */         this.debug.message(1, "Invalid catalog entry type", localName);
/*     */       }
/*     */ 
/* 404 */       if (entryType >= 0)
/*     */         try {
/* 406 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 407 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/* 409 */           if (cex.getExceptionType() == 3)
/* 410 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 411 */           else if (cex.getExceptionType() == 2)
/* 412 */             this.debug.message(1, "Invalid catalog entry", localName);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean checkAttributes(Attributes atts, String attName)
/*     */   {
/* 420 */     if (atts.getValue(attName) == null) {
/* 421 */       this.debug.message(1, "Error: required attribute " + attName + " missing.");
/* 422 */       return false;
/*     */     }
/* 424 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean checkAttributes(Attributes atts, String attName1, String attName2)
/*     */   {
/* 431 */     return (checkAttributes(atts, attName1)) && (checkAttributes(atts, attName2));
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 441 */     int entryType = -1;
/* 442 */     Vector entryArgs = new Vector();
/*     */ 
/* 444 */     boolean inExtension = inExtensionNamespace();
/*     */ 
/* 446 */     if ((namespaceURI != null) && (!inExtension) && (("urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(namespaceURI)) || ("urn:oasis:names:tc:entity:xmlns:tr9401:catalog".equals(namespaceURI))))
/*     */     {
/* 451 */       String popURI = (String)this.baseURIStack.pop();
/* 452 */       String baseURI = (String)this.baseURIStack.peek();
/*     */ 
/* 454 */       if (!baseURI.equals(popURI)) {
/* 455 */         entryType = Catalog.BASE;
/* 456 */         entryArgs.add(baseURI);
/*     */ 
/* 458 */         this.debug.message(4, "(reset) xml:base", baseURI);
/*     */         try
/*     */         {
/* 461 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 462 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/* 464 */           if (cex.getExceptionType() == 3)
/* 465 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 466 */           else if (cex.getExceptionType() == 2) {
/* 467 */             this.debug.message(1, "Invalid catalog entry (rbase)", localName);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 473 */     if ((namespaceURI != null) && ("urn:oasis:names:tc:entity:xmlns:xml:catalog".equals(namespaceURI)) && (!inExtension))
/*     */     {
/* 475 */       if ((localName.equals("catalog")) || (localName.equals("group"))) {
/* 476 */         String popOverride = (String)this.overrideStack.pop();
/* 477 */         String override = (String)this.overrideStack.peek();
/*     */ 
/* 479 */         if (!override.equals(popOverride)) {
/* 480 */           entryType = Catalog.OVERRIDE;
/* 481 */           entryArgs.add(override);
/* 482 */           this.overrideStack.push(override);
/*     */ 
/* 484 */           this.debug.message(4, "(reset) override", override);
/*     */           try
/*     */           {
/* 487 */             CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 488 */             this.catalog.addEntry(ce);
/*     */           } catch (CatalogException cex) {
/* 490 */             if (cex.getExceptionType() == 3)
/* 491 */               this.debug.message(1, "Invalid catalog entry type", localName);
/* 492 */             else if (cex.getExceptionType() == 2) {
/* 493 */               this.debug.message(1, "Invalid catalog entry (roverride)", localName);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 500 */     this.namespaceStack.pop();
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void skippedEntity(String name)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startPrefixMapping(String prefix, String uri)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endPrefixMapping(String prefix)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader
 * JD-Core Version:    0.6.2
 */