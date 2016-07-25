/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogEntry;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XCatalogReader extends SAXCatalogReader
/*     */   implements SAXCatalogParser
/*     */ {
/*  50 */   protected Catalog catalog = null;
/*     */ 
/*     */   public void setCatalog(Catalog catalog)
/*     */   {
/*  54 */     this.catalog = catalog;
/*     */   }
/*     */ 
/*     */   public Catalog getCatalog()
/*     */   {
/*  59 */     return this.catalog;
/*     */   }
/*     */ 
/*     */   public XCatalogReader(SAXParserFactory parserFactory)
/*     */   {
/*  64 */     super(parserFactory);
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
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
/* 105 */     int entryType = -1;
/* 106 */     Vector entryArgs = new Vector();
/*     */ 
/* 108 */     if (localName.equals("Base")) {
/* 109 */       entryType = Catalog.BASE;
/* 110 */       entryArgs.add(atts.getValue("HRef"));
/*     */ 
/* 112 */       this.catalog.getCatalogManager().debug.message(4, "Base", atts.getValue("HRef"));
/* 113 */     } else if (localName.equals("Delegate")) {
/* 114 */       entryType = Catalog.DELEGATE_PUBLIC;
/* 115 */       entryArgs.add(atts.getValue("PublicId"));
/* 116 */       entryArgs.add(atts.getValue("HRef"));
/*     */ 
/* 118 */       this.catalog.getCatalogManager().debug.message(4, "Delegate", PublicId.normalize(atts.getValue("PublicId")), atts.getValue("HRef"));
/*     */     }
/* 121 */     else if (localName.equals("Extend")) {
/* 122 */       entryType = Catalog.CATALOG;
/* 123 */       entryArgs.add(atts.getValue("HRef"));
/*     */ 
/* 125 */       this.catalog.getCatalogManager().debug.message(4, "Extend", atts.getValue("HRef"));
/* 126 */     } else if (localName.equals("Map")) {
/* 127 */       entryType = Catalog.PUBLIC;
/* 128 */       entryArgs.add(atts.getValue("PublicId"));
/* 129 */       entryArgs.add(atts.getValue("HRef"));
/*     */ 
/* 131 */       this.catalog.getCatalogManager().debug.message(4, "Map", PublicId.normalize(atts.getValue("PublicId")), atts.getValue("HRef"));
/*     */     }
/* 134 */     else if (localName.equals("Remap")) {
/* 135 */       entryType = Catalog.SYSTEM;
/* 136 */       entryArgs.add(atts.getValue("SystemId"));
/* 137 */       entryArgs.add(atts.getValue("HRef"));
/*     */ 
/* 139 */       this.catalog.getCatalogManager().debug.message(4, "Remap", atts.getValue("SystemId"), atts.getValue("HRef"));
/*     */     }
/* 142 */     else if (!localName.equals("XMLCatalog"))
/*     */     {
/* 146 */       this.catalog.getCatalogManager().debug.message(1, "Invalid catalog entry type", localName);
/*     */     }
/*     */ 
/* 149 */     if (entryType >= 0)
/*     */       try {
/* 151 */         CatalogEntry ce = new CatalogEntry(entryType, entryArgs);
/* 152 */         this.catalog.addEntry(ce);
/*     */       } catch (CatalogException cex) {
/* 154 */         if (cex.getExceptionType() == 3)
/* 155 */           this.catalog.getCatalogManager().debug.message(1, "Invalid catalog entry type", localName);
/* 156 */         else if (cex.getExceptionType() == 2)
/* 157 */           this.catalog.getCatalogManager().debug.message(1, "Invalid catalog entry", localName);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void endElement(String namespaceURI, String localName, String qName)
/*     */     throws SAXException
/*     */   {
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
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader
 * JD-Core Version:    0.6.2
 */