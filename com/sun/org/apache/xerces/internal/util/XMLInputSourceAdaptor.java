/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import javax.xml.transform.Source;
/*     */ 
/*     */ public final class XMLInputSourceAdaptor
/*     */   implements Source
/*     */ {
/*     */   public final XMLInputSource fSource;
/*     */ 
/*     */   public XMLInputSourceAdaptor(XMLInputSource core)
/*     */   {
/*  92 */     this.fSource = core;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId) {
/*  96 */     this.fSource.setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public String getSystemId() {
/*     */     try {
/* 101 */       return XMLEntityManager.expandSystemId(this.fSource.getSystemId(), this.fSource.getBaseSystemId(), false);
/*     */     } catch (URI.MalformedURIException e) {
/*     */     }
/* 104 */     return this.fSource.getSystemId();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLInputSourceAdaptor
 * JD-Core Version:    0.6.2
 */