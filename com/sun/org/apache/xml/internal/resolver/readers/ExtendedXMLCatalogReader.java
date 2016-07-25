/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import com.sun.org.apache.xml.internal.resolver.Resolver;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class ExtendedXMLCatalogReader extends OASISXMLCatalogReader
/*     */ {
/*     */   public static final String extendedNamespaceName = "http://nwalsh.com/xcatalog/1.0";
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */     throws SAXException
/*     */   {
/*  70 */     boolean inExtension = inExtensionNamespace();
/*     */ 
/*  72 */     super.startElement(namespaceURI, localName, qName, atts);
/*     */ 
/*  74 */     int entryType = -1;
/*  75 */     Vector entryArgs = new Vector();
/*     */ 
/*  77 */     if ((namespaceURI != null) && ("http://nwalsh.com/xcatalog/1.0".equals(namespaceURI)) && (!inExtension))
/*     */     {
/*  81 */       if (atts.getValue("xml:base") != null) {
/*  82 */         String baseURI = atts.getValue("xml:base");
/*  83 */         entryType = Catalog.BASE;
/*  84 */         entryArgs.add(baseURI);
/*  85 */         this.baseURIStack.push(baseURI);
/*     */ 
/*  87 */         this.debug.message(4, "xml:base", baseURI);
/*     */         try
/*     */         {
/*  90 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/*  91 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/*  93 */           if (cex.getExceptionType() == 3)
/*  94 */             this.debug.message(1, "Invalid catalog entry type", localName);
/*  95 */           else if (cex.getExceptionType() == 2) {
/*  96 */             this.debug.message(1, "Invalid catalog entry (base)", localName);
/*     */           }
/*     */         }
/*     */ 
/* 100 */         entryType = -1;
/* 101 */         entryArgs = new Vector();
/*     */       } else {
/* 103 */         this.baseURIStack.push(this.baseURIStack.peek());
/*     */       }
/*     */ 
/* 106 */       if (localName.equals("uriSuffix")) {
/* 107 */         if (checkAttributes(atts, "suffix", "uri")) {
/* 108 */           entryType = Resolver.URISUFFIX;
/* 109 */           entryArgs.add(atts.getValue("suffix"));
/* 110 */           entryArgs.add(atts.getValue("uri"));
/*     */ 
/* 112 */           this.debug.message(4, "uriSuffix", atts.getValue("suffix"), atts.getValue("uri"));
/*     */         }
/*     */ 
/*     */       }
/* 116 */       else if (localName.equals("systemSuffix")) {
/* 117 */         if (checkAttributes(atts, "suffix", "uri")) {
/* 118 */           entryType = Resolver.SYSTEMSUFFIX;
/* 119 */           entryArgs.add(atts.getValue("suffix"));
/* 120 */           entryArgs.add(atts.getValue("uri"));
/*     */ 
/* 122 */           this.debug.message(4, "systemSuffix", atts.getValue("suffix"), atts.getValue("uri"));
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 128 */         this.debug.message(1, "Invalid catalog entry type", localName);
/*     */       }
/*     */ 
/* 131 */       if (entryType >= 0)
/*     */         try {
/* 133 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 134 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/* 136 */           if (cex.getExceptionType() == 3)
/* 137 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 138 */           else if (cex.getExceptionType() == 2)
/* 139 */             this.debug.message(1, "Invalid catalog entry", localName);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
/* 152 */     super.endElement(namespaceURI, localName, qName);
/*     */ 
/* 156 */     boolean inExtension = inExtensionNamespace();
/*     */ 
/* 158 */     int entryType = -1;
/* 159 */     Vector entryArgs = new Vector();
/*     */ 
/* 161 */     if ((namespaceURI != null) && ("http://nwalsh.com/xcatalog/1.0".equals(namespaceURI)) && (!inExtension))
/*     */     {
/* 165 */       String popURI = (String)this.baseURIStack.pop();
/* 166 */       String baseURI = (String)this.baseURIStack.peek();
/*     */ 
/* 168 */       if (!baseURI.equals(popURI)) {
/* 169 */         entryType = Catalog.BASE;
/* 170 */         entryArgs.add(baseURI);
/*     */ 
/* 172 */         this.debug.message(4, "(reset) xml:base", baseURI);
/*     */         try
/*     */         {
/* 175 */           CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 176 */           this.catalog.addEntry(ce);
/*     */         } catch (CatalogException cex) {
/* 178 */           if (cex.getExceptionType() == 3)
/* 179 */             this.debug.message(1, "Invalid catalog entry type", localName);
/* 180 */           else if (cex.getExceptionType() == 2)
/* 181 */             this.debug.message(1, "Invalid catalog entry (rbase)", localName);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader
 * JD-Core Version:    0.6.2
 */