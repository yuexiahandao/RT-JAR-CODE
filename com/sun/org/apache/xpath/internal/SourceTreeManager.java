/*     */ package com.sun.org.apache.xpath.internal;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTM;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*     */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.URIResolver;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ public class SourceTreeManager
/*     */ {
/*  50 */   private Vector m_sourceTree = new Vector();
/*     */   URIResolver m_uriResolver;
/*     */ 
/*     */   public void reset()
/*     */   {
/*  58 */     this.m_sourceTree = new Vector();
/*     */   }
/*     */ 
/*     */   public void setURIResolver(URIResolver resolver)
/*     */   {
/*  72 */     this.m_uriResolver = resolver;
/*     */   }
/*     */ 
/*     */   public URIResolver getURIResolver()
/*     */   {
/*  83 */     return this.m_uriResolver;
/*     */   }
/*     */ 
/*     */   public String findURIFromDoc(int owner)
/*     */   {
/*  94 */     int n = this.m_sourceTree.size();
/*     */ 
/*  96 */     for (int i = 0; i < n; i++)
/*     */     {
/*  98 */       SourceTree sTree = (SourceTree)this.m_sourceTree.elementAt(i);
/*     */ 
/* 100 */       if (owner == sTree.m_root) {
/* 101 */         return sTree.m_url;
/*     */       }
/*     */     }
/* 104 */     return null;
/*     */   }
/*     */ 
/*     */   public Source resolveURI(String base, String urlString, SourceLocator locator)
/*     */     throws TransformerException, IOException
/*     */   {
/* 125 */     Source source = null;
/*     */ 
/* 127 */     if (null != this.m_uriResolver)
/*     */     {
/* 129 */       source = this.m_uriResolver.resolve(urlString, base);
/*     */     }
/*     */ 
/* 132 */     if (null == source)
/*     */     {
/* 134 */       String uri = SystemIDResolver.getAbsoluteURI(urlString, base);
/*     */ 
/* 136 */       source = new StreamSource(uri);
/*     */     }
/*     */ 
/* 139 */     return source;
/*     */   }
/*     */ 
/*     */   public void removeDocumentFromCache(int n)
/*     */   {
/* 149 */     if (-1 == n)
/* 150 */       return;
/* 151 */     for (int i = this.m_sourceTree.size() - 1; i >= 0; i--)
/*     */     {
/* 153 */       SourceTree st = (SourceTree)this.m_sourceTree.elementAt(i);
/* 154 */       if ((st != null) && (st.m_root == n))
/*     */       {
/* 156 */         this.m_sourceTree.removeElementAt(i);
/* 157 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void putDocumentInCache(int n, Source source)
/*     */   {
/* 174 */     int cachedNode = getNode(source);
/*     */ 
/* 176 */     if (-1 != cachedNode)
/*     */     {
/* 178 */       if (cachedNode != n) {
/* 179 */         throw new RuntimeException("Programmer's Error!  putDocumentInCache found reparse of doc: " + source.getSystemId());
/*     */       }
/*     */ 
/* 183 */       return;
/*     */     }
/* 185 */     if (null != source.getSystemId())
/*     */     {
/* 187 */       this.m_sourceTree.addElement(new SourceTree(n, source.getSystemId()));
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNode(Source source)
/*     */   {
/* 205 */     String url = source.getSystemId();
/*     */ 
/* 207 */     if (null == url) {
/* 208 */       return -1;
/*     */     }
/* 210 */     int n = this.m_sourceTree.size();
/*     */ 
/* 213 */     for (int i = 0; i < n; i++)
/*     */     {
/* 215 */       SourceTree sTree = (SourceTree)this.m_sourceTree.elementAt(i);
/*     */ 
/* 219 */       if (url.equals(sTree.m_url)) {
/* 220 */         return sTree.m_root;
/*     */       }
/*     */     }
/*     */ 
/* 224 */     return -1;
/*     */   }
/*     */ 
/*     */   public int getSourceTree(String base, String urlString, SourceLocator locator, XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 247 */       Source source = resolveURI(base, urlString, locator);
/*     */ 
/* 250 */       return getSourceTree(source, locator, xctxt);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 254 */       throw new TransformerException(ioe.getMessage(), locator, ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getSourceTree(Source source, SourceLocator locator, XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 278 */     int n = getNode(source);
/*     */ 
/* 280 */     if (-1 != n) {
/* 281 */       return n;
/*     */     }
/* 283 */     n = parseToNode(source, locator, xctxt);
/*     */ 
/* 285 */     if (-1 != n) {
/* 286 */       putDocumentInCache(n, source);
/*     */     }
/* 288 */     return n;
/*     */   }
/*     */ 
/*     */   public int parseToNode(Source source, SourceLocator locator, XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 308 */       Object xowner = xctxt.getOwnerObject();
/*     */       DTM dtm;
/*     */       DTM dtm;
/* 310 */       if ((null != xowner) && ((xowner instanceof DTMWSFilter)))
/*     */       {
/* 312 */         dtm = xctxt.getDTM(source, false, (DTMWSFilter)xowner, false, true);
/*     */       }
/*     */       else
/*     */       {
/* 317 */         dtm = xctxt.getDTM(source, false, null, false, true);
/*     */       }
/* 319 */       return dtm.getDocument();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 324 */       throw new TransformerException(e.getMessage(), locator, e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static XMLReader getXMLReader(Source inputSource, SourceLocator locator)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 349 */       XMLReader reader = (inputSource instanceof SAXSource) ? ((SAXSource)inputSource).getXMLReader() : null;
/*     */ 
/* 352 */       if (null == reader)
/*     */       {
/*     */         try {
/* 355 */           SAXParserFactory factory = SAXParserFactory.newInstance();
/*     */ 
/* 357 */           factory.setNamespaceAware(true);
/* 358 */           SAXParser jaxpParser = factory.newSAXParser();
/*     */ 
/* 360 */           reader = jaxpParser.getXMLReader();
/*     */         }
/*     */         catch (ParserConfigurationException ex) {
/* 363 */           throw new SAXException(ex);
/*     */         } catch (FactoryConfigurationError ex1) {
/* 365 */           throw new SAXException(ex1.toString());
/*     */         } catch (NoSuchMethodError ex2) {
/*     */         } catch (AbstractMethodError ame) {
/*     */         }
/* 369 */         if (null == reader) {
/* 370 */           reader = XMLReaderFactory.createXMLReader();
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 375 */         reader.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
/*     */       }
/*     */       catch (SAXException se)
/*     */       {
/*     */       }
/*     */ 
/* 385 */       return reader;
/*     */     }
/*     */     catch (SAXException se)
/*     */     {
/* 389 */       throw new TransformerException(se.getMessage(), locator, se);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.SourceTreeManager
 * JD-Core Version:    0.6.2
 */