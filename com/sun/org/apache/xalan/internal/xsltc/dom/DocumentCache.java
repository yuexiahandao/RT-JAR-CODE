/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMCache;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOMEnhancedForDTM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.Translet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
/*     */ import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
/*     */ import java.io.File;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLDecoder;
/*     */ import java.util.Date;
/*     */ import java.util.Hashtable;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.sax.SAXSource;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public final class DocumentCache
/*     */   implements DOMCache
/*     */ {
/*     */   private int _size;
/*     */   private Hashtable _references;
/*     */   private String[] _URIs;
/*     */   private int _count;
/*     */   private int _current;
/*     */   private SAXParser _parser;
/*     */   private XMLReader _reader;
/*     */   private XSLTCDTMManager _dtmManager;
/*     */   private static final int REFRESH_INTERVAL = 1000;
/*     */ 
/*     */   public DocumentCache(int size)
/*     */     throws SAXException
/*     */   {
/* 157 */     this(size, null);
/*     */     try {
/* 159 */       this._dtmManager = ((XSLTCDTMManager)XSLTCDTMManager.getDTMManagerClass().newInstance());
/*     */     }
/*     */     catch (Exception e) {
/* 162 */       throw new SAXException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public DocumentCache(int size, XSLTCDTMManager dtmManager)
/*     */     throws SAXException
/*     */   {
/* 170 */     this._dtmManager = dtmManager;
/* 171 */     this._count = 0;
/* 172 */     this._current = 0;
/* 173 */     this._size = size;
/* 174 */     this._references = new Hashtable(this._size + 2);
/* 175 */     this._URIs = new String[this._size];
/*     */     try
/*     */     {
/* 179 */       SAXParserFactory factory = SAXParserFactory.newInstance();
/*     */       try {
/* 181 */         factory.setFeature("http://xml.org/sax/features/namespaces", true);
/*     */       }
/*     */       catch (Exception e) {
/* 184 */         factory.setNamespaceAware(true);
/*     */       }
/* 186 */       this._parser = factory.newSAXParser();
/* 187 */       this._reader = this._parser.getXMLReader();
/*     */     }
/*     */     catch (ParserConfigurationException e) {
/* 190 */       BasisLibrary.runTimeError("NAMESPACES_SUPPORT_ERR");
/*     */     }
/*     */   }
/*     */ 
/*     */   private final long getLastModified(String uri)
/*     */   {
/*     */     try
/*     */     {
/* 199 */       URL url = new URL(uri);
/* 200 */       URLConnection connection = url.openConnection();
/* 201 */       long timestamp = connection.getLastModified();
/*     */       File localfile;
/* 203 */       if ((timestamp == 0L) && 
/* 204 */         ("file".equals(url.getProtocol())))
/* 205 */         localfile = new File(URLDecoder.decode(url.getFile()));
/* 206 */       return localfile.lastModified();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */     }
/*     */ 
/* 213 */     return System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   private CachedDocument lookupDocument(String uri)
/*     */   {
/* 221 */     return (CachedDocument)this._references.get(uri);
/*     */   }
/*     */ 
/*     */   private synchronized void insertDocument(String uri, CachedDocument doc)
/*     */   {
/* 228 */     if (this._count < this._size)
/*     */     {
/* 230 */       this._URIs[(this._count++)] = uri;
/* 231 */       this._current = 0;
/*     */     }
/*     */     else
/*     */     {
/* 235 */       this._references.remove(this._URIs[this._current]);
/*     */ 
/* 237 */       this._URIs[this._current] = uri;
/* 238 */       if (++this._current >= this._size) this._current = 0;
/*     */     }
/* 240 */     this._references.put(uri, doc);
/*     */   }
/*     */ 
/*     */   private synchronized void replaceDocument(String uri, CachedDocument doc)
/*     */   {
/* 247 */     CachedDocument old = (CachedDocument)this._references.get(uri);
/* 248 */     if (doc == null)
/* 249 */       insertDocument(uri, doc);
/*     */     else
/* 251 */       this._references.put(uri, doc);
/*     */   }
/*     */ 
/*     */   public DOM retrieveDocument(String baseURI, String href, Translet trs)
/*     */   {
/* 261 */     String uri = href;
/* 262 */     if ((baseURI != null) && (!baseURI.equals("")))
/*     */       try {
/* 264 */         uri = SystemIDResolver.getAbsoluteURI(uri, baseURI);
/*     */       }
/*     */       catch (TransformerException te)
/*     */       {
/*     */       }
/*     */     CachedDocument doc;
/* 271 */     if ((doc = lookupDocument(uri)) == null) {
/* 272 */       doc = new CachedDocument(uri);
/* 273 */       if (doc == null) return null;
/* 274 */       doc.setLastModified(getLastModified(uri));
/* 275 */       insertDocument(uri, doc);
/*     */     }
/*     */     else
/*     */     {
/* 279 */       long now = System.currentTimeMillis();
/* 280 */       long chk = doc.getLastChecked();
/* 281 */       doc.setLastChecked(now);
/*     */ 
/* 283 */       if (now > chk + 1000L) {
/* 284 */         doc.setLastChecked(now);
/* 285 */         long last = getLastModified(uri);
/*     */ 
/* 287 */         if (last > doc.getLastModified()) {
/* 288 */           doc = new CachedDocument(uri);
/* 289 */           if (doc == null) return null;
/* 290 */           doc.setLastModified(getLastModified(uri));
/* 291 */           replaceDocument(uri, doc);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 298 */     DOM dom = doc.getDocument();
/*     */ 
/* 302 */     if (dom == null) return null;
/*     */ 
/* 304 */     doc.incAccessCount();
/*     */ 
/* 306 */     AbstractTranslet translet = (AbstractTranslet)trs;
/*     */ 
/* 310 */     translet.prepassDocument(dom);
/*     */ 
/* 312 */     return doc.getDocument();
/*     */   }
/*     */ 
/*     */   public void getStatistics(PrintWriter out)
/*     */   {
/* 319 */     out.println("<h2>DOM cache statistics</h2><center><table border=\"2\"><tr><td><b>Document URI</b></td><td><center><b>Build time</b></center></td><td><center><b>Access count</b></center></td><td><center><b>Last accessed</b></center></td><td><center><b>Last modified</b></center></td></tr>");
/*     */ 
/* 326 */     for (int i = 0; i < this._count; i++) {
/* 327 */       CachedDocument doc = (CachedDocument)this._references.get(this._URIs[i]);
/* 328 */       out.print("<tr><td><a href=\"" + this._URIs[i] + "\">" + "<font size=-1>" + this._URIs[i] + "</font></a></td>");
/*     */ 
/* 330 */       out.print("<td><center>" + doc.getLatency() + "ms</center></td>");
/* 331 */       out.print("<td><center>" + doc.getAccessCount() + "</center></td>");
/* 332 */       out.print("<td><center>" + new Date(doc.getLastReferenced()) + "</center></td>");
/*     */ 
/* 334 */       out.print("<td><center>" + new Date(doc.getLastModified()) + "</center></td>");
/*     */ 
/* 336 */       out.println("</tr>");
/*     */     }
/*     */ 
/* 339 */     out.println("</table></center>");
/*     */   }
/*     */ 
/*     */   public final class CachedDocument
/*     */   {
/*     */     private long _firstReferenced;
/*     */     private long _lastReferenced;
/*     */     private long _accessCount;
/*     */     private long _lastModified;
/*     */     private long _lastChecked;
/*     */     private long _buildTime;
/*  83 */     private DOMEnhancedForDTM _dom = null;
/*     */ 
/*     */     public CachedDocument(String uri)
/*     */     {
/*  90 */       long stamp = System.currentTimeMillis();
/*  91 */       this._firstReferenced = stamp;
/*  92 */       this._lastReferenced = stamp;
/*  93 */       this._accessCount = 0L;
/*  94 */       loadDocument(uri);
/*     */ 
/*  96 */       this._buildTime = (System.currentTimeMillis() - stamp);
/*     */     }
/*     */ 
/*     */     public void loadDocument(String uri)
/*     */     {
/*     */       try
/*     */       {
/* 105 */         long stamp = System.currentTimeMillis();
/* 106 */         this._dom = ((DOMEnhancedForDTM)DocumentCache.this._dtmManager.getDTM(new SAXSource(DocumentCache.this._reader, new InputSource(uri)), false, null, true, false));
/*     */ 
/* 109 */         this._dom.setDocumentURI(uri);
/*     */ 
/* 113 */         long thisTime = System.currentTimeMillis() - stamp;
/* 114 */         if (this._buildTime > 0L)
/* 115 */           this._buildTime = (this._buildTime + thisTime >>> 1);
/*     */         else
/* 117 */           this._buildTime = thisTime;
/*     */       }
/*     */       catch (Exception e) {
/* 120 */         this._dom = null;
/*     */       }
/*     */     }
/*     */ 
/* 124 */     public DOM getDocument() { return this._dom; } 
/*     */     public long getFirstReferenced() {
/* 126 */       return this._firstReferenced;
/*     */     }
/* 128 */     public long getLastReferenced() { return this._lastReferenced; } 
/*     */     public long getAccessCount() {
/* 130 */       return this._accessCount;
/*     */     }
/* 132 */     public void incAccessCount() { this._accessCount += 1L; } 
/*     */     public long getLastModified() {
/* 134 */       return this._lastModified;
/*     */     }
/* 136 */     public void setLastModified(long t) { this._lastModified = t; } 
/*     */     public long getLatency() {
/* 138 */       return this._buildTime;
/*     */     }
/* 140 */     public long getLastChecked() { return this._lastChecked; } 
/*     */     public void setLastChecked(long t) {
/* 142 */       this._lastChecked = t;
/*     */     }
/*     */     public long getEstimatedSize() {
/* 145 */       if (this._dom != null) {
/* 146 */         return this._dom.getSize() << 5;
/*     */       }
/* 148 */       return 0L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.DocumentCache
 * JD-Core Version:    0.6.2
 */