/*     */ package com.sun.org.apache.xml.internal.resolver.readers;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.resolver.Catalog;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogException;
/*     */ import com.sun.org.apache.xml.internal.resolver.CatalogManager;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Namespaces;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.Hashtable;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class DOMCatalogReader
/*     */   implements CatalogReader
/*     */ {
/*  80 */   protected Hashtable namespaceMap = new Hashtable();
/*     */ 
/*     */   public void setCatalogParser(String namespaceURI, String rootElement, String parserClass)
/*     */   {
/*  96 */     if (namespaceURI == null)
/*  97 */       this.namespaceMap.put(rootElement, parserClass);
/*     */     else
/*  99 */       this.namespaceMap.put("{" + namespaceURI + "}" + rootElement, parserClass);
/*     */   }
/*     */ 
/*     */   public String getCatalogParser(String namespaceURI, String rootElement)
/*     */   {
/* 115 */     if (namespaceURI == null) {
/* 116 */       return (String)this.namespaceMap.get(rootElement);
/*     */     }
/* 118 */     return (String)this.namespaceMap.get("{" + namespaceURI + "}" + rootElement);
/*     */   }
/*     */ 
/*     */   public void readCatalog(Catalog catalog, InputStream is)
/*     */     throws IOException, CatalogException
/*     */   {
/* 156 */     DocumentBuilderFactory factory = null;
/* 157 */     DocumentBuilder builder = null;
/*     */ 
/* 159 */     factory = DocumentBuilderFactory.newInstance();
/* 160 */     factory.setNamespaceAware(false);
/* 161 */     factory.setValidating(false);
/*     */     try {
/* 163 */       builder = factory.newDocumentBuilder();
/*     */     } catch (ParserConfigurationException pce) {
/* 165 */       throw new CatalogException(6);
/*     */     }
/*     */ 
/* 168 */     Document doc = null;
/*     */     try
/*     */     {
/* 171 */       doc = builder.parse(is);
/*     */     } catch (SAXException se) {
/* 173 */       throw new CatalogException(5);
/*     */     }
/*     */ 
/* 176 */     Element root = doc.getDocumentElement();
/*     */ 
/* 178 */     String namespaceURI = Namespaces.getNamespaceURI(root);
/* 179 */     String localName = Namespaces.getLocalName(root);
/*     */ 
/* 181 */     String domParserClass = getCatalogParser(namespaceURI, localName);
/*     */ 
/* 184 */     if (domParserClass == null) {
/* 185 */       if (namespaceURI == null) {
/* 186 */         catalog.getCatalogManager().debug.message(1, "No Catalog parser for " + localName);
/*     */       }
/*     */       else {
/* 189 */         catalog.getCatalogManager().debug.message(1, "No Catalog parser for {" + namespaceURI + "}" + localName);
/*     */       }
/*     */ 
/* 193 */       return;
/*     */     }
/*     */ 
/* 196 */     DOMCatalogParser domParser = null;
/*     */     try
/*     */     {
/* 199 */       domParser = (DOMCatalogParser)ReflectUtil.forName(domParserClass).newInstance();
/*     */     } catch (ClassNotFoundException cnfe) {
/* 201 */       catalog.getCatalogManager().debug.message(1, "Cannot load XML Catalog Parser class", domParserClass);
/* 202 */       throw new CatalogException(6);
/*     */     } catch (InstantiationException ie) {
/* 204 */       catalog.getCatalogManager().debug.message(1, "Cannot instantiate XML Catalog Parser class", domParserClass);
/* 205 */       throw new CatalogException(6);
/*     */     } catch (IllegalAccessException iae) {
/* 207 */       catalog.getCatalogManager().debug.message(1, "Cannot access XML Catalog Parser class", domParserClass);
/* 208 */       throw new CatalogException(6);
/*     */     } catch (ClassCastException cce) {
/* 210 */       catalog.getCatalogManager().debug.message(1, "Cannot cast XML Catalog Parser class", domParserClass);
/* 211 */       throw new CatalogException(6);
/*     */     }
/*     */ 
/* 214 */     Node node = root.getFirstChild();
/* 215 */     while (node != null) {
/* 216 */       domParser.parseCatalogEntry(catalog, node);
/* 217 */       node = node.getNextSibling();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readCatalog(Catalog catalog, String fileUrl)
/*     */     throws MalformedURLException, IOException, CatalogException
/*     */   {
/* 239 */     URL url = new URL(fileUrl);
/* 240 */     URLConnection urlCon = url.openConnection();
/* 241 */     readCatalog(catalog, urlCon.getInputStream());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.readers.DOMCatalogReader
 * JD-Core Version:    0.6.2
 */