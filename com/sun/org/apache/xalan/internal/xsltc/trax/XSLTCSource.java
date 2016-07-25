/*     */ package com.sun.org.apache.xalan.internal.xsltc.trax;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.transform.stream.StreamSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class XSLTCSource
/*     */   implements Source
/*     */ {
/*  45 */   private String _systemId = null;
/*  46 */   private Source _source = null;
/*  47 */   private ThreadLocal _dom = new ThreadLocal();
/*     */ 
/*     */   public XSLTCSource(String systemId)
/*     */   {
/*  54 */     this._systemId = systemId;
/*     */   }
/*     */ 
/*     */   public XSLTCSource(Source source)
/*     */   {
/*  62 */     this._source = source;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/*  74 */     this._systemId = systemId;
/*  75 */     if (this._source != null)
/*  76 */       this._source.setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/*  87 */     if (this._source != null) {
/*  88 */       return this._source.getSystemId();
/*     */     }
/*     */ 
/*  91 */     return this._systemId;
/*     */   }
/*     */ 
/*     */   protected DOM getDOM(XSLTCDTMManager dtmManager, AbstractTranslet translet)
/*     */     throws SAXException
/*     */   {
/* 101 */     SAXImpl idom = (SAXImpl)this._dom.get();
/*     */ 
/* 103 */     if (idom != null) {
/* 104 */       if (dtmManager != null)
/* 105 */         idom.migrateTo(dtmManager);
/*     */     }
/*     */     else
/*     */     {
/* 109 */       Source source = this._source;
/* 110 */       if (source == null) {
/* 111 */         if ((this._systemId != null) && (this._systemId.length() > 0)) {
/* 112 */           source = new StreamSource(this._systemId);
/*     */         }
/*     */         else {
/* 115 */           ErrorMsg err = new ErrorMsg("XSLTC_SOURCE_ERR");
/* 116 */           throw new SAXException(err.toString());
/*     */         }
/*     */       }
/*     */ 
/* 120 */       DOMWSFilter wsfilter = null;
/* 121 */       if ((translet != null) && ((translet instanceof StripFilter))) {
/* 122 */         wsfilter = new DOMWSFilter(translet);
/*     */       }
/*     */ 
/* 125 */       boolean hasIdCall = translet != null ? translet.hasIdCall() : false;
/*     */ 
/* 127 */       if (dtmManager == null) {
/* 128 */         dtmManager = XSLTCDTMManager.newInstance();
/*     */       }
/*     */ 
/* 131 */       idom = (SAXImpl)dtmManager.getDTM(source, true, wsfilter, false, false, hasIdCall);
/*     */ 
/* 133 */       String systemId = getSystemId();
/* 134 */       if (systemId != null) {
/* 135 */         idom.setDocumentURI(systemId);
/*     */       }
/* 137 */       this._dom.set(idom);
/*     */     }
/* 139 */     return idom;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.trax.XSLTCSource
 * JD-Core Version:    0.6.2
 */